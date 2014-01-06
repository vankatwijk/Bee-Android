package com.example.beeproject.global.classes;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "yards")
public class YardObject implements BeeObjectInterface{

	@DatabaseField(generatedId = true)
    private int id;
    
	@DatabaseField(canBeNull = false)
    private String yardName;
    
    @DatabaseField(canBeNull = true)
	private String location;
	
    @DatabaseField(canBeNull = true, foreign = true, foreignAutoCreate = true) 
	private UserObject userID;
    
    @DatabaseField(canBeNull = false)
	private boolean synced;
    
    @DatabaseField(canBeNull = true)
    private int serverSideID;
	
	

	public void setUserID(UserObject userID) {
		this.userID = userID;
	}

	public YardObject() {
    }
	
	public YardObject(String yardName, String location, UserObject userID, boolean synced) {
		this.yardName = yardName;
		this.location = location;
		this.userID = userID;
		this.synced = synced;
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

	public boolean isSynced() {
		return synced;
	}

	public void setSynced(boolean synced) {
		this.synced = synced;
	}
	
	public int getServerSideID() {
		return serverSideID;
	}

	public void setServerSideID(int serverSideID) {
		this.serverSideID = serverSideID;
	}

	@Override
	public String getDBTableName() {
		return "yards";
	}
		
	@Override
	public String toString() {
		return "YardObject [id=" + id + ", yardName=" + yardName
				+ ", location=" + location + ", userID=" + userID + ", synced="
				+ synced + "]";
	}
		
	
}

