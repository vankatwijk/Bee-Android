package com.example.beeproject.commandexecution.commands;

/**
 * An object encapsulating the command to delete an object of one of the domain classes.
 * <p>Attributes of command:
 * <p>Objects of this class can be persisted to a database using ORMLite
 * @author rezolya
 * @version 1.0	
 */
public class DeleteCommand implements BeeCommand{
	public final BeeCommandType type = BeeCommandType.DELETE;
	private String className;	//class of the object to be deleted
	private String objectJson; //object to be deleted

	public DeleteCommand(String className, String objectJson) {
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
		return type;
	}
	
	public String toString(){
		return "Delete command: [type = "+type+", className = '"+className+"', objectJson = "+objectJson+"]";
	}
}
