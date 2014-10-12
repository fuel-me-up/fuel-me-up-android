package de.fuelmeup.ui.fragment;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import de.fuelmeup.R;
import de.fuelmeup.observable.SeekBarObservable;
import de.fuelmeup.rest.model.Car;
import de.fuelmeup.rest.model.GasStation;
import de.fuelmeup.ui.model.Marker;
import de.fuelmeup.ui.model.MarkerMapper;
import de.fuelmeup.ui.presenter.CarMapPresenter;
import de.fuelmeup.ui.presenter.PresenterModule;
import de.fuelmeup.ui.view.custom.LabelledSeekBar;
import rx.android.observables.AndroidObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Fragment that displays cars in map.
 *
 * @author jonas
 */
public class CarMapFragment extends BaseMapFragment implements CarMapView {

    private static final String LOG_TAG = CarMapFragment.class.getSimpleName();
    public static final int MAX_NO_OF_PROVIDERS = 2;

    @Inject
    CarMapPresenter presenter;
    private LabelledSeekBar seekBarFuelLevel;
    private ClusterManager<CarItem> clusterManager;

    @Override
    protected int provideLayout() {
        return R.layout.fragment_car_map;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        seekBarFuelLevel = (LabelledSeekBar) view.findViewById(R.id.seekBarFuelLevel);

        // Observe seekBar tracking to inform presenter about fuel level
        SeekBarObservable.startTrackingTouch(seekBarFuelLevel)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
                        .subscribe(progress -> presenter.fuelLevelChanged(progress));
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
        getMap().setOnInfoWindowClickListener(marker -> {
            presenter.onMarkerClicked(MarkerMapper.fromMapsMarker(marker));
        });
        setUpClusterer();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                return true;
            case R.id.action_settings:
                // TODO: Show settings?
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

        clusterManager.clearItems();

        for (Marker marker : markers) {
            /*MarkerOptions mapMarker = new MarkerOptions().position(
                    marker.position).title(marker.title)
                    .snippet(marker.snippet)
                    .icon(BitmapDescriptorFactory.defaultMarker(marker.markerHue));
            getMap().addMarker(mapMarker);*/
            clusterManager.addItem(new CarItem(marker.position.latitude, marker.position.longitude));
        }
    }

    @Override
    public void setFuelLevel(int fuelLevel) {
        seekBarFuelLevel.setProgress(fuelLevel);
    }

    @Override
    public void startViewIntentWithStringUri(String uri) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri)));
    }

    private void setUpClusterer() {
        // Position the map.
        getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.503186, -0.126446), 10));

        // Initialize the manager with the context and the map.
        clusterManager = new ClusterManager<>(getActivity(), getMap());

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        getMap().setOnCameraChangeListener(clusterManager);
        getMap().setOnMarkerClickListener(clusterManager);
    }

    public class CarItem implements ClusterItem {
        private final LatLng mPosition;

        public CarItem(double lat, double lng) {
            mPosition = new LatLng(lat, lng);
        }

        @Override
        public LatLng getPosition() {
            return mPosition;
        }
    }
}
