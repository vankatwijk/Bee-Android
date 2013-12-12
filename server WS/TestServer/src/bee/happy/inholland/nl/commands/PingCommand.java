package bee.happy.inholland.nl.commands;

public class PingCommand implements BeeCommand{
	public final BeeCommandType type = BeeCommandType.PING;
	
	public PingCommand() {
		super();
	}
	
	@Override
	public BeeCommandType getCommandType() {
		return type;
	}
	

	public String toString(){
		return "Ping command: [type = "+type+"]";
	}
	
}
