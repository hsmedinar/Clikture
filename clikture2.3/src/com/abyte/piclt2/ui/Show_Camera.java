package com.abyte.piclt2.ui;



import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.abyte.piclt2.R;
import com.abyte.piclt2.piclt2_App;
import com.abyte.piclt2.camera.Compatibility;
import com.abyte.piclt2.model.Image;

/**
 * this class show and manager camera scream
 */
public class Show_Camera implements OnClickListener, SurfaceHolder.Callback{
	

	
	public piclt2_App app;	
	public LinearLayout view_layout;

	private Camera camera;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private static final String TAG = "CameraApiTest";
    boolean mPreviewRunning = false;
    boolean newinsert=false;
    private Button BotonCamara;
    private byte[] mImageByteArray;
    private int rotate;
	
	public Show_Camera(piclt2_App app) {
		super();
		this.app=app;		
	}


	public View Show_view()
	{
		
		view_layout=(LinearLayout) app.ViewManager.Inflate_View(R.layout.camera);	
		
		BotonCamara = (Button)view_layout.findViewById(R.id.take);
		BotonCamara.setOnClickListener(HacerFoto);
		
		app.mContext.getWindow().setFormat(PixelFormat.TRANSLUCENT);
	        
		mSurfaceView = (SurfaceView)view_layout.findViewById(R.id.surface_camera);
	    mSurfaceHolder = mSurfaceView.getHolder();
	    mSurfaceHolder.addCallback(this);
	    mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
      
        
    	return view_layout;
		
	}


	 private OnClickListener HacerFoto = new OnClickListener()
	    {
	        public void onClick(View v){
	            camera.takePicture(shutterCallback, rawCallback, jpegCallback);
	        }
	    };
	    
	

	    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
	        public void onPictureTaken(byte[] data, Camera c) {
	            Log.i(TAG, "PICTURE CALLBACK: data.length = " + data.length);
	            camera.startPreview();
	        }
	    };
/*
	    public boolean onKeyDown(int keyCode, KeyEvent event)
	    {
	        if (keyCode == KeyEvent.KEYCODE_BACK) {
	            //return super.onKeyDown(keyCode, event);
	        }
	 
	        if (keyCode == KeyEvent.KEYCODE_SPACE) {
	            camera.takePicture(shutterCallback, rawCallback, jpegCallback);
	            return true;
	        }

	        return false;
	    }
*/	
	    public void surfaceCreated(SurfaceHolder holder)
	    {
	        Log.i(TAG, "surfaceCreated");
	        camera = Camera.open();
	        //mCamera.startPreview();
	    }

	    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h)
	    {
	           
	    	   Log.e("INFO", "set info");
	        
	    	   	
	            android.hardware.Camera.CameraInfo info =
	                    new android.hardware.Camera.CameraInfo();
	            int val = android.hardware.Camera.getNumberOfCameras();
	            for(int x=0; x<=val -1;x++){
	            	android.hardware.Camera.getCameraInfo(x, info);	
	            }
	            
	            int rotation = app.mContext.getWindowManager().getDefaultDisplay()
	                   .getRotation();
	            int degrees = 0;
	           
	            switch (rotation) {
	                case Surface.ROTATION_0: degrees = 0; break;
	                case Surface.ROTATION_90: degrees = 90; break;
	                case Surface.ROTATION_180: degrees = 180; break;
	                case Surface.ROTATION_270: degrees = 270; break;
	                
	            }
 	
	            int result;
	            
	            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
	                  result = (info.orientation + degrees) % 360;
	                  result = (360 - result) % 360;  // compensate the mirror
	            } else {  // back-facing
	                result = (info.orientation - degrees + 360) % 360;
	            }
	          //  Toast.makeText(activity, Integer.toString(result), Toast.LENGTH_LONG).show();
	            camera.setDisplayOrientation(result);
	            rotate= result;
	        
	        // XXX stopPreview() will crash if preview is not running
	        if (mPreviewRunning) {
	            camera.stopPreview();
	        }
	        
	        Camera.Parameters p = camera.getParameters();

	        p.setPictureSize(320, 240);
	        p.set("jpeg-quality", 100);
	        p.set("orientation", "portrait");
	        
	        try {
	        	
	        	List<Camera.Size> supportedSizes = null;
				supportedSizes = Compatibility.getSupportedPreviewSizes(p);

				Iterator<Camera.Size> itr = supportedSizes.iterator(); 
				while(itr.hasNext()) {
					Camera.Size element = itr.next(); 
					element.width -= w;
					element.height -= h;
				} 
				Collections.sort(supportedSizes, new ResolutionsOrder());
				p.setPreviewSize(w + supportedSizes.get(supportedSizes.size()-1).width, h + supportedSizes.get(supportedSizes.size()-1).height);			

				
	            camera.setPreviewDisplay(holder);
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	        	p.setPreviewSize(320,240);
	            Log.e(TAG, e.getMessage());
	        }
	        
	        //camera.setParameters(p);
	        camera.startPreview();
	        mPreviewRunning = true;
	    }

	    public void surfaceDestroyed(SurfaceHolder holder)
	    {
	        Log.i(TAG, "surfaceDestroyed");
	        camera.stopPreview();
	        mPreviewRunning = false;
	        camera.release();
	    }
	    
	    ShutterCallback shutterCallback = new ShutterCallback() {
	          public void onShutter() {
	            // TODO Do something when the shutter closes.
	          }
	        };
	         
	        PictureCallback rawCallback = new PictureCallback() {
	          public void onPictureTaken(byte[] _data, Camera _camera) {
	            // TODO Do something with the image RAW data.
	          }
	        };
	         
	        PictureCallback jpegCallback = new PictureCallback() {
	          public void onPictureTaken(byte[] data, Camera _camera) {
	                FileOutputStream outStream = null;
	                Intent i;
	                try {
	     
	                	mImageByteArray = data;
	                	Bitmap photo1=BitmapFactory.decodeByteArray(mImageByteArray, 0, mImageByteArray.length);
	                	
	                	float scaleWidth = ((float) 640) / photo1.getWidth();
	                    float scaleHeight = ((float) 480) / photo1.getHeight();
	                     
	                    Matrix mtx = new Matrix();
	                    mtx.postScale(scaleWidth, scaleHeight);
	                    mtx.postRotate(rotate);
	                    Bitmap photo = Bitmap.createBitmap(photo1, 0, 0, photo1.getWidth(),photo1.getHeight(), mtx, true);

	                    String image_path=System.currentTimeMillis()+".jpg";
	        			FileOutputStream fileOutputStream = new FileOutputStream(app.image_path+image_path);
	        			BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);
	        			photo.compress(CompressFormat.JPEG, 10, bos);
	        			bos.flush();
	        			bos.close();
	        			System.gc();
	        			
	        			Image im=new Image(image_path,System.currentTimeMillis()+"",app.Preference.getLatitude(),app.Preference.getLongitude());	        			
	        	        app.Data.Insert_Image(im);	        			
	        			Show_Temp si=new Show_Temp(app,im);
	        			app.ViewManager.Show_Screen(6);
	                } catch (FileNotFoundException e) {
	                    e.printStackTrace();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                } finally {
	                }
	                Log.d(TAG, "onPictureTaken - jpeg");
	          }
	        };
	        
	        /*****************************************/
	        
	  
	        
	        class ResolutionsOrder implements java.util.Comparator<Camera.Size> {
	        	public int compare(Camera.Size left, Camera.Size right) {

	        		return Float.compare(left.width + left.height, right.width + right.height);
	        	}
	        }
	    
	    
	public void onClick(View v) {
		// TODO Auto-generated method stub
		

			
	}
	
	


	 
}
