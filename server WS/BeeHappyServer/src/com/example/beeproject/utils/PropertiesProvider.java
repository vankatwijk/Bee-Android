package com.example.beeproject.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import sun.awt.AppContext;

import com.example.beeproject.TestCRUDServlet;
import com.example.beeproject.test.TestActions;

public class PropertiesProvider {

	/*usage: Properties properties = PropertiesProvider.getProperties();
		responseWriter.println("\n\nproperties:"+properties);
		responseWriter.println(properties.getProperty("db.username"));*/
	
	private static Properties properties;
	
	public static Properties getProperties(){
		if(properties == null){

			InputStream inputStream;
			try {
				inputStream = PropertiesProvider.class.getClassLoader().getResourceAsStream("/WEB-INF/config.properties");
				System.out.println(PropertiesProvider.class.getClassLoader().getResource("../"));
				System.out.println(inputStream);
				properties = new Properties();
				properties.load(inputStream);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return properties;
	}
}
