package de.fuelmeup;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
public class CarMapFragment extends MapFragment implements LocationListener {
	private LocationManager locationManager;
	private String provider;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.car_map, container, false);
	    return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
		refreshMap();
		/*
		 * restClient.getCars(Client.Provider.DRIVE_NOW, Client.City.HAMBURG,
		 * mDNCarResponseHandler);
		 */

		// Get the location manager
		locationManager = (LocationManager) getActivity().getSystemService(
				Context.LOCATION_SERVICE);
		// Define the criteria how to select the locatioin provider -> use
		// default
		Criteria criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, false);
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 4000, 0, this);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				4000, 0, this);
		Location location = locationManager.getLastKnownLocation(provider);

		// Initialize the location fields
		if (location != null) {
			System.out.println("Provider " + provider + " has been selected.");
			onLocationChanged(location);
		}
	}
	
	@Override
	public void onResume(){
		super.onResume();
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
	}
	
	private void refreshMap() {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
		int defaultMaxFuelLevel = Integer.parseInt(getString(R.string.default_max_fuel_level));
		int maxFuelLevelC2G =  settings.getInt(getString(R.string.max_fuel_preference_c2g), defaultMaxFuelLevel);
		
		Client restClient = Client.getInstance();
		restClient.getCars(Client.Provider.FUEL_ME_UP, Client.City.HAMBURG, maxFuelLevelC2G,
				mFMUCarResponseHandler);
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

	private JsonHttpResponseHandler mFMUCarResponseHandler = new JsonHttpResponseHandler() {
		@Override
		public void onSuccess(JSONArray jsonResponse) {
			try {
				ArrayList<Car> cars = Car
						.getCarsFromFMUJSONObject(jsonResponse);
				getMap().clear();
				for (Car car : cars) {
					LatLng position = new LatLng(car.getmLng(), car.getmLat());
					MarkerOptions carMarker = new MarkerOptions().position(position)
					.title(car.getmName());
					if(car.getmProvider().equals(Car.FMU_PROVIDER_C2G))
						carMarker.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
					else if(car.getmProvider().equals(Car.FMU_PROVIDER_DN))
						carMarker.icon(BitmapDescriptorFactory
								.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
					getMap().addMarker(carMarker);
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

	@Override
	public void onLocationChanged(Location location) {
		System.out.println("location changed!!!!");
		System.out.println("location.getLongitude(), location.getLatitude()");
		LatLng center = new LatLng(location.getLatitude(),
				location.getLongitude());
		float zoom = 15f;
		getMap().animateCamera(CameraUpdateFactory.newLatLngZoom(center, zoom));
		locationManager.removeUpdates(this);
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}
}
