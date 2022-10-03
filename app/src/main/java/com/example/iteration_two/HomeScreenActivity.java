package com.example.iteration_two;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HomeScreenActivity extends AppCompatActivity {

    private ImageView genderHome, foodElimIcon;
    private RelativeLayout foodElimPage, childUserPage, navigationHomeScreen, exportCancel, exportSend;
    private TextView childName, childAge, foodElimDays, foodElimItem, summaryDates, eczemaCount, stomachAcheCount, diarrhoeaCount, bloatingCount, respiratoryCount, nauseaCount, lethargicCount, jointPainsCount, headachesCount;
    private EditText exportEnterEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        foodElimPage = findViewById(R.id.rl_food_elim_page);
        /*need to declare ID for childUserPage - can't do it - don't have access to screen*/

        genderHome = findViewById(R.id.iv_child_gender_home);
        childName = findViewById(R.id.tv_child_name_home);
        childAge = findViewById(R.id.tv_child_age_home);
        foodElimDays = findViewById(R.id.tv_food_elim_days);
        foodElimIcon = findViewById(R.id.iv_food_elim_icon);
        foodElimItem= findViewById(R.id.tv_food_elim_item);
        summaryDates = findViewById(R.id.tv_summary_dates);
        navigationHomeScreen = findViewById(R.id.rl_navigation_home_screen);

        eczemaCount = findViewById(R.id.tv_eczema_count);
        stomachAcheCount = findViewById(R.id.tv_stomach_ache_count);
        diarrhoeaCount = findViewById(R.id.tv_diarrhoea_count);
        bloatingCount = findViewById(R.id.tv_bloating_count);
        respiratoryCount = findViewById(R.id.tv_respiratory_problems_count);
        nauseaCount = findViewById(R.id.tv_nausea_count);
        lethargicCount = findViewById(R.id.tv_lethargic_count);
        jointPainsCount = findViewById(R.id.tv_joint_pains_count);
        headachesCount = findViewById(R.id.tv_headaches_count);

        /* data_export_popup.xml*/
        exportEnterEmail = findViewById(R.id.et_export_enter_email);
        exportCancel = findViewById(R.id.rl_cancel_export);
        exportSend = findViewById(R.id.rl_send_export);

    }
}