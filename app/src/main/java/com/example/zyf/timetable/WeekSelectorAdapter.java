package com.example.zyf.timetable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class WeekSelectorAdapter extends RecyclerView.Adapter<WeekSelectorAdapter.ViewHolder> {
    List<WeekItem> weekList;
    int textSize;
    public WeekSelectorAdapter(List<WeekItem> list, int size){
        weekList = list;
        textSize = size;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView weekNum;
        ViewHolder(View v){
            super(v);
            weekNum = v.findViewById(R.id.week_selector_num);
            weekNum.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
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
        if(weekList.get(i).isLit())
            viewHolder.itemView.setBackgroundColor(0xffe1bee7);
        else viewHolder.itemView.setBackgroundColor(0x01e1bee7);
    }

    @Override
    public int getItemCount() {
        return weekList.size();
    }
}
