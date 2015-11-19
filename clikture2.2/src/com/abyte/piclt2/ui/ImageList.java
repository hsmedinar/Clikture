package com.abyte.piclt2.ui;
import com.abyte.piclt2.R;
import com.abyte.piclt2.piclt2_App;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * this class show image list
 */

public class ImageList {
	

	
	public piclt2_App app;
	public LinearLayout view_layout;
	public ImageAdapter adapter;
	
	
	public ImageList(piclt2_App app) {
		super();
		this.app=app;
		
	}
	

	public void Update_View()
	{
		
	
	}

	public View Show_view()
	{

		view_layout=(LinearLayout) app.ViewManager.Inflate_View(R.layout.image_list);
		
		ListView image_list_view=(ListView) view_layout.findViewById(R.id.ListView);
		adapter=new ImageAdapter(app,app.Data.getImage());
		image_list_view.setAdapter(adapter);
	
		Button new_image=(Button) view_layout.findViewById(R.id.new_image);
		new_image.setOnClickListener(new OnClickListener(){

			
			public void onClick(View arg0) {
				
				app.ViewManager.Show_Screen(4);
			}
			
		});

    	return view_layout;
		
	}


	
	

}
