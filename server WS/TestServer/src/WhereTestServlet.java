

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bee.happy.inholland.nl.commands.DBCommandExecuter;
import bee.happy.inholland.nl.domainmodel.BeeObjectInterface;
import bee.happy.inholland.nl.domainmodel.Yard;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.StatementBuilder.StatementType;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.CompiledStatement;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.support.DatabaseResults;

/**
 * Servlet implementation class WhereTestServlet
 */
@WebServlet("/WhereTestServlet")
public class WhereTestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WhereTestServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 PrintWriter responseWriter = response.getWriter();
		 responseWriter.println("Hello!");
		 Class objectClass = Yard.class;
		 System.out.println("objects to be selected: " + objectClass);
		 try { // instantiate the dao
			 Dao<? super BeeObjectInterface, Integer> objectClassDao = DaoManager.createDao(DBCommandExecuter.getConnectionSource(), objectClass);
			 QueryBuilder<? super BeeObjectInterface, Integer> qb = objectClassDao.queryBuilder();
			 Where where = qb.where();
			 where.eq("name", "New Yard");
			 where.and();
			 where.eq("latitude", 1);
			 
			 responseWriter.println("where.getStatement() = "+where.getStatement());
			 
			 String whereStatement = where.getStatement();
			 
			 
			 Dao<? super BeeObjectInterface, Integer> objectClassDao2 = DaoManager.createDao(DBCommandExecuter.getConnectionSource(), objectClass);
			 String queryString = "SELECT * FROM " + objectClass.getSimpleName() + "s WHERE " + whereStatement;
			 responseWriter.println("queryString = " + queryString);
			 
			 responseWriter.println("objectClassDao2.getRawRowMapper() = " + objectClassDao2.getRawRowMapper());
			 
			 GenericRawResults<? super BeeObjectInterface> selectedResult = objectClassDao2.queryRaw(queryString, objectClassDao2.getRawRowMapper());
			 List<BeeObjectInterface> selectedList = (List<BeeObjectInterface>) selectedResult.getResults();
			 printArrayList(selectedList, responseWriter);
			 
			 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	
	@SuppressWarnings("rawtypes")
	public void printArrayList(List list, PrintWriter responseWriter){
		for(Object o: list){
			responseWriter.println(o);
		}			
	}
	
	@SuppressWarnings("rawtypes")
	public <T> void printArrayList(GenericRawResults<T> list, PrintWriter responseWriter){
		for(T o: list){
			responseWriter.println(o);
		}			
	}
}
