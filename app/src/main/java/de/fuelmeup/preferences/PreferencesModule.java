package de.fuelmeup.preferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.fuelmeup.resources.CarMarkerTitleFormat;
import de.fuelmeup.resources.FuelLevelString;
import de.fuelmeup.resources.ResourceWrapper;
import de.fuelmeup.resources.StringResource;


@Module(
        complete = false,
        library = true
)
public class PreferencesModule {

    @Provides
    @Singleton
    @FuelLevelPreference
    public IntegerPreference provideFuelLevelPreference(PreferenceWrapper wrapper) {
        return new IntegerPreference(wrapper, "fuel_level");
    }
}