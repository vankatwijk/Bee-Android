package com.example.beeproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.beeproject.commandexecution.CommandExecuter;
import com.example.beeproject.commandexecution.commands.BeeCommand;
import com.example.beeproject.commandexecution.results.BeeCommandResult;
import com.example.beeproject.gsonconvertion.GsonProvider;
import com.google.gson.Gson;


/**
 * Servlet implementation class CommandExecuteServlet
 * Executes commands, received from the BeeHappy app 
 */
@WebServlet("/CommandExecuteServlet")
public class CommandExecuteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CommandExecuteServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("application/json");
		System.out.println("HELLO1");
		PrintWriter responseWriter = response.getWriter();
		
		StringBuilder sb = new StringBuilder();
		BufferedReader br = request.getReader();
		String str;
		while( (str = br.readLine()) != null ){
		    sb.append(str);
		}    
		
		String commandJson = sb.toString();
		
		System.out.println(commandJson);
		Gson gson = GsonProvider.getGson();
		BeeCommand command = gson.fromJson(commandJson, BeeCommand.class);
		System.out.println(command);
		
		CommandExecuter commandExecuter = new CommandExecuter();
		BeeCommandResult result = commandExecuter.execute(command);
		//responseWriter.println(result);
		
		String resultJson = gson.toJson(result, BeeCommandResult.class);
		responseWriter.println(resultJson);
		System.out.println("HELLO!!!!");
	}

}
