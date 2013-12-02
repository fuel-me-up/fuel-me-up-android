package de.fuelmeup.rest;

import org.apache.http.protocol.HTTP;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * Singleton implementation of a rest client for communication
 * with the car2go rest API.
 * 
 * @author jonas
 *
 */
public class Client {
	private final String CAR_2_GO = "https://www.car2go.com/api/v2.1/";
	private final String DRIVE_NOW = "https://de.drive-now.com/php/metropolis/json.vehicle_filter?cit=";
	private final String FUEL_ME_UP = "http://fuel-me-up.herokuapp.com/vehicles/%s?max_fuel_level=%d";
	private final String FUEL_ME_UP_GAS_STATIONS = "http://fuel-me-up.herokuapp.com/gasstations/%s";
	private final String VEHICLE_LOCATION = "vehicles?loc=";
	private final String C2G_REQUEST_PARAMS = "&oauth_consumer_key=car2go&format=json";		
	private final String C2G_HAMBURG_STRING = "hamburg";
	private final String DN_HAMBURG_STRING = "40065";
	private final String PROVIDER_CAR2GO = "car2go";
	private final String PROVIDER_DN = "drive-now";

	
	private final String HTTP_CONTENT_TYPE_JSON = "application/json";
	private final String HTTP_ACCEPT = "Accept";

	private static Client mInstance;
	private static AsyncHttpClient mHttpClient;
	
	public enum City {
	    HAMBURG
	}
	
	public enum Provider {
		CAR2GO,
		DRIVE_NOW,
		FUEL_ME_UP
	}
	
	//Constructor is private - Singleton Pattern
	private Client(){}
	/**
	 * Instatiates this class if necessary.
	 * @return An instance of this class.
	 */
	public static Client getInstance(){
		if(mInstance == null){
			mHttpClient = new AsyncHttpClient();
			mInstance = new Client();
			return mInstance;
		}else
			return mInstance;
	}
	
	/**
	 * Request cars from server. 
	 * @param city
	 * @param responseHandler Handler for response data
	 */
	public void getCars(Provider provider, City  city, int maxFuelLevel, AsyncHttpResponseHandler responseHandler){
		String url = null;
		switch(provider){
			case CAR2GO:
				switch(city) {
					case HAMBURG:
						url = buildRequestURL(provider, C2G_HAMBURG_STRING, maxFuelLevel);
						break;
					default:
						url = buildRequestURL(provider, C2G_HAMBURG_STRING, maxFuelLevel);
						break;
				}
				mHttpClient.get(url, responseHandler);
				break;
			case DRIVE_NOW:
				switch(city) {
				case HAMBURG:
					url = buildRequestURL(provider, DN_HAMBURG_STRING, maxFuelLevel);
					break;
				default:
					url = buildRequestURL(provider, DN_HAMBURG_STRING, maxFuelLevel);
					break;
				}
				mHttpClient.get(url, responseHandler);
				break;
			case FUEL_ME_UP:
				switch(city) {
					case HAMBURG:
						url = buildRequestURL(provider, C2G_HAMBURG_STRING, maxFuelLevel);
						break;
					default:
						url = buildRequestURL(provider, C2G_HAMBURG_STRING, maxFuelLevel);
						break;
				}
				mHttpClient.addHeader(HTTP.CONTENT_TYPE, HTTP_CONTENT_TYPE_JSON);
				mHttpClient.addHeader(HTTP_ACCEPT, HTTP_CONTENT_TYPE_JSON);
				mHttpClient.get(url, responseHandler);
				break;
		}
	}
	
	public void getGasStations(City city, AsyncHttpResponseHandler responseHandler){
		String url = "";
		switch(city) {
		case HAMBURG:
			url = buildRequestURLGasStations(C2G_HAMBURG_STRING);
			break;
		default:
			url = buildRequestURLGasStations(C2G_HAMBURG_STRING);
			break;
		}
		mHttpClient.addHeader(HTTP.CONTENT_TYPE, HTTP_CONTENT_TYPE_JSON);
		mHttpClient.addHeader(HTTP_ACCEPT, HTTP_CONTENT_TYPE_JSON);
		mHttpClient.get(url, responseHandler);
	}

	
	private String buildRequestURL(Provider provider, String city, int fuelLevel){
		String url = "";
		if(provider.equals(Provider.CAR2GO))
			url = CAR_2_GO + VEHICLE_LOCATION + city + C2G_REQUEST_PARAMS;
		else if(provider.equals(Provider.DRIVE_NOW)){
			url = DRIVE_NOW + city;
		}else if(provider.equals(Provider.FUEL_ME_UP)){
			url = String.format(FUEL_ME_UP, city, fuelLevel);
		}
		return url;		
	}
	
	private String buildRequestURLGasStations(String city){
		String url = String.format(FUEL_ME_UP_GAS_STATIONS, city);
		return url;		
	}
	
}
