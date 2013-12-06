package bee.happy.inholland.nl.commands;

public class CommandExecuter {
	
	public int execute(Command command){
		switch (command.getType()){
			case PING:
				PingCommand pingCommand = (PingCommand) command;
				System.out.println("executing ping command: " + pingCommand);
				break;
			case ADD:
				AddCommand addCommand = (AddCommand) command;
				System.out.println("executing add command: " + addCommand);
				DBActionsExecuter dbActionsExecuter = new DBActionsExecuter();
				dbActionsExecuter.add(addCommand);
				break;
				
		}
		return 0;
		
	}

}
