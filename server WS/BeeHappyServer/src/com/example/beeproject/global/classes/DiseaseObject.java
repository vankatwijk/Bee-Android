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
 * Class representing disease object.
 * THIS VERSION IS ONLY FOR SERVER SIDE. Client must have its own different implementation
 * @author rezolya
 *
 */
@DatabaseTable(tableName = "diseases")
public class DiseaseObject implements BeeObjectInterface{

	@DatabaseField(generatedId = true)
    private int id;
	
	@DatabaseField(canBeNull = false)
    private String diseaseName;
	
	@DatabaseField(canBeNull = false, width = 1000)
    private String description;
	
	@DatabaseField(canBeNull = false, width = 1000)
    private String treatment;
	
	@DatabaseField(canBeNull = false)
	private boolean contagious;

    //NOT A DATABASE FIELD, only database field on the client side
	private boolean synced;

    @DatabaseField(canBeNull = true) 
	private boolean deleted; // true if object is considered deleted on the server
    
    //NOT A DATABASE FIELD, only database field on the client side
    private int serverSideID;
    
	public DiseaseObject(){
	}
	
	public DiseaseObject(int id){
		this.id= id;
	}

	public DiseaseObject(String diseaseName, String description,
			String treatment, boolean contagious) {
		super();
		this.diseaseName = diseaseName;
		this.description = description;
		this.treatment = treatment;
		this.contagious = contagious;
	}

	public DiseaseObject(int id, String diseaseName, String description,
			String treatment, boolean contagious) {
		super();
		this.id= id;
		this.diseaseName = diseaseName;
		this.description = description;
		this.treatment = treatment;
		this.contagious = contagious;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDiseaseName() {
		return diseaseName;
	}

	public void setDiseaseName(String diseaseName) {
		this.diseaseName = diseaseName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTreatment() {
		return treatment;
	}

	public void setTreatment(String treatment) {
		this.treatment = treatment;
	}

	public boolean isContagious() {
		return contagious;
	}

	public void setContagious(boolean contagious) {
		this.contagious = contagious;
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
		return "diseases";
	}

	@Override
	public String toString() {
		return "DiseaseObject [id=" + id + ", diseaseName=" + diseaseName
				+ ", description=" + description + ", treatment=" + treatment
				+ ", contagious=" + contagious + "]";
	}
	
	@Override
	public List<BeeObjectInterface> listChildRelations() throws SQLException {
		List<BeeObjectInterface> result = new ArrayList<BeeObjectInterface>();
		
		Class[] listChildObjectClasses = new Class[] { OutbrakeObject.class };
		@SuppressWarnings("serial")
		Map<String, String> tablenames = new HashMap<String, String>(){{
			put("OutbrakeObject", "outbrakes");
		}};

		String fieldName = "\"diseaseID_id\"";
		
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
