package de.fuelmeup.rest;

import java.util.ArrayList;
import java.util.List;

import de.fuelmeup.rest.model.Car;
import de.fuelmeup.rest.model.Coordinate;
import de.fuelmeup.rest.model.GasStation;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by jonas on 11.10.14.
 */
public class MockApiService implements ApiService {
    @Override
    public Observable<List<Car>> fetchVehicles(@Path("city") String city, @Query("max_fuel_level") int maxFuelLevel) {
        List<Car> carList = new ArrayList<>();
        carList.add(new Car("Biedermannplatz 18", 14, new Coordinate(12, 12), "HH-IJ-1901", "car2go"));
        carList.add(new Car("Biedermannplatz 18", 14, new Coordinate(12.5, 12.1), "HH-IJ-1901", "car2go"));
        carList.add(new Car("Biedermannplatz 18", 14, new Coordinate(12.3, 12.2), "HH-IJ-1901", "car2go"));

        return Observable.just(carList);
    }

    @Override
    public Observable<List<GasStation>> fetchGasStations(@Path("city") String city) {
        List<GasStation> gasStations = new ArrayList<>();
        return Observable.just(gasStations);
    }
}
