package com.example.zyf.timetable.db;

import org.litepal.crud.LitePalSupport;

import java.util.List;

public class WeekSettings extends LitePalSupport {
    private int startYear, startMonth, startDay;
    private int endYear, endMonth, endDay;
    private int currentWeek=1, startWeek=1, endWeek=20;
    private int currentWeekday;//currentWeekday:0-Sunday, 1-Monday, etc, 6-Saturday.
    private int daysPerWeek=7, classesPerDay=12;

    private List<Integer> weekList;

    private boolean isDateSet=false;
    private boolean isWeekSet=false;

    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public int getStartMonth() {
        return startMonth;
    }

    public void setStartMonth(int startMonth) {
        this.startMonth = startMonth;
    }

    public int getStartDay() {
        return startDay;
    }

    public void setStartDay(int startDay) {
        this.startDay = startDay;
    }

    public int getEndYear() {
        return endYear;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }

    public int getEndMonth() {
        return endMonth;
    }

    public void setEndMonth(int endMonth) {
        this.endMonth = endMonth;
    }

    public int getEndDay() {
        return endDay;
    }

    public void setEndDay(int endDay) {
        this.endDay = endDay;
    }

    public int getCurrentWeek() {
        return currentWeek;
    }

    public void setCurrentWeek(int currentWeek) {
        this.currentWeek = currentWeek;
    }

    public int getStartWeek() {
        return startWeek;
    }

    public void setStartWeek(int startWeek) {
        this.startWeek = startWeek;
    }

    public int getEndWeek() {
        return endWeek;
    }

    public void setEndWeek(int endWeek) {
        this.endWeek = endWeek;
    }

    public int getCurrentWeekday() {
        return currentWeekday;
    }

    public void setCurrentWeekday(int currentWeekday) {
        this.currentWeekday = currentWeekday;
    }

    public int getDaysPerWeek() {
        return daysPerWeek;
    }

    public void setDaysPerWeek(int daysPerWeek) {
        this.daysPerWeek = daysPerWeek;
    }

    public int getClassesPerDay() {
        return classesPerDay;
    }

    public void setClassesPerDay(int classesPerDay) {
        this.classesPerDay = classesPerDay;
    }

    public List<Integer> getWeekList() {
        return weekList;
    }

    public void setWeekList(List<Integer> weekList) {
        this.weekList = weekList;
    }

    public boolean isDateSet() {
        return isDateSet;
    }

    public void setDateSet(boolean dateSet) {
        isDateSet = dateSet;
    }

    public boolean isWeekSet() {
        return isWeekSet;
    }

    public void setWeekSet(boolean weekSet) {
        isWeekSet = weekSet;
    }
}
