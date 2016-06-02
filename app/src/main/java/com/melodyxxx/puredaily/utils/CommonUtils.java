package com.melodyxxx.puredaily.utils;

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
            if (current.get(Calendar.DAY_OF_YEAR) == current.get(Calendar.DAY_OF_YEAR)) {
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

}
