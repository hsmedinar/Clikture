package com.abyte.piclt2.model;
/**
 * this class store image properties
 */


public class Image {
	public String path;
	public String date;
	public String latitude;
	public String longitude;
	
	
	public Image(String path, String date, String latitude, String longitude) {
		super();
		this.path = path;
		this.date = date;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	public Image() {
		super();
		this.path = "";
		this.date = "";
		this.latitude = "";
		this.longitude = "";
	}
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	
	
	
	
	
	
	
}
