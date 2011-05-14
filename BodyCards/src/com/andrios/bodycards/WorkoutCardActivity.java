package com.andrios.bodycards;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WorkoutCardActivity extends Activity {

	private static final String MY_AD_UNIT_ID = "a14dca3fe4ea15f";
	// XML Variables
	TextView setView, rTL, rBR, exerciseName, cardsRem;
	TextView sideLabel;
	ImageView wkImg;
	Button begin, finish;
	AlertDialog ad;

	AdView adView;
	AdRequest request;
	// Profiles
	ArrayList<Profile> unusedProfiles;
	ArrayList<Profile> selectedProfiles;
	CountDownTimer alertTime;
	
	// Workout Initialization Variables
	ArrayList<Exercise> exercises;
	int numPeople, numSets, min, max;
	LinearLayout workoutCardAdLayout;

	// Workout Card Variables
	boolean running, started;
	int numCards;
	int numProf;
	long milisLeft, baseAdTime;
	// Workout Progress Variables
	int reps, 	// Number of Reps of the exercise
		set, 	// Which set user is on
		count; 	// Exercise number
	Exercise exercise; // Exercise name

	// Workouts to add to Profiles
	Workout[] workouts;
	String workoutName;
	
	View v;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Remove status and title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// Set layout
		setContentView(R.layout.workoutcard);

		// Retrieve info from calling class
		getExtras();

		// Set the connections for this class]
		setConnections();
		toggleRunning();

	}

	@SuppressWarnings("unchecked")
	private void getExtras() {
		Intent intent = this.getIntent();
		numPeople = intent.getIntExtra("peeps", -1);
		
		numSets = intent.getIntExtra("sets", -1);
		min = intent.getIntExtra("min", -1);
		max = intent.getIntExtra("max", -1);
		selectedProfiles = (ArrayList<Profile>) intent.getSerializableExtra("profiles");
		unusedProfiles = (ArrayList<Profile>) intent
				.getSerializableExtra("profilesU");
		exercises = (ArrayList<Exercise>) intent
				.getSerializableExtra("exercises");
		workoutName = intent.getStringExtra("workoutName");
		System.out.println("PEOPLE"+numPeople);
		System.out.println("SETS"+numSets);
		System.out.println("MIN"+min);

		System.out.println("MAX"+max);

		System.out.println("Exercise"+exercises.get(0).getName());
	}

	private void setConnections() {

		setViews();
		System.out.println("WORKOUT CARD PROFILE 0 NAME:" + selectedProfiles.get(0).getFirstName());
		numProf = selectedProfiles.size();
		rNum = new Random();
		createWorkouts();
		numCards = numPeople * numSets;
		cardsRem.setText("Cards Remaining: " + numCards);
		started = running = false;
		totalTime = 0;
		reps = count = 0;
		set = 1;

		workoutCardAdLayout = (LinearLayout) findViewById(R.id.workoutCardAdLayout);
		
		
	    // Add the adView to it
	    adView = (AdView)this.findViewById(R.id.workoutCardAdView);
	      
	    // Initiate a generic request to load it with an ad
	    baseAdTime = SystemClock.elapsedRealtime();
	    request = new AdRequest();
		request.setTesting(true);
		adView.loadAd(request);

		begin = (Button) findViewById(R.id.wcStart);
		begin.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

			toggleRunning();

			}
		});

		finish = (Button) findViewById(R.id.wcEnd);
		finish.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				end();

				setResult(RESULT_OK);
				WorkoutCardActivity.this.finish();
			}

		});

		wkImg.setOnClickListener(new myOnClick());
		rTL.setOnClickListener(new myOnClick());
		rBR.setOnClickListener(new myOnClick());
		exerciseName.setOnClickListener(new myOnClick());
		sideLabel.setOnClickListener(new myOnClick());
	}

	private void updateAds(){
		Long thisTime = SystemClock.elapsedRealtime();
		if((thisTime-baseAdTime)/1000 > 30){
			baseAdTime = SystemClock.elapsedRealtime();
		    // Initiate a generic request to load it with an ad
			request = new AdRequest();
			request.setTesting(true);
			adView.loadAd(request);
		}

	}
	
	
	
	private void end() {

		if (running) {

			clock.stop();
		}

		if (started) {
			if(!exercise.isTimed){
				if (((count - 1) % numPeople) < numProf) {
					workouts[(count - 1) % numPeople].stop();
				}
			}
			for (int i = 0; i < workouts.length; i++) {
				workouts[i].setFinSets(set);
			}
			addTimeToWorkout();
			setWorkoutsToProfile();

		}

	}

	private class myOnClick implements OnClickListener {

		public void onClick(View vi) {
			updateAds();
			if (running) {
				v = vi;
				if(!exercise.isTimed){
					if (((count - 1) % numPeople) < numProf)
						workouts[(count - 1) % numPeople].stop();
				}
				
				getRandomWorkoutCard();
				

			}
		}

	}

	Random rNum;

	private void getRandomWorkoutCard() {
		if (max == min) {
			reps = max;

		} else {
			int modulo = max - min;

			int num = Math.abs(rNum.nextInt());
			reps = min + (num % modulo);
		}
		int randomNumber = Math.abs(rNum.nextInt()) % exercises.size();
		exercise = (exercises.get(randomNumber));
			reps = (int) (reps*exercises.get(randomNumber).getMultiplier());
		displayWorkout();

	}

	private void displayWorkout() {
		String name;

		if (count % numPeople < selectedProfiles.size()) {
			name = selectedProfiles.get(count % numPeople).getFirstName();

		} else {
			name = "User " + (count % numPeople + 1);
		}
		if(!exercise.isTimed){
			if ((count % numPeople) < numProf)
				workouts[count % numPeople].start();
		}
		

		showCard(name);
	}

	private void showCard(String n) {

		if (count < numCards) {
			sideLabel.setText(n);
			exerciseName.setText(exercise.getName());

			set = (count / numPeople) + 1;


			
			setView.setText("Set: " + set);

			if ((count % numPeople) < numProf)
				workouts[count % numPeople].incrementCount(exercise.getName(), reps);

			count++;
			cardsRem.setText("Cards Remaining: " + (numCards - count));
			if(exercise.isTimed){
				rTL.setText("T");
				rBR.setText("T");
				setAlertDialog();
				ad.show();
			}else{
				rTL.setText(Integer.toString(reps));
				rBR.setText(Integer.toString(reps));
			}

		} else {

			end();

			Intent el_fin = new Intent(v.getContext(), FinishedActivity.class);

			startActivity(el_fin);
			setResult(RESULT_OK);
			WorkoutCardActivity.this.finish();
		}
	}

	private void setViews() {
		setView = (TextView) findViewById(R.id.setCount);
		clock = (Chronometer) findViewById(R.id.timerView);
		exerciseName = (TextView) findViewById(R.id.exerciseName);
		rTL = (TextView) findViewById(R.id.repsTL);
		rBR = (TextView) findViewById(R.id.repsBR);
		wkImg = (ImageView) findViewById(R.id.wkImg);
		cardsRem = (TextView) findViewById(R.id.cardsRemaining);
		sideLabel = (TextView) findViewById(R.id.sideLabel);

	}

	public void setWorkoutsToProfile() {
		ArrayList<Profile> profiles = new ArrayList<Profile>();
		for (int k = 0; k < numProf; k++) {

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

			Toast.makeText(WorkoutCardActivity.this, "Error: Writing to file",
					Toast.LENGTH_SHORT).show();
		}
	}

	private void createWorkouts() {
		workouts = new Workout[numProf];


		for (int i = 0; i < numProf; i++) {
			workouts[i] = new Workout(numPeople, numSets, max, min, exercises, workoutName);
		}
	}

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
	public void formatTimer(){
		TextView alertExerciseTimeLBL = (TextView) findViewById(R.id.exerciseTimerAlertExerciseTimeLBL);
		
	
	}
	private void setAlertDialog() {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = LayoutInflater.from(this);
		final View layout = inflater.inflate(R.layout.exercisetimeralert, null);
		TextView alertExerciseNameLBL = (TextView) layout.findViewById(R.id.exerciseTimerAlertExerciseNameLBL);
		
		alertExerciseNameLBL.setText("");
		final Button timerAlertStartBTN = (Button) layout.findViewById(R.id.timerAlertStartBTN);
		milisLeft = reps*1000;
		//TODO Make this formatTimer() function
		
		int time = (int) (milisLeft / 1000);
		int hours = time / (60 * 60);
		time = time - (hours * 60 * 60);
		int mins = time / 60;
		int secs = time - (mins * 60);
		

		String tStr = "";
		tStr += (hours > 9) ? (hours + ":") : ("0" + hours + ":");
		tStr += (mins > 9) ? (mins + ":") : ("0" + mins + ":");
		tStr += (secs > 9) ? (secs) : ("0" + secs);

		
		TextView alertExerciseTimeLBL = (TextView) layout.findViewById(R.id.exerciseTimerAlertExerciseTimeLBL);
		
    	 alertExerciseTimeLBL.setText(tStr);
    	 
		timerAlertStartBTN.setOnClickListener(new OnClickListener(){
			

			public void onClick(View v) {
				
				if(timerAlertStartBTN.getText().equals("Start")){
					timerAlertStartBTN.setText("Pause");
					
					 alertTime = new CountDownTimer(milisLeft, 1000) {

					     public void onTick(long millisUntilFinished) {
					    	milisLeft = millisUntilFinished;
					    	//TODO Make this formatTimer() function
							
							int time = (int) (milisLeft / 1000);
							int hours = time / (60 * 60);
							time = time - (hours * 60 * 60);
							int mins = time / 60;
							int secs = time - (mins * 60);
							

							String tStr = "";
							tStr += (hours > 9) ? (hours + ":") : ("0" + hours + ":");
							tStr += (mins > 9) ? (mins + ":") : ("0" + mins + ":");
							tStr += (secs > 9) ? (secs) : ("0" + secs);

							TextView alertExerciseTimeLBL = (TextView) layout.findViewById(R.id.exerciseTimerAlertExerciseTimeLBL);
							
					    	 alertExerciseTimeLBL.setText(tStr);
					    	
					     }

					     public void onFinish() {
					    	 TextView alertExerciseTimeLBL = (TextView) layout.findViewById(R.id.exerciseTimerAlertExerciseTimeLBL);
					 		
					    	 alertExerciseTimeLBL.setText("done!");
					    	 timerAlertStartBTN.setText("Finish");
					     }
					  }.start();
				}else if(timerAlertStartBTN.getText().equals("Finish")){
					if (((count - 1) % numPeople) < numProf)
						workouts[(count - 1) % numPeople].addTime(reps);
					ad.dismiss();
				}else if(timerAlertStartBTN.getText().equals("Pause")){
					alertTime.cancel();
					
					//TODO Make this formatTimer() function
					
					int time = (int) (milisLeft / 1000);
					int hours = time / (60 * 60);
					time = time - (hours * 60 * 60);
					int mins = time / 60;
					int secs = time - (mins * 60);
					

					String tStr = "";
					tStr += (hours > 9) ? (hours + ":") : ("0" + hours + ":");
					tStr += (mins > 9) ? (mins + ":") : ("0" + mins + ":");
					tStr += (secs > 9) ? (secs) : ("0" + secs);

					
					TextView alertExerciseTimeLBL = (TextView) layout.findViewById(R.id.exerciseTimerAlertExerciseTimeLBL);
					
			    	 alertExerciseTimeLBL.setText(tStr);
					
					timerAlertStartBTN.setText("Start");
				}
				
				
			}
			
		});
		builder.setView(layout)
				.setTitle(exercise.getName());
		ad = builder.create();
	}
	
	private void toggleRunning(){
		if (running) {
			clock.stop();
			pause_time = SystemClock.elapsedRealtime()
					- clock.getBase();
			if (((count - 1) % numPeople) < numProf)
				workouts[(count - 1) % numPeople].start();
			begin.setText("Resume");
			running = false;
		} else {
			if (started) {
				clock.setBase(SystemClock.elapsedRealtime()
						- pause_time);
				if (((count - 1) % numPeople) < numProf)
					workouts[(count - 1) % numPeople].stop();
				clock.start();
			} else {
				clock.setBase(SystemClock.elapsedRealtime());
				clock.start();
				getRandomWorkoutCard();
				started = true;
			}

			begin.setText("Pause");
			running = true;
		}
	}

}
