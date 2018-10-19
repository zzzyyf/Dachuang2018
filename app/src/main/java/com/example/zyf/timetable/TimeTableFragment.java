package com.example.zyf.timetable;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static com.example.zyf.timetable.DateHelper.*;



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TimeTableFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimeTableFragment extends Fragment implements HandleScroll {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    int selectedWeek;

    RecyclerView tableView;
    RecyclerView picker;
    LinearLayoutManager linearManager;
    SnapHelper snapHelper;

    public TimeTableFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TimeTableFragment.
     */
    public static TimeTableFragment newInstance(String param1, String param2) {
        TimeTableFragment fragment = new TimeTableFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.timetable_layout, container, false);
        //TODO: 创建周数显示器（以及实现自动隐藏空白weekday功能）
        //TODO：添加recyclerView的空白（数据为空白时显示的）view

        //初始化日期选择器
        picker = view.findViewById(R.id.picker);

        //初始化RecyclerViews
        tableView = view.findViewById(R.id.table_view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void initFragment(List<SingleClassItem> classList){
        //获取数据
        initWeekList();
        //初始化picker
        linearManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        picker.setLayoutManager(linearManager);
        PickerAdapter weekAdapter = new PickerAdapter(weekList);
        PickerAdapterWrapper wrapper = new PickerAdapterWrapper(weekAdapter, this);
        View headerView = new PickerFillin(getContext());
        View footerView = new PickerFillin(getContext());
        wrapper.addHeaderView(headerView);
        wrapper.addFooterView(footerView);
        picker.setAdapter(wrapper);

        snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(picker);
        //set initial position of picker
        picker.scrollToPosition(currentWeek);

        //初始化tableView
        GridLayoutManager manager = new GridLayoutManager(getActivity(), daysPerWeek, LinearLayoutManager.VERTICAL, false);
        tableView.setLayoutManager(manager);
        SingleClassAdapter adapter = new SingleClassAdapter(classList);
        tableView.setAdapter(adapter);
    }

    @Override
    public void scrollToPosition(int position, TextView view) {
        smoothMoveToPosition(picker, position);
        PickerHelper.setDefaultColor(getResources().getColor(R.color.darkslategrey));
        PickerHelper.setHighlightColor(getResources().getColor(R.color.darkviolet));
        PickerHelper.changeWeekNumHighlight(view);
        selectedWeek = Integer.parseInt(view.getText().toString());
        Toast.makeText(getContext(), "第 "+selectedWeek+" 周", Toast.LENGTH_SHORT).show();
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
}