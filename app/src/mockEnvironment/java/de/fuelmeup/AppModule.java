package de.fuelmeup;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.fuelmeup.data.DataModule;
import de.fuelmeup.rest.ApiService;
import de.fuelmeup.rest.MockApiService;

@Module(
        injects = {
                App.class,
        },
        includes = {
                DataModule.class
        },
        library = true
)
public class AppModule {

    private final App app;

    public AppModule(App app) {
        this.app = app;
    }


    @Provides
    @Singleton
    Application provideApplication() {
        return app;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return app.getApplicationContext();
    }


    @Provides
    @Singleton
    public ApiService provideApiService(Context context) {
        return getMockApiSerivce();
    }

    private ApiService getMockApiSerivce() {
        return new MockApiService();
    }
}