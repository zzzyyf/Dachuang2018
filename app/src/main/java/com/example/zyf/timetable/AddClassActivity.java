package com.example.zyf.timetable;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import java.util.ArrayList;
import java.util.List;


public class AddClassActivity extends AppCompatActivity {
    EditText weekText;
    ArrayList<Integer> litList;
    RecyclerView weekdayPicker, sessionPicker;
    List<WeekItem> weekdays, sessions;
    ArrayList<Integer> pickedSessions, pickedWeekdays ;//0=未点亮的，1=已点亮的
    SingleClassItem classItem = new SingleClassItem();
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏，AppCompatActivity专用
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_class);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        //TODO: Toolbar阴影
        Toolbar toolbar = findViewById(R.id.add_class_dialog_toolbar);
        toolbar.setNavigationIcon(R.drawable.close);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initLit();
        weekText = findViewById(R.id.week_text);
        weekText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(AddClassActivity.this, WeekSelector.class).putIntegerArrayListExtra("Lit", litList), 1);
            }
        });

        weekdayPicker = findViewById(R.id.weekday_picker);
        sessionPicker = findViewById(R.id.session_picker);
        initWeekdays();
        initSessions();
        final LinearLayoutManager weekdaysManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        GridLayoutManager sessionsManager = new GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false);
        weekdayPicker.setLayoutManager(weekdaysManager);
        sessionPicker.setLayoutManager(sessionsManager);
        WeekSelectorAdapter weekdaysAdapter = new WeekSelectorAdapter(weekdays);
        WeekSelectorAdapter sessionsAdapter = new WeekSelectorAdapter(sessions);
        weekdayPicker.setAdapter(weekdaysAdapter);
        sessionPicker.setAdapter(sessionsAdapter);

        SwipeHelper weekdayHelper = new SwipeHelper(weekdaysManager, AddClassActivity.this, false);
        weekdayHelper.setLayout(DateHelper.daysPerWeek, 28, 24);
        weekdayHelper.setStatusList(createWeekdayHelperList());
        weekdayPicker.setOnTouchListener(weekdayHelper);

        SwipeHelper sessionHelper = new SwipeHelper(sessionsManager, AddClassActivity.this, true);
        sessionHelper.setLayout(4, 60, 24);
        sessionHelper.setStatusList(createSessionHelperList());
        sessionPicker.setOnTouchListener(sessionHelper);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_class_menu, menu);
        return true;
    }

    public void initLit(){
        litList = new ArrayList<>();
        for (int i=0;i<DateHelper.endWeek;i++){
            litList.add(0);
        }
    }

    private ArrayList<Boolean> createWeekdayHelperList(){
        ArrayList<Boolean> helperList = new ArrayList<>();
        for (int i=0;i<DateHelper.daysPerWeek;i++){
            //if (litList.get(i)==1)
                //helperList.add(true);
            /*else*/ helperList.add(false);
        }
        return helperList;
    }
    private ArrayList<Boolean> createSessionHelperList(){
        ArrayList<Boolean> helperList = new ArrayList<>();
        for (int i=0;i<DateHelper.classesPerDay;i++){
            //if (litList.get(i)==1)
                //helperList.add(true);
            /*else*/ helperList.add(false);
        }
        return helperList;
    }

    public void initSessions(){
        sessions = new ArrayList<>();
        for (int i=0;i<DateHelper.classesPerDay;i++){
            sessions.add(new WeekItem("第"+(i+1)+"节", 0));
        }
    }

    public void initWeekdays(){
        weekdays = new ArrayList<>();
        for (int i=0;i<DateHelper.daysPerWeek;i++){
            switch (i){
                case 0:
                    weekdays.add(new WeekItem("一", 0));
                    break;
                case 1:
                    weekdays.add(new WeekItem("二", 0));
                    break;
                case 2:
                    weekdays.add(new WeekItem("三", 0));
                    break;
                case 3:
                    weekdays.add(new WeekItem("四", 0));
                    break;
                case 4:
                    weekdays.add(new WeekItem("五", 0));
                    break;
                case 5:
                    weekdays.add(new WeekItem("六", 0));
                    break;
                case 6:
                    weekdays.add(new WeekItem("日", 0));
                    break;
                    default:
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1){
            switch (resultCode){
                case RESULT_OK:
                    litList = data.getIntegerArrayListExtra("Lit");
                    StringBuilder builder = new StringBuilder();
                    boolean isContinued = false;
                    builder.append("已选择：");
                    int j=DateHelper.endWeek-1;//最后一个点亮的周数
                    while(j>=0&&litList.get(j)==0)
                        j--;
                    for (int i=0;i<DateHelper.endWeek;i++) {
                        //第i项已被选中
                        if (litList.get(i) != 0) {
                            if(!isContinued) {
                                //从不连续选中到连续选中，一个新周段的开始
                                builder.append(i+1);
                                isContinued = true;
                                //若最后一周为离散选择
                                if (i==DateHelper.endWeek-1)
                                    builder.append("周");
                            }else{
                                //处在连续过程中
                                //若一直连续选到最后一周
                                if (i==DateHelper.endWeek-1){
                                    builder.append("-");
                                    builder.append(i+1);
                                    builder.append("周");
                                }
                            }
                        //第i项未被选中
                        } else {
                            if(isContinued){
                                //从连续选中到不连续选中
                                isContinued=false;
                                //若不是只取了一周
                                if (!(i==1||litList.get(i-2)==0)){
                                    builder.append("-");
                                    builder.append(i);
                                }
                                builder.append("周");
                                if (i<j)//不是在最后一个周段结束时
                                    builder.append("，");
                            }
                        }
                    }
                    weekText.setText(builder.toString());
                    break;
                default:
            }
        }
    }


}
