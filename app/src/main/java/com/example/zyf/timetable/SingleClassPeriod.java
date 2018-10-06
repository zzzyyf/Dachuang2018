package com.example.zyf.timetable;

/**
 * 表示课程表上的一节大课（注意与一门课作区分）
 */
public class SingleClassPeriod {
    /**
     * 上本节课的地点
     */
    private String classPlace;
    /**
     * 上本节课的各周次
     */
    private int[] weeks;
    /**
     * 单节课的节数
     */
    private int[] periods;


    public SingleClassPeriod(){

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

    public int[] getPeriods() {
        return periods;
    }
    public void setPeriods(int[] periods) {
        this.periods = periods;
    }

}
