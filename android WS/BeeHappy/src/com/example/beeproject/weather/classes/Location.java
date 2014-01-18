package com.example.beeproject.weather.classes;

import java.io.Serializable;

public class Location implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private float longitude;
    private float latitude;
    private long sunset;
    private long sunrise;
    private String country;
    private String city;
    
    public float getLongitude() {
            return longitude;
    }
    public void setLongitude(float longitude) {
            this.longitude = longitude;
    }
    public float getLatitude() {
            return latitude;
    }
    public void setLatitude(float latitude) {
            this.latitude = latitude;
    }
    public long getSunset() {
            return sunset;
    }
    public void setSunset(long sunset) {
            this.sunset = sunset;
    }
    public long getSunrise() {
            return sunrise;
    }
    public void setSunrise(long sunrise) {
            this.sunrise = sunrise;
    }
    public String getCountry() {
            return country;
    }
    public void setCountry(String country) {
            this.country = country;
    }
    public String getCity() {
            return city;
    }
    public void setCity(String city) {
            this.city = city;
    }
    
    
    
    
}
