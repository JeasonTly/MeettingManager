package com.aorise.companymeeting;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.aorise.companymeeting.base.DepartmentInfo;
import com.aorise.companymeeting.base.LogT;
import com.aorise.companymeeting.base.MeettingInfo;
import com.aorise.companymeeting.base.TimeAreaUtil;
import com.aorise.companymeeting.databinding.ActivityMeettingContentBinding;
import com.aorise.companymeeting.sqlite.DatabaseHelper;
import com.haibin.calendarview.Calendar;
import com.hjq.toast.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MeettingContentActivity extends AppCompatActivity {
    private final String TAG = "MeettingContentActivity";
    private ActivityMeettingContentBinding mDataBinding;

    /**
     * 传入时间区
     */

    private Calendar mSelectCalendar;

    /**
     * 数据库
     */
    private DatabaseHelper mDb;

    /**
     * 当前日期下的会议内容；
     */
    private List<MeettingInfo> mCurrentDayMeettingList;
    private String RoomName;
    private String DepartmentName;
    private MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_meetting_content);
        /** 设置日历时间*/
        initCalendar();
        RoomName = getIntent().getStringExtra("room_name_");
        mDb = new DatabaseHelper(this);
        mDataBinding.endTime.getTimePicker().setmCursorPathColor(Color.GREEN);
        mDataBinding.sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                determineTime(mDataBinding.startTime.getCurrent_hour(), mDataBinding.endTime.getCurrent_hour(),
                        mDataBinding.startTime.getCurrent_minutes(), mDataBinding.endTime.getCurrent_minutes());
            }
        });
    }

    private void initCalendar() {
        Bundle receive_data = getIntent().getBundleExtra("select_date");
        mSelectCalendar = (Calendar) receive_data.get("select_date");
        if (mSelectCalendar.isCurrentDay()) {
            Log.d(TAG, " is today");
            Date date = new Date();
            SimpleDateFormat hour = new SimpleDateFormat("HH");
            SimpleDateFormat minutes = new SimpleDateFormat("mm");
            int selection = Integer.valueOf(minutes.format(date)) / 5;
            mDataBinding.startTime.getTimePicker().setStartHour(Integer.valueOf(hour.format(date)));
            mDataBinding.startTime.getMinutsPicker().setMinutes(selection);
            mDataBinding.startTime.getTimePicker().setNow(Integer.valueOf(hour.format(date)));
            mDataBinding.endTime.getTimePicker().setStartHour(23);
            mDataBinding.endTime.getMinutsPicker().setMinutes(0);
        } else {
            mDataBinding.startTime.getTimePicker().setStartHour(8);
            mDataBinding.startTime.getMinutsPicker().setMinutes(0);
            mDataBinding.endTime.getTimePicker().setStartHour(18);
            mDataBinding.endTime.getMinutsPicker().setMinutes(0);
        }
    }

    private void determineTime(int start_time, int end_time, int start_min, int end_minutes) {
        LogT.d(" start_time " + start_time + " start_min " + start_min + " end _hour " + end_time + " end min " + end_minutes);
        String start_time_str = appendZero(start_time) + ":" + appendZero(start_min);
        String end_time_str = appendZero(end_time) + ":" + appendZero(end_minutes);
        if (end_time < start_time) {
            ToastUtils.show("会议结束时间不得小于开始时间!");
            return;
        }
        if (TextUtils.isEmpty(DepartmentName)) {
            ToastUtils.show("必须选择部门！");
            return;
        }
        if (start_time == end_time) {
            if (start_min > end_minutes) {
                ToastUtils.show("会议开始时间不得小于结束时间!");
                return;
            }
        }
        mCurrentDayMeettingList = mDb.queryDayofMeetting(RoomName, mSelectCalendar);

        for (MeettingInfo meettingInfo : mCurrentDayMeettingList) {
            LogT.d(" meettingContent " + meettingInfo);
            if ((start_time_str.equals(meettingInfo.getStart_time()))) {
                ToastUtils.show("该时段已存在会议!请重新选择会议时间!");
                return;
            }
            if (TimeAreaUtil.getInstance().isInAnotherTimeArea(start_time_str, end_time_str, meettingInfo.getStart_time(), meettingInfo.getEnd_time())) {
                Log.d(TAG, " 此时间段内已有会议!");
                ToastUtils.show("该时段已存在会议!请重新选择会议时间!");
                return;
            }
            ;
        }

        Intent mIntent = new Intent();
        mIntent.putExtra("content", mDataBinding.meettingContent.getText().toString());
        Bundle bundle = new Bundle();
        bundle.putSerializable("current_date", mSelectCalendar);
        mIntent.putExtra("date", bundle);
        mIntent.putExtra("start_hour", mDataBinding.startTime.getCurrent_hour());
        mIntent.putExtra("start_minutes", mDataBinding.startTime.getCurrent_minutes());
        mIntent.putExtra("end_hour", mDataBinding.endTime.getCurrent_hour());
        mIntent.putExtra("end_minutes", mDataBinding.endTime.getCurrent_minutes());
        mIntent.putExtra("depart_name", DepartmentName);
        setResult(RESULT_OK, mIntent);
        MeettingContentActivity.this.finish();
    }

    private String appendZero(int time) {
        if (time < 10) {
            return "0" + String.valueOf(time);
        } else {
            return String.valueOf(time);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.meetting_depart_choose, menu);
        menuItem = menu.findItem(R.id.choose_depart);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.choose_depart:
                showDepartmentList();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDepartmentList() {

        List<DepartmentInfo> mDepartmentInfoList = DatabaseHelper.getInstance(this).getDepartList();

        final String[] array = new String[mDepartmentInfoList.size()];
        for (int i = 0; i < mDepartmentInfoList.size(); i++) {
            array[i] = mDepartmentInfoList.get(i).getName();
        }
        if(array.length == 0){
            ToastUtils.show("您还没有创建部门!");
            return;
        }
        LogT.d("list size is " + array.length);
        AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle("部门选择")
                .setItems(array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DepartmentName = array[which];
                        menuItem.setTitle("当前部门:" + DepartmentName);
                        dialog.dismiss();
                    }
                })
                .setCancelable(true).create();
        alertDialog.show();
        LogT.d(" departname is " + DepartmentName);
    }
}
