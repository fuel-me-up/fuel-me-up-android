package de.fuelmeup.api.model;

import java.util.List;

public class GasStation {

    public final String name;
    public final Coordinate coordinate;
    public final List<String> provider;
    public final String city;


    public GasStation(String name, Coordinate coordinate, List<String> provider, String city) {
        this.name = name;
        this.coordinate = coordinate;
        this.provider = provider;
        this.city = city;
    }
}
