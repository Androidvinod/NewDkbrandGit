package com.dolphin.zanders.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dolphin.zanders.Activity.NavigationActivity;

import com.dolphin.zanders.Activity.SplashActivity;
import com.dolphin.zanders.Adapter.Addressbook_Adapter;
import com.dolphin.zanders.Adapter.Filterlist_Adapter;
import com.dolphin.zanders.Adapter.NewPaymentMethodAdapter;
import com.dolphin.zanders.Adapter.NewShippingMethodAdapter;
import com.dolphin.zanders.Adapter.PaymentInfoAdapter;

import com.dolphin.zanders.Model.Addresss_Modell.Address;
import com.dolphin.zanders.Model.Addresss_Modell.AddressModell;
import com.dolphin.zanders.Model.NewShippingModel;;
import com.dolphin.zanders.Model.PaymentMethodModel.PaymentMehtodModel;
import com.dolphin.zanders.Model.PaymentMethodModel.PaymentMethod;
import com.dolphin.zanders.Preference.Login_preference;
import com.dolphin.zanders.R;
import com.dolphin.zanders.Retrofit.ApiClient;
import com.dolphin.zanders.Retrofit.ApiClientcusome;
import com.dolphin.zanders.Retrofit.ApiInterface;
import com.dolphin.zanders.Util.CheckNetwork;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewCheckoutFragment extends Fragment implements View.OnClickListener {
    ApiInterface api;
    CoordinatorLayout cordinator_checkout;

    View v;
    Toolbar toolbar_checkout;
    RecyclerView recv_shipping_method,recv_payment_information;
    ShimmerFrameLayout shimmer_payment,shimmer_shipping;
    LinearLayout lv_nodata_shipping,lv_ordernow,lv_checkout_progress,lv_address;
    NewShippingMethodAdapter shippingMethodAdapter;
    NewPaymentMethodAdapter paymentInfoAdapter;
    List<NewShippingModel> modelList=new ArrayList<>();
    List<PaymentMethod> paymentMethodArrayList=new ArrayList<>();
    TextView tv_subtotal,tv_subtotal_value,tv_tax,tv_taxt_value,tv_discount,tv_discount_value,tv_total,tv_total_value,tv_apply;
    Bundle b;
    String cartid,cart_sku,cart_product_type;
    CheckBox checkbox_shipping;
    TextView tv_billing_value,tv_billing_change,tv_billing_add_address;

    String city,country_id,email,firstname,lastname,postcode,street,telephone;
    String paymentMethod="cashondelivery",customer_group_id="",address_type="billing";
    String halfUrl="",billingaddressurl="",shippingAddressUrl="",cartItemsUrl="";
    Boolean isShippingAddress=false;


    String base_currency_code,base_discount_amount,base_grand_total,base_shipping_amount,base_subtotal
            ,base_tax_amount,customer_email,customer_firstname,customer_id,customer_lastname,
            discount_amount,grand_total,shipping_amount,subtotal,subtotal_incl_tax,tax_amount,total_qty_ordered;
    String cart_base_tax_invoiced="",cart_base_row_invoiced="",cart_base_discount_amount="",cart_base_original_price="",cart_base_price="",cart_base_price_incl_tax=""
            ,cart_base_row_total="",cart_base_tax_amount="",cart_discount_amount="",cart_discount_percent="",cart_name="",cart_original_price="",cart_price="",
            cart_price_incl_tax="",cart_product_id="",cart_qty_ordered="",cart_row_total="",cart_row_total_incl_tax="",cart_free_shipping="";

    public static String paymentMethod_pass="";
    String quoteid="",add_id;
    public static String shiping_method_order="",payment_method_order="",passaddress="";

    LinearLayout lv_add_newadd,lv_checkout_main_paren;
    EditText edt_coupon_code;

    public static String shippingMethod="";
    public NewCheckoutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_new_checkout, container, false);
             api = ApiClientcusome.getClient().create(ApiInterface.class);
        quoteid=Login_preference.getdkQuoteId(getActivity());

        shiping_method_order="";
        payment_method_order="";
        passaddress="";
        AllocateMemory();
        setHasOptionsMenu(true);
        AttachRecyclerView();
        setupUI(lv_checkout_main_paren);

        ((NavigationActivity) getActivity()).setSupportActionBar(toolbar_checkout);
        ((NavigationActivity) getActivity()).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
        ((NavigationActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_36dp);
            b=this.getArguments();
            if(b!=null)
            {
                cartid=b.getString("cartid");
                cart_sku=b.getString("cart_sku");
                cart_product_type=b.getString("cart_product_type");
                Log.e("debug11cartid","="+cartid);
            }

        customer_email=Login_preference.getemail(getActivity());
        customer_id=Login_preference.getcustomer_id(getActivity());
        customer_group_id=Login_preference.getgroupid(getActivity());
        Log.e("ddfirstname","="+Login_preference.getfirstname(getActivity()));
        Log.e("ddflasdtname","="+Login_preference.getlastname(getActivity()));

        if (Login_preference.getfirstname(getActivity()) == null || Login_preference.getfirstname(getActivity()).equalsIgnoreCase(null) || Login_preference.getfirstname(getActivity()).equalsIgnoreCase("")
        ) {
            customer_firstname="";
        }else {
            customer_firstname=(Login_preference.getfirstname(getActivity()));
        }
        if (Login_preference.getlastname(getActivity()) == null || Login_preference.getlastname(getActivity()).equalsIgnoreCase(null) || Login_preference.getlastname(getActivity()).equalsIgnoreCase("")
        ) {
            customer_lastname="";
        }else {
            customer_lastname=(Login_preference.getlastname(getActivity()));
        }

        checkbox_shipping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                // Check which checkbox was clicked
                if (checked){
                    isShippingAddress=checked;
                    Log.e("debug_89","="+checked);
                    Log.e("debug_89","="+isShippingAddress);

                    // Do your coding
                }
                else{
                    isShippingAddress=checked;

                    Log.e("debug_00_unchecked","="+checked);
                    Log.e("debug_00_unchecked","="+isShippingAddress);
                    // Do your coding
                }
            }
        });

        if (CheckNetwork.isNetworkAvailable(getActivity())) {
            callPaymentInformationApi();
            CallAddressApi();
            getNewShippingModel();
            getPaymentMethod();


        } else {
            //    noInternetDialog(NavigationActivity.this);
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
        }


        lv_add_newadd.setOnClickListener(this);
        tv_billing_change.setOnClickListener(this);
        lv_ordernow.setOnClickListener(this);
        tv_apply.setOnClickListener(this);
        return v;
    }
    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideKeyboard(getActivity());
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
    private void pushFragment(Fragment fragment, String add_to_backstack) {
        if (fragment == null)
            return;
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (ft != null) {
                ft.replace(R.id.framlayout, fragment);
                ft.setCustomAnimations(R.anim.fade_in,
                        0, 0, R.anim.fade_out).setCustomAnimations(R.anim.fade_in,
                        0, 0, R.anim.fade_out);
                ft.commit();
            }
        }

    }
    private void CallCreateOrderApi() {
        lv_checkout_progress.setVisibility(View.VISIBLE);
        cordinator_checkout.setVisibility(View.GONE);

        createordrapi().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                lv_checkout_progress.setVisibility(View.GONE);
                cordinator_checkout.setVisibility(View.GONE);
                Log.e("res634", "===" + response);
                Log.e("ressss634", "=" + response.body());
                Log.e("6sss34", "==" + response.body().toString());

                if (response.isSuccessful() || response.code() == 200) {
                    JSONObject jsonObject = null;
                    try {
                        lv_checkout_progress.setVisibility(View.GONE);
                        cordinator_checkout.setVisibility(View.GONE);

                        jsonObject = new JSONObject(response.body().string());
                        Log.e("jsonObject_640", "" + jsonObject);

                        JSONArray jsonArray=jsonObject.getJSONArray("items");
                        Log.e("jsonArray", "" + jsonArray);

                       // JSONObject billing_address=jsonObject.getJSONObject("billing_address");
                       // Log.e("billing_address", "" + billing_address);
                        Toast.makeText(getActivity(), "Order Placed successfully", Toast.LENGTH_SHORT).show();

                        Intent intent=new Intent(getActivity(),NavigationActivity.class);
                        startActivity(intent);
                        getActivity().finish();




                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    lv_checkout_progress.setVisibility(View.GONE);
                    cordinator_checkout.setVisibility(View.VISIBLE);

                }

            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                lv_checkout_progress.setVisibility(View.GONE);
                cordinator_checkout.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("debug_175125", "" + t.getMessage());
            }
        });
    }

    private Call<ResponseBody> createordrapi() {

        Log.e("debug_authtoken", "=" + Login_preference.gettoken(getActivity()));
        Log.e("cuome3333", "=" + Login_preference.getCustomertoken(getActivity()));
        Log.e("paymentmethod", "=" + paymentMethod_pass);
        Log.e("cart_sku", "=" + cart_sku);
        Log.e("shiping_method_order", "=" + shiping_method_order);
        String url;
        if(isShippingAddress==true)
        {
            url = ApiClientcusome.MAIN_URLL + "orders/create?" +"entity[payment][method]="+paymentMethod_pass+ halfUrl+"&"+cartItemsUrl+shippingAddressUrl
                    + billingaddressurl+"&"+cart_sku + "&entity[customer_is_guest]=0"+"&entity[email_sent]=1"+"&entity[is_virtual]=0"+"&entity[order_currency_code]="
                    +base_currency_code+"&entity[shipping_description]="+shiping_method_order+"&entity[status]=pending"+"&entity[store_currency_code]="+base_currency_code+
            "&entity[total_item_count]="+total_qty_ordered+"&entity[store_id]="+Login_preference.getstoreid(getActivity())+"&entity[store_name]="+
                    Login_preference.getstoreName(getActivity())+"&"+cart_product_type;
        }else {
            url = ApiClientcusome.MAIN_URLL + "orders/create?"+"entity[payment][method]="+paymentMethod_pass+ halfUrl+"&"+cartItemsUrl+billingaddressurl+"&"+cart_sku
                    + "&entity[customer_is_guest]=0"+"&entity[email_sent]=1"+"&entity[is_virtual]=0"+"&entity[order_currency_code]="
                    +base_currency_code+"&entity[shipping_description]="+shiping_method_order+"&entity[status]=pending"+"&entity[store_currency_code]="+base_currency_code+
                    "&entity[total_item_count]="+total_qty_ordered+"&entity[store_id]="+Login_preference.getstoreid(getActivity())+"&entity[store_name]="+
                    Login_preference.getstoreName(getActivity())+"&"+cart_product_type;
        }
        Log.e("debug_155", "=" + url);
        return api.createorder("Bearer " + Login_preference.gettoken(getActivity()), url);
    }

    private void CallAddressApi() {
        cordinator_checkout.setVisibility(View.GONE);
        lv_checkout_progress.setVisibility(View.VISIBLE);
        calladdressgapi().enqueue(new Callback<AddressModell>() {
            @Override
            public void onResponse(Call<AddressModell> call, Response<AddressModell> response) {
                Log.e("response", "" + response.body());
                Log.e("response_77", "" + response);
                Log.e("status", "" + response.body());

                if (response.code() == 200) {
                    cordinator_checkout.setVisibility(View.VISIBLE);
                    lv_checkout_progress.setVisibility(View.GONE);
                    Log.e("response_77", "" + response);
                    Log.e("status", "" + response.body());

                    AddressModell addressModell = response.body();
                    List<Address> additionalAddresses = response.body().getAddresses();
                    if (additionalAddresses.isEmpty() || additionalAddresses==null) {
                        // tv_no_addressfafound.setVisibility(View.VISIBLE);
                        lv_address.setVisibility(View.GONE);
                        tv_billing_value.setVisibility(View.GONE);
                        tv_billing_change.setVisibility(View.GONE);
                        lv_add_newadd.setVisibility(View.VISIBLE);
                        Log.e("status128", "=" + additionalAddresses.size());

                        passaddress=null;
                    } else {

                        Log.e("status127", "=" + response.body().getAddresses());
                        Log.e("status128", "=" + response.body().toString());

                        // tv_no_addressfafound.setVisibility(View.GONE);
                        List<Address> datalist = fetchResultsaa(response);

                        if (fetchResultsaa(response).size() == 0) {
                            lv_address.setVisibility(View.GONE);
                            tv_billing_value.setVisibility(View.GONE);
                            tv_billing_change.setVisibility(View.GONE);
                            lv_add_newadd.setVisibility(View.VISIBLE);
                            passaddress=null;
                        } else {
                            passaddress="text";
                            lv_address.setVisibility(View.VISIBLE);
                            tv_billing_value.setVisibility(View.VISIBLE);
                            tv_billing_change.setVisibility(View.VISIBLE);
                            lv_add_newadd.setVisibility(View.GONE);
                            Log.e("feesInnerData", "=" + datalist);
                            List<String> stringArrayList=datalist.get(0).getStreet();
                            city=datalist.get(0).getCity();
                            telephone=datalist.get(0).getTelephone();
                            postcode=datalist.get(0).getPostcode();
                            firstname=datalist.get(0).getFirstname();
                            lastname=datalist.get(0).getLastname();
                            country_id=datalist.get(0).getCountryId();
                            email=response.body().getEmail();
                            street=stringArrayList.get(0);

                            String add=firstname+" "+lastname+", \n"+street+", "+city+", "+postcode+",\n"+telephone;
                            tv_billing_value.setText(add);
                            Log.e("street", "=" + street);
                            Log.e("email", "=" + email);
                            Log.e("country_id", "=" + country_id);
                            Log.e("lastname", "=" + lastname);
                            Log.e("firstname", "=" + firstname);
                            Log.e("postcode", "=" + postcode);
                            Log.e("telephone", "=" + telephone);
                            Log.e("city", "=" + city);
                            add_id= String.valueOf(datalist.get(0).getId());
                            billingaddressurl="&entity[billing_address][address_type]="+address_type+
                                    "&entity[billing_address][city]="+city+"&entity[billing_address][country_id]="+country_id+
                                    "&entity[billing_address][email]="+email+"&entity[billing_address][firstname]="+firstname+
                                    "&entity[billing_address][lastname]="+lastname+"&entity[billing_address][postcode]="+postcode+
                                    "&entity[billing_address][street][0]="+street+"&entity[billing_address][telephone]="+telephone;


                            shippingAddressUrl="&entity[extension_attributes][shipping_assignments][0][shipping][address][address_type]=shipping"+
                                    "&entity[extension_attributes][shipping_assignments][0][shipping][address][city]="+city+
                                    "&entity[extension_attributes][shipping_assignments][0][shipping][address][country_id]="+country_id+
                                    "&entity[extension_attributes][shipping_assignments][0][shipping][address][email]="+email+
                                    "&entity[extension_attributes][shipping_assignments][0][shipping][address][firstname]="+firstname+
                                    "&entity[extension_attributes][shipping_assignments][0][shipping][address][lastname]="+lastname+
                                    "&entity[extension_attributes][shipping_assignments][0][shipping][address][postcode]="+postcode+
                                    "&entity[extension_attributes][shipping_assignments][0][shipping][address][street][0]="+street+
                                    "&entity[extension_attributes][shipping_assignments][0][shipping][address][telephone]="+telephone;
                        }


//                        addressBookAdapter.addAll(feesInnerData);
                    }

                    Log.e("billingaddressurl", "==" + billingaddressurl);
                    Log.e("shippingAddressUrl", "==" + shippingAddressUrl);

                } else {
                    cordinator_checkout.setVisibility(View.VISIBLE);
                    lv_checkout_progress.setVisibility(View.GONE);
                    Log.e("response_77", "" + response);
                    Log.e("status", "" + response.body());
                }

            }

            @Override
            public void onFailure(Call<AddressModell> call, Throwable t) {
                cordinator_checkout.setVisibility(View.VISIBLE);
                lv_checkout_progress.setVisibility(View.GONE);
                Toast.makeText(getContext(), "" + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<Address> fetchResultsaa(Response<AddressModell> response) {
        AddressModell getAddressModel = response.body();
        return getAddressModel.getAddresses();
    }

    private Call<AddressModell> calladdressgapi() {

        Log.e("debug_111", "=" + Login_preference.gettoken(getActivity()));
        Log.e("custormid", "=" + Login_preference.gettoken(getActivity()));
        String url = ApiClientcusome.MAIN_URLL + "customers/" + Login_preference.getcustomer_id(getActivity());
        return api.address("Bearer " + Login_preference.gettoken(getActivity()), url);
    }

    public  Call<ResponseBody> callcartdataapi() {
        Log.e("debugcustomertoen","="+Login_preference.getCustomertoken(getActivity()));

        return api.getpricedata("Bearer " + Login_preference.getCustomertoken(getActivity()));
    }

    private void callPaymentInformationApi() {
        callcartdataapi().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("res_140", "" + response.body());
                if(response.isSuccessful() || response.code()==200)
                {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        JSONObject totalsobj = jsonObject.getJSONObject("totals");
                        Log.e("totalsobj", "" + totalsobj);
                        Log.e("res_147", "" + totalsobj.optString("grand_total"));
                        Log.e("pricereponse_158", "" + totalsobj.optString("base_currency_code"));


                        shipping_amount= totalsobj.optString("shipping_amount");
                        base_currency_code= totalsobj.optString("base_currency_code");
                        base_discount_amount= totalsobj.optString("base_discount_amount");
                        base_grand_total= totalsobj.optString("base_grand_total");
                        base_shipping_amount= totalsobj.optString("base_shipping_amount");
                        base_subtotal= totalsobj.optString("base_subtotal");
                        base_tax_amount= totalsobj.optString("base_tax_amount");
                        discount_amount= totalsobj.optString("discount_amount");
                        grand_total= totalsobj.optString("grand_total");
                        subtotal= totalsobj.optString("subtotal");
                        subtotal_incl_tax= totalsobj.optString("subtotal_incl_tax");
                        tax_amount= totalsobj.optString("tax_amount");
                        total_qty_ordered= totalsobj.optString("items_qty");

                        tv_subtotal_value.setText(base_subtotal+" "+Login_preference.getcurrencycode(getActivity()));
                        tv_taxt_value.setText(base_tax_amount+" "+Login_preference.getcurrencycode(getActivity()));
                        tv_discount_value.setText(discount_amount+" "+Login_preference.getcurrencycode(getActivity()));
                        tv_total_value.setText(subtotal_incl_tax+" "+Login_preference.getcurrencycode(getActivity()));

                        JSONArray itemsArray = totalsobj.getJSONArray("items");
                        for (int i=0; i < itemsArray.length(); i++)
                        {
                            JSONObject item_obj=itemsArray.getJSONObject(i);
                            // cart_base_discount_amount=item_obj.optString("base_discount_amount");
                            // cart_base_original_price=item_obj.optString("price");
                            // cart_base_price=item_obj.optString("base_price");
                            //  cart_base_price_incl_tax=item_obj.optString("base_price_incl_tax");
                            // cart_base_row_invoiced=item_obj.optString("base_row_invoiced");
                            //  cart_base_row_total=item_obj.optString("base_row_total");
                            //cart_base_tax_amount=item_obj.optString("base_tax_amount");
                            // cart_discount_amount=item_obj.optString("discount_amount");
                            //  cart_discount_percent=item_obj.optString("discount_percent");
                            //  cart_name=item_obj.optString("name");
                            //  cart_original_price=item_obj.optString("price");
                            //cart_price=item_obj.optString("base_price");
                            // cart_price_incl_tax=item_obj.optString("price_incl_tax");
                            //  cart_product_id=item_obj.optString("item_id");
                            //  cart_qty_ordered=item_obj.optString("qty");
                            //   cart_row_total=item_obj.optString("row_total");
                            //  cart_row_total_incl_tax=item_obj.optString("row_total_incl_tax");


                            if(cart_free_shipping.equalsIgnoreCase(""))
                            {
                                cart_free_shipping="entity[items]"+"["+i+"]"+"[free_shipping]="+"0";
                            }else {
                                cart_free_shipping+="&entity[items]"+"["+i+"]"+"[free_shipping]="+"0";
                            }

                            if(cart_row_total_incl_tax.equalsIgnoreCase(""))
                            {
                                cart_row_total_incl_tax="entity[items]"+"["+i+"]"+"[row_total_incl_tax]="+item_obj.optString("row_total_incl_tax");
                            }else {
                                cart_row_total_incl_tax+="&entity[items]"+"["+i+"]"+"[row_total_incl_tax]="+item_obj.optString("row_total_incl_tax");
                            }
                            if(cart_row_total.equalsIgnoreCase(""))
                            {
                                cart_row_total="entity[items]"+"["+i+"]"+"[row_total]="+item_obj.optString("row_total");
                            }else {
                                cart_row_total+="&entity[items]"+"["+i+"]"+"[row_total]="+item_obj.optString("row_total");
                            }
                            if(cart_qty_ordered.equalsIgnoreCase(""))
                            {
                                cart_qty_ordered="entity[items]"+"["+i+"]"+"[qty_ordered]="+item_obj.optString("qty");
                            }else {
                                cart_qty_ordered+="&entity[items]"+"["+i+"]"+"[qty_ordered]="+item_obj.optString("qty");
                            }
                            if(cart_product_id.equalsIgnoreCase(""))
                            {
                                cart_product_id="entity[items]"+"["+i+"]"+"[product_id]="+item_obj.optString("item_id");
                            }else {
                                cart_product_id+="&entity[items]"+"["+i+"]"+"[product_id]="+item_obj.optString("item_id");
                            }

                            if(cart_price_incl_tax.equalsIgnoreCase(""))
                            {
                                cart_price_incl_tax="entity[items]"+"["+i+"]"+"[price_incl_tax]="+item_obj.optString("price_incl_tax");
                            }else {
                                cart_price_incl_tax+="&entity[items]"+"["+i+"]"+"[price_incl_tax]="+item_obj.optString("price_incl_tax");
                            }

                            if(cart_price.equalsIgnoreCase(""))
                            {
                                cart_price="entity[items]"+"["+i+"]"+"[price]="+item_obj.optString("base_price");
                            }else {
                                cart_price+="&entity[items]"+"["+i+"]"+"[price]="+item_obj.optString("base_price");
                            }

                            if(cart_base_row_invoiced.equalsIgnoreCase(""))
                            {
                                cart_base_row_invoiced="entity[items]"+"["+i+"]"+"[base_row_invoiced]="+item_obj.optString("base_price");
                            }else {
                                cart_base_row_invoiced+="&entity[items]"+"["+i+"]"+"[base_row_invoiced]="+item_obj.optString("base_price");
                            }

                            if(cart_original_price.equalsIgnoreCase(""))
                            {
                                cart_original_price="entity[items]"+"["+i+"]"+"[original_price]="+item_obj.optString("price");
                            }else {
                                cart_original_price+="&entity[items]"+"["+i+"]"+"[original_price]="+item_obj.optString("price");
                            }
                            if(cart_name.equalsIgnoreCase(""))
                            {
                                cart_name="entity[items]"+"["+i+"]"+"[name]="+item_obj.optString("name");
                            }else {
                                cart_name+="&entity[items]"+"["+i+"]"+"[name]="+item_obj.optString("name");
                            }
                            if(cart_discount_percent.equalsIgnoreCase(""))
                            {
                                cart_discount_percent="entity[items]"+"["+i+"]"+"[discount_percent]="+item_obj.optString("discount_percent");
                            }else {
                                cart_discount_percent+="&entity[items]"+"["+i+"]"+"[discount_percent]="+item_obj.optString("discount_percent");
                            }

                            if(cart_discount_amount.equalsIgnoreCase(""))
                            {
                                cart_discount_amount="entity[items]"+"["+i+"]"+"[discount_amount]="+item_obj.optString("discount_amount");
                            }else {
                                cart_discount_amount+="&entity[items]"+"["+i+"]"+"[discount_amount]="+item_obj.optString("discount_amount");
                            }

                            if(cart_base_tax_amount.equalsIgnoreCase(""))
                            {
                                cart_base_tax_amount="entity[items]"+"["+i+"]"+"[base_tax_amount]="+item_obj.optString("base_tax_amount");
                            }else {
                                cart_base_tax_amount+="&entity[items]"+"["+i+"]"+"[base_tax_amount]="+item_obj.optString("base_tax_amount");
                            }

                            if(cart_base_tax_invoiced.equalsIgnoreCase(""))
                            {
                                cart_base_tax_invoiced="entity[items]"+"["+i+"]"+"[base_tax_invoiced]="+item_obj.optString("base_tax_amount");
                            }else {
                                cart_base_tax_invoiced+="&entity[items]"+"["+i+"]"+"[base_tax_invoiced]="+item_obj.optString("base_tax_amount");
                            }

                            if(cart_base_row_total.equalsIgnoreCase(""))
                            {
                                cart_base_row_total="entity[items]"+"["+i+"]"+"[base_row_total]="+item_obj.optString("base_row_total");
                            }else {
                                cart_base_row_total+="&entity[items]"+"["+i+"]"+"[base_row_total]="+item_obj.optString("base_row_total");
                            }

                          /*  if(cart_base_row_invoiced.equalsIgnoreCase(""))
                            {
                                cart_base_row_invoiced="entity[items]"+"["+i+"]"+"[base_row_invoiced]="+item_obj.optString("base_row_invoiced");
                            }else {
                                cart_base_row_invoiced+="&entity[items]"+"["+i+"]"+"[base_row_invoiced]="+item_obj.optString("base_row_invoiced");
                            }*/

                            if(cart_base_discount_amount.equalsIgnoreCase(""))
                            {
                                cart_base_discount_amount="entity[items]"+"["+i+"]"+"[base_discount_amount]="+item_obj.optString("base_discount_amount");
                            }else {
                                cart_base_discount_amount+="&entity[items]"+"["+i+"]"+"[base_discount_amount]="+item_obj.optString("base_discount_amount");
                            }

                            if(cart_base_original_price.equalsIgnoreCase(""))
                            {
                                cart_base_original_price="entity[items]"+"["+i+"]"+"[base_original_price]="+item_obj.optString("price");
                            }else {
                                cart_base_original_price+="&entity[items]"+"["+i+"]"+"[base_original_price]="+item_obj.optString("price");
                            }

                            if(cart_base_price.equalsIgnoreCase(""))
                            {
                                cart_base_price="entity[items]"+"["+i+"]"+"[base_price]="+item_obj.optString("base_price");
                            }else {
                                cart_base_price+="&entity[items]"+"["+i+"]"+"[base_price]="+item_obj.optString("base_price");
                            }

                            if(cart_base_price_incl_tax.equalsIgnoreCase(""))
                            {
                                cart_base_price_incl_tax="entity[items]"+"["+i+"]"+"[base_price_incl_tax]="+item_obj.optString("base_price_incl_tax");
                            }else {
                                cart_base_price_incl_tax+="&entity[items]"+"["+i+"]"+"[base_price_incl_tax]="+item_obj.optString("base_price_incl_tax");
                            }


                        }

                        halfUrl="&entity[base_currency_code]="+base_currency_code+"&entity[base_discount_amount]="+base_discount_amount+
                                "&entity[base_grand_total]="+base_grand_total+"&entity[base_shipping_amount]="+base_shipping_amount+
                                "&entity[base_subtotal]="+base_subtotal+"&entity[base_tax_amount]="+base_tax_amount+
                                "&entity[customer_email]="+customer_email+"&entity[customer_firstname]="+customer_firstname+
                                "&entity[customer_group_id]="+customer_group_id+"&entity[customer_id]="+customer_id+
                                "&entity[customer_lastname]="+customer_lastname+"&entity[discount_amount]="+discount_amount+
                                "&entity[grand_total]="+grand_total+"&entity[shipping_amount]="+shipping_amount+"&entity[subtotal]="+subtotal+
                                "&entity[subtotal_incl_tax]="+subtotal_incl_tax+"&entity[tax_amount]="+tax_amount+"&total_qty_ordered="+total_qty_ordered;


                        cartItemsUrl=cart_base_discount_amount+"&"+cart_base_original_price+"&"+cart_base_price+
                                "&"+cart_base_price_incl_tax+"&"+cart_base_price_incl_tax+"&"+cart_discount_amount+"&"+cart_base_row_total+"&"+
                                cart_row_total_incl_tax+"&"+cart_row_total+"&"+cart_qty_ordered+"&"+cart_product_id+"&"+cart_price_incl_tax+
                                "&"+cart_price+"&"+cart_original_price+"&"+cart_name+"&"+cart_discount_percent+"&"+cart_discount_amount+"&"+
                                cart_base_row_invoiced+"&"+cart_free_shipping+"&"+cart_base_tax_invoiced;


                        Log.e("cartdiscountamount_212", "" +cart_base_discount_amount);
                        Log.e("cart_base_riginalprice", "" +cart_base_original_price);
                        Log.e("cart_base_price", "" +cart_base_price);
                        Log.e("cart_base_row_invoiced", "" +cart_base_row_invoiced);
                        Log.e("cart_basepricencl_tax", "" +cart_base_price_incl_tax);
                        Log.e("cart_discount_amount", "" +cart_discount_amount);
                        Log.e("cart_base_row_total", "" +cart_base_row_total);
                        Log.e("cart_row_total_incl_tax", "" +cart_row_total_incl_tax);
                        Log.e("cart_row_total", "" +cart_row_total);
                        Log.e("cart_qty_ordered", "" +cart_qty_ordered);
                        Log.e("cart_product_id", "" +cart_product_id);
                        Log.e("cart_price_incl_tax", "" +cart_price_incl_tax);
                        Log.e("cart_price", "" +cart_price);
                        Log.e("cart_original_price", "" +cart_original_price);
                        Log.e("cart_name", "" +cart_name);
                        Log.e("cart_discount_percent", "" +cart_discount_percent);
                        Log.e("cart_discount_amount", "" +cart_discount_amount);
                        Log.e("halfUrl", "========" +halfUrl);
                        Log.e("cartItemsUrl", "========" +cartItemsUrl);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    Log.e("error_293",""+response.body());
                    NavigationActivity.get_Customer_tokenapi();
                    callPaymentInformationApi();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "" + getActivity().getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();
                Log.e("debug_175125", "pages: " + t);
            }
        });
    }

    private void AllocateMemory() {
        lv_checkout_main_paren = v.findViewById(R.id.lv_checkout_main_paren);
        tv_apply = v.findViewById(R.id.tv_apply);
        edt_coupon_code = v.findViewById(R.id.edt_coupon_code);
        lv_address = v.findViewById(R.id.lv_address);
        lv_add_newadd = v.findViewById(R.id.lv_add_newadd);
        tv_billing_add_address = v.findViewById(R.id.tv_billing_add_address);
        tv_billing_change = v.findViewById(R.id.tv_billing_change);
        cordinator_checkout = v.findViewById(R.id.cordinator_checkout);
        tv_total_value = v.findViewById(R.id.tv_total_value);
        tv_total = v.findViewById(R.id.tv_total);
        tv_discount_value = v.findViewById(R.id.tv_discount_value);
        tv_discount = v.findViewById(R.id.tv_discount);
        tv_taxt_value = v.findViewById(R.id.tv_taxt_value);
        lv_ordernow = v.findViewById(R.id.lv_ordernow);
        lv_checkout_progress = v.findViewById(R.id.lv_checkout_progress);

        tv_subtotal = v.findViewById(R.id.tv_subtotal);
        tv_subtotal_value = v.findViewById(R.id.tv_subtotal_value);
        tv_tax = v.findViewById(R.id.tv_tax);
        checkbox_shipping = v.findViewById(R.id.checkbox_shipping);

        toolbar_checkout=v.findViewById(R.id.toolbar_checkout);
        recv_shipping_method=v.findViewById(R.id.recv_shipping_method);
        shimmer_payment=v.findViewById(R.id.shimmer_payment);
        recv_payment_information=v.findViewById(R.id.recv_payment_information);
        shimmer_shipping=v.findViewById(R.id.shimmer_shipping);
        lv_nodata_shipping=v.findViewById(R.id.lv_nodata_shipping);
        tv_billing_value=v.findViewById(R.id.tv_billing_value);

        tv_apply.setTypeface(NavigationActivity.montserrat_semibold);
        edt_coupon_code.setTypeface(NavigationActivity.montserrat_semibold);
    }

    private void getPaymentMethod() {
        paymentMethodArrayList.clear();

      //  lv_nodata_chheckout.setVisibility(View.GONE);
        recv_payment_information.setVisibility(View.VISIBLE);
        callpayment().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody paymentMehtodModel = response.body();

                Log.e("res_payment11", "aaa" + response.body());
              //  Log.e("res_payment", "aaa" + response.body().toString());
                //progressBar_review.setVisibility(View.GONE);
                if(response.code()==200)
                {
                    shimmer_payment.stopShimmerAnimation();
                    shimmer_payment.setVisibility(View.GONE);

                    try {
                        JSONArray jsonArray=new JSONArray(response.body().string());

                        for(int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject=jsonArray.getJSONObject(i);

                            Log.e("jsonobj_11", "aaa" + jsonObject);

                            paymentMethodArrayList.add(new PaymentMethod(
                                    jsonObject.getString("code"),
                                    jsonObject.getString("title")));
                        }

                        paymentInfoAdapter.notifyDataSetChanged();

                        AttachRecyclerView();
                        Log.e("resultsresults22", "aaa" + jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {

                }

            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // String error=  t.printStackTrace();
                Toast.makeText(getActivity(), "" + getActivity().getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();
                Log.e("debug_175125", "pages: " + t);
            }
        });

    }

    private void AttachRecyclerView() {
        //payment method
        paymentInfoAdapter = new NewPaymentMethodAdapter(getActivity(),paymentMethodArrayList);
        recv_payment_information.setLayoutManager(new LinearLayoutManager(getActivity()));
        recv_payment_information.setItemAnimator(new DefaultItemAnimator());
        recv_payment_information.setAdapter(paymentInfoAdapter);
        // shiping method
        shippingMethodAdapter = new NewShippingMethodAdapter(getActivity(),modelList);
        recv_shipping_method.setLayoutManager(new LinearLayoutManager(getActivity()));
        recv_shipping_method.setItemAnimator(new DefaultItemAnimator());
        recv_shipping_method.setAdapter(shippingMethodAdapter);
    }

    //call shipping method api
    private Call<ResponseBody> callNewShippingModelApi() {
        Log.e("debug_185", "aaa" + Login_preference.gettoken(getActivity()));
        String url="http://dkbraende.demoproject.info/rest//V1/carts/192001/shipping-methods";
        return api.getshipping("Bearer "+Login_preference.gettoken(getActivity()),url);
    }

    private void getNewShippingModel() {
        modelList.clear();
        lv_nodata_shipping.setVisibility(View.GONE);
        recv_shipping_method.setVisibility(View.VISIBLE);
        callNewShippingModelApi().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
               // ResponseBody NewShippingModelModel = response.body();
              /*  Log.e("resul", "aaa" + response.body());
                Log.e("resuldd", "aaa" + response);
                Log.e("resul22", "aaa" + response.body().toString());
             */   if(response.code()==200)
                 {
                    shimmer_shipping.stopShimmerAnimation();
                    shimmer_shipping.setVisibility(View.GONE);

                    try {
                        JSONArray jsonArray=new JSONArray(response.body().string());
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject=jsonArray.getJSONObject(i);

                         //   Log.e("jsonobj_11", "aaa" + jsonObject);
                            modelList.add(new NewShippingModel(
                                    jsonObject.getString("carrier_code"),
                                    jsonObject.getString("method_code"),
                                    jsonObject.getString("carrier_title"),
                                    jsonObject.getString("method_title"),
                                    jsonObject.getString("error_message")

                            ));
                        }
                        shippingMethodAdapter.notifyDataSetChanged();

                        AttachRecyclerView();
                      //  Log.e("resultsresults22", "aaa" + jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // String error=  t.printStackTrace();
                Toast.makeText(getActivity(), "" + getActivity().getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();
                Log.e("debug_175125", "pages: " + t);
            }
        });
    }

    private Call<ResponseBody> callpayment() {
        Log.e("debug_185ss", "aaa" + Login_preference.gettoken(getActivity()));
        Log.e("quote", "aaa" +Login_preference.getquote_id(getActivity()));
        Log.e("cartid", "aaa" + cartid);
        //http://dkbraende.demoproject.info/rest//V1/carts/"+Login_preference.getquote_id(getActivity())+"/payment-methods"
        String url=ApiClientcusome.MAIN_URLL+"carts/"+Login_preference.getdkQuoteId(getActivity())+"/payment-methods";
        Log.e("cartid222", "aaa" + url);
        return api.getpaymentmethod("Bearer "+Login_preference.gettoken(getActivity()),url);
    }

    @Override
    public void onClick(View v) {
    if(v==lv_add_newadd)
    {
        Bundle b=new Bundle();
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        b.putString("screen","edit address");
        b.putString("address","create");
        b.putString("screenname","checkout");

        EditAddressFragment myFragment = new EditAddressFragment();
        myFragment.setArguments(b);
        activity.getSupportFragmentManager().beginTransaction()
                .addToBackStack(null).replace(R.id.framlayout, myFragment)
                .commit();
    }else if(v==tv_billing_change)
        {
            Bundle b=new Bundle();
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            b.putString("screen","edit address");
            b.putString("address","edit");
            b.putString("address_id",add_id);
            b.putString("screenname","checkout");

            EditAddressFragment myFragment = new EditAddressFragment();
            myFragment.setArguments(b);
            activity.getSupportFragmentManager().beginTransaction()
                    .addToBackStack(null).replace(R.id.framlayout, myFragment)
                    .commit();
        }else if(v==lv_ordernow)
        {
            Log.e("res634111", "clikeddd" );
            if(passaddress==null || passaddress.equalsIgnoreCase("") || passaddress=="")
            {
                Toast.makeText(getActivity(), "Please Add Billing Address", Toast.LENGTH_SHORT).show();
            }
           else if (shiping_method_order == null || shiping_method_order == "" || shiping_method_order == "null"){
                Toast.makeText(getActivity(), "Please Select Shipping Method", Toast.LENGTH_SHORT).show();
            } else if (payment_method_order == null || payment_method_order == "" || payment_method_order == "null") {
                Toast.makeText(getActivity(), "Please Select Payment Method", Toast.LENGTH_SHORT).show();
            }else {
                if (CheckNetwork.isNetworkAvailable(getActivity())) {
                    CallCreateOrderApi();
                } else {
                    //    noInternetDialog(NavigationActivity.this);
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
                }


            }
        }else if(v==tv_apply)
         {

             if(tv_apply.getText().equals(getActivity().getResources().getString(R.string.apply)))
             {
                 String code=edt_coupon_code.getText().toString();
                 if(code.equalsIgnoreCase("") || code.length()==0)
                 {

                 }else {
                     if (CheckNetwork.isNetworkAvailable(getActivity())) {
                         callCopounCodeApi(code);
                     } else {
                         //    noInternetDialog(NavigationActivity.this);
                         Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
                     }
                 }
             }else if(tv_apply.getText().equals(getActivity().getResources().getString(R.string.remove)))
             {
                 if (CheckNetwork.isNetworkAvailable(getActivity())) {
                     callremoveCodeApi();
                 } else {
                     //    noInternetDialog(NavigationActivity.this);
                     Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
                 }
             }


    }
    }

    private void callremoveCodeApi() {
        hideKeyboard(getActivity());
        lv_checkout_progress.setVisibility(View.VISIBLE);
        cordinator_checkout.setVisibility(View.GONE);
        ApiInterface customeapi = ApiClientcusome.getClient().create(ApiInterface.class);
        String url=ApiClientcusome.MAIN_URLL+"carts/"+Login_preference.getquote_id(getActivity())+"/coupons";
        Log.e("debug_url","="+url);
        Log.e("token","="+ Login_preference.gettoken(getActivity()));

        Call<Boolean> homevideos = customeapi.deletecopouncode("Bearer "+Login_preference.gettoken(getActivity()),url);
        homevideos.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                Log.e("response200",""+response.toString());
                Log.e("response201",""+response.body());

                if(response.code()==200)
                {  lv_checkout_progress.setVisibility(View.GONE);
                    cordinator_checkout.setVisibility(View.VISIBLE);

                    if(response.body()==true)
                    {

                        Toast.makeText(getActivity(), "Copoun Code Removed  Successfully", Toast.LENGTH_SHORT).show();
                        edt_coupon_code.setText("");
                        tv_apply.setText(getActivity().getResources().getString(R.string.apply));
                        tv_apply.setTextColor(getActivity().getResources().getColor(R.color.black));
                    }

                }else {
                    lv_checkout_progress.setVisibility(View.GONE);
                    cordinator_checkout.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
                lv_checkout_progress.setVisibility(View.GONE);
                cordinator_checkout.setVisibility(View.VISIBLE);

            }
        });
    }


    private void callCopounCodeApi(String code) {
        hideKeyboard(getActivity());
        lv_checkout_progress.setVisibility(View.VISIBLE);
        cordinator_checkout.setVisibility(View.GONE);
        addcoponcode(code).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                lv_checkout_progress.setVisibility(View.GONE);
                cordinator_checkout.setVisibility(View.VISIBLE);

                Log.e("debug1111",""+ response.body());
                Log.e("debug1111dsgdf",""+ response);

                if(response.code()==200 || response.isSuccessful())
                { lv_checkout_progress.setVisibility(View.GONE);
                    cordinator_checkout.setVisibility(View.VISIBLE);

                    if(response.body()==true)
                    {

                        Toast.makeText(getActivity(), "Copoun Code Added  Successfully", Toast.LENGTH_SHORT).show();
                        tv_apply.setText(getActivity().getResources().getString(R.string.remove));
                        tv_apply.setTextColor(getActivity().getResources().getColor(R.color.colorAccent));
                    }
                }else {
                      Toast.makeText(getActivity(), "The coupon code isn't valid. Verify the code and try again.", Toast.LENGTH_SHORT).show();
                    lv_checkout_progress.setVisibility(View.GONE);
                    cordinator_checkout.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                lv_checkout_progress.setVisibility(View.GONE);
                cordinator_checkout.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), ""+getActivity().getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Call<Boolean> addcoponcode(String code) {
        ApiInterface customeapi = ApiClientcusome.getClient().create(ApiInterface.class);

        //http://dkbraende.demoproject.info/rest/V1/carts/192029/coupons/test
        String url=ApiClientcusome.MAIN_URLL+"carts/"+Login_preference.getquote_id(getActivity())+"/coupons/"+code;
        Log.e("debug_url","="+url);
        Log.e("token","="+ Login_preference.gettoken(getActivity()));

        return customeapi.addCoponCode("Bearer "+Login_preference.gettoken(getActivity()),url);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {


            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
