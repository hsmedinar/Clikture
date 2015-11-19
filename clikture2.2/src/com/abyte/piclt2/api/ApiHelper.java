package com.abyte.piclt2.api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

import com.abyte.piclt2.model.Promotion;



public class ApiHelper {


	public static String ubication = "http://maps.google.com/maps/api/geocode/xml?latlng=-6.762,-79.86&sensor=true";
	
	public static String upload = "http://cloud.pongr.com:8080/index/geosync";
	
	public static String sendurl = "http://clikture.4abyte.com/view_campaign/";
	
	public static String brandimg = "http://clikture.4abyte.com/view_brand_global/";
	
	public static String validateuser = "http://clikture.4abyte.com/userxml/";
	
	public static boolean proxy=true;
	
	public static int time_out=900000;


	public static String Upload_file(String file_path,String mimetype,String key) 
			{

		HashMap<String, String> resultMap = new HashMap<String, String>();

		try {

			try {

				

				HttpClient httpclient;
				

				HttpParams my_httpParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(my_httpParams, time_out);
				
				ConnManagerParams.setMaxTotalConnections(my_httpParams, 10);
				ConnManagerParams.setMaxConnectionsPerRoute(my_httpParams, new ConnPerRouteBean(10));
				
		    	final String proxyHost = android.net.Proxy.getDefaultHost();
		    	final int proxyPort = android.net.Proxy.getDefaultPort();
		    	if(proxyPort != -1 && proxy) { 
		    		my_httpParams.setParameter(ConnRoutePNames.DEFAULT_PROXY, new HttpHost(proxyHost, proxyPort));
		    		
		    	}
		    	
		    	
		    	
		    	
		    	SchemeRegistry registry = new SchemeRegistry();
			    registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			    ClientConnectionManager cm = new ThreadSafeClientConnManager(my_httpParams, registry);
			    
			    
			    
		    	httpclient = new DefaultHttpClient(cm,my_httpParams);

		    	//httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		    	
				HttpPost httppost = new HttpPost(upload);
				
				
				httppost.setHeader("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
				
				
				ContentBody string_body=new ByteBody(key.getBytes());
				ContentBody file_body= new MyFileBody(new File(file_path),"image/jpeg");
				
				
				MultipartEntity entity = new MultipartEntity(HttpMultipartMode.STRICT);
				entity.addPart("key",string_body);
				entity.addPart("image",file_body);

				
				
				httppost.setEntity(entity);
			
				
				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity resEntity = response.getEntity();
				int status = response.getStatusLine().getStatusCode();

				
				String result = "";
				try {
					result = EntityUtils.toString(resEntity, HTTP.UTF_8);
					result=result.replaceAll( "\n","");
					Log.v("Upload file",result);
				} catch (FileNotFoundException e) {
					return e.getMessage();
				}
				
				resEntity.consumeContent();
				return result;

			} catch (ClientProtocolException e) {
				return "-2"; //e.getMessage();
			} catch (IOException e) {
				return "-2"; //e.getMessage();
			}

		} catch (Exception e) {
			return "-2"; //e.getMessage();
		}
		

	}
	
	
	
	public static HashMap Get_Promotion(String node) //HashMap 
	{
		
		try {

			try {

				
				
				// ------------------------------------

				// ----------send post server----------
				
				HttpClient httpclient;
				
		
		    	httpclient = new DefaultHttpClient();
		    	
		    	
				HttpGet httget = new HttpGet(sendurl + node);

				HttpResponse response = httpclient.execute(httget);

				HttpEntity resEntity = response.getEntity();

				String result = "";
				try {
					result = EntityUtils.toString(resEntity, HTTP.UTF_8);
					result=HTMLEntities.unhtmlentities(result);
					result=result.replaceAll( "&","&amp;");
					result=result.replaceAll( "\n","");
					
					
				//	Log.v("ApiHelper","respuesta: "+ result);

		
					HashMap resultMap = ApiHelper.parse_layout(result);
				
					return resultMap;
					//return result.toString();
					
				} catch (FileNotFoundException e) {
					return null;
				}

				//resEntity.consumeConten();
				
			} catch (ClientProtocolException e) {
				return null;
			} catch (IOException e) {
				return null;
			}

		} catch (Exception e) {
			return null;
		}

	}
	
	public static HashMap parse_layout(String xml) {

		//ArrayList<Layout_image> result = new ArrayList<Layout_image>();

		HashMap paramUser = new HashMap();
		//Object[] param = {this.sessionID,paramUser};
		
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			xpp.setInput(new StringReader(xml));
			int eventType = xpp.getEventType();
			String elementName= "";
			
			
			//boolean status=false;
			
			String Icon = "";
			String Field_Browser = "";
			String Group_Collection = "";
	
			
			int count_data=0;
			
	

			while (eventType != XmlPullParser.END_DOCUMENT) {

				if (eventType == XmlPullParser.START_DOCUMENT) {

				} else if (eventType == XmlPullParser.START_TAG) {
					elementName = xpp.getName();	
					
				} else if (eventType == XmlPullParser.TEXT) {
			
						if(elementName.equals("LayoutIcon") && Icon.equals(""))
						{
							Icon= xpp.getText();
							paramUser.put("icon", Icon);
							count_data++;
						}				
						if(elementName.equals("LayoutAction") && Field_Browser.equals(""))
						{
							Field_Browser= xpp.getText();
							paramUser.put("link", Field_Browser);
							count_data++;
						}
						if(elementName.equals("IdGroup") && Group_Collection.equals(""))
						{
							Group_Collection= xpp.getText();
							paramUser.put("group", Group_Collection);
							count_data++;
						}
						
				} 
				else if (eventType == XmlPullParser.END_TAG )
				{
					elementName = xpp.getName();	
					if( elementName.equals("node") && count_data>=3) {
					
						//result.add(new Layout_image(Title,Icon,"",Field_Browser));
						Icon = "";
						Field_Browser = "";
						Group_Collection= "";
						count_data=0;
					}

				}
				
				eventType = xpp.next();
			}
			
		return paramUser;
			
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}

	}
	
	
	/************************ VALIDATE USER *****************************/
	
	
	public static String Get_Validtae_User(String user) 
	{
		
		try {

			try {
			
				HttpClient httpclient;
				
		
		    	httpclient = new DefaultHttpClient();
		    	
		    	
				HttpGet httget = new HttpGet(validateuser + user);

				HttpResponse response = httpclient.execute(httget);

				HttpEntity resEntity = response.getEntity();

				String result = "";
				try {
					result = EntityUtils.toString(resEntity, HTTP.UTF_8);
					result=HTMLEntities.unhtmlentities(result);
					result=result.replaceAll( "&","&amp;");
					result=result.replaceAll( "\n","");


		
					String resultMap = ApiHelper.parse_layout_user(result);
								
					return resultMap.toString();
					
				} catch (FileNotFoundException e) {
					return null;
				}

			} catch (ClientProtocolException e) {
				return null;
			} catch (IOException e) {
				return null;
			}

		} catch (Exception e) {
			return null;
		}

	}
	
	
	public static String parse_layout_user(String xml) {

		String valor="";
		
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			xpp.setInput(new StringReader(xml));
			int eventType = xpp.getEventType();
			String elementName= "";
			

			while (eventType != XmlPullParser.END_DOCUMENT) {

				if (eventType == XmlPullParser.START_DOCUMENT) {

				} else if (eventType == XmlPullParser.START_TAG) {
					elementName = xpp.getName();	
					
					
				} else if (eventType == XmlPullParser.TEXT) {
			
						if(elementName.equals("Uid"))
						{
							valor= "1";
						}				
			
				} 
		
				eventType = xpp.next();
			}
			
		return valor;
			
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
	

	
	/**************************new code ****************/

	public static ArrayList<Promotion> Get_Promotions(String valor) 
	{

		ArrayList<Promotion> resultMap = new ArrayList<Promotion>();

		try {

			try {


		HttpClient httpclient;
		
		//Usando Proxi
		HttpParams my_httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(my_httpParams, time_out);
		
		ConnManagerParams.setMaxTotalConnections(my_httpParams, 10);
		ConnManagerParams.setMaxConnectionsPerRoute(my_httpParams, new ConnPerRouteBean(10));
		
    	final String proxyHost = android.net.Proxy.getDefaultHost();
    	final int proxyPort = android.net.Proxy.getDefaultPort();
    	if(proxyPort != -1 && proxy) { 
    		my_httpParams.setParameter(ConnRoutePNames.DEFAULT_PROXY, new HttpHost(proxyHost, proxyPort));
    		
    	}
    	
    	
    	SchemeRegistry registry = new SchemeRegistry();
	    registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
	    ClientConnectionManager cm = new ThreadSafeClientConnManager(my_httpParams, registry);
	    
    	httpclient = new DefaultHttpClient(cm,my_httpParams);
    	
    	
    	
		HttpGet httget = new HttpGet(sendurl + valor);


		// Execute HTTP Post Request
		HttpResponse response = httpclient.execute(httget);

		HttpEntity resEntity = response.getEntity();

		String result = "";
		
			try {
				result = EntityUtils.toString(resEntity, HTTP.UTF_8);
				result=HTMLEntities.unhtmlentities(result);
				result=result.replaceAll( "&","&amp;");
				result=result.replaceAll( "\n","");
			
			
				Log.v("ApiHelper","respuesta: "+ result);



				resultMap = ApiHelper.parse_promotion(result);
			

			} catch (FileNotFoundException e) {
				return null;
			}

			resEntity.consumeContent();

        } catch (ClientProtocolException e) {
		    return null;
	    } catch (IOException e) {
		    return null;
	    }

	  } catch (Exception e) {
	  	return null;
	  }
	
	return resultMap;

  }

	public static ArrayList<Promotion> parse_promotion(String xml) {

		ArrayList<Promotion> result = new ArrayList<Promotion>();
		String Group = "";
		String Name = "";
		String Icon = "";
		String Url = "";
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			xpp.setInput(new StringReader(xml));
			int eventType = xpp.getEventType();
			String elementName= "";
	
	
			boolean status=false;
	
		
			int count_data=0;
	


			while (eventType != XmlPullParser.END_DOCUMENT) {

				if (eventType == XmlPullParser.START_DOCUMENT) {

				} else if (eventType == XmlPullParser.START_TAG) {
					elementName = xpp.getName();	
			
				} else if (eventType == XmlPullParser.TEXT) {

					if(elementName.equals("Title") && Group.equals(""))
					{
						Group= xpp.getText();
						count_data++;
					}
					if(elementName.equals("IconImage") && Icon.equals(""))
					{
						Icon= xpp.getText();
						count_data++;
					}
					if(elementName.equals("IconUrl") && Url.equals(""))
					{
						Url= xpp.getText();	
						count_data++;
					}
					if(elementName.equals("IconName") && Name.equals(""))
					{
						Name= xpp.getText();
						count_data++;
					}	
				} 
				else if (eventType == XmlPullParser.END_TAG )
				{
					elementName = xpp.getName();	
					if( elementName.equals("node") && count_data>=3) {
			
					 result.add(new Promotion(Group,Name,Icon,Url));
					 Group = "";
					 Name = "";
					 Icon = "";
					 Url = "";						
					 count_data=0;
					}

				}
		
				eventType = xpp.next();
				
			}

			
		} catch (Exception e) {
			// TODO: handle exception
		}

		return result;

	}
	
	/************************ get image brand *********************************/
	
	
	public static String Get_Image_Brand(String state)
	{
		
		try {

			try {
			
				HttpClient httpclient;
				
		
		    	httpclient = new DefaultHttpClient();
		    	
		    	
				HttpGet httget = new HttpGet(brandimg + state);
				Log.i("ApiBrand", brandimg + state);
				HttpResponse response = httpclient.execute(httget);

				HttpEntity resEntity = response.getEntity();

				String result = "";
				try {
					result = EntityUtils.toString(resEntity, HTTP.UTF_8);
					result=HTMLEntities.unhtmlentities(result);
					result=result.replaceAll( "&","&amp;");
					result=result.replaceAll( "\n","");


					Log.i("result", result);	
					String pathimage = ApiHelper.parse_layout_brand(result);
					Log.i("ApiBrand", pathimage);			
					return pathimage;
					
				//	return result.toString();
					
				} catch (FileNotFoundException e) {
					return null;
				}

			} catch (ClientProtocolException e) {
				return null;
			} catch (IOException e) {
				return null;
			}

		} catch (Exception e) {
			return null;
		}

	}
	
	
	public static String parse_layout_brand(String xml) {

		String valor="";
		
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			xpp.setInput(new StringReader(xml));
			int eventType = xpp.getEventType();
			String elementName= "";
			

			while (eventType != XmlPullParser.END_DOCUMENT) {

				if (eventType == XmlPullParser.START_DOCUMENT) {

				} else if (eventType == XmlPullParser.START_TAG) {
					elementName = xpp.getName();	
					
				} else if (eventType == XmlPullParser.TEXT) {
			
						if(elementName.equals("Brand" ) && valor.equals("")) 
						{
							valor= xpp.getText();
							Log.i("ApiBrandText", xpp.getText());		
						}							
				} 
		
				eventType = xpp.next();
			}
			
		return valor;
			
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
	
 /********************************decode geography **************************************/
	
	//public static ArrayList<String> Get_Geo_Ubication()
	public static String Get_Geo_Ubication(String lat , String longi) 
	{
		
		try {

			try {
			
				HttpClient httpclient;
				
		
		    	httpclient = new DefaultHttpClient();
		    	
		    	
				//HttpGet httget = new HttpGet("http://maps.google.com/maps/api/geocode/xml?latlng=" + lat + "," + longi + "&sensor=true");
				HttpGet httget = new HttpGet("http://maps.google.com/maps/api/geocode/json?latlng=" + lat + "," + longi + "&sensor=true");
				Log.i("API", "http://maps.google.com/maps/api/geocode/json?latlng=" + lat + "," + longi + "&sensor=true");
				HttpResponse response = httpclient.execute(httget);

				HttpEntity resEntity = response.getEntity();

				String result = "";
				try {
					result = EntityUtils.toString(resEntity, HTTP.UTF_8);
					result=HTMLEntities.unhtmlentities(result);
					result=result.replaceAll( "&","&amp;");
					result=result.replaceAll( "\n","");


		
					//ArrayList<String> statename = ApiHelper.parse_layout_brand_global(result);
					//String statename = ApiHelper.parse_layout_brand_global(result);
					String statename = parse_json_brand_global(result);
							
					return statename;
					
					//return result.toString();
					
				} catch (FileNotFoundException e) {
					return null;
				}

			} catch (ClientProtocolException e) {
				return null;
			} catch (IOException e) {
				return null;
			}

		} catch (Exception e) {
			return null;
		}

	}
	
	public static String parse_json_brand_global(String json){
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			//String jsonContentType = jsonObject.getString("types");
			
			    String strData = jsonObject.getString("results");
			    JSONArray jsonContent = new JSONArray(strData);			    
			    int numItems = jsonContent.length();
			    int cant=0;
			    String data="";
			    String dataa="";
			    //return Integer.toString(numItems);
			    
			    Log.i("PARSER", "level1");
			    
			    for (int i = 0; i < numItems; i++) {
			      JSONObject item = jsonContent.getJSONObject(i);			  
			       if (item.getString("types").equals("[\"street_address\"]") || item.getString("types").equals("[\"route\"]")){	
			    	   Log.i("PARSER", "level2");
			    	   String str_address = item.getString("address_components");
			    	   JSONArray jsonContentAddress = new JSONArray(str_address);
			    	   
			    	   for(int x=0;x<item.getJSONArray("address_components").length();x++){
			    		   JSONObject item2 = jsonContentAddress.getJSONObject(x);
			    		   Log.i("PARSER", "level3");
			    		   if (item2.getString("types").equals("[\"administrative_area_level_1\",\"political\"]")){	
			    		      data= item2.getString("long_name");	
			    		      Log.i("PARSER", data);
			    		   } 
			    	   } 
			       }			       
			    }
			    
			    //return Integer.toString(cant);
			    return data;
			    
		} catch (JSONException e) {
			return "";
		}
		
	}
	
  // public static ArrayList<String> parse_layout_brand_global(String xml) {
   public static String parse_layout_brand_global(String xml) {

		String valor="";
		
		try {
			
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			
			factory.setNamespaceAware(true);
			
			XmlPullParser xpp = factory.newPullParser();
			xpp.setInput(new StringReader(xml));
			int eventType = xpp.getEventType();
			
	
			//String elementName= "";
			//int x=0;
			//ArrayList<String> etiqueta = new ArrayList<String>();

			while (eventType != XmlPullParser.END_DOCUMENT) {
				
				switch(eventType){
					case XmlPullParser.START_DOCUMENT:
										
					case XmlPullParser.START_TAG:	
						String eti = xpp.getName();
				
				}			
			}
			
		//return Integer.toString(x);
		return valor;
		 //return etiqueta; 
			
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	
}
