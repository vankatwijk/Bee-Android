package com.example.beeproject.syncing;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.util.Log;

import com.example.beeproject.commandexecution.commands.BeeCommand;
import com.example.beeproject.commandexecution.commands.CreateCommand;
import com.example.beeproject.commandexecution.commands.PingCommand;
import com.example.beeproject.commandexecution.commands.UpdateCommand;
import com.example.beeproject.commandexecution.results.BeeCommandResult;
import com.example.beeproject.commandexecution.results.BeeCommandResultType;
import com.example.beeproject.commandexecution.results.CreateCommandResult;
import com.example.beeproject.commandexecution.results.ErrorResult;
import com.example.beeproject.commandexecution.results.UpdateCommandResult;
import com.example.beeproject.global.classes.BeeObjectClasses;
import com.example.beeproject.global.classes.BeeObjectInterface;
import com.example.beeproject.global.classes.CheckFormObject;
import com.example.beeproject.global.classes.DatabaseHelper;
import com.example.beeproject.global.classes.DatabaseManager;
import com.example.beeproject.global.classes.DiseaseObject;
import com.example.beeproject.global.classes.HiveObject;
import com.example.beeproject.global.classes.UserObject;
import com.example.beeproject.global.classes.YardObject;
import com.example.beeproject.gsonconvertion.GsonProvider;
import com.google.gson.Gson;
import com.j256.ormlite.dao.RuntimeExceptionDao;

public class SyncHelper {
	public final String LOG_TAG ="SyncHelper";
	
	DatabaseManager dbManager;
	DatabaseHelper db;
	Gson gson; 
	
	public SyncHelper(Activity context){
		dbManager = new DatabaseManager();
		db = dbManager.getHelper(context);
		gson = GsonProvider.getGson();
	}
	
	/**
	 * Method implementing the syncronisation to server:
	 * all changes to the objects that need to be syncronised are syncronised to the server:
	 * created, updated or deleted 
	 * @return
	 */
	public String syncronizeToServer(){
		
		//createSomeStuff();
		//updateSomeStuff();
		
		String result = "";
		BeeCommand pingCommand = new PingCommand();
		BeeCommandResult pingResult = BeeServerHttpClient.executeCommand(pingCommand);
    	//Log.d(LOG_TAG, pingResult.toString());
		if(pingResult != null && pingResult.getCommandResultType() == BeeCommandResultType.SUCCESS){

			Class[] classesToSync = BeeObjectClasses.getClassesToSyncList();
			
			for (Class objectClass : classesToSync){
		    	//Log.d(LOG_TAG, objectClass.toString());
		    	syncronizeClass(objectClass);
			}
			
			result = "syncronizeToServer finished";
		}
		else{
			result = "Cannot connect to BeeHappy server.";
		}
		
		return result;
	}
	
	/**
	 * Helper method to syncronise all unsincronised objects of the given class 
	 * and all the deleted objects as well.
	 * @param objectClass
	 * @return
	 */
	public int syncronizeClass(Class objectClass){
		try {
			RuntimeExceptionDao<? super BeeObjectInterface, Integer> objectClassDao = db.getObjectClassRunDao(objectClass);
			Map<String, Object> fieldValues = new HashMap<String, Object>();
			fieldValues.put("synced", false);
			List<BeeObjectInterface> notSyncedObjects = (List<BeeObjectInterface>) objectClassDao.queryForFieldValues(fieldValues);

	    	Log.d(LOG_TAG, "notSyncedObjects"+ notSyncedObjects.toString());
	    	
	    	for(BeeObjectInterface objToSync: notSyncedObjects){

		    	syncronizeObject(objectClass, objectClassDao, objToSync);
		    	
		    	
	    	}

	    	//TODO:implement deleting
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return 0;
		
	}

	/**
	 * Helper method to synchronize object to the server:
	 * either create - if objToSync.serversideId==0. 
	 * 					After creating, ID assigned by server on the Server side must be saved for future reverence
	 * or update - objToSync.serversideId!=0
	 * @param objectClass
	 * @param objectClassDao
	 * @param objToSync
	 */
	private void syncronizeObject(Class objectClass,
			RuntimeExceptionDao<? super BeeObjectInterface, Integer> objectClassDao, BeeObjectInterface objToSync) {
		//Log.d(LOG_TAG, "syncronizeObject: "+ objToSync.toString());

		if(objToSync.getServerSideID()==0){
			//object needs to be added to server DB
			BeeCommand command = new CreateCommand(objectClass.getName(), gson.toJson(objToSync,objectClass));
			BeeCommandResult commandResult = BeeServerHttpClient.executeCommand(command);
			//Log.d(LOG_TAG, "createResult"+ commandResult.toString());
			
			if(commandResult.getCommandResultType()==BeeCommandResultType.SUCCESS){
				objToSync.setSynced(true);
				String resultObjJson = ((CreateCommandResult)commandResult).getObjectJson();
				//Log.d(LOG_TAG, "resultObjJson"+ resultObjJson.toString());
				
				BeeObjectInterface resultObj = gson.fromJson(resultObjJson, BeeObjectInterface.class);
				int serverSideID = resultObj.getId(); //ID assigned by server
				objToSync.setServerSideID(serverSideID);
				//Log.d(LOG_TAG, "objToSync: \n"+objToSync);
				objectClassDao.update(objToSync);
			}
			else{
				String errorMessage = getErrorMessageFromResult(commandResult);
				Log.e(LOG_TAG, errorMessage + " " + objToSync.toString());
			}
		}
		else{
			//object needs to be updated to server DB
			BeeCommand command = new UpdateCommand(objectClass.getName(), gson.toJson(objToSync,objectClass));
			BeeCommandResult commandResult = BeeServerHttpClient.executeCommand(command);
			//Log.d(LOG_TAG, "commandResult"+ commandResult.toString());
			
			if(commandResult.getCommandResultType()==BeeCommandResultType.SUCCESS){
				objToSync.setSynced(true);
				String resultObjJson = ((UpdateCommandResult)commandResult).getObjectJson();
				//Log.d(LOG_TAG, "resultObjJson"+ resultObjJson.toString());
				objectClassDao.update(objToSync);
			}
			else{
				String errorMessage = getErrorMessageFromResult(commandResult);
				Log.e(LOG_TAG, errorMessage + " " + objToSync.toString());
			}
		}
	}
	
	/**
	 * Stores the information on the deleted object in the local database.
	 * @param deletedObject
	 * @return
	 */
	public int storeDeletedObjectForSyncronisation(BeeObjectInterface object){
		//DeletedObject deletedObject = new DeletedObject(object);
		
		return 0;
	}
	
	public String getErrorMessageFromResult(BeeCommandResult commandResult){
		String errorMessage = "Something went wrong while synchronising";
		if(commandResult.getCommandResultType()==BeeCommandResultType.ERROR){
			errorMessage = ((ErrorResult)commandResult).getExceptionMessage();
		}
		return errorMessage;
	}
	
	/**
	 * this method is only for testing
	 */
	public void createSomeStuff(){
		Class[] classesToSync = BeeObjectClasses.getClassesToSyncList();
		try {
			for (Class objectClass : classesToSync){
				createSomething(objectClass);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * this method is only for testing
	 * @param objectClass
	 * @throws SQLException
	 */
	public void createSomething(Class objectClass) throws SQLException{
		//Create objects to work with
		BeeObjectInterface object = null;
		
		Log.d(LOG_TAG, "Create something("+objectClass.getSimpleName());
		
		if(objectClass.getSimpleName().equals("YardObject")){
			object = new YardObject("new yard", "my location", 1, false);
		}
		else if(objectClass.getSimpleName().equals("HiveObject")){
			object = new HiveObject("new hive", 1, false);
		}
		else if(objectClass.getSimpleName().equals("UserObject")){
			object = new UserObject("new username", "new password");
		}
		else if(objectClass.getSimpleName().equals("CheckFormObject")){
			object = new CheckFormObject(1, (new Date()).getTime(), true, "26.05.2013", false, "normal queen",
					10, 10, 1, 500, 300, 300, 0, 10, "new check!", false);
		}
		
		if(object!=null){
			RuntimeExceptionDao<? super BeeObjectInterface, Integer> objectClassDao = db.getObjectClassRunDao(objectClass);
			int nrInsertedRows = objectClassDao.create(object);
			if(nrInsertedRows==1){
				Log.d(LOG_TAG, "inserted: " + object);
			}
			else{
				Log.d(LOG_TAG, "couldnt insert: " + object);
			}
		}
		else{
			Log.d(LOG_TAG, "Didnt create anything...");
		}
	}
	
	/**
	 * this method is only for testing
	 */
	public void updateSomeStuff(){
		Class[] classesToSync = BeeObjectClasses.getClassesToSyncList();
		try {
			for (Class objectClass : classesToSync){
				updateSomething(objectClass);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * this method is only for testing
	 * @param objectClass
	 * @throws SQLException
	 */
	public void updateSomething(Class objectClass) throws SQLException{
		//Create objects to work with
		BeeObjectInterface object = null;
		
		Log.d(LOG_TAG, "Update something("+objectClass.getSimpleName());
		
		if(objectClass.getSimpleName().equals("YardObject")){
			object = new YardObject(1, "updated yard", "updated location", 1, false);
			object.setServerSideID(1);
		}
		else if(objectClass.getSimpleName().equals("HiveObject")){
			object = new HiveObject(1, "updated hive", 1, false);
			object.setServerSideID(1);
		}
		else if(objectClass.getSimpleName().equals("UserObject")){
			object = new UserObject(1, "updated username", "updated password");
			object.setServerSideID(1);
		}
		else if(objectClass.getSimpleName().equals("CheckFormObject")){
			object = new CheckFormObject(1, 1, (new Date()).getTime(), true, "26.05.2013", false, "updated queen",
					10, 10, 1, 500, 300, 300, 0, 10, "updated check!", false);
			object.setServerSideID(1);
		}
		
		if(object!=null){
			RuntimeExceptionDao<? super BeeObjectInterface, Integer> objectClassDao = db.getObjectClassRunDao(objectClass);
			int nrInsertedRows = objectClassDao.update(object);
			if(nrInsertedRows==1){
				Log.d(LOG_TAG, "updated: " + object);
			}
			else{
				Log.d(LOG_TAG, "couldnt update: " + object);
			}
		}
		else{
			Log.d(LOG_TAG, "Didnt update anything...");
		}
	}
	
	
	/**
	 * this method is only for testing
	 */
	public void deleteSomeStuff(){
		Class[] classesToSync = BeeObjectClasses.getClassesToSyncList();
		try {
			for (Class objectClass : classesToSync){
				deleteSomething(objectClass);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * this method is only for testing
	 * @param objectClass
	 * @throws SQLException
	 */
	public void deleteSomething(Class objectClass) throws SQLException{
		//Create objects to work with
		BeeObjectInterface object = null;
		
		Log.d(LOG_TAG, "Delete something("+objectClass.getSimpleName());
		
		if(objectClass.getSimpleName().equals("YardObject")){
			object = new YardObject(2);
			object.setServerSideID(1);
		}
		else if(objectClass.getSimpleName().equals("HiveObject")){
			object = new HiveObject(2);
			object.setServerSideID(1);
		}
		else if(objectClass.getSimpleName().equals("UserObject")){
			object = new UserObject(2,"","");
			object.setServerSideID(1);
		}
		else if(objectClass.getSimpleName().equals("CheckFormObject")){
			object = new CheckFormObject(2);
			object.setServerSideID(1);
		}
		
		if(object!=null){
			RuntimeExceptionDao<? super BeeObjectInterface, Integer> objectClassDao = db.getObjectClassRunDao(objectClass);
			int nrInsertedRows = objectClassDao.delete(object);
			if(nrInsertedRows==1){
				Log.d(LOG_TAG, "deleted: " + object);
			}
			else{
				Log.d(LOG_TAG, "couldnt delete: " + object);
			}
		}
		else{
			Log.d(LOG_TAG, "Didnt delete anything...");
		}
	}
	
}
