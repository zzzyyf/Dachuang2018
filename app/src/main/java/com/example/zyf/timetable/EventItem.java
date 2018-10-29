package com.example.zyf.timetable;

public class EventItem {
    private String eventlistname;//显示的列表中事件的名字

    private String eventlistdate;//显示的列表中事件的年月日

    private String eventlistnote;//显示的列表中事件的备注
    private int leftday;//显示的列表中剩余的天数

    private int eventlistcolor;//显示的列表中的颜色

    private long id;//存放数据库中的id


    public EventItem(String eventlistname,int leftday,int eventlistcolor,String eventlistdate,String eventlistnote,long id){
        this.eventlistname=eventlistname;
        this.eventlistdate=eventlistdate;
        this.leftday=leftday;
        this.eventlistcolor=eventlistcolor;

        this.eventlistnote=eventlistnote;
        this.id=id;

    }

    public String getEventlistname() {
        return eventlistname;
    }

    public String getEventlistdate() {
        return eventlistdate;
    }

    public int  getLeftday() {
        return leftday;
    }

    public int getEventlistcolor() {
        return eventlistcolor;
    }

    public String getEventlistnote() {
        return eventlistnote;
    }

    public long getId() {
        return id;
    }
}
