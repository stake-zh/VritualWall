package com.hans.vw.activity.travel;

public class SelfBrick {

	String name;
	String locationDesc;
	long time;
	int lat;
	int lon;
	String text;
	String picPath;

	boolean ismale;
	boolean select = false;

	public SelfBrick() {
		// TODO Auto-generated constructor stub
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public boolean isIsmale() {
		return ismale;
	}

	public void setIsmale(boolean ismale) {
		this.ismale = ismale;
	}

	public String getLocationDesc() {
		return locationDesc;
	}

	public void setLocationDesc(String locationDesc) {
		this.locationDesc = locationDesc;
	}

	public SelfBrick(String name, String locationDesc, long time, int lat,
			int lon, String text, String picPath, boolean ismale) {
		this.name = name;
		this.locationDesc = locationDesc;
		this.time = time;
		this.lat = lat;
		this.lon = lon;
		this.text = text;
		this.picPath = picPath;
		this.ismale = ismale;
	}

}
