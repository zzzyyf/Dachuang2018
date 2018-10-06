package com.example.zyf.timetable;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.NumberPicker;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    RecyclerView tableView;
    RecyclerView picker;
    List<SingleClass> classList;
    List<Integer> weekList;
    int year, month, day;
    int currentWeek, startWeek=1, endWeek=20;
    int currentWeekday;
    int daysPerWeek=5, classesPerDay=12;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //TODO: add BottomNaviBar & Toolbar menu
        //初始化Lists
        classList = new ArrayList<>();
        initClassListTest(classList);
        weekList = new ArrayList<>();
        initWeekList(startWeek, endWeek, weekList);

        //初始化RecyclerViews
        tableView = findViewById(R.id.table_view);
        GridLayoutManager manager = new GridLayoutManager(this, daysPerWeek, LinearLayoutManager.VERTICAL, false);
        tableView.setLayoutManager(manager);

        SingleClassAdapter adapter = new SingleClassAdapter(classList);
        tableView.setAdapter(adapter);

        picker = findViewById(R.id.picker);
        LinearLayoutManager linearManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        picker.setLayoutManager(linearManager);

        PickerAdapter weekAdapter = new PickerAdapter(weekList);
        picker.setAdapter(weekAdapter);

        //TODO: Make WeekPicker be able to swipe & pick
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    public void initClassListTest(List<SingleClass> list){
        list.clear();
        for(int i = 0;i<50;i++){
            SingleClass initClass = new SingleClass();
            initClass.setClassName(i+1+"");
            initClass.setClassPlace("");
            list.add(initClass);
        }
    }
    public void initWeekList(int start, int end, List<Integer> list){
        for (int i=start;i<=end;i++){
            list.add(i);
        }
    }
    /**
     * 添加一节课
     * @param cls 要添加的课
     */
    public void addClass(SingleClass cls){
        classList.add(cls);
    }

    /**
     * 通过获取某节课在集合中的位置（点击RecyclerView或在列表中删除）删除某节课
     * @param pos 获取到的位置
     */
    public void deleteClass(int pos)throws Exception{
        try{
            classList.remove(pos);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
