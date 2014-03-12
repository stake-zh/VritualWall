package com.hans.vw.activity.wall;

public class Brick {

	int lat;
	int lon;
	String text;
	String picPath;
	int distance;

	String name;
	long time;

	boolean ismale;

	public Brick() {
		// TODO Auto-generated constructor stub
	}

	public Brick(int lat, int lon, String text, String picPath, int distance,
			String name, long time, boolean ismale) {
		this.lat = lat;
		this.lon = lon;
		this.text = text;
		this.picPath = picPath;
		this.distance = distance;
		this.name = name;
		this.time = time;
		this.ismale = ismale;
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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public boolean isIsmale() {
		return ismale;
	}

	public void setIsmale(boolean ismale) {
		this.ismale = ismale;
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

}
