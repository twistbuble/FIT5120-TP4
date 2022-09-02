package com.app.foodintol.Models.AddMeal;

import com.google.gson.annotations.SerializedName;

public class AddMealRequest {

    @SerializedName("child_id")
    private int childId;

    @SerializedName("food_name")
    private String foodName;

    @SerializedName("food_description")
    private String foodDescription;

    @SerializedName("mood")
    private int mood;

    @SerializedName("energy")
    private int energy;

    @SerializedName("stomach_ache")
    private int stomachAche;

    @SerializedName("timestamp")
    private long timestamp;

    public void setChildId(int childId) {
        this.childId = childId;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public void setFoodDescription(String foodDescription) {
        this.foodDescription = foodDescription;
    }

    public void setMood(int mood) {
        this.mood = mood;
    }

    public void setStomachAche(int stomachAche) {
        this.stomachAche = stomachAche;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
