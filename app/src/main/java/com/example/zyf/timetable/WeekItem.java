package com.example.zyf.timetable;

public class WeekItem {
    private String weekNum;
    private int lit;

    public WeekItem(String num, int lit){
        weekNum = num;
        this.lit = lit;
    }

    public WeekItem(){

    }

    public String getWeekNum() {
        return weekNum;
    }

    public void setWeekNum(String weekNum) {
        this.weekNum = weekNum;
    }

    public int getLit() {
        return lit;
    }

    public void setLit(int lit) {
        this.lit = lit;
    }
}
