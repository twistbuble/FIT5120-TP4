package com.app.sunsafe;

import com.google.gson.annotations.SerializedName;

public class PostUVDataResultResponseCurrentLocation {

    @SerializedName("road")
    private String road;

    @SerializedName("suburb")
    private String suburb;

    @SerializedName("city")
    private String city;

    @SerializedName("state")
    private String state;

    public String getRoad() {
        return road;
    }

    public String getSuburb() {
        return suburb;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }
}
