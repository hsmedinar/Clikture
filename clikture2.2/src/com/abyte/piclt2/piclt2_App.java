package com.abyte.piclt2;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Application;
import android.location.Location;
import android.os.Environment;
import android.util.Log;

import com.abyte.piclt2.api.ApiHelper;
import com.abyte.piclt2.model.Data_Access;
import com.abyte.piclt2.model.Preferences;
import com.abyte.piclt2.model.Promotion;
import com.abyte.piclt2.ui.View_Manager;
/**
 * This is the Main application Object  
 * 
 */
public class piclt2_App extends Application{
	
	public Data_Access Data;
	public View_Manager ViewManager;
	public Activity mContext;
	public String image_path;
	public String download_path;
	public Preferences Preference;
	public String pick_file;
	public String pick_file_mimeType;
	public boolean pause;
	public int orientation;
	public int screen_orientation;
	public int degrees;
	public int width;
	public int height;
	public int Screen; // 1 login, 2 Registration, 3 listImage, 4 camera, 5 Show_Image
	
	
/**
 * This method is call, when activity is created.
 * @see android.app.Application#onCreate()
 */
	@Override
	public void onCreate() {
		pause=false;
		super.onCreate();
		Data = new Data_Access(this);

		//String appPath =getApplicationContext().getFilesDir().getAbsolutePath();
		
		String appPath = Environment.getExternalStorageDirectory().getAbsolutePath();
		File image_dir = new File(appPath+"/image/");
		if(!image_dir.exists())
			image_dir.mkdirs();
		
		File download_dir = new File(appPath+"/download/");
		if(!download_dir.exists())
			download_dir.mkdirs();
		
		Preference=Preferences.Get_Instance();
		Preference.setContext(this);
		image_path=appPath+"/image/";
		download_path=appPath+"/download/";
		

		Data.image_path=image_path;
		
		orientation=1;
			
	}

/**
 * This is the implementation of interface onGetLocation. Allow to store GPS location on preference.
 * @see com.abyte.piclt2.api.onGetLocation#onGet_Location(android.location.Location)
 */
	

	public void onGet_Location(Location location) {
		
		if(location!=null)
		{
			Preference.setLatitude(String.valueOf(location.getLatitude()));
			Preference.setLongitude(String.valueOf(location.getLongitude()));
			Preference.setLocatinTime(String.valueOf(location.getTime()));
			
			Log.e("Localizacion", "Latitud "+location.getLatitude()+"  longitud"+location.getLongitude());

		}
	}
	
	
	
	/************* get data promotion and store in database *************/
	
	public void get_promotion(String valor)
	 {
		Data.Delete_Promotion();		
		ArrayList<Promotion> ns =(ArrayList<Promotion>) ApiHelper.Get_Promotions(valor);	
		if(ns!=null)
		{
			for (int i = 0; i < ns.size(); i++)
			{
				Data.Insert_Promotion(ns.get(i),i);
			}
		}
	 }	
}
