package Hw8;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AccuWeatherProvider implements WeatherProvider {

    private static final String BASE_HOST = "dataservice.accuweather.com";
    private static final String CURRENT_CONDITIONS_ENDPOINT = "currentconditions";
    private static final String API_VERSION = "v1";
    private static final String API_KEY = ApplicationGlobalState.getInstance().getApiKey();
    private static final String FORECAST_PERIOD = "5day";
    private static final String FORECAST_TYPE = "daily";
    private static final String FORECAST = "forecasts";

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private DatabaseRepositorySQLiteImpl databaseRepositorySQLite = new DatabaseRepositorySQLiteImpl();

    @Override
    public void getWeather(Periods periods) throws IOException, SQLException {

        if (periods.equals(Periods.NOW)) {
            String cityKey = detectCityKey();
            HttpUrl url = new HttpUrl.Builder()
                    .scheme("http")
                    .host(BASE_HOST)
                    .addPathSegment(CURRENT_CONDITIONS_ENDPOINT)
                    .addPathSegment(API_VERSION)
                    .addPathSegment(cityKey)
                    .addQueryParameter("apikey", API_KEY)
                    .addQueryParameter("language", "ru-ru")
                    .addQueryParameter("metric", "true")
                    .build();

            Request request = new Request.Builder()
                    .addHeader("accept", "application/json")
                    .url(url)
                    .build();

            String responseList = client.newCall(request).execute().body().string();

            ObjectMapper responseMapper = new ObjectMapper();
            List<WeatherResponse> weatherResponseList = responseMapper.readValue(responseList, new TypeReference<List<WeatherResponse>>() {});

            String [] arrayResponse = new String[1];

            for (int i = 0; i < 1 ; i++) {
                ObjectMapper stringMapper = new ObjectMapper();
                arrayResponse[i] = stringMapper.writeValueAsString(weatherResponseList.get(i));
            }

            for (String string : arrayResponse) {
                JsonNode weatherResponse = responseMapper.readTree(string).at("/WeatherText");
                JsonNode TemperatureResponse = responseMapper.readTree(string).at("/Temperature/Metric/Value");

                System.out.println("Сейчас в городе " + ApplicationGlobalState.getInstance().getSelectedCity() +
                        " температура "  + TemperatureResponse.asText() + "°С, и " + trimBrackets(weatherResponse).toLowerCase()+ ".");
            }
        }

        if (periods.equals(Periods.FIVE_DAYS)) {
            String cityKey = detectCityKey();
            HttpUrl url = new HttpUrl.Builder()
                    .scheme("http")
                    .host(BASE_HOST)
                    .addPathSegment(FORECAST)
                    .addPathSegment(API_VERSION)
                    .addPathSegment(FORECAST_TYPE)
                    .addPathSegment(FORECAST_PERIOD)
                    .addPathSegment(cityKey)
                    .addQueryParameter("apikey", API_KEY)
                    .addQueryParameter("language", "ru-ru")
                    .addQueryParameter("metric", "true")
                    .build();

            Request request = new Request.Builder()
                    .addHeader("accept", "application/json")
                    .url(url)
                    .build();

            String responseList = client.newCall(request).execute().body().string();

            System.out.println(responseList);

            int firstIndexBody = responseList.indexOf("[{\"Date\"");
            int lastIndexBody = responseList.lastIndexOf("}");
            responseList = responseList.substring(firstIndexBody, lastIndexBody);

            ObjectMapper responseMapper = new ObjectMapper();
            List<WeatherResponse> weatherResponseList = responseMapper.readValue(responseList, new TypeReference<List<WeatherResponse>>() {});

            String [] arrayResponse5days = new String[5];

            for (int i = 0; i < 5 ; i++) {
                ObjectMapper stringMapper = new ObjectMapper();
                arrayResponse5days[i] = stringMapper.writeValueAsString(weatherResponseList.get(i));
            }

            for (String string : arrayResponse5days) {
                JsonNode dateResponse = responseMapper.readTree(string).at("/Date");
                JsonNode maxTemperatureResponse = responseMapper.readTree(string).at("/Temperature/Maximum/Value");
                JsonNode minTemperatureResponse = responseMapper.readTree(string).at("/Temperature/Minimum/Value");
                JsonNode dayResponse = responseMapper.readTree(string).at("/Day/IconPhrase");
                JsonNode nightResponse = responseMapper.readTree(string).at("/Night/IconPhrase");

                System.out.println("В городе " + ApplicationGlobalState.getInstance().getSelectedCity() + " на следующую дату " + dateResponse.asText().substring(0,10) +
                        " ожидается такая погода: Минимальная температура "  + minTemperatureResponse.asText() + "°С. Максимальная температура " +
                        maxTemperatureResponse.asText() + "°С. Днём - " + trimBrackets(dayResponse).toLowerCase() + ". Ночью - " + trimBrackets(nightResponse).toLowerCase() + ".");


                WeatherData weatherData = new WeatherData(ApplicationGlobalState.getInstance().getSelectedCity(),
                        dateResponse.asText().substring(0,10), trimBrackets(dayResponse).toLowerCase(), trimBrackets(nightResponse).toLowerCase(),
                        minTemperatureResponse.asDouble(), maxTemperatureResponse.asDouble());

                databaseRepositorySQLite.createTableIfNotExists();
                databaseRepositorySQLite.saveWeatherData(weatherData);
            }
        }

        if (periods.equals(Periods.BASE)) {
            getAllFromDb();
        }

        if (periods.equals(Periods.ZERO)) {
            databaseRepositorySQLite.closeConnection();
        }
    }

    @Override
    public List<WeatherData> getAllFromDb() throws SQLException {
        List<WeatherData> weatherDataList = databaseRepositorySQLite.getAllSavedData();
        for (WeatherData weatherData : weatherDataList) {
            System.out.println(weatherData);
        }
        return weatherDataList;
    }

    public String detectCityKey() throws IOException {
        String selectedCity = ApplicationGlobalState.getInstance().getSelectedCity();

        HttpUrl detectLocationURL = new HttpUrl.Builder()
                .scheme("http")
                .host(BASE_HOST)
                .addPathSegment("locations")
                .addPathSegment(API_VERSION)
                .addPathSegment("cities")
                .addPathSegment("autocomplete")
                .addQueryParameter("apikey", API_KEY)
                .addQueryParameter("q", selectedCity)
                .build();

        Request request = new Request.Builder()
                .addHeader("accept", "application/json")
                .url(detectLocationURL)
                .build();

        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) {
            throw new IOException("Невозможно прочесть информацию о городе. " +
                    "Код ответа сервера = " + response.code() + " тело ответа = " + response.body().string());
        }
        String jsonResponse = response.body().string();
        System.out.println("Произвожу поиск города " + selectedCity);

        if (objectMapper.readTree(jsonResponse).size() > 0) {
            String cityName = objectMapper.readTree(jsonResponse).get(0).at("/LocalizedName").asText();
            String countryName = objectMapper.readTree(jsonResponse).get(0).at("/Country/LocalizedName").asText();
            System.out.println("Найден город " + cityName + " в стране " + countryName);
        } else throw new IOException("Server returns 0 cities");

        return objectMapper.readTree(jsonResponse).get(0).at("/Key").asText();
    }

    public String trimBrackets(JsonNode string){
        int lastIndexBody = string.toString().lastIndexOf("\"");
        String newString = string.toString().substring(1, lastIndexBody);
        return newString;
    }
}
