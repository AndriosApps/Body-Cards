package com.andrios.bodycards;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class NewProfileActivity extends Activity {
	Button back, done, reset;
	RadioButton male, female;
	EditText fName, lName, age;
	String gen;
	ArrayList<Workout> workouts;
	List<Profile> profList;

	Profile created;

	boolean update = false;

	int row;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.newprofile);

		setConnections();
		getExtra();

	}

	@SuppressWarnings("unchecked")
	private void getExtra() {
		// TODO Auto-generated method stub
		Intent intent = this.getIntent();
		update = intent.getBooleanExtra("update", false);
		if (update) {
			fName.setText(intent.getStringExtra("fname"));
			lName.setText(intent.getStringExtra("lname"));
			age.setText(Integer.toString(intent.getIntExtra("age", 0)));
			if (intent.getStringExtra("gender").equals("Male")) {
				male.setChecked(true);
				female.setChecked(false);
			} else {
				female.setChecked(true);
				male.setChecked(false);
			}

			created.setCalDate((Calendar) intent
					.getSerializableExtra("calDate"));
			created.setCreateDate(intent.getStringExtra("creaDate"));
			row = intent.getIntExtra("row", -1);
			workouts = (ArrayList<Workout>) intent
					.getSerializableExtra("workouts");
		}
	}

	private void setConnections() {
		// TODO Auto-generated method stub
		male = (RadioButton) findViewById(R.id.male);
		female = (RadioButton) findViewById(R.id.female);
		fName = (EditText) findViewById(R.id.userFirstName);
		lName = (EditText) findViewById(R.id.userLastName);
		age = (EditText) findViewById(R.id.userAge);
		created = new Profile();
		readProfiles();

		back = (Button) findViewById(R.id.npBack);
		back.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				NewProfileActivity.this.finish();
			}

		});

		reset = (Button) findViewById(R.id.npReset);
		reset.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				fName.setText("");
				lName.setText("");
				age.setText("");
				male.setChecked(true);
				gen = "Male";
			}

		});

		done = (Button) findViewById(R.id.npDone);
		done.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean error = false;
				;
				if (isEmpty(fName)) {
					Toast.makeText(NewProfileActivity.this,
							"Error: First Name Field Empty ",
							Toast.LENGTH_SHORT).show();
					error = true;
				}

				if (isEmpty(lName)) {
					Toast.makeText(NewProfileActivity.this,
							"Error: Last Name Field Empty ", Toast.LENGTH_SHORT)
							.show();
					error = true;
				}

				if (isEmpty(age)) {
					Toast.makeText(NewProfileActivity.this, "Error: Age Field Empty ",
							Toast.LENGTH_SHORT).show();
					error = true;
				}

				if (!error) {

					if (male.isChecked())
						gen = "Male";
					else
						gen = "Female";

					created.setNew(fName.getText().toString(), lName.getText()
							.toString(), gen, Integer.parseInt(age.getText()
							.toString()));

					if (update) {

						Intent done = new Intent();
						profList.remove(row);
						created.workoutList = workouts;
						done.putExtra("row", row);
						done.putExtra("profile", created);
						NewProfileActivity.this.setResult(RESULT_OK, done);
						Toast.makeText(NewProfileActivity.this, "User Profile Updated",
								Toast.LENGTH_SHORT).show();
					}

					profList.add(created);

					try {
						FileOutputStream fos = openFileOutput("profiles",
								Context.MODE_PRIVATE);
						ObjectOutputStream oos = new ObjectOutputStream(fos);

						oos.writeObject(profList);

						oos.close();
						fos.close();

						if(!update) {
							Toast.makeText(NewProfileActivity.this, "User Profile Created",
									Toast.LENGTH_SHORT).show();
						}

						NewProfileActivity.this.finish();
					} catch (IOException e) {
						System.out.println("Test: " + e);
						Toast.makeText(NewProfileActivity.this,
								"Error: Writing to file", Toast.LENGTH_SHORT)
								.show();
					}
				}

			}

		});
	}

	@SuppressWarnings("unchecked")
	private void readProfiles() {
		try {
			FileInputStream fis = openFileInput("profiles");
			ObjectInputStream ois = new ObjectInputStream(fis);

			profList = (ArrayList<Profile>) ois.readObject();
			ois.close();
			fis.close();
		} catch (Exception e) {
			profList = new ArrayList<Profile>();
		}
	}

	public boolean isEmpty(EditText field) {
		return field.getText().toString().equals("");
	}
}
