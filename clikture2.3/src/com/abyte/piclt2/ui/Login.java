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
	
	private FileOutputStream fileOutputStream = null;
	
	public Login(piclt2_App app) {
		super();

		this.app=app;		
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
		
		
		if(app.Preference.getBrand().equals("")){		  
		  comenzarLocalizacion();
		  mostrarPosicion(app.Preference.getLatitude(),app.Preference.getLongitude());
		}else{
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
  
    private void mostrarPosicion(String lat, String lon) {  
    	   
    		
    	try{
    		String valor = api.Get_Geo_Ubication(lat,lon);
    		if(!valor.equals("")){    			
    			String image = api.Get_Image_Brand(valor);
    			if (!image.equals("")){
    			    brandimg.setImageBitmap(DownloadImage(image));
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
	        } catch (IOException e1) {
	            // TODO Auto-generated catch block
	            e1.printStackTrace();
	        }
	        return bitmap;                
	    }
	
  
	 
}
