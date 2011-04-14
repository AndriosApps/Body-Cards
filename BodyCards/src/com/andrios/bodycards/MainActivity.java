package com.andrios.bodycards;

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


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.mainactivity);
		setConnections();
	}

	private void setConnections() {
		Button quickWorkout = (Button) findViewById(R.id.mainActivityQuickWorkoutBTN);
		quickWorkout.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), QuickWorkoutActivity.class);
				startActivity(intent);
			}

		});

		Button newWorkout = (Button) findViewById(R.id.mainActivityNewWorkoutBTN);
		newWorkout.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), ExerciseListActivity.class);
				startActivityForResult(intent, 31415);
			}

		});

		Button viewProfile = (Button) findViewById(R.id.mainActivityViewProfilesBTN);
		viewProfile.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), ViewProfileActivity.class);
				startActivity(intent);
			}

		});

		Button help = (Button) findViewById(R.id.mainActivityHelpBTN);
		help.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), HelpActivity.class);
				startActivity(intent);
			}

		});

		Button quit = (Button) findViewById(R.id.mainActivityQuitBTN);
		quit.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				MainActivity.this.finish();
			}

		});

	}

}