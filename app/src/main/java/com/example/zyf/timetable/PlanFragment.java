package com.example.zyf.timetable;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zyf.timetable.db.Plan;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class PlanFragment extends Fragment {
    private RecyclerView mRecyclerView;

    private RecyclerView.Adapter mAdapter;

    private List<PlanItem> planitem=new ArrayList<>();

    private RecyclerView.LayoutManager mLayoutManager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.plan_fragment,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LitePal.getDatabase();
        planitem.clear();
        initData();

        RecyclerView recyclerView=view.findViewById(R.id.planview);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        PlanAdapter adapter=new PlanAdapter(planitem,getContext());
        recyclerView.setAdapter(adapter);

    }
    private void initData(){
        List<Plan> plans= LitePal.findAll(Plan.class);

        for (Plan plan:plans){
            String planbegindate=plan.getPlanbeginyear()+"年"+plan.getPlanbeginmonth()+"月"+plan.getPlanbeginday()+"日";
            String planbegintime=String.format("%02d:%02d",plan.getPlanbeginhour(),plan.getPlanbeginminute());
            String planenddate=plan.getPlanendyear()+"年"+plan.getPlanendmonth()+"月"+plan.getPlanendday()+"日";
            String planendtime=String.format("%02d:%02d",plan.getPlanendhour(),plan.getPlanendminute());

            PlanItem list=new PlanItem(plan.getPlanname(),planbegindate,planbegintime,planenddate,planendtime,plan.getPlancolor(),plan.getPlanfrequ(),plan.getId(),plan.getPlannote());
            planitem.add(list);
        }
    }
}
