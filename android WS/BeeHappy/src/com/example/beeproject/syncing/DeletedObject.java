package com.example.beeproject.syncing;

import com.example.beeproject.global.classes.BeeObjectInterface;
import com.j256.ormlite.field.DatabaseField;

/**
 * Class representing deleted object.
 * <p>After an object that needs to be syncronised to the server database is deleted from the local database
 * A DeletedObject must be created to store the information about the deleted object 
 * to be syncronised to the server. <br> 
 * After the syncronisation is successful, the DeletedObject should be deleted from local DB.
 * 
 * <p>Objects of this class can be persisted to a database using ORMLite
 * <p>THIS VERSION IS ONLY FOR CLIENT SIDE. Server must not have this object class
 * @author rezolya
 *
 */
public class DeletedObject {

	@DatabaseField(generatedId = true)
	private int id; //id in the local db

	@DatabaseField(canBeNull = false)
	private String objectClassName;  //name of the class of the deleted object

	@DatabaseField(canBeNull = false)
	private int serverSideId; //id of the deleted object on the server side
	
	public DeletedObject(BeeObjectInterface deletedObject){
		this.objectClassName = deletedObject.getClass().getName();
		this.serverSideId = deletedObject.getServerSideID();
	}
}
