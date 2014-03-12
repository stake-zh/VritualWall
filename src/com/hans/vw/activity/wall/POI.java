package com.hans.vw.activity.wall;

public class POI {
	int lat;
	int lon;
	String name;
	String text;
	int distance;
	double rate;
	boolean custom;
	String customerName;
	int greate;
	boolean hasGreated;
	int discuss;

	//
	public POI() {
		// TODO Auto-generated constructor stub
	}
	
	

	



	public POI(int lat, int lon, String name, String text, int distance,
			double rate, boolean custom, String customerName, int greate,
			boolean hasGreated, int discuss) {
		this.lat = lat;
		this.lon = lon;
		this.name = name;
		this.text = text;
		this.distance = distance;
		this.rate = rate;
		this.custom = custom;
		this.customerName = customerName;
		this.greate = greate;
		this.hasGreated = hasGreated;
		this.discuss = discuss;
	}







	public int getLat() {
		return lat;
	}

	public void setLat(int lat) {
		this.lat = lat;
	}

	public int getLon() {
		return lon;
	}

	public void setLon(int lon) {
		this.lon = lon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getDistance() {
		return distance;
	}

	

	public double getRate() {
		return rate;
	}



	public void setRate(double rate) {
		this.rate = rate;
	}



	public void setDistance(int distance) {
		this.distance = distance;
	}



	public void setRate(int rate) {
		this.rate = rate;
	}

	public boolean isCustom() {
		return custom;
	}

	public void setCustom(boolean custom) {
		this.custom = custom;
	}

	public int getGreate() {
		return greate;
	}

	public void setGreate(int greate) {
		this.greate = greate;
	}

	public boolean isHasGreated() {
		return hasGreated;
	}

	public void setHasGreated(boolean hasGreated) {
		this.hasGreated = hasGreated;
	}

	public int getDiscuss() {
		return discuss;
	}

	public void setDiscuss(int discuss) {
		this.discuss = discuss;
	}



	public String getCustomerName() {
		return customerName;
	}



	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	

}
