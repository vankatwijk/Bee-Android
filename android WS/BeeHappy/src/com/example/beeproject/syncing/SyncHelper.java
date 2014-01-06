package com.example.beeproject.syncing;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.util.Log;

import com.example.beeproject.commandexecution.commands.BeeCommand;
import com.example.beeproject.commandexecution.commands.CreateCommand;
import com.example.beeproject.commandexecution.commands.PingCommand;
import com.example.beeproject.commandexecution.results.BeeCommandResult;
import com.example.beeproject.commandexecution.results.BeeCommandResultType;
import com.example.beeproject.commandexecution.results.CreateCommandResult;
import com.example.beeproject.global.classes.BeeObjectClasses;
import com.example.beeproject.global.classes.BeeObjectInterface;
import com.example.beeproject.global.classes.DatabaseHelper;
import com.example.beeproject.global.classes.DatabaseManager;
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
	
	public String syncronizeToServer(){
		String result = "";
		BeeCommand pingCommand = new PingCommand();
		BeeCommandResult pingResult = BeeServerHttpClient.executeCommand(pingCommand);
    	//Log.d(LOG_TAG, pingResult.toString());
		if(pingResult != null && pingResult.getCommandResultType() == BeeCommandResultType.SUCCESS){

			Class[] classesToSync = BeeObjectClasses.getClassesToSyncList();
			
			for (Class objectClass : classesToSync){
		    	Log.d(LOG_TAG, objectClass.toString());
		    	syncronizeClass(objectClass);
			}
			
			result = "syncronizeToServer finished";
		}
		else{
			result = "Cannot connect to BeeHappy server.";
		}
		
		return result;
	}
	
	public int syncronizeClass(Class objectClass){
		// instantiate the dao
		try {
			RuntimeExceptionDao<? super BeeObjectInterface, Integer> objectClassDao = db.getObjectClassRunDao(objectClass);
			Map<String, Object> fieldValues = new HashMap<String, Object>();
			fieldValues.put("synced", false);
			List<BeeObjectInterface> notSyncedObjects = (List<BeeObjectInterface>) objectClassDao.queryForFieldValues(fieldValues);

	    	Log.d(LOG_TAG, "notSyncedObjects"+ notSyncedObjects.toString());
	    	
	    	for(BeeObjectInterface objToSync: notSyncedObjects){

		    	Log.d(LOG_TAG, objToSync.toString());

		    	//Add object to server DB
		    	BeeCommand createCommand = new CreateCommand(objectClass.getName(), gson.toJson(objToSync,objectClass));
				BeeCommandResult createResult = BeeServerHttpClient.executeCommand(createCommand);
		    	Log.d(LOG_TAG, "createResult"+ createResult.toString());
		    	
		    	if(createResult.getCommandResultType()==BeeCommandResultType.SUCCESS){
		    		objToSync.setSynced(true);
		    		String resultObjJson = ((CreateCommandResult)createResult).getObjectJson();
			    	Log.d(LOG_TAG, "resultObjJson"+ resultObjJson.toString());
		    		
		    		BeeObjectInterface resultObj = gson.fromJson(resultObjJson, BeeObjectInterface.class);
		    		int serverSideID = resultObj.getId(); //ID assigned by server
		    		objToSync.setServerSideID(serverSideID);
		    		objectClassDao.update(objToSync);
		    	}
		    	else{
		    		// did not syncronise...
		    	}
		    	
		    	
		    	//if synced==false and serversideId==0, object was updated!!!
		    	//TODO: implement updating
		    	//TODO:implement deleting
	    	}
	    	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return 0;
		
	}
	
	
}
