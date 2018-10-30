package com.example.zyf.timetable;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.zyf.timetable.db.Event;
import org.litepal.LitePal;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private List<EventItem> mEventList;

    private Resources mResource;
    private Context mContext;

    public EventAdapter(List<EventItem> eventList, Context context) {
        mEventList = eventList;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.event_item, viewGroup, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final EventItem eventItem = mEventList.get(i);
        final long a = eventItem.getId();
        //设置单元格格式
        switch (eventItem.getEventlistcolor()) {
            case 0:
                viewHolder.eventListName.setTextColor(mContext.getResources().getColor(R.color.darkorchid));
                break;
            case 1:
                viewHolder.eventListName.setTextColor(mContext.getResources().getColor(R.color.seagreen));
                break;
            case 2:
                viewHolder.eventListName.setTextColor(mContext.getResources().getColor(R.color.royalblue));
                break;
            case 3:
                viewHolder.eventListName.setTextColor(mContext.getResources().getColor(R.color.gold));
                break;
            case 4:
                viewHolder.eventListName.setTextColor(mContext.getResources().getColor(R.color.saddlebrown));
                break;
            case 5:
                viewHolder.eventListName.setTextColor(mContext.getResources().getColor(R.color.red));
                break;
            case 6:
                viewHolder.eventListName.setTextColor(mContext.getResources().getColor(R.color.darkorange));
                break;
        }

        viewHolder.eventListName.setText(eventItem.getEventlistname());
        viewHolder.eventListDate.setText(eventItem.getEventlistdate());
        viewHolder.eventListLeftDay.setText(eventItem.getLeftday() + "");
        viewHolder.eventListNote.setText(eventItem.getEventlistnote());

        //viewHolder.itemView.setTag(i);
        viewHolder.eventView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //显示点击了什么
                PopupMenu popupMenu=new PopupMenu(mContext,v);
                popupMenu.getMenuInflater().inflate(R.menu.event_item_menu,popupMenu.getMenu());
                popupMenu.show();
                //popMenu的点击事件
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.editevent:
                                Intent intent=new Intent(mContext,EditEventActivity.class);
                                intent.putExtra("data",eventItem.getId());
                                ((Activity)mContext).startActivity(intent);
                                break;
                            case R.id.deleteevent:
                                //getState((Integer) v.getTag());
                                LitePal.delete(Event.class,a);
                                mEventList.remove(eventItem);
                                notifyDataSetChanged();
                                break;
                        }
                        return true;

                    }
                });

            }//设置点击事件
        });
    }

    @Override
    public int getItemCount() {
        return mEventList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        View eventView;
        TextView eventListName;
        TextView eventListDate;
        TextView eventListLeftDay;
        TextView eventListNote;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventView = itemView;
            eventListName = itemView.findViewById(R.id.eventitemname);
            eventListDate = itemView.findViewById(R.id.eventitemdate);
            eventListLeftDay = itemView.findViewById(R.id.eventitemleftday);
            eventListNote = itemView.findViewById(R.id.eventitemnote);
        }
    }
}
