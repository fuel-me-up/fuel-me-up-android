package de.fuelmeup.preferences;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.fuelmeup.R;
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
    public IntegerPreference provideFuelLevelPreference(PreferenceWrapper wrapper, Context context) {
        return new IntegerPreference(wrapper, context.getString(R.string.max_fuel_preference));
    }
}