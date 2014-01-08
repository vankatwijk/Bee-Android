package com.example.beeproject.syncing;

import com.example.beeproject.global.classes.BeeObjectInterface;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Class representing deleted object.
 * <p>On the client side: After an object that needs to be syncronised to the server database is deleted from the local database
 * A DeletedObject stores the information about the deleted object 
 * 
 * <p>THIS VERSION IS ONLY FOR SERVER SIDE. Client must have its own different implementation
 * @author rezolya
 *
 */
public class DeletedObject {

	private int id; //id in the local db

	private String objectClassName;  //name of the class of the deleted object

	private int serverSideId; //id of the deleted object on the server side
	
	public String getObjectClassName() {
		return objectClassName;
	}

	public void setObjectClassName(String objectClassName) {
		this.objectClassName = objectClassName;
	}

	public int getServerSideId() {
		return serverSideId;
	}

	public void setServerSideId(int serverSideId) {
		this.serverSideId = serverSideId;
	}

	
	/**
	 * Default no-arg constructor
	 * required by ORMLite
	 */
	public DeletedObject(){
		
	}
	
	public DeletedObject(BeeObjectInterface deletedObject){
		this.objectClassName = deletedObject.getClass().getName();
		this.serverSideId = deletedObject.getServerSideID();
	}
	
	@Override
	public String toString() {
		return "DeletedObject [id=" + id + ", objectClassName=" + objectClassName
				+ ", serverSideId=" + serverSideId + " ]";
	}
}
