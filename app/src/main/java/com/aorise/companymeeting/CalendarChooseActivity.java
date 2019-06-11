package com.aorise.companymeeting;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.aorise.companymeeting.adapter.MeettingContentAdapter;
import com.aorise.companymeeting.base.LogT;
import com.aorise.companymeeting.base.MeettingContent;
import com.aorise.companymeeting.base.MeettingInfo;
import com.aorise.companymeeting.base.SpacesItemDecoration;
import com.aorise.companymeeting.databinding.ActivityCalendarChooseBinding;
import com.aorise.companymeeting.sqlite.DatabaseHelper;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.hjq.toast.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalendarChooseActivity extends AppCompatActivity implements MeettingContentAdapter.MeettingContentItemDelete {
    private final String TAG = CalendarChooseActivity.class.getName();
    private ActivityCalendarChooseBinding mDataBinding;
    private Calendar schemecalendar;
    private Calendar currentCalendar;
    private List<MeettingInfo> meettingContents = new ArrayList<>();
    private MeettingContentAdapter mAdatper;
    private DatabaseHelper databaseHelper;
    private String RoomName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_calendar_choose);
        databaseHelper = new DatabaseHelper(this);
        RoomName = getIntent().getStringExtra("room_name");
        init();
    }

    /**
     * 初始化日历和列表
     */
    private void init() {

        mDataBinding.calendar.setOnMonthChangeListener(new CalendarView.OnMonthChangeListener() {
            @Override
            public void onMonthChange(int year, int month) {
                mDataBinding.calendarDate.setText(year + "年" + month + "月");
            }
        });
        mDataBinding.calendar.setSchemeColor(Color.RED, Color.RED, Color.RED);


        meettingContents = databaseHelper.queryDayofMeetting(RoomName, mDataBinding.calendar.getSelectedCalendar());
        schemecalendar = mDataBinding.calendar.getSelectedCalendar();
        currentCalendar = schemecalendar;
        LogT.d(" schemecalendar " + schemecalendar.toString());
        mAdatper = new MeettingContentAdapter(this, meettingContents, this);
        mDataBinding.calendar.setOnCalendarSelectListener(new CalendarView.OnCalendarSelectListener() {
            @Override
            public void onCalendarOutOfRange(Calendar calendar) {

            }

            @Override
            public void onCalendarSelect(Calendar calendar, boolean isClick) {
                Log.d(TAG, " onCalendarSelect " + calendar.getYear() + "年" + calendar.getMonth() + "月" + calendar.getDay() + "日");
                schemecalendar = calendar;
                mDataBinding.calendarDate.setText(calendar.getYear() + "年" + calendar.getMonth() + "月");
                meettingContents = databaseHelper.queryDayofMeetting(RoomName, calendar);
                if (meettingContents != null) {
                    mAdatper.refreshData(meettingContents);
                }
            }
        });

        mDataBinding.contentList.setLayoutManager(new LinearLayoutManager(this));
        mDataBinding.contentList.addItemDecoration(new SpacesItemDecoration(8));
        mDataBinding.contentList.setAdapter(mAdatper);
    }

    @Override
    protected void onResume() {
        super.onResume();
        signSchemeDate();
    }

    /**
     * 标记事件日期
     */
    private void signSchemeDate() {
        List<MeettingInfo> datas = new ArrayList<>();
        datas = databaseHelper.queryAllDayToSignScheme(RoomName);
        if (datas != null && datas.size() != 0) {
            for (MeettingInfo data : datas) {
                mDataBinding.calendar.addSchemeDate(String2Calendar(data.getChooseDate()));
            }
            mDataBinding.calendar.update();
        }
    }

    private Calendar String2Calendar(String chooseDate) {
        LogT.d("chooseDate " + chooseDate);
        Calendar calendar = new Calendar();
        int year = Integer.valueOf(chooseDate.substring(0, 4));
        int month = Integer.valueOf(chooseDate.substring(5, 7));
        int day = Integer.valueOf(chooseDate.substring(8, 10));
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        return calendar;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_meetting:
                goTosetMeettingTime();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 启动会议时间选择
     */
    private void goTosetMeettingTime() {
        if (Integer.valueOf(currentCalendar.toString()) > Integer.valueOf(schemecalendar.toString())) {
            ToastUtils.show("不可以在当前日期前设置会议！");
            return;
        }
        Intent mIntent = new Intent();
        mIntent.setClass(this, MeettingContentActivity.class);//会议时间选择器
        Bundle bundle = new Bundle();
        bundle.putSerializable("select_date", schemecalendar);
        mIntent.putExtra("select_date", bundle);
        mIntent.putExtra("room_name_", RoomName);
        startActivityForResult(mIntent, 2034);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 2034) {
                String content = data.getStringExtra("content");
                Bundle bundle = data.getBundleExtra("date");
                Calendar mCalendar = (Calendar) bundle.getSerializable("current_date");
                mDataBinding.calendar.addSchemeDate(mCalendar);//日历控件添加并标记日期
                String depart_name = data.getStringExtra("depart_name");
                MeettingInfo meettingInfo = new MeettingInfo();
                meettingInfo.setStart_time(appendZero(data.getIntExtra("start_hour", 0)) + ":" + appendZero(data.getIntExtra("start_minutes", 0)));
                meettingInfo.setEnd_time(appendZero(data.getIntExtra("end_hour", 0)) + ":" + appendZero(data.getIntExtra("end_minutes", 0)));
                meettingInfo.setContent(content);
                meettingInfo.setRoomName(RoomName);
                meettingInfo.setChooseDate(appendZero(mCalendar.getYear()) + "年" + appendZero(mCalendar.getMonth()) + "月" + appendZero(mCalendar.getDay()) + "日");
                meettingInfo.setDepartmentInfo(TextUtils.isEmpty(depart_name) ? "未知部门" : depart_name);
                Log.d(TAG, " meettingInfo content is " + meettingInfo.toString() + "  meettingInfo size " + meettingContents.size());
                databaseHelper.insertMeetting(meettingInfo);
                mAdatper.addData(meettingInfo);//更新RecyclerView
                mDataBinding.calendar.update();//日历动态更新显示标记的日期
            }
        }
    }

    /**
     * 把小于10的时间改为0x
     *
     * @param time
     * @return
     */
    private String appendZero(int time) {
        if (time < 10) {
            return "0" + String.valueOf(time);
        } else {
            return String.valueOf(time);
        }
    }

    @Override
    public void onItemLongClick(final MeettingInfo content) {
        View view = LayoutInflater.from(this).inflate(R.layout.delete_warning, null);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("警告!")
                .setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseHelper.getInstance(CalendarChooseActivity.this).deleteMeetting(content);
                        meettingContents = DatabaseHelper.getInstance(CalendarChooseActivity.this).queryDayofMeetting(RoomName, mDataBinding.calendar.getSelectedCalendar());
                        mAdatper.refreshData(meettingContents);
                        // signSchemeDate();
                        mDataBinding.calendar.removeSchemeDate(String2Calendar(content.getChooseDate()));
                        ToastUtils.show("会议删除成功!");
                        dialog.dismiss();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setCancelable(false).create();
        dialog.show();

    }
}
