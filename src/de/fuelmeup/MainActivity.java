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
		actionBar.addTab(actionBar.newTab().setText(getString(R.string.map))
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(getString(R.string.list))
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

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		Fragment fragment = null;
		String tag;
		FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();

		// at first detach currently visible fragment from UI
		Fragment currShowingFragment = getFragmentManager().findFragmentById(
				R.id.tab_container);
		if (currShowingFragment != null)
			transaction.detach(currShowingFragment);

		// check if fragment is already available in FragmentManager if so just
		// re-attach it to UI else create a new instance
		switch (tab.getPosition()) {
		case 0:
			tag = TAG_CAR_MAP;
			fragment = getFragmentManager().findFragmentByTag(tag);
			if (fragment == null) {
				fragment = new CarMapFragment();
				transaction.add(R.id.tab_container, fragment, tag);
			} else
				transaction.attach(fragment);
			break;
		case 1:
			tag = TAG_CAR_LIST;
			fragment = getFragmentManager().findFragmentByTag(tag);
			if (fragment == null) {
				fragment = new CarListFragment();
				transaction.add(R.id.tab_container, fragment, tag);
			} else
				transaction.attach(fragment);
			break;
		default:
			break;
		}
		transaction.commitAllowingStateLoss();
		getFragmentManager().executePendingTransactions();
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

}
