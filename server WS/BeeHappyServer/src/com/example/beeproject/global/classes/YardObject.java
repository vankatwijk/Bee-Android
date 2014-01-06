
package com.example.beeproject.global.classes;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


/**
 * Class representing yard object.
 * ONLY for server side. Client must have its own different implementaion
 * @author Olya
 *
 */
@DatabaseTable(tableName = "yards")
public class YardObject implements BeeObjectInterface{

	@DatabaseField(generatedId = true)
    private int id; //server side id
    
	@DatabaseField(canBeNull = false)
    private String yardName;
    
    @DatabaseField(canBeNull = true)
	private String location;
	
    @DatabaseField(canBeNull = true, foreign = true, foreignAutoCreate = true) 
	private UserObject userID;
    
    @DatabaseField(canBeNull = false)
	private boolean synced;
	
    //NOT A DATABASE FIELD, only database field on the client side
    private int serverSideID;
    
	public YardObject() {
    }	
	
	public YardObject(int id) {
		this.id = id;
    }
	
	
	public YardObject(String yardName, String location, UserObject userID, boolean synced) {
		this.yardName = yardName;
		this.location = location;
		this.userID = userID;
		this.synced = synced;
	}
	
	public YardObject(int id, String yardName, String location, UserObject userID, boolean synced) {
		this.id = id;
		this.yardName = yardName;
		this.location = location;
		this.userID = userID;
		this.synced = synced;
	}

	public YardObject(String yardName, String location, int userID, boolean synced) {
		this.yardName = yardName;
		this.location = location;
		this.userID = new UserObject(userID, "", "");
		this.synced = synced;
	}
	
	public YardObject(int id, String yardName, String location, int userID, boolean synced) {
		this.id = id;
		this.yardName = yardName;
		this.location = location;
		this.userID = new UserObject(userID, "", "");
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

