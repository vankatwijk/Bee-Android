package com.example.beeproject.db;

import java.sql.SQLException;

import org.postgresql.util.PSQLException;

import com.example.beeproject.global.classes.UserObject;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class ConnectionProvider {
	private static JdbcPooledConnectionSource connectionSource; //connection source to our database, connections are pooled and reused

	static String url = "jdbc:postgresql://localhost:5432/";
	static String databaseName = "BeeHappy"; 
	//TODO: get the connection string from config file
	static String databaseUsername = "postgres"; 
	static String databasePassword = "beeHappy"; 
	
	public static JdbcPooledConnectionSource getConnectionSource(){
		String databaseUrl = url + databaseName;
		if(connectionSource == null){
			try {
				try{
					connectionSource = new JdbcPooledConnectionSource(databaseUrl);
					connectionSource.setUsername(databaseUsername);
					connectionSource.setPassword(databasePassword);
					
					Dao<UserObject, Integer> dao = DaoManager.createDao(connectionSource, UserObject.class);
					int result  = dao.executeRawNoArgs("SELECT 1 FROM test;");
				}
				catch(PSQLException e){
					//probably, database does not exist
					//create it
					connectionSource.close();
					
					System.out.println("Creating database " + databaseName);
					JdbcPooledConnectionSource tempConnectionSource = new JdbcPooledConnectionSource(url + "postgres"); //connect to default database to create other one 
					tempConnectionSource.setUsername(databaseUsername);
					tempConnectionSource.setPassword(databasePassword);
					
					Dao<UserObject, Integer> dao = DaoManager.createDao(tempConnectionSource, UserObject.class);
					int result  = dao.executeRawNoArgs("CREATE DATABASE \"" + databaseName + "\";");
					System.out.println("Created database " + databaseName+ " " + result);
					
					tempConnectionSource.close();

					//connection source to connect to requested database
					connectionSource = new JdbcPooledConnectionSource(databaseUrl);
					connectionSource.setUsername(databaseUsername);
					connectionSource.setPassword(databasePassword);
				}
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return connectionSource;
	}
	

}
