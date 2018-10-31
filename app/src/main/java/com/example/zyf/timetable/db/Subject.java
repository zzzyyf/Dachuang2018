package com.example.zyf.timetable.db;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;
import java.util.List;

public class Subject extends LitePalSupport implements Serializable {
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
     * 在一周的哪一天上本节课
     */
    private int weekday;
    /**
     * 本节课开始的节数,1为第一节
     */
    private int startPeriod=0;
    /**
     * 本节课结束的节数，1为第一节
     */
    private int endPeriod=-1;

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

    /**
     * 获取本节课开始上课的节数，1为第一节。
     * @return
     */
    public int getStartPeriod() {
        return startPeriod;
    }

    /**
     * 设置本节课开始上课的节数，1为第一节。
     * @param startPeriod
     */
    public void setStartPeriod(int startPeriod) {
        this.startPeriod = startPeriod;
    }

    /**
     * 获取本节课结束的节数（即上到第几节），1为第一节。
     * @return
     */
    public int getEndPeriod() {
        return endPeriod;
    }

    /**
     * 设置本节课结束的节数（即上到第几节），1为第一节。
     * @param endPeriod
     */
    public void setEndPeriod(int endPeriod) {
        this.endPeriod = endPeriod;
    }
}
