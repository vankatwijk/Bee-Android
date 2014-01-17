package com.example.beeproject.calendar;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.beeproject.R;
import com.example.beeproject.global.classes.GlobalVar;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

public class CalendarActivity extends FragmentActivity {
	private CaldroidFragment _CaldroidFragment;
	private final String TAG = "CalendarActivity";
	private Date _CurrentSelectedDate;
	private int _CurrentSelectedMonth = Calendar.getInstance().get(Calendar.MONTH);
	
	private void setCustomResourceForDates() {
		if(!CalendarResolver.hasCalendar(getApplicationContext())) {
			Log.i(TAG, "Create Calendar");
			CalendarResolver.createCalendar(getApplicationContext());
		}
		else {
			Log.i(TAG, "Calendar Exist");
		}				
	}
	
	private Date getTodayDate() {
		return Calendar.getInstance().getTime();
	}
	
	private void setSelectedDate(Date date) {
		// clear current selected date
		if(_CurrentSelectedDate != null) {
			_CaldroidFragment.setTextColorForDate(R.color.black, _CurrentSelectedDate);
			
			if(sameDay(getTodayDate(), _CurrentSelectedDate)) {
				_CaldroidFragment.setBackgroundResourceForDate(R.drawable.red_border, _CurrentSelectedDate);
			}
			else{
				_CaldroidFragment.setBackgroundResourceForDate(R.color.white, _CurrentSelectedDate);
				
				if(!sameMonth(_CurrentSelectedDate, _CurrentSelectedMonth)) {
					_CaldroidFragment.setTextColorForDate(R.color.caldroid_gray, _CurrentSelectedDate);
				}
			}
		}
		_CurrentSelectedDate = date;
				
		// set new current selected date
		if(date != null) {
			_CaldroidFragment.setBackgroundResourceForDate(R.color.blue, date);
			_CaldroidFragment.setTextColorForDate(R.color.white, date);
			_CaldroidFragment.refreshView();
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
				EditEventDialog();
				break;
			case R.id.action_new:
				NewEventDialog();
				break;
			default:
				throw new IllegalStateException("Unidentified id" + v.getItemId());
		}		
	}
	
	private void EditEventDialog() {
		AlertDialog alert = buildDialog("Edit event", "edit");
		alert.show();
	}
	
	private void NewEventDialog() {
		AlertDialog alert = buildDialog("New event", "new");
		alert.show();
		java.text.DateFormat _Format = DateFormat.getDateFormat(this);
		EditText et_start = (EditText) alert.findViewById(R.id.event_start);
		et_start.setText(_Format.format(_CurrentSelectedDate));
		
		EditText et_end = (EditText) alert.findViewById(R.id.event_end);
		et_end.setText(_Format.format(_CurrentSelectedDate));
	}
	
	private AlertDialog buildDialog(String title, String positiveButton) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(title)
			.setView(getLayoutInflater().inflate(R.layout.dialog_event, null))
	        .setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int id) {
	            }
	        })
	        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int id) {
	            	Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_LONG).show();
	            }
	        });
		return builder.create();
	}
	
	/**
	 * Implemented version of 
	 * http://www.derekbekoe.co.uk/blog/item/16-using-the-android-4-0-calendar-api
	 *
	 */
	public static class CalendarResolver {
		private static final String TAG = "CalendarResolver";
		private static final String CALENDAR_NAME = "BeeHappy";
		
		private static final Uri CAL_URI = CalendarContract.Calendars.CONTENT_URI;
		private static final Uri EVENT_URI = CalendarContract.Events.CONTENT_URI;
		
		public static String getUserId() {
			return Integer.toString(GlobalVar.getInstance().getUserID());
		}
		
		public static boolean hasCalendar(Context ctx)	{
			final String[] CALENDARS_PROPS_PROJECTION = { Calendars._ID, Calendars.NAME, Calendars.CALENDAR_DISPLAY_NAME, 
					Calendars.CALENDAR_TIME_ZONE, Calendars.DELETED };
			ContentResolver cr = ctx.getContentResolver();
			Cursor cur = cr.query(buildCalUri(), CALENDARS_PROPS_PROJECTION, Calendars.NAME + " = ?", new String[] { CALENDAR_NAME }, null);			
			return cur.getCount() > 0;
		}
		
		/**
		 * creates a calendar
		 * @param ctx (activity context)
		 * @return Calendar_ID
		 */
		public static long createCalendar(Context ctx) {
			ContentResolver cr = ctx.getContentResolver();
			final ContentValues cv = buildNewCalContentValues();
			Uri calUri = buildCalUri();
			//insert the calendar into the database
			cr.insert(calUri, cv);
			Uri newUri = cr.insert(buildCalUri(), cv);
			return Long.parseLong(newUri.getLastPathSegment());
		}
		
		/**
		 * adds an event to a calendar
		 * @param ctx (activity context)
		 * @param cv, ContentValues. Must contain a Calendar_ID
		 * @return URI of the event
		 */
		public static Uri addEvent(Context ctx, ContentValues cv) {
			ContentResolver cr = ctx.getContentResolver();				
			return cr.insert(buildEventUri(), cv);
		}
		
		public static int updateEvent(Context ctx, Long eventId, ContentValues cv) {
			ContentResolver cr = ctx.getContentResolver();
			String selection = "("+Events._ID+" = ?)";
			String[] selectionArgs = new String[] {String.valueOf(eventId)};
			return cr.update(buildEventUri(), cv, selection, selectionArgs);
		}
		
		public static int deleteEvent(Context ctx, Long eventId) {
			ContentResolver cr = ctx.getContentResolver();
			String selection = "("+Events._ID+" = ?)";
			String[] selectionArgs = new String[] {String.valueOf(eventId)};
			return cr.delete(EVENT_URI, selection, selectionArgs);
		}
		
		public static long getEvent(Context ctx, String selection, String[] selectionArgs) {			 
			ContentResolver cr = ctx.getContentResolver();
			final String[] PROJECTION = new String[] { Events._ID };
			Cursor cursor = cr.query(buildEventUri(), PROJECTION, selection, selectionArgs, null);
			return cursor.getLong(0);
		}
		
		private static ContentValues buildNewCalContentValues() {
			final ContentValues cv = new ContentValues();
			cv.put(Calendars.ACCOUNT_NAME, getUserId());
			cv.put(Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
			cv.put(Calendars.NAME, CALENDAR_NAME);
			cv.put(Calendars.CALENDAR_DISPLAY_NAME, CALENDAR_NAME);
			cv.put(Calendars.CALENDAR_COLOR, 0xEA8561);
			//user can only read the calendar
			cv.put(Calendars.CALENDAR_ACCESS_LEVEL, Calendars.CAL_ACCESS_NONE);
			cv.put(Calendars.OWNER_ACCOUNT, getUserId());
			cv.put(Calendars.VISIBLE, 0);
			cv.put(Calendars.SYNC_EVENTS, 0);
			return cv;
		}
		
		private static Uri buildCalUri() {
			return CAL_URI
					.buildUpon()
					.appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
					.appendQueryParameter(Calendars.ACCOUNT_NAME, getUserId())
					.appendQueryParameter(Calendars.ACCOUNT_TYPE,
							CalendarContract.ACCOUNT_TYPE_LOCAL)
					.build();
		}
		
		private static Uri buildEventUri() {
			return EVENT_URI
					.buildUpon()
					.appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
					.appendQueryParameter(Calendars.ACCOUNT_NAME, getUserId())
					.appendQueryParameter(Calendars.ACCOUNT_TYPE,
							CalendarContract.ACCOUNT_TYPE_LOCAL)
					.build();
		}
	}
	public static class DatePickerFragment extends DialogFragment
    	implements DatePickerDialog.OnDateSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);
			
			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}
		
		public void onDateSet(DatePicker view, int year, int month, int day) {
		// Do something with the date chosen by the user
		}
}
}
