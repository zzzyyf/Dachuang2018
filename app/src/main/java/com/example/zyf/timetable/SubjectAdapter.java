package com.example.zyf.timetable;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.zyf.timetable.db.Subject;

import java.util.ArrayList;
import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder> {
    Context context;
    List<PopupWindow> popupWindows;
    int pos;
    private List<Subject> classList;
    //TODO: 为每个格子随机选取给定的颜色
    public SubjectAdapter(Context context, List<Subject> classList) {
        this.context = context;
        this.classList = classList;
        popupWindows = new ArrayList<>(classList.size());

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.class_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        viewHolder.className.setText(classList.get(i).getClass_name());
        viewHolder.classPlace.setText(classList.get(i).getClass_place());
        pos = viewHolder.getAdapterPosition();
        popupWindows.add(pos, new PopupWindow(LayoutInflater.from(context).inflate(R.layout.timetable_menu,
                (ViewGroup) ((Activity) context).findViewById(android.R.id.content), false), ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        popupWindows.get(pos).setBackgroundDrawable(context.getDrawable(R.drawable.table_menu_bg));
        popupWindows.get(pos).setElevation(dp2px(8));
        // 设置PopupWindow是否能响应外部点击事件
        popupWindows.get(pos).setOutsideTouchable(true);
        // 设置PopupWindow是否能响应点击事件
        popupWindows.get(pos).setTouchable(true);
        //TODO:弹出菜单的点击事件

        int span = classList.get(i).getEndPeriod() - classList.get(i).getStartPeriod() + 1;
        LayoutParams params = viewHolder.itemView.getLayoutParams();
        params.height = dp2px(80) * span;
        viewHolder.itemView.setLayoutParams(params);
        viewHolder.itemView.setElevation(dp2px(1));
        viewHolder.itemView.setFocusableInTouchMode(true);
        viewHolder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    viewHolder.itemView.setElevation(dp2px(8));
                    pos = viewHolder.getAdapterPosition();
                    ((TextView) popupWindows.get(pos).getContentView().findViewById(R.id.table_menu_title)).setText(classList.get(pos).getClass_name());
                    if(classList.get(pos).getClass_name()!=null && !classList.get(pos).getClass_name().equals("空课")) {
                        ((TextView) popupWindows.get(pos).getContentView().findViewById(R.id.table_menu_content)).setText(DateHelper.weeksToString(classList.get(pos).getWeeks()));
                    }else{
                        ((TextView) popupWindows.get(pos).getContentView().findViewById(R.id.table_menu_content)).setText("空课");
                    }
                    popupWindows.get(pos).showAsDropDown(viewHolder.itemView);
                }else{
                    viewHolder.itemView.setElevation(dp2px(1));
                }
            }
        });
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return classList.size();
    }

    public void setClassList(List<Subject> classList) {
        this.classList = classList;
    }

    private int dp2px(int dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView className;
        TextView classPlace;

        public ViewHolder(View v) {
            super(v);
            className = v.findViewById(R.id.class_name);
            classPlace = v.findViewById(R.id.class_place);
        }
    }

}
