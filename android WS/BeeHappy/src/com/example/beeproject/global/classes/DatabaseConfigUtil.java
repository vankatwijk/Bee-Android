package com.example.beeproject.global.classes;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;


/**
 * Database helper class used to manage the creation and upgrading of your database. This class also usually provides
 * the DAOs used by the other classes.
 */
public class DatabaseConfigUtil extends OrmLiteConfigUtil {
		
	private static final Class<?>[] classes = new Class[] {
	    YardObject.class, UserObject.class, HiveObject.class, CheckFormObject.class, DiseaseObject.class, StockObject.class, OutbrakeObject.class, DiseaseNotesObject.class
	  };
		
        public static void main(String[] args) throws SQLException, IOException {
 
                writeConfigFile(new File("D:/Programming/Android/BeeHappy/res/raw/ormlite_config.txt"), classes);
        }
}
