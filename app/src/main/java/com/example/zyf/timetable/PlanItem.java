package com.example.zyf.timetable;

public class PlanItem {
    private String planitemname;

    private String planitembegindate;

    private String planitembegintime;

    private String planitemenddate;

    private String planitemendtime;

    private int planitemcolor;

    private int planitemfre;//频率

    private long id;

    private String planitemnote;


    public PlanItem(String planitemname, String planitembegindate, String planitembegintime, String planitemenddate, String planitemendtime, int planitemcolor, int planitemfre, long id, String planitemnote) {
        this.planitemname = planitemname;
        this.planitembegindate = planitembegindate;
        this.planitembegintime = planitembegintime;
        this.planitemenddate = planitemenddate;
        this.planitemendtime = planitemendtime;
        this.planitemcolor = planitemcolor;
        this.planitemfre = planitemfre;
        this.id = id;
        this.planitemnote=planitemnote;
    }

    public String getPlanitemname() {
        return planitemname;
    }

    public String getPlanitembegindate() {
        return planitembegindate;
    }

    public String getPlanitembegintime() {
        return planitembegintime;
    }

    public String getPlanitemenddate() {
        return planitemenddate;
    }

    public String getPlanitemendtime() {
        return planitemendtime;
    }

    public int getPlanitemcolor() {
        return planitemcolor;
    }

    public int getPlanitemfre() {
        return planitemfre;
    }

    public long getId() {
        return id;
    }

    public String getPlanitemnote() {
        return planitemnote;
    }
}
