package com.isuru.mymovies.screens;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.isuru.mymovies.R;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Isuru Senanayake on 16/03/2019.
 *
 * -- Splash screen of the application
 * -- Checking the permissions
 */

public class SplashScreen extends AppCompatActivity {

    // Splash screen wait duration
    private final int SPLASH_SCREEN_DISPLAY_LENGTH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_spash_screen);

        // Checking the permissions
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                android.Manifest.permission.INTERNET,
                android.Manifest.permission.ACCESS_NETWORK_STATE,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
        };

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Intent to start MainActivity
                    Intent splashIntent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(splashIntent);
                    finish();   // Closing the spash screen
                }
            }, SPLASH_SCREEN_DISPLAY_LENGTH);
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Switching to the Main activity
        Intent i = new Intent(SplashScreen.this, MainActivity.class);
        startActivity(i);
        SplashScreen.this.finish();
    }
}
