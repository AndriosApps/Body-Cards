package com.andrios.bodycards;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
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
	static boolean hasTrained;
	AlertDialog ad;
	
	String workoutName;
	GoogleAnalyticsTracker tracker;
	
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
		
		tracker = GoogleAnalyticsTracker.getInstance();

	    // Start the tracker in manual dispatch mode...
	    tracker.start("UA-23366060-1", this);
		tracker.trackPageView("New Deck of Cards Activity");
		
		setAlertDialog();
		
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
		
		numGuestsTXT.setOnFocusChangeListener(new OnFocusChangeListener(){

			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					
					InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					// only will trigger it if no physical keyboard is open

					mgr.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
					
				}else{
					InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					mgr.hideSoftInputFromWindow(numGuestsTXT.getWindowToken(), 0);
				}


				
			}
			
		});
		
	
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
				boolean isCorrect = true;
				int decksize=0;
				int numGuests =0;
				try{
					numGuests = Integer.parseInt(numGuestsTXT.getText().toString().trim());
				}catch(Exception e){
					Toast.makeText(NewDeckOfCardsWorkoutActivity.this,
							"Can only have integers in Guest User field",
							Toast.LENGTH_SHORT).show();
					isCorrect = false;
				}
				
				if(quarterRDO.isChecked()){
					decksize = 1;
				}else if(halfRDO.isChecked()){
					decksize = 2;
				}
				
				if(numGuests+chosenProfile < 1) {
					Toast.makeText(NewDeckOfCardsWorkoutActivity.this,
							"Must choose at least one profile or guest user",
							Toast.LENGTH_SHORT).show();
					isCorrect = false;
				}
				
				if(isCorrect) {
					tracker.dispatch();
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
	
	  @Override
	  protected void onDestroy() {
	    super.onDestroy();
	    // Stop the tracker when it is no longer needed.
	    tracker.stop();
	  }
		private void setAlertDialog() {
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			LayoutInflater inflater = LayoutInflater.from(this);
			final View layout = inflater.inflate(R.layout.ratealertdialog, null);
			final CheckBox rateCheck = (CheckBox) layout.findViewById(R.id.rateAlertDialogCheckBox);
			final TextView topText = (TextView) layout.findViewById(R.id.ratingTextView);
			final TextView middleText = (TextView) layout.findViewById(R.id.ratingTextView2);
			
			rateCheck.setText("Don't show this again");
			topText.setText("Using a simple deck of playing cards to develop a random workout served as our inspiration for Body Cards.");
			middleText.setText("Click 'Help' below for more information or 'Continue' to proceed to the workout");
			builder.setView(layout)
					.setTitle("About Deck of Cards!")
					.setPositiveButton("Help", new DialogInterface.OnClickListener(){
						
						
						public void onClick(DialogInterface dialog, int which) {
								
							if(rateCheck.isChecked()){
								hasTrained = true;
								writeTrained();
								tracker.trackEvent(
							            "Clicks",  // Category
							            "Training",  // Action
							            "Deck of Cards - Last Time", // Label
							            1);       // Value
								
							  
							  
							}else{
								hasTrained = false;
								writeTrained();
								tracker.trackEvent(
							            "Clicks",  // Category
							            "Training",  // Action
							            "Deck of Cards", // Label
							            1);       // Value
							}
							
							Intent intent = new Intent(layout.getContext(), HelpGenericActivity.class);
							intent.putExtra("ID", 2);
							startActivity(intent);
							
							
							
							
						}
					})
					.setNeutralButton("Continue", new DialogInterface.OnClickListener(){
						
						public void onClick(DialogInterface dialog, int which) {
							if(rateCheck.isChecked()){
								hasTrained = true;
								writeTrained();
								tracker.trackEvent(
							            "Clicks",  // Category
							            "Training",  // Action
							            "Deck of Cards - Last Time - No View", // Label
							            1);       // Value
							}else{
								hasTrained = false;
								writeTrained();
								tracker.trackEvent(
							            "Clicks",  // Category
							            "Training",  // Action
							            "Deck of Cards - No View", // Label
							            1);       // Value
							}
							
						}
					});
			ad = builder.create();
			readDeckTraining();
		}
		
		private void readDeckTraining() {
			
			try {
				
				FileInputStream fis = NewDeckOfCardsWorkoutActivity.this.openFileInput("deckTraining");
				ObjectInputStream ois = new ObjectInputStream(fis);
				
				try{
					hasTrained = (boolean) ois.readBoolean();
					System.out.println("TRY");
				}catch(Exception e){
					hasTrained = false;
					System.out.println("Catch");
				}
				
				System.out.println("Read HAS TRAINED: " + hasTrained);
				if(hasTrained){
					ad.show();
				}

				ois.close();
				fis.close();

			} catch (Exception e) {
				System.out.println("ERROR READING");
				ad.show();
				

				

			}
			
			
		}
		
		private void writeTrained() {
			System.out.println("Write Trainined");
			try {
				FileOutputStream fos = NewDeckOfCardsWorkoutActivity.this.openFileOutput("deckTraining",
						Context.MODE_PRIVATE);
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				System.out.println("HAS TRAINED: " + hasTrained);
				oos.writeBoolean(hasTrained);
				oos.close();
				fos.close();

			} catch (IOException e) {
				System.out.println("HAS TRAINED: catch");
			
			}

			
		}
}
