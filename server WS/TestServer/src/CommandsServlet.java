

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import bee.happy.inholland.nl.commands.AddCommand;
import bee.happy.inholland.nl.commands.Command;
import bee.happy.inholland.nl.commands.CommandExecuter;
import bee.happy.inholland.nl.commands.PingCommand;
import bee.happy.inholland.nl.domainmodel.Yard;

/**
 * Servlet implementation class CommandsServlet
 */
@WebServlet("/CommandsServlet")
public class CommandsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CommandsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 PrintWriter responseWriter = response.getWriter();
		 double[][] values = new double[5][5];
		 for(int i=0; i<5; i++)
			 for(int j=0; j<5; j++){
				 values[i][j] = i*10+j;
			 }

		 Yard myYard1 = new Yard("Yard 1", 10, 10);
		 Yard myYard2 = new Yard("Yard 2", 15, 10);
		 responseWriter.println(myYard1);
		 responseWriter.println(myYard2);
		 
		 Gson gson = new Gson();
		 
		 Command addCommand1 = new AddCommand(Yard.class.toString(), gson.toJson(myYard1, Yard.class));
		 Command addCommand2 = new AddCommand(Yard.class.toString(), gson.toJson(myYard2, Yard.class));
		 Command pingCommand = new PingCommand();

		 responseWriter.println("Created commands:");
		 responseWriter.println(addCommand1);
		 responseWriter.println(pingCommand);
		 responseWriter.println(addCommand2);
		 
		 CommandExecuter commandExecuter = new CommandExecuter();
		 commandExecuter.execute(addCommand1);
		 commandExecuter.execute(pingCommand);
		 commandExecuter.execute(addCommand2);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
