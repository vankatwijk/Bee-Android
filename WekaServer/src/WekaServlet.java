

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instances;
import weka.experiment.InstanceQuery;
/**
 * Servlet implementation class WekaServlet
 */
@WebServlet("/WekaServlet")
public class WekaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WekaServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
        InstanceQuery query;
        String queryString = "select " + 
        		" checkforms.\"nrOfMites\" as nrOfMites, checkforms.\"hasQueen\" as has_queen, checkforms.\"qRace\" as q_race, checkforms.\"nrOfFrames\" as nr_Frames, checkforms.\"eggs\" as nr_Eggs, checkforms.\"larve\" as nr_larvae, checkforms.\"pupae\" as nr_pupae," +
        		"CASE " +
        		"WHEN checkforms.\"hiveID_id\"=outbrakes.\"hiveID_id\" and outbrakes.\"endDate\" is null THEN 'sick' "+
        		"ELSE 'not sick' "+ 
        		"END as \"isSick\" "+
        		"from hives "+
        		"left join checkforms on checkforms.\"hiveID_id\" = hives.id "+
        		"left join outbrakes on outbrakes.\"hiveID_id\" = hives.id "+
        		"order by hives.id";
        
		try {
			query = new InstanceQuery();
			query.setUsername("postgres");
			query.setPassword("beeHappy");
			query.setQuery(queryString);
			 
			Instances data = query.retrieveInstances();
			query.close();
			//out.println(data);
			
			NaiveBayes nb = new NaiveBayes();
			data.setClassIndex(data.numAttributes() - 1);
			nb.buildClassifier(data);
			Evaluation ev = new Evaluation(data);
			ev.crossValidateModel(nb, data, 10, new Random(1));
			out.println(nb.toString());
			out.println(ev.toSummaryString("\nResults\n---------\n-------", true));
			out.println();
			
			out.println("\n\n MultiLayer Perceptron:\n");
			MultilayerPerceptron mp = new MultilayerPerceptron();
			data.setClassIndex(data.numAttributes() - 1);
			mp.buildClassifier(data);
			Evaluation eval = new Evaluation(data);
			eval.crossValidateModel(mp, data, 10, new Random(1));
			out.println(mp.toString());
			out.println(ev.toSummaryString("\nResults\n---------\n-------", true));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			
	}

}
