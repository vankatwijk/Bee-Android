package com.example.beeproject.calendar;

import java.util.Calendar;
import java.util.Date;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.util.Log;

/**
 * class that manages the content resolver of the calendar Android API 14
 * @author Harold
 *
 */
public class BeeHappyCalendarResolver {
	private String TAG = "BeeHappyCalendarResolver";
	
	private ContentResolver _ContentResolver;
	private long _CalendarId;
	private int _UserId;
	
	private String _CalendarName; 
	private static final Uri CALENDAR_URI = CalendarContract.Calendars.CONTENT_URI;
	private static final Uri EVENT_URI = CalendarContract.Events.CONTENT_URI;
	
	private final String[] DEFAULT_PROJECTION = new String[] 
			{ Events.CALENDAR_ID, Events._ID, Events.TITLE, Events.DTSTART, Events.DTEND, Events.DESCRIPTION};	

	public BeeHappyCalendarResolver(Context ctx, int userId) {
		_ContentResolver = ctx.getContentResolver();
		_UserId = userId;
		_CalendarName = "BeeHappy" + userId;
		_CalendarId = getCalendar();
		Log.i(TAG, "new Resolver with " +_CalendarName + " - " + _CalendarId);
	}
	
	public long getCalendarId() {
		return _CalendarId;
	}
	/**
	 * gets a calendar, if it doesn't exist it will create a new one.
	 * @return
	 */
	private long getCalendar() {
		String[] calendarProjection = { Calendars._ID, Calendars.NAME, Calendars.CALENDAR_DISPLAY_NAME, 
				Calendars.CALENDAR_TIME_ZONE, Calendars.DELETED };
		String selection = Calendars.NAME + " = ?";
		String[] selectionArgs = new String[] { _CalendarName };
		Cursor cursor = _ContentResolver.query(CALENDAR_URI, calendarProjection, selection, selectionArgs, null);
		long calendarId;
		try {
			cursor.moveToFirst();			
			switch (cursor.getCount()) {
			case 0:
				Log.i(TAG, "Create new Calendar");
				calendarId = createCalendar();
				break;
			case 1:
				Log.i(TAG, "Retrieving Calendar");
				calendarId = cursor.getLong(cursor.getColumnIndex(Calendars._ID));
				break;
			default:
				throw new IllegalStateException("more than one beehappy calendar, action required");
			}
		}
		finally {
			cursor.close();
		}
		return calendarId;		
	}
	
	public long createEvent(ContentValues cv) {
		Uri newEvent = _ContentResolver.insert(buildEventUri(), cv);
		Log.i(TAG, "New event : " + newEvent.getLastPathSegment());
		return Long.parseLong(newEvent.getLastPathSegment());
	}
	
	/**
	 * get the events of a set date
	 * @param date
	 * @return
	 */
	public Cursor getDateEvents(Date date) {
		Calendar c = Calendar.getInstance();
		Calendar c1 = Calendar.getInstance();
		c.setTime(date);
		c1.setTime(date);
		// add one day
		c1.add(Calendar.DATE, 1);
		String calendarId = String.valueOf(_CalendarId);
		String beginTime = String.valueOf(c.getTimeInMillis());
		String endTime = String.valueOf(c1.getTimeInMillis());
		Log.i(TAG, "Retrieve events between "+ beginTime + " and " + endTime);		
		
		String selection = Events.DTSTART + " >= ? AND "
				+ Events.DTSTART + " <= ?";
		String[] selectionArgs = new String[] {beginTime, endTime};
		return _ContentResolver.query(EVENT_URI, DEFAULT_PROJECTION, selection, selectionArgs, null);
	}
	
	private long createCalendar() {
		Uri newCalendarUri = _ContentResolver.insert(buildCalendarUri(), NewCalendarContentValues());
		return Long.parseLong(newCalendarUri.getLastPathSegment());
	}	
	
	private ContentValues NewCalendarContentValues() {
		final ContentValues cv = new ContentValues();
		cv.put(Calendars.ACCOUNT_NAME, _UserId);
		cv.put(Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
		cv.put(Calendars.NAME, _CalendarName);
		cv.put(Calendars.CALENDAR_DISPLAY_NAME, _CalendarName);
		cv.put(Calendars.CALENDAR_COLOR, 0xEA8561);
		//user can only read the calendar
		cv.put(Calendars.CALENDAR_ACCESS_LEVEL, Calendars.CAL_ACCESS_NONE);
		cv.put(Calendars.OWNER_ACCOUNT, _UserId);
		cv.put(Calendars.VISIBLE, 0);
		cv.put(Calendars.SYNC_EVENTS, 0);
		return cv;
	}
	
	/**
	 * builds an uri to add a new calendar
	 * @return
	 */
	private Uri buildCalendarUri() {
		return CALENDAR_URI
				.buildUpon()
				.appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
				.appendQueryParameter(Calendars.ACCOUNT_NAME, String.valueOf(_UserId))
				.appendQueryParameter(Calendars.ACCOUNT_TYPE,
						CalendarContract.ACCOUNT_TYPE_LOCAL)
				.build();
	}
	
	/**
	 * builds an uri to add a new event
	 * @return
	 */
	private Uri buildEventUri() {
		return EVENT_URI
				.buildUpon()
				.appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
				.appendQueryParameter(Calendars.ACCOUNT_NAME, String.valueOf(_UserId))
				.appendQueryParameter(Calendars.ACCOUNT_TYPE,
						CalendarContract.ACCOUNT_TYPE_LOCAL)
				.build();
	}
}
