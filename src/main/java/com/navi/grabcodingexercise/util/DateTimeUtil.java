package com.navi.grabcodingexercise.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {

    public static String getCurrentTimeStamp() {
        return DateTimeFormatter.ofPattern("yyyyMMdd-hhmmss").format(LocalDateTime.now());
    }
}
