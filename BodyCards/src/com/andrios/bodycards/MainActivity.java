package com.andrios.bodycards;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class MainActivity extends Activity {
	/** Called when the activity is first created. */

	Button deckOfCardsWorkoutBTN, customWorkoutBTN, randomWorkoutBTN;
	Button viewProfileBTN, helpBTN, quitBTN;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.mainactivity);
		
		
		setConnections();
		
	}

	public void onStart(){
		super.onStart();
		checkForProfiles();
	}
	
	@SuppressWarnings("unchecked")
	private void checkForProfiles() {
		ArrayList<Profile> profileList;
		try {
			FileInputStream fis = openFileInput("profiles");
			ObjectInputStream ois = new ObjectInputStream(fis);

			profileList = (ArrayList<Profile>) ois.readObject();
			ois.close();
			fis.close();
			
		} catch (Exception e) {
			profileList = new ArrayList<Profile>();
			
		}
		
		if(profileList.isEmpty()) {
			viewProfileBTN.setText("Create Profile");
		} else {
			viewProfileBTN.setText("View Profiles");
		}
		
	}

	private void setConnections() {
		deckOfCardsWorkoutBTN = (Button) findViewById(R.id.mainActivityDeckOfCardsWorkoutBTN);
		customWorkoutBTN = (Button) findViewById(R.id.mainActivityCustomWorkoutBTN);
		randomWorkoutBTN = (Button) findViewById(R.id.mainActivityRandomWorkoutBTN);
		viewProfileBTN = (Button) findViewById(R.id.mainActivityViewProfilesBTN);
		helpBTN = (Button) findViewById(R.id.mainActivityHelpBTN);
		quitBTN = (Button) findViewById(R.id.mainActivityQuitBTN);

		
		setOnClickListeners();
	}

	private void setOnClickListeners() {
		deckOfCardsWorkoutBTN.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), NewDeckOfCardsWorkoutActivity.class);
				intent.putExtra("workoutName", "Deck of Cards");
				startActivity(intent);
			}

		});
		
		randomWorkoutBTN.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				
				Intent randomIntent = new Intent(v.getContext(), NewRandomWorkoutActivity.class);
				randomIntent.putExtra("workoutName", "Random Workout");
				startActivity(randomIntent);
				
			}

		});
		
		customWorkoutBTN.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), ExerciseListActivity.class);
				intent.putExtra("workoutName", "Custom Workout");
				startActivityForResult(intent, 31415);
			}

		});
		
		viewProfileBTN.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				int whichOne = 0;
				if(viewProfileBTN.getText().equals("Create Profile")){
					whichOne = 1;
				}
				Intent intent = new Intent(v.getContext(), ViewProfileActivity.class);
				intent.putExtra("whichOne", whichOne);
				startActivity(intent);
			}

		});
		
		helpBTN.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), MainHelpActivity.class);
				startActivity(intent);
			}

		});
		
		quitBTN.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				MainActivity.this.finish();
			}

		});
		
	}

}