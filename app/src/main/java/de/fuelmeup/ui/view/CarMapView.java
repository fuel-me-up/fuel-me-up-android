package de.fuelmeup.ui.view;

import android.location.Location;

import java.util.List;

import de.fuelmeup.ui.model.Marker;

/**
 * Created by jonas on 06.10.14.
 */
public interface CarMapView {

    public void drawMarkers(List<Marker> markers);

    public void setFuelLevel(int fuelLevel);

    public void startViewIntentWithStringUri(String uri);

    public void setMapPosition(Location location);
}
