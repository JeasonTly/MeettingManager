package com.example.shijianzhou;

import java.util.Calendar;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

public class DigitalClock extends TextView {
	private final static String TAG = "DigitalClock";

	private Calendar mCalendar;
	private String mFormat = "yyyy.M.d E hh:mm:ss";

	private Runnable mTicker;
	private Handler mHandler;

	private boolean mTickerStopped = false;
	private refreshCalendar refreshcalendar;

	public DigitalClock(Context context) {
		super(context);
		initClock(context);
	}

	public DigitalClock(Context context, AttributeSet attrs) {
		super(context, attrs);
		initClock(context);
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.clocktext);
		int indexCount = a.getIndexCount();
		for (int i = 0; i < indexCount; i++) {
			int index = a.getIndex(i);
			switch (index) {
			case R.styleable.clocktext_clocktexttype:
				mFormat = a.getString(index);
				break;
			}
		}
		a.recycle();
	}

	private void initClock(Context context) {
		if (mCalendar == null) {
			mCalendar = Calendar.getInstance();
		}
	}

	@Override
	protected void onAttachedToWindow() {
		mTickerStopped = false;
		super.onAttachedToWindow();
		mHandler = new Handler();

		mTicker = new Runnable() {
			public void run() {
				if (mTickerStopped)
					return;
				mCalendar.setTimeInMillis(System.currentTimeMillis());
				// setText(mSimpleDateFormat.format(mCalendar.getTime()));
				setText(DateFormat.format(mFormat, mCalendar));
				invalidate();
				long now = SystemClock.uptimeMillis();
				// long next = now + (1000 - now % 1000);
				long next = now + (1000 - System.currentTimeMillis() % 1000);
				// TODO
				Time t = new Time();
				t.setToNow();
				int hour = t.hour;
				int minute = t.minute;
				int second = t.second;
				// Log.v("znz", "hour ---> " + hour);
				// Log.v("znz", "minute ---> " + minute);
				// Log.v("znz", "second ---> " + second);
				if (hour == 0 & minute == 0 & second == 3) {
					refreshcalendar.refreshCalendar();
				}
				mHandler.postAtTime(mTicker, next);
			}
		};
		mTicker.run();
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		mTickerStopped = true;
	}

	public void setFormat(String format) {
		mFormat = format;
	}

	/**
	 * @param refreshcalendar
	 *            the refreshcalendar to set
	 */
	public void setRefreshcalendar(refreshCalendar refreshcalendar) {
		this.refreshcalendar = refreshcalendar;
	}

}