package com.example.beeproject.global.classes;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "diseases")
public class DiseaseObject {

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
	
	public DiseaseObject(){
	}

	public DiseaseObject(String diseaseName, String description,
			String treatment, boolean contagious) {
		super();
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
	
	
	
}
