package com.example.beeproject.statistics;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridLayout;
import android.widget.TextView;

import com.example.beeproject.R;
import com.example.beeproject.global.classes.CheckFormObject;
import com.example.beeproject.global.classes.DatabaseHelper;
import com.example.beeproject.global.classes.DatabaseManager;
import com.example.beeproject.global.classes.HiveObject;
import com.example.beeproject.global.classes.YardObject;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.CustomLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;

public class StatisticsSectionFragment extends Fragment {

	public static final String ARG_SECTION_NUMBER = "section_number";
	public static final String ARG_YARDS_AND_HIVES = "yards_and_hives";
	
	public GridLayout graphsGrid; 
	List<Object[]> yardsAndHives;
	DatabaseHelper db;
	
	public StatisticsSectionFragment(){
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		db = new DatabaseManager().getHelper(getActivity());
		
		View rootView = inflater.inflate(R.layout.fragment_statistics_graphs,container, false);
		int sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
		yardsAndHives = (List<Object[]>) getArguments().getSerializable(ARG_YARDS_AND_HIVES);

		TextView headerTextView = (TextView) rootView.findViewById(R.id.section_label);
		headerTextView.setText(getPageTitledes(sectionNumber));
		graphsGrid = (GridLayout) rootView.findViewById(R.id.graphsGrid);
		
		GraphAsyncTask graphAsyncTask = new GraphAsyncTask(yardsAndHives, sectionNumber);
		graphAsyncTask.execute("");
		
		return rootView;
	}
	
	public CharSequence getPageTitledes(int position) {
		Locale l = Locale.getDefault();
		switch (position) {
		case 1:
			return getString(R.string.title_section1des).toUpperCase(l);
		case 2:
			return getString(R.string.title_section2des).toUpperCase(l);
		case 3:
			return getString(R.string.title_section3des).toUpperCase(l);
		}
		return null;
	}
	
	public Double[] getBeeData(int position) {
		
		Double[] BeeData =  new Double[3]; 
		
		for(int i=0;i<3;i++){
			 Random rand = new Random();
			 int randomNum = rand.nextInt((40 - 0) + 1) + 0;
			BeeData[i] = (double) randomNum;
		}
		
		switch (position) {
		case 1:
			return BeeData;
		case 2:
			return BeeData;
		case 3:
			return BeeData;
		}
		return null;
	}
	
	
	public class GraphAsyncTask extends AsyncTask<String, Integer, List<GraphViewSeries>>{
		private static final String LOG_TAG = "GraphAsyncTask";
		public List<Object[]> yardsAndHives;
		public int position;
		public double maxY; //to make all graphs use the same scale
		
		public GraphAsyncTask(List<Object[]> yardsAndHives, int position){
			this.yardsAndHives = yardsAndHives;
			this.position = position;
		}

		@Override
		protected List<GraphViewSeries> doInBackground(String... params) {
			if(yardsAndHives!=null){
				List<GraphViewSeries> resultList = new ArrayList<GraphViewSeries>();
				
				RuntimeExceptionDao<CheckFormObject, Integer> checkformDao = db.getCheckFormRunDao();
				QueryBuilder<CheckFormObject, Integer> checkformQuery = checkformDao.queryBuilder();
				
				//System.out.println("position: " + position);
				maxY = 0;
				for(Object[] yardAndHive : yardsAndHives){
					if(yardAndHive.length == 2){
						String yardName = (String) yardAndHive[0];
						HiveObject hive = (HiveObject) yardAndHive[1];
						
						try{
							Where<CheckFormObject, Integer> where = checkformQuery.where().eq("hiveID_id", hive.getId());
							checkformQuery.setWhere(where);
							List<CheckFormObject> checkforms = checkformDao.query(checkformQuery.orderBy("timestamp", true).prepare());
							
							if(checkforms.size()>0){
								GraphViewData[] checkformValues = new GraphViewData[checkforms.size()];
								for(int i=0; i<checkforms.size(); i++){
									CheckFormObject checkform = checkforms.get(i);
									
									int seriesValue = 0;
									switch (position-1) {
									case 0:
										seriesValue = checkform.getEggs(); 
										System.out.println(" checkform.getEggs(): " + checkform.getEggs());
										break;
									case 1:
										seriesValue = checkform.getPupae();
										System.out.println(" checkform.getPupae(): " + checkform.getPupae());
										break;
									case 2:
										seriesValue = checkform.getNrOfMites();
										System.out.println(" checkform.getNrOfMites(): " + checkform.getNrOfMites());
										break;
									}
									
									checkformValues[i] = new GraphViewData(checkform.getTimestamp(), seriesValue);
									if(seriesValue>maxY){
										maxY = seriesValue;
									}
									
									//System.out.println("timestamp: " + checkform.getTimestamp() + " value: " + seriesValue + " checkform.getEggs(): "+ checkform.getEggs());
								}
								
								GraphViewSeries hiveSeries = new GraphViewSeries(checkformValues);
								resultList.add(hiveSeries);
							}
							else{
								resultList.add(null); //to keep yardAndHive and series syncronised
							}
						}
						catch(SQLException e){
							e.printStackTrace();
						}
					}
					else{
						Log.e("StatisticsSectionFragment", "Inalid set in yardsAndHives");
					}
				}
				
				return resultList;
			}
			else{
				return null;
			}
		}
		
		protected void onPostExecute(List<GraphViewSeries> result) {
			if(result!=null){
		        graphsGrid.setRowCount(result.size());
			
		        int graphHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics());
		        LayoutParams graphLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, graphHeight);
		        
		        for(int i=0; i<yardsAndHives.size(); i++){
		        	Object[] yardAndHive = yardsAndHives.get(i);
			        GraphViewSeries graphSeries = result.get(i);
					if(yardAndHive.length == 2 && graphSeries!=null){
						String yardName = (String) yardAndHive[0];
						HiveObject hive = (HiveObject) yardAndHive[1];
						String graphTitle = yardName + " - " + hive.getHiveName();
						
						GraphView graphView = new BarGraphView(getActivity(), graphTitle); 
						
						graphView.setBackgroundColor(Color.BLACK);
						graphView.getGraphViewStyle().setHorizontalLabelsColor(Color.WHITE);
						graphView.getGraphViewStyle().setVerticalLabelsColor(Color.WHITE);
				        graphView.setLayoutParams(graphLayoutParams);
				        graphView.setPadding(0, 10, 0, 10);
				        graphView.setManualYAxisBounds(maxY, 0);
			        	graphView.setCustomLabelFormatter(new DateLabelFormatter());
			        	
			        	graphView.addSeries(graphSeries); 
				        graphsGrid.addView(graphView);
					}
		        }
			}
			
	    	Log.d(LOG_TAG, "FINISHED");
		}
		
		public GraphViewSeries getHiveSeries(HiveObject hive){
			GraphViewSeries result;
			Double[] seriesData = getBeeData(getArguments().getInt(ARG_SECTION_NUMBER));
	        GraphViewSeries exampleSeries = new GraphViewSeries(new GraphViewData[] {  
	                new GraphViewData(0, seriesData[0])  
	                , new GraphViewData(1, seriesData[1])  
	                , new GraphViewData(2, seriesData[2])
	           

	        });  
	        return null;
		}
	}
}
