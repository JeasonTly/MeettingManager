package com.example.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;

/**
 * 
 * TODO 原先的smartbutton对9patch支持不好，做了这个新的。如果背景是9patch推荐使用这个。
 * 
 * @author ZhouNanzhao
 * @data: 2014-2-23 下午2:00:24
 * @version: V1.0
 */
public class SmartButton9 extends Button {
	private Bitmap mHighlightBitmap = null;
	private Drawable mDefaultBackgroud = null;
	private OnClickListener myOnClick;

	public void setMyOnClick(OnClickListener myOnClick) {
		this.myOnClick = myOnClick;
	}

	public SmartButton9(Context context) {
		super(context);

	}

	public SmartButton9(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public SmartButton9(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	public void reset() {
		mDefaultBackgroud = null;
		mHighlightBitmap = null;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// init members
		if (mDefaultBackgroud == null) {
			mDefaultBackgroud = getBackground();
		}

		// generate gray mask bitmap
		if (mHighlightBitmap == null) {
			int w = getWidth();
			int h = getHeight();
			// create bitmap from current view
			Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(bm);
			layout(0, 0, w, h);
			draw(canvas);
			// get pixels from bm
			int pixels[] = new int[w * h];
			bm.getPixels(pixels, 0, w, 0, 0, w, h);
			for (int i = 0; i != pixels.length; ++i) {
				pixels[i] = addGrayMask(pixels[i]);
			}
			// create highlight bitmap
			mHighlightBitmap = Bitmap.createBitmap(pixels, w, h,
					Bitmap.Config.ARGB_8888);
			// FIXME: when setBg by highlight bitmap, it will change the size of
			// button.
			setWidth(w);
			setHeight(h);
		}
		switch (event.getAction()) {
		// press status
		case MotionEvent.ACTION_DOWN:
			setBackgroundDrawable(new BitmapDrawable(mHighlightBitmap));
			break;
		case MotionEvent.ACTION_MOVE:
			setBackgroundDrawable(new BitmapDrawable(mHighlightBitmap));
			break;
		case MotionEvent.ACTION_UP:
			if (myOnClick != null) {
				myOnClick.onClick(this);
			}
			setBackgroundDrawable(mDefaultBackgroud);
			break;

		default:
			setBackgroundDrawable(mDefaultBackgroud);
			break;
		}

		return super.onTouchEvent(event);
	}

	private int addGrayMask(int color) {
		int b = color & 0xff;
		int g = (color & 0xff00) >> 8;
		int r = (color & 0xff0000) >> 16;
		int a = color >> 24;
		int gray = 50;
		float radio = 0.5f;
		b = hold(b * radio + gray);
		g = hold(g * radio + gray);
		r = hold(r * radio + gray);
		return (a << 24) | (r << 16) | (g << 8) | b;
	}

	private int hold(float v) {
		int r = (int) v;
		return r > 255 ? 255 : r;
	}
}
