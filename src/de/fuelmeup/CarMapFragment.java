package de.fuelmeup;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
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
public class CarMapFragment extends SupportMapFragment implements
		LocationListener {
	private LocationManager locationManager;
	private String provider;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);
		Client restClient = Client.getInstance();
		restClient.getCars(Client.Provider.DRIVE_NOW, Client.City.HAMBURG,
				mDNCarResponseHandler);
		restClient.getCars(Client.Provider.CAR2GO, Client.City.HAMBURG,
				mC2GCarResponseHandler);
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

	private JsonHttpResponseHandler mDNCarResponseHandler = new JsonHttpResponseHandler() {
		@Override
		public void onSuccess(JSONObject jsonResponse) {
			try {
				ArrayList<Car> cars = Car.getCarsFromDNJSONObject(jsonResponse);
				for (Car car : cars) {
					if (car.getmFuel() < 30) {
						getMap().addMarker(
								new MarkerOptions()
										.position(
												new LatLng(car.getmLat(), car
														.getmLng()))
										.title(car.getmName())
										.icon(BitmapDescriptorFactory
												.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
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

	private JsonHttpResponseHandler mC2GCarResponseHandler = new JsonHttpResponseHandler() {
		@Override
		public void onSuccess(JSONObject jsonResponse) {
			try {
				ArrayList<Car> cars = Car
						.getCarsFromC2GJSONObject(jsonResponse);
				for (Car car : cars) {
					if (car.getmFuel() < 30) {
						getMap().addMarker(
								new MarkerOptions()
										.position(
												new LatLng(car.getmLat(), car
														.getmLng()))
										.title(car.getmName())
										.icon(BitmapDescriptorFactory
												.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
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
