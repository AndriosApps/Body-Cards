package com.andrios.bodycards;

import java.util.ArrayList;
import java.util.Calendar;

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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class FinishedActivity extends Activity {
	
	boolean hasRated;
	AlertDialog ad;
	ArrayList<Profile> selectedProfiles;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.finished);
		
		getExtras();
		setConnections();
		
	}

	private void getExtras() {
		hasRated = AndriosPatcher.readRated(FinishedActivity.this);
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, -30);
		if(!hasRated){
			setAlertDialog();
			Intent intent = this.getIntent();
			selectedProfiles = (ArrayList<Profile>) intent.getSerializableExtra("profiles");
			for(int i = 0; i< selectedProfiles.size(); i++){
				if(selectedProfiles.get(i).creationDate.before(c)){
					ad.show();
					break;
				}
			}
		
		}
	}

	private void setConnections() {
		myListener l = new myListener();
		TextView tv = (TextView) findViewById(R.id.finishText);
		tv.setOnClickListener(l);

		ImageView iv = (ImageView) findViewById(R.id.finishPic);
		iv.setOnClickListener(l);

		TextView cong = (TextView) findViewById(R.id.congrats);
		cong.setOnClickListener(l);

		TextView comp = (TextView) findViewById(R.id.complete);
		comp.setOnClickListener(l);
	}

	class myListener implements OnClickListener {

		public void onClick(View v) {
			Intent intent = new Intent();
			
			setResult(RESULT_OK, intent);
		
			FinishedActivity.this.finish();
		}
	}
	
	private void setAlertDialog() {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = LayoutInflater.from(this);
		final View layout = inflater.inflate(R.layout.welcomealertdialog, null);
		final CheckBox welcomeCheck = (CheckBox) layout.findViewById(R.id.welcomeAlertDialogCheckBox);
		
		builder.setView(layout)
				.setTitle("You have been using Body Cards for a while, Please take a moment to rate it!")
				.setPositiveButton("OK", new DialogInterface.OnClickListener(){
					
					
					public void onClick(DialogInterface dialog, int which) {
							
						hasRated = true;
						AndriosPatcher.setRated(FinishedActivity.this);
						
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setData(Uri.parse("market://details?id=com.andrios.bodycards"));
						startActivity(intent);
						
						
					}
				})
				.setNegativeButton("No Thanks", new DialogInterface.OnClickListener(){

					public void onClick(DialogInterface dialog, int which) {
						if(welcomeCheck.isChecked()){
							hasRated = true;
							AndriosPatcher.setRated(FinishedActivity.this);
						}
						
					}
					
				});
		ad = builder.create();
	}
}
