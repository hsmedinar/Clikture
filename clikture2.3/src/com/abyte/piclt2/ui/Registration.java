package com.abyte.piclt2.ui;



import java.io.File;

import com.abyte.piclt2.R;
import com.abyte.piclt2.piclt2_App;
import com.abyte.piclt2.api.ApiHelper;
import com.abyte.xmlrpcservices.Api;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
/**
 * this class show and manager network scream
 */

public class Registration implements OnClickListener{
	

	
	public piclt2_App app;
	
	public LinearLayout view_layout;
	public Button cancel;		
	public Button submit;

	private EditText email;
	private EditText pass;
	private EditText repass;

	private CheckBox remember;
	private ImageView brandimg;

	
	public Registration(piclt2_App app) {
		super();
		this.app=app;
		
	}


	public View Show_view()
	{
		view_layout=(LinearLayout) app.ViewManager.Inflate_View(R.layout.registration);
	
		
		submit=(Button) view_layout.findViewById(R.id.submit);
		cancel=(Button) view_layout.findViewById(R.id.cancel);
		email=(EditText) view_layout.findViewById(R.id.email);
		pass=(EditText) view_layout.findViewById(R.id.pass);
		repass=(EditText) view_layout.findViewById(R.id.repass);
		brandimg =(ImageView) view_layout.findViewById(R.id.brandimg);
		
		remember=(CheckBox) view_layout.findViewById(R.id.remember);
		if(app.Preference.getRemenber().equals("0"))
			remember.setChecked(false);
	
		submit.setOnClickListener(this);
		cancel.setOnClickListener(this);
		loadImage();
    	return view_layout;
		
	}



	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		if(cancel.equals(v))
		{
			app.ViewManager.Show_Login();
			return;
		}
		
		
		if(submit.equals(v))
		{
			
			
			
			if(email.getText().toString().length()<1)
			{
				msgerror("Register","Enter email address");
		
				email.requestFocus();
				return;
			} 
			if(!email.getText().toString().matches("^[-a-zA-Z0-9\\-_\\.]+@[-a-zA-Z0-9\\-_\\.]+[\\.][-a-zA-Z0-9\\-_\\.]+"))
			{
				msgerror("Register","Enter valid email address");
		
				email.requestFocus();
				return;
			}
			if(pass.getText().toString().length()<1)
			{
				msgerror("Register","Enter password");
		
				pass.requestFocus();
				return;
			}
			
		
			if(!pass.getText().toString().equals(repass.getText().toString()))
			{
				msgerror("Register","Passwords do not match");
			
				repass.requestFocus();
				return;
			}
			
			/**
			 * Here you can store the user data in your server
			 */
			
			
			 ApiHelper resultvalidate = new ApiHelper(); 
			 
			 String validate = resultvalidate.Get_Validtae_User(email.getText().toString());
			 
			 if (validate.equals("1")){
				 msgerror("Register","User already registered");
				 return;
			 }else{
				 Api result= new Api();
				 boolean connected = result.registeruser(email.getText().toString(),
	        			 email.getText().toString(),
	        			 pass.getText().toString());
				 
				 if (!connected)
	        	 {
					 msgerror("Register","Sorry register not completed Please retry");
					 return;
	        	 }
	        	 if (connected)
	        	 {
	        		app.Preference.setUser(email.getText().toString());
	     			app.Preference.setPass(pass.getText().toString());
	     			app.Preference.setLogin("1");
	     			if(remember.isChecked()){
	     				app.Preference.setRemenber("1");
	     			}	
	     			else{
	     				app.Preference.setRemenber("0");
	        	    } 	     			
	  				ImageList ima=new ImageList(this.app);
     				app.ViewManager.Hide_keypad(pass);
     				app.ViewManager.chenge_view(ima.Show_view(),3);     				
	        	 }
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
