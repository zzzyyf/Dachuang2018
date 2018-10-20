package com.example.zyf.timetable;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.example.zyf.timetable.db.Subject;

import org.litepal.exceptions.LitePalSupportException;

import java.util.ArrayList;
import java.util.List;

public class AddClassActivity extends AppCompatActivity {
    EditText weekText;//显示已选择的周
    AutoCompleteTextView nameText;//课名
    MultiAutoCompleteTextView placeText;//上课地点
    //TODO: 实现自动填充课名、地点
    ArrayList<Integer> litWeeksList;//已选择周数的列表
    RecyclerView weekdayPicker, sessionPicker;
    List<WeekItem> weekdays, sessions;//已选择的上课日、上课节的列表
    Subject classItem = new Subject();//待存储的一节课
    SwipeHelper weekdayHelper, sessionHelper;

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

        nameText = findViewById(R.id.class_name);
        placeText = findViewById(R.id.class_place);

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
        GridLayoutManager sessionsManager = new GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false);
        weekdayPicker.setLayoutManager(weekdaysManager);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_class_menu, menu);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1) {
            switch (resultCode) {
                case RESULT_OK:
                    litWeeksList = data.getIntegerArrayListExtra("Lit");
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
                    break;
                default:
            }
        }
    }

    boolean checkWeekItems(List<WeekItem> list) {
        for (WeekItem item : list
                ) {
            if (item.isLit()) return true;
        }
        return false;
    }

    ArrayList<Integer> saveSelectedItems(List<WeekItem> list) {
        ArrayList<Integer> listToSave = new ArrayList<>();
        String str;
        for (WeekItem item : list
                ) {
            if (item.isLit()) {
                str = item.getWeekNum().trim();
                StringBuilder builder = new StringBuilder();
                if (!"".equals(str)) {
                    for (int i = 0; i < str.length(); i++) {
                        if (str.charAt(i) >= 48 && str.charAt(i) <= 57)
                            builder.append(str.charAt(i));
                    }
                }
                listToSave.add(Integer.parseInt(builder.toString()));
            }
        }
        return listToSave;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_class:
                //先读取选择结果
                for (int i = 0; i < sessions.size(); i++)
                    if (sessionHelper.getLit().get(i).isLit())
                        sessions.get(i).setLit(sessionHelper.getLit().get(i).isLit());

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
                } else if (weekdayHelper.isSelected == -1) {
                    Toast.makeText(AddClassActivity.this, "你还没有选择上课日哦~", Toast.LENGTH_SHORT).show();
                    break;
                } else if (!checkWeekItems(sessions)) {
                    Toast.makeText(AddClassActivity.this, "你还没有选择上课时间哦~", Toast.LENGTH_SHORT).show();
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

                for (int i = 0; i < weekdays.size(); i++)
                    if (weekdays.get(i).isLit())
                        classItem.setWeekday(Integer.parseInt(weekdays.get(i).getWeekNum()));

                subject.setPeriods(saveSelectedItems(sessions));
                try {
                    subject.saveThrows();
                } catch (LitePalSupportException e) {
                    Toast.makeText(AddClassActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                setResult(RESULT_OK);
                finish();
            //设置点击toolbar返回按钮的动作
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
            default:
        }
        return true;
    }
}
