package com.andrios.bodycards;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileAdapter extends ArrayAdapter<Profile>{

    private ArrayList<Profile> items;
	
   
	public ProfileAdapter(Context context, int textViewResourceId,
			ArrayList<Profile> items) {
		super(context, textViewResourceId, items);
		this.items = items;
		
		// TODO Auto-generated constructor stub
	}
	
	
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.profile_list_item, null);
            }
            Profile p;
            p = items.get(position);
      
            if (p != null) {
                    TextView nameLBL = (TextView) v.findViewById(R.id.profile_list_item_nameLBL);
                    TextView birthdayLBL = (TextView) v.findViewById(R.id.profile_list_item_birthdayLBL);
                    ImageView star1IMG = (ImageView) v.findViewById(R.id.profile_list_item_rating_star1);
                   ImageView star2IMG = (ImageView) v.findViewById(R.id.profile_list_item_rating_star2);
                    ImageView star3IMG = (ImageView) v.findViewById(R.id.profile_list_item_rating_star3);
                    ImageView star4IMG = (ImageView) v.findViewById(R.id.profile_list_item_rating_star4);
                    ImageView star5IMG = (ImageView) v.findViewById(R.id.profile_list_item_rating_star5);
                   ImageView displayIMG = (ImageView) v.findViewById(R.id.profile_list_item_displayIMG);
                    if (nameLBL != null) {
                          nameLBL.setText(p.toString());                            
                    }
                    if(birthdayLBL != null){
                          birthdayLBL.setText("Weekly workouts: ");
                    }
                   
                    if(displayIMG != null){
                    	ArrayList<Workout> w = p.getWorkouts();
                    	int x = w.size();
                		String g = p.getGender();
                		Calendar d = Calendar.getInstance();
                    	
                    	if (g.equals("Male")) {
                			if (x >= 5 && difference(d, w.get(4).getDate()))
                				displayIMG.setImageResource(R.drawable.mstrong);
                			else
                				displayIMG.setImageResource(R.drawable.mweak);
                		} else {
                			if (x >= 5 && difference(d, w.get(4).getDate()))
                				displayIMG.setImageResource(R.drawable.fstrong);
                			else
                				displayIMG.setImageResource(R.drawable.fweak);
                		}
                    }
                    
                    int days = p.getNumWorkouts();
                    if(days > 0){
                    	star1IMG.setImageResource(R.drawable.star_lit);
                    }
                    if(days > 1){
                    	star2IMG.setImageResource(R.drawable.star_lit);
                    }
                    if(days > 2){
                    	star3IMG.setImageResource(R.drawable.star_lit);
                    }
                    if(days > 3){
                    	star4IMG.setImageResource(R.drawable.star_lit);
                    }
                    if(days > 4){
                    	star5IMG.setImageResource(R.drawable.star_lit);
                    }
            }
            return v;
    }
    
	private boolean difference(Calendar b, Calendar c) {
		int y = b.get(Calendar.YEAR) - c.get(Calendar.YEAR);
		int m = b.get(Calendar.MONTH) - c.get(Calendar.MONTH);
		int d = b.get(Calendar.DAY_OF_MONTH) - c.get(Calendar.DAY_OF_MONTH);
		if (y > 0)
			return false;
		if (m > 0)
			return false;
		if (d > 7)
			return false;
		return true;
	}

}
