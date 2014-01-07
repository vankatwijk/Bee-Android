package com.example.beeproject.global.classes;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;



@DatabaseTable(tableName = "users")
public class UserObject implements BeeObjectInterface{
	
	@DatabaseField(generatedId = true)
    private int id;
    
    @DatabaseField(canBeNull = false)
    private String username;
    
    @DatabaseField(canBeNull = false)
    private String password;

    @DatabaseField(canBeNull = false)
	private boolean synced;
    
    @DatabaseField(canBeNull = true)
    private int serverSideID;
	
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

	@Override
	public String getDBTableName() {
		return "users";
	}
	
	@Override
	public String toString() {
		return "UserObject [id=" + id + ", username=" + username
				+ ", password=" + password + ", synced=" + synced + ", serverSideID="+serverSideID+" ]";
	}

	@Override
	public boolean isSynced() {
		return synced;
	}
	
	@Override
	public void setSynced(boolean synced) {
		this.synced = synced;
	}

	public int getServerSideID() {
		return serverSideID;
	}

	public void setServerSideID(int serverSideID) {
		this.serverSideID = serverSideID;
	}

	
}
