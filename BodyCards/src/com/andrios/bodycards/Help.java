package com.andrios.bodycards;

import java.io.Serializable;

public class Help implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6919497367488975933L;

	String title, body;
	
	public Help(String title, String body){
		this.title = title;
		this.body = body;
	}
	
	public String getTitle(){
		return title;
	}
	
	public String getBody(){
		return body;
	}
	
}
