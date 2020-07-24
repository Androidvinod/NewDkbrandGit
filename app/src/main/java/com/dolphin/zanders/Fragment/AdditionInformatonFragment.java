package com.dolphin.zanders.Fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dolphin.zanders.Activity.NavigationActivity;
import com.dolphin.zanders.Adapter.AdditionalInfoAdapter;
import com.dolphin.zanders.R;
import com.dolphin.zanders.Retrofit.ApiClient;
import com.dolphin.zanders.Retrofit.ApiInterface;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdditionInformatonFragment extends Fragment {
    public static RecyclerView recv_addition_information;
    View v;
    public static AdditionalInfoAdapter additionalInfoAdapter;
    FrameLayout lv_additionalinfo_parent;
    ApiInterface api;
    LinearLayout lv_addition_pd;
    NavigationActivity parent;

    public AdditionInformatonFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_addition_informaton, container, false);
        api = ApiClient.getClient().create(ApiInterface.class);
        parent=(NavigationActivity) getActivity();
        AllocateMemory(v);
        hideKeyboard(parent);
        setupUI(lv_additionalinfo_parent);
        additionalInfoAdapter.addAll(ProductDetailFragment.additionals);
        return v;
    }

    private void AllocateMemory(View v) {
        lv_additionalinfo_parent=v.findViewById(R.id.lv_additionalinfo_parent);
        recv_addition_information=v.findViewById(R.id.recv_addition_information);
        lv_addition_pd=v.findViewById(R.id.lv_addition_pd);
        additionalInfoAdapter = new AdditionalInfoAdapter(parent);
        recv_addition_information.setLayoutManager(new LinearLayoutManager(parent, LinearLayoutManager.VERTICAL, false));
        recv_addition_information.setAdapter(additionalInfoAdapter);
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
