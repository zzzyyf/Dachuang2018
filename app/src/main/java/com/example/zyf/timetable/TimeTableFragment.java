package com.example.zyf.timetable;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.example.zyf.timetable.db.Subject;
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
    RecyclerView tableView;
    RecyclerView picker;
    LinearLayoutManager linearManager;
    GridLayoutManager tableManager;
    SnapHelper snapHelper;
    SubjectAdapter tableAdapter;
    PickerAdapterWrapper wrapper;
    boolean isDragging;//判断scroll是否是用户主动拖拽
    boolean isScrolling;//判断scroll是否处于滑动中
    int position;
    private String mParam1;
    private String mParam2;


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
        //TODO: 创建周几显示器（以及实现自动隐藏空白weekday功能）
        //TODO: 创建节数显示器
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

    public void initFragment(List<List<Subject>> classList) {
        //获取数据
        initWeekList();
        //初始化picker
        linearManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        picker.setLayoutManager(linearManager);
        PickerAdapter weekAdapter = new PickerAdapter(weekList);
        wrapper = new PickerAdapterWrapper(weekAdapter, this);
        View headerView = new PickerFillin(getContext());
        View footerView = new PickerFillin(getContext());
        wrapper.addHeaderView(headerView);
        wrapper.addFooterView(footerView);
        picker.setAdapter(wrapper);
        picker.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int firstPosition;
                switch(newState){
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        if (!isDragging && !isScrolling){
                            isScrolling = true; //a scrolling occurs
                        }
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        isDragging = true; //如果是用户主动滑动recyclerview，则不触发位置计算。
                        break;
                    case RecyclerView.SCROLL_STATE_IDLE:
                        if (!isDragging && isScrolling){
                            isDragging = false;
                            isScrolling = false;
                            PickerHelper.changeWeekNumHighlight(
                                    ((PickerAdapter.ViewHolder)picker.findViewHolderForAdapterPosition(position)).weekNum);  //由于滚动事件会多次触发IDLE状态，我们只需要在第一次IDLE被触发时获取ItemView。
                        }
                        break;
                }
            }
        });

        snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(picker);


        //初始化tableView
        tableManager = new GridLayoutManager(getActivity(), daysPerWeek, LinearLayoutManager.VERTICAL, false);
        tableView.setLayoutManager(tableManager);
        tableAdapter = new SubjectAdapter(getContext(), classList);
        tableView.setAdapter(tableAdapter);
    }

    /**
     * 滚动至将指定的adapter位置居中的位置
     * @param position 要居中的adapter位置
     */
    @Override
    public void scrollToPosition(int position) {
        this.position = position;
        smoothMoveToPosition(picker, position);
        PickerHelper.setDefaultColor(getResources().getColor(R.color.darkslategrey));
        PickerHelper.setHighlightColor(getResources().getColor(R.color.darkviolet));

        Toast.makeText(getContext(), "第 " + position + " 周", Toast.LENGTH_SHORT).show();
        //更新数据
        DateHelper.setSelectedWeek(position);
        tableAdapter.setClassList(((MainActivity) getActivity()).fillWithEmptyClass(getSelectedWeek()));
        tableAdapter.notifyDataSetChanged();
    }

    /**
     * 滑动到指定的adapter位置
     * 已知目前的顶端位置和目标中间位置，首先把目标中间位置转换为目标顶端位置（-2）
     * 之后，若目标顶端位置在目前顶端之前（不可见），则直接滚动至目标顶端位置即可
     * 若目标顶端位置目前可见，则需要测算目标顶端至容器顶的距离，这需要算出目标顶端在容器中的序号，
     * 所以序号为目标顶端-目前可见顶端。测算出距离之后再滚动即可。
     * 若目标顶端位置在目前底端之后，则先滚动至可见，再滚动至目前顶端，即先case1再case2.
     *
     * @param mRecyclerView
     * @param position      欲滚动至RecyclerView中间的adapter位置
     */
    public void smoothMoveToPosition(RecyclerView mRecyclerView, int position) {
        // 第一个可见位置
        //用getChildAt(0)出现连续前点时向前偏移的现象
        int firstItem = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        // 最后一个可见位置
        int lastItem = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findLastVisibleItemPosition();

        //对添加了头、尾的修正
        if (firstItem < 1) firstItem = 1;
        if (lastItem > endWeek) lastItem = endWeek;

        // 跳转位置在第一个可见项之后，最后一个可见项之前
        // smoothScrollToPosition根本不会动，此时调用smoothScrollBy来滑动到指定位置

        View centerView = snapHelper.findSnapView(linearManager);
        int length = centerView.getWidth();
        int center = linearManager.getPosition(centerView);
        mRecyclerView.smoothScrollBy((length+1) * (position - center), 0);


    }

    @Override
    public void onDestroy() {
        PickerHelper.freeTextView();
        super.onDestroy();
    }
}