package com.example.beeproject.global.classes;

import java.util.ArrayList;


public class GlobalVariables {

	private static GlobalVariables mInstance= null;
	
	private ArrayList<Object> YardList;
	private ArrayList<Object> HiveList;
	
	public GlobalVariables(){
		YardList = new ArrayList<Object>();
		HiveList = new ArrayList<Object>();
	}
	
	public static synchronized GlobalVariables getInstance(){
	    	if(null == mInstance){
	    		mInstance = new GlobalVariables();
	    	}
	    return mInstance;
	}
	
	public ArrayList<Object> getYardList(){
		return YardList;
	}
	
	public ArrayList<Object> getHiveList(){
		return HiveList;
	}
	
	public void addYard(String yardName){
		YardObject yard = new YardObject(yardName);
		
		YardList.add(yard);
	}
	
	public void addHive(String hiveName){
		HiveObject hive = new HiveObject(hiveName);
		
		HiveList.add(hive);
	}
	
}
