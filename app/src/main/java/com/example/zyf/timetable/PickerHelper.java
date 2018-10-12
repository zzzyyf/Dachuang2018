package com.example.zyf.timetable;

import android.util.TypedValue;
import android.widget.TextView;

import org.w3c.dom.Text;

public class PickerHelper {
    static TextView weekNum;//设为静态易造成内存泄漏，用完应释放引用
    static int num;
    static int defaultColor, highlightColor;

    public static void setDefaultColor(int defaultColor) {
        PickerHelper.defaultColor = defaultColor;
    }

    public static void setHighlightColor(int highlightColor) {
        PickerHelper.highlightColor = highlightColor;
    }

    public static void changeWeekNumHighlight(TextView newNum) {
        //如果weekNum已被初始化（已经对某个weekNum设置过特效）或者经过复用ViewHolder后weekNum的值改变就还原特效
        if (weekNum != null) {
            resetHighlight(weekNum);
        }
        weekNum=newNum;
        num = Integer.parseInt(weekNum.getText().toString());
        weekNum.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
        weekNum.setTextColor(highlightColor);
    }
    public static void resetHighlight(TextView weekText){
        weekNum.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        weekNum.setTextColor(defaultColor);
    }

    /**
     * 通过比较Text检查传入的TextView的值是否为设置过特效的TextView的值，若已经不是则还原传入TextView的特效。
     * @param weekText 要检查的TextView
     */
    public static void checkHighlight(TextView weekText){
        if (weekNum == weekText && Integer.parseInt(weekText.getText().toString())!=num) resetHighlight(weekText);
        else if (Integer.parseInt(weekText.getText().toString())==num) changeWeekNumHighlight(weekText);
    }
    /**
     * free TextView here, remember to invoke this method before destroy main activity!
     */
    public static void freeTextView(){
        weekNum = null;
    }
}
