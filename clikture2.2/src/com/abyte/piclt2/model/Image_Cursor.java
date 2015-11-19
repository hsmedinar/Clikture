package com.abyte.piclt2.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
/**
 * this class is a cursor for manager images
 */

public class Image_Cursor extends SQLiteCursor{
	

	public static final String QUERY_GetAll = "SELECT * FROM contact";
	
	public Image_Cursor(SQLiteDatabase db, SQLiteCursorDriver driver,
			String editTable, SQLiteQuery query) {
		super(db, driver, editTable, query);
		// TODO Auto-generated constructor stub
	}
	
	public static class Factory implements SQLiteDatabase.CursorFactory {
		public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query) {
			return new Image_Cursor(db, driver, editTable, query);
		}
	}
	
	public String getPath() {
		int index = getColumnIndex("path");
		String str = getString(index);
		return str;
	}
	
	public String getDate() {
		int index = getColumnIndex("date");
		String str = getString(index);
		return str;
	}
	
	public String getLatitude() {
		int index = getColumnIndex("latitude");
		String str = getString(index);
		return str;
	}
	
	public String getLongitude() {
		int index = getColumnIndex("longitude");
		String str = getString(index);
		return str;
	}
	
	public String get_Id() {
		int index = getColumnIndex("_id");
		String str = getString(index);
		return str;
	}

}
