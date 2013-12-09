package com.example.beeproject.calendar;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;

public class Calendar {	
	private Uri CAL_URI = CalendarContract.Calendars.CONTENT_URI;
	private String accountName = null;
	private String calendarName = null;
	
	public Uri getCAL_URI() {
		return CAL_URI;
	}

	public void setCAL_URI(Uri cAL_URI) {
		CAL_URI = cAL_URI;
	}
	
	public Calendar(String accountName, String calendarName) {
		
	}
	public ContentValues buildNewCalContentValues() {
	    final ContentValues cv = new ContentValues();
	    cv.put(Calendars.ACCOUNT_NAME, accountName);
	    cv.put(Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
	    cv.put(Calendars.NAME, calendarName);
	    cv.put(Calendars.CALENDAR_DISPLAY_NAME, calendarName);
	    cv.put(Calendars.CALENDAR_COLOR, 0xEA8561);
	    //user can only read the calendar
	    cv.put(Calendars.CALENDAR_ACCESS_LEVEL, Calendars.CAL_ACCESS_READ);
	    cv.put(Calendars.OWNER_ACCOUNT, accountName);
	    cv.put(Calendars.VISIBLE, 1);
	    cv.put(Calendars.SYNC_EVENTS, 1);
	    return cv;
	}
	
	public Uri buildCalUri() {
	    return getCAL_URI()
	            .buildUpon()
	            .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
	            .appendQueryParameter(Calendars.ACCOUNT_NAME, accountName)
	            .appendQueryParameter(Calendars.ACCOUNT_TYPE,
	                        CalendarContract.ACCOUNT_TYPE_LOCAL)
	            .build();
	}
	
	public void createCalendar(Context ctx) {
	    ContentResolver cr = ctx.getContentResolver();
	    final ContentValues cv = buildNewCalContentValues();
	    Uri calUri = buildCalUri();
	    cr.insert(calUri, cv);
	}
	
	public void deleteCalendar(Context ctx) {
		ContentResolver cr = ctx.getContentResolver();
		Uri newUri = cr.insert(buildCalUri(), cv);
		long CAL_ID = Long.parseLong(newUri.getLastPathSegment());
		Uri calUri = ContentUris.withAppendedId(buildCalUri(), CAL_ID);
		cr.delete(calUri, null, null);
	}
}
