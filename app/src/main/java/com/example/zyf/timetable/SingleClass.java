package com.example.zyf.timetable;

/**
 * 表示课程表上周几的某一节大课（注意与一门课作区分）
 */
public class SingleClass {

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
    private int[] weeks;
    /**
     * 上本节课的各周次
     */
    private int weekday;
    /**
     * 单节课的节数
     */
    private int[] periods;


    public SingleClass(){

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

    public int[] getWeeks() {
        return weeks;
    }
    public void setWeeks(int[] weeks) {
        this.weeks = weeks;
    }

    public int getWeekday() {
        return weekday;
    }
    public void setWeekday(int weekday) {
        this.weekday = weekday;
    }

    public int[] getPeriods() {
        return periods;
    }
    public void setPeriods(int[] periods) {
        this.periods = periods;
    }

}
