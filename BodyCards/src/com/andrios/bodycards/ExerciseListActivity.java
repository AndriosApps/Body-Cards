package com.andrios.bodycards;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ExerciseListActivity extends Activity {
	Button back, next, clear;

	ListView av, sv;

	TextView tv;
	View view;
	String name, desc;

	int selectedRow;

	AlertDialog ad;

	ArrayAdapter<Exercise> aa, sa;
	List<Exercise> exercises, sel;

	boolean[] selected;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.exerciselist);
		readExercises();
		setAlertDialog();
		setConnections();
	}

	@Override
	public void onRestart() {
		super.onRestart();
		readExercises();
		setConnections();
	}

	public void onActivityResult(int requestCode, int returnCode, Intent intent) {
		if (returnCode == RESULT_OK) {
			setResult(RESULT_OK);
			ExerciseListActivity.this.finish();
		}
	}

	private void setAlertDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = LayoutInflater.from(this);
		final View layout = inflater.inflate(R.layout.alertdialog, null);
		tv = (TextView) layout.findViewById(R.id.alertText);

		builder.setView(layout)
				.setTitle("View or Delete")
				.setNegativeButton("Delete",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int position) {

								deleteEntry(selectedRow);
							}
						})
				.setPositiveButton("View",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								Intent intent = new Intent(view.getContext(),
										DisplayExerciseActivity.class);
								intent.putExtra("exer", (Exercise)av.getItemAtPosition(selectedRow));
								startActivity(intent);
							}
						});
		ad = builder.create();
	}

	public void deleteEntry(int item) {
		Exercise i = (Exercise) exercises.get(item);
		aa.remove(i);
		// exercises.remove(item);

		write();
	}

	@SuppressWarnings("unchecked")
	private void readExercises() {
		try {
			FileInputStream fis = openFileInput("exercises");
			ObjectInputStream ois = new ObjectInputStream(fis);

			exercises = (ArrayList<Exercise>) ois.readObject();

			ois.close();
			fis.close();

		} catch (Exception e) {

			exercises = new ArrayList<Exercise>();

			createDefaultExercises();

			write();

		}

	}

	private void createDefaultExercises() {
		exercises
				.add(0,
						new Exercise(
								"Push-Ups with Rotation",
								"1. Start by lying on your back with your knees towards your chest and your arms flat on the ground.\n\n" +
								"2. Keeping your back flat throughout the movement kick your legs out and away until they are almost straight.\n\n" +
								"3. Bring your legs back in and repeat for the required number of repetitions.\n\n" +
								"4. If you are unable to keep your back flat on the floor throughout the movement shorten the distance that your legs extend until you get stronger.",
								R.drawable.pushupwithrotation));
		exercises
				.add(0,
						new Exercise(
								"Double Leg Pressouts",
								"1. Start by lying on your back with your knees towards your chest and your arms flat on the ground.\n\n" +
								"2. Keeping your back flat throughout the movement kick your legs out and away until they are almost straight.\n\n" +
								"3. Bring your legs back in and repeat for the required number of repetitions.\n\n" +
								"4. If you are unable to keep your back flat on the floor throughout the movement shorten the distance that your legs extend until you get stronger.",
								R.drawable.doublelegpressouts));
		exercises
				.add(0,
						new Exercise(
								"Hip Thrust",
								"1. Lie on your back with your legs bent 90 degrees at the hip.\n\n" +
								"2. Slowly lift your hips off the floor and towards the ceiling.\n\n" +
								"3. Lower your hips to the floor and repeat for the prescribed number of repetitions.",
								R.drawable.hipthrust));
		exercises
				.add(0,
						new Exercise(
								"Oblique Crunch",
								"1. Start by placing your left foot over your right knee and place your hands at the side of your head.\n\n" +
								"2. Lift your shoulders up off the ground and twist so that your right elbows tries to touch your left knee.\n\n" +
								"3. Return to the starting position and repeat according to the required repetitions.\n\n" +
								"4. Repeat with the other side.",
								R.drawable.obliquecrunch));
		exercises
				.add(0,
						new Exercise(
								"V-Up",
								"1. Start position: Lie back onto floor or bench with knees bent and hands extended towards ceiling. Head should be in a neutral position with a space between chin and chest.\n\n" +
								"2. Leading with the chin and chest towards the ceiling, contract the abdominal and raise shoulders off floor or bench. Also raise legs up towards ceiling and attempt to touch your hands to your feet.\n\n" +
								"3. Return to start position.\n\n",
								R.drawable.vup));
		exercises
				.add(0,
						new Exercise(
								"Sit Ups",
								"1. Start position: Lie back onto floor or bench with knees bent and hands at the side of your head. Keep elbows back and out of sight. Head should be in a neutral position with a space between chin and chest.\n\n" +
								"2. Leading with the chin and chest towards the ceiling, contract the abdominal and raise shoulders off floor or bench until you are seated in an upright position.\n\n" +
								"3. Return to start position.\n\n" +
								"4. Remember to keep head and back in a neutral position. Hyperextension or flexion of either may cause injury.",
								R.drawable.situp));
	}

	private void setConnections() {
		// TODO Auto-generated method stub

		selected = new boolean[exercises.size()];
		for (int i = 0; i < selected.length; i++) {
			selected[i] = false;
		}
		sel = new ArrayList<Exercise>();

		av = (ListView) findViewById(R.id.exerciseListView);
		aa = new ArrayAdapter<Exercise>(this, R.layout.list_view2, exercises);
		aa.setNotifyOnChange(true);
		aa.sort(new Comparator<Exercise>() {

			public int compare(Exercise object1, Exercise object2) {

				return object1.toString().compareToIgnoreCase(
						object2.toString());
			}

		});
		av.setAdapter(aa);

		av.setOnItemClickListener(new OnItemClickListener() {

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

		av.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int row, long arg3) {
				// TODO Auto-generated method stub
				selectedRow = row;

				tv.setText("Would you like to view "
						+ ((Exercise) av.getItemAtPosition(row)).toString()
						+ " exercise card or delete the exercise from the list?");
				view = arg1;
				name = ((Exercise) av.getItemAtPosition(row)).getName();
				desc = ((Exercise) av.getItemAtPosition(row)).getDesc();
				ad.show();
				return true;
			}

		});

		sv = (ListView) findViewById(R.id.selectedExerciseListView);
		sa = new ArrayAdapter<Exercise>(this, R.layout.list_view2, sel);
		sv.setAdapter(sa);
		sa.setNotifyOnChange(true);

		sv.setOnItemClickListener(new OnItemClickListener() {

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

		sv.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int row, long arg3) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(arg1.getContext(),
						DisplayExerciseActivity.class);
				intent.putExtra("exer", (Exercise)sv.getItemAtPosition(row));
				
				startActivity(intent);
				

				return true;
			}

		});

		Button add = (Button) findViewById(R.id.exAdd);
		add.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(v.getContext(), CreateExerciseActivity.class);
				startActivity(intent);
			}

		});

		back = (Button) findViewById(R.id.exBack);
		back.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				ExerciseListActivity.this.finish();
			}

		});

		next = (Button) findViewById(R.id.exNext);
		next.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

				ArrayList<String> exersNames = new ArrayList<String>();

				// CHANGE THIS TO PULL FROM sl LISTVIEW

				while (!sel.isEmpty()) {

					exersNames.add(sel.remove(0).toString());

				}

				// ---------

				if (exersNames.isEmpty()) {
					Toast.makeText(ExerciseListActivity.this,
							"You must choose at least 1 exercise",
							Toast.LENGTH_SHORT).show();
				} else {
					Intent intent = new Intent(v.getContext(), NewWorkoutActivity.class);
					intent.putExtra("names", exersNames);
					startActivityForResult(intent, 31415);
				}
			}

		});

		clear = (Button) findViewById(R.id.exClear);
		clear.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
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

		});
	}

	public void write() {
		try {
			FileOutputStream fos = openFileOutput("exercises",
					Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			oos.writeObject(exercises);

			oos.close();
			fos.close();

		} catch (IOException e) {

			Toast.makeText(ExerciseListActivity.this, "Error: Writing to file",
					Toast.LENGTH_SHORT).show();
		}

	}
}
