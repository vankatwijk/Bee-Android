package bee.happy.inholland.nl.commands;

public class CommandExecuter {
	
	/**
	 * Receives commands, converts them to approppriate command type (Add, Update, Delete, etc)
	 * and executes them
	 * @param command
	 * @return
	 */
	public BeeCommandResult execute(BeeCommand command){

		DBCommandExecuter dbActionsExecuter = new DBCommandExecuter();
		BeeCommandResult result = null;
		
		switch (command.getCommandType()){
			case PING:
				PingCommand pingCommand = (PingCommand) command;
				System.out.println("executing ping command: " + pingCommand);
				result = new PingCommandResult();
				break;
			case CREATE:
				CreateCommand createCommand = (CreateCommand) command;
				System.out.println("executing add command: " + createCommand);
				result = dbActionsExecuter.create(createCommand);
				break;
			case UPDATE:
				UpdateCommand updateCommand = (UpdateCommand) command;
				System.out.println("executing update command: " + updateCommand);
				result = dbActionsExecuter.update(updateCommand);
				break;
			case DELETE:
				DeleteCommand deleteCommand = (DeleteCommand) command;
				System.out.println("executing update command: " + deleteCommand);
				result = dbActionsExecuter.delete(deleteCommand);
				break;
				
		}
		return result;
		
	}

}
