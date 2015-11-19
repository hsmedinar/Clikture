package com.abyte.piclt2.ui;



import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.abyte.piclt2.R;
import com.abyte.piclt2.piclt2_App;
import com.abyte.piclt2.api.ApiHelper;
import com.abyte.xmlrpcservices.Api;

/**
 * this class show and manager login scream
 */


public class Login implements OnClickListener{
	

	
	public piclt2_App app;
	
	public LinearLayout login_layout;
	public Button btn_new_user;		
	public Button btn_login;
	

	private EditText loginuser;
	private EditText pass;
	private CheckBox remember;
	private ApiHelper api = new ApiHelper();
	private ImageView brandimg;

	//private LocationManager locManager;
	//private LocationListener locListener;
	
	private LocationManager locManager;
	private LocationListener locListener = new MyLocationListener();
	private boolean gps_enabled = false;
	private boolean network_enabled = false;
	Context context;
	
	private FileOutputStream fileOutputStream = null;
	
	public Login(piclt2_App app) {
		super();

		this.app=app;
		context = app;
	}


	public View Show_view()
	{
	
		login_layout=(LinearLayout) app.ViewManager.Inflate_View(R.layout.login);	
		loginuser=(EditText) login_layout.findViewById(R.id.loginuser);
		pass=(EditText) login_layout.findViewById(R.id.pass);
		brandimg =(ImageView) login_layout.findViewById(R.id.brandimg);
		
		btn_new_user=(Button) login_layout.findViewById(R.id.new_user);
		btn_login=(Button) login_layout.findViewById(R.id.login);
		remember=(CheckBox) login_layout.findViewById(R.id.remember);
		app.Preference.setLogin("1");
		
		if(app.Preference.getRemenber().equals("0"))
			remember.setChecked(false);
		
		btn_new_user.setOnClickListener(this);
		btn_login.setOnClickListener(this);
		
		locManager = 
    		(LocationManager) app.getSystemService(app.mContext.LOCATION_SERVICE);
		
		if(app.Preference.getBrand().equals("")){
			Log.i("BRAND", "Ubication");
			StartLocalizacion();
		}else{
			Log.i("BRAND", "Exits brand");
			String path = app.download_path+ app.Preference.getBrand();
			File image_dir = new File(path);
			if(image_dir.exists())
			{
				Bitmap pho=Bitmap_Manager.getResourceScale(path, 320, 480);
				brandimg.setImageBitmap(pho);			
			}
		}  
		
		
		
    	return login_layout;
		
	}
	  
    private boolean StartLocalizacion(){
    	// exceptions will be thrown if provider is not permitted.
    	boolean valor=false; 
    	try {
			gps_enabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception ex) {
		}
		try {
			network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception ex) {
		}

		// don't start listeners if no provider is enabled
		if (!gps_enabled && !network_enabled) {
			Toast.makeText(app.mContext, "GPS tracking and NETWORK are disabled", Toast.LENGTH_SHORT).show();
			Log.i("BRAND", "GPS tracking and NETWORK are disabled");
			//Toast.makeText(app.mContext, "Set imagen default", Toast.LENGTH_LONG).show();
			String default_image = api.Get_Image_Brand(app.ViewManager.getstring(R.string.defaultimage));
			if (!default_image.equals("")){
			    brandimg.setImageBitmap(DownloadImage(default_image));
			}
			/*
			Builder confirm=new AlertDialog.Builder(app.mContext);
			confirm.setTitle("Clikture");
			confirm.setMessage("Please, activate GPS");
			confirm.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
							public void onClick(
							DialogInterface dialog, int arg1) {										
								Intent myin = new Intent(Settings.ACTION_SECURITY_SETTINGS);
								myin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								app.startActivity(myin);
								return;
							}
			});					
			confirm.show();
			*/
			valor=false;
		}

		if (gps_enabled) {
			locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1500, 0, locListener);
			valor=true;
		}
		if (network_enabled) {
			locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1500, 0, locListener);
			valor=true;
		}
		
		return valor;
    	
    }
    
	class MyLocationListener implements LocationListener {
		
		public void onLocationChanged(Location location) {
			if (location != null) {
				// This needs to stop getting the location data and save the battery power.
				locManager.removeUpdates(locListener); 
				app.Preference.setLatitude(String.valueOf(location.getLatitude()));
	    		app.Preference.setLongitude(String.valueOf(location.getLongitude()));
	    	 	//ShowPosition("43.8315581","-79.3551732");
	    		Log.i("GPSLOCATION", String.valueOf(location.getLatitude()) + " / " + String.valueOf(location.getLongitude()));
	    		ShowPosition(app.Preference.getLatitude(),app.Preference.getLongitude()); 
			} 
		}
		
		public void onProviderDisabled(String provider) {}
		public void onProviderEnabled(String provider) {}
		public void onStatusChanged(String provider, int status, Bundle extras) {}
		
	}
	
    private void ShowPosition(String lat, String lon) {  
    	   
    		
    	try{
    		String value = api.Get_Geo_Ubication(lat,lon);
    		Log.i("value", value);
    		if(!value.equals("")){    
    			String image = api.Get_Image_Brand(value);
    			if (!image.equals("")){
    			    brandimg.setImageBitmap(DownloadImage(image));
    			}else{
    				//Toast.makeText(app.mContext, "Set imagen default", Toast.LENGTH_LONG).show();
        			String default_image = api.Get_Image_Brand(app.ViewManager.getstring(R.string.defaultimage));
        			if (!default_image.equals("")){
        			    brandimg.setImageBitmap(DownloadImage(default_image));
        			}	
    			}
    		}else{
    			//Toast.makeText(app.mContext, "Set imagen default", Toast.LENGTH_LONG).show();
    			String default_image = api.Get_Image_Brand(app.ViewManager.getstring(R.string.defaultimage));
    			if (!default_image.equals("")){
    			    brandimg.setImageBitmap(DownloadImage(default_image));
    			}
    		}
    	
    		
    	}catch(Exception e){
    		Builder confirm=new AlertDialog.Builder(app.mContext);
			confirm.setTitle("Clikture");
			confirm.setMessage("Error internet connection");
			confirm.show();    		
    	}
   }  
    	
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		if(btn_new_user.equals(v))
		{
		Registration rg=new Registration(this.app);
		app.ViewManager.chenge_view(rg.Show_view(),2);		
		}
		
		
		if(btn_login.equals(v))
		{
			if(loginuser.getText().toString().length()<1)
			{
				msgerror("Login Fail","Enter email address");
				loginuser.requestFocus();
				return;
			}
			if(pass.getText().toString().length()<1)
			{
				msgerror("Login Fail","Enter password");
				pass.requestFocus();
				return;
			}
			
			/** Validate in drupal */
			 
			 Api result= new Api();
			 boolean connected = result.LogintoDrupal(loginuser.getText().toString(), pass.getText().toString());
			 if (connected)
              {
				  ImageList ima=new ImageList(this.app);
				  app.ViewManager.Hide_keypad(pass);
				  app.ViewManager.chenge_view(ima.Show_view(),3);
					
					app.Preference.setLogin("1");
					if(remember.isChecked())
						app.Preference.setRemenber("1");
					else
						app.Preference.setRemenber("0");

              } 

			  if (!connected)
              {				 
				msgerror("Login Fail","Invalid user or password");
              }
			  

		}
		
	}
	
	private void msgerror(String title, String msg){
		
		Builder confirm=new AlertDialog.Builder(app.mContext);
		confirm.setTitle(title);
		confirm.setMessage(msg);
		confirm.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
						public void onClick(
						DialogInterface dialog, int arg1) {										

						}
		});					
		confirm.show();
		
     }	 
	
	
	private InputStream OpenHttpConnection(String urlString) 
    throws IOException
    {
        InputStream in = null;
        int response = -1;

        URL url = new URL(urlString); 
        URLConnection conn = url.openConnection();

        if (!(conn instanceof HttpURLConnection))                     
            throw new IOException("Not an HTTP connection");

        try{
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect(); 

            response = httpConn.getResponseCode();                 
            if (response == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();                                 
            }                     
        }
        catch (Exception ex)
        {
            throw new IOException("Error connecting");            
        }
        return in;     
    }
	
	 private Bitmap DownloadImage(String URL)
	    {        
	        Bitmap bitmap = null;
	        InputStream in = null;        
	        try {
	            in = OpenHttpConnection(URL);
	            bitmap = BitmapFactory.decodeStream(in);
	            String name = System.currentTimeMillis()+".jpg";
	            fileOutputStream = new FileOutputStream(app.download_path + name);
	            BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);
	            bitmap.compress(CompressFormat.JPEG, 50, bos);
	            bos.flush();
	            bos.close();
	            in.close();
	            app.Preference.setBrand(name);
	            Log.i("Default_imge", app.Preference.getBrand());
	        } catch (IOException e1) {
	            // TODO Auto-generated catch block
	            e1.printStackTrace();
	        }
	        return bitmap;                
	    }
	
  
	 
}
