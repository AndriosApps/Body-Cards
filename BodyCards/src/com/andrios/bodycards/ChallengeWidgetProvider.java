package com.andrios.bodycards;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class ChallengeWidgetProvider extends AppWidgetProvider {

	public static ChallengeWidgetProvider Widget = null;
	public static Context context;
	public static AppWidgetManager AWM;
	public static int[] IDs;
	
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
		System.out.println("Body Cards onUpdate");//TODO REMOVE
		if (null == context){
			this.context = ChallengeWidgetProvider.context;
		}else{
			this.context = context;
		}
	    if (null == appWidgetManager){
	    	AWM = ChallengeWidgetProvider.AWM;
	    }else{
	    	AWM = appWidgetManager;
	    }
	    if (null == appWidgetIds){
	    	IDs = ChallengeWidgetProvider.IDs;
	    }else{
	    	IDs = appWidgetIds;
	    }
	    ChallengeWidgetProvider.Widget = this;
	    
		final int N = IDs.length;
        
        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
        	System.out.println("Updating AppWidget "+ IDs[i]);//TODO REMOVE
        	rNum = new Random();
        	readProfiles();
        	myProfile(IDs[i]);
        	readExercises(context, IDs[i]);
    		getRandomExercise();
        	
        	RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.challengewidget);
    		Intent active = new Intent(context, ChallengeWidgetProvider.class);
    		active.setAction(CLICK);
    		active.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, IDs[i]);
    	
    		
    		
    		try{
    			remoteViews.setTextViewText(R.id.challengeWidgetExerciseLBL, exercise.getName());
                remoteViews.setTextViewText(R.id.challengeWidgetNameLBL, selectProf.get(0).getFirstName());
                
        		remoteViews.setTextViewText(R.id.challengeWidgetCountLBL, Integer.toString((int) (selectReps * chosenList.get(0).getMultiplier())));
               
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    		
    		
    		PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, IDs[i], active, 0);
    		
    		remoteViews.setOnClickPendingIntent(R.id.challengeWidgetBottomLayout, actionPendingIntent);
    		remoteViews.setOnClickPendingIntent(R.id.challengeWidgetMiddleLayout, actionPendingIntent);
    		remoteViews.setOnClickPendingIntent(R.id.challengeWidgetTopLayout, actionPendingIntent);
    		AWM.updateAppWidget(IDs[i], remoteViews);
        }
        super.onUpdate(context, appWidgetManager, ChallengeWidgetProvider.IDs);
    }
	
	public void onDeleted(Context context, int[] appWidgetIds){

		
		readProfiles();
		myProfile(appWidgetIds[0]);
		selectProf.get(0).setID(false, -1);
		unusedProf.add(selectProf.remove(0));
		
		context.deleteFile(appWidgetIds[0]+"widgetexercises");
		
		writeProfiles(context);
		
		
		super.onDeleted(context, appWidgetIds);
	}
	
	public void onDisabled(Context context){
		System.out.println("OnDisabled");//TODO REMOVE
		readProfiles();
		for(int i = 0; i<unusedProf.size(); i++){
			unusedProf.get(i).setID(false, -1);
		}
		writeProfiles(context);
		super.onDisabled(context);
	}
	
	
	private static void myProfile(int ID) {
		for(int i = 0; i < unusedProf.size(); i++){
			System.out.println("myProfile is "+ ID + " equal to " + unusedProf.get(i).getID());//TODO REMOVE
			if(unusedProf.get(i).getID() == ID){
				Profile p = unusedProf.remove(i);
				selectProf = new ArrayList<Profile>();
				selectProf.add(p);
				System.out.println("Select Prof Size "+ selectProf.size());//TODO
				break;
			}
		}
		
	}


	private static void readExercises(Context context, int id){
		try{
			FileInputStream fis = context.openFileInput(id+"widgetexercises");
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
	
	@SuppressWarnings("unchecked")
	private static void readProfiles() {
		System.out.println("REad Profiles");//TODO REMOVE
		try {
			FileInputStream fis = context.openFileInput("profiles");
			ObjectInputStream ois = new ObjectInputStream(fis);

			unusedProf = (ArrayList<Profile>) ois.readObject();
			System.out.println("UNUSED PROFILES SIZE "+ unusedProf.size());
			ois.close();
			fis.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void writeProfiles(Context context){
		try {
			FileOutputStream fos = context.openFileOutput("profiles",
					Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			oos.writeObject(unusedProf);

			oos.close();
			fos.close();

		
		} catch (IOException e) {
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
	
	@Override
	public void onReceive(Context context, Intent intent){
		
		final String action = intent.getAction();
		if(AppWidgetManager.ACTION_APPWIDGET_DELETED.equals(action)){
			
		
		}else{
			//check if our Action was called
			if(intent.getAction().equals(CLICK)){
				int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 
						AppWidgetManager.INVALID_APPWIDGET_ID);
			   	Intent wkout = new Intent(context, StartDeckActivity.class);
		    	wkout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				readProfiles();//TODO Look HERE
				myProfile(appWidgetId);
		    	wkout.putExtra("max", selectReps);
				wkout.putExtra("min", selectReps);
				wkout.putExtra("sets", 1);
				wkout.putExtra("peeps", 1);
				wkout.putExtra("profilesU", unusedProf);
				wkout.putExtra("profiles", selectProf);
				wkout.putExtra("exercises", chosenList);
				wkout.putExtra("workoutName", "Daily Challenge");
				System.out.println("Starting Daily Challenge Activity: "+ selectProf.size());//TODO REMOVE
				
				if(ChallengeWidgetProvider.Widget != null){
					ChallengeWidgetProvider.Widget.onUpdate(null, null, null);
					
				}
				
				context.startActivity(wkout);
			}
			
		}
		super.onReceive(context, intent);
	}
	
	public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
			 int appWidgetId){
	
		ChallengeWidgetProvider.context = context;
		ChallengeWidgetProvider.AWM = appWidgetManager;
	    
	   
	    
		
		
		rNum = new Random();
    	readExercises(context, appWidgetId);
    	readProfiles();
    	myProfile(appWidgetId);
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
		
		
		PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, appWidgetId, active, 0);
		
		remoteViews.setOnClickPendingIntent(R.id.challengeWidgetBottomLayout, actionPendingIntent);
		remoteViews.setOnClickPendingIntent(R.id.challengeWidgetMiddleLayout, actionPendingIntent);
		remoteViews.setOnClickPendingIntent(R.id.challengeWidgetTopLayout, actionPendingIntent);
		appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
			}

}
