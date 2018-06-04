package com.gp.tools;

import java.util.Calendar;

/**
 * Created by Administrator on 2018/6/4.
 */

public class CalendarUtil {
    public static int getYear(){
        Calendar calendar=Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }
    public static int getMonth(){
        Calendar calendar=Calendar.getInstance();
        return calendar.get(Calendar.MONTH)+1;
    }
    public static int getDay(){
        Calendar calendar=Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_MONTH);
    }
}
