package com.dolphin.zanders.Fragment;


import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

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


import com.dolphin.zanders.Activity.NavigationActivity;
import com.dolphin.zanders.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Terms_conditions extends Fragment {
    LinearLayout lv_termsandcondition_progress;
    WebView webView;
    Toolbar toolbar_termscondition;
    ProgressDialog dialog;
    NavigationActivity parent;


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
        webView = v.findViewById(R.id.webView);
        lv_termsandcondition_progress = v.findViewById(R.id.lv_termsandcondition_progress);
        toolbar_termscondition = v.findViewById(R.id.toolbar_termscondition);

        setHasOptionsMenu(true);
        ((NavigationActivity) parent).setSupportActionBar(toolbar_termscondition);
        ((NavigationActivity) parent).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
        ((NavigationActivity) parent).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_36dp);


        final ProgressDialog progressBar = new ProgressDialog(getContext());
        progressBar.setMessage("Please wait...");

        webView.getSettings().setJavaScriptEnabled(true);
        String pdf = "http://testgz.shop2.gzanders.com/media/docs/TERMSANDCONDITIONS.pdf";
        webView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + pdf);
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
        });

        /*webView.getSettings().setJavaScriptEnabled(true);
        String pdf = "http://testgz.shop2.gzanders.com/media/docs/TERMSANDCONDITIONS.pdf";
        webView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + pdf);

        webView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            public void onLoadResource(WebView view, String url) {
                if (dialog == null) {
                    dialog = new ProgressDialog(getContext());
                    dialog.setMessage("Loading...");
                    dialog.show();
                }
            }

            public void onPageFinished(WebView view, String url) {
                try {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                        dialog = null;
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });*/

        return v;
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
