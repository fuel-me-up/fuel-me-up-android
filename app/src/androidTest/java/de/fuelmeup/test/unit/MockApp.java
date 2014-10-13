package de.fuelmeup.test.unit;

import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import dagger.ObjectGraph;
import de.fuelmeup.AppModule;
import de.fuelmeup.ui.fragment.CarMapModule;
import de.fuelmeup.ui.fragment.CarMapView;

import static org.mockito.Mockito.mock;

public abstract class MockApp extends  android.test.mock.MockApplication {

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