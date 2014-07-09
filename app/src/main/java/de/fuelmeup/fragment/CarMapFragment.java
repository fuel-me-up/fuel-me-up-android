package de.fuelmeup.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import de.fuelmeup.R;
import de.fuelmeup.api.RestClient;
import de.fuelmeup.api.model.Car;
import de.fuelmeup.api.model.GasStation;

/**
 * Fragment that displays cars in map.
 *
 * @author jonas
 */
public class CarMapFragment extends MapFragment implements OnMyLocationChangeListener, BaseFragment {

    private static final String LOG_TAG = CarMapFragment.class.getSimpleName();
    private static final String MAPS_NAVIGATION_URL = "http://maps.google.com/maps?&daddr=%s,%s";
    public static final int DEFAULT_ZOOM_LEVEL = 13;
    public static final int MAX_NO_OF_PROVIDERS = 2;
    private boolean notLocalized = true;

    private RestClient restClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restClient = new RestClient(getActivity());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
        initMap();
        refreshMap();
    }

    private void initMap() {
        getMap().getUiSettings().setMyLocationButtonEnabled(true);
        getMap().setMyLocationEnabled(true);
        if (notLocalized)
            getMap().setOnMyLocationChangeListener(this);
        notLocalized = false;
        getMap().setOnInfoWindowClickListener(marker -> {
            LatLng markerPosition = marker.getPosition();
            sendIntentForNavigation(markerPosition);
        });
    }

    private void sendIntentForNavigation(LatLng location) {
        String uri = String.format(MAPS_NAVIGATION_URL, location.latitude, location.longitude);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);
    }

    private void refreshMap() {
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(getActivity());
        int defaultMaxFuelLevel = Integer
                .parseInt(getString(R.string.default_max_fuel_level));
        int maxFuelLevelC2G = settings.getInt(
                getString(R.string.max_fuel_preference),
                defaultMaxFuelLevel);

        getMap().clear();
        restClient.fetchCarsInHamburg(maxFuelLevelC2G, getActivity(),
                cars -> drawCars(cars), throwable -> Log.d(LOG_TAG, "", throwable));

        restClient.fetchGasStationsInHamburg(getActivity(),
                gastStations -> drawGasStations(gastStations), throwable -> Log.d(LOG_TAG, "", throwable));
    }

    private void drawCars(List<Car> cars) {
        if (getMap() == null) {
            return;
        }

        for (Car car : cars) {
            LatLng position = new LatLng(car.coordinate.latitude,
                    car.coordinate.longitude);
            MarkerOptions carMarker = new MarkerOptions().position(
                    position).title(car.licensePlate);
            if (car.provider.equals(Car.PROVIDER_C2G)) {
                carMarker.icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            } else if (car.provider.equals(Car.PROVIDER_DN)) {
                carMarker.icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
            }

            String providerString = getString(R.string.provider_and_plate);
            carMarker.title(String.format(providerString, car.provider, car.licensePlate))
                    .snippet(getString(R.string.fuel_label) + car.fuelLevel);
            getMap().addMarker(carMarker);
        }
    }

    private void drawGasStations(List<GasStation> gasStations) {
        if (getMap() == null) {
            return;
        }

        for (GasStation gasStation : gasStations) {
            LatLng position = new LatLng(gasStation.coordinate.latitude,
                    gasStation.coordinate.longitude);
            MarkerOptions carMarker = new MarkerOptions().position(
                    position);
            if (gasStation.provider.size() == MAX_NO_OF_PROVIDERS) {
                carMarker.icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            } else {
                if (gasStation.provider.contains(Car.PROVIDER_C2G)) {
                    carMarker.icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                } else if (gasStation.provider.contains(Car.PROVIDER_DN)) {
                    carMarker.icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
                }
            }
            String providerString = gasStation.provider.get(0);
            if (gasStation.provider.size() > 1) {
                providerString = providerString + ", " + gasStation.provider.get(1);
            }

            carMarker.title(gasStation.name)
                    .snippet(providerString);
            getMap().addMarker(carMarker);
        }
    }


    @Override
    public void onMyLocationChange(Location lastKnownLocation) {
        CameraUpdate myLoc = CameraUpdateFactory.newCameraPosition(
                new CameraPosition.Builder().target(new LatLng(lastKnownLocation.getLatitude(),
                        lastKnownLocation.getLongitude())).zoom(DEFAULT_ZOOM_LEVEL).build()
        );
        getMap().moveCamera(myLoc);
        getMap().setOnMyLocationChangeListener(null);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                refreshMap();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResumeFragment() {
        refreshMap();
    }
}
