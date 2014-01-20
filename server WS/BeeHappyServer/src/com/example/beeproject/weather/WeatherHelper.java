package com.example.beeproject.weather;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.example.beeproject.db.ConnectionProvider;
import com.example.beeproject.global.classes.YardObject;
import com.example.beeproject.weather.classes.WeatherInfoObject;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.stmt.QueryBuilder;

/**
 * Helper class responsible for retrieving weather information and storing in in the database.
* Weather information is retrieved from weather api server several times a day.
 * This information is stored in the database for future datamining.
 * <p>Weather information is related to a location - yard location.
 * When weather retrieval task runs, weather for all distinct locations of all yards 
 * is retrieved and stored.
 * <p>See com.example.beeproject.weather.WeatherHttpClient
 * <p>To set how often the weather is retrieved, see Quartz documentation, 
 * particularly src/quartz_data.xml
 * @author Olya
 */
public class WeatherHelper {
	JdbcPooledConnectionSource connectionSource;
	
	public WeatherHelper(){
		connectionSource = ConnectionProvider.getConnectionSource();
	}

	public int retrieveAndStoreWeather(){
        //System.out.println("data: "+ data);
        
        try {
            Dao<WeatherInfoObject, Integer> weatherInfoDao = DaoManager.createDao(
            		connectionSource, WeatherInfoObject.class);
            
        	List<String> locationList = getLocationList();
        	//System.out.println("locationList: " + locationList);
        	
        	for(String location : locationList){
        		String data = ( (new WeatherHttpClient()).getWeatherData(location, null));
        		
	            WeatherInfoObject weather = JSONWeatherParser.getWeather(data);
	            weather.setLocation(location);
	            weather.setTimestamp(System.currentTimeMillis()/1000);
	            System.out.println("weather: "+ weather);
	            
	            weatherInfoDao.create(weather);
        	}
            
	    } catch (JSONException e) {                                
	            e.printStackTrace();
	    } catch (SQLException e) { 
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return 0;
	}
	
	public List<String> getLocationList() throws SQLException{
		List<String> result = new ArrayList<>();
		
		Dao<YardObject, Integer> yardDao = DaoManager.createDao(connectionSource, YardObject.class);
		List<String> columns = new ArrayList<String>(); 
		columns.add("location");
		
		QueryBuilder<YardObject, Integer> qBuilder = yardDao.queryBuilder().selectColumns(columns).distinct();
		
		List<YardObject> queryResult = qBuilder.query();
		
		for(YardObject yard : queryResult){
			result.add(yard.getLocation());
		}
		
		return result;
	}
}
