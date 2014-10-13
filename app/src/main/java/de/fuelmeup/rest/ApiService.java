package de.fuelmeup.rest;


import java.util.List;

import de.fuelmeup.rest.model.Car;
import de.fuelmeup.rest.model.GasStation;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

public interface ApiService {

    public final String FUEL_ME_UP_BASE_URL = "http://fuel-me-up.herokuapp.com";

    @GET("/vehicles/{city}")
    Observable<List<Car>> fetchVehiclesForCity(@Path("city") String city, @Query("max_fuel_level") int maxFuelLevel);

    @GET("/vehicles")
    Observable<List<Car>> fetchVehicles(@Query("max_fuel_level") int maxFuelLevel);


    @GET("/gasstations")
    Observable<List<GasStation>> fetchGasStations();
}
