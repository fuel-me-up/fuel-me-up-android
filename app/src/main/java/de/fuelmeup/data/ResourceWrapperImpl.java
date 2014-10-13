package de.fuelmeup.data;

import android.content.Context;

import javax.inject.Inject;

import de.fuelmeup.resources.ResourceWrapper;


public class ResourceWrapperImpl implements ResourceWrapper {
    private Context context;

    @Inject
    public ResourceWrapperImpl(Context context) {
        this.context = context;
    }


    @Override
    public String getString(String key) {
        String packageName = context.getPackageName();
        int resId = 0;

        if (packageName != null && key != null) {
            resId = context.getResources().getIdentifier(key, "string", packageName);

            if (resId == 0)
                return null;

            return context.getString(resId);
        }

        return null;
    }
}
