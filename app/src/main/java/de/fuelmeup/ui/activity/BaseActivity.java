package de.fuelmeup.ui.activity;

import android.app.Activity;
import android.os.Bundle;

import java.util.List;

import dagger.ObjectGraph;
import de.fuelmeup.App;

public abstract class BaseActivity extends Activity {

    private ObjectGraph activityGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityGraph = ((App) getApplication()).createScopedGraph(getModules().toArray());
        activityGraph.inject(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityGraph = null;
    }

    protected abstract List<Object> getModules();
}