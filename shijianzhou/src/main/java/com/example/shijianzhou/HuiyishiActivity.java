package com.example.shijianzhou;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HuiyishiActivity extends Activity {

	private Button huiyishiqueding;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.huiyishi);
		huiyishiqueding = (Button) findViewById(R.id.huiyishiqueding);
		huiyishiqueding.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				HuiyishiActivity.this.finish();
			}
		});
	}
}
