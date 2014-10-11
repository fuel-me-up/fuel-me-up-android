package de.fuelmeup.test.unit;

import android.test.AndroidTestCase;

public abstract class UnitTest extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        // workaround for
        // <https://code.google.com/p/dexmaker/issues/detail?id=2> on API-18+
        System.setProperty("dexmaker.dexcache", getContext().getCacheDir().getPath());

    }

}