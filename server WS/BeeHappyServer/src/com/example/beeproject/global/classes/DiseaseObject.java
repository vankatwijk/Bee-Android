package com.example.beeproject.global.classes;

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
	
	@DatabaseField(canBeNull = false)
    private String description;
	
	@DatabaseField(canBeNull = false)
    private String treatment;
	
	@DatabaseField(canBeNull = false)
	private boolean contagious;

    //NOT A DATABASE FIELD, only database field on the client side
	private boolean synced;

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
	public String getDBTableName() {
		return "diseases";
	}

	@Override
	public String toString() {
		return "DiseaseObject [id=" + id + ", diseaseName=" + diseaseName
				+ ", description=" + description + ", treatment=" + treatment
				+ ", contagious=" + contagious + "]";
	}
	
	
	
}
