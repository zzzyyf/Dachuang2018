package com.example.zyf.timetable;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.example.zyf.timetable.db.Subject;

import org.litepal.LitePal;
import org.litepal.exceptions.LitePalSupportException;

import java.util.ArrayList;
import java.util.List;

public class AddClassActivity extends AppCompatActivity {
    EditText weekText;//显示已选择的所有周
    AutoCompleteTextView nameText;//课名
    MultiAutoCompleteTextView placeText;//上课地点
    //TODO: 实现自动填充课名、地点
    ArrayList<Integer> litWeeksList, periods;//已选择周数的列表
    RecyclerView weekdayPicker, sessionPicker;
    List<WeekItem> weekdays, sessions;//已选择的上课日、上课节的列表
    SwipeHelper weekdayHelper, sessionHelper;
    Button deleteBtn;
    Subject item;
    Intent intent;

    final int PERIOD_EMPTY=1, PERIOD_DISCONTINUED=2, PERIOD_LAGGED=3, PERIOD_OK=4,
    EDIT_SUBJECT=5, ADD_SUBJECT=10;//period为空，period不连续,当前课程与已有某节课重复,课程正常

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏，AppCompatActivity专用
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_class);
        //使全屏之一
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        //TODO: Toolbar阴影
        Toolbar toolbar = findViewById(R.id.add_class_dialog_toolbar);
        toolbar.setNavigationIcon(R.drawable.close);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nameText = findViewById(R.id.class_name_text);
        placeText = findViewById(R.id.class_place_text);

        initLit();
        weekText = findViewById(R.id.week_text);
        weekText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(AddClassActivity.this, WeekSelector.class).putIntegerArrayListExtra("Lit", litWeeksList), 1);
            }
        });

        weekdayPicker = findViewById(R.id.weekday_picker);
        sessionPicker = findViewById(R.id.session_picker);
        initWeekdays();
        initSessions();
        final LinearLayoutManager weekdaysManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        GridLayoutManager sessionsManager = new GridLayoutManager(this, 4, RecyclerView.VERTICAL, false);
        weekdayPicker.setLayoutManager(weekdaysManager);
        //TODO: 按照早、午、晚的课节排列
        sessionPicker.setLayoutManager(sessionsManager);
        WeekSelectorAdapter weekdaysAdapter = new WeekSelectorAdapter(weekdays, 15);
        WeekSelectorAdapter sessionsAdapter = new WeekSelectorAdapter(sessions, 15);
        weekdayPicker.setAdapter(weekdaysAdapter);
        sessionPicker.setAdapter(sessionsAdapter);

        weekdayHelper = new SwipeHelper(weekdaysManager, AddClassActivity.this, false);
        weekdayHelper.setLayout(DateHelper.daysPerWeek, 28, 24);
        weekdayHelper.setStatusList(createWeekdayHelperList());
        weekdayPicker.setOnTouchListener(weekdayHelper);

        sessionHelper = new SwipeHelper(sessionsManager, AddClassActivity.this, true);
        sessionHelper.setLayout(4, 60, 24);
        sessionHelper.setStatusList(createSessionHelperList());
        sessionPicker.setOnTouchListener(sessionHelper);
        deleteBtn=findViewById(R.id.delete_class);
        deleteBtn.setVisibility(View.GONE);

        //接收编辑/从空课新建课程的有关数据
        intent = getIntent();
        if(intent != null){
            item = (Subject) intent.getSerializableExtra("ClassItem");
            if(intent.getIntExtra("OperationType", 10)==5) {//10==ADD_SUBJECT, 5==EDIT_SUBJECT
                nameText.setText(item.getClass_name());
                placeText.setText(item.getClass_place());
                //设置周数的初始值
                for (int i = 0; i < item.getWeeks().size(); i++) {
                    litWeeksList.set(item.getWeeks().get(i) - 1, 1);
                }
                setWeekText();
            }
            if(item!=null) {
                //此时不是从头开始新建课程
                //设置weekday的初始值
                weekdayHelper.setLit(item.getWeekday() - 1, true);
                weekdayHelper.selected =item.getWeekday()-1;
                //设置period的初始值
                for (int i = item.getStartPeriod(); i <= item.getEndPeriod(); i++) {
                    sessionHelper.setLit(i - 1, true);
                }
                if (!item.getClass_name().equals("空课")&&item.getWeeks().size()!=0) {
                    //此时既不是从头新建也不是填充的空课，可以删除
                    deleteBtn.setVisibility(View.VISIBLE);
                    deleteBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddClassActivity.this, R.style.Theme_AppCompat_Light_Dialog_Alert);
                            builder.setCancelable(true)
                                    .setMessage("确认删除本节课吗？")
                                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            try {
                                                LitePal.delete(Subject.class, item.getId());
                                                Intent intentAfterDel = new Intent();
                                                intentAfterDel.putExtra("Weekday", item.getWeekday());
                                                setResult(3, intentAfterDel);
                                                finish();
                                            } catch (LitePalSupportException e) {
                                                Toast.makeText(AddClassActivity.this, "删除失败：" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }).setNegativeButton("取消", null)
                                    .show();
                        }
                    });
                }
            }
        }
        weekdayPicker.post(new Runnable() {
            @Override
            public void run() {
                weekdayHelper.initLit();
            }
        });
        sessionPicker.post(new Runnable() {
            @Override
            public void run() {
                sessionHelper.initLit();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_dialog_menu, menu);
        return true;
    }

    public void initLit() {
        litWeeksList = new ArrayList<>();
        for (int i = 0; i < DateHelper.endWeek; i++) {
            litWeeksList.add(0);
        }
    }

    private ArrayList<Boolean> createWeekdayHelperList() {
        ArrayList<Boolean> helperList = new ArrayList<>();
        for (int i = 0; i < DateHelper.daysPerWeek; i++) {
            if (litWeeksList.get(i) == 1)
                helperList.add(true);
            else helperList.add(false);
        }
        return helperList;
    }

    private ArrayList<Boolean> createSessionHelperList() {
        ArrayList<Boolean> helperList = new ArrayList<>();
        for (int i = 0; i < DateHelper.classesPerDay; i++) {
            if (litWeeksList.get(i) == 1)
                helperList.add(true);
            else helperList.add(false);
        }
        return helperList;
    }

    public void initSessions() {
        sessions = new ArrayList<>();
        for (int i = 0; i < DateHelper.classesPerDay; i++) {
            sessions.add(new WeekItem("第" + (i + 1) + "节", false));
        }
    }

    public void initWeekdays() {
        weekdays = new ArrayList<>();
        for (int i = 0; i < DateHelper.daysPerWeek; i++) {
            switch (i) {
                case 0:
                    weekdays.add(new WeekItem("一", false));
                    break;
                case 1:
                    weekdays.add(new WeekItem("二", false));
                    break;
                case 2:
                    weekdays.add(new WeekItem("三", false));
                    break;
                case 3:
                    weekdays.add(new WeekItem("四", false));
                    break;
                case 4:
                    weekdays.add(new WeekItem("五", false));
                    break;
                case 5:
                    weekdays.add(new WeekItem("六", false));
                    break;
                case 6:
                    weekdays.add(new WeekItem("日", false));
                    break;
                default:
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1) {
            switch (resultCode) {
                case RESULT_OK:
                    litWeeksList = data.getIntegerArrayListExtra("Lit");
                    setWeekText();
                    break;
                default:
            }
        }
    }

    /**
     *
     * @param list
     * @return
     */
    int checkWeekItems(List<WeekItem> list, int weekday) {
        periods = new ArrayList<>();
        for (WeekItem item : list
                ) {
            if (item.isLit()) {
                //说明节列表非空
                periods.add(list.indexOf(item)+1);//把选择的节数添加至列表
            }
        }
        //检查列表是否连续
        for (int i=0,j=periods.get(0); i<periods.size();i++,j++){
            if(j!=periods.get(i)) {
                Toast.makeText(AddClassActivity.this, "一次只能添加一节大课哦~",Toast.LENGTH_SHORT).show();
                return PERIOD_DISCONTINUED;
            }
        }
        //检查是否有重复
        List<Subject> subjectList =LitePal.where("weekday = ?", weekday+"").find(Subject.class);
        if(subjectList.size()!=0){
            for (Subject subject:subjectList){
                //若没有完全错开则必有重合
                if (intent.getIntExtra("OperationType", 10)!=5)
                    if (!(subject.getEndPeriod()<periods.get(0)||subject.getStartPeriod()>periods.get(periods.size()-1)) && checkWeekOverLap(subject, litWeeksList)){
                        Toast.makeText(AddClassActivity.this, "该节课程的时间与 "+subject.getClass_name()+" 冲突了哦，请检查~", Toast.LENGTH_SHORT).show();
                        return PERIOD_LAGGED;
                    }
            }
        }
        if(periods.size()==0) {
            Toast.makeText(AddClassActivity.this, "你还没有选择上课时间哦~", Toast.LENGTH_SHORT).show();
            return PERIOD_EMPTY;
        }
        return PERIOD_OK;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_btn:
                //先读取选择结果
                for (int i = 0; i < sessions.size(); i++) {
                    //先初始化
                    sessions.get(i).setLit(false);
                    if (sessionHelper.getLit().get(i).isLit())
                        sessions.get(i).setLit(sessionHelper.getLit().get(i).isLit());
                }

                //检查输入
                if (nameText.getText().toString().equals("")) {
                    Toast.makeText(AddClassActivity.this, "你还没有填写课名哦~", Toast.LENGTH_SHORT).show();
                    break;
                } else if (placeText.getText().toString().equals("")) {
                    Toast.makeText(AddClassActivity.this, "你还没有填写上课地点哦~", Toast.LENGTH_SHORT).show();
                    break;
                } else if (weekText.getText().toString().equals("")) {
                    Toast.makeText(AddClassActivity.this, "你还没有选择上课周哦~", Toast.LENGTH_SHORT).show();
                    break;
                } else if (weekdayHelper.selected == -1) {
                    Toast.makeText(AddClassActivity.this, "你还没有选择上课日哦~", Toast.LENGTH_SHORT).show();
                    break;
                    //weekday+1
                } else if (checkWeekItems(sessions, weekdayHelper.selected +1)!=PERIOD_OK) {
                    break;
                }

                //存储
                Subject subject = new Subject();
                String name = nameText.getText().toString();
                subject.setClass_name(name);
                subject.setClass_place(placeText.getText().toString());

                ArrayList<Integer> selectedWeeks = new ArrayList<>();
                for (int i = 0; i < litWeeksList.size(); i++)
                    if (litWeeksList.get(i) != 0) selectedWeeks.add(i + 1);
                subject.setWeeks(selectedWeeks);
                subject.setWeekday(weekdayHelper.selected +1);
                subject.setStartPeriod(periods.get(0));
                subject.setEndPeriod(periods.get(periods.size()-1));
                try {
                    subject.saveThrows();
                    Toast.makeText(AddClassActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                } catch (LitePalSupportException e) {
                    Toast.makeText(AddClassActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent();
                intent.putExtra("weekday", weekdayHelper.selected +1);
                setResult(RESULT_OK, intent);
                finish();
                break;
            //设置点击toolbar返回按钮的动作
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                break;
            default:
        }
        return true;
    }

    private void setWeekText(){
        StringBuilder builder = new StringBuilder();
        boolean isContinued = false;
        builder.append("已选择：");
        int j = DateHelper.endWeek - 1;//最后一个点亮的周数
        while (j >= 0 && litWeeksList.get(j) == 0)
            j--;
        for (int i = 0; i < DateHelper.endWeek; i++) {
            //第i项已被选中
            if (litWeeksList.get(i) != 0) {
                if (!isContinued) {
                    //从不连续选中到连续选中，一个新周段的开始
                    builder.append(i + 1);
                    isContinued = true;
                    //若最后一周为离散选择
                    if (i == DateHelper.endWeek - 1)
                        builder.append("周");
                } else {
                    //处在连续过程中
                    //若一直连续选到最后一周
                    if (i == DateHelper.endWeek - 1) {
                        builder.append("-");
                        builder.append(i + 1);
                        builder.append("周");
                    }
                }
                //第i项未被选中
            } else {
                if (isContinued) {
                    //从连续选中到不连续选中
                    isContinued = false;
                    //若不是只取了一周
                    if (!(i == 1 || litWeeksList.get(i - 2) == 0)) {
                        builder.append("-");
                        builder.append(i);
                    }
                    builder.append("周");
                    if (i < j)//不是在最后一个周段结束时
                        builder.append("，");
                }
            }
        }
        if (builder.toString().equals("已选择："))
            weekText.setText("");
        else weekText.setText(builder.toString());
    }
    private boolean checkWeekOverLap(Subject subject, List<Integer> weekList){
        for(int week: subject.getWeeks()){
            if(weekList.get(week-1) ==1)
                return true;
        }
        return false;
    }
}
