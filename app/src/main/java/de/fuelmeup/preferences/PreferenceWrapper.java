package de.fuelmeup.preferences;

public interface PreferenceWrapper {
    public int getIntegerPreference(String key, int defaultValue);
    public void setIntegerPreference(String key, int value);
}
