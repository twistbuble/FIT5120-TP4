package com.app.foodintol.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.app.foodintol.MainActivity;
import com.app.foodintol.Models.AddMeal.AddMealRequest;
import com.app.foodintol.Models.AddMeal.AddMealResponse;
import com.app.foodintol.R;
import com.app.foodintol.Retrofit.FoodInTolApplication;
import com.app.foodintol.Retrofit.Helper.APIUtility;

import java.util.Calendar;

public class AddMealActivity extends AppCompatActivity {

    private RelativeLayout date, time, confirm, moodZero, moodOne, moodTwo, energyZero, energyOne, energyTwo, stomachAcheYes, stomachAcheNo;

    private TextView tvDate, tvTime;

    private EditText title, descriptionOne, descriptionTwo;

    private LinearLayout cancel;

    private String childId = "", mealId = "";

    private int mood = -1, energy = -1, stomachAche = -1;

    private long timestamp = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);

        date = findViewById(R.id.rl_date);
        time = findViewById(R.id.rl_time);
        confirm = findViewById(R.id.rl_confirm);
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

        cancel = findViewById(R.id.ll_cancel);

        try{

            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            if(extras.containsKey("ChildId")) {
                childId = intent.getStringExtra("ChildId");
            }

        }catch (Exception e){


        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AddMealActivity.this, FoodDiaryActivity.class);

                intent.putExtra("ChildId", childId);

                startActivity(intent);

            }
        });

        moodZero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mood = 0;

                moodZero.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_orange));
                moodOne.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_grey));
                moodTwo.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_grey));

            }
        });

        moodOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mood = 1;

                moodOne.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_orange));
                moodZero.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_grey));
                moodTwo.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_grey));

            }
        });

        moodTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mood = 2;

                moodTwo.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_orange));
                moodOne.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_grey));
                moodZero.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_grey));

            }
        });

        energyZero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                energy = 0;

                energyZero.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_orange));
                energyOne.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_grey));
                energyTwo.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_grey));

            }
        });

        energyOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                energy = 1;

                energyOne.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_orange));
                energyZero.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_grey));
                energyTwo.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_grey));

            }
        });

        energyTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                energy = 2;

                energyTwo.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_orange));
                energyOne.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_grey));
                energyZero.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_grey));

            }
        });

        stomachAcheYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stomachAche = 1;

                stomachAcheYes.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_orange));
                stomachAcheNo.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_grey));

            }
        });

        stomachAcheNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stomachAche = 0;

                stomachAcheNo.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_orange));
                stomachAcheYes.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_grey));

            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validate()){

                    addMeal();

                }

            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chooseDateTime();

            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chooseDateTime();

            }
        });

    }

    private boolean validate() {

        if(tvDate.getText().toString().equals("Select date") || tvTime.getText().toString().equals("Select time") ||
        title.getText().toString().equals("") || mood == -1 || energy == -1 || stomachAche == -1){

            Toast.makeText(AddMealActivity.this, "Kindly choose/enter meal details!", Toast.LENGTH_SHORT).show();
            return false;

        }

        return true;
    }

    private void addMeal() {

        AddMealRequest request = new AddMealRequest();
        request.setChildId(Integer.parseInt(childId));
        request.setFoodDescription(descriptionOne.getText().toString() + descriptionTwo.getText().toString());
        request.setFoodName(title.getText().toString());
        request.setMood(mood);
        request.setStomachAche(stomachAche);
        request.setEnergy(energy);
        request.setTimestamp(timestamp);

        FoodInTolApplication.getApiUtility(AddMealActivity.this).AddMeal(AddMealActivity.this, true, request, new APIUtility.APIResponseListener<AddMealResponse>() {
            @Override
            public void onReceiveResponse(AddMealResponse response) {

                try{

                    Intent intent = new Intent(AddMealActivity.this, FoodDiaryActivity.class);

                    intent.putExtra("ChildId", childId);

                    startActivity(intent);

                }catch (Exception e){


                }

            }
        });

    }

    private void chooseDateTime() {

        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                final Calendar selectedDate = Calendar.getInstance();

                selectedDate.set(Calendar.YEAR, year);
                selectedDate.set(Calendar.MONTH, monthOfYear);
                selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                tvDate.setText("" + timestamp);

                new TimePickerDialog(AddMealActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        selectedDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        selectedDate.set(Calendar.MINUTE, minute);
                        selectedDate.set(Calendar.SECOND, 0);
                        selectedDate.set(Calendar.MILLISECOND, 0);

                        tvDate.setText(selectedDate.get(Calendar.DAY_OF_MONTH) + "-" + (selectedDate.get(Calendar.MONTH) + 1) + "-" +
                                selectedDate.get(Calendar.YEAR));

                        tvTime.setText(selectedDate.get(Calendar.HOUR_OF_DAY) + ":" + selectedDate.get(Calendar.MINUTE));

                        timestamp = selectedDate.getTimeInMillis();

                    }
                }, selectedDate.get(Calendar.HOUR_OF_DAY), selectedDate.get(Calendar.MINUTE), false).show();


            }
        };

        Calendar cal = Calendar.getInstance();

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DATE);

        DatePickerDialog dpDialog = new DatePickerDialog(AddMealActivity.this, R.style.DialogTheme, listener, year, month, day);
        dpDialog.show();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(AddMealActivity.this, FoodDiaryActivity.class);

        intent.putExtra("ChildId", childId);

        startActivity(intent);
        finish();

    }
}