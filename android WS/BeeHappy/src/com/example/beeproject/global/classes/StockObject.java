package com.example.beeproject.global.classes;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "stocks")
public class StockObject {

	@DatabaseField(generatedId = true, allowGeneratedIdInsert=true)
	private int id;
	
	@DatabaseField(canBeNull = false)
	private String numberOfFrames;
	
	public StockObject(){
	}
	
	public StockObject(int id, String numberOfFrames) {
		super();
		this.id = id;
		this.numberOfFrames = numberOfFrames;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNumberOfFrames() {
		return numberOfFrames;
	}

	public void setNumberOfFrames(String numberOfFrames) {
		this.numberOfFrames = numberOfFrames;
	}

	
}
