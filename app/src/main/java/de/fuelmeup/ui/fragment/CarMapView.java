package de.fuelmeup.ui.fragment;

import java.util.List;

import de.fuelmeup.ui.model.Marker;

/**
 * Created by jonas on 06.10.14.
 */
public interface CarMapView {

    public void drawMarkers(List<Marker> markers);


    public void startViewIntentWithStringUri(String uri);
}
