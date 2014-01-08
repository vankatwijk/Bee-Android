package com.example.beeproject.commandexecution.results;

import com.example.beeproject.commandexecution.commands.BeeCommandType;

public class ErrorResult implements BeeCommandResult{
	private final BeeCommandResultType resultType = BeeCommandResultType.ERROR;
	private BeeCommandType commandType;

	private String exceptionClassName;
	private String exceptionMessage;
	

	public ErrorResult(BeeCommandType commandType, String exceptionClassName, String exceptionMessage) {
		this.commandType = commandType;
		this.exceptionClassName = exceptionClassName;
		this.exceptionMessage = exceptionMessage;
	}

	public String getExceptionClassName() {
		return exceptionClassName;
	}

	public void setExceptionClassName(String exceptionClassName) {
		this.exceptionClassName = exceptionClassName;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}

	public void setCommandType(BeeCommandType commandType) {
		this.commandType = commandType;
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
		return "Error result: [resultType = "+resultType+", commandType = "+commandType+", exceptionClassName = '"+exceptionClassName+"', exceptionMessage = "+exceptionMessage+"]";
	}
}
