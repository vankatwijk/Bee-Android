package com.example.beeproject.commandexecution.commands;

public class UpdateCommand implements BeeCommand{
	private final BeeCommandType commandType = BeeCommandType.UPDATE;
	private String className;
	private String objectJson; //updated object to be updated in the DB
 
	public UpdateCommand(String className, String objectJson) {
		super();
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
	
	public String toString(){
		return "Update command: [commandType = "+commandType+", className = '"+className+"', objectJson = "+objectJson+"]";
	}

}
