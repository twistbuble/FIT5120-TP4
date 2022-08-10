package com.app.sunsafe.Retrofit.Helper;

import android.content.Context;
import android.util.Log;

import com.app.sunsafe.CommonUtils;
import com.app.sunsafe.PostUVDataRequest;
import com.app.sunsafe.PostUVDataResponse;
import com.app.sunsafe.R;
import com.app.sunsafe.Retrofit.APIService;

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
        APIServiceGenerator.addHeader("Content-Type", "application/json");
        mApiService = APIServiceGenerator.createService(APIService.class, "");

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

    public void PostUVData(final Context context, final boolean isDialog, PostUVDataRequest requestBody, final APIResponseListener<PostUVDataResponse> listener) {
        showDialog(context, isDialog);

        mApiService.PostUVData(requestBody).enqueue(new Callback<PostUVDataResponse>() {
            @Override
            public void onResponse(Call<PostUVDataResponse> call, Response<PostUVDataResponse> response) {
                dismissDialog(isDialog);
//                Log.e("TAG",response.code()+"");
                if (response.isSuccessful()) {

                    try{
                    listener.onReceiveResponse(response.body());

                    Log.e(response.body().getResult().getUvMax(), response.body().getResult().getUvMaxTime());

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
            public void onFailure(Call<PostUVDataResponse> call, Throwable t) {
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