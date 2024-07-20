package com.zerobase.used_trade.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtility {
  private final static String DATETIME_FORMAT = "yyyy-MM-dd HH:mm";

  public static LocalDateTime stringToLocalDateTime(String string) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATETIME_FORMAT);
    try {
      return LocalDateTime.parse(string, formatter);
    } catch (Exception e) {
      return null;
    }
  }
}
