package com.example.zyf.timetable;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class PickerAdapter extends RecyclerView.Adapter<PickerAdapter.ViewHolder> {
    List<Integer> weekList;
    int startWeek, endWeek;
    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView weekNum;
        protected  ViewHolder(View v){
            super(v);
            weekNum = v.findViewById(R.id.week_num);
        }
    }
    public PickerAdapter(List<Integer> list){
        weekList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.picker_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        int num = Integer.parseInt(weekList.get(i).toString());
        viewHolder.weekNum.setText(num+"");
        if(PickerHelper.weekNum!=null) PickerHelper.checkHighlight(viewHolder.weekNum);
    }

    @Override
    public int getItemCount() {
        return weekList.size();
    }
}
