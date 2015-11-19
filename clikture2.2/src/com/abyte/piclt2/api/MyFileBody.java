package com.abyte.piclt2.api;

import java.io.File;
import java.util.Map;

import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;

public class MyFileBody  extends FileBody implements ContentBody{

	public String mimetype;
	public MyFileBody(File file,String mimetype) {
		super(file,mimetype);
		this.mimetype=mimetype;
	}
	

	@Override
	public String getMimeType() {
		// TODO Auto-generated method stub
		return this.mimetype;
	}



	@Override
	public String getMediaType() {
		// TODO Auto-generated method stub
		return "image/jpeg";
	}


	@Override
	public String getTransferEncoding() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
