package de.fuelmeup;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
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
	private ArrayList<Car> mCars = new ArrayList<Car>();
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
		ProgressBar progressView = (ProgressBar) mCarListLayout
				.findViewById(R.id.progress);
		progressView.setVisibility(View.VISIBLE);
		mCars = new ArrayList<Car>();
		CarListAdapter adapter = new CarListAdapter(getActivity(), mCars);
		setListAdapter(adapter);
		Client restClient = Client.getInstance();
		restClient.getCars(Client.Provider.FUEL_ME_UP, Client.City.HAMBURG,
				mFMUCarResponseHandler);

	}

	public void addCars(ArrayList<Car> cars) {
		synchronized(lock) {
			mCars.addAll(cars);
			CarListAdapter adapter = (CarListAdapter) getListAdapter();
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
				addCars(cars);
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
