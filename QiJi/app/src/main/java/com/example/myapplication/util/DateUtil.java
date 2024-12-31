package com.example.myapplication.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static String convertTimestampToDateString(long timestamp) {
        timestamp *= 1000;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        Date date = new Date(timestamp);
        return sdf.format(date);
    }

    public static String convertTimeToString(Long timestamp) {
        if (timestamp == null) {
            return "";
        }
        if (timestamp.toString().length() == 10) {
            timestamp *= 1000;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date(timestamp);
        return sdf.format(date);
    }

    public static String convertMinutesToFormat(int minutes) {
        int hours = minutes / 60;
        int remainingMinutes = minutes % 60;
        int seconds = remainingMinutes * 60;
        int finalSeconds = seconds % 60;

        return String.format("%d小时%d分钟%d秒", hours, remainingMinutes, finalSeconds);
    }

    public static long getTenDigitTimestamp() {
        return System.currentTimeMillis() / 1000;
    }
}
