package de.fuelmeup.preferences;

import de.fuelmeup.resources.ResourceWrapper;

/**
 * Created by jonas on 11.10.14.
 */
public class IntegerPreference {
    PreferenceWrapper preferenceWrapper;
    String preferenceName;

    public IntegerPreference(PreferenceWrapper preferenceWrapper, String preferenceName) {
        this.preferenceWrapper = preferenceWrapper;
        this.preferenceName = preferenceName;
    }


    public int get(int defaultValue) {
        return preferenceWrapper.getIntegerPreference(preferenceName, defaultValue);
    }

    public void set(int value) {
        preferenceWrapper.setIntegerPreference(preferenceName, value);
    }
}
