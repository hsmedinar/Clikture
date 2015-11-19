package com.abyte.xmlrpcservices;

import java.util.HashMap;
import java.util.Map;

/**
 * A drupal node.
 * It sets and gets all its variables as Map entries.
 * @author chantha
 */
public class DrupalNodeMobile extends HashMap<String, Object> {
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
        public static String IMAGE = "field_image";
        public static String DATE = "field_date";
        public static String LAT = "field_latitud";
        public static String LON = "field_longitud";
        
        public DrupalNodeMobile() {
                super();
        }
        
        public DrupalNodeMobile(int initialCapacity, float loadFactor) {
                super(initialCapacity, loadFactor);
        }
        
        public DrupalNodeMobile(int initialCapacity) {
                super(initialCapacity);
        }
        
        @SuppressWarnings("unchecked")
        public DrupalNodeMobile(Map m) {
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
        
        public String getImage() {
                Object o = get(IMAGE);
                if (o == null) return null;
                else return o.toString();
        }
        
        public void setImage(String o) {
                put(IMAGE, o);
        }
        
        public String getDate() {
                Object o = get(DATE);
                if (o == null) return null;
                else return o.toString();
        }
        
        public void setDate(String o) {
                put(DATE, o);
        }
        
        public String getLatitud() {
            Object o = get(LAT);
            if (o == null) return null;
            else return o.toString();
        }
    
        public void setLatitud(String o) {
            put(LAT, o);
        }
        public String getLongitud() {
            Object o = get(LON);
            if (o == null) return null;
            else return o.toString();
        }
    
        public void setLongitud(String o) {
            put(LON, o);
        }
        public void setType(String o) {
                put(TYPE, o);
        }
        public void setUid(String o) {
            put(UID, o);
    }
}