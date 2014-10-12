package de.fuelmeup.ui.fragment;


import android.app.Fragment;
import android.os.Bundle;

import com.google.android.gms.maps.MapFragment;

import java.util.List;

import dagger.ObjectGraph;
import de.fuelmeup.App;

public abstract class BaseFragment extends Fragment {
    private ObjectGraph fragmentGraph;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentGraph = ((App) getActivity().getApplication()).createScopedGraph(getModules().toArray());
        fragmentGraph.inject(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fragmentGraph = null;
    }

    protected abstract List<Object> getModules();
}
