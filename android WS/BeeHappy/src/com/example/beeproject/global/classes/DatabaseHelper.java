package com.example.beeproject.global.classes;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.beeproject.R;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * Database helper class used to manage the creation and upgrading of your database. This class also usually provides
 * the DAOs used by the other classes.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	// name of the database file for your application -- change to something appropriate for your app
	private static final String DATABASE_NAME = "BeeHappy.db";
	// any time you make changes to your database objects, you may have to increase the database version
	private static final int DATABASE_VERSION = 1;

	
	private Dao<UserObject, Integer> userDao = null;
	private RuntimeExceptionDao<UserObject, Integer> userRuntimeDao = null;
	
	private Dao<YardObject, Integer> yardDao = null;
	private RuntimeExceptionDao<YardObject, Integer> yardRuntimeDao = null;
	
	private Dao<HiveObject, Integer> hiveDao = null;
	private RuntimeExceptionDao<HiveObject, Integer> hiveRuntimeDao = null;
	
	private Dao<CheckFormObject, Integer> checkFormDao = null;
	private RuntimeExceptionDao<CheckFormObject, Integer> checkFormRuntimeDao = null;
	
	private Dao<DiseaseObject, Integer> diseaseDao = null;
	private RuntimeExceptionDao<DiseaseObject, Integer> diseaseRuntimeDao = null;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
	}

	/**
	 * This is called when the database is first created. Usually you should call createTable statements here to create
	 * the tables that will store your data.
	 */
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			Log.i(DatabaseHelper.class.getName(), "onCreate");
			TableUtils.createTable(connectionSource, YardObject.class);
			TableUtils.createTable(connectionSource, HiveObject.class);
			TableUtils.createTable(connectionSource, UserObject.class);
			TableUtils.createTable(connectionSource, CheckFormObject.class);
			TableUtils.createTable(connectionSource, DiseaseObject.class);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		}

		// here we try inserting data in the on-create as a test
		
	}

	/**
	 * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
	 * the various data to match the new version number.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		try {
			Log.i(DatabaseHelper.class.getName(), "onUpgrade");
			TableUtils.dropTable(connectionSource, YardObject.class, true);
			TableUtils.dropTable(connectionSource, HiveObject.class, true);
			TableUtils.dropTable(connectionSource, UserObject.class, true);
			TableUtils.dropTable(connectionSource, CheckFormObject.class, true);
			TableUtils.dropTable(connectionSource, DiseaseObject.class, true);
			// after we drop the old databases, we create the new ones
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		}
	}
	//user
	public Dao<UserObject, Integer> getUserDao() throws SQLException {
		if (userDao == null) {
			userDao = getDao(UserObject.class);
		}
		return userDao;
	}
	
	public RuntimeExceptionDao<UserObject, Integer> getUserRunDao() {
		if (userRuntimeDao == null) {
			userRuntimeDao = getRuntimeExceptionDao(UserObject.class);
		}
		return userRuntimeDao;
	}

	//yard
	public Dao<YardObject, Integer> getYardDao() throws SQLException {
		if (yardDao == null) {
			yardDao = getDao(YardObject.class);
		}
		return yardDao;
	}

	public RuntimeExceptionDao<YardObject, Integer> getYardRunDao() {
		if (yardRuntimeDao == null) {
			yardRuntimeDao = getRuntimeExceptionDao(YardObject.class);
		}
		return yardRuntimeDao;
	}
	
	//hive
	public Dao<HiveObject, Integer> getHiveDao() throws SQLException {
		if (hiveDao == null) {
			hiveDao = getDao(HiveObject.class);
		}
		return hiveDao;
	}
	
	public RuntimeExceptionDao<HiveObject, Integer> getHiveRunDao() {
		if (hiveRuntimeDao == null) {
			hiveRuntimeDao = getRuntimeExceptionDao(HiveObject.class);
		}
		return hiveRuntimeDao;
	}
	
	//checkform
	public Dao<CheckFormObject, Integer> getCheckFormDao() throws SQLException {
		if (checkFormDao == null) {
			checkFormDao = getDao(CheckFormObject.class);
		}
		return checkFormDao;
	}
	
	public RuntimeExceptionDao<CheckFormObject, Integer> getCheckFormRunDao() {
		if (checkFormRuntimeDao == null) {
			checkFormRuntimeDao = getRuntimeExceptionDao(CheckFormObject.class);
		}
		return checkFormRuntimeDao;
	}
	
	//disease
	public Dao<DiseaseObject, Integer> getDiseaseDao() throws SQLException {
		if (diseaseDao == null) {
			diseaseDao = getDao(DiseaseObject.class);
		}
		return diseaseDao;
	}
	
	public RuntimeExceptionDao<DiseaseObject, Integer> getDiseaseRunDao() {
		if (diseaseRuntimeDao == null) {
			diseaseRuntimeDao = getRuntimeExceptionDao(DiseaseObject.class);
		}
		return diseaseRuntimeDao;
	}
	
	/**
	 * Close the database connections and clear any cached DAOs.
	 */
	@Override
	public void close() {
		super.close();
		yardRuntimeDao = null;
		hiveRuntimeDao = null;
		userRuntimeDao = null;
		diseaseRuntimeDao = null;
	}
}
