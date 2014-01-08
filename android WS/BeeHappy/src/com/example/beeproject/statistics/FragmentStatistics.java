package com.example.beeproject.statistics;

import java.util.Locale;
import java.util.Random;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.beeproject.R;
import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphView.GraphViewData;



public class FragmentStatistics extends Fragment {


	public FragmentStatistics() {
	}

	public static final String ARG_SECTION_NUMBER = "section_number";


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

		
		View rootView = inflater.inflate(R.layout.fragment_statistics,container, false);
		TextView dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
		dummyTextView.setText(getPageTitledes(getArguments().getInt(ARG_SECTION_NUMBER)));

		Double[] BeeData =  getBeeData(1);
		
        GraphViewSeries exampleSeries = new GraphViewSeries(new GraphViewData[] {  
                new GraphViewData(0, BeeData[0])  
                , new GraphViewData(1, BeeData[1])  
                , new GraphViewData(2, BeeData[2])

          });  
             
          //LineGraphView
          BarGraphView graphView = new BarGraphView(  
        		  getActivity() // context  
                , "bee project" // heading  
          );  
          //graphView.setManualYAxisBounds(0, 10); set length manually

          //set background color to black
          graphView.setBackgroundColor(Color.BLACK);
          
          //add data to graph
          graphView.addSeries(exampleSeries); // data  
             
          LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.layout);  
          layout.addView(graphView); 
		
		
		
		
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
}