package com.example.zyf.timetable;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;


public class AddClassActivity extends AppCompatActivity {
    EditText weekText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏，AppCompatActivity专用
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_class);

        Toolbar toolbar = findViewById(R.id.add_class_dialog_toolbar);
        toolbar.setNavigationIcon(R.drawable.close);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        weekText = findViewById(R.id.week_text);
        weekText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(AddClassActivity.this, WeekSelector.class), 1);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_class_menu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1){
            switch (resultCode){
                case RESULT_OK:
                    ArrayList<Integer> litList = data.getIntegerArrayListExtra("Lit");
                    StringBuilder builder = new StringBuilder();
                    boolean isContinued = false;
                    builder.append("已选择：");
                    for (int i=0;i<DateHelper.endWeek;i++) {
                        //第i项已选中
                        if (litList.get(i) != 0) {
                            if(!isContinued) {
                                //从不连续选中到连续选中，一个新周段的开始
                                builder.append(i+1);
                                isContinued = true;
                            }else{
                                //处在连续过程中
                                //若一直连续选到最后一周
                                if (i==DateHelper.endWeek-1){
                                    builder.append("-");
                                    builder.append(i+1);
                                    builder.append("周");
                                }
                            }
                            //第i项未被选中
                        } else {
                            if(isContinued){
                                //从连续选中到不连续选中
                                isContinued=false;
                                //若不是只取了一周
                                if (!(i==1||litList.get(i-2)==0)){
                                    builder.append("-");
                                    builder.append(i);
                                }
                                builder.append("周");
                                if (litList.indexOf(litList.get(i))<litList.size())
                                    builder.append("，");
                            }
                        }
                    }
                    weekText.setText(builder.toString());
            }
        }
    }
}
