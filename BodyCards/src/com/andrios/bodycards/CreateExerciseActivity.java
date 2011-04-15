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

public class CreateExerciseActivity extends Activity {

	Exercise exercise;

	String originalName;
	Button back, reset, done;
	EditText nameTXT, descTXT, multiplierTXT;
	boolean isUpdate;

	ArrayList<Exercise> exerciseList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.createexercise);
		
		getExtras();
		readExercises();
		setConnections();
		

	}

	private void getExtras() {
		isUpdate = false;
		Intent intent = this.getIntent();
		if((Exercise) intent.getSerializableExtra("exercise") != null){
			exercise = (Exercise) intent.getSerializableExtra("exercise");
			originalName = exercise.getName();
			isUpdate = true;
		}
		
		
	}

	@SuppressWarnings("unchecked")
	private void readExercises() {
		// TODO Auto-generated method stub
		try {
			FileInputStream fis = openFileInput("exercises");
			ObjectInputStream ois = new ObjectInputStream(fis);

			exerciseList = (ArrayList<Exercise>) ois.readObject();
			ois.close();
			fis.close();
		} catch (Exception e) {
			exerciseList = new ArrayList<Exercise>();
		}
	}

	private void setConnections() {
		// TODO Auto-generated method stub
		nameTXT = (EditText) findViewById(R.id.newExName);
		descTXT = (EditText) findViewById(R.id.newExDesc);
		multiplierTXT = (EditText) findViewById(R.id.newExMultiplier);
		
		back = (Button) findViewById(R.id.ceBack);
		reset = (Button) findViewById(R.id.ceReset);
		done = (Button) findViewById(R.id.ceDone);
		
		if(isUpdate){
			nameTXT.setText(exercise.getName());
			descTXT.setText(exercise.getDesc());
			multiplierTXT.setText(Double.toString(exercise.getMultiplier()));
		}
		
		setOnClickListeners();

	}
	
	private void setOnClickListeners(){
		back.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

				CreateExerciseActivity.this.finish();
			}

		});

		
		reset.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				nameTXT.setText("");
				descTXT.setText("");
				multiplierTXT.setText("1.0");
			}

		});

		
		done.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				double multiplier = 1.0;
				
				
				
				
				if (nameTXT.getText().toString().equals("")
						|| descTXT.getText().toString().equals("") || multiplierTXT.getText().toString().equals("")) {
					Toast.makeText(CreateExerciseActivity.this,
							"You must fill in all fields", Toast.LENGTH_SHORT)
							.show();
				} else {
					multiplier = Double.parseDouble(multiplierTXT.getText().toString().trim());
					exercise = new Exercise(nameTXT.getText().toString(), descTXT
							.getText().toString(),
							R.drawable.nopic, multiplier);
					
					sort(exerciseList);
					if(isUpdate){
						int index = checkDupes(originalName);
						if(index != -1){
							exerciseList.remove(index);
						}
						
						Toast.makeText(CreateExerciseActivity.this, "Exercise Updated",
								Toast.LENGTH_SHORT).show();
						
					}else{
						Toast.makeText(CreateExerciseActivity.this, "Exercise Created",
								Toast.LENGTH_SHORT).show();
						
					}
					exerciseList.add(exercise);
					
					
					write();
					CreateExerciseActivity.this.finish();
					
				}
			}

			private void sort(ArrayList<Exercise> exerciseList) {
				// TODO Auto-generated method stub
				if (exerciseList.size() == 0)
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

			oos.writeObject(exerciseList);

			oos.close();
			fos.close();

		} catch (IOException e) {

			Toast.makeText(CreateExerciseActivity.this, "Error: Writing to file",
					Toast.LENGTH_SHORT).show();
		}

	}
	
	private int checkDupes(String name){
		for(int i = 0; i < exerciseList.size(); i++ ){
			if(name.equals(exerciseList.get(i).getName())){
				return i;
			}
		}
		
		return -1;
	}
	
	@Override
	public void finish(){
		System.out.println("FINISH");
		Intent intent = new Intent();
			intent.putExtra("exercise", exercise);
			
			setResult(RESULT_OK, intent);
		
		super.finish();
	}

}
