package com.example.beeproject.global.classes;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "hives")
public class HiveObject {
	
	@DatabaseField(generatedId = true)
	private int id;
	
	@DatabaseField(canBeNull = false)
	private String hiveName;
	
	@DatabaseField(canBeNull = true, foreign = true, foreignAutoCreate = true)
	private YardObject yardID;
	
	@DatabaseField(canBeNull = false)
	private boolean synced;
	
	
	public HiveObject(){
	}
	
	

	public HiveObject(String hiveName, YardObject yardId, boolean synced){
		this.hiveName = hiveName;
		this.yardID = yardId;
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


}
