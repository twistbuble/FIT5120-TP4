package com.example.iteration_two;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FoodDiaryDetails extends AppCompatActivity {

    private RelativeLayout diaryReportBack;
    private LinearLayout diaryReportMealImage, diaryReportSymptoms;
    private TextView diaryReportMealType, diaryReportMealTime, diaryReportMealTitle, diaryReportMealDescription, diaryReportAdditionalNotes;
    private ImageView diaryReportMealTypeIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_diary_details);

        diaryReportBack = findViewById(R.id.rl_diary_report_back);
        diaryReportMealTypeIcon = findViewById(R.id.diary_report_meal_type_icon);
        diaryReportMealType = findViewById(R.id.tv_diary_report_meal_type);
        diaryReportMealTime = findViewById(R.id.tv_diary_report_meal_time);
        diaryReportMealTitle = findViewById(R.id.tv_diary_report_meal_title);
        diaryReportMealDescription = findViewById(R.id.tv_diary_report_meal_description);
        diaryReportMealImage = findViewById(R.id.ll_diary_report_meal_image);
        diaryReportSymptoms = findViewById(R.id.ll_diary_report_symptoms);
        diaryReportAdditionalNotes = findViewById(R.id.tv_diary_report_additional_notes);



    }
}