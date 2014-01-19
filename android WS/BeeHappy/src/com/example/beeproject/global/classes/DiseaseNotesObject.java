package com.example.beeproject.global.classes;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Class representing notes on the disease outbreak object.
 * <p>Objects of this class can be persisted to a database using ORMLite
 * <p>THIS VERSION IS ONLY FOR CLIENT SIDE. Server must have its own different implementation
 *
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
	
	public DiseaseNotesObject(OutbrakeObject outbrakeID, String date,
			String description, boolean synced, int serverSideID) {
		super();
		this.outbrakeID = outbrakeID;
		this.date = date;
		this.description = description;
		this.synced = synced;
		this.serverSideID = serverSideID;
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
		this.id = id;
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
	public int getServerSideID() {
		return serverSideID;
	}

	@Override
	public void setServerSideID(int serverSideID) {
		this.serverSideID = serverSideID;
	}

	@Override
	public int refresh(DatabaseHelper db) {
		RuntimeExceptionDao<OutbrakeObject, Integer> outbrakeDao = db.getOutbrakeRunDao();
		outbrakeDao.refresh(this.outbrakeID);
		return 0;
	}

	@Override
	public BeeObjectInterface getServerSideObject(DatabaseHelper db) {
		DiseaseNotesObject serverSideObject = new DiseaseNotesObject(outbrakeID, date, description, synced, serverSideID);
		serverSideObject.setId(serverSideID); 
		
		serverSideObject.refresh(db);
		serverSideObject.setOutbrakeID(new OutbrakeObject(serverSideObject.getOutbrakeID().getServerSideID()));
		
		return serverSideObject;
	}
	
	
}
