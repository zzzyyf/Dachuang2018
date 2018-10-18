package com.example.zyf.timetable;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.Button;

import java.util.ArrayList;

public class WeekSelector extends AppCompatActivity {
    ArrayList<Integer> lit = new ArrayList<>();//0=未点亮的，1=已点亮的
    int itemWidth, itemHeight;
    Rect[] weekRect = new Rect[DateHelper.endWeek];
    View item;
    boolean isItemMeasured=false;
    boolean inTouchArea=false;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_week_selector);

        //TODO: 加入滑动选择上课周的功能
        Toolbar toolbar = findViewById(R.id.week_selector_toolbar);
        setSupportActionBar(toolbar);

        final Button finishBtn = findViewById(R.id.finish_btn);
        Button discardBtn = findViewById(R.id.discard_btn);
        RecyclerView weekView = findViewById(R.id.week_selector);
        initLit(lit);
        final GridLayoutManager manager = new GridLayoutManager(this, 5, LinearLayoutManager.VERTICAL, false);
        weekView.setLayoutManager(manager);
        WeekSelectorAdapter adapter = new WeekSelectorAdapter(DateHelper.weekList);
        weekView.setAdapter(adapter);

        /*只需判断点击（抬起）和滑出
        在父View挂`onTouchListener()`，
            if 监听到`ACTION_MOVE`
                if 触点在某个数字区域内
                    flag1=true//拖动进行中
                    记下该数字对应的位置 currentPosition
                    若lastPosition==0则初始化lastPosition为currentPosition
                    if Lit[]集合内有该位置（已被点亮过）
                        method 暗该子项
                        把该子项位置移出Lit[]集合
                    else
                        method 亮该子项
                        把该子项位置存入Lit[]集合
            else if 监听到ACTION_UP
                method 判断在哪一个数字内
                if Lit[]集合内有该位置（已被点亮过）
                    method 暗该子项
                    把该子项位置移出Lit[]集合
                else
                    method 亮该子项
                    把该子项位置存入Lit[]集合
        */
        weekView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                item = manager.findViewByPosition(0);
                if (!isItemMeasured) {
                    itemWidth = item.getWidth();
                    itemHeight = item.getHeight();
                    initWeekRects(weekRect, itemWidth, itemHeight, WeekSelector.this);
                    isItemMeasured=true;
                }
                Log.i("Coords", MotionEvent.actionToString(event.getAction())+" "+event.getX()+","+event.getY());
                switch (event.getAction()){
                    case MotionEvent.ACTION_MOVE:
                        for (int i = 0;i<DateHelper.endWeek;i++){
                            //如果触点进入第i个矩形
                            if (weekRect[i].contains((int)event.getX(), (int)event.getY())) {
                                //该格未被点亮
                                if (!inTouchArea && lit.get(i) == 0) {
                                    ObjectAnimator litAnim = ObjectAnimator.ofArgb(manager.findViewByPosition(i), "backgroundColor", 0x01e1bee7, 0xffe1bee7);
                                    litAnim.setDuration(250)
                                            .start();
                                    lit.set(i, 1);//点亮该区域
                                    inTouchArea = true;//设置已进入区域
                                    Log.i("Coords", "Lit " + (i + 1));
                                }
                                //该格已被点亮
                                else if (!inTouchArea && lit.get(i) == 1) {
                                    ObjectAnimator dimAnim = ObjectAnimator.ofArgb(manager.findViewByPosition(i), "backgroundColor", 0xffe1bee7, 0x01e1bee7);
                                    dimAnim.setDuration(250)
                                            .start();
                                    lit.set(i, 0);//熄灭该区域
                                    inTouchArea = true;//设置已进入区域
                                    Log.i("Coords", "Dim " + (i + 1));
                                } else if (inTouchArea) {
                                    //仍在区域内，MotionEvent利用完毕
                                    Log.i("Coords", "Swiping in " + (i + 1));
                                }
                                return true;
                                //触点不在第i个矩形内，继续遍历，不作处理，也不用写
                            }
                        }
                        //若执行至此处只能是遍历完成后发现这次滑动不在任一矩形内
                        inTouchArea=false;
                        return true;
                    default:
                }
                return true;}
        });

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击“完成”按钮
                Intent intent = new Intent();
                intent.putIntegerArrayListExtra("Lit", lit);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        discardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击“取消”按钮
                setResult(RESULT_CANCELED);
                finish();
            }
        });


    }

    private void initWeekRects(Rect[] weekRects, float itemWidth, float itemHeight, Context context){
        int row, column;//第几行和第几列
        int paddingHorizontal = (int)(itemWidth/2 -dp2px(context,15));//item view中检测触摸区域横向边界距item view横向边界的宽度
        int paddingVertical = (int)(itemHeight/2-dp2px(context,15));//item view中检测触摸区域纵向边界距item view纵向边界的高度
        for (int i = 0;i<DateHelper.endWeek;i++){
            row = i/5+1;
            column = ((i+1)%5==0)?5:((i+1)%5);
            //MotionEvent的getX/getY方法均返回相对于监听View左上角的坐标，单位为px。
            weekRects[i]=new Rect(paddingHorizontal+(column-1)*(int)itemWidth,
                    paddingVertical+(row-1)*(int)itemHeight,
                    paddingHorizontal+(column-1)*(int)itemWidth+dp2px(context, 30),
                    paddingVertical+(row-1)*(int)itemHeight+dp2px(context, 30));
        }
    }
    private void initLit(ArrayList<Integer> lit){
        for (int i=0;i<DateHelper.endWeek;i++){
            lit.add(0);
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    private int dp2px(Context context,int dpValue){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dpValue,context.getResources().getDisplayMetrics());
    }

}
