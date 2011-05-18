package com.andrios.bodycards;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class WidgetProvider extends AppWidgetProvider{

	public static String ACTION_WIDGET_RECEIVER = "ActionReceiverWidget";
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
		
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.challengewidget);
		Intent active = new Intent(context, WidgetProvider.class);
		active.setAction(ACTION_WIDGET_RECEIVER);
		
		PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);
		remoteViews.setOnClickPendingIntent(R.id.challengeWidgetBottomLayout, actionPendingIntent);
		remoteViews.setOnClickPendingIntent(R.id.challengeWidgetMiddleLayout, actionPendingIntent);
		remoteViews.setOnClickPendingIntent(R.id.challengeWidgetTopLayout, actionPendingIntent);
		
		appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
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
			if(intent.getAction().equals(ACTION_WIDGET_RECEIVER)){
				System.out.println("I GO CLICKED GOOD");
				
			}
			
		}
		super.onReceive(context, intent);
	}
}
