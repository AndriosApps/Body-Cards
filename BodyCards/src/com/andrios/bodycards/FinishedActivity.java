package com.andrios.bodycards;

import java.util.ArrayList;
import java.util.Calendar;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class FinishedActivity extends Activity {
	
	boolean hasRated;
	AlertDialog ad;
	ArrayList<Profile> selectedProfiles;
	GoogleAnalyticsTracker tracker;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.finished);
		
		tracker = GoogleAnalyticsTracker.getInstance();

	    // Start the tracker in manual dispatch mode...
	    tracker.start("UA-23366060-1", this);
		
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

	    tracker.trackPageView("Finished Activity");
	}
	
	public void onPause(){
		super.onPause();
		tracker.dispatch();
	}

	  @Override
	  protected void onDestroy() {
	    super.onDestroy();
	    // Stop the tracker when it is no longer needed.
	    tracker.stop();
	  }
}
