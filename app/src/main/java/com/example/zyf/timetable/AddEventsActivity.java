package com.example.zyf.timetable;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.example.zyf.timetable.db.Event;

import java.util.Calendar;

public class AddEventsActivity extends AppCompatActivity {

    private TextView eventname;

    ArrayAdapter <String> adapter;
    private TextView eventdate;
    private TextView eventnote;

    private Spinner eventcolor;


    private int year;
    private int month;
    private int day;
    private int colornum;
    private int newyear;
    private int newmonth;
    private int newday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addevents);

        //Toolbar设置
        Toolbar toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("事件");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        eventname=findViewById(R.id.event_name);
        eventdate=findViewById(R.id.event_date);
        eventnote=findViewById(R.id.event_note);

        eventcolor=findViewById(R.id.event_color);


        SpinnerAdapter adapter=null;
        adapter=ArrayAdapter.createFromResource(this,R.array.eventcolors,android.R.layout.simple_spinner_dropdown_item);
        eventcolor.setAdapter(adapter);

        //颜色选择
        eventcolor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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



        eventdate=findViewById(R.id.event_date);
        final Calendar calendar=Calendar.getInstance();
        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH)+1;
        day=calendar.get(Calendar.DAY_OF_MONTH);
//        eventdate.setFocusable(false);
        eventdate.setText(String.format("%d年%d月%d日",year,month,day));

        eventdate.setInputType(InputType.TYPE_NULL);
        eventdate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    showDatePickerDialog();
                }
            }
        });

        eventdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        final Event event=new Event();
        Button button=findViewById(R.id.eventsure);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eventname.getText().toString().equals("") ){
                    Toast.makeText(getApplicationContext(),"未输入事件名称",Toast.LENGTH_SHORT).show();
                }
                else {
                    event.setEventName(eventname.getText().toString());

                    event.setEventYear(newyear);
                    event.setEventMonth(newmonth);
                    event.setEventDay(newday);

                    event.setEventColor(colornum);//存储颜色

                    event.setEventNote(eventnote.getText().toString());

                    event.save();

                    Intent intent=new Intent(AddEventsActivity.this,MainActivity.class);
                    //getSupportFragmentManager().beginTransaction().replace(R.id.fragment,efFragment).commitAllowingStateLoss();
                    startActivity(intent);
                }



            }
        });


    }
    private void showDatePickerDialog() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                eventdate.setText(year+"年"+(monthOfYear+1)+"月"+dayOfMonth+"日");
                newyear=year;
                newmonth=monthOfYear+1;
                newday=dayOfMonth;
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

    }



//    public void setDate(int year,int month,int day){
//        this.year=year;
//        this.month=month+1;
//        this.day=day;
//        eventdate.setText(String.format("%d年%d月%d日",year,month,day));
//    }

//    @Override
//    public void sendDate(int year, int month, int day) {
//        setDate(year,month,day);
//    }
}
