package com.app.sunsafe;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.app.sunsafe.Location.GPSTracker;
import com.app.sunsafe.Location.Locator;
import com.google.android.gms.common.api.Status;

import java.util.prefs.Preferences;

public class RequestLocationPermissionActivity extends AppCompatActivity {

    private Button buttonRequestLocationPermission;

    private static int PLACES_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_location_permission);

        buttonRequestLocationPermission = findViewById(R.id.button_request);

        buttonRequestLocationPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkPermission();

            }
        });

    }

    private void checkPermission() {


        if (ContextCompat.checkSelfPermission(RequestLocationPermissionActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted

            checkLocationPermission();

        }else{

            getLocation();

        }
    }

    public void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(RequestLocationPermissionActivity.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("Location Access")
                        .setMessage("Please provide Location permission to continue...")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(RequestLocationPermissionActivity.this,
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        Constants.MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        Constants.MY_PERMISSIONS_REQUEST_LOCATION);
            }


        } else {
            //got location permission

            Log.e("RequestLocPerm", "Permission granted!");

            getLocation();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.MY_PERMISSIONS_REQUEST_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted

                    getLocation();

                    Log.e("RequestLocPerm", "Permission granted!");

                } else {
                    // not granted

                    Log.e("RequestLocPerm", "Permission not granted!");
                }
                break;

        }
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {

        GPSTracker gpsTracker = new GPSTracker(RequestLocationPermissionActivity.this);

        double lat = gpsTracker.getLatitude();
        double lon = gpsTracker.getLongitude();

        Log.e("ReqPermAct", "Lat : " + lat);
        Log.e("ReqPermAct", "Lon : " + lon);
//        Log.e("ReqPermAct", "Alt : " + location.getAltitude());

        if(lat != 0 && lon != 0){

            startActivity(new Intent(RequestLocationPermissionActivity.this, MainActivity.class));
            finish();

        }else{

            Toast.makeText(RequestLocationPermissionActivity.this, "Kindly turn on GPS on your device and try again!", Toast.LENGTH_SHORT).show();

        }
    }

}