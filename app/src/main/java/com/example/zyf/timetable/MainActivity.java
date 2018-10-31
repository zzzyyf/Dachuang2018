package com.example.zyf.timetable;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import java.util.List;

import static com.example.zyf.timetable.DateHelper.getSelectedWeek;
import static com.example.zyf.timetable.DateHelper.initWeekList;
import static com.example.zyf.timetable.DateHelper.selectedWeek;
import static com.example.zyf.timetable.DateHelper.setDate;

public class MainActivity extends AppCompatActivity {
    final int HOME_FRAGMENT = 0, TIMETABLE_FRAGMENT = 1, PLAN_FRAGMENT = 2, EVENT_FRAGMENT = 3;
    final int EDIT_SUBJECT=5, ADD_SUBJECT = 10, EDIT_EVENT=15, ADD_EVENT = 20, ADD_PLAN = 30, SET_SEMESTER = 40;
    List<Subject> allClassList;
    List<List<Subject>> weekClassList;
    Fragment fragment;
    Toolbar toolbar;
    int selectedFragment = 0;
    BottomNavigationBar naviBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SQLiteDatabase db = LitePal.getDatabase();
        //LitePal.deleteAll("Subject");

        //TODO: 加入刷新操作
        setContentView(R.layout.activity_main);
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

        //初始化初始片段
        //防止旋转屏幕时Fragment重复加载
        if (savedInstanceState != null) {
            selectedFragment = savedInstanceState.getInt("selectedFragment", HOME_FRAGMENT);
        }
        switch (selectedFragment) {
            case HOME_FRAGMENT:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new HomeFragment(), "Home")
                        .commit();
                break;
            case TIMETABLE_FRAGMENT:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, TimeTableFragment.newInstance("param1", "param2"), "Timetable")
                        .commit();
                break;
            case EVENT_FRAGMENT:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new EventFragment(), "Event")
                        .commit();
                break;
            case PLAN_FRAGMENT:
                //TODO: PlanFragment unfinished
            default:
        }


        //初始化BottomNaviBar
        naviBar = findViewById(R.id.navi_main);
        naviBar
                .addItem(new BottomNavigationItem(R.drawable.home, "主页"))
                .addItem(new BottomNavigationItem(R.drawable.table, "课程表"))
                .addItem(new BottomNavigationItem(R.drawable.plan_list, "计划"))
                .addItem(new BottomNavigationItem(R.drawable.event_list, "事件"))
                .setFirstSelectedPosition(selectedFragment)
                .initialise();
        naviBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                switch (position) {
                    case HOME_FRAGMENT:
                        //选择“主页”
                        fragment = new HomeFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, fragment, "Home")
                                .commit();
                        invalidateOptionsMenu();
                        break;
                    case TIMETABLE_FRAGMENT:
                        //选择“课程表”
                        fragment = TimeTableFragment.newInstance("1", "2");
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, fragment, "Timetable")
                                .commit();
                        invalidateOptionsMenu();
                        break;
                    case PLAN_FRAGMENT:
                        //选择“计划”
                        invalidateOptionsMenu();
                    case EVENT_FRAGMENT:
                        //选择“事件”
                        fragment = new EventFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, fragment, "Event")
                                .commit();
                        invalidateOptionsMenu();
                    default:
                }
                selectedFragment = position;
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
        if (fragment == null) {
            fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        }
        //此处初始化fragment
        switch (selectedFragment) {
            case HOME_FRAGMENT:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new HomeFragment(), "Home")
                        .commit();
                break;
            case TIMETABLE_FRAGMENT:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment, "Timetable")
                        .commit();
                break;
            case EVENT_FRAGMENT:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new EventFragment(), "Event")
                        .commit();
                break;
            case PLAN_FRAGMENT:
                //
            default:
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        switch (selectedFragment){
            case HOME_FRAGMENT:
                menu.findItem(R.id.add_class).setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
                menu.findItem(R.id.add_event).setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
                menu.findItem(R.id.add_plan).setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
                break;
            case TIMETABLE_FRAGMENT:
                menu.findItem(R.id.add_event).setVisible(false);
                menu.findItem(R.id.add_plan).setVisible(false);
                menu.findItem(R.id.add_class).setVisible(true);
                break;
            case EVENT_FRAGMENT:
                menu.findItem(R.id.add_event).setVisible(true);
                menu.findItem(R.id.add_plan).setVisible(false);
                menu.findItem(R.id.add_class).setVisible(false);
                break;
            case PLAN_FRAGMENT:
                menu.findItem(R.id.add_event).setVisible(false);
                menu.findItem(R.id.add_plan).setVisible(true);
                menu.findItem(R.id.add_class).setVisible(false);
                break;
                default:
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_class:
                //点击添加课程按钮
                startActivityForResult(new Intent(MainActivity.this, AddClassActivity.class), ADD_SUBJECT);
                break;
            case R.id.set_table_item:
                startActivityForResult(new Intent(MainActivity.this, SetSemesterActivity.class), SET_SEMESTER);
                //点击课表设置按钮
                break;
            case R.id.add_event:
                startActivityForResult(new Intent(MainActivity.this, AddEventsActivity.class), ADD_EVENT);
                break;
            case R.id.add_plan:
                startActivityForResult(new Intent(MainActivity.this, AddPlansActivity.class), ADD_PLAN);
                break;
            default:
        }
        return true;
    }

    /**
     * 当从添加/更改的Activity返回时更新对应的更改
     *
     * @param requestCode 进行的操作
     * @param resultCode  是否做了更改，RESULT_OK为做了更改，RESULT_CANCELLED为更改取消了
     * @param data        返回Intent，内含辅助用数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case ADD_SUBJECT:
                if (resultCode == RESULT_OK) {
                    //若当前显示的碎片不是Timetable
                    if (!(getSupportFragmentManager().findFragmentById(R.id.container) instanceof TimeTableFragment)) {
                        //把当前显示的碎片替换为Timetable
                        fragment = TimeTableFragment.newInstance("1", "2");
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, fragment, "Timetable")
                                .commit();
                        naviBar.setFirstSelectedPosition(TIMETABLE_FRAGMENT);
                    }
                    //只需部分更新即可
                    int weekday = data.getIntExtra("weekday", 1);
                    if (LitePal.findLast(Subject.class).getWeeks().contains(getSelectedWeek())) {
                        ((TimeTableFragment) fragment).partialUpdateTableList(weekday);
                    }
                }else if(resultCode == 3) {//删除了对应课程
                    //若当前显示的碎片不是Timetable
                    if (!(getSupportFragmentManager().findFragmentById(R.id.container) instanceof TimeTableFragment)) {
                        //把当前显示的碎片替换为Timetable
                        fragment = TimeTableFragment.newInstance("1", "2");
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, fragment, "Timetable")
                                .commit();
                        naviBar.setFirstSelectedPosition(TIMETABLE_FRAGMENT);
                    }
                    //只需部分更新即可
                    int weekday = data.getIntExtra("weekday", 1);
                    if (LitePal.findLast(Subject.class).getWeeks().contains(getSelectedWeek())) {
                        ((TimeTableFragment) fragment).partialUpdateTableList(weekday);
                    }
                }
                break;
            case SET_SEMESTER:
                if (resultCode == RESULT_OK) {
                    initWeekList();
                    //若当前显示的碎片不是Timetable
                    if (!(getSupportFragmentManager().findFragmentById(R.id.container) instanceof TimeTableFragment)) {
                        //把当前显示的碎片替换为Timetable
                        fragment = TimeTableFragment.newInstance("1", "2");
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, fragment, "Timetable")
                                .commit();
                        naviBar.setFirstSelectedPosition(TIMETABLE_FRAGMENT);
                    }
                    //更新数据
                    ((TimeTableFragment) fragment).wrapper.notifyDataSetChanged();
                    ((TimeTableFragment) fragment).updateEntireTableList(selectedWeek, false);
                }
                break;
            case EDIT_SUBJECT:
                if (resultCode == RESULT_OK) {
                    //若当前显示的碎片不是Timetable
                    if (!(getSupportFragmentManager().findFragmentById(R.id.container) instanceof TimeTableFragment)) {
                        //把当前显示的碎片替换为Timetable
                        fragment = TimeTableFragment.newInstance("1", "2");
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, fragment, "Timetable")
                                .commit();
                        naviBar.setFirstSelectedPosition(TIMETABLE_FRAGMENT);
                    }
                    //只需部分更新即可
                    int weekday = data.getIntExtra("weekday", 1);
                    ((TimeTableFragment) fragment).partialUpdateTableList(weekday);
                }else if(resultCode == 3) {//删除了对应课程
                    //若当前显示的碎片不是Timetable
                    if (!(getSupportFragmentManager().findFragmentById(R.id.container) instanceof TimeTableFragment)) {
                        //把当前显示的碎片替换为Timetable
                        fragment = TimeTableFragment.newInstance("1", "2");
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, fragment, "Timetable")
                                .commit();
                        naviBar.setFirstSelectedPosition(TIMETABLE_FRAGMENT);
                    }
                    //只需部分更新即可
                    int weekday = data.getIntExtra("weekday", 1);
                    ((TimeTableFragment) fragment).partialUpdateTableList(weekday);
                }
                break;
            default:
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("selectedFragment", selectedFragment);
        super.onSaveInstanceState(outState);
    }
}
