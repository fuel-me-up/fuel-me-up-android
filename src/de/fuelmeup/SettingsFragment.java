package de.fuelmeup;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class SettingsFragment extends PreferenceFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = super.onCreateView(inflater, container, savedInstanceState);
		view.setBackgroundColor(getResources().getColor(android.R.color.white));
		addPreferencesFromResource(R.xml.settings);
		return view;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    // inflate menu from xml
		menu.clear();
	    inflater.inflate(R.menu.main, menu);
	    MenuItem item = menu.findItem(R.id.action_settings);
	    item.setVisible(false);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			/*SharedPreferences settings = getActivity().getSharedPreferences(SETTINGS, 0);
			SharedPreferences.Editor editor = settings.edit();
			Preference pref =  this.findPreference("");
			getPreferenceScreen().get
			editor.putInt(PREF_MIN_FUEL, this.findPreference(""));
			editor.commit();*/
			getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
			getFragmentManager().popBackStackImmediate();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
