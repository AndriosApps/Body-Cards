package com.andrios.bodycards;

import java.io.Serializable;

public class Exercise implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7824758618240534640L;

	int img;
	String name;
	String desc;

	
	public int getImg() {
		return img;
	}

	public void setImg(int img) {
		this.img = img;
	}
	
	public Exercise(String n) {
		name = n;
	}

	public Exercise(String n, String d, int i) {
		name = n;
		desc = d;
		//img = "/BodyCards/res/drawable-hdpi/alttoecrunch" + i + ".gif";
		img = i;
	}

	public String toString() {
		return name;
	}

	public String getName() {
		return name;
	}

	public String getDesc() {
		return desc;
	}

	public boolean equals(Exercise ex) {

		return this.toString().equals(ex.toString());

	}

}
