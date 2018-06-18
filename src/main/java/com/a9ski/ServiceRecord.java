package com.a9ski;


public class ServiceRecord {
	private final String activityCode;
	private int count = 0;
	private final String description;
	
	
	public ServiceRecord(String activityCode, String description) {
		super();
		this.activityCode = activityCode;
		this.description = description;
	}
	
	public void incrementCount() {
		count++;
	}
	
	public int getCount() {
		return count;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getActivityCode() {
		return activityCode;
	}

	@Override
	public String toString() {
		return "ServiceRecord [count=" + count + ", description=" + description + "]";
	}
}
