package com.andrios.bodycards;

public class AndriosKey {

	private int currentKey;
	
	public AndriosKey(){
		currentKey = 0;
	}
	
	public int getKey(){
		currentKey++;
		return currentKey;
		
	}
}
