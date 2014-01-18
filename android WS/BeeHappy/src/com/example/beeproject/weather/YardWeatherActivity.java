package com.example.beeproject.weather;


import org.json.JSONException;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.beeproject.R;
import com.example.beeproject.weather.classes.Weather;
import com.example.beeproject.weather.classes.WeatherForecast;


public class YardWeatherActivity extends FragmentActivity {

	private TextView cityText;
    private TextView condDescr;
    private TextView temp;
    private TextView press;
    private TextView windSpeed;
    private TextView windDeg;
    
    private TextView hum;

    
    private ViewPager pager;
    
    
    private static String forecastDaysNum = "5";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_yard_weather);
            
            Intent intent = getIntent();
            
            String city = intent.getStringExtra("location");
            String lang = "en";
            
            cityText = (TextView) findViewById(R.id.cityText);
            condDescr = (TextView) findViewById(R.id.condDescr);
            temp = (TextView) findViewById(R.id.temp);
            hum = (TextView) findViewById(R.id.hum);
            press = (TextView) findViewById(R.id.press);
            windSpeed = (TextView) findViewById(R.id.windSpeed);
            windDeg = (TextView) findViewById(R.id.windDeg);
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
                    System.out.println(data);
                    try {
                            weather = JSONWeatherParser.getWeather(data);
                           
                            
                    } catch (JSONException e) {                                
                            e.printStackTrace();
                    }
                    return weather;
            
    }
            
            
            
            
    @Override
            protected void onPostExecute(Weather weather) {                        
                    super.onPostExecute(weather);
                    
                    int id = weather.currentCondition.getWeatherId();
                    System.out.println(weather.currentCondition.getWeatherId());
                    
                    if(id == 200 || id == 201 || id == 202 || id == 210 || id == 211 || id == 212 || id == 221 || id == 230 || id == 231 || id == 232){
                    	int imageResource = R.drawable.thunderstorm;

                        ImageView imageView = (ImageView) findViewById(R.id.condIcon);
                        Drawable image = getResources().getDrawable(imageResource);
                        imageView.setImageDrawable(image);
                    }else if(id == 300 || id == 301 || id == 302 || id == 310 || id == 311 || id == 312 || id == 321){
                    	int imageResource = R.drawable.rainy;

                        ImageView imageView = (ImageView) findViewById(R.id.condIcon);
                        Drawable image = getResources().getDrawable(imageResource);
                        imageView.setImageDrawable(image);
                    }else if(id == 500 || id == 501 || id == 502 || id == 503 || id == 504){
                    	int imageResource = R.drawable.rainy_with_sun;

                        ImageView imageView = (ImageView) findViewById(R.id.condIcon);
                        Drawable image = getResources().getDrawable(imageResource);
                        imageView.setImageDrawable(image);
                    }else if(id == 511){
                    	int imageResource = R.drawable.snow;

                        ImageView imageView = (ImageView) findViewById(R.id.condIcon);
                        Drawable image = getResources().getDrawable(imageResource);
                        imageView.setImageDrawable(image);
                    }else if(id == 520 || id == 521 || id == 522){
                    	int imageResource = R.drawable.rainy;

                        ImageView imageView = (ImageView) findViewById(R.id.condIcon);
                        Drawable image = getResources().getDrawable(imageResource);
                        imageView.setImageDrawable(image);
                    }else if(id == 600 || id == 601 || id == 602 || id == 611 || id == 621){
                    	int imageResource = R.drawable.snow;

                        ImageView imageView = (ImageView) findViewById(R.id.condIcon);
                        Drawable image = getResources().getDrawable(imageResource);
                        imageView.setImageDrawable(image);
                    }else if(id == 701 || id == 711 || id == 721 || id == 731 || id == 741){
                    	int imageResource = R.drawable.myst;

                        ImageView imageView = (ImageView) findViewById(R.id.condIcon);
                        Drawable image = getResources().getDrawable(imageResource);
                        imageView.setImageDrawable(image);
                    }else if(id == 800){
                    	int imageResource = R.drawable.sunny;

                        ImageView imageView = (ImageView) findViewById(R.id.condIcon);
                        Drawable image = getResources().getDrawable(imageResource);
                        imageView.setImageDrawable(image);
                    }else if(id == 801){
                    	int imageResource = R.drawable.sunny_with_clouds;

                        ImageView imageView = (ImageView) findViewById(R.id.condIcon);
                        Drawable image = getResources().getDrawable(imageResource);
                        imageView.setImageDrawable(image);
                    }else if(id == 802){
                    	int imageResource = R.drawable.cloudy;

                        ImageView imageView = (ImageView) findViewById(R.id.condIcon);
                        Drawable image = getResources().getDrawable(imageResource);
                        imageView.setImageDrawable(image);
                    }else if(id == 803 || id == 804){
                    	int imageResource = R.drawable.dark_clouds;

                        ImageView imageView = (ImageView) findViewById(R.id.condIcon);
                        Drawable image = getResources().getDrawable(imageResource);
                        imageView.setImageDrawable(image);
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

	
