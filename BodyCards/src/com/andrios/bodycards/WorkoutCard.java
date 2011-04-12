package com.andrios.bodycards;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class WorkoutCard extends Activity {

	// XML Variables
	TextView nameView, setView, rTL, rBR, exerciseName, prNm, cardsRem;
	ImageView wkImg;
	Button begin, finish;

	// Profiles
	ArrayList<Profile> unusedProfiles;
	ArrayList<Profile> selectedProfiles;

	// Workout Initialization Variables
	ArrayList<String> exercises;
	int numPeople, numSets, min, max;

	// Workout Card Variables
	boolean running, started;
	int numCards;
	int numProf;

	// Workout Progress Variables
	int reps, // Number of Reps of the exercise
			set, // Which set user is on
			count; // Exercise number
	String exercise; // Exercise name

	// Workouts to add to Profiles
	Workout[] workouts;

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

	}

	@SuppressWarnings("unchecked")
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
		exercises = (ArrayList<String>) intent
				.getSerializableExtra("exercises");
	}

	private void setConnections() {

		setViews();
		numProf = selectedProfiles.size();
		rNum = new Random();
		createWorkouts();
		numCards = numPeople * numSets;
		cardsRem.setText("Cards Remaining: " + numCards);
		started = running = false;
		totalTime = 0; // TIME / TIMER / STOPWATCH / CHRONOMETER STUFF
		reps = count = 0;
		set = 1;

		begin = (Button) findViewById(R.id.wcStart);
		begin.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				if (running) { // TIME / TIMER / STOPWATCH / CHRONOMETER STUFF
					clock.stop();
					pause_time = SystemClock.elapsedRealtime()
							- clock.getBase();
					if (((count - 1) % numPeople) < numProf)
						workouts[(count - 1) % numPeople].start();
					begin.setText("Resume");
					running = false;
				} else { // TIME / TIMER / STOPWATCH / CHRONOMETER STUFF
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
		});

		finish = (Button) findViewById(R.id.wcEnd);
		finish.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				end();

				setResult(RESULT_OK);
				WorkoutCard.this.finish();
			}

		});

		wkImg.setOnClickListener(new myOnClick());
		rTL.setOnClickListener(new myOnClick());
		rBR.setOnClickListener(new myOnClick());
		exerciseName.setOnClickListener(new myOnClick());
		prNm.setOnClickListener(new myOnClick());
	}

	private void end() {

		if (running) {

			clock.stop();
		}

		if (started) {
			if (((count - 1) % numPeople) < numProf) {
				workouts[(count - 1) % numPeople].stop();
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
			if (running) {
				v = vi;
				if (((count - 1) % numPeople) < numProf)
					workouts[(count - 1) % numPeople].stop();
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
		exercise = (exercises.get(Math.abs(rNum.nextInt()) % exercises.size()));

		displayWorkout();

	}

	private void displayWorkout() {
		String name;

		if (count % numPeople < selectedProfiles.size()) {
			name = selectedProfiles.get(count % numPeople).getFirstName();

		} else {
			name = "User " + (count % numPeople + 1);
		}

		if ((count % numPeople) < numProf)
			workouts[count % numPeople].start();

		showCard(name);
	}

	View v;

	private void showCard(String n) {

		if (count < numCards) {
			nameView.setText(n);
			exerciseName.setText(exercise);

			set = (count / numPeople) + 1;

			rTL.setText(Integer.toString(reps));
			rBR.setText(Integer.toString(reps));
			setView.setText("Set: " + set);

			if ((count % numPeople) < numProf)
				workouts[count % numPeople].incrementCount(exercise, reps);

			count++;
			cardsRem.setText("Cards Remaining: " + (numCards - count));

		} else {

			end();

			Intent el_fin = new Intent(v.getContext(), Finished.class);

			startActivity(el_fin);
			setResult(RESULT_OK);
			WorkoutCard.this.finish();
		}
	}

	private void setViews() {
		nameView = (TextView) findViewById(R.id.userName);
		setView = (TextView) findViewById(R.id.setCount);
		clock = (Chronometer) findViewById(R.id.timerView); // TIME / TIMER /
															// STOPWATCH /
															// CHRONOMETER STUFF
		exerciseName = (TextView) findViewById(R.id.exerciseName);
		rTL = (TextView) findViewById(R.id.repsTL);
		rBR = (TextView) findViewById(R.id.repsBR);
		wkImg = (ImageView) findViewById(R.id.wkImg);
		prNm = (TextView) findViewById(R.id.progTitle);
		cardsRem = (TextView) findViewById(R.id.cardsRemaining);
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

			Toast.makeText(WorkoutCard.this, "Error: Writing to file",
					Toast.LENGTH_SHORT).show();
		}
	}

	private void createWorkouts() {
		workouts = new Workout[numProf];

		String[] newAr = new String[exercises.size()];
		for (int l = 0; l < newAr.length; l++) {
			newAr[l] = exercises.get(l);
		}
		for (int i = 0; i < numProf; i++) {
			workouts[i] = new Workout(numPeople, numSets, max, min, newAr);
		}
	}

	// TIME / TIMER / STOPWATCH / CHRONOMETER STUFF ----------- TIME / TIMER /
	// STOPWATCH / CHRONOMETER STUFF ----------- TIME / TIMER / STOPWATCH /
	// CHRONOMETER STUFF //

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

}
