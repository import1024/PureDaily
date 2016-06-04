package com.melodyxxx.puredaily.utils;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.util.TypedValue;

import com.melodyxxx.puredaily.R;
import com.melodyxxx.puredaily.ui.activity.HomeActivity;

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

    public static void restartApp(Context context) {
        ActivityCollector.finishAll();
        context.startActivity(new Intent(context, HomeActivity.class));
    }

    public static int getThemePrimaryColor(Context context) {
        final TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorPrimary, value, true);
        return value.data;
    }

    public static int getSkinStyle(int pos) {
        int style = 0;
        switch (pos) {
            case 0:
                style = R.style.AppTheme_Blue;
                break;
            case 1:
                style = R.style.AppTheme_Light_Blue;
                break;
            case 2:
                style = R.style.AppTheme_Pink;
                break;
            case 3:
                style = R.style.AppTheme_Red;
                break;
            case 4:
                style = R.style.AppTheme_Purple;
                break;
            case 5:
                style = R.style.AppTheme_Deep_Purple;
                break;
            case 6:
                style = R.style.AppTheme_Teal;
                break;
            case 7:
                style = R.style.AppTheme_Deep_Orange;
                break;
            case 8:
                style = R.style.AppTheme_Green;
                break;
            case 9:
                style = R.style.AppTheme_Cyan;
                break;
            case 10:
                style = R.style.AppTheme_Orange;
                break;
            case 11:
                style = R.style.AppTheme_Indigo;
                break;
            case 12:
                style = R.style.AppTheme_Brown;
                break;
            case 13:
                style = R.style.AppTheme_Blue_Gray;
                break;
            case 14:
                style = R.style.AppTheme_Amber;
                break;
        }
        return style;
    }

    public static boolean nowIsDay(Context context) {
        return isExistInterval(6, 0, 18, 0) ? true : false;
    }


    public static boolean isExistInterval(int startHour, int startMinuteOfHour, int endHour, int endMinuteOfHour) {
        boolean isExist = false;
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);// 获取小时,自动24小时制
        int minute = cal.get(Calendar.MINUTE);// 获取分钟
        int minuteOfDay = hour * 60 + minute;// 从0:00分开是到目前为止的分钟数
        int start = startHour * 60 + startMinuteOfHour;
        int end = endHour * 60 + endMinuteOfHour;
        isExist = minuteOfDay >= start && minuteOfDay < end;
        return isExist;
    }

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

    /**
     * 获取当前App版本号:versionName
     *
     * @return 返回当前App的版本号
     */
    public static String getVersionName(Context context) {
        // 得到包管理器
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取当前App版本号:versionName
     *
     * @return 返回当前App的版本号
     */
    public static int getVersionCode(Context context) {
        // 得到包管理器
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static void jumpTo(Activity activity, String url) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        activity.startActivity(intent);
    }

    public static void copy2Clipboard(Activity activity, String content) {
        ClipboardManager clipboardManager = (ClipboardManager) activity.getSystemService(Activity.CLIPBOARD_SERVICE);
        clipboardManager.setText(content);
    }

}

