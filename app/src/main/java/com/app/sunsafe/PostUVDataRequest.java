package com.app.sunsafe;

import com.google.gson.annotations.SerializedName;

public class PostUVDataRequest {

    @SerializedName("lat")
    private String lat;

    @SerializedName("lng")
    private String lng;

    @SerializedName("alt")
    private String alt;

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }
}
