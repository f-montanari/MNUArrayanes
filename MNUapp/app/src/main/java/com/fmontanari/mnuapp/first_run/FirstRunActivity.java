package com.fmontanari.mnuapp.first_run;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.Toast;

import com.fmontanari.mnuapp.R;
import com.fmontanari.mnuapp.SavedServers;
import com.fmontanari.mnuapp.data.DeviceInfo;

public class FirstRunActivity extends AppCompatActivity {

    static final int GET_SERVER_ADDRESS = 1;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private DeviceInfo deviceInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_run);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (NoSwipeViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // Default device info. Will get overwritten through the steps in this activity.
        deviceInfo = new DeviceInfo();
        deviceInfo.DeviceID = "DefaultTestID";
        deviceInfo.maxVotes = 10;

        // The FloatingActionButton changes sections. Basically acts as a "Next" button.
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mViewPager.getCurrentItem() != 2)
                {
                    // Which is our fragment?
                    switch (mViewPager.getCurrentItem())
                    {
                        case 0:
                            // Fragment 1: Set device name
                            Fragment f1 = mSectionsPagerAdapter.getRegisteredFragment(0);
                            EditText t = (EditText)f1.getActivity().findViewById(R.id.txtDeviceName);

                            for (Character c: t.getText().toString().toCharArray()) {
                                if(!c.toString().matches("^[a-zA-Z0-9]*$"))
                                {
                                    Toast.makeText(FirstRunActivity.this, "El nombre tiene caracteres inválidos", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }

                            deviceInfo.DeviceID = t.getText().toString();

                        break;
                        case 1:
                            // Fragment 2: Set max votes for this device.
                            Fragment f2 = mSectionsPagerAdapter.getRegisteredFragment(1);
                            EditText t2 = (EditText)f2.getActivity().findViewById(R.id.txtNumDelegaciones);
                            deviceInfo.maxVotes = Integer.parseInt(t2.getText().toString());
                        break;
                    }
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                    return;
                }

                // We're in the 3rd fragment. We already have the device info that we need. This fragment only handles
                // connection, so it's safe to save the device info.
                SharedPreferences connPrefs = getSharedPreferences("DevicePrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = connPrefs.edit();
                editor.putString("DeviceInfo", deviceInfo.toJSON());
                editor.commit();

                Intent i = new Intent();
                i.putExtra("info",deviceInfo.toJSON());
                setResult(RESULT_OK,i);
                finish();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_first_run, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * onClickListener for ConnectionsManager button.
     * Will show the ConnectionsManager Activity, that will setup a default connection data for this
     * device.
     * @param view The view that called this function.
     */
    public void showConnectionsManager(View view) {
        Intent theIntent = new Intent(this, SavedServers.class);
        startActivityForResult(theIntent, GET_SERVER_ADDRESS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == GET_SERVER_ADDRESS) {
            // This is the ConnectionsManager.
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

                // Get connection data from Activity
                String IP = data.getStringExtra("IP");
                int port = data.getIntExtra("Port", 0000);

                // Save the selected connection data as the default connection.
                SharedPreferences prefs = getSharedPreferences("SavedConnections", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("DefaultConnection", IP);
                editor.putInt("DefaultPort", port);
                editor.commit();
            }
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            switch(position)
            {
                case 0:
                    return new NameFragment();
                case 1:
                    return new CountriesCountFragment();
                case 2:
                    return new ConnectionsFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Modificar Info";
                case 1:
                    return "Delegaciones";
                case 2:
                    return "Conectarse";
            }
            return null;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        public Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }
    }
}
