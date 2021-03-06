package com.dolphin.zanders.Fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dolphin.zanders.Activity.NavigationActivity;
import com.dolphin.zanders.Adapter.Filterlist_Adapter;
import com.dolphin.zanders.Model.AboutUsModel.AboutusModel;
import com.dolphin.zanders.Model.PrivacyModel;
import com.dolphin.zanders.R;
import com.dolphin.zanders.Retrofit.ApiClient;
import com.dolphin.zanders.Retrofit.ApiClientcusome;
import com.dolphin.zanders.Retrofit.ApiInterface;
import com.dolphin.zanders.Util.CheckNetwork;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class About_usFregment extends Fragment {

    //rtrtrt
    TextView tv_textdata;
    ApiInterface api;
    Toolbar toolbar_about;
    LinearLayout lv_about_progress,lv_parent_aboutus;
    NavigationActivity parent;
    public About_usFregment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_about_us, container, false);
        parent=(NavigationActivity) getActivity();
        AllocateMemory(v);
        setHasOptionsMenu(true);
        hideKeyboard(parent);
        setupUI(lv_parent_aboutus);
        Filterlist_Adapter.filter_child_value_list.clear();
        api = ApiClientcusome.getClient().create(ApiInterface.class);

        ((NavigationActivity) parent).setSupportActionBar(toolbar_about);
        ((NavigationActivity) parent).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
        ((NavigationActivity) parent).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_36dp);

        if (CheckNetwork.isNetworkAvailable(parent)) {
            Aboutusapi();
        } else {
            Toast.makeText(parent, parent.getResources().getString(R.string.intcon), Toast.LENGTH_SHORT).show();
        }

        return v;
    }

    private void AllocateMemory(View v) {
        tv_textdata=v.findViewById(R.id.tv_textdata);
        lv_about_progress=v.findViewById(R.id.lv_about_progress);
        toolbar_about=v.findViewById(R.id.toolbar_about);
        lv_parent_aboutus=v.findViewById(R.id.lv_parent_aboutus);

    }
///  tv_textdata.setText( Html.fromHtml(response.body().getPage().getContent()));
    private void Aboutusapi() {
        lv_about_progress.setVisibility(View.VISIBLE);
        callCategoryApi().enqueue(new Callback<PrivacyModel>() {
            @Override
            public void onResponse(Call<PrivacyModel> call, Response<PrivacyModel> response) {
                //  shimmer_view_catalog.stopShimmerAnimation();
                //   shimmer_view_catalog.setVisibility(View.GONE);
                Log.e("response",""+response.toString());
                Log.e("response99",""+response.body());
                lv_about_progress.setVisibility(View.GONE);
                PrivacyModel categoryModel = response.body();
                tv_textdata.setText(HtmlCompat.fromHtml(categoryModel.getContent(), 0));
              /*  if (categoryModel.get().equalsIgnoreCase("Success")) {
                    //   shimmer_view_catalog.stopShimmerAnimation();
                    //   shimmer_view_catalog.setVisibility(View.GONE);
                    tv_privacypolicy.setText(HtmlCompat.fromHtml(categoryModel.getMainContent(), 0));
                } else {
                    //      shimmer_view_catalog.stopShimmerAnimation();
                    //    shimmer_view_catalog.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), categoryModel.getMessage(), Toast.LENGTH_SHORT).show();

                }*/
            }
            @Override
            public void onFailure(Call<PrivacyModel> call, Throwable t) {
                lv_about_progress.setVisibility(View.GONE);
                Toast.makeText(getActivity(), ""+t, Toast.LENGTH_SHORT).show();
                Log.e("debug_175125", "pages: " + t);
            }
        });
    }

    private Call<PrivacyModel> callCategoryApi() {
        return api.getaboutus();
    }
    @Override

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        //   inflater.inflate(R.menu.menu_home, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case android.R.id.home:
                parent.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
