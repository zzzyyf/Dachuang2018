package com.example.zyf.timetable;

import java.util.ArrayList;
import java.util.List;

/**
 * 表示单门课的类，一门课可以在不同周的不同天有不同节，这些包含在列表成员中。
 */
public class SingleClass {
    /**
     * 课名
     */
    private String className;
    private List<SingleClassPeriod> classPeriodList = new ArrayList<SingleClassPeriod>();

    public String getClassName() {
        return className;
    }
    public void setClassName(String className) {
        this.className = className;
    }

    public void addClass(){
        classPeriodList.add(new SingleClassPeriod());
    }
}
