package com.andrios.bodycards;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class MainActivity extends Activity {
	/** Called when the activity is first created. */

	Button deckOfCardsWorkoutBTN, customWorkoutBTN, randomWorkoutBTN;
	Button viewProfileBTN, helpBTN;
	AlertDialog ad;
	boolean showWelcome;
	GoogleAnalyticsTracker tracker;
	AdView adView;
	AdRequest request;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.mainactivity);
		
		
		tracker = GoogleAnalyticsTracker.getInstance();

	    // Start the tracker in manual dispatch mode...
	    tracker.start("UA-23366060-1", this);
		
	    
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
				 tracker.trackEvent(
				            "Use",  // Category
				            "Login",  // Action
				            "First Login / No Profile", // Label
				            0);       // Value
				   tracker.dispatch();
				ad.show();
			}
		} else {
			viewProfileBTN.setText("");
			 tracker.trackEvent(
			            "Use",  // Category
			            "Login",  // Action
			            "Regular Login", // Label
			            0);       // Value
			   tracker.dispatch();
			
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
				   tracker.trackEvent(
				            "Clicks",  // Category
				            "Button",  // Action
				            "Deck of Cards", // Label
				            0);       // Value
				   

				
				Intent intent = new Intent(v.getContext(), NewDeckOfCardsWorkoutActivity.class);
				intent.putExtra("workoutName", "Deck of Cards");
				startActivity(intent);
			}

		});
		
		randomWorkoutBTN.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				   tracker.trackEvent(
				            "Clicks",  // Category
				            "Button",  // Action
				            "Random Workout", // Label
				            0);       // Value
				   
				
				
				Intent randomIntent = new Intent(v.getContext(), NewRandomWorkoutActivity.class);
				randomIntent.putExtra("workoutName", "Random Workout");
				startActivity(randomIntent);
				
			}

		});
		
		customWorkoutBTN.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				   tracker.trackEvent(
				            "Clicks",  // Category
				            "Button",  // Action
				            "Custom Workout", // Label
				            0);       // Value
				
				Intent intent = new Intent(v.getContext(), ExerciseListActivity.class);
				intent.putExtra("workoutName", "Custom Workout");
				startActivityForResult(intent, 31415);
			}

		});
		
		viewProfileBTN.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				   tracker.trackEvent(
				            "Clicks",  // Category
				            "Button",  // Action
				            "View Profiles", // Label
				            0);       // Value
				   
				
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
				   tracker.trackEvent(
				            "Clicks",  // Category
				            "Button",  // Action
				            "Help", // Label
				            0);       // Value
				   
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

	    tracker.trackPageView("Main Activity");
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
	  

		private void welcome() {
			showWelcome = AndriosPatcher.readWelcome(MainActivity.this);
			
		}
		private void patch() {
			
			AndriosPatcher.patch(MainActivity.this);
			
			
		}
}