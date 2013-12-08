package com.example.beeproject.global.classes;

public class GlobalVar {

	private static GlobalVar mInstance= null;
	
	private int userID;
	
	public GlobalVar(){
		userID = 0;
	}
	
	public static synchronized GlobalVar getInstance(){
    	if(null == mInstance){
    		mInstance = new GlobalVar();
    	}
    	return mInstance;
    }
	
	public int getUserID(){
		return userID;
	}
	
	public void setUserID(int userID){
		this.userID = userID;
	}
	
}
