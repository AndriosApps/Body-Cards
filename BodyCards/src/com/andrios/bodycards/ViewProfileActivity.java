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

public class ViewProfileActivity extends Activity {
	Button backBTN, newProfileBTN;
	ListView listView;
	ArrayList<Profile> profileList;
	ArrayAdapter<Profile> profileListAdapter;
	AlertDialog ad;
	int selectedRow;

	//TODO rename boolean button to something meaningfull. 
	boolean button;

	TextView tv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.viewprofileactivity);
		
		getExtras();
		setConnections();
		setAlertDialog();
	}

	private void getExtras() {
		Intent intent = this.getIntent();
		if(intent.getIntExtra("whichOne", 0) == 1){
			Intent nextIntent = new Intent(this.getBaseContext(), NewProfileActivity.class);
			startActivity(nextIntent);
		}
		
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
								if (button)
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
	public void onRestart() {
		super.onRestart();
		setConnections();
	}

	@SuppressWarnings("unchecked")
	private void readProfiles() {
		try {
			FileInputStream fis = openFileInput("profiles");
			ObjectInputStream ois = new ObjectInputStream(fis);

			profileList = (ArrayList<Profile>) ois.readObject();
			ois.close();
			fis.close();

		} catch (Exception e) {

			profileList = new ArrayList<Profile>();
		}

	}

	@Override
	public void onActivityResult(int requestCode, int returnCode, Intent intent) {
		if (returnCode == RESULT_OK) {
			setConnections();
		}
	}

	private void setConnections() {
		readProfiles();

		listView = (ListView) findViewById(R.id.viewProfileProfileListView);
		profileListAdapter = new ArrayAdapter<Profile>(this, R.layout.list_view2, profileList);
		profileListAdapter.setNotifyOnChange(true);
		profileListAdapter.sort(new Comparator<Profile>() {

			public int compare(Profile object1, Profile object2) {

				return object1.getLastName().compareToIgnoreCase(
						object2.getLastName());
			}

		});

		write();
		
		listView.setAdapter(profileListAdapter);

		backBTN = (Button) findViewById(R.id.viewProfileBackBTN);


		newProfileBTN = (Button) findViewById(R.id.viewProfileNewProfileBTN);
		
		setOnClickListeners();
	}

	private void setOnClickListeners() {

		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View theView,
					int position, long arg3) {

				Intent intent = new Intent(theView.getContext(),
						DisplayProfileActivity.class);
				intent.putExtra("obj", profileList.get(position));
				intent.putExtra("row", position);
				intent.putExtra("profs", profileList);
				startActivityForResult(intent, 0);

			}

		});

		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View theView,
					int row, long arg3) {
				if(profileList.get(row).isWidget){
					Toast.makeText(ViewProfileActivity.this, "Delete widget for this profile first",
							Toast.LENGTH_SHORT).show();
				}else{
					tv.setText("Delete " + profileList.get(row).toString()
							+ "'s profile?");
					selectedRow = row;
					button = true;
					ad.show();
				}
				
				return true;
			}

		});
		
		backBTN.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				ViewProfileActivity.this.finish();
			}

		});
		newProfileBTN.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Intent intent = new Intent(v.getContext(), NewProfileActivity.class);
				startActivity(intent);

			}

		});
		
	}

	public void deleteEntry(int row) {
		profileList.remove(row);
		write();
	}

	public void write() {
		try {
			FileOutputStream fos = openFileOutput("profiles",
					Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			oos.writeObject(profileList);

			oos.close();
			fos.close();

		} catch (IOException e) {

			Toast.makeText(ViewProfileActivity.this, "Error: Writing to file",
					Toast.LENGTH_SHORT).show();
		}

		profileListAdapter.notifyDataSetChanged();
	}
}
