package com.ifenduo.common.tool;

import android.text.TextUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

public class Utils {

    public static String stringForTime(int timeMs) {
        if (timeMs <= 0 || timeMs >= 24 * 60 * 60 * 1000) {
            return "00:00";
        }
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        StringBuilder mFormatBuilder = new StringBuilder();
        Formatter mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }
    public static String stringForTime(long timeMs) {
        Log.i("TAG","stringForTime:"+timeMs);
        if(timeMs <= 0) {
            return "00:00";
        }
        //超过一天
        if(timeMs >= 24 * 60 * 60 * 1000){
            long dayNum =timeMs/(24 * 60 * 60 * 1000);
            long hourNum=timeMs-dayNum*(60 * 60 * 1000);
            return dayNum+"天"+hourNum+"时";
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

    public static String stringForSecond(int totalSeconds){
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        StringBuilder mFormatBuilder = new StringBuilder();
        Formatter mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        return mFormatter.format("%02d'%02d\"", minutes, seconds).toString();

    }

    public static String getCurrentTime(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
    }

    public static String getMMDDHHMMFormatTime(String time){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date parse = format.parse(time);
            format =new SimpleDateFormat("MM-dd HH:mm");
            return format.format(parse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String getHHMMFormatTime(String time){
        if(TextUtils.isEmpty(time))return "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date parse = format.parse(time);
            format =new SimpleDateFormat("HH:mm");
            return format.format(parse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static long getDiffFormDateWithNow(String time){
        if(TextUtils.isEmpty(time))return 0;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {

            Date parse = format.parse(time);
            Date now = Calendar.getInstance().getTime();
            Log.d("TAG","time:"+time);
            Log.d("TAG","parse:"+parse.getTime());
            Log.d("TAG","now:"+now.getTime());
            return now.getTime()-parse.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static String getYYYYMMDDFormatTime(String time){
        if(TextUtils.isEmpty(time))return "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date parse = format.parse(time);
            format =new SimpleDateFormat("yyyy.MM.dd");
            return format.format(parse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }


    static String dayNames[] = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

    public static String getChatTime(String date){
        if(TextUtils.isEmpty(date))return "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date parse = format.parse(date);
            return getChatTime(parse.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getChatTime(long timesamp) {
        String result = "";
        Calendar todayCalendar = Calendar.getInstance();
        Calendar otherCalendar = Calendar.getInstance();
        otherCalendar.setTimeInMillis(timesamp);

        String timeFormat="M月d日 HH:mm";
        String yearTimeFormat="yyyy/M/d";
        String am_pm="";
        int hour=otherCalendar.get(Calendar.HOUR_OF_DAY);
        if(hour>=0&&hour<6){
            am_pm="凌晨";
        }else if(hour>=6&&hour<12){
            am_pm="早上";
        }else if(hour==12){
            am_pm="中午";
        }else if(hour>12&&hour<18){
            am_pm="下午";
        }else if(hour>=18){
            am_pm="晚上";
        }
        timeFormat="M月d日 "+ am_pm +"HH:mm";
        yearTimeFormat="yyyy年M月d日 "+ am_pm +"HH:mm";

        boolean yearTemp = todayCalendar.get(Calendar.YEAR)==otherCalendar.get(Calendar.YEAR);
        if(yearTemp){
            int todayMonth=todayCalendar.get(Calendar.MONTH);
            int otherMonth=otherCalendar.get(Calendar.MONTH);
            if(todayMonth==otherMonth){//表示是同一个月
                int temp=todayCalendar.get(Calendar.DATE)-otherCalendar.get(Calendar.DATE);
                switch (temp) {
                    case 0:
                        result = getHourAndMin(timesamp);
                        break;
                    case 1:
                        result = "昨天 ";
                        //result = "昨天 " + getHourAndMin(timesamp);
                        break;
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                        int dayOfMonth = otherCalendar.get(Calendar.WEEK_OF_MONTH);
                        int todayOfMonth=todayCalendar.get(Calendar.WEEK_OF_MONTH);
                        if(dayOfMonth==todayOfMonth){//表示是同一周
                            int dayOfWeek=otherCalendar.get(Calendar.DAY_OF_WEEK);
                            if(dayOfWeek!=1){//判断当前是不是星期日     如想显示为：周日 12:09 可去掉此判断
                                //result = dayNames[otherCalendar.get(Calendar.DAY_OF_WEEK)-1] + getHourAndMin(timesamp);
                                result = dayNames[otherCalendar.get(Calendar.DAY_OF_WEEK)-1];
                            }else{
                                result = getTime(timesamp,timeFormat);
                            }
                        }else{
                            result = getTime(timesamp,timeFormat);
                        }
                        break;
                    default:
                        result = getTime(timesamp,timeFormat);
                        break;
                }
            }else{
                result = getTime(timesamp,timeFormat);
            }
        }else{
            result=getYearTime(timesamp,yearTimeFormat);
        }
        return result;
    }

    /**
     * 当天的显示时间格式
     * @param time
     * @return
     */
    public static String getHourAndMin(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(new Date(time));
    }

    /**
     * 不同一周的显示时间格式
     * @param time
     * @param timeFormat
     * @return
     */
    public static String getTime(long time,String timeFormat) {
        SimpleDateFormat format = new SimpleDateFormat(timeFormat);
        return format.format(new Date(time));
    }

    /**
     * 不同年的显示时间格式
     * @param time
     * @param yearTimeFormat
     * @return
     */
    public static String getYearTime(long time,String yearTimeFormat) {
        SimpleDateFormat format = new SimpleDateFormat(yearTimeFormat);
        return format.format(new Date(time));
    }
}
