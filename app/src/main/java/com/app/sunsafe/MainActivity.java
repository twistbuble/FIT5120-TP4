package com.app.sunsafe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.sunsafe.Location.GPSTracker;
import com.app.sunsafe.Retrofit.Helper.APIUtility;
import com.app.sunsafe.Retrofit.SunSafeApplication;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private TextView currentUVI, currentUVIRange, maxUVI, maxUVITime, skinColourTypeHeading, textRecommendation, recommendedProductsHeading, currentLocation,
            currentSeason, seasonAvgUVI;
    private RelativeLayout skinTypeOne, skinTypeTwo, skinTypeThree, skinTypeFour, skinTypeFive, skinTypeSix, UVIColourType, handbook,
            effectiveSunProtection, rlCurrentUVI;
    private LinearLayout recommendedProducts;
    private ImageView skinTypeIndex;

    private SwipeRefreshLayout swipeRefreshLayout;

    private double currentUVValue = 0;

    private double uvi = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentUVI = findViewById(R.id.tv_current_uv_index);
        currentUVIRange = findViewById(R.id.tv_uv_index_range);
        maxUVI = findViewById(R.id.tv_max_uv_index);
        maxUVITime = findViewById(R.id.tv_max_uv_index_time);
        skinColourTypeHeading = findViewById(R.id.tv_skin_colour_type_heading);
        textRecommendation = findViewById(R.id.tv_text_recommendation);
        recommendedProductsHeading = findViewById(R.id.tv_recommended_products_heading);
        currentLocation = findViewById(R.id.tv_current_location);
        currentSeason = findViewById(R.id.tv_current_season);
        seasonAvgUVI = findViewById(R.id.tv_season_average_uv);

        skinTypeOne = findViewById(R.id.rl_skin_type_one);
        skinTypeTwo = findViewById(R.id.rl_skin_type_two);
        skinTypeThree = findViewById(R.id.rl_skin_type_three);
        skinTypeFour = findViewById(R.id.rl_skin_type_four);
        skinTypeFive = findViewById(R.id.rl_skin_type_five);
        skinTypeSix = findViewById(R.id.rl_skin_type_six);
        UVIColourType = findViewById(R.id.rl_index);
        handbook = findViewById(R.id.rl_handbook);
        effectiveSunProtection = findViewById(R.id.rl_effective_sun_protection);
        rlCurrentUVI = findViewById(R.id.rl_current_uv);

        recommendedProducts = findViewById(R.id.ll_recommended_products);

        skinTypeIndex = findViewById(R.id.iv_skin_colour_type);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(MainActivity.this);

        textRecommendation.setVisibility(View.GONE);

        recommendedProducts.setVisibility(View.GONE);

        recommendedProductsHeading.setVisibility(View.GONE);

        checkLocationPermission();

        handbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(MainActivity.this);

                dialog.setContentView(R.layout.dialog_handbook);

                Button ok = dialog.findViewById(R.id.btn_ok);

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();

                    }
                });

                dialog.show();

            }
        });

        effectiveSunProtection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(MainActivity.this);

                dialog.setContentView(R.layout.dialog_effective_protection);

                Button ok = dialog.findViewById(R.id.btn_ok);

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();

                    }
                });

                dialog.show();

            }
        });

        UVIColourType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(MainActivity.this);

                dialog.setContentView(R.layout.dialog_uvi_colour_type);

                Button ok = dialog.findViewById(R.id.btn_ok);

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();

                    }
                });

                dialog.show();

            }
        });

        skinTypeIndex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(MainActivity.this);

                dialog.setContentView(R.layout.dialog_skin_colour_type);

                Button ok = dialog.findViewById(R.id.btn_ok);

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();

                    }
                });

                dialog.show();

            }
        });

        skinTypeOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                skinColourTypeHeading.setPaintFlags(skinColourTypeHeading.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                skinColourTypeHeading.setText(getString(R.string.selected_choose_skin_colour_type));

                skinTypeOne.setBackground(getDrawable(R.drawable.rectangle_black));

                skinTypeTwo.setBackgroundDrawable(null);
                skinTypeThree.setBackgroundDrawable(null);
                skinTypeFour.setBackgroundDrawable(null);
                skinTypeFive.setBackgroundDrawable(null);
                skinTypeSix.setBackgroundDrawable(null);

                textRecommendation.setVisibility(View.VISIBLE);

                recommendedProducts.setVisibility(View.VISIBLE);

                recommendedProductsHeading.setVisibility(View.VISIBLE);

                displayTextRecommendation(currentUVValue, 1);

            }
        });

        skinTypeTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                skinColourTypeHeading.setPaintFlags(skinColourTypeHeading.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                skinColourTypeHeading.setText(getString(R.string.selected_choose_skin_colour_type));

                skinTypeTwo.setBackground(getDrawable(R.drawable.rectangle_black));

                skinTypeOne.setBackgroundDrawable(null);
                skinTypeThree.setBackgroundDrawable(null);
                skinTypeFour.setBackgroundDrawable(null);
                skinTypeFive.setBackgroundDrawable(null);
                skinTypeSix.setBackgroundDrawable(null);

                textRecommendation.setVisibility(View.VISIBLE);

                recommendedProducts.setVisibility(View.VISIBLE);

                recommendedProductsHeading.setVisibility(View.VISIBLE);

                displayTextRecommendation(currentUVValue, 2);

            }
        });

        skinTypeThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                skinColourTypeHeading.setPaintFlags(skinColourTypeHeading.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                skinColourTypeHeading.setText(getString(R.string.selected_choose_skin_colour_type));

                skinTypeThree.setBackground(getDrawable(R.drawable.rectangle_black));

                skinTypeOne.setBackgroundDrawable(null);
                skinTypeTwo.setBackgroundDrawable(null);
                skinTypeFour.setBackgroundDrawable(null);
                skinTypeFive.setBackgroundDrawable(null);
                skinTypeSix.setBackgroundDrawable(null);

                textRecommendation.setVisibility(View.VISIBLE);

                recommendedProducts.setVisibility(View.VISIBLE);

                recommendedProductsHeading.setVisibility(View.VISIBLE);

                displayTextRecommendation(currentUVValue, 3);

            }
        });

        skinTypeFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                skinColourTypeHeading.setPaintFlags(skinColourTypeHeading.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                skinColourTypeHeading.setText(getString(R.string.selected_choose_skin_colour_type));

                skinTypeFour.setBackground(getDrawable(R.drawable.rectangle_black));

                skinTypeOne.setBackgroundDrawable(null);
                skinTypeTwo.setBackgroundDrawable(null);
                skinTypeThree.setBackgroundDrawable(null);
                skinTypeFive.setBackgroundDrawable(null);
                skinTypeSix.setBackgroundDrawable(null);

                textRecommendation.setVisibility(View.VISIBLE);

                recommendedProducts.setVisibility(View.VISIBLE);

                recommendedProductsHeading.setVisibility(View.VISIBLE);

                displayTextRecommendation(currentUVValue, 4);

            }
        });

        skinTypeFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                skinColourTypeHeading.setPaintFlags(skinColourTypeHeading.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                skinColourTypeHeading.setText(getString(R.string.selected_choose_skin_colour_type));

                skinTypeFive.setBackground(getDrawable(R.drawable.rectangle_black));

                skinTypeOne.setBackgroundDrawable(null);
                skinTypeTwo.setBackgroundDrawable(null);
                skinTypeThree.setBackgroundDrawable(null);
                skinTypeFour.setBackgroundDrawable(null);
                skinTypeSix.setBackgroundDrawable(null);

                textRecommendation.setVisibility(View.VISIBLE);

                recommendedProducts.setVisibility(View.VISIBLE);

                recommendedProductsHeading.setVisibility(View.VISIBLE);

                displayTextRecommendation(currentUVValue, 5);

            }
        });

        skinTypeSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                skinColourTypeHeading.setPaintFlags(skinColourTypeHeading.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                skinColourTypeHeading.setText(getString(R.string.selected_choose_skin_colour_type));

                skinTypeSix.setBackground(getDrawable(R.drawable.rectangle_black));

                skinTypeOne.setBackgroundDrawable(null);
                skinTypeTwo.setBackgroundDrawable(null);
                skinTypeThree.setBackgroundDrawable(null);
                skinTypeFour.setBackgroundDrawable(null);
                skinTypeFive.setBackgroundDrawable(null);

                textRecommendation.setVisibility(View.VISIBLE);

                recommendedProducts.setVisibility(View.VISIBLE);

                recommendedProductsHeading.setVisibility(View.VISIBLE);

                displayTextRecommendation(currentUVValue, 6);

            }
        });

    }

    private void displayTextRecommendation(double currentUVValue, int skinColourType) {

        String textRecommendationStr = "";

        if(skinColourType == 1) {

            textRecommendationStr += "UV radiation leads to sunburn within 10 minutes, skin doesn't tan. \n";

        }else if(skinColourType == 2) {

            textRecommendationStr += "UV radiation leads to sunburn within 20 minutes, skin hardly tans or tans only moderately. \n";

        }else if(skinColourType == 3) {

            textRecommendationStr += "UV radiation leads to sunburn within 30 minutes, skin tans easily. \n";

        }else if(skinColourType == 4) {

            textRecommendationStr += "UV radiation leads to sunburn within 50 minutes, skin soon becomes deeply tanned. \n";

        }else if(skinColourType == 5) {

            textRecommendationStr += "UV radiation only leads to sunburn after more than 60 minutes, skin doesn't become darker. \n";

        }else if(skinColourType == 6) {

            textRecommendationStr += "UV radiation only leads to sunburn after more than 60 minutes, skin doesn't become darker. \n";

        }

        if(currentUVValue < 3){

            textRecommendationStr += "Wear sunglasses on bright days.\n" +
                    "If you burn easily, cover up and use broad spectrum SPF 30+ sunscreen.\n" +
                    "Watch out for bright surfaces, like sand, water and snow, which reflect UV and increase exposure.";

        }else if(currentUVValue >= 3 && currentUVValue < 6){

            textRecommendationStr += "Stay in shade near midday when the sun is strongest.\n" +
                    "If outdoors, wear protective clothing, a wide-brimmed hat, and UV-blocking sunglasses.\n" +
                    "Generously apply broad spectrum SPF 30+ sunscreen every 2 hours, even on cloudy days, and after swimming or sweating. \n" +
                    "Watch out for bright surfaces, like sand, water and snow, which reflect UV and increase exposure.";

        }else if(currentUVValue >= 6 && currentUVValue < 8){

            textRecommendationStr += "Reduce time in the sun between 10 a.m. and 4 p.m.\n" +
                    "If outdoors, seek shade and wear protective clothing, a wide-brimmed hat, and UV-blocking sunglasses.\n" +
                    "Generously apply broad spectrum SPF 30+ sunscreen every 2 hours, even on cloudy days, and after swimming or sweating. \n" +
                    "Watch out for bright surfaces, like sand, water and snow, which reflect UV and increase exposure.";

        }else if(currentUVValue >= 8 && currentUVValue < 11){

            textRecommendationStr += "Minimize sun exposure between 10 a.m. and 4 p.m.\n" +
                    "If outdoors, seek shade and wear protective clothing, a wide-brimmed hat, and UV-blocking sunglasses.\n" +
                    "Generously apply broad spectrum SPF 30+ sunscreen every 2 hours, even on cloudy days, and after swimming or sweating. \n" +
                    "Watch out for bright surfaces, like sand, water and snow, which reflect UV and increase exposure.";

        }else if(currentUVValue > 11){

            textRecommendationStr += "Try to avoid sun exposure between 10 a.m. and 4 p.m.\n" +
                    "If outdoors, seek shade and wear protective clothing, a wide-brimmed hat, and UV-blocking sunglasses.\n" +
                    "Generously apply broad spectrum SPF 30+ sunscreen every 2 hours, even on cloudy days, and after swimming or sweating.\n" +
                    "Watch out for bright surfaces, like sand, water and snow, which reflect UV and increase exposure.";

        }

        textRecommendation.setText(textRecommendationStr);

    }

    public void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            startActivity(new Intent(MainActivity.this, RequestLocationPermissionActivity.class));
            finish();

        } else {
            //got location permission

            getLocation();

            Log.e("RequestLocPerm", "Permission granted!");

        }
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {

        GPSTracker gpsTracker = new GPSTracker(MainActivity.this);

        double lat = gpsTracker.getLatitude();
        double lon = gpsTracker.getLongitude();

//        Log.e("ReqPermAct", "Lat : " + lat);
//        Log.e("ReqPermAct", "Lon : " + lon);
//        Log.e("ReqPermAct", "Alt : " + location.getAltitude());

        if(lat != 0 && lon != 0){

            // need to check gps before this
            loadUVData();

        }else{

            startActivity(new Intent(MainActivity.this, RequestLocationPermissionActivity.class));
            finish();

        }
    }

    private void loadUVData() {

        GPSTracker gpsTracker = new GPSTracker(MainActivity.this);

        double lat = gpsTracker.getLatitude();
        double lon = gpsTracker.getLongitude();

        PostUVDataRequest postUVDataRequest = new PostUVDataRequest();

        postUVDataRequest.setLat("" + lat);
        postUVDataRequest.setLng("" + lon);
        postUVDataRequest.setAlt("50");

        SunSafeApplication.getApiUtility(MainActivity.this).PostUVData(MainActivity.this, false, postUVDataRequest, new APIUtility.APIResponseListener<PostUVDataResponse>() {
            @Override
            public void onReceiveResponse(PostUVDataResponse response) {

                try{

                    currentUVValue = Double.parseDouble(response.getResult().getUv());

                    currentLocation.setText(response.getCurrentLocation().getSuburb() + ", " + response.getCurrentLocation().getState());

                    currentSeason.setText(response.getCurrentSeason());

                    try{

                        seasonAvgUVI.setText(response.getAverageUV().substring(0,3));

                    }catch (Exception e){

                        seasonAvgUVI.setText(response.getAverageUV());

                    }

                    try{

                        currentUVI.setText(response.getResult().getUv().substring(0,3));

                    }catch (Exception e){

                        currentUVI.setText(response.getResult().getUv());

                    }

                    try{

                        maxUVI.setText(response.getResult().getUvMax().substring(0,3));

                    }catch (Exception e){

                        maxUVI.setText(response.getResult().getUvMax());

                    }

                    String inputValue = response.getResult().getUvMaxTime();
                    Instant timestamp = Instant.parse(inputValue);
                    ZonedDateTime melbourneTime = timestamp.atZone(ZoneId.of("Australia/Melbourne"));

                    String time = convert("" + melbourneTime, "yyyy-MM-dd'T'HH:mm:ss", "hh:mm a");


                    maxUVITime.setText(time);

                    changeElementColours(response.getResult().getUv());
//                    seasonAvgUV.setText(response.getAverageUV());

                }catch(Exception e){

                    Toast.makeText(MainActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void changeElementColours(String uv) {

        double currentUVI = Double.parseDouble(uv);

        if(currentUVI < 3){

            rlCurrentUVI.setBackground(getDrawable(R.drawable.circle_green));
            handbook.setBackground(getDrawable(R.drawable.circle_green));
            effectiveSunProtection.setBackground(getDrawable(R.drawable.circle_green));
            UVIColourType.setBackground(getDrawable(R.drawable.circle_green));
            currentUVIRange.setText("Low");

        }else if(currentUVI >= 3 && currentUVI < 6){

            rlCurrentUVI.setBackground(getDrawable(R.drawable.circle_yellow));
            handbook.setBackground(getDrawable(R.drawable.circle_yellow));
            effectiveSunProtection.setBackground(getDrawable(R.drawable.circle_yellow));
            UVIColourType.setBackground(getDrawable(R.drawable.circle_yellow));
            currentUVIRange.setText("Moderate");

        }else if(currentUVI >= 6 && currentUVI < 8){

            rlCurrentUVI.setBackground(getDrawable(R.drawable.circle_orange));
            handbook.setBackground(getDrawable(R.drawable.circle_orange));
            effectiveSunProtection.setBackground(getDrawable(R.drawable.circle_orange));
            UVIColourType.setBackground(getDrawable(R.drawable.circle_orange));
            currentUVIRange.setText("High");

        }else if(currentUVI >= 8 && currentUVI < 11){

            rlCurrentUVI.setBackground(getDrawable(R.drawable.circle_red));
            handbook.setBackground(getDrawable(R.drawable.circle_red));
            effectiveSunProtection.setBackground(getDrawable(R.drawable.circle_red));
            UVIColourType.setBackground(getDrawable(R.drawable.circle_red));
            currentUVIRange.setText("Very high");

        }else if(currentUVI > 11){

            rlCurrentUVI.setBackground(getDrawable(R.drawable.circle_purple));
            handbook.setBackground(getDrawable(R.drawable.circle_purple));
            effectiveSunProtection.setBackground(getDrawable(R.drawable.circle_purple));
            UVIColourType.setBackground(getDrawable(R.drawable.circle_purple));
            currentUVIRange.setText("Extreme");

        }

    }

    public String convert(String inputStr, String inputFormat, String outputFormat){

        Date date = null;
        try {
            date = new SimpleDateFormat(inputFormat).parse( inputStr );
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String outputStr = new SimpleDateFormat(outputFormat).format( date );

        return outputStr;
    }

    @Override
    public void onRefresh() {

        checkLocationPermission();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);

    }
}