package com.app.foodintol.Models.Login;

import com.app.foodintol.Models.BaseResponse;
import com.google.gson.annotations.SerializedName;

public class LoginResponse extends BaseResponse {

    @SerializedName("username")
    private String username;

    public String getUsername() {
        return username;
    }
}
