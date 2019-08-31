package com.example.zyf.timetable.db;

import org.litepal.crud.LitePalSupport;

public class Plan extends LitePalSupport {
    private long id;
    private String planname;//计划名字

    private int planbeginyear;
    private int planbeginmonth;
    private int planbeginday;

    private int planbeginhour;
    private int planbeginminute;

    private int planendyear;
    private int planendmonth;
    private int planendday;

    private int planendhour;
    private int planendminute;

    private String plannote;

    private int plancolor;

    private int planfrequ;//计划频率

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPlanname() {
        return planname;
    }

    public void setPlanname(String planname) {
        this.planname = planname;
    }

    public int getPlanbeginyear() {
        return planbeginyear;
    }

    public void setPlanbeginyear(int planbeginyear) {
        this.planbeginyear = planbeginyear;
    }

    public int getPlanbeginmonth() {
        return planbeginmonth;
    }

    public void setPlanbeginmonth(int planbeginmonth) {
        this.planbeginmonth = planbeginmonth;
    }

    public int getPlanbeginday() {
        return planbeginday;
    }

    public void setPlanbeginday(int planbeginday) {
        this.planbeginday = planbeginday;
    }

    public int getPlanbeginhour() {
        return planbeginhour;
    }

    public void setPlanbeginhour(int planbeginhour) {
        this.planbeginhour = planbeginhour;
    }

    public int getPlanbeginminute() {
        return planbeginminute;
    }

    public void setPlanbeginminute(int planbeginminute) {
        this.planbeginminute = planbeginminute;
    }

    public int getPlanendyear() {
        return planendyear;
    }

    public void setPlanendyear(int planendyear) {
        this.planendyear = planendyear;
    }

    public int getPlanendmonth() {
        return planendmonth;
    }

    public void setPlanendmonth(int planendmonth) {
        this.planendmonth = planendmonth;
    }

    public int getPlanendday() {
        return planendday;
    }

    public void setPlanendday(int planendday) {
        this.planendday = planendday;
    }

    public int getPlanendhour() {
        return planendhour;
    }

    public void setPlanendhour(int planendhour) {
        this.planendhour = planendhour;
    }

    public int getPlanendminute() {
        return planendminute;
    }

    public void setPlanendminute(int planendminute) {
        this.planendminute = planendminute;
    }

    public String getPlannote() {
        return plannote;
    }

    public void setPlannote(String plannote) {
        this.plannote = plannote;
    }

    public int getPlancolor() {
        return plancolor;
    }

    public void setPlancolor(int plancolor) {
        this.plancolor = plancolor;
    }

    public int getPlanfrequ() {
        return planfrequ;
    }

    public void setPlanfrequ(int planfrequ) {
        this.planfrequ = planfrequ;
    }
}
