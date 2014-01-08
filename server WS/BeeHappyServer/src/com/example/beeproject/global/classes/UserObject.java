package com.example.beeproject.global.classes;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


/**
 * Class representing user object.
 * <p>Objects of this class can be persisted to a database using ORMLite
 * <p>THIS VERSION IS ONLY FOR SERVER SIDE. Client must have its own different implementation
 * @author rezolya
 *
 */
@DatabaseTable(tableName = "users")
public class UserObject implements BeeObjectInterface{
	
	@DatabaseField(generatedId = true)
    private int id;
    
    @DatabaseField(canBeNull = false)
    private String username;
    
    @DatabaseField(canBeNull = false)
    private String password;

    //NOT A DATABASE FIELD, only database field on the client side
	private boolean synced;
	
    //NOT A DATABASE FIELD, only database field on the client side
    private int serverSideID;

    @DatabaseField(canBeNull = true) 
	private boolean deleted; // true if object is considered deleted on the server
    
	public UserObject(){
	}
	
	public UserObject(String username, String password){
		this.username = username;
		this.password = password;
	}
	
	public UserObject(int id, String username, String password){
		this.id = id;
		this.username = username;
		this.password = password;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getUsername(){
		return username;
	}
	
	public void setUsername(String username){
		this.username = username;
	}
	
	public String getPassword(){
		return password;
	}
	
	public void setPassword(String password){
		this.password = password;
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
	public boolean isDeleted() {
		return deleted;
	}

	@Override
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	
	@Override
	public String getDBTableName() {
		return "users";
	}

	@Override
	public String toString() {
		return "UserObject [id=" + id + ", username=" + username
				+ ", password=" + password + "]";
	}
	
	
}
