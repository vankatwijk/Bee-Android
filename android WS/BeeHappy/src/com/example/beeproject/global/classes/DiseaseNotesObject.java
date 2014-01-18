package com.example.beeproject.global.classes;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "diseasenotes")
public class DiseaseNotesObject {
	
	@DatabaseField(generatedId = true)
    private int id;
	
	@DatabaseField(canBeNull = true, foreign = true, foreignAutoCreate = true)
	private OutbrakeObject outbrakeID;
	
	@DatabaseField(canBeNull = false)
	private String date;
	
	@DatabaseField(canBeNull = false)
	private String description;
	
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
	
	
}
