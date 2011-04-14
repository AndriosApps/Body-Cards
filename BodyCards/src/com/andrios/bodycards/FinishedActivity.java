package com.andrios.bodycards;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class FinishedActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.finished);
		setConnections();
	}

	private void setConnections() {
		// TODO Auto-generated method stub
		myListener l = new myListener();
		TextView tv = (TextView) findViewById(R.id.finishText);
		tv.setOnClickListener(l);

		ImageView iv = (ImageView) findViewById(R.id.finishPic);
		iv.setOnClickListener(l);

		TextView cong = (TextView) findViewById(R.id.congrats);
		cong.setOnClickListener(l);

		TextView comp = (TextView) findViewById(R.id.complete);
		comp.setOnClickListener(l);
	}

	class myListener implements OnClickListener {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			FinishedActivity.this.finish();
		}
	}
}
