package com.emsphere.commando4.kirloskarempowerapp.geofencing;

import com.emsphere.commando4.kirloskarempowerapp.constantclass.EmpowerApplication;
import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

/**
 * Created by brijesh on 15/4/17.
 */

   public class Constants {

       public static final String GEOFENCE_ID_STAN_UNI = "STAN_UNI";
    public static final String GEOFENCE_ID_STAN_UNI1 = "STAN_UNI1";
    /*public static final float GEOFENCE_RADIUS_IN_METERS = 20;
    public static final float GEOFENCE_RADIUS_IN_METERS1 = 30;

    *//**
     * Map for storing information about stanford university in the Stanford.
     *//*
    public static final HashMap<String, LatLng> AREA_LANDMARKS = new HashMap<String, LatLng>();

    static {
        // stanford university.
        AREA_LANDMARKS.put(GEOFENCE_ID_STAN_UNI, new LatLng(18.5322727, 73.8279859));
        //AREA_LANDMARKS.put(GEOFENCE_ID_STAN_UNI, new LatLng(18.5324124, 73.82984160000001));
        //AREA_LANDMARKS.put(GEOFENCE_ID_STAN_UNI1, new LatLng(18.5324124, 73.82984160000001));
    }*/

    public static final float GEOFENCE_RADIUS_IN_METERS = Float.parseFloat(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("DistanceOfficeArea")));
    public static final float GEOFENCE_RADIUS_IN_METERS1 = Float.parseFloat(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("DistanceNearBy")));

    /**
     * Map for storing information about stanford university in the Stanford.
     */
    public static final HashMap<String, LatLng> AREA_LANDMARKS = new HashMap<String, LatLng>();

    static {
        // stanford university.
        AREA_LANDMARKS.put(GEOFENCE_ID_STAN_UNI, new LatLng(Double.parseDouble(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("LatitudeForGeofencing"))), Double.parseDouble(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("LongitudeForGeofencing")))));
        //AREA_LANDMARKS.put(GEOFENCE_ID_STAN_UNI, new LatLng(18.5324124, 73.82984160000001));
        //AREA_LANDMARKS.put(GEOFENCE_ID_STAN_UNI1, new LatLng(18.5324124, 73.82984160000001));
    }
}
