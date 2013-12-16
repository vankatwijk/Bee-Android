package com.example.beeproject.calendar;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;
import android.util.Log;

public final class CalendarContentResolver {	
	private final static String tag = "Calendar";
	private static Uri CAL_URI = CalendarContract.Calendars.CONTENT_URI;
	
	public static Uri getCAL_URI() {
		return CAL_URI;
	}

	public static void setCAL_URI(Uri calUri) {
		CAL_URI = calUri;
	}
	
	public static ContentValues buildNewCalContentValues(String accountName, String calendarName) {
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
	
	public static Uri buildCalUri(String accountName) {
	    return getCAL_URI()
	            .buildUpon()
	            .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
	            .appendQueryParameter(Calendars.ACCOUNT_NAME, accountName)
	            .appendQueryParameter(Calendars.ACCOUNT_TYPE,
	                        CalendarContract.ACCOUNT_TYPE_LOCAL)
	            .build();
	}
	
	public static Uri buildCalUri(String accountName, String calendarName) {
	    return getCAL_URI()
	            .buildUpon()
	            .appendQueryParameter(Calendars.ACCOUNT_NAME, accountName)
				.appendQueryParameter(Calendars.NAME, calendarName)
	            .appendQueryParameter(Calendars.ACCOUNT_TYPE,
	                        CalendarContract.ACCOUNT_TYPE_LOCAL)
				.build();
	}
	
	public static Uri create(Context ctx, String accountName, String calendarName) {
	    ContentResolver cr = ctx.getContentResolver();
	    final ContentValues cv = buildNewCalContentValues(accountName, calendarName);
	    Uri calUri = buildCalUri(accountName);
	    Log.i(tag, calUri.toString());
	    return cr.insert(calUri, cv);
	}
	
	public static int delete(Context ctx, String accountName, String calendarName) {
		ContentResolver cr = ctx.getContentResolver();
		Uri calUri = buildCalUri(accountName, calendarName);
		Log.i(tag, calUri.toString());
		return cr.delete(calUri, null, null);
	}
}
