package bee.happy.inholland.nl.servlet;

public interface BeeServletRequestCommand {
	public enum ActionType {
		   SELECT, ADD, UPDATE, DELETE
	}

	public abstract ActionType getType();
	
	public abstract void execute();
	
}
