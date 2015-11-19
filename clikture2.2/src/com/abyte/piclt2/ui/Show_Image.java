package com.abyte.piclt2.ui;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.abyte.piclt2.R;
import com.abyte.piclt2.piclt2_App;
import com.abyte.piclt2.api.ApiHelper;
import com.abyte.piclt2.model.Image;
/**
 * this class show and manager image
 */
public class Show_Image implements OnClickListener{
	

	
	public piclt2_App app;
	
	public LinearLayout view_layout;
	public Button cancel;		
	public Button save;
	public Button submit;
	public ImageView imageb;
	public Image image;
	public ProgressDialog pDialog;
	private ApiHelper api = new ApiHelper();
	private ImageView brandimg;
	
	public Show_Image(piclt2_App app,Image image) {
		super();
		this.app=app;
		this.image=image;		
		app.Preference.setCurrent_Image(image.getPath());		
	}
	
	
	public Show_Image(piclt2_App app) {
		super();
		this.app=app;
		
		String Current_Image = app.Preference.getCurrent_Image();
		if(Current_Image!=null && Current_Image.length()>0)
		{
			this.image=app.Data.getImage_by_Path(Current_Image);
		}
	}


	public View Show_view()
	{
		view_layout=(LinearLayout) app.ViewManager.Inflate_View(R.layout.show_image);
	
		
		save=(Button) view_layout.findViewById(R.id.save);
		submit=(Button) view_layout.findViewById(R.id.submit);
		cancel=(Button) view_layout.findViewById(R.id.cancel);
		imageb=(ImageView) view_layout.findViewById(R.id.image);
		brandimg=(ImageView) view_layout.findViewById(R.id.brandimg);
		
		/**
		 * Here you can Get the screen you want from your server and show it.
		 */
		if(this.image!=null && this.image.path.length()>0) 
	 	    {
			 File image_dir = new File(app.image_path+this.image.path);
			 if(image_dir.exists())
			  {
				imageb.setImageBitmap(BitmapFactory.decodeFile(app.image_path+image.getPath()));
			  }
		    }
		
		save.setOnClickListener(this);
		cancel.setOnClickListener(this);
		submit.setOnClickListener(this);
		loadImage();
		
		
    	return view_layout;
		
	}

  
	public void onClick(View v) {
		// TODO Auto-generated method stub

		Intent intent;
		if(save.equals(v))
		{
			app.ViewManager.Show_Screen(3);
			return;
		}
		
		if(submit.equals(v))
		{
			Upload_Image(this.image);			
			return;
		}
		if(cancel.equals(v))
		{
			
			if(this.image!=null && this.image.path.length()>0) 
			{
				File image_dir = new File(app.image_path+this.image.path);
				if(image_dir.exists())
				{
					image_dir.delete();
				}
			}
			
			app.Data.Delete_Image(image.path);
			
			app.ViewManager.Show_Screen(3);
			return;
		}
	}

	private void Upload_Image(final Image img)
	 {
		 
		 pDialog = ProgressDialog.show(app.mContext, null, app.ViewManager.getstring(R.string.Uploading_Image));
	    	
	    	final Handler handler = new Handler() {
				@Override
				public void handleMessage(Message message) {
					if (pDialog != null)
						pDialog.cancel();
					String msg =(String)message.obj;
					if (Integer.parseInt(msg)<0){
						Toast.makeText(app.mContext, "Sorry, we do not recognize this image", Toast.LENGTH_LONG).show();																			
					}else{
						Toast.makeText(app.mContext, msg, Toast.LENGTH_LONG).show();						
						HashMap data_promo =ApiHelper.Get_Promotion(msg);
						if(data_promo.isEmpty()){
							Toast.makeText(app.mContext, "Sorry, we do not recognize this image", Toast.LENGTH_LONG).show();
						}else{
					        app.get_promotion(msg);
							app.ViewManager.Show_Screen_Data_Promotion(6, msg);	
						}	
					}				
				}
			};
			
			Thread thread= new Thread() {
				@Override
				public void run() {
						
					//call to server
				
					    String result=ApiHelper.Upload_file(app.image_path+img.path,"image/jpg", app.ViewManager.getstring(R.string.Upload_Key));
					    Message message = handler.obtainMessage(1, result);
						handler.sendMessage(message);
								
				}
			}; 
			
			thread.start();
			
	 }
	
	private void loadImage(){
		String path = app.download_path+ app.Preference.getBrand();
		File image_dir = new File(path);
		if(image_dir.exists() && !app.Preference.getBrand().equals(""))
		{
			Bitmap pho=Bitmap_Manager.getResourceScale(path, 320, 480);
			brandimg.setImageBitmap(pho);			
		}
	}
	
	
}
