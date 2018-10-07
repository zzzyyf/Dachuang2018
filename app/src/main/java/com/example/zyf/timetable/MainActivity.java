package com.example.zyf.timetable;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

interface HandleScroll{
    void scrollToPosition(int position, TextView view);
}
public class MainActivity extends AppCompatActivity implements HandleScroll{
    RecyclerView tableView;
    RecyclerView picker;
    List<SingleClass> classList;
    List<Integer> weekList;
    LinearLayoutManager linearManager;
    SnapHelper snapHelper;
    int year, month, day;
    int currentWeek, endWeek=20;
    int currentWeekday;
    int selectedWeek;
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
        initWeekList(1, endWeek, weekList);

        //初始化RecyclerViews
        tableView = findViewById(R.id.table_view);
        GridLayoutManager manager = new GridLayoutManager(this, daysPerWeek, LinearLayoutManager.VERTICAL, false);
        tableView.setLayoutManager(manager);

        SingleClassAdapter adapter = new SingleClassAdapter(classList);
        tableView.setAdapter(adapter);

        picker = findViewById(R.id.picker);
        linearManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        picker.setLayoutManager(linearManager);

        PickerAdapter weekAdapter = new PickerAdapter(weekList);
        PickerAdapterWrapper wrapper = new PickerAdapterWrapper(weekAdapter, MainActivity.this);
        View headerView = LayoutInflater.from(this).inflate(R.layout.picker_fillin_layout, picker, false);
        View footerView = LayoutInflater.from(this).inflate(R.layout.picker_fillin_layout, picker, false);
        wrapper.addHeaderView(headerView);
        wrapper.addFooterView(footerView);
        picker.setAdapter(wrapper);

        snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(picker);
        //set initial position of picker
        picker.scrollToPosition(4);

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

   @Override
    public void scrollToPosition(int position, TextView view) {
        smoothMoveToPosition(picker, position);
        PickerHelper.changeWeekNumHighlight(view, getResources().getColor(R.color.darkslategrey), getResources().getColor(R.color.darkviolet));
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
