package de.fuelmeup.ui.fragment;

import de.fuelmeup.ui.model.Marker;

/**
 * Created by jonas on 06.10.14.
 */
public interface CarMapPresenter {

    public void onResume();

    public void onMarkerClicked(Marker marker);

}
