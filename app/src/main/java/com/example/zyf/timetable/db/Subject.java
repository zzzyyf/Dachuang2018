package com.example.zyf.timetable.db;

import org.litepal.crud.LitePalSupport;

import java.util.List;

public class Subject extends LitePalSupport {
    /**
     * 课程名
     */
    private String class_name;
    /**
     * 上本节课的地点
     */
    private String class_place;
    /**
     * 上本节课的各周次
     */
    private List<Integer> weeks;
    /**
     * 上本节课的各周次
     */
    private int weekday;
    /**
     * 单节课的节数
     */
    private List<Integer> periods;

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getClass_place() {
        return class_place;
    }

    public void setClass_place(String class_place) {
        this.class_place = class_place;
    }

    public List<Integer> getWeeks() {
        return weeks;
    }

    public void setWeeks(List<Integer> weeks) {
        this.weeks = weeks;
    }

    public int getWeekday() {
        return weekday;
    }

    public void setWeekday(int weekday) {
        this.weekday = weekday;
    }

    public List<Integer> getPeriods() {
        return periods;
    }

    public void setPeriods(List<Integer> periods) {
        this.periods = periods;
    }
}
