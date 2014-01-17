package com.example.beeproject.test;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import java.util.Properties;

import com.example.beeproject.commandexecution.*;
import com.example.beeproject.commandexecution.commands.*;
import com.example.beeproject.commandexecution.results.*;
import com.example.beeproject.db.ConnectionProvider;
import com.example.beeproject.global.classes.BeeObjectInterface;
import com.example.beeproject.global.classes.CheckFormObject;
import com.example.beeproject.global.classes.DiseaseObject;
import com.example.beeproject.global.classes.HiveObject;
import com.example.beeproject.global.classes.UserObject;
import com.example.beeproject.global.classes.YardObject;
import com.example.beeproject.gsonconvertion.GsonProvider;
import com.example.beeproject.utils.PropertiesProvider;
import com.example.beeproject.utils.StringUtils;
import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.GenericRawResults;

public class TestActions {
	private static final Class[] classessToTest = {YardObject.class};//, HiveObject.class, UserObject.class, CheckFormObject.class, DiseaseObject.class};
	
	public static void testORM(PrintWriter responseWriter) throws SQLException{
		String queryString = "SELECT yards.id, count(checkforms.id) "
				+ " FROM checkforms JOIN hives ON hives.id = checkforms.\"hiveID_id\""
				+ " JOIN yards ON yards.id=hives.\"yardID_id\""
				+ " GROUP BY yards.id";

		Dao<CheckFormObject, Integer> objectClassDao = DaoManager.createDao(ConnectionProvider.getConnectionSource(), CheckFormObject.class); 
		GenericRawResults<String[]> selectedResult = objectClassDao.queryRaw(queryString);
		List<String[]> selectedList = (List<String[]>) selectedResult.getResults();
		responseWriter.println(selectedResult); 
		for(String col : selectedResult.getColumnNames()){
			responseWriter.println(col);
		}
		
		responseWriter.println(selectedList); 
		for(String[] row : selectedList){
			for(String col : row){
				responseWriter.print(col+" ");
			}
			responseWriter.println("");
		}
	}
	
	public static void testClassess(PrintWriter responseWriter){
		
		for (Class objectClass : classessToTest){
			responseWriter.println("\n==== TESTING "+ objectClass.getName() + " ======================\n");
			testCRUD(objectClass, responseWriter);
			testObjectToJsonAndBack(responseWriter, objectClass);
		}
	}
	
		
	public static void testCRUD(Class objectClass, PrintWriter responseWriter){
		responseWriter.println("==== TESTING "+ objectClass.getName() + " ======================");
		Gson gson = GsonProvider.getGson();
		List<BeeCommand> crudCommands = generateCRUDCommands(objectClass);
		responseWriter.println("\nCommands:");
		responseWriter.println(StringUtils.listToString(crudCommands));
		
		testCommandsToJsonAndBack(responseWriter, crudCommands);
		
		List<BeeCommandResult> crudResults = testExecuting(responseWriter, crudCommands);
	}
	
	public static List<BeeCommand> generateCRUDCommands(Class objectClass){

		Gson gson = GsonProvider.getGson();
		System.out.println("gson = " + gson);
		
		//Create objects to work with
		BeeObjectInterface objToCreate = null;
		BeeObjectInterface objToUpdate = null;
		BeeObjectInterface objToDelete = null;
		String whereStatement = "";
		
		switch(objectClass.getSimpleName()){
		case "YardObject":
			objToCreate = new YardObject("new yard", "my location", 1, false);
			objToUpdate = new YardObject(1, "updated yard", "updated location", 1, false);
			objToDelete = new YardObject(4, "", "", 3, false);
			whereStatement = "(\"yardName\" = 'new yard' AND \"location\" = 'my location' )";
			break;
		case "HiveObject":
			objToCreate = new HiveObject("new hive", 1, false);
			objToUpdate = new HiveObject(1, "updated hive", 1, false);
			objToDelete = new HiveObject(5, "", 0, false);
			whereStatement = "(\"hiveName\" = 'new hive')";
			break;
		case "UserObject":
			objToCreate = new UserObject("new username", "new password");
			objToUpdate = new UserObject(1, "updated username", "updated passwpr");
			objToDelete = new UserObject(5, "", "");
			whereStatement = "(\"username\" = 'new username')";
			break;
		case "CheckFormObject":
			objToCreate = new CheckFormObject(1, (new Date()).getTime(), true, "26.05.2013", false, "normal queen",
					10, 10, 1, 500, 300, 300, 0, 10, "new check!", false);
			objToUpdate = new CheckFormObject(1, 1, (new Date()).getTime(), true, "26.05.2013", false, "normal queen",
					10, 10, 1, 500, 300, 300, 0, 10, "updated check!", false);
			objToDelete = new CheckFormObject(5);
			whereStatement = "(\"hasQueen\" = true)";
			break;
		case "DiseaseObject":
			objToCreate = new DiseaseObject("new disease", "descr", "treatment", true);
			objToUpdate = new DiseaseObject(1, "updated disease", "descr", "treatment", true);
			objToDelete = new DiseaseObject(5);
			whereStatement = "(\"diseaseName\" = 'new disease')";
			break;
		}
		
		//create commands
		ArrayList<BeeCommand> beeCommandList = new ArrayList<BeeCommand>();
		beeCommandList.add(new PingCommand());
		beeCommandList.add(new SelectCommand(objectClass.getName(), whereStatement));
		beeCommandList.add(new SelectCommand(objectClass.getName()));
		beeCommandList.add(new CreateCommand(objectClass.getName(), gson.toJson(objToCreate, objectClass)));
		beeCommandList.add(new UpdateCommand(objectClass.getName(), gson.toJson(objToUpdate, objectClass)));
		beeCommandList.add(new DeleteCommand(objectClass.getName(), gson.toJson(objToDelete, objectClass)));
		
		return beeCommandList;
		
	}
	
	public static void testCommandsToJsonAndBack(PrintWriter responseWriter, List<BeeCommand> beeCommandList){
		responseWriter.println("\n\t======= COMMANDS TO JSON AND BACK ======= ");
		
		Gson gson = GsonProvider.getGson();
		//convert commands to json
		ArrayList<String> jsonCommandsList = new ArrayList<String>();
 
		responseWriter.println("Commands to JSON: \n");
		for(BeeCommand com :beeCommandList){
			responseWriter.println(com);
			String comjson = gson.toJson(com, BeeCommand.class);
			jsonCommandsList.add(comjson);
		}

		//get commands from json
		ArrayList<BeeCommand> beeCommandListFromJson = new ArrayList<BeeCommand>();
		
		responseWriter.println("\nJSONS: \n");
		for(String jsonstring : jsonCommandsList){
			responseWriter.println(jsonstring);
			beeCommandListFromJson.add(gson.fromJson(jsonstring, BeeCommand.class));
		}
	
		//print commands, decoded from json
		responseWriter.println("\nCommands from JSON: \n");
		responseWriter.println(StringUtils.listToString(beeCommandListFromJson));
		
		responseWriter.println("\t======= FINISHED COMMANDS TO JSON AND BACK ======= \n");
	 
	}
	
	public static void testObjectToJsonAndBack(PrintWriter responseWriter, Class objectClass){
		responseWriter.println("\n\t======= OBJECT TO JSON AND BACK ======= ");
		
		Gson gson = GsonProvider.getGson();
		
		//Create object to work with
		BeeObjectInterface obj = null;
		
		switch(objectClass.getSimpleName()){
		case "YardObject":
			obj = new YardObject("new yard", "my location", 1, false);
			break;
		case "HiveObject":
			obj = new HiveObject("new hive", 1, false);
			break;
		case "UserObject":
			obj = new UserObject("new username", "new password");
			break;
		case "CheckFormObject":
			obj = new CheckFormObject(1, (new Date()).getTime(), true, "26.05.2013", false, "normal queen",
					10, 10, 1, 500, 300, 300, 0, 10, "new check!", false);
			break;
		case "DiseaseObject":
			obj = new DiseaseObject("new disease", "descr", "treatment", true);
			break;
		}
				

		responseWriter.println("\nObject to JSON: \n" + obj);
		
		//convert object to json
		String json = gson.toJson(obj, objectClass);
		responseWriter.println("\nJSON: \n" + json);
 

		//get object from json
		BeeObjectInterface objFromJson = gson.fromJson(json, objectClass);
		responseWriter.println("\nObject from JSON: \n" + objFromJson);
		
		responseWriter.println("======= FINISHED OBJECT TO JSON AND BACK ======= \n");
	 
	}

	private static List<BeeCommandResult> testExecuting(PrintWriter responseWriter, List<BeeCommand> beeCommandList){
		ArrayList<BeeCommandResult> resultList = new ArrayList<>();
		
		responseWriter.println("\n======= TESTING EXECUTING ======= ");

		responseWriter.println("\nExecuting commands: \n");
		CommandExecuter commandExecuter = new CommandExecuter();
		for(BeeCommand com : beeCommandList){
			responseWriter.println(com);
			BeeCommandResult result = commandExecuter.execute(com);
			System.out.println(result);
			responseWriter.println(result);
			resultList.add(result);
		}
		responseWriter.println("======= FINISHED TESTING EXECUTING ======= \n");
		return resultList;
	}
}
