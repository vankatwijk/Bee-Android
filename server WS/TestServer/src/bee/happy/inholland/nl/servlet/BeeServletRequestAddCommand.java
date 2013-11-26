package bee.happy.inholland.nl.servlet;

public class BeeServletRequestAddCommand implements BeeServletRequestCommand{
	DBCommandReceiver receiver;

	@Override
	public ActionType getType() {
		return BeeServletRequestCommand.ActionType.ADD;
	}

	@Override
	public void execute() {
		receiver.add();
	}
	

}
