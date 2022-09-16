package com.example.iteration_two;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NavigationDrawerActivity extends AppCompatActivity {

    private ImageView childGender;
    private TextView childName;
    private LinearLayout homeButton, intolInfoButton, diaryButton, foodTrialButton, sourcesButton, switchProfileButton, logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        childGender = findViewById(R.id.iv_nav_drawer_gender);
        childName = findViewById(R.id.tv_nav_drawer_child_name);
        homeButton = findViewById(R.id.ll_nav_drawer_home);
        intolInfoButton = findViewById(R.id.ll_nav_drawer_intol_info);
        diaryButton = findViewById(R.id.ll_nav_drawer_diary);
        foodTrialButton = findViewById(R.id.ll_nav_drawer_elim_trial);
        sourcesButton = findViewById(R.id.ll_nav_drawer_sources);
        switchProfileButton = findViewById(R.id.ll_nav_drawer_switch_profile);
        logoutButton = findViewById(R.id.ll_nav_drawer_logout);

    }
}