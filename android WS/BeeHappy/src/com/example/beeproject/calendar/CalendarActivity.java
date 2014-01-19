package com.example.beeproject.calendar;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.beeproject.R;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

public class CalendarActivity extends FragmentActivity {
	private CaldroidFragment _CaldroidFragment;
	private final String TAG = "CalendarActivity";
	private Date _CurrentSelectedDate;
	private int _CurrentSelectedMonth = Calendar.getInstance().get(Calendar.MONTH);
	// calendar dialog properties
	private EditText et_title;
	private EditText et_start;
	private EditText et_end;
	private AlertDialog alertDialog;
	
	private void setCustomResourceForDates() {
		if(!BeeHappyCalendarResolver.hasCalendar(getApplicationContext())) {
			Log.i(TAG, "Create Calendar");
			BeeHappyCalendarResolver.createCalendar(getApplicationContext());
		}
		else {
			Log.i(TAG, "Calendar Exist");
		}				
	}
	
	private Date getTodayDate() {
		return Calendar.getInstance().getTime();
	}
	
	private void setSelectedDate(Date date) {
		resetSelectedDate(_CurrentSelectedDate, _CurrentSelectedMonth);
		_CurrentSelectedDate = date;
				
		// set new current selected date
		if(date != null) {
			_CaldroidFragment.setBackgroundResourceForDate(R.color.blue, date);
			_CaldroidFragment.setTextColorForDate(R.color.white, date);
			_CaldroidFragment.refreshView();
		}
	}
	/** clear current selected date
	 * 
	 * @param date
	 * @param month
	 */
	private void resetSelectedDate(Date date, int month) {
		if (date == null) {
			return;
		}
		
		_CaldroidFragment.setTextColorForDate(R.color.black, date);
		
		if(sameDay(getTodayDate(), date)) {
			_CaldroidFragment.setBackgroundResourceForDate(R.drawable.red_border, date);
		}
		else{
			_CaldroidFragment.setBackgroundResourceForDate(R.color.white, date);
			
			if(!sameMonth(date, month)) {
				_CaldroidFragment.setTextColorForDate(R.color.caldroid_gray, date);
			}
		}
		
	}
	
	private boolean sameDay(Date date1, Date date2) {
		Calendar cal1 = new GregorianCalendar();
		Calendar cal2 = new GregorianCalendar();
		
		cal1.setTime(date1);
		cal2.setTime(date2);
		
		return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
				cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
				cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
	}
	
	private boolean sameMonth(Date date1, int month) {
		Calendar cal1 = new GregorianCalendar();		
		cal1.setTime(date1);
		// GregorianCalendar runs from 0 - 11 while month runs from 1 - 12		
		return cal1.get(Calendar.MONTH) == month - 1;				
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DateFormat.getDateFormat(this);
		setContentView(R.layout.activity_calendar);		

		_CaldroidFragment = new CaldroidFragment();

		if (savedInstanceState != null) {
			_CaldroidFragment.restoreStatesFromKey(savedInstanceState,
					"CALDROID_SAVED_STATE");
		}
		else {
			Bundle args = new Bundle();
			Calendar cal = Calendar.getInstance();
			args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
			args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
			args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
			args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, false);
			args.putInt(CaldroidFragment.START_DAY_OF_WEEK, Calendar.MONDAY);
			_CaldroidFragment.setArguments(args);
		}
		
		setCustomResourceForDates();
		
		FragmentTransaction t = getSupportFragmentManager().beginTransaction();
		t.replace(R.id.calendar1, _CaldroidFragment);
		t.commit();

		// Setup listener
		final CaldroidListener listener = new CaldroidListener() {

			@Override
			public void onSelectDate(Date date, View view) {
				setSelectedDate(date);
			}

			@Override
			public void onChangeMonth(int month, int year) {
				setSelectedDate(null);
				_CurrentSelectedMonth = month;
			}

			@Override
			public void onLongClickDate(Date date, View view) {
				
			}
		};

		_CaldroidFragment.setCaldroidListener(listener);		
	}	
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		if(dateHasEvent()) {
			getMenuInflater().inflate(R.menu.date_with_event, menu);
		}
		else {
			getMenuInflater().inflate(R.menu.date_no_event, menu);
		}			
		return super.onPrepareOptionsMenu(menu);
	}
	
	private boolean dateHasEvent() {
		// TODO Auto-generated method stub
		return true;
	}

	public void goToToday(MenuItem v) {
		Date date = getTodayDate();
		_CaldroidFragment.moveToDate(date);
		setSelectedDate(date);		
	}
	
	public void toBeImplemented(MenuItem v) {
		Toast.makeText(getApplicationContext(), "to be implemented", Toast.LENGTH_SHORT).show();
	}
	
	public void openDialog(MenuItem v) {				
		switch(v.getItemId()) {
			case R.id.action_edit:
				alertDialog = buildDialog("Edit event", "edit");
				break;
			case R.id.action_new:
				alertDialog = buildDialog("New event", "new");
				break;
			default:
				throw new IllegalStateException("Unidentified id" + v.getItemId());
		}
		alertDialog.show();
		alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
		
		// if a date has been selected, fill in start and end dates.
		if(_CurrentSelectedDate != null) {
			java.text.DateFormat _Format = DateFormat.getDateFormat(this);
			EditText et_start = (EditText) alertDialog.findViewById(R.id.event_start);
			et_start.setText(_Format.format(_CurrentSelectedDate));			
			EditText et_end = (EditText) alertDialog.findViewById(R.id.event_end);
			et_end.setText(_Format.format(_CurrentSelectedDate));
		}
		
		setEditTextListeners(alertDialog);
		setPositiveButtonListener(alertDialog);
	}
	
	/**
	 * sets listener to save event if pressed positive
	 * @param alert
	 */
	private void setPositiveButtonListener(AlertDialog alert) {
		alert.getButton(AlertDialog.BUTTON_POSITIVE);
	}
	private void setEditTextListeners(AlertDialog alert) {
		et_title = (EditText) alert.findViewById(R.id.event_title);
		et_title.addTextChangedListener(hasText());
		et_start = (EditText) alert.findViewById(R.id.event_start);
		et_start.addTextChangedListener(hasText());
		et_end = (EditText) alert.findViewById(R.id.event_end);
		et_end.addTextChangedListener(hasText());
	}
	
	private TextWatcher hasText() {
		return new TextWatcher() {
		      @Override
		      public void afterTextChanged(Editable arg0) {
		    	  isNotEmpty();
		      }

		      @Override
		      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		      }

		      @Override
		      public void onTextChanged(CharSequence s, int start, int before, int count) {
		      }
		    };
	}
	
	private void isNotEmpty() {
		if(!(et_title.getText().toString().matches("") &&
				et_start.getText().toString().matches("") &&
				et_start.getText().toString().matches("")))
		{
			alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
		}
	}
	
	
	/**
	 * dialog builder, needs to be refactor how to choose the positive button
	 * @param title
	 * @param positiveButton
	 * @return
	 */
	private AlertDialog buildDialog(String title, String positiveButton) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(title)
			.setView(getLayoutInflater().inflate(R.layout.dialog_event, null))
	        .setPositiveButton(positiveButton, positiveClickListener(positiveButton))
	        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int id) {
	            	Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_LONG).show();
	            }
	        });
		return builder.create();
	}

	private OnClickListener positiveClickListener(String type) {
		if(type == "new") { 
			return new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int id) {
			    	Toast.makeText(getApplicationContext(), "new", Toast.LENGTH_LONG).show();
			    }
			};
		}
		else if (type == "edit") {
			return new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int id) {
			    	Toast.makeText(getApplicationContext(), "edit", Toast.LENGTH_LONG).show();
			    }
			};
		}
		
		throw new IllegalStateException("unknown type listener found for " + type);
	}	
}
