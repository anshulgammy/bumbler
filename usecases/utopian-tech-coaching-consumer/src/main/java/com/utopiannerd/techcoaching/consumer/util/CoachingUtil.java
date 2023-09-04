package com.utopiannerd.techcoaching.consumer.util;

import static java.util.Objects.requireNonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CoachingUtil {

  public static final String DATE_FORMAT = "yyyy-MM-dd";
  public static final DateFormat DATE_FORMATTER = new SimpleDateFormat(DATE_FORMAT);

  public static Date toDate(String dateString) throws ParseException {
    requireNonNull(dateString, "dateString is required and cannot be null or empty");
    return DATE_FORMATTER.parse(dateString);
  }
}
