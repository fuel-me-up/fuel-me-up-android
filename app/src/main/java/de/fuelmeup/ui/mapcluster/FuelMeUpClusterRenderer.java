package de.fuelmeup.ui.mapcluster;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

public class FuelMeUpClusterRenderer extends DefaultClusterRenderer<CarItem> {

    public FuelMeUpClusterRenderer(Context context, GoogleMap map,
                                   ClusterManager<CarItem> clusterManager) {
        super(context, map, clusterManager);
    }

    @Override
    protected void onBeforeClusterItemRendered(CarItem item, MarkerOptions markerOptions) {
        super.onBeforeClusterItemRendered(item, markerOptions);

        markerOptions.title(item.title).snippet(item.snippet);
    }

    @Override
    protected void onClusterItemRendered(CarItem clusterItem, Marker marker) {
        super.onClusterItemRendered(clusterItem, marker);
    }
}