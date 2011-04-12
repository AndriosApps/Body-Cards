package com.andrios.bodycards;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class QuickWorkout extends Activity {

	ImageView card;

	int[] cards = { R.drawable.c2, R.drawable.c3, R.drawable.c4, R.drawable.c5,
			R.drawable.c6, R.drawable.c7, R.drawable.c8, R.drawable.c9,
			R.drawable.c10, R.drawable.ca, R.drawable.cj, R.drawable.cq,
			R.drawable.ck, R.drawable.d2, R.drawable.d3, R.drawable.d4,
			R.drawable.d5, R.drawable.d6, R.drawable.d7, R.drawable.d8,
			R.drawable.d9, R.drawable.d10, R.drawable.da, R.drawable.dj,
			R.drawable.dq, R.drawable.dk, R.drawable.s2, R.drawable.s3,
			R.drawable.s4, R.drawable.s5, R.drawable.s6, R.drawable.s7,
			R.drawable.s8, R.drawable.s9, R.drawable.s10, R.drawable.sa,
			R.drawable.sj, R.drawable.sq, R.drawable.sk, R.drawable.h2,
			R.drawable.h3, R.drawable.h4, R.drawable.h5, R.drawable.h6,
			R.drawable.h7, R.drawable.h8, R.drawable.h9, R.drawable.h10,
			R.drawable.ha, R.drawable.hj, R.drawable.hq, R.drawable.hk,
			R.drawable.jb, R.drawable.jr };

	int[] backs = { R.drawable.back, R.drawable.back2, R.drawable.back3,
			R.drawable.back4 };

	int cardNum = 0;

	int cardBack;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.quickworkout);

		chooseBack();
		shuffleDeck();
		setConnections();
	}

	private void chooseBack() {
		Random number = new Random();
		cardBack = backs[number.nextInt(backs.length)];
	}

	private void shuffleDeck() {
		for (int i = 0; i < 10; i++) {
			shuffle();
		}
	}

	private void shuffle() {
		Random number = new Random();
		for (int i = 0; i < cards.length; i++) {
			int s = number.nextInt(cards.length);
			int x = cards[s];
			cards[s] = cards[i];
			cards[i] = x;
		}

	}

	private void getNewCard() {

		card.setImageResource(cardBack);

		dealCard();

		TextView remaining = (TextView) findViewById(R.id.cardCount);
		remaining.setText("Cards Remaining: " + (53 - cardNum));
	}

	private void dealCard() {
		Timer clock = new Timer();
		clock.schedule(new myTask(), 150);
	}

	public class myTask extends TimerTask {
		final Handler handler = new Handler();

		public void run() {
			handler.post(new Runnable() {
				public void run() {
					// TODO Auto-generated method stub
					card.setImageResource(cards[cardNum++]);
				}

			});
		};
	}

	private void setConnections() {
		// TODO Auto-generated method stub
		card = (ImageView) findViewById(R.id.card);
		card.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (cardNum < cards.length) {
					getNewCard();
				} else {
					Intent el_fin = new Intent(v.getContext(), Finished.class);

					startActivity(el_fin);
					QuickWorkout.this.finish();

				}
			}

		});
	}

}
