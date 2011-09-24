package com.andrios.bodycards;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WorkoutAdapter extends ArrayAdapter<Workout>{

    private ArrayList<Workout> items;
	
   
	public WorkoutAdapter(Context context, int textViewResourceId,
			ArrayList<Workout> items) {
		super(context, textViewResourceId, items);
		this.items = items;
		
		// TODO Auto-generated constructor stub
	}
	
	
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.workout_list_item, null);
            }
            Workout w;
            w = items.get(position);
      
            if (w != null) {
                    TextView nameLBL = (TextView) v.findViewById(R.id.profile_list_item_nameLBL);
                    TextView dateLBL = (TextView) v.findViewById(R.id.profile_list_item_calendarLBL);
                    LinearLayout calendarLayout = (LinearLayout) v.findViewById(R.id.profile_list_item_calendarLayout);
                    if (nameLBL != null) {
                          nameLBL.setText(w.toString());                            
                    }
                    Calendar c = (Calendar) w.getDate();
                    if(dateLBL != null){
                          dateLBL.setText(Integer.toString(c.get(Calendar.DAY_OF_MONTH)));
                    }
                   
                    if(calendarLayout != null){
                    	//displayIMG.setImageResource(p.getImage());
                    
                    
                    
	                    if(c.get(Calendar.MONTH) == 0){
	                    	calendarLayout.setBackgroundResource(R.drawable.cal_0);
	                    }else if(c.get(Calendar.MONTH) == 1){
	                    	calendarLayout.setBackgroundResource(R.drawable.cal_1);
	                    }else if(c.get(Calendar.MONTH) == 2){
	                    	calendarLayout.setBackgroundResource(R.drawable.cal_2);
	                    }else if(c.get(Calendar.MONTH) == 3){
	                    	calendarLayout.setBackgroundResource(R.drawable.cal_3);
	                    }else if(c.get(Calendar.MONTH) == 4){
	                    	calendarLayout.setBackgroundResource(R.drawable.cal_4);
	                    }else if(c.get(Calendar.MONTH) == 5){
	                    	calendarLayout.setBackgroundResource(R.drawable.cal_5);
	                    }else if(c.get(Calendar.MONTH) == 6){
	                    	calendarLayout.setBackgroundResource(R.drawable.cal_6);
	                    }else if(c.get(Calendar.MONTH) == 7){
	                    	calendarLayout.setBackgroundResource(R.drawable.cal_7);
	                    }else if(c.get(Calendar.MONTH) == 8){
	                    	calendarLayout.setBackgroundResource(R.drawable.cal_8);
	                    }else if(c.get(Calendar.MONTH) == 9){
	                    	calendarLayout.setBackgroundResource(R.drawable.cal_9);
	                    }else if(c.get(Calendar.MONTH) == 10){
	                    	calendarLayout.setBackgroundResource(R.drawable.cal_10);
	                    }else if(c.get(Calendar.MONTH) == 111){
	                    	calendarLayout.setBackgroundResource(R.drawable.cal_11);
	                    }
                    
                    }
            }
            return v;
    }

}
