package de.fuelmeup.ui.map;

import com.google.android.gms.maps.model.Marker;

/**
 * Created by jonas on 06.10.14.
 */
public class MockableMarker {

    public final Marker mapMarker;

    public MockableMarker(final Marker marker) {
        this.mapMarker = marker;
    }

    /**
     * Use this instead of mapMarker.remove() to allow testing.
     */
    public void remove() {
        mapMarker.remove();
    }

    @Override
    public int hashCode() {
        return mapMarker.hashCode();
    }

    @Override
    public boolean equals(final Object otherMarker) {
        MockableMarker otherMockableMarker = null;
        if (otherMarker.getClass() == MockableMarker.class) {
            otherMockableMarker = (MockableMarker) otherMarker;
        }
        return mapMarker.equals(otherMockableMarker.mapMarker);
    }

}
