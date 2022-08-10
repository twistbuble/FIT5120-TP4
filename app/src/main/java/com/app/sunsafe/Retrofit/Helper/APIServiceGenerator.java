package com.app.sunsafe.Retrofit.Helper;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIServiceGenerator {

    public static final String API_BASE_URL = "http://52.23.208.105:11730/api/";
    private static String BASE_URL;
    private static HashMap<String, String> headers = new HashMap<>();
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private static Gson gson = new GsonBuilder()
            .enableComplexMapKeySerialization()
            .serializeNulls()
            .setLenient()
            .setPrettyPrinting()
            .setVersion(1.0)
            .create();
    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson));
    private static Retrofit retrofit = builder.build();

    public static void addHeader(String key, String value) {
        headers.put(key, value);
    }

    protected static HashMap<String, String> getHeaders() {
        return headers;
    }

    public static void setBaseUrl(String url) {
        BASE_URL = url;
    }

    public static <S> S createService(
            Class<S> serviceClass, String username, String password) {
        if (!TextUtils.isEmpty(username)
                && !TextUtils.isEmpty(password)) {
            String authToken = Credentials.basic(username, password);
            return createService(serviceClass, authToken);
        }

        return createService(serviceClass, null);
    }

    public static <S> S createService(
            Class<S> serviceClass, final String authToken) {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        if (!TextUtils.isEmpty(authToken)) {
            AuthenticationInterceptor interceptor =
                    new AuthenticationInterceptor(authToken);

            if (!httpClient.interceptors().contains(interceptor))
                httpClient.addInterceptor(interceptor);
            if (!httpClient.interceptors().contains(httpLoggingInterceptor))
                httpClient.addInterceptor(httpLoggingInterceptor);
            httpClient.retryOnConnectionFailure(false);
            httpClient.connectTimeout(100, TimeUnit.SECONDS);
            httpClient.readTimeout(100, TimeUnit.SECONDS);
            httpClient.addNetworkInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request.Builder request = chain.request().newBuilder();

                    HashMap<String, String> headers = APIServiceGenerator.getHeaders();

                    Iterator iterator = headers.keySet().iterator();

                    while (iterator.hasNext()) {
                        String key = (String) iterator.next();
                        String value = headers.get(key);
                        request.addHeader(key, value);
                    }

                    request.method(chain.request().method(), chain.request().body());
                    return chain.proceed(request.build());
                }
            });

            builder.client(httpClient.build());
            //if (retrofit == null)
            retrofit = builder.build();


        }
        return retrofit.create(serviceClass);
    }
}