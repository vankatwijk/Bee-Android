package com.example.beeproject;

import org.json.JSONException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.example.beeproject.weather.JSONWeatherParser;
import com.example.beeproject.weather.WeatherHelper;
import com.example.beeproject.weather.WeatherHttpClient;
import com.example.beeproject.weather.classes.WeatherInfoObject;

public class WeatherJob implements Job {

    @Override
    public void execute(final JobExecutionContext ctx)
            throws JobExecutionException {

        System.out.println("==================================");
        System.out.println("Executing Weatjer Job");
        
        WeatherHelper weatherHelper = new WeatherHelper();
        weatherHelper.retrieveAndStoreWeather();
        System.out.println("Fininshed Executing Weatjer Job");
        System.out.println("==================================");
    }

}