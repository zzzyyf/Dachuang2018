package com.example.zyf.timetable;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import com.example.zyf.timetable.db.WeekSettings;
import org.litepal.exceptions.LitePalSupportException;
import java.util.Calendar;
import static com.example.zyf.timetable.DateHelper.*;

public class SetSemesterActivity extends AppCompatActivity implements DatePickerFragment.SendDate {
    TextInputLayout startDateText, endDateText, classPDayText;
    Calendar startDate, endDate;
    DatePickerFragment startFragment = new DatePickerFragment(), endFragment = new DatePickerFragment();
    boolean isFilled, isStartFilled=false, isEndFilled=false;
    Button saveBtn;
    ImageButton closeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_set_semester);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        startDateText = findViewById(R.id.start_date);
        endDateText = findViewById(R.id.end_date);
        classPDayText = findViewById(R.id.classes_per_day);
        saveBtn = findViewById(R.id.save_btn);
        closeBtn = findViewById(R.id.close_btn);
        startDateText.getEditText().setText(String.format("%d-%d-%d", startYear, startMonth+1, startDay));
        startDateText.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFragment.isStart=true;
                Calendar date = Calendar.getInstance();
                if (DateHelper.isWeekSet)
                    date.set(startYear, startMonth, startDay);
                startFragment.c= date;
                startFragment.show(SetSemesterActivity.this.getSupportFragmentManager(), "datePicker");
            }
        });
        endDateText.getEditText().setText(String.format("%d-%d-%d", endYear, endMonth+1, endDay));
        endDateText.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endFragment.isStart=false;Calendar date = Calendar.getInstance();
                if (DateHelper.isWeekSet)
                    date.set(endYear, endMonth, endDay);
                endFragment.c= date;
                endFragment.show(SetSemesterActivity.this.getSupportFragmentManager(), "datePicker");
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((!isWeekSet&&!isStartFilled)||startDateText.getEditText().getText().toString().equals("")){
                    showError(startDateText, "请设置学期开始日期");
                    return;
                }else startDateText.setErrorEnabled(false);
                if((!isWeekSet&&!isEndFilled)||endDateText.getEditText().getText().toString().equals("")){
                    showError(endDateText, "请设置学期结束日期");
                    return;
                } else endDateText.setErrorEnabled(false);
                if(classPDayText.getEditText().getText().toString().equals("")){
                    showError(classPDayText, "请设置每日上课节数");
                    return;
                }else classPDayText.setErrorEnabled(false);
                setWeek(startDate, endDate, Integer.parseInt(classPDayText.getEditText().getText().toString()));
                try {
                    saveToDb(new WeekSettings());
                    setResult(RESULT_OK);
                    finish();
                }catch (LitePalSupportException e){
                    Toast.makeText(SetSemesterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    @Override
    public void sendDate(int year, int month, int day, boolean isStart) {
        if (isStart) {
            startDate = Calendar.getInstance();
            startDate.set(year, month, day);
            startDateText.getEditText().setText(String.format("%d-%d-%d", year, month+1, day));
            isStartFilled=true;

        }else{
            endDate = Calendar.getInstance();
            endDate.set(year, month, day);
            endDateText.getEditText().setText(String.format("%d-%d-%d", year, month+1, day));
            isEndFilled=true;
        }
    }

    private void showError(TextInputLayout textInputLayout, String error){
        textInputLayout.setError(error);//设置错误信息，注意此处只要error为非空则setErrorEnable()会自动设为true
        isFilled =false;
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        super.onAttachedToWindow();
        //即设定DecorView在PhoneWindow里的位置
        View view = getWindow().getDecorView();
        WindowManager.LayoutParams lp = (WindowManager.LayoutParams) view.getLayoutParams();
        lp.gravity = Gravity.LEFT | Gravity.TOP;
        getWindowManager().updateViewLayout(view, lp);

    }
}
