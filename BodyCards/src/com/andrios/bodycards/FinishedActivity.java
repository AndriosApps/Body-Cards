package com.andrios.bodycards;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class FinishedActivity extends Activity {
	
	boolean hasRated;
	AlertDialog ad;
	ArrayList<Profile> selectedProfiles;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.finished);
		getExtras();
		setConnections();
		
	}

	private void getExtras() {
		hasRated = AndriosPatcher.readRated(FinishedActivity.this);
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, -15);
		
	}

	private void setConnections() {
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
			Intent intent = new Intent();
			
			setResult(RESULT_OK, intent);
		
			FinishedActivity.this.finish();
		}
	}
	

	
	public void onResume(){
		super.onResume();
	}
	
	public void onPause(){
		super.onPause();
	}

	  @Override
	  protected void onDestroy() {
	    super.onDestroy();
	  }
}
