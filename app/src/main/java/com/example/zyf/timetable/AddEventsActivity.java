package com.example.zyf.timetable;

import android.app.DatePickerDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.example.zyf.timetable.db.Event;

import org.litepal.exceptions.LitePalSupportException;

import java.util.Calendar;

public class AddEventsActivity extends AppCompatActivity {

    private TextView eventNameText;

    ArrayAdapter <String> adapter;
    private TextView eventDateText;
    private TextView eventNoteText;
    private Spinner eventColorText;

    private int year;
    private int month;
    private int day;
    private int colornum;
    private int newyear;
    private int newmonth;
    private int newday;

    Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏，AppCompatActivity专用
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_events);
        //使全屏之一
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        //Toolbar设置
        Toolbar toolbar=findViewById(R.id.add_event_dialog_toolbar);
        toolbar.setNavigationIcon(R.drawable.close);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //TODO: 设置TextInputLayout
        eventNameText =findViewById(R.id.event_name);
        eventDateText =findViewById(R.id.event_date);
        eventNoteText =findViewById(R.id.event_note);
        eventColorText =findViewById(R.id.event_color);

        SpinnerAdapter adapter=ArrayAdapter.createFromResource(this,R.array.eventcolors,android.R.layout.simple_spinner_dropdown_item);
        eventColorText.setAdapter(adapter);

        //颜色选择
        eventColorText.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        eventDateText =findViewById(R.id.event_date);
        final Calendar calendar=Calendar.getInstance();
        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH)+1;
        day=calendar.get(Calendar.DAY_OF_MONTH);
        eventDateText.setText(String.format("%d年%d月%d日",year,month,day));

        eventDateText.setInputType(InputType.TYPE_NULL);
        eventDateText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    showDatePickerDialog();
                }
            }
        });

        eventDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        event=new Event();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_dialog_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_btn:
                if (eventNameText.getText().toString().equals("") ){
                    Toast.makeText(getApplicationContext(),"未输入事件名称",Toast.LENGTH_SHORT).show();
                }
                else {
                    event.setEventName(eventNameText.getText().toString());
                    event.setEventYear(newyear);
                    event.setEventMonth(newmonth);
                    event.setEventDay(newday);
                    event.setEventColor(colornum);//存储颜色
                    event.setEventNote(eventNoteText.getText().toString());
                    try {
                        event.save();
                        setResult(RESULT_OK);
                        finish();
                    }catch (LitePalSupportException e){
                        Toast.makeText(AddEventsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                break;
                default:
        }
        return true;
    }

    private void showDatePickerDialog() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                eventDateText.setText(year+"年"+(monthOfYear+1)+"月"+dayOfMonth+"日");
                newyear=year;
                newmonth=monthOfYear+1;
                newday=dayOfMonth;
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

    }
}
