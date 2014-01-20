package com.example.beeproject.weather.classes;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Class representing weather information object.
 * Weather information is retrieved from weather api server several times a day.
 * This information is stored in the database for future datamining.
 * <p>Weather information is related to a location - yard location.
 * When weather retrieval task runs, weather for all distinct locations of all yards 
 * is retrieved and stored.
 * <p>See com.example.beeproject.weather.WeatherHelper
 * <p>Objects of this class can be persisted to a database using ORMLite
 * @author rezolya
 *
 */
@DatabaseTable(tableName = "weatherinfo")
public class WeatherInfoObject {

	@DatabaseField(generatedId = true)
    private int id; //server side id

    @DatabaseField(canBeNull = false)
    private String location; //must be the same location as in yard

    @DatabaseField(canBeNull = false)
    private long timestamp; //timestamp of retrieval time
    
    /*
     * fields received from weather server api:
     */

    @DatabaseField(canBeNull = true)
	private long location_sunset;
    
    @DatabaseField(canBeNull = true)
    private long location_sunrise;

    @DatabaseField(canBeNull = true)
    private String location_country;
    
    @DatabaseField(canBeNull = true)
    private String location_city;
    
    
    @DatabaseField(canBeNull = true)
    private int condition_weatherId;
    
    @DatabaseField(canBeNull = true)
    private String condition_condition;
    
    @DatabaseField(canBeNull = true)
    private String condition_descr;
    
    @DatabaseField(canBeNull = true)
    private float condition_pressure;
    
    @DatabaseField(canBeNull = true)
    private float condition_humidity;

    
    @DatabaseField(canBeNull = true)
    private float temperature_temp;
    
    @DatabaseField(canBeNull = true)
    private float temperature_minTemp;
    
    @DatabaseField(canBeNull = true)
    private float temperature_maxTemp;

    
    @DatabaseField(canBeNull = true)
    private float wind_speed;
    
    @DatabaseField(canBeNull = true)
    private float wind_deg;

    
    @DatabaseField(canBeNull = true)
    private String rain_time;
    
    @DatabaseField(canBeNull = true)
    private float rain_ammount;

    
    @DatabaseField(canBeNull = true)
    private String snow_time;
    
    @DatabaseField(canBeNull = true)
    private float snow_ammount;

    
    @DatabaseField(canBeNull = true)
    private int clouds_perc;
    
    
    public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

    public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	public long getLocation_sunset() {
		return location_sunset;
	}

	public void setLocation_sunset(long location_sunset) {
		this.location_sunset = location_sunset;
	}

	public long getLocation_sunrise() {
		return location_sunrise;
	}

	public void setLocation_sunrise(long location_sunrise) {
		this.location_sunrise = location_sunrise;
	}

	public String getLocation_country() {
		return location_country;
	}

	public void setLocation_country(String location_country) {
		this.location_country = location_country;
	}

	public String getLocation_city() {
		return location_city;
	}

	public void setLocation_city(String location_city) {
		this.location_city = location_city;
	}

	public int getCondition_weatherId() {
		return condition_weatherId;
	}

	public void setCondition_weatherId(int condition_weatherId) {
		this.condition_weatherId = condition_weatherId;
	}

	public String getCondition_condition() {
		return condition_condition;
	}

	public void setCondition_condition(String condition_condition) {
		this.condition_condition = condition_condition;
	}

	public String getCondition_descr() {
		return condition_descr;
	}

	public void setCondition_descr(String condition_descr) {
		this.condition_descr = condition_descr;
	}

	public float getCondition_pressure() {
		return condition_pressure;
	}

	public void setCondition_pressure(float condition_pressure) {
		this.condition_pressure = condition_pressure;
	}

	public float getCondition_humidity() {
		return condition_humidity;
	}

	public void setCondition_humidity(float condition_humidity) {
		this.condition_humidity = condition_humidity;
	}

    /**
     * @return temperature in Kelvin. To get in Celsius: substract 273.15
     */
    public float getTemperature_temp() {
		return temperature_temp;
	}

	public void setTemperature_temp(float temperature_temp) {
		this.temperature_temp = temperature_temp;
	}
	
    /**
     * @return temperature in Kelvin. To get in Celsius: substract 273.15
     */
	public float getTemperature_minTemp() {
		return temperature_minTemp;
	}

	public void setTemperature_minTemp(float temperature_minTemp) {
		this.temperature_minTemp = temperature_minTemp;
	}
	
    /**
     * @return temperature in Kelvin. To get in Celsius: substract 273.15
     */
	public float getTemperature_maxTemp() {
		return temperature_maxTemp;
	}

	public void setTemperature_maxTemp(float temperature_maxTemp) {
		this.temperature_maxTemp = temperature_maxTemp;
	}

	public float getWind_speed() {
		return wind_speed;
	}

	public void setWind_speed(float wind_speed) {
		this.wind_speed = wind_speed;
	}

	public float getWind_deg() {
		return wind_deg;
	}

	public void setWind_deg(float wind_deg) {
		this.wind_deg = wind_deg;
	}

	public String getRain_time() {
		return rain_time;
	}

	public void setRain_time(String rain_time) {
		this.rain_time = rain_time;
	}

	public float getRain_ammount() {
		return rain_ammount;
	}

	public void setRain_ammount(float rain_ammount) {
		this.rain_ammount = rain_ammount;
	}

	public String getSnow_time() {
		return snow_time;
	}

	public void setSnow_time(String snow_time) {
		this.snow_time = snow_time;
	}

	public float getSnow_ammount() {
		return snow_ammount;
	}

	public void setSnow_ammount(float snow_ammount) {
		this.snow_ammount = snow_ammount;
	}

	public int getClouds_perc() {
		return clouds_perc;
	}

	public void setClouds_perc(int clouds_perc) {
		this.clouds_perc = clouds_perc;
	}

	@Override
	public String toString() {
		return "WeatherInfoObject [id=" + id + ", location=" + location
				+ ", timestamp=" + timestamp + ", location_sunset="
				+ location_sunset + ", location_sunrise=" + location_sunrise
				+ ", location_country=" + location_country + ", location_city="
				+ location_city + ", condition_weatherId="
				+ condition_weatherId + ", condition_condition="
				+ condition_condition + ", condition_descr=" + condition_descr
				+ ", condition_pressure=" + condition_pressure
				+ ", condition_humidity=" + condition_humidity
				+ ", temperature_temp=" + temperature_temp
				+ ", temperature_minTemp=" + temperature_minTemp
				+ ", temperature_maxTemp=" + temperature_maxTemp
				+ ", wind_speed=" + wind_speed + ", wind_deg=" + wind_deg
				+ ", rain_time=" + rain_time + ", rain_ammount=" + rain_ammount
				+ ", snow_time=" + snow_time + ", snow_ammount=" + snow_ammount
				+ ", clouds_perc=" + clouds_perc + "]";
	}



}
