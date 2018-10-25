package com.example.zyf.timetable;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.zyf.timetable.db.Subject;

import java.util.ArrayList;
import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder> {
    Context context;
    List<PopupWindow> popupWindows;
    int pos;
    int id[];
    private List<List<Subject>> classList;

    public SubjectAdapter(Context context, List<List<Subject>> classList) {
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
        id = getPosId(i);
        viewHolder.className.setText(classList.get(id[0]).get(id[1]).getClass_name());
        viewHolder.classPlace.setText(classList.get(id[0]).get(id[1]).getClass_place());
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
                id = getPosId(pos);
                ((TextView) popupWindows.get(pos).getContentView().findViewById(R.id.table_menu_title)).setText(classList.get(id[0]).get(id[1]).getClass_name());
                ((TextView) popupWindows.get(pos).getContentView().findViewById(R.id.table_menu_content)).setText(DateHelper.weeksToString(classList.get(id[0]).get(id[1]).getWeeks()));
                popupWindows.get(pos).showAsDropDown(viewHolder.itemView);
            }
        });

    }

    @Override
    public int getItemCount() {
        return classList.size();
    }

    public void setClassList(List<List<Subject>> classList) {
        this.classList = classList;
    }

    /**
     * 计算出给定的id是在周几的第几节课
     *
     * @param i 给定的id
     * @return int[2] = {weekday, session}
     */
    private int[] getPosId(int i) {
        //TODO: unfinished
        int j = 0, pos = 0, num = 0;//pos为在每个子数组中的位置，num为第num个子数组
        int sum = 0;
        for (; j < classList.size() - 1; j++) {
            sum += (classList.get(j).size());
        }
        for (j = 0; i >= 0 && j < sum - 1; j++, i--) {
            num = j % DateHelper.daysPerWeek;
            if (num != 0) num++;//

            if (j != 0 && num == 0) pos++;//num不是第一次触发时为0，说明已经把所有子数组中第pos个元素都读取完了。
            while (num < DateHelper.daysPerWeek && pos >= classList.get(num).size())
                //已经超过了某天的课程数
                num++;
            if (num > classList.size() - 1)
                num = 0;
        }
        return new int[]{num, pos};
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
