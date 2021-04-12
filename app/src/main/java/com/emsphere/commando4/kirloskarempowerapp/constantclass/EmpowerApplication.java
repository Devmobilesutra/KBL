package com.emsphere.commando4.kirloskarempowerapp.constantclass;

import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;

import android.support.multidex.MultiDex;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.emsphere.commando4.kirloskarempowerapp.ConnectivityReceiver;
import com.emsphere.commando4.kirloskarempowerapp.MainActivity;
import com.emsphere.commando4.kirloskarempowerapp.R;
import com.emsphere.commando4.kirloskarempowerapp.database.KirloskarEmpowerDatabase;
import com.emsphere.commando4.kirloskarempowerapp.encryptionanddecryption.AESAlgorithm;
import com.emsphere.commando4.kirloskarempowerapp.encryptionanddecryption.BASE64DecoderStream;
import com.emsphere.commando4.kirloskarempowerapp.markhistory.EmployeeMarkHistory;

import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


/**
 * Created by commando1 on 8/2/2017.
 */

public class EmpowerApplication extends Application {
    public static SharedPreferences sharedPref;
    public static SharedPreferences.Editor editor;
    static Context context;
    static EmpowerApplication minstance;
    String PREFS_NAME = "korloskar_vik";
    public static String SESSION_LATTITUDE1 = "session_lattitude1", SESSION_LONGITUDE1 = "session_longitude1";
    public KirloskarEmpowerDatabase db;
    public static String ForSessionExpire = "Your Session Expired";
    public static String SessionKey = "j5aD9uweHEAncbhd";// Must have 16
   // public static String SessionKey1 = "!SMS@16%04#qwrty";                                                  // character session
                                                         // key

    //for encryption
    public static AESAlgorithm aesAlgorithm;


    public void onCreate() {
        super.onCreate();
        db= new KirloskarEmpowerDatabase(getApplicationContext());
        context = getApplicationContext();
        sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        aesAlgorithm= new AESAlgorithm();
        minstance=this;
    }

    public static Context getAppContext(){
        return context;
    }
    public static void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
    public static void set_session(String key, String value) {
        Log.e("","set_session->" + key + ":" + value);
        String temp_key = aesAlgorithm.Encrypt(key);
        String temp_value = aesAlgorithm.Encrypt(value);
        EmpowerApplication.editor.putString(temp_key, temp_value);
        EmpowerApplication.editor.commit();
    }
    public static String get_session(String key) {
/*
        try {
            String temp_key1 = aesAlgorithm.Decrypt1("D2FdG5hFnD1LasEJst0DTg==","!SMS@16%04#qwrty");
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        String temp_key = aesAlgorithm.Encrypt(key);
        String str = "";
        if (sharedPref.contains(temp_key))
            str = sharedPref.getString(temp_key,"");
        Log.e("","get_session->" + key + ":" + str);
        return str;
    }
    //for multidex----becoz when file size cross the limit
     protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    public static boolean check_is_gps_on(Context context) {
        boolean flag = false;
        LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        // getting network status
        boolean isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        Log.e("","IsNetworkEnabled->" + isNetworkEnabled);
        boolean isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        Log.e("","isGPSEnabled->" + isGPSEnabled);
        if (isGPSEnabled && isNetworkEnabled)
            flag = false;
        else
            flag = true;
        return flag;
    }
    public  final boolean isInternetOn() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }

    }
    public  String send_Mark_Attendance_to_SQLiteDatabase( String emp_id,String date,String in_time,
                                                        String out_time,String in_time_latitude,String out_time_latitude,
                                                        String in_time_longitude,String out_time_longitude,String in_photo,
                                                        String out_photo,String in_remark,String out_remark ,
                                                        String is_sync)
    {
  String rowid= db.InsertMobileAttendance(emp_id,
               date,  in_time,  out_time,
               in_time_latitude, in_time_longitude,
               out_time_latitude,  out_time_longitude,
               in_photo, out_photo,  in_remark,  out_remark,  is_sync) ;

      return rowid;
  }


    public  String EmployeePersonalDetails( int employee_ID,
                                            String first_Name, String last_Name, String employee_Image,String _Gender,String officalEmail_ID,String designation_Name,String BirthDate)
    {
        String rowid= db.InsertPersonalDetails(employee_ID,first_Name,last_Name,employee_Image,_Gender,officalEmail_ID,designation_Name,BirthDate) ;

        return rowid;
    }

    public  String EmployeeMarkDetails(
                                            String swaptime, String latitude, String longitude,String door,String locationAddress,String swapimage,String isonline,String remark,String lprovider)
    {
        String rowid= db.InsertSwapDetails(swaptime,latitude,longitude,door,locationAddress,swapimage,isonline,remark,lprovider) ;

        return rowid;
    }

    public  String PunchHistry(
          String id,  String swaptime, String door)
    {
        String rowid= db.InsertMarkAttendanceHistory(id,swaptime,"",door) ;

        return rowid;
    }
    public  ArrayList<EmployeeMarkHistory> getPunchHistry()
    {
        ArrayList<EmployeeMarkHistory> rowid= db.GetLastPunches() ;

        return rowid;
    }

   // GetLastPunches
    public LinkedHashMap<String, String> PersonalDetails(int employee_ID){

        LinkedHashMap<String, String> rowid= db.getPersonalDetails(employee_ID) ;

        return rowid;
    }

    public static void dialog(String msg,Context context) {
        final Dialog dialog1 = new Dialog(context);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.dialog);
        Window window = dialog1.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog1.show();
        dialog1.setCancelable(true);
        dialog1.setCanceledOnTouchOutside(false);
        TextView title = (TextView) dialog1.findViewById(R.id.textView_dialog);
        title.setText(msg);
        Button btn_ok = (Button) dialog1.findViewById(R.id.button_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                dialog1.dismiss();
            }
        });
    }
    public static void alertdialog( String msg, final Context context1) {
        final String session;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context1);
        //String str1=msg+";";
      // msg = msg.Replace("\r", " ").Replace("\n", " ");
       msg=msg.replace("\\r",".").replace("\\n","");
        // String newstr= msg.replace("[\r\n]", ".");
       // newstr =  newstr.replace("\r", ".").replace("\r", "");
         //msg.replace("\r\n", ".");
        session=msg;
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        if(session.equals(ForSessionExpire)){
                            Intent intent = new Intent(context1, MainActivity.class);
                            // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                           context1.startActivity(intent);
                        }
                        //Toast.makeText(context, "You clicked yes button", Toast.LENGTH_LONG).show();

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    public  String EmployeeStatusDetails(
            String datetime, String gpsstatus, String internatestatus,String batterystatus,String Deviceonoff)
    {
        String rowid= db.InsertDeviceStatusDetails(datetime,gpsstatus,internatestatus,batterystatus,Deviceonoff) ;

        return rowid;
    }




}
