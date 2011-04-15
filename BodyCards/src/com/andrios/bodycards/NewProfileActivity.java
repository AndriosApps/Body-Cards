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
	EditText fName, lName, month, year, day;
	EditText age;
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
		setContentView(R.layout.newprofileactivity);

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
//			age.setText(Integer.toString(intent.getIntExtra("age", 0)));
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
			
			Calendar bday = (Calendar)intent.getSerializableExtra("bday");
			month.setText(Integer.toString((bday.get(Calendar.MONTH)+1)));
			day.setText(Integer.toString(bday.get(Calendar.DAY_OF_MONTH)));
			year.setText(Integer.toString(bday.get(Calendar.YEAR)));
		}
	}

	private void setConnections() {
		// TODO Auto-generated method stub
		male = (RadioButton) findViewById(R.id.newProfileMaleRDO);
		female = (RadioButton) findViewById(R.id.newProfileFemaleRDO);
		fName = (EditText) findViewById(R.id.newProfileUserFirstNameTXT);
		lName = (EditText) findViewById(R.id.newProfileUserLastNameTXT);
		age = (EditText) findViewById(R.id.newProfileUserAgeTXT);
		month = (EditText) findViewById(R.id.newProfileUserBirthdayMonthTXT);
		day = (EditText) findViewById(R.id.newProfileUserBirthdayDayTXT);
		year = (EditText) findViewById(R.id.newProfileUserBirthdayYearTXT);
		back = (Button) findViewById(R.id.newProfileBackBTN);
		reset = (Button) findViewById(R.id.newProfileResetBTN);
		done = (Button) findViewById(R.id.newProfileDoneBTN);
		
		//TODO DOES THIS NEED TO GO HERE????
		created = new Profile();
		readProfiles();
		setOnClickListeners();
	}

	private void setOnClickListeners() {
		back.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				NewProfileActivity.this.finish();
			}

		});
		
		reset.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				fName.setText("");
				lName.setText("");
//				age.setText("");
				month.setText("");
				day.setText("");
				year.setText("");
				male.setChecked(true);
				gen = "Male";
			}

		});
		
		done.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean error = false;
				int mn, dy, yr;
				
				mn = Integer.parseInt(month.getText().toString());
				dy = Integer.parseInt(day.getText().toString());
				yr = Integer.parseInt(year.getText().toString());
				
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

				if (isEmpty(year) || isEmpty(day) || isEmpty(month)) {
					Toast.makeText(NewProfileActivity.this, "Error: Birthday Field Incomplete ",
							Toast.LENGTH_SHORT).show();
					error = true;
				}

				if (!error) {

					if (male.isChecked())
						gen = "Male";
					else
						gen = "Female";

					Calendar bd = Calendar.getInstance();
					bd.set(yr, mn-1, dy);
					created.setNew(fName.getText().toString(), lName.getText()
							.toString(), gen, bd);

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
