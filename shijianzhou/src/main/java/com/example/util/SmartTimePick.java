package com.example.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.shijianzhou.R;

/**
 * 实现时间选择，只有小时和分钟，步长可设置。
 * 
 * @author zhou
 * 
 */
public class SmartTimePick extends LinearLayout {

	private SmartButton9 add, sub;
	private TextView tv;
	private int minute = 0;
	private int hour = 0;
	private int hour_now = 0;
	private int minute_now = 0;
	private int timeoffset = 0;
	private int stepLength = 5;

	public SmartTimePick(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.smartnumpick, this);
		add = (SmartButton9) findViewById(R.id.add_smartpick);
		sub = (SmartButton9) findViewById(R.id.sub_smartpick);
		tv = (TextView) findViewById(R.id.text_smartpick);
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.timepick);
		int indexCount = a.getIndexCount();
		for (int i = 0; i < indexCount; i++) {
			int index = a.getIndex(i);
			switch (index) {
			case R.styleable.timepick_stepLength:
				stepLength = a.getInt(index, 5);
				Log.v("znz", "stepLength ---> " + stepLength);
				break;
			case R.styleable.timepick_timeoffset:
				timeoffset = a.getInt(index, 0);
				Log.v("znz", "timeoffset ---> " + timeoffset);
				break;
			}
		}
		a.recycle();
		initTimePick();
	}

	/**
	 * 初始化。
	 */
	public void initTimePick() {
		Time t = new Time(); // or Time t=new Time("GMT+8"); 加上TimeZone资料。
		t.setToNow(); // 取得系统时间。
		hour_now = t.hour; // 0-23
		minute_now = t.minute;
		hour = hour_now;
		minute = minute_now;
		setMeetingLength();
		tv.setText(formate(hour) + ":" + formate(minute));
		add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (minute >= 55) {
					minute = minute + 5 - 60;
					if ((hour + 1) == 24) {
						// 如果再加5分钟，就超过24小时，什么都不做。
						minute = minute - 5 + 60;
						return;
					} else {
						hour++;
					}
				} else {
					minute = minute + 5;
				}
				tv.setText(formate(hour) + ":" + formate(minute));
			}
		});
		sub.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if ((minute == (minute_now + timeoffset) & hour == hour_now)
						| ((minute == (minute_now + timeoffset - 60)) & hour == (hour_now + 1))) {
					return;
				} else {
					if (minute <= 5) {
						minute = 60 - (5 - minute);
						hour--;
					} else {
						minute = minute - 5;
					}
					tv.setText(formate(hour) + ":" + formate(minute));
				}
			}
		});
	}

	/**
	 * 将个位数的时间转换成两位数，比如1->01。
	 * 
	 * @param time
	 * @return
	 */
	private String formate(int time) {
		if (time <= 9) {
			return "0" + time;
		} else {
			return "" + time;
		}
	}

	/**
	 * 设置默认会议时长。
	 * 
	 * @param hour
	 * @param minute
	 * @return
	 */
	private void setMeetingLength() {
		if ((minute + timeoffset) >= 60) {
			hour++;
			minute = minute + timeoffset - 60;
		} else {
			minute = minute + timeoffset;
		}
	}

	public int getStart() {
		return hour_now * 60 * 60 + minute_now * 60;
	}

	public int getEnd() {
		return hour * 60 * 60 + minute * 60;
	}

}
