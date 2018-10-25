package com.example.zyf.timetable;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.zyf.timetable.db.Subject;
import com.example.zyf.timetable.db.WeekSettings;

import org.litepal.LitePal;

import java.util.ArrayList;
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
            ((TimeTableFragment) fragments[1]).initFragment(weekClassList);

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
                    int weekday = data.getIntExtra("weekday", 0);
                    //TODO: 仅当添加了在本周的课程时才更新
                    weekClassList.set(weekday, fillWeekdayWithEmpty(getSelectedWeek(), weekday));
                    ((TimeTableFragment) getSupportFragmentManager().findFragmentById(R.id.container)).tableAdapter.setClassList(weekClassList);
                    ((TimeTableFragment) getSupportFragmentManager().findFragmentById(R.id.container)).tableAdapter.notifyDataSetChanged();
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

    public List<List<Subject>> fillWithEmptyClass(int selectedWeek) {
        List<Subject> classList = new ArrayList<>();
        weekClassList = new ArrayList<>(7);
        for (int i = 0; i < 7; i++) {
            weekClassList.add(new ArrayList<Subject>());
        }
        for (Subject elem : allClassList
                ) {
            //如果某节课在本周
            if (elem.getWeeks().contains(selectedWeek)) {
                classList.add(elem);
                //将该节课添加至其对应的一天
                weekClassList.get(elem.getWeekday()).add(elem);
            }
        }

        for (int i = 0; i < daysPerWeek; i++) {
            List<Integer> period = new ArrayList<>();
            boolean isContinuing = false;//true为在一节空课内
            Subject empty=new Subject();
            for (int k = 0,j = 0; j < classesPerDay; j++,k++) {
                Subject elem;
                //若j已经过了最后一节有课的课，即后面全是空课
                if (k> weekClassList.get(i).size()-1) {
                    empty = new Subject();
                    empty.setWeekday(i);
                    for (;j<classesPerDay;j++)
                        period.add(j);
                    empty.setStartPeriod(period.get(0)+1);
                    empty.setEndPeriod(period.get(period.size()-1)+1);
                    weekClassList.get(i).add(empty);
                    break;//周i的空课添加完了
                }
                //没过的话还得检查j是否在这一节课之前
                elem = weekClassList.get(i).get(k);
                //若不是在一节空课里，就跳过有课的课
                while(!isContinuing && (j>=(elem.getStartPeriod()-1) && j<=(elem.getEndPeriod()-1)))j++;
                //现在j要么在有课之前的空课，要么后面全是空课，要么==classesPerDay
                if(j==classesPerDay)break;//周i的空课添加完了

                //若j及后面全是空课
                if(j>elem.getEndPeriod()-1) {
                    empty = new Subject();
                    empty.setWeekday(i);
                    for (; j < classesPerDay; j++)
                        period.add(j);
                    empty.setStartPeriod(period.get(0)+1);
                    empty.setEndPeriod(period.get(period.size()-1)+1);
                    weekClassList.get(i).add(empty);
                    break;//周i的空课添加完了
                } else{
                    //如果不连续说明刚刚出现空课
                    if (!isContinuing) {
                        //初始化空课
                        empty = new Subject();
                        empty.setWeekday(i);
                        period.add(j);
                        empty.setStartPeriod(period.get(0)+1);
                        empty.setEndPeriod(period.get(period.size()-1)+1);
                        isContinuing = true;
                    } else {
                        period.add(j);
                        empty.setStartPeriod(period.get(0)+1);
                        empty.setEndPeriod(period.get(period.size()-1)+1);
                    }
                    if(j==elem.getEndPeriod()-1) {
                        weekClassList.get(i).add(k, empty);
                        isContinuing=false;
                    }
                }
                //若j在已有课程范围内则直接跳过j即可
            }
        }
        return weekClassList;
    }

    public List<Subject> fillWeekdayWithEmpty(int selectedWeek, int weekday){
        List<Subject> classList = new ArrayList<>();
        for (Subject elem : allClassList
                ) {
            //如果某节课在本周
            if (elem.getWeeks().contains(selectedWeek) && elem.getWeekday()==weekday) {
                classList.add(elem);//将该节课添加
            }
        }

        List<Integer> period = new ArrayList<>();
        boolean isContinuing = false;//true为在一节空课内
        Subject empty=new Subject();
        for (int k = 0,j = 0; j < classesPerDay; j++,k++) {
            Subject elem;
            //若j已经过了最后一节有课的课，即后面全是空课
            if (k> classList.size()-1) {
                empty = new Subject();
                empty.setWeekday(weekday);
                for (;j<classesPerDay;j++)
                    period.add(j);
                empty.setStartPeriod(period.get(0)+1);
                empty.setEndPeriod(period.get(period.size()-1)+1);
                classList.add(empty);
                break;//周i的空课添加完了
            }
            //没过的话还得检查j是否在这一节课之前
            elem = classList.get(k);
            //若不是在一节空课里，就跳过有课的课
            while(!isContinuing && (j>=(elem.getStartPeriod()-1) && j<=(elem.getEndPeriod()-1)))j++;
            //现在j要么在有课之前的空课，要么后面全是空课，要么==classesPerDay
            if(j==classesPerDay)break;//周i的空课添加完了

            //若j及后面全是空课
            if(j>elem.getEndPeriod()-1) {
                empty = new Subject();
                empty.setWeekday(weekday);
                for (; j < classesPerDay; j++)
                    period.add(j);
                empty.setStartPeriod(period.get(0)+1);
                empty.setEndPeriod(period.get(period.size()-1)+1);
                classList.add(empty);
                break;//周i的空课添加完了
            } else{
                //如果不连续说明刚刚出现空课
                if (!isContinuing) {
                    //初始化空课
                    empty = new Subject();
                    empty.setWeekday(weekday);
                    period.add(j);
                    empty.setStartPeriod(period.get(0)+1);
                    empty.setEndPeriod(period.get(period.size()-1)+1);
                    isContinuing = true;
                } else {
                    period.add(j);
                    empty.setStartPeriod(period.get(0)+1);
                    empty.setEndPeriod(period.get(period.size()-1)+1);
                }
                if(j==elem.getEndPeriod()-1) {
                    classList.add(k, empty);
                    isContinuing=false;
                }
            }
            //若j在已有课程范围内则直接跳过j即可
        }

        return classList;
    }
}
