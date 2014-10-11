package de.fuelmeup.resources;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        complete = false,
        library = true
)
public class ResourcesModule {

    @Provides
    @Singleton
    @CarMarkerTitleFormat
    public StringResource provideCarMarkerTitleFormat(ResourceWrapper wrapper) {
        return new StringResource(wrapper, "provider_and_plate");
    }

    @Provides
    @Singleton
    @FuelLevelString
    public StringResource provideFuelLevelString(ResourceWrapper wrapper) {
        return new StringResource(wrapper, "fuel_label");
    }
}