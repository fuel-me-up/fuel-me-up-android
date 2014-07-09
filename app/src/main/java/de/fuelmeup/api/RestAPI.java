package de.fuelmeup.api;


import java.util.List;

import de.fuelmeup.api.model.Car;
import de.fuelmeup.api.model.GasStation;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by jonas on 7/9/14.
 */
public interface RestAPI {

    public final String FUEL_ME_UP_BASE_URL = "http://fuel-me-up.herokuapp.com";

    @GET("/vehicles/{city}")
    Observable<List<Car>> fetchVehicles(@Path("city") String city, @Query("max_fuel_level") int maxFuelLevel);

    @GET("/gasstations/{city}")
    Observable<List<GasStation>> fetchGasStations(@Path("city") String city);
}
