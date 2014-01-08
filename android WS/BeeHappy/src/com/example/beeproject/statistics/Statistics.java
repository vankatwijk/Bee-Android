package com.example.beeproject.statistics;

import java.util.Locale;
import java.util.Random;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.beeproject.R;
import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;

public class Statistics extends FragmentActivity {


	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistics);
		
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.statistics, menu);
		return true;
	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

	public static class DummySectionFragment extends Fragment {

		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
			View rootView = inflater.inflate(R.layout.fragment_main_dummy,container, false);
			TextView dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
			dummyTextView.setText(getPageTitledes(getArguments().getInt(ARG_SECTION_NUMBER)));

			Double[] BeeData = getBeeData(getArguments().getInt(ARG_SECTION_NUMBER));
	        GraphViewSeries exampleSeries = new GraphViewSeries(new GraphViewData[] {  
	                new GraphViewData(0, BeeData[0])  
	                , new GraphViewData(1, BeeData[1])  
	                , new GraphViewData(2, BeeData[2])

	          });  
	             
	          //-------------------------------------------------------------------------------------
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
	             
	          //-------------------------------------------------------------------------------------			
			
			
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
}
