package com.example.shijianzhou;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

/**
 * ������ʾ�ؼ���
 * 
 * @author �ı�zhou ԭ��Vincent Lee
 * 
 */

public class Calendar extends LinearLayout implements View.OnClickListener {

	private GestureDetector gestureDetector = null;
	private CalendarAdapter calV = null;
	private ViewFlipper flipper = null;
	private GridView gridView = null;
	private GridView currentgridView = null;
	private static int jumpMonth = 0; // ÿ�λ��������ӻ��ȥһ����,Ĭ��Ϊ0������ʾ��ǰ�£�
	private static int jumpYear = 0; // ������Խһ�꣬�����ӻ��߼�ȥһ��,Ĭ��Ϊ0(����ǰ��)
	private int year_c = 0;
	private int month_c = 0;
	private int day_c = 0;
	private String currentDate = "";
	/** ÿ�����gridview��viewflipper��ʱ���ı�� */
	private int gvFlag = 0;
	/** ��ǰ�����£������������� */
	private TextView currentMonth;
	/** �ϸ��� */
	private ImageView prevMonth;
	/** �¸��� */
	private ImageView nextMonth;

	private CalendarCallback calendarcallback;

	public void setCalendarcallback(CalendarCallback calendarcallback) {
		this.calendarcallback = calendarcallback;
	}

	public Calendar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater.from(context).inflate(R.layout.calendar, this, true);
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
		currentDate = sdf.format(date); // ��������
		year_c = Integer.parseInt(currentDate.split("-")[0]);
		month_c = Integer.parseInt(currentDate.split("-")[1]);
		day_c = Integer.parseInt(currentDate.split("-")[2]);

		currentMonth = (TextView) findViewById(R.id.currentMonth);
		prevMonth = (ImageView) findViewById(R.id.prevMonth);
		nextMonth = (ImageView) findViewById(R.id.nextMonth);
		setListener();

		gestureDetector = new GestureDetector(new MyGestureListener());
		flipper = (ViewFlipper) findViewById(R.id.flipper);
		flipper.removeAllViews();
		calV = new CalendarAdapter(getContext(), getResources(), jumpMonth,
				jumpYear, year_c, month_c, day_c);
		addGridView();
		gridView.setAdapter(calV);
		flipper.addView(gridView, 0);
		addTextToTopTextView(currentMonth);
	}

	private class MyGestureListener extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			int gvFlag = 0; // ÿ�����gridview��viewflipper��ʱ���ı��
			if (e1.getX() - e2.getX() > 120) {
				// ���󻬶�
				enterNextMonth(gvFlag);
				return true;
			} else if (e1.getX() - e2.getX() < -120) {
				// ���һ���
				enterPrevMonth(gvFlag);
				return true;
			}
			return false;
		}
	}

	/**
	 * �ƶ�����һ����
	 * 
	 * @param gvFlag
	 */
	private void enterNextMonth(int gvFlag) {
		addGridView(); // ���һ��gridView
		jumpMonth++; // ��һ����

		calV = new CalendarAdapter(getContext(), this.getResources(),
				jumpMonth, jumpYear, year_c, month_c, day_c);
		gridView.setAdapter(calV);
		addTextToTopTextView(currentMonth); // �ƶ�����һ�º󣬽�������ʾ��ͷ������
		gvFlag++;
		flipper.addView(gridView, gvFlag);
		flipper.setInAnimation(AnimationUtils.loadAnimation(getContext(),
				R.anim.push_left_in));
		flipper.setOutAnimation(AnimationUtils.loadAnimation(getContext(),
				R.anim.push_left_out));
		flipper.showNext();
		flipper.removeViewAt(0);
	}

	/**
	 * �ƶ�����һ����
	 * 
	 * @param gvFlag
	 */
	private void enterPrevMonth(int gvFlag) {
		addGridView(); // ���һ��gridView
		jumpMonth--; // ��һ����

		calV = new CalendarAdapter(getContext(), this.getResources(),
				jumpMonth, jumpYear, year_c, month_c, day_c);
		gridView.setAdapter(calV);
		gvFlag++;
		addTextToTopTextView(currentMonth); // �ƶ�����һ�º󣬽�������ʾ��ͷ������
		flipper.addView(gridView, gvFlag);

		flipper.setInAnimation(AnimationUtils.loadAnimation(getContext(),
				R.anim.push_right_in));
		flipper.setOutAnimation(AnimationUtils.loadAnimation(getContext(),
				R.anim.push_right_out));
		flipper.showPrevious();
		flipper.removeViewAt(0);
	}

	/**
	 * ���ͷ������� �����µ���Ϣ
	 * 
	 * @param view
	 */
	public void addTextToTopTextView(TextView view) {
		StringBuffer textDate = new StringBuffer();
		// draw = getResources().getDrawable(R.drawable.top_day);
		// view.setBackgroundDrawable(draw);
		textDate.append(calV.getShowYear()).append("��")
				.append(calV.getShowMonth()).append("��").append("\t");
		view.setText(textDate);
	}

	private void addGridView() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		// ȡ����Ļ�Ŀ�Ⱥ͸߶�
		WindowManager windowManager = (WindowManager) getContext()
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		int Width = display.getWidth();
		int Height = display.getHeight();

		gridView = new GridView(getContext());
		gridView.setNumColumns(7);
		gridView.setColumnWidth(40);
		// gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		if (Width == 720 && Height == 1280) {
			gridView.setColumnWidth(40);
		}
		gridView.setGravity(Gravity.CENTER_VERTICAL);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		// ȥ��gridView�߿�
		gridView.setVerticalSpacing(1);
		gridView.setHorizontalSpacing(1);
		gridView.setOnTouchListener(new OnTouchListener() {
			// ��gridview�еĴ����¼��ش���gestureDetector

			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return Calendar.this.gestureDetector.onTouchEvent(event);
			}
		});

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				// ʹѡ�����ڱ�ɫ
				calV.setSeclection(position);
				calV.notifyDataSetChanged();
				// ����κ�һ��item���õ����item������(�ų�����������յ�����(�������Ӧ))
				int startPosition = calV.getStartPositon();
				int endPosition = calV.getEndPosition();
				// Log.v("znz", "startPosition ---> " + startPosition
				// + " endPosition ---> " + endPosition
				// + " position ---> " + position);
				// if (startPosition <= position + 7
				// && position <= endPosition - 7) {
				String scheduleDay = calV.getDateByClickItem(position).split(
						"\\.")[0]; // ��һ�������
				// String scheduleLunarDay =
				// calV.getDateByClickItem(position).split("\\.")[1];
				// //��һ�������
				String scheduleYear = calV.getShowYear();
				String scheduleMonth = calV.getShowMonth();

				if (calendarcallback != null) {
					calendarcallback.calendarcallback(scheduleYear,
							scheduleMonth, scheduleDay);
				}

				// }
			}
		});
		gridView.setLayoutParams(params);
	}

	private void setListener() {
		prevMonth.setOnClickListener(this);
		nextMonth.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.nextMonth: // ��һ����
			enterNextMonth(gvFlag);
			break;
		case R.id.prevMonth: // ��һ����
			enterPrevMonth(gvFlag);
			break;
		default:
			break;
		}
	}

	public int getYear_c() {
		return year_c;
	}

	public int getMonth_c() {
		return month_c;
	}

	public int getDay_c() {
		return day_c;
	}

}
