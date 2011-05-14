package com.andrios.bodycards;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Random;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;
import android.widget.TextView;

public class ChallengeWidgetProvider extends AppWidgetProvider {

	ArrayList<Exercise> exerciseList;
	int maxReps, minReps, selectReps;
	Exercise exercise;
	Random rNum;
	
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        rNum = new Random();
        
        System.out.println("onUpdate");
        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
        	try{
        		readExercises(context);
            	System.out.println("Exercise: " + exerciseList.get(0));
            	System.out.println("Max: " + maxReps);
            	System.out.println("Min: " + minReps);
            	getRandomExercise();
        	}catch(Exception e){
        		e.printStackTrace();
        	}
        	
        	
            int appWidgetId = appWidgetIds[i];
/*
            // Create an Intent to launch ExampleActivity
            Intent intent = new Intent(context, ExampleActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
*/
            // Get the layout for the App Widget and attach an on-click listener to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.challengewidget);
            try{
            	 views.setTextViewText(R.id.challengeWidgetCountLBL, Integer.toString(selectReps));
                 views.setTextViewText(R.id.challengeWidgetExerciseLBL, exercise.getName());
                 //views.setOnClickPendingIntent(R.id.button, pendingIntent);

            }catch(Exception e){
            	e.printStackTrace();
            }
           
            // Tell the AppWidgetManager to perform an update on the current App Widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
       
    }
	
	public void onDeleted(Context context, AppWidgetManager appWiedgetManager, int[] appWidgetIds){
		super.onDeleted(context, appWidgetIds);
	}
	
	public void onDisabled(Context context){
		super.onDisabled(context);
	}
	
	private void readExercises(Context context){
		try{
			FileInputStream fis = context.openFileInput("widgetexercises");
			ObjectInputStream ois = new ObjectInputStream(fis);

			exerciseList = (ArrayList<Exercise>) ois.readObject();
			maxReps = (int) ois.readInt();
			minReps = (int) ois.readInt();
			
			ois.close();
			fis.close();

			
		}catch(Exception e){
			e.printStackTrace();	
		}
		
		
		
	}
	
	private void getRandomExercise() {
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
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		

	}

}
