package com.example.zyf.timetable;

import android.util.TypedValue;
import android.widget.TextView;

public class PickerHelper {
    static TextView weekNum;//TODO: 设为静态易造成内存泄漏，用完应释放引用
    public static void changeWeekNumHighlight(TextView newNum, int defaultColor, int highlightColor) {
        if (weekNum != null) {
            weekNum.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
            weekNum.setTextColor(defaultColor);
        }
        weekNum=newNum;
        weekNum.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
        weekNum.setTextColor(highlightColor);
    }

    /**
     * free TextView here, remember to invoke this method before destroy main activity!
     */
    public static void freeTextView(){
        weekNum = null;
    }
}
