

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bee.happy.inholland.nl.domainmodel.Yard;

import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;


/**
 * Servlet implementation class TestServlet
 */
@WebServlet("/TestServlet")
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TestServlet() {
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
		 PrintWriter out = response.getWriter();
		 double[][] values = new double[5][5];
		 for(int i=0; i<5; i++)
			 for(int j=0; j<5; j++){
				 values[i][j] = i*10+j;
			 }

		 Yard myYard = new Yard("Yard 1", 10, 10);
		 System.out.println(myYard);
		 
		 Gson gson = new Gson();
		 String mgson = gson.toJson(myYard, Yard.class);
		 System.out.println("gsonResponse: " + mgson);
		 out.println(mgson);
		 
		 

        //TODO: get the connection string from config file
        String databaseUrl = "jdbc:postgresql://localhost:5432/BeeHappy";
        // create a connection source to our database
        ConnectionSource connectionSource;
		try {
			connectionSource = new JdbcConnectionSource(databaseUrl);
	        //TODO: get the password from config file?
			((JdbcConnectionSource)connectionSource).setUsername("postgres");
			((JdbcConnectionSource)connectionSource).setPassword("beeHappy");
			
			// instantiate the dao
			Dao<Yard, Integer> yardDao = DaoManager.createDao(connectionSource, Yard.class);

			
			// if you need to create the 'accounts' table make this call
			if(!yardDao.isTableExists())
				TableUtils.createTable(connectionSource, Yard.class);
			
			yardDao.create(myYard);
			yardDao.create(myYard);
			yardDao.create(myYard);
			
			Yard myYard2 = yardDao.queryForId(1);
			yardDao.delete(myYard2);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
