package com.example.iteration_two;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MealRecipesPageActivity extends AppCompatActivity {

    private ImageView foodElimIcon;
    private TextView  foodElimItem, recommendedRecipeOneText, recommendedRecipeTwoText, otherRecipeOneText, otherRecipeTwoText, otherRecipeThreeText, otherRecipeFourText;
    private RelativeLayout recipeSearch, recommendedRecipeOneImage, recommendedRecipeTwoImage, recommendedRecipeOneHeart, recommendedRecipeTwoHeart, otherRecipeOneImage, otherRecipeTwoImage, otherRecipeThreeImage, otherRecipeFourImage, otherRecipeOneHeart, otherRecipeTwoHeart, otherRecipeThreeHeart, otherRecipeFourHeart;
    private LinearLayout recommendedRecipeOne, recommendedRecipeTwo, otherRecipeOne, otherRecipeTwo, otherRecipeThree, otherRecipeFour;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_recipes_page);

        foodElimIcon = findViewById(R.id.iv_recipes_without_item_icon);
        foodElimItem = findViewById(R.id.tv_recipes_without_item);
        recipeSearch = findViewById(R.id.rl_recipe_search);
        recommendedRecipeOneImage = findViewById(R.id.rl_recommended_recipe_one_image);
        recommendedRecipeTwoImage = findViewById(R.id.rl_recommended_recipe_two_image);
        recommendedRecipeOneHeart = findViewById(R.id.rl_recommended_recipe_one_heart);
        recommendedRecipeTwoHeart = findViewById(R.id.rl_recommended_recipe_two_heart);
        recommendedRecipeOne = findViewById(R.id.ll_recommended_recipe_one);
        recommendedRecipeTwo = findViewById(R.id.ll_recommended_recipe_two);
        recommendedRecipeOneText = findViewById(R.id.tv_recommended_recipe_one_text);
        recommendedRecipeTwoText = findViewById(R.id.tv_recommended_recipe_two_text);

        otherRecipeOne = findViewById(R.id.ll_other_recipe_one);
        otherRecipeTwo = findViewById(R.id.ll_other_recipe_two);
        otherRecipeThree = findViewById(R.id.ll_other_recipe_three);
        otherRecipeFour = findViewById(R.id.ll_other_recipe_four);

        otherRecipeOneText = findViewById(R.id.tv_other_recipe_one_text);
        otherRecipeTwoText = findViewById(R.id.tv_other_recipe_two_text);
        otherRecipeThreeText = findViewById(R.id.tv_other_recipe_three_text);
        otherRecipeFourText = findViewById(R.id.tv_other_recipe_four_text);

        otherRecipeOneImage = findViewById(R.id.rl_other_recipe_one_image);
        otherRecipeTwoImage = findViewById(R.id.rl_other_recipe_two_image);
        otherRecipeThreeImage = findViewById(R.id.rl_other_recipe_three_image);
        otherRecipeFourImage = findViewById(R.id.rl_other_recipe_four_image);

        otherRecipeOneHeart = findViewById(R.id.rl_other_recipe_one_heart);
        otherRecipeTwoHeart = findViewById(R.id.rl_other_recipe_two_heart);
        otherRecipeThreeHeart = findViewById(R.id.rl_other_recipe_three_heart);
        otherRecipeFourHeart = findViewById(R.id.rl_other_recipe_four_heart);
    }
}