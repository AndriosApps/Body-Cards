package com.andrios.bodycards;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

import android.os.SystemClock;

public class Workout implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8427533538991161418L;

	int numPeople, numSets, max, min, finSets;
	ArrayList<CompletedExercises> exercises;

	long seconds;

	String elapsedWorkoutTime;
	String[] months = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug",
			"Sep", "Oct", "Nov", "Dec" };
	String date;
	Calendar workoutDate;

	public Workout() {
		workoutDate = Calendar.getInstance();
		date = workoutDate.get(Calendar.DAY_OF_MONTH) + "-"
				+ months[workoutDate.get(Calendar.MONTH)] + "-"
				+ workoutDate.get(Calendar.YEAR);
	}

	public Workout(int np, int ns, int x, int n, String[] e) {
		numPeople = np;
		numSets = ns;
		max = x;
		min = n;
		exercises = new ArrayList<CompletedExercises>();

		seconds = 0;
		workoutDate = Calendar.getInstance();
		date = workoutDate.get(Calendar.DAY_OF_MONTH) + "-"
				+ months[workoutDate.get(Calendar.MONTH)] + "-"
				+ workoutDate.get(Calendar.YEAR);

		for (int i = 0; i < e.length; i++) {
			exercises.add(new CompletedExercises(e[i]));
		}

	}

	long base;

	public void start() {
		base = SystemClock.elapsedRealtime();
	}

	public void stop() {
		long res = (SystemClock.elapsedRealtime() - base) / 1000;

		seconds += res;
	}

	public int getNumPeople() {
		return numPeople;
	}

	public String getFormattedTime() {
		long t = seconds;
		long hr = t / (60 * 60);
		t = seconds - (hr * 60 * 60);
		long min = t / 60;
		long sec = seconds - (min * 60);

		String tStr = "  ";
		if (hr > 0 && hr < 9) {
			tStr += "0" + hr + ":";
		} else if (hr > 9) {
			tStr += hr + ":";
		}

		if (min > 0 && min < 9) {
			tStr += "0" + min + ":";
		} else if (min > 9) {
			tStr += min + ":";
		} else {
			tStr += "00:";
		}

		if (sec > 0 && sec < 9) {
			tStr += "0" + sec;
		} else if (sec > 9) {
			tStr += Long.toString(sec);
		} else {
			tStr += "00";
		}

		return tStr;

	}

	public int getFinSets() {
		return finSets;
	}

	public void setFinSets(int finSets) {
		this.finSets = finSets;
	}

	public void setWorkoutTime(String t) {
		elapsedWorkoutTime = t;
	}

	public String getWorkoutTime() {
		return elapsedWorkoutTime;
	}

	public String toString() {
		String ret = date + " (";
		for (int i = 0; i < exercises.size(); i++) {
			if (i == 0)
				ret += exercises.get(i).getName();
			else
				ret += ", " + exercises.get(i).getName();
		}
		ret += ")";
		return ret;
	}

	public Calendar getDate() {
		return workoutDate;
	}

	public int getNumSets() {
		return numSets;
	}

	public void incrementCount(String exercise, int number) {
		int i;
		for (i = 0; i < exercises.size(); i++) {
			if (exercise.equals(exercises.get(i).getName()))
				break;
		}
		exercises.get(i).increment(number);

		System.out.println("test: " + exercises.get(i).toString());
	}
}
