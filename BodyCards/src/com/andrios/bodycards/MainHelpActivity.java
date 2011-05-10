package com.andrios.bodycards;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainHelpActivity extends Activity {

	
	Button helpGettingStartedBTN, helpDeckOfCardsBTN, helpRandomWorkoutBTN, helpCustomWorkoutBTN;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.mainhelpactivity);
		setConnections();
	}//onCreate()
	
	
	private void setConnections() {
		helpGettingStartedBTN = (Button) findViewById(R.id.mainHelpActivityGettingStartedBTN);
		helpDeckOfCardsBTN= (Button) findViewById(R.id.mainHelpActivityDeckOfCardsBTN);
		helpRandomWorkoutBTN = (Button) findViewById(R.id.mainHelpActivityRandomWorkoutBTN);
		helpCustomWorkoutBTN = (Button) findViewById(R.id.mainHelpActivityCustomWorkoutBTN);
		
		setOnClickListeners();
		
	}
	private void setOnClickListeners() {
	
		
		helpGettingStartedBTN.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), HelpGenericActivity.class);
				intent.putExtra("ID", 0);
				startActivity(intent);
				
			}
			
		});
		
		helpDeckOfCardsBTN.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), HelpGenericActivity.class);
				intent.putExtra("ID", 2);
				startActivity(intent);
				
			}
			
		});
		
		helpRandomWorkoutBTN.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), HelpGenericActivity.class);
				intent.putExtra("ID", 5);
				startActivity(intent);
				
			}
			
		});
		
		helpCustomWorkoutBTN.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), HelpGenericActivity.class);
				intent.putExtra("ID", 4);
				startActivity(intent);
				
			}
			
		});
		
		
	}
	
	
	
	
	
	
	
}
