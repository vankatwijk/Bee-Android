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
 * Class representing hive object.
 * <p>Objects of this class can be persisted to a database using ORMLite
 * <p>THIS VERSION IS ONLY FOR SERVER SIDE. Client must have its own different implementation
 * @author rezolya
 *
 */
@DatabaseTable(tableName = "hives")
public class HiveObject implements BeeObjectInterface{
	
	@DatabaseField(generatedId = true)
	private int id;
	
	@DatabaseField(canBeNull = false)
	private String hiveName;
	
	@DatabaseField(canBeNull = true, foreign = true, foreignAutoCreate = true)
	private YardObject yardID;

    //NOT A DATABASE FIELD, only database field on the client side
	private boolean synced;

    @DatabaseField(canBeNull = true) 
	private boolean deleted; // true if object is considered deleted on the server
    
    //NOT A DATABASE FIELD, only database field on the client side
    private int serverSideID;
	
	public HiveObject(){
	}

	public HiveObject(int id){
		this.id = id;
	}
	

	public HiveObject(String hiveName, YardObject yardId, boolean synced){
		this.hiveName = hiveName;
		this.yardID = yardId;
		this.synced = synced;
	}
	
	public HiveObject(String hiveName, int yardId, boolean synced){
		this.hiveName = hiveName;
		this.yardID = new YardObject(yardId);
		this.synced = synced;
	}	
	
	public HiveObject(int id, String hiveName, int yardId, boolean synced){
		this.id=id;
		this.hiveName = hiveName;
		this.yardID = new YardObject(yardId);
		this.synced = synced;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public String getHiveName() {
		return hiveName;
	}

	public void setHiveName(String hiveName) {
		this.hiveName = hiveName;
	}

	public YardObject getYardId() {
		return yardID;
	}

	public void setYardId(YardObject yardId) {
		this.yardID = yardId;
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
		return "hives";
	}
	
	public static String getDBTableNameStatic() {
		return "hives";
	}

	@Override
	public String toString() {
		return "HiveObject [id=" + id + ", hiveName=" + hiveName + ", yardID="
				+ yardID + ", synced=" + synced + "]";
	}

	@Override
	public List<BeeObjectInterface> listChildRelations() throws SQLException {
		List<BeeObjectInterface> result = new ArrayList<BeeObjectInterface>();
		
		Class[] listChildObjectClasses = new Class[] { CheckFormObject.class, OutbrakeObject.class };
		@SuppressWarnings("serial")
		Map<String, String> tablenames = new HashMap<String, String>(){{
			put("CheckFormObject", "checkforms");
			put("OutbrakeObject", "outbrakes");
		}};

		String fieldName = "\"hiveID_id\"";
		
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
