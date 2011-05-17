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
	ArrayList<Exercise> exerciseList;
	long base, totalBase, seconds, totalSeconds;
	
	String workoutName;
 
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
		seconds = 0;

		System.out.println("init 1 Seconds: " + seconds);
	}

	public Workout(int np, int ns, int x, int n, ArrayList<Exercise> e, String wn) {
		numPeople = np;
		numSets = ns;
		max = x;
		min = n;
		exercises = new ArrayList<CompletedExercises>();
		this.exerciseList = e;
		seconds = 0;
		totalSeconds = 0;
		System.out.println("init Seconds: " + seconds);
		workoutName = wn;
		workoutDate = Calendar.getInstance();
		date = workoutDate.get(Calendar.DAY_OF_MONTH) + "-"
				+ months[workoutDate.get(Calendar.MONTH)] + "-"
				+ workoutDate.get(Calendar.YEAR);

		for (int i = 0; i < this.exerciseList.size(); i++) {
			Exercise e1 = this.exerciseList.get(i);
			exercises.add(new CompletedExercises(e1.getName(), e1.getMultiplier(), e1.isTimed));
		}

	}
	
    public void startTotal(long time){
    	totalBase = time;
    }
    
	public void stopTotal(long time) {
		long res = (time - totalBase) / 1000;
		
		totalSeconds += res;
		System.out.println("Total Seconds " + totalSeconds);
		
	}

	public void start() {
		
		base = SystemClock.elapsedRealtime();
		
		System.out.println("start Seconds: " + seconds);
	}

	public void stop() {
		long res = (SystemClock.elapsedRealtime() - base) / 1000;
		
		seconds += res;
		

		System.out.println(" stop Seconds: " + seconds);
		
	}
	
	public void addTime(int seconds){

		
		this.seconds += seconds;
		System.out.println("add Seconds: " + seconds);
	}

	public int getNumPeople() {
		return numPeople;
	}

	public String getPersonalTime() {
		System.out.println("Seconds: " + seconds);
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

		System.out.println("output Seconds: " + seconds);
		return tStr;

	}
	public String getTotalFormattedTime() {
		long t = totalSeconds;
		long hr = t / (60 * 60);
		t = totalSeconds - (hr * 60 * 60);
		long min = t / 60;
		long sec = totalSeconds - (min * 60);

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
		String ret = date + " ("+ workoutName + ")";
		return ret;
	}

	public Calendar getDate() {
		return workoutDate;
	}

	public void setNumSets(int numSets){
		this.numSets = numSets;
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
		System.out.println(exercises.get(i).getName() + ": "+exercises.get(i).count );

	}
}
