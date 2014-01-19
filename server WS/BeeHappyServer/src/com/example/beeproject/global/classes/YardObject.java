
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
 * Class representing yard object.
 * <p>Objects of this class can be persisted to a database using ORMLite
 * <p>THIS VERSION IS ONLY FOR SERVER SIDE. Client must have its own different implementation
 * @author rezolya
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

    //NOT A DATABASE FIELD, only database field on the client side
	private boolean synced;
	
    @DatabaseField(canBeNull = true) 
	private boolean deleted; // true if object is considered deleted on the server
	
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

	public void setId(int id) {
		this.id = id;
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
	
	public static String getDBTableNameStatic() {
		return "yards";
	}
	
	@Override
	public String toString() {
		return "YardObject [id=" + id + ", yardName=" + yardName
				+ ", location=" + location + ", userID=" + userID + ", synced="
				+ synced + ", serverSideID="+serverSideID+ "]";
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
	public List<BeeObjectInterface> listChildRelations() throws SQLException {
		List<BeeObjectInterface> result = new ArrayList<BeeObjectInterface>();
		
		Class[] listChildObjectClasses = new Class[] { HiveObject.class };
		@SuppressWarnings("serial")
		Map<String, String> tablenames = new HashMap<String, String>(){{
			put("HiveObject", "hives");
		}};

		String fieldName = "\"yardID_id\"";
		
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

