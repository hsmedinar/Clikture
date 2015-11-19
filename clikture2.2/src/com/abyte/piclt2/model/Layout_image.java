package com.abyte.piclt2.model;

public class Layout_image {
	public String Title;
	public String Icon;
	public String Field_Browser;
	public String Group_image;
	public String Time_Stamp;
	
	
	
	
	public Layout_image() {
		super();
		Title = "";
		Icon = "";
		Group_image = "";
		Field_Browser="";
	}
	public Layout_image(String title, String icon, String group_image,String field_browser) {
		super();
		Title = title;
		Icon = icon;
		Field_Browser = field_browser;
		Group_image = group_image;		
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getIcon() {
		return Icon;
	}
	public void setIcon(String icon) {
		Icon = icon;
	}
	public String getGroup() {
		return Group_image;
	}
	public void setDate(String group_image) {
		Group_image = group_image;
	}
	public String getTime_Stamp() {
		return Time_Stamp;
	}
	public void setTime_Stamp(String time_Stamp) {
		Time_Stamp = time_Stamp;
	}
	public String getField_Browser() {
		return Field_Browser;
	}
	public void setField_Browser(String field_Browser) {
		Field_Browser = field_Browser;
	}
	
	

}
