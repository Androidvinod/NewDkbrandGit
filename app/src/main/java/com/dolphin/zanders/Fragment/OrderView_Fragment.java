package com.dolphin.zanders.Fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
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
import com.dolphin.zanders.Adapter.Productlistvieworder_Adapter;
import com.dolphin.zanders.Model.GetAddresslistModel.GetAddressModel;
import com.dolphin.zanders.Model.NewOrderDetailModel.Item;
import com.dolphin.zanders.Model.NewOrderDetailModel.NewOrderDetailModel;
import com.dolphin.zanders.Model.OrderView_model.OrderviewModel;
import com.dolphin.zanders.Model.OrderView_model.Product;
import com.dolphin.zanders.Preference.Login_preference;
import com.dolphin.zanders.R;
import com.dolphin.zanders.Retrofit.ApiClient;
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
public class OrderView_Fragment extends Fragment {

    TextView tv_subtotal_value,tv_defaultbillingadd,tv_defaultshippingadd, tv_shipping_data,tv_payment_data,tv_discount,tv_discount_valueeeeee,
            tv_purchashordernumber,tv_shippickupdate,tv_misccharges,tv_salestex,tv_grandtotal;
    RecyclerView rv_productlist;
    LinearLayout lv_vieworder_progress,lv_parent_oreder_view;
    LinearLayoutManager linearLayoutManager;
    Productlistvieworder_Adapter productlistvieworder_adapter;
    String order_id;
    Bundle b;
    Toolbar toolbar_orderdetails;
    ShimmerFrameLayout shimmer_shipping;
    ApiInterface api;
    NavigationActivity parent;

    public OrderView_Fragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_order_view_, container, false);
        api = ApiClient.getClient().create(ApiInterface.class);
        parent=(NavigationActivity) getActivity();
        AllocateMemory(v);
        setupUI(lv_parent_oreder_view);
        setHasOptionsMenu(true);
        ((NavigationActivity) parent).setSupportActionBar(toolbar_orderdetails);
        ((NavigationActivity) parent).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
        ((NavigationActivity) parent).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_36dp);
        hideKeyboard(parent);


        b = this.getArguments();

        if (b != null) {
            order_id=b.getString("order_id");
            if (CheckNetwork.isNetworkAvailable(parent)) {
                orderviewapi(order_id);
            } else {
                Toast.makeText(parent, parent.getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
            }
        }else {
        }
        return v;
    }
    private void AllocateMemory(View v) {
        tv_discount_valueeeeee=v.findViewById(R.id.tv_discount_valueeeeee);
        tv_discount=v.findViewById(R.id.tv_discount);
        lv_parent_oreder_view=v.findViewById(R.id.lv_parent_oreder_view);
        lv_vieworder_progress=v.findViewById(R.id.lv_vieworder_progress);
        tv_subtotal_value=v.findViewById(R.id.tv_subtotal_valueeeeee);
        tv_defaultshippingadd=v.findViewById(R.id.tv_defaultshippingadd);
        tv_defaultbillingadd=v.findViewById(R.id.tv_defaultbillingadd);
        tv_shipping_data=v.findViewById(R.id.tv_shipping_data);
        tv_payment_data=v.findViewById(R.id.tv_payment_data);
        tv_purchashordernumber=v.findViewById(R.id.tv_purchashordernumber);
        tv_shippickupdate=v.findViewById(R.id.tv_shippickupdate);
        tv_misccharges=v.findViewById(R.id.tv_misccharges);
        tv_salestex=v.findViewById(R.id.tv_salestex);
        tv_grandtotal=v.findViewById(R.id.tv_grandtotal);
        rv_productlist=v.findViewById(R.id.rv_productlist);
        shimmer_shipping=v.findViewById(R.id.shimmer_shipping);
        toolbar_orderdetails=v.findViewById(R.id.toolbar_orderdetails);

        productlistvieworder_adapter = new Productlistvieworder_Adapter(parent);
        linearLayoutManager = new LinearLayoutManager(parent,LinearLayoutManager.VERTICAL, false);
        rv_productlist.setLayoutManager(linearLayoutManager);
        rv_productlist.setAdapter(productlistvieworder_adapter);


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

    private void orderviewapi(String increment_id) {
        lv_vieworder_progress.setVisibility(View.VISIBLE);
        shimmer_shipping.setVisibility(View.VISIBLE);
        callorderviewapi(increment_id).enqueue(new Callback<NewOrderDetailModel>() {
            @Override
            public void onResponse(Call<NewOrderDetailModel> call, Response<NewOrderDetailModel> response) {
                Log.e("response_check", "" + response.body());
                Log.e("response_77_checkout", "" + response);
                lv_vieworder_progress.setVisibility(View.GONE);

                if (response.code()==200 || response.isSuccessful()) {

                    if(response.body().getBillingAddress()==null )
                    {
                        tv_defaultbillingadd.setText("No Billing Address Found");
                    }else {
                        List<String> streetlist=new ArrayList<>();
                        streetlist=response.body().getBillingAddress().getStreet();
                        tv_defaultbillingadd.setText(response.body().getBillingAddress().getFirstname()+" "+response.body().getBillingAddress().getLastname()
                                + "\n" + streetlist.get(0) +  " ," + response.body().getBillingAddress().getCity() + " ,"
                                +response.body().getBillingAddress().getPostcode() + "\n" +   "T : " +response.body().getBillingAddress().getTelephone());
                        //      Log.e("address", "77   " + response.body().getBillingAddress().getName() + "\n" + response.body().getBillingAddress().getStreet() + "\n" + response.body().getBillingAddress().getRegion() + "," + response.body().getBillingAddress().getCity() + "," + response.body().getBillingAddress().getPostcode() + "\n" + response.body().getBillingAddress().getCountry() + "\n" + "T :" + response.body().getBillingAddress().getTelephone());

                    }



                    if(response.body().getExtensionAttributes().getShippingAssignments().get(0).getShipping().getAddress()==null)
                    {
                        tv_defaultshippingadd.setText("No Shipping Address Found");
                    }else {
                        List<String> streetlist_shiping=new ArrayList<>();
                        streetlist_shiping=response.body().getExtensionAttributes().getShippingAssignments().get(0).getShipping().getAddress().getStreet();
                        tv_defaultshippingadd.setText(response.body().getExtensionAttributes().getShippingAssignments().get(0).getShipping().getAddress().getFirstname() +" "
                                +response.body().getExtensionAttributes().getShippingAssignments().get(0).getShipping().getAddress().getLastname() +
                                "\n" + streetlist_shiping.get(0) + ", " +
                                response.body().getExtensionAttributes().getShippingAssignments().get(0).getShipping().getAddress().getCity() + "," +
                                response.body().getExtensionAttributes().getShippingAssignments().get(0).getShipping().getAddress().getPostcode() + "\n" +
                                "T : " +response.body().getExtensionAttributes().getShippingAssignments().get(0).getShipping().getAddress().getTelephone());
                    }

                    /*  if (response.body().getExtensionAttributes().getShippingAssignments().get(0).getShipping().getAddress()==null){
                        tv_shipping_data.setText("No shipping information available");
                    }else{
                        tv_shipping_data.setText(response.body().getOrder().getDeliveryMethod().getShippingDescription());
                    }*/

                    tv_payment_data.setText(response.body().getPayment().getMethod());
                    tv_purchashordernumber.setText("Purchase Order Number : "+response.body().getIncrementId());
                    tv_shippickupdate.setText("Ship/Pick Up Date : "+response.body().getCreatedAt());

                    tv_subtotal_value.setText(String.valueOf(response.body().getBaseSubtotal())+" "+Login_preference.getcurrencycode(getActivity()));
                    tv_grandtotal.setText(String.valueOf(response.body().getSubtotalInclTax())+" "+Login_preference.getcurrencycode(getActivity()));
                    tv_salestex.setText(String.valueOf(response.body().getBaseTaxAmount())+" "+Login_preference.getcurrencycode(getActivity()));
                    tv_discount_valueeeeee.setText(String.valueOf(response.body().getDiscountAmount())+" "+Login_preference.getcurrencycode(getActivity()));

                    List<Item> results = response.body().getItems();
                    shimmer_shipping.setVisibility(View.GONE);
                    Log.e("debug_995153377tax", "" + response.body().getSubtotalInclTax());
                    productlistvieworder_adapter.addAll(results);

                } else {
                    // tv_no_addressfafound.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<NewOrderDetailModel> call, Throwable t) {
                lv_vieworder_progress.setVisibility(View.GONE);
                Toast.makeText(parent, "" + t, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private Call<NewOrderDetailModel> callorderviewapi(String orderid) {
        String url="http://dkbraende.demoproject.info/rest/V1/orders/"+orderid;
        Log.e( "debug_url", "" + url);
        Log.e("debug_cusotoken_205", "" + Login_preference.getCustomertoken(getActivity()));
        Log.e("debug_authtoken_205", "" + Login_preference.gettoken(getActivity()));
        return api.orderDetail("Bearer "+Login_preference.gettoken(getActivity()),url);
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
