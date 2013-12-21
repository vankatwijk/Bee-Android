

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.stmt.Where;

import bee.happy.inholland.nl.commands.*;
import bee.happy.inholland.nl.domainmodel.BeeObjectInterface;
import bee.happy.inholland.nl.domainmodel.Yard;

/**
 * Servlet implementation class CommandsServlet
 */
@WebServlet("/CommandsServlet")
public class CommandsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	Gson gson; 
	DBCommandExecuter dbCommandExecuter; 
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CommandsServlet() {
        super();
        
        System.out.println("HI");

        GsonBuilder gsonBuilder = new GsonBuilder();
		//make Gson use adapter for converting the BeeCommand interface to json
        gsonBuilder.registerTypeAdapter(BeeCommand.class, new InterfaceAdapter<BeeCommand>());
        gsonBuilder.registerTypeAdapter(BeeCommandResult.class, new InterfaceAdapter<BeeCommandResult>());
        
        gson = gsonBuilder.create();
        dbCommandExecuter = new DBCommandExecuter();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 PrintWriter responseWriter = response.getWriter();
		 testYardCRUD(responseWriter);
		 testPing(responseWriter);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	
	private void testYardCRUD(PrintWriter responseWriter){
		System.out.println("\n====================== TESTING YARD CRUD ===================");
		responseWriter.println("\n====================== TESTING YARD CRUD ===================");
		
		
		 
		Yard newYard = new Yard("New Yard", 1, 1);
		Yard updatedYard = new Yard(5, "Updated Yard", 15, 10);
		Yard deletedYard = new Yard(38, " ", 15, 10);
		
		 
		String whereStatement = "(\"name\" = 'New Yard' AND \"latitude\" = 2 )";

		//create commands
		ArrayList<BeeCommand> beeCommandList = new ArrayList<BeeCommand>();
		beeCommandList.add(new SelectCommand(Yard.class.getName(), whereStatement));
		beeCommandList.add(new SelectCommand(Yard.class.getName()));
		beeCommandList.add(new CreateCommand(Yard.class.getName(), gson.toJson(newYard, Yard.class)));
		beeCommandList.add(new UpdateCommand(Yard.class.getName(), gson.toJson(updatedYard, Yard.class)));
		beeCommandList.add(new DeleteCommand(Yard.class.getName(), gson.toJson(deletedYard, Yard.class)));
		beeCommandList.add(new SelectCommand(Yard.class.getName()));
		
		ArrayList<BeeCommandResult> resultList = testExecuting(responseWriter, beeCommandList);
		testCommandsToJsonAndBack(responseWriter, beeCommandList);
		testCommandResultsToJsonAndBack(responseWriter, resultList);
		


		System.out.println("====================== FINISHED TESTING YARD CRUD ===================\n");
		responseWriter.println("====================== FINISHED TESTING YARD CRUD ===================\n");
	}
	
	private void testPing(PrintWriter responseWriter){
		System.out.println("\n====================== TESTING PING ===================");
		responseWriter.println("\n====================== TESTING PING ===================");

		//create commands
		ArrayList<BeeCommand> beeCommandList = new ArrayList<BeeCommand>();
		beeCommandList.add(new PingCommand());

		ArrayList<BeeCommandResult> resultList = testExecuting(responseWriter, beeCommandList);
		testCommandsToJsonAndBack(responseWriter, beeCommandList);
		testCommandResultsToJsonAndBack(responseWriter, resultList);

		System.out.println("====================== FINISHED TESTING PING ===================\n");
		responseWriter.println("====================== FINISHED TESTING PING ===================\n");
	}
	
	private void testCommandsToJsonAndBack(PrintWriter responseWriter, ArrayList<BeeCommand> beeCommandList){
		responseWriter.println("\n======= TESTING TO JSON AND BACK ======= ");

		//convert commands to json
		ArrayList<String> jsonCommandsList = new ArrayList<String>();
 
		responseWriter.println("Commands to JSON: \n");
		for(BeeCommand com :beeCommandList){
			responseWriter.println(com);
			jsonCommandsList.add(gson.toJson(com, BeeCommand.class));
		}

		//get commands from json
		ArrayList<BeeCommand> beeCommandListFromJson = new ArrayList<BeeCommand>();
 
		responseWriter.println("\nJSONS: \n");
		for(String jsonstring : jsonCommandsList){
			responseWriter.println(jsonstring);
			beeCommandListFromJson.add(gson.fromJson(jsonstring, BeeCommand.class));
		}
	
		//print commands, decoded from json
		responseWriter.println("\nCommands from JSON: \n");
		printArrayList(beeCommandListFromJson, responseWriter);
		
		responseWriter.println("======= FINISHED TESTING TO JSON AND BACK ======= \n");
	 
	}
	
	private void testCommandResultsToJsonAndBack(PrintWriter responseWriter, ArrayList<BeeCommandResult> beeCommandResultList){
		responseWriter.println("\n======= TESTING TO JSON AND BACK ======= ");

		//convert commands to json
		ArrayList<String> jsonList = new ArrayList<String>();
 
		responseWriter.println("Results to JSON: \n");
		for(BeeCommandResult com : beeCommandResultList){
			responseWriter.println(com);
			jsonList.add(gson.toJson(com, BeeCommand.class));
		}

		//get commands from json
		ArrayList<BeeCommandResult> listFromJson = new ArrayList<BeeCommandResult>();
 
		responseWriter.println("\nJSONS: \n");
		for(String jsonstring : jsonList){
			responseWriter.println(jsonstring);
			listFromJson.add(gson.fromJson(jsonstring, BeeCommandResult.class));
		}
	
		//print commands, decoded from json
		responseWriter.println("\nResults from JSON: \n");
		printArrayList(listFromJson, responseWriter);
		
		responseWriter.println("======= FINISHED TESTING TO JSON AND BACK ======= \n");
	 
	}
	
	private void testListToJsonAndBack(PrintWriter responseWriter, List<BeeObjectInterface> objectList){
		responseWriter.println("\n======= TESTING TO JSON AND BACK ======= ");

		//convert list to json
		String jsonList = gson.toJson(objectList, new TypeToken<List<BeeObjectInterface>>(){}.getType());
 
		responseWriter.println("List of BeeObjectInterface in JSON: \n" + jsonList);
		
		//get objects from json
		List<BeeObjectInterface> objectListFromJson = gson.fromJson(jsonList, new TypeToken<List<BeeObjectInterface>>(){}.getType()); 
		//print commands, decoded from json
		responseWriter.println("\nObjects decoded from JSON list: \n");
		printArrayList(objectListFromJson, responseWriter);
		
		responseWriter.println("======= FINISHED TESTING TO JSON AND BACK ======= \n");
	 
	}
	
	private ArrayList<BeeCommandResult> testExecuting(PrintWriter responseWriter, ArrayList<BeeCommand> beeCommandList){
		ArrayList<BeeCommandResult> resultList = new ArrayList<>();
		
		responseWriter.println("\n======= TESTING EXECUTING ======= ");
		responseWriter.println("Commands to execute: \n");
		for(BeeCommand com : beeCommandList){
			responseWriter.println(com);
		}

		responseWriter.println("\nExecuting commands: \n");
		CommandExecuter commandExecuter = new CommandExecuter();
		for(BeeCommand com : beeCommandList){
			responseWriter.println(com);
			BeeCommandResult result = commandExecuter.execute(com);
			System.out.println(result);
			responseWriter.println(result);
			resultList.add(result);
		}
		responseWriter.println("======= FINISHED TESTING EXECUTING ======= \n");
		return resultList;
	}
	
	@SuppressWarnings("rawtypes")
	public void printArrayList(List list, PrintWriter responseWriter){
		for(Object o: list){
			responseWriter.println(o);
		}			
	}
}
