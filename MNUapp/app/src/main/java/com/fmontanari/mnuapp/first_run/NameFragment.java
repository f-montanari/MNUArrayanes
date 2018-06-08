package com.fmontanari.mnuapp.first_run;

/**
 * Created by Franco Montanari on 28/04/2017.
 */

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.fmontanari.mnuapp.R;

public class NameFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_name, container, false);
        return rootView;
    }



}
