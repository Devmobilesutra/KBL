package com.emsphere.commando4.kirloskarempowerapp;

/**
 * Created by commando4 on 3/16/2018.
 */

import android.annotation.SuppressLint;
import android.location.Location;
import android.util.Log;

import com.emsphere.commando4.kirloskarempowerapp.constantclass.EmpowerApplication;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationSettingsRequest;

/**
 * Uses Google Play API for obtaining device locations
 * Created by vikas lakade

 * www.emsphere.com Mobile Development
 */

public class Wherebouts {

    private static final Wherebouts instance = new Wherebouts();

    private static final String TAG = Wherebouts.class.getSimpleName();

    private FusedLocationProviderClient mFusedLocationClient;
    LocationCallback locationCallback;
    LocationRequest locationRequest;
    private LocationSettingsRequest locationSettingsRequest;

    //private Workable<GPSPoint> workable;

    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 1000;


    @SuppressLint("RestrictedApi")
    public Wherebouts() {
        this.locationRequest = new LocationRequest();
        this.locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        this.locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        this.locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(this.locationRequest);
        this.locationSettingsRequest = builder.build();


        this.locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult); // why? this. is. retarded. Android.
                Location currentLocation = locationResult.getLastLocation();

                EmpowerApplication.set_session("getProvider1", currentLocation.getProvider());
                EmpowerApplication.set_session(EmpowerApplication.SESSION_LATTITUDE1, String.valueOf(currentLocation.getLatitude()));
                EmpowerApplication.set_session(EmpowerApplication.SESSION_LONGITUDE1, String.valueOf(currentLocation.getLongitude()));



                GPSPoint gpsPoint = new GPSPoint(currentLocation.getLatitude(), currentLocation.getLongitude());
                Log.i(TAG, "Location Callback results: " + gpsPoint);
                if (null != Attendance_cameraActivity.workable)
                  Attendance_cameraActivity.workable.work(gpsPoint);
            }
        };

       // this.mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //this.mFusedLocationClient.requestLocationUpdates(this.locationRequest, this.locationCallback, Looper.myLooper());
    }

    public static Wherebouts instance() {
        return instance;
    }

   /* public void onChange(Workable<GPSPoint> workable) {
        MainActivity.workable = workable;
    }
*/
    public LocationSettingsRequest getLocationSettingsRequest() {
        return this.locationSettingsRequest;
    }

    public void stop() {
        Log.i(TAG, "stop() Stopping location tracking");
        this.mFusedLocationClient.removeLocationUpdates(this.locationCallback);
    }


    public interface Workable<GPSPoint> {

        public void work(GPSPoint t);
    }

}