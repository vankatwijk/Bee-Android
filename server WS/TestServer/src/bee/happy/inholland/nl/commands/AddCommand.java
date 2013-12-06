package bee.happy.inholland.nl.commands;

public class AddCommand implements Command{
	public final CommandType type = CommandType.ADD;
	private String className;
	private String objectJson;

	public AddCommand(String className, String objectJson) {
		super();
		this.className = className;
		this.objectJson = objectJson;
	}

	public String getClassName() {
		return className;
	}


	public void setClassName(String className) {
		this.className = className;
	}


	public String getObjectJson() {
		return objectJson;
	}


	public void setObjectJson(String objectJson) {
		this.objectJson = objectJson;
	}


	@Override
	public CommandType getType() {
		return type;
	}
	
	public String toString(){
		return "Add command: [type = "+type+", className = '"+className+"', objectJson = "+objectJson+"]";
	}
}
