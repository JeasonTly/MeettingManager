package com.example.shijianzhou;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class KuaisuYudingActivity extends Activity {

	private Button yuding;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kuaisu);
		yuding = (Button) findViewById(R.id.yuding);
		yuding.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				KuaisuYudingActivity.this.finish();
			}
		});
	}

}
