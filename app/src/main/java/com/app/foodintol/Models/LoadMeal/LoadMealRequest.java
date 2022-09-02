package com.app.foodintol.Models.LoadMeal;

import com.google.gson.annotations.SerializedName;

public class LoadMealRequest {

    @SerializedName("timestamp")
    private long timestamp;

    @SerializedName("child_id")
    private String id;

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setId(String id) {
        this.id = id;
    }
}
