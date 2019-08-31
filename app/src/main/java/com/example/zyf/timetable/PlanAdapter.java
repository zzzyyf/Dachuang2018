package com.example.zyf.timetable;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.zyf.timetable.db.Plan;

import org.litepal.LitePal;

import java.util.List;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.ViewHolder>{
    private List<PlanItem> mPlanItem;

    private Resources mResources;
    private Context mContext;

    static  class ViewHolder extends RecyclerView.ViewHolder{
        View planView;
        CheckBox plancheck;
        TextView planitemname;
        TextView planitemdate;
        TextView planitemtime;
        TextView planitemfre;
        TextView planitemnote;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            planView=itemView;
            plancheck=itemView.findViewById(R.id.plancheck);
            planitemname=itemView.findViewById(R.id.planitemname);
            planitemdate=itemView.findViewById(R.id.planitemdate);
            planitemtime=itemView.findViewById(R.id.planitemtime);
            planitemfre=itemView.findViewById(R.id.planitemfre);
            planitemnote=itemView.findViewById(R.id.planitemnote);
        }
    }

    public PlanAdapter(List<PlanItem> planitem, Context context) {
        mPlanItem = planitem;
        //this.mResources = mResources;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view= LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.plan_item,viewGroup,false);
        final ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final PlanItem planitem=mPlanItem.get(i);
        final long a=planitem.getId();

        switch (planitem.getPlanitemcolor()){
            case 0:
                viewHolder.planitemname.setTextColor(mContext.getResources().getColor(R.color.darkorchid));
                break;
            case 1:
                viewHolder.planitemname.setTextColor(mContext.getResources().getColor(R.color.seagreen));
                break;
            case 2:
                viewHolder.planitemname.setTextColor(mContext.getResources().getColor(R.color.royalblue));
                break;
            case 3:
                viewHolder.planitemname.setTextColor(mContext.getResources().getColor(R.color.gold));
                break;
            case 4:
                viewHolder.planitemname.setTextColor(mContext.getResources().getColor(R.color.saddlebrown));
                break;
            case 5:
                viewHolder.planitemname.setTextColor(mContext.getResources().getColor(R.color.red));
                break;
            case 6:
                viewHolder.planitemname.setTextColor(mContext.getResources().getColor(R.color.darkorange));
                break;
        }

        switch (planitem.getPlanitemfre()){
            case 0:
                viewHolder.planitemfre.setText("关闭");
                break;
            case 1:
                viewHolder.planitemfre.setText("每天");
                break;
            case 2:
                viewHolder.planitemfre.setText("每周");
                break;
            case 3:
                viewHolder.planitemfre.setText("每两周");
                break;
            case 4:
                viewHolder.planitemfre.setText("每个月");
                break;
            case 5:
                viewHolder.planitemfre.setText("每3个月");
                break;
            case 6:
                viewHolder.planitemfre.setText("每年");
                break;
        }
        viewHolder.planitemname.setText(planitem.getPlanitemname());
        viewHolder.planitemdate.setText(planitem.getPlanitembegindate()+"-"+planitem.getPlanitemenddate());
        viewHolder.planitemtime.setText(planitem.getPlanitembegintime()+"-"+planitem.getPlanitemendtime());
        viewHolder.planitemnote.setText(planitem.getPlanitemnote());

        //checkbox 未写

        //点击修改
        viewHolder.planView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu=new PopupMenu(mContext,v);
                popupMenu.getMenuInflater().inflate(R.menu.planselect,popupMenu.getMenu());
                popupMenu.show();
                //popMenu的点击事件
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){
                            case R.id.editplan:
                                //List <String>
                                //Toast.makeText(mContext,"1",Toast.LENGTH_SHORT).show();

                                Intent intent=new Intent(mContext, EditPlanActivity.class);
                                //String data="emmm";
                                //传参
                                //Bundle bundle=new bundle();

                                intent.putExtra("data",planitem.getId());
                                ((Activity)mContext).startActivity(intent);
                                break;
                            case R.id.deleteplan:
                                //getState((Integer) v.getTag());
                                LitePal.delete(Plan.class,a);
                                mPlanItem.remove(planitem);
                                notifyDataSetChanged();
                                break;
                        }
                        return true;

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPlanItem.size();
    }
}
