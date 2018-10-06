package com.example.zyf.timetable;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    RecyclerView tableView;
    List<SingleClass> classList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //读取或初始化classList
        tableView = findViewById(R.id.table_view);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * 添加一节课
     * @param cls 要添加的课
     */
    public void addClass(SingleClass cls){
        classList.add(cls);
    }

    /**
     * 通过获取某节课在集合中的位置（点击RecyclerView或在列表中删除）删除某节课
     * @param pos 获取到的位置
     */
    public void deleteClass(int pos)throws Exception{
        try{
            classList.remove(pos);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
