package com.fmontanari.mnuapp.first_run;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fmontanari.mnuapp.R;
import com.fmontanari.mnuapp.SavedDevices;
import com.fmontanari.mnuapp.data.DeviceInfo;

import java.io.UnsupportedEncodingException;

public class FirstRunActivity extends AppCompatActivity {

    static final int GET_SERVER_ADDRESS = 1;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private DeviceInfo deviceInfo;

    public static final boolean isUTF8(final byte[] pText) {

        int expectedLength = 0;

        for (int i = 0; i < pText.length; i++) {
            if ((pText[i] & 0b10000000) == 0b00000000) {
                expectedLength = 1;
            } else if ((pText[i] & 0b11100000) == 0b11000000) {
                expectedLength = 2;
            } else if ((pText[i] & 0b11110000) == 0b11100000) {
                expectedLength = 3;
            } else if ((pText[i] & 0b11111000) == 0b11110000) {
                expectedLength = 4;
            } else if ((pText[i] & 0b11111100) == 0b11111000) {
                expectedLength = 5;
            } else if ((pText[i] & 0b11111110) == 0b11111100) {
                expectedLength = 6;
            } else {
                return false;
            }

            while (--expectedLength > 0) {
                if (++i >= pText.length) {
                    return false;
                }
                if ((pText[i] & 0b11000000) != 0b10000000) {
                    return false;
                }
            }
        }

        return true;
    }

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

        deviceInfo = new DeviceInfo();
        deviceInfo.DeviceID = "DefaultTestID";
        deviceInfo.maxVotes = 10;




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mViewPager.getCurrentItem() != 2)
                {
                    switch (mViewPager.getCurrentItem())
                    {
                        case 0:
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
                            Fragment f2 = mSectionsPagerAdapter.getRegisteredFragment(1);
                            EditText t2 = (EditText)f2.getActivity().findViewById(R.id.txtNumDelegaciones);
                            deviceInfo.maxVotes = Integer.parseInt(t2.getText().toString());
                        break;
                    }
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                    return;
                }

                SharedPreferences connPrefs = getSharedPreferences("DevicePrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = connPrefs.edit();
                editor.putString("DeviceInfo", deviceInfo.toJSON());
                editor.commit();

                Intent i = new Intent();
                i.putExtra("info",deviceInfo.toJSON());
                //Log.i("First Run", "Putting extra data:" + deviceInfo.toJSON());
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

    public void showConnectionsManager(View view) {
        Intent theIntent = new Intent(this, SavedDevices.class);
        startActivityForResult(theIntent, GET_SERVER_ADDRESS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == GET_SERVER_ADDRESS) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

                String IP = data.getStringExtra("IP");
                int port = data.getIntExtra("Port", 0000);

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
            Log.i("First Run", "Position " + position);
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
