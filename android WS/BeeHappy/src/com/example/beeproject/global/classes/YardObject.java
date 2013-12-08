package com.example.beeproject.global.classes;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "yards")
public class YardObject {

	@DatabaseField(generatedId = true)
    private int id;
    
	@DatabaseField(canBeNull = false)
    private String yardName;
    
    @DatabaseField(canBeNull = true)
	private String location;
	
    @DatabaseField(canBeNull = true, foreign = true, foreignAutoCreate = true) 
	private UserObject userID;
	
	public YardObject() {
    }
	
	public YardObject(String yardName, String location, UserObject userID) {
		this.yardName = yardName;
		this.location = location;
		this.userID = userID;
	}

	public int getId() {
		return id;
	}

	public String getYardName() {
		return yardName;
	}

	public void setYardName(String yardName) {
		this.yardName = yardName;
	}

	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location){
		this.location = location;
	}


	public UserObject getUserID() {
		return userID;
	}
		
	public String toString(){
		return "Yard:{id: " + getId() + ", name: "+getYardName() + 
				", location: "+getLocation() + ", userID: " + getUserID() + "}";
		
	}
	
}

