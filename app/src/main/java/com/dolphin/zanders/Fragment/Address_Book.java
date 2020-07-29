package com.dolphin.zanders.Fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.dolphin.zanders.Activity.NavigationActivity;
import com.dolphin.zanders.Adapter.Addressbook_Adapter;
import com.dolphin.zanders.Model.Addresss_Modell.Address;
import com.dolphin.zanders.Model.Addresss_Modell.AddressModell;
import com.dolphin.zanders.Model.GetAddresslistModel.AdditionalAddress;
import com.dolphin.zanders.Model.GetAddresslistModel.GetAddressModel;
import com.dolphin.zanders.Preference.Login_preference;
import com.dolphin.zanders.R;
import com.dolphin.zanders.Retrofit.ApiClient;
import com.dolphin.zanders.Retrofit.ApiClientcusome;
import com.dolphin.zanders.Retrofit.ApiInterface;
import com.dolphin.zanders.Util.CheckNetwork;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Address_Book extends Fragment implements View.OnClickListener {
    RecyclerView rv_additionaladdress;
    Addressbook_Adapter addressbookAdapter;
    TextView tv_no_addressfafound,tv_defaultshippingadd,tv_defaultbillingadd,tv_account_book;
    Toolbar toolbar_addressbook;
    ApiInterface api,apiinterface;
    public static Bundle b;
    LinearLayout lv_defaultbilling,lv_defaultshipping,lv_parent_addressbook,lv_address_progress,lvnodata_addlist;
    String screenname,defaultbilling_id,defaultshipping_id,defaultbilling_add,defaultshipping_add;
    private ShimmerFrameLayout mShimmerViewContainer;
    NavigationActivity parent;
    RecyclerView recv_address;
    ScrollView scrollview_address;

    public Address_Book() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_address_book, container, false);
        AllocateMemory(v);
        setupUI(lv_parent_addressbook);
        parent=(NavigationActivity) getActivity();
        api = ApiClient.getClient().create(ApiInterface.class);
        apiinterface = ApiClientcusome.getClient().create(ApiInterface.class);
        setHasOptionsMenu(true);
        hideKeyboard(parent);
        ((NavigationActivity) parent).setSupportActionBar(toolbar_addressbook);
        ((NavigationActivity) parent).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
        ((NavigationActivity) parent).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_36dp);

        b = this.getArguments();
        if (b != null) {
            lv_defaultbilling.setVisibility(View.VISIBLE);
            lv_defaultshipping.setVisibility(View.VISIBLE);
            screenname = b.getString("title");
        }else {
            lv_defaultbilling.setVisibility(View.GONE);
            lv_defaultshipping.setVisibility(View.GONE);
        }

        if (CheckNetwork.isNetworkAvailable(parent)) {
         //   Addressbook_getapi();

            CallAddressApi();
        } else {
            Toast.makeText(parent, parent.getResources().getString(R.string.intcon), Toast.LENGTH_SHORT).show();
        }

        lv_defaultbilling.setOnClickListener(this);
        lv_defaultshipping.setOnClickListener(this);
        return v;
    }

    private void CallAddressApi() {
        lv_address_progress.setVisibility(View.VISIBLE);
        scrollview_address.setVisibility(View.GONE);
        calladdressgapi().enqueue(new Callback<AddressModell>() {
            @Override
            public void onResponse(Call<AddressModell> call, Response<AddressModell> response) {
                Log.e("response",""+response.body());
                Log.e("response_77",""+response);
                Log.e("status",""+response.body());

                if (response.code()==200)
                {

                    lv_address_progress.setVisibility(View.GONE);
                    scrollview_address.setVisibility(View.VISIBLE);
                    Log.e("response_77",""+response);
                    Log.e("status",""+response.body());

                    AddressModell addressModell=response.body();
                    List<Address> additionalAddresses = response.body().getAddresses();
                    if (additionalAddresses.isEmpty()) {
                        lvnodata_addlist.setVisibility(View.VISIBLE);
                        scrollview_address.setVisibility(View.GONE);
                        tv_no_addressfafound.setVisibility(View.VISIBLE);
                    }else {

                        Log.e("status127","="+response.body().getAddresses());
                        Log.e("status128","="+response.body().toString());

                        tv_no_addressfafound.setVisibility(View.GONE);
                        List<Address> feesInnerData =fetchResultsaa(response);

                        if(feesInnerData.size()==0)
                        {
                            lvnodata_addlist.setVisibility(View.VISIBLE);
                            scrollview_address.setVisibility(View.GONE);
                        }else {

                            lvnodata_addlist.setVisibility(View.GONE);
                            scrollview_address.setVisibility(View.VISIBLE);

                        }

                        Log.e("feesInnerData","="+feesInnerData);

                       /* addressbookAdapter = new Addressbook_Adapter(parent);
                        recv_address.setLayoutManager(new LinearLayoutManager(parent, LinearLayoutManager.VERTICAL, false));
                        recv_address.setItemAnimator(new DefaultItemAnimator());
                        recv_address.setAdapter(addressbookAdapter);
*/
                        addressbookAdapter.addAll(feesInnerData);


                    }
                }else {
                    Log.e("response_77",""+response);
                    Log.e("status",""+response.body());

                    lv_address_progress.setVisibility(View.VISIBLE);
                    scrollview_address.setVisibility(View.GONE);;

                }

            }
            @Override
            public void onFailure(Call<AddressModell> call, Throwable t) {
                Toast.makeText(parent, ""+t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<AdditionalAddress> fetchResults(Response<GetAddressModel> response) {
        GetAddressModel getAddressModel = response.body();
        return getAddressModel.getAdditionalAddress();
    }

    private Call<GetAddressModel> calladdressgetapi() {
        Log.e("debug_111",""+Login_preference.getcustomer_id(parent));
        return api.getaddressapi("34406");
    }
    private List<Address> fetchResultsaa(Response<AddressModell> response) {
        AddressModell getAddressModel = response.body();
        return getAddressModel.getAddresses();
    }

    private Call<AddressModell> calladdressgapi() {
        Log.e("debug_111",""+Login_preference.getcustomer_id(parent));
        return apiinterface.address("Bearer "+Login_preference.gettoken(parent),"http://dkbraende.demoproject.info/rest//V1/customers/"+Login_preference.getcustomer_id(parent));
    }

    private void AllocateMemory(View v) {
        tv_account_book=v.findViewById(R.id.tv_account_book);
        recv_address=v.findViewById(R.id.recv_address);
        lv_parent_addressbook=v.findViewById(R.id.lv_parent_addressbook);
        toolbar_addressbook=v.findViewById(R.id.toolbar_addressbook);
        rv_additionaladdress=v.findViewById(R.id.rv_additionaladdress);
        tv_no_addressfafound=v.findViewById(R.id.tv_no_addressfafound);
        tv_defaultshippingadd=v.findViewById(R.id.tv_defaultshippingadd);
        tv_defaultbillingadd=v.findViewById(R.id.tv_defaultbillingadd);
        lv_defaultbilling=v.findViewById(R.id.lv_defaultbilling);
        lv_defaultshipping=v.findViewById(R.id.lv_defaultshipping);
        scrollview_address=v.findViewById(R.id.scrollview_address);
        lv_address_progress=v.findViewById(R.id.lv_address_progress);
        lvnodata_addlist=v.findViewById(R.id.lvnodata_addlist);

        tv_account_book.setTypeface(NavigationActivity.montserrat_semibold);

        if(getActivity()!=null)
        {
            addressbookAdapter = new Addressbook_Adapter(getActivity(),"AddressBook");
            recv_address.setLayoutManager(new LinearLayoutManager(parent, LinearLayoutManager.VERTICAL, false));
            recv_address.setItemAnimator(new DefaultItemAnimator());
            recv_address.setAdapter(addressbookAdapter);
        }

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
    public void onClick(View view) {
        if (lv_defaultshipping == view){

            if (screenname.equals("Shipping")){
                Login_preference.setshippingadd(parent,defaultshipping_add);
                Login_preference.setshippingid(parent,defaultshipping_id);
            }else{
                Login_preference.setbillingadd(parent,defaultshipping_add);
                Login_preference.setbillingid(parent,defaultshipping_id);
            }
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Bundle b = new Bundle();
                b.putString("title",screenname);
                b.putString("address_id",defaultshipping_id);
                b.putString("address",defaultshipping_add);
                CheckoutFragment myFragment = new CheckoutFragment();
                myFragment.setArguments(b);
                activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                        0, 0, R.anim.fade_out).setCustomAnimations(R.anim.fade_in,
                        0, 0, R.anim.fade_out).replace(R.id.framlayout, myFragment).addToBackStack(null).commit();


        }else if(lv_defaultbilling == view){

            if (screenname.equals("Shipping")){
                Login_preference.setshippingadd(parent,defaultbilling_add);
                Login_preference.setshippingid(parent,defaultbilling_id);
            }else{
                Login_preference.setbillingadd(parent,defaultbilling_add);
                Login_preference.setbillingid(parent,defaultbilling_id);
            }
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            Bundle b = new Bundle();
            b.putString("title",screenname);
            b.putString("address_id",defaultbilling_id);
            b.putString("address",defaultbilling_add);
            CheckoutFragment myFragment = new CheckoutFragment();
            myFragment.setArguments(b);
            activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                    0, 0, R.anim.fade_out).setCustomAnimations(R.anim.fade_in,
                    0, 0, R.anim.fade_out).replace(R.id.framlayout, myFragment).addToBackStack(null).commit();
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
