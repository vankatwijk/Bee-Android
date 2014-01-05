package com.example.beeproject.commandexecution.results;

import com.example.beeproject.commandexecution.commands.BeeCommandType;

public class PingCommandResult implements BeeCommandResult{
	private final BeeCommandResultType resultType = BeeCommandResultType.SUCCESS; //Result of ping is always success
	private final BeeCommandType commandType = BeeCommandType.PING;

	@Override
	public BeeCommandType getCommandType() {
		return commandType;
	}

	@Override
	public BeeCommandResultType getCommandResultType() {
		return resultType;
	}
	

	public String toString(){
		return "Ping result: [resultType = "+resultType+", commandType = "+commandType+" ]";
	}
}
