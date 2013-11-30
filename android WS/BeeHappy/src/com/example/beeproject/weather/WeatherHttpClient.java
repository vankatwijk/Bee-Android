package com.example.beeproject.weather;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherHttpClient {

    private static String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
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
    
    
    public String getForecastWeatherData(String location, String lang, String sForecastDayNum) {
            HttpURLConnection con = null ;
            InputStream is = null;
            int forecastDayNum = Integer.parseInt(sForecastDayNum);
                            
            try {
                            
                    // Forecast
                    String url = BASE_FORECAST_URL + location;
                    if (lang != null)
                            url = url + "&lang=" + lang;
                    
                    url = url + "&cnt=" + forecastDayNum;
                    con = (HttpURLConnection) ( new URL(url)).openConnection();
                    con.setRequestMethod("GET");
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    con.connect();
                    
                    // Let's read the response
                    StringBuffer buffer1 = new StringBuffer();
                    is = con.getInputStream();
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(is));
                    String line1 = null;
                    while (  (line1 = br1.readLine()) != null )
                            buffer1.append(line1 + "\r\n");
                    
                    is.close();
                    con.disconnect();
                    
                    System.out.println("Buffer ["+buffer1.toString()+"]");
                    return buffer1.toString();
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
    
    public byte[] getImage(String code) {
            HttpURLConnection con = null ;
            InputStream is = null;
            try {
                    con = (HttpURLConnection) ( new URL(IMG_URL + code)).openConnection();
                    con.setRequestMethod("GET");
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    con.connect();
                    
                    // Let's read the response
                    is = con.getInputStream();
                    byte[] buffer = new byte[1024];
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    
                    while ( is.read(buffer) != -1)
                            baos.write(buffer);
                    
                    return baos.toByteArray();
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
