package com.app.sunsafe;

import com.google.gson.annotations.SerializedName;

public class PostUVDataResponse {

    @SerializedName("result")
    private PostUVDataResultResponse result;

    @SerializedName("average_uv")
    private String averageUV;

    @SerializedName("current_season")
    private String currentSeason;

    @SerializedName("current_loc")
    private PostUVDataResultResponseCurrentLocation currentLocation;

    public PostUVDataResultResponse getResult() {
        return result;
    }

    public String getAverageUV() {
        return averageUV;
    }

    public String getCurrentSeason() {
        return currentSeason;
    }

    public PostUVDataResultResponseCurrentLocation getCurrentLocation() {
        return currentLocation;
    }
}
