package com.example.zyf.timetable;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;
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
        planfre=findViewById(R.id.planfre);

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
                //Toast.makeText(AddEventsActivity.this,((TextView) view).getText().toString(),Toast.LENGTH_SHORT).show();

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

        planfre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //TextView textView=(TextView)view;
                //Toast.makeText(AddEventsActivity.this,((TextView) view).getText().toString(),Toast.LENGTH_SHORT).show();

                switch (view.getId()){
                    case 0:
                        frenum=0;
                        break;
                    case 1:
                        frenum=1;
                        break;
                    case 2:
                        frenum=2;
                        break;
                    case 3:
                        frenum=3;
                        break;
                    case 4:
                        frenum=4;
                        break;
                    case 5:
                        frenum=5;
                        break;
                    case 6:
                        frenum=6;
                        break;

                }//颜色判断
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}
