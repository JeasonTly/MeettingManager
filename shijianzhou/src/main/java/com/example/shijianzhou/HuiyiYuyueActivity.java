package com.example.shijianzhou;

import com.example.util.SmartTimePick;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class HuiyiYuyueActivity extends Activity {

	private LinearLayout kuaisuroot;
	private LinearLayout putongroot;
	private Button kuaisu;
	private Button putong;
	private Button yuding;

	private EditText yudingren_kuaisu;
	private SmartTimePick timepick_kuaisu;

	private EditText huiyimingcheng_putong;
	private EditText yudingren_putong;
	private EditText chuxiren_putong;
	private EditText waiburenyuan_putong;
	private EditText beizhu_putong;
	private SmartTimePick timepick_start_putong;
	private SmartTimePick timepick_end_putong;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.huiyiyuyue);
		kuaisuroot = (LinearLayout) findViewById(R.id.kuaisuroot);
		putongroot = (LinearLayout) findViewById(R.id.putongroot);
		showLayout(R.id.kuaisuroot);
		initGear();
		kuaisu = (Button) findViewById(R.id.kuaisu);
		putong = (Button) findViewById(R.id.putong);
		yuding = (Button) findViewById(R.id.yuding);
		kuaisu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showLayout(R.id.kuaisuroot);
			}
		});
		putong.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showLayout(R.id.putongroot);
			}
		});
		yuding.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				if (kuaisuroot.getVisibility() == View.VISIBLE) {
					intent.putExtra("type", "kuaisu");
					intent.putExtra("start", timepick_kuaisu.getStart());
					intent.putExtra("end", timepick_kuaisu.getEnd());
					intent.putExtra("yudingren", yudingren_kuaisu.getText()
							.toString());
				} else if (putongroot.getVisibility() == View.VISIBLE) {
					intent.putExtra("type", "putong");
					intent.putExtra("start", timepick_start_putong.getEnd());
					intent.putExtra("end", timepick_end_putong.getEnd());
					intent.putExtra("huiyimingcheng", huiyimingcheng_putong
							.getText().toString());
					intent.putExtra("yudingren", yudingren_putong.getText()
							.toString());
					intent.putExtra("chuxiren", chuxiren_putong.getText()
							.toString());
					intent.putExtra("waiburen", waiburenyuan_putong.getText()
							.toString());
					intent.putExtra("beizhu", beizhu_putong.getText()
							.toString());
				}
				HuiyiYuyueActivity.this.setResult(0, intent);
				HuiyiYuyueActivity.this.finish();

			}
		});
	}

	/**
	 * 使快速或者普通的布局显示一个。
	 * 
	 * @param id
	 */
	private void showLayout(int id) {
		kuaisuroot.setVisibility(View.GONE);
		putongroot.setVisibility(View.GONE);
		if (id == R.id.kuaisuroot) {
			kuaisuroot.setVisibility(View.VISIBLE);
		} else if (id == R.id.putongroot) {
			putongroot.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 初始化。
	 */
	private void initGear() {
		yudingren_kuaisu = (EditText) findViewById(R.id.yudingren_kuaisu);
		timepick_kuaisu = (SmartTimePick) findViewById(R.id.timepick_kuaisu);

		timepick_start_putong = (SmartTimePick) findViewById(R.id.timepick_start_putong);
		timepick_end_putong = (SmartTimePick) findViewById(R.id.timepick_end_putong);
		huiyimingcheng_putong = (EditText) findViewById(R.id.huiyimingcheng_putong);
		yudingren_putong = (EditText) findViewById(R.id.yudingren_putong);
		chuxiren_putong = (EditText) findViewById(R.id.chuxiren_putong);
		waiburenyuan_putong = (EditText) findViewById(R.id.waiburenyuan_putong);
		beizhu_putong = (EditText) findViewById(R.id.beizhu_putong);
	}
}
