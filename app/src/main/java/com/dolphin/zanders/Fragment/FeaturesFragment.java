package com.dolphin.zanders.Fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dolphin.zanders.Activity.NavigationActivity;
import com.dolphin.zanders.Adapter.FeaturesAdapter;

import com.dolphin.zanders.Model.FeaturesModel;

import com.dolphin.zanders.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeaturesFragment extends Fragment {
    View v;
    private List<FeaturesModel> featuresModels = new ArrayList<FeaturesModel>();
    RecyclerView recv_features;
    FeaturesAdapter featuresAdapter;
    FrameLayout lv_features_main;
    NavigationActivity parent;

    public static TextView tv_features;
    public FeaturesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_features, container, false);
        AllocateMemory();
        parent=(NavigationActivity) getActivity();
        setupUI(lv_features_main);
        hideKeyboard(parent);
        tv_features.setText(ProductDetailFragment.features);
        return v;
    }



    private void AllocateMemory() {
      //  recv_features=v.findViewById(R.id.recv_features);
        tv_features=v.findViewById(R.id.tv_features);
        lv_features_main=v.findViewById(R.id.lv_features_main);
    }


    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideKeyboard(parent);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View currentFocusedView = activity.getCurrentFocus();
        if (currentFocusedView != null) {
            inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
