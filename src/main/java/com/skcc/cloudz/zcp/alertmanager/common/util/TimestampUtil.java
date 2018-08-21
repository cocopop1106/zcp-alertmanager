package com.skcc.cloudz.zcp.alertmanager.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

public class TimestampUtil {
	
	public static String ISO8601ToDate(Object startsAt) {
		
		ISO8601DateFormat df = new ISO8601DateFormat();
		String dateFormat = "";
		try {
			Date d = df.parse(startsAt.toString());
			
			SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
			dateFormat = sDateFormat.format(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
//		java.util.Date date = Date.from( Instant.parse((CharSequence) startsAt));
//		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
//		String dateFormat = df.format(date);
		
		return dateFormat;
	}


}