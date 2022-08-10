package com.app.sunsafe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import android.widget.VideoView;

import com.app.sunsafe.Location.GPSTracker;

public class SplashScreenActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 10;

    private VideoView videoViewSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        videoViewSplash = (VideoView) findViewById(R.id.vv_splash);

        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.splash_screen_video;

        videoViewSplash.setVideoPath(videoPath);

        videoViewSplash.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        checkLocationPermission();

                    }
                },SPLASH_TIME_OUT);

            }
        });

        videoViewSplash.start();

    }

    public void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            startActivity(new Intent(SplashScreenActivity.this, RequestLocationPermissionActivity.class));
            finish();

        } else {
            //got location permission

            getLocation();

            Log.e("RequestLocPerm", "Permission granted!");

        }
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {

        GPSTracker gpsTracker = new GPSTracker(SplashScreenActivity.this);

        double lat = gpsTracker.getLatitude();
        double lon = gpsTracker.getLongitude();

        Log.e("ReqPermAct", "Lat : " + lat);
        Log.e("ReqPermAct", "Lon : " + lon);
//        Log.e("ReqPermAct", "Alt : " + location.getAltitude());

        if(lat != 0 && lon != 0){

            startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
            finish();

        }else{

            startActivity(new Intent(SplashScreenActivity.this, RequestLocationPermissionActivity.class));
            finish();

        }
    }

}