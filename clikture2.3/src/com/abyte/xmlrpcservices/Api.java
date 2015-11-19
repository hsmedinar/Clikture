package com.abyte.xmlrpcservices;

import com.abyte.piclt2.api.ApiHelper;



public class Api {
	
	ApiHelper api; 
	
	public static final String domain = "clikture.4abyte.com";
    public static final String key = "9a70493ec5c0c2e757fdce2c54055897";    
    public static final String xmlserver = "http://clikture.4abyte.com/services/xmlrpc";


   
	public boolean LogintoDrupal(String user, String pass)
	{
		 boolean valor=false; 
		try 
	      {
	              DrupalXmlRpcService Service = 
	                          new DrupalXmlRpcService(domain, key , xmlserver);	              
	              Service.connect();	              
	              valor= Service.login(user, pass);
	              return valor;
	      }
	      catch (Exception e)
	     {
	             return false;
	      }
	}


	public boolean registeruser(String user, String email, String pass)
	{
		try 
	      {
	              DrupalXmlRpcService Service = 
	                          new DrupalXmlRpcService(domain, key , xmlserver);	              
	              Service.connect();	              
	              Service.login("management_user", "@2011mana");
	              Service.newlogin(user,email,pass);
	              Service.logout();
	              return true;
	      }
	      catch (Exception e)
	     {
	             return false;
	      }
	}
	
	public boolean validateuser(String user)
	{
		try 
	      {
	              String validate= api.Get_Validtae_User(user);
	            if (validate.equals("1")){
	            	return true;
	            } 
	              
	      }
	      catch (Exception e)
	     {
	             return false;
	      }
	      
	      return false;
	}
	
	public String registerdatamobile(String image, String date, String lat, String lon)
	{
		try 
	      {
	              DrupalXmlRpcService Service = 
	                          new DrupalXmlRpcService(domain, key , xmlserver);	              
	              String valor = Service.nodeSavee(image,date,lat,lon);
	              Service.logout();
	              return valor;
	      }
	      catch (Exception e)
	     {
	    	  return "Mal-Api";
	      }
	}
	
}
