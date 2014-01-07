package com.example.beeproject;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.beeproject.commandexecution.commands.BeeCommand;
import com.example.beeproject.commandexecution.commands.PingCommand;
import com.example.beeproject.commandexecution.commands.SelectCommand;
import com.example.beeproject.global.classes.YardObject;
import com.example.beeproject.gsonconvertion.GsonProvider;
import com.google.gson.Gson;

/**
 * Servlet implementation class TestCommandExecuteServlet
 */
@WebServlet("/TestCommandExecuteServlet")
public class TestCommandExecuteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TestCommandExecuteServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		PrintWriter responseWriter = response.getWriter();
		responseWriter.println("TestServlet says hi<br/>");

		String testURL = "CommandExecuteServlet";
		
		BeeCommand com;
		//com = new PingCommand();
		com = new SelectCommand(YardObject.class.getName());
		responseWriter.println("com "+ com);
		
		Gson gson = GsonProvider.getGson();
		String comJson = gson.toJson(com, BeeCommand.class);
		responseWriter.println("comJson "+ comJson);
		
		request.setAttribute("command", comJson);
		
		RequestDispatcher rd = request.getRequestDispatcher(testURL);
		rd.include(request, response);		
		//rd.forward(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
