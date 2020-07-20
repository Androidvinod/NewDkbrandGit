package com.dolphin.zanders.Fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dolphin.zanders.Activity.NavigationActivity;
import com.dolphin.zanders.Adapter.Filterlist_Adapter;
import com.dolphin.zanders.Model.UpdateuserModel.UpdateserinfoModel;
import com.dolphin.zanders.Model.UserInformationModel.UserinfoModel;
import com.dolphin.zanders.Preference.Login_preference;
import com.dolphin.zanders.R;
import com.dolphin.zanders.Retrofit.ApiClient;
import com.dolphin.zanders.Retrofit.ApiInterface;
import com.dolphin.zanders.Util.CheckNetwork;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class Account_information extends Fragment {
    TextInputEditText et_information_email,et_information_name;
    LinearLayout lv_edit_information_save,lv_information_progress,lv_main;
    ApiInterface api;
    TextView tv_title;
    Toolbar toolbar_accountinfo;
    Bundle b;
    NavigationActivity parent;

    public Account_information() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_account_information, container, false);
        api = ApiClient.getClient().create(ApiInterface.class);
        parent=(NavigationActivity) getActivity();
        Filterlist_Adapter.filter_child_value_list.clear();
        hideKeyboard(parent);
        tv_title=v.findViewById(R.id.tv_title);
        et_information_email=v.findViewById(R.id.et_information_email);
        et_information_name=v.findViewById(R.id.et_information_name);
        lv_edit_information_save=v.findViewById(R.id.lv_edit_information_save);
        lv_information_progress=v.findViewById(R.id.lv_information_progress);
        lv_main=v.findViewById(R.id.lv_main);
        toolbar_accountinfo=v.findViewById(R.id.toolbar_accountinfo);
        setHasOptionsMenu(true);
        ((NavigationActivity) parent).setSupportActionBar(toolbar_accountinfo);
        ((NavigationActivity) parent).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
        ((NavigationActivity) parent).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_36dp);
        b=this.getArguments();

        setupUI(lv_main);
        if(b!=null)
        {
            et_information_email.setEnabled(true);
            et_information_name.setEnabled(true);
           tv_title.setText(b.getString("titlename"));
            lv_edit_information_save.setVisibility(View.VISIBLE);
        }else {
            et_information_email.setEnabled(false);
            et_information_name.setEnabled(false);
            lv_edit_information_save.setVisibility(View.GONE);
        }
        if (CheckNetwork.isNetworkAvailable(parent)) {
            Accountinformation_api();
        } else {
            Toast.makeText(parent, parent.getResources().getString(R.string.intcon), Toast.LENGTH_SHORT).show();
        }
        lv_edit_information_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String eamil=et_information_email.getText().toString();
                String name=et_information_name.getText().toString();
                if (et_information_name.getText().length() == 0) {

                    Toast.makeText(parent, ""+parent.getResources().getString(R.string.name_hint), Toast.LENGTH_SHORT).show();
                    et_information_email.requestFocus();

                } else if (et_information_email.getText().length() == 0) {

                    Toast.makeText(parent, ""+parent.getResources().getString(R.string.enteremail), Toast.LENGTH_SHORT).show();
                    et_information_email.requestFocus();

                }else if(isValidEmailAddress(et_information_email.getText().toString())==false)
                {
                    Toast.makeText(parent, ""+parent.getResources().getString(R.string.entervalidemail), Toast.LENGTH_SHORT).show();
                    et_information_email.requestFocus();
                }else {
                    if (CheckNetwork.isNetworkAvailable(parent)) {
                        UPdateAccountinformation_api(eamil,name);
                    } else {
                        Toast.makeText(parent, parent.getResources().getString(R.string.intcon), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


        return v;
    }
    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
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


    private void UPdateAccountinformation_api(String email,String name) {
        lv_information_progress.setVisibility(View.VISIBLE);
        callupdateinfoapi(email,name).enqueue(new Callback<UpdateserinfoModel>() {
            @Override
            public void onResponse(Call<UpdateserinfoModel> call, Response<UpdateserinfoModel> response) {
                lv_information_progress.setVisibility(View.GONE);
                Log.e("status",""+response.body().getStatus());
                if (response.body().getStatus().equals("Success")){
                    Toast.makeText(parent, "Successfully Updated user information", Toast.LENGTH_SHORT).show();
                    if (CheckNetwork.isNetworkAvailable(parent)) {
                        Accountinformation_api();
                    } else {
                        Toast.makeText(parent, parent.getResources().getString(R.string.intcon), Toast.LENGTH_SHORT).show();
                    }
                    AccountDashboard myFragment = new AccountDashboard();
                    myFragment.setArguments(b);
                    parent.getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.fade_in,
                                    0, 0, R.anim.fade_out)
                            .setCustomAnimations(R.anim.fade_in,
                                    0, 0, R.anim.fade_out)
                            .addToBackStack(null).replace(R.id.framlayout, myFragment)
                            .commit();
                   /* List<AdditionalAddress> additionalAddresses = response.ody().getAdditionalAddress();
                    if (additionalAddresses.isEmpty()) {
                        tv_no_addressfafound.setVisibility(View.VISIBLE);
                    }else {
                        tv_no_addressfafound.setVisibility(View.GONE);
                        List<AdditionalAddress> feesInnerData =fetchResults(response);
                        addressbook_adapter.addAll(feesInnerData);
                    }*/
                }else {
                    lv_information_progress.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<UpdateserinfoModel> call, Throwable t) {
                lv_information_progress.setVisibility(View.GONE);
                Toast.makeText(parent, ""+t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Call<UpdateserinfoModel> callupdateinfoapi(String email, String name) {
        return api.updateusrinfo(Login_preference.getcustomer_id(parent),name,email);
    }

    private void Accountinformation_api() {
        lv_information_progress.setVisibility(View.VISIBLE);
        callgetuserinfoapi().enqueue(new Callback<UserinfoModel>() {
            @Override
            public void onResponse(Call<UserinfoModel> call, Response<UserinfoModel> response) {
                lv_information_progress.setVisibility(View.GONE);
                Log.e("status",""+response.body().getStatus());
                if (response.body().getStatus().equals("Success")){
                    Log.e("address","77   "+response.body().getBillingAddress().getName()+"\n"+response.body().getBillingAddress().getStreet()+"\n"+response.body().getBillingAddress().getRegion()+","+response.body().getBillingAddress().getCity()+","+response.body().getBillingAddress().getPostcode()+"\n"+response.body().getBillingAddress().getCountry()+"\n"+"T :"+response.body().getBillingAddress().getTelephone());
                    et_information_email.setText(response.body().getUser().getEmail());
                    et_information_name.setText(response.body().getUser().getFirstname());


                   /* List<AdditionalAddress> additionalAddresses = response.body().getAdditionalAddress();
                    if (additionalAddresses.isEmpty()) {
                        tv_no_addressfafound.setVisibility(View.VISIBLE);
                    }else {
                        tv_no_addressfafound.setVisibility(View.GONE);
                        List<AdditionalAddress> feesInnerData =fetchResults(response);
                        addressbook_adapter.addAll(feesInnerData);
                    }*/
                }else {
                    lv_information_progress.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<UserinfoModel> call, Throwable t) {
                lv_information_progress.setVisibility(View.GONE);
                Toast.makeText(parent, ""+t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Call<UserinfoModel> callgetuserinfoapi() {
        return api.getuserinfo(Login_preference.getcustomer_id(parent));
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
