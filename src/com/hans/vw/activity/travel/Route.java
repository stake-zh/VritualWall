package com.hans.vw.activity.travel;

import java.util.ArrayList;

public class Route {

	long time;
	String name;
	long startTime;
	long endTime;
	String customer;
	ArrayList<SelfBrick> bricks;

	public Route() {
		// TODO Auto-generated constructor stub
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public ArrayList<SelfBrick> getBricks() {
		return bricks;
	}

	public void setBricks(ArrayList<SelfBrick> bricks) {
		this.bricks = bricks;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public Route(long time, String name, long startTime, long endTime,
			ArrayList<SelfBrick> bricks, String customer) {
		this.time = time;
		this.customer = customer;
		this.name = name;
		this.startTime = startTime;
		this.endTime = endTime;
		this.bricks = bricks;
	}

}
