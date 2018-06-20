package com.skcc.cloudz.zcp.common.util;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class TimestampUtil {
	
	public static String ISO8601ToDate(Object startsAt) {
		
		java.util.Date date = Date.from( Instant.parse((CharSequence) startsAt));
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
		String dateFormat = df.format(date);
		
		return dateFormat;
	}


}