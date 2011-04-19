package com.andrios.bodycards;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayProfileActivity extends Activity {
	Button edit, back;
	Profile profile;
	ListView lv;
	ArrayAdapter<Workout> aa;
	ArrayList<Profile> profs;
	int row, selectedRow;
	boolean update = false;
	TextView tv;

	AlertDialog ad;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.displayprofile);
		getExtra();
		setConnections();
		setProfileText();
		setAlertDialog();

	}

	private void setAlertDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = LayoutInflater.from(this);
		final View layout = inflater.inflate(R.layout.alertdialog, null);

		tv = (TextView) layout.findViewById(R.id.alertText);

		builder.setView(layout)
				.setTitle("Delete Confirmation")
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int position) {

								deleteEntry(selectedRow);

							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		ad = builder.create();
	}

	@Override
	public void onActivityResult(int requestCode, int returnCode, Intent intent) {
		if (returnCode == RESULT_OK) {
			profile = (Profile) intent.getSerializableExtra("profile");
			DisplayProfileActivity.this.setResult(RESULT_OK, intent);
			setProfileText();
			setConnections();
			update = true;
		}
	}

	private void setProfileText() {
		TextView name = (TextView) findViewById(R.id.profName);
		name.setText(profile.toString());

		profile.updateAge();
		TextView age = (TextView) findViewById(R.id.profAge);
		age.setText("Age: " + Integer.toString(profile.getAge()));

		TextView numworkouts = (TextView) findViewById(R.id.profWorkouts);
		numworkouts.setText("Total Workouts: "
				+ Integer.toString(profile.getNumWorkouts()));

		TextView create = (TextView) findViewById(R.id.profCreation);
		create.setText("Profile Created on " + profile.getCreationDate());

		ImageView iv = (ImageView) findViewById(R.id.profImg);

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
		System.out.println("test: " + y + m + d);
		if (y > 0)
			return false;
		if (m > 0)
			return false;
		if (d > 7)
			return false;
		return true;
	}

	@SuppressWarnings("unchecked")
	private void getExtra() {

		Intent intent = this.getIntent();

		profile = (Profile) intent.getSerializableExtra("obj");
		row = intent.getIntExtra("row", -1);
		profs = (ArrayList<Profile>) intent.getSerializableExtra("profs");

	}

	private void setConnections() {
		lv = (ListView) findViewById(R.id.profWorkoutList);
		aa = new ArrayAdapter<Workout>(this, R.layout.list_view2,
				profile.getWorkouts());
		lv.setAdapter(aa);

		aa.notifyDataSetChanged();

		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View v, int rw,
					long arg3) {
				Intent intent = new Intent(v.getContext(), DisplayWorkoutActivity.class);
				intent.putExtra("profile", profile);
				intent.putExtra("row", rw);
				startActivity(intent);
			}

		});

		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View v, int rw,
					long arg3) {
				selectedRow = rw;
				tv.setText("Delete this workout?");
				ad.show();
				return true;
			}

		});

		back = (Button) findViewById(R.id.dpBack);
		back.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				DisplayProfileActivity.this.setResult(RESULT_OK);

				DisplayProfileActivity.this.finish();
			}

		});

		edit = (Button) findViewById(R.id.dpEdit);
		edit.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), NewProfileActivity.class);
				intent.putExtra("fname", profile.getFirstName());
				intent.putExtra("lname", profile.getLastName());
				intent.putExtra("age", profile.getAge());
				intent.putExtra("bday", profile.birthday);
				intent.putExtra("calDate", profile.getCalendar());
				intent.putExtra("creaDate", profile.getCreationDate());
				intent.putExtra("gender", profile.getGender());
				intent.putExtra("update", true);
				intent.putExtra("row", row);
				intent.putExtra("workouts", profile.getWorkouts());
				startActivityForResult(intent, 0);

			}

		});
	}

	public void deleteEntry(int r) {
		profile.getWorkouts().remove(r);
		profs.get(row).getWorkouts().remove(r);
		write();
	}

	public void write() {
		try {
			FileOutputStream fos = openFileOutput("profiles",
					Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			oos.writeObject(profs);

			oos.close();
			fos.close();

		} catch (IOException e) {

			Toast.makeText(DisplayProfileActivity.this, "Error: Writing to file",
					Toast.LENGTH_SHORT).show();
		}

		aa.notifyDataSetChanged();
		setProfileText();
	}

}
