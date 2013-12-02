package de.fuelmeup;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.JsonHttpResponseHandler;

import de.fuelmeup.rest.Car;
import de.fuelmeup.rest.Client;
import de.fuelmeup.rest.GasStation;

/**
 * Fragment that displays cars in map.
 * 
 * @author jonas
 * 
 */
public class CarMapFragment extends MapFragment implements OnMyLocationChangeListener, BaseFragment {

	private static final String MAPS_REQUEST_URL = "http://maps.google.com/maps?&daddr=%s,%s";
	private boolean notLocalized = true;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
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

	@Override
	public void onResume() {
		super.onResume();
	}
	
	private void initMap() {
		getMap().getUiSettings().setMyLocationButtonEnabled(true);
		getMap().setMyLocationEnabled(true);
		if(notLocalized)
			getMap().setOnMyLocationChangeListener(this);
		notLocalized = false;
		getMap().setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
			@Override
			public void onInfoWindowClick(Marker marker) {
				LatLng markerPosition = marker.getPosition();
				sendIntentForNavigation(markerPosition);
			}
		});
	}
	
	private void sendIntentForNavigation(LatLng location){
		String uri = String.format(MAPS_REQUEST_URL, location.latitude, location.longitude);
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

		Client restClient = Client.getInstance();
		getMap().clear();
		restClient.getCars(Client.Provider.FUEL_ME_UP, Client.City.HAMBURG,
				maxFuelLevelC2G, mFMUCarResponseHandler);
		restClient.getGasStations(Client.City.HAMBURG, mFMUGasStationResponseHandler);
	}

	@Override
	public void onMyLocationChange(Location lastKnownLocation) {
	    CameraUpdate myLoc = CameraUpdateFactory.newCameraPosition(
	            new CameraPosition.Builder().target(new LatLng(lastKnownLocation.getLatitude(),
	                    lastKnownLocation.getLongitude())).zoom(13).build());
	    getMap().moveCamera(myLoc);
	    getMap().setOnMyLocationChangeListener(null);
	}

	private JsonHttpResponseHandler mFMUCarResponseHandler = new JsonHttpResponseHandler() {
		@Override
		public void onSuccess(JSONArray jsonResponse) {
			try {
				ArrayList<Car> cars = Car
						.getCarsFromFMUJSONObject(jsonResponse);
				if (getMap() != null) {
					for (Car car : cars) {
						LatLng position = new LatLng(car.getmLng(),
								car.getmLat());
						MarkerOptions carMarker = new MarkerOptions().position(
								position).title(car.getmName());
						if (car.getmProvider().equals(Car.FMU_PROVIDER_C2G))
							carMarker
									.icon(BitmapDescriptorFactory
											.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
						else if (car.getmProvider().equals(Car.FMU_PROVIDER_DN))
							carMarker
									.icon(BitmapDescriptorFactory
											.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
						String providerString = getString(R.string.provider_and_plate);
						carMarker.title(String.format(providerString, car.getmProvider(), car.getmName()))
                        .snippet(getString(R.string.fuel_label)+car.getmFuel());
						getMap().addMarker(carMarker);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable e, JSONObject errorResponse) {
		}

	};

	private JsonHttpResponseHandler mFMUGasStationResponseHandler = new JsonHttpResponseHandler() {
		@Override
		public void onSuccess(JSONArray jsonResponse) {
			try {
				ArrayList<GasStation> gasStations = GasStation
						.getGasStationsFromJSONArray(jsonResponse);
				if (getMap() != null) {
					for (GasStation gasStation : gasStations) {
						LatLng position = new LatLng(gasStation.getmLongitude(),
								gasStation.getmLatitude());
						MarkerOptions carMarker = new MarkerOptions().position(
								position);
						if(gasStation.getmProvider().size() == 2)
							carMarker
							.icon(BitmapDescriptorFactory
									.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
						else {
							if (gasStation.getmProvider().contains(Car.FMU_PROVIDER_C2G))
								carMarker
										.icon(BitmapDescriptorFactory
												.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
							else if (gasStation.getmProvider().contains(Car.FMU_PROVIDER_DN))
								carMarker
										.icon(BitmapDescriptorFactory
												.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
						}
						String providerString = gasStation.getmProvider().get(0);
						if(gasStation.getmProvider().size() > 1)
							providerString = providerString + ", " + gasStation.getmProvider().get(1);
						
						carMarker.title(gasStation.getmName())
                        .snippet(providerString);
						getMap().addMarker(carMarker);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable e, JSONObject errorResponse) {
		}

	};

	
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
		System.out.println("onRESUMEFRAGMENT");
		refreshMap();
	}
}
