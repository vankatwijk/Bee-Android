package com.example.beeproject.commandexecution.results;

import com.example.beeproject.commandexecution.commands.BeeCommandType;

public interface BeeCommandResult {
	public BeeCommandType getCommandType();
	public BeeCommandResultType getCommandResultType();
}
