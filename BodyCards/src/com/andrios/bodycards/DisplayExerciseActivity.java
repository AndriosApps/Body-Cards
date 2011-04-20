package com.andrios.bodycards;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DisplayExerciseActivity extends Activity {

	protected static final int EXERCISE_ACTIVITY = 0;
	Button back, update;
	Exercise exer;
	TextView multiplierTXT,f,g;
	ImageView img;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.displayexercise);
		getExtras();
		setConnections();
		setOnClickListeners();
	}

	private void getExtras() {

		Intent intent = this.getIntent();
		exer = (Exercise)intent.getSerializableExtra("exer");
		
	}

	private void setConnections() {	
		back = (Button) findViewById(R.id.deBack);
		update = (Button) findViewById(R.id.deUpdate);
		
		multiplierTXT = (TextView) findViewById(R.id.displayExMultiplierTXT);
		multiplierTXT.setText("Multiplier: " + Double.toString(exer.getMultiplier()));
		
		f = (TextView) findViewById(R.id.newExDesc);
		f.setText(exer.getDesc());

		g = (TextView) findViewById(R.id.newExName);
		g.setText(exer.getName());
		
		img = (ImageView) findViewById(R.id.newExImg);
		img.setImageResource(exer.getImg());
		
		ImageView timerIMG = (ImageView) findViewById(R.id.newExTimedIconIMG);
		if(exer.isTimed)
			timerIMG.setImageResource(R.drawable.stopwatch);
		
		TextView category = (TextView) findViewById(R.id.newExCategoryTXT);
		category.setText("Muscle Group: " + exer.getMuscleGroup());
	}
	
	private void setOnClickListeners(){
		back.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				DisplayExerciseActivity.this.finish();
			}

		});
		
		update.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
		
				Intent intent = new Intent(v.getContext(), CreateExerciseActivity.class);
				intent.putExtra("exercise", exer);
				
				startActivityForResult(intent,EXERCISE_ACTIVITY);

			}

		});
	}
	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

    	if (requestCode == EXERCISE_ACTIVITY) {
    		try{
	        exer = (Exercise) intent.getSerializableExtra("exercise");
    		if (resultCode == RESULT_OK) {
    	    
    	    	multiplierTXT.setText(Double.toString(exer.getMultiplier()));
    			
    			f.setText(exer.getDesc());

    			g.setText(exer.getName());
    			
    			img.setImageResource(exer.getImg());
    			
    		}
    		}catch(Exception e){
    			System.out.println("FAILURE");
    		}
    		
    	}
    	
    }	

}
