package com.example.zyf.timetable;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zyf.timetable.db.Event;

import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView mRecycleView;
    private RecyclerView.Adapter mAdapter;
    private List<EventItem> eventList = new ArrayList<>();

    private RecyclerView.LayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eventList.clear();
        initData();
//        initView();
        RecyclerView recyclerView = view.findViewById(R.id.event_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        EventAdapter adapter = new EventAdapter(eventList, getContext());
        recyclerView.setAdapter(adapter);


    }

    private void initData() {
        List<Event> events = LitePal.findAll(Event.class);

        int num = 1;
        for (Event event : events) {
            // 获取当前日期
            DateHelper.setDate();
            int year = DateHelper.year;
            int month = DateHelper.month;
            int day = DateHelper.day;
            String nowDate = year + "-" + month + "-" + day; //当前日期

            String date = event.getEventYear() + "-" + event.getEventMonth() + "-" + event.getEventDay(); //第一个日期
//算两个日期间隔多少天
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date1 = format.parse(date);
                Date date2 = format.parse(nowDate);
                int a = (int) ((date1.getTime() - date2.getTime()) / (1000 * 3600 * 24));
                EventItem item = new EventItem(event.getEventName(), a, event.getEventColor(), event.getEventYear() + "年" + event.getEventMonth() + "月" + event.getEventDay() + "日", event.getEventNote(), event.getId());

                eventList.add(item);
            } catch (Exception ex) {
                Log.e("HomeFragment", "initData: " + ex.toString());
            }
        }

    }
}
