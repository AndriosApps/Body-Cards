package com.andrios.bodycards;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class HelpActivity extends Activity {

	Button helpBTN;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.helpactivity);
		
		readHelp();
		getExtras();
		setConnections();
	}//onCreate()

	private void getExtras() {
		// TODO Auto-generated method stub
		
	}

	private void readHelp() {
		// TODO Auto-generated method stub
		
	}

	private void setConnections() {

		helpBTN = (Button) findViewById(R.id.helpActivityBackBTN);
		
		setOnClickListeners();

	}//setConnections()

	private void setOnClickListeners() {
		
		helpBTN.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				HelpActivity.this.finish();
			}
		});
	}//setOnClickListeners()
	
	
}//HelpActivity
