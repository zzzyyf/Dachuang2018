package com.example.zyf.timetable;

import java.util.ArrayList;

/**
 * 表示课程表上周几的某一节大课（注意与一门课作区分）
 */
public class SingleClassItem {

    /**
     * 课程名
     */
    private String className;
    /**
     * 上本节课的地点
     */
    private String classPlace;
    /**
     * 上本节课的各周次
     */
    private ArrayList<Integer> weeks;
    /**
     * 上本节课的各周次
     */
    private int weekday;
    /**
     * 单节课的节数
     */
    private ArrayList<Integer> periods;


    public SingleClassItem(){

    }

    public String getClassName() {
        return className;
    }
    public void setClassName(String className) {
        this.className = className;
    }
    public String getClassPlace() {
        return classPlace;
    }
    public void setClassPlace(String classPlace) {
        this.classPlace = classPlace;
    }

    public void setWeeks(ArrayList<Integer> weeks) {
        this.weeks = weeks;
    }
    public void setPeriods(ArrayList<Integer> periods) {
        this.periods = periods;
    }

    public int getWeekday() {
        return weekday;
    }
    public void setWeekday(int weekday) {
        this.weekday = weekday;
    }


}
