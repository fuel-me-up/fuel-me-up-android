package de.fuelmeup.data;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;

import de.fuelmeup.preferences.PreferenceWrapper;
import de.fuelmeup.resources.ResourceWrapper;


public class PreferenceWrapperImpl implements PreferenceWrapper {
    private Context context;

    @Inject
    public PreferenceWrapperImpl(Context context) {
        this.context = context;
    }

    @Override
    public int getIntegerPreference(String key, int defaultValue) {
        SharedPreferences preferences = context.getSharedPreferences("preference_screen", Context.MODE_PRIVATE);
        return preferences.getInt(key, defaultValue);
    }

    @Override
    public void setIntegerPreference(String key, int value) {
        SharedPreferences preferences = context.getSharedPreferences("preference_screen", Context.MODE_PRIVATE);
        preferences.edit().putInt(key, value).apply();
    }
}
