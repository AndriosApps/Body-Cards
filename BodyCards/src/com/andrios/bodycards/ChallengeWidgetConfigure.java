package com.andrios.bodycards;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;

import android.app.Activity;
import android.app.AlertDialog;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class ChallengeWidgetConfigure extends Activity {
	int mAppWidgetId;

	Button nextBTN, clearBTN;

	ListView availableListView, selectedListView;
	EditText min, max;
	TextView tv;
	View view;
	String name, desc, workoutName;

	int selectedRow, maxReps, minReps;

	AlertDialog ad;

	ArrayAdapter<Exercise> aa, sa;
	ArrayList<Exercise> exerciseList, sel;
	ArrayList<Profile> profs;
	String[] profNames;
	Intent resultValue;
	boolean[] selected;
	
	@Override
	public void onCreate(Bundle savedInstanceBundle) {
		super.onCreate(savedInstanceBundle);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.challengewidgetconfigure);

		
		getExtras();
		readExercises();
		readProfiles();
		setAlertDialog();
		setConnections();
		ad.show();
	}

	private void getExtras() {
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
		    mAppWidgetId = extras.getInt(
		            AppWidgetManager.EXTRA_APPWIDGET_ID, 
		            AppWidgetManager.INVALID_APPWIDGET_ID);
		}
		
		//TODO Remove these
		System.out.println(AppWidgetManager.EXTRA_APPWIDGET_ID);
		System.out.println(Integer.toString(mAppWidgetId));
		System.out.println(Integer.toString(AppWidgetManager.INVALID_APPWIDGET_ID));
		
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
	
	private void writeProfiles(){
		try {
			FileOutputStream fos = openFileOutput("profiles",
					Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			oos.writeObject(profs);

			oos.close();
			fos.close();

		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	


	private void readExercises() {
		
			System.out.println("READING EXERCISES"); // TODO Remove
			try {
				FileInputStream fis = openFileInput("exercises");
				ObjectInputStream ois = new ObjectInputStream(fis);

				exerciseList = (ArrayList<Exercise>) ois.readObject();
				sel = (ArrayList<Exercise>) ois.readObject();

				ois.close();
				fis.close();

			} catch (Exception e) {
				e.printStackTrace();
				exerciseList = new ArrayList<Exercise>();
				sel = new ArrayList<Exercise>();

				createDefaultExercises();

				write();

			}

		
		
	}

	private void setConnections() {

		min = (EditText) findViewById(R.id.challengeWidgetMinEdit);
		max = (EditText) findViewById(R.id.challengeWidgetMaxEdit);
		resultValue = new Intent(this, ChallengeWidgetConfigure.class);
		setResult(RESULT_CANCELED, resultValue);
		
		
		selected = new boolean[exerciseList.size()];
		for (int i = 0; i < selected.length; i++) {
			selected[i] = false;
		}

		availableListView = (ListView) findViewById(R.id.challengeWidgetAvailableExercisesListView);
		aa = new ArrayAdapter<Exercise>(this, R.layout.list_view2, exerciseList);
		aa.setNotifyOnChange(true);
		aa.sort(new Comparator<Exercise>() {

			public int compare(Exercise object1, Exercise object2) {

				return object1.toString().compareToIgnoreCase(
						object2.toString());
			}

		});
		availableListView.setAdapter(aa);



		selectedListView = (ListView) findViewById(R.id.challengeWidgetSelectedExercisesListView);
		sa = new ArrayAdapter<Exercise>(this, R.layout.list_view2, sel);
		selectedListView.setAdapter(sa);
		sa.setNotifyOnChange(true);






		nextBTN = (Button) findViewById(R.id.challengeWidgetNextBTN);


		clearBTN = (Button) findViewById(R.id.challengeWidgetClearBTN);

		
		setOnClickListeners();
	}

	private void setOnClickListeners() {
		
		availableListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int row,
					long arg3) {
				Exercise r = aa.getItem(row);
				aa.remove(r);
				sa.add(r);
				sa.sort(new Comparator<Exercise>() {

					public int compare(Exercise object1, Exercise object2) {

						return object1.toString().compareToIgnoreCase(
								object2.toString());
					}

				});

			}

		});

		availableListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int row, long arg3) {
				selectedRow = row;

				tv.setText("Would you like to view "
						+ ((Exercise) availableListView.getItemAtPosition(row)).toString()
						+ " exercise card or delete the exercise from the list?");
				view = arg1;
				name = ((Exercise) availableListView.getItemAtPosition(row)).getName();
				desc = ((Exercise) availableListView.getItemAtPosition(row)).getDesc();
				ad.show();
				return true;
			}

		});
		
		selectedListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int row,
					long arg3) {

				Exercise r = sa.getItem(row);
				sa.remove(r);
				aa.add(r);

				aa.sort(new Comparator<Exercise>() {

					public int compare(Exercise object1, Exercise object2) {

						return object1.toString().compareToIgnoreCase(
								object2.toString());
					}

				});
			}

		});

		selectedListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int row, long arg3) {

				Intent intent = new Intent(arg1.getContext(),
						DisplayExerciseActivity.class);
				intent.putExtra("exer", (Exercise)selectedListView.getItemAtPosition(row));
				
				startActivity(intent);
				

				return true;
			}

		});
		
	
		
		nextBTN.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				maxReps = Integer.parseInt(max.getText().toString().trim());
				minReps = Integer.parseInt(min.getText().toString().trim());
				
				if (sel.isEmpty()) {
					Toast.makeText(ChallengeWidgetConfigure.this,
							"You must choose at least 1 exercise",
							Toast.LENGTH_SHORT).show();
				} else if(minReps > maxReps){
					Toast.makeText(ChallengeWidgetConfigure.this,
							"Max Reps must be more than Min Reps",
							Toast.LENGTH_SHORT).show();
					
				}else {
					write();
					configureWidget();
				}
			}


			
			
			private void configureWidget() {
				System.out.println("CONTEXT GETTER"); // TODO Remove
				Context context = ChallengeWidgetConfigure.this;
				AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
				

				System.out.println("REMOTE VIEWS GETTER"); // TODO Remove
				
				//TODO Toggle here between Provider solutions. 
				System.out.println("AppWidgetId "+ mAppWidgetId);
				RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.challengewidget);
				//appWidgetManager.updateAppWidget(mAppWidgetId, views);
				ChallengeWidgetProvider.updateAppWidget(context, appWidgetManager, mAppWidgetId);
				

				System.out.println("INTENT resultValue"); // TODO Remove
				
				resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);

				setResult(RESULT_OK, resultValue);
				finish();
				
			}



		});
		
		clearBTN.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				
				resetExerciseList();
			}

		});
		
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
		System.out.println("WRITE CONFIGURE");
		try {
			FileOutputStream fos = openFileOutput(mAppWidgetId+"widgetexercises",
					Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			oos.writeObject(sel);
			
			
			oos.writeInt(maxReps);
			oos.writeInt(minReps);
			
			oos.close();
			fos.close();

		} catch (IOException e) {

			e.printStackTrace();
		}

	}
	
	private void resetExerciseList() {
		while (!sa.isEmpty()) {
			Exercise r = (Exercise) sa.getItem(0);
			sa.remove(r);
			aa.add(r);
			aa.sort(new Comparator<Exercise>() {

				public int compare(Exercise object1, Exercise object2) {

					return object1.toString().compareToIgnoreCase(
							object2.toString());
				}

			});
		}
		
	}

private void setAlertDialog() {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = LayoutInflater.from(this);
		final View layout = inflater.inflate(R.layout.widgetconfiguredialog, null);
		final ListView profileLV = (ListView) layout.findViewById(R.id.widgetConfigureDialogListView);
		ArrayAdapter<Profile> profAdapter = new ArrayAdapter<Profile>(this, android.R.layout.simple_list_item_1, profs){
	        
			@Override
	        public int getCount() {
	                return profs.size();
	        } 
			
	        @Override
	        public Profile getItem(int position) {
	                return profs.get(position);
	        } 
	        
	        @Override
	        public long getItemId(int position) {
	                return position;
	        } 
	        
			public <ViewGroup> View getView(int position, View convertView, ViewGroup parent){
				 TextView view = (TextView) super.getView(position, convertView, (android.view.ViewGroup) parent);
				 view.setText(profs.get(position).getFirstName());
				 return view;
			}
			
		};
		
		profileLV.setAdapter(profAdapter);
		profileLV.setOnItemClickListener(new OnItemClickListener(){
			
			
			
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				if(profs.get(position).isWidget){
					
					Toast.makeText(ChallengeWidgetConfigure.this,
							profs.get(position).getFirstName() +" already has a widget",
							Toast.LENGTH_SHORT).show();
				}else{
					profs.get(position).setID(true, mAppWidgetId);
					writeProfiles();
					ad.dismiss();
				}
				
				
			}
			
		});
		
		
		builder.setView(layout)
				.setTitle("Choose Profile!");
		
		ad = builder.create();
	}
	
}
