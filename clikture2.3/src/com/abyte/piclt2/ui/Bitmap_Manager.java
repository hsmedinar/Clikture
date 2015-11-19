package com.abyte.piclt2.ui;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * this class is for manager image
 */
public class Bitmap_Manager {

	public HashMap<String,Bitmap> Bitmap_String;
	public HashMap<Integer,Bitmap> Bitmap_Integer;
	public static Bitmap_Manager instance;
	public Activity mContext;
	
	private Bitmap_Manager()
	{
		Bitmap_String =new HashMap<String,Bitmap>();
		Bitmap_Integer=new HashMap<Integer,Bitmap>();
	}
	
	public void setContext(Activity Context)
	{
		mContext=Context;
	}
	
	public static Bitmap_Manager Get_Instance()
	{
		if(instance==null)
		{
				instance=new Bitmap_Manager();
		}
		return instance;
	}
	

	

	
	public Bitmap getResource(int resource) {
		
		if(Bitmap_Integer.containsKey(Integer.valueOf(resource)))
		{
			return Bitmap_Integer.get(Integer.valueOf(resource));
		}
		else
		{
			Bitmap img= BitmapFactory.decodeResource(mContext.getResources(), resource);
			Bitmap_Integer.put(Integer.valueOf(resource), img);
			return img;
		}
	}
	
	public Bitmap getResourceScale(int resource,int width,int height) {
		
		if(Bitmap_Integer.containsKey(Integer.valueOf(resource)))
		{
			return Bitmap_Integer.get(Integer.valueOf(resource));
		}
		else
		{
			Bitmap img= BitmapFactory.decodeResource(mContext.getResources(), resource);
			Bitmap img2= Bitmap.createScaledBitmap(img, width, height, true);
			Bitmap_Integer.put(Integer.valueOf(resource), img2);
			return img2;
		}
	}
	
public static Bitmap getResourceScale(String path,int width,int height) {
		
		Bitmap img= BitmapFactory.decodeFile(path);
			float ratio=(float)img.getHeight()/(float)height;
			if(ratio<1)
				ratio=1;
			Bitmap img2= Bitmap.createScaledBitmap(img, (int)(img.getWidth()/ratio), (int)(img.getHeight()/ratio), true);
			return img2;

	}

public static Bitmap getResourceScale(Bitmap img,int width,int height) {
	
	float ratio=(float)img.getHeight()/(float)height;
	if(ratio<1)
		ratio=1;
		Bitmap img2= Bitmap.createScaledBitmap(img, (int)(img.getWidth()/ratio), (int)(img.getHeight()/ratio), true);
		return img2;

}
	public Bitmap getImage(String Path) {
		
		if(Bitmap_String.containsKey(Path))
		{
			return Bitmap_String.get(Path);
		}
		else
		{
			Bitmap img= BitmapFactory.decodeFile(Path);
			Bitmap_String.put(Path, img);
			return img;
		}
	}
	
public Bitmap DownloadFromUrl(String imageURL) {  //this is the downloader method

	  try {
		  URL url = new URL( imageURL); 
		  URLConnection ucon = url.openConnection();

		  InputStream is = ucon.getInputStream();
		  Bitmap image=BitmapFactory.decodeStream(is);

		  return image;

	  } catch (IOException e) {
			  Log.d("DownloadFromUrl", "Error: " + e);
	  }

	  return null;

}
	
}
