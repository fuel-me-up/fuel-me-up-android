package de.fuelmeup.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import de.fuelmeup.R;
import de.fuelmeup.observable.SeekBarObservable;
import de.fuelmeup.ui.mapcluster.CarItem;
import de.fuelmeup.ui.mapcluster.FuelMeUpClusterRenderer;
import de.fuelmeup.ui.model.Marker;
import de.fuelmeup.ui.model.MarkerMapper;
import de.fuelmeup.ui.presenter.CarMapPresenter;
import de.fuelmeup.ui.presenter.PresenterModule;
import de.fuelmeup.ui.component.custom.LabelledSeekBar;
import de.fuelmeup.ui.view.CarMapView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Fragment that displays cars in map.
 *
 * @author jonas
 */
public class CarMapFragment extends BaseMapFragment implements CarMapView {

    private static final String LOG_TAG = CarMapFragment.class.getSimpleName();

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
                .subscribe(progress -> presenter.loadCarsForFuelLevel(progress));
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                presenter.loadCarsForFuelLevel(seekBarFuelLevel.getProgress());
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

        getMap().clear();
        clusterManager.clearItems();

        for (Marker marker : markers) {
            CarItem carItem = new CarItem(marker.position, marker.title, marker.snippet, marker.markerHue);
            clusterManager.addItem(carItem);
        }

        clusterManager.cluster();
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
        clusterManager.setRenderer(new FuelMeUpClusterRenderer(getActivity(), getMap(), clusterManager));

        // Point the map's listeners at the listeners implemented by the cluster
        getMap().setOnCameraChangeListener(clusterManager);
        getMap().setOnMarkerClickListener(clusterManager);
    }


}
