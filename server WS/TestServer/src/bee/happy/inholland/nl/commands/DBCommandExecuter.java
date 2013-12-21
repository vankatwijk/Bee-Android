package bee.happy.inholland.nl.commands;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import sun.org.mozilla.javascript.internal.Token.CommentType;
import bee.happy.inholland.nl.commands.SelectCommand.SelectType;
import bee.happy.inholland.nl.domainmodel.BeeObjectInterface;
import bee.happy.inholland.nl.domainmodel.Yard;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

public class DBCommandExecuter {
    private static JdbcPooledConnectionSource connectionSource; //connection source to our database, connections are pooled and reused
    
	Gson gson;
	
	public DBCommandExecuter() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		//make Gson use adapter for converting the BeeCommand interface to json
        gsonBuilder.registerTypeAdapter(BeeObjectInterface.class, new InterfaceAdapter<BeeObjectInterface>());
        
        gson = gsonBuilder.create();
	        
		
	}

	public static JdbcPooledConnectionSource getConnectionSource(){
		if(connectionSource == null){
			//TODO: get the connection string from config file
		    String databaseUrl = "jdbc:postgresql://localhost:5432/BeeHappy";
		     
			try {
				connectionSource = new JdbcPooledConnectionSource(databaseUrl);
		        //TODO: get the password from config file?
				connectionSource.setUsername("postgres");
				connectionSource.setPassword("beeHappy");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return connectionSource;
	}
	
	public BeeCommandResult create(CreateCommand command) {
		BeeCommandResult result = null;
		
		System.out.println("adding to DB:");
		
		try {
			Class objectClass = getObjectClass(command.getClassName());
			Object object = gson.fromJson(command.getObjectJson(), objectClass);
			System.out.println("object to be inserted: " + object);
			
			// instantiate the dao
			Dao<? super Object, Integer> objectClassDao = DaoManager.createDao(getConnectionSource(), objectClass);
			
			//create object in DB
			int nInsertedRows = objectClassDao.create(objectClass.cast(object));

			if(nInsertedRows==1){
				String objectJson = gson.toJson(object);
				//object was successfuly added to DB
				result = new CreateCommandResult(command.getClassName(), objectJson);
			} else if(nInsertedRows == 0){
				throw new Exception(object + " could not be added in DB");
			}
			else{
				throw new Exception("Something went wrong...");
			}
			
		} catch (SQLException e) {
			result = errorResultFromException(e, command.getCommandType());
			e.printStackTrace();
		} catch (Exception e){
			result = errorResultFromException(e, command.getCommandType());
			e.printStackTrace();			
		}
		
		return result;
		
	}
	
	public BeeCommandResult update(UpdateCommand command) {
		BeeCommandResult result = null;
		
		System.out.println("updating in DB:");
		
		try {
			Class objectClass = getObjectClass(command.getClassName());
			Object object = gson.fromJson(command.getObjectJson(), objectClass);
			System.out.println("object to be updated: " + object);
			
			// instantiate the dao
			Dao<? super Object, Integer> objectClassDao = DaoManager.createDao(getConnectionSource(), objectClass);
			
			//update the object in DB
			int nUpdatedRows = objectClassDao.update(object);			
			if(nUpdatedRows==1){
				String objectJson = gson.toJson(object);
				//object was successfuly updated
				result = new UpdateCommandResult(command.getClassName(), objectJson);
			} else if(nUpdatedRows == 0){
				throw new Exception(object + " cound not be updated in DB");
			}
			else{
				throw new Exception("Something went wrong...");
			}
			
		} catch (SQLException e) {
			result = errorResultFromException(e, command.getCommandType());
			e.printStackTrace();
		} catch (Exception e){
			result = errorResultFromException(e, command.getCommandType());
			e.printStackTrace();			
		}
		
		return result;
		
	}

	public BeeCommandResult delete(DeleteCommand command) {
		BeeCommandResult result = null;
		System.out.println("deleting in DB:");
		
		try {
			Class objectClass = getObjectClass(command.getClassName());
			Object object = gson.fromJson(command.getObjectJson(), objectClass);
			System.out.println("object to be deleted: " + object);
			// instantiate the dao
			Dao<? super Object, Integer> objectClassDao = DaoManager.createDao(getConnectionSource(), objectClass);
			
			//delete the object in DB
			int nDeletedRows = objectClassDao.delete(object);	
			if(nDeletedRows==1){
				String objectJson = gson.toJson(object);
				//object was successfuly deleted
				result = new DeleteCommandResult(command.getClassName(), objectJson);
			} else if(nDeletedRows == 0){
				throw new Exception(object + " could not be deleted in DB");
			}
			else{
				throw new Exception("Something went wrong...");
			}
			
		} catch (SQLException e) {
			result = errorResultFromException(e, command.getCommandType());
			e.printStackTrace();
		} catch (Exception e){
			result = errorResultFromException(e, command.getCommandType());
			e.printStackTrace();			
		}
		
		
		return result;
	}
	
	
	public Class getObjectClass(String className){
		Class objectClass = null;
		try {
			objectClass = Class.forName(className);
			
		} catch (ClassNotFoundException e) {			
			//TODO: implement proper logging
			e.printStackTrace();
		}
		return objectClass;		
	}
	
	
	
	
	public ErrorResult errorResultFromException(Exception e, BeeCommandType commandType){
		String exceptionClassName = e.getClass().getName();
		String exceptionMessage = e.getMessage();
		return new ErrorResult(commandType, exceptionClassName, exceptionMessage);
	}

	@SuppressWarnings("unchecked")
	public BeeCommandResult select(SelectCommand command) {
		BeeCommandResult result = null;
		System.out.println("selected from DB:");
		
		try {
			Class objectClass = getObjectClass(command.getClassName());
			System.out.println("objects to be selected: " + objectClass);
			// instantiate the dao
			Dao<? super BeeObjectInterface, Integer> objectClassDao = DaoManager.createDao(getConnectionSource(), objectClass);
			
			//select the objects from DB
			List<BeeObjectInterface> selectedObjects = null;
			switch(command.getSelectType()){
				case ALL:
					selectedObjects = (List<BeeObjectInterface>) objectClassDao.queryForAll();
					break;
				case WHERE:
					selectedObjects = queryWithWhere(objectClassDao, command.getWhereString());
			}		
			
			if(selectedObjects !=null && selectedObjects.size()>0){
				//object were successfuly selected
				//convert list to json
				String jsonList = gson.toJson(selectedObjects, new TypeToken<List<BeeObjectInterface>>(){}.getType());
				result = new SelectCommandResult(jsonList);
			} else {
				result = new EmptyResult(command.getCommandType(), command.getClassName());
			}
			
		} catch (SQLException e) {
			result = errorResultFromException(e, command.getCommandType());
			e.printStackTrace();
		} catch (Exception e){
			result = errorResultFromException(e, command.getCommandType());
			e.printStackTrace();			
		}
		
		return result;
	}

	private List<BeeObjectInterface> queryWithWhere(Dao<? super BeeObjectInterface, Integer> objectClassDao, String whereStatement) throws SQLException {

		 String queryString = "SELECT * FROM " + objectClassDao.getDataClass().getSimpleName() + "s WHERE " + whereStatement;
		 
		 GenericRawResults<? super BeeObjectInterface> selectedResult = objectClassDao.queryRaw(queryString, objectClassDao.getRawRowMapper());
		 List<BeeObjectInterface> selectedList = (List<BeeObjectInterface>) selectedResult.getResults();
		 
		 return selectedList;
	}
}
