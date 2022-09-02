package com.app.foodintol.Models.LoadMeal;

import com.google.gson.annotations.SerializedName;

public class LoadMealResponseList {

    @SerializedName("meal_id")
    private String meal_id;

    @SerializedName("meal_food_name")
    private String title;

    @SerializedName("meal_datetime")
    private String time;

    @SerializedName("meal_food_description")
    private String description;

    @SerializedName("meal_mood")
    private int mood;

    @SerializedName("meal_energy")
    private int energy;

    @SerializedName("stomach_ache")
    private int stomachAche;

    public String getMeal_id() {
        return meal_id;
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public int getMood() {
        return mood;
    }

    public int getEnergy() {
        return energy;
    }

    public int getStomachAche() {
        return stomachAche;
    }
}
