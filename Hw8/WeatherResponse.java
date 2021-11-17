package Hw8;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse {

    @Override
    public String toString() {
        return "WeatherResponse{" +
                ", WeatherText='" + WeatherText + '\'' +
                ", Date='" + Date + '\'' +
                ", Temperature=" + Temperature +
                ", Day=" + Day +
                ", Night=" + Night +
                '}';
    }

    @JsonProperty(value = "WeatherText")
    private String WeatherText;

    @JsonIgnore
    private String weatherText;

    @JsonProperty(value = "Date")
    private String Date;

    @JsonIgnore
    private String date;

    @JsonProperty(value = "Temperature")
    private Temperature Temperature;

    @JsonIgnore
    private Temperature temperature;

    @JsonProperty(value = "Day")
    private Day Day;

    @JsonIgnore
    private Day day;

    @JsonProperty(value = "Night")
    private Night Night;

    @JsonIgnore
    private Night night;
}

class Night {
    @Override
    public String toString() {
        return "Night{" +
                "Icon=" + Icon +
                ", IconPhrase='" + IconPhrase + '\'' +
                ", HasPrecipitation=" + HasPrecipitation +
                ", PrecipitationType='" + PrecipitationType + '\'' +
                ", PrecipitationIntensity='" + PrecipitationIntensity + '\'' +
                '}';
    }

    @JsonProperty(value = "Icon")
    private float Icon;

    @JsonIgnore
    private float icon;

    @JsonProperty(value = "IconPhrase")
    private String IconPhrase;

    @JsonIgnore
    private String iconPhrase;

    @JsonProperty(value = "HasPrecipitation")
    private boolean HasPrecipitation;

    @JsonIgnore
    private boolean hasPrecipitation;

    @JsonProperty(value = "PrecipitationType")
    private String PrecipitationType;

    @JsonIgnore
    private String precipitationType;

    @JsonProperty(value = "PrecipitationIntensity")
    private String PrecipitationIntensity;

    @JsonIgnore
    private String precipitationIntensity;
}

class Day {
    @Override
    public String toString() {
        return "Day{" +
                "Icon=" + Icon +
                ", IconPhrase='" + IconPhrase + '\'' +
                ", HasPrecipitation=" + HasPrecipitation +
                ", PrecipitationType='" + PrecipitationType + '\'' +
                ", PrecipitationIntensity='" + PrecipitationIntensity + '\'' +
                '}';
    }

    @JsonProperty(value = "Icon")
    private float Icon;

    @JsonIgnore
    private float icon;

    @JsonProperty(value = "IconPhrase")
    private String IconPhrase;

    @JsonIgnore
    private String iconPhrase;

    @JsonProperty(value = "HasPrecipitation")
    private boolean HasPrecipitation;

    @JsonIgnore
    private boolean hasPrecipitation;

    @JsonProperty(value = "PrecipitationType")
    private String PrecipitationType;

    @JsonIgnore
    private String precipitationType;

    @JsonProperty(value = "PrecipitationIntensity")
    private String PrecipitationIntensity;

    @JsonIgnore
    private String precipitationIntensity;
}

class Temperature {
    @Override
    public String toString() {
        return "Temperature{" +
                "Metric=" + Metric +
                ", Minimum=" + Minimum +
                ", Maximum=" + Maximum +
                ", Imperial=" + Imperial +
                '}';
    }


    @JsonProperty(value = "Metric")
    private Metric Metric;

    @JsonIgnore
    private Metric metric;

    @JsonProperty(value = "Minimum")
    private Minimum Minimum;

    @JsonIgnore
    private Minimum minimum;

    @JsonProperty(value = "Maximum")
    private Maximum Maximum;

    @JsonIgnore
    private Maximum maximum;

    @JsonProperty(value = "Imperial")
    private Imperial Imperial;

    @JsonIgnore
    private Imperial imperial;
}

class Imperial{
    @Override
    public String toString() {
        return "Imperial{" +
                "Unit='" + Unit + '\'' +
                ", UnitType=" + UnitType +
                ", Value=" + Value +
                '}';
    }

    @JsonProperty(value = "Unit")
    private String Unit;

    @JsonIgnore
    private String unit;

    @JsonProperty(value = "UnitType")
    private float UnitType;

    @JsonIgnore
    private float unitType;

    @JsonProperty(value = "Value")
    private float Value;

    @JsonIgnore
    private float value;
}

class Metric{
    @Override
    public String toString() {
        return "Metric{" +
                "Unit='" + Unit + '\'' +
                ", UnitType=" + UnitType +
                ", Value=" + Value +
                '}';
    }

    @JsonProperty(value = "Unit")
    private String Unit;

    @JsonIgnore
    private String unit;

    @JsonProperty(value = "UnitType")
    private float UnitType;

    @JsonIgnore
    private float unitType;

    @JsonProperty(value = "Value")
    private float Value;

    @JsonIgnore
    private float value;
}


class Maximum {
    @Override
    public String toString() {
        return "Maximum{" +
                "Unit='" + Unit + '\'' +
                ", UnitType=" + UnitType +
                ", Value=" + Value +
                '}';
    }

    @JsonProperty(value = "Unit")
    private String Unit;

    @JsonIgnore
    private String unit;

    @JsonProperty(value = "UnitType")
    private float UnitType;

    @JsonIgnore
    private float unitType;

    @JsonProperty(value = "Value")
    private float Value;

    @JsonIgnore
    private float value;
}

class Minimum {
    @Override
    public String toString() {
        return "Minimum{" +
                "Unit='" + Unit + '\'' +
                ", UnitType=" + UnitType +
                ", Value=" + Value +
                '}';
    }

    @JsonProperty(value = "Unit")
    private String Unit;

    @JsonIgnore
    private String unit;

    @JsonProperty(value = "UnitType")
    private float UnitType;

    @JsonIgnore
    private float unitType;

    @JsonProperty(value = "Value")
    private float Value;

    @JsonIgnore
    private float value;
}
