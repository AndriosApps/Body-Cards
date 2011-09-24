package com.andrios.bodycards;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainHelpActivity extends Activity {

	
	Button helpGettingStartedBTN, helpDeckOfCardsBTN, helpRandomWorkoutBTN, rateBTN;
	Button helpCustomWorkoutBTN, DisclaimerBTN, imagesBTN, andriosBTN;
	GoogleAnalyticsTracker tracker;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.mainhelpactivity);
		
		tracker = GoogleAnalyticsTracker.getInstance();

	    // Start the tracker in manual dispatch mode...
	    tracker.start("UA-23366060-1", this);

	    tracker.trackPageView("Main Help Activity");
	    
		setConnections();
	}//onCreate()
	
	
	private void setConnections() {
		helpGettingStartedBTN = (Button) findViewById(R.id.mainHelpActivityGettingStartedBTN);
		helpDeckOfCardsBTN= (Button) findViewById(R.id.mainHelpActivityDeckOfCardsBTN);
		helpRandomWorkoutBTN = (Button) findViewById(R.id.mainHelpActivityRandomWorkoutBTN);
		helpCustomWorkoutBTN = (Button) findViewById(R.id.mainHelpActivityCustomWorkoutBTN);
		DisclaimerBTN = (Button) findViewById(R.id.mainHelpActivityDisclaimerBTN);
		imagesBTN = (Button) findViewById(R.id.mainHelpActivityImagesBTN);
		rateBTN = (Button) findViewById(R.id.mainHelpActivityRatingBTN);
		andriosBTN = (Button) findViewById(R.id.mainHelpActivityAndriosBTN);
		
		
		setOnClickListeners();
		
	}
	private void setOnClickListeners() {
	
		andriosBTN.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				  tracker.trackEvent(
				            "Clicks",  // Category
				            "Button",  // Action
				            "About AndriOS", // Label
				            1);       // Value
				  tracker.dispatch();
				
				Intent intent = new Intent(v.getContext(), AboutActivity.class);
				intent.putExtra("ID", 0);
				startActivity(intent);
				
			}
			
		});
		
		helpGettingStartedBTN.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				  tracker.trackEvent(
				            "Clicks",  // Category
				            "Button",  // Action
				            "Help Getting Started", // Label
				            1);       // Value
				  tracker.dispatch();
				
				Intent intent = new Intent(v.getContext(), HelpGenericActivity.class);
				intent.putExtra("ID", 0);
				startActivity(intent);
				
			}
			
		});
		
		helpDeckOfCardsBTN.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				  tracker.trackEvent(
				            "Clicks",  // Category
				            "Button",  // Action
				            "Help Deck of Cards", // Label
				            1);       // Value
				  tracker.dispatch();
				Intent intent = new Intent(v.getContext(), HelpGenericActivity.class);
				intent.putExtra("ID", 2);
				startActivity(intent);
				
			}
			
		});
		
		helpRandomWorkoutBTN.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				
				  tracker.trackEvent(
				            "Clicks",  // Category
				            "Button",  // Action
				            "Help Random Workout", // Label
				            1);       // Value
				  tracker.dispatch();
				Intent intent = new Intent(v.getContext(), HelpGenericActivity.class);
				intent.putExtra("ID", 5);
				startActivity(intent);
				
			}
			
		});
		
		helpCustomWorkoutBTN.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				  tracker.trackEvent(
				            "Clicks",  // Category
				            "Button",  // Action
				            "Help Custom Workout", // Label
				            1);       // Value
				  tracker.dispatch();
				Intent intent = new Intent(v.getContext(), HelpGenericActivity.class);
				intent.putExtra("ID", 4);
				startActivity(intent);
				
			}
			
		});
		
		DisclaimerBTN.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				  tracker.trackEvent(
				            "Clicks",  // Category
				            "Button",  // Action
				            "Help Disclaimer", // Label
				            1);       // Value
				  tracker.dispatch();
				Intent intent = new Intent(v.getContext(), HelpGenericActivity.class);
				intent.putExtra("ID", 7);
				startActivity(intent);
				
			}
			
		});
		
		imagesBTN.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				  tracker.trackEvent(
				            "Clicks",  // Category
				            "Button",  // Action
				            "Help Image Reference", // Label
				            1);       // Value
				   tracker.dispatch();
				Intent intent = new Intent(v.getContext(), HelpGenericActivity.class);
				intent.putExtra("ID", 8);
				startActivity(intent);
				
			}
			
		});
		
		rateBTN.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				  tracker.trackEvent(
				            "Clicks",  // Category
				            "Button",  // Action
				            "Help Rate", // Label
				            1);       // Value
				   tracker.dispatch();
				   Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse("market://details?id=com.andrios.bodycards"));
					startActivity(intent);
				
			}
			
		});
		
		
	}
	
	  @Override
	  protected void onDestroy() {
	    super.onDestroy();
	    // Stop the tracker when it is no longer needed.
	    tracker.dispatch();
	    tracker.stop();
	  }

	
	
	
	
}
