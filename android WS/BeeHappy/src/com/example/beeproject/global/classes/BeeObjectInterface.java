package com.example.beeproject.global.classes;


/**
 * Interface, which all domain objects must implement in order to:
 *  allow gson to convertion in the right way
 * 	provide DB table name from the class
 * @author Olya
 *
 */
public interface BeeObjectInterface {
	/**
	 * @return name of the table in DB created by ormLite, the same as marked by @DatabaseTable(tableName = "...")
	 */
	public String getDBTableName();
	
	/**
	 * @return id in the database, all objects must have integer id
	 */
	public int getId();
	
	/**
	 * @return true if the object is synced to server, otherwise false
	 */
	public boolean isSynced();

	/**
	 * @param synced==true if the synchronization to server was performed
	 */
	public void setSynced(boolean synced);
	
	/**
	 * @return id of this object stored in the server side
	 */
	public int getServerSideID();

	/**
	 * @param id of this object stored in the server side
	 */
	public void setServerSideID(int serverSideID);
}
