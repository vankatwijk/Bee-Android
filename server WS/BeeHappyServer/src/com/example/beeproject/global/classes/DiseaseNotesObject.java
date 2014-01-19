package com.example.beeproject.global.classes;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Class representing notes for disease outbreak object.
 * <p>Objects of this class can be persisted to a database using ORMLite
 * <p>THIS VERSION IS ONLY FOR SERVER SIDE. Client must have its own different implementation
 * @author rezolya
 */
@DatabaseTable(tableName = "diseasenotes")
public class DiseaseNotesObject implements BeeObjectInterface{
	
	@DatabaseField(generatedId = true)
    private int id;
	
	@DatabaseField(canBeNull = true, foreign = true, foreignAutoCreate = true)
	private OutbrakeObject outbrakeID;
	
	@DatabaseField(canBeNull = false)
	private String date;
	
	@DatabaseField(canBeNull = false)
	private String description;
	
	@DatabaseField(canBeNull = false)
	private boolean synced;
	
    @DatabaseField(canBeNull = true) 
	private boolean deleted; // true if object is considered deleted on the server
	
    //NOT A DATABASE FIELD, only database field on the client side
    private int serverSideID;
   
	
	public DiseaseNotesObject(){
	}

	public DiseaseNotesObject(OutbrakeObject outbrakeID, String date,
			String description) {
		super();
		this.outbrakeID = outbrakeID;
		this.date = date;
		this.description = description;
	}

	public OutbrakeObject getOutbrakeID() {
		return outbrakeID;
	}

	public void setOutbrakeID(OutbrakeObject outbrakeID) {
		this.outbrakeID = outbrakeID;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String getDBTableName() {
		return "diseasenotes";
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setId(int id) {
		this.id=id;
	}

	@Override
	public boolean isSynced() {
		return synced;
	}

	@Override
	public void setSynced(boolean synced) {
		this.synced = synced;
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
	public int getServerSideID() {
		return serverSideID;
	}

	@Override
	public void setServerSideID(int serverSideID) {
		this.serverSideID = serverSideID;
	}
	
	
}
