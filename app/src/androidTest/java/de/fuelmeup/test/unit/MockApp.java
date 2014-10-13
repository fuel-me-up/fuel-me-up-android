package de.fuelmeup.test.unit;

import java.util.List;

import dagger.ObjectGraph;

public abstract class MockApp extends android.test.mock.MockApplication {

    private ObjectGraph applicationGraph;

    @Override
    public void onCreate() {
        applicationGraph = ObjectGraph.create(getModules().toArray());
    }

    protected abstract List<Object> getModules();

    public ObjectGraph getApplicationGraph() {
        return applicationGraph;
    }

}