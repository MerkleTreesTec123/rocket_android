package com.github.tifezh.kchartlib.chart.formatter;

import android.util.Log;

import com.github.tifezh.kchartlib.chart.base.IDateTimeFormatter;
import com.github.tifezh.kchartlib.utils.DateUtil;

import java.util.Calendar;
import java.util.Date;

/**
 * 时间格式化器
 * Created by tifezh on 2016/6/21.
 */

public class DateFormatter implements IDateTimeFormatter {
    @Override
    public String format(Date date) {
        if(date==null)return "";
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());
        Calendar calendar_=Calendar.getInstance();

        if (calendar.get(Calendar.YEAR) == calendar_.get(Calendar.YEAR)) {
            return DateUtil.longTimeFormat_.format(date);
        } else {
            return DateUtil.longTimeFormat.format(date);
        }
    }
}
