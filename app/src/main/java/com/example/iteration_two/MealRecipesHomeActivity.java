package com.example.iteration_two;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;

public class MealRecipesHomeActivity extends AppCompatActivity {

    private RelativeLayout recipeBack, rateRecipe, addFavourite;
    private TextView recipeIngredients, recipeInstructions, recipeRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_recipes_home);

        recipeBack = findViewById(R.id.rl_meal_recipe_back);
        rateRecipe = findViewById(R.id.rl_rate_recipe);
        addFavourite = findViewById(R.id.rl_add_meal_favourites);
        recipeIngredients = findViewById(R.id.tv_recipe_ingredients);
        recipeInstructions = findViewById(R.id.tv_recipe_instructions);
        recipeRating = findViewById(R.id.tv_recipe_rating_score_detail);
    }
}