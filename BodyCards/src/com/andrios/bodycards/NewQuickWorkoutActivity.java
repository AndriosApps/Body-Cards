package com.andrios.bodycards;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class NewQuickWorkoutActivity extends Activity  {

	EditText numGuestsTXT;
	ListView availableProfilesLV, chosenProfilesLV;
	Button backBTN, resetBTN, doneBTN;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.newquickworkoutactivity);
		getExtras();
		setConnections();
	}

	private void setConnections() {
		numGuestsTXT = (EditText) findViewById(R.id.newQuickWorkoutActivityNumberGuestsTXT);
		availableProfilesLV = (ListView) findViewById(R.id.newQuickWorkoutActivityAvailableProfilesListView);
		chosenProfilesLV = (ListView) findViewById(R.id.newQuickWorkoutActivityChosenProfilesListView);
		backBTN = (Button) findViewById(R.id.newQuickWorkoutActivityBackBTN);
		resetBTN = (Button) findViewById(R.id.newQuickWorkoutActivityResetBTN);
		doneBTN = (Button) findViewById(R.id.newQuickWorkoutActivityDoneBTN);
		
		setOnClickListeners();
	}

	private void setOnClickListeners() {
		// TODO Auto-generated method stub
		
	}

	private void getExtras() {
		// TODO Auto-generated method stub
		
	}
}
