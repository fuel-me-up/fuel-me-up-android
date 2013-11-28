package de.fuelmeup;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.loopj.android.http.JsonHttpResponseHandler;

import de.fuelmeup.rest.Car;
import de.fuelmeup.rest.Client;


/**
 * Fragment that displays cars in list.
 * 
 * @author jonas
 * 
 */
public class CarListFragment extends ListFragment {
	private RelativeLayout mCarListLayout;
	private Object lock = new Object();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		mCarListLayout = (RelativeLayout) inflater.inflate(R.layout.car_list,
				container, false);
		return mCarListLayout;
	}

	@Override
	public void onResume() {
		super.onResume();
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
		ProgressBar progressView = (ProgressBar) mCarListLayout
				.findViewById(R.id.progress);
		progressView.setVisibility(View.VISIBLE);
		CarListAdapter adapter = new CarListAdapter(getActivity(), new ArrayList<Car>());
		setListAdapter(adapter);
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
		int defaultMaxFuelLevel = Integer.parseInt(getString(R.string.default_max_fuel_level));
		int maxFuelLevelC2G =  settings.getInt(getString(R.string.max_fuel_preference_c2g), defaultMaxFuelLevel);
		
		Client restClient = Client.getInstance();
		restClient.getCars(Client.Provider.FUEL_ME_UP, Client.City.HAMBURG, maxFuelLevelC2G,
				mFMUCarResponseHandler);

	}

	public void setCarsOnMap(ArrayList<Car> cars) {
		synchronized(lock) {
			CarListAdapter adapter = (CarListAdapter) getListAdapter();
			adapter.clear();
			for (Car car : cars) {
				adapter.addItem(car);
			}
			ProgressBar progressView = (ProgressBar) mCarListLayout
					.findViewById(R.id.progress);
			progressView.setVisibility(View.GONE);
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// Do something with the data

	}

	private JsonHttpResponseHandler mFMUCarResponseHandler = new JsonHttpResponseHandler() {
		@Override
		public void onSuccess(JSONArray jsonResponse) {
			try {
				ArrayList<Car> cars = Car.getCarsFromFMUJSONObject(jsonResponse);
				setCarsOnMap(cars);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable e, JSONObject errorResponse) {
		}

	};
}
