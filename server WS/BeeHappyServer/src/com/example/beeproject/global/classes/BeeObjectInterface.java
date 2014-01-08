package com.example.beeproject.global.classes;

import com.j256.ormlite.table.DatabaseTable;

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
	 * @param id in the database, all objects must have integer id
	 */
	public void setId(int id);
	
	/**
	 * @return true if the object is synced to server, otherwise false
	 */
	public boolean isSynced();

	/**
	 * @param synced==true if the synchronization to server was performed
	 */
	public void setSynced(boolean synced);
	
	/**
	 * @return true if the object is considered deleted on the server, otherwise false
	 */
	public boolean isDeleted();

	/**
	 * @param deleted==true if object is considered deleted on the server
	 */
	public void setDeleted(boolean deleted);
	
	/**
	 * @return id of this object stored in the server side
	 */
	public int getServerSideID();

	/**
	 * @param id of this object stored in the server side
	 */
	public void setServerSideID(int serverSideID);
}
