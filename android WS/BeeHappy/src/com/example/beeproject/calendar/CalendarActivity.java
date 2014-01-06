package com.example.beeproject.calendar;

import hirondelle.date4j.DateTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.TimeZone;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.beeproject.R;
import com.example.beeproject.global.classes.GlobalVar;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

public class CalendarActivity extends FragmentActivity {
	private CaldroidFragment caldroidFragment;
	private final String TAG = "CalendarActivity";
	private void setCustomResourceForDates() {		
		if(!CalendarResolver.hasCalendar(getApplicationContext())) {
			Log.i(TAG, "Create Calendar");
			CalendarResolver.createCalendar(getApplicationContext());
		}
		else {
			Log.i(TAG, "Calendar Exist");
		}
		Calendar cal = Calendar.getInstance();

		cal.add(Calendar.DATE, -18);
		Date blueDate = cal.getTime();

		cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 16);
		Date greenDate = cal.getTime();

		if (caldroidFragment != null) {
			caldroidFragment.setBackgroundResourceForDate(R.color.blue,
					blueDate);
			caldroidFragment.setBackgroundResourceForDate(R.color.green,
					greenDate);
			caldroidFragment.setTextColorForDate(R.color.white, blueDate);
			caldroidFragment.setTextColorForDate(R.color.white, greenDate);
		}		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar);

		final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy", getResources().getConfiguration().locale);

		caldroidFragment = new CaldroidFragment();

		if (savedInstanceState != null) {
			caldroidFragment.restoreStatesFromKey(savedInstanceState,
					"CALDROID_SAVED_STATE");
		}
		else {
			Bundle args = new Bundle();
			Calendar cal = Calendar.getInstance();
			args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
			args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
			args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
			args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);
			args.putInt(CaldroidFragment.START_DAY_OF_WEEK, Calendar.MONDAY);
			caldroidFragment.setArguments(args);
		}
		DateTime start = DateTime.now(TimeZone.getDefault());
		Log.i(TAG,"start custom resource @" + start);
		setCustomResourceForDates();
		DateTime end = DateTime.now(TimeZone.getDefault());
		Log.i(TAG,"end custom resource @" + end);
		
		start = DateTime.now(TimeZone.getDefault());
		Log.i(TAG,"start refresh @" + start);
		FragmentTransaction t = getSupportFragmentManager().beginTransaction();
		t.replace(R.id.calendar1, caldroidFragment);
		t.commit();
		end = DateTime.now(TimeZone.getDefault());
		Log.i(TAG,"end refresh @" + end);

		// Setup listener
		final CaldroidListener listener = new CaldroidListener() {

			@Override
			public void onSelectDate(Date date, View view) {
				Toast.makeText(getApplicationContext(), formatter.format(date),
						Toast.LENGTH_SHORT).show();				
			}

			@Override
			public void onChangeMonth(int month, int year) {
				String text = "month: " + month + " year: " + year;
				Toast.makeText(getApplicationContext(), text,
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onLongClickDate(Date date, View view) {
				Toast.makeText(getApplicationContext(),
						"Long click " + formatter.format(date),
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onCaldroidViewCreated() {
				if (caldroidFragment.getLeftArrowButton() != null) {
					Toast.makeText(getApplicationContext(),
							"Caldroid view is created", Toast.LENGTH_SHORT)
							.show();
				}
			}
		};

		caldroidFragment.setCaldroidListener(listener);
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
			return (cur.getCount() > 0);
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
}
