package com.example.zyf.timetable;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class WeekSelectorAdapter extends RecyclerView.Adapter<WeekSelectorAdapter.ViewHolder> {
    List<WeekItem> weekList;
    public WeekSelectorAdapter(List<WeekItem> list){
        weekList = list;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView weekNum;
        ViewHolder(View v){
            super(v);
            weekNum = v.findViewById(R.id.week_selector_num);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.selector_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.weekNum.setText(weekList.get(i).getWeekNum());
        if(weekList.get(i).getLit()!=0)
            viewHolder.itemView.setBackgroundColor(0xffe1bee7);
        else viewHolder.itemView.setBackgroundColor(0x01e1bee7);
    }

    @Override
    public int getItemCount() {
        return weekList.size();
    }
}
