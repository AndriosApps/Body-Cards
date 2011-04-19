package com.andrios.bodycards;

import java.util.ArrayList;
import java.util.Calendar;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class DisplayWorkoutActivity extends Activity {
	String[] months = { "January", "February", "March", "April", "May", "June",
			"July", "August", "September", "October", "November", "December" };
	String[] days = { "", "Sunday", "Monday", "Tuesday", "Wednesday",
			"Thursday", "Friday", "Saturday" };
	Profile profile;
	int row;
	Button back;
	Workout w;

	@Override
	public void onCreate(Bundle savedInstanceBundle) {
		super.onCreate(savedInstanceBundle);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.displayworkout);

		getExtras();
		setConnections();
		setWokoutDetails();
	}

	private void setWokoutDetails() {
		TextView dt = (TextView) findViewById(R.id.workoutDate);
		TextView t = (TextView) findViewById(R.id.workoutDuration);
		TextView pt = (TextView) findViewById(R.id.workoutPersonalDuration);
		TextView setView = (TextView) findViewById(R.id.workoutSets);
		TextView peepView = (TextView) findViewById(R.id.workoutPeople);

		peepView.setText("  " + Integer.toString(w.getNumPeople()));

		setView.setText("  " + w.getFinSets() + " of " + w.getNumSets());

		ListView ce = (ListView) findViewById(R.id.exerciseLV);
		ArrayAdapter<CompletedExercises> cea = new ArrayAdapter<CompletedExercises>(
				this, R.layout.list_view2, w.exercises);
		ce.setAdapter(cea);

		t.setText("  " + w.getWorkoutTime());

		Calendar c = w.getDate();
		dt.setText("  " + days[c.get(Calendar.DAY_OF_WEEK)] + ", "
				+ months[c.get(Calendar.MONTH)] + " "
				+ c.get(Calendar.DAY_OF_MONTH) + ", " + c.get(Calendar.YEAR));
		pt.setText(w.getFormattedTime());

	}

	private void setConnections() {
		setProfileText();
		w = profile.getWorkouts().get(row);
		back = (Button) findViewById(R.id.dwBack);
		back.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				DisplayWorkoutActivity.this.finish();
			}

		});
	}

	private void getExtras() {
		Intent intent = this.getIntent();
		profile = (Profile) intent.getSerializableExtra("profile");
		row = intent.getIntExtra("row", -1);
	}

	private void setProfileText() {
		TextView name = (TextView) findViewById(R.id.dprofName);
		name.setText(profile.toString());

		TextView age = (TextView) findViewById(R.id.dprofAge);
		age.setText("Age: " + Integer.toString(profile.getAge()));

		TextView numworkouts = (TextView) findViewById(R.id.dprofWorkouts);
		numworkouts.setText("Total Workouts: "
				+ Integer.toString(profile.getNumWorkouts()));

		TextView create = (TextView) findViewById(R.id.dprofCreation);
		create.setText("Profile Created on " + profile.getCreationDate());

		ImageView iv = (ImageView) findViewById(R.id.dprofImg);

		ArrayList<Workout> w = profile.getWorkouts();
		int x = w.size();
		String g = profile.getGender();
		Calendar d = Calendar.getInstance();
		if (g.equals("Male")) {
			if (x >= 5 && difference(d, w.get(4).getDate()))
				iv.setImageResource(R.drawable.mstrong);
			else
				iv.setImageResource(R.drawable.mweak);
		} else {
			if (x >= 5 && difference(d, w.get(4).getDate()))
				iv.setImageResource(R.drawable.fstrong);
			else
				iv.setImageResource(R.drawable.fweak);
		}

	}

	private boolean difference(Calendar b, Calendar c) {
		int y = b.get(Calendar.YEAR) - c.get(Calendar.YEAR);
		int m = b.get(Calendar.MONTH) - c.get(Calendar.MONTH);
		int d = b.get(Calendar.DAY_OF_MONTH) - c.get(Calendar.DAY_OF_MONTH);
		if (y > 0)
			return false;
		if (m > 0)
			return false;
		if (d > 7)
			return false;
		return true;
	}

}
