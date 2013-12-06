package bee.happy.inholland.nl.commands;

public class PingCommand implements Command{
	public final CommandType type = CommandType.PING;
	
	public PingCommand() {
		super();
	}
	
	@Override
	public CommandType getType() {
		return type;
	}
	

	public String toString(){
		return "Ping command: [type = "+type+"]";
	}
	
}
