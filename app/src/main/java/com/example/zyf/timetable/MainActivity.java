package com.example.zyf.timetable;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import java.util.ArrayList;
import java.util.List;
import static com.example.zyf.timetable.DateHelper.*;

public class MainActivity extends AppCompatActivity {
    List<SingleClass> classList;
    Fragment[] fragments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //TODO: add other fragments & Toolbar menu
        //初始化Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //初始化Lists
        classList = new ArrayList<>();
        initClassListTest(classList);
        initWeekList();

        //初始化各片段
        fragments = new Fragment[4];
        //防止旋转屏幕时Fragment重复加载
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, TimeTableFragment.newInstance("1", "2"), "Timetable")
                    .commit();
        }


        //初始化BottomNaviBar
        BottomNavigationBar naviBar = findViewById(R.id.navi_main);
        naviBar
                .addItem(new BottomNavigationItem(R.drawable.home, "主页"))
                .addItem(new BottomNavigationItem(R.drawable.table, "课程表"))
                .addItem(new BottomNavigationItem(R.drawable.plan_list, "计划"))
                .addItem(new BottomNavigationItem(R.drawable.event_list, "事件"))
                .setFirstSelectedPosition(1)
                .initialise();
        naviBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener(){
            @Override
            public void onTabSelected(int position) {
                switch (position){
                    case 1:
                        //选择“课程表”

                        break;
                    case 2:
                        //选择“计划”
                    case 3:
                        //选择“事件”
                    default:
                }
            }
            @Override
            public void onTabUnselected(int position) {
            }
            @Override
            public void onTabReselected(int position) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (fragments[1]==null) {
            fragments[1] = getSupportFragmentManager().findFragmentById(R.id.container);
            ((TimeTableFragment)fragments[1]).initFragment(classList);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        menu.findItem(R.id.add_event).setVisible(false);
        menu.findItem(R.id.add_plan).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_class:
                //点击添加课程按钮

                break;
            case R.id.set_table_item:
                //点击课表设置按钮
                break;
            default:
        }
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
}
