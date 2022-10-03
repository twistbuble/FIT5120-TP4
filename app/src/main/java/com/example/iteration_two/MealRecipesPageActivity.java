package com.example.iteration_two;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MealRecipesPageActivity extends AppCompatActivity {

    private ImageView foodElimIcon;
    private TextView  foodElimItem, recommendedRecipeText, allRecipesNumber, recommendedRecipesNumber, favouritesRecipesNumber;
    private RelativeLayout recommendedRecipeImage, recommendedRecipeHeart, mealRecipePageBack, mealRecommended, mealRecipeNavigation;
    private LinearLayout recipeSearch, recommendedRecipe, allRecipesFilter, recommendedRecipesFilter, favouritesRecipesFilter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_recipes_page);

        /* activity_meal_recipes_page.xml */
        foodElimIcon = findViewById(R.id.iv_recipes_without_item_icon);
        foodElimItem = findViewById(R.id.tv_recipes_without_item);
        recipeSearch = findViewById(R.id.ll_search_recipes);
        mealRecommended = findViewById(R.id.rl_is_recommended_meal);
        mealRecipePageBack = findViewById(R.id.rl_meal_recipe_back);
        allRecipesFilter = findViewById(R.id.ll_all_recipes_filter);
        recommendedRecipesFilter = findViewById(R.id.ll_recommended_recipes_filter);
        favouritesRecipesFilter = findViewById(R.id.ll_favourite_recipes_filter);
        mealRecipeNavigation = findViewById(R.id.rl_navigation_meal_recipe);
        allRecipesNumber = findViewById(R.id.tv_all_recipes_number);
        favouritesRecipesNumber = findViewById(R.id.tv_favourite_recipes_number);
        recommendedRecipesNumber = findViewById(R.id.tv_recommended_recipes_number);

        /* meal_recipe_item.xml */
        recommendedRecipeImage = findViewById(R.id.rl_recommended_recipe_image);
        recommendedRecipeHeart = findViewById(R.id.rl_recommended_recipe_heart);
        recommendedRecipe = findViewById(R.id.ll_recommended_recipe);
        recommendedRecipeText = findViewById(R.id.tv_recommended_recipe_text);


    }
}