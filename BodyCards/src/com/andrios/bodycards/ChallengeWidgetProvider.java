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

	public static ChallengeWidgetProvider Widget = null;
	public static Context context;
	public static AppWidgetManager AWM;
	public static int IDs[];
	
	public static String CLICK = "click";
	static ArrayList<Exercise> exerciseList, chosenList;
	static ArrayList<Profile> selectProf, unusedProf;
	static int maxReps;
	static int minReps;
	static int selectReps;
	static Exercise exercise;
	static Random rNum;
	// http://stackoverflow.com/questions/2748613/force-android-widget-to-update
	
	
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		
		if (null == context){
			context = ChallengeWidgetProvider.context;
		}else{
			this.context = context;
		}
	    if (null == AWM){
	    	AWM = ChallengeWidgetProvider.AWM;
	    }else{
	    	AWM = appWidgetManager;
	    }
	    if (null == IDs){
	    	IDs = ChallengeWidgetProvider.IDs;
	    }else{
	    	IDs = appWidgetIds;
	    }
	
		final int N = appWidgetIds.length;
        
        
        System.out.println("onUpdate"); //TODO Remove
        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
        	rNum = new Random();
        	readExercises(context, appWidgetIds[i]);
    		getRandomExercise();
        	
        	RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.challengewidget);
    		Intent active = new Intent(context, ChallengeWidgetProvider.class);
    		active.setAction(CLICK);
    		
    		
    		try{
    			
    			remoteViews.setTextViewText(R.id.challengeWidgetExerciseLBL, exercise.getName());
                remoteViews.setTextViewText(R.id.challengeWidgetNameLBL, selectProf.get(0).getFirstName());
                
        		remoteViews.setTextViewText(R.id.challengeWidgetCountLBL, Integer.toString((int) (selectReps * chosenList.get(0).getMultiplier())));
               
    		}catch(NullPointerException e){
    			e.printStackTrace();
    		}
    		
    		
    		PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, appWidgetIds[i], active, 0);
    		
    		remoteViews.setOnClickPendingIntent(R.id.challengeWidgetBottomLayout, actionPendingIntent);
    		remoteViews.setOnClickPendingIntent(R.id.challengeWidgetMiddleLayout, actionPendingIntent);
    		remoteViews.setOnClickPendingIntent(R.id.challengeWidgetTopLayout, actionPendingIntent);
    		
    		appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
	
	
	private static void readExercises(Context context, int id){
		System.out.println("Read Exercises");
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
				System.out.println("Reps"+ selectReps);
			}
			int randomNumber = Math.abs(rNum.nextInt()) % exerciseList.size();
			exercise = (exerciseList.get(randomNumber));
			
			chosenList = new ArrayList<Exercise>();
			chosenList.add(exercise);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		

	}
	
	@Override
	public void onReceive(Context context, Intent intent){
		
		final String action = intent.getAction();
		if(AppWidgetManager.ACTION_APPWIDGET_DELETED.equals(action)){
			final int appWidgetId = intent.getExtras().getInt(
					AppWidgetManager.EXTRA_APPWIDGET_ID, 
					AppWidgetManager.INVALID_APPWIDGET_ID);
			if(appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID){
				this.onDeleted(context, new int[] {appWidgetId});
			}
		
		}else{
			//check if our Action was called
			if(intent.getAction().equals(CLICK)){
				System.out.println("I GO CLICKED GOOD");
			   	Intent wkout = new Intent(context, StartDeckActivity.class);
		    	wkout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		    	System.out.println("IM SENDING REPS: "+selectReps); //TODO Remove
				wkout.putExtra("max", selectReps);
				wkout.putExtra("min", selectReps);
				wkout.putExtra("sets", 1);
				wkout.putExtra("peeps", 1);
				wkout.putExtra("profilesU", unusedProf);
				wkout.putExtra("profiles", selectProf);
				wkout.putExtra("exercises", chosenList);
				wkout.putExtra("workoutName", "Daily Challenge");
				
				if(ChallengeWidgetProvider.Widget != null){
					ChallengeWidgetProvider.Widget.onUpdate(null, null, null);
					
				}else{
					System.out.println("Widget still == null");
				}
				
				context.startActivity(wkout);
			}
			
		}
		super.onReceive(context, intent);
	}
	
	

}
