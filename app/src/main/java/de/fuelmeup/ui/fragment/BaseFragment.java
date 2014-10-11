package de.fuelmeup.ui.fragment;


import android.os.Bundle;

import com.google.android.gms.maps.MapFragment;

import java.util.List;

import dagger.ObjectGraph;
import de.fuelmeup.App;

public abstract class BaseFragment extends MapFragment {
    abstract void onResumeFragment();

    private ObjectGraph activityGraph;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityGraph = ((App) getActivity().getApplication()).createScopedGraph(getModules().toArray());
        activityGraph.inject(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        activityGraph = null;
    }

    protected abstract List<Object> getModules();
}
