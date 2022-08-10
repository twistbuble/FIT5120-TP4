package com.app.sunsafe.Location;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ActivityCompat;


public class Locator implements LocationListener {

    static private final String LOG_TAG = "locator";

    int TIME_INTERVAL = 3000;
    static private final int DISTANCE_INTERVAL = 0; // minimum distance between updates in meters

    static public enum Method {NETWORK, GPS, NETWORK_THEN_GPS}

    private Context mContext;
    private LocationManager locationManager;
    private Locator.Method method;
    private Listener callback;
    Location previousLocation;

    boolean isGPSEnabled =false;
    boolean isNetworkEnabled = false;


    public Locator(Context context) {
        super();
        //TIME_INTERVAL = Singleton.getInstance().getINTERVAL();
        this.mContext = context;
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

    }

    public void getLocation(Locator.Method method, Locator.Listener callback) {
        this.method = method;
        this.callback = (Listener) callback;
        switch (this.method) {
            case GPS:
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                Location networkLocation = this.locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (networkLocation != null) {
                    this.callback.onLocationFound(networkLocation, networkLocation.getBearing());
                    Log.d(LOG_TAG, "Last known location found for network provider : " + networkLocation.toString());
                    //this.requestUpdates(LocationManager.NETWORK_PROVIDER);
                }

                locationManager.removeUpdates(this);

                // getting GPS status
                isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                // getting network status
                isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if (isGPSEnabled){
                    this.requestUpdates(LocationManager.GPS_PROVIDER);
                    break;
                }
                if (isNetworkEnabled){
                    this.requestUpdates(LocationManager.NETWORK_PROVIDER);
                    break;
                }

                if (!isNetworkEnabled && !isGPSEnabled)
                    locationManager.removeUpdates(this);

                break;
        }
    }

    @SuppressLint("MissingPermission")
    private void requestUpdates(String provider) {
        if (this.locationManager.isProviderEnabled(provider)) {
            if (provider.contentEquals(LocationManager.NETWORK_PROVIDER) /*&& CommonUtils.isNetworkAvailable(this.mContext)*/) {
                Log.d(LOG_TAG, "Network connected, start listening : " + provider);
                this.locationManager.requestLocationUpdates(provider, TIME_INTERVAL, DISTANCE_INTERVAL, this);
            } else if (provider.contentEquals(LocationManager.GPS_PROVIDER) /*&& CommonUtils.isNetworkAvailable(this.mContext)*/) {
                Log.d(LOG_TAG, "Mobile network connected, start listening : " + provider);
                this.locationManager.requestLocationUpdates(provider, TIME_INTERVAL, DISTANCE_INTERVAL, this);
            } else {
                Log.d(LOG_TAG, "Proper network not connected for provider : " + provider);
                this.onProviderDisabled(provider);
            }
        } else {
            this.onProviderDisabled(provider);
        }
    }

    @SuppressLint("MissingPermission")
    private void requestSingleUpdate(String provider){
        if (this.locationManager.isProviderEnabled(provider)) {
            if (provider.contentEquals(LocationManager.NETWORK_PROVIDER) /*&& CommonUtils.isNetworkAvailable(this.mContext)*/) {
                Log.d(LOG_TAG, "Network connected, start listening : " + provider);
                this.locationManager.requestSingleUpdate(provider, this,null);
            } else if (provider.contentEquals(LocationManager.GPS_PROVIDER) /*&& CommonUtils.isNetworkAvailable(this.mContext)*/) {
                Log.d(LOG_TAG, "Mobile network connected, start listening : " + provider);
                this.locationManager.requestSingleUpdate(provider, this,null);
            } else {
                Log.d(LOG_TAG, "Proper network not connected for provider : " + provider);
                this.callback.onLocationNotFound();
            }
        } else {
            this.callback.onLocationNotFound();
        }
    }

    public void cancel() {
        Log.d(LOG_TAG, "Locating canceled.");
        this.locationManager.removeUpdates(this);
    }



    @Override
    public void onLocationChanged(Location location) {
        String msg = "\nLocation found : " + location.getLatitude() + " : " + location.getLongitude() + " \nProvider: " + location.getProvider() + "\nAccuracy " + (location.hasAccuracy() ? " : +- " + location.getAccuracy() + " meters" : " ");

        Log.d(LOG_TAG, msg);
        Location prevLoc = previousLocation;
        Location newLoc = location;
        if (previousLocation == null) {
            previousLocation = location;
            prevLoc = location;
        } else if (previousLocation != location) {
            prevLoc = previousLocation;
            newLoc = location;
        } else {
            prevLoc = location;
            newLoc = location;
        }

        float bearing = prevLoc.bearingTo(newLoc);


        this.callback.onLocationFound(location, bearing);

    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }



    public interface Listener {
        void onLocationFound(Location location, float bearing);
        void onLocationNotFound();
    }

    public void getSingleLocation(Locator.Method method, Locator.Listener callback) {
        this.method = method;
        this.callback = (Listener) callback;

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // getting GPS status
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (isGPSEnabled){
            this.requestSingleUpdate(LocationManager.GPS_PROVIDER);
        }
        else if (isNetworkEnabled){
            this.requestSingleUpdate(LocationManager.NETWORK_PROVIDER);
        }

        if (!isNetworkEnabled && !isGPSEnabled){
            locationManager.removeUpdates(this);
            this.callback.onLocationNotFound();
        }



    }
}