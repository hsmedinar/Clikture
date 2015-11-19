package com.abyte.piclt2;


import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.SensorManager;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.OrientationEventListener;
import android.view.WindowManager;

import com.abyte.piclt2.ui.Show_Temp;
import com.abyte.piclt2.ui.View_Manager;

public class StartActivity extends Activity {
    /** Called when the activity is first created. */
	
	public int grados;
	//public int orientation=1;//1 Portrait, 2 LamdScape  
	public int orientation_delay=5;
	public boolean changing_orientation=false;
	public OrientationEventListener myOrientationEventListener;
	String pathimg;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.registration);
        
        final piclt2_App app=(piclt2_App) this.getApplication();
        
        app.mContext=this;
        app.ViewManager=new View_Manager(app);
        
        Display display = ((WindowManager) app.mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		app.width = display.getWidth();
		app.height = display.getHeight();
		
		if(app.height>app.width)
		{
			app.orientation=1;
		}
		else
		{
			app.orientation=2;
		}
		
		change_orientation();
        
        myOrientationEventListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL){
       		@Override
       		public void onOrientationChanged(int degrees) {
       			grados=degrees;
       			app.degrees=degrees;
       			if(((degrees<45 || degrees >315)||(degrees>135 && degrees <225)) && !changing_orientation && app.orientation==2)
       			{
       				changing_orientation=true;
           			 new Handler().postDelayed(new Runnable(){
           	            //@Override
           	            public void run() {
           	            	changing_orientation=false;
           	            	change_orientation();
           	            }
           	       }, orientation_delay);
       			}
       			
       			if(((degrees>45 && degrees <135)||(degrees >225 && degrees <315)) && !changing_orientation && app.orientation==1)
       			{
       				changing_orientation=true;
           			 new Handler().postDelayed(new Runnable(){
           	            //@Override
           	            public void run() {
           	            	changing_orientation=false;
           	            	change_orientation();
           	            }
           	       }, orientation_delay);
       			}

   
       		}};
       		
       		if (myOrientationEventListener.canDetectOrientation()){
               	
               	myOrientationEventListener.enable();
               }
    	}
		
		@Override
		protected void onDestroy() {
			piclt2_App app=(piclt2_App) this.getApplication();
			if(app.ViewManager!=null && app.ViewManager.camera!=null)

					try {
						//app.ViewManager.camera.finalize();
					} catch (Throwable e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			super.onDestroy();
			System.gc();
			Log.i("DESTROYCLIKTURE", "DESTROSADO");
		
		}
		
		
		@Override
		public void onSaveInstanceState(Bundle savedInstanceState) {
		  
		  final piclt2_App app=(piclt2_App) this.getApplication();
		  savedInstanceState.putInt("Screen",app.Screen);
		  
		  super.onSaveInstanceState(savedInstanceState);
		}

		@Override
		public void onRestoreInstanceState(Bundle savedInstanceState) {
		  super.onRestoreInstanceState(savedInstanceState);


		  
		  final piclt2_App app=(piclt2_App) this.getApplication();
		  app.Screen=savedInstanceState.getInt("Screen");
		  
		}
	
		public void change_orientation()
		{

			final piclt2_App app=(piclt2_App) this.getApplication();
	
			if((grados<45 || grados >315)||(grados>135 && grados <225))
			{
				if(app.orientation!=1)
				{

					app.orientation=1;
				}					
				
			}
			else
			{
				
				if(app.orientation!=2)
				{
					app.orientation=2;
				}	

			}
			
			if(app.Screen!=3)
			{
			

					//app.orientation=1;
					app.screen_orientation=1;
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
					
					if(app.Screen!=0)
			    	{
			    		//app.ViewManager.Show_Screen(app.Screen);
			    	}
					
			    	else
			    	{
			    		
			    			Bundle bundle = this.getIntent().getExtras(); 
			        	
			        		if(bundle!=null){
			        			pathimg = bundle.getString("external");
			        			Show_Temp si=new Show_Temp(app,pathimg);
			        			app.ViewManager.chenge_view(si.Show_view(), 5);				                
			        			return;
			        		}	    
			        	
			        		if(app.Preference.getRemenber().equals("1"))
			        			{
				        			app.ViewManager.Show_Screen(3);
				        			return;
			        			}
				        else
				        {
				        	app.ViewManager.Show_Screen(1);
				        }
			    	}
				
			}
			
			else
			{
			
				
					if(app.orientation==1 && app.screen_orientation!=1)
					{
	
						app.screen_orientation=1;
						
						setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
					}
					
					if(app.orientation==2 && app.screen_orientation!=2)
					{	
							app.screen_orientation=2;
							setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);							
					}
				
					
				app.ViewManager.Show_Screen(3);
				
			}
			
		}


		@Override
		protected void onPause() {

			Log.i("PAUSECLIKTURE", "PAUSADO");
			super.onPause();
			System.gc();
			//
		}
		
		@Override
		protected void onStop() {
			// TODO Auto-generated method stub
			super.onStop();
			System.gc();
			finish();
			System.exit(0);
			Log.i("STOPCLIKTURE", "STOP");			
		}
		
		
		
    
}

