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
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.abyte.piclt2.R;
import com.abyte.piclt2.piclt2_App;
import com.abyte.piclt2.api.ApiHelper;
import com.abyte.piclt2.model.Image;
import com.abyte.xmlrpcservices.Api;

public class Show_Temp {
	
	public piclt2_App app;
	public ProgressDialog pDialog;
	public Image image;
	public LinearLayout view_layout;
	private String name_img_ext;
	private boolean external=false;
	
	private LocationManager locManager;
	private LocationListener locListener = new MyLocationListener();
	private boolean gps_enabled = false;
	private boolean network_enabled = false;
	private FileOutputStream fileOutputStream = null;
	
	public Show_Temp(piclt2_App app,Image image) {
		super();
		this.app=app;
		this.image=image;		
		app.Preference.setCurrent_Image(image.getPath());		
	}
	
	public Show_Temp(piclt2_App app,String name_img) {
		super();
		this.app=app;
		this.name_img_ext=name_img;		
		external=true;		
	}
	 
	public Show_Temp(piclt2_App app) {
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
		
		view_layout=(LinearLayout) app.ViewManager.Inflate_View(R.layout.show_temp);
		ConnectivityManager connectivityManager = (ConnectivityManager) app.getSystemService(app.mContext.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        
        LocationManager locationManager =
            (LocationManager)app.getSystemService(app.mContext.LOCATION_SERVICE);
     
        if(activeNetworkInfo!=null && activeNetworkInfo.isConnected() && locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
        	comenzarLocalizacion();
        		if(!external){		
        			Upload_Image(image.path,image.date);		
        			external=false;	       
        		}else{						   
        			//Toast.makeText(app.mContext, app.Preference.getLatitude() + "/" + app.Preference.getLongitude(), Toast.LENGTH_LONG).show();
        			String dt= System.currentTimeMillis()+"";
        			save_imagen_external(name_img_ext,dt);			  
        			Upload_Image(name_img_ext,dt);		  			  				
        		}		   
        }else{
        	Builder confirm=new AlertDialog.Builder(app.mContext);
			confirm.setTitle("Clikture");
			confirm.setMessage("Please, could you review that the options \"Use wireless networks\" and \"GPS\" in your device?");
			confirm.show(); 
        }
		
		return view_layout;
	}
	
	
	private void save_imagen_external(String name, String date){
		Image im=new Image(name,date,"","");
	    app.Data.Insert_Image(im);
	}
	
	private void save_in_drupal(String name,String date,String lat,String lon){
		Api result= new Api();
		result.registerdatamobile(name, date, lat, lon);		
	}
	
	private void delete_image(String name_img){
		if(this.image!=null && this.image.path.length()>0) 
		{
			File image_dir = new File(app.image_path + name_img);
			if(image_dir.exists())
			{				
				image_dir.delete();
			}
		}
		
		app.Data.Delete_Image(name_img);		
	 }
	
	
	
	
	private void Upload_Image(final String img,final String date)
	 {
		
		 pDialog = ProgressDialog.show(app.mContext, null, app.ViewManager.getstring(R.string.Uploading_Image));
	    	
	    	final Handler handler = new Handler() {
				@Override
				public void handleMessage(Message message) {
					if (pDialog != null)
						pDialog.cancel();
					String msg =(String)message.obj;
					if (Integer.parseInt(msg)==-2){
						Toast.makeText(app.mContext, "Sorry, Error server connection", Toast.LENGTH_LONG).show();
						delete_image(img);
					}else if (Integer.parseInt(msg)==-1){						
						Toast.makeText(app.mContext, "Sorry, we do not recognize this image", Toast.LENGTH_LONG).show();						
						delete_image(img);
						if(app.Preference.getRemenber().equals("1"))
	        			{
						 app.ViewManager.Show_Screen(3);
	        			}else{
	        			  app.ViewManager.Show_Screen(1);
	        			}
					}else{
						    //Toast.makeText(app.mContext, msg, Toast.LENGTH_LONG).show();
					        app.get_promotion(msg);
							app.ViewManager.Show_Screen_Data_Promotion(6, msg);	
					}
					
				}
			};
			
			Thread thread= new Thread() {
				@Override
				public void run() {
								
					showposition(app.Preference.getLatitude(),app.Preference.getLongitude());
					save_in_drupal(img,app.Data.DateLongToStr(date),app.Preference.getLatitude(),app.Preference.getLongitude());
					String result=ApiHelper.Upload_file(app.image_path + img,"image/jpg", app.ViewManager.getstring(R.string.Upload_Key));									
					Message message = handler.obtainMessage(1, result);
					handler.sendMessage(message);
				}
			}; 
			
			
			try{
			 thread.start();
			 thread.sleep(2000);
			}catch(Exception e){
				Toast.makeText(app.mContext, "Error connection server", Toast.LENGTH_LONG).show();
			}
	 }
	
	
	/******************************** update ubication *************************/
	
    private void showposition(String lat, String lon) {  
		
			String valor = ApiHelper.Get_Geo_Ubication(lat,lon);		
			if(!valor.equals("")){    			
				String image = ApiHelper.Get_Image_Brand(valor);
					if (!image.equals("")){
						DownloadImage(image);
					}
				}
			
    }
	
    private void comenzarLocalizacion(){
    	// exceptions will be thrown if provider is not permitted.
    	
    	locManager = 
    		(LocationManager) app.getSystemService(app.mContext.LOCATION_SERVICE);
		try {
			gps_enabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			Log.i("GPS", "ENABLE");
		} catch (Exception ex) {
			//Toast.makeText(app.mContext, ex.getMessage(), Toast.LENGTH_LONG).show();
			Log.i("GPS", "NO ENABLE");
		}
		try {
			network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			Log.i("RED", "ENABLE");
		} catch (Exception ex) {
			//Toast.makeText(app.mContext, ex.getMessage(), Toast.LENGTH_LONG).show();
			Log.i("GPS", "NO ENABLE");
		}

		// don't start listeners if no provider is enabled
		if (!gps_enabled && !network_enabled) {
			
			
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
			return;
			
		}

		if (gps_enabled) {
			//Toast.makeText(app.mContext, "GPS", Toast.LENGTH_LONG).show();
			locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
		}
		if (network_enabled) {
			//Toast.makeText(app.mContext, "NETWORK", Toast.LENGTH_LONG).show();
			locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListener);
		}
    }
    
    
    class MyLocationListener implements LocationListener {
    	
		public void onLocationChanged(Location location) {
			if (location != null) {
				// This needs to stop getting the location data and save the battery power.
				locManager.removeUpdates(locListener); 

				//String londitude = "Londitude: " + location.getLongitude();
				//String latitude = "Latitude: " + location.getLatitude();
				//String altitiude = "Altitiude: " + location.getAltitude();
				//String accuracy = "Accuracy: " + location.getAccuracy();
				//String time = "Time: " + location.getTime();
				app.Preference.setLatitude(String.valueOf(location.getLatitude()));
	    		app.Preference.setLongitude(String.valueOf(location.getLongitude()));
			} 
		}


		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

	
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}
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
	
	 private void DownloadImage(String URL)
	    {        
	        Bitmap bitmap = null;
	        InputStream in = null;        
	        try {
	            in = OpenHttpConnection(URL);
	            bitmap = BitmapFactory.decodeStream(in);
	            String name = System.currentTimeMillis()+".jpg";
	            fileOutputStream = new FileOutputStream(app.download_path + name);
	            BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);
	            bitmap.compress(CompressFormat.JPEG, 10, bos);
	            bos.flush();
	            bos.close();
	            in.close();
	            app.Preference.setBrand(name);
	        } catch (IOException e1) {
	            // TODO Auto-generated catch block
	            e1.printStackTrace();
	        }              
	    }
	 

}
