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
                    //TODO: 添加了课程后不会更新，且切换周也不更新，重启才更新
                    if(LitePal.findLast(Subject.class).getWeeks().contains(getSelectedWeek())) {
                        weekClassList.set(weekday, fillWeekdayWithEmpty(getSelectedWeek(), weekday));
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

    //TODO: 逻辑未完成，添加课程后无法正常更新和显示
    //TODO: 实际上应放入总共daysPerWeek*classesPerDay个课，除了底下sum节课以外的其他课按照正常课表中的顺序放入高度为0的占位
    //TODO: 空课不会根据已有课程的更改而更改

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
        List<Subject> classList = new ArrayList<>();
        int endOfLastClass=0;//最后一节课结束的小节数
        //取出数据库中在指定周的指定weekday的所有课
        allClassList = LitePal.order("startperiod asc").find(Subject.class);//按小节的升序读取所有课程
        for (Subject elem : allClassList
                ) {
            //如果某节课在本周
            if (elem.getWeeks().contains(selectedWeek) && elem.getWeekday()==weekday) {
                classList.add(elem);//将该节课添加
            }
        }
        if(classList.size()!=0)endOfLastClass=classList.get(classList.size()-1).getEndPeriod();

        List<Integer> period = new ArrayList<>();
        boolean isContinuing = false;//true为在一节空课内
        Subject empty=new Subject();
        //j:一天中第j小节课；k:classList中本大节课开始的index
        for (int k = 0,j = 1; j <= classesPerDay; j++) {
            Subject elem;
            if (!isContinuing) {
                //若j已经过了最后一节有课的课，即后面全是空课，此时k==size()
                if (j > endOfLastClass-1 ) {
                    empty = new Subject();
                    empty.setWeekday(weekday);
                    for (; j <= classesPerDay; j++)
                        period.add(j);
                    empty.setStartPeriod(period.get(0));
                    empty.setEndPeriod(period.get(period.size() - 1));
                    empty.setClass_name("空课");
                    classList.add(k, empty);
                    for (int l = empty.getStartPeriod(); l < empty.getEndPeriod(); l++)
                        classList.add(k + 1, new Subject());//若课长为n小节，则需在课后添加n-1小节的占位
                    return classList;//周i的空课添加完了
                }
                //后面还有课的话还得检查j是否在这一节课之前
                //为之后连续的n节课或1节课添加占位

                do {
                    elem = classList.get(k);
                    //若不是在一节空课里，就给下一节课占位
                    while (!isContinuing && j >= elem.getStartPeriod() && j <= elem.getEndPeriod()) {
                        if (j < elem.getEndPeriod()) {
                            classList.add(++k, new Subject());//给已有的课后添加占位
                        }
                        j++;
                    }
                    k++;//让k指向下一节课
                    //现在j要么是在一节跟上一节连续的课，要么在有课之前的空课，要么后面全是空课，要么==classesPerDay+1
                    if (j == classesPerDay + 1) return classList;//周i的空课添加完了
                }
                while (k < classList.size() && j == classList.get(k).getStartPeriod());//若j在一节跟上一节连续的课就继续
            }
            //现在j要么在有课之前的空课，要么后面全是空课
            if(j>endOfLastClass) {
                //若j及后面全是空课
                empty = new Subject();
                empty.setWeekday(weekday);
                for (; j <= classesPerDay; j++)
                    period.add(j);
                empty.setStartPeriod(period.get(0));
                empty.setEndPeriod(period.get(period.size()-1));
                empty.setClass_name("空课");
                classList.add(k, empty);
                for (int l=empty.getStartPeriod();l<empty.getEndPeriod();l++) classList.add(k+1,new Subject());//若课长为n小节，则需在课后添加n-1小节的占位
                return classList;//周i的空课添加完了
            } else{
                //这节空课后面还有课
                //如果不连续说明刚刚出现空课
                if (!isContinuing) {
                    //初始化空课
                    empty = new Subject();
                    period.add(j);
                    isContinuing = true;
                } else {
                    period.add(j);
                }
                if(j==classList.get(k).getStartPeriod()-1) {
                    //若执行到一节课之前说明该节空课添加完了
                    empty.setWeekday(weekday);
                    empty.setStartPeriod(period.get(0));
                    empty.setEndPeriod(period.get(period.size()-1));
                    empty.setClass_name("空课");
                    classList.add(k, empty);//空课应加在k之前
                    for (int l=empty.getStartPeriod();l<empty.getEndPeriod();l++) {
                        classList.add(++k, new Subject());//若课长为l小节，则需在课后添加l-1小节的占位
                    }
                        k++;//让k指向下一节课
                        period.clear();
                    isContinuing=false;
                }
            }
            //若j在已有课程范围内则直接跳过j即可
        }

        return classList;
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
