package com.andrios.bodycards;

import java.io.Serializable;

public class CompletedExercises implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7173677931181122963L;

	String name;
	int count;
	boolean isTimed;
	Double multiplier;

	public CompletedExercises(String e, Double multiplier, boolean isTimed) {
		name = e;
		count = 0;
		this.multiplier = multiplier;
		this.isTimed = isTimed;
	}

	public void increment(int n) {
		count += n;
	}

	public void setName(String e) {
		name = e;
	}

	public String getName() {
		return name;
	}

	public String toString() {
		if(isTimed){
			int time = (int) (count);
			int hours = time / (60 * 60);
			time = time - (hours * 60 * 60);
			int mins = time / 60;
			int secs = time - (mins * 60);
			

			String tStr = "";
			tStr += (hours > 9) ? (hours + ":") : ("0" + hours + ":");
			tStr += (mins > 9) ? (mins + ":") : ("0" + mins + ":");
			tStr += (secs > 9) ? (secs) : ("0" + secs);
			
			return name + " time: " + tStr;
		}else{
			return name + " completed: " + count;
		}
		
	}

}
