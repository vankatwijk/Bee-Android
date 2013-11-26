package bee.happy.inholland.nl.servlet;

import bee.happy.inholland.nl.servlet.BeeServletRequestCommand.ActionType;

public class BeeServletRequestDeleteCommand {
	DBCommandReceiver receiver;

	public ActionType getType() {
		return BeeServletRequestCommand.ActionType.DELETE;
	}

	public void execute() {
		receiver.delete();
	}
	
}
