package com.abyte.piclt2;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.abyte.piclt2.api.ApiHelper;
import com.abyte.piclt2.model.Data_Access;
import com.abyte.piclt2.model.Promotion;
import com.abyte.piclt2.ui.Bitmap_Manager;

public class campaign implements OnClickListener {
	
	public Data_Access Data;
	public ApiHelper api;
	public piclt2_App app;
	public String id;
	
	public LinearLayout view_layout;

	
	TableLayout tgroup;
	Promotion Promo;
	ImageView brandimg;
	
	public campaign(piclt2_App app, String id) {
		super();
		this.app=app;
		this.id=id;
	}

	public View Show_view()
	{
		view_layout=(LinearLayout) app.ViewManager.Inflate_View(R.layout.promotion);
		brandimg =(ImageView) view_layout.findViewById(R.id.brandimg);
		generateContent();
		loadImage();	
		return view_layout;
	}
	
	private void generateContent(){
		ArrayList groupname = (ArrayList) app.Data.getGroupName();
		final int lastvalor = groupname.size() - 1;

		int x=0;
			
		if(x<=groupname.size() - 1){
			ArrayList<Promotion> result= app.Data.getIconsForGroup(groupname.get(x).toString());
			tgroup=(TableLayout) view_layout.findViewById(R.id.tgroup1);
			tgroup.setVisibility(View.VISIBLE);
			createrowicons(result,tgroup,groupname.get(x).toString());	
	       x++; 
		}
		
		if(x<=groupname.size() - 1){
			ArrayList<Promotion> result= app.Data.getIconsForGroup(groupname.get(x).toString());			
			tgroup=(TableLayout) view_layout.findViewById(R.id.tgroup2);
			tgroup.setVisibility(View.VISIBLE);	
			createrowicons(result,tgroup,groupname.get(x).toString());		
	       x++; 
		}
		
		if(x<=groupname.size() - 1){
			ArrayList<Promotion> result= app.Data.getIconsForGroup(groupname.get(x).toString());			
			tgroup=(TableLayout) view_layout.findViewById(R.id.tgroup3);
			tgroup.setVisibility(View.VISIBLE);
			if (x==lastvalor)
		      createFinalicons(result,tgroup,groupname.get(x).toString());
			else
			  createrowicons(result,tgroup,groupname.get(x).toString());	
	       x++; 
		}



		if(x<=groupname.size() - 1){
			ArrayList<Promotion> result= app.Data.getIconsForGroup(groupname.get(x).toString());			
			tgroup=(TableLayout) view_layout.findViewById(R.id.tgroup4);
			tgroup.setVisibility(View.VISIBLE);	
			createrowicons(result,tgroup,groupname.get(x).toString());
	       x++; 
		}
		
		
		if(x<=groupname.size() - 1){
			ArrayList<Promotion> result= app.Data.getIconsForGroup(groupname.get(x).toString());			
			tgroup=(TableLayout) view_layout.findViewById(R.id.tgroup5);
			tgroup.setVisibility(View.VISIBLE);	
			if (x==lastvalor)
			      createFinalicons(result,tgroup,groupname.get(x).toString());
				else
				  createrowicons(result,tgroup,groupname.get(x).toString());
	       x++; 
		}
		
		
		if(x<=groupname.size() - 1){
			ArrayList<Promotion> result= app.Data.getIconsForGroup(groupname.get(x).toString());			
			tgroup=(TableLayout) view_layout.findViewById(R.id.tgroup6);
			tgroup.setVisibility(View.VISIBLE);	
			createrowicons(result,tgroup,groupname.get(x).toString());
	       x++; 
		}
		
		
		if(x<=groupname.size() - 1){
			ArrayList<Promotion> result= app.Data.getIconsForGroup(groupname.get(x).toString());			
			tgroup=(TableLayout) view_layout.findViewById(R.id.tgroup7);
			tgroup.setVisibility(View.VISIBLE);	
			if (x==lastvalor)
			      createFinalicons(result,tgroup,groupname.get(x).toString());
				else
				  createrowicons(result,tgroup,groupname.get(x).toString());
	       x++; 
		}
		
		
		if(x<=groupname.size() - 1){
			ArrayList<Promotion> result= app.Data.getIconsForGroup(groupname.get(x).toString());			
			tgroup=(TableLayout) view_layout.findViewById(R.id.tgroup8);
			tgroup.setVisibility(View.VISIBLE);	
			createrowicons(result,tgroup,groupname.get(x).toString());
	       x++; 
		}
		
		
		if(x<=groupname.size() - 1){
			ArrayList<Promotion> result= app.Data.getIconsForGroup(groupname.get(x).toString());			
			tgroup=(TableLayout) view_layout.findViewById(R.id.tgroup9);
			tgroup.setVisibility(View.VISIBLE);	
			if (x==lastvalor)
			      createFinalicons(result,tgroup,groupname.get(x).toString());
				else
				  createrowicons(result,tgroup,groupname.get(x).toString());
	       x++; 
		}
		
		if(x<=groupname.size() - 1){
			ArrayList<Promotion> result= app.Data.getIconsForGroup(groupname.get(x).toString());			
			tgroup=(TableLayout) view_layout.findViewById(R.id.tgroup10);
			tgroup.setVisibility(View.VISIBLE);	
			createrowicons(result,tgroup,groupname.get(x).toString());
	       x++; 
		}
		
		
		if(x<=groupname.size() - 1){
			ArrayList<Promotion> result= app.Data.getIconsForGroup(groupname.get(x).toString());			
			tgroup=(TableLayout) view_layout.findViewById(R.id.tgroup11);
			tgroup.setVisibility(View.VISIBLE);	
			if (x==lastvalor)
			      createFinalicons(result,tgroup,groupname.get(x).toString());
				else
				  createrowicons(result,tgroup,groupname.get(x).toString());
	       x++; 
		}
	
		if(x<=groupname.size() - 1){
			ArrayList<Promotion> result= app.Data.getIconsForGroup(groupname.get(x).toString());			
			tgroup=(TableLayout) view_layout.findViewById(R.id.tgroup12);
			tgroup.setVisibility(View.VISIBLE);	
			createrowicons(result,tgroup,groupname.get(x).toString());
	       x++; 
		}
		
		if(x<=groupname.size() - 1){
			ArrayList<Promotion> result= app.Data.getIconsForGroup(groupname.get(x).toString());			
			tgroup=(TableLayout) view_layout.findViewById(R.id.tgroup13);
			tgroup.setVisibility(View.VISIBLE);	
			if (x==lastvalor)
			      createFinalicons(result,tgroup,groupname.get(x).toString());
				else
				  createrowicons(result,tgroup,groupname.get(x).toString());
	       x++; 
		}
		
		if(x<=groupname.size() - 1){
			ArrayList<Promotion> result= app.Data.getIconsForGroup(groupname.get(x).toString());			
			tgroup=(TableLayout) view_layout.findViewById(R.id.tgroup14);
			tgroup.setVisibility(View.VISIBLE);	
			createrowicons(result,tgroup,groupname.get(x).toString());
	       x++; 
		}
		
	}
	
	public void createrowicons(ArrayList<Promotion> result, TableLayout  tgroup, String namegroup){
		
		// set margin to tablerow	
        TableLayout.LayoutParams trparm = new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER);	         
        trparm.setMargins(0, 8, 0, 0);

		// create asign view and parameter to tablrow
		TableRow trtitle = new TableRow(app.mContext); 
        trtitle.setLayoutParams(trparm);
        trtitle.addView(txttitle(namegroup));
        tgroup.addView(trtitle);
		
		for (int f=0; f<=result.size() - 1; f++) {
			 final Promotion n=(Promotion)result.get(f);		 
	          TableRow tr = new TableRow(app.mContext); 
	          tr.setLayoutParams(trparm);
	              ImageView b = new ImageView (app.mContext);
	              b.setImageBitmap(DownloadImage(n.getIcon()));	
	              b.setOnClickListener(new OnClickListener() {					
					public void onClick(View v) {			
						String url = n.getUrl();
						Intent i = new Intent(Intent.ACTION_VIEW);
						i.setData(Uri.parse(url));
						app.mContext.startActivity(i);
						
					}
				});
	              
	            tr.addView(b);
	            tr.setBackgroundColor(f);
	            tgroup.addView(tr);		           
	    }	
		System.gc();
	}
	
	private TextView txttitle(String namegroup){
		TextView title = new TextView(app.mContext);
		title.setText(namegroup);
		title.setTextColor(app.getResources().getColor(R.color.narange));
		title.setTextSize(20);
		title.setTypeface(null, Typeface.BOLD);		
		return title;
	}
	
	
	public void createFinalicons(ArrayList<Promotion> result, TableLayout  tgroup, String namegroup){
		
		
		// set margin to tablerow
        TableLayout.LayoutParams trparm = new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);	         
        trparm.setMargins(0, 0, 0, 0);
    
		// create table row title
		TableRow trtitle = new TableRow(app.mContext); 
		trtitle.setLayoutParams(trparm);
	    trtitle.addView(txttitle(namegroup));
	    tgroup.addView(trtitle);
	        

		TableRow tr = new TableRow(app.mContext);
        tr.setLayoutParams(trparm);
        
        LinearLayout ln = new LinearLayout(app.mContext);
        LayoutParams lnparm = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        lnparm.setMargins(5, 0, 0, 0);
       
         
		for (int f=0; f<=result.size() - 1; f++) {
			 final Promotion n=(Promotion)result.get(f);		 	          
	              ImageView b = new ImageView (app.mContext);
	              b.setImageBitmap(DownloadImage(n.getIcon()));
	              b.setOnClickListener(new OnClickListener() {
					

					public void onClick(View v) {			
						String url = n.getUrl();
						Intent i = new Intent(Intent.ACTION_VIEW);
						i.setData(Uri.parse(url));
						app.mContext.startActivity(i);
						
					}
				});
	         b.setLayoutParams(lnparm);
	         ln.addView(b);
	    }	
		tr.addView(ln);
		tgroup.addView(tr);	
		
		
	}
		
	public void onClick(View v) {
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
            in.close();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return bitmap;                
    }
    
    private void loadImage(){
		String path = app.download_path+ app.Preference.getBrand();
		//Toast.makeText(app.mContext, path, Toast.LENGTH_LONG).show();
		File image_dir = new File(path);
		if(image_dir.exists() && !app.Preference.getBrand().equals(""))
		{
			Bitmap pho=Bitmap_Manager.getResourceScale(path, 320, 480);
			brandimg.setImageBitmap(pho);			
		}
	}
	
}


