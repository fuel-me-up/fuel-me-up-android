package de.fuelmeup;

import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.fuelmeup.data.DataModule;
import de.fuelmeup.rest.ApiService;
import de.fuelmeup.rest.MockApiService;
import de.fuelmeup.rest.model.Car;
import de.fuelmeup.rest.model.Coordinate;
import de.fuelmeup.rest.model.GasStation;
import rx.Observable;

import static org.mockito.AdditionalMatchers.gt;
import static org.mockito.AdditionalMatchers.leq;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
        return getMockApiSerivce();
    }

    private ApiService getMockApiSerivce() {
        //Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
        ApiService mockApiService = mock(ApiService.class);

        List<Car> carList = new ArrayList<>();
        carList.add(new Car("Biedermannplatz 18", 14, new Coordinate(12, 12), "HH-IJ-1901", "car2go"));
        carList.add(new Car("Biedermannplatz 18", 7, new Coordinate(12.5, 12.1), "HH-IJ-1901", "car2go"));
        carList.add(new Car("Biedermannplatz 18", 3, new Coordinate(12.3, 12.2), "HH-IJ-1901", "car2go"));

        List<Car> otherCarList = new ArrayList<>();
        carList.add(new Car("Biedermannplatz 18", 33, new Coordinate(12, 12), "HH-IJ-1901", "car2go"));


        when(mockApiService.fetchVehicles(anyString(), leq(20))).thenReturn(Observable.just(carList));
        when(mockApiService.fetchVehicles(anyString(), gt(20))).thenReturn(Observable.just(otherCarList));

        when(mockApiService.fetchGasStations(anyString())).thenReturn(Observable.just(new ArrayList<GasStation>()));


        return mockApiService;
    }
}