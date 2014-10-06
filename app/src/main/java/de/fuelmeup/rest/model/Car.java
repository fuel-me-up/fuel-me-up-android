package de.fuelmeup.rest.model;

public class Car {

    public final static String PROVIDER_DN = "drive-now";
    public final static String PROVIDER_C2G = "car2go";


    public final String address;
    public final String fuelLevel;
    public final Coordinate coordinate;
    public final String licensePlate;
    public final String provider;


    public Car(String address, String fuelLevel, Coordinate coordinate, String licensePlate, String provider) {
        this.address = address;
        this.fuelLevel = fuelLevel;
        this.coordinate = coordinate;
        this.licensePlate = licensePlate;
        this.provider = provider;
    }
}
