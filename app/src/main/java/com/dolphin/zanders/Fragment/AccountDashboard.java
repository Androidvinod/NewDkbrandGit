package com.dolphin.zanders.Fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import com.dolphin.zanders.Adapter.Filterlist_Adapter;
import com.dolphin.zanders.Model.Addresss_Modell.Address;
import com.dolphin.zanders.Model.Addresss_Modell.AddressModell;
import com.dolphin.zanders.Model.UserInformationModel.UserinfoModel;
import com.dolphin.zanders.Preference.Login_preference;
import com.dolphin.zanders.R;
import com.dolphin.zanders.Retrofit.ApiClient;
import com.dolphin.zanders.Retrofit.ApiInterface;
import com.dolphin.zanders.Util.CheckNetwork;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountDashboard extends Fragment implements View.OnClickListener {
    Addressbook_Adapter addressbookAdapter;
    Toolbar toolbar_account_information;
    ApiInterface api;
    LinearLayout lv_add_addrss,lv_account_progress,lv_editinfo,lv_parent_acc_dashboard;
    ScrollView sv_showdata;
    TextView tv_nodata,tv_customerid,tv_email,tv_name,tv_account_dash;
    View v;
    NavigationActivity parent;
    String add_id,screen;
    RecyclerView recv_addresss;
    public AccountDashboard() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_account_dashboard, container, false);
        Filterlist_Adapter.filter_child_value_list.clear();
        parent=(NavigationActivity) getActivity();
        screen="AccountDashboard";
        AllocateMemory();

        setupUI(lv_parent_acc_dashboard);
        api = ApiClient.getClient().create(ApiInterface.class);
        setHasOptionsMenu(true);
        hideKeyboard(parent);
        ((NavigationActivity) parent).setSupportActionBar(toolbar_account_information);
        ((NavigationActivity) parent).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
        ((NavigationActivity) parent).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_36dp);
        toolbar_account_information.setTitleTextColor(parent.getResources().getColor(R.color.black));
        if (CheckNetwork.isNetworkAvailable(parent)) {
            CallAAddressApi();
        } else {
            Toast.makeText(parent, parent.getResources().getString(R.string.intcon), Toast.LENGTH_SHORT).show();
        }



        lv_editinfo.setOnClickListener(this);
        lv_add_addrss.setOnClickListener(this);

        if(Login_preference.getfirstname(getActivity()) == null  || Login_preference.getfirstname(getActivity()).equalsIgnoreCase("null"))
        {
            tv_name.setText("");
            tv_name.setText("");
            tv_name.setVisibility(View.GONE);
            tv_name.setVisibility(View.GONE);
            tv_name.setVisibility(View.GONE);
        }else {
            tv_name.setText(Login_preference.getfirstname(getActivity()) +" "+Login_preference.getlastname(getActivity()));
            tv_name.setText(Login_preference.getfirstname(getActivity()) +" "+Login_preference.getlastname(getActivity()));

        }

        tv_email.setText(Login_preference.getemail(getActivity()));

        return v;
    }
    private void CallAAddressApi() {
        lv_account_progress.setVisibility(View.VISIBLE);
        sv_showdata.setVisibility(View.GONE);
        calladdressgapi().enqueue(new Callback<AddressModell>() {
            @Override
            public void onResponse(Call<AddressModell> call, Response<AddressModell> response) {
                Log.e("response",""+response.body());
                Log.e("response_77",""+response);
                Log.e("status",""+response.body());

                if (response.code()==200)
                {

                    lv_account_progress.setVisibility(View.GONE);
                    sv_showdata.setVisibility(View.VISIBLE);
                    Log.e("response_77",""+response);
                    Log.e("status",""+response.body());
                    Log.e("store_id",""+response.body().getStoreId());

                    Login_preference.setstoreid(getActivity(), String.valueOf(response.body().getStoreId()));

                    AddressModell addressModell=response.body();
                    List<Address> additionalAddresses = response.body().getAddresses();
                    if (additionalAddresses.isEmpty()) {
                        tv_nodata.setVisibility(View.VISIBLE);
                        recv_addresss.setVisibility(View.GONE);
                    }else {

                        Log.e("status127","="+response.body().getAddresses());
                        Log.e("status128","="+response.body().toString());

                        tv_nodata.setVisibility(View.GONE);
                        List<Address> feesInnerData =fetchResultsaa(response);

                        if(feesInnerData.size()==0)
                        {
                            tv_nodata.setVisibility(View.VISIBLE);
                            recv_addresss.setVisibility(View.GONE);
                        }else {

                            tv_nodata.setVisibility(View.GONE);
                            recv_addresss.setVisibility(View.VISIBLE);

                        }
                        Log.e("feesInnerData","="+feesInnerData);
                        addressbookAdapter.addAll(feesInnerData);


                    }
                }else {
                    Log.e("response_77",""+response);
                    Log.e("status",""+response.body());

                    lv_account_progress.setVisibility(View.VISIBLE);
                    sv_showdata.setVisibility(View.GONE);;

                }

            }
            @Override
            public void onFailure(Call<AddressModell> call, Throwable t) {
                Toast.makeText(parent, ""+t, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private List<Address> fetchResultsaa(Response<AddressModell> response) {
        AddressModell getAddressModel = response.body();
        return getAddressModel.getAddresses();
    }

    private Call<AddressModell> calladdressgapi() {
        Log.e("debug_111",""+Login_preference.getcustomer_id(parent));
        Log.e("debug_111token",""+Login_preference.gettoken(getActivity()));

        return api.address("Bearer "+Login_preference.gettoken(getActivity()),"http://dkbraende.demoproject.info/rest//V1/customers/"+Login_preference.getcustomer_id(parent));
    }




    private void AllocateMemory() {
        lv_add_addrss = v.findViewById(R.id.lv_add_addrss);
        recv_addresss = v.findViewById(R.id.recv_addresss);
        toolbar_account_information = v.findViewById(R.id.toolbar_account_information);
        lv_account_progress = v.findViewById(R.id.lv_account_progress);
        lv_editinfo = v.findViewById(R.id.lv_editinfo);
        sv_showdata = v.findViewById(R.id.sv_showdata);
        tv_customerid = v.findViewById(R.id.tv_customerid);
        tv_email = v.findViewById(R.id.tv_email);
        tv_name = v.findViewById(R.id.tv_name);
        lv_parent_acc_dashboard = v.findViewById(R.id.lv_parent_acc_dashboard);
        tv_nodata = v.findViewById(R.id.tv_nodata);
        tv_account_dash = v.findViewById(R.id.tv_account_dash);
        tv_account_dash.setTypeface(NavigationActivity.montserrat_semibold);


        addressbookAdapter = new Addressbook_Adapter(parent,screen);
        recv_addresss.setLayoutManager(new LinearLayoutManager(parent, LinearLayoutManager.VERTICAL, false));
        recv_addresss.setItemAnimator(new DefaultItemAnimator());
        recv_addresss.setAdapter(addressbookAdapter);

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
    public void onClick(View v) {
        if (v==lv_editinfo)
        {
            Bundle b=new Bundle();
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            b.putString("screen","edit detail");

            EditAddressFragment myFragment = new EditAddressFragment();
            myFragment.setArguments(b);
            activity.getSupportFragmentManager().beginTransaction()
                    .addToBackStack(null).replace(R.id.framlayout, myFragment)
                    .commit();

        }else if(v==lv_add_addrss)
        {
            Bundle b=new Bundle();
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            b.putString("screen","edit address");
            b.putString("address","create");
            b.putString("screenname","AccountDashboard");

            EditAddressFragment myFragment = new EditAddressFragment();
            myFragment.setArguments(b);
            activity.getSupportFragmentManager().beginTransaction()
                    .addToBackStack(null).replace(R.id.framlayout, myFragment)
                    .commit();
        }
    }


}
