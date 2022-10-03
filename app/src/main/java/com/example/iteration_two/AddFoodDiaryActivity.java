package com.example.iteration_two;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class AddFoodDiaryActivity extends AppCompatActivity {

    private RelativeLayout cancelAddMeal, selectBreakfast, selectLunch, selectDinner, selectSnack, confirmAddMeal, exportData;
    private EditText mealTitle, mealDescription, additionalNotes;
    private LinearLayout addSymptoms, addPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food_diary);

        cancelAddMeal = findViewById(R.id.rl_cancel_add_meal);
        selectBreakfast = findViewById(R.id.rl_select_breakfast);
        selectLunch = findViewById(R.id.rl_select_lunch);
        selectDinner = findViewById(R.id.rl_select_dinner);
        selectSnack = findViewById(R.id.rl_select_snack);
        mealTitle = findViewById(R.id.et_meal_title);
        mealDescription = findViewById(R.id.et_meal_description);
        addSymptoms = findViewById(R.id.ll_add_symptoms);
        confirmAddMeal = findViewById(R.id.rl_add_meal_confirm);
        addPhoto = findViewById(R.id.ll_add_photo);
        additionalNotes = findViewById(R.id.et_additional_notes_add_food);
        exportData = findViewById(R.id.rl_export_data);

    }
}