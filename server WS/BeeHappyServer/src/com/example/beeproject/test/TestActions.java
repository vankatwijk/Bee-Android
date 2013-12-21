package com.example.beeproject.test;

import java.io.PrintWriter;
import java.util.ArrayList;

import bee.happy.inholland.nl.commands.BeeCommand;

public class TestActions {
	public static void testCommandsToJsonAndBack(PrintWriter responseWriter, ArrayList<BeeCommand> beeCommandList){
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
}
