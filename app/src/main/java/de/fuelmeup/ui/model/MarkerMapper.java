package de.fuelmeup.ui.model;

/**
 * Created by jonas on 07.10.14.
 */
public class MarkerMapper {

    public static Marker fromMapsMarker(final com.google.android.gms.maps.model.Marker mapsMarker) {
        return new Marker(mapsMarker.getPosition(), mapsMarker.getTitle(), mapsMarker.getSnippet(), 0f);
    }

}
