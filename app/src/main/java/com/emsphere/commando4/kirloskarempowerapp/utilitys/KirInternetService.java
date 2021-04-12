package com.emsphere.commando4.kirloskarempowerapp.utilitys;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.emsphere.commando4.kirloskarempowerapp.Utilities;
import com.emsphere.commando4.kirloskarempowerapp.constantclass.EmpowerApplication;
import com.emsphere.commando4.kirloskarempowerapp.database.KirloskarEmpowerDatabase;
import com.emsphere.commando4.kirloskarempowerapp.pojo.CommanResponsePojo;
import com.emsphere.commando4.kirloskarempowerapp.pojo.EmployeeDeviceStatusPoJo;
import com.emsphere.commando4.kirloskarempowerapp.pojo.OffLineAttendancePoJo;
import com.emsphere.commando4.kirloskarempowerapp.rest.ApiClient;
import com.emsphere.commando4.kirloskarempowerapp.rest.ApiInterface;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Admin on 22/01/2017.
 */
public class KirInternetService extends Service   {


    /**
     * indicates how to behave if the service is killed
     */
    int mStartMode;

    /**
     * interface for clients that bind
     */
    IBinder mBinder;

    /**
     * indicates whether onRebind should be used
     */
    boolean mAllowRebind;

    ProgressDialog pd;
    boolean isInProgress=false;

    private String delRowId = "";
    KirloskarEmpowerDatabase db;
    ApiInterface apiService;
    String time,remark1,lat,log,addreee,isonline,door1,provider;
    String datetime,isgps,isInternet,isBattery,deviceSwitchoff,recordId;
    //private WebServiceListener mWebServiceListener;

    /**
     * Called when the service is being created.
     */
    @Override
    public void onCreate() {

        db = new KirloskarEmpowerDatabase(this);
        apiService =
                ApiClient.getClient().create(ApiInterface.class);
        //mWebServiceListener=this;

    }

    /**
     * The service is starting, due to a call to startService()
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("service"," start called");

        List<OffLineAttendancePoJo> empList = db.getAttendenceList();
        if (!empList.isEmpty()) {
            addreee = getLocationAddress(Double.parseDouble(empList.get(0).getLatitude()), Double.parseDouble(empList.get(0).getLongitude()));
            lat=empList.get(0).getLatitude();
            log=empList.get(0).getLongitude();
            remark1=empList.get(0).getRemark();
            isonline=empList.get(0).getIsOnlineSwipe();
            time=empList.get(0).getSwipeTime();
            door1=empList.get(0).getDoor();
            provider=empList.get(0).getLocationProvider();
            delRowId = empList.get(0).getId_mark();
            //String image = loadImageFromStorage(empList.get(0).getSwipeImageFileName());
           // addreee = getLocationAddress(Double.parseDouble(empList.get(0).getLatitude()), Double.parseDouble(empList.get(0).getLongitude()));
            //delRowId = empList.get(0).getId_mark();
            if(empList.get(0).getSwipeImageFileName().isEmpty()){
                if(Utilities.isNetworkAvailable(getApplicationContext())) {
                    if(!isInProgress)
                    AddMobileSwipesData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId1")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("deviceId")), time, lat, log, door1, addreee, "", isonline, remark1,provider);
                    Log.e("service"," method called"+EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId1")));
                  //  System.out.println("AddSwipData Method called on offline data");}else {
                    Toast.makeText(getApplicationContext(),"No Internet Connection..",Toast.LENGTH_LONG).show();
                }
            }else {
                String image = loadImageFromStorage(empList.get(0).getSwipeImageFileName());
            }

            //getDeviceStatusList()



        }
        List<EmployeeDeviceStatusPoJo> empList1 = db.getDeviceStatusList();
        if (!empList1.isEmpty()) {
            //addreee = getLocationAddress(Double.parseDouble(empList.get(0).getLatitude()), Double.parseDouble(empList.get(0).getLongitude()));
            datetime = empList1.get(0).getDatetime();
            isgps = empList1.get(0).getIsgpsenable();
            isInternet = empList1.get(0).getIsinternateenable();
            isBattery = empList1.get(0).getIsbatterylow();
            deviceSwitchoff = empList1.get(0).getDeviceswitchoff();
            recordId = empList1.get(0).getRecordid();

            //String image = loadImageFromStorage(empList.get(0).getSwipeImageFileName());
            // addreee = getLocationAddress(Double.parseDouble(empList.get(0).getLatitude()), Double.parseDouble(empList.get(0).getLongitude()));
            //delRowId = empList.get(0).getId_mark();

            if (Utilities.isNetworkAvailable(getApplicationContext())) {
                if(!isInProgress)
                AddEmployeeStatusData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId1")),datetime,isgps,isInternet,isBattery,deviceSwitchoff);

            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection..", Toast.LENGTH_LONG).show();
            }

        }



        return mStartMode;
    }

    /** A client is binding to the service with bindService() */
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /** Called when all clients have unbound with unbindService() */
    @Override
    public boolean onUnbind(Intent intent) {
        return mAllowRebind;
    }

    /** Called when a client is binding to the service with bindService()*/
    @Override
    public void onRebind(Intent intent) {

    }

    /** Called when The service is no longer used and is being destroyed */
    @Override
    public void onDestroy() {

    }





    public void AddMobileSwipesData(String employeeId,String deviceId,String swipeTime,String latitude,String longitude,String door,String locationAddress,String swipeImageFileName,String isOnlineSwipe,String remark,String lprovider) {



        Map<String,String> MarkAttendancedata=new HashMap<String,String>();
        MarkAttendancedata.put("employeeId",employeeId);
        MarkAttendancedata.put("deviceId",deviceId);
        MarkAttendancedata.put("swipeTime",swipeTime);
        MarkAttendancedata.put("latitude",latitude);
        MarkAttendancedata.put("longitude",longitude);
        MarkAttendancedata.put("door",door);
        MarkAttendancedata.put("locationAddress",locationAddress);
        MarkAttendancedata.put("swipeImageFileName",swipeImageFileName);
        MarkAttendancedata.put("isOnlineSwipe",isOnlineSwipe);
        MarkAttendancedata.put("remark",remark);
        MarkAttendancedata.put("locationProvider",lprovider);
        isInProgress=true;

        retrofit2.Call<CommanResponsePojo> call = apiService.AddMobileSwipesData(MarkAttendancedata);

        call.enqueue(new Callback<CommanResponsePojo>() {
            @Override
            public void onResponse(retrofit2.Call<CommanResponsePojo> call, Response<CommanResponsePojo> response) {

                if (response.isSuccessful()) {
                    Log.e("service ","isSuccessful");


                    if (response.body().getStatus().equals("1")) {
                        Log.e("service ","1");
                        Log.e("onRecieve","onrecive method called");
                      //  System.out.println("Punch push successfully");

                        db.deleteMarkDataRecord(delRowId);
                        isInProgress=false;

                        List<OffLineAttendancePoJo> empList = db.getAttendenceList();
                        if(!empList.isEmpty()) {

                            lat = empList.get(0).getLatitude();
                            log = empList.get(0).getLongitude();
                            remark1 = empList.get(0).getRemark();
                            isonline = empList.get(0).getIsOnlineSwipe();
                            time = empList.get(0).getSwipeTime();
                            door1 = empList.get(0).getDoor();
                            delRowId = empList.get(0).getId_mark();
                            addreee = getLocationAddress(Double.parseDouble(lat), Double.parseDouble(log));
                            provider=empList.get(0).getLocationProvider();

                            // addreee = getLocationAddress(Double.parseDouble(lat), Double.parseDouble(log));
                            if(empList.get(0).getSwipeImageFileName().isEmpty()){
                                if(Utilities.isNetworkAvailable(getApplicationContext())) {
                                    AddMobileSwipesData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId1")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("deviceId")), time, lat, log, door1, addreee, "", isonline, remark1,provider);

                                }else {
                                    Toast.makeText(getApplicationContext(),"No Internet Connection..",Toast.LENGTH_LONG).show();
                                }
                            }else {
                                String image = loadImageFromStorage(empList.get(0).getSwipeImageFileName());
                            }
                        }

                        //dialog(current_date.getText().toString());

                        isInProgress=false;

                    } else {
                        db.deleteMarkDataRecord(delRowId);
                        isInProgress=false;
                        List<OffLineAttendancePoJo> empList = db.getAttendenceList();
                        if(!empList.isEmpty()) {
                            //addreee = getLocationAddress(Double.parseDouble(empList.get(0).getLatitude()), Double.parseDouble(empList.get(0).getLongitude()));
                            lat = empList.get(0).getLatitude();
                            log = empList.get(0).getLongitude();
                            remark1 = empList.get(0).getRemark();
                            isonline = empList.get(0).getIsOnlineSwipe();
                            time = empList.get(0).getSwipeTime();
                            door1 = empList.get(0).getDoor();
                            delRowId = empList.get(0).getId_mark();
                            addreee = getLocationAddress(Double.parseDouble(lat), Double.parseDouble(log));

                            String image = loadImageFromStorage(empList.get(0).getSwipeImageFileName());
                        }
                       // EmpowerApplication.alertdialog(response.body().getMessage(), KirInternetService.this);

                    }

                }else {
                    isInProgress=false;
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            EmpowerApplication.alertdialog("File or directory not found", KirInternetService.this);
                            break;
                        case 500:
                            EmpowerApplication.alertdialog("server broken", KirInternetService.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            EmpowerApplication.alertdialog("unknown error", KirInternetService.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<CommanResponsePojo> call, Throwable t) {
                isInProgress=false;

                // Log error here since request failed
                Log.e("TAG", t.toString());

            }
        });
    }

    public String getLocationAddress(double latitude,double longitude){
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();

        return address;
    }

    private String loadImageFromStorage(String img)
    {

        String encoded=null;

        try {
            String filepath = Environment.getExternalStorageDirectory().getPath();
            File dirfile = new File(filepath, "Empower");
            File f=new File(dirfile, img);
            Log.e("image file",f.getAbsolutePath());
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            Log.e("file Stram", String.valueOf(new FileInputStream(f)));
            FileInputStream fileInputStream=new FileInputStream(f);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            try {
                for (int readNum; (readNum = fileInputStream.read(buf)) != -1;) {
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
            if(!isInProgress)
            AddMobileSwipesData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId1")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("deviceId")), time, lat, log, door1, addreee, encoded, isonline,remark1,provider);

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return encoded;

    }
    public void AddEmployeeStatusData(String employeeId,  String DateTime,String gpsoff, String internetoff, String batterylow, String switchoff) {

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
                        Log.e("data",response.body().getMessage());

                        //dialog(current_date.getText().toString());


                        db.DeleteDeviceStatusDataRecord(recordId);
                        List<EmployeeDeviceStatusPoJo> empList1 = db.getDeviceStatusList();
                        if (!empList1.isEmpty()) {
                            //addreee = getLocationAddress(Double.parseDouble(empList.get(0).getLatitude()), Double.parseDouble(empList.get(0).getLongitude()));
                            datetime = empList1.get(0).getDatetime();
                            isgps = empList1.get(0).getIsgpsenable();
                            isInternet = empList1.get(0).getIsinternateenable();
                            isBattery = empList1.get(0).getIsbatterylow();
                            deviceSwitchoff = empList1.get(0).getDeviceswitchoff();
                            recordId = empList1.get(0).getRecordid();

                            //String image = loadImageFromStorage(empList.get(0).getSwipeImageFileName());
                            // addreee = getLocationAddress(Double.parseDouble(empList.get(0).getLatitude()), Double.parseDouble(empList.get(0).getLongitude()));
                            //delRowId = empList.get(0).getId_mark();

                            if (Utilities.isNetworkAvailable(getApplicationContext())) {
                                AddEmployeeStatusData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId1")),datetime,isgps,isInternet,isBattery,deviceSwitchoff);

                            } else {
                                Toast.makeText(getApplicationContext(), "No Internet Connection..", Toast.LENGTH_LONG).show();
                            }

                        }

                    } else {
                        db.DeleteDeviceStatusDataRecord(recordId);
                    List<EmployeeDeviceStatusPoJo> empList1 = db.getDeviceStatusList();
                    if (!empList1.isEmpty()) {
                        //addreee = getLocationAddress(Double.parseDouble(empList.get(0).getLatitude()), Double.parseDouble(empList.get(0).getLongitude()));
                        datetime = empList1.get(0).getDatetime();
                        isgps = empList1.get(0).getIsgpsenable();
                        isInternet = empList1.get(0).getIsinternateenable();
                        isBattery = empList1.get(0).getIsbatterylow();
                        deviceSwitchoff = empList1.get(0).getDeviceswitchoff();
                        recordId = empList1.get(0).getRecordid();

                        //String image = loadImageFromStorage(empList.get(0).getSwipeImageFileName());
                        // addreee = getLocationAddress(Double.parseDouble(empList.get(0).getLatitude()), Double.parseDouble(empList.get(0).getLongitude()));
                        //delRowId = empList.get(0).getId_mark();

                        /*if (Utilities.isNetworkAvailable(getApplicationContext())) {
                            AddEmployeeStatusData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId1")),datetime,isgps,isInternet,isBattery,deviceSwitchoff);

                        } else {
                            Toast.makeText(getApplicationContext(), "No Internet Connection..", Toast.LENGTH_LONG).show();
                        }*/

                    }
                        // EmpowerApplication.alertdialog(response.body().getMessage(), KirInternetService.this);

                    }

                }else {
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            EmpowerApplication.alertdialog("File or directory not found", KirInternetService.this);
                            break;
                        case 500:
                            EmpowerApplication.alertdialog("server broken", KirInternetService.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            EmpowerApplication.alertdialog("unknown error", KirInternetService.this);

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
}