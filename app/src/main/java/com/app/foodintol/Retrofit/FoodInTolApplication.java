package com.app.foodintol.Retrofit;

import android.app.Application;
import android.content.Context;

import com.app.foodintol.Retrofit.Helper.APIUtility;


public class FoodInTolApplication extends Application {

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
