package com.garloinvest.gandalf.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.oanda.v20.primitives.DateTime;

public class DateUtil {

	public static LocalDateTime convertFromOandaDateTimeToJavaLocalDateTime(DateTime oandaDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        return LocalDateTime.parse(oandaDateTime.toString(),formatter);
    }
}
