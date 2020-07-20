package com.dolphin.zanders.Fragment;


import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.dolphin.zanders.Activity.NavigationActivity;
import com.dolphin.zanders.Adapter.MyOrdersPagerAdapter;
import com.dolphin.zanders.R;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyOrders extends Fragment implements TabLayout.OnTabSelectedListener {

    View v;
    TabLayout myorders_tabLayout;
    ViewPager myorders_pager;
    Toolbar toolbar_myorders;
    NavigationActivity parent;

    public MyOrders() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_my_orders, container, false);
        parent=(NavigationActivity) getActivity();
        setHasOptionsMenu(true);
        toolbar_myorders = v.findViewById(R.id.toolbar_myorders);
        hideKeyboard(parent);

        ((NavigationActivity) parent).setSupportActionBar(toolbar_myorders);
        ((NavigationActivity) parent).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
        ((NavigationActivity) parent).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_36dp);
        toolbar_myorders.setTitle("My Orders");
        toolbar_myorders.setTitleTextColor(parent.getResources().getColor(R.color.colorPrimary));

        myorders_tabLayout = v.findViewById(R.id.myorders_tabLayout);
        myorders_pager = v.findViewById(R.id.myorders_pager);

        myorders_tabLayout.setTabTextColors(parent.getResources().getColor(R.color.black), parent.getResources().getColor(R.color.black));
        myorders_tabLayout.setTabTextColors(ColorStateList.valueOf(parent.getResources().getColor(R.color.black)));
        myorders_tabLayout.setBackgroundColor(parent.getResources().getColor(R.color.white));
        myorders_tabLayout.setSelectedTabIndicatorColor(parent.getResources().getColor(R.color.colorPrimary));
        myorders_tabLayout.addTab(myorders_tabLayout.newTab().setText("Past Order"));
        myorders_tabLayout.addTab(myorders_tabLayout.newTab().setText("Current Order"));
        myorders_tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        MyOrdersPagerAdapter adapter = new MyOrdersPagerAdapter(parent.getSupportFragmentManager(), myorders_tabLayout.getTabCount());
        myorders_pager.setAdapter(adapter);
        /*myorders_tabLayout.setupWithViewPager(myorders_pager);*/
        myorders_tabLayout.setOnTabSelectedListener(this);


        /*Spannable wordtoSpan = new SpannableString(String.valueOf(myorders_tabLayout.getText()));
        wordtoSpan.setSpan(new StyleSpan(Typeface.BOLD), 0, wordtoSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        myorders_tabLayout.setText(wordtoSpan);*/

        myorders_pager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        myorders_tabLayout.getTabAt(position).select();
                    }
                });

        return v;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        myorders_pager.setCurrentItem(tab.getPosition());
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

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
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
