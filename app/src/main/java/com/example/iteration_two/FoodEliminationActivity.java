package com.example.iteration_two;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FoodEliminationActivity extends AppCompatActivity {

    private TextView foodEliminationItem, foodEliminationDates, foodEliminationStatus;
    private ImageView foodEliminationIcon;
    private RelativeLayout startElimination, ingredientDropdown, cancelNewElimination, startNewElimination, startEliminationEmpty, navigationFoodElimEmpty, navigationFoodElim;
    private LinearLayout foodEliminationContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_elimination_empty);

        // activity_food_elimination_empty.xml
        startEliminationEmpty = findViewById(R.id.rl_start_elimination_empty);
        navigationFoodElimEmpty = findViewById(R.id.rl_navigation_food_elim_empty);

        // activity_food_elimination.xml
        navigationFoodElim = findViewById(R.id.rl_navigation_food_elim);
        startElimination = findViewById(R.id.rl_start_elimination);
        foodEliminationContainer = findViewById(R.id.ll_food_elimination_container);

        // food_elimination_item.xml
        foodEliminationIcon = findViewById(R.id.iv_food_elimination_icon);
        foodEliminationItem = findViewById(R.id.tv_food_elimination_item);
        foodEliminationDates = findViewById(R.id.tv_food_elimination_dates);
        foodEliminationStatus = findViewById(R.id.tv_food_elimination_status);


        // new_elimination_popup.xml
        ingredientDropdown = findViewById(R.id.rl_ingredient_dropdown);
        cancelNewElimination = findViewById(R.id.rl_cancel_new_elimination);
        startNewElimination = findViewById(R.id.rl_start_new_elimination);



    }
}