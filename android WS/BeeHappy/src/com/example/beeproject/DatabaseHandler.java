package com.example.beeproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
	
	// Database Version
    private static final int DATABASE_VERSION = 1;
    
    // Database Name
    private static final String DATABASE_NAME = "BeeAppDatabase";
    
    // Table Names
    private static final String TABLE_USERS = "users";
    
    // Common column names
    private static final String KEY_ID = "id";
    
    // Users column names
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    
    // Users table statement
    private static final String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS +
    		" (" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_USERNAME + " VARCHAR, " + KEY_PASSWORD + " VARCHAR" + ")";
    
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    @Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_USERS_TABLE);

	}
    
    @Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

        onCreate(db);
	}
    
    // Add user
    public Boolean addUser(String username, String password){
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	Boolean check = false;
    	
    	Cursor c = db.rawQuery("SELECT  * FROM " + TABLE_USERS + " WHERE " + KEY_USERNAME + "=?" , new String[] {username});
    	
    	if (c.getCount() == 0){
	    	ContentValues values = new ContentValues();
	    	values.put(KEY_USERNAME, username);
	    	values.put(KEY_PASSWORD, password);
	    	
	    	db.insert(TABLE_USERS, null, values);
	    	db.close();
	    	
	    	check = true;
    	}
    	return check;
    }
    
    // Check username and password
    public Boolean checkUser(String username, String password){
    	SQLiteDatabase db = this.getReadableDatabase();
    	
    	Boolean check = false;
    	
    	Cursor c = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + KEY_USERNAME + "=? AND " + KEY_PASSWORD + "=?" , new String[] {username, password });
    	
    	if (c.getCount() > 0){
    		check = true;
    	}
    	
    	return check;
    }
	
}
