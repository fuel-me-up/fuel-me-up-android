package de.fuelmeup.rest;

import android.app.Activity;
import android.os.AsyncTask;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.fuelmeup.rest.model.Car;
import de.fuelmeup.rest.model.GasStation;
import rx.Subscription;
import rx.android.observables.AndroidObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


@Singleton
public class RestClient {

    @Inject
    ApiService apiService;

    public Subscription fetchCarsInHamburg(int maxFuelLevel, Activity activity, Action1<List<Car>> onComplete, Action1<Throwable> onError) {
        return AndroidObservable.
                bindActivity(activity, apiService.fetchVehicles("hamburg", maxFuelLevel)).
                observeOn(AndroidSchedulers.mainThread()).
                subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR)).
                subscribe(onComplete, onError);
    }

    public Subscription fetchCarsInHamburg(int maxFuelLevel, Action1<List<Car>> onComplete, Action1<Throwable> onError) {
        return apiService.fetchVehicles("hamburg", maxFuelLevel).
                observeOn(AndroidSchedulers.mainThread()).
                subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR)).
                subscribe(onComplete, onError);
    }

    public Subscription fetchGasStationsInHamburg(Activity activity, Action1<List<GasStation>> onComplete, Action1<Throwable> onError) {
        return AndroidObservable.bindActivity(activity, apiService.fetchGasStations("hamburg")).
                observeOn(AndroidSchedulers.mainThread()).
                subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR)).
                subscribe(onComplete, onError);
    }
}
