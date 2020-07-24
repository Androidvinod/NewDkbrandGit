package com.dolphin.zanders.Fragment;


import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.webkit.WebView;

import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.dolphin.zanders.Activity.NavigationActivity;
import com.dolphin.zanders.Model.PrivacyModel;
import com.dolphin.zanders.R;
import com.dolphin.zanders.Retrofit.ApiClientcusome;
import com.dolphin.zanders.Retrofit.ApiInterface;
import com.dolphin.zanders.Util.CheckNetwork;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class Terms_conditions extends Fragment {
    LinearLayout lv_termsandcondition_progress;
    WebView webView;
    Toolbar toolbar_termscondition;
    TextView tv_termscondition;
    ProgressDialog dialog;
    NavigationActivity parent;
    ApiInterface api;


    public Terms_conditions() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_terms_conditions, container, false);

        //pb_tnc = v.findViewById(R.id.pb_tnc);
        parent=(NavigationActivity) getActivity();
        api = ApiClientcusome.getClient().create(ApiInterface.class);
        lv_termsandcondition_progress = v.findViewById(R.id.lv_termsandcondition_progress);
        toolbar_termscondition = v.findViewById(R.id.toolbar_termscondition);
        tv_termscondition = v.findViewById(R.id.tv_termscondition);

        setHasOptionsMenu(true);
        ((NavigationActivity) parent).setSupportActionBar(toolbar_termscondition);
        ((NavigationActivity) parent).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
        ((NavigationActivity) parent).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_36dp);

        if (CheckNetwork.isNetworkAvailable(parent)) {
            termscondition();
        } else {
            Toast.makeText(parent, parent.getResources().getString(R.string.intcon), Toast.LENGTH_SHORT).show();
        }


        return v;
    }

    private void termscondition() {
        lv_termsandcondition_progress.setVisibility(View.VISIBLE);
        callCategoryApi().enqueue(new Callback<PrivacyModel>() {
            @Override
            public void onResponse(Call<PrivacyModel> call, Response<PrivacyModel> response) {
                //  shimmer_view_catalog.stopShimmerAnimation();
                //   shimmer_view_catalog.setVisibility(View.GONE);
                Log.e("response",""+response.toString());
                Log.e("response99",""+response.body());
                lv_termsandcondition_progress.setVisibility(View.GONE);
                PrivacyModel categoryModel = response.body();
                tv_termscondition.setText(HtmlCompat.fromHtml(categoryModel.getContent(), 0));
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
                lv_termsandcondition_progress.setVisibility(View.GONE);
                Toast.makeText(getActivity(), ""+t, Toast.LENGTH_SHORT).show();
                Log.e("debug_175125", "pages: " + t);
            }
        });
    }

    private Call<PrivacyModel> callCategoryApi() {
        return api.gettrems();
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

}
