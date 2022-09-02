package com.app.foodintol.Models.Login;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {

    @SerializedName("username")
    private String email;

    @SerializedName("password")
    private String password;

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
