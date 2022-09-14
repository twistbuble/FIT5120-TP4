package com.example.iteration_two;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeScreenActivity extends AppCompatActivity {

    private ImageView genderHome, foodElimIcon;
    private TextView childName, childAge, foodElimDays, foodElimItem, summaryDates, eczemaCount, stomachAcheCount, diarrhoeaCount, bloatingCount, respiratoryCount, nauseaCount, lethargicCount, jointPainsCount, headachesCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        genderHome = findViewById(R.id.iv_child_gender_home);
        childName = findViewById(R.id.tv_child_name_home);
        childAge = findViewById(R.id.tv_child_age_home);
        foodElimDays = findViewById(R.id.tv_food_elim_days);
        foodElimIcon = findViewById(R.id.iv_food_elim_icon);
        foodElimItem= findViewById(R.id.tv_food_elim_item);
        summaryDates = findViewById(R.id.tv_summary_dates);

        eczemaCount = findViewById(R.id.tv_eczema_count);
        stomachAcheCount = findViewById(R.id.tv_stomach_ache_count);
        diarrhoeaCount = findViewById(R.id.tv_diarrhoea_count);
        bloatingCount = findViewById(R.id.tv_bloating_count);
        respiratoryCount = findViewById(R.id.tv_respiratory_problems_count);
        nauseaCount = findViewById(R.id.tv_nausea_count);
        lethargicCount = findViewById(R.id.tv_lethargic_count);
        jointPainsCount = findViewById(R.id.tv_joint_pains_count);
        headachesCount = findViewById(R.id.tv_headaches_count);

    }
}