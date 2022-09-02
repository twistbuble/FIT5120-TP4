package com.app.foodintol.Retrofit.Helper;

import android.content.Context;
import android.util.Log;

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
import com.app.foodintol.R;
import com.app.foodintol.Retrofit.APIService;
import com.app.foodintol.Utils.CommonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class APIUtility {
    private static final String TAG = "APIUtility";
    private static APIService mApiService = null;
    private static APIService mApiServiceLogin = null;

    public APIUtility(Context context) {
        APIServiceGeneratorUnsafeRequests.addHeader("Content-Type", "application/json");
        mApiService = APIServiceGeneratorUnsafeRequests.createService(APIService.class, "");

    }

    public void showDialog(Context context, boolean isDialog) {
        if (isDialog) {
            ProcessDialog.start(context);
        }
    }

    public void dismissDialog(boolean isDialog) {
        if (isDialog) {
            ProcessDialog.dismiss();
        }
    }

    public void Login(final Context context, final boolean isDialog, LoginRequest requestBody, final APIResponseListener<LoginResponse> listener) {
        showDialog(context, isDialog);

        mApiService.Login(requestBody).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                dismissDialog(isDialog);
//                Log.e("TAG",response.code()+"");
                if (response.isSuccessful()) {

                    try{
                    listener.onReceiveResponse(response.body());

//                    Log.e(response.body().getResult().getUvMax(), response.body().getResult().getUvMaxTime());

                    }catch (Exception e){

                        CommonUtils.alert(context, context.getString(R.string.error));

                    }

                } else {
                    if (response.code() == 403){

                        CommonUtils.alert(context, context.getString(R.string.error));

                    }
                    else{
                        CommonUtils.alert(context, context.getString(R.string.error));
//                        Log.e(TAG, response.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                dismissDialog(isDialog);
//                CommonUtils.alert(context, context.getString(R.string.error));
                CommonUtils.alert(context, t.getMessage());
//                Log.e(TAG, t.getMessage());
            }
        });
    }

    public void HomeScreenData(final Context context, final boolean isDialog, final APIResponseListener<HomeScreenDataResponse> listener) {
        showDialog(context, isDialog);

        mApiService.HomeScreenData().enqueue(new Callback<HomeScreenDataResponse>() {
            @Override
            public void onResponse(Call<HomeScreenDataResponse> call, Response<HomeScreenDataResponse> response) {
                dismissDialog(isDialog);
//                Log.e("TAG",response.code()+"");
                if (response.isSuccessful()) {

                    try{
                        listener.onReceiveResponse(response.body());

//                    Log.e(response.body().getResult().getUvMax(), response.body().getResult().getUvMaxTime());

                    }catch (Exception e){

                        CommonUtils.alert(context, context.getString(R.string.error));

                    }

                } else {
                    if (response.code() == 403){

                        CommonUtils.alert(context, context.getString(R.string.error));

                    }
                    else{
                        CommonUtils.alert(context, context.getString(R.string.error));
//                        Log.e(TAG, response.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<HomeScreenDataResponse> call, Throwable t) {
                dismissDialog(isDialog);
//                CommonUtils.alert(context, context.getString(R.string.error));
                CommonUtils.alert(context, t.getMessage());
//                Log.e(TAG, t.getMessage());
            }
        });
    }

    public void AddChild(final Context context, final boolean isDialog, AddChildRequest requestBody, final APIResponseListener<AddChildResponse> listener) {
        showDialog(context, isDialog);

        mApiService.AddChild(requestBody).enqueue(new Callback<AddChildResponse>() {
            @Override
            public void onResponse(Call<AddChildResponse> call, Response<AddChildResponse> response) {
                dismissDialog(isDialog);
//                Log.e("TAG",response.code()+"");
                if (response.isSuccessful()) {

                    try{
                        listener.onReceiveResponse(response.body());

//                    Log.e(response.body().getResult().getUvMax(), response.body().getResult().getUvMaxTime());

                    }catch (Exception e){

                        CommonUtils.alert(context, context.getString(R.string.error));

                    }

                } else {
                    if (response.code() == 403){

                        CommonUtils.alert(context, context.getString(R.string.error));

                    }
                    else{
                        CommonUtils.alert(context, context.getString(R.string.error));
//                        Log.e(TAG, response.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<AddChildResponse> call, Throwable t) {
                dismissDialog(isDialog);
//                CommonUtils.alert(context, context.getString(R.string.error));
                CommonUtils.alert(context, t.getMessage());
//                Log.e(TAG, t.getMessage());
            }
        });
    }

    public void DeleteChild(final Context context, final boolean isDialog, DeleteChildRequest requestBody, final APIResponseListener<DeleteChildResponse> listener) {
        showDialog(context, isDialog);

        mApiService.DeleteChild(requestBody).enqueue(new Callback<DeleteChildResponse>() {
            @Override
            public void onResponse(Call<DeleteChildResponse> call, Response<DeleteChildResponse> response) {
                dismissDialog(isDialog);
//                Log.e("TAG",response.code()+"");
                if (response.isSuccessful()) {

                    try{
                        listener.onReceiveResponse(response.body());

//                    Log.e(response.body().getResult().getUvMax(), response.body().getResult().getUvMaxTime());

                    }catch (Exception e){

                        CommonUtils.alert(context, context.getString(R.string.error));

                    }

                } else {
                    if (response.code() == 403){

                        CommonUtils.alert(context, context.getString(R.string.error));

                    }
                    else{
                        CommonUtils.alert(context, context.getString(R.string.error));
//                        Log.e(TAG, response.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<DeleteChildResponse> call, Throwable t) {
                dismissDialog(isDialog);
//                CommonUtils.alert(context, context.getString(R.string.error));
                CommonUtils.alert(context, t.getMessage());
//                Log.e(TAG, t.getMessage());
            }
        });
    }

    public void LoadMeal(final Context context, final boolean isDialog, LoadMealRequest requestBody, final APIResponseListener<LoadMealResponse> listener) {
        showDialog(context, isDialog);

        mApiService.LoadMeal(requestBody).enqueue(new Callback<LoadMealResponse>() {
            @Override
            public void onResponse(Call<LoadMealResponse> call, Response<LoadMealResponse> response) {
                dismissDialog(isDialog);
//                Log.e("TAG",response.code()+"");
                if (response.isSuccessful()) {

                    try{
                        listener.onReceiveResponse(response.body());

//                    Log.e(response.body().getResult().getUvMax(), response.body().getResult().getUvMaxTime());

                    }catch (Exception e){

                        CommonUtils.alert(context, context.getString(R.string.error));

                    }

                } else {
                    if (response.code() == 403){

                        CommonUtils.alert(context, context.getString(R.string.error));

                    }
                    else{
                        CommonUtils.alert(context, context.getString(R.string.error));
//                        Log.e(TAG, response.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<LoadMealResponse> call, Throwable t) {
                dismissDialog(isDialog);
//                CommonUtils.alert(context, context.getString(R.string.error));
                CommonUtils.alert(context, t.getMessage());
//                Log.e(TAG, t.getMessage());
            }
        });
    }

    public void AddMeal(final Context context, final boolean isDialog, AddMealRequest requestBody, final APIResponseListener<AddMealResponse> listener) {
        showDialog(context, isDialog);

        mApiService.AddMeal(requestBody).enqueue(new Callback<AddMealResponse>() {
            @Override
            public void onResponse(Call<AddMealResponse> call, Response<AddMealResponse> response) {
                dismissDialog(isDialog);
//                Log.e("TAG",response.code()+"");
                if (response.isSuccessful()) {

                    try{
                        listener.onReceiveResponse(response.body());

//                    Log.e(response.body().getResult().getUvMax(), response.body().getResult().getUvMaxTime());

                    }catch (Exception e){

                        CommonUtils.alert(context, context.getString(R.string.error));

                    }

                } else {
                    if (response.code() == 403){

                        CommonUtils.alert(context, context.getString(R.string.error));

                    }
                    else{
                        CommonUtils.alert(context, context.getString(R.string.error));
//                        Log.e(TAG, response.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<AddMealResponse> call, Throwable t) {
                dismissDialog(isDialog);
//                CommonUtils.alert(context, context.getString(R.string.error));
                CommonUtils.alert(context, t.getMessage());
//                Log.e(TAG, t.getMessage());
            }
        });
    }

    public interface APIResponseListener<T> {
        void onReceiveResponse(T response);
        /* void onNetworkFailed();*/
    }

    private void displayErrorMessage(String errorBody, Context context) {

        try {
            if (errorBody != null) {
                JSONObject object = new JSONObject(errorBody);
                CommonUtils.alert(context, object.getString("message"));
            } else {
                CommonUtils.alert(context, context.getString(R.string.error));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            CommonUtils.alert(context, context.getString(R.string.error));
        }

    }

}