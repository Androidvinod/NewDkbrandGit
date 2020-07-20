package com.dolphin.zanders.Fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.dolphin.zanders.Activity.NavigationActivity;
import com.dolphin.zanders.Adapter.CurrentOrderAdapter;
import com.dolphin.zanders.Model.CurrentOrderModel;
import com.dolphin.zanders.R;
import com.dolphin.zanders.Retrofit.ApiClient;
import com.dolphin.zanders.Retrofit.ApiInterface;
import com.dolphin.zanders.Util.CheckNetwork;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentOrder extends Fragment {
    NavigationActivity parent;

    View v;
    RecyclerView rv_current_orders;
    private static CurrentOrderAdapter currentOrderAdapter;
    private ShimmerFrameLayout mShimmerViewContainer;
    public static List<CurrentOrderModel> currentOrderModels = new ArrayList<CurrentOrderModel>();
    public CurrentOrder() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_current_order, container, false);
        parent=(NavigationActivity) getActivity();
        AllocateMemory();
        hideKeyboard(parent);
        if (CheckNetwork.isNetworkAvailable(parent)) {
            CALL_Current_Orders_API();
        } else {
            Toast.makeText(parent,parent.getResources().getString(R.string.nointernet), Toast.LENGTH_SHORT).show();
        }

        return v;
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
    private void CALL_Current_Orders_API() {
        for (int i = 0; i < 20; i++) {
            currentOrderModels.add(new CurrentOrderModel("","","","",""));
        }
        currentOrderAdapter.notifyDataSetChanged();
    }

    private void AllocateMemory() {

        /*lv_no_data_found = v.findViewById(R.id.lv_no_data_found);
        lv_maincategoty_products_main = v.findViewById(R.id.lv_maincategoty_products_main);*/
        mShimmerViewContainer = v.findViewById(R.id.shimmer_view_container);
        //lv_pb_categoryproductlist = v.findViewById(R.id.lv_pb_categoryproductlist);
        //nested_scroll_category_products = v.findViewById(R.id.nested_scroll_category_products);
        rv_current_orders=v.findViewById(R.id.rv_current_orders);
        rv_current_orders.getItemAnimator().setChangeDuration(700);
        currentOrderAdapter = new CurrentOrderAdapter(parent,currentOrderModels);
        rv_current_orders.setLayoutManager(new LinearLayoutManager(parent, LinearLayoutManager.VERTICAL, false));
        rv_current_orders.setAdapter(currentOrderAdapter);
    }
    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        mShimmerViewContainer.stopShimmerAnimation();
        super.onPause();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        //inflater.inflate(R.menu.menu_home, menu);

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
