package com.example.zyf.timetable;

import com.example.zyf.timetable.db.WeekSettings;

import org.litepal.LitePal;
import org.litepal.exceptions.LitePalSupportException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DateHelper {
    public static boolean isDateSet = false;
    public static boolean isWeekSet = false;
    static int year, month, day;//month为真实月份-1
    static Calendar today;
    static int currentWeek = 1, selectedWeek;
    static int currentWeekday;//currentWeekday:0-Sunday, 1-Monday, etc, 6-Saturday.
    //以下需要用数据库存储
    static int startYear, startMonth, startDay;
    static int endYear, endMonth, endDay;
    static int startWeek = 1, endWeek = 20;
    static int daysPerWeek = 7, classesPerDay = 12;
    static List<Integer> weekList;

    /**
     * 手动设定当前日期。
     *
     * @param year
     * @param month
     * @param day
     */
    public static void setDate(int year, int month, int day) {
        DateHelper.year = year;
        DateHelper.month = month;
        DateHelper.day = day;
        today.set(year, month, day);
        currentWeekday = today.get(Calendar.DAY_OF_WEEK) - 1;
        isDateSet = true;

    }

    /**
     * 自动设定当前日期。注意，每过一天需要重新调用。
     */
    public static void setDate() {
        today = Calendar.getInstance();
        year = today.get(Calendar.YEAR);
        month = today.get(Calendar.MONTH);
        day = today.get(Calendar.DAY_OF_MONTH);
    }


    public static void setWeek(Calendar startDate, Calendar endDate, int daysPerWeek, int classesPerDay) {
        setWeek(startDate, endDate, classesPerDay);
        DateHelper.daysPerWeek = daysPerWeek;
    }


    public static void setWeek(Calendar startDate, Calendar endDate, int classesPerDay) {
        startYear = startDate.get(Calendar.YEAR);
        startMonth = startDate.get(Calendar.MONTH);
        startDay = startDate.get(Calendar.DAY_OF_MONTH);
        endYear = endDate.get(Calendar.YEAR);
        endMonth = endDate.get(Calendar.MONTH);
        endDay = endDate.get(Calendar.DAY_OF_MONTH);
        DateHelper.classesPerDay = classesPerDay;

        endWeek = startWeek + countEndWeek(startDate, endDate);
        setCurrentWeek(startDate);
        isWeekSet = true;
    }

    /**
     * 给weekList分配空间及初始化内容的方法。
     */
    public static void initWeekList() {
        weekList = new ArrayList<>();
        for (int i = startWeek; i <= endWeek; i++) {
            weekList.add(i);
        }
    }

    private static int countEndWeek(Calendar startDate, Calendar endDate) {
        int end = 0;
        //设置endWeek
        while (startDate.before(endDate)) {
            // 如果开始日期和结束日期在同年、同月且当前月的同一周时结束循环
            if (startDate.get(Calendar.YEAR) == endDate.get(Calendar.YEAR)
                    && startDate.get(Calendar.MONTH) == endDate.get(Calendar.MONTH)
                    && startDate.get(Calendar.DAY_OF_WEEK_IN_MONTH) == endDate.get(Calendar.DAY_OF_WEEK_IN_MONTH)) {
                //TODO: the algorithm here is wrong.
                break;
            } else {
                startDate.add(Calendar.DAY_OF_YEAR, 7);
                end += 1;
            }
        }
        return end;
    }

    public static void setCurrentWeek(Calendar startDate) {
        currentWeek = startWeek + countEndWeek(startDate, today);
    }

    public static int getSelectedWeek() {
        return selectedWeek;
    }

    public static void setSelectedWeek(int selectedWeek) {
        DateHelper.selectedWeek = selectedWeek;
    }

    public static void countCurrentWeekday(int currentWeekday) {
        DateHelper.currentWeekday = currentWeekday;
    }

    public static String weeksToString(List<Integer> list) {
        StringBuilder builder = new StringBuilder();
        int last=list.get(0);
        boolean isContinued = false;
        for (int elem : list) {
            if (!isContinued) {
                //从不连续到连续，一个新周段的开始
                builder.append(elem);
                last=elem;
                isContinued = true;
                //若最后一周为离散选择
                if (list.indexOf(elem) == list.size() - 1)
                    builder.append("周");
            } else {
                //处在连续过程中
                if (elem-last!=1){
                    //不连续了
                    builder.append("-");
                    builder.append(elem);
                    builder.append("周");
                    if(list.indexOf(elem) == list.size() - 1){
                        break;
                    }
                    builder.append("，");
                    isContinued=false;
                }else{
                    //仍连续
                    last=elem;
                }
                //若一直连续到最后一周
                if (list.indexOf(elem) == list.size() - 1) {
                    builder.append("-");
                    builder.append(elem);
                    builder.append("周");
                }
            }
        }
        return builder.toString();
    }


    public static void readFromDb(WeekSettings settings) {
        classesPerDay = settings.getClassesPerDay();
        isDateSet = settings.isDateSet();
        daysPerWeek = settings.getDaysPerWeek();
        endDay = settings.getEndDay();
        endMonth = settings.getEndMonth();
        endWeek = settings.getEndWeek();
        endYear = settings.getEndYear();
        startDay = settings.getStartDay();
        startMonth = settings.getStartMonth();
        startWeek = settings.getStartWeek();
        startYear = settings.getStartYear();
        weekList = settings.getWeekList();
        isWeekSet = settings.isWeekSet();
    }

    public static void saveToDb(WeekSettings settings) throws LitePalSupportException {
        settings.setClassesPerDay(classesPerDay);
        settings.setDateSet(isDateSet);
        settings.setDaysPerWeek(daysPerWeek);
        settings.setEndDay(endDay);
        settings.setEndMonth(endMonth);
        settings.setEndWeek(endWeek);
        settings.setEndYear(endYear);
        settings.setStartDay(startDay);
        settings.setStartMonth(startMonth);
        settings.setStartWeek(startWeek);
        settings.setStartYear(startYear);
        settings.setWeekList(weekList);
        settings.setWeekSet(isWeekSet);
        if (LitePal.findAll(WeekSettings.class).size() != 0)
            LitePal.deleteAll(WeekSettings.class);
        settings.saveThrows();
    }
}
