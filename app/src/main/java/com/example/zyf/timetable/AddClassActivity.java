package com.example.zyf.timetable;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;


public class AddClassActivity extends AppCompatActivity {

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

        Button button = findViewById(R.id.button_test);
        //TODO: 加入滑动选择上课周的功能
        /*只需判断点击（抬起）和滑出
        在父View挂`onTouchListener()`，
            if 监听到`ACTION_MOVE`
                设置flag1（拖动进行中）
                判断在哪一个数字内
                    记下该数字对应的位置 currentPosition
                    若lastPosition==0则初始化lastPosition为currentPosition
                    若currentPosition为数字外则（说明已经出了刚刚的数字）
                        if Lit[]集合内有该位置（已被点亮过）
                            暗该子项
                            把该子项位置移出Lighted[]集合
                        else
                            亮该子项
                            把该子项位置存入Lighted[]集合
            else if 监听到ACTION_UP
                判断在哪一个数字内
                if Lit[]集合内有该位置（已被点亮过）
                    暗该子项
                    把该子项位置移出Lighted[]集合
                else
                    亮该子项
                    把该子项位置存入Lighted[]集合
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_class_menu, menu);
        return true;
    }
}
