package de.fuelmeup.ui.model;

import com.google.android.gms.maps.model.LatLng;

public class Marker {
    public final LatLng position;

    public final String title;

    public final String snippet;

    public final float markerHue;

    public Marker(LatLng position, String title, String snippet, float markerHue) {
        this.position = position;
        this.title = title;
        this.snippet = snippet;
        this.markerHue = markerHue;
    }
}
