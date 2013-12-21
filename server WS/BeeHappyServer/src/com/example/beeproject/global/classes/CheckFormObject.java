package com.example.beeproject.global.classes;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "checkforms")
public class CheckFormObject {

	@DatabaseField(generatedId = true)
	private int id;
	
	@DatabaseField(canBeNull = true, foreign = true, foreignAutoCreate = true)
	private HiveObject hiveID;
	
	@DatabaseField(canBeNull = false)
	private Long timestamp;
	
	@DatabaseField(canBeNull = false)
	private boolean hasQueen;
	
	@DatabaseField(canBeNull = true)
	private String qDateBorn;
	
	@DatabaseField(canBeNull = true)
	private boolean qWingsCliped;
	
	@DatabaseField(canBeNull = true)
	private String qRace;
	
	@DatabaseField(canBeNull = false)
	private int nrOfFrames;
	
	@DatabaseField(canBeNull = false)
	private int occupiedFrames;
	
	@DatabaseField(canBeNull = false)
	private int nrOfLayers;
	
	@DatabaseField(canBeNull = false)
	private int eggs;
	
	@DatabaseField(canBeNull = false)
	private int larve;
	
	@DatabaseField(canBeNull = false)
	private int pupae;
	
	@DatabaseField(canBeNull = false)
	private int nrOfMites;
	
	@DatabaseField(canBeNull = false)
	private int honeyCombs;
	
	@DatabaseField(canBeNull = true)
	private String comments;
	
	@DatabaseField(canBeNull = false)
	private boolean synced;

	public CheckFormObject(){	
	}
	
	
	
	public CheckFormObject(HiveObject hiveID, Long timestamp, boolean hasQueen,
			String qDateBorn, boolean qWingsCliped, String qRace,
			int nrOfFrames, int occupiedFrames, int nrOfLayers, int eggs,
			int larve, int pupae, int nrOfMites, int honeyCombs,
			String comments, boolean synced) {
		super();
		this.hiveID = hiveID;
		this.timestamp = timestamp;
		this.hasQueen = hasQueen;
		this.qDateBorn = qDateBorn;
		this.qWingsCliped = qWingsCliped;
		this.qRace = qRace;
		this.nrOfFrames = nrOfFrames;
		this.occupiedFrames = occupiedFrames;
		this.nrOfLayers = nrOfLayers;
		this.eggs = eggs;
		this.larve = larve;
		this.pupae = pupae;
		this.nrOfMites = nrOfMites;
		this.honeyCombs = honeyCombs;
		this.comments = comments;
		this.synced = synced;
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public HiveObject getHiveID() {
		return hiveID;
	}

	public void setHiveID(HiveObject hiveID) {
		this.hiveID = hiveID;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public boolean isHasQueen() {
		return hasQueen;
	}

	public void setHasQueen(boolean hasQueen) {
		this.hasQueen = hasQueen;
	}

	public String getqDateBorn() {
		return qDateBorn;
	}

	public void setqDateBorn(String qDateBorn) {
		this.qDateBorn = qDateBorn;
	}

	public boolean isqWingsCliped() {
		return qWingsCliped;
	}

	public void setqWingsCliped(boolean qWingsCliped) {
		this.qWingsCliped = qWingsCliped;
	}

	public String getqRace() {
		return qRace;
	}

	public void setqRace(String qRace) {
		this.qRace = qRace;
	}

	public int getNrOfFrames() {
		return nrOfFrames;
	}

	public void setNrOfFrames(int nrOfFrames) {
		this.nrOfFrames = nrOfFrames;
	}

	public int getOccupiedFrames() {
		return occupiedFrames;
	}

	public void setOccupiedFrames(int occupiedFrames) {
		this.occupiedFrames = occupiedFrames;
	}

	public int getNrOfLayers() {
		return nrOfLayers;
	}

	public void setNrOfLayers(int nrOfLayers) {
		this.nrOfLayers = nrOfLayers;
	}

	public int getEggs() {
		return eggs;
	}

	public void setEggs(int eggs) {
		this.eggs = eggs;
	}

	public int getLarve() {
		return larve;
	}

	public void setLarve(int larve) {
		this.larve = larve;
	}

	public int getPupae() {
		return pupae;
	}

	public void setPupae(int pupae) {
		this.pupae = pupae;
	}

	public int getNrOfMites() {
		return nrOfMites;
	}

	public void setNrOfMites(int nrOfMites) {
		this.nrOfMites = nrOfMites;
	}

	public int getHoneyCombs() {
		return honeyCombs;
	}

	public void setHoneyCombs(int honeyCombs) {
		this.honeyCombs = honeyCombs;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public boolean isSynced() {
		return synced;
	}

	public void setSynced(boolean synced) {
		this.synced = synced;
	}
	
	
	
}
