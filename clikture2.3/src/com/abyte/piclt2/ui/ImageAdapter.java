package com.abyte.piclt2.ui;


import java.io.File;

import com.abyte.piclt2.R;
import com.abyte.piclt2.piclt2_App;
import com.abyte.piclt2.model.Data_Access;
import com.abyte.piclt2.model.Image;
import com.abyte.piclt2.model.Image_Cursor;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * this class is the adapter of image view
 */

public class ImageAdapter extends CursorAdapter {
	
	public piclt2_App app;
	public LinearLayout view_layout;
	public Cursor cursor;
	
	public ImageAdapter(piclt2_App app, Cursor c) {
		super(app.mContext, c);
		cursor=c;

			this.app=app;
			
		}
	
	

	@Override
	public void bindView(View convertView, Context context, Cursor cursor) {
		
		Image_Cursor c =(Image_Cursor)cursor;
		view_holder row_view;
		row_view= new view_holder();
		row_view.date =(TextView)convertView.findViewById(R.id.date);
		row_view.gps =(TextView)convertView.findViewById(R.id.gps);
		row_view.image =(ImageView)convertView.findViewById(R.id.image);
		
		row_view.date.setText(Data_Access.DateLongToStr(c.getDate()));
		String lat="";
		if(c.getLatitude()!=null)
			if(c.getLatitude().length()>6)
				lat=c.getLatitude().substring(0, 6);
			else
				lat=c.getLatitude();
		String lon="";
		if(c.getLongitude()!=null)
			if(c.getLongitude().length()>6)
				lon=c.getLongitude().substring(0, 6);
			else
				lon=c.getLongitude();
		if(lat.length()!=0 || lon.length()!=0)
			row_view.gps.setText("lat: "+lat+"  lon:"+lon);
		else
			row_view.gps.setText("");
		
		
		String path = app.image_path+c.getPath();
	
		if(path!=null && path.length()>0)
		{
			
			File image_dir = new File(path);
			if(image_dir.exists())
			{
				Bitmap pho=Bitmap_Manager.getResourceScale(path, 320, 480);
				row_view.image.setImageBitmap(pho);
				
			}
			else
			{
				row_view.image.setImageBitmap(null);
			}
		}
		convertView.setTag(new Image(c.getPath(),c.getDate(),c.getLatitude(),c.getLongitude()));
		
		convertView.setOnClickListener(new OnClickListener(){

			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Image i = (Image) v.getTag();
				Show_Image si=new Show_Image(app,i);
		        app.ViewManager.chenge_view(si.Show_view(), 5);
				
			}} );
	}
	

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		
		if(cursor!=null && !cursor.isClosed())
			cursor.close();
		
		super.finalize();
	}



	@Override
	public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		
		
		return  app.ViewManager.Inflate_View(R.layout.image);
	}
	
	private class view_holder{
			
			public TextView date;
			public TextView gps;
			public ImageView image;

		
		}

	public void Update(Cursor cursor)
	{
		cursor.close();
		this.changeCursor(cursor);
	}
	

}
