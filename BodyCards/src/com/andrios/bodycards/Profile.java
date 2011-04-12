package com.andrios.bodycards;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

public class Profile implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -805645198058919673L;

	String fName;
	String lName;
	String gender;
	int age;
	String createDate;
	public Calendar creationDate;

	StopWatch clock;

	ArrayList<Workout> workoutList;

	String[] months = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug",
			"Sep", "Oct", "Nov", "Dec" };

	public Profile() {

	}

	public void setNew(String f, String l, String g, int a) {
		fName = f;
		lName = l;
		gender = g;
		age = a;

		creationDate = Calendar.getInstance();
		createDate = creationDate.get(Calendar.DAY_OF_MONTH) + "-"
				+ months[creationDate.get(Calendar.MONTH)] + "-"
				+ creationDate.get(Calendar.YEAR);

		workoutList = new ArrayList<Workout>();
	}

	@Override
	public String toString() {
		return fName + " " + lName;
	}

	// GETTER METHODS

	public int getNumWorkouts() {
		return workoutList.size();
	}

	public Calendar getCalendar() {
		return creationDate;
	}

	public String getCreationDate() {
		return createDate;
	}

	public String getFirstName() {
		return fName;
	}

	public String getLastName() {
		return lName;
	}

	public String getGender() {
		return gender;
	}

	public int getAge() {
		return age;
	}

	public ArrayList<Workout> getWorkouts() {
		return workoutList;
	}

	// SETTER METHODS

	public void setFirstName(String f) {
		fName = f;
	}

	public void setLastName(String l) {
		lName = l;
	}

	public void setGender(String g) {
		gender = g;
	}

	public void setAge(int a) {
		age = a;
	}

	public void setCreateDate(String d) {
		createDate = d;
	}

	public void setCalDate(Calendar c) {
		creationDate = c;
	}

	public void addWorkout(Workout w) {
		workoutList.add(0, w);
	}

}
