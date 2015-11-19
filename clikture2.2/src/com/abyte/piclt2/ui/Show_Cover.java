package com.abyte.piclt2.ui;



import com.abyte.piclt2.R;
import com.abyte.piclt2.piclt2_App;
import com.abyte.piclt2.coverflow.CoverFlow;
import com.abyte.piclt2.coverflow.ResourceImageAdapter;
import com.abyte.piclt2.model.Image;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;
/**
 * this class show and manager cover
 */

public class Show_Cover implements OnClickListener{
	

	
	public piclt2_App app;	
	public LinearLayout view_layout;
	public int height;
	public int width;
	public  CoverFlow coverFlow1;
	public ResourceImageAdapter coverImageAdapter;
	
	public Show_Cover(piclt2_App app,int height,int width) {
		super();
		this.app=app;
		this.height=height;
		this.width=width;
		
	}


	public View Show_view()
	{
		view_layout=(LinearLayout) app.ViewManager.Inflate_View(R.layout.cover);
		
	        // note resources below are taken using getIdentifier to allow importing
	        // this library as library.
	        coverFlow1 = (CoverFlow) view_layout.findViewById(R.id.coverflow);
	        coverFlow1.setImageHeight(height);
	        coverFlow1.setImageWidth(height);
	        setupCoverFlow(coverFlow1);
    	return view_layout;
		
	}
	
	

	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
	

	 /**
     * Setup cover flow.
     * 
     * @param mCoverFlow
     *            the m cover flow
     * @param reflect
     *            the reflect
     */
    private void setupCoverFlow(final CoverFlow mCoverFlow) {
        
    	coverImageAdapter = new ResourceImageAdapter(app);
        mCoverFlow.setAdapter(coverImageAdapter);
        mCoverFlow.setSelection(0, true);
        setupListeners(mCoverFlow);
    }

    /**
     * Sets the up listeners.
     * 
     * @param mCoverFlow
     *            the new up listeners
     */
    private void setupListeners(final CoverFlow mCoverFlow) {
        mCoverFlow.setOnItemClickListener(new OnItemClickListener() {
        	
	         
	            public void onItemClick(final AdapterView< ? > parent, final View view, final int position, final long id) {
	            Image i=(Image)coverImageAdapter.getItems(position);
	            
	            Show_Image si=new Show_Image(app,i);
	            app.ViewManager.chenge_view(si.Show_view(), 5);
	            
	            
            }

        });
        
      
    }
	
	
	

	 
}
