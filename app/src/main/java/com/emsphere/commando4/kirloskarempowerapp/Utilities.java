package com.emsphere.commando4.kirloskarempowerapp;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by commando4 on 1/9/2018.
 */

public class Utilities {

    public static boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
