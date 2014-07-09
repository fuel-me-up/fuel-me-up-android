package de.fuelmeup.rest;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GasStation {
	private static final String NAME = "name";
	private static final String COORDINATE = "coordinate";
	private static final String LATITUDE = "latitude";
	private static final String LONGITUDE = "longitude";
	private static final String PROVIDER = "provider";
	private static final String CITY = "city";

	
	private String mName;
	private double mLatitude;
	private double mLongitude;
	private List<String> mProvider;
	private String mCity;
	
	
	
	public GasStation(String mName, double mLatitude, double mLongitude,
			List<String> mProvider, String mCity) {
		super();
		this.mName = mName;
		this.mLatitude = mLatitude;
		this.mLongitude = mLongitude;
		this.mProvider = mProvider;
		this.mCity = mCity;
	}



	/**
	 * Create arraylist with GasStation objects from JSONArray
	 * @param jsonArray gas station data
	 * @return gas station list
	 * @throws JSONException
	 */
	public static ArrayList<GasStation> getGasStationsFromJSONArray(JSONArray jsonArray) throws JSONException{
		ArrayList<GasStation> gasStations = new  ArrayList<GasStation>();
		for(int i=0; i<jsonArray.length(); i++) {
			JSONObject jsonGasStation = jsonArray.getJSONObject(i);
			JSONObject jsonPosition = jsonGasStation.getJSONObject(COORDINATE);
			double lng = jsonPosition.getDouble(LATITUDE);
			double lat = jsonPosition.getDouble(LONGITUDE);
			String name = jsonGasStation.getString(NAME);
			String city = jsonGasStation.getString(CITY);
			JSONArray providerJSONArray = jsonGasStation.getJSONArray(PROVIDER);
			ArrayList<String> providerList = new ArrayList<String>();
			for(int j=0; j<providerJSONArray.length(); j++){
				providerList.add(providerJSONArray.getString(j));
			}

			GasStation gasStation = new GasStation(name, lat, lng, providerList, city);
			gasStations.add(gasStation);
		}
		return gasStations;
	}



	public String getmName() {
		return mName;
	}



	public void setmName(String mName) {
		this.mName = mName;
	}



	public double getmLatitude() {
		return mLatitude;
	}



	public void setmLatitude(double mLatitude) {
		this.mLatitude = mLatitude;
	}



	public double getmLongitude() {
		return mLongitude;
	}



	public void setmLongitude(double mLongitude) {
		this.mLongitude = mLongitude;
	}



	public List<String> getmProvider() {
		return mProvider;
	}



	public void setmProvider(List<String> mProvider) {
		this.mProvider = mProvider;
	}



	public String getmCity() {
		return mCity;
	}



	public void setmCity(String mCity) {
		this.mCity = mCity;
	}
	
	
}
