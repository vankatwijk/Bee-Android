package com.example.beeproject.commandexecution.commands;

public class SelectCommand implements BeeCommand{
	public enum SelectType{ ALL, WHERE}
	
	private final BeeCommandType commandType = BeeCommandType.SELECT;
	private String className;
	private SelectType selectType;
	
	private String whereString;

	/**
	 * Constructor, creates command to select everything from the class
	 * @param className
	 */
	public SelectCommand(String className) {
		super();
		this.className = className;
		this.selectType = SelectType.ALL;
	}
	
	/**
	 * Constructor, creates command to select objects satisfying a where clause
	 * @param className
	 */
	public SelectCommand(String className, String whereString) {
		super();
		this.className = className;
		this.selectType = SelectType.WHERE;
		this.whereString = whereString;
	}
	
	

	public String getClassName() {
		return className;
	}


	public void setClassName(String className) {
		this.className = className;
	}


	@Override
	public BeeCommandType getCommandType() {
		return commandType;
	}
	
	
	
	public SelectType getSelectType() {
		return selectType;
	}

	public void setSelectType(SelectType selectType) {
		this.selectType = selectType;
	}

	public String getWhereString() {
		return whereString;
	}

	public void setWhereString(String whereString) {
		this.whereString = whereString;
	}

	public String toString(){
		return "Select command: [commandType = "+commandType+", className = '"+className+"', selectType = '"+selectType+"', whereString = '"+whereString+"']";
	}

}
