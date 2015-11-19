package com.abyte.piclt2.ui;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.abyte.piclt2.campaign;
import com.abyte.piclt2.piclt2_App;
import com.abyte.piclt2.model.Data_Access;

/**
 * this class manager scream flow
 */
public class View_Manager implements OnClickListener{
	

	public Data_Access Data;
	public LayoutInflater inflater;
	public LinearLayout.LayoutParams Layout_params;
	public Bitmap_Manager BitmapManager;
	public LinearLayout tab_area;
	public LinearLayout tab_frame;
	public LinearLayout lock;
	
	
	
	
	//notificaciones
	public ProgressBar progress_bar;
	public int Progress;
	public int Progress_limit;
	public int Progress_sleep;
	public int notification_type;
	public int notification_tab=0;
	public LinearLayout notificaciones;
	public Thread notification_thread; 
	public LinearLayout progress_layout;
	public TextView notification_text; 
	public ImageView notification_icon; 
	public OnClickListener notification_cancel;
	private Toast toast;
	public piclt2_App app;

	public Show_Camera camera;
	
	public View_Manager(piclt2_App app) {
		
		this.app=app;
		
		inflater=(LayoutInflater) app.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	
	
	public void Show_Login()
	{
		
		Login login=new Login(this.app);
		chenge_view(login.Show_view(),1);
	}

	
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	public void Hide_keypad()
	{
		Log.e("Hide_keypad()", "true");
		InputMethodManager imm = (InputMethodManager) app.mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(this.tab_area.getApplicationWindowToken(), 0);

	}
	public void Hide_keypad(View v)
	{
		Log.e("Hide_keypad", v.toString());
		
		InputMethodManager imm = (InputMethodManager) app.mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	
	}
	public void Show_Screen_Data_Promotion(int screen, String id)
	{	
		app.Screen=6;
		campaign re=new campaign(app, id);
		app.mContext.setContentView(re.Show_view());
		return;	
	}
	
	
	public void Show_Screen(int screen)
	{
		
		if(screen==3)
		{
			app.Screen=3;
			if(app.orientation!=app.screen_orientation)
			{
				if(app.orientation==1)
				{
					app.screen_orientation=1;	
					app.mContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				}
				
				if(app.orientation==2)
				{
					app.screen_orientation=2;
					app.mContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
				}
			}

			if(app.orientation==1)
			{
				ImageList re=new ImageList(app);
				app.mContext.setContentView(re.Show_view());
				return;
			}
			if(app.orientation==2)
			{
				Show_Cover sc=new Show_Cover(app,(app.height*6)/10,(app.width*6)/10);
				app.mContext.setContentView(sc.Show_view());
				return;
			}
			
		}
		else
		{
			if(app.screen_orientation!=1)
			{
				app.screen_orientation=1;	
				app.mContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			}
		}


		if(screen==1)
		{
			app.Screen=1;
			Show_Login();
			return;
		}
		if(screen==2)
		{
			app.Screen=2;
			Registration re=new Registration(app);
			app.mContext.setContentView(re.Show_view());
			return;
		}
		if(screen==4)
		{
			app.Screen=4;
			camera = null;
			camera=new Show_Camera(app);
			app.mContext.setContentView(camera.Show_view());
			return;
		}
		
		if(screen==5)
		{
			app.Screen=5;
			Show_Image si=new Show_Image(app);
            app.ViewManager.chenge_view(si.Show_view(), 5);
            return;
		}
		
		if(screen==6)
		{
			app.Screen=6;
			Show_Temp si=new Show_Temp(app);
            app.ViewManager.chenge_view(si.Show_view(), 6);
            return;
		}
			
		Show_Login();
		
	}
	


	public void chenge_view(View v,int Screen)
	{
		if(app.screen_orientation!=1)
		{
			app.screen_orientation=1;	
			app.mContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		app.mContext.setContentView(v);
		app.Screen=Screen;
	}

	public void Show_Content_view(View v)
	{
		app.mContext.setContentView(v);
	}
	public void Show_Content_view(int v)
	{
		app.mContext.setContentView(v);
	}
	public View Inflate_View (int LayoutID)
	{
		return this.inflater.inflate(app.mContext.getResources().getLayout(LayoutID),null);
	}
	public String getstring(int id)
	{
		return app.mContext.getString(id);
	}



	 public static int StrToInt(String s)
	 {
		 if(s==null)
			 return 0;
		 if(s.length()==0)
			 return 0;
		 int r=0;
		 try {
			r=Integer.parseInt(s);
		} catch (Exception e) {
			Log.e("Paser StrToInt",s);
			
		}
		return r;
	 }
	 
	 public static long StrToLong(String s)
	 {
		 if(s==null)
			 return 0;
		 if(s.length()==0)
			 return 0;
		 long r=0;
		 try {
			r=Long.parseLong(s);
		} catch (Exception e) {
			Log.e("Paser StrToInt",s);
			
		}
		return r;
	 }
	 
	 public void Show_Toast(String text,int duration)
	 {
		toast=Toast.makeText(app.mContext, text,duration);
		toast.show();
	 }
 
	
	
}
