package com.andrios.bodycards;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Random;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.TextView;

public class ChallengeWidgetProvider extends AppWidgetProvider {

	static ArrayList<Exercise> exerciseList, chosenList;
	static ArrayList<Profile> selectProf, unusedProf;
	static int maxReps;
	static int minReps;
	static int selectReps;
	static Exercise exercise;
	static Random rNum;
	
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		
		final int N = appWidgetIds.length;
        
        
        System.out.println("onUpdate");
        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
        	updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
	
	public void onDeleted(Context context, AppWidgetManager appWiedgetManager, int[] appWidgetIds){
		
		for(int i = 0; i<appWidgetIds.length; i++){
			System.out.println("DELETING filename");
			String filename = (appWidgetIds[i] + "widgetexercises");
			System.out.println("DELETING filename");
			File file = new File(filename);
			file.delete();
		}
		super.onDeleted(context, appWidgetIds);
	}
	
	public void onDisabled(Context context){
		super.onDisabled(context);
	}
	
	private static void readExercises(Context context, int id){
		
		try{
			FileInputStream fis = context.openFileInput(id+"widgetexercises");
			ObjectInputStream ois = new ObjectInputStream(fis);

			exerciseList = (ArrayList<Exercise>) ois.readObject();
			selectProf = (ArrayList<Profile>) ois.readObject();
			unusedProf = (ArrayList<Profile>) ois.readObject();
			maxReps = (int) ois.readInt();
			minReps = (int) ois.readInt();

			
			ois.close();
			fis.close();

			
		}catch(Exception e){
			e.printStackTrace();	
		}
		
		
		
	}
	
	

	
	private static void getRandomExercise() {
		try{
			
			if (maxReps == minReps) {
				selectReps = maxReps;

			} else {
				int modulo = maxReps - minReps;

				int num = Math.abs(rNum.nextInt());
				selectReps = minReps + (num % modulo);
			}
			int randomNumber = Math.abs(rNum.nextInt()) % exerciseList.size();
			exercise = (exerciseList.get(randomNumber));
			chosenList = new ArrayList<Exercise>();
			chosenList.add(exercise);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		

	}
	
	
	public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
			 int appWidgetId){
		rNum = new Random();
    	try{
    		readExercises(context, appWidgetId);
        	System.out.println("Exercise: " + exerciseList.get(0));
        	System.out.println("Max: " + maxReps);
        	System.out.println("Min: " + minReps);

        	System.out.println("PROFILE IN WIDGET"+selectProf.get(0).getFirstName()+selectProf.size());
        	getRandomExercise();
        	System.out.println("Exercise IN WIDGET"+chosenList.get(0).getName());
        	System.out.println("Exercise REPS IN WIDGET"+selectReps);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	
    	//TODO REMOVE THIS
    	

        // Create an Intent to launch ExampleActivity

    	
    	Intent wkout = new Intent(context, StartDeckActivity.class);
    	System.out.println("IM SENDING REPS: "+selectReps);
		wkout.putExtra("max", selectReps);
		wkout.putExtra("min", selectReps);
		wkout.putExtra("sets", 1);
		wkout.putExtra("peeps", 1);
		wkout.putExtra("profilesU", unusedProf);
		wkout.putExtra("profiles", selectProf);
		wkout.putExtra("exercises", chosenList);
		wkout.putExtra("workoutName", "Daily Challenge");
		
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, wkout, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get the layout for the App Widget and attach an on-click listener to the button
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.challengewidget);
        try{
        	 views.setTextViewText(R.id.challengeWidgetCountLBL, Integer.toString(selectReps));
             views.setTextViewText(R.id.challengeWidgetExerciseLBL, exercise.getName());
             views.setTextViewText(R.id.challengeWidgetNameLBL, selectProf.get(0).getFirstName());
             //Basically an onClick Listener that launches the pending Intent (StartDeckActivity)
             views.setOnClickPendingIntent(R.id.challengeWidgetBottomLayout, pendingIntent);
             views.setOnClickPendingIntent(R.id.challengeWidgetMiddleLayout, pendingIntent);
             views.setOnClickPendingIntent(R.id.challengeWidgetTopLayout, pendingIntent);

        }catch(Exception e){
        	e.printStackTrace();
        }
       
        // Tell the AppWidgetManager to perform an update on the current App Widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
	}

}
