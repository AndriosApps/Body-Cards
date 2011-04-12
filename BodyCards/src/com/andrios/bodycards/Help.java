package com.andrios.bodycards;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class Help extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.help);
		setConnections();
	}

	private void setConnections() {

		Button help = (Button) findViewById(R.id.hpBack);
		help.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Help.this.finish();

			}

		});
	}
}
