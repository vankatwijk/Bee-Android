package bee.happy.inholland.nl.commands;

public class EmptyResult  implements BeeCommandResult{
	private final BeeCommandResultType resultType = BeeCommandResultType.ERROR;
	private BeeCommandType commandType;
	private String className; //name of class of deleted object
	
	public EmptyResult(BeeCommandType commandType, String className){
		this.commandType = commandType;
		this.className = className;
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
		return "Empty result: [resultType = "+resultType+", commandType = "+commandType+", className = '"+className+"']";
	}

}
