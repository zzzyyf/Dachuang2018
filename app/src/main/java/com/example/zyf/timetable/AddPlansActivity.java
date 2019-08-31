package com.example.zyf.timetable;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.zyf.timetable.db.Plan;

import java.util.Calendar;

public class AddPlansActivity extends AppCompatActivity {

    private TextView planname;
    private TextView planbegindate;
    private TextView planbegintime;
    private TextView planenddate;
    private TextView planendtime;

    private Spinner plancolor;
    private Spinner planfre;

    private TextView plannote;

    private int colornum;
    private int frenum;//频率

    //现在时间
    private int nowyear;
    private int nowmonth;
    private int nowday;
    private int nowhour;
    private int nowmin;
    //计划开始时间
    private int beginyear;
    private int beginmonth;
    private int beginday;
    //计划结束时间
    private int endyear;
    private int endmonth;
    private int endday;
    //
    private int newyear;
    private int newmonth;
    private int newday;
    //计划开始时间
    private int beginhour;
    private int beginminute;
    //计划结束时间
    private int endhour;
    private int endminute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addplans);

        //Toolbar
        Toolbar toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("添加计划");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        planname=findViewById(R.id.planname);
        planbegindate=findViewById(R.id.planbegindate);
        planbegintime=findViewById(R.id.planbegintime);
        planenddate=findViewById(R.id.planenddate);
        planendtime=findViewById(R.id.planendtime);
        plannote=findViewById(R.id.plannote);
        plancolor=findViewById(R.id.plancolor);
        planfre=findViewById(R.id.planfreq);

        //spinner设置
        //颜色
        SpinnerAdapter coloradapter=null;
        coloradapter= ArrayAdapter.createFromResource(this,R.array.eventcolors,android.R.layout.simple_spinner_dropdown_item);
        plancolor.setAdapter(coloradapter);
        //频率
        SpinnerAdapter freadapter=null;
        freadapter=ArrayAdapter.createFromResource(this,R.array.planfre,android.R.layout.simple_spinner_dropdown_item);
        planfre.setAdapter(freadapter);

        //颜色选择
        plancolor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                TextView textView=(TextView)view;
                //Toast.makeText(AddeventsActivity.this,((TextView) view).getText().toString(),Toast.LENGTH_SHORT).show();

                switch (textView.getText().toString()){
                    case "紫色":
                        colornum=0;
                        break;
                    case "绿色":
                        colornum=1;
                        break;
                    case "蓝色":
                        colornum=2;
                        break;
                    case "黄色":
                        colornum=3;
                        break;
                    case "褐色":
                        colornum=4;
                        break;
                    case "红色":
                        colornum=5;
                        break;
                    case "橙色":
                        colornum=6;
                        break;

                }//颜色判断
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //频率选择
        planfre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                TextView textView=(TextView)view;
                //Toast.makeText(AddeventsActivity.this,((TextView) view).getText().toString(),Toast.LENGTH_SHORT).show();

                switch (textView.getText().toString()){
                    case "关闭":
                        frenum=0;
                        break;
                    case "每天":
                        //Toast.makeText(AddplansActivity.this,"1",Toast.LENGTH_SHORT).show();
                        frenum=1;
                        break;
                    case "每周":
                        frenum=2;
                        break;
                    case "每两周":
                        frenum=3;
                        break;
                    case "每个月":
                        frenum=4;
                        break;
                    case "每3个月":
                        frenum=5;
                        break;
                    case "每年":
                        frenum=6;
                        break;

                }//频率判断
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final Calendar calendar=Calendar.getInstance();
        //获取现在时间
        nowyear=calendar.get(Calendar.YEAR);
        nowmonth=calendar.get(Calendar.MONTH)+1;
        nowday=calendar.get(Calendar.DAY_OF_MONTH);
        nowhour=calendar.get(Calendar.HOUR_OF_DAY);
        nowmin=calendar.get(Calendar.MINUTE);

        planbegindate.setText(String.format("%d年%d月%d日",nowyear,nowmonth,nowday));
        planbegindate.setInputType(InputType.TYPE_NULL);
        planbegindate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    showbeginDatePickerDialog();
                }
            }
        });
        planbegindate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showbeginDatePickerDialog();

                //beginyear=newyear;
                //beginmonth=newmonth;
                //beginday=newday;
            }
        });

        planenddate.setText(String.format("%d年%d月%d日",nowyear,nowmonth,nowday));
        planenddate.setInputType(InputType.TYPE_NULL);
        planenddate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    showendDatePickerDialog();
                }
            }
        });
        planenddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showendDatePickerDialog();

                //endyear=newyear;
                //endmonth=newmonth;
                //endday=newday;
            }
        });


        planbegintime.setText(String.format("%02d:%02d",nowhour,nowmin));
        planbegintime.setInputType(InputType.TYPE_NULL);
        planbegintime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    showbeginTimePickerDialog();
                }
            }
        });
        planbegintime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showbeginTimePickerDialog();
            }
        });

        planendtime.setText(String.format("%02d:%02d",nowhour,nowmin));
        planendtime.setInputType(InputType.TYPE_NULL);
        planendtime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    showendTimePickerDialog();
                }
            }
        });
        planendtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showendTimePickerDialog();

            }
        });

        final Plan plan=new Plan();
        Button button=findViewById(R.id.plansure);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (planname.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"未输入计划名称",Toast.LENGTH_SHORT).show();
                }
                else {
                    plan.setPlanname(planname.getText().toString());

                    plan.setPlanbeginyear(beginyear);
                    plan.setPlanbeginmonth(beginmonth);
                    plan.setPlanbeginday(beginday);

                    plan.setPlanbeginhour(beginhour);
                    plan.setPlanbeginminute(beginminute);

                    plan.setPlanendyear(endyear);
                    plan.setPlanendmonth(endmonth);
                    plan.setPlanendday(endday);

                    plan.setPlanendhour(endhour);
                    plan.setPlanendminute(endminute);

                    plan.setPlannote(plannote.getText().toString());

                    plan.setPlancolor(colornum);
                    plan.setPlanfrequ(frenum);

                    plan.save();

                    Intent intent=new Intent(AddPlansActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        });


    }

    private void showbeginDatePickerDialog(){
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub

                beginyear=year;
                beginmonth=monthOfYear+1;
                beginday=dayOfMonth;
                planbegindate.setText(beginyear+"年"+beginmonth+"月"+beginday+"日");
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }
    private void showendDatePickerDialog(){
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub

                endyear=year;
                endmonth=monthOfYear+1;
                endday=dayOfMonth;
                planenddate.setText(endyear+"年"+endmonth+"月"+endday+"日");
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showbeginTimePickerDialog(){
        Calendar c = Calendar.getInstance();
        new TimePickerDialog(this,new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                beginhour=hourOfDay;
                beginminute=minute;
                planbegintime.setText(String.format("%02d:%02d",beginhour,beginminute));
            }
        },c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),true).show();
    }

    private void showendTimePickerDialog(){
        Calendar c = Calendar.getInstance();
        new TimePickerDialog(this,new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                endhour=hourOfDay;
                endminute=minute;
                planendtime.setText(String.format("%02d:%02d",endhour,endminute));
            }
        },c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),true).show();
    }
}
