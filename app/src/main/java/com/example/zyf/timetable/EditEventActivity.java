package com.example.zyf.timetable;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import org.litepal.LitePal;

import java.util.Calendar;

public class EditEventActivity extends AppCompatActivity {

    private TextView editeventname;

    ArrayAdapter <String> adapter;
    private TextView editeventdate;
    private TextView editeventnote;

    private Spinner editeventcolor;


    private int year;
    private int month;
    private int day;
    private int colornum;
    private int newyear;
    private int newmonth;
    private int newday;

    private int edyear;
    private int edmonth;
    private int edday;

    public long id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editevent);

        //Toolbar设置
        Toolbar toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("事件");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editeventname=findViewById(R.id.editeventname);
        editeventdate=findViewById(R.id.editeventdate);
        editeventnote=findViewById(R.id.editeventnote);

        editeventcolor=findViewById(R.id.editeventcolor);


        SpinnerAdapter adapter=null;
        adapter= ArrayAdapter.createFromResource(this,R.array.eventcolors,android.R.layout.simple_spinner_dropdown_item);
        editeventcolor.setAdapter(adapter);

        //传递id
        Intent intent=getIntent();
        id=intent.getLongExtra("data",0);

        //List<Event> events= LitePal.select("id").find(Event.class);
        Event oneevent=LitePal.find(Event.class,id);
        //显示要修改的数据
        editeventname.setText(oneevent.getEventName());
        //editeventdate.setText(oneevent.getEventyear()+"年"+oneevent.getEventmonth()+"月"+oneevent.getEventday()+"日");

        //String s=String.valueOf(id);

        //Toast.makeText(EditEventActivity.this,s,Toast.LENGTH_SHORT).show();
        //颜色选择
        editeventcolor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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



        //editeventdate=findViewById(R.id.editeventdate);
        final Calendar calendar=Calendar.getInstance();
        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH)+1;
        day=calendar.get(Calendar.DAY_OF_MONTH);
//        eventdate.setFocusable(false);
        edyear=oneevent.getEventYear();
        edmonth=oneevent.getEventMonth();
        edday=oneevent.getEventDay();
        editeventdate.setText(String.format("%d年%d月%d日",edyear,edmonth,edday));

        editeventdate.setInputType(InputType.TYPE_NULL);
        editeventdate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    showDatePickerDialog();
                }
            }
        });

        editeventdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        final Event event=new Event();
        Button button=findViewById(R.id.editeventsure);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editeventname.getText().toString().equals("") ){
                    Toast.makeText(getApplicationContext(),"未输入事件名称",Toast.LENGTH_SHORT).show();
                }
                else {
                    event.setEventName(editeventname.getText().toString());

                    event.setEventYear(newyear);
                    event.setEventMonth(newmonth);
                    event.setEventDay(newday);

                    event.setEventColor(colornum);//存储颜色

                    event.setEventNote(editeventnote.getText().toString());

                    event.update(id);

                    Intent intent=new Intent(EditEventActivity.this,MainActivity.class);
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
                editeventdate.setText(year+"年"+(monthOfYear+1)+"月"+dayOfMonth+"日");
                newyear=year;
                newmonth=monthOfYear+1;
                newday=dayOfMonth;
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

    }
}
