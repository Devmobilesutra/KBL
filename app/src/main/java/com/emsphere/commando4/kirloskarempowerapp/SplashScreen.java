package com.emsphere.commando4.kirloskarempowerapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;


/**
 * Created by commando1 on 7/31/2017.
 */

public class SplashScreen extends AppCompatActivity
{
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;
    ProgressDialog progressDoalog;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
       // FirebaseCrash.report(new Exception("My first Android ` error"));
        setContentView(R.layout.plashscreen);
        progressDoalog = new ProgressDialog(SplashScreen.this);
        progressDoalog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //progressDoalog.getWindow().setGravity(Gravity.CENTER);
        progressDoalog.show();
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreen.this,MainActivity.class);
                startActivity(i);
                // close this activity
                finish();
                progressDoalog.dismiss();
            }
        }, SPLASH_TIME_OUT);
    }
    }

