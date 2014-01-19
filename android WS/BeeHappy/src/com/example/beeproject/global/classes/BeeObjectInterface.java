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
	 * @return id of this object stored in the server side
	 */
	public int getServerSideID();

	/**
	 * @param id of this object stored in the server side
	 */
	public void setServerSideID(int serverSideID);
	
	/**
	 * This method must refresh all the foreign object fields of the implementing class.
	 * This is needed in order to pass to the server side an serverSideIDs of the related objects
	 * Without refreshing, only the local id is passed
	 * @param db 
	 * @return
	 */
	public int refresh(DatabaseHelper db);
	
	/**
	 * This object must return server side represenation of this object,
	 * with server side ids of foreign objects (f.e. UserObject is foreign object for YardObject)
	 * i.e. serverSideObject.id = this.serverSideID,
	 * 		serverSideObject.foreignObject = new ForeignObject(this.foreignObject.serverSideID)
	 * @return
	 */
	public BeeObjectInterface getServerSideObject(DatabaseHelper db);
}
