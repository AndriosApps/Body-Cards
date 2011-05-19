package com.andrios.bodycards;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class StartDeckActivity extends Activity {
	ImageView startImage;
	int numPeople, numSets, min, max;
	
	ArrayList<Profile> unusedProfiles;
	ArrayList<Profile> selectedProfiles;
	
	ArrayList<Exercise> exercises;
	
	String workoutName;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// Remove status and title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// Set layout
		setContentView(R.layout.startscreen);

		// Retrieve info from calling class
		getExtras();

		// Set the connections for this class]
		setConnections();

	}
	
	public void onActivityResult(int requestCode, int returnCode, Intent intent) {
		if (returnCode == RESULT_OK) {
			setResult(RESULT_OK);
			StartDeckActivity.this.finish();
		}
	}


	private void setConnections() {
		startImage = (ImageView) findViewById(R.id.startButton);
		startImage.setOnClickListener(new OnClickListener (){

			public void onClick(View v) {
				setResult(RESULT_OK);
				Intent wkout = new Intent(v.getContext(),
						WorkoutCardActivity.class);

				wkout.putExtra("max", max);
				wkout.putExtra("min", min);
				wkout.putExtra("sets", numSets);
				wkout.putExtra("peeps", numPeople);
				wkout.putExtra("profilesU", unusedProfiles);
				wkout.putExtra("profiles", selectedProfiles);
				wkout.putExtra("exercises", exercises);
				wkout.putExtra("workoutName", workoutName);
				startActivityForResult(wkout, 31415);
				
				
			}
			
		});
		
	}


	private void getExtras() {
		Intent intent = this.getIntent();
		numPeople = intent.getIntExtra("peeps", -1);
		numSets = intent.getIntExtra("sets", -1);
		min = intent.getIntExtra("min", -1);
		max = intent.getIntExtra("max", -1);
		
		selectedProfiles = (ArrayList<Profile>) intent
				.getSerializableExtra("profiles");
		unusedProfiles = (ArrayList<Profile>) intent
				.getSerializableExtra("profilesU");
		exercises = (ArrayList<Exercise>) intent
				.getSerializableExtra("exercises");
		workoutName = intent.getStringExtra("workoutName");
		
	}

}
