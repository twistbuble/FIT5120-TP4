package com.example.iteration_two;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FoodDiaryActivity extends AppCompatActivity {

    private TextView todayDate, mealName, mealTime;
    private RelativeLayout calenderButton, navigationFoodDiary;
    private LinearLayout addFood;
    private ImageView timeIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_diary);

        // activity_food_diary.xml
        todayDate = findViewById(R.id.tv_today_date);
        calenderButton = findViewById(R.id.rl_calender_button);
        addFood = findViewById(R.id.ll_add_food);
        navigationFoodDiary = findViewById(R.id.rl_navigation_food_diary);

        // meal_item.xml
        timeIcon = findViewById(R.id.iv_time_icon);
        mealName = findViewById(R.id.tv_meal_name);
        mealTime = findViewById(R.id.tv_meal_time);

    }
}