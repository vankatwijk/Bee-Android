package com.example.beeproject.calendar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.beeproject.R;


public class FragmentCalender extends Fragment {
	public FragmentCalender() {
	}
	
	private String accountName = "test";
	private String calendarName = "test2";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragmentcalender,
				container, false);
		Button createCalendar = (Button) rootView.findViewById(R.id.buttonCreateCalendar);
		Button deleteCalendar = (Button) rootView.findViewById(R.id.buttonDeleteCalendar);
		Button fakeDeleteCalendar = (Button) rootView.findViewById(R.id.buttonFakeDeleteCalendar);
		
		createCalendar.setOnClickListener(new OnClickListener() {	
			
			@Override
			public void onClick(View v) {
				CalendarContentResolver.create(getActivity().getApplicationContext(), accountName, calendarName);
				Toast.makeText(getActivity(), "Created", Toast.LENGTH_SHORT).show();
			}
		});
		
		deleteCalendar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int rows = CalendarContentResolver.delete(getActivity(), accountName, calendarName);
				Log.i("rows", Integer.toString(rows));
				if (rows > 0)
					Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(getActivity(), "No Deleted", Toast.LENGTH_SHORT).show();
			}
			
		});
		
		fakeDeleteCalendar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int rows = CalendarContentResolver.delete(getActivity(), accountName, calendarName + "1");
				Log.i("rows", Integer.toString(rows));
				if (rows > 0)
					Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(getActivity(), "No Deleted", Toast.LENGTH_SHORT).show();
			}
			
		});
		
		return rootView;
	}	
}