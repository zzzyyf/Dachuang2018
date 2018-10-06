package com.example.zyf.timetable;

import android.service.autofill.TextValueSanitizer;
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
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView weekNum;
        public ViewHolder(View v){
            super(v);
            weekNum = v.findViewById(R.id.week_num);
        }
    }
    public PickerAdapter(List<Integer> list){
        weekList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.picker_item, viewGroup, false);
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
