package de.fuelmeup.ui.fragment;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import de.fuelmeup.R;
import de.fuelmeup.rest.model.Car;
import de.fuelmeup.rest.model.GasStation;
import de.fuelmeup.ui.model.Marker;
import de.fuelmeup.ui.model.MarkerMapper;
import de.fuelmeup.ui.presenter.CarMapPresenter;
import de.fuelmeup.ui.presenter.PresenterModule;

/**
 * Fragment that displays cars in map.
 *
 * @author jonas
 */
public class CarMapFragment extends BaseFragment implements OnMyLocationChangeListener, CarMapView {

    private static final String LOG_TAG = CarMapFragment.class.getSimpleName();
    public static final int DEFAULT_ZOOM_LEVEL = 13;
    public static final int MAX_NO_OF_PROVIDERS = 2;
    private boolean notLocalized = true;

    @Inject
    CarMapPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activity.getActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.car_map, menu);
    }

    @Override
    public void onResume() {
        super.onResume();

        presenter.onResume();
    }

    @Override
    protected List<Object> getModules() {
        return Arrays.asList(new CarMapModule(this), new PresenterModule());
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        initMap();
    }

    private void initMap() {
        getMap().getUiSettings().setMyLocationButtonEnabled(true);
        getMap().setMyLocationEnabled(true);
        if (notLocalized)
            getMap().setOnMyLocationChangeListener(this);
        notLocalized = false;
        getMap().setOnInfoWindowClickListener(marker -> {
            presenter.onMarkerClicked(MarkerMapper.fromMapsMarker(marker));
        });
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
                return true;
            case R.id.action_settings:
                    PreferenceFragment settingsFragment = new SettingsFragment();
                    FragmentTransaction transaction = getFragmentManager()
                            .beginTransaction();
                    transaction.replace(android.R.id.content, settingsFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void drawMarkers(List<Marker> markers) {
        if (getMap() == null) {
            return;
        }

        for (Marker marker : markers) {
            MarkerOptions mapMarker = new MarkerOptions().position(
                    marker.position).title(marker.title)
                    .snippet(marker.snippet)
                    .icon(BitmapDescriptorFactory.defaultMarker(marker.markerHue));
            getMap().addMarker(mapMarker);
        }
    }

    @Override
    public void startViewIntentWithStringUri(String uri) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri)));
    }
}
