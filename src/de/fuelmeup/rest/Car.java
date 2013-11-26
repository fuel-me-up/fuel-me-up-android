package de.fuelmeup.rest;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Wrapper class for car data.
 * @author jonas
 *
 */

public class Car {
	
	private final static String PLACEMARKS = "placemarks";
	private final static String ADDRESS = "address";
	private final static String COORDINATES = "coordinates";
	private final static int LNG_POSITION = 0;
	private final static int LAT_POSITION = 1;
	private final static String ENGINE_TYPE = "engineType";
	private final static String EXTERIOR = "exterior";
	private final static String FUEL = "fuel";
	private final static String INTERIOR = "interior";
	private final static String NAME = "name";
	private final static String VIN = "vin";
	
	private final static String DN_RECORD = "rec";
	private final static String DN_VEHICLES_OBJ = "vehicles";
	private final static String DN_VEHICLES_ARRAY = "vehicles";
	private final static String DN_POSITION_OBJ = "position";
	private final static String DN_POSITION_LAT = "latitude";
	private final static String DN_POSITION_LNG = "longitude";
	private final static String DN_FUEL = "fuelState";
	
	private final static String FMU_FUEL = "fuel_level";
	private final static String FMU_POSITION_OBJ = "coordinate";
	private final static String FMU_POSITION_LAT = "latitude";
	private final static String FMU_POSITION_LNG = "longitude";




	private String mAddress;
	private double mLng;
	private double mLat;
	private String mEngineType;
	private String mExterior;
	private int mFuel;
	private String mInterior;
	private String mName;
	private String mVin;
	
	public Car(String mAddress, double mLng, double mLat, String mEngineType,
			String mExterior, int mFuel, String mInterior, String mName,
			String mVin) {
		super();
		this.mAddress = mAddress;
		this.mLng = mLng;
		this.mLat = mLat;
		this.mEngineType = mEngineType;
		this.mExterior = mExterior;
		this.mFuel = mFuel;
		this.mInterior = mInterior;
		this.mName = mName;
		this.mVin = mVin;
	}
	
	/**
	 * Create arraylist with Car objects from JSONObject
	 * @param jsonObject car data
	 * @return carList
	 * @throws JSONException
	 */
	public static ArrayList<Car> getCarsFromDNJSONObject(JSONObject jsonObject) throws JSONException{
		JSONObject jsonRecord = jsonObject.getJSONObject(DN_RECORD);
		JSONObject jsonCarObject = jsonRecord.getJSONObject(DN_VEHICLES_OBJ);
		System.out.println(jsonCarObject);
		JSONArray jsonCarArray = jsonCarObject.getJSONArray(DN_VEHICLES_ARRAY);
		
		ArrayList<Car> cars = new  ArrayList<Car>();
		for(int i=0; i<jsonCarArray.length(); i++) {
			JSONObject jsonCar = jsonCarArray.getJSONObject(i);
			JSONObject jsonCarPos = jsonCar.getJSONObject(DN_POSITION_OBJ);
			double lng = jsonCarPos.getDouble(DN_POSITION_LAT);
			double lat = jsonCarPos.getDouble(DN_POSITION_LNG);
			int fuel = jsonCar.getInt(DN_FUEL);
			Car car = new Car("", lat, lng, "", "", fuel, "", "", "");
			cars.add(car);
		}
		return cars;
	}
	
	/**
	 * Create arraylist with Car objects from JSONObject
	 * @param jsonObject car data
	 * @return carList
	 * @throws JSONException
	 */
	public static ArrayList<Car> getCarsFromC2GJSONObject(JSONObject jsonObject) throws JSONException{
		JSONArray jsonCarArray = jsonObject.getJSONArray(PLACEMARKS);
		ArrayList<Car> cars = new  ArrayList<Car>();
		for(int i=0; i<jsonCarArray.length(); i++) {
			JSONObject jsonCar = jsonCarArray.getJSONObject(i);
			String address = jsonCar.getString(ADDRESS);
			JSONArray coordArray = jsonCar.getJSONArray(COORDINATES);
			double lng = coordArray.getDouble(LNG_POSITION);
			double lat = coordArray.getDouble(LAT_POSITION);
			String engineType = jsonCar.getString(ENGINE_TYPE);
			String exterior = jsonCar.getString(EXTERIOR);
			int fuel = jsonCar.getInt(FUEL);;
			String interior = jsonCar.getString(INTERIOR);;
			String name = jsonCar.getString(NAME);;
			String vin = jsonCar.getString(VIN);;	
			Car car = new Car(address, lng, lat, engineType, exterior, fuel, interior, name, vin);
			cars.add(car);
		}
		return cars;
	}
	
	/**
	 * Create arraylist with Car objects from JSONObject
	 * @param jsonObject car data
	 * @return carList
	 * @throws JSONException
	 */
	public static ArrayList<Car> getCarsFromFMUJSONObject(JSONArray jsonArray) throws JSONException{
		ArrayList<Car> cars = new  ArrayList<Car>();
		for(int i=0; i<jsonArray.length(); i++) {
			JSONObject jsonCar = jsonArray.getJSONObject(i);
			JSONObject coordObject = jsonCar.getJSONObject(FMU_POSITION_OBJ);
			double lng = coordObject.getDouble(FMU_POSITION_LAT);
			double lat = coordObject.getDouble(FMU_POSITION_LNG);
			String address = coordObject.getString(ADDRESS);
			int fuel = jsonCar.getInt(FMU_FUEL);
			/*String engineType = jsonCar.getString(ENGINE_TYPE);
			String exterior = jsonCar.getString(EXTERIOR);
			int fuel = jsonCar.getInt(FUEL);
			String interior = jsonCar.getString(INTERIOR);
			String name = jsonCar.getString(NAME);
			String vin = jsonCar.getString(VIN);*/
			Car car = new Car(address, lng, lat, "", "", fuel, "", "", "");
			cars.add(car);
		}
		return cars;
	}
	
	public double getmLng() {
		return mLng;
	}

	public void setmLng(double mLng) {
		this.mLng = mLng;
	}

	public double getmLat() {
		return mLat;
	}

	public void setmLat(double mLat) {
		this.mLat = mLat;
	}

	
	public String getmAddress() {
		return mAddress;
	}
	public void setmAddress(String mAddress) {
		this.mAddress = mAddress;
	}
	public String getmEngineType() {
		return mEngineType;
	}
	public void setmEngineType(String mEngineType) {
		this.mEngineType = mEngineType;
	}
	public String getmExterior() {
		return mExterior;
	}
	public void setmExterior(String mExterior) {
		this.mExterior = mExterior;
	}
	public int getmFuel() {
		return mFuel;
	}
	public void setmFuel(int mFuel) {
		this.mFuel = mFuel;
	}
	public String getmInterior() {
		return mInterior;
	}
	public void setmInterior(String mInterior) {
		this.mInterior = mInterior;
	}
	public String getmName() {
		return mName;
	}
	public void setmName(String mName) {
		this.mName = mName;
	}
	public String getmVin() {
		return mVin;
	}
	public void setmVin(String mVin) {
		this.mVin = mVin;
	}
	
	
	
}
