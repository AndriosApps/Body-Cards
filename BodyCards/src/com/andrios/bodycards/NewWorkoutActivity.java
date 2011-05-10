package com.andrios.bodycards;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Comparator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class NewWorkoutActivity extends Activity {
	Button back, reset, done;
	ArrayList<Exercise> exerciseNames;
	ArrayList<Workout> peoplesWorkouts;
	ListView lv, ls;
	ArrayAdapter<Profile> aa, as;
	ArrayList<Profile> profs, selects;
	int chsnPrf;
	EditText numPeeps, numSets, max, min;
	String workoutName;
	ImageView helpReps;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.newworkout);
		
		getExtras();
		setConnections();
	}

	public void onActivityResult(int requestCode, int returnCode, Intent intent) {
		if (returnCode == RESULT_OK) {
			setResult(RESULT_OK);
			NewWorkoutActivity.this.finish();
		}
	}

	@SuppressWarnings("unchecked")
	private void getExtras() {
		Intent intent = this.getIntent();
		exerciseNames = (ArrayList<Exercise>) intent
				.getSerializableExtra("selectedexercises");
		workoutName = intent.getStringExtra("workoutName");
		
	}

	@SuppressWarnings("unchecked")
	private void readProfiles() {
		try {
			FileInputStream fis = openFileInput("profiles");
			ObjectInputStream ois = new ObjectInputStream(fis);

			profs = (ArrayList<Profile>) ois.readObject();
			ois.close();
			fis.close();

		} catch (Exception e) {

			profs = new ArrayList<Profile>();
		}

	}

	private void setConnections() {
		 
		readProfiles();

		chsnPrf = 0;

		helpReps = (ImageView) findViewById(R.id.newWorkoutActivityHelpRepsIMG);
		TextView t = (TextView) findViewById(R.id.exerList);
		String exLst = "";
		for (int i = 0; i < exerciseNames.size(); i++) {
			if (i == 0)
				exLst += exerciseNames.get(i).getName();
			else
				exLst += "\n" + exerciseNames.get(i).getName();
		}
		t.setText(exLst);

		numPeeps = (EditText) findViewById(R.id.numPeoEdit);
		
		numSets = (EditText) findViewById(R.id.repEdit);
		max = (EditText) findViewById(R.id.maxEdit);
		min = (EditText) findViewById(R.id.minEdit);
		InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		// only will trigger it if no physical keyboard is open
		mgr.showSoftInput(numPeeps, InputMethodManager.SHOW_IMPLICIT);
		mgr.showSoftInput(numSets, InputMethodManager.SHOW_IMPLICIT);
		mgr.showSoftInput(max, InputMethodManager.SHOW_IMPLICIT);
		mgr.showSoftInput(min, InputMethodManager.SHOW_IMPLICIT);

		lv = (ListView) findViewById(R.id.profileChooser);
		aa = new ArrayAdapter<Profile>(this, R.layout.list_view2, profs);
		lv.setAdapter(aa);
		aa.sort(new Comparator<Profile>() {

			public int compare(Profile object1, Profile object2) {
				 
				return object1.getLastName().compareToIgnoreCase(
						object2.getLastName());
			}

		});
		aa.setNotifyOnChange(true);

		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int row,
					long arg3) {
				 

				chsnPrf++;
				Profile r = (Profile) aa.getItem(row);
				aa.remove(r);
				as.add(r);
				as.sort(new Comparator<Profile>() {

					public int compare(Profile object1, Profile object2) {
						 
						return object1.getLastName().compareToIgnoreCase(
								object2.getLastName());
					}

				});
			}

		});
		selects = new ArrayList<Profile>();
		ls = (ListView) findViewById(R.id.chosenProfiles);
		as = new ArrayAdapter<Profile>(this, R.layout.list_view2, selects);
		ls.setAdapter(as);
		as.sort(new Comparator<Profile>() {

			public int compare(Profile object1, Profile object2) {
				 
				return object1.getLastName().compareToIgnoreCase(
						object2.getLastName());
			}

		});
		as.setNotifyOnChange(true);

		ls.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int row,
					long arg3) {
				 

				chsnPrf--;
				Profile r = (Profile) as.getItem(row);
				as.remove(r);
				aa.add(r);
				aa.sort(new Comparator<Profile>() {

					public int compare(Profile object1, Profile object2) {
						 
						return object1.getLastName().compareToIgnoreCase(
								object2.getLastName());
					}

				});
			}

		});

		back = (Button) findViewById(R.id.nwBack);
		back.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				 
				NewWorkoutActivity.this.finish();
			}

		});

		reset = (Button) findViewById(R.id.nwReset);
		reset.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				 
				EditText people = (EditText) findViewById(R.id.numPeoEdit);
				EditText reps = (EditText) findViewById(R.id.repEdit);
				EditText min = (EditText) findViewById(R.id.minEdit);
				EditText max = (EditText) findViewById(R.id.maxEdit);

				people.setText("0");
				reps.setText("20");
				min.setText("2");
				max.setText("15");

			}

		});

		done = (Button) findViewById(R.id.nwDone);
		numSets.setOnFocusChangeListener(new OnFocusChangeListener(){

			public void onFocusChange(View v, boolean hasFocus) {
				System.out.println("DONE FOCUS CHANGE");
				if(hasFocus){
					/*
					InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					// only will trigger it if no physical keyboard is open

					mgr.showSoftInput(numSets, InputMethodManager.SHOW_IMPLICIT);
					*/
				}else{
					InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					mgr.hideSoftInputFromWindow(numSets.getWindowToken(), 0);
				}


				
			}
			
		});
		numPeeps.setOnFocusChangeListener(new OnFocusChangeListener(){

			public void onFocusChange(View v, boolean hasFocus) {
				System.out.println("DONE FOCUS CHANGE");
				if(hasFocus){
					/*
					InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					// only will trigger it if no physical keyboard is open

					mgr.showSoftInput(numSets, InputMethodManager.SHOW_IMPLICIT);
					*/
				}else{
					InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					mgr.hideSoftInputFromWindow(numPeeps.getWindowToken(), 0);
				}


				
			}
			
		});
		max.setOnFocusChangeListener(new OnFocusChangeListener(){

			public void onFocusChange(View v, boolean hasFocus) {
				System.out.println("DONE FOCUS CHANGE");
				if(hasFocus){
					/*
					InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					// only will trigger it if no physical keyboard is open

					mgr.showSoftInput(numSets, InputMethodManager.SHOW_IMPLICIT);
					*/
				}else{
					InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					mgr.hideSoftInputFromWindow(max.getWindowToken(), 0);
				}


				
			}
			
		});
		min.setOnFocusChangeListener(new OnFocusChangeListener(){

			public void onFocusChange(View v, boolean hasFocus) {
				System.out.println("DONE FOCUS CHANGE");
				if(hasFocus){
					/*
					InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					// only will trigger it if no physical keyboard is open

					mgr.showSoftInput(numSets, InputMethodManager.SHOW_IMPLICIT);
					*/
				}else{
					InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					mgr.hideSoftInputFromWindow(min.getWindowToken(), 0);
				}


				
			}
			
		});
	
		done.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				 
				try{
					int x = Integer.parseInt(max.getText().toString());
					int n = Integer.parseInt(min.getText().toString());
					int s = Integer.parseInt(numSets.getText().toString());
					int p = Integer.parseInt(numPeeps.getText().toString());
					if(p+chsnPrf < 1) {
						Toast.makeText(NewWorkoutActivity.this,
								"Must choose at least one profile or guest user",
								Toast.LENGTH_SHORT).show();
					}
					else if (x < n) {
						Toast.makeText(NewWorkoutActivity.this,
								"Max must be greater than or equal to min",
								Toast.LENGTH_SHORT).show();
					} else if (x <= 0) {
						Toast.makeText(NewWorkoutActivity.this,
								"Max must be greater than zero",
								Toast.LENGTH_SHORT).show();
					} else if (n <= 0) {
						Toast.makeText(NewWorkoutActivity.this,
								"Min must be greater than zero",
								Toast.LENGTH_SHORT).show();
					} else if (s < 1) {
						Toast.makeText(NewWorkoutActivity.this,
								"Sets must be greater than zero",
								Toast.LENGTH_SHORT).show();
					}else {

						Intent wkout = new Intent(v.getContext(),
								WorkoutCardActivity.class);
						wkout.putExtra("max", x);
						wkout.putExtra("min", n);
						wkout.putExtra("sets", s);
						wkout.putExtra("peeps", p+chsnPrf);
						wkout.putExtra("profilesU", profs);
						wkout.putExtra("profiles", selects);
						wkout.putExtra("exercises", exerciseNames);
						wkout.putExtra("workoutName", workoutName);
						startActivityForResult(wkout, 31415);
					}
				}catch(Exception e){
					Toast.makeText(NewWorkoutActivity.this,
							"Ensure values are entered in all fields",
							Toast.LENGTH_SHORT).show();
				}
			

				

				
			}

		});
		
		helpReps.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), HelpGenericActivity.class);
				intent.putExtra("ID", 6);
				startActivity(intent);
				
			}
			
		});
	}

}
