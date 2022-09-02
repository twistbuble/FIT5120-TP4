package com.app.foodintol.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.foodintol.MainActivity;
import com.app.foodintol.Models.HomeScreenData.HomeScreenDataResponse;
import com.app.foodintol.Models.HomeScreenData.HomeScreenDataResponseChildren;
import com.app.foodintol.Models.LoadMeal.LoadMealRequest;
import com.app.foodintol.Models.LoadMeal.LoadMealResponse;
import com.app.foodintol.Models.LoadMeal.LoadMealResponseList;
import com.app.foodintol.R;
import com.app.foodintol.Retrofit.FoodInTolApplication;
import com.app.foodintol.Retrofit.Helper.APIUtility;

import java.util.ArrayList;
import java.util.Calendar;

public class FoodDiaryActivity extends AppCompatActivity implements MealAdapter.ChildAdapterCallback{

    private String childId = "";
    private long timestamp = 0;
    private RelativeLayout date;
    private TextView tvDate;
    private RecyclerView rvMeals;
    private MealAdapter mealAdapter;
    private ArrayList<LoadMealResponseList> mealList;
    private ImageView addFood;
    private Calendar chosenDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_diary);

        date = findViewById(R.id.rl_date);
        rvMeals = findViewById(R.id.rv_meals);
        addFood = findViewById(R.id.iv_add_food);
        tvDate = findViewById(R.id.tv_date);

        mealList = new ArrayList<>();

        try{

            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            if(extras.containsKey("ChildId")) {
                childId = intent.getStringExtra("ChildId");
            }

        }catch (Exception e){


        }

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        Calendar selectedDate = Calendar.getInstance();

                        selectedDate.set(Calendar.YEAR, year);
                        selectedDate.set(Calendar.MONTH, monthOfYear);
                        selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        selectedDate.set(Calendar.HOUR_OF_DAY, 23);
                        selectedDate.set(Calendar.MINUTE, 00);
                        selectedDate.set(Calendar.SECOND, 00);
                        selectedDate.set(Calendar.MILLISECOND, 01);

                        timestamp = selectedDate.getTimeInMillis();

                        chosenDate = selectedDate;

                        tvDate.setText(selectedDate.get(Calendar.DAY_OF_MONTH) + "-" + (selectedDate.get(Calendar.MONTH) + 1) + "-" +
                                selectedDate.get(Calendar.YEAR));


                        Log.e("FOODDIARYACTIVITY:", "timestamp before: " + selectedDate.getTimeInMillis());

                        loadMealsList(selectedDate.getTimeInMillis());

                    }
                };

                Calendar cal = Calendar.getInstance();

                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DATE);

                DatePickerDialog dpDialog = new DatePickerDialog(FoodDiaryActivity.this, R.style.DialogTheme, listener, year, month, day);
                dpDialog.show();

            }
        });

        addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(FoodDiaryActivity.this, AddMealActivity.class);

                intent.putExtra("ChildId", childId);

                startActivity(intent);

            }
        });

    }

    private void loadMealsList(long timestamp) {

        LoadMealRequest request = new LoadMealRequest();

        Log.e("FOODDIARYACTIVITY:", "childId: " + childId);
        Log.e("FOODDIARYACTIVITY:", "timestamp: " + timestamp);

        request.setId(childId);
        request.setTimestamp(timestamp);

        FoodInTolApplication.getApiUtility(FoodDiaryActivity.this).LoadMeal(FoodDiaryActivity.this, true, request, new APIUtility.APIResponseListener<LoadMealResponse>() {
            @Override
            public void onReceiveResponse(LoadMealResponse response) {

                try{

                    ArrayList<LoadMealResponseList> finalList = new ArrayList<>();

                    Log.e("FOODDIARYACTIVITY:", "size: " + response.getMeals().size());

                    if(response.getMeals().size() > 0){

                        Log.e("FOODDIARYACTIVITY:", "title: " + response.getMeals().get(0).getTitle());

                        finalList = response.getMeals();

                        mealAdapter = new MealAdapter(finalList, FoodDiaryActivity.this);
                        rvMeals.setHasFixedSize(true);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(FoodDiaryActivity.this);
                        rvMeals.setLayoutManager(layoutManager);
                        rvMeals.setAdapter(mealAdapter);

                    }else{

                        Toast.makeText(FoodDiaryActivity.this, "No meals recorded on this day!", Toast.LENGTH_SHORT).show();

                    }

                }catch (Exception e){

                    Toast.makeText(FoodDiaryActivity.this, "No meals recorded on this day!", Toast.LENGTH_SHORT).show();

//                    Toast.makeText(FoodDiaryActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    @Override
    public void onMealClick(LoadMealResponseList data, int pos) {

        Log.e("FoodDiaryActivity:", "mealId: " + data.getMeal_id());

        Intent i = new Intent(FoodDiaryActivity.this, ViewMealActivity.class);

        i.putExtra("mealId", data.getMeal_id());
        i.putExtra("timestamp", "" + timestamp);
        i.putExtra("ChildId", childId);

        startActivity(i);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(FoodDiaryActivity.this, MainActivity.class));
        finish();

    }
}