package com.example.zyf.timetable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DateHelper {
    static int year, month, day;
    static int startDay, endDay;
    static int currentWeek=1, startWeek=1, endWeek=20;
    static int currentWeekday;
    static int daysPerWeek=5, classesPerDay=12;
    static List<Integer> weekList;

    public static void setYear(int year) {
        DateHelper.year = year;
    }

    public static void setMonth(int month) {
        DateHelper.month = month;
    }

    public static void setDay(int day) {
        DateHelper.day = day;
    }

    public static void setStartDay(int startDay) {
        DateHelper.startDay = startDay;
    }

    public static void setEndDay(int endDay) {
        DateHelper.endDay = endDay;
    }

    public static void setDaysPerWeek(int daysPerWeek) {
        DateHelper.daysPerWeek = daysPerWeek;
    }

    public static void setClassesPerDay(int classesPerDay) {
        DateHelper.classesPerDay = classesPerDay;
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

}
