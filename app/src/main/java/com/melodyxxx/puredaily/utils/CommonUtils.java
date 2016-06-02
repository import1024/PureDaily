package com.melodyxxx.puredaily.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * CommonUtils
 * <p>
 * Created by hanjie on 2016/6/2.
 */
public class CommonUtils {

    public static String formatTime(long timeMillis, String pattern) {
        return new SimpleDateFormat(pattern).format(new Date(timeMillis));
    }

    public static String formatCommentTime(long timeMillis) {
        Calendar current = Calendar.getInstance();
        Calendar commentTime = Calendar.getInstance();
        commentTime.setTimeInMillis(timeMillis);
        if (current.get(Calendar.YEAR) == commentTime.get(Calendar.YEAR)) {
            if (current.get(Calendar.DAY_OF_YEAR) == commentTime.get(Calendar.DAY_OF_YEAR)) {
                // 同一天
                return "今天 " + new SimpleDateFormat("HH:mm").format(new Date(timeMillis));
            } else {
                // 同一年
                return new SimpleDateFormat("MM-dd HH:mm").format(new Date(timeMillis));
            }
        } else {
            // 不同一天
            return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(timeMillis));
        }
    }

    public static String formatResultDate(String resultDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        try {
            Date date = sdf.parse(resultDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return new SimpleDateFormat("MM月dd日 ").format(date) + getWeek(calendar.get(Calendar.DAY_OF_WEEK));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String formatDateForHistory(int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        return new SimpleDateFormat("yyyyMMdd").format(new Date(calendar.getTimeInMillis()));
    }

    private static String getWeek(int dayId) {
        String week = "";
        if (dayId == 1) {
            week = "周日";
        } else if (dayId == 2) {
            week = "周一";
        } else if (dayId == 3) {
            week = "周二";
        } else if (dayId == 4) {
            week = "周三";
        } else if (dayId == 5) {
            week = "周四";
        } else if (dayId == 6) {
            week = "周五";
        } else if (dayId == 7) {
            week = "周六";
        }
        return week;
    }
}

