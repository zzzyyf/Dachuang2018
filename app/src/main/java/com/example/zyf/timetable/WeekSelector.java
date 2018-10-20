package com.example.zyf.timetable;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.annotation.NonNull;
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
import java.util.List;

public class WeekSelector extends AppCompatActivity {
    ArrayList<Integer> lit;//0=未点亮的，1=已点亮的

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_week_selector);

        Toolbar toolbar = findViewById(R.id.week_selector_toolbar);
        setSupportActionBar(toolbar);

        Button finishBtn = findViewById(R.id.finish_btn);
        Button discardBtn = findViewById(R.id.discard_btn);
        RecyclerView weekView = findViewById(R.id.week_selector);
        lit = getIntent().getIntegerArrayListExtra("Lit");

        final GridLayoutManager manager = new GridLayoutManager(this, 5, LinearLayoutManager.VERTICAL, false);
        weekView.setLayoutManager(manager);
        WeekSelectorAdapter adapter = new WeekSelectorAdapter(initItems(lit));
        weekView.setAdapter(adapter);

        final SwipeHelper helper = new SwipeHelper(manager, WeekSelector.this, true);
        helper.setLayout(5, 28, 24);
        helper.setStatusList(createHelperList());
        weekView.setOnTouchListener(helper);

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击“完成”按钮
                Intent intent = new Intent();
                transportLitList(helper, lit);
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

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    private List<WeekItem> initItems(List<Integer>litList){
        List<WeekItem> weekItems = new ArrayList<>();
        for (int i=0;i<DateHelper.endWeek;i++){
            WeekItem item = new WeekItem();
            item.setWeekNum((i+1)+"");
            item.setLit((litList.get(i))==1);
            weekItems.add(item);
        }
        return weekItems;
    }

    private ArrayList<Boolean> createHelperList(){
        ArrayList<Boolean> helperList = new ArrayList<>();
        for (int i=0;i<DateHelper.endWeek;i++){
            if (lit.get(i)==1)
                helperList.add(true);
            else helperList.add(false);
        }
        return helperList;
    }

    private void transportLitList(SwipeHelper helper, @NonNull ArrayList<Integer> litList){
        List<Boolean> helperList = new ArrayList<>();
        for (int i=0;i<helper.getLit().size();i++){
            helperList.add(helper.getLit().get(i).isLit());
        }
        for (int i=0;i<helperList.size();i++){
            if (litList.get(i)==0) {
                //若原来为0则可能要改为1
                litList.remove(i);
                litList.add(i, helperList.get(i) ? 1 : 0);
            }
            else{
                //若原来点亮的现在被取消了
                if(!helperList.get(i)) {
                    litList.remove(i);
                    litList.add(i, 0);
                }
            }
        }
    }
}
