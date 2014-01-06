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
	
	public UserObject(){
	}
	
	public UserObject(String username, String password){
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
				+ ", password=" + password + "]";
	}

	@Override
	public boolean isSynced() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setSynced(boolean synced) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getServerSideID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setServerSideID(int serverSideID) {
		// TODO Auto-generated method stub
		
	}
}
