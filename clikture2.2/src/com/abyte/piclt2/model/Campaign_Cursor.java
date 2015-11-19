package com.abyte.piclt2.model;


import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;

public class Campaign_Cursor extends SQLiteCursor{
	
	public static final String QUERY_GetAll = "SELECT * FROM promotion order by group_name";
	//CREATE TABLE news(title TEXT,content TEXT,date TEXT,time_stamp TEXT,field_browser TEXT);
	public Campaign_Cursor(SQLiteDatabase db, SQLiteCursorDriver driver,
			String editTable, SQLiteQuery query) {
		super(db, driver, editTable, query);
		// TODO Auto-generated constructor stub
	}
	
	public static class Factory implements SQLiteDatabase.CursorFactory {
		public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query) {
			return new Campaign_Cursor(db, driver, editTable, query);
		}
	}
	
	public String getGroup() {
		int index = getColumnIndex("group_name");
		String str = getString(index);
		return str;
	}
	
	public String getName() {
		int index = getColumnIndex("name_icon");
		String str = getString(index);
		return str;
	}
	
	public String getIcon() {
		int index = getColumnIndex("icon");
		String str = getString(index);
		return str;
	}
	
	public String getUrl() {
		int index = getColumnIndex("url");
		String str = getString(index);
		return str;
	}
}
