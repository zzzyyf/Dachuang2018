package com.example.zyf.timetable;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
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
    //TODO: 只读取每周的第一天就结束了
    public SubjectAdapter(Context context, List<Subject> classList) {
        this.context = context;
        this.classList = classList;
        popupWindows = new ArrayList<>(this.classList.size());

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.class_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        viewHolder.className.setText(classList.get(pos).getClass_name());
        viewHolder.classPlace.setText(classList.get(pos).getClass_place());
        pos = viewHolder.getAdapterPosition();
        popupWindows.add(pos, new PopupWindow(LayoutInflater.from(context).inflate(R.layout.timetable_menu,
                (ViewGroup) ((Activity) context).findViewById(android.R.id.content), false), ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        popupWindows.get(pos).setBackgroundDrawable(context.getDrawable(R.drawable.table_menu_bg));
        popupWindows.get(pos).setElevation((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, context.getResources().getDisplayMetrics()));
        // 设置PopupWindow是否能响应外部点击事件
        popupWindows.get(pos).setOutsideTouchable(true);
        // 设置PopupWindow是否能响应点击事件
        popupWindows.get(pos).setTouchable(true);
        //TODO:弹出菜单的点击事件
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                 * LitePal.where() get weekday==x的所有课;
>                * 删掉period不符合的其他课；
>                * 把最接近本周的课置顶，其他课按周序、字母序排列
>                * 对选择的课程调用修改课程界面
>                * （可选）做一个卡片显示选择的课程
                 */
                ((TextView) popupWindows.get(pos).getContentView().findViewById(R.id.table_menu_title)).setText(classList.get(pos).getClass_name());
                if(!classList.get(pos).getClass_name().equals("空课")) {
                    ((TextView) popupWindows.get(pos).getContentView().findViewById(R.id.table_menu_content)).setText(DateHelper.weeksToString(classList.get(pos).getWeeks()));
                }else{
                    ((TextView) popupWindows.get(pos).getContentView().findViewById(R.id.table_menu_content)).setText("空课");
                }
                popupWindows.get(pos).showAsDropDown(viewHolder.itemView);
            }
        });
        int span = classList.get(pos).getEndPeriod() - classList.get(pos).getStartPeriod() + 1;
        viewHolder.className.setPadding(0, dp2px(80) * span / 2 - viewHolder.className.getHeight() - dp2px(1), 0, 0);
        viewHolder.classPlace.setPadding(0, 0, 0, dp2px(80) * span / 2 - viewHolder.classPlace.getHeight() - dp2px(1));
        LayoutParams params = viewHolder.itemView.getLayoutParams();
        params.height = dp2px(80) * span;
        viewHolder.itemView.setLayoutParams(params);

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
        TextView header, footer;

        public ViewHolder(View v) {
            super(v);
            className = v.findViewById(R.id.class_name);
            classPlace = v.findViewById(R.id.class_place);
        }
    }

}
