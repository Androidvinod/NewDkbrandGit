package com.dolphin.zanders.Fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dolphin.zanders.Activity.NavigationActivity;
import com.dolphin.zanders.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuickOverviewFragment extends Fragment {
    public static TextView tv_quickoverview;
    NavigationActivity parent;

    public QuickOverviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_quick_overview, container, false);
        parent=(NavigationActivity) getActivity();
        tv_quickoverview=v.findViewById(R.id.tv_quickoverview);
        tv_quickoverview.setText(ProductDetailFragment.quickOverView);
        return v;
    }

}
