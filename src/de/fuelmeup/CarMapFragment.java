package de.fuelmeup;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ProgressBar;

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
public class CarMapFragment extends MapFragment implements
		OnMyLocationChangeListener, BaseFragment {

	private static final String MAPS_REQUEST_URL = "http://maps.google.com/maps?&daddr=%s,%s";
	private boolean notLocalized = true;
	private boolean refreshing = false;

	private ArrayList<GasStation> gasStations;

	private ProgressBar progressBar;
	private FrameLayout layout;
	Client restClient;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		layout = (FrameLayout) this.getView();
		progressBar = new ProgressBar(getActivity());
		setRetainInstance(true);
		setHasOptionsMenu(true);
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
		initMap();
		restClient = Client.getInstance();
		restClient.getGasStations(Client.City.HAMBURG,
				mFMUGasStationResponseHandler);
		refreshMap();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private void initMap() {
		getMap().getUiSettings().setMyLocationButtonEnabled(true);
		getMap().setMyLocationEnabled(true);
		if (notLocalized)
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

	private void sendIntentForNavigation(LatLng location) {
		String uri = String.format(MAPS_REQUEST_URL, location.latitude,
				location.longitude);
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
		startActivity(intent);
	}

	private void refreshMap() {
		// set mutex
		refreshing = true;
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		int defaultMaxFuelLevel = Integer
				.parseInt(getString(R.string.default_max_fuel_level));
		int maxFuelLevelC2G = settings.getInt(
				getString(R.string.max_fuel_preference), defaultMaxFuelLevel);

		LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		layoutParams.gravity = Gravity.CENTER;
		layout.addView(progressBar, layoutParams);
		restClient.getCars(Client.Provider.FUEL_ME_UP, Client.City.HAMBURG,
				maxFuelLevelC2G, mFMUCarResponseHandler);
	}

	@Override
	public void onMyLocationChange(Location lastKnownLocation) {
		CameraUpdate myLoc = CameraUpdateFactory
				.newCameraPosition(new CameraPosition.Builder()
						.target(new LatLng(lastKnownLocation.getLatitude(),
								lastKnownLocation.getLongitude())).zoom(13)
						.build());
		getMap().moveCamera(myLoc);
		getMap().setOnMyLocationChangeListener(null);
	}

	private JsonHttpResponseHandler mFMUCarResponseHandler = new JsonHttpResponseHandler() {
		@Override
		public void onSuccess(JSONArray jsonResponse) {
			try {
				List<Car> cars = Car.getCarsFromFMUJSONObject(jsonResponse);
				if (getMap() != null) {
					getMap().clear();
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
						carMarker.title(
								String.format(providerString,
										car.getmProvider(), car.getmName()))
								.snippet(
										getString(R.string.fuel_label)
												+ car.getmFuel());
						getMap().addMarker(carMarker);
					}
				}
				if (gasStations != null)
					addGasStationsToMap();
				refreshing = false;
			} catch (JSONException e) {
				e.printStackTrace();
			}
			layout.removeView(progressBar);
		}

		@Override
		public void onFailure(Throwable e, JSONObject errorResponse) {
		}

	};

	private void addGasStationsToMap() {
		if (getMap() != null) {
			for (GasStation gasStation : gasStations) {
				LatLng position = new LatLng(gasStation.getmLongitude(),
						gasStation.getmLatitude());
				MarkerOptions carMarker = new MarkerOptions()
						.position(position);
				if (gasStation.getmProvider().size() == 2)
					carMarker.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
				else {
					if (gasStation.getmProvider()
							.contains(Car.FMU_PROVIDER_C2G))
						carMarker
								.icon(BitmapDescriptorFactory
										.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
					else if (gasStation.getmProvider().contains(
							Car.FMU_PROVIDER_DN))
						carMarker
								.icon(BitmapDescriptorFactory
										.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
				}
				String providerString = gasStation.getmProvider().get(0);
				if (gasStation.getmProvider().size() > 1)
					providerString = providerString + ", "
							+ gasStation.getmProvider().get(1);

				carMarker.title(gasStation.getmName()).snippet(providerString);
				getMap().addMarker(carMarker);
			}
		}

	}

	private JsonHttpResponseHandler mFMUGasStationResponseHandler = new JsonHttpResponseHandler() {
		@Override
		public void onSuccess(JSONArray jsonResponse) {
			try {
				gasStations = GasStation
						.getGasStationsFromJSONArray(jsonResponse);
				addGasStationsToMap();
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
			if (!refreshing) {
				refreshMap();
			}
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
