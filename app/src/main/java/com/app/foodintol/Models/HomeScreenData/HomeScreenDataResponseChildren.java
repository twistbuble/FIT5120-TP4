package com.app.foodintol.Models.HomeScreenData;

import com.google.gson.annotations.SerializedName;

public class HomeScreenDataResponseChildren {

    @SerializedName("child_id")
    private String id;

    @SerializedName("child_name")
    private String name;

    @SerializedName("child_gender")
    private int gender;

    @SerializedName("child_age")
    private String age;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getGender() {
        return gender;
    }

    public String getAge() {
        return age;
    }
}
