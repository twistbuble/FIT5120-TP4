package com.app.foodintol.UI;

import androidx.appcompat.app.AppCompatActivity;
import com.app.foodintol.R;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.VideoView;

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

                        checkIfLoggedIn();

                    }
                },SPLASH_TIME_OUT);

            }
        });

        videoViewSplash.start();

    }

    private void checkIfLoggedIn() {

        // create logic to check if user already logged in

        startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
        finish();

    }
}