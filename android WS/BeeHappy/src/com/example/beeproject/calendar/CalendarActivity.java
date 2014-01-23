package com.example.beeproject.calendar;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract.Events;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.beeproject.MainActivity;
import com.example.beeproject.R;
import com.example.beeproject.global.classes.GlobalVar;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

/**
 * Creates a calendar activity
 * bugs known: when a date has no events, a new event is added, the view has to be refreshed
 * @author Harold
 *
 */
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
			boolean _DateHasEvent = _CalendarResolver.hasDateEvents(_CurrentSelectedDate);
			if(_DateHasEvent) {
				setEventList(date);				
			}
			else {
				updateEventSelectedSettings(false, -1);
				ListView lv = (ListView) findViewById(R.id.list_event);
				lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[] { "No events for " + _DateFormat.format(date)} ));
			}			
		}
	}
	
	/**
	 * displays the events on the view
	 * @param date
	 */
	private void setEventList(Date date) {
		@SuppressWarnings("deprecation")
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(
				this, 
				android.R.layout.simple_list_item_activated_1,
				_CalendarResolver.getDateEvents(date),
				new String[] {Events.TITLE, Events.DESCRIPTION},
				new int[] { android.R.id.text1, android.R.id.text2 }
			);	
		
		ListView lv = (ListView) findViewById(R.id.list_event);
		lv.setAdapter(adapter);
		lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String message = String.valueOf(arg0.getCount()) + " " + String.valueOf(arg2) + " " +  String.valueOf(arg3);
				Log.i(TAG, "button clicked : " + message);
				updateEventSelectedSettings(true, arg3);
			}			
		});		
	}
	
	private void unsetEventList() {
		ListView lv = (ListView) findViewById(R.id.list_event);
		lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[] { "No date selected"} ));
		updateEventSelectedSettings(false, -1);
	}
	
	private void updateEventSelectedSettings(boolean selected, long eventId) {
		_HasEventSelected = selected;
		invalidateOptionsMenu();
		_EventId = eventId;
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
		getActionBar().setDisplayHomeAsUpEnabled(true);

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
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpTo(this,
					new Intent(this, MainActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
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
		_CalendarResolver.deleteEvent(_EventId);
		Toast.makeText(getApplicationContext(), "event deleted", Toast.LENGTH_SHORT).show();
	}
	
	public void openDialog(MenuItem v) {				
		String title;
		switch(v.getItemId()) {
			case R.id.action_edit:
				alertDialog = buildDialog("Edit event", "edit");
				title = "edit";
				break;
			case R.id.action_new:
				alertDialog = buildDialog("New event", "new");
				title = "new";
				break;
			default:
				throw new IllegalStateException("Unidentified id" + v.getItemId());
		}
		
		alertDialog.show();
		
		EditText et_title = (EditText) alertDialog.findViewById(R.id.event_title);
		EditText et_start = (EditText) alertDialog.findViewById(R.id.event_start);
		EditText et_end = (EditText) alertDialog.findViewById(R.id.event_end);
		EditText et_notes = (EditText) alertDialog.findViewById(R.id.event_notes);
		
		if(title == "new") {
			// if a date has been selected, fill in start and end dates.
			if(_CurrentSelectedDate != null) {
				java.text.DateFormat _Format = DateFormat.getDateFormat(this);
				et_start.setText(_Format.format(_CurrentSelectedDate));			
				et_end.setText(_Format.format(_CurrentSelectedDate));
			}
		}
		else if(title == "edit") {
			ContentValues cv = _CalendarResolver.getEvent(_CalendarId, _EventId);
			String eventTitle = cv.getAsString(Events.TITLE);
			Long start = Long.parseLong(cv.getAsString(Events.DTSTART));
			Long end = Long.parseLong(cv.getAsString(Events.DTEND));
			String notes = cv.getAsString(Events.DESCRIPTION);
			
			et_title.setText(eventTitle);
			et_start.setText(_DateFormat.format(new Date(start)));
			et_end.setText(_DateFormat.format(new Date(end)));
			et_notes.setText(notes);
		}
		
		alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
		
		setEditTextListeners(alertDialog);
		setPositiveButtonListener(alertDialog);
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
		    	String event_notes = notes.getText().toString();	    	
		    	
		    	long event_start;
		    	long event_end;
				try {
					event_start = _DateFormat.parse(start.getText().toString()).getTime();
					event_end = _DateFormat.parse(end.getText().toString()).getTime();
				} catch (ParseException e) {
					Toast.makeText(getApplicationContext(), "event date has incorrect format.", Toast.LENGTH_LONG).show();;
					return;
				}
				
		    	ContentValues cv = new ContentValues();
		    	cv.put(Events.CALENDAR_ID, _CalendarId);
		    	cv.put(Events.TITLE, event_title);
		    	cv.put(Events.DTSTART, event_start);
		    	cv.put(Events.DTEND, event_end);
		    	cv.put(Events.DESCRIPTION, event_notes);
		    	cv.put(Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());	
		    	
		    	// TODO refactor this for a more secure to check!
				if(_EventId != -1){
					_CalendarResolver.deleteEvent(_EventId);
					_CalendarResolver.createEvent(cv);
				}
				else{			    	
			    	_CalendarResolver.createEvent(cv);
		    	}
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
	        .setPositiveButton(positiveButton, null)
	        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int id) {
	            	Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_LONG).show();
	            }
	        });
		alertDialog = builder.create();
		
		return alertDialog;
	}
	
	public void showDatePicker(View v){
		
		if(v.getId() == R.id.button_datePickerFrom){
			et_toEditAfterDatePicker = et_start;
		}
		else{
			et_toEditAfterDatePicker = et_end;
		}
		
	    DialogFragment newFragment = new DatePickerFragment() {
			
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				GregorianCalendar date = new GregorianCalendar(year, monthOfYear, dayOfMonth);
				Log.i(TAG, "new date selected " + date.getTime());
				String dateString = _DateFormat.format(date.getTime());
				et_toEditAfterDatePicker.setText(dateString);          
	                				
			}
		};
		
	    newFragment.show(getFragmentManager(), "datePicker");
	    
    }
	
}
