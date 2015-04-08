package de.fuelmeup.rest;

import android.os.AsyncTask;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.fuelmeup.rest.model.Car;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


@Singleton
public class RestClient {

    @Inject
    ApiService apiService;

    public Subscription fetchCars(int maxFuelLevel, Action1<List<Car>> onComplete, Action1<Throwable> onError) {
        return apiService.fetchVehicles(maxFuelLevel).
                observeOn(AndroidSchedulers.mainThread()).
                subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR)).
                subscribe(onComplete, onError);
    }
}
