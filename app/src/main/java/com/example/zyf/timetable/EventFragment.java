package com.example.zyf.timetable;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zyf.timetable.db.Event;

import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class EventFragment extends Fragment {
    private RecyclerView mRecyclerView;

    private RecyclerView.Adapter mAdapter;

    private List<EventItem> eventlist = new ArrayList<>();

    private RecyclerView.LayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.event_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LitePal.getDatabase();
        eventlist.clear();
        initData();

        RecyclerView recyclerView = view.findViewById(R.id.eventview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        EventAdapter adapter = new EventAdapter(eventlist, getContext());
        recyclerView.setAdapter(adapter);


    }

    private void initData() {

        List<Event> events = LitePal.findAll(Event.class);

        //long num;
        for (Event event : events) {
            //Date date = new Date(System.currentTimeMillis());//获取当前日期
            Calendar calendar = Calendar.getInstance();

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            String nowdate = year + "-" + month + "-" + day; //当前日期

            String date = event.getEventYear() + "-" + event.getEventMonth() + "-" + event.getEventDay(); //第一个日期
//算两个日期间隔多少天
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date1 = format.parse(date);
                Date date2 = format.parse(nowdate);
                int a = (int) ((date1.getTime() - date2.getTime()) / (1000 * 3600 * 24));
                EventItem list = new EventItem(event.getEventName(), a, event.getEventColor(), event.getEventYear() + "年" + event.getEventMonth() + "月" + event.getEventDay() + "日", event.getEventNote(), event.getId());
                //num++;
                eventlist.add(list);
            } catch (Exception ex) {

            }

        }

    }
}
