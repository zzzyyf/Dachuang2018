package com.example.zyf.timetable;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class WeekSelectorAdapter extends RecyclerView.Adapter<WeekSelectorAdapter.ViewHolder> {
    List<Integer> weekList;
    public WeekSelectorAdapter(List<Integer> list){
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
        viewHolder.weekNum.setText(weekList.get(i).toString());
    }

    @Override
    public int getItemCount() {
        return weekList.size();
    }
}
