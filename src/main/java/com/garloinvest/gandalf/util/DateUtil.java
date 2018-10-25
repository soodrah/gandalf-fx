package com.garloinvest.gandalf.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.StringUtils;

import com.oanda.v20.primitives.DateTime;

public class DateUtil {

	public static LocalDateTime convertFromOandaDateTimeToJavaLocalDateTime(DateTime oandaDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        return LocalDateTime.parse(oandaDateTime.toString(),formatter);
    }

	public static String formatDateToCsvFile(String localTime) {
		return StringUtils.replace(localTime.toString(),":","-").toString();
	}
}
