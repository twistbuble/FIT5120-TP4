package com.app.foodintol.Models.LoadMeal;

import com.app.foodintol.Models.BaseResponse;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LoadMealResponse extends BaseResponse {

    @SerializedName("meal_list")
    private ArrayList<LoadMealResponseList> meals;

    public ArrayList<LoadMealResponseList> getMeals() {
        return meals;
    }
}
