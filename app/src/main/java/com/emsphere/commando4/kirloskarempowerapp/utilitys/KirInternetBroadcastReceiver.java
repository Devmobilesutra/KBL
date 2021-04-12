package com.emsphere.commando4.kirloskarempowerapp.utilitys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Admin on 22/01/2017.
 */
public class KirInternetBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
      //  Toast.makeText(context, "Intent Detected.", Toast.LENGTH_LONG).show();
        if(isNetworkAvailable(context)) {
            //context.startService(new Intent(context, KirInternetService.class));
            Intent intents = new Intent(context.getApplicationContext(), KirInternetService.class);
            context.getApplicationContext().startService(intents);
            //System.out.println("service started");
            Log.e("onRecieve","onrecive method called");

        }
    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
