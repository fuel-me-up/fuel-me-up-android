package de.fuelmeup;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.Menu;


/**
 * Main GUI activity that contains the tabhost.
 * @author jonas
 *
 */
public class MainActivity extends FragmentActivity {
 

	public final static String TAG_CAR_LIST = "carlist";
	public final String TAG_CAR_MAP = "carmap";

    private FragmentTabHost mTabHost;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //Create tabhost and add fragments
        
        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.tabFrameLayout);

        mTabHost.addTab(mTabHost.newTabSpec("list").setIndicator("Liste"),
        		CarListFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("map").setIndicator("Maps"),
        		CarMapFragment.class, null);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
     
    
}
