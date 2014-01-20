package com.example.beeproject.weather;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherHttpClient {

    private static String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
    private static String BASE_URL_LATLONG = "http://api.openweathermap.org/data/2.5/weather?";
    private static String IMG_URL = "http://openweathermap.org/img/w/";

    
    private static String BASE_FORECAST_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?mode=json&q=";

    
    public String getWeatherData(String location, String lang) {
            HttpURLConnection con = null ;
            InputStream is = null;
            
            try {
                    String url = BASE_URL + location;
                    if (lang != null)
                            url = url + "&lang=" + lang;
                    
                    con = (HttpURLConnection) ( new URL(url)).openConnection();
                    con.setRequestMethod("GET");
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    con.connect();
                    
                    // Let's read the response
                    StringBuffer buffer = new StringBuffer();
                    is = con.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String line = null;
                    while (  (line = br.readLine()) != null )
                            buffer.append(line + "\r\n");
                    
                    is.close();
                    con.disconnect();
                    
                    return buffer.toString();
        }
            catch(Throwable t) {
                    t.printStackTrace();
            }
            finally {
                    try { is.close(); } catch(Throwable t) {}
                    try { con.disconnect(); } catch(Throwable t) {}
            }

            return null;
                            
    }
    
}
