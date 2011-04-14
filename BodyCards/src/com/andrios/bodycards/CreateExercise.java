package com.andrios.bodycards;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
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
import android.widget.Toast;

public class CreateExercise extends Activity {

	Exercise newExer;

	Button back, reset, done;
	EditText name, desc, multiplierTXT;

	ArrayList<Exercise> exer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.createexercise);
		
		readExercises();
		setConnections();
		getExtras();

	}

	private void getExtras() {
		Intent intent = this.getIntent();
		name.setText(intent.getStringExtra("name"));
		desc.setText(intent.getStringExtra("desc"));
	}

	@SuppressWarnings("unchecked")
	private void readExercises() {
		// TODO Auto-generated method stub
		try {
			FileInputStream fis = openFileInput("exercises");
			ObjectInputStream ois = new ObjectInputStream(fis);

			exer = (ArrayList<Exercise>) ois.readObject();
			ois.close();
			fis.close();
		} catch (Exception e) {
			exer = new ArrayList<Exercise>();
		}
	}

	private void setConnections() {
		// TODO Auto-generated method stub
		name = (EditText) findViewById(R.id.newExName);
		desc = (EditText) findViewById(R.id.newExDesc);
		multiplierTXT = (EditText) findViewById(R.id.newExMultiplier);
		
		back = (Button) findViewById(R.id.ceBack);
		reset = (Button) findViewById(R.id.ceReset);
		done = (Button) findViewById(R.id.ceDone);
		
		setOnClickListeners();

	}
	
	private void setOnClickListeners(){
		back.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

				CreateExercise.this.finish();
			}

		});

		
		reset.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				name.setText("");
				desc.setText("");
				multiplierTXT.setText("1.0");
			}

		});

		
		done.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				double multiplier = 1.0;
				if (name.getText().toString().equals("")
						|| desc.getText().toString().equals("") || multiplierTXT.getText().toString().equals("")) {
					Toast.makeText(CreateExercise.this,
							"You must fill in all fields", Toast.LENGTH_SHORT)
							.show();
				} else {
					multiplier = Double.parseDouble(multiplierTXT.getText().toString().trim());
					newExer = new Exercise(name.getText().toString(), desc
							.getText().toString(),
							R.drawable.nopic, multiplier);
					exer.add(exer.size(), newExer);
					sort(exer);
					write();
					Toast.makeText(CreateExercise.this, "Exercise Created",
							Toast.LENGTH_SHORT).show();
					CreateExercise.this.finish();
				}
			}

			private void sort(ArrayList<Exercise> exer) {
				// TODO Auto-generated method stub
				if (exer.size() == 0)
					return;
				else {

				}
			}

		});
	}

	protected void write() {
		// TODO Auto-generated method stub
		try {
			FileOutputStream fos = openFileOutput("exercises",
					Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			oos.writeObject(exer);

			oos.close();
			fos.close();

		} catch (IOException e) {

			Toast.makeText(CreateExercise.this, "Error: Writing to file",
					Toast.LENGTH_SHORT).show();
		}

	}

}
