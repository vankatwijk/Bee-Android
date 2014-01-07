package com.example.beeproject.global.classes;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "outbrakes")
public class OutbrakeObject {

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
	
	@DatabaseField(canBeNull = false)
	private boolean synced;
	
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
	
	
}
