package com.dolphin.zanders.Fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dolphin.zanders.Activity.NavigationActivity;
import com.dolphin.zanders.Model.Homedk.Homedata;
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
public class Freight_Policy extends Fragment {
    WebView webView;
    Toolbar toolbar_freightpolicy;
    LinearLayout lv_main_freight,lv_termsandcondition_progress;
    TextView tv_privacypolicy;
    ApiInterface api;
    NavigationActivity parent;

    public Freight_Policy() {
        // Required empty public constructor
    }
///http://testgz.shop2.gzanders.com/media/docs/FREIGHT_POLICY.pdf

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_freight__policy, container, false);
        api = ApiClientcusome.getClient().create(ApiInterface.class);
        toolbar_freightpolicy=v.findViewById(R.id.toolbar_freightpolicy);
        lv_main_freight=v.findViewById(R.id.lv_main_freight);
        tv_privacypolicy=v.findViewById(R.id.tv_privacypolicy);
        lv_termsandcondition_progress=v.findViewById(R.id.lv_termsandcondition_progress);
     //   setupUI(lv_main_freight);
        parent=(NavigationActivity) getActivity();
     //   hideKeyboard(parent);

        setHasOptionsMenu(true);
        ((NavigationActivity) parent).setSupportActionBar(toolbar_freightpolicy);
        ((NavigationActivity) parent).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
        ((NavigationActivity) parent).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_36dp);
        if (CheckNetwork.isNetworkAvailable(getActivity())) {
            getprvicypolicy();
        } else {
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
        }

      /*  final ProgressDialog progressBar = new ProgressDialog(parent);
        progressBar.setMessage("Please wait...");
*/
      /*  webView.getSettings().setJavaScriptEnabled(true);
        String pdf = "http://testgz.shop2.gzanders.com/media/docs/FREIGHT_POLICY.pdf";
        webView.loadUrl("https://docs.google.com/gview?embedded=true&url="+ pdf);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (!progressBar.isShowing()) {
                    progressBar.show();
                }
            }

            public void onPageFinished(WebView view, String url) {
                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }
            }
        });*/

        return v;
    }

    private void getprvicypolicy() {

        lv_termsandcondition_progress.setVisibility(View.VISIBLE);
        callCategoryApi().enqueue(new Callback<PrivacyModel>() {
            @Override
            public void onResponse(Call<PrivacyModel> call, Response<PrivacyModel> response) {
                //  shimmer_view_catalog.stopShimmerAnimation();
                //   shimmer_view_catalog.setVisibility(View.GONE);
                lv_termsandcondition_progress.setVisibility(View.GONE);
                PrivacyModel categoryModel = response.body();
                Log.e("statussssss", "" + categoryModel.getContent());
                tv_privacypolicy.setText(HtmlCompat.fromHtml(categoryModel.getContent(), 0));
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
                Toast.makeText(getActivity(), ""+t, Toast.LENGTH_SHORT).show();
                Log.e("debug_175125", "pages: " + t);
            }
        });
    }
    private Call<PrivacyModel> callCategoryApi() {
        return api.getprivacy();
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
   /* public void setupUI(View view) {

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
    }*/

}
