package com.example.zyf.timetable;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import static com.example.zyf.timetable.DateHelper.setSelectedWeek;

public class PickerAdapterWrapper extends RecyclerView.Adapter<PickerAdapter.ViewHolder> {
    enum ITEM_TYPE{
        HEADER,
        FOOTER,
        NORMAL
    }

    private PickerAdapter mAdapter;
    private View mHeaderView;
    private View mFooterView;
    private HandleScroll handler;

    public PickerAdapterWrapper(PickerAdapter adapter, HandleScroll handleScroll){
        mAdapter = adapter;
        handler = handleScroll;
    }

    public int getItemViewType(int position) {
        if(position == 0){
            return ITEM_TYPE.HEADER.ordinal();
        } else if(position == mAdapter.getItemCount() + 1){
            return ITEM_TYPE.FOOTER.ordinal();
        } else{
            return ITEM_TYPE.NORMAL.ordinal();
        }
    }

    @Override
    public int getItemCount() {
        return mAdapter.getItemCount() + 2;
    }

    @NonNull
    @Override
    public PickerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == ITEM_TYPE.HEADER.ordinal()){
            return  new PickerAdapter.ViewHolder(mHeaderView);
        } else if(viewType == ITEM_TYPE.FOOTER.ordinal()){
            return  new PickerAdapter.ViewHolder(mFooterView);
        } else{
            return mAdapter.onCreateViewHolder(parent,viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final PickerAdapter.ViewHolder holder, int position) {
        if(position == 0){
            return;
        } else if(position == mAdapter.getItemCount() + 1){
            return;
        } else{
            mAdapter.onBindViewHolder(holder, position - 1);
            holder.weekNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int selected = holder.getAdapterPosition();
                    setSelectedWeek(selected);
                    //调用RecyclerView的activity处理滚动
                    handler.scrollToPosition(selected);
                    PickerHelper.changeWeekNumHighlight(holder.weekNum);
                }
            });
        }
    }



    public void addHeaderView(View view){
        this.mHeaderView = view;
    }
    public void addFooterView(View view){
        this.mFooterView = view;
    }
}

