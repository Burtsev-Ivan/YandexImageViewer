package ru.burtsev.imageviewer.rest;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.burtsev.imageviewer.rest.interceptor.AccessTokenInterceptor;
import ru.burtsev.imageviewer.rest.services.ApiService;

public class RestUtils {

    private static final long TIMEOUT = 15;
    private static final String BASE_URL = "https://api.unsplash.com";

    private static OkHttpClient okHttpClient;
    private static Retrofit retrofit;

    public static synchronized Retrofit getRetrofit() {
        if (retrofit == null) {
            initOkHttpClient();
            buildRetrofit(BASE_URL);
        }
        return retrofit;
    }

    private static void buildRetrofit(String url) {
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    private static void initOkHttpClient() {
        okHttpClient = new OkHttpClient.Builder()
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(new AccessTokenInterceptor())
                .build();
    }

    public static ApiService getApiService() {
        return getRetrofit().create(ApiService.class);
    }
}
