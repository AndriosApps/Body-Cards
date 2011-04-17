package com.andrios.bodycards;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class QuickWorkoutActivity extends Activity {

	int numPeople, currentUser, sets;
	ImageView card;
	TextView userTXT;
	ArrayList<Profile> unusedProfiles, selectedProfiles;
	ArrayList<Exercise> exercises;
	Workout[] workouts;

	int[] cards = { R.drawable.c2, R.drawable.c3, R.drawable.c4, R.drawable.c5,
			R.drawable.c6, R.drawable.c7, R.drawable.c8, R.drawable.c9,
			R.drawable.c10, R.drawable.ca, R.drawable.cj, R.drawable.cq,
			R.drawable.ck, R.drawable.d2, R.drawable.d3, R.drawable.d4,
			R.drawable.d5, R.drawable.d6, R.drawable.d7, R.drawable.d8,
			R.drawable.d9, R.drawable.d10, R.drawable.da, R.drawable.dj,
			R.drawable.dq, R.drawable.dk, R.drawable.s2, R.drawable.s3,
			R.drawable.s4, R.drawable.s5, R.drawable.s6, R.drawable.s7,
			R.drawable.s8, R.drawable.s9, R.drawable.s10, R.drawable.sa,
			R.drawable.sj, R.drawable.sq, R.drawable.sk, R.drawable.h2,
			R.drawable.h3, R.drawable.h4, R.drawable.h5, R.drawable.h6,
			R.drawable.h7, R.drawable.h8, R.drawable.h9, R.drawable.h10,
			R.drawable.ha, R.drawable.hj, R.drawable.hq, R.drawable.hk,
			R.drawable.jb, R.drawable.jr };

	int[] backs = { R.drawable.back, R.drawable.back2, R.drawable.back3,
			R.drawable.back4 };

	int cardNum = 0;

	int cardBack;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.quickworkout);

		getExtras();
		
		chooseBack();
		shuffleDeck();
		setConnections();
		createWorkouts();
	}

	private void createWorkouts() {
		workouts = new Workout[numPeople];

		ArrayList<Exercise> exercises = new ArrayList<Exercise>();
		Exercise quickWorkout = new Exercise("Quick Workout");
		exercises.add(quickWorkout);
		for (int i = 0; i < numPeople; i++) {
			workouts[i] = new Workout(numPeople, 52 / numPeople, 14, 2, exercises);
		}
	}

	@SuppressWarnings("unchecked")
	private void getExtras() {
	
		
		Intent intent = this.getIntent();
		numPeople = intent.getIntExtra("peeps", -1);
	
		selectedProfiles = (ArrayList<Profile>) intent
				.getSerializableExtra("profiles");
		unusedProfiles = (ArrayList<Profile>) intent
				.getSerializableExtra("profilesU");
	}

	private void chooseBack() {
		Random number = new Random();
		cardBack = backs[number.nextInt(backs.length)];
	}

	private void shuffleDeck() {
		for (int i = 0; i < 10; i++) {
			shuffle();
		}
	}

	private void shuffle() {
		Random number = new Random();
		for (int i = 0; i < cards.length; i++) {
			int s = number.nextInt(cards.length);
			int x = cards[s];
			cards[s] = cards[i];
			cards[i] = x;
		}

	}

	private void getNewCard() {

		if(currentUser != -1){
			workouts[currentUser].stop();
			workouts[currentUser].incrementCount("Quick Workout", 1);
			
		}else{
			sets = 1;
			long startTime = SystemClock.elapsedRealtime();
			for(int i = 0; i < workouts.length; i++){
				
				workouts[i].startTotal(startTime);
			}
		}
		card.setImageResource(cardBack);

		dealCard();

		TextView remaining = (TextView) findViewById(R.id.cardCount);
		currentUser++;
		if(currentUser > (numPeople-1)){
			currentUser = 0;
			sets++;
		}
		//Guests turn to do exercise
		if(currentUser >= selectedProfiles.size() ){
			userTXT.setText("Guest User" + (numPeople - currentUser));
			
		}
		//User With Profile's turn to do exercise
		else{
			userTXT.setText(selectedProfiles.get(currentUser).getFirstName());
		}
		workouts[currentUser].setFinSets(sets);
		remaining.setText("Cards Remaining: " + (53 - cardNum));
		workouts[currentUser].start();
	}

	private void dealCard() {
		Timer clock = new Timer();
		clock.schedule(new myTask(), 150);
	}

	public class myTask extends TimerTask {
		final Handler handler = new Handler();

		public void run() {
			handler.post(new Runnable() {
				public void run() {
					// TODO Auto-generated method stub
					card.setImageResource(cards[cardNum++]);
				}

			});
		};
	}

	private void setConnections() {
		
		userTXT = (TextView) findViewById(R.id.quickWorkoutActivityUserNameTXT);
		
		card = (ImageView) findViewById(R.id.card);
		
		currentUser = -1;
		setOnClickListeners();
		
	}

	private void setOnClickListeners() {
		card.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (cardNum < cards.length) {
					getNewCard();
				} else {
					workouts[currentUser].stop();
					workouts[currentUser].incrementCount("Quick Workout", 1);
					long endTime = SystemClock.elapsedRealtime();
					for(int i = 0; i < workouts.length; i++){
						
						workouts[i].stopTotal(endTime);
						workouts[i].setWorkoutTime(workouts[i].getTotalFormattedTime());
					}
					setWorkoutsToProfile();
					Intent el_fin = new Intent(v.getContext(), FinishedActivity.class);
					
					startActivity(el_fin);
					QuickWorkoutActivity.this.finish();

				}
			}

		});
		
	}
	
	public void setWorkoutsToProfile() {
		ArrayList<Profile> profiles = new ArrayList<Profile>();
		for (int k = 0; k < selectedProfiles.size(); k++) {

			selectedProfiles.get(k).addWorkout(workouts[k]);
			profiles.add(selectedProfiles.get(k));
		}
		for (int l = 0; l < unusedProfiles.size(); l++) {
			profiles.add(unusedProfiles.get(l));
		}

		try {
			FileOutputStream fos = openFileOutput("profiles",
					Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			oos.writeObject(profiles);

			oos.close();
			fos.close();
		} catch (IOException e) {

			Toast.makeText(QuickWorkoutActivity.this, "Error: Writing to file",
					Toast.LENGTH_SHORT).show();
		}
	}
	
	// TIME / TIMER / STOPWATCH / CHRONOMETER STUFF ----------- TIME / TIMER /
	// STOPWATCH / CHRONOMETER STUFF ----------- TIME / TIMER / STOPWATCH /
	// CHRONOMETER STUFF //

	/*
	Chronometer clock;
	int totalTime;
	private long pause_time = 0;

	public void updateTimer() {

		int time = totalTime;
		int hours = time / (60 * 60);
		time = time - (hours * 60 * 60);
		int mins = time / 60;
		int secs = time - (mins * 60);

		String tStr = "";
		tStr += (hours > 9) ? (hours + ":") : ("0" + hours + ":");
		tStr += (mins > 9) ? (mins + ":") : ("0" + mins + ":");
		tStr += (secs > 9) ? (secs + ":") : ("0" + secs);

		clock.setText(tStr);
	}

	protected void addTimeToWorkout() {

		for (int i = 0; i < workouts.length; i++) {
			workouts[i].setWorkoutTime(clock.getText().toString());
		}
	}
	*/
}
