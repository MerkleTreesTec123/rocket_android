package com.ifenduo.common.tool;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by xuefengyang on 2016/3/25.
 */
public class DateTimeTool {

    private static int MILL_MIN = 1000 * 60;
    private static int MILL_HOUR = MILL_MIN * 60;
    private static int MILL_DAY = MILL_HOUR * 24;

    private static Calendar msgCalendar = null;
    private static SimpleDateFormat dayFormat = null;
    private static SimpleDateFormat dateFormat = null;
    private static SimpleDateFormat yearFormat = null;

    public final static String PATTERN_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public final static String PATTERN_YYYY_MM_DD_CHAR = "yyyy年MM月dd日";

    public final static String PATTERN_YYYY_MM_DD_HH_MM= "yyyy-MM-dd HH:mm";

    public final static String PATTERN_YYYY_MM_DD = "yyyy-MM-dd";
    public final static String PATTERN_MM_DD_HH_MM = "MM-dd HH:mm";

    public static final String PATTERN_YYYY="yyyy";
    public static final String PATTERN_MM_DD="MM-dd";


    private DateTimeTool() {

    }

    /*public static String getListTime(ItemBean bean) {
        long msg = 0L;

        if (bean.getMills() != 0) {
            msg = bean.getMills();
        } else {
            DateUtility.dealMills(bean);
            msg = bean.getMills();
        }
        return getListTime(msg);
    }*/

    private static boolean isSameHalfDay(Calendar now, Calendar msg) {
        int nowHour = now.get(Calendar.HOUR_OF_DAY);
        int msgHOur = msg.get(Calendar.HOUR_OF_DAY);

        if (nowHour <= 12 & msgHOur <= 12) {
            return true;
        } else if (nowHour >= 12 & msgHOur >= 12) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean isSameDay(Calendar now, Calendar msg) {
        int nowDay = now.get(Calendar.DAY_OF_YEAR);
        int msgDay = msg.get(Calendar.DAY_OF_YEAR);

        return nowDay == msgDay;
    }

    private static boolean isYesterDay(Calendar now, Calendar msg) {
        int nowDay = now.get(Calendar.DAY_OF_YEAR);
        int msgDay = msg.get(Calendar.DAY_OF_YEAR);

        return (nowDay - msgDay) == 1;
    }

    private static boolean isTheDayBeforeYesterDay(Calendar now, Calendar msg) {
        int nowDay = now.get(Calendar.DAY_OF_YEAR);
        int msgDay = msg.get(Calendar.DAY_OF_YEAR);

        return (nowDay - msgDay) == 2;
    }

    private static boolean isSameYear(Calendar now, Calendar msg) {
        int nowYear = now.get(Calendar.YEAR);
        int msgYear = msg.get(Calendar.YEAR);
        return nowYear == msgYear;
    }

    public static long dealMills(String time) {
        Date date = new Date(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getTimeInMillis();
    }   

    public static String getYYYYMMDDHHMMSS(long mills) {
        if (msgCalendar == null) {
            msgCalendar = Calendar.getInstance();
        }
        mills *= 1000;
        msgCalendar.setTimeZone(TimeZone.getDefault());
        msgCalendar.setTime(new Date(mills));
        return new SimpleDateFormat(PATTERN_YYYY_MM_DD_HH_MM_SS).format(msgCalendar.getTime());
    }
    public static String getYYYYMMDDHHMM(long mills) {
        if (msgCalendar == null) {
            msgCalendar = Calendar.getInstance();
        }
        mills *= 1000;
        msgCalendar.setTimeZone(TimeZone.getDefault());
        msgCalendar.setTime(new Date(mills));
        return new SimpleDateFormat(PATTERN_YYYY_MM_DD_HH_MM).format(msgCalendar.getTime());
    }

    public static String getCurrentDate(String pattern){
        Calendar instance = Calendar.getInstance();
        return new SimpleDateFormat(pattern).format(instance.getTime());
    }

    public static String formatTimeWithPattern(long mills, String pattern) {
        if (msgCalendar == null) {
            msgCalendar = Calendar.getInstance();
        }
        mills *= 1000;
        msgCalendar.setTimeZone(TimeZone.getDefault());
        msgCalendar.setTime(new Date(mills));
        return new SimpleDateFormat(pattern).format(new Date(mills));
    }


    public static String stringForTime(long timeMs) {
        if (timeMs <= 0 || timeMs >= 24 * 60 * 60 * 1000) {
            return "00:00";
        }
        long totalSeconds = timeMs / 1000;
        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds / 60) % 60;
        long hours = totalSeconds / 3600;
        StringBuilder mFormatBuilder = new StringBuilder();
        Formatter mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    public static String stringForSecond(int totalSeconds) {
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        StringBuilder mFormatBuilder = new StringBuilder();
        Formatter mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        return mFormatter.format("%02d'%02d\"", minutes, seconds).toString();

    }
}
