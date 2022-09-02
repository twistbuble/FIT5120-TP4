package com.app.foodintol.Retrofit;

import com.app.foodintol.Models.AddChild.AddChildRequest;
import com.app.foodintol.Models.AddChild.AddChildResponse;
import com.app.foodintol.Models.AddMeal.AddMealRequest;
import com.app.foodintol.Models.AddMeal.AddMealResponse;
import com.app.foodintol.Models.DeleteChild.DeleteChildRequest;
import com.app.foodintol.Models.DeleteChild.DeleteChildResponse;
import com.app.foodintol.Models.HomeScreenData.HomeScreenDataResponse;
import com.app.foodintol.Models.LoadMeal.LoadMealRequest;
import com.app.foodintol.Models.LoadMeal.LoadMealResponse;
import com.app.foodintol.Models.Login.LoginRequest;
import com.app.foodintol.Models.Login.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers({"Content-Type: application/json"})
    @POST("login")
    Call<LoginResponse> Login(@Body LoginRequest requestBody);

    @Headers({"Content-Type: application/json"})
    @GET("get_children")
    Call<HomeScreenDataResponse> HomeScreenData();

    @Headers({"Content-Type: application/json"})
    @POST("add_child")
    Call<AddChildResponse> AddChild(@Body AddChildRequest requestBody);

    @Headers({"Content-Type: application/json"})
    @POST("del_child")
    Call<DeleteChildResponse> DeleteChild(@Body DeleteChildRequest requestBody);

    @Headers({"Content-Type: application/json"})
    @POST("get_diary")
    Call<LoadMealResponse> LoadMeal(@Body LoadMealRequest requestBody);

    @Headers({"Content-Type: application/json"})
    @POST("add_meal_to_diary")
    Call<AddMealResponse> AddMeal(@Body AddMealRequest requestBody);

}
