package com.example.iteration_two;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FoodEliminationActivity extends AppCompatActivity {

    private TextView childName, childAge, foodEliminationItem, foodEliminationDates;
    private ImageView childGender, foodEliminationIcon, progressIcon;
    private RelativeLayout startElimination, ingredientDropdown, cancelNewElimination, startNewElimination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_elimination);

        // activity_food_elimination.xml
        childName = findViewById(R.id.tv_child_name_food_elimination);
        childAge = findViewById(R.id.tv_child_age_food_elimination);
        childGender = findViewById(R.id.iv_child_gender_food_elimination);
        startElimination = findViewById(R.id.rl_start_elimination);

        // food_elimination_item.xml
        foodEliminationIcon = findViewById(R.id.iv_food_elimination_icon);
        foodEliminationItem = findViewById(R.id.tv_food_elimination_item);
        foodEliminationDates = findViewById(R.id.tv_food_elimination_dates);
        progressIcon = findViewById(R.id.iv_progress_icon);

        // new_elimination_popup.xml
        ingredientDropdown = findViewById(R.id.rl_ingredient_dropdown);
        cancelNewElimination = findViewById(R.id.rl_cancel_new_elimination);
        startNewElimination = findViewById(R.id.rl_start_new_elimination);

    }
}