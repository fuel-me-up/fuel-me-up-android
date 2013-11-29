package de.fuelmeup;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.JsonHttpResponseHandler;

import de.fuelmeup.rest.Car;
import de.fuelmeup.rest.Client;

/**
 * Fragment that displays cars in map.
 * 
 * @author jonas
 * 
 */
public class CarMapFragment extends MapFragment implements  OnMyLocationChangeListener {


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);
		setHasOptionsMenu(true);
		getMap().getUiSettings().setMyLocationButtonEnabled(true);
		getMap().setMyLocationEnabled(true);
		getMap().setOnMyLocationChangeListener(this);
		refreshMap();
		 getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
		/*	CameraUpdate myLoc = CameraUpdateFactory.newCameraPosition(
		            new CameraPosition.Builder().zoom(13).build());
		    getMap().moveCamera(myLoc);*/
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private void refreshMap() {
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		int defaultMaxFuelLevel = Integer
				.parseInt(getString(R.string.default_max_fuel_level));
		int maxFuelLevelC2G = settings.getInt(
				getString(R.string.max_fuel_preference_c2g),
				defaultMaxFuelLevel);

		Client restClient = Client.getInstance();
		restClient.getCars(Client.Provider.FUEL_ME_UP, Client.City.HAMBURG,
				maxFuelLevelC2G, mFMUCarResponseHandler);
	}

	private JsonHttpResponseHandler mDNCarResponseHandler = new JsonHttpResponseHandler() {
		@Override
		public void onSuccess(JSONObject jsonResponse) {
			try {
				ArrayList<Car> cars = Car.getCarsFromDNJSONObject(jsonResponse);
				for (Car car : cars) {
					getMap().addMarker(
							new MarkerOptions()
									.position(
											new LatLng(car.getmLat(), car
													.getmLng()))
									.title(car.getmName())
									.icon(BitmapDescriptorFactory
											.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable e, JSONObject errorResponse) {
		}
	};
	
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
						getMap().addMarker(carMarker);
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
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
}
