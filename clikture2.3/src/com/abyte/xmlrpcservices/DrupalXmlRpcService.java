package com.abyte.xmlrpcservices;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.xmlrpc.android.XMLRPCClient;

import android.os.SystemClock;
import android.util.Log;


public class DrupalXmlRpcService {

        /**
         * Method names
         */
        public static final String MethodNodeSave = "node.save";
        public static final String MethodSystemConnect = "system.connect";
        public static final String MethodUserLogout = "user.logout";
        public static final String MethodUserLogin = "user.login";
        public static final String MethodNewUser = "user.save";
        public static final String MethodFileSave = "file.save";
        public static final String MethodTestCount = "test.count";
        
        Logger log = Logger.getLogger(DrupalXmlRpcService.class.getName());
        
        final String serviceURL;
        final String serviceDomain;
        final String apiKey;
        
        String sessionID;
        XMLRPCClient xmlRpcClient;
        
        final Charset asciiCs = Charset.forName("US-ASCII");
        
        Mac apikeyMac;
        
        /**
         * Initialize the Drupal XML-RPC Service.
         * The serviceURL must be a valid URL.
         * @param serviceDomain domain
         * @param apiKey the API key with the domain
         * @param serviceURL the URL for the Drupal service.
         * @throws MalformedURLException if the serviceURL is not a URL.
         */
        public DrupalXmlRpcService(String serviceDomain, String apiKey, 
                        String serviceURL) throws MalformedURLException {
                this.serviceDomain = serviceDomain;
                this.apiKey = apiKey;
                this.serviceURL = serviceURL;
                
                // initialize the xmlRpcClient, it won't change as the parameters
                // are final.
                xmlRpcClient = new XMLRPCClient(new URL(this.serviceURL));
        }
        
        /**
         * Generate a random string for signing methods
         * @return
         */
        private String generateNonce() {
                return "" + SystemClock.elapsedRealtime();
        }
        
        /**
         * Generate the default parameters that need to precede every call.
         * @param methodname pass in the method name which should be signed.
         * @return the default parameters. The Vector can be reused to pass
         * other objects via XML_RPC, hence the resulting vector is typed to
         * "Object" rather than String.
         */
        private Vector<Object> generateDefaultParams(String methodname) 
                throws Exception {
                String nonce = generateNonce();
                //long timestamp = SystemClock.elapsedRealtime();
                long timestamp = System.currentTimeMillis();
                // Build String for hashing.
                String hashString = Long.toString(timestamp) + ";" + serviceDomain +
                        ";" + nonce + ";" + methodname;
                String hash = generateHmacHash(hashString);
                
                Vector<Object> params = new Vector<Object>();
                
                params.add(hash);
                params.add(this.serviceDomain);
                params.add(Long.toString(timestamp));
                params.add(nonce);
                //params.add(this.sessionID);
                return params;
        }
        
        /**
         * Compute the HMAC-SHA256 hash of the passed message.
         * As key, the API key is used.
         * @param message the message to hash
         * @return the hash as hex-encoded string.
         * @throws Exception if the encoding algorithm HmacSHA256 can't
         * be found or other problems arise.
         */
        public String generateHmacHash(String message) throws Exception {
                byte[] hash = getApikeyMac().doFinal(asciiCs.encode(message).array());
                
                String result = "";
                for (int i = 0; i < hash.length; i++) {
                        result += Integer.toString((hash[i] & 0xff) + 0x100, 16).substring(1);
                }
                log.finest("Created HMAC: " + result + " of " + message);
                return result;
        }
        
        /**
         * Getter for HMAC, as this is used on every call.
         * @return
         */
        private Mac getApikeyMac() throws Exception {
                if (apikeyMac == null) {
                        SecretKeySpec keySpec = 
                                new javax.crypto.spec.SecretKeySpec(asciiCs.encode(
                                                this.apiKey).array(), "HmacSHA256");
                        apikeyMac = javax.crypto.Mac.getInstance("HmacSHA256");
                        apikeyMac.init(keySpec);
                }
                return apikeyMac;
        }
        
        /**
         * Connect to the remove service
         * @return
         * @throws Exception
         */
        public boolean connect() {
                try {
                        Map map = (Map)xmlRpcClient.call(MethodSystemConnect, new Object[] {});
                        this.sessionID = (String)map.get("sessid");
                        log.fine("Connected to server using SessionID: " + this.sessionID);
                        return true;
                } 
                catch (Exception ex) {
                        System.out.println("cannot connect to " + serviceURL + ":"
                                        + ex.getMessage());
                }
                return false;
        }
        
        /**
         * Call user.login
         */
        
        public boolean login(String username, String password) {
                try {
                	    Vector<Object> params = generateDefaultParams(MethodUserLogin);
                	    params.add(username);
                        params.add(password);
                        //Vector<Object> params = new Vector<Object>();
                        // Add Login parameters
                        //params.add(this.sessionID);
                        
                
                        Map o = (Map)xmlRpcClient.callEx(MethodUserLogin, params.toArray());
                        // the login changes the session id. The new session ID is
                        // authorized, the old one not.
                        this.sessionID = (String) o.get("sessid");
                        Log.i("Login","Successful Login");
                        return true;
                }
                catch (Exception ex) {
                        System.out.println(ex.toString());
                }
                return false;
        }
        /*
        public String login(String username, String password) {
            try {
            	  Vector<Object> params = generateDefaultParams(MethodUserLogin);
         	      params.add(username);
                  params.add(password);
            
                    //Map o = (Map)xmlRpcClient.callEx(MethodUserLogin, params.toArray());
                    String o = (String)xmlRpcClient.test(MethodUserLogin, params.toArray());
                    // the login changes the session id. The new session ID is
                    // authorized, the old one not.
                    //this.sessionID = (String) o.get("sessid");
                    //log.fine("Successful Login");
                    return o;
            }
            catch (Exception ex) {
                    System.out.println(ex.toString());
                    return ex.toString();
            }
               
    	}       
        */
        public boolean newlogin(String username, String email, String password) {
            try {
            	
            	HashMap paramUser = new HashMap();
        		paramUser.put("name", username);
        		paramUser.put("mail",email);
        		paramUser.put("pass",password);
            	Vector<Object> params = generateDefaultParams(MethodNewUser);
            	params.add(paramUser);
              
                Map o = (Map)xmlRpcClient.callEx(MethodNewUser, params.toArray());
                    // the login changes the session id. The new session ID is
                    // authorized, the old one not.
                this.sessionID = (String) o.get("sessid");
                log.fine("Successful Login");
                return true;
            }
            catch (Exception ex) {
                    System.out.println(ex.toString());                   
            }
            return false;
        }
        /*
        public String newlogin(String username, String email, String password) {
            try {
            		HashMap paramUser = new HashMap();
            		paramUser.put("name", "androiduser");
            		paramUser.put("mail","hsmedinar@gmail.com");
            		paramUser.put("pass","123");
            		Object[] param = {this.sessionID,paramUser};
            
                    String o = (String)xmlRpcClient.test(MethodNewUser, param);
                    // the login changes the session id. The new session ID is
                    // authorized, the old one not.
                    //this.sessionID = (String) o.get("sessid");
                    //log.fine("Successful Login");
                    return o;
            }
            catch (Exception ex) {
                    System.out.println(ex.toString());
                    return ex.toString();
            }
            
        }
        */
        
        /**
         * Call user.logout
         */
        public boolean logout() {
                try {
                        Vector<Object> params = new Vector<Object>();
                        params.add(this.sessionID);
                        xmlRpcClient.callEx(MethodUserLogout, params.toArray());
                        log.finer("Logout Successful");
                        return true;
                }
                catch (Exception ex) {
                        System.out.println(ex.toString());
                }
                return false;
        }
        
        /**
         * Call file.save
         * Pass in the file as byte-array.
         */
        public boolean fileSave(byte[] file) {
                try {
                        Vector<Object> params = generateDefaultParams(MethodFileSave);
                        params.add(file);
                        Object o = xmlRpcClient.callEx(MethodFileSave, params.toArray());
                        if (log.isLoggable(Level.FINEST))
                                log.finest(MethodFileSave + " returned " + o.toString());
                        return true;
                }
                catch (Exception ex) {
                        System.out.println(ex.toString());
                }
                return false;
        }
        
        /**
         * Call node.save
         * @param node the node to save
         */
        public boolean nodeSave(DrupalNode node) {
                try {
                        Vector<Object> params = new Vector<Object>();
                        params.add(this.sessionID);
                        params.add(node);
                        Object o = xmlRpcClient.callEx(MethodNodeSave, params.toArray());
                        if (log.isLoggable(Level.FINEST))
                                log.finest(MethodNodeSave + " returned " + o.toString());
                        return true;
                }
                catch (Exception ex) {
                        System.out.println(ex.toString());
                }
                return false;
        }
        
        /********* new code ***********/
        
        public String nodeSavee(String image,String date,String lat,String lon) {
            try {
            	
            	 String nonce = generateNonce();                
                 long timestamp = System.currentTimeMillis();
                 // Build String for hashing.
                 String hashString = Long.toString(timestamp) + ";" + serviceDomain +
                         ";" + nonce + ";" + MethodNodeSave;
                 String hash = generateHmacHash(hashString);                 

                 String node = DataMovile(hash, this.serviceDomain,Long.toString(timestamp),nonce,image,date,lat,lon);
                 String o = xmlRpcClient.testtt(node);
                return o;
            }
            catch (Exception ex) {
                    //System.out.println(ex.toString());
                    return ex.getMessage();
            }
            //return "";
        }
        
        
        
        private String DataMovile(String hashkey,String domain,String longtime,String nonce
        		,String image,String date,String lat,String lon){
        	  String xmlv = "<?xml version=\"1.0\"?>" +
				"<methodCall>"+
					"<methodName>node.save</methodName>"+
						"<params>"+
							"<param>"+
								"<value><string>"+ hashkey + "</string></value>"+
							"</param>"+
							"<param>"+
						 		"<value><string>"+ domain + "</string></value>"+
						 	"</param>"+
						 	"<param>"+
						 		"<value><string>"+ longtime + "</string></value>"+
						 	"</param>"+
						 	"<param>"+
						 		"<value><string>"+ nonce + "</string></value>"+
						 	"</param>"+
						 	"<param>"+
						 		"<value><struct>"+
						 			"<member><name>type</name>"+
						 				"<value><string>datamobile</string></value>"+
						 			"</member>"+
						 			"<member><name>title</name>"+
						 				"<value><string>Data Mobile</string></value>"+
						 			"</member>"+
						 			"<member><name>field_image</name>"+
						 				"<value><array>"+
						 					"<data>"+
						 						"<value><struct>"+
						 							"<member><name>value</name>"+
						 								"<value><string>"+ image + "</string></value>"+
						 							"</member>"+
						 						"</struct></value>"+
						 					"</data>"+
						 				"</array></value>"+
						 			"</member>"+
						 			"<member><name>field_date</name>"+
						 				"<value><array>"+
						 					"<data>"+
						 						"<value><struct>"+
						 							"<member><name>value</name>"+
						 								"<value><string>"+ date + "</string></value>"+
						 							"</member>"+
						 						"</struct></value>"+
						 					"</data>"+
						 				"</array></value>"+
						 			"</member>"+
						 			"<member><name>field_date</name>"+
					 				"<value><array>"+
					 					"<data>"+
					 						"<value><struct>"+
					 							"<member><name>value</name>"+
					 								"<value><string>"+ date + "</string></value>"+
					 							"</member>"+
					 						"</struct></value>"+
					 					"</data>"+
					 				"</array></value>"+
					 				"</member>"+
						 			"<member><name>field_latitud</name>"+
					 				"<value><array>"+
					 					"<data>"+
					 						"<value><struct>"+
					 							"<member><name>value</name>"+
					 								"<value><string>"+ lat + "</string></value>"+
					 							"</member>"+
					 						"</struct></value>"+
					 					"</data>"+
					 				"</array></value>"+
					 				"</member>"+
						 			"<member><name>field_longitud</name>"+
					 				"<value><array>"+
					 					"<data>"+
					 						"<value><struct>"+
					 							"<member><name>value</name>"+
					 								"<value><string>"+ lon + "</string></value>"+
					 							"</member>"+
					 						"</struct></value>"+
					 					"</data>"+
					 				"</array></value>"+
					 				"</member>"+
					 				
						 		"</struct></value>"+
						 	"</param>"+
						 	"</params>"+
						 	"</methodCall>"; 
        	  
        	  return xmlv;        	
        }

}