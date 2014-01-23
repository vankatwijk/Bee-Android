package com.example.beeproject.calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.provider.CalendarContract.Events;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.beeproject.R;
import com.example.beeproject.global.classes.GlobalVar;
import com.example.beeproject.syncing.SyncTask;
import com.example.beeproject.syncing.SyncTaskCallback;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

public class CalendarActivity extends FragmentActivity {
	private CaldroidFragment _CaldroidFragment;
	private final String TAG = "CalendarActivity";
	private Date _CurrentSelectedDate;
	private boolean _HasEventSelected;
	private int _CurrentSelectedMonth = Calendar.getInstance().get(Calendar.MONTH);
	private long _CalendarId;
	private long _EventId;
	private BeeHappyCalendarResolver _CalendarResolver;
	// calendar dialog properties
	private EditText et_title;
	private EditText et_start;
	private EditText et_end;
	private EditText et_toEditAfterDatePicker;
	private AlertDialog alertDialog;
	private java.text.DateFormat _DateFormat;
	private void setCustomResourceForDates() {
		_CalendarResolver = new BeeHappyCalendarResolver(getApplicationContext(), GlobalVar.getInstance().getUserID());
		_CalendarId = _CalendarResolver.getCalendarId();
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
			boolean _DateHasEvent = (_CalendarResolver.getDateEvents(_CurrentSelectedDate).getCount() > 0);
			invalidateOptionsMenu();
			if(_DateHasEvent) {
				setEventList(date);				
			}
			else {
				ListView lv = (ListView) findViewById(R.id.list_event);
				lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[] { "No events for " + _DateFormat.format(date)} ));
			}			
		}
		
		// TODO count events and display in scroll view beneath calendar
	}
	
	/**
	 * displays the events on the view
	 * @param date
	 */
	private void setEventList(Date date) {
		@SuppressWarnings("deprecation")
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(
				this, 
				android.R.layout.simple_list_item_2,
				_CalendarResolver.getDateEvents(date),
				new String[] {Events.TITLE, Events.DESCRIPTION},
				new int[] { android.R.id.text1, android.R.id.text2 }
			);	
		
		ListView lv = (ListView) findViewById(R.id.list_event);
		lv.setAdapter(adapter);
		lv.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				String message = String.valueOf(arg0.getCount()) + " " + String.valueOf(arg2) + " " +  String.valueOf(arg3);
				_HasEventSelected = true;
				_EventId = arg3;
				Log.i(TAG, message);				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				_HasEventSelected = false;				
			}			
		});
	}
	
	private void unsetEventList() {
		ListView lv = (ListView) findViewById(R.id.list_event);
		lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[] { "No date selected"} ));
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
		_DateFormat = DateFormat.getDateFormat(this);
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
				unsetEventList();
				_CurrentSelectedMonth = month;
			}

			@Override
			public void onLongClickDate(Date date, View view) {
				
			}
		};

		_CaldroidFragment.setCaldroidListener(listener);
		unsetEventList();
	}	
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		if(_HasEventSelected) {
			getMenuInflater().inflate(R.menu.date_with_event_selected, menu);
		}
		else {
			getMenuInflater().inflate(R.menu.date_no_event, menu);
		}			
		return super.onPrepareOptionsMenu(menu);
	}

	public void goToToday(MenuItem v) {
		Date date = getTodayDate();
		_CaldroidFragment.moveToDate(date);
		setSelectedDate(date);		
	}
	
	public void toBeImplemented(MenuItem v) {
		// TODO remove this method when all is implemented
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
		setSelectedDate(_CurrentSelectedDate);
	}
	
	/**
	 * sets listener to save event if pressed positive
	 * @param alert
	 */
	private void setPositiveButtonListener(AlertDialog alert) {
		Button positive = alert.getButton(AlertDialog.BUTTON_POSITIVE);
		positive.setOnClickListener(savePositiveClickListener());
	}
	
	private View.OnClickListener savePositiveClickListener() {
		return new View.OnClickListener() {
		    public void onClick(View view) {
		    	View root =  view.getRootView();
		    	EditText title = (EditText) root.findViewById(R.id.event_title);
		    	EditText start = (EditText) root.findViewById(R.id.event_start);
		    	EditText end = (EditText) root.findViewById(R.id.event_end);
		    	EditText notes = (EditText) root.findViewById(R.id.event_notes);
		    	
		    	String event_title = title.getText().toString();
		    	long event_start;
		    	long event_end;
				try {
					event_start = _DateFormat.parse(start.getText().toString()).getTime();
					event_end = _DateFormat.parse(end.getText().toString()).getTime();
				} catch (ParseException e) {
					Toast.makeText(getApplicationContext(), "event date has incorrect format.", Toast.LENGTH_LONG).show();;
					return;
				}
				
		    	String event_notes = notes.getText().toString();
		    	ContentValues cv = new ContentValues();
		    	cv.put(Events.CALENDAR_ID, _CalendarId);
		    	cv.put(Events.TITLE, event_title);
		    	cv.put(Events.DTSTART, event_start);
		    	cv.put(Events.DTEND, event_end);
		    	cv.put(Events.DESCRIPTION, event_notes);
		    	cv.put(Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());		    	
		    	_CalendarResolver.createEvent(cv);
		    	alertDialog.dismiss();		    	
		    }
		};
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
	
	public void showDatePicker(View v){
		
		if(v.getId() == R.id.button_datePickerFrom){
			et_toEditAfterDatePicker = et_start;
		}
		else{
			et_toEditAfterDatePicker = et_end;
		}
		
	    DialogFragment newFragment = new DatePickerFragment() {
			
			@SuppressLint("SimpleDateFormat")
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				@SuppressWarnings("deprecation")
				Date date = new Date(year, monthOfYear, dayOfMonth);
				SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
				String dateString = df.format(date);
				et_toEditAfterDatePicker.setText(dateString);          
	                				
			}
		};
		
	    newFragment.show(getFragmentManager(), "datePicker");
	    
    }
	
}
