/*
OpenCV for Android NDK
Copyright (c) 2006-2009 SIProp Project http://www.siprop.org/

This software is provided 'as-is', without any express or implied warranty.
In no event will the authors be held liable for any damages arising from the use of this software.
Permission is granted to anyone to use this software for any purpose,
including commercial applications, and to alter it and redistribute it freely,
subject to the following restrictions:

1. The origin of this software must not be misrepresented; you must not claim that you wrote the original software. If you use this software in a product, an acknowledgment in the product documentation would be appreciated but is not required.
2. Altered source versions must be plainly marked as such, and must not be misrepresented as being the original software.
3. This notice may not be removed or altered from any source distribution.
 */
package com.abyte.piclt2.camera;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Config;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.Toast;


/**
 * It is implementing of CameraIF for G1. 
 * 
 *
 */
public class Dev1Camera implements CameraIF {
	
	private static final String TAG = "G1Camera";

	private PreviewCallback cb = null;
    private long mRawPictureCallbackTime;
	
	
    public static final int SCREEN_DELAY =100;
    
    public static final String ACTION_IMAGE_CAPTURE = "ACTION_IMAGE_CAPTURE";
    
    private SharedPreferences mPreferences;
    
    public static final int IDLE = 1;
    public static final int SNAPSHOT_IN_PROGRESS = 2;
    public static final int SNAPSHOT_COMPLETED = 3;
    
    private int mStatus = IDLE;

    private Camera mCameraDevice;
    
    private Activity mMainActivity;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder = null;

	private int mViewFinderWidth, mViewFinderHeight;
	private boolean mPreviewing = false;

	private ImageCapture mCaptureObject;
	private ImageCapture mImageCapture = null;
	
	private boolean mPausing = false;

	private boolean mIsFocusing = false;
	private boolean mIsFocused = false;
	private boolean mCaptureOnFocus = false;

	private LocationManager mLocationManager = null;

    private JpegPictureCallback mJpegPictureCallback = new JpegPictureCallback();
   // private AutoFocusCallback mAutoFocusCallback = new AutoFocusCallback();

    private boolean mKeepAndRestartPreview;

    private Handler mHandler = null; 
    
    public enum DataLocation { NONE, INTERNAL, EXTERNAL, ALL }
 

	private JpegShutterCallback shutterCallback = new JpegShutterCallback();
    private final class JpegShutterCallback implements ShutterCallback {
		public void onShutter() {
			
		}
    }


	public Dev1Camera(Activity mMainActivity, SurfaceView mSurfaceView,Handler mHandler) {
		Log.i("G1Camera", "instance");
		
		this.mMainActivity = mMainActivity;
        mPreferences = PreferenceManager.getDefaultSharedPreferences(mMainActivity);
        mLocationManager = (LocationManager) mMainActivity.getSystemService(Context.LOCATION_SERVICE);

	    this.mSurfaceView = mSurfaceView;
        
		
		this.mHandler = mHandler;
	}
	

	public Parameters getParameters() {
		return mCameraDevice.getParameters();
	}

	public void setParameters(Parameters params) {
		mCameraDevice.setParameters(params);
	}

	public void setPreviewCallback(PreviewCallback cb) {
		this.cb = cb;
		
	}

	public void onStart() {
	}

	public void onDestroy() {
		Log.i("AttachedCamera", "call stopPreview");
	}		

	public void resetPreviewSize(int width, int height) {
		// TODO Auto-generated method stub
		
	}
	
	
    public void Prepare() 
    {
        mHandler.sendEmptyMessageDelayed(4, SCREEN_DELAY);
        mPausing = false;
        ensureCameraDevice();
        mImageCapture = new ImageCapture(mMainActivity, mHandler, mCameraDevice);
    }

    public void onStop() {
        keep();
        stopPreview();
        closeCamera();
        mHandler.removeMessages(4);
    }

    public void onPause() {
        keep();

        mPausing = true;
        stopPreview();

        if (!mImageCapture.getCapturing()) {
            closeCamera();
        }

    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // if we're creating the surface, start the preview as well.
        boolean preview = holder.isCreating();
        setViewFinder(w, h, preview);
        mCaptureObject = mImageCapture;
    	startPreview();

    }

    public void surfaceCreated(SurfaceHolder holder) {
        mSurfaceHolder = holder;
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        stopPreview();
        mSurfaceHolder = null;
    }
    

	public void handleMessage(Message msg) {
        switch (msg.what) {
            case 2: {
                keep();
                
                mKeepAndRestartPreview = true;
                
                if (msg.obj != null) {
                    mHandler.post((Runnable)msg.obj);
                }
                break;
            }
        
            case 3: {
                if (mStatus == SNAPSHOT_IN_PROGRESS) {
                    // We are still in the processing of taking the picture, wait.
                    // This is is strange.  Why are we polling?
                    // TODO remove polling
                    mHandler.sendEmptyMessageDelayed(3, 100);
                } else if (mStatus == SNAPSHOT_COMPLETED){
                    mCaptureObject.dismissFreezeFrame(true);
                }
                break;
            }
        }
	}

	
	
	
//----------------------- Camera's method ------------------------
    /*
    private boolean autoFocus() {
        if (!mIsFocusing) {
            if (mCameraDevice != null) {
                mIsFocusing = true;
                mIsFocused = false;
                mCameraDevice.autoFocus(mAutoFocusCallback);
                return true;
            }
        }
        return false;
    }
    */


    
    private void closeCamera() {
        if (mCameraDevice != null) {
            mCameraDevice.release();
            mCameraDevice = null;
            mPreviewing = false;
        }
    }
    
    private boolean ensureCameraDevice() {
        if (mCameraDevice == null) {
            mCameraDevice = Camera.open();
           
            
            //tambien cambio el codigo aqui
        	try {
        		
        		Method rotateSet = Camera.class.getMethod("setDisplayOrientation", new Class[] { Integer.TYPE } );
        		Object arguments[] = new Object[] { new Integer(90) };
        		rotateSet.invoke(mCameraDevice, arguments);
        		
    		} catch (NoSuchMethodException nsme) {
    			// Older Device
    			Log.v("CAMERAVIEW","No Set Rotation");
    		} catch (IllegalArgumentException e) {
    			Log.v("CAMERAVIEW","Exception IllegalArgument");
    		} catch (IllegalAccessException e) {
    			Log.v("CAMERAVIEW","Illegal Access Exception");
    		} catch (InvocationTargetException e) {
    			Log.v("CAMERAVIEW","Invocation Target Exception");
        	}
             /**/
        }
        return mCameraDevice != null;
    }
    
    public void restartPreview() {
		Log.i("restartPreview", "in restartPreview.");

        SurfaceView surfaceView = mSurfaceView;
        if (surfaceView == null || 
                surfaceView.getWidth() == 0 || surfaceView.getHeight() == 0) {
    		Log.i("restartPreview", "surfaceView = null");
            return;
        }
        // make sure the surfaceview fills the whole screen when previewing
        ViewGroup.LayoutParams lp = surfaceView.getLayoutParams();
        lp.width = ViewGroup.LayoutParams.FILL_PARENT;
        lp.height = ViewGroup.LayoutParams.FILL_PARENT;
        surfaceView.requestLayout();
        setViewFinder(mViewFinderWidth, mViewFinderHeight, true);
        mStatus = IDLE;
    }
    
    private void setViewFinder(int w, int h, boolean startPreview) {
		Log.i("setViewFinder", "in setViewFinder.");
		Log.i("setViewFinder", "w=" + w + "  h=" + h);
        if (mPausing) {
    		Log.i("setViewFinder", "in mPausing.");
            return;
        }
        
        if (mPreviewing && 
                w == mViewFinderWidth && 
                h == mViewFinderHeight) {
    		Log.i("setViewFinder", "in no change size.");
            return;
        }
        
        if (!ensureCameraDevice()) {
    		Log.i("setViewFinder", "in ensureCameraDevice.");
            return;
        }
        
        if (mSurfaceHolder == null) {
    		Log.i("setViewFinder", "in mSurfaceHolder is null.");
            return;
        }
        
        if (mMainActivity.isFinishing()) {
    		Log.i("setViewFinder", "in fihishing.");
            return;
        }
        
        if (mPausing) {
    		Log.i("setViewFinder", "in mPausing.");
            return;
        }
        
        // remember view finder size
        mViewFinderWidth = w;
        mViewFinderHeight = h;

        if (startPreview == false) {
    		Log.i("setViewFinder", "in startPreview = false.");
            return;
        }

        /** 
         * start the preview if we're asked to...
         */

        // we want to start the preview and we're previewing already,
        // stop the preview first (this will blank the screen).
        if (mPreviewing)
            stopPreview();
        
        // this blanks the screen if the surface changed, no-op otherwise
        try {
        	mCameraDevice.setPreviewDisplay(mSurfaceHolder);
		} catch (Exception e) {
			e.printStackTrace();
            stopPreview();
            return;
		}
        

        // request the preview size, the hardware may not honor it,
        // if we depended on it we would have to query the size again        
        Parameters parameters = mCameraDevice.getParameters();
        //parameters.setPreviewSize(w, h);
        parameters.set("jpeg-quality", 100);
        parameters.set("orientation", "portrait");
        
		try {
			List<Camera.Size> supportedSizes = null;
			supportedSizes = Compatibility.getSupportedPreviewSizes(parameters);

			Iterator<Camera.Size> itr = supportedSizes.iterator(); 
			while(itr.hasNext()) {
				Camera.Size element = itr.next(); 
				element.width -= w;
				element.height -= h;
			} 
			Collections.sort(supportedSizes, new ResolutionsOrder());
			parameters.setPreviewSize(w + supportedSizes.get(supportedSizes.size()-1).width, h + supportedSizes.get(supportedSizes.size()-1).height);
		} catch (Exception ex) {
			parameters.setPreviewSize(320,240);
		}
		
		Log.e("ANTES DEL ERROR", "mCameraDevice.setParameters(parameters);");
        //mCameraDevice.setParameters(parameters);
        Log.e("DESPUES DEL ERROR", "mCameraDevice.setParameters(parameters);");
        


		Log.i("setViewFinder", "start mCameraDevice.startPreview().");
        mCameraDevice.startPreview();
        mPreviewing = true;    	
        this.startPreview();
        

    }
    
    class ResolutionsOrder implements java.util.Comparator<Camera.Size> {
    	public int compare(Camera.Size left, Camera.Size right) {

    		return Float.compare(left.width + left.height, right.width + right.height);
    	}
    }

    
    public void takePicture() {

    	if (!mPreviewing) {
			Log.i("CaptureThread", "in onSnap");
			mCaptureObject.snap(mImageCapture);

		} else {
			Log.i("CaptureThread", "in onSnap");
			mCaptureObject.snap(mImageCapture);

		}
    }
    

    
    
    private void startPreview() {
    	Log.i("startPreview", "in startPreview.");
    	Log.i("startPreview", "out startPreview.");
    }
    private void stopPreview() {
    	Log.i("stopPreview", "in stopPreview.");
        if (mCameraDevice != null && mPreviewing) {
            mCameraDevice.stopPreview();
        }
        mPreviewing = false;
    	Log.i("startPreview", "out stopPreview.");
    }

    void keep() {
        if (mCaptureObject != null) {
            mCaptureObject.dismissFreezeFrame(true);
        }
    };

   
// ---------------------------- Callback classes ---------------------------------	
	
    private final class JpegPictureCallback implements PictureCallback {
        public void onPictureTaken(byte [] jpegData, Camera camera) {
            Log.i("JpegPictureCallback", "in JpegPictureCallback...");
            
            mStatus = SNAPSHOT_COMPLETED;
                      
            
        	if(jpegData != null) {
				Log.i("JpegPictureCallback", "data= OK");
				
				cb.onPreviewFrame(jpegData, null);
				
        	} else {
				try {
					// The measure against over load. 
					Thread.sleep(500);
				} catch (InterruptedException e) {
					;
				}
        	}
        }
    };
    /*
    private final class AutoFocusCallback implements android.hardware.Camera.AutoFocusCallback {
        public void onAutoFocus(boolean focused, android.hardware.Camera camera) {
            mIsFocusing = false;
            mIsFocused = focused;
            Log.i("AutoFocusCallback", "focused:" + mIsFocused);
            Log.i("AutoFocusCallback", "mCaptureOnFocus:" + mCaptureOnFocus);
            if (focused) {
                if (mCaptureOnFocus && mCaptureObject != null) {
                    // No need to play the AF sound if we're about to play the shutter sound
                    if(mCaptureObject.snap(mImageCapture)) {
                        clearFocus();
                        mCaptureOnFocus = false;
                        return;
                    }
                    clearFocus();
                }
                mCaptureOnFocus = false;
            }
        }
    };

    */
        
// ---------------------- ImageCapture Class ---------------------------	
	
	public class ImageCapture {
		
		private Activity arActivity;	
		private Camera mCameraDevice;
		private Handler mHandler;
		
		
	    public ImageCapture(Activity arActivity, Handler mHandler, Camera mCameraDevice) {
	    	this.arActivity = arActivity;
	    	this.mHandler = mHandler;
	    	this.mCameraDevice = mCameraDevice;
	    }
	    
	    private boolean mCapturing = false;
	    public boolean getCapturing() {
	    	return mCapturing;
	    }

	    /** 
	     * This method sets whether or not we are capturing a picture. This method must be called
	     * with the ImageCapture.this lock held.
	     */
	    public void setCapturingLocked(boolean capturing) {
	        mCapturing = capturing;
	    }
	    
	    public void dismissFreezeFrame(boolean keep) {
	        if (!keep) {
	            Toast.makeText(arActivity, "R.string.camera_tossing", Toast.LENGTH_SHORT).show();
	        }
	        
	        if (mStatus == SNAPSHOT_IN_PROGRESS) {
	            // If we are still in the process of taking a picture, then just post a message.
	            mHandler.sendEmptyMessage(3);
	        } else {
	        	restartPreview();
	        }
	    }

	    /**
	     * Initiate the capture of an image.
	     */
	    public boolean initiate() {
			Log.i("ImageCap", "in initiate.");
			Log.i("ImageCap", "mCameraDevice ID:" + mCameraDevice);
	        if (mCameraDevice == null) {
	            return false;
	        }
	        
	        mCapturing = true;

	        return capture();
	    }
	  
	    private boolean capture() {
			Log.i("ImageCap", "in capture.");
	    	mPreviewing = false;

	    	
	        Boolean recordLocation = mPreferences.getBoolean("pref_camera_recordlocation_key", false);
	      
	        Camera.Parameters parameters = mCameraDevice.getParameters();
	        
	        /*
	        if (app. != null) {
	            parameters.set("gps-latitude",  String.valueOf(loc.getLatitude()));
	            parameters.set("gps-longitude", String.valueOf(loc.getLongitude()));
	            parameters.set("gps-altitude",  String.valueOf(loc.getAltitude()));
	            parameters.set("gps-timestamp", String.valueOf(loc.getTime()));
	        } else {
	            parameters.remove("gps-latitude");
	            parameters.remove("gps-longitude");
	            parameters.remove("gps-altitude");
	            parameters.remove("gps-timestamp");
	        }
	         */
	        
	        
	       
	        //picture size
	        //parameters.setPictureSize(320,240);
	        
	        Size pictureSize = parameters.getPictureSize();
	        Size previewSize = parameters.getPreviewSize();

	        // resize the SurfaceView to the aspect-ratio of the still image
	        // and so that we can see the full image that was taken
	        ViewGroup.LayoutParams lp = mSurfaceView.getLayoutParams();
	     
	        if (pictureSize.width*previewSize.height < previewSize.width*pictureSize.height) {
	            lp.width = (pictureSize.width * previewSize.height) / pictureSize.height; 
	        } else {
	            lp.height = (pictureSize.height * previewSize.width) / pictureSize.width; 
	        }
	         
	        
	        mSurfaceView.requestLayout();
	       
	        Log.e("ImageCap", "exec takePicture.");
	        mCameraDevice.setParameters(parameters);
	      
	        mCameraDevice.takePicture(shutterCallback, null, mJpegPictureCallback);

	        Log.e("passoooo", "passooo");
	        return true;
	    }

	    public boolean snap(ImageCapture realCapure) {
			Log.i("ImageCap", "in onSnap");
	        // If we are already in the middle of taking a snapshot then we should just save
	        // the image after we have returned from the camera service.
	        if (mStatus == SNAPSHOT_IN_PROGRESS 
	        		|| mStatus == SNAPSHOT_COMPLETED) {
	    		Log.i("ImageCap", "in SNAPSHOT_IN_PROGRESS.");
	            mKeepAndRestartPreview = true;
	            mHandler.sendEmptyMessage(3);
	            return false;
	        }

	        mStatus = SNAPSHOT_IN_PROGRESS;

	        mKeepAndRestartPreview = !mPreferences.getBoolean("pref_camera_postpicturemenu_key", true);

	        return realCapure.initiate();
	    }
	}


	
	public void onResume() {
		// TODO Auto-generated method stub
		
	}

}
