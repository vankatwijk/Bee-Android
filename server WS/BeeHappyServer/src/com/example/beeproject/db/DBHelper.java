package com.example.beeproject.db;

import java.io.PrintWriter;

import com.example.beeproject.global.classes.BeeObjectClasses;
import com.example.beeproject.global.classes.BeeObjectInterface;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DBHelper {
	public static void createDB(PrintWriter responseWriter){
		JdbcPooledConnectionSource connectionSource = ConnectionProvider.getConnectionSource();
		Class[] classList = BeeObjectClasses.getClassesList();
		try{
			for (Class objectClass : classList){
				// instantiate the dao
				Dao<BeeObjectInterface, Integer> classDao = DaoManager.createDao(connectionSource, objectClass);
				// if you need to create the 'accounts' table make this call
				responseWriter.println("Checking Table for class: " + objectClass);
				if(!classDao.isTableExists()){
					TableUtils.createTable(connectionSource, objectClass);
					responseWriter.println("Created Table for class: " + objectClass);
				}
			}	
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
