package com.fmontanari.mnuapp.first_run;

/**
 * Created by Carlito on 28/04/2017.
 */

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fmontanari.mnuapp.R;

public class ConnectionsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_connections, container, false);
        return rootView;
    }
}
