package com.andrios.bodycards;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import android.content.Context;

public class AndriosPatcher {
	

	static ArrayList<Exercise> exerciseList;
	static ArrayList<Exercise> sel;
	static ArrayList<Boolean> patchList;
	static boolean isWelcome, hasRated;

	public static boolean patch(Context ctx){
		readPatches(ctx);
		for(int i = 0; i < patchList.size(); i++){
			System.out.println(patchList.get(i));
		}
		if(!patchList.get(0)){
			patchList.set(0, patch0(ctx));
		}
		if(!patchList.get(1)){
			patchList.set(1, patch1(ctx));
			
			
		}
		writePatch(ctx);
		return true;
	}
	
	public static boolean patch0(Context ctx){
		
		
		readExercises(ctx);
		while(sel.size() > 0){
			exerciseList.add(sel.remove(0));
		}
		for(int i =0; i<exerciseList.size(); i++){
			if(exerciseList.get(i).getName().toLowerCase().equals("push-ups")){
				exerciseList.remove(i);
				i--;
			}else if(exerciseList.get(i).getName().toLowerCase().equals("bench dips")){
				exerciseList.remove(i);
				i--;
			}else if(exerciseList.get(i).getName().toLowerCase().equals("calf raises")){
				exerciseList.remove(i);
				i--;
			}else if(exerciseList.get(i).getName().toLowerCase().equals("reverse flyes")){
				exerciseList.remove(i);
				i--;
			}else if(exerciseList.get(i).getName().toLowerCase().equals("jumping jacks")){
				exerciseList.remove(i);
				i--;
			}else if(exerciseList.get(i).getName().toLowerCase().equals("8 count body-builders")){
				exerciseList.remove(i);
				i--;
			}else if(exerciseList.get(i).getName().toLowerCase().equals("sprints")){
				exerciseList.remove(i);
				i--;
			}else if(exerciseList.get(i).getName().toLowerCase().equals("lunges")){
				exerciseList.remove(i);
				i--;
			}else if(exerciseList.get(i).getName().toLowerCase().equals("sit ups")){
				exerciseList.remove(i);
				i--;
			}else if(exerciseList.get(i).getName().toLowerCase().equals("planks")){
				exerciseList.remove(i);
				i--;
			}else if(exerciseList.get(i).getName().toLowerCase().equals("squats")){
				exerciseList.remove(i);
				i--;
			}else{
				Exercise e = exerciseList.get(i);
				exerciseList.set(i, new Exercise(e.getName(), e.getDesc(), R.drawable.exercise_custom, e.getMultiplier(), e.getMuscleGroup(), e.getIsTimed()));
			}
			
		}
		createDefaultExercises();
		write(ctx);
		return true;
		
	}
	/*
	 * patch1
	 * Sets Widget Boolean to false for all profiles. 
	 * 
	 * @parameter Context ctx  Context passed from main activity
	 * @Result boolean success indicates if all profiles have been cleared of widget status
	 */
	public static boolean patch1(Context ctx){
		ArrayList<Profile> profList = readProfiles(ctx);
		for(int i = 0; i < profList.size(); i++){
			profList.get(i).isWidget = false;
		}
		writeProfiles(profList, ctx);
		return true;
	}

	@SuppressWarnings("unchecked")
	private static ArrayList<Profile> readProfiles(Context ctx) {
		ArrayList<Profile> profList;
		try {
			FileInputStream fis = ctx.openFileInput("profiles");
			ObjectInputStream ois = new ObjectInputStream(fis);

			profList = (ArrayList<Profile>) ois.readObject();
			ois.close();
			fis.close();
		} catch (Exception e) {
			profList = new ArrayList<Profile>();
		}
		
		return profList;
	}
	
	public static boolean writeProfiles(ArrayList<Profile> profList, Context ctx) {
		try {
			FileOutputStream fos = ctx.openFileOutput("profiles",
					Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			oos.writeObject(profList);

			oos.close();
			fos.close();
			return true;
		} catch (IOException e) {
			return false;
		}

	}
	

	@SuppressWarnings("unchecked")
	private static void readExercises(Context ctx) {
		try {
			
			FileInputStream fis = ctx.openFileInput("exercises");
			ObjectInputStream ois = new ObjectInputStream(fis);

			exerciseList = (ArrayList<Exercise>) ois.readObject();
			sel = (ArrayList<Exercise>) ois.readObject();

			ois.close();
			fis.close();

		} catch (Exception e) {
			e.printStackTrace();
			exerciseList = new ArrayList<Exercise>();
			sel = new ArrayList<Exercise>();
			
			createDefaultExercises();
			

			write(ctx);

		}

	}
	
	@SuppressWarnings("unchecked")
	private static void readPatches(Context ctx) {
		
		try {
			
			FileInputStream fis = ctx.openFileInput("patches");
			ObjectInputStream ois = new ObjectInputStream(fis);
			
			
			patchList = (ArrayList<Boolean>) ois.readObject();
			if(patchList.size() < 2){
				patchList.add(false);
			}
			//For Patch 2 add past 3 lines again. 

			ois.close();
			fis.close();

		} catch (Exception e) {
			e.printStackTrace();
			patchList = new ArrayList<Boolean>();
			patchList.add(false); // Patch 0
			patchList.add(false);// Patch 1
			

			

		}
		
		
	}
	
	@SuppressWarnings("unchecked")
	public static boolean readRated(Context ctx) {
		
		try {
			
			FileInputStream fis = ctx.openFileInput("rated");
			ObjectInputStream ois = new ObjectInputStream(fis);
			
			hasRated = (Boolean) ois.readObject();
			
			

			ois.close();
			fis.close();

		} catch (Exception e) {
			e.printStackTrace();
			hasRated = false;
			

			

		}
		
		writeRated(ctx);
		return hasRated;
	}
	
	private static void writeRated(Context ctx) {
		try {
			FileOutputStream fos = ctx.openFileOutput("rated",
					Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			oos.writeObject(hasRated);
			oos.close();
			fos.close();

		} catch (IOException e) {

		
		}

		
	}
	
	public static void setRated(Context ctx){
		hasRated = true;
		writeRated(ctx);
	}

	@SuppressWarnings("unchecked")
	public static boolean readWelcome(Context ctx) {
		
		try {
			
			FileInputStream fis = ctx.openFileInput("welcome");
			ObjectInputStream ois = new ObjectInputStream(fis);
			
			isWelcome = (Boolean) ois.readObject();
			
			

			ois.close();
			fis.close();

		} catch (Exception e) {
			e.printStackTrace();
			isWelcome = true;
			

			

		}
		
		writeWelcome(ctx);
		return isWelcome;
	}
	
	public static void setWelcome(Context ctx){
		isWelcome = false;
		writeWelcome(ctx);
	}

	private static void createDefaultExercises() {
		exerciseList
				.add(0,
						new Exercise(
								"Push-Ups",
								"1. Begin with your hands and toes on the floor.\n\n" +
								"2. Your torso and legs should remain rigid, keeping your back perfectly straight throughout the move.\n\n" +
								"3. Bend your arms and slowly lower your body downward, stopping just before your upper chest touches the ground.\n\n" +
								"4. Feel a stretch in your chest muscles and then reverse direction, pushing your body up along the same path back to the start position.",
								R.drawable.exercise_push_ups,
								1.0, 
								"Chest", 
								false));
		exerciseList
				.add(0,
						new Exercise(
								"Squats",
								"1. Stand erect with the feet about shoulder-width apart, feet pointed straight ahead or slightly toed-out.\n\n" +
								"2. Keeping the head up, the chest lifted, and the abdominal muscles tight, squat until thighs are parallel to the floor.\n\n" +
								"3. Arch your lower back slightly, and keep that arch throughout the exercise.\n\n" +
								"4. Pause briefly, then stand upright by pressing heels into the floor and keeping the glutes (the rump muscles) tight.",
								R.drawable.exercise_squats,
								1.0,
								"Hamstrings",
								false));
		exerciseList
				.add(0,
						new Exercise(
								"Bench Dips",
								"1. Sit on the edge of a solid bench or chair with your hands holding the edge by your hips.\n\n" +
								"2. Walk your feet forward, then straighten your arms to lift your body off the bench.\n\n" +
								"3. Lower yourself until your upper arms are about parallel with the floor, then press with the triceps to lift yourself back up - straighten the arms, but don’t lock the elbows at the top."+
								"Alternative. Place your feet on another bench or chair and add weights to your lap to add difficulty.",
								R.drawable.exercise_bench_dips,
								1.0,
								"Triceps",
								false));
		exerciseList
				.add(0,
						new Exercise(
								"Calf Raises",
								"1. Stand on the balls of your feet on the edge of a step, with your heels hanging over the edge.\n\n" +
								"2. Hold onto the wall or a railing lightly with one hand.\n\n" +
								"3. Keeping your head up and your torso upright, stand up on your toes, then lower yourself until your heels are slightly below the step.\n\n" +
								"Alternative. Balance without using your hands, conduct exercise one leg at a time.",
								R.drawable.exercise_calf_raises,
								1.0,
								"Calves",
								false));
		exerciseList
				.add(0,
						new Exercise(
								"Reverse Flyes",
								"1. Using light dumbbells, sit on bench or ball and lean over holding weights in front of legs.\n\n" +
								"2. Keep your torso parallel to the floor.\n\n" +
								"3. Raise arms out to the side to shoulder height, keeping elbows in a fixed (slightly bent) position.\n\n" +
								"Alternative. Conduct exercise while standing.",
								R.drawable.exercise_reverse_flyes,
								1.0,
								"Back",
								false));
		
		exerciseList
				.add(0,
						new Exercise(
								"Jumping Jacks",
								"1. Begin by standing with your feet together and arms at your sides. Tighten your abdominal muscles to pull your pelvis forward and take the curve out of your lower back.\n\n" +
								"2. Bend your knees and jump, moving your feet apart until they are wider than your shoulders. At the same time, raise your arms over your head. You should be on the balls of your feet.\n\n" +
								"3. Keep your knees bent while you jump again, bringing your feet together and your arms back to your sides. At the end of the movement, your weight should be on your heels.\n\n",
								R.drawable.exercise_jumping_jacks,
								2.0,
								"Various",
								false));
		
		exerciseList
				.add(0,
						new Exercise(
								"8 Count Body-Builders",
								"Start in a standing position, arms comfortably at your sides. Keep knees slightly flexed with feet hip width apart.\n\n" +
								"1. Squat down while keeping your back straight. Lean forward and place your hands, palms down, on the floor. Keep your arms extended so that your upper body is off the floor.\n\n" +
								"2. Extend your legs straight out from your body, resting on your toes.\n\n" +
								"3. Lower your upper body to the floor by bending your elbows out to the side, perpendicular to your body.\n\n" +
								"4. Raise your upper body again, and hold it up on extended arms.\n\n" +
								"5. Scissor your legs open.\n\n" +
								"6. Scissor your legs closed.\n\n" +
								"7. Draw your knees up to your chest.\n\n" +
								"8. Quickly stand up.\n\n" +
								"Repeat.\n\n",
								R.drawable.exercise_8_count_bodybuilders,
								1.0,
								"Various",
								false));
		
		exerciseList
			.add(0,
					new Exercise(
							"Sprints",
							"Warm up with a short walk and a brief run or jog, followed by light stretching of the major muscle groups.\n\n" +
							"1. Find a flat stretch of ground at least 100 meters long.\n\n" +
							"2. Mark a spot in the distance, either mentally or visually, and begin running as fast as you can toward that spot.\n\n" +
							"3. Raise your knees as high as you can with each step (think of your legs as two pistons pumping vigorously).\n\n" +
							"4. Pump your arms hard, but keep them loose; swing them at your sides, not across your body.\n\n" +
							"5. Breathe naturally and deeply into your abdomen as you sprint, and keep your torso erect.\n\n" +
							"6. Slow down after you've passed your goal post, then stop.\n\n" +
							"Repeat.\n\n",
							R.drawable.exercise_sprints,
							0.5,
							"Cardio",
							false));
		
		exerciseList
				.add(0,
						new Exercise(
								"Lunges",
								"1. Stand in a split stance with the right foot forward and the left leg back, feet should be about 2 to 3 feet apart, depending on your leg length.\n\n" +
								"2. Before you lunge, make sure your torso is straight and that you’re up on the back toe.\n\n" +
								"3. Bend the knees and lower the body down until the back knee is a few inches from the floor.\n\n" +
								"4. At the bottom of the movement, the front thigh should be parallel to the floor and the back knee should point toward the floor.\n\n" +
								"5. Keep the weight evenly distributed between both legs and push back up, keeping the weight in the heel of the front foot.\n\n"+
								"Repeat.\n\n",
								R.drawable.exercise_lunges,
								1.0,
								"Calves",
								false));
		
		exerciseList
				.add(0,
						new Exercise(
								"Sit Ups",
								"1. Start position: Lie back onto floor or bench with knees bent and hands at the side of your head. Keep elbows back and out of sight. Head should be in a neutral position with a space between chin and chest.\n\n" +
								"2. Leading with the chin and chest towards the ceiling, contract the abdominal and raise shoulders off floor or bench until you are seated in an upright position.\n\n" +
								"3. Return to start position.\n\n" +
								"4. Remember to keep head and back in a neutral position. Hyperextension or flexion of either may cause injury.",
								R.drawable.exercise_sit_ups,
								1.0,
								"Abs",
								false));
		exerciseList
			.add(0,
					new Exercise(
							"Planks",
							"1. Start by lying face down on a mat. Place your forearms on the mat with your shoulders aligned directly over your elbows. Clasp your hands in front of you.\n\n" +
							"2. Extend your legs behind you and rest on your toes, as if you are going to do a pushup. Your hips should not be lifted to the ceiling, nor should your back be arched. You should look to attain a straight line between your shoulders and toes.\n\n" +
							"3. Tighten your abdominal muscles to help you hold the position correctly, and hold it as long as you can or until time expires.\n\n" +
							"4. Alternative. Lift one leg at a time behind you to make the exercise more challenging.",
							R.drawable.exercise_planks,
							1.0,
							"Abs",
							true));
	}
	
	public static void write(Context ctx) {
		try {
			FileOutputStream fos = ctx.openFileOutput("exercises",
					Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			oos.writeObject(exerciseList);
			oos.writeObject(sel);
			oos.close();
			fos.close();

		} catch (IOException e) {

		
		}

	}
	
	public static void writePatch(Context ctx) {
		try {
			FileOutputStream fos = ctx.openFileOutput("patches",
					Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			oos.writeObject(patchList);
			oos.close();
			fos.close();

		} catch (IOException e) {

		
		}

	}
	
	public static void writeWelcome(Context ctx) {
		try {
			FileOutputStream fos = ctx.openFileOutput("welcome",
					Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			oos.writeObject(isWelcome);
			oos.close();
			fos.close();

		} catch (IOException e) {

		
		}

	}

	
	
}
