package com.example.zyf.timetable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateHelper {
    static int year, month, day;
    static int startYear, startMonth, startDay;
    static int endYear, endMonth, endDay;
    static int currentWeek=1, startWeek=1, endWeek=20;//currentWeek:0-Sunday, 1-Monday, etc, 6-Saturday.
    static int currentWeekday;
    static int daysPerWeek=5, classesPerDay=12;
    static List<Integer> weekList;

    static boolean isDateSet=false;
    static boolean isWeekSet=false;

    public static void setDate(int year, int month, int day){
        DateHelper.year = year;
        DateHelper.month = month;
        DateHelper.day = day;
        Calendar today = Calendar.getInstance();
        today.set(year, month, day);
        currentWeek = today.get(Calendar.DAY_OF_WEEK)-1;
        isDateSet=true;

    }

    public static void setWeek(Calendar startDate, Calendar endDate, int daysPerWeek, int classesPerDay){
        startYear=startDate.get(Calendar.YEAR);
        startMonth=startDate.get(Calendar.MONTH)+1;
        startDay=startDate.get(Calendar.DAY_OF_MONTH);
        endYear=endDate.get(Calendar.YEAR);
        endMonth=endDate.get(Calendar.MONTH)+1;
        endDay=endDate.get(Calendar.DAY_OF_MONTH);
        DateHelper.daysPerWeek=daysPerWeek;
        DateHelper.classesPerDay=classesPerDay;

        endWeek=startWeek+countEndWeek(startDate, endDate);
        setCurrentWeek(startDate);

    }

    /**
     * 给weekList分配空间及初始化内容的方法。
     */
    public static void initWeekList(){
        weekList = new ArrayList<>();
        for (int i=startWeek;i<=endWeek;i++){
            weekList.add(i);
        }
    }

    public static int countEndWeek(Calendar startDate, Calendar endDate){
        int end=0;
        //设置endWeek
        while (startDate.before(endDate)) {
            // 如果开始日期和结束日期在同年、同月且当前月的同一周时结束循环
            if (startDate.get(Calendar.YEAR) == endDate
                    .get(Calendar.YEAR)
                    && startDate.get(Calendar.MONTH) == endDate
                    .get(Calendar.MONTH)
                    && startDate.get(Calendar.DAY_OF_WEEK_IN_MONTH) == endDate
                    .get(Calendar.DAY_OF_WEEK_IN_MONTH)) {
                break;
            } else {
                startDate.add(Calendar.DAY_OF_YEAR, 7);
                end += 1;
            }
        }
        return end;
    }

    public static void setCurrentWeek(Calendar startDate) {
        Calendar today = Calendar.getInstance();
        today.set(year, month, day);
        currentWeek = startWeek+countEndWeek(startDate, today);
    }

    public static void countCurrentWeekday(int currentWeekday) {
        DateHelper.currentWeekday = currentWeekday;
    }
}
