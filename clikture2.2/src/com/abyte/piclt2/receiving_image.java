package com.abyte.piclt2;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

public class receiving_image extends Activity {
	 //YOU CAN EDIT THIS TO WHATEVER YOU WANT
    private static final int SELECT_PICTURE = 1;

    private String selectedImagePath;
    //ADDED
    
    private FileOutputStream fileOutputStream = null;

    ImageView img;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.galleryimage);

     
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String action = intent.getAction();
        
        final piclt2_App app=(piclt2_App) this.getApplication();
        
   
        if (Intent.ACTION_SEND.equals(action))
        {
            if (extras.containsKey(Intent.EXTRA_STREAM))
            {
                //try
                //{
            		
                    Uri uri = (Uri) extras.getParcelable(Intent.EXTRA_STREAM);
                    selectedImagePath = getPath(uri);
                         
                    String name = upload_to_path_Image(selectedImagePath);
                    //Toast.makeText(this, "descargo", Toast.LENGTH_LONG).show();
                    //Toast.makeText(this, name, Toast.LENGTH_LONG).show();                    
                    Bundle bundle = new Bundle();
                    bundle.putString("external", name);
                    Intent i = new Intent(this,StartActivity.class);
                    i.putExtras(bundle);
                    startActivity(i);
                    
  
                    return;
                //} catch (Exception e)
                //{
                 //   Log.e(this.getClass().getName(), e.toString());
                //}

            } else if (extras.containsKey(Intent.EXTRA_TEXT))
            {
                return;
            }
        }
        
   
    }
    
    private String upload_to_path_Image(String url){
		try{			
			  String appPath = Environment.getExternalStorageDirectory().getAbsolutePath();
			  String name = System.currentTimeMillis()+".jpg";
			  String path = appPath+"/image/";
		      Bitmap Image = BitmapFactory.decodeFile(url);
		      
		      //Bitmap myImage = getResizedBitmap(Image,Image.getHeight()-200,Image.getWidth()-200);		     
		      Bitmap myImage = getResizedBitmap(Image,Image.getHeight()/3,Image.getWidth()/3);
			  fileOutputStream = new FileOutputStream(path + name);
			  BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);
			  myImage.compress(CompressFormat.JPEG, 50, bos);
			  bos.flush();
			  bos.close();		
			  System.gc();
			  return name;
		}catch(Exception e){
			  Log.e("ImageError",e.getMessage());	
		}
		
		return "";
	}
    
    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
    	int width = bm.getWidth();
    	int height = bm.getHeight();
    	float scaleWidth = ((float) newWidth) / width;
    	float scaleHeight = ((float) newHeight) / height;
    	// CREATE A MATRIX FOR THE MANIPULATION
    	Matrix matrix = new Matrix();
    	// RESIZE THE BIT MAP
    	matrix.postScale(scaleWidth, scaleHeight);
    	// RECREATE THE NEW BITMAP
    	Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
    	return resizedBitmap;
    }


    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if(cursor!=null)
        {
            //HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            //THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        else return null;
    }

}
