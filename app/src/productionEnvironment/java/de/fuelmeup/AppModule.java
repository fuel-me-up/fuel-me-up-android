package de.fuelmeup;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.HttpResponseCache;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.io.IOException;
import java.net.ResponseCache;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.fuelmeup.data.DataModule;
import de.fuelmeup.rest.ApiService;
import de.fuelmeup.rest.MockApiService;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.Converter;
import retrofit.converter.GsonConverter;

@Module(
        injects = {
                App.class,
        },
        includes = {
                DataModule.class
        },
        library = true
)
public class AppModule {

    /**
     * For security reasons in production do not log REST Calls
     */
    private final RestAdapter.LogLevel logLevel = BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE;

    public static final int RESPONSE_CACHE_LIMIT = 1 * 1024 * 1024;
    private static final String LOG_TAG = AppModule.class.getSimpleName();

    private final App app;

    public AppModule(App app) {
        this.app = app;
    }


    @Provides
    @Singleton
    Application provideApplication() {
        return app;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return app.getApplicationContext();
    }


    @Provides
    @Singleton
    public ApiService provideApiService(Context context) {
        OkHttpClient client = new OkHttpClient();
        client.setResponseCache(createResponseCache(context));
        //
        OkClient okClient = new OkClient(client);

        // init http converter
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        Converter httpConverter = new GsonConverter(gson);

        RestAdapter rest = new RestAdapter.Builder()
                .setRequestInterceptor(request -> {
                    request.addHeader("Accept", "application/json");
                    request.addHeader("Content-Type", "application/json");
                }).
                        setExecutors(AsyncTask.THREAD_POOL_EXECUTOR, AsyncTask.THREAD_POOL_EXECUTOR).
                        setEndpoint(ApiService.FUEL_ME_UP_BASE_URL).
                        setClient(okClient).setConverter(httpConverter).
                        setLogLevel(logLevel).build();

        return new rest.create(ApiService.class);
    }

    private ResponseCache createResponseCache(Context context) {
        try {
            File httpCacheDir = new File(context.getCacheDir(), "http");
            return new HttpResponseCache(httpCacheDir, RESPONSE_CACHE_LIMIT);
        } catch (IOException e) {
            Log.i(LOG_TAG, "HTTP response cache installation failed:" + e);
        }
        return ResponseCache.getDefault();
    }
}