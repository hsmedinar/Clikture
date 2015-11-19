package com.abyte.piclt2.coverflow;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.abyte.piclt2.piclt2_App;
import com.abyte.piclt2.model.Image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


/**
 * This class is an adapter that provides images from a fixed set of resource
 * ids. Bitmaps and ImageViews are kept as weak references so that they can be
 * cleared by garbage collection when not needed.
 * 
 */
public class ResourceImageAdapter extends AbstractCoverFlowImageAdapter {



    /** The Constant DEFAULT_LIST_SIZE. */
    private static final int DEFAULT_LIST_SIZE = 20;

    /** The Constant IMAGE_RESOURCE_IDS. */
    private static final List<Integer> IMAGE_RESOURCE_IDS = new ArrayList<Integer>(DEFAULT_LIST_SIZE);

  

    /** The bitmap map. */
    private final Map<Integer, WeakReference<Bitmap>> bitmapMap = new HashMap<Integer, WeakReference<Bitmap>>();

    public piclt2_App app;
    
    public ArrayList<Image> images;
    
    
    
    

    /**
     * Creates the adapter with default set of resource images.
     * 
     * @param context
     *            context
     */
    
    public ResourceImageAdapter(final piclt2_App app) {
        super();
        this.app = app;
        images=app.Data.getImage_array();
    }

    /**
     * Replaces resources with those specified.
     * 
     * @param resourceIds
     *            array of ids of resources.
     */
    public final synchronized void setResources(final int[] resourceIds) {
        IMAGE_RESOURCE_IDS.clear();
        for (final int resourceId : resourceIds) {
            IMAGE_RESOURCE_IDS.add(resourceId);
        }
        notifyDataSetChanged();
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getCount()
     */
    
    public synchronized int getCount() {
        return images.size();
    }
    
    public Image getItems(int pos)
    {
    	return (Image)images.get(pos);
    }

    /*
     * (non-Javadoc)
     * 
     * @see pl.polidea.coverflow.AbstractCoverFlowImageAdapter#createBitmap(int)
     */
    @Override
    protected Bitmap createBitmap(final int position) {
        
    	String path = app.image_path+images.get(position).getPath();;
		
		
		if(path!=null && path.length()>0)
		{
			
			
			File image_dir = new File(path);
			if(image_dir.exists())
			{
				final Bitmap bitmap = BitmapFactory.decodeFile(path);
		        bitmapMap.put(position, new WeakReference<Bitmap>(bitmap));
		        return bitmap;
			}

		}
		return null;
       
    }
}