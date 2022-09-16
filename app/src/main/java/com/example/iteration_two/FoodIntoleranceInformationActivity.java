package com.example.iteration_two;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class FoodIntoleranceInformationActivity extends AppCompatActivity {

    private TextView foodIntoleranceItem, foodIntoleranceText, symptomOne, symptomTwo, symptomThree, symptomFour, symptomFive, symptomSix, foodOne, foodTwo, foodThree, foodFour, foodFive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_intolerance_information);

        foodIntoleranceItem = findViewById(R.id.tv_food_intolerance_item);
        foodIntoleranceText = findViewById(R.id.tv_food_intolerance_text);
        symptomOne = findViewById(R.id.tv_symptom_one);
        symptomTwo = findViewById(R.id.tv_symptom_two);
        symptomThree = findViewById(R.id.tv_symptom_three);
        symptomFour = findViewById(R.id.tv_symptom_four);
        symptomFive = findViewById(R.id.tv_symptom_five);
        symptomSix = findViewById(R.id.tv_symptom_six);
        foodOne = findViewById(R.id.tv_common_foods_one);
        foodTwo = findViewById(R.id.tv_common_foods_two);
        foodThree = findViewById(R.id.tv_common_foods_three);
        foodFour = findViewById(R.id.tv_common_foods_four);
        foodFive = findViewById(R.id.tv_common_foods_five);

    }
}