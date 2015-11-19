package com.abyte.xmlrpcservices;

import java.util.HashMap;
import java.util.Map;

public class DrupalNodeUser extends HashMap<String, Object> {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    // types
    public static final String TYPE_STORY = "test";
    
    public static String NID = "nid";
    public static String TYPE = "type";
    public static String LANGUAGE = "language";
    public static String UID = "uid";
    public static String STATUS = "status";
    public static String CREATED = "created";
    public static String CHANGED = "changed";
    public static String TITLE = "title";
    public static String BODY = "body";
    
    public DrupalNodeUser() {
            super();
    }
    
    public DrupalNodeUser(int initialCapacity, float loadFactor) {
            super(initialCapacity, loadFactor);
    }
    
    public DrupalNodeUser(int initialCapacity) {
            super(initialCapacity);
    }
    
    @SuppressWarnings("unchecked")
    public DrupalNodeUser(Map m) {
            super(m);
    }
    
    public long getNid() {
            Object nid = get(NID);
            if (nid == null) throw new RuntimeException("nid not set (null)");
            if (nid instanceof String) {
                    long result = Long.parseLong((String)nid);
                    put(NID, result);
                    return result;
            }
            else {
                    return (Long)nid;
            }
    }
    
    public void setNid(long nid) {
            put(NID, nid);
    }
    
    public String getTitle() {
            Object o = get(TITLE);
            if (o == null) return null;
            else return o.toString();
    }
    
    public void setTitle(String o) {
            put(TITLE, o);
    }
    
    public String getBody() {
            Object o = get(BODY);
            if (o == null) return null;
            else return o.toString();
    }
    
    public void setBody(String o) {
            put(BODY, o);
    }
    
    public void setType(String o) {
            put(TYPE, o);
    }
}