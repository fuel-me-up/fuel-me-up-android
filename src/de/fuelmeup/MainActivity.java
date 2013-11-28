package de.fuelmeup;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Main GUI activity that contains the tabhost.
 * 
 * @author jonas
 * 
 */
public class MainActivity extends Activity implements ActionBar.TabListener {

	public final static String TAG_CAR_LIST = "carlist";
	public final String TAG_CAR_MAP = "carmap";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Set up the action bar to show tabs.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		getActionBar().setDisplayHomeAsUpEnabled(false);

		// for each of the sections in the app, add a tab to the action bar.
		actionBar.addTab(actionBar.newTab().setText("tab1")
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText("tab2")
				.setTabListener(this));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.action_settings:
			// Create new fragment and transaction
			PreferenceFragment newFragment = new SettingsFragment();
			FragmentTransaction transaction = getFragmentManager()
					.beginTransaction();
			// Replace whatever is in the fragment_container view with this
			// fragment,
			// and add the transaction to the back stack
			transaction.replace(android.R.id.content, newFragment);
			transaction.addToBackStack(null);
			// Commit the transaction
			transaction.commit();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// When the given tab is selected, show the tab contents in the
		// container view.
		Fragment fragment = null;
		switch (tab.getPosition()) {
			case 0:
				fragment = new CarMapFragment();
				break;
			case 1:
				fragment = new CarListFragment();
				break;
			default:
				break;
		}
		Bundle args = new Bundle();
		fragment.setArguments(args);
		getFragmentManager().beginTransaction()
				.replace(R.id.tab_container, fragment).commit();

	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

}
