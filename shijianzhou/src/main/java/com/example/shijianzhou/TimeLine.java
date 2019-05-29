package com.example.shijianzhou;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * 用来实现会议预定的时间轴。
 * 
 * @version 0.1
 * @author zhou
 * 
 */
public class TimeLine extends LinearLayout {

	private Runnable mTicker;
	private Handler mHandler;
	/**
	 * 控制定时线程是否运行的flag。
	 */
	private boolean mTickerStopped;
	private Calendar mCalendar;
	private String mFormat = "hh:mm:ss";
	private ImageView pasttime;
	private int Width;
	private int Height;
	private LinearLayout mainline;

	private ArrayList<HashMap<String, Integer>> huiyitimelist;

	public TimeLine(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater.from(context).inflate(R.layout.timeline, this, true);
		pasttime = (ImageView) findViewById(R.id.pasttime);
		mainline = (LinearLayout) findViewById(R.id.mainline);
		initTimeLine(context);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		setmTickerstart();
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		mTickerStopped = true;
	}

	private void initTimeLine(Context context) {
		if (mCalendar == null) {
			mCalendar = Calendar.getInstance();
		}
		huiyitimelist = new ArrayList<HashMap<String, Integer>>();
		// 取得屏幕的宽度和高度
		WindowManager windowManager = (WindowManager) getContext()
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		Width = display.getWidth();
		Height = display.getHeight();
	}

	/**
	 * 
	 * 移动游标到指定位置。
	 * 
	 * @param position
	 *            位置参数。
	 */
	public void movePoint(int position) {
		android.view.ViewGroup.LayoutParams params = pasttime.getLayoutParams();
		params.width = position;
		pasttime.setLayoutParams(params);
	}

	/**
	 * 
	 * 使时间轴上指定的一段变红，即该段时间会议室已经被预定。
	 * 
	 * @param start
	 *            起始时间(秒)
	 * @param end
	 *            结束时间(秒)
	 * 
	 * @return -1 失败 1 成功。
	 */
	public int setSegmentRed(int start, int end) {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		map.put("starttime", start);
		map.put("endtime", end);
		start = start * Width / (24 * 60 * 60);
		end = end * Width / (24 * 60 * 60);
		map.put("start", start);
		map.put("end", end);
		if (end <= start) {
			return -1;// 代表会议时间错误。
		}
		for (int i = 0; i < huiyitimelist.size(); i++) {
			int startlocal = huiyitimelist.get(i).get("start");
			int endlocal = huiyitimelist.get(i).get("end");
			if ((start > startlocal & start < endlocal)
					| (end > startlocal & end < endlocal)) {
				return -2;// 代表会议时间冲突。
			}
		}
		huiyitimelist.add(map);
		mainline.removeAllViews();
		for (int i = 0; i < huiyitimelist.size(); i++) {
			int startlocal = huiyitimelist.get(i).get("start");
			int endlocal = huiyitimelist.get(i).get("end");
			LinearLayout layout_sub_Lin = new LinearLayout(getContext());
			LinearLayout.LayoutParams LP_WW = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT);
			LP_WW.width = (endlocal - startlocal);
			if (i == 0) {
				LP_WW.leftMargin = startlocal;
			} else {
				LP_WW.leftMargin = startlocal
						- huiyitimelist.get(i - 1).get("end");
			}
			layout_sub_Lin.setLayoutParams(LP_WW);
			layout_sub_Lin.setBackgroundColor(0x88ffff00);
			mainline.addView(layout_sub_Lin);
		}
		return 1;
	}

	/**
	 * 清除所有时间轴上的会议占用时间标记。
	 */
	public void clearAllsegment() {
		mainline.removeAllViews();
		huiyitimelist.clear();
	}

	/**
	 * 控制定时器是否继续工作。
	 * 
	 * @param mTickerStopped
	 *            true则时钟停止。
	 */
	public void setmTickerStopped() {
		mTickerStopped = true;
		movePoint(0);
	}

	/**
	 * 时间轴动起来。
	 */
	public void setmTickerstart() {
		// TODO Auto-generated method stub
		mTickerStopped = false;
		mHandler = new Handler();
		mTicker = new Runnable() {
			public void run() {
				if (mTickerStopped) {
					return;
				} else {
					mCalendar.setTimeInMillis(System.currentTimeMillis());
					Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time
											// Zone资料。
					t.setToNow(); // 取得系统时间。
					int hour = t.hour; // 0-23
					int minute = t.minute;
					int second = t.second;
					int allseconds = hour * 60 * 60 + minute * 60 + second;
					int point = allseconds * Width / (24 * 60 * 60);
					movePoint(point);
					invalidate();
					// 遍历是否有会议开始或者结束。
					// Log.v("znz", "size --> " + huiyitimelist.size());
					// Log.v("znz",
					// "starttime --> "
					// + huiyitimelist.get(0).get("starttime"));
					// Log.v("znz", "allseconds ---> " + allseconds);
					// Log.v("znz",
					// "endtime --> " + huiyitimelist.get(0).get("endtime"));
					for (int i = 0; i < huiyitimelist.size(); i++) {
						if (allseconds >= huiyitimelist.get(i).get("starttime")
								& allseconds <= huiyitimelist.get(i).get(
										"endtime")) {

							if (timelinecallback != null) {
								timelinecallback.huiyiStarted(huiyitimelist
										.get(i).get("starttime"), huiyitimelist
										.get(i).get("endtime"));
								break;
							}
						}
						if (i == (huiyitimelist.size() - 1)) {
							if (timelinecallback != null) {
								timelinecallback.huiyiStoped();
							}
						}
					}
					long now = SystemClock.uptimeMillis();
					long next = now
							+ (1000 - System.currentTimeMillis() % 1000);
					mHandler.postAtTime(mTicker, next);
				}
			}
		};
		mTicker.run();
	}

	private TimelineCallback timelinecallback;

	/**
	 * @param timelinecallback
	 *            the timelinecallback to set
	 */
	public void setTimelinecallback(TimelineCallback timelinecallback) {
		this.timelinecallback = timelinecallback;
	}

}
