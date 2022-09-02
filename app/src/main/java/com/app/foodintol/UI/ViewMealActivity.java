package com.app.foodintol.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.foodintol.Models.LoadMeal.LoadMealRequest;
import com.app.foodintol.Models.LoadMeal.LoadMealResponse;
import com.app.foodintol.R;
import com.app.foodintol.Retrofit.FoodInTolApplication;
import com.app.foodintol.Retrofit.Helper.APIUtility;
import com.app.foodintol.Utils.CommonUtils;

import java.util.Calendar;

public class ViewMealActivity extends AppCompatActivity {

    private RelativeLayout date, time, moodZero, moodOne, moodTwo, energyZero, energyOne, energyTwo, stomachAcheYes, stomachAcheNo;

    private TextView tvDate, tvTime;

    private EditText title, descriptionOne, descriptionTwo;

    private String childId = "", mealId = "";
    private long timestamp = 0;

    private int mood = -1, energy = -1, stomachAche = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_meal);

        date = findViewById(R.id.rl_date);
        time = findViewById(R.id.rl_time);
        moodZero = findViewById(R.id.rl_mood_zero);
        moodOne = findViewById(R.id.rl_mood_one);
        moodTwo = findViewById(R.id.rl_mood_two);
        energyZero = findViewById(R.id.rl_energy_zero);
        energyOne = findViewById(R.id.rl_energy_one);
        energyTwo = findViewById(R.id.rl_energy_two);
        stomachAcheYes = findViewById(R.id.rl_stomach_ache_yes);
        stomachAcheNo = findViewById(R.id.rl_stomach_ache_no);

        tvDate = findViewById(R.id.tv_date);
        tvTime = findViewById(R.id.tv_time);

        title = findViewById(R.id.et_title);
        descriptionOne = findViewById(R.id.et_description_one);
        descriptionTwo = findViewById(R.id.et_description_two);

        try{

            Intent intent = getIntent();
            Bundle extras = intent.getExtras();

            childId = intent.getStringExtra("ChildId");
            mealId = intent.getStringExtra("mealId");
            timestamp = Long.parseLong(intent.getStringExtra("timestamp"));


        }catch (Exception e){


        }

        loadData();

    }

    private void loadData() {

        LoadMealRequest request = new LoadMealRequest();

        request.setId(childId);
        request.setTimestamp(timestamp);

        FoodInTolApplication.getApiUtility(ViewMealActivity.this).LoadMeal(ViewMealActivity.this, true, request, new APIUtility.APIResponseListener<LoadMealResponse>() {
            @Override
            public void onReceiveResponse(LoadMealResponse response) {

                try{

                    for(int i = 0; i < response.getMeals().size(); i++){

                        if(response.getMeals().get(i).getMeal_id().equals(mealId)){

                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(Long.parseLong(response.getMeals().get(i).getTime()));

                            int mYear = calendar.get(Calendar.YEAR);
                            int mMonth = (calendar.get(Calendar.MONTH) + 1);
                            int mDay = calendar.get(Calendar.DAY_OF_MONTH);
                            int hour = calendar.get(Calendar.HOUR_OF_DAY);
                            int minute = calendar.get(Calendar.MINUTE);

                            tvDate.setText("" + mDay + "-" + mMonth + "-" + mYear);
                            tvTime.setText("" + hour + ":" + minute);

                            title.setText(response.getMeals().get(i).getTitle());
                            descriptionOne.setText(response.getMeals().get(i).getDescription());

                            if(response.getMeals().get(i).getMood() == 0){

                                moodZero.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_orange));

                            }else if(response.getMeals().get(i).getMood() == 1){

                                moodOne.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_orange));

                            }else if(response.getMeals().get(i).getMood() == 2){

                                moodTwo.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_orange));

                            }

                            if(response.getMeals().get(i).getEnergy() == 0){

                                energyZero.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_orange));

                            }else if(response.getMeals().get(i).getEnergy() == 1){

                                energyOne.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_orange));

                            }else if(response.getMeals().get(i).getEnergy() == 2){

                                energyTwo.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_orange));

                            }

                            if(response.getMeals().get(i).getStomachAche() == 0){

                                stomachAcheNo.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_orange));

                            }else if(response.getMeals().get(i).getStomachAche() == 1){

                                stomachAcheYes.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_orange));

                            }

                        }

                    }

                }catch (Exception e){


                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(ViewMealActivity.this, FoodDiaryActivity.class);

        intent.putExtra("ChildId", childId);

        startActivity(intent);

    }
}