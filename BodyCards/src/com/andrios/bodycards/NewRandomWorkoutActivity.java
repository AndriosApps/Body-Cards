package com.andrios.bodycards;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class NewRandomWorkoutActivity extends Activity {
	Button back, reset, done;
	ArrayList<Exercise> exerciseNames, selectedExercises;
	ArrayList<Workout> peoplesWorkouts;
	ListView lv, ls;
	ArrayAdapter<Profile> aa, as;
	ArrayList<Profile> profs, selects;
	int chsnPrf;
	EditText numPeeps, numSets, max, min;
	TextView t;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.newworkout);
		
		readProfiles();
		readExercises();
		getExtras();
		setConnections();
	}

	public void onActivityResult(int requestCode, int returnCode, Intent intent) {
		if (returnCode == RESULT_OK) {
			setResult(RESULT_OK);
			NewRandomWorkoutActivity.this.finish();
		}
	}

	@SuppressWarnings("unchecked")
	private void getExtras() {
		//TODO Delete This?
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
		
	
		chsnPrf = 0;
		selectedExercises = new ArrayList<Exercise>();
		t = (TextView) findViewById(R.id.exerList);
		
		
		pickRandomExercises();


		numPeeps = (EditText) findViewById(R.id.numPeoEdit);
		numSets = (EditText) findViewById(R.id.repEdit);
		max = (EditText) findViewById(R.id.maxEdit);
		min = (EditText) findViewById(R.id.minEdit);

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



		back = (Button) findViewById(R.id.nwBack);


		reset = (Button) findViewById(R.id.nwReset);
		reset.setText("Randomize");


		done = (Button) findViewById(R.id.nwDone);
		
		
		
		setOnClickListeners();
	}
	
	private void pickRandomExercises() {
		int index = 0;
		Exercise e = null;
		String exLst = "";
		Random generator = new Random();
		int numExercises = generator.nextInt(4) + 2; // select 2-5 exercises
		selectedExercises.clear();
		ArrayList<Exercise> tempHolder = new ArrayList<Exercise>();
		for (int i = 0; i < numExercises; i++) {
			System.out.println("INdex" +index);
			index = generator.nextInt(exerciseNames.size());
		
			e = exerciseNames.get(index);
			
			System.out.println("exerciseSize" + exerciseNames.size());
			System.out.println("tempHolderSize" + tempHolder.size());
			tempHolder.add(exerciseNames.remove(index));
			System.out.println("INdex" +index);
			System.out.println("exerciseSize" + exerciseNames.size());
			System.out.println("tempHolderSize" + tempHolder.size());
			selectedExercises.add(e);
		}
		
		for (int i = 0; i < selectedExercises.size(); i++) {
			if (i == 0)
				exLst += selectedExercises.get(i).getName();
			else
				exLst += "\n" + selectedExercises.get(i).getName();
		}
		
		for (int i = 0; i < tempHolder.size(); i++) {
			System.out.println("tempHolderSize" + i + tempHolder.size());
			exerciseNames.add(tempHolder.get(i));
		}
		tempHolder.clear();
		t.setText(exLst);
		
		//TODO Implement Random Sets Min / Max Etc. 
		//numSets.setText(Integer.toString(generator.nextInt(16)+15));
		//max.setText(Integer.toString(generator.nextInt(6)+15));
		//min.setText(Integer.toString(generator.nextInt(3)+2));
	}

	private void setOnClickListeners() {
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
		
		back.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				 
				NewRandomWorkoutActivity.this.finish();
			}

		});
		
		reset.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				 
				//TODO Remove these when Randomizing. 
				numPeeps.setText("0");
				numSets.setText("20");
				min.setText("2");
				max.setText("15");
				
				pickRandomExercises();

			}

		});
		
		done.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				 

				int x = Integer.parseInt(max.getText().toString());
				int n = Integer.parseInt(min.getText().toString());
				int s = Integer.parseInt(numSets.getText().toString());
				int p = Integer.parseInt(numPeeps.getText().toString());

				if(p+chsnPrf < 1) {
					Toast.makeText(NewRandomWorkoutActivity.this,
							"Must choose at least one profile or guest user",
							Toast.LENGTH_SHORT).show();
				}
				else if (x < n) {
					Toast.makeText(NewRandomWorkoutActivity.this,
							"Max must be greater than or equal to min",
							Toast.LENGTH_SHORT).show();
				} else if (x <= 0) {
					Toast.makeText(NewRandomWorkoutActivity.this,
							"Max must be greater than zero",
							Toast.LENGTH_SHORT).show();
				} else if (n <= 0) {
					Toast.makeText(NewRandomWorkoutActivity.this,
							"Min must be greater than zero",
							Toast.LENGTH_SHORT).show();
				} else if (s < 1) {
					Toast.makeText(NewRandomWorkoutActivity.this,
							"Sets must be greater than zero",
							Toast.LENGTH_SHORT).show();
				} else {

					Intent wkout = new Intent(v.getContext(),
							WorkoutCardActivity.class);
					wkout.putExtra("max", x);
					wkout.putExtra("min", n);
					wkout.putExtra("sets", s);
					wkout.putExtra("peeps", p+chsnPrf);
					wkout.putExtra("profilesU", profs);
					wkout.putExtra("profiles", selects);
					wkout.putExtra("exercises", selectedExercises);
					startActivityForResult(wkout, 31415);
				}

				
			}

		});
	}

	@SuppressWarnings("unchecked")
	private void readExercises() {
		try {
			FileInputStream fis = openFileInput("exercises");
			ObjectInputStream ois = new ObjectInputStream(fis);

			exerciseNames = (ArrayList<Exercise>) ois.readObject();

			ois.close();
			fis.close();

		} catch (Exception e) {

			exerciseNames = new ArrayList<Exercise>();

			createDefaultExercises();

			write();

		}

	}
	
	private void createDefaultExercises() {
		exerciseNames
				.add(0,
						new Exercise(
								"Push-Ups with Rotation",
								"1. Start by lying on your back with your knees towards your chest and your arms flat on the ground.\n\n" +
								"2. Keeping your back flat throughout the movement kick your legs out and away until they are almost straight.\n\n" +
								"3. Bring your legs back in and repeat for the required number of repetitions.\n\n" +
								"4. If you are unable to keep your back flat on the floor throughout the movement shorten the distance that your legs extend until you get stronger.",
								"Chest",
								R.drawable.pushupwithrotation));
		exerciseNames
				.add(0,
						new Exercise(
								"Double Leg Pressouts",
								"1. Start by lying on your back with your knees towards your chest and your arms flat on the ground.\n\n" +
								"2. Keeping your back flat throughout the movement kick your legs out and away until they are almost straight.\n\n" +
								"3. Bring your legs back in and repeat for the required number of repetitions.\n\n" +
								"4. If you are unable to keep your back flat on the floor throughout the movement shorten the distance that your legs extend until you get stronger.",
								"Abs",
								R.drawable.doublelegpressouts));
		exerciseNames
				.add(0,
						new Exercise(
								"Hip Thrust",
								"1. Lie on your back with your legs bent 90 degrees at the hip.\n\n" +
								"2. Slowly lift your hips off the floor and towards the ceiling.\n\n" +
								"3. Lower your hips to the floor and repeat for the prescribed number of repetitions.",
								"Abs",
								R.drawable.hipthrust));
		exerciseNames
				.add(0,
						new Exercise(
								"Oblique Crunch",
								"1. Start by placing your left foot over your right knee and place your hands at the side of your head.\n\n" +
								"2. Lift your shoulders up off the ground and twist so that your right elbows tries to touch your left knee.\n\n" +
								"3. Return to the starting position and repeat according to the required repetitions.\n\n" +
								"4. Repeat with the other side.",
								"Abs",
								R.drawable.obliquecrunch));
		exerciseNames
				.add(0,
						new Exercise(
								"V-Up",
								"1. Start position: Lie back onto floor or bench with knees bent and hands extended towards ceiling. Head should be in a neutral position with a space between chin and chest.\n\n" +
								"2. Leading with the chin and chest towards the ceiling, contract the abdominal and raise shoulders off floor or bench. Also raise legs up towards ceiling and attempt to touch your hands to your feet.\n\n" +
								"3. Return to start position.\n\n",
								"Abs",
								R.drawable.vup));
		exerciseNames
				.add(0,
						new Exercise(
								"Sit Ups",
								"1. Start position: Lie back onto floor or bench with knees bent and hands at the side of your head. Keep elbows back and out of sight. Head should be in a neutral position with a space between chin and chest.\n\n" +
								"2. Leading with the chin and chest towards the ceiling, contract the abdominal and raise shoulders off floor or bench until you are seated in an upright position.\n\n" +
								"3. Return to start position.\n\n" +
								"4. Remember to keep head and back in a neutral position. Hyperextension or flexion of either may cause injury.",
								"Abs",
								R.drawable.situp));
	}
	
	public void write() {
		try {
			FileOutputStream fos = openFileOutput("exercises",
					Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			oos.writeObject(exerciseNames);

			oos.close();
			fos.close();

		} catch (IOException e) {

			Toast.makeText(NewRandomWorkoutActivity.this, "Error: Writing to file",
					Toast.LENGTH_SHORT).show();
		}

	}

}
