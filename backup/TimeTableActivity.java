package com.example.zyf.timetable;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import java.util.ArrayList;
import java.util.List;

import static com.example.zyf.timetable.DateHelper.*;

interface HandleScroll{
    void scrollToPosition(int position, TextView view);
}
public class TimeTableActivity extends AppCompatActivity implements HandleScroll{
    int selectedWeek;

    RecyclerView tableView;
    RecyclerView picker;
    List<SingleClass> classList;
    LinearLayoutManager linearManager;
    SnapHelper snapHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetable_layout);

        //初始化Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //初始化Lists
        classList = new ArrayList<>();
        initClassListTest(classList);
        initWeekList();

        //初始化日期选择器
        picker = findViewById(R.id.picker);
        linearManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        picker.setLayoutManager(linearManager);

        PickerAdapter weekAdapter = new PickerAdapter(DateHelper.weekList);
        PickerAdapterWrapper wrapper = new PickerAdapterWrapper(weekAdapter, TimeTableActivity.this);
        View headerView = LayoutInflater.from(this).inflate(R.layout.picker_fillin_layout, picker, false);
        View footerView = LayoutInflater.from(this).inflate(R.layout.picker_fillin_layout, picker, false);
        wrapper.addHeaderView(headerView);
        wrapper.addFooterView(footerView);
        picker.setAdapter(wrapper);

        snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(picker);
        //set initial position of picker
        picker.scrollToPosition(4);

        //初始化RecyclerViews
        tableView = findViewById(R.id.table_view);
        GridLayoutManager manager = new GridLayoutManager(this, daysPerWeek, LinearLayoutManager.VERTICAL, false);
        tableView.setLayoutManager(manager);

        SingleClassAdapter adapter = new SingleClassAdapter(classList);
        tableView.setAdapter(adapter);

        //初始化BottomNaviBar

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

    @Override
    public void scrollToPosition(int position, TextView view) {
        smoothMoveToPosition(picker, position);
        PickerHelper.setDefaultColor(getResources().getColor(R.color.darkslategrey));
        PickerHelper.setHighlightColor(getResources().getColor(R.color.darkviolet));
        PickerHelper.changeWeekNumHighlight(view);
        selectedWeek = Integer.parseInt(view.getText().toString());
        Toast.makeText(this, "第 "+selectedWeek+" 周", Toast.LENGTH_SHORT).show();
    }

    /**
     * 滑动到指定位置
     * 已知目前的顶端位置和目标中间位置，首先把目标中间位置转换为目标顶端位置（-2）
     * 之后，若目标顶端位置在目前顶端之前（不可见），则直接滚动至目标顶端位置即可
     * 若目标顶端位置目前可见，则需要测算目标顶端至容器顶的距离，这需要算出目标顶端在容器中的序号，
     * 所以序号为目标顶端-目前可见顶端。测算出距离之后再滚动即可。
     * 若目标顶端位置在目前底端之后，则先滚动至可见，再滚动至目前顶端，即先case1再case2.
     * @param mRecyclerView
     * @param position 欲滚动至RecyclerView中间的位置
     */
    public void smoothMoveToPosition(RecyclerView mRecyclerView, int position) {
        // 第一个可见位置
        //用getChildAt(0)出现连续前点时向前偏移的现象
        int firstItem = ((LinearLayoutManager)mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        // 最后一个可见位置
        int lastItem = ((LinearLayoutManager)mRecyclerView.getLayoutManager()).findLastVisibleItemPosition();

        //对添加了头、尾的修正
        if(firstItem<1)firstItem=1;
        if(lastItem>endWeek)lastItem=endWeek;

        // 跳转位置在第一个可见项之后，最后一个可见项之前
        // smoothScrollToPosition根本不会动，此时调用smoothScrollBy来滑动到指定位置
        View centerView = snapHelper.findSnapView(linearManager);
        int length = centerView.getWidth();
        int center = linearManager.getPosition(centerView);
        mRecyclerView.smoothScrollBy(length*(position-center), 0);
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

    @Override
    protected void onDestroy() {
        PickerHelper.freeTextView();
        super.onDestroy();
    }

}
