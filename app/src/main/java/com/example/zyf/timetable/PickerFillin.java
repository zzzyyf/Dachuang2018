package com.example.zyf.timetable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

public class PickerFillin extends View {
    private Paint mPaint;
    public PickerFillin(Context context){
        super(context);
    }

    private int getScreenWidth() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getScreenWidth() / 2;
        setMeasuredDimension(width, getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
    }

    public void initPaint(){
        mPaint = new Paint();
        mPaint.setColor(android.R.attr.colorBackground);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        initPaint();
        canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
    }
}
