package de.fuelmeup.ui.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.Menu;
import android.view.MenuItem;

import de.fuelmeup.R;
import de.fuelmeup.ui.fragment.CarMapFragment;
import de.fuelmeup.ui.fragment.SettingsFragment;

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

        getActionBar().setDisplayHomeAsUpEnabled(false);
        Fragment fragment = new CarMapFragment();
        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.tab_container, fragment, TAG_CAR_MAP);
        transaction.commit();
        //getFragmentManager().addOnBackStackChangedListener(getListener());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                PreferenceFragment newFragment = new SettingsFragment();
                FragmentTransaction transaction = getFragmentManager()
                        .beginTransaction();
                transaction.replace(android.R.id.content, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

	/*private OnBackStackChangedListener getListener() {
        OnBackStackChangedListener result = new OnBackStackChangedListener() {
			public void onBackStackChanged() {
				FragmentManager manager = getFragmentManager();
				if (manager != null) {
					BaseFragment currFragment = (BaseFragment) manager
							.findFragmentByTag(TAG_CAR_MAP);
					if(currFragment != null)
						currFragment.onResumeFragment();
				}
			}
		};

		return result;
	}*/

}
