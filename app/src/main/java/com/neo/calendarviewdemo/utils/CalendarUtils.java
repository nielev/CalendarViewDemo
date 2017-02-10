package com.neo.calendarviewdemo.utils;


import android.content.Context;
import android.util.TypedValue;

import com.neo.calendarviewdemo.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Neo on 2017/1/17.
 */

public class CalendarUtils {
    /**
     * dp to px
     * @param dp
     * @param context
     * @return
     */
    public static int dp2px(int dp,Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    /**
     * 获取当月第一天是星期几
     * @return
     */
    public static String getFistWeekDayOfMonth(Context ctx) {
        String[] array = ctx.getResources().getStringArray(R.array.weeks);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int week = calendar.get(Calendar.DAY_OF_WEEK);
//        SimpleDateFormat format = new SimpleDateFormat("E");//local
//        format.format(calendar.getTime())
        return array[week - 1];
    }

    /**
     * 获取当前月份天数
     * @return
     */
    public static int getCurrentMonthDays() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, 1);
        calendar.roll(Calendar.DATE, -1);
        int maxDate = calendar.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 获取当前月份天数集合
     * @return
     */
    public static List<String> getCurrentMonthDaysList() {
        int count = getCurrentMonthDays();
        List<String> list = new ArrayList<>();
        for(int i = 1; i <= count; i++) {
            list.add(String.valueOf(i));
        }
        return list;
    }

    /**
     * 获取当前月份显示名称
     * @return
     */
    public static String getCurrentMonthName(Context ctx) {
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH) + 1;//获取月份，0表示1月份
        int year = c.get(Calendar.YEAR);//获取年份
        return getMonthName(ctx, year,month);
    }

    /**
     * 获取当前月份显示名称
     * @return
     */
    public static String getMonthName(Context ctx, int year, int month) {
        String[] array = ctx.getResources().getStringArray(R.array.months);
        String monthName = array[month - 1];
        return monthName + " " + year;
    }

    /**
     * 根据年 月 获取对应的月份 天数
     * */
    public static int getDaysByYearMonth(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 获取指定月份的日期数
     * @param year
     * @param month
     * @return
     */
    public static List<String> getMonthDaysList(int year, int month) {
        int count = getDaysByYearMonth(year, month);
        List<String> list = new ArrayList<>();
        for(int i = 1; i <= count; i++) {
            list.add(String.valueOf(i));
        }
        return list;
    }
    /**
     * 根据日期 找到对应日期的 星期
     *
     */
    public static String getDayOfWeekByDate(Context ctx,Date date) {

        String[] array = ctx.getResources().getStringArray(R.array.weeks);
        Calendar calendar = Calendar.getInstance();
        if (null != date) {
            calendar.setTime(date);
        }
        int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;//1为星期日
        if(week < 0) {
            week = 0;
        }
        return array[week];
    }

    /**
     * yyyy-MM-dd
     * @param date
     * @return
     */
    public static String getDayOfWeekByDate(Context ctx, String date) {
        Date tansDate = stringToDate(date);
        return getDayOfWeekByDate(ctx, tansDate);
    }

//    public static String dateToString(Date date) {
//
//    }

    public static Date stringToDate(String date) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date parse = format.parse(date);
            return parse;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String intToString(int year, int month, int day) {
        String y = String.valueOf(year);
        String m = String.valueOf(month);
        String d = String.valueOf(day);
        if (m.length() < 2) {
            m = "0" + m;
        }
        if (d.length() < 2) {
            d = "0" + d;
        }
        return y+"-"+m+"-"+d;
    }
}
