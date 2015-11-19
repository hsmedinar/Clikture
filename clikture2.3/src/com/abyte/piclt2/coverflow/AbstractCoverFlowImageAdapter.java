package com.abyte.piclt2.coverflow;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * This class is an adapter that provides base, abstract class for images
 * adapter.
 * 
 */
public abstract class AbstractCoverFlowImageAdapter extends BaseAdapter {



    /** The width. */
    private float width = 0;

    /** The height. */
    private float height = 0;

    /** The bitmap map. */
    private final Map<Integer, WeakReference<Bitmap>> bitmapMap = new HashMap<Integer, WeakReference<Bitmap>>();

    public AbstractCoverFlowImageAdapter() {
        super();
    }

    /**
     * Set width for all pictures.
     * 
     * @param width
     *            picture height
     */
    public synchronized void setWidth(final float width) {
        this.width = width;
    }

    /**
     * Set height for all pictures.
     * 
     * @param height
     *            picture height
     */
    public synchronized void setHeight(final float height) {
        this.height = height;
    }

   
    public final Bitmap getItem(final int position) {
        final WeakReference<Bitmap> weakBitmapReference = bitmapMap.get(position);
        if (weakBitmapReference != null) {
            final Bitmap bitmap = weakBitmapReference.get();
            if (bitmap == null) {
             
            } else {
              
                return bitmap;
            }
        }
       
        final Bitmap bitmap = createBitmap(position);
        bitmapMap.put(position, new WeakReference<Bitmap>(bitmap));
        
        return bitmap;
    }

    /**
     * Creates new bitmap for the position specified.
     * 
     * @param position
     *            position
     * @return Bitmap created
     */
    protected abstract Bitmap createBitmap(int position);

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getItemId(int)
     */
    
    public final synchronized long getItemId(final int position) {
        return position;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getView(int, android.view.View,
     * android.view.ViewGroup)
     */
   
    public final synchronized ImageView getView(final int position, final View convertView, final ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            final Context context = parent.getContext();
           
            imageView = new ImageView(context);
            imageView.setLayoutParams(new CoverFlow.LayoutParams((int) width, (int) height));
        } else {
            
            imageView = (ImageView) convertView;
        }
        imageView.setImageBitmap(getItem(position));
        return imageView;
    }

}
