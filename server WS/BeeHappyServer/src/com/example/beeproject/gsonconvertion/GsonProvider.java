package com.example.beeproject.gsonconvertion;

import com.example.beeproject.commandexecution.commands.*;
import com.example.beeproject.commandexecution.results.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonProvider {
	private static Gson gson; 
	
	public static Gson getGson(){
		if (gson == null){
			GsonBuilder gsonBuilder = new GsonBuilder();
			//make Gson use adapter for converting the BeeCommand interface to json
	        gsonBuilder.registerTypeAdapter(BeeCommandType.class, new InterfaceAdapter<BeeCommand>());
	        gsonBuilder.registerTypeAdapter(BeeCommandResult.class, new InterfaceAdapter<BeeCommandResult>());
	        
	        gson = gsonBuilder.create();
		}
		return gson;
	}
}
