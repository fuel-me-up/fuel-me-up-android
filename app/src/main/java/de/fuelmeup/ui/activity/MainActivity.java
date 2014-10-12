package de.fuelmeup.ui.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

import de.fuelmeup.R;
import de.fuelmeup.ui.fragment.CarMapFragment;

/**
 * Main GUI activity.
 *
 * @author jonas
 */
public class MainActivity extends Activity {

    public final String TAG_CAR_MAP = "carmap";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment fragment = new CarMapFragment();
        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.content, fragment, TAG_CAR_MAP);
        transaction.commit();
    }

}
