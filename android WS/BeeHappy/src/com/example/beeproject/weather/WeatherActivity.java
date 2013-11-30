package com.example.beeproject.weather;


import org.json.JSONException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.beeproject.R;
import com.example.beeproject.weather.classes.Weather;
import com.example.beeproject.weather.classes.WeatherForecast;


public class WeatherActivity extends FragmentActivity {

	private TextView cityText;
    private TextView condDescr;
    private TextView temp;
    private TextView press;
    private TextView windSpeed;
    private TextView windDeg;
    
    private TextView hum;
    private ImageView imgView;
    
    private ViewPager pager;
    
    private static String forecastDaysNum = "3";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_weather);
            
            String city = "Diemen,NL";
            String lang = "en";
            
            cityText = (TextView) findViewById(R.id.cityText);
            condDescr = (TextView) findViewById(R.id.condDescr);
            temp = (TextView) findViewById(R.id.temp);
            hum = (TextView) findViewById(R.id.hum);
            press = (TextView) findViewById(R.id.press);
            windSpeed = (TextView) findViewById(R.id.windSpeed);
            windDeg = (TextView) findViewById(R.id.windDeg);
            imgView = (ImageView) findViewById(R.id.condIcon);
            pager = (ViewPager) findViewById(R.id.pager);
            
            JSONWeatherTask task = new JSONWeatherTask();
            task.execute(new String[]{city,lang});
            
            JSONForecastWeatherTask task1 = new JSONForecastWeatherTask();
            task1.execute(new String[]{city,lang, forecastDaysNum});
    }

    
    private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {
            
            @Override
            protected Weather doInBackground(String... params) {
                    Weather weather = new Weather();
                    String data = ( (new WeatherHttpClient()).getWeatherData(params[0],params[1]));

                    try {
                            weather = JSONWeatherParser.getWeather(data);
                            
                            // Let's retrieve the icon
                            weather.iconData = ( (new WeatherHttpClient()).getImage(weather.currentCondition.getIcon()));
                            
                    } catch (JSONException e) {                                
                            e.printStackTrace();
                    }
                    return weather;
            
    }
            
            
            
            
    @Override
            protected void onPostExecute(Weather weather) {                        
                    super.onPostExecute(weather);
                    
                    if (weather.iconData != null && weather.iconData.length > 0) {
                            Bitmap img = BitmapFactory.decodeByteArray(weather.iconData, 0, weather.iconData.length); 
                            imgView.setImageBitmap(img);
                    }
                    
                    cityText.setText(weather.location.getCity() + "," + weather.location.getCountry());
                    condDescr.setText(weather.currentCondition.getCondition() + "(" + weather.currentCondition.getDescr() + ")");
                    temp.setText("" + Math.round((weather.temperature.getTemp() - 273.15)) + "°C");
                    hum.setText("" + weather.currentCondition.getHumidity() + "%");
                    press.setText("" + weather.currentCondition.getPressure() + " hPa");
                    windSpeed.setText("" + weather.wind.getSpeed() + " mps");
                    windDeg.setText("" + weather.wind.getDeg() + "°");
                            
            }
    
    }
    
    private class JSONForecastWeatherTask extends AsyncTask<String, Void, WeatherForecast> {
        
        @Override
        protected WeatherForecast doInBackground(String... params) {
                
                String data = ( (new WeatherHttpClient()).getForecastWeatherData(params[0], params[1], params[2]));
                WeatherForecast forecast = new WeatherForecast();
                try {
                        forecast = JSONWeatherParser.getForecastWeather(data);
                        // Let's retrieve the icon
                        //weather.iconData = ( (new WeatherHttpClient()).getImage(weather.currentCondition.getIcon()));
                        
                } catch (JSONException e) {                                
                        e.printStackTrace();
                }
                return forecast;
        
}
        
        
        
        
        @Override
        protected void onPostExecute(WeatherForecast forecastWeather) {                        
                super.onPostExecute(forecastWeather);
                
                DailyForecastPageAdapter adapter = new DailyForecastPageAdapter(Integer.parseInt(forecastDaysNum), getSupportFragmentManager(), forecastWeather);
                
                pager.setAdapter(adapter);
        }



}
}

	
