package com.example.iteration_two;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.RelativeLayout;

public class FoodIntoleranceMainPageActivity extends AppCompatActivity {

    private RelativeLayout dairyInformation, glutenInformation, soyInformation, eggsInformation, fructoseInformation, fodmapsInformation, salicylatesInformation, histamineInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_intolerance_main_page);

        dairyInformation = findViewById(R.id.rl_dairy_information);
        glutenInformation = findViewById(R.id.rl_gluten_information);
        soyInformation = findViewById(R.id.rl_soy_information);
        eggsInformation = findViewById(R.id.rl_eggs_information);
        fructoseInformation = findViewById(R.id.rl_fructose_information);
        fodmapsInformation = findViewById(R.id.rl_fodmaps_information);
        salicylatesInformation = findViewById(R.id.rl_salicylates_information);
        histamineInformation = findViewById(R.id.rl_histamine_information);

    }
}