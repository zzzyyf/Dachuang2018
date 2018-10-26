package com.example.zyf.timetable;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.WindowManager;

//TODO: 强制设定宽度后居中稍有问题
public class TableItemLayout extends ConstraintLayout {
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
