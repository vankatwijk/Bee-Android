package com.example.beeproject.commandexecution.commands;

/**
 * An object encapsulating the command to create an object of one of the domain classes.
 * <p>Attributes of command
 * <p>Objects of this class can be persisted to a database using ORMLite
 * @author rezolya
 * @version 1.0	
 */
public class CreateCommand implements BeeCommand{
	private final BeeCommandType commandType = BeeCommandType.CREATE;
	private String className;
	private String objectJson; //created object to be saved in the DB

	public CreateCommand(String className, String objectJson) {
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
		return "Create command: [commandType = "+commandType+", className = '"+className+"', objectJson = "+objectJson+"]";
	}
}
