package com.example.zyf.timetable;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.LinearLayout;


public class TableItemLayout extends LinearLayout {
    public TableItemLayout(Context context){
        this(context, null, 0);
    }
    public TableItemLayout(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }

    public TableItemLayout(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
    }

    private int getScreenWidth() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getScreenWidth() / 5;
        setMeasuredDimension(width, getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
    }

}
