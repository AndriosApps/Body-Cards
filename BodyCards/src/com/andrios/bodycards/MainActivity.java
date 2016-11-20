package com.andrios.bodycards;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class MainActivity extends Activity {
	/** Called when the activity is first created. */

	Button deckOfCardsWorkoutBTN, customWorkoutBTN, randomWorkoutBTN;
	Button viewProfileBTN, helpBTN;
	AlertDialog ad;
	boolean showWelcome;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.mainactivity);

	    
	    patch();
	    welcome();
		setConnections();
		setAlertDialog();
		
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
			viewProfileBTN.setText("+");
			if(showWelcome){
				ad.show();
			}
		} else {
			viewProfileBTN.setText("");
		}
		
	}

	private void setConnections() {
		deckOfCardsWorkoutBTN = (Button) findViewById(R.id.mainActivityDeckOfCardsWorkoutBTN);
		customWorkoutBTN = (Button) findViewById(R.id.mainActivityCustomWorkoutBTN);
		randomWorkoutBTN = (Button) findViewById(R.id.mainActivityRandomWorkoutBTN);
		viewProfileBTN = (Button) findViewById(R.id.mainActivityViewProfilesBTN);
		helpBTN = (Button) findViewById(R.id.mainActivityHelpBTN);
		

		//adView = (AdView)this.findViewById(R.id.homeAdView2);
	      
	    //request = new AdRequest();
		//request.setTesting(false);
		//adView.loadAd(request);
		
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
				if(viewProfileBTN.getText().equals("+")){
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
		

		
	}

	private void setAlertDialog() {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = LayoutInflater.from(this);
		final View layout = inflater.inflate(R.layout.welcomealertdialog, null);
		final CheckBox welcomeCheck = (CheckBox) layout.findViewById(R.id.welcomeAlertDialogCheckBox);
		
		builder.setView(layout)
				.setTitle("Welcome!")
				.setPositiveButton("OK", new DialogInterface.OnClickListener(){
					
					
					public void onClick(DialogInterface dialog, int which) {
							
							if(welcomeCheck.isChecked()){
								showWelcome = false;
								AndriosPatcher.setWelcome(MainActivity.this);
							}
							int whichOne = 1;
							
							Intent intent = new Intent(MainActivity.this.getBaseContext(), ViewProfileActivity.class);
							intent.putExtra("whichOne", whichOne);
							startActivity(intent);
						
						
					}
				})
				.setNegativeButton("No Thanks", new DialogInterface.OnClickListener(){

					public void onClick(DialogInterface dialog, int which) {
						if(welcomeCheck.isChecked()){
							showWelcome = false;
							AndriosPatcher.setWelcome(MainActivity.this);
						}
						
					}
					
				});
		ad = builder.create();
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
	  

		private void welcome() {
			showWelcome = AndriosPatcher.readWelcome(MainActivity.this);
			
		}
		private void patch() {
			AndriosPatcher.patch(MainActivity.this);
			
		}
}