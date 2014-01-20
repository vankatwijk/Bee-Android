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

import com.example.beeproject.global.classes.GlobalVar;

/**
 * Implemented version of 
 * http://www.derekbekoe.co.uk/blog/item/16-using-the-android-4-0-calendar-api
 * TODO refactor this in two seperate classes. One calendar resolver and one bee happy version. 
 */
public class BeeHappyCalendarResolver {
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
	
	public static long getCalendar(Context ctx) {
		final String[] CALENDARS_PROPS_PROJECTION = { Calendars._ID, Calendars.NAME, Calendars.CALENDAR_DISPLAY_NAME, 
				Calendars.CALENDAR_TIME_ZONE, Calendars.DELETED };
		ContentResolver cr = ctx.getContentResolver();
		Cursor cur = cr.query(buildCalUri(), CALENDARS_PROPS_PROJECTION, Calendars.NAME + " = ?", new String[] { CALENDAR_NAME }, null);
		if(cur.moveToFirst())
		{
			String calendarId = cur.getString(0);
			return Long.parseLong(calendarId);
		}
		throw new IllegalStateException("Couldn't move to first");
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
	 * required entries in CV and example: <BR> 
	cv.put(Events.CALENDAR_ID, _CalendarId); <BR>
	cv.put(Events.TITLE, event_title); <BR>
	cv.put(Events.DTSTART, event_start); <BR>
	cv.put(Events.DTEND, event_end); <BR>
	cv.put(Events.DESCRIPTION, event_notes); <BR>
	cv.put(Events.EVENT_TIMEZONE, TimeZone.getDefault().getID()); <BR>		 
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
	
	public static Cursor getEvent(Context ctx, String selection, String[] selectionArgs) {			 
		ContentResolver cr = ctx.getContentResolver();
		final String[] PROJECTION = new String[] { Events._ID };
		Cursor cursor = cr.query(buildEventUri(), PROJECTION, selection, selectionArgs, null);
		return cursor;
	}
	
	public static int getDateEvents(Context ctx, Date date) {		
		ContentResolver cr = ctx.getContentResolver();
		Calendar c = Calendar.getInstance();
		Calendar c1 = Calendar.getInstance();
		c.setTime(date);
		c1.setTime(date);
		// add one day
		c1.add(Calendar.DATE, 1);
		String beginTime = String.valueOf(c.getTimeInMillis());
		String endTime = String.valueOf(c1.getTimeInMillis());
		Log.i(TAG, beginTime + endTime);
		
		String[] projection = new String[] { Events.CALENDAR_ID, Events.TITLE, Events.DESCRIPTION, Events.DTSTART, Events.DTEND};
		String selection = Events.DTSTART + " BETWEEN ? AND ?";
		String[] selectionArgs = new String[] {beginTime, endTime};
		Cursor cursor = cr.query(EVENT_URI, projection, selection, selectionArgs, null);
		cursor.moveToFirst();
		return cursor.getCount();
	}
	
	public static int getAllEvents(Context ctx) {
		ContentResolver cr = ctx.getContentResolver();
		//cr.query(EVENT_URI, projection, selection, selectionArgs, sortOrder)
		return 0;
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