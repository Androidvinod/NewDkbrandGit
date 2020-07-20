package com.dolphin.zanders.Fragment;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dolphin.zanders.Activity.NavigationActivity;
import com.dolphin.zanders.Adapter.CategoryAdapter;
import com.dolphin.zanders.Adapter.PaymentInfoAdapter;
import com.dolphin.zanders.Adapter.ShippingMethodAdapter;
import com.dolphin.zanders.Model.CartWishlistCountModel.CountModel;
import com.dolphin.zanders.Model.Checkout_model.CheckoutTotalModel;
import com.dolphin.zanders.Model.Create_order_model.CreateorderModel;
import com.dolphin.zanders.Model.GetAddresslistModel.GetAddressModel;
import com.dolphin.zanders.Model.PaymentMethodModel.PaymentMehtodModel;
import com.dolphin.zanders.Model.PaymentMethodModel.PaymentMethod;
import com.dolphin.zanders.Model.Shipingmethod.ShippingMethod;
import com.dolphin.zanders.Model.Shipingmethod.ShippingMethodModel;
import com.dolphin.zanders.Preference.Login_preference;
import com.dolphin.zanders.R;
import com.dolphin.zanders.Retrofit.ApiClient;
import com.dolphin.zanders.Retrofit.ApiInterface;
import com.dolphin.zanders.Util.CheckNetwork;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dolphin.zanders.Activity.NavigationActivity.tv_bottomcount;
import static com.dolphin.zanders.Activity.NavigationActivity.tv_wishlist_count;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckoutFragment extends Fragment implements View.OnClickListener {
    public ShimmerFrameLayout shimmer_payment,shimmer_shipping;
    View v;
    NavigationActivity parent;

    Toolbar toolbar_checkout;
    TextView tv_billing_info,tv_billing_change,tv_shipping_info,tv_shipping_change,
            tv_shipping_mehtod,tv_payment_method,tv_order_review,tv_shippable_subtotal,
            tv_subtotal,
            tv_grand_total, tv_shippable_grand_total_main,tv_order_now,tv_freightpolicy;

    RecyclerView recv_shipping_method,recv_payment_information;
    LinearLayout lv_ordernow,lv_nodata_shipping,lv_checkout_parent,
            lv_nodata_chheckout;
    ApiInterface api;
    PaymentInfoAdapter paymentInfoAdapter;

    public static LinearLayout lv_checkout_progress,lv_checkout_main;

    public static TextView tv_shippable_subtotal_value,tv_subtotal_value,tv_shipping_handling_bestway_value
            ,tv_grand_total_value,tv_billing_value,tv_shipping_value,tv_shippable_grand_total_main_value,tv_shipping_handling_bestway;

    ShippingMethodAdapter shippingMethodAdapter;
    public static String biiling_address_id,shipping_address_id;
    public static String shiping_method="",payment_method="";
    public static String shiping_method_order="",payment_method_order="";

    Bundle b;
    String address_id,address,title;

    public CheckoutFragment() {

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_checkout, container, false);
        AllocateMemory(v);
        parent=(NavigationActivity) getActivity();

        hideKeyboard(parent);
        setupUI(lv_checkout_parent);
        shiping_method_order="";
        payment_method_order="";
        api = ApiClient.getClient().create(ApiInterface.class);
        setHasOptionsMenu(true);
        ((NavigationActivity) parent).setSupportActionBar(toolbar_checkout);
        ((NavigationActivity) parent).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
        ((NavigationActivity) parent).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_36dp);

        Log.e("debug_1855590", "dsf" + Login_preference.getquote_id(parent));
        b = this.getArguments();
        tv_shipping_change.setOnClickListener(this);
        tv_billing_change.setOnClickListener(this);
        lv_ordernow.setOnClickListener(this);
        tv_freightpolicy.setOnClickListener(this);

        //bind data to recyclerview
        AttachRecyclerView();
        //calling api of categiry list for side menu

        if (CheckNetwork.isNetworkAvailable(parent)) {
            if (b != null) {
                biiling_address_id=Login_preference.getbillingid(parent);
                shipping_address_id=Login_preference.getbillingid(parent);
                tv_billing_value.setText(Login_preference.getbillingadd(parent));
                tv_shipping_value.setText(Login_preference.getshippingadd(parent));
                CallCheckoutTotalApi(biiling_address_id,shipping_address_id);

            }else {
                Addressbook_getapi();
            }

            getShippingMethod();
            getPaymentMethod();

        } else {
            //    noInternetDialog(NavigationActivity.this);
            Toast.makeText(parent, parent.getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
        }
        return v;
    }


    private void Callcreateorder_api() {
        lv_checkout_progress.setVisibility(View.VISIBLE);
        lv_checkout_main.setVisibility(View.GONE);
        callcreatorderapi().enqueue(new Callback<CreateorderModel>() {
            @Override
            public void onResponse(Call<CreateorderModel> call, Response<CreateorderModel> response) {
                Log.e("response_check", "" + response.body());
                Log.e("response_77_checkout", "" + response);
                Log.e("statuscgh", "" + response.body().getStatus());
                lv_checkout_progress.setVisibility(View.GONE);
                lv_checkout_main.setVisibility(View.VISIBLE);
                if (response.body().getStatus().equals("Success")) {
                    Toast.makeText(parent, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    if (CheckNetwork.isNetworkAvailable(parent)) {
                        if(Login_preference.getLogin_flag(parent).equalsIgnoreCase("1"))
                        {
                            CallCartWishlistCount();
                        }
                    } else {
                        //    noInternetDialog(NavigationActivity.this);
                        Toast.makeText(parent, parent.getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();

                    }
                    ShowSuccessDialog(v,"success");
                } else {
                    Toast.makeText(parent, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    // tv_no_addressfafound.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<CreateorderModel> call, Throwable t) {
                lv_checkout_progress.setVisibility(View.GONE);
                lv_checkout_main.setVisibility(View.VISIBLE);

                Toast.makeText(parent, "" + parent.getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();
            }
        });
    }
    //Login_preference.getemail(parent,biiling_address_id,shipping_address_id,shiping_method,payment_method,"12345678","2020-01-07","test","Ctest","test"
    private Call<CreateorderModel> callcreatorderapi() {
        Log.e("payment_method",""+payment_method);
        if (payment_method_order.equals("zpay")) {
            Log.e("login_email_124",""+Login_preference.getemail(parent));
            return api.createorder(Login_preference.getemail(parent),
                    biiling_address_id,shipping_address_id,shiping_method_order,
                    payment_method,PaymentInfoAdapter.et_ordernumber.getText().toString()
                    ,PaymentInfoAdapter.et_shipping.getText().toString(),
                    PaymentInfoAdapter.et_comments.getText().toString(),
                    "Ctest","test");

        }else if (payment_method_order.equals("zcard")) {
            Log.e("login_email_creditcard",""+Login_preference.getemail(parent));


            return api.createorder_zcard(Login_preference.getemail(parent),
                    biiling_address_id,shipping_address_id,
                    shiping_method_order,
                    payment_method_order,
                    PaymentInfoAdapter.card_number,
                    PaymentInfoAdapter.card_exp_year
                    ,PaymentInfoAdapter.card_exp_moth
                    ,PaymentInfoAdapter.card_varification_no,
                    PaymentInfoAdapter.card_ship_date
                    , PaymentInfoAdapter.card_purchesorder_number);

        }else{
            Log.e("login_email_124",""+Login_preference.getemail(parent));
            return api.createorder(Login_preference.getemail(parent),biiling_address_id,shipping_address_id,shiping_method_order,payment_method,PaymentInfoAdapter.et_ordernumber.getText().toString(),PaymentInfoAdapter.et_shipping.getText().toString(),PaymentInfoAdapter.et_comments.getText().toString(),"Ctest","test");

        }
    }
    //cartcount api
    private void CallCartWishlistCount() {

        getcount().enqueue(new Callback<CountModel>() {
            @Override
            public void onResponse(Call<CountModel> call, Response<CountModel> response) {
                CountModel countModel = response.body();

                Log.e("statussssss", "" + countModel.getStatus());
                if (countModel.getStatus().equalsIgnoreCase("Success"))
                {
                    //show cart count
                    Log.e("debug_count", "" + countModel.getItemsCount());
                    String item_count = null,count_wishlist = null;
                    if(countModel.getItemsCount()==0)
                    {
                        Login_preference.setCart_item_count(parent,"");
                        //  item_count = String.valueOf(countModel.getItemsCount());
                    }else {
                        Login_preference.setCart_item_count(parent,String.valueOf( countModel.getItemsCount()));
                        item_count = String.valueOf(countModel.getItemsCount());
                    }
                    if(countModel.getWishlist()==0)
                    {
                        //wishlist count
                        Login_preference.set_wishlist_count(parent, "");
                        //count_wishlist = String.valueOf(countModel.getWishlist());

                    }else {
                        //wishlist count
                        Login_preference.set_wishlist_count(parent, String.valueOf(countModel.getWishlist()));
                        count_wishlist = String.valueOf(countModel.getWishlist());
                    }
                    if (item_count == null || item_count.equalsIgnoreCase("null") || item_count.equals("")) {
                        Log.e("count_40", "" + String.valueOf(countModel.getItemsCount()));
                        tv_bottomcount.setVisibility(View.GONE);
                    } else {
                        tv_bottomcount.setVisibility(View.VISIBLE);
                        Log.e("count_80", "" + String.valueOf(countModel.getItemsCount()));
                        NavigationActivity.Check_String_NULL_Value(tv_bottomcount, String.valueOf(countModel.getItemsCount()));
                    }
                    //show wishlist count
                    Log.e("item_qtyfav", "" + item_count);

                    if ( count_wishlist == null || count_wishlist.equalsIgnoreCase("null") || count_wishlist.equals("")) {
                        Log.e("count_40fav", "" + String.valueOf(countModel.getWishlist()));
                        tv_wishlist_count.setVisibility(View.GONE);
                    } else {
                        tv_wishlist_count.setVisibility(View.VISIBLE);
                        Log.e("count_80fav", "" + String.valueOf(countModel.getWishlist()));
                        NavigationActivity.Check_String_NULL_Value(tv_wishlist_count, String.valueOf(countModel.getWishlist()));
                    }
                }
                else {
                    Toast.makeText(parent, countModel.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<CountModel> call, Throwable t) {
                // String error=  t.printStackTrace();
                Toast.makeText(parent, "" + parent.getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();

                Log.e("debug_175125", "pages: " + t);
            }
        });
    }
    private Call<CountModel> getcount() {
        return api.getCount(Login_preference.getcustomer_id(parent));
    }
    private void CallCheckoutTotalApi(String biiling_address_id, String shipping_address_id) {
        callcheckoutapi(biiling_address_id,shipping_address_id).enqueue(new Callback<CheckoutTotalModel>() {
            @Override
            public void onResponse(Call<CheckoutTotalModel> call, Response<CheckoutTotalModel> response) {
                Log.e("response_check", "" + response.body());
                Log.e("response_77_checkout", "" + response);
                Log.e("statuscgh", "" + response.body().getStatus());
                if (response.body().getStatus().equals("Success")) {
                    Log.e("address_63shhip", "" + response.body().getTotals().getShippableSubtotal());

                    tv_shippable_subtotal_value.setText(response.body().getTotals().getShippableSubtotal());
                    tv_subtotal_value.setText(response.body().getTotals().getSubtotal());
                    tv_shipping_handling_bestway_value.setText(response.body().getTotals().getShippingAndHandling());
                    Log.e("shippingmessg_312",""+response.body().getTotals().getShippingAndHandlingName());
                  //  tv_shipping_handling_bestway.setText("Shipping & Handling("+response.body().getTotals().getShippingAndHandlingName()+")");
                    tv_grand_total_value.setText(response.body().getTotals().getGrandTotal());
                    Log.e("garant_total_191111",""+response.body().getTotals().getGrandTotal());
                    tv_shippable_grand_total_main_value.setText(response.body().getTotals().getGrandTotal());
                } else {
                    Toast.makeText(parent, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CheckoutTotalModel> call, Throwable t) {
                Toast.makeText(parent, "" +t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Call<CheckoutTotalModel> callcheckoutapi(String biiling_address_id, String shipping_address_id) {
        if (shiping_method.equals("")||shiping_method.equals("null")||shiping_method.equals(null)){
            shiping_method="zandersship_PU";
        }
        if (payment_method.equals("")||payment_method.equals("null")||payment_method.equals(null)){
            payment_method="zpay";
        }
        Log.e("debug_111_checkoui", "" + Login_preference.getemail(parent));
        Log.e("biiling_address_id154", "" + this.biiling_address_id);
        Log.e("shipping_address_id154", "" + this.shipping_address_id);
        Log.e("shipping_address_id154", "" + shiping_method);
        Log.e("shipping_address_id154", "" + payment_method);

        return api.getCheckouttotal(Login_preference.getemail(parent), biiling_address_id,
                shipping_address_id,"zandersship_PU",Login_preference.getquote_id(parent),payment_method);
    }
    //call address api
    private void Addressbook_getapi() {
        lv_checkout_progress.setVisibility(View.VISIBLE);
        lv_checkout_main.setVisibility(View.GONE);
        calladdressgetapi().enqueue(new Callback<GetAddressModel>() {
            @Override
            public void onResponse(Call<GetAddressModel> call, Response<GetAddressModel> response) {
                Log.e("response_check", "" + response.body());
                Log.e("response_77_checkout", "" + response);
                Log.e("statuscgh", "" + response.body().getStatus());
                if (response.body().getStatus().equals("Success")) {
                    Log.e("address_63", "" + response.body().getAdditionalAddress());
                    Log.e("address", "77   " + response.body().getBillingAddress().getName() + "\n" + response.body().getBillingAddress().getStreet() + "\n" + response.body().getBillingAddress().getRegion() + "," + response.body().getBillingAddress().getCity() + "," + response.body().getBillingAddress().getPostcode() + "\n" + response.body().getBillingAddress().getCountry() + "\n" + "T :" + response.body().getBillingAddress().getTelephone());
                    //set address id
                    biiling_address_id=response.body().getBillingAddress().getCustomerAddressId();
                    shipping_address_id=response.body().getShippingAddress().getCustomerAddressId();

                    Log.e("biiling_address_id", "" + biiling_address_id);
                    Log.e("shipping_address_id", "" + shipping_address_id);
                    Login_preference.setbillingid(parent,response.body().getBillingAddress().getCustomerAddressId());
                    Login_preference.setshippingid(parent,response.body().getShippingAddress().getCustomerAddressId());

                    Login_preference.setbillingadd(parent,response.body().getBillingAddress().getFirstname() + "\n" + response.body().getBillingAddress().getStreet() + "\n" + response.body().getBillingAddress().getRegion() + "," + response.body().getBillingAddress().getCity() + "," + response.body().getBillingAddress().getPostcode() + "\n" + response.body().getBillingAddress().getCountry() + "\n" + "T : " + response.body().getBillingAddress().getTelephone());
                    Login_preference.setshippingadd(parent,response.body().getShippingAddress().getFirstname() + "\n" + response.body().getShippingAddress().getStreet() + "\n" + response.body().getShippingAddress().getRegion() + "," + response.body().getShippingAddress().getCity() + "," + response.body().getShippingAddress().getPostcode() + "\n" + response.body().getShippingAddress().getCountry() + "\n" + "T : " + response.body().getShippingAddress().getTelephone());
                    tv_billing_value.setText(response.body().getBillingAddress().getFirstname() + "\n" + response.body().getBillingAddress().getStreet() + "\n" + response.body().getBillingAddress().getRegion() + "," + response.body().getBillingAddress().getCity() + "," + response.body().getBillingAddress().getPostcode() + "\n" + response.body().getBillingAddress().getCountry() + "\n" + "T : " + response.body().getBillingAddress().getTelephone());
                    Log.e("address_248", "77   " + response.body().getBillingAddress().getName() + "\n" + response.body().getBillingAddress().getStreet() + "\n" + response.body().getBillingAddress().getRegion() + "," + response.body().getBillingAddress().getCity() + "," + response.body().getBillingAddress().getPostcode() + "\n" + response.body().getBillingAddress().getCountry() + "\n" + "T : " + response.body().getBillingAddress().getTelephone());
                    tv_shipping_value.setText(response.body().getShippingAddress().getFirstname() + "\n" + response.body().getShippingAddress().getStreet() + "\n" + response.body().getShippingAddress().getRegion() + "," + response.body().getShippingAddress().getCity() + "," + response.body().getShippingAddress().getPostcode() + "\n" + response.body().getShippingAddress().getCountry() + "\n" + "T : " + response.body().getShippingAddress().getTelephone());

                    CallCheckoutTotalApi(biiling_address_id,shipping_address_id);
                    lv_checkout_progress.setVisibility(View.GONE);
                    lv_checkout_main.setVisibility(View.VISIBLE);

                } else {
                    tv_billing_value.setText("No data Found");
                    tv_shipping_value.setText("No data Found");
                    // tv_no_addressfafound.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<GetAddressModel> call, Throwable t) {
                lv_checkout_progress.setVisibility(View.GONE);
                lv_checkout_main.setVisibility(View.VISIBLE);
                Toast.makeText(parent, "" +t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Call<GetAddressModel> calladdressgetapi() {
        Log.e("debug_111_checkoui", "" + Login_preference.getcustomer_id(parent));
        return api.getaddressapi(Login_preference.getcustomer_id(parent));
    }
    //call payment method api
    private void getPaymentMethod() {
        lv_nodata_chheckout.setVisibility(View.GONE);
        recv_payment_information.setVisibility(View.VISIBLE);
        callPayMethodApi().enqueue(new Callback<PaymentMehtodModel>() {
            @Override
            public void onResponse(Call<PaymentMehtodModel> call, Response<PaymentMehtodModel> response) {
                PaymentMehtodModel paymentMehtodModel = response.body();
                //progressBar_review.setVisibility(View.GONE);

                if (paymentMehtodModel.getStatus().equalsIgnoreCase("Success")) {
                    Log.e("Totallllll", "" + paymentMehtodModel.getPaymentMethod());
                    if (fetchResults(response) == null || fetchResults(response).size() == 0) {
                        Toast.makeText(parent, ""+paymentMehtodModel.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("debug_109", "pages: " + fetchResults(response));
                        lv_nodata_chheckout.setVisibility(View.VISIBLE);
                        recv_payment_information.setVisibility(View.GONE);
                        shimmer_payment.stopShimmerAnimation();
                        shimmer_payment.setVisibility(View.GONE);

                    }else {
                        recv_payment_information.setVisibility(View.VISIBLE);
                        lv_nodata_chheckout.setVisibility(View.GONE);
                        List<PaymentMethod> results = fetchResults(response);
                        List<PaymentMethod> category_arr = response.body().getPaymentMethod();
                        paymentInfoAdapter.addAll(results);
                        if (category_arr.isEmpty()) {
                            lv_nodata_chheckout.setVisibility(View.VISIBLE);
                            recv_payment_information.setVisibility(View.GONE);
                            shimmer_payment.stopShimmerAnimation();
                            shimmer_payment.setVisibility(View.GONE);

                        } else {
                            recv_payment_information.setVisibility(View.VISIBLE);
                            lv_nodata_chheckout.setVisibility(View.GONE);
                        }
                        shimmer_payment.stopShimmerAnimation();
                        shimmer_payment.setVisibility(View.GONE);
                    }
                }else {
                    Toast.makeText(parent, ""+paymentMehtodModel.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<PaymentMehtodModel> call, Throwable t) {
                // String error=  t.printStackTrace();
                Toast.makeText(parent, "" + parent.getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();
                Log.e("debug_175125", "pages: " + t);
            }
        });
    }
    private Call<PaymentMehtodModel> callPayMethodApi() {
        Log.e("debug_185", "aaa" + Login_preference.getquote_id(parent));
        return api.getPaymentMethod(Login_preference.getquote_id(parent));
    }
    private List<PaymentMethod> fetchResults(Response<PaymentMehtodModel> response) {
        PaymentMehtodModel paymentMehtodModel = response.body();
        return paymentMehtodModel.getPaymentMethod();
    }
    //call shipping method api
    private Call<ShippingMethodModel> callshippingMethodApi() {
        Log.e("debug_185", "aaa" + Login_preference.getquote_id(parent));
        return api.getShippingMethod(Login_preference.getquote_id(parent));
    }
    private void getShippingMethod() {
        lv_nodata_shipping.setVisibility(View.GONE);
        recv_shipping_method.setVisibility(View.VISIBLE);
        callshippingMethodApi().enqueue(new Callback<ShippingMethodModel>() {
            @Override
            public void onResponse(Call<ShippingMethodModel> call, Response<ShippingMethodModel> response) {
                ShippingMethodModel shippingMethodModel = response.body();
                ;

                if (shippingMethodModel.getStatus().equalsIgnoreCase("Success")) {
                    Log.e("Totallllll", "" + shippingMethodModel.getShippingMethod());
                    if (fetchShipingMethodResult(response) == null || fetchShipingMethodResult(response).size() == 0) {
                        Toast.makeText(parent, ""+shippingMethodModel.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("debug_109", "pages: " + fetchShipingMethodResult(response));
                        lv_nodata_shipping.setVisibility(View.VISIBLE);
                        recv_shipping_method.setVisibility(View.GONE);
                        shimmer_shipping.stopShimmerAnimation();
                        shimmer_shipping.setVisibility(View.GONE);

                    }else {
                        recv_shipping_method.setVisibility(View.VISIBLE);
                        lv_nodata_shipping.setVisibility(View.GONE);
                        List<ShippingMethod> results = fetchShipingMethodResult(response);
                        List<ShippingMethod> shipping_arr = response.body().getShippingMethod();
                        shippingMethodAdapter.addAll(results);
                        if (shipping_arr.isEmpty()) {
                            lv_nodata_shipping.setVisibility(View.VISIBLE);
                            recv_shipping_method.setVisibility(View.GONE);
                            shimmer_shipping.stopShimmerAnimation();
                            shimmer_shipping.setVisibility(View.GONE);

                        } else {
                            recv_shipping_method.setVisibility(View.VISIBLE);
                            lv_nodata_shipping.setVisibility(View.GONE);
                        }
                        shimmer_shipping.stopShimmerAnimation();
                        shimmer_shipping.setVisibility(View.GONE);
                    }
                }else {
                    Toast.makeText(parent, ""+shippingMethodModel.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ShippingMethodModel> call, Throwable t) {
                // String error=  t.printStackTrace();
                Toast.makeText(parent, "" + parent.getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();
                Log.e("debug_175125", "pages: " + t);
            }
        });
    }
    private List<ShippingMethod> fetchShipingMethodResult(Response<ShippingMethodModel> response) {
        ShippingMethodModel paymentMehtodModel = response.body();
        return paymentMehtodModel.getShippingMethod();
    }
    private void AllocateMemory(View v) {
        lv_checkout_progress=v.findViewById(R.id.lv_checkout_progress);
        lv_nodata_chheckout=v.findViewById(R.id.lv_nodata_chheckout);
        shimmer_payment=v.findViewById(R.id.shimmer_payment);
        shimmer_shipping=v.findViewById(R.id.shimmer_shipping);
        lv_ordernow=v.findViewById(R.id.lv_ordernow);
        recv_payment_information=v.findViewById(R.id.recv_payment_information);
        recv_shipping_method=v.findViewById(R.id.recv_shipping_method);
        lv_checkout_parent=v.findViewById(R.id.lv_checkout_parent);
        lv_checkout_main=v.findViewById(R.id.lv_checkout_main);
        lv_nodata_shipping=v.findViewById(R.id.lv_nodata_shipping);
        tv_freightpolicy=v.findViewById(R.id.tv_freightpolicy);

        tv_shippable_grand_total_main_value=v.findViewById(R.id.tv_shippable_grand_total_main_value);
        tv_grand_total_value=v.findViewById(R.id.tv_grand_total_value);
        tv_shipping_handling_bestway_value=v.findViewById(R.id.tv_shipping_handling_bestway_value);
        tv_subtotal_value=v.findViewById(R.id.tv_subtotal_value);
        tv_shippable_subtotal_value=v.findViewById(R.id.tv_shippable_subtotal_value);
        tv_billing_change=v.findViewById(R.id.tv_billing_change);
        tv_billing_value=v.findViewById(R.id.tv_billing_value);
        tv_shipping_change=v.findViewById(R.id.tv_shipping_change);
        tv_shipping_value=v.findViewById(R.id.tv_shipping_value);

        tv_order_now=v.findViewById(R.id.tv_order_now);
        tv_shippable_grand_total_main=v.findViewById(R.id.tv_shippable_grand_total_main);
        tv_grand_total=v.findViewById(R.id.tv_grand_total);
        tv_shipping_handling_bestway=v.findViewById(R.id.tv_shipping_handling_bestway);
        tv_subtotal=v.findViewById(R.id.tv_subtotal);
        tv_shippable_subtotal=v.findViewById(R.id.tv_shippable_subtotal);
        tv_order_review=v.findViewById(R.id.tv_order_review);
        tv_payment_method=v.findViewById(R.id.tv_payment_method);

        toolbar_checkout=v.findViewById(R.id.toolbar_checkout);
        tv_billing_info=v.findViewById(R.id.tv_billing_info);
        tv_shipping_info=v.findViewById(R.id.tv_shipping_info);
        tv_shipping_mehtod=v.findViewById(R.id.tv_shipping_mehtod);
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
    private void AttachRecyclerView() {
        //payment method
        paymentInfoAdapter = new PaymentInfoAdapter(parent);
        recv_payment_information.setLayoutManager(new LinearLayoutManager(parent));
        recv_payment_information.setItemAnimator(new DefaultItemAnimator());
        recv_payment_information.setAdapter(paymentInfoAdapter);
        // shiping method
        shippingMethodAdapter = new ShippingMethodAdapter(parent);
        recv_shipping_method.setLayoutManager(new LinearLayoutManager(parent));
        recv_shipping_method.setItemAnimator(new DefaultItemAnimator());
        recv_shipping_method.setAdapter(shippingMethodAdapter);
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

    @Override
    public void onClick(View v) {
        if(v==tv_shipping_change)
        {
            String title="Shipping";
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            Bundle b = new Bundle();
            b.putString("title",title);

            Address_Book myFragment = new Address_Book();
            myFragment.setArguments(b);
            activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                    0, 0, R.anim.fade_out).setCustomAnimations(R.anim.fade_in,
                    0, 0, R.anim.fade_out).replace(R.id.framlayout, myFragment).addToBackStack(null).commit();

        }else if(v==tv_billing_change)
        {
            String title="billing";
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            Bundle b = new Bundle();
            b.putString("title",title);
            Address_Book myFragment = new Address_Book();
            myFragment.setArguments(b);
            activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                    0, 0, R.anim.fade_out).setCustomAnimations(R.anim.fade_in,
                    0, 0, R.anim.fade_out).replace(R.id.framlayout, myFragment).addToBackStack(null).commit();
        }else if (v== tv_freightpolicy) {
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            Freight_Policy myFragment = new Freight_Policy();
            activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                    0, 0, R.anim.fade_out).setCustomAnimations(R.anim.fade_in,
                    0, 0, R.anim.fade_out).replace(R.id.framlayout, myFragment).addToBackStack(null).commit();
        } else if(v==lv_ordernow)
        {
            Log.e("shipping_528",""+shiping_method_order);
            Log.e("payment_method_529",""+payment_method_order);
            if (shiping_method_order == null || shiping_method_order == "" || shiping_method_order == "null"){
                Toast.makeText(parent, "Please Select Shipping Method", Toast.LENGTH_SHORT).show();
            } else if (payment_method_order == null || payment_method_order == "" || payment_method_order == "null") {
                Toast.makeText(parent, "Please Select Payment Method", Toast.LENGTH_SHORT).show();
            }
            else {
                Callcreateorder_api();
            }

        }
    }

    private void ShowSuccessDialog(View v, String credit_card) {
        final Dialog dialog = new Dialog(parent, android.R.style.Theme_Dialog);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.order_success_row);
        dialog.setCanceledOnTouchOutside(false);

        LinearLayout ok=dialog.findViewById(R.id.lv_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyOrderFragment myFragment = new MyOrderFragment();
                parent.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                        0, 0, R.anim.fade_out).setCustomAnimations(R.anim.fade_in,
                        0, 0, R.anim.fade_out).replace(R.id.framlayout, myFragment).commit();
                dialog.dismiss();
            }
        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
    }
    @Override
    public void onResume() {
        super.onResume();
        shimmer_payment.startShimmerAnimation();
        shimmer_shipping.startShimmerAnimation();
    }
    @Override
    public void onPause() {
        shimmer_payment.stopShimmerAnimation();
        shimmer_shipping.stopShimmerAnimation();
        super.onPause();
    }
}
