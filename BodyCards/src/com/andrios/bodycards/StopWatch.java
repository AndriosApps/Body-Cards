package com.andrios.bodycards;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

public class StopWatch implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3939475472050700467L;

	public int seconds;
	Timer clock;

	public StopWatch(int startTime) {
		seconds = startTime;
		clock = new Timer();
	}

	public void start() {
		clock.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				seconds++;
			}

		}, 0, 1000);

	}

	public String toString() {

		int time = seconds;
		int hours = time / (60 * 60);
		time = time - (hours * 60 * 60);
		int mins = time / 60;
		int secs = time - (mins * 60);

		String tStr = "";
		tStr += (hours > 9) ? (hours + ":") : ("0" + hours + ":");
		tStr += (mins > 9) ? (mins + ":") : ("0" + mins + ":");
		tStr += (secs > 9) ? (secs + ":") : ("0" + secs);
		return tStr;
	}

	public int getTime() {
		return seconds;
	}

	public int stop() {
		clock.cancel();
		return seconds;
	}
}
