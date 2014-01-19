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
 * Class representing disease outbreak object.
 * <p>Objects of this class can be persisted to a database using ORMLite
 * <p>THIS VERSION IS ONLY FOR SERVER SIDE. Client must have its own different implementation
 * @author rezolya
 */
@DatabaseTable(tableName = "outbrakes")
public class OutbrakeObject implements BeeObjectInterface{

	@DatabaseField(generatedId = true)
	private int id;
	
	@DatabaseField(canBeNull = true, foreign = true, foreignAutoCreate = true)
	private HiveObject hiveID;
	
	@DatabaseField(canBeNull = true, foreign = true, foreignAutoCreate = true)
	private DiseaseObject diseaseID;
	
	@DatabaseField(canBeNull = false)
	private String startDate;
	
	@DatabaseField(canBeNull = true)
	private String endDate;
	
    //NOT A DATABASE FIELD, only database field on the client side
	private boolean synced;
	
    @DatabaseField(canBeNull = true) 
	private boolean deleted; // true if object is considered deleted on the server
	
    //NOT A DATABASE FIELD, only database field on the client side
    private int serverSideID;
    
	public OutbrakeObject(){
	}

	public OutbrakeObject(HiveObject hiveID, DiseaseObject diseaseID,
			String startDate, String endDate, boolean synced) {
		super();
		this.hiveID = hiveID;
		this.diseaseID = diseaseID;
		this.startDate = startDate;
		this.endDate = endDate;
		this.synced = synced;
	}

	public int getID(){
		return id;
	}
	
	public HiveObject getHiveID() {
		return hiveID;
	}

	public void setHiveID(HiveObject hiveID) {
		this.hiveID = hiveID;
	}

	public DiseaseObject getDiseaseID() {
		return diseaseID;
	}

	public void setDiseaseID(DiseaseObject diseaseID) {
		this.diseaseID = diseaseID;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public boolean isSynced() {
		return synced;
	}

	public void setSynced(boolean synced) {
		this.synced = synced;
	}

	@Override
	public String getDBTableName() {
		return "outbrakes";
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public boolean isDeleted() {
		return deleted;
	}

	@Override
	public void setDeleted(boolean deleted) {
		this.deleted=deleted;
	}

	@Override
	public int getServerSideID() {
		return serverSideID;
	}

	@Override
	public void setServerSideID(int serverSideID) {
		this.serverSideID = serverSideID;
	}
	
	@Override
	public List<BeeObjectInterface> listChildRelations() throws SQLException {
		List<BeeObjectInterface> result = new ArrayList<BeeObjectInterface>();
		
		Class[] listChildObjectClasses = new Class[] { DiseaseNotesObject.class };
		@SuppressWarnings("serial")
		Map<String, String> tablenames = new HashMap<String, String>(){{
			put("DiseaseNotesObject", "diseasenotes");
		}};

		String fieldName = "\"outbrakeID_id\"";
		
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
