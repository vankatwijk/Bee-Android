package com.example.beeproject.weather;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    private ImageView imgView;
    
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
            imgView = (ImageView) findViewById(R.id.condIcon);
            
            gps = new GPSTracker(this);
            
            // check if GPS enabled     
            if(gps.canGetLocation()){
                 
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
    
}

	
