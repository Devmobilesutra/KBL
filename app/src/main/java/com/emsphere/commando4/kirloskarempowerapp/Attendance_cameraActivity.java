package com.emsphere.commando4.kirloskarempowerapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.emsphere.commando4.kirloskarempowerapp.constantclass.EmpowerApplication;
import com.emsphere.commando4.kirloskarempowerapp.encryptionanddecryption.AESAlgorithm;
import com.emsphere.commando4.kirloskarempowerapp.geofencing.Constants;
//import com.emsphere.commando4.kirloskarempowerapp.geofencing.GeofenceRegistrationService;
import com.emsphere.commando4.kirloskarempowerapp.markhistory.EmployeeMarkHistory;
import com.emsphere.commando4.kirloskarempowerapp.markhistory.HeaderData;
import com.emsphere.commando4.kirloskarempowerapp.pojo.CommanResponsePojo;
import com.emsphere.commando4.kirloskarempowerapp.pojo.area.AreaPojo;
import com.emsphere.commando4.kirloskarempowerapp.pojo.config.EmployeeConfig;
import com.emsphere.commando4.kirloskarempowerapp.utilitys.KirInternetBroadcastReceiver;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by commando1 on 7/31/2017.
 */

public class Attendance_cameraActivity extends AppCompatActivity implements SurfaceHolder.Callback, ConnectivityReceiver.ConnectivityReceiverListener {

    // , GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
    private static final String TAG = "Attendance_camera";
    //for camera
    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    Camera.PictureCallback jpegCallback;
    //for file
    private File file;
    String img_log = "", imgfile, dtm, photostring, img = "", formattedDate, remark_text;
    Bitmap bitmap, scaledBitmap;
    //Bitmap scaledBitmap;
    ByteArrayOutputStream ByteArray;
    int flag = 0, x;
    byte[] ba;
    //for GPS
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static GoogleApiClient mGoogleApiClient;
    private static final int ACCESS_FINE_LOCATION_INTENT_ID = 3;
    private static final String BROADCAST_ACTION = "android.location.PROVIDERS_CHANGED";
    boolean flag1 = false, isInorOUTPunch = false, isOutAreaPunch = false, dialog_flag = false, gpsfirststate = true, internetfirststate = true;
    //for date and time
    TextView current_date, current_time, last_punch_txt;
    public int a, b;
    Date noteTS;

    public static AESAlgorithm aesAlgorithm;

    Dialog dialogRemark = null;
    //for mark hostory
    ExpandableListView expListView;
    ExpandableListAdapter expandableListAdapter;
    ArrayList<HeaderData> listGroupTitles1;
    HashMap<String, ArrayList<EmployeeMarkHistory>> listDataMembers;

    ProgressDialog pd;
    private com.emsphere.commando4.kirloskarempowerapp.rest.ApiInterface apiService;
    FusedLocationProviderClient mFusedLocationClient;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public static Wherebouts.Workable<GPSPoint> workable;
    Timer repeatTask = new Timer();
    int repeatInterval = Integer.parseInt(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("ConfigTime")));
    private ArrayList<String> latitude;
    private ArrayList<String> longitude;
    private ArrayList<String> nearByDistance;
    private ArrayList<String> officeAreaDistance;


    @Override
    public void onPause() {
        super.onPause();
        try {
            if (new KirInternetBroadcastReceiver() != null)
                unregisterReceiver(new KirInternetBroadcastReceiver());
        } catch (Exception e) {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Mark Attendance");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setLogo(R.drawable.em_logo);
        apiService = com.emsphere.commando4.kirloskarempowerapp.rest.ApiClient.getClient().create(com.emsphere.commando4.kirloskarempowerapp.rest.ApiInterface.class);
        last_punch_txt = (TextView) findViewById(R.id.txt_last_date);
        last_punch_txt.setText(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("LastPunch")));
        current_date = (TextView) findViewById(R.id.txt_date);
        initGoogleAPIClient();//Init Google API Client
        checkPermissions();//Check Permission
        aesAlgorithm = new AESAlgorithm();
        surfaceView = (SurfaceView) findViewById(R.id.CameraView);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);

        // for outside punchx
        if (EmpowerApplication.sharedPref.contains(EmpowerApplication.aesAlgorithm.Encrypt("AllowAttendanceAsPerLocation"))) {
            int flagForPunch = Integer.parseInt(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("AllowAttendanceAsPerLocation")));
            if (flagForPunch == 1) {
                isOutAreaPunch = false;
            } else {
                isOutAreaPunch = true;
            }

        }
        if (Utilities.isNetworkAvailable(Attendance_cameraActivity.this)) {
            //if(EmpowerApplication.sharedPref.contains("deviceId") && EmpowerApplication.sharedPref.contains("employeeCode")) {
            getEmployeeAreaConfig(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId1")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")));//data


        } else {
            Toast.makeText(Attendance_cameraActivity.this, "No Internet Connection...", Toast.LENGTH_LONG).show();
        }
        //getEmployeeAreaConfig

       /* googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();*/
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }

            return;
        }
        mFusedLocationClient.requestLocationUpdates(Wherebouts.instance().locationRequest, Wherebouts.instance().locationCallback, Looper.myLooper());
        LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        // deprecated setting, but required on Android versions prior to 3.0
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        jpegCallback = new Camera.PictureCallback() {
            public void onPictureTaken(byte[] data, Camera camera) {
                Log.i("tag", "At on picture taken");
                ByteArray = new ByteArrayOutputStream();
                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                x = bitmap.getWidth();
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
                Matrix matrix = new Matrix();
                if (flag == 1) {
                    matrix.postRotate(0);
                } else {
                    matrix.postRotate(-90);
                }
                Bitmap bitmap1 = Bitmap.createBitmap(scaledBitmap, 0, 0,
                        scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                        false);
                matrix.reset();
                bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, ByteArray);
                ba = null;
                ba = ByteArray.toByteArray();
                Log.i("tag", "Bytearray_first" + ByteArray);
                try {
                    ByteArray.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (insertinfile(ba) == 1) ;
                refreshCamera();
            }
        };

        //for time and date set to text view

        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // update TextView here!
                                //current_date.setText(formattedDate);
                                updateTextView();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.fillInStackTrace();
                }
            }
        };
        t.start();
        //for mark History
        // Get the expandable list
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        expListView.setIndicatorBounds(width - GetPixelFromDips(70), width - GetPixelFromDips(40));
        // Setting up list
        setUpExpList();
        workable = new Wherebouts.Workable<GPSPoint>() {
            @Override
            public void work(GPSPoint gpsPoint) {
                // draw something in the UI with this new data
                Log.e("updated location", gpsPoint.getLatitude() + "" + gpsPoint.getLongitude());
             //   Toast.makeText(Attendance_cameraActivity.this, gpsPoint.getLatitude() + "" + gpsPoint.getLongitude(), Toast.LENGTH_LONG).show();

            }
        };

        repeatTask.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                String latitude = EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session(EmpowerApplication.SESSION_LATTITUDE1));
                String longitude = EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session(EmpowerApplication.SESSION_LONGITUDE1));
                String provider = EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("getProvider1"));
                Log.e("provider", provider);
                String adress = getLocationAddress(Double.parseDouble(latitude), Double.parseDouble(longitude));

                if (latitude != null && longitude != null && provider != null && adress != null) {
                    if (Utilities.isNetworkAvailable(Attendance_cameraActivity.this)) {
                        LocationTrackData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId1")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("deviceId1")), latitude, longitude, adress, provider);
                    } else {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(Attendance_cameraActivity.this, "No Internet Connection...", Toast.LENGTH_LONG).show();
                            }
                        });
                        // save your flags offline here....1)Battery status 2)GPS status 3)Internet Status
                        // Toast.makeText(Attendance_cameraActivity.this, "No Internet Connection...", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }, 60000, repeatInterval);

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(new KirInternetBroadcastReceiver(), filter);
    }

    public int GetPixelFromDips(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.3f);
    }

    //timer....
    private void updateTextView() {
        noteTS = Calendar.getInstance().getTime();

        String time = "hh:mm:ss a"; // 12:00
        // String date = "dd MMM yy";
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd MMM yy");
        formattedDate = df.format(c.getTime());

        //current_date.setText(formattedDate);*/
        current_date.setText(formattedDate + "  " + android.text.format.DateFormat.format(time, noteTS));
    }

    //.........
    public void refreshCamera() {
        if (surfaceHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }
        // stop preview before making changes
        try {
            camera.stopPreview();
        } catch (Exception e) {
        }
        try {
            // camera.setPreviewDisplay(surfaceHolder);
            camera.setDisplayOrientation(90);
            camera.startPreview();

        } catch (Exception e) {
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // Now that the size is known, set up the camera parameters and begin
        // the preview.

        Camera.Parameters parameters = camera.getParameters();
        List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();

        // You need to choose the most appropriate previewSize for your app
        Camera.Size previewSize = previewSizes.get(0);// .... select one of previewSizes here
        //previewSizes.get(0);
        parameters.setPreviewSize(previewSize.width, previewSize.height);
        camera.setParameters(parameters);
        camera.startPreview();
        refreshCamera();
    }

    public void surfaceCreated(SurfaceHolder holder) {
        try {
            // open the camera
            // camera = Camera.open();
            if (Camera.getNumberOfCameras() > 1) {
                camera = Camera.open(1);
            } else {
                camera = Camera.open(0);
            }
        } catch (RuntimeException e) {
            // check for exceptions
            System.err.println(e);
            return;
        }
        Camera.Parameters param;
        param = camera.getParameters();
        //setWillNotDraw(false)
        // modify parameter

        Camera.Parameters parameters = camera.getParameters();
        List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();

        // You need to choose the most appropriate previewSize for your app
        Camera.Size previewSize = previewSizes.get(0);// .... select one of previewSizes here
        //previewSizes.get(0);
        parameters.setPreviewSize(previewSize.width, previewSize.height);
        camera.setParameters(parameters);
        try {
            // The Surface has been created, now tell the camera where to draw
            // the preview.
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {
            // check for exceptions
            System.err.println(e);
            return;
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // stop preview and release camera
        camera.stopPreview();
        camera.release();
        camera = null;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem item = menu.findItem(R.id.action_sync);
        MenuItem item1 = menu.findItem(R.id.change_pswd);

        item.setVisible(true);
        item1.setVisible(true);
        return true;

    }
    public void in_picture(View view) {
        // camera.takePicture(null, null, jpegCallback);
        /*boolean a1=EmpowerApplication.sharedPref.contains("LatitudeForGeofencing");
        boolean a2=EmpowerApplication.sharedPref.contains("LongitudeForGeofencing");
        boolean a3=EmpowerApplication.sharedPref.contains("LatitudeForGeofencing");
        boolean a4=EmpowerApplication.sharedPref.contains("LatitudeForGeofencing");*/
       // if (EmpowerApplication.sharedPref.contains(EmpowerApplication.aesAlgorithm.Encrypt("LatitudeForGeofencing")) && EmpowerApplication.sharedPref.contains(EmpowerApplication.aesAlgorithm.Encrypt("LongitudeForGeofencing")) && EmpowerApplication.sharedPref.contains(EmpowerApplication.aesAlgorithm.Encrypt("DistanceNearBy")) && EmpowerApplication.sharedPref.contains(EmpowerApplication.aesAlgorithm.Encrypt("DistanceOfficeArea"))) {
            //LatLng latLng = Constants.AREA_LANDMARKS.get(Constants.GEOFENCE_ID_STAN_UNI);
            /*if (latLng.latitude != 0 && latLng.longitude != 0 && Constants.GEOFENCE_RADIUS_IN_METERS != 0 && Constants.GEOFENCE_RADIUS_IN_METERS1 != 0) {
                Float distance = locationDistance();
                if (distance < Constants.GEOFENCE_RADIUS_IN_METERS || distance < Constants.GEOFENCE_RADIUS_IN_METERS1) {
                    inPunch();
                } else {
                    if (isOutAreaPunch) {
                        isInorOUTPunch = true;
                        OAMPermission("You are not in office area.");
                    } else {
                        EmpowerApplication.alertdialog("You are not allowed to mark attendance outside of office area.", this);

                    }
                }
            } else {
                inPunch();

            }*/
            if(latitude.size() !=0){
            for (int i=0; i<latitude.size(); i++){
            if (Double.parseDouble(latitude.get(i)) != 0.0 && Double.parseDouble(longitude.get(i)) != 0.0 && Integer.parseInt(nearByDistance.get(i)) != 0 && Integer.parseInt(officeAreaDistance.get(i))!= 0) {
                Float distance = locationDistance(Double.parseDouble(latitude.get(i)),Double.parseDouble(longitude.get(i)));
                if (distance < Integer.parseInt(officeAreaDistance.get(i)) || distance < Integer.parseInt(nearByDistance.get(i))) {
                    inPunch();
                    break;
                } else {
                    if(latitude.size()-1== i) {
                        if (isOutAreaPunch) {
                            isInorOUTPunch = true;
                            OAMPermission("You are not in office area.");
                        } else {
                            EmpowerApplication.alertdialog("You are not allowed to mark attendance outside of office area.", this);

                        }
                    }
                }
            } else {
                inPunch();
                break;

            }
            }
            }else {
                inPunch();
            }
        /*} else {
            EmpowerApplication.alertdialog("Please set configuration Key and Values in Configuration table.", this);

        }*/

    }

    public void inPunch() {
        dialog_flag = false;
        refreshLocationProvider();
        //for check automatic time setting
        if (AutomaticDateTimeSetting()) {
            if (false) {
                camera.takePicture(null, null, jpegCallback);
                // insertinfile(ba);
                String pString = getFilePath();
                Calendar c = Calendar.getInstance();
                SimpleDateFormat dateformat = new SimpleDateFormat("dd MMM yy ");
                String CurrDate1 = dateformat.format(c.getTime());
                SimpleDateFormat timeformat = new SimpleDateFormat("dd-MMM-yy hh:mm a");
                String CurrTime1 = timeformat.format(c.getTime());
                Log.e("datetime", CurrTime1);
                // String latitude = EmpowerApplication.get_session(EmpowerApplication.SESSION_LATTITUDE);
                //String longitude = EmpowerApplication.get_session(EmpowerApplication.SESSION_LONGITUDE);
                String latitude = EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session(EmpowerApplication.SESSION_LATTITUDE1));
                String longitude = EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session(EmpowerApplication.SESSION_LONGITUDE1));
                String provider = EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("getProvider1"));
                //Log.e("getProvider",provider);
                String imagedata = loadImageFromStorage(img);
                if (((EmpowerApplication) getApplication()).isInternetOn()) {
                    try {
                        String adress = getLocationAddress(Double.parseDouble(latitude), Double.parseDouble(longitude));

                        AddMobileSwipesData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId1")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("deviceId1")), CurrTime1, latitude, longitude, "1", adress, imagedata, "True", remark_text, provider);
                        EmpowerApplication.set_session("LastPunch", CurrTime1 + " " + "IN");
                        last_punch_txt.setText(CurrTime1 + " " + "IN");
                        ((EmpowerApplication) getApplication()).PunchHistry(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId1")), CurrTime1, "IN");
                        setUpExpList();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Before Data saved in local", Toast.LENGTH_LONG).show();
                    String rowid = ((EmpowerApplication) getApplication()).EmployeeMarkDetails(
                            CurrTime1, latitude, longitude, "1", "", img, "False", remark_text, provider);
                    EmpowerApplication.set_session("LastPunch", CurrTime1 + " " + "IN");
                    last_punch_txt.setText(CurrTime1 + " " + "IN");

                    ((EmpowerApplication) getApplication()).PunchHistry(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), CurrTime1, "IN");
                    Toast.makeText(getApplicationContext(), "Data saved in local", Toast.LENGTH_LONG).show();
                    Log.e("rowid", rowid);
                    setUpExpList();
                    // Toast.makeText(getApplicationContext(), "Data saved in local", Toast.LENGTH_LONG).show();
                    dialog(current_date.getText().toString());
                }
            } else {
                try {
                    camera.takePicture(null, null, jpegCallback);
                    dialogRemark = new Dialog(Attendance_cameraActivity.this);
                    dialogRemark.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialogRemark.setContentView(R.layout.remark_dailogue);
                    dialogRemark.show();
                    dialogRemark.setCancelable(true);
                    dialogRemark.setCanceledOnTouchOutside(false);
                }catch (Exception e){
                    e.printStackTrace();
                }

                final EditText remark = (EditText) dialogRemark
                        .findViewById(R.id.remark_txt_reason);

                Button btn_submit = (Button) dialogRemark
                        .findViewById(R.id.submit);

                Button btn_cancel = (Button) dialogRemark
                        .findViewById(R.id.cancel);
                btn_cancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        dialogRemark.dismiss();
                    }
                });
                //dialogue code...
                btn_submit.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View arg0) {
                        if (remark.getText().toString().length() <= 500) {
                            remark_text = remark.getText().toString();
                            LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                            boolean gps_enabled = false;
                            boolean network_enabled = false;

                            if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                if (!remark_text.isEmpty()) {
                                    dialogRemark.dismiss();
                                    //camera.takePicture(null, null, jpegCallback);
                                    Calendar c = Calendar.getInstance();
                                    SimpleDateFormat dateformat = new SimpleDateFormat("dd MMM yy ");
                                    String CurrDate1 = dateformat.format(c.getTime());
                                    SimpleDateFormat timeformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
                                    String CurrTime1 = timeformat.format(c.getTime());

                                    String latitude = EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session(EmpowerApplication.SESSION_LATTITUDE1));
                                    String longitude = EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session(EmpowerApplication.SESSION_LONGITUDE1));
                                    // Log.e("lat And Lon",latitude+longitude);
                                    String provider = EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("getProvider1"));
                                    //Log.e("getProvider",provider);

                                    //String latitude = EmpowerApplication.get_session(EmpowerApplication.SESSION_LATTITUDE);
                                    //String longitude = EmpowerApplication.get_session(EmpowerApplication.SESSION_LONGITUDE);
                                    // getLocationAddress(Double.parseDouble(aesAlgorithm.Decrypt(latitude)),Double.parseDouble(aesAlgorithm.Decrypt(longitude)));
                                    String imagedata = loadImageFromStorage(img);

                                    if (((EmpowerApplication) getApplication()).isInternetOn()) {
                                        try {
                                            String adress = getLocationAddress(Double.parseDouble(latitude), Double.parseDouble(longitude));


                                            //dialog(current_date.getText().toString());
                                            AddMobileSwipesData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId1")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("deviceId1")), CurrTime1, latitude, longitude, "1", adress, imagedata, "True", remark_text, provider);
                                            EmpowerApplication.set_session("LastPunch", CurrTime1 + " " + "IN");
                                            last_punch_txt.setText(CurrTime1 + " " + "IN");
                                            ((EmpowerApplication) getApplication()).PunchHistry(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), CurrTime1, "IN");
                                            setUpExpList();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        //(((EmpowerApplication) getApplication()).db).InsertMarkAttendanceHistory(((EmpowerApplication) getApplication()).get_session("empid"), CurrDate1, CurrTime1, "IN");
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Before Data saved in local", Toast.LENGTH_LONG).show();
                                        String rowid = ((EmpowerApplication) getApplication()).EmployeeMarkDetails(
                                                CurrTime1, latitude, longitude, "1", "", img, "False", remark_text, provider);
                                        EmpowerApplication.set_session("LastPunch", CurrTime1 + " " + "IN");
                                        last_punch_txt.setText(CurrTime1 + " " + "IN");
                                        ((EmpowerApplication) getApplication()).PunchHistry(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId1")), CurrTime1, "IN");
                                        Toast.makeText(getApplicationContext(), "Data saved in local", Toast.LENGTH_LONG).show();
                                        Log.e("rowid", rowid);
                                        setUpExpList();

                                        dialog(current_date.getText().toString());

                                    }
                                } else {
                                    EmpowerApplication.dialog("Please Enter Remark", Attendance_cameraActivity.this);
                                }

                            } else {
                                //dialogRemark.dismiss();
                                checkPermissions();
                            }
                        } else {
                            remark.setError("Remark Should be less than 500 charactor");
                        }
                    }
                });

                //................
            }
        } else {
            AutomaticDateTimeSetting();
        }

    }

    public void out_picture(View view) {

        /*if (EmpowerApplication.sharedPref.contains(EmpowerApplication.aesAlgorithm.Encrypt("LatitudeForGeofencing")) && EmpowerApplication.sharedPref.contains(EmpowerApplication.aesAlgorithm.Encrypt("LongitudeForGeofencing")) && EmpowerApplication.sharedPref.contains(EmpowerApplication.aesAlgorithm.Encrypt("DistanceNearBy")) && EmpowerApplication.sharedPref.contains(EmpowerApplication.aesAlgorithm.Encrypt("DistanceOfficeArea"))) {
            LatLng latLng = Constants.AREA_LANDMARKS.get(Constants.GEOFENCE_ID_STAN_UNI);
            if (latLng.latitude != 0 && latLng.longitude != 0 && Constants.GEOFENCE_RADIUS_IN_METERS != 0 && Constants.GEOFENCE_RADIUS_IN_METERS1 != 0) {
                Float distance = locationDistance();
                if (distance < Constants.GEOFENCE_RADIUS_IN_METERS || distance < Constants.GEOFENCE_RADIUS_IN_METERS1) {
                    outPunch();
                } else {
                    if (isOutAreaPunch) {
                        isInorOUTPunch = false;
                        OAMPermission("You are not in office area.");
                    } else {
                        EmpowerApplication.alertdialog("You are not allowed to mark attendance outside of office area.", this);
                    }
                }
            } else {
                outPunch();

            }
        } else {
            EmpowerApplication.alertdialog("Please set configuration Key and Values in Configuration table.", this);

        }*/
        //if (EmpowerApplication.sharedPref.contains(EmpowerApplication.aesAlgorithm.Encrypt("LatitudeForGeofencing")) && EmpowerApplication.sharedPref.contains(EmpowerApplication.aesAlgorithm.Encrypt("LongitudeForGeofencing")) && EmpowerApplication.sharedPref.contains(EmpowerApplication.aesAlgorithm.Encrypt("DistanceNearBy")) && EmpowerApplication.sharedPref.contains(EmpowerApplication.aesAlgorithm.Encrypt("DistanceOfficeArea"))) {
          //  LatLng latLng = Constants.AREA_LANDMARKS.get(Constants.GEOFENCE_ID_STAN_UNI);
        if(latitude.size() !=0) {
            for (int i = 0; i < latitude.size(); i++) {
                if (Double.parseDouble(latitude.get(i)) != 0.0 && Double.parseDouble(longitude.get(i)) != 0.0 && Integer.parseInt(nearByDistance.get(i)) != 0 && Integer.parseInt(officeAreaDistance.get(i)) != 0) {
                    Float distance = locationDistance(Double.parseDouble(latitude.get(i)),Double.parseDouble(longitude.get(i)));
                    if (distance < Integer.parseInt(officeAreaDistance.get(i)) || distance < Integer.parseInt(nearByDistance.get(i))) {
                        outPunch();
                    } else {
                        if(latitude.size()-1== i) {
                            if (isOutAreaPunch) {
                                isInorOUTPunch = false;
                                OAMPermission("You are not in office area.");
                            } else {
                                EmpowerApplication.alertdialog("You are not allowed to mark attendance outside of office area.", this);
                            }
                        }
                    }
                } else {
                    outPunch();
                    break;

                }
            }
        }else {
            outPunch();
        }
        /*} else {
            EmpowerApplication.alertdialog("Please set configuration Key and Values in Configuration table.", this);

        }*/

    }

    public void outPunch() {
        dialog_flag = true;
        refreshLocationProvider();
        if (AutomaticDateTimeSetting()) {
            if (false) {
                camera.takePicture(null, null, jpegCallback);
                Calendar c = Calendar.getInstance();
                SimpleDateFormat dateformat = new SimpleDateFormat("dd MMM yy ");
                String CurrDate1 = dateformat.format(c.getTime());
                SimpleDateFormat timeformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
                String CurrTime1 = timeformat.format(c.getTime());
                //Log.e("datetime",formattedDate+""+timeformat);

                //String latitude = EmpowerApplication.get_session(EmpowerApplication.SESSION_LATTITUDE);
                //String longitude = EmpowerApplication.get_session(EmpowerApplication.SESSION_LONGITUDE);

                String latitude = EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session(EmpowerApplication.SESSION_LATTITUDE1));
                String longitude = EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session(EmpowerApplication.SESSION_LONGITUDE1));
                String imagedata = loadImageFromStorage(img);
                String provider = EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("getProvider1"));
                //Log.e("getProvider",provider);


                //Log.e("lat&lon",lat+""+lon);
                if (((EmpowerApplication) getApplication()).isInternetOn()) {
                    try {
                        String adress = getLocationAddress(Double.parseDouble(latitude), Double.parseDouble(longitude));


                        AddMobileSwipesData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId1")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("deviceId1")), CurrTime1, latitude, longitude, "2", adress, imagedata, "True", remark_text, provider);

                        EmpowerApplication.set_session("LastPunch", CurrTime1 + " " + "OUT");
                        last_punch_txt.setText(CurrTime1 + " " + "OUT");
                        ((EmpowerApplication) getApplication()).PunchHistry(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId1")), CurrTime1, "OUT");
                        setUpExpList();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    String rowid = ((EmpowerApplication) getApplication()).EmployeeMarkDetails(
                            CurrTime1, latitude, longitude, "2", "", img, "False", remark_text, provider);
                    EmpowerApplication.set_session("LastPunch", CurrTime1 + " " + "OUT");
                    last_punch_txt.setText(CurrTime1 + " " + "OUT");
                    ((EmpowerApplication) getApplication()).PunchHistry(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId1")), CurrTime1, "OUT");


                    Log.e("rowid", rowid);
                    setUpExpList();
                    Toast.makeText(getApplicationContext(), "Data saved in local", Toast.LENGTH_LONG).show();
                    dialog(current_date.getText().toString());
                }
            } else {
                try {
                    camera.takePicture(null, null, jpegCallback);

                    dialogRemark = new Dialog(Attendance_cameraActivity.this);
                    dialogRemark.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialogRemark
                            .setContentView(R.layout.remark_dailogue);
                    dialogRemark.show();
                    dialogRemark.setCancelable(true);
                    dialogRemark.setCanceledOnTouchOutside(false);


                }catch (Exception e){
                    e.printStackTrace();
                }

                final EditText remark = (EditText) dialogRemark
                        .findViewById(R.id.remark_txt_reason);

                Button btn_submit = (Button) dialogRemark
                        .findViewById(R.id.submit);
                Button btn_cancel = (Button) dialogRemark
                        .findViewById(R.id.cancel);
                btn_cancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        dialogRemark.dismiss();
                    }
                });
                //dialogue code....

                btn_submit.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View arg0) {
                        remark_text = remark.getText().toString();
                        LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                        boolean gps_enabled = false;
                        boolean network_enabled = false;
                        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            if (!remark_text.isEmpty()) {
                                dialogRemark.dismiss();
                                //camera.takePicture(null, null, jpegCallback);
                                Calendar c = Calendar.getInstance();
                                SimpleDateFormat dateformat = new SimpleDateFormat("dd MMM yy ");
                                String CurrDate1 = dateformat.format(c.getTime());
                                SimpleDateFormat timeformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
                                String CurrTime1 = timeformat.format(c.getTime());

                                Log.e("timedateout", CurrTime1);


                                String latitude = EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session(EmpowerApplication.SESSION_LATTITUDE1));
                                String longitude = EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session(EmpowerApplication.SESSION_LONGITUDE1));

                                String imagedata = loadImageFromStorage(img);
                                String provider = EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("getProvider1"));
                                Log.e("getProvider", provider);


                                if (((EmpowerApplication) getApplication()).isInternetOn()) {

                                    try {
                                        String adress = getLocationAddress(Double.parseDouble(latitude), Double.parseDouble(longitude));
                                        //dialog(current_date.getText().toString());
                                        AddMobileSwipesData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId1")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("deviceId1")), CurrTime1, latitude, longitude, "2", adress, imagedata, "True", remark_text, provider);

                                        EmpowerApplication.set_session("LastPunch", CurrTime1 + " " + "OUT");
                                        last_punch_txt.setText(CurrTime1 + " " + "OUT");
                                        ((EmpowerApplication) getApplication()).PunchHistry(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId1")), CurrTime1, "OUT");
                                        setUpExpList();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                } else {
                                    String rowid = ((EmpowerApplication) getApplication()).EmployeeMarkDetails(
                                            CurrTime1, latitude, longitude, "2", "", img, "False", remark_text, provider);
                                    EmpowerApplication.set_session("LastPunch", CurrTime1 + " " + "OUT");
                                    last_punch_txt.setText(CurrTime1 + " " + "OUT");
                                    ((EmpowerApplication) getApplication()).PunchHistry(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId1")), CurrTime1, "OUT");
                                    Log.e("rowid", rowid);
                                    setUpExpList();
                                    Toast.makeText(getApplicationContext(), "Data saved in local", Toast.LENGTH_LONG).show();
                                    dialog(current_date.getText().toString());
                                }
                            } else {
                                EmpowerApplication.dialog("Please Enter Remark", Attendance_cameraActivity.this);
                            }

                        } else {
                            //dialogRemark.dismiss();
                            checkPermissions();
                        }
                    }
                });
                //.................
            }
        } else {
            AutomaticDateTimeSetting();
        }

    }

    public long insertinfile(byte[] ba2) {
        boolean image_flag = false;
        imgfile = getFilePath();
        Log.i("tag", "imgfile" + imgfile);
        img_log = "\nimgfile" + imgfile;
        file = new File(imgfile);
        try {
            image_flag = file.createNewFile();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            img_log = img_log + "\n exception is" + e1.getMessage();
        }
        Log.i("tag", "At in insertFile");
        Log.i("tag", "At in insertFile" + file);
        Log.e("", "file exists is:" + file.exists());
        img_log = img_log + "\n image_flag" + image_flag;
        img_log = img_log + "\n exists" + file.exists();
        long l = ba2.length;
        try {
            FileOutputStream fo = new FileOutputStream(file);
            fo.write(ba2);
            // bytes.close();
            l = file.length();
            // loadImageFromStorage(img);
            ba = null;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            System.out.println("Exception is:" + e.getMessage());
            Log.e("", "image_captured exception is:" + e.getMessage());
            img_log = img_log + "\n" + e.getMessage();
        }
        // TODO Auto-generated method stub
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e("", "image_captured exception is:" + e.getMessage());
            img_log = img_log + "\n" + e.getMessage();
        }
        // db.add(pinentered,Globals.schoolid,photoid,df1.format(c.getTime()));
        Toast.makeText(Attendance_cameraActivity.this, "photo_captured",
                Toast.LENGTH_SHORT).show();
        Log.i("tag", "At end of insertFile");
        Log.e("", "file exists is:" + file.exists());
        MediaScannerConnection.scanFile(Attendance_cameraActivity.this,
                new String[]{imgfile}, null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        // TODO Auto-generated method stub
                        Log.i("MyFileStorage", "Scanned " + path);
                    }
                });
        return l;
    }

    public String getFilePath() {
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath, "Empower");
        if (!file.exists())
            file.mkdirs();
        SimpleDateFormat f1 = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss",
                Locale.ENGLISH);
        dtm = f1.format(new Date());
        Log.i("imagename", "file-->" + file.getAbsolutePath() + "/" + dtm
                + ".jpg");
        img = (dtm + ".jpg");
        photostring = file.getAbsolutePath() + "/" + img;

        Log.i("TAG", "photostring" + photostring);
        return photostring;
    }

    private String loadImageFromStorage(String img) {

        String encoded = null;

        try {
            String filepath = Environment.getExternalStorageDirectory().getPath();
            File dirfile = new File(filepath, "Empower");
            File f = new File(dirfile, img);
            Log.e("image file", f.getAbsolutePath());
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            Log.e("file Stram", String.valueOf(new FileInputStream(f)));
            FileInputStream fileInputStream = new FileInputStream(f);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            try {
                for (int readNum; (readNum = fileInputStream.read(buf)) != -1; ) {
                    bos.write(buf, 0, readNum); //no doubt here is 0
                    //Writes len bytes from the specified byte array starting at offset off to this byte array output stream.
                    System.out.println("read " + readNum + " bytes,");
                }
            } catch (IOException ex) {
                ex.fillInStackTrace();
                //Logger.getLogger(genJpeg.class.getName()).log(Level.SEVERE, null, ex);
            }
            byte[] bytes = bos.toByteArray();
            Log.e("bytess", String.valueOf(bytes));
            encoded = Base64.encodeToString(bytes, Base64.DEFAULT);
            Log.e("encoded bytes", encoded);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return encoded;

    }

    /* Initiate Google API Client  */
    private void initGoogleAPIClient() {
        //Without Google API Client Auto Location Dialog will not work
        mGoogleApiClient = new GoogleApiClient.Builder(Attendance_cameraActivity.this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    /* Check Location Permission for Marshmallow Devices */
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT > 22) {
            if (ContextCompat.checkSelfPermission(Attendance_cameraActivity.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED)
                requestLocationPermission();
            else
                showSettingDialog();
        } else
            showSettingDialog();
    }

    /*  Show Popup to access User Permission  */
    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(Attendance_cameraActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(Attendance_cameraActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION_INTENT_ID);
        } else {
            ActivityCompat.requestPermissions(Attendance_cameraActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION_INTENT_ID);
        }
    }

    /* Show Location Access Dialog */
    private void showSettingDialog() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);//Setting priotity of Location request to high
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(3 * 1000);//5 sec Time interval for location update
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient to show dialog always when GPS is off
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        //updateGPSStatus("GPS is Enabled in your device");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(Attendance_cameraActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case RESULT_OK:
                        Log.e("Settings", "Result OK");
                        //updateGPSStatus("GPS is Enabled in your device");
                        flag1 = true;
                        //startLocationUpdates();
                        break;
                    case RESULT_CANCELED:
                        Log.e("Settings", "Result Cancel");
                        //updateGPSStatus("GPS is Disabled in your device");
                        break;
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(gpsLocationReceiver, new IntentFilter(BROADCAST_ACTION));//Register broadcast receiver to check the status of GPS
        // EmpowerApplication..setConnectivityListener(this);
        EmpowerApplication.setConnectivityListener(this);

        int response = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(Attendance_cameraActivity.this);
        if (response != ConnectionResult.SUCCESS) {
            Log.d(TAG, "Google Play Service Not Available");
            GoogleApiAvailability.getInstance().getErrorDialog(Attendance_cameraActivity.this, response, 1).show();
        } else {
            Log.d(TAG, "Google play service available");
        }
        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            registerReceiver(new KirInternetBroadcastReceiver(), filter);
        }catch (Exception e){}

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Unregister receiver on destroy
        if (gpsLocationReceiver != null)
            unregisterReceiver(gpsLocationReceiver);

        if (repeatTask != null) {
            repeatTask.cancel();
        }
        try {
            if (new KirInternetBroadcastReceiver() != null)
                unregisterReceiver(new KirInternetBroadcastReceiver());
        }catch (Exception e){}
    }

    //Run on UI
    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
            // showSettingDialog();
        }
    };
    /* Broadcast receiver to check status of GPS */
    private BroadcastReceiver gpsLocationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //If Action is Location
            if (intent.getAction().matches(BROADCAST_ACTION)) {
                LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

                //Check if GPS is turned ON or OFF
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Log.e("About GPS", "GPS is Enabled in your device");
                    // updateGPSStatus("GPS is Enabled in your device");
                } else {
                    //If GPS turned OFF show Location Dialog
                    new Handler().postDelayed(sendUpdatesToUI, 10);
                    // showSettingDialog();
                    // updateGPSStatus("GPS is Disabled in your device");
                    Log.e("About GPS", "GPS is Disabled in your device");
                }
            }
        }
    };

    /* On Request permission method to check the permisison is granted or not for Marshmallow+ Devices  */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case ACCESS_FINE_LOCATION_INTENT_ID: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

                    //If permission granted show location dialog if APIClient is not null
                    if (mGoogleApiClient == null) {
                        initGoogleAPIClient();
                        // showSettingDialog();
                    } else
                        showSettingDialog();


                } else {
                    // updateGPSStatus("Location Permission denied.");
                    Toast.makeText(Attendance_cameraActivity.this, "Location Permission denied.", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    //this is for refresh location
    private void refreshLocationProvider() {
        // TODO Auto-generated method stub
        if (EmpowerApplication.check_is_gps_on(getApplicationContext())) {
            requestLocationPermission();
        } else {
            // startService(new Intent(Attendance_cameraActivity.this, MyService.class));
        }
    }

    public boolean AutomaticDateTimeSetting() {
        boolean auto = false;
        if (Build.VERSION.SDK_INT > 16) {
            try {
                a = Settings.Global.getInt(getContentResolver(), Settings.Global.AUTO_TIME);
                String aa = TimeZone.getDefault().getDisplayName();
                b = Settings.Global.getInt(getContentResolver(), Settings.Global.AUTO_TIME_ZONE);

                if (a == 1 && b == 1) {
                    Log.d("b", " " + b);
                    Toast.makeText(getApplicationContext(), "automatic setting" + a, Toast.LENGTH_LONG).show();
                    Calendar c = Calendar.getInstance();
                    Date d = c.getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
                    String time1 = sdf.format(d);
                    auto = true;

                } else if (a == 0 || b == 0) {
                    Toast.makeText(this, "Go to automatic setting " + a, Toast.LENGTH_LONG).show();
                    startActivityForResult(new Intent(Settings.ACTION_DATE_SETTINGS), 1);

                }
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
        }


        return auto;
    }

    public void dialog(String msg) {
        final Dialog dialog1 = new Dialog(Attendance_cameraActivity.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (dialog_flag) {
            dialog1.setContentView(R.layout.dialog_markattendance_out);
        } else {
            dialog1.setContentView(R.layout.dialog_markattendance);
        }
        Window window = dialog1.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog1.show();
        dialog1.setCancelable(true);
        dialog1.setCanceledOnTouchOutside(false);
        TextView title = (TextView) dialog1.findViewById(R.id.textView_dialog_mark_attendance);
        title.setText(msg);
        Button btn_ok = (Button) dialog1.findViewById(R.id.button_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                dialog1.dismiss();
            }
        });
    }

    //for Mark hostory
    private void setUpExpList() {
        listGroupTitles1 = new ArrayList<HeaderData>();
        listDataMembers = new HashMap<String, ArrayList<EmployeeMarkHistory>>();
        // Adding province names and number of population as groups
        HeaderData headerData = new HeaderData();
        headerData.setDate("Punch history");
        headerData.setPunch("");

        listGroupTitles1.add(headerData);

        ArrayList<EmployeeMarkHistory> listData = ((EmpowerApplication) getApplication()).getPunchHistry();
        listDataMembers.put(listGroupTitles1.get(0).getDate(), listData);


        /*EmployeeMarkHistory employeeMarkHistoryHeader =new EmployeeMarkHistory();

        String header_datetxt= "<b> <font color='#1c2039'>Date</font> </b> ";

        String header_punchstatustxt="<b><font color='#1c2039'>Punch IN/OUT </font></b>";*/


        expandableListAdapter = new com.emsphere.commando4.kirloskarempowerapp.markhistory.ExpandableListAdapter(getApplicationContext(), listGroupTitles1, listDataMembers);

        //ExpandableListAdapter(this, listGroupTitles1, listDataMembers);
        // Setting list adapter
        expListView.setAdapter(expandableListAdapter);


    }

    //Back press................
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        if(id == R.id.change_pswd){

            Intent i = new Intent(this, ChangepwdActivity.class);
            this.startActivity(i);
        }

        if (id == R.id.action_sync) {

            if (((EmpowerApplication) getApplication()).isInternetOn()) {
                Intent i = new Intent(this, Attendance_cameraActivity.class);
                this.startActivity(i);
            }else{
                //  Toast.makeText(this, "No Internet Connection...", Toast.LENGTH_SHORT).show();
                EmpowerApplication.alertdialog("No Internet Connection...", Attendance_cameraActivity.this);

            }
        }


        return super.onOptionsItemSelected(item);
    }
    //....................to call AddMobileSwipes service..........

    public void AddMobileSwipesData(String employeeId, String deviceId, String swipeTime, String latitude, String longitude, String door, String locationAddress, String swipeImageFileName, String isOnlineSwipe, String remark, String lprovider) {

        pd = new ProgressDialog(Attendance_cameraActivity.this);
        pd.setMessage("Loading....");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        Map<String, String> MarkAttendancedata = new HashMap<String, String>();
        MarkAttendancedata.put("employeeId", employeeId);
        MarkAttendancedata.put("deviceId", deviceId);
        MarkAttendancedata.put("swipeTime", swipeTime);
        MarkAttendancedata.put("latitude", latitude);
        MarkAttendancedata.put("longitude", longitude);
        MarkAttendancedata.put("door", door);
        MarkAttendancedata.put("locationAddress", locationAddress);
        MarkAttendancedata.put("swipeImageFileName", swipeImageFileName);
        MarkAttendancedata.put("isOnlineSwipe", isOnlineSwipe);
        MarkAttendancedata.put("remark", remark);
        MarkAttendancedata.put("locationProvider", lprovider);

        Log.d("", "MarkAttendancedata12345: "+MarkAttendancedata);


        retrofit2.Call<CommanResponsePojo> call = apiService.AddMobileSwipesData(MarkAttendancedata);
        call.enqueue(new Callback<CommanResponsePojo>() {
            @Override
            public void onResponse(retrofit2.Call<CommanResponsePojo> call, Response<CommanResponsePojo> response) {
                pd.dismiss();
                Log.d("User ID1: ", response.body().toString());
                if (response.isSuccessful()) {


                    if (response.body().getStatus().equals("1")) {

                        dialog(current_date.getText().toString());


                    } else {
                        EmpowerApplication.alertdialog(response.body().getMessage(), Attendance_cameraActivity.this);

                    }

                } else {
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            EmpowerApplication.alertdialog("File or directory not found", Attendance_cameraActivity.this);
                            break;
                        case 500:
                            EmpowerApplication.alertdialog("server broken", Attendance_cameraActivity.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            EmpowerApplication.alertdialog("unknown error", Attendance_cameraActivity.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<CommanResponsePojo> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());

            }
        });
    }

    public void LocationTrackData(String employeeId, String deviceId, String latitude, String longitude, String locationAddress, String locationProvider) {

        //pd = new ProgressDialog(Attendance_cameraActivity.this);
        // pd.setMessage("Loading....");
        // pd.show();

        Map<String, String> MarkAttendancedata = new HashMap<String, String>();
        MarkAttendancedata.put("employeeId", employeeId);
        MarkAttendancedata.put("deviceId", deviceId);
        MarkAttendancedata.put("latitude", latitude);
        MarkAttendancedata.put("longitude", longitude);
        MarkAttendancedata.put("locationAddress", locationAddress);//locationProvider
        MarkAttendancedata.put("locationProvider", locationProvider);


        retrofit2.Call<CommanResponsePojo> call = apiService.LocationTrackData(MarkAttendancedata);
        call.enqueue(new Callback<CommanResponsePojo>() {
            @Override
            public void onResponse(retrofit2.Call<CommanResponsePojo> call, Response<CommanResponsePojo> response) {
                // pd.dismiss();
                //Log.d("User ID1: ", response.body().toString());
                if (response.isSuccessful()) {


                    if (response.body().getStatus().equals("1")) {

                        //dialog(current_date.getText().toString());


                    } else {
                        EmpowerApplication.alertdialog(response.body().getMessage(), Attendance_cameraActivity.this);

                    }

                } else {
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            EmpowerApplication.alertdialog("File or directory not found", Attendance_cameraActivity.this);
                            break;
                        case 500:
                            EmpowerApplication.alertdialog("server broken", Attendance_cameraActivity.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            EmpowerApplication.alertdialog("unknown error", Attendance_cameraActivity.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<CommanResponsePojo> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());

            }
        });
    }

    //for to get the location address from latitude and langitude

    public String getLocationAddress(double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> addresses = null;
        String address = "";
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return address;
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected, boolean isEnabled, int level, boolean isShutDown) {
        //showSnack(isConnected);
        boolean isBatterylow = false;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd MMM yy hh:mm aa ");
        String CurrDate1 = dateformat.format(c.getTime());
        Log.e("getdata", String.valueOf(isConnected) + String.valueOf(isEnabled) + String.valueOf(level) + String.valueOf(isShutDown));

        if (isShutDown) {
            String rowid = ((EmpowerApplication) getApplication()).EmployeeStatusDetails(CurrDate1, String.valueOf(isEnabled), String.valueOf(isConnected), String.valueOf(isBatterylow), String.valueOf(isShutDown));
        }

        Toast.makeText(Attendance_cameraActivity.this, "Event Received" + isConnected + isEnabled + level + isShutDown, Toast.LENGTH_SHORT).show();


        if (level != 0) {
            isBatterylow = true;

        }
        if (gpsfirststate != isEnabled || internetfirststate != isConnected) {
            gpsfirststate = isEnabled;
            internetfirststate = isConnected;
            if (((EmpowerApplication) getApplication()).isInternetOn()) {
                AddEmployeeStatusData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId1")), CurrDate1, String.valueOf(isEnabled), String.valueOf(isConnected), String.valueOf(isBatterylow), String.valueOf(isShutDown));

            } else {
                String rowid = ((EmpowerApplication) getApplication()).EmployeeStatusDetails(CurrDate1, String.valueOf(isEnabled), String.valueOf(isConnected), String.valueOf(isBatterylow), String.valueOf(isShutDown));
                Log.e("recordadded", "internet off case");
                Log.e("recordRowId", rowid);

            }
        }
    }

    public void AddEmployeeStatusData(String employeeId, String DateTime, String gpsoff, String internetoff, String batterylow, String switchoff) {

        //pd = new ProgressDialog(Attendance_cameraActivity.this);
        // pd.setMessage("Loading....");
        // pd.setCanceledOnTouchOutside(false);
        // pd.show();

        Map<String, String> MarkAttendancedata = new HashMap<String, String>();
        MarkAttendancedata.put("employeeId", employeeId);
        MarkAttendancedata.put("actionDateTime", DateTime);
        MarkAttendancedata.put("GPSFlag", gpsoff);
        MarkAttendancedata.put("InternetFlag", internetoff);
        MarkAttendancedata.put("BatteryFlag", batterylow);
        MarkAttendancedata.put("SwitchOffFlag", switchoff);


        retrofit2.Call<CommanResponsePojo> call = apiService.EmployeeDeviceStatus(MarkAttendancedata);
        call.enqueue(new Callback<CommanResponsePojo>() {
            @Override
            public void onResponse(retrofit2.Call<CommanResponsePojo> call, Response<CommanResponsePojo> response) {
                // pd.dismiss();
                //Log.d("User ID1: ", response.body().toString());
                if (response.isSuccessful()) {


                    if (response.body().getStatus().equals("1")) {
                        Log.e("data", response.body().getMessage());

                        //dialog(current_date.getText().toString());


                    } else {
                        EmpowerApplication.alertdialog(response.body().getMessage(), Attendance_cameraActivity.this);

                    }

                } else {
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            EmpowerApplication.alertdialog("File or directory not found", Attendance_cameraActivity.this);
                            break;
                        case 500:
                            EmpowerApplication.alertdialog("server broken", Attendance_cameraActivity.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            EmpowerApplication.alertdialog("unknown error", Attendance_cameraActivity.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<CommanResponsePojo> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());

            }
        });
    }

    /*this is for geofencing*/
    public void OAMPermission(String msg) {
        // Build an AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(Attendance_cameraActivity.this);

        // Set a title for alert dialog
        builder.setTitle("You are not in office area.");

        // Ask the final question
        builder.setMessage("Are you sure to mark Attendance outside office area?");

        // Set the alert dialog yes button click listener
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do something when user clicked the Yes button
                if (isInorOUTPunch) {
                    isInorOUTPunch = false;
                    inPunch();

                } else {
                    outPunch();

                }
            }
        });

        // Set the alert dialog no button click listener
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do something when No button clicked
                Toast.makeText(getApplicationContext(),
                        "No Button Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = builder.create();
        // Display the alert dialog on interface
        dialog.show();

    }

    public float locationDistance( Double lat,Double lon) {

//        LatLng latLng = Constants.AREA_LANDMARKS.get(Constants.GEOFENCE_ID_STAN_UNI);
        Location loc1 = new Location("");

        loc1.setLatitude(lat);
        loc1.setLongitude(lon);

        Location loc2 = new Location("");
        loc2.setLatitude(Double.parseDouble(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session(EmpowerApplication.SESSION_LATTITUDE1))));
        loc2.setLongitude(Double.parseDouble(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session(EmpowerApplication.SESSION_LONGITUDE1))));

        float distanceInMeters = loc1.distanceTo(loc2);
        Log.e("distance", String.valueOf(distanceInMeters));

        return distanceInMeters;
    }

    public void getEmployeeAreaConfig(String employeeid,String token) {

        Map<String,String> configdata=new HashMap<String,String>();
        configdata.put("employeeId",employeeid);
        configdata.put("token",token);
        latitude=new ArrayList<>();
        longitude=new ArrayList<>();
        nearByDistance=new ArrayList<>();
        officeAreaDistance=new ArrayList<>();
        retrofit2.Call<AreaPojo> call = apiService.getEmployeeAreaConfig(configdata);
        call.enqueue(new Callback<AreaPojo>() {
            @Override
            public void onResponse(retrofit2.Call<AreaPojo> call, Response<AreaPojo> response) {
                latitude.clear();
                longitude.clear();
                nearByDistance.clear();
                officeAreaDistance.clear();
                if(response.isSuccessful()) {
                    if (response.body().getStatus().equals("1")) {
                        for(int i=0;i<response.body().getData().size();i++) {
                            if(response.body().getData().get(i).getLatitude()!=null || !response.body().getData().get(i).getLatitude().equals("")){
                              latitude.add(response.body().getData().get(i).getLatitude());
                           }
                            if(response.body().getData().get(i).getLongitude()!=null  || !response.body().getData().get(i).getLongitude().equals("")){
                                longitude.add(response.body().getData().get(i).getLongitude());
                            }
                            if(response.body().getData().get(i).getDistanceNearBy()!=null  || !response.body().getData().get(i).getDistanceNearBy().equals("")){
                                nearByDistance.add(response.body().getData().get(i).getDistanceNearBy());
                            }
                            if(response.body().getData().get(i).getDistanceOfficeArea()!=null || !response.body().getData().get(i).getDistanceOfficeArea().equals("")){
                                officeAreaDistance.add(response.body().getData().get(i).getDistanceOfficeArea());
                            }

                           
                        }

                    } else {
                     //   EmpowerApplication.alertdialog(response.body().getMessage(), Attendance_cameraActivity.this);

                    }
                }else {
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            EmpowerApplication.alertdialog("File or directory not found", Attendance_cameraActivity.this);
                            break;
                        case 500:
                            EmpowerApplication.alertdialog("server broken", Attendance_cameraActivity.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            EmpowerApplication.alertdialog("unknown error", Attendance_cameraActivity.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }

            }

            @Override
            public void onFailure(retrofit2.Call<AreaPojo> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());


                EmpowerApplication.alertdialog(t.getMessage(), Attendance_cameraActivity.this);



            }
        });
    }
}
