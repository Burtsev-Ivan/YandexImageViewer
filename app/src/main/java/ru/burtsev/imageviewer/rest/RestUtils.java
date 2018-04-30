package ru.burtsev.imageviewer.rest;

import android.os.Build;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;

import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.TlsVersion;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.burtsev.imageviewer.rest.interceptor.AccessTokenInterceptor;
import ru.burtsev.imageviewer.rest.services.ApiService;

/**
 * Класс для настройки Retrofit и OkHttp3.
 */
public class RestUtils {

    private static final long TIMEOUT = 15;
    private static final String BASE_URL = "https://api.unsplash.com";

    private static OkHttpClient okHttpClient;
    private static Retrofit retrofit;

    private static synchronized Retrofit getRetrofit() {
        if (retrofit == null) {
            buildOkHttpClient();
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

    private static void buildOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(new AccessTokenInterceptor());
        okHttpClient = enableTls12OnPreLollipop(builder)
                .build();

    }

    /**
     * Метод позволяющий использовать TLSv1.2 в андроиде младше api 22.
     * Без него вылетает  SSLProtocolException
     */
    private static OkHttpClient.Builder enableTls12OnPreLollipop(OkHttpClient.Builder client) {
        if (Build.VERSION.SDK_INT >= 16 && Build.VERSION.SDK_INT < 22) {
            try {
                SSLContext sc = SSLContext.getInstance("TLSv1.2");
                sc.init(null, null, null);
                client.sslSocketFactory(new Tls12SocketFactory(sc.getSocketFactory()));

                ConnectionSpec cs = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                        .tlsVersions(TlsVersion.TLS_1_2)
                        .build();

                List<ConnectionSpec> specs = new ArrayList<>();
                specs.add(cs);
                specs.add(ConnectionSpec.COMPATIBLE_TLS);
                specs.add(ConnectionSpec.CLEARTEXT);

                client.connectionSpecs(specs);
            } catch (Exception exc) {
                Log.e("OkHttpTLSCompat", "Error while setting TLS 1.2", exc);
            }
        }

        return client;
    }

    public static ApiService getApiService() {
        return getRetrofit().create(ApiService.class);
    }
}
