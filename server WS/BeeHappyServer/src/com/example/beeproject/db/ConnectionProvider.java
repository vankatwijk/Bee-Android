package com.example.beeproject.db;

import java.sql.SQLException;

import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;

public class ConnectionProvider {
	private static JdbcPooledConnectionSource connectionSource; //connection source to our database, connections are pooled and reused

	static String databaseUrl = "jdbc:postgresql://localhost:5432/BeeHappy"; 
	//TODO: get the connection string from config file
	static String databaseUsername = "postgres"; 
	static String databasePassword = "beeHappy"; 
	
	public static JdbcPooledConnectionSource getConnectionSource(){
		if(connectionSource == null){
			try {
				connectionSource = new JdbcPooledConnectionSource(databaseUrl);
				connectionSource.setUsername(databaseUsername);
				connectionSource.setPassword(databasePassword);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return connectionSource;
	}
	

}
