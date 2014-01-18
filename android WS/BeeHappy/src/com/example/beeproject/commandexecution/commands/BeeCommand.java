package com.example.beeproject.commandexecution.commands;

/** 
 * An interface all commands send to server must implement. 
 * <p>The commands must be sent to the server in shape of BeeCommand class, 
 * and then will be converted to the object of class for its command type and executed by CommandExecuter
 * @link CreateCommand, UpdateCommand, DeleteCommand, PingCommand
 * @author rezolya
 * @version 1.0
 */
public interface BeeCommand {
	public BeeCommandType getCommandType();
}
