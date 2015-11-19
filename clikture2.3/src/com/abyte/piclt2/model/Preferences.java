package com.abyte.piclt2.model;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * This class is for manager preference
 */

public class Preferences {

	public static final String PREFERENCES_ID="PREFERENCES_ID";
	public static final int PREFERENCES_Mode = Activity.MODE_PRIVATE;
	public static final String app="app";
	public static final String app_ver="app_ver";
	public static final String Latitude="Latitude";
	public static final String Longitude="Longitude";
	public static final String LocatinTime="LocatinTime";
	public static final String user="user";
	public static final String pass="pass";
	public static final String movil="movil";
	public static final String login="login";
	public static final String Remenber="Remenber";
	public static final String Brand="Brand";

	
	public static final String Current_Image="Current_Image";
	
	public Runnable run_get_mensaje;
	
	public static Preferences instance;
	public Context mContext;
	public SharedPreferences myPreferences;
	
	private Preferences()
	{
		run_get_mensaje=null;
	}
	
	public void setContext(Context Context)
	{
		mContext=Context;
		myPreferences =mContext.getSharedPreferences(PREFERENCES_ID, PREFERENCES_Mode);
		run_get_mensaje=null;
		
	}
	
	
	public static Preferences Get_Instance()
	{
		if(instance==null)
		{
				instance=new Preferences();
		}
		return instance;
	}
	
	public void SetLong(String key,long l)
	{
	SharedPreferences.Editor editor = myPreferences 
		.edit();
	editor.putLong(key, l);
	editor.commit();
	}
	public void SetString(String key,String s)
	{
	SharedPreferences.Editor editor = myPreferences.edit();
	editor.putString(key, s);
	editor.commit();
	}
	public void SetInt(String key,int i)
	{
	SharedPreferences.Editor editor = myPreferences 
		.edit();
	editor.putInt(key, i);
	editor.commit();
	}


	
	public long GetLong(String key)
	{
		return this.myPreferences.getLong(key, 0);		
	}
	public String GetString(String key)
	{
		return this.myPreferences.getString(key, "");		
	}
	public int GetInt(String key)
	{
		return this.myPreferences.getInt(key, 0);		
	}

	
	public String getApp() {
		return GetString(app);
	}
	public void setaApp(String s) {
		SetString(app,s);
	}
	public String getApp_ver_name() {
		return GetString(app_ver);
	}
	public void setApp_ver(String s) {
		SetString(app_ver,s);
	}


	public String getLongitude() {
		return GetString(Longitude);
	}
	public void setLongitude(String s) {
		SetString(Longitude,s);
	}
	
	public String getLocatinTime() {
		return GetString(LocatinTime);
	}
	public void setLocatinTime(String s) {
		SetString(LocatinTime,s);
	}
	public String getLatitude() {
		return GetString(Latitude);
	}
	public void setLatitude(String s) {
		SetString(Latitude,s);
	}

	public String getUser() {
		return GetString(user);
	}
	public void setUser(String s) {
		SetString(user,s);
	}
	public String getPass() {
		return GetString(pass);
	}
	public void setPass(String s) {
		SetString(pass,s);
	}
	public String getMovil() {
		return GetString(movil);
	}
	public void setMovil(String s) {
		SetString(movil,s);
	}
	
	public String getLogin() {
		return GetString(login);
	}
	public void setLogin(String s) {
		SetString(login,s);
	}

	public String getCurrent_Image() {
		return GetString(Current_Image);
	}
	public void setCurrent_Image(String s) {
		SetString(Current_Image,s);
	}
		public String getRemenber() {
		return GetString(Remenber);
	}
	public void setRemenber(String s) {
		SetString(Remenber,s);
	}
	public String getBrand() {
		return GetString(Brand);
	}
	public void setBrand(String s) {
		SetString(Brand,s);
	}
	
	

}
