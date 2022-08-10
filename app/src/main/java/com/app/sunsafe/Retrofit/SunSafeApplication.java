package com.app.sunsafe.Retrofit;

import android.app.Application;
import android.content.Context;

import com.app.sunsafe.Retrofit.Helper.APIUtility;


public class SunSafeApplication extends Application {

    private static APIUtility apiUtility;

    public static APIUtility getApiUtility(Context context) {
        apiUtility = new APIUtility(context);
        return apiUtility;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        apiUtility = new APIUtility(getApplicationContext());
    }

}
