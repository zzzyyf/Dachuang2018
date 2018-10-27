package com.example.zyf.timetable;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.zyf.timetable.db.Subject;
import com.example.zyf.timetable.db.WeekSettings;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.zyf.timetable.DateHelper.*;

public class MainActivity extends AppCompatActivity {
    List<Subject> allClassList;
    List<List<Subject>> weekClassList;
    Fragment[] fragments;
    Toolbar toolbar;
    TextInputLayout startDateText, endDateText, classPDayText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SQLiteDatabase db = LitePal.getDatabase();
        //LitePal.deleteAll("Subject");

        //TODO: 加入刷新操作
        setContentView(R.layout.activity_main);
        //TODO: add other fragments & Toolbar menu
        //初始化Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //读取数据库
        allClassList = LitePal.order("startperiod asc").find(Subject.class);//按小节的升序读取所有课程
        WeekSettings weekSettings = LitePal.findFirst(WeekSettings.class);
        if (weekSettings != null)
            DateHelper.readFromDb(weekSettings);
        initWeekList();
        setDate();
        fillWithEmptyClass(currentWeek);

        //初始化各片段
        fragments = new Fragment[4];
        //防止旋转屏幕时Fragment重复加载
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, TimeTableFragment.newInstance("1", "2"), "Timetable")
                    .commit();
        }
        //TODO: 旋转屏幕时刷新recyclerview

        //初始化BottomNaviBar
        BottomNavigationBar naviBar = findViewById(R.id.navi_main);
        naviBar
                .addItem(new BottomNavigationItem(R.drawable.home, "主页"))
                .addItem(new BottomNavigationItem(R.drawable.table, "课程表"))
                .addItem(new BottomNavigationItem(R.drawable.plan_list, "计划"))
                .addItem(new BottomNavigationItem(R.drawable.event_list, "事件"))
                .setFirstSelectedPosition(1)
                .initialise();
        naviBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                switch (position) {
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
        if (fragments[1] == null) {
            fragments[1] = getSupportFragmentManager().findFragmentById(R.id.container);
            ((TimeTableFragment) fragments[1]).initFragment(getClassListOfWeek(weekClassList));

            //set initial position of picker
            if (isWeekSet) {
                ((TimeTableFragment) fragments[1]).picker.post(new Runnable() {
                    @Override
                    public void run() {
                        //直接传入adapterPosition即周数即可
                        //TODO: 偶尔会报错Fragment not attached to a context.
                        ((TimeTableFragment) fragments[1]).scrollToPosition(currentWeek);

                    }
                });
            }
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
        switch (item.getItemId()) {
            case R.id.add_class:
                //点击添加课程按钮
                startActivityForResult(new Intent(MainActivity.this, AddClassActivity.class), 1);
                break;
            case R.id.set_table_item:
                startActivityForResult(new Intent(MainActivity.this, SetSemesterActivity.class), 2);
                //点击课表设置按钮
                break;
            default:
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    //若当前显示的碎片不是Timetable
                    if (!(getSupportFragmentManager().findFragmentById(R.id.container) instanceof TimeTableFragment)) {
                        //把当前显示的碎片替换为Timetable
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, TimeTableFragment.newInstance("1", "2"), "Timetable")
                                .commit();
                    }
                    //更新数据
                    allClassList = LitePal.order("startperiod asc").find(Subject.class);//按小节的升序读取所有课程
                    int weekday = data.getIntExtra("weekday", 1);
                    if(LitePal.findLast(Subject.class).getWeeks().contains(getSelectedWeek())) {
                        weekClassList.set(weekday-1, fillWeekdayWithEmpty(getSelectedWeek(), weekday));
                        ((TimeTableFragment) getSupportFragmentManager().findFragmentById(R.id.container)).tableAdapter.setClassList(getClassListOfWeek(weekClassList));
                        ((TimeTableFragment) getSupportFragmentManager().findFragmentById(R.id.container)).tableAdapter.notifyDataSetChanged();
                    }
                } else {
                    if (!(getSupportFragmentManager().findFragmentById(R.id.container) instanceof TimeTableFragment)) {
                        //把当前显示的碎片替换为Timetable
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, TimeTableFragment.newInstance("1", "2"), "Timetable")
                                .commit();
                    }
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    initWeekList();
                    //若当前显示的碎片不是Timetable
                    if (!(getSupportFragmentManager().findFragmentById(R.id.container) instanceof TimeTableFragment)) {
                        //把当前显示的碎片替换为Timetable
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, TimeTableFragment.newInstance("1", "2"), "Timetable")
                                .commit();
                    }
                    //更新数据
                    ((TimeTableFragment) getSupportFragmentManager().findFragmentById(R.id.container)).wrapper.notifyDataSetChanged();
                }
            default:
        }
    }

    //TODO: 空间换时间，存储新课的时候就把空课和占位存好

    /**
     * 取出数据库中在指定周的所有课，并添加空课和占位
     * 占位统一加在课的后面
     * @param selectedWeek 要取出课的指定周
     * @return List<List<Subject>> weekClassList 按每天一子数组存储的指定周的课表
     */
    public List<List<Subject>> fillWithEmptyClass(int selectedWeek) {
        weekClassList = new ArrayList<>(7);
        for (int i=0;i<daysPerWeek;i++)
            weekClassList.add(i, fillWeekdayWithEmpty(selectedWeek, i+1));
        return weekClassList;
    }

    /**
     * 取出数据库中在指定周的指定weekday的所有课，并添加空课和占位
     * 占位统一加在课的后面
     * @param selectedWeek 要取出课的指定周
     * @param weekday 要取出课的指定周的指定weekday
     * @return List<Subject> classList 指定weekday的课表
     */
    public List<Subject> fillWeekdayWithEmpty(int selectedWeek, int weekday){
        boolean isContinuing=false;
        int startEmpty=0;
        int startClass=0, endClass=classesPerDay+1;
        Subject[] subjects = new Subject[classesPerDay];
        //初始化
        for (int i=0;i<classesPerDay;i++){
            subjects[i] = new Subject();
        }

        //取出数据库中在指定周的指定weekday的所有课
        allClassList = LitePal.order("startperiod asc").find(Subject.class);//按小节的升序读取所有课程
        for (Subject elem : allClassList
                ) {
            //如果某节课在本周
            if (elem.getWeeks().contains(selectedWeek) && elem.getWeekday()==weekday) {
                subjects[elem.getStartPeriod()-1]=elem;//将该节课添加至开始处
            }
        }

        Subject empty = new Subject();
        for (int i=0;i<classesPerDay;i++){
            //遇到课就记下起止节
            if (subjects[i].getClass_name()!=null){
                startClass = subjects[i].getStartPeriod()-1;
                endClass = subjects[i].getEndPeriod()-1;
            }
            //第一次遇到不在上一节课起止节内的课才是空课的开始
            //若endclass为classesPerDay+1则说明未遇到过课
            if(!isContinuing && (endClass==classesPerDay+1 || i<startClass || i>endClass) && subjects[i].getClass_name()==null){
                startEmpty=i;
                empty = new Subject();
                empty.setWeekday(weekday);
                empty.setClass_name("空课");
                empty.setStartPeriod(i+1);
                isContinuing=true;
            }else if (isContinuing && subjects[i].getClass_name() != null || i == classesPerDay - 1){
                //连续的空课突然中断或者到了末尾还没中断
                empty.setEndPeriod(i);
                if(i==classesPerDay-1)empty.setEndPeriod(i+1);
                subjects[startEmpty]=empty;
                isContinuing=false;
            }
        }
        return new ArrayList<>(Arrays.asList(subjects));
    }

    //按依次从每天的课表中取出一节课的顺序把当周所有课放入一个列表
    public List<Subject> getClassListOfWeek(List<List<Subject>> weekClassList){
        List<Subject> allOfWeekList = new ArrayList<>();
        for (int j=0; j<classesPerDay;j++){
            for (int i=0; i<daysPerWeek;i++) allOfWeekList.add(weekClassList.get(i).get(j));
        }
        return allOfWeekList;
    }
}
