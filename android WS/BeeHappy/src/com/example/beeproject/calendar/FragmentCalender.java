package com.example.beeproject.calendar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beeproject.R;


public class FragmentCalender extends Fragment {
	public FragmentCalender() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// replace this with a custom calendar view as this only shows a calendar. 
		// it is not possible to change colours of the day with this.
		CalendarView calendarView = (CalendarView) getActivity().findViewById(R.id.calendarView1);
		calendarView.setOnDateChangeListener(new OnDateChangeListener()
		{
			@Override
			public void onSelectedDayChange(CalendarView view, int year, int month,
					int dayOfMonth) {
				Toast.makeText(getActivity(), dayOfMonth+"/"+month+"/"+year, Toast.LENGTH_LONG).show();
				changeTextToDay(year,month,dayOfMonth);
			}					
		});
	}
	
	private void changeTextToDay(int year, int month, int dayOfMonth)
	{		
		String date = Integer.toString(year) + "/" + Integer.toString(month) +"/"+ Integer.toString(dayOfMonth);  
		TextView currentDateText = (TextView) getActivity().findViewById(R.id.newUsernameTextView);
		currentDateText.setText(date);
		TextView remindersText = (TextView) getActivity().findViewById(R.id.newPasswordTextView);
		remindersText.setText("Reminders of : " + date);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragmentcalender,
				container, false);

		return rootView;
	}
	
}