package com.app.sunsafe;

import com.google.gson.annotations.SerializedName;

public class PostUVDataResultResponse {

    @SerializedName("uv")
    private String uv;

    @SerializedName("uv_max")
    private String uvMax;

    @SerializedName("uv_max_time")
    private String uvMaxTime;

    public String getUv() {
        return uv;
    }

    public String getUvMax() {
        return uvMax;
    }

    public String getUvMaxTime() {
        return uvMaxTime;
    }
}
