package com.abyte.piclt2.model;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.provider.Settings.Secure;
import android.text.format.DateFormat;
import android.util.Log;



/**
 * This class is use for manager sqlite database 
 */

public class Data_Access extends SQLiteOpenHelper {

	public String android_id; 
	
	private static final String DATABASE_NAME = "piclt2.sqlite";
	private static final int DATABASE_VERSION = 1;
	private final Context mContext;
	public String image_path;
	public boolean needToLoadData=false;
	private SQLiteDatabase db;
	
	public boolean NeedToLoadData() {
		return needToLoadData;
	}

	public Data_Access(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.mContext = context;
		this.android_id = Secure.getString(this.mContext.getContentResolver(),Secure.ANDROID_ID); 

		db=getReadableDatabase();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		super.onOpen(db);
	}
	

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		
		//TODO: 		
		//String[] sql = "CREATE TABLE image(path TEXT,date TEXT,latitude TEXT,longitude TEXT);CREATE TABLE promotion(group TEXT,name TEXT,icon TEXT,url TEXT);".split("\n"); //mContext.getString(R.string.Database_onCreate).split("\n");
		db.beginTransaction();
		
		try {
			// Create tables and test data
			db.execSQL("CREATE TABLE image(path TEXT,date TEXT,latitude TEXT,longitude TEXT)");
			db.execSQL("CREATE TABLE promotion(group_name TEXT,name_icon TEXT,icon TEXT,url TEXT,locate int);");
			//execMultipleSQL(db, sql);
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			Log.e("Error creating tables and debug data", e.toString());

			throw e;
		} finally {
			db.endTransaction();
		}
		
		


	}
	
	private long insert(String Table, ContentValues map) {
		try {
			long id = getWritableDatabase().insert(Table, null, map);
			
			
			return id;
		} catch (SQLException e) {
			Log.e("Error Inserting in Table "+Table, e.toString());

		}
		catch (Exception e) {
			Log.e("Error Inserting in Table "+Table, e.toString());

		}
		return -1;
	}
	private void update(String Table, ContentValues map, String where,Object whereArgs) {
		try{
			getWritableDatabase().update(Table, map, where, (String[]) whereArgs);
			} 
			catch (SQLException e) {
			Log.e("Error Update in Table "+Table, e.toString());
			}
			catch (Exception e) {
				Log.e("Error Update in Table "+Table, e.toString());
	
			}
	}
	private void deleted(String Table,  String where,Object whereArgs) {
		try{
			
			
			getWritableDatabase().delete(Table, where, (String[]) whereArgs);
			} 
			catch (SQLException e) {
			Log.e("Error deleted in Table "+Table, e.toString());
			}
			catch (Exception e) {
				Log.e("Error deleted in Table "+Table, e.toString());
	
			}
	}
	
	public void ExecuteQuery(String q)
	{
		
		
		try {
			// Create tables and test data
			
			getWritableDatabase().execSQL(q);
		} catch (SQLException e) {
			Log.e("Error ExecuteQuery", e.toString());
			
		} finally {
		}
	}
	
	/**
	 * Select Image
	 */
	public Image_Cursor getImage()
	{

		
		String query = "SELECT path as _id,* from image";
		
		SQLiteDatabase d = getReadableDatabase();
		Image_Cursor cursor = (Image_Cursor) d.rawQueryWithFactory(
				new Image_Cursor.Factory(), query,null, null);
		
		return cursor;
	}
	
	/**
	 * get All Image in Array list
	 */
	public ArrayList<Image> getImage_array()
	{
		ArrayList<Image> result=new ArrayList<Image>();

		Image_Cursor cursor =getImage();

		if(cursor.getCount()>0)
		{
			while(cursor.moveToNext())
			 {
				Image i=new Image();
				i.setDate(cursor.getDate());
				i.setPath(cursor.getPath());
				i.setLatitude(cursor.getLatitude());
				i.setLatitude(cursor.getLatitude());
				
				 result.add(i);
			 }
		}
		 cursor.close();
		return result;
	}
	
	/**
	 * get Image by path
	 */
	public Image getImage_by_Path(String path)
	{

		String query = "SELECT path as _id,* from image where path='"+path+"'";
		
		SQLiteDatabase d = getReadableDatabase();
		Image_Cursor cursor = (Image_Cursor) d.rawQueryWithFactory(
				new Image_Cursor.Factory(), query,null, null);

		if(cursor.getCount()>0)
		{
			while(cursor.moveToNext())
			 {
				Image i=new Image();
				i.setDate(cursor.getDate());
				i.setPath(cursor.getPath());
				i.setLatitude(cursor.getLatitude());
				i.setLatitude(cursor.getLatitude());
				cursor.close();
				 return i;
			 }
		}
		 cursor.close();
		return null;
	}
	
	
	public void Delete_Image(String path)
	{
		this.deleted("image","path=?",new String[]{path});
	}
	
	/**
	 * Insert Image
	 */
	public void Insert_Image(Image image)
	{
		ContentValues map = new ContentValues();
		map.put("path", image.getPath());
		map.put("date", image.getDate());
		map.put("latitude",image.getLatitude());
		map.put("longitude", image.getLongitude());
		this.insert("image", map);

	}
	
	

	private void execMultipleSQL(SQLiteDatabase db, String[] sql) {
		for (String s : sql)
			if (s.trim().length() > 0)
				db.execSQL(s);
	}
	
	/**
	 * 	Convert date to long 2010-11-26 21:09:44 yyyy-mm-dd HH:mm:ss"
	 */
	public static long StrToDate_long(String date,String format)
	{
		 
		 
		 SimpleDateFormat formatter = new SimpleDateFormat(format); 
		 long r=new Date().getTime();

			try {
				r=formatter.parse(date).getTime();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return r;	
	}
	
	
	public static String DateLongToStr(String date)
	{
		String month=(String) DateFormat.format("MMM", Long.valueOf(date));
		month=month.toUpperCase().substring(0, month.length()-1);
		return  month+DateFormat.format(" dd, yyyy "+DateFormat.HOUR_OF_DAY+":mm", Long.valueOf(date)); 
	}
	public static String DateLongToStr(long date)
	{
		return (String) DateFormat.format("dd/MM/yyyy "+DateFormat.HOUR_OF_DAY+":mm", date); 
	}
	public static String DateLongToStr(long date,String format)
	{
		return (String) DateFormat.format(format,date); 
	}
	
	/*********************** add data info promotion ************************/
	
	
	public void Delete_Promotion()
	{
		
			//CREATE TABLE news(title TEXT,content TEXT,date TEXT,time_stamp TEXT,field_browser TEXT);
			SQLiteStatement stmt = db.compileStatement("DELETE FROM promotion");		
			stmt.execute();
			stmt.close();
		
	}
	
	public ArrayList<Promotion> getPromotion()
	{
		ArrayList<Promotion> result=new ArrayList<Promotion>();
		
		//String query = "SELECT * FROM news order by time_stamp desc";
		String query = "SELECT * FROM promotion order by group_name";
		
		SQLiteDatabase d = getReadableDatabase();
		Campaign_Cursor cursor = (Campaign_Cursor) d.rawQueryWithFactory(
				new Campaign_Cursor.Factory(), query,null, null);

		if(cursor.getCount()>0)
		{
			while(cursor.moveToNext())
			 {
				 Promotion n=new Promotion();
				 //n.setGroup(cursor);
				 n.setGroup(cursor.getGroup());
				 n.setName(cursor.getName());
				 n.setIcon(cursor.getIcon());
				 n.setUrl(cursor.getUrl());
				 result.add(n);
			 }
		}
		 cursor.close();

		return result;
	}
	
	
	public ArrayList getGroupName()
	{
		
		

		String query = "SELECT group_name FROM promotion group by group_name order by locate asc";
		
		SQLiteDatabase d = getReadableDatabase();
		Cursor cursor = (Campaign_Cursor) d.rawQueryWithFactory(
				new Campaign_Cursor.Factory(), query,null, null);
		
		ArrayList ItemsGroup = new ArrayList();
		//HashMap<String, String> meMap=new HashMap<String, String>();
		if(cursor.getCount()>0)
		{
			while(cursor.moveToNext())
			 {
		//		 Promotion n=new Promotion();
				 //n.setGroup(cursor);
				 //meMap.put("group", cursor.getGroup());
				ItemsGroup.add(cursor.getString(0));
			 }
		}
		cursor.close();

		return ItemsGroup;
	}
	
	
	public ArrayList<Promotion> getIconsForGroup(String group)
	{
	
		ArrayList<Promotion> result=new ArrayList<Promotion>();

		String query = "SELECT icon,url FROM promotion where group_name='" + group + "'";
		
		SQLiteDatabase d = getReadableDatabase();
		Cursor cursor = (Campaign_Cursor) d.rawQueryWithFactory(
				new Campaign_Cursor.Factory(), query,null, null);

		//HashMap<String, String> meMap=new HashMap<String, String>();
		if(cursor.getCount()>0)
		{
			while(cursor.moveToNext())
			 {
				 Promotion n=new Promotion();
				 n.setIcon(cursor.getString(0));
				 n.setUrl(cursor.getString(1));
				 result.add(n);
			 }
		}
		cursor.close();

		return result;
	}
	
	
	public boolean ExistNews(Promotion promo)
	{
		//news(title TEXT,content TEXT,date TEXT);	
		Cursor cursor = db.query(true,"promotion", null,"group_name=? and name_icon=? and icon=? and url=?", new String[]{promo.Group,promo.Name,promo.Icon,promo.Url}, null, null, null, null);
		if(cursor.getCount()>0)
		{
			cursor.close();
			return true;
		}
		cursor.close();
		return false;
		
	}
	
	public void Insert_Promotion(Promotion promo,int locate)
	{
		if(!ExistNews(promo))
		{
			ContentValues map = new ContentValues();
			
			//CREATE TABLE news(title TEXT,content TEXT,date TEXT,time_stamp TEXT,field_browser TEXT);
			SQLiteStatement stmt = db.compileStatement("Insert into promotion(group_name,name_icon,icon,url,locate) VALUES(?,?,?,?,?)");
			stmt.bindString(1,promo.Group );
			stmt.bindString(2,promo.Name );
			stmt.bindString(3,promo.Icon);
			stmt.bindString(4,promo.Url);
			stmt.bindString(5,Integer.toString(locate));
			stmt.execute();
			stmt.close();
		}
		
	}
	
	
	
	
}