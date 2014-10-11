package de.fuelmeup.ui.fragment;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.fuelmeup.AppModule;
import de.fuelmeup.data.DataModule;
import de.fuelmeup.resources.ResourcesModule;

@Module(
        complete = false,
        injects = {CarMapFragment.class, CarMapPresenterImpl.class},
        addsTo = AppModule.class,
        includes = {ResourcesModule.class, DataModule.class}
)
public class CarMapModule {

    private CarMapView view;

    public CarMapModule(CarMapView view) {
        this.view = view;
    }

    @Provides
    @Singleton
    public CarMapView provideView() {
        return view;
    }

    @Provides
    @Singleton
    public CarMapPresenter providePresenter(CarMapPresenterImpl carMapPresenter) {
        return carMapPresenter;
    }
}