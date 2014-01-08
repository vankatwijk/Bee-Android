package com.example.beeproject.global.classes;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import com.example.beeproject.syncing.DeletedObject;
import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;


/**
 * Database helper class used to manage the creation and upgrading of your database. This class also usually provides
 * the DAOs used by the other classes.
 */
public class DatabaseConfigUtil extends OrmLiteConfigUtil {
		
	/**
	 * All the classes that are being persisted to local database
	 * using ORMLite must be listed here
	 */
	private static final Class<?>[] classes = new Class[] {
	    YardObject.class, UserObject.class, HiveObject.class, CheckFormObject.class, DiseaseObject.class, StockObject.class, 
	    OutbrakeObject.class, DiseaseNotesObject.class, DeletedObject.class
	  };
		
	public static void main(String[] args) throws SQLException, IOException {
       writeConfigFile(new File("D:/BEEHAPPY/bee/android WS/BeeHappy/res/raw/ormlite_config.txt"), classes);
    }
}
