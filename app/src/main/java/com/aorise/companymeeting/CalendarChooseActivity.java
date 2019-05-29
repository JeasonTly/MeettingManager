package com.aorise.companymeeting;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.aorise.companymeeting.adapter.MeettingContentAdapter;
import com.aorise.companymeeting.base.MeettingContent;
import com.aorise.companymeeting.base.SpacesItemDecoration;
import com.aorise.companymeeting.databinding.ActivityCalendarChooseBinding;
import com.aorise.companymeeting.sqlite.DatabaseHelper;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;

import java.util.ArrayList;
import java.util.List;

public class CalendarChooseActivity extends AppCompatActivity {
    private final String TAG = CalendarChooseActivity.class.getName();
    private ActivityCalendarChooseBinding mDataBinding;
    private Calendar schemecalendar;
    private SharedPreferences sp;
    private List<MeettingContent> meettingContents = new ArrayList<>();
    private MeettingContentAdapter mAdatper;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_calendar_choose);
        sp = getSharedPreferences("MeettingList", Context.MODE_PRIVATE);
        databaseHelper = new DatabaseHelper(this);
        mAdatper = new MeettingContentAdapter(this, meettingContents);
        mDataBinding.calendar.setOnCalendarSelectListener(new CalendarView.OnCalendarSelectListener() {
            @Override
            public void onCalendarOutOfRange(Calendar calendar) {

            }

            @Override
            public void onCalendarSelect(Calendar calendar, boolean isClick) {
                Log.d(TAG, " onCalendarSelect " + calendar.getYear() + "年" + calendar.getMonth() + "月" + calendar.getDay() + "日");
                schemecalendar = calendar;
                meettingContents = databaseHelper.queryMeetting(appendZero(schemecalendar.getYear()), appendZero(schemecalendar.getMonth()), appendZero(schemecalendar.getDay()));
                if (meettingContents != null) {
                    mAdatper.refreshData(meettingContents);
                }
            }
        });

        mDataBinding.calendar.setRange(mDataBinding.calendar.getCurYear(),
                mDataBinding.calendar.getCurMonth(),
                mDataBinding.calendar.getCurDay(),
                2099, 12, 31);
        mDataBinding.calendar.setSchemeColor(Color.RED, Color.RED, Color.RED);

        mDataBinding.contentList.setLayoutManager(new LinearLayoutManager(this));
        mDataBinding.contentList.addItemDecoration(new SpacesItemDecoration(8));
        mDataBinding.contentList.setAdapter(mAdatper);

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
                Log.d(TAG, " iscurrent day" + schemecalendar.isCurrentDay());
                Intent mIntent = new Intent();
                mIntent.setClass(this, MeettingContentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("select_date", schemecalendar);
                mIntent.putExtra("select_date", bundle);
                startActivityForResult(mIntent, 2034);

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 2034) {
                String content = data.getStringExtra("content");

                Bundle bundle = data.getBundleExtra("date");
                Calendar mCalendar = (Calendar) bundle.getSerializable("current_date");
                mDataBinding.calendar.addSchemeDate(mCalendar);
                MeettingContent meettingContent = new MeettingContent(appendZero(mCalendar.getYear()), appendZero(mCalendar.getMonth()), appendZero(mCalendar.getDay()),
                        appendZero(data.getIntExtra("start_hour",0)),
                        appendZero(data.getIntExtra("start_minutes",0)),
                        appendZero(data.getIntExtra("end_hour",0)),
                        appendZero(data.getIntExtra("end_minutes",0)));
                meettingContent.setContent(content);

                meettingContents.add(meettingContent);
                Log.d(TAG, " meetting content is " + meettingContent.toString() + "  meettingContents size " + meettingContents.size());
                databaseHelper.insertMeetting(meettingContent);
                mAdatper.addData(meettingContent);
                mDataBinding.calendar.update();
            }
        }
    }
    private String appendZero(int time) {
        if (time < 10) {
            return "0" + String.valueOf(time);
        } else {
            return String.valueOf(time);
        }
    }
}
