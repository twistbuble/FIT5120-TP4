package com.app.sunsafe.Retrofit;

import com.app.sunsafe.PostUVDataRequest;
import com.app.sunsafe.PostUVDataResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers({"Content-Type: application/json"})
    @POST("get_current_uv_index_info")
    Call<PostUVDataResponse> PostUVData(@Body PostUVDataRequest requestBody);

}
