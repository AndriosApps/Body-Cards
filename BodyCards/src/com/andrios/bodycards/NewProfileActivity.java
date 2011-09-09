package com.andrios.bodycards;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class NewProfileActivity extends Activity {
	Button done, reset, birthdayBTN;
	RadioButton male, female;
	EditText fName, lName;
	EditText age;
	TextView birthdayTXT;
	String gen;
	ArrayList<Workout> workouts;
	List<Profile> profList;
	Calendar bday, baseDate;
	Profile created;
	AlertDialog ad;
	boolean update = false;
	static final int DATE_DIALOG_ID = 1;
	GoogleAnalyticsTracker tracker;

	int row, mYear, mMonth, mDay;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.newprofileactivity);

		tracker = GoogleAnalyticsTracker.getInstance();

	    // Start the tracker in manual dispatch mode...
	    tracker.start("UA-23366060-1", this);
		
		setConnections();
		getExtra();

	}

	@SuppressWarnings("unchecked")
	private void getExtra() {
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
			
			bday = (Calendar)intent.getSerializableExtra("bday");
			if(bday != null){
				String birthday = "";
				birthday += Integer.toString((bday.get(Calendar.MONTH)+1));
				birthday += "/";
				birthday += Integer.toString(bday.get(Calendar.DAY_OF_MONTH));
				birthday +="/";
				birthday += Integer.toString(bday.get(Calendar.YEAR));
				birthdayTXT.setText(birthday);
				setBirthday(bday);
			}
			
		}
	}
	
	private void setBirthday(Calendar c){
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
	}

	private void setConnections() {
		male = (RadioButton) findViewById(R.id.newProfileMaleRDO);
		female = (RadioButton) findViewById(R.id.newProfileFemaleRDO);
		fName = (EditText) findViewById(R.id.newProfileUserFirstNameTXT);
		lName = (EditText) findViewById(R.id.newProfileUserLastNameTXT);
		age = (EditText) findViewById(R.id.newProfileUserAgeTXT);
	
		birthdayTXT = (TextView) findViewById(R.id.newProfileBirthdayTXT);
		birthdayBTN = (Button) findViewById(R.id.newProfileUserBirthdayBTN);
		reset = (Button) findViewById(R.id.newProfileResetBTN);
		done = (Button) findViewById(R.id.newProfileDoneBTN);
		
		baseDate = Calendar.getInstance();
		baseDate.set(Calendar.YEAR, 1975);
		bday = (Calendar) baseDate.clone();
		setBirthday(bday);
		
		
		//TODO DOES THIS NEED TO GO HERE???? 
		//TODO Answer: Either here, getExtras()or onCreate() makes more sense here
		created = new Profile();
		readProfiles();
		setOnClickListeners();
	}

	private void setOnClickListeners() {
		
		
		reset.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				fName.setText("");
				lName.setText("");
//				age.setText("");
				birthdayTXT.setText("Set Birthday");
				male.setChecked(true);
				gen = "Male";
			}

		});
		birthdayBTN.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				showDialog(DATE_DIALOG_ID);
				
			}
			
		});
		
		done.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				boolean error = false;
				int mn, dy, yr;
				
				
				
				if (isEmpty(fName)) {
					Toast.makeText(NewProfileActivity.this,
							"Error: First Name Field Empty ",
							Toast.LENGTH_SHORT).show();
					error = true;
				}

				if (isEmpty(lName)) {
					lName.setText("");
				}
				//TODO Change this wicket... it's wrong
				if (birthdayTXT.getText().equals("Set Birthday")) {
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
					bd = bday;
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
						int mf;
						if(male.isChecked()){
							mf = 0;
						}else{
							mf = 1;
						}
						tracker.trackEvent(
						            "Use",  // Category
						            "Profile",  // Action
						            "Update Profile", // Label
						            mf);       // Value
						   tracker.dispatch();
					}else{
						int mf;
						if(male.isChecked()){
							mf = 0;
						}else{
							mf = 1;
						}
						tracker.trackEvent(
						            "Use",  // Category
						            "Profile",  // Action
						            "New Profile", // Label
						            mf);       // Value
						   tracker.dispatch();
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
	
	
    @Override
    protected Dialog onCreateDialog(int id) {
            switch (id) {

            case DATE_DIALOG_ID:
                    return new DatePickerDialog(this,
                            mDateSetListener,
                            mYear, mMonth, mDay);
            }
            return null;
    }
    protected void onPrepareDialog(int id, Dialog dialog) {
            switch (id) {

            case DATE_DIALOG_ID:
                    ((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
                    break;
            }
    }    
    private void updateDisplay() {
            birthdayTXT.setText(
                    new StringBuilder()
                    // Month is 0 based so add 1
                    .append(mMonth + 1).append("/")
                    .append(mDay).append("/")
                    .append(mYear).append(" "));
    }
    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

				public void onDateSet(DatePicker view, int year,
						int monthOfYear, int dayOfMonth) {
					mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    bday.set(Calendar.YEAR, mYear);
                    bday.set(Calendar.MONTH, mMonth);
                    bday.set(Calendar.DAY_OF_MONTH, mDay);
                    updateDisplay();
					
				}

          
    };

}
