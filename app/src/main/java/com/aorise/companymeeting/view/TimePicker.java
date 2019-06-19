package com.aorise.companymeeting.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.aorise.companymeeting.R;
import com.aorise.companymeeting.base.LogT;
import com.hjq.toast.ToastUtils;


/**
 * Created by Tuliyuan.
 * Date: 2019/5/21.
 */
public class TimePicker extends View {

    private float width;
    private float height;

    private Paint mSelectedPaint;
    private Paint mTextPaint;
    private Paint mCursorPathPaint;
    @ColorInt
    private int mSecletedColor;
    @ColorInt
    private int mCursorPathColor;


    private float mPaintLineWidth;

    private float[] location_list;
    private float line_location_margin;//每一格所占的宽度
    private float margin = 2f;
    private float textMargin = 75f;// 时间点和文字的距离;
    private float line_base_location = 100f;

    private final float left_padding = 0;
    private final float right_padding = 10f;
    /**
     * 开始游标坐标点
     */
    private float cursor_startX_location = 0;
    private float cursor_NextX_location = 0;
    private float cursor_halfX_location = 0;
    private TimePickerListener timePickerListener;

    /**
     * 设置默认的游标时间点;
     */
    private int startTime = 8;
    private int now = -1;

    public TimePicker(Context context) {
        super(context);
    }

    public TimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TimePicker);
        mSecletedColor = typedArray.getColor(R.styleable.TimePicker_timePicker_Selected_color, context.getColor(R.color.timePicker_selected));
        //      mUnSecletColor = typedArray.getColor(R.styleable.TimePicker_timePicker_Selected_color, context.getColor(R.color.timePicker_unselect));
        mCursorPathColor = typedArray.getColor(R.styleable.TimePicker_timePicker_cursor_color, context.getColor(R.color.timePicker_cursor));
        ///       mCursorEndPathColor = typedArray.getColor(R.styleable.TimePicker_timePicker_cursor_end_color, context.getColor(R.color.timePicker_end_cursor));
        mPaintLineWidth = typedArray.getDimension(R.styleable.TimePicker_timePicker_line_width, getResources().getDimension(R.dimen.timePicker_width));
        initPaint();
    }

    public TimePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private void initPaint() {

        mSelectedPaint = new Paint();
        mSelectedPaint.setStyle(Paint.Style.STROKE);
        mSelectedPaint.setColor(mSecletedColor);
        mSelectedPaint.setStrokeWidth(mPaintLineWidth);
        mSelectedPaint.setAntiAlias(true);

        mTextPaint = new Paint();
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(Color.RED);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setStrokeWidth(5);
        mTextPaint.setTextSize(25f);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setFakeBoldText(true);

        mCursorPathPaint = new Paint();
        mCursorPathPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mCursorPathPaint.setColor(mCursorPathColor);
        mCursorPathPaint.setAntiAlias(true);
        mCursorPathPaint.setStrokeWidth(3f);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("TimePicker", " location is " + line_location_margin);
        float x;
        //绘制游标
        drawStartTimeCursor(canvas);

        for (int i = 0; i < 24; i++) {
            //绘制时间点
            if (i == 0) {
                canvas.drawText("0", location_list[1] / 2, textMargin + line_base_location, mTextPaint);
            } else {
                x = (location_list[i + 1] - location_list[i]) / 2 + location_list[i];
                canvas.drawText(String.valueOf(i), x, textMargin + line_base_location, mTextPaint);
            }
            //绘制 线
            canvas.drawLine(location_list[i] + margin, line_base_location, location_list[i + 1], line_base_location, mSelectedPaint);
        }
    }

    private void drawStartTimeCursor(Canvas canvas) {
        Path path = new Path();
        path.moveTo(cursor_startX_location, 0);
        path.lineTo(cursor_NextX_location, 0);
        path.lineTo(cursor_halfX_location, line_base_location / 2);
        path.lineTo(cursor_startX_location, 0);
        path.close();
        canvas.drawPath(path, mCursorPathPaint);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    public void setTimePickerListener(TimePickerListener listener) {
        this.timePickerListener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                cursor_startX_location = x;
                Log.d("TimePicker", " x is " + x + " location_list[22] " + location_list[23]);
                if (x > location_list[23]) {
                    cursor_startX_location = location_list[23];
                }
                cursor_NextX_location = cursor_startX_location + line_location_margin;
                cursor_halfX_location = (cursor_NextX_location - cursor_startX_location) / 2 + cursor_startX_location;
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                for (int i = 0; i < location_list.length; i++) {
                    if (location_list[i] < cursor_halfX_location && cursor_halfX_location < (location_list[i] + line_location_margin)) {
                        if (now == -1) {
                            timePickerListener.onHourSelect(i);
                        }
                        if (i < now) {
                            ToastUtils.show("会议开始时间不可以比现在还早!");
                            cursor_startX_location = location_list[now];
                            cursor_NextX_location = location_list[now + 1];
                            cursor_halfX_location = (cursor_NextX_location - cursor_startX_location) / 2 + cursor_startX_location;
                            invalidate();
                            break;
                        }

                        timePickerListener.onHourSelect(i);
                        startTime = i;
                    }
                }
                break;
        }

        return true;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec) - left_padding;
        height = MeasureSpec.getSize(heightMeasureSpec);
        line_location_margin = (float) ((width - margin * 24 - left_padding) / 23);

        LogT.d(" width is " + width);
        location_list = new float[25];
        location_list[0] = left_padding;
        for (int i = 1; i < 25; i++) {
            location_list[i] = line_location_margin * i;
            Log.d("TimePicker", " location list " + location_list[i]);
        }
        //初始化开始时间设置为 8点
        cursor_startX_location = location_list[startTime];
        cursor_NextX_location = location_list[startTime + 1];
        cursor_halfX_location = (cursor_NextX_location - cursor_startX_location) / 2 + cursor_startX_location;

    }

    public void setStartHour(int startTime) {
        this.startTime = startTime;
        timePickerListener.onHourSelect(startTime);
        invalidate();
    }

    public void setmCursorPathColor(int mCursorPathColor) {
        this.mCursorPathColor = mCursorPathColor;
        mCursorPathPaint.setColor(this.mCursorPathColor);
        invalidate();
    }

    public void setNow(int now) {
        this.now = now;
    }
}
