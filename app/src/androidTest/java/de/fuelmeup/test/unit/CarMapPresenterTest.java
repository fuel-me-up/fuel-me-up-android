package de.fuelmeup.test.unit;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

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
import de.fuelmeup.ui.view.CarMapView;
import de.fuelmeup.ui.model.Marker;
import de.fuelmeup.ui.presenter.CarMapPresenter;
import de.fuelmeup.ui.presenter.CarMapPresenterImpl;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CarMapPresenterTest extends UnitTest {

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

    private void initMocks() {
        mockFuelLevelPreference = mock(IntegerPreference.class);
        mockContext = mock(Context.class);
        mockCarMapView = mock(CarMapView.class);
    }

    public void testSetFuelLevel() {
        carMapPresenter.loadCarsForFuelLevel(30);

        verify(apiService).fetchVehicles(30);

        verify(mockFuelLevelPreference).set(eq(30));
    }

    public void testOnMarkerClicked() {
        LatLng location = new LatLng(1f, 1f);
        carMapPresenter.onMarkerClicked(new Marker(location, "title", "snippet", 1f));

        String uri = String.format(CarMapPresenterImpl.MAPS_NAVIGATION_URL, location.latitude, location.longitude);
        verify(mockCarMapView).startViewIntentWithStringUri(uri);
    }

    public void testOnResume() {
        when(mockFuelLevelPreference.get(anyInt())).thenReturn(42);
        carMapPresenter.onResume();

        verify(mockFuelLevelPreference).get(eq(25));

        verify(mockCarMapView).setFuelLevel(42);

        verify(apiService).fetchVehicles(42);
    }

}