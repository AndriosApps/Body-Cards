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

public class DisplayExercise extends Activity {

	Button back, update;
	Exercise exer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.displayexercise);
		getExtras();
		setConnections();
	}

	private void getExtras() {

		Intent intent = this.getIntent();
		exer = (Exercise)intent.getSerializableExtra("exer");
		
	}

	private void setConnections() {
		
		
		back = (Button) findViewById(R.id.deBack);
		back.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				DisplayExercise.this.finish();
			}

		});

		update = (Button) findViewById(R.id.deUpdate);
		update.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String name = exer.getName();
				String desc = exer.getDesc();
				Intent intent = new Intent(v.getContext(), CreateExercise.class);
				intent.putExtra("name", name);
				intent.putExtra("desc", desc);
				startActivity(intent);

			}

		});

		TextView f = (TextView) findViewById(R.id.newExDesc);
		f.setText(exer.getDesc());

		TextView g = (TextView) findViewById(R.id.newExName);
		g.setText(exer.getName());
		
		ImageView img = (ImageView) findViewById(R.id.newExImg);
		img.setImageResource(exer.getImg());
	}

}
