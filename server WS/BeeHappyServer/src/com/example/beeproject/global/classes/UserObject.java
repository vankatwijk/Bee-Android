package com.example.beeproject.global.classes;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.beeproject.db.ConnectionProvider;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.GenericRawResults;
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
				+ ", password=" + password + ", serverSideID="+serverSideID+"]";
	}

	@Override
	public List<BeeObjectInterface> listChildRelations() throws SQLException {
		List<BeeObjectInterface> result = new ArrayList<BeeObjectInterface>();
		
		Class[] listChildObjectClasses = new Class[] { YardObject.class };
		@SuppressWarnings("serial")
		Map<String, String> tablenames = new HashMap<String, String>(){{
			put("YardObject", "yards");
		}};
		
		String fieldName = "\"userID_id\"";
		
		for(Class childObjectClass : listChildObjectClasses){
			String tablename = tablenames.get(childObjectClass.getSimpleName());
			if(tablename!=null){
				String queryString = "SELECT * FROM " + tablename + " WHERE " + "( " + fieldName + " = " + id + ")";
				
				Dao<? super BeeObjectInterface, Integer> childObjectClassDao = DaoManager.createDao(ConnectionProvider.getConnectionSource(), childObjectClass);
				GenericRawResults<? super BeeObjectInterface> selectedResult = childObjectClassDao.queryRaw(queryString, childObjectClassDao.getRawRowMapper());
				List<BeeObjectInterface> childrenOfChildObjectClass = (List<BeeObjectInterface>) selectedResult.getResults();
				
				result.addAll(childrenOfChildObjectClass);
			}
		}
		
		return result;
	}
	
	
}
