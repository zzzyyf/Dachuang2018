package com.example.zyf.timetable;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zyf.timetable.db.Subject;

import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder> {
    private List<Subject> classList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView className;
        TextView classPlace;
        public ViewHolder(View v){
            super(v);
            className = v.findViewById(R.id.class_name);
            classPlace = v.findViewById(R.id.class_place);
        }
    }
    public SubjectAdapter(List<Subject> classList){
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
        viewHolder.className.setText(classList.get(i).getClass_name());
        viewHolder.classPlace.setText(classList.get(i).getClass_place());
    }

    @Override
    public int getItemCount() {
        return classList.size();
    }

    public void setClassList(List<Subject> classList) {
        this.classList = classList;
    }
}
