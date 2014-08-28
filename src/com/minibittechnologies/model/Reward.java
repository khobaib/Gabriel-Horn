package com.minibittechnologies.model;

public class Reward {
	
	private String name;
	private String daysRemaining;
	
	public Reward(String name, String days) {
		this.name=name;
		this.daysRemaining=days;
		
	}
	public String getName()
	{
		return this.name;
	}
	public String getRemainingDays()
	{
		return this.daysRemaining;
	}


}
