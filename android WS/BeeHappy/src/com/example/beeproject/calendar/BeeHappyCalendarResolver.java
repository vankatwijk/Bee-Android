package com.example.beeproject.calendar;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
	
	/**
	 * creates an event.
	 * content values (cv) should contain the following keys : <BR>
	 * Events.CALENDAR_ID, Events.TITLE, Events.DTSTART, Events.DTEND, Events.DESCRIPTION, Events.EVENT_TIMEZONE 
	 * @param cv
	 * @return
	 */
	public long createEvent(ContentValues cv) {
		String title = cv.getAsString(Events.TITLE);
		String event_start = String.valueOf(cv.getAsLong(Events.DTSTART));
		String event_end = String.valueOf(cv.getAsLong(Events.DTEND));
		String notes = String.valueOf(cv.getAsLong(Events.DESCRIPTION));
		String timezone = String.valueOf(cv.getAsString(Events.EVENT_TIMEZONE));
		Uri newEvent = _ContentResolver.insert(buildEventUri(), cv);
		Log.i(TAG, "New event : " + title + " - " + event_start + " - " + event_end + " - " + notes + " - " + timezone);
		return Long.parseLong(newEvent.getLastPathSegment());
	}
	
	/**
	 * get the events of a set date with default locale
	 * @param date
	 * @return
	 */
	public Cursor getDateEvents(Date date) {
		Calendar c = GregorianCalendar.getInstance();
		Calendar c1 = GregorianCalendar.getInstance();
		c.setTime(date);
		c1.setTime(date);
		// add one day
		c1.add(Calendar.DATE, 1);
		String calendarId = String.valueOf(_CalendarId);
		String beginTime = String.valueOf(c.getTimeInMillis());
		String endTime = String.valueOf(c1.getTimeInMillis());
		Log.i(TAG, "Retrieve events between "+ beginTime + " and " + endTime);
		// Event should be in the calendar and the event should occur start or end in the date specified
		String selection = Events.CALENDAR_ID + "= ? AND " + Events.DTSTART + " >= ? AND "	+ Events.DTSTART + " < ? OR " + 
		Events.DTEND + ">= ? AND " + Events.DTEND + " < ?" ;
		String[] selectionArgs = new String[] {calendarId, beginTime, endTime, beginTime, endTime};
		return _ContentResolver.query(EVENT_URI, DEFAULT_PROJECTION, selection, selectionArgs, null);
	}
	
	/**
	 * @param date
	 * @return
	 */
	public boolean hasDateEvents(Date date) {
		Cursor cursor = getDateEvents(date);
		int count = cursor.getCount();
		cursor.close();
		return count > 0;		
	}
	
	public ContentValues getEvent(long calendarId, long eventId) {
		
		String selection = Events.CALENDAR_ID + " = ? AND " + Events._ID + " = ?";
		String[] selectionArgs = new String[] {String.valueOf(calendarId), String.valueOf(eventId)};
		Cursor cursor = _ContentResolver.query(EVENT_URI, DEFAULT_PROJECTION, selection, selectionArgs, null);
		cursor.moveToFirst();
		ContentValues cv = new ContentValues();
		try{
			for(int i = 0; i < cursor.getColumnCount(); i++) {
				String key = DEFAULT_PROJECTION[i];
				int columnIndex = cursor.getColumnIndex(key);
				cv.put(key, cursor.getString(columnIndex));
			}
		}
		finally {
			cursor.close();
		}
		return cv;		
	}
	
	/**
	 * deletes an event.
	 * @param eventId
	 */
	public void deleteEvent(long eventId) {
		String where = Events._ID + " = ?";
		String[] selectionArgs = new String[] {String.valueOf(eventId)};
		_ContentResolver.delete(EVENT_URI, where, selectionArgs);
	}
	
	private long createCalendar() {
		Uri newCalendarUri = _ContentResolver.insert(buildCalendarUri(), NewCalendarContentValues());
		return Long.parseLong(newCalendarUri.getLastPathSegment());
	}	
	/**
	 * content values for a new calendar
	 * @return
	 */
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
