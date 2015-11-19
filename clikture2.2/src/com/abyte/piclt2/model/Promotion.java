package com.abyte.piclt2.model;

public class Promotion {
	public String Group;
	public String Name;
	public String Icon;
	public String Url;
	
	public Promotion(){
		super();
		Group="";
		Name="";
		Icon="";
		Url="";
	}
	
	public Promotion(String group, String name, String icon,String url) {
		super();
		Group=group;
		Name=name;
		Icon=icon;
		Url=url;
	}
	public String getGroup() {
		return Group;
	}
	public void setGroup(String group) {
		Group = group;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getIcon() {
		return Icon;
	}
	public void setIcon(String icon) {
		Icon = icon;
	}
	public String getUrl() {
		return Url;
	}
	public void setUrl(String url) {
		Url = url;
	}
}
