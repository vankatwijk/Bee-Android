package bee.happy.inholland.nl.commands;


import com.google.gson.Gson;

public class DBActionsExecuter {
	Gson gson;
	
	public DBActionsExecuter() {
		gson = new Gson();
	}

	public int add(AddCommand command){
		
		System.out.println("adding to DB:");
		
		try {
			Class objectClass = Class.forName(command.getClassName());
			System.out.println("objectClass:" + objectClass);
			
			Object object = gson.fromJson(command.getObjectJson(), objectClass);
			
			System.out.println("object: " + object);
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return 0;
		
	}
}
