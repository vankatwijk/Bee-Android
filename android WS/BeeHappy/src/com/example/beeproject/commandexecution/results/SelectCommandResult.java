package com.example.beeproject.commandexecution.results;

import com.example.beeproject.commandexecution.commands.BeeCommandType;

public class SelectCommandResult  implements BeeCommandResult{
	private final BeeCommandResultType resultType = BeeCommandResultType.SUCCESS; //result is success. If selecting was not successful, an ErrorResult must be generated
	private final BeeCommandType commandType = BeeCommandType.SELECT;
	private String oblectListJson; //list of objects selected from the DB 

	public SelectCommandResult(String objectJson) {
		//this.className = className;
		this.oblectListJson = objectJson;
	}

	public String getObjectListJson() {
		return oblectListJson;
	}

	public void setObjectListJson(String objectListJson) {
		this.oblectListJson = objectListJson;
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
		return "Select result: [resultType = "+resultType+", commandType = "+commandType+", objectListJson='"+oblectListJson+"' ]";
	}
}
