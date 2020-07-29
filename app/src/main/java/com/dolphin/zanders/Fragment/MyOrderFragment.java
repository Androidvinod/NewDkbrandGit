package com.dolphin.zanders.Fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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
import com.dolphin.zanders.Adapter.MyOrderAdapter;
import com.dolphin.zanders.Adapter.NewOrderListAdapter;
import com.dolphin.zanders.Model.NewOrderModel.Item;
import com.dolphin.zanders.Model.NewOrderModel.NewOrderModel;
import com.dolphin.zanders.Model.Ordermodel.Order;
import com.dolphin.zanders.Preference.Login_preference;
import com.dolphin.zanders.R;
import com.dolphin.zanders.Retrofit.ApiClientcusome;
import com.dolphin.zanders.Retrofit.ApiInterface;
import com.dolphin.zanders.Util.CheckNetwork;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyOrderFragment extends Fragment {
    View v;
    Toolbar toolbar_myorders;
    LinearLayout lv_nodata_muorder;
    LinearLayout lv_progress_myorder_bottom,lv_order_main;
    NestedScrollView nested_scroll_myorder;
    NavigationActivity parent;
    NewOrderListAdapter newOrderListAdapter;

    TextView tv_messgenoti,tv_account_myorder;
    RecyclerView rv_current_orders;
    private static MyOrderAdapter myOrderAdapter;
    private ShimmerFrameLayout shimmer_view_order;
    public static List<Order> orderList = new ArrayList<Order>();
    ApiInterface apiInterface;
    //pagination
    LinearLayoutManager linearLayoutManager;
    int pastvisibleitem, visibleitemcount, totalitemcount;
    int page_no = 1, page;
    int isLastPage;
    public MyOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       v= inflater.inflate(R.layout.fragment_my_order, container, false);
        AllocateMemory();
        setupUI(lv_order_main);
        apiInterface = ApiClientcusome.getClient().create(ApiInterface.class);
        setHasOptionsMenu(true);
        AttachRecyclerView();
        if(getActivity()!=null)
        {
            parent=(NavigationActivity) getActivity();
        }

        hideKeyboard(parent);

        ((NavigationActivity) parent).setSupportActionBar(toolbar_myorders);
        ((NavigationActivity) parent).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
        ((NavigationActivity) parent).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_36dp);


        if(CheckNetwork.isNetworkAvailable(parent))
        {
           callMyOrderListApi(page_no);
        }else {
            Toast.makeText(parent, parent.getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
        }

        return v;
    }


    private void callMyOrderListApi(int pageno) {

            Log.e("debug_page1", "" + pageno);
            lv_progress_myorder_bottom.setVisibility(View.GONE);
            orderList.clear();
            lv_nodata_muorder.setVisibility(View.GONE);
            nested_scroll_myorder.setVisibility(View.VISIBLE);


        callorderapi(page).enqueue(new Callback<NewOrderModel>() {
            @Override
            public void onResponse(Call<NewOrderModel> call, Response<NewOrderModel> response) {
                NewOrderModel model = response.body();
                lv_nodata_muorder.setVisibility(View.GONE);
                nested_scroll_myorder.setVisibility(View.VISIBLE);
                lv_progress_myorder_bottom.setVisibility(View.GONE);
                Log.e("12_168", "" + response);
                Log.e("isge_168", "" + response.body());
                Log.e("is22Page_168", "" + response.body().toString());

                if (response.code()==200 || response.isSuccessful()) {

                    Log.e("isLastPage_168", "" + isLastPage);
                    if (fetchResults(response) == null || fetchResults(response).size() == 0) {
                        Log.e("debug_166", "" + fetchResults(response));
                        shimmer_view_order.stopShimmerAnimation();
                        shimmer_view_order.setVisibility(View.GONE);
                        lv_nodata_muorder.setVisibility(View.VISIBLE);
                        nested_scroll_myorder.setVisibility(View.GONE);
                        lv_progress_myorder_bottom.setVisibility(View.GONE);
                        tv_messgenoti.setText(parent.getResources().getString(R.string.noorder));

                    } else {
                        Log.e("debug_174", "" + fetchResults(response));
                        shimmer_view_order.stopShimmerAnimation();
                        shimmer_view_order.setVisibility(View.GONE);
                        lv_nodata_muorder.setVisibility(View.GONE);
                        nested_scroll_myorder.setVisibility(View.VISIBLE);
                        lv_progress_myorder_bottom.setVisibility(View.GONE);

                        List<Item> results = fetchResults(response);
                        if (results.isEmpty()) {
                            shimmer_view_order.stopShimmerAnimation();
                            shimmer_view_order.setVisibility(View.GONE);
                            lv_nodata_muorder.setVisibility(View.VISIBLE);
                            nested_scroll_myorder.setVisibility(View.GONE);
                            lv_progress_myorder_bottom.setVisibility(View.GONE);
                            tv_messgenoti.setText(parent.getResources().getString(R.string.noorder));

                        } else {
                            shimmer_view_order.stopShimmerAnimation();
                            shimmer_view_order.setVisibility(View.GONE);
                            lv_nodata_muorder.setVisibility(View.GONE);
                            nested_scroll_myorder.setVisibility(View.VISIBLE);
                            lv_progress_myorder_bottom.setVisibility(View.GONE);

                            newOrderListAdapter.addAll(results);

                        }
                    }
                } else {
                    shimmer_view_order.stopShimmerAnimation();
                    shimmer_view_order.setVisibility(View.GONE);
                    lv_nodata_muorder.setVisibility(View.GONE);
                    nested_scroll_myorder.setVisibility(View.VISIBLE);
                    lv_progress_myorder_bottom.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<NewOrderModel> call, Throwable t) {
                // progressBar_review.setVisibility(View.GONE);
                shimmer_view_order.stopShimmerAnimation();
                shimmer_view_order.setVisibility(View.GONE);
                lv_nodata_muorder.setVisibility(View.GONE);
                nested_scroll_myorder.setVisibility(View.VISIBLE);
                lv_progress_myorder_bottom.setVisibility(View.GONE);

                Log.e("debug_186","="+t.getLocalizedMessage());
                Toast.makeText(parent, "" + parent.getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private List<Item> fetchResults(Response<NewOrderModel> response) {
        NewOrderModel getManufacturelistModel = response.body();
        return getManufacturelistModel.getItems();
    }

    private Call<NewOrderModel> callorderapi(int page) {
        Log.e("customerid","dff"+Login_preference.getcustomer_id(parent));

        //http://dkbraende.demoproject.info/rest/V1/orders/?searchCriteria[filterGroups][0][filters][0][field]=customer_id&searchCriteria[filterGroups][0][filters][0][value]=12517&searchCriteria[filterGroups][0][filters][0][conditionType]=eq
        String url=ApiClientcusome.MAIN_URLL+"orders/?searchCriteria[filterGroups][0][filters][0][field]=customer_id&searchCriteria[filterGroups][0][filters][0][value]="
                +Login_preference.getcustomer_id(parent)+"&searchCriteria[filterGroups][0][filters][0][conditionType]=eq";
        Log.e("authtoken","dff"+Login_preference.gettoken(parent));
        Log.e("customertoken","dff"+Login_preference.getCustomertoken(parent));
        Log.e("url22","dff"+url);


        return apiInterface.getorderListData("Bearer "+Login_preference.gettoken(parent),url);
    }

    private void AttachRecyclerView() {
        newOrderListAdapter = new NewOrderListAdapter(getActivity());
        linearLayoutManager = new LinearLayoutManager(parent,LinearLayoutManager.VERTICAL, false);
        rv_current_orders.setLayoutManager(linearLayoutManager);
        rv_current_orders.setAdapter(newOrderListAdapter);
    }

    private void AllocateMemory() {
        tv_account_myorder=v.findViewById(R.id.tv_account_myorder);
        lv_order_main=v.findViewById(R.id.lv_order_main);
        tv_messgenoti=v.findViewById(R.id.tv_messgenoti);
        toolbar_myorders=v.findViewById(R.id.toolbar_myorders);
        lv_nodata_muorder=v.findViewById(R.id.lv_nodata_muorder);
        lv_progress_myorder_bottom=v.findViewById(R.id.lv_progress_myorder_bottom);
        nested_scroll_myorder=v.findViewById(R.id.nested_scroll_myorder);

        shimmer_view_order = v.findViewById(R.id.shimmer_view_order);
        rv_current_orders=v.findViewById(R.id.rv_current_orders);
        tv_account_myorder.setTypeface(NavigationActivity.montserrat_semibold);
        
      
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
    @Override
    public void onResume() {
        super.onResume();
        shimmer_view_order.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        shimmer_view_order.stopShimmerAnimation();
        super.onPause();
    }
}
