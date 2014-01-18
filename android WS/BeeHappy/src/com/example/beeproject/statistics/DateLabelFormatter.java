package com.example.beeproject.statistics;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.jjoe64.graphview.CustomLabelFormatter;

/**
 * Custom label formatter for statistics graphs values.
 * Converts timestamp to string date
 * @author Olya
 *
 */
public class DateLabelFormatter implements CustomLabelFormatter{

	@Override
	public String formatLabel(double value, boolean isValueX) {
		if(isValueX){
			//convert timestamp to date string
			Long timestamp = (long) value;
			Date date = new Date(timestamp);
			System.out.println("timestamp " + timestamp + " date: " + date); 
			DateFormat df = new SimpleDateFormat("dd.MM.yy");
			String dateString = df.format(date);
			return dateString;
		}

		//Y-values dont need to be converted
		return null;
	}

}
