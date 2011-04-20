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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

public class CreateExerciseActivity extends Activity {

	Exercise exercise;

	String originalName;
	Button back, reset, done;
	EditText nameTXT, descTXT, multiplierTXT;
	boolean isUpdate;
	RadioButton yes, no;
	Spinner muscleGroup;
	
	String[] mgOptions = {"Hamstrings", "Calves", "Chest", "Back", "Shoulders", "Triceps", "Biceps", "Forearms", "Trapezius", "Abs"};
	int selectedMuscle;

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
		nameTXT = (EditText) findViewById(R.id.newExName);
		descTXT = (EditText) findViewById(R.id.newExDesc);
		multiplierTXT = (EditText) findViewById(R.id.newExMultiplier);
		
		back = (Button) findViewById(R.id.ceBack);
		reset = (Button) findViewById(R.id.ceReset);
		done = (Button) findViewById(R.id.ceDone);
		
		yes = (RadioButton) findViewById(R.id.newExTimedYesRadioBTN);
		no = (RadioButton) findViewById(R.id.newExTimedNoRadioBTN);
		
		muscleGroup = (Spinner) findViewById(R.id.newExMuscleGroupSpinner);
		
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
	            this, R.array.muscleGroupChoices, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    muscleGroup.setAdapter(adapter);

		muscleGroup.setOnItemSelectedListener(new myOnItemSelectedListener());
		
		if(isUpdate){
			nameTXT.setText(exercise.getName());
			descTXT.setText(exercise.getDesc());
			multiplierTXT.setText(Double.toString(exercise.getMultiplier()));
			if(exercise.getIsTimed()) {
				yes.setChecked(true);
			} else {
				no.setChecked(true);
			}
			
			int position;
			String mg = exercise.getMuscleGroup();
			for(position=0;position<mgOptions.length;position++) {
				if(mg.equals(mgOptions[position])) {
					break;
				}
			}
			
			muscleGroup.setSelection(position);
			
		}
		
		setOnClickListeners();

	}
	
	private class myOnItemSelectedListener implements OnItemSelectedListener  {
		
		public void onItemSelected(AdapterView<?> parent,
	        View view, int pos, long id) {
			selectedMuscle = pos;
	    }
		
		
	    public void onNothingSelected(@SuppressWarnings("rawtypes") AdapterView parent) {
	      selectedMuscle = 0;
	    }

	}
	
	private void setOnClickListeners(){
		back.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				CreateExerciseActivity.this.finish();
			}

		});

		
		reset.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				nameTXT.setText("");
				descTXT.setText("");
				multiplierTXT.setText("1.0");
			}

		});

		
		done.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				double multiplier = 1.0;
				boolean timed;
				
				if(yes.isChecked()) {
					timed = true;
				} else {
					timed = false;
				}
				
				String mg = muscleGroup.getItemAtPosition(selectedMuscle).toString();
				
				if (nameTXT.getText().toString().equals("")
						|| descTXT.getText().toString().equals("") || multiplierTXT.getText().toString().equals("")) {
					Toast.makeText(CreateExerciseActivity.this,
							"You must fill in all fields", Toast.LENGTH_SHORT)
							.show();
				} else {
					multiplier = Double.parseDouble(multiplierTXT.getText().toString().trim());
					exercise = new Exercise(nameTXT.getText().toString(), descTXT
							.getText().toString(),
							R.drawable.nopic, multiplier,
							mg, timed);
					
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
				if (exerciseList.size() == 0)
					return;
				else {

				}
			}

		});
	}

	protected void write() {
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
