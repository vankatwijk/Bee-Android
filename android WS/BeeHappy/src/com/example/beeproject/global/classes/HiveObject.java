package com.example.beeproject.global.classes;

public class HiveObject {
	
	private int id;
	private String hiveName;
	private int yardId;
	private int queenId;
	private int nrOfFrames;
	private int occupiedFrames;
	private int eggs;
	private int larve;
	private int pupae;
	private int nrOfMites;
	
	public HiveObject(){
	}
	
	public HiveObject(int id, String hiveName, int yardId, int queenId, int nrOfFrames, int occupiedFrames, int eggs, int larve, int pupae, int nrOfMites){
		this.id = id;
		this.hiveName = hiveName;
		this.yardId = yardId;
		this.queenId = queenId;
		this.nrOfFrames = nrOfFrames;
		this.occupiedFrames = occupiedFrames;
		this.eggs = eggs;
		this.larve = larve;
		this.pupae = pupae;
		this.nrOfMites = nrOfMites;
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

	public int getYardId() {
		return yardId;
	}

	public void setYardId(int yardId) {
		this.yardId = yardId;
	}

	public int getQueenId() {
		return queenId;
	}

	public void setQueenId(int queenId) {
		this.queenId = queenId;
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
	


}
