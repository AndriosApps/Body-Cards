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
	public Calendar birthday;
	boolean isWidget;
	

	StopWatch clock;

	ArrayList<Workout> workoutList;

	String[] months = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug",
			"Sep", "Oct", "Nov", "Dec" };

	public Profile() {

	}

	public void setNew(String f, String l, String g, Calendar b) {
		fName = f;
		lName = l;
		gender = g;
		
		birthday = b;

		creationDate = Calendar.getInstance();
		createDate = creationDate.get(Calendar.DAY_OF_MONTH) + "-"
				+ months[creationDate.get(Calendar.MONTH)] + "-"
				+ creationDate.get(Calendar.YEAR);

		updateAge();
		isWidget = false;
		workoutList = new ArrayList<Workout>();
	}
	
	public void updateAge() {
		Calendar today = Calendar.getInstance();
		int year = today.get(Calendar.YEAR) - birthday.get(Calendar.YEAR);
		int month = today.get(Calendar.MONTH) - birthday.get(Calendar.MONTH);
		int day = today.get(Calendar.DAY_OF_MONTH) - birthday.get(Calendar.DAY_OF_MONTH);
		
		if(month < 0) {
			age = year-1;
			
		} else if (month == 0) {
			if(day < 0) {
				age = year-1;
			} else {
				age = year;
			}
		} else {
			age = year;
		}
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
		//TODO remove
		System.out.println("addWorkout"+ w.workoutName +" "+ getNumWorkouts());
		if(w.workoutName.equals("Daily Challenge")){
			System.out.println("its a match"); // TODO remove
			if(getNumWorkouts() == 0){
				System.out.println("First Entry"); //TODO remove
				workoutList.add(0, w);
			} else {
				Calendar c = (Calendar) w.workoutDate.clone();
				c.set(Calendar.HOUR, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 1);
				for(int i = 0; i < workoutList.size(); i++){
					System.out.println(i); // TODO Remove
					if(c.after(workoutList.get(i).getDate())){
						System.out.println("First Daily Challenge"); // TODO Remove
						workoutList.add(0, w);
						break;//The most recent workout is at the earliest, the previous day. 
					} else if(workoutList.get(i).workoutName.equals("Daily Challenge")){
						//This workout happened today. 
						System.out.println("TIME TO MERGE"); // TODO Remove
						workoutList.get(i).totalSeconds += w.totalSeconds;
						workoutList.get(i).seconds += w.seconds;
						workoutList.get(i).finSets += w.finSets;
						workoutList.get(i).numSets += w.numSets;
						workoutList.get(i).exercises.add(0,w.exercises.get(0));
						break;
					}
				}
			}
			
		} else {
			System.out.println("Other Workout Type"); // TODO remove

			workoutList.add(0, w);
		}
		
		
	}

}
