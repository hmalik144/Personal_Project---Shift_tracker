package com.example.h_mal.shift_tracker;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by h_mal on 27/06/2017.
 */

public class SplashScreen extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences settings = getSharedPreferences("PREFS", 0);
        password = settings.getString("password", "");


        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                if (password.equals("")) {
                    Intent i = new Intent(SplashScreen.this, LoginSetup.class);
                    startActivity(i);
                }else {
                    Intent i = new Intent(SplashScreen.this, LoginScreen.class);
                    startActivity(i);
                }

                // close this activity
                finish();


            }
        }, SPLASH_TIME_OUT);
    }


}