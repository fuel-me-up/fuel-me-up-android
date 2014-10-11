package de.fuelmeup.data;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.fuelmeup.resources.ResourceWrapper;
import de.fuelmeup.resources.ResourcesModule;

@Module(
        includes = {
                ResourcesModule.class
        },
        complete = false,
        library = true
)
public class DataModule {

    /**
     * Provide ResourceWrapper implementation
     */
    @Provides
    @Singleton
    ResourceWrapper provideResourceWrapper(Application app) {
        return new ResourceWrapperImpl(app.getApplicationContext());
    }


}