package com.andrios.bodycards;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class Main extends Activity {
	/** Called when the activity is first created. */
	int width, height;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main);
		setConnections();
	}

	private void setConnections() {
		// TODO Auto-generated method stub
		Button quickWorkout = (Button) findViewById(R.id.qw);
		quickWorkout.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), QuickWorkout.class);
				startActivity(intent);
			}

		});

		Button newWorkout = (Button) findViewById(R.id.nw);
		newWorkout.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), ExerciseList.class);
				startActivityForResult(intent, 31415);
			}

		});

		Button viewProfile = (Button) findViewById(R.id.vp);
		viewProfile.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), ViewProfile.class);
				startActivity(intent);
			}

		});

		Button help = (Button) findViewById(R.id.hp);
		help.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), Help.class);
				startActivity(intent);
			}

		});

		Button quit = (Button) findViewById(R.id.qt);
		quit.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Main.this.finish();
			}

		});

	}

}