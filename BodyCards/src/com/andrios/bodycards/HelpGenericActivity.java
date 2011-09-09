package com.andrios.bodycards;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class HelpGenericActivity extends Activity {

	int ID;
	ArrayList<Help> helpList;
	Help help;
	TextView title, body;
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.helpgenericactivity);
		
		writeHelp();
		getExtras();
		setConnections();
}



	private void getExtras() {
		Intent intent = this.getIntent();
		ID = intent.getIntExtra("ID", -1);
		if(ID != -1){
			help = helpList.get(ID);
		}
		
	}

	private void setConnections() {
		title = (TextView) findViewById (R.id.helpGenericActivityTitle);
		body = (TextView) findViewById (R.id.helpGenericActivityBody);
		String titleText = help.getTitle();
		title.setText(titleText);
		body.setText(help.getBody());
		
		
	}
	
	

	private void writeHelp(){
		helpList = new ArrayList<Help>();
		
		//ID = 0
		help = new Help("Getting Started", 
				"In order to track any of your workouts you should begin by creating at least one profile.\n\n" +
				"To create a profile, look at the bottom left button on the homescreen, it should say 'Create Profile' or 'View Profiles'.\n\n" +
				"After you have created a profile, the statistics for any workout that you complete will be saved to your profile.\n\n" +
				"For more detailed help, use the main help screen.");
		
		helpList.add(help);
		
		//ID = 1
		help = new Help("Exercise Multiplier", 
				"'Multipliers' were designed to modify the difficulty level of individual exercises.\n\n" +
				"If you find an exercise is very difficult for you, set a multiplier to 0.5.  If it is too easy set it to 2.0 or more..\n\n" +
				"The multiplier defaults to 1.0 for any given exercise.\n\n" +
				"All reps for any given set are multiplied by the multiplier. If you have base reps set to 2-15 and Multiplier set to 2 then you effectively have set all reps of that exercise to 4-30.");
		
		helpList.add(help);
		
		//ID = 2
		help = new Help("Deck of Cards", 
				"'Deck of Cards' Workout shows the inspiration for Body Cards.  We used to workout to a deck of cards, assigning a different exercise to each suite and using face values to represent number of reps.\n\n" +
				"Use this version to see how we did the workout before we wrote our app. We expect that most users will favor Custom or Random workouts, so please try those out as well!\n\n" +
				"Individual exercises are not tracked when you do a 'Deck of Cards' workout. We do plan to track that in some manner in a future release.");
		
		helpList.add(help);
		
		
		//ID = 3
		help = new Help("Exercise List", 
				"This activity allows you to edit your exercises and add exercises to Custom Workouts.\n\n" +
				"Long press on any exercise to view and edit it.\n\n" +
				"Click the New button to create new custom workouts.\n\n" +
				"Click any exercise in Exercise List to move it to Selected Exercises, and vice versa." +
				"Click the Clear button to move all exercises from Selected Exercises back to Exercise List.");
		
		helpList.add(help);
		
		//ID = 4
		help = new Help("Custom Workout", 
				"'Custom Workout' is the meat and potatoes of this app.  We expect most users to use this type of workout most often.\n\n" +
				"Use this workout to create your own unique workout, you can customize everything about your workout.\n\n" +
				"You can add and remove exercises here as well as customize the number of base sets, reps, guests, and profiles are in the workout.\n\n" +
				"To create a new exercise, click the new button." +
				"To update or delete an exercise, long click(press) an exercise and choose either 'View' or 'Delete'.");
		
		helpList.add(help);
		
		//ID = 5
		help = new Help("Random Workout", 
				"'Random Workout' was a follow up to the 'Custom Workout'.  When you don't know what exercises you want to do, choose a Random Workout.\n\n" +
				"Random Workout will choose between 2-5 exercises for you and default to 20 sets of 2-15 reps.\n\n" +
				"You can then choose number of guests and number of profiles to participate in the workout.\n\n" +
				"You can also change the number of sets, and the min / max number of reps for each set." +
				"If you decide that you don't like the workouts selected, click the 'Randomize' button and you will be provided with a new selection of exercises.");
		
		helpList.add(help);
		
		//ID = 6
		help = new Help("min and max reps or seconds", 
				"Min and Max are the base number of reps (seconds for timed exercises) you will complete for each exercise.\n\n" +
				"Each exercise has a multiplier associated with it.\n\n" +
				"If you choose 2-15 base reps / seconds, a Body Card of an exercise with a multiplier of 2 will show between 4-30 reps or seconds.\n\n" +
				"In this version of Body Cards reps and seconds are calculated the same way. In a workout with base 2-15, a timed exercise with a multiplier of 1 will be randomly assigned between 2-15 seconds, an exercise that is not timed with a multiplier of 1 will be randomly assigned between 2-15 reps.");
		
		helpList.add(help);
		
		//ID = 7
		help = new Help("Disclaimer", 
				"Not all exercises are suitable for everyone and this or any other exercise program may result in injury.\n\n" +
				"To reduce the risk of injury in your case, consult your doctor before beginning any exercise program.\n\n" +
				"The instructions and advice presented are in no way intended as a substitute for medical counseling. \n\n" +
				"The creators, producers, participants, distributors of Body Cards disclaim any liabilities or loss in connection with the exercises and advice herein.");
		
		helpList.add(help);		
		
		//ID = 8
		help = new Help("Images", 
				"'Homepage Image: Ambro / FreeDigitalPhotos.net.\n\n" +
				"'Complete Image: Ambro / FreeDigitalPhotos.net.\n\n");
		
		helpList.add(help);	
		

	}
}
