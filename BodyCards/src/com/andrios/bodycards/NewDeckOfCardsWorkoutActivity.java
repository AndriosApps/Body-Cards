package com.andrios.bodycards;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Comparator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class NewDeckOfCardsWorkoutActivity extends Activity  {

	int chosenProfile;
	EditText numGuestsTXT;
	ListView availableProfilesLV, chosenProfilesLV;
	ArrayList<Profile> availableProfilesList, selectedProfilesList;
	ArrayAdapter<Profile> availableProfilesAdapter, chosenProfilesAdapter;
	Button backBTN, resetBTN, doneBTN;
	RadioButton quarterRDO, halfRDO, fullRDO;
	
	String workoutName;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.newdeckofcardsworkoutactivity);
		
		readProfiles();
		getExtras();
		setConnections();
	}

	private void setConnections() {
		numGuestsTXT = (EditText) findViewById(R.id.newQuickWorkoutActivityNumberGuestsTXT);
		availableProfilesLV = (ListView) findViewById(R.id.newQuickWorkoutActivityAvailableProfilesListView);
		chosenProfilesLV = (ListView) findViewById(R.id.newQuickWorkoutActivityChosenProfilesListView);
		backBTN = (Button) findViewById(R.id.newQuickWorkoutActivityBackBTN);
		resetBTN = (Button) findViewById(R.id.newQuickWorkoutActivityResetBTN);
		doneBTN = (Button) findViewById(R.id.newQuickWorkoutActivityDoneBTN);
		quarterRDO = (RadioButton) findViewById(R.id.newDeckOfCardsQuarterRDO);
		halfRDO = (RadioButton) findViewById(R.id.newDeckOfCardsHalfRDO);
		fullRDO = (RadioButton) findViewById(R.id.newDeckOfCardsFullRDO);
		
		chosenProfile = 0;
		
	
		//Set array adapter for availableProfilesLV
		availableProfilesAdapter = new ArrayAdapter<Profile>(this, R.layout.list_view2, availableProfilesList);
		availableProfilesLV.setAdapter(availableProfilesAdapter);
		availableProfilesAdapter.sort(new Comparator<Profile>() {

			public int compare(Profile object1, Profile object2) {
			
				return object1.getLastName().compareToIgnoreCase(
						object2.getLastName());
			}

		});
		availableProfilesAdapter.setNotifyOnChange(true);
		
		//Set array adapter for chosendProfilesLV
		selectedProfilesList = new ArrayList<Profile>();
		chosenProfilesAdapter = new ArrayAdapter<Profile>(this, R.layout.list_view2, selectedProfilesList);
		chosenProfilesLV.setAdapter(chosenProfilesAdapter);
		chosenProfilesAdapter.sort(new Comparator<Profile>() {

			public int compare(Profile object1, Profile object2) {
				return object1.getLastName().compareToIgnoreCase(
						object2.getLastName());
			}

		});
		chosenProfilesAdapter.setNotifyOnChange(true);
		
		
		
		setOnClickListeners();
	}

	private void setOnClickListeners() {
		availableProfilesLV.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int row,
					long arg3) {

				chosenProfile++;
				Profile r = (Profile) availableProfilesAdapter.getItem(row);
				availableProfilesAdapter.remove(r);
				chosenProfilesAdapter.add(r);
				chosenProfilesAdapter.sort(new Comparator<Profile>() {

					public int compare(Profile object1, Profile object2) {
						return object1.getLastName().compareToIgnoreCase(
								object2.getLastName());
					}

				});
			}

		});
		
		chosenProfilesLV.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int row,
					long arg3) {

				chosenProfile--;
				Profile r = (Profile) chosenProfilesAdapter.getItem(row);
				chosenProfilesAdapter.remove(r);
				availableProfilesAdapter.add(r);
				availableProfilesAdapter.sort(new Comparator<Profile>() {

					public int compare(Profile object1, Profile object2) {
						return object1.getLastName().compareToIgnoreCase(
								object2.getLastName());
					}

				});
			}

		});//chosenProfilesLV.setOnItemClickListener
		
		backBTN.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				NewDeckOfCardsWorkoutActivity.this.finish();
			}

		});//backBTN.setOnClickListener
		
		resetBTN.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
		
				numGuestsTXT.setText("0");
				
				for(int i = 0; i < chosenProfilesAdapter.getCount(); i++ ){
					chosenProfile--;
					Profile r = (Profile) chosenProfilesAdapter.getItem(i);
					chosenProfilesAdapter.remove(r);
					availableProfilesAdapter.add(r);
					availableProfilesAdapter.sort(new Comparator<Profile>() {

						public int compare(Profile object1, Profile object2) {
							 
							return object1.getLastName().compareToIgnoreCase(
									object2.getLastName());
						}

					});
				}
			}

		});//resetBTN.setOnClickListener
		
		doneBTN.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				
				int decksize=0;
				int numGuests = Integer.parseInt(numGuestsTXT.getText().toString().trim());

				if(quarterRDO.isChecked()){
					decksize = 1;
				}else if(halfRDO.isChecked()){
					decksize = 2;
				}
				
				if(numGuests+chosenProfile < 1) {
					Toast.makeText(NewDeckOfCardsWorkoutActivity.this,
							"Must choose at least one profile or guest user",
							Toast.LENGTH_SHORT).show();
				}else {

					Intent deckworkout = new Intent(v.getContext(),
							DeckOfCardsWorkoutActivity.class);
					
					deckworkout.putExtra("peeps", numGuests+chosenProfile);
					deckworkout.putExtra("profilesU", availableProfilesList);
					deckworkout.putExtra("profiles", selectedProfilesList);
					deckworkout.putExtra("decksize", decksize);
					deckworkout.putExtra("workoutName", workoutName);
					startActivityForResult(deckworkout, 31415);
				}

				
			}

		});//doneBTN.setOnClickListener
		
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

			availableProfilesList = (ArrayList<Profile>) ois.readObject();
			ois.close();
			fis.close();

		} catch (Exception e) {

			availableProfilesList = new ArrayList<Profile>();
		}

	}
	
	public void onActivityResult(int requestCode, int returnCode, Intent intent) {
		if (returnCode == RESULT_OK) {
			setResult(RESULT_OK);
			NewDeckOfCardsWorkoutActivity.this.finish();
		}
	}
}
