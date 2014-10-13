package de.fuelmeup.test.unit;


import android.content.Context;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.ObjectGraph;
import dagger.Provides;
import de.fuelmeup.AppModule;
import de.fuelmeup.preferences.FuelLevelPreference;
import de.fuelmeup.preferences.IntegerPreference;
import de.fuelmeup.resources.ResourceWrapper;
import de.fuelmeup.rest.ApiService;
import de.fuelmeup.ui.fragment.CarMapModule;
import de.fuelmeup.ui.presenter.CarMapPresenter;
import de.fuelmeup.ui.view.CarMapView;

import static org.mockito.Mockito.mock;

public class CarMapViewTest extends UnitTest {

    private static IntegerPreference mockFuelLevelPreference;

    private static Context mockContext;

    private static CarMapView mockCarMapView;

    @Inject
    CarMapPresenter carMapPresenter;

    @Inject
    ApiService apiService;

    /**
     * Test module injects mocks
     */
    @Module(
            injects = CarMapPresenterTest.class,
            overrides = true,
            library = true,
            complete = false
    )
    static class TestModule {

        @Provides
        @Singleton
        Context provideContext() {
            return mockContext;
        }

        @Provides
        @Singleton
        ResourceWrapper provideResourceWrapper() {
            return mock(ResourceWrapper.class);
        }

        @Provides
        @Singleton
        @FuelLevelPreference
        public IntegerPreference provideFuelLevelPreference() {
            return mockFuelLevelPreference;
        }

        @Provides
        @Singleton
        CarMapView provideCarMapView() {
            return mockCarMapView;
        }
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        initMocks();

        MockApp mockApp = new MockApp() {
            @Override
            protected List<Object> getModules() {
                return Arrays.asList(new TestModule(), new AppModule(this), new CarMapModule(mock(CarMapView.class)));
            }
        };
        mockApp.onCreate();
        ObjectGraph objectGraph = mockApp.getApplicationGraph();
        objectGraph.inject(this);
    }

}
