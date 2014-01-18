package com.example.beeproject.weather;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.beeproject.R;
import com.example.beeproject.global.classes.DatabaseHelper;
import com.example.beeproject.global.classes.DatabaseManager;
import com.example.beeproject.global.classes.YardObject;
import com.example.beeproject.weather.classes.Weather;
import com.j256.ormlite.dao.RuntimeExceptionDao;


public class WeatherActivity extends FragmentActivity {

	private TextView cityText;
    private TextView condDescr;
    private TextView temp;
    private TextView press;
    private TextView windSpeed;
    private TextView windDeg;
    
    private TextView hum;
    
    GPSTracker gps;
    String lat = "";
    String lon = "";
    
    ArrayAdapter<String> aa;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_weather);
            
            cityText = (TextView) findViewById(R.id.cityText);
            condDescr = (TextView) findViewById(R.id.condDescr);
            temp = (TextView) findViewById(R.id.temp);
            hum = (TextView) findViewById(R.id.hum);
            press = (TextView) findViewById(R.id.press);
            windSpeed = (TextView) findViewById(R.id.windSpeed);
            windDeg = (TextView) findViewById(R.id.windDeg);
            
            gps = new GPSTracker(this);
            
            // check if GPS enabled     
            if(gps.canGetLocation() && isNetworkAvailable()){
                 
                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();
                 
                lat = Integer.toString((int) latitude);
                lon = Integer.toString((int) longitude);
                
                JSONWeatherTask task = new JSONWeatherTask();
                task.execute(new String[]{lat,lon});
                // \n is for new line
            }else{
                // can't get location
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
                gps.showSettingsAlert();
            }
            
            ListView locationList = (ListView)findViewById(R.id.locationsList);
            DatabaseManager dbManager = new DatabaseManager();
			DatabaseHelper db = dbManager.getHelper(getApplicationContext());
			
			RuntimeExceptionDao<YardObject, Integer> yardDao = db.getYardRunDao();
            
            List<YardObject> yardLocations;
            ArrayList<String> locationsList = new ArrayList<String>();
            
            try {
				yardLocations = yardDao.queryBuilder()
				.distinct().selectColumns("location").query();
				
				
				for(int i = 0; i < yardLocations.size(); i++){
					locationsList.add(yardLocations.get(i).getLocation());
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            aa = new ArrayAdapter<String>(getBaseContext(),
					android.R.layout.simple_list_item_1,
					locationsList);
            locationList.setAdapter(aa);
            
            locationList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					TextView loc = (TextView) arg1;
					String location = loc.getText().toString();
					
					Intent intent = new Intent(getBaseContext(), YardWeatherActivity.class);
					intent.putExtra("location", location);
					startActivity(intent);
				}
			});
    }

    
    private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {
            
            @Override
            protected Weather doInBackground(String... params) {
                    Weather weather = new Weather();
                    String data = ( (new WeatherHttpClient()).getWeatherDataByLatLon(params[0],params[1]));

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
    
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager 
              = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    
}

	
