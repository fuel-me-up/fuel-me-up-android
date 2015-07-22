package de.fuelmeup.ui.mapcluster;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class CarItem implements ClusterItem {
    private final LatLng position;

    public final String title;

    public final String snippet;

    public final float markerHue;

    public CarItem(LatLng position, String title, String snippet, float markerHue) {
        this.title = title;
        this.snippet = snippet;
        this.markerHue = markerHue;
        this.position = position;
    }

    @Override
    public LatLng getPosition() {
        return position;
    }
}
