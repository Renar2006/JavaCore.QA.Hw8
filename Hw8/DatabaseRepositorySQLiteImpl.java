package Hw8;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseRepositorySQLiteImpl implements DatabaseRepository {

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    String filename = null;
    String createTableQuery = "CREATE TABLE IF NOT EXISTS weather (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "city TEXT NOT NULL, date_time TEXT NOT NULL, weather_dayText TEXT NOT NULL, weather_nightText" +
            " TEXT NOT NULL, minTemperature REAL NOT NULL, maxTemperature REAL NOT NULL);";
    String insertWeatherQuery = "INSERT INTO weather (city, date_time, weather_dayText, weather_nightText, minTemperature, maxTemperature) VALUES (?,?,?,?,?,?)";

    public DatabaseRepositorySQLiteImpl() {
        filename = ApplicationGlobalState.getInstance().getDbFileName();
    }

    public Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + filename);
        return connection;
    }

    public Statement getStatement() throws SQLException {
        Statement statement = getConnection().createStatement();
        return statement;
    }
    public PreparedStatement getPreparedStatement() throws SQLException{
        PreparedStatement preparedStatement = getConnection().prepareStatement(insertWeatherQuery);
        return preparedStatement;
    }

    public void createTableIfNotExists() {
        try (Statement statement = getStatement()) {
            statement.executeUpdate(createTableQuery);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public boolean saveWeatherData(WeatherData weatherData) throws SQLException {
        try (PreparedStatement saveWeather = getPreparedStatement()) {
            try (saveWeather) {
                saveWeather.setString(1, weatherData.getCity());
                saveWeather.setString(2, weatherData.getLocalDate());
                saveWeather.setString(3, weatherData.getDayText());
                saveWeather.setString(4, weatherData.getNightText());
                saveWeather.setDouble(5, weatherData.getMinTemperature());
                saveWeather.setDouble(6, weatherData.getMaxTemperature());

                return saveWeather.execute();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        throw new SQLException("Failure on saving weather object");
    }

    @Override
    public List<WeatherData> getAllSavedData() throws SQLException {
        ResultSet resultSet = getConnection().createStatement().executeQuery("SELECT * FROM weather");
        List<WeatherData> weatherDataList = new ArrayList<WeatherData>();
        while (resultSet.next()) {
            weatherDataList.add(new WeatherData(resultSet.getString(2), resultSet.getString(3),  resultSet.getString(4),
                    resultSet.getString(5), resultSet.getDouble(6), resultSet.getDouble(7)));
        }
        return weatherDataList;
    }

    public void closeConnection (){
        try {
            getConnection().close();
            getStatement().close();
            getPreparedStatement().close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
