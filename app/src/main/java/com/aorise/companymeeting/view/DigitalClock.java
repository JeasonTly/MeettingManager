package com.aorise.companymeeting.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;

import com.aorise.companymeeting.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Tuliyuan.
 * Date: 2019/5/24.
 */
public class DigitalClock extends android.support.v7.widget.AppCompatTextView {

    private String data_format = "yyyy年MM月dd日 hh:mm:ss";
    private SimpleDateFormat simpleDateFormat;
    private Handler mHandler;
    private Runnable mTime;

    private float text_size = 13f;
    private int text_color = Color.RED;
    public DigitalClock(Context context) {
        super(context);
    }

    public DigitalClock(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                    R.styleable.DigitalClock);
        text_size =  typedArray.getDimension(R.styleable.DigitalClock_digital_clock_text_size ,context.getResources().getDimension(R.dimen.digital_text_size));
        text_color = typedArray.getColor(R.styleable.DigitalClock_digital_clock_text_color,context.getColor(R.color.colorPrimary));
        init();
    }

    public DigitalClock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        simpleDateFormat = new SimpleDateFormat(data_format);
        mHandler = new Handler();
        setTextColor(text_color);
        setTextSize(text_size);
        mTime = new Runnable() {
            @Override
            public void run() {
                String Time = simpleDateFormat.format(new Date());
                Log.d("DigitalClock", " Time is " + Time);

                setText(Time);
                invalidate();
                //long currentTime = System.currentTimeMillis();
                mHandler.postDelayed(mTime, 1000);
            }
        };
        mTime.run();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    /**
     * 设置日期格式 默认 2019年5月24日 10时53分06秒
     * @param data_format
     */
    public void setData_format(String data_format) {
        this.data_format = data_format;
        init();
    }
}
