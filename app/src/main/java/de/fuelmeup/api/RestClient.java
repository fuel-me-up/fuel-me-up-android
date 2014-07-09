package de.fuelmeup.api;

import android.app.Activity;
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
import java.util.List;

import de.fuelmeup.BuildConfig;
import de.fuelmeup.api.model.Car;
import de.fuelmeup.api.model.GasStation;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.Converter;
import retrofit.converter.GsonConverter;
import rx.Subscription;
import rx.android.observables.AndroidObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by jonas on 7/9/14.
 */
public class RestClient {

    public static final int RESPONSE_CACHE_LIMIT = 1 * 1024 * 1024;
    private static final String LOG_TAG = RestClient.class.getSimpleName();

    /**
     * For security reasons in production do not log REST Calls
     */
    private final RestAdapter.LogLevel logLevel = BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE;

    private RestAPI restAPI;

    public RestClient(Context context) {

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
                        setEndpoint(RestAPI.FUEL_ME_UP_BASE_URL).
                        setClient(okClient).setConverter(httpConverter).
                        setLogLevel(logLevel).build();

        restAPI = rest.create(RestAPI.class);

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

    public Subscription fetchCarsInHamburg(int maxFuelLevel, Activity activity, Action1<List<Car>> onComplete, Action1<Throwable> onError) {
        return AndroidObservable.
                bindActivity(activity, restAPI.fetchVehicles("hamburg", maxFuelLevel)).
                observeOn(AndroidSchedulers.mainThread()).
                subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR)).
                subscribe(onComplete, onError);
    }

    public Subscription fetchGasStationsInHamburg(Activity activity, Action1<List<GasStation>> onComplete, Action1<Throwable> onError) {
        return AndroidObservable.bindActivity(activity, restAPI.fetchGasStations("hamburg")).
                observeOn(AndroidSchedulers.mainThread()).
                subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR)).
                subscribe(onComplete, onError);
    }
}
