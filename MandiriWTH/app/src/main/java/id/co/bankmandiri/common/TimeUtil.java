package id.co.bankmandiri.common;

import android.text.format.DateUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * @author hendrawd on 10/19/16
 */

public class TimeUtil {

    public final static long ONE_SECOND = 1000;
    public final static long SECONDS = 60;

    public final static long ONE_MINUTE = ONE_SECOND * 60;
    public final static long MINUTES = 60;

    public final static long ONE_HOUR = ONE_MINUTE * 60;
    public final static long HOURS = 24;

    public final static long ONE_DAY = ONE_HOUR * 24;

    public static void setDefaultTimeZoneToGMT7(SimpleDateFormat formatter) {
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+7"));
    }

    /**
     * converts time (in milliseconds) to human-readable format
     * "<w> days, <x> hours, <y> minutes and (z) seconds"
     */
    public static String millisToLongDHMS(long duration) {
        StringBuffer res = new StringBuffer();
        long temp = 0;
        if (duration >= ONE_SECOND) {
            temp = duration / ONE_DAY;
            if (temp > 0) {
                duration -= temp * ONE_DAY;
                res.append(temp).append(" day").append(temp > 1 ? "s" : "")
                        .append(duration >= ONE_MINUTE ? ", " : "");
            }

            temp = duration / ONE_HOUR;
            if (temp > 0) {
                duration -= temp * ONE_HOUR;
                res.append(temp).append(" hour").append(temp > 1 ? "s" : "")
                        .append(duration >= ONE_MINUTE ? ", " : "");
            }

            temp = duration / ONE_MINUTE;
            if (temp > 0) {
                duration -= temp * ONE_MINUTE;
                res.append(temp).append(" minute").append(temp > 1 ? "s" : "");
            }

            if (!res.toString().equals("") && duration >= ONE_SECOND) {
                res.append(" and ");
            }

            temp = duration / ONE_SECOND;
            if (temp > 0) {
                res.append(temp).append(" second").append(temp > 1 ? "s" : "");
            }
            return res.toString();
        } else {
            return "0 second";
        }
    }

    public static int getDayDifference(String pastDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        setDefaultTimeZoneToGMT7(sdf);
        long millisInThePast;
        try {
            Date date = sdf.parse(pastDate);
            millisInThePast = date.getTime();
        } catch (Exception e) {
            millisInThePast = System.currentTimeMillis();
        }
        long millisNow = System.currentTimeMillis();
        return (int) TimeUnit.MILLISECONDS.toDays(millisNow - millisInThePast);
    }

    public static long stringToLongMillis(String time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        setDefaultTimeZoneToGMT7(sdf);
        long timeMillis;
        try {
            Date date = sdf.parse(time);
            timeMillis = date.getTime();
        } catch (Exception e) {
            timeMillis = System.currentTimeMillis();
        }
        return timeMillis;
    }

    /**
     * @param time relative time span string
     * @return String relative time span with custom timezone
     */
    public static String getRelativeTimeSpanString(String time) {
        return getRelativeTimeSpanString(stringToLongMillis(time));
    }

    /**
     * @param timeMillis relative time span string
     * @return String relative time span
     */
    public static String getRelativeTimeSpanString(long timeMillis) {
        int flags = DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_ABBREV_MONTH;
        long currentTimeMillis = System.currentTimeMillis();
        String relativeTimeSpanString;
        //it is a hacky implementation to implement the max transition resolution to 3 days
        //because i can't find other way to implement this with only standard library
        //the standard library just set the relative time span to a week without another option to change
        //the max transition resolution
        if (currentTimeMillis - timeMillis > DateUtils.DAY_IN_MILLIS * 3) {
            relativeTimeSpanString = DateUtils.getRelativeTimeSpanString(timeMillis,
                    currentTimeMillis + DateUtils.WEEK_IN_MILLIS + DateUtils.DAY_IN_MILLIS,
                    DateUtils.DAY_IN_MILLIS, flags).toString();
        } else {
            relativeTimeSpanString = DateUtils.getRelativeTimeSpanString(timeMillis, currentTimeMillis,
                    DateUtils.DAY_IN_MILLIS, flags).toString();
        }
        return relativeTimeSpanString;
    }

    /**
     * @return String timestamp, contoh : "2010-03-08 14:59:30.252"
     */
    public static String getCurrentTimeStamp() {
        java.util.Date date = new java.util.Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        return timestamp.toString();
    }

    /**
     * @return String timestamp tanpa mili detik, contoh : "2010-03-08 14:59:30"
     */
    public static String getCurrentDate() {
        String s = getCurrentTimeStamp();
        if (s.contains(".")) {
            s = s.split("\\.")[0];
        }
        return s;
    }

    /**
     * generating am/pm time like "12:22 PM"
     * input example : "2010-03-08 14:59:30"(from the getCurrentTimeStamp method)
     */
    public static String getAMPMHourTime(String input) {
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date inputDate = null;
        try {
            inputDate = inputFormat.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat outputFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        return outputFormat.format(inputDate);
    }
}
