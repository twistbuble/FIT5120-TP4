package com.app.foodintol.Models.AddChild;

import com.google.gson.annotations.SerializedName;

public class AddChildRequest {

    @SerializedName("gender")
    private int gender;

    @SerializedName("name")
    private String name;

    @SerializedName("age")
    private String age;

    @SerializedName("intolerance")
    private String intolerance;

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setIntolerance(String intolerance) {
        this.intolerance = intolerance;
    }
}
