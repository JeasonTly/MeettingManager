package com.aorise.companymeeting.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.aorise.companymeeting.R;

/**
 * Created by Tuliyuan.
 * Date: 2019/5/27.
 */
public class TimePickerLinearLayout extends LinearLayout implements TimePickerListener, MinutesView.OnSelectionChangeListener {
    private int current_hour = 8;
    private int current_minutes = 30;
    private Context mContext;

    private TimePicker timePicker;
    private MinutesView minutsPicker;

    public TimePickerLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.linear_time_select, this);
        timePicker = (TimePicker) findViewById(R.id.hour_pick);
        timePicker.setTimePickerListener(this);
        minutsPicker = (MinutesView) findViewById(R.id.minuts_pick);
        minutsPicker.setOnSelectionChangeListener(this);
    }

    @Override
    public void onHourSelect(int hour) {
        current_hour = hour;
    }

    @Override
    public void onPositionChange(int newPositoin, int oldPosition) {
        current_minutes = newPositoin;
        Toast.makeText(mContext, "时间是:" + current_hour + ":" + current_minutes, Toast.LENGTH_SHORT).show();
    }


    public TimePicker getTimePicker() {
        return timePicker;
    }

    public MinutesView getMinutsPicker() {
        return minutsPicker;
    }

    public int getCurrent_hour() {
        return current_hour;
    }

    public int getCurrent_minutes() {
        return current_minutes;
    }
}
