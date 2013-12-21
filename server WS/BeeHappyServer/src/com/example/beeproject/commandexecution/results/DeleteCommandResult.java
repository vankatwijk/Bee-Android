package com.example.beeproject.commandexecution.results;

import com.example.beeproject.commandexecution.commands.BeeCommandType;

public class DeleteCommandResult  implements BeeCommandResult{
	private final BeeCommandResultType resultType = BeeCommandResultType.SUCCESS; //result is success. If deleting the object in DB was not successful, an ErrorResult must be generated
	private final BeeCommandType commandType = BeeCommandType.DELETE;
	private String className; //name of class of deleted object
	private String objectJson; //object, deleted from the DB 
	//TODO: consider just storing id
	
	public DeleteCommandResult(String className, String objectJson) {
		this.className = className;
		this.objectJson = objectJson;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getObjectJson() {
		return objectJson;
	}

	public void setObjectJson(String objectJson) {
		this.objectJson = objectJson;
	}

	@Override
	public BeeCommandType getCommandType() {
		return commandType;
	}

	@Override
	public BeeCommandResultType getCommandResultType() {
		return resultType;
	}
	

	public String toString(){
		return "Delete result: [resultType = "+resultType+", commandType = "+commandType+", className='"+className+"', objectJson='"+objectJson+"' ]";
	}
}