package com.example.beeproject.weather;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.beeproject.R;
import com.example.beeproject.weather.classes.DayForecast;

public class FragmentDayForecast extends Fragment {
    
    private DayForecast dayForecast;
    private ImageView iconWeather;
    
    public FragmentDayForecast() {}
    
    public void setForecast(DayForecast dayForecast) {
            
            this.dayForecast = dayForecast;
            
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                    Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragmentdayforecast, container, false);
            
            TextView tempView = (TextView) v.findViewById(R.id.tempForecast);
            TextView descView = (TextView) v.findViewById(R.id.skydescForecast);
            TextView presureView = (TextView) v.findViewById(R.id.forecastPresure);
            TextView humidityView = (TextView) v.findViewById(R.id.forecastHumidity);
            TextView windView = (TextView) v.findViewById(R.id.forecastWind);
            
            tempView.setText( (int) (dayForecast.forecastTemp.min - 275.15) + "-" + (int) (dayForecast.forecastTemp.max - 275.15) + "°C");
            descView.setText(dayForecast.weather.currentCondition.getDescr());
            presureView.setText("" + dayForecast.weather.currentCondition.getPressure() + " hPa");
            humidityView.setText("" + dayForecast.weather.currentCondition.getHumidity() + "%");
            windView.setText("" + dayForecast.weather.wind.getSpeed() + " mps" + " " + dayForecast.weather.wind.getDeg() + "°");
            
            iconWeather = (ImageView) v.findViewById(R.id.forCondIcon);
            // Now we retrieve the weather icon
            JSONIconWeatherTask task = new JSONIconWeatherTask();
            task.execute(new String[]{dayForecast.weather.currentCondition.getIcon()});
            
            return v;
    }

    
    
    private class JSONIconWeatherTask extends AsyncTask<String, Void, byte[]> {
            
            @Override
            protected byte[] doInBackground(String... params) {
                    
                    byte[] data = null;
                    
                    try {
                            
                            // Let's retrieve the icon
                            data = ( (new WeatherHttpClient()).getImage(params[0]));
                            
                    } catch (Exception e) {                                
                            e.printStackTrace();
                    }
                    
                    return data;
    }
            
            
            
            
    @Override
            protected void onPostExecute(byte[] data) {                        
                    super.onPostExecute(data);
                    
                    if (data != null) {
                            Bitmap img = BitmapFactory.decodeByteArray(data, 0, data.length); 
                            iconWeather.setImageBitmap(img);
                    }
            }



}
}
