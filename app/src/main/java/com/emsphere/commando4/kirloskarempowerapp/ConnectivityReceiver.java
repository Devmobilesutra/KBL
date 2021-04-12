package com.emsphere.commando4.kirloskarempowerapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;

import com.emsphere.commando4.kirloskarempowerapp.constantclass.EmpowerApplication;

/**
 * Created by commando4 on 5/28/2018.
 */

public class ConnectivityReceiver extends BroadcastReceiver {

    public static ConnectivityReceiverListener connectivityReceiverListener;
    boolean isenabled=false;
    boolean isShutDown=false;
   // int level=-1;

    public ConnectivityReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent arg1) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
       LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
      isenabled=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        int level = arg1.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);

        if(arg1.getAction().equals("android.intent.action.ACTION_SHUTDOWN")){
            isShutDown=true;
        }
        if (connectivityReceiverListener != null) {
            connectivityReceiverListener.onNetworkConnectionChanged(isConnected,isenabled,level,isShutDown);
        }
    }

    public static boolean isConnected() {
        ConnectivityManager
                cm = (ConnectivityManager) EmpowerApplication.getAppContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }


    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected,boolean isEnabled,int level,boolean isShutDown);
    }
}
