package de.fuelmeup.ui.presenter;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import de.fuelmeup.preferences.FuelLevelPreference;
import de.fuelmeup.preferences.IntegerPreference;
import de.fuelmeup.resources.CarMarkerTitleFormat;
import de.fuelmeup.resources.FuelLevelString;
import de.fuelmeup.resources.StringResource;
import de.fuelmeup.rest.RestClient;
import de.fuelmeup.rest.model.Car;
import de.fuelmeup.ui.view.CarMapView;
import de.fuelmeup.ui.model.Marker;

/**
 * Created by jonas on 06.10.14.
 */
public class CarMapPresenterImpl implements CarMapPresenter {

    public static final String MAPS_NAVIGATION_URL = "http://maps.google.com/maps?&daddr=%s,%s";

    @Inject
    @CarMarkerTitleFormat
    StringResource format;

    @Inject
    @FuelLevelString
    StringResource fuelString;

    @Inject
    @FuelLevelPreference
    IntegerPreference fuelLevelPreference;

    @Inject
    CarMapView carMapView;


    @Inject
    RestClient restClient;

    @Override
    public void onResume() {
        final int initialFuelLevel = fuelLevelPreference.get(25);
        carMapView.setFuelLevel(initialFuelLevel);
        restClient.fetchCars(initialFuelLevel, cars -> displayCars(cars), error -> {
        });
    }

    private void displayCars(List<Car> cars) {
        List<Marker> markers = new ArrayList<>();

        for (Car car : cars) {
            final LatLng position = new LatLng(car.coordinate.latitude,
                    car.coordinate.longitude);
            final float hue;
            if (car.provider.equals(Car.PROVIDER_C2G)) {
                hue = BitmapDescriptorFactory.HUE_AZURE;
            } else {
                hue = BitmapDescriptorFactory.HUE_VIOLET;
            }

            final String title = String.format(format.get(), car.provider, car.licensePlate);

            final String snippet = String.format(fuelString.get() + car.fuelLevel);

            markers.add(new Marker(position, title, snippet, hue));
        }

        carMapView.drawMarkers(markers);
    }

    @Override
    public void onMarkerClicked(Marker marker) {
        sendIntentForNavigation(marker.position);
    }

    @Override
    public void loadCarsForFuelLevel(int level) {
        fuelLevelPreference.set(level);
        restClient.fetchCars(level, cars -> displayCars(cars), error -> {
        });
    }

    private void sendIntentForNavigation(LatLng location) {
        String uri = String.format(MAPS_NAVIGATION_URL, location.latitude, location.longitude);
        carMapView.startViewIntentWithStringUri(uri);
    }
}
