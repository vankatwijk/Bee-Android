package com.example.beeproject.global.classes;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;



@DatabaseTable(tableName = "users")
public class UserObject {
	
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
}
