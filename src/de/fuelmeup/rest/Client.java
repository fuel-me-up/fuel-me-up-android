package de.fuelmeup.rest;

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
	private final String VEHICLE_LOCATION = "vehicles?loc=";
	private final String C2G_REQUEST_PARAMS = "&oauth_consumer_key=car2go&format=json";		
	private final String C2G_HAMBURG_STRING = "hamburg";
	private final String DN_HAMBURG_STRING = "40065";
	private static Client mInstance;
	private static AsyncHttpClient mHttpClient;
	
	public enum City {
	    HAMBURG
	}
	
	public enum Provider {
		CAR2GO,
		DRIVE_NOW
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
	public void getCars(Provider provider, City  city, AsyncHttpResponseHandler responseHandler){
		String url = null;
		switch(provider){
			case CAR2GO:
				switch(city) {
					case HAMBURG:
						url = buildRequestURL(provider, C2G_HAMBURG_STRING);
						break;
					default:
						url = buildRequestURL(provider, C2G_HAMBURG_STRING);
						break;
				}
				mHttpClient.get(url, responseHandler);
				break;
			case DRIVE_NOW:
				switch(city) {
				case HAMBURG:
					url = buildRequestURL(provider, DN_HAMBURG_STRING);
					break;
				default:
					url = buildRequestURL(provider, DN_HAMBURG_STRING);
					break;
				}
				mHttpClient.get(url, responseHandler);
				break;
		}
	}
	
	private String buildRequestURL(Provider provider, String city){
		String url = "";
		if(provider.equals(Provider.CAR2GO))
			url = CAR_2_GO + VEHICLE_LOCATION + city + C2G_REQUEST_PARAMS;
		else{
			url = DRIVE_NOW + city;
			System.out.println(url);
		}
		return url;		
	}
	
}
