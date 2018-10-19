package com.example.zyf.timetable;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

public class SingleClassAdapter extends RecyclerView.Adapter<SingleClassAdapter.ViewHolder> {
    private List<SingleClassItem> classList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView className;
        TextView classPlace;
        public ViewHolder(View v){
            super(v);
            className = v.findViewById(R.id.class_name);
            classPlace = v.findViewById(R.id.class_place);
        }
    }
    public SingleClassAdapter(List<SingleClassItem> classList){
        this.classList = classList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.class_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.className.setText(classList.get(i).getClassName());
        viewHolder.classPlace.setText(classList.get(i).getClassPlace());
    }

    @Override
    public int getItemCount() {
        return classList.size();
    }
}
