package com.andrios.bodycards;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
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

public class NewRandomWorkoutActivity extends Activity {
	Button reset, done;
	ArrayList<Exercise> exerciseList, selectedExercises;
	ArrayList<Workout> peoplesWorkouts;
	ListView lv, ls;
	ArrayAdapter<Profile> aa, as;
	ArrayList<Profile> profs, selects;
	int chsnPrf;
	EditText numPeeps, numSets, max, min;
	TextView t;
	String workoutName;
	ImageView helpReps;
	GoogleAnalyticsTracker tracker;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.newworkout);
		
		tracker = GoogleAnalyticsTracker.getInstance();

	    // Start the tracker in manual dispatch mode...
	    tracker.start("UA-23366060-1", this);
	    tracker.trackPageView("New Random Workout Activity");
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

	private void getExtras() {

		Intent intent = this.getIntent();
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
		
	
		chsnPrf = 0;
		selectedExercises = new ArrayList<Exercise>();
		t = (TextView) findViewById(R.id.exerList);
		
		
		pickRandomExercises();


		helpReps = (ImageView) findViewById(R.id.newWorkoutActivityHelpRepsIMG);
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



		
		reset = (Button) findViewById(R.id.nwReset);
		reset.setText("Randomize");


		done = (Button) findViewById(R.id.nwDone);
		
		
		setOnFocusChangeListeners();
		setOnClickListeners();
	}
	
	private void setOnFocusChangeListeners() {

		numPeeps.setOnFocusChangeListener(new OnFocusChangeListener(){

			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					
					InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					// only will trigger it if no physical keyboard is open

					mgr.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
					
				}else{
					InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					mgr.hideSoftInputFromWindow(numPeeps.getWindowToken(), 0);
				}


				
			}
			
		});
		
		numSets.setOnFocusChangeListener(new OnFocusChangeListener(){

			public void onFocusChange(View v, boolean hasFocus) {
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
		
		max.setOnFocusChangeListener(new OnFocusChangeListener(){

			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					
					InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					// only will trigger it if no physical keyboard is open

					mgr.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
					
				}else{
					InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					mgr.hideSoftInputFromWindow(max.getWindowToken(), 0);
				}


				
			}
			
		});
		
		min.setOnFocusChangeListener(new OnFocusChangeListener(){

			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					
					InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					// only will trigger it if no physical keyboard is open

					mgr.showSoftInput(numSets, InputMethodManager.SHOW_IMPLICIT);
					
				}else{
					InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					mgr.hideSoftInputFromWindow(min.getWindowToken(), 0);
				}


				
			}
			
		});
		
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
			index = generator.nextInt(exerciseList.size());
		
			e = exerciseList.get(index);
			
			tempHolder.add(exerciseList.remove(index));
			
			selectedExercises.add(e);
		}
		
		for (int i = 0; i < selectedExercises.size(); i++) {
			if (i == 0)
				exLst += selectedExercises.get(i).getName();
			else
				exLst += "\n" + selectedExercises.get(i).getName();
		}
		
		for (int i = 0; i < tempHolder.size(); i++) {
			exerciseList.add(tempHolder.get(i));
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
				 
				try{
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
								StartDeckActivity.class);
						wkout.putExtra("max", x);
						wkout.putExtra("min", n);
						wkout.putExtra("sets", s);
						wkout.putExtra("peeps", p+chsnPrf);
						wkout.putExtra("profilesU", profs);
						wkout.putExtra("profiles", selects);
						wkout.putExtra("exercises", selectedExercises);
						wkout.putExtra("workoutName", workoutName);
						startActivityForResult(wkout, 31415);
					}
				}catch(Exception e){
					Toast.makeText(NewRandomWorkoutActivity.this,
							"Ensure correct values are entered in all fields",
							Toast.LENGTH_SHORT).show();
				}

				
			}

		});
		
		helpReps.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				 tracker.trackEvent(
				            "Clicks",  // Category
				            "Button",  // Action
				            "Help Random Workout Help Button", // Label
				            1);       // Value
				
				Intent intent = new Intent(v.getContext(), HelpGenericActivity.class);
				intent.putExtra("ID", 6);
				startActivity(intent);
				
			}
			
		});
	}

	@SuppressWarnings("unchecked")
	private void readExercises() {
		try {
			FileInputStream fis = openFileInput("exercises");
			ObjectInputStream ois = new ObjectInputStream(fis);

			exerciseList = (ArrayList<Exercise>) ois.readObject();

			ois.close();
			fis.close();

		} catch (Exception e) {

			exerciseList = new ArrayList<Exercise>();

			createDefaultExercises();

			write();

		}

	}
	
	private void createDefaultExercises() {
		exerciseList
				.add(0,
						new Exercise(
								"Push-Ups",
								"1. Begin with your hands and toes on the floor.\n\n" +
								"2. Your torso and legs should remain rigid, keeping your back perfectly straight throughout the move.\n\n" +
								"3. Bend your arms and slowly lower your body downward, stopping just before your upper chest touches the ground.\n\n" +
								"4. Feel a stretch in your chest muscles and then reverse direction, pushing your body up along the same path back to the start position.",
								R.drawable.nopic,
								1.0, 
								"Chest", 
								false));
		exerciseList
				.add(0,
						new Exercise(
								"Squats",
								"1. Stand erect with the feet about shoulder-width apart, feet pointed straight ahead or slightly toed-out.\n\n" +
								"2. Keeping the head up, the chest lifted, and the abdominal muscles tight, squat until thighs are parallel to the floor.\n\n" +
								"3. Arch your lower back slightly, and keep that arch throughout the exercise.\n\n" +
								"4. Pause briefly, then stand upright by pressing heels into the floor and keeping the glutes (the rump muscles) tight.",
								R.drawable.nopic,
								1.0,
								"Hamstrings",
								false));
		exerciseList
				.add(0,
						new Exercise(
								"Bench Dips",
								"1. Sit on the edge of a solid bench or chair with your hands holding the edge by your hips.\n\n" +
								"2. Walk your feet forward, then straighten your arms to lift your body off the bench.\n\n" +
								"3. Lower yourself until your upper arms are about parallel with the floor, then press with the triceps to lift yourself back up - straighten the arms, but don’t lock the elbows at the top."+
								"Alternative. Place your feet on another bench or chair and add weights to your lap to add difficulty.",
								R.drawable.nopic,
								1.0,
								"Triceps",
								false));
		exerciseList
				.add(0,
						new Exercise(
								"Calf Raises",
								"1. Stand on the balls of your feet on the edge of a step, with your heels hanging over the edge.\n\n" +
								"2. Hold onto the wall or a railing lightly with one hand.\n\n" +
								"3. Keeping your head up and your torso upright, stand up on your toes, then lower yourself until your heels are slightly below the step.\n\n" +
								"Alternative. Balance without using your hands, conduct exercise one leg at a time.",
								R.drawable.nopic,
								1.0,
								"Calves",
								false));
		exerciseList
				.add(0,
						new Exercise(
								"Reverse Flyes",
								"1. Using light dumbbells, sit on bench or ball and lean over holding weights in front of legs.\n\n" +
								"2. Keep your torso parallel to the floor.\n\n" +
								"3. Raise arms out to the side to shoulder height, keeping elbows in a fixed (slightly bent) position.\n\n" +
								"Alternative. Conduct exercise while standing.",
								R.drawable.nopic,
								1.0,
								"Back",
								false));
		
		exerciseList
				.add(0,
						new Exercise(
								"Jumping Jacks",
								"1. Begin by standing with your feet together and arms at your sides. Tighten your abdominal muscles to pull your pelvis forward and take the curve out of your lower back.\n\n" +
								"2. Bend your knees and jump, moving your feet apart until they are wider than your shoulders. At the same time, raise your arms over your head. You should be on the balls of your feet.\n\n" +
								"3. Keep your knees bent while you jump again, bringing your feet together and your arms back to your sides. At the end of the movement, your weight should be on your heels.\n\n",
								R.drawable.nopic,
								2.0,
								"Various",
								false));
		
		exerciseList
				.add(0,
						new Exercise(
								"8 Count Body-Builders",
								"Start in a standing position, arms comfortably at your sides. Keep knees slightly flexed with feet hip width apart.\n\n" +
								"1. Squat down while keeping your back straight. Lean forward and place your hands, palms down, on the floor. Keep your arms extended so that your upper body is off the floor.\n\n" +
								"2. Extend your legs straight out from your body, resting on your toes.\n\n" +
								"3. Lower your upper body to the floor by bending your elbows out to the side, perpendicular to your body.\n\n" +
								"4. Raise your upper body again, and hold it up on extended arms.\n\n" +
								"5. Scissor your legs open.\n\n" +
								"6. Scissor your legs closed.\n\n" +
								"7. Draw your knees up to your chest.\n\n" +
								"8. Quickly stand up.\n\n" +
								"Repeat.\n\n",
								R.drawable.nopic,
								1.0,
								"Various",
								false));
		
		exerciseList
			.add(0,
					new Exercise(
							"Sprints",
							"Warm up with a short walk and a brief run or jog, followed by light stretching of the major muscle groups.\n\n" +
							"1. Find a flat stretch of ground at least 100 meters long.\n\n" +
							"2. Mark a spot in the distance, either mentally or visually, and begin running as fast as you can toward that spot.\n\n" +
							"3. Raise your knees as high as you can with each step (think of your legs as two pistons pumping vigorously).\n\n" +
							"4. Pump your arms hard, but keep them loose; swing them at your sides, not across your body.\n\n" +
							"5. Breathe naturally and deeply into your abdomen as you sprint, and keep your torso erect.\n\n" +
							"6. Slow down after you've passed your goal post, then stop.\n\n" +
							"Repeat.\n\n",
							R.drawable.nopic,
							0.5,
							"Cardio",
							false));
		
		exerciseList
				.add(0,
						new Exercise(
								"Lunges",
								"1. Stand in a split stance with the right foot forward and the left leg back, feet should be about 2 to 3 feet apart, depending on your leg length.\n\n" +
								"2. Before you lunge, make sure your torso is straight and that you’re up on the back toe.\n\n" +
								"3. Bend the knees and lower the body down until the back knee is a few inches from the floor.\n\n" +
								"4. At the bottom of the movement, the front thigh should be parallel to the floor and the back knee should point toward the floor.\n\n" +
								"5. Keep the weight evenly distributed between both legs and push back up, keeping the weight in the heel of the front foot.\n\n"+
								"Repeat.\n\n",
								R.drawable.nopic,
								1.0,
								"Calves",
								false));
		
		exerciseList
				.add(0,
						new Exercise(
								"Sit Ups",
								"1. Start position: Lie back onto floor or bench with knees bent and hands at the side of your head. Keep elbows back and out of sight. Head should be in a neutral position with a space between chin and chest.\n\n" +
								"2. Leading with the chin and chest towards the ceiling, contract the abdominal and raise shoulders off floor or bench until you are seated in an upright position.\n\n" +
								"3. Return to start position.\n\n" +
								"4. Remember to keep head and back in a neutral position. Hyperextension or flexion of either may cause injury.",
								R.drawable.nopic,
								1.0,
								"Abs",
								false));
		exerciseList
			.add(0,
					new Exercise(
							"Planks",
							"1. Start by lying face down on a mat. Place your forearms on the mat with your shoulders aligned directly over your elbows. Clasp your hands in front of you.\n\n" +
							"2. Extend your legs behind you and rest on your toes, as if you are going to do a pushup. Your hips should not be lifted to the ceiling, nor should your back be arched. You should look to attain a straight line between your shoulders and toes.\n\n" +
							"3. Tighten your abdominal muscles to help you hold the position correctly, and hold it as long as you can or until time expires.\n\n" +
							"4. Alternative. Lift one leg at a time behind you to make the exercise more challenging.",
							R.drawable.nopic,
							1.0,
							"Abs",
							true));
	}
	
	public void write() {
		try {
			FileOutputStream fos = openFileOutput("exercises",
					Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			oos.writeObject(exerciseList);

			oos.close();
			fos.close();

		} catch (IOException e) {

			Toast.makeText(NewRandomWorkoutActivity.this, "Error: Writing to file",
					Toast.LENGTH_SHORT).show();
		}

	}
	  @Override
	  protected void onDestroy() {
	    super.onDestroy();
	    // Stop the tracker when it is no longer needed.
	    tracker.stop();
	  }
}
