package ru.burtsev.imageviewer.rest.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AccessTokenInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder requestBuilder = chain.request().newBuilder();
        requestBuilder.header("Authorization", "Client-ID 0bd7304d669c2a900ff77e181962efac03911ab095bbb9ed817a92323ea30d63");
        return chain.proceed(requestBuilder.build());
    }
}
