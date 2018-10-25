package com.example.zyf.timetable;

import com.example.zyf.timetable.db.WeekSettings;

import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.litepal.LitePal;
import org.litepal.exceptions.LitePalSupportException;

import java.util.ArrayList;
import java.util.List;

public class DateHelper {
    public static boolean isDateSet = false;
    static int year, month, day;//month为真实月份
    static LocalDate today;
    static int currentWeek = 1, selectedWeek;
    static int currentWeekday;//currentWeekday:1-Monday, etc, 6-Saturday, 7-Sunday.

    //以下需要用数据库存储
    public static boolean isWeekSet = false;
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
        today = new LocalDate(year, month, day);
        currentWeekday = today.getDayOfWeek();
        isDateSet = true;

    }

    /**
     * 自动设定当前日期。注意，每过一天需要重新调用。
     */
    public static void setDate() {
        today = new LocalDate();
        year = today.getYear();
        month = today.getMonthOfYear();
        day = today.getDayOfMonth();
        if (isWeekSet)
            setCurrentWeek(new LocalDate(startYear, startMonth, startDay));
    }


    public static void setWeek(LocalDate startDate, LocalDate endDate, int daysPerWeek, int classesPerDay) {
        setWeek(startDate, endDate, classesPerDay);
        DateHelper.daysPerWeek = daysPerWeek;
    }


    public static void setWeek(LocalDate startDate, LocalDate endDate, int classesPerDay) {
        startYear = startDate.getYear();
        startMonth = startDate.getMonthOfYear();
        startDay = startDate.getDayOfMonth();
        endYear = endDate.getYear();
        endMonth = endDate.getMonthOfYear();
        endDay = endDate.getDayOfMonth();
        DateHelper.classesPerDay = classesPerDay;

        endWeek = startWeek + countEndWeek(startDate, endDate);
        setCurrentWeek(startDate);
        setSelectedWeek(currentWeek);
        isWeekSet = true;
    }

    /**
     * 给weekList分配空间及初始化内容的方法。
     * weekList从1开始，即weekList的元素值为index+1.
     */
    public static void initWeekList() {
        weekList = new ArrayList<>();
        for (int i = startWeek; i <= endWeek; i++) {
            weekList.add(i);
        }
    }

    private static int countEndWeek(LocalDate startDate, LocalDate endDate) {
        int end = 0;
        endDate = endDate.withDayOfWeek(1);//转化为当周周一
        int days = new Period(startDate, endDate, PeriodType.days()).getDays();//获取相差天数

        //若相差0-6天说明在同一周
        for(;!(days>=0 && days<7);days-=7) end+=1;
        return end;
    }

    public static void setCurrentWeek(LocalDate startDate) {
        currentWeek = startWeek + countEndWeek(startDate, today);
        setSelectedWeek(currentWeek);
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
