package com.example.shijianzhou;

import java.util.List;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.database.Meeting;

public class MainActivity extends Activity implements CalendarCallback,
		TimelineCallback, refreshCalendar {

	private Calendar calendar;
	private TimeLine timeline;
	private Button huiyiyuyue;
	private TextView titleofall;
	private TextView huiyititle;
	private TextView yudingren;
	private TextView chuxiren;
	private TextView waiburen;
	private TextView beizhu;
	private String datecurrent;
	private String dateclicked;
	private LinearLayout linearincludecalendar;
	private DigitalClock digitalclock;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		SQLiteDatabase db = Connector.getDatabase();
		titleofall = (TextView) findViewById(R.id.titleofall);
		calendar = (Calendar) findViewById(R.id.calendar);
		calendar.setCalendarcallback(this);
		datecurrent = "" + calendar.getYear_c() + calendar.getMonth_c()
				+ calendar.getDay_c();
		dateclicked = datecurrent;
		timeline = (TimeLine) findViewById(R.id.timeline);
		timeline.setTimelinecallback(this);
		huiyititle = (TextView) findViewById(R.id.huiyititle);
		yudingren = (TextView) findViewById(R.id.yudingren);
		chuxiren = (TextView) findViewById(R.id.chuxiren);
		waiburen = (TextView) findViewById(R.id.waiburen);
		beizhu = (TextView) findViewById(R.id.beizhu);
		huiyiyuyue = (Button) findViewById(R.id.huiyiyuyue);
		linearincludecalendar = (LinearLayout) findViewById(R.id.linearincludecalendar);
		digitalclock = (DigitalClock) findViewById(R.id.digitalclock);
		digitalclock.setRefreshcalendar(this);
		huiyiyuyue.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
						HuiyiYuyueActivity.class);
				startActivityForResult(intent, 0);
			}
		});
		titleofall.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
						HuiyishiActivity.class);
				startActivityForResult(intent, 2);
			}
		});
		List<Meeting> meetingList = DataSupport.where("date == ?", dateclicked)
				.find(Meeting.class);
		if (meetingList != null) {
			for (int i = 0; i < meetingList.size(); i++) {
				timeline.setSegmentRed(meetingList.get(i).getStart(),
						meetingList.get(i).getEnd());
			}
		}
	}

	@Override
	public void calendarcallback(String scheduleYear, String scheduleMonth,
			String scheduleDay) {
		// TODO Auto-generated method stub
		String scheduledate = "" + scheduleYear + scheduleMonth + scheduleDay;
		if (scheduledate.equalsIgnoreCase(dateclicked)) {
			// 重复点击同一个日期，直接返回不用进行任何操作。
			return;
		} else {
			// 点击到了不同于当前选中日期的日期。
			if (scheduledate.equalsIgnoreCase(datecurrent)) {
				// 是当日的日期则开表。
				// Log.v("znz", "timelinestart()");
				timeline.setmTickerstart();
			} else {
				// 不是当日的日期则停表。
				// Log.v("znz", "timelinestoped");
				huiyititle.setText("暂无会议");
				yudingren.setText("预定人：");
				chuxiren.setText("出席人员：");
				waiburen.setText("外部人员：");
				beizhu.setText("备注：");
				timeline.setmTickerStopped();
			}
			// 更新时间轴上会议占用时间信息。
			timeline.clearAllsegment();
			List<Meeting> meetingList = DataSupport.where("date == ?",
					scheduledate).find(Meeting.class);
			if (meetingList != null) {
				for (int i = 0; i < meetingList.size(); i++) {
					timeline.setSegmentRed(meetingList.get(i).getStart(),
							meetingList.get(i).getEnd());
				}
			}
		}
		dateclicked = scheduledate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (data == null) {
			return;
		}
		Log.v("znz", "requestCode ---> " + requestCode + " resultCode ---> "
				+ resultCode);
		Log.v("znz", "type " + data.getStringExtra("type"));
		if (requestCode == 0) {
			if (data.getStringExtra("type").equalsIgnoreCase("kuaisu")) {
				int result = timeline.setSegmentRed(
						data.getIntExtra("start", 0),
						data.getIntExtra("end", 0));
				if (result == 1) {
					Meeting meeting = new Meeting();
					meeting.setDate(dateclicked);
					meeting.setStart(data.getIntExtra("start", 0));
					meeting.setEnd(data.getIntExtra("end", 0));
					meeting.setTitle("快速会议");
					meeting.setYudingren("预定人: "
							+ data.getStringExtra("yudingren"));
					meeting.setChuxiren("出席人员：");
					meeting.setWaiburen("外部人员：");
					meeting.setBeizhu("备注：");
					meeting.save();
				} else {
					if (result == -1) {
						Toast.makeText(getBaseContext(), "会议时间错误",
								Toast.LENGTH_SHORT).show();
					} else if (result == -2) {
						Toast.makeText(getBaseContext(), "该时段已有会议",
								Toast.LENGTH_SHORT).show();
					}
				}
			} else if (data.getStringExtra("type").equalsIgnoreCase("putong")) {
				int result = timeline.setSegmentRed(
						data.getIntExtra("start", 0),
						data.getIntExtra("end", 0));
				if (result == 1) {
					Meeting meeting = new Meeting();
					meeting.setDate(dateclicked);
					meeting.setStart(data.getIntExtra("start", 0));
					meeting.setEnd(data.getIntExtra("end", 0));
					meeting.setTitle(data.getStringExtra("huiyimingcheng"));
					meeting.setYudingren("预定人: "
							+ data.getStringExtra("yudingren"));
					meeting.setChuxiren("出席人员："
							+ data.getStringExtra("chuxiren"));
					meeting.setWaiburen("外部人员："
							+ data.getStringExtra("waiburen"));
					meeting.setBeizhu("备注：" + data.getStringExtra("beizhu"));
					meeting.save();
				} else {
					if (result == -1) {
						Toast.makeText(getBaseContext(), "会议时间错误",
								Toast.LENGTH_SHORT).show();
					} else if (result == -2) {
						Toast.makeText(getBaseContext(), "该时段已有会议",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		}
	}

	@Override
	public void huiyiStarted(int start, int end) {
		// TODO Auto-generated method stub
		// Log.v("znz", "huiyistarted");
		List<Meeting> meetingList = DataSupport.where(
				"start == ? and end == ?", start + "", end + "").find(
				Meeting.class);
		if (meetingList != null) {
			Meeting meeting = meetingList.get(0);
			if (meeting != null) {
				if (!dateclicked.equalsIgnoreCase(datecurrent)) {
					// 不是当日，不进行会议信息显示。
					huiyititle.setText("暂无会议");
					yudingren.setText("预定人：");
					chuxiren.setText("出席人员：");
					waiburen.setText("外部人员：");
					beizhu.setText("备注：");
					return;
				} else {
					// 是当天，则显示会议信息。
					huiyititle.setText(meeting.getTitle());
					yudingren.setText(meeting.getYudingren());
					chuxiren.setText(meeting.getChuxiren());
					waiburen.setText(meeting.getWaiburen());
					beizhu.setText(meeting.getBeizhu());
				}
			} else {
				huiyititle.setText("暂无会议");
				yudingren.setText("预定人：");
				chuxiren.setText("出席人员：");
				waiburen.setText("外部人员：");
				beizhu.setText("备注：");
			}
		} else {
			huiyititle.setText("暂无会议");
			yudingren.setText("预定人：");
			chuxiren.setText("出席人员：");
			waiburen.setText("外部人员：");
			beizhu.setText("备注：");
		}
	}

	@Override
	public void huiyiStoped() {
		// TODO Auto-generated method stub
		// Log.v("znz", "huiyistoped");
		huiyititle.setText("暂无会议");
		yudingren.setText("预定人：");
		chuxiren.setText("出席人员：");
		waiburen.setText("外部人员：");
		beizhu.setText("备注：");
	}

	/**
	 * 日历刷新。
	 */
	public void refreshCalendar() {
		linearincludecalendar.removeAllViews();
		calendar = new Calendar(getBaseContext(), null);
		linearincludecalendar.addView(calendar);
		calendar.setCalendarcallback(this);

		datecurrent = "" + calendar.getYear_c() + calendar.getMonth_c()
				+ calendar.getDay_c();
		dateclicked = datecurrent;

		timeline.clearAllsegment();
		List<Meeting> meetingList = DataSupport.where("date == ?", dateclicked)
				.find(Meeting.class);
		if (meetingList != null) {
			for (int i = 0; i < meetingList.size(); i++) {
				timeline.setSegmentRed(meetingList.get(i).getStart(),
						meetingList.get(i).getEnd());
			}
		}
	}

}
// Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time
// // Zone资料。
// t.setToNow(); // 取得系统时间。
// int hour = t.hour; // 0-23
// int minute = t.minute;
// int second = t.second;
// int all = hour * 60 * 60 + minute * 60 + second;
// if (requestCode == 0) {
// int result = timeline.setSegmentRed(all, all + 60 * 60);
// if (result == 1) {
// Meeting meeting = new Meeting();
// meeting.setDate(dateclicked);
// meeting.setStart(all);
// meeting.setEnd(all + 60 * 60);
// meeting.setTitle("快速会议");
// meeting.setYudingren("预定人：张三");
// meeting.setChuxiren("出席人员：李四、王五、赵六");
// meeting.setWaiburen("外部人员：刘七、秦八");
// meeting.setBeizhu("备注：App原型展示");
// if (meeting.save()) {
// Toast.makeText(getBaseContext(), "会议信息存储成功",
// Toast.LENGTH_SHORT).show();
// } else {
// Toast.makeText(getBaseContext(), "会议信息存储失败",
// Toast.LENGTH_SHORT).show();
// }
// } else {
// Toast.makeText(getBaseContext(), "该时段已有会议", Toast.LENGTH_SHORT)
// .show();
// }
// }
