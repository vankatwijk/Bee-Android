package com.example.beeproject.weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.beeproject.weather.classes.WeatherInfoObject;


public class JSONWeatherParser {

    public static WeatherInfoObject getWeather(String data) throws JSONException  {
             WeatherInfoObject weather = new WeatherInfoObject();
            
            // We create out JSONObject from the data
            JSONObject jObj = new JSONObject(data);
            
            JSONObject sysObj = getObject("sys", jObj);
            weather.setLocation_country(getString("country", sysObj));
            weather.setLocation_sunrise(getInt("sunrise", sysObj));
            weather.setLocation_sunset(getInt("sunset", sysObj));
            weather.setLocation_city(getString("name", jObj));
            
            // We get weather info (This is an array)
            JSONArray jArr = jObj.getJSONArray("weather");
            
            // We use only the first value
            JSONObject JSONWeather = jArr.getJSONObject(0);
            weather.setCondition_weatherId(getInt("id", JSONWeather));
            weather.setCondition_descr(getString("description", JSONWeather));
            weather.setCondition_condition(getString("main", JSONWeather));
            
            JSONObject mainObj = getObject("main", jObj);
            weather.setCondition_humidity(getInt("humidity", mainObj));
            weather.setCondition_pressure(getInt("pressure", mainObj));
            weather.setTemperature_maxTemp(getFloat("temp_max", mainObj));
            weather.setTemperature_minTemp(getFloat("temp_min", mainObj));
            weather.setTemperature_temp(getFloat("temp", mainObj));
            
            // Wind
            JSONObject wObj = getObject("wind", jObj);
            weather.setWind_speed(getFloat("speed", wObj));
            weather.setWind_deg(getFloat("deg", wObj));
            
            // Clouds
            JSONObject cObj = getObject("clouds", jObj);
            weather.setClouds_perc(getInt("all", cObj));
            
            
            return weather;
    }
         
    private static JSONObject getObject(String tagName, JSONObject jObj)  throws JSONException {
            JSONObject subObj = jObj.getJSONObject(tagName);
            return subObj;
    }
    
    private static String getString(String tagName, JSONObject jObj) throws JSONException {
            return jObj.getString(tagName);
    }

    private static float  getFloat(String tagName, JSONObject jObj) throws JSONException {
            return (float) jObj.getDouble(tagName);
    }
    
    private static int  getInt(String tagName, JSONObject jObj) throws JSONException {
            return jObj.getInt(tagName);
    }
    
}
