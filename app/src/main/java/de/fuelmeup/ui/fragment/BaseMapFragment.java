package de.fuelmeup.ui.fragment;


import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;

import de.fuelmeup.R;


public abstract class BaseMapFragment extends BaseFragment {

    private final static String LOG_TAG = BaseMapFragment.class.getSimpleName();
    public static final int ZOOM_FACTOR = 15;
    public static final String KEY_GMAP_STATE = "gmap_state";

    private GoogleMap googleMap = null;

    private volatile boolean autoLocateOnMap = true;
    private MapView mapView;

    protected abstract int provideLayout();

    public GoogleMap getMap() {
        return googleMap;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        final int result = MapsInitializer.initialize(getActivity());
        if (result != ConnectionResult.SUCCESS) {
            throw new IllegalArgumentException("Failed to init maps");
        }

    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        // MapView is confused by custom parcelables (DirverButton)
        // So use seperate bundle for googleMap
        // issue: https://github.com/Prototik/HoloEverywhere/issues/643
        final Bundle gmapstate = new Bundle();
        mapView.onSaveInstanceState(gmapstate);
        outState.putParcelable(KEY_GMAP_STATE, gmapstate);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(provideLayout(), null);
        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) view.findViewById(R.id.mapView);

        if (savedInstanceState != null) {
            mapView.onCreate((Bundle) savedInstanceState.getParcelable(KEY_GMAP_STATE));
        } else {
            mapView.onCreate(null);
        }

        // Gets to GoogleMap from the MapView and does initialization stuff
        googleMap = mapView.getMap();
        googleMap.setMyLocationEnabled(true);
        final UiSettings settings = googleMap.getUiSettings();
        settings.setMyLocationButtonEnabled(true);
        settings.setZoomControlsEnabled(false);


        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    public void initMapPosition(final Location location) {
        final CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), ZOOM_FACTOR);
        googleMap.moveCamera(update);
    }

    protected void moveTo(final double latitude, final double longitude) {
        final CameraUpdate update = CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude));
        googleMap.moveCamera(update);
    }

    protected void animateTo(final double latitude, final double longitude) {
        final CameraUpdate update = CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude));
        googleMap.animateCamera(update);
    }

}
