package com.dolphin.zanders.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dolphin.zanders.Activity.NavigationActivity;
import com.dolphin.zanders.Adapter.CountryAdapter;
import com.dolphin.zanders.Model.Country;
import com.dolphin.zanders.Preference.Login_preference;
import com.dolphin.zanders.R;
import com.dolphin.zanders.Retrofit.ApiClientcusome;
import com.dolphin.zanders.Retrofit.ApiInterface;
import com.dolphin.zanders.Util.CheckNetwork;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



/**
 * A simple {@link Fragment} subclass.
 */
public class EditAddressFragment extends Fragment {

    NestedScrollView scroll_edit;
    View v;
    Toolbar toolbar_edit;
    TextView tv_mydetails,tv_save;
    LinearLayout lv_edit_detail,lv_main_edit,lv_progress_edittead,lv_main_countryyy;

    TextInputLayout layout_details_email,layout_details_address,layout_details_city,layout_details_postalcode,layout_details_phoneno,layout_detail_fullname,layout_detail_lastname;

    TextInputEditText et_deails_phoneno,et_deails_postal,et_deails_city,et_deails_address,et_details_fullname,et_details_lastname;

   public static TextView tv_choose_country;
    Bundle b;
    String screen,address,address_id,screenname;
    RecyclerView rv_country;
    CountryAdapter countryAdapter;
    public static BottomSheetDialog dialog;
    public static String countryId;
    LinearLayout lv_country_close,lv_progress_country;
    ApiInterface apiInterface;
    private List<Country> countryModelList=new ArrayList<>();
    private List<String> countryidlist=new ArrayList<>();
    private List<String> countryLablelist=new ArrayList<>();
    TextInputEditText et_deails_email;
    public EditAddressFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_edit_address, container, false);
        AllocateMemory(v);
        setupUI(lv_main_edit);
        apiInterface = ApiClientcusome.getClient().create(ApiInterface.class);
        setHasOptionsMenu(true);
        countryId="";

        b=this.getArguments();
        if(b!=null)
        {
            screen=b.getString("screen");
            address=b.getString("address");
            address_id=b.getString("address_id");
            screenname=b.getString("screenname");
            Log.e("debug_33","="+screen);
            Log.e("address11","="+address);
            Log.e("address_id","="+address_id);
        }

        ((NavigationActivity) getActivity()).setSupportActionBar(toolbar_edit);
        ((NavigationActivity) getActivity()).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
        ((NavigationActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_36dp);

        if(address_id != null)
        {
            if (CheckNetwork.isNetworkAvailable(getActivity())) {
                CallCountrylistApi("main");
                CallAddressApi();

            }else {
                Toast.makeText(getContext(), getActivity().getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
            }

        }else { }



        if(screen.equalsIgnoreCase("edit address"))
        {
            if(address.equalsIgnoreCase("create"))
            {
                et_deails_email.setEnabled(false);
                et_deails_email.setActivated(false);
                et_deails_email.setText(Login_preference.getemail(getActivity()));

                tv_mydetails.setText("Generate Address");
            }else {
                tv_mydetails.setText(getActivity().getResources().getString(R.string.editadd));
                et_details_fullname.setText(Login_preference.getfirstname(getActivity()));
                et_details_lastname.setText(Login_preference.getlastname(getActivity()));
                et_deails_email.setText(Login_preference.getemail(getActivity()));
                //layout_details_email.setVisibility(View.GONE);
                et_deails_email.setEnabled(false);
                et_deails_email.setActivated(false);

            }


            layout_details_address.setVisibility(View.VISIBLE);
            layout_details_city.setVisibility(View.VISIBLE);
            lv_main_countryyy.setVisibility(View.VISIBLE);
            layout_details_postalcode.setVisibility(View.VISIBLE);

        }else if(screen.equalsIgnoreCase("edit detail"))
        {

            if(Login_preference.getfirstname(getActivity()) == null  || Login_preference.getfirstname(getActivity()).equalsIgnoreCase("null"))
            {

            }else {
                et_details_fullname.setText(Login_preference.getfirstname(getActivity()));
                et_details_lastname.setText(Login_preference.getlastname(getActivity()));

            }

            et_deails_email.setText(Login_preference.getemail(getActivity()));
            et_deails_email.setEnabled(true);
            et_deails_email.setActivated(true);

            tv_mydetails.setText(getActivity().getResources().getString(R.string.editdetails));
            layout_details_address.setVisibility(View.GONE);
            layout_details_city.setVisibility(View.GONE);
            lv_main_countryyy.setVisibility(View.GONE);
            layout_details_postalcode.setVisibility(View.GONE);
            layout_details_phoneno.setVisibility(View.GONE);
        }


        lv_edit_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(screen.equalsIgnoreCase("edit address"))
                {
                    validateDate();
                }else if(screen.equalsIgnoreCase("edit detail")){
                    validateUserInfoDetail();
                }
            }
        });

        lv_main_countryyy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CountryDialog();
            }
        });


        return v;
    }

    private void validateUserInfoDetail() {
        if (et_details_fullname.getText().length() == 0) {
            Toast.makeText(getContext(), "" + getResources().getString(R.string.enterfirstname), Toast.LENGTH_SHORT).show();
            et_details_fullname.requestFocus();
        }
        else if (et_details_lastname.getText().length() == 0) {
            Toast.makeText(getContext(), "" + getResources().getString(R.string.enterlastname), Toast.LENGTH_SHORT).show();
            et_details_lastname.requestFocus();
        }
        else  if (et_deails_email.getText().length() == 0) {
            Toast.makeText(getContext(), "" + getResources().getString(R.string.enteremailaddress), Toast.LENGTH_SHORT).show();
            et_deails_email.requestFocus();

        }else if(isValidEmailAddress(et_deails_email.getText().toString())==false)
        {
            Toast.makeText(getContext(), "" + getResources().getString(R.string.validemail), Toast.LENGTH_SHORT).show();
            et_deails_email.requestFocus();
        }else {
            String email,firstname,lastname,postalcode=null,phoneno = null,street=null,city=null,country;

            firstname=et_details_fullname.getText().toString();
            lastname=et_details_lastname.getText().toString();
            email=et_deails_email.getText().toString();


            if (CheckNetwork.isNetworkAvailable(getActivity())) {
                callCreateAddressApi(firstname,lastname,email,phoneno,street,city,countryId,postalcode);
                //Loginapi(email, password);
            } else {
                Toast.makeText(getContext(), getActivity().getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void CallAddressApi() {
        lv_progress_edittead.setVisibility(View.VISIBLE);
        scroll_edit.setVisibility(View.GONE);
          calladdressgapi().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response",""+response.body());
                Log.e("response_77",""+response);
                Log.e("status",""+response.body());

                if (response.code()==200) {
                 Log.e("response_77", "" + response);
                    Log.e("status", "=" + response.body());
                    lv_progress_edittead.setVisibility(View.GONE);
                    scroll_edit.setVisibility(View.VISIBLE);

                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());

                        Log.e("debug_jsonobj", "=" + jsonObject);
                        JSONArray jsonArray =jsonObject.getJSONArray("addresses");

                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject object=jsonArray.getJSONObject(0);
                                JSONArray streetarray=object.getJSONArray("street");
                                String street= String.valueOf(streetarray.get(0));
                                Log.e("debuh_street=","="+street);

                                et_details_fullname.setText(object.optString("firstname"));
                                et_details_lastname.setText(object.optString("lastname"));
                                et_deails_phoneno.setText(object.optString("telephone"));
                                et_deails_address.setText(street);
                                et_deails_city.setText(object.optString("city"));
                                et_deails_postal.setText(object.optString("postcode"));
                                countryId=object.optString("country_id");
                                Log.e("debug_222=","="+countryidlist.size());
                                Log.e("debug_278=","="+countryLablelist.size());

                                int pos = 0;
                                if(countryidlist.size()>0)
                                {
                                     pos= countryidlist.indexOf(countryId);
                                }

                                if(countryLablelist.size()>0)
                                {
                                    String countryname=countryLablelist.get(pos);
                                    tv_choose_country.setText(countryname);
                                    tv_choose_country.setTextColor(getResources().getColor(R.color.black));
                                    Log.e("pos=","="+pos);
                                    Log.e("countryname=","="+countryname);

                                }



                           /*     Login_preference.setfirstname(getActivity(),jsonObject.optString("firstname"));
                                Login_preference.setlastname(getActivity(),jsonObject.optString("lastname"));
                                Login_preference.setphone(getActivity(),object.optString("telephone"));
                  */
                            }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    lv_progress_edittead.setVisibility(View.GONE);
                    scroll_edit.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), ""+t, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private Call<ResponseBody> calladdressgapi() {
        ApiInterface apiinterface = ApiClientcusome.getClient().create(ApiInterface.class);
        Log.e("debug_111",""+Login_preference.getcustomer_id(getActivity()));
        String url="http://dkbraende.demoproject.info/rest//V1/customers/"+Login_preference.getcustomer_id(getActivity());
        return apiinterface.getAddress("Bearer "+Login_preference.gettoken(getActivity()),url);
    }

    private void validateDate() {
        if (et_details_fullname.getText().length() == 0) {
            Toast.makeText(getContext(), "" + getResources().getString(R.string.enterfirstname), Toast.LENGTH_SHORT).show();
            et_details_fullname.requestFocus();
        }
       else if (et_details_lastname.getText().length() == 0) {
            Toast.makeText(getContext(), "" + getResources().getString(R.string.enterlastname), Toast.LENGTH_SHORT).show();
            et_details_lastname.requestFocus();
       }
        else  if (et_deails_email.getText().length() == 0) {
            Toast.makeText(getContext(), "" + getResources().getString(R.string.enteremailaddress), Toast.LENGTH_SHORT).show();
            et_deails_email.requestFocus();

        }else if(isValidEmailAddress(et_deails_email.getText().toString())==false)
        {
            Toast.makeText(getContext(), "" + getResources().getString(R.string.validemail), Toast.LENGTH_SHORT).show();
            et_deails_email.requestFocus();
        } else if (et_deails_phoneno.getText().length() == 0) {
            Toast.makeText(getContext(), "" + getResources().getString(R.string.entermobilenumber), Toast.LENGTH_SHORT).show();
            et_deails_phoneno.requestFocus();

        }
        else if (et_deails_phoneno.getText().length() > 12 || et_deails_phoneno.getText().length() <8) {
            Toast.makeText(getContext(), "" + getResources().getString(R.string.entervalidmobileno), Toast.LENGTH_SHORT).show();
            et_deails_phoneno.requestFocus();

        }
        else if (et_deails_address.getText().length() == 0) {
            Toast.makeText(getContext(), "" + getResources().getString(R.string.enteraddress), Toast.LENGTH_SHORT).show();
            et_deails_address.requestFocus();
        }
        else if (et_deails_city.getText().length() == 0) {
            Toast.makeText(getContext(), "" + getResources().getString(R.string.entercity), Toast.LENGTH_SHORT).show();
            et_deails_city.requestFocus();
        }
        else if(countryId==null || countryId=="") {
            Toast.makeText(getContext(), ""+getActivity().getResources().getString(R.string.selectecountry), Toast.LENGTH_SHORT).show();
        }
        else if (et_deails_postal.getText().length() == 0) {
            Toast.makeText(getContext(), "" + getResources().getString(R.string.enterpostalcode), Toast.LENGTH_SHORT).show();
            et_deails_postal.requestFocus();
        }

        else {
            String email,firstname,lastname,postalcode,phoneno,street,city,country;

            firstname=et_details_fullname.getText().toString();
            lastname=et_details_lastname.getText().toString();
            email=et_deails_email.getText().toString();
            phoneno=et_deails_phoneno.getText().toString();
            street=et_deails_address.getText().toString();
            city=et_deails_city.getText().toString();
            country=countryId;
            postalcode=et_deails_postal.getText().toString();


            if (CheckNetwork.isNetworkAvailable(getActivity())) {
                callCreateAddressApi(firstname,lastname,email,phoneno,street,city,countryId,postalcode);
                //Loginapi(email, password);
            } else {
                Toast.makeText(getContext(), getActivity().getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void callCreateAddressApi(String firstname, String lastname, String email, String phoneno, String street, String city, String country, String postalcode) {

        lv_progress_edittead.setVisibility(View.VISIBLE);
        scroll_edit.setVisibility(View.GONE);
        calladdressgapi(firstname,lastname,email,phoneno,street,city,country,postalcode).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response","="+response.body());
                Log.e("response_77","="+response);
            //    Log.e("statusee","="+response.body().toString());
                Log.e("codddddd","="+response.code());

                if(response.isSuccessful() || response.code()==200)
                {
                    lv_progress_edittead.setVisibility(View.GONE);
                    scroll_edit.setVisibility(View.VISIBLE);

                    try {
                        JSONObject jsonObject=new JSONObject(response.body().string());
                        Log.e("idd==",""+jsonObject.optString("id"));

                       if(screen.equalsIgnoreCase("edit detail"))
                        {

                            Login_preference.setfirstname(getActivity(),jsonObject.optString("firstname"));
                            Login_preference.setlastname(getActivity(),jsonObject.optString("lastname"));
                            Login_preference.setemail(getActivity(),jsonObject.optString("email"));

                        }
                       if(screenname==null || screenname.equalsIgnoreCase("null") || screenname==null)
                       {
                           pushFragment(new AccountDashboard() ,"Address");

                       }
                       else if(screenname.equalsIgnoreCase("AddressBook"))
                       {
                           pushFragment(new Address_Book() ,"Address");

                       }else if(screenname.equalsIgnoreCase("AccountDashboard")){
                           pushFragment(new AccountDashboard() ,"Address");

                       }else if(screenname.equalsIgnoreCase("checkout"))
                       {
                           pushFragment(new NewCheckoutFragment() ,"checkout");

                       }else {

                       }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }else {
                    lv_progress_edittead.setVisibility(View.GONE);
                    scroll_edit.setVisibility(View.VISIBLE);
                    Log.e("response_77errr","="+response.code());
                    Toast.makeText(getActivity(), "Invalid Value for country id", Toast.LENGTH_SHORT).show();

                }

            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), ""+t, Toast.LENGTH_SHORT).show();
            }
        });


    }

    private Call<ResponseBody> calladdressgapi(String firstname,String lasname,String email, String telephone,String street
                                              ,String city,String countryid,String postcode) {

        Log.e("debug_111","="+Login_preference.getcustomer_id(getActivity()));
        Log.e("debugtoken","="+Login_preference.gettoken(getActivity()));

        String customerid=Login_preference.getcustomer_id(getActivity());
        String url = null;
        //http://dkbraende.demoproject.info/rest/V1/customers/12497/?customer[addresses][0][customer_id]=12497&customer[addresses][0][countryId]=DK&customer[addresses][0][street][0]=sdsds&customer[addresses][0][firstname]=info&customer[addresses][0][lastname]=info&customer[addresses][0][company]=dolphin_test&customer[addresses][0][telephone]=123456789&customer[addresses][0][city]=test&customer[addresses][0][postcode]=382348&customer[email]=info@gmail.com&customer[id]=12497&customer[websiteId]=1
        if(screen.equalsIgnoreCase("edit detail")) {
            //http://dkbraende.demoproject.info/rest/V1/customers/12503?customer[id]=12502&customer[email]=infodolphin@gmail.com&customer[firstname]=vinod_dolphin&customer[lastname]=prjapatigjhgj&customer[storeId]=1&customer[websiteId]=1
            url="http://dkbraende.demoproject.info/rest/V1/customers/"+Login_preference.getcustomer_id(getActivity())+"?customer[id]="+Login_preference.getcustomer_id(getActivity())
                    +"&customer[email]="+email+"&customer[firstname]="+firstname+"&customer[lastname]="+lasname+"&customer[storeId]=1&customer[websiteId]=1";
        }
        else if(screen.equalsIgnoreCase("edit address")){
            if(address_id==null)
            {
                url="http://dkbraende.demoproject.info/rest/V1/customers/"+Login_preference.getcustomer_id(getActivity())+"/?customer[addresses][0][customer_id]="+customerid+"&customer[addresses][0][countryId]="+countryid+
                        "&customer[addresses][0][street][0]="+street+"&customer[addresses][0][firstname]="+firstname+"&customer[addresses][0][lastname]="+lasname
                        +"&customer[addresses][0][telephone]="+telephone+"&customer[addresses][0][city]="+city
                        +"&customer[addresses][0][postcode]="+postcode+"&customer[id]="+customerid+"&customer[websiteId]=1"+"&customer[email]="+email;

            }else {
                url="http://dkbraende.demoproject.info/rest/V1/customers/"+Login_preference.getcustomer_id(getActivity())+"/?customer[addresses][0][customer_id]="+customerid+"&customer[addresses][0][countryId]="+countryid+
                        "&customer[addresses][0][street][0]="+street+"&customer[addresses][0][firstname]="+firstname+"&customer[addresses][0][lastname]="+lasname
                        +"&customer[addresses][0][telephone]="+telephone+"&customer[addresses][0][city]="+city
                        +"&customer[addresses][0][postcode]="+postcode+"&customer[id]="+customerid+"&customer[websiteId]=1"+"&customer[addresses][0][id]="+address_id+"&customer[email]="+email;

            }
        }

        Log.e("url","="+url);

        return apiInterface.createAddreess("Bearer "+Login_preference.gettoken(getActivity()),url);
    }

    private void AllocateMemory(View v) {

        tv_choose_country=v.findViewById(R.id.tv_choose_country);
        lv_main_countryyy=v.findViewById(R.id.lv_main_countryyy);
        scroll_edit=v.findViewById(R.id.scroll_edit);
        lv_progress_edittead=v.findViewById(R.id.lv_progress_edittead);
        et_deails_phoneno=v.findViewById(R.id.et_deails_phoneno);
        lv_main_edit=v.findViewById(R.id.lv_main_edit);
        et_deails_email=v.findViewById(R.id.et_deails_email);
        layout_details_email=v.findViewById(R.id.layout_details_email);
        et_details_lastname=v.findViewById(R.id.et_details_lastname);
        layout_detail_lastname=v.findViewById(R.id.layout_detail_lastname);
        et_details_fullname=v.findViewById(R.id.et_details_fullname);
        layout_detail_fullname=v.findViewById(R.id.layout_detail_fullname);
        layout_details_phoneno=v.findViewById(R.id.layout_details_phoneno);
        et_deails_city=v.findViewById(R.id.et_deails_city);
        layout_details_postalcode=v.findViewById(R.id.layout_details_postalcode);
        toolbar_edit=v.findViewById(R.id.toolbar_edit);
        tv_mydetails=v.findViewById(R.id.tv_mydetails);
        lv_edit_detail=v.findViewById(R.id.lv_edit_detail);
        tv_save=v.findViewById(R.id.tv_save);
        layout_details_address=v.findViewById(R.id.layout_details_address);
        layout_details_city=v.findViewById(R.id.layout_details_city);
        et_deails_postal=v.findViewById(R.id.et_deails_postal);
        et_deails_address=v.findViewById(R.id.et_deails_address);



        tv_save.setTypeface(NavigationActivity.montserrat_semibold);
        tv_mydetails.setTypeface(NavigationActivity.montserrat_semibold);
        et_deails_address.setTypeface(NavigationActivity.montserrat_medium);
        et_deails_postal.setTypeface(NavigationActivity.montserrat_medium);

        et_deails_city.setTypeface(NavigationActivity.montserrat_medium);
        et_details_fullname.setTypeface(NavigationActivity.montserrat_medium);
        et_details_lastname.setTypeface(NavigationActivity.montserrat_medium);
        et_deails_email.setTypeface(NavigationActivity.montserrat_medium);
        tv_choose_country.setTypeface(NavigationActivity.montserrat_medium);
    }


    private boolean isValidEmailAddress(String toString) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern p = Pattern.compile(ePattern);
        Matcher m = p.matcher(toString);
        return m.matches();
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


    @Override

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        //    inflater.inflate(R.menu.menu_cart, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void pushFragment(Fragment fragment, String add_to_backstack) {
        if (fragment == null)
            return;
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (ft != null) {
                ft.replace(R.id.framlayout, fragment);
                ft.addToBackStack(add_to_backstack);

                ft.commit();
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private void CountryDialog() {
        countryModelList.clear();
        dialog = new BottomSheetDialog(getActivity());
        dialog.setContentView(R.layout.choose_country);
        dialog.show();
        rv_country = dialog.findViewById(R.id.rv_country);
        lv_country_close = dialog.findViewById(R.id.lv_country_close);
        lv_progress_country = dialog.findViewById(R.id.lv_progress_country);
        lv_country_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        countryAdapter = new CountryAdapter(getActivity(),countryModelList);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_country.setLayoutManager(layoutManager);
        rv_country.setAdapter(countryAdapter);
        if (CheckNetwork.isNetworkAvailable(getActivity())) {
            CallCountrylistApi("button click");
        } else {
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
        }
        Log.e("debug_country","f"+countryId);
    }



    //call country list api
    private void CallCountrylistApi(final String main) {
        countryModelList.clear();
        countryidlist.clear();
        countryLablelist.clear();

        if(main.equalsIgnoreCase("main"))
        { lv_progress_edittead.setVisibility(View.VISIBLE);
            scroll_edit.setVisibility(View.GONE);


        }else if(main.equalsIgnoreCase("button click")){
            lv_progress_country.setVisibility(View.VISIBLE);
            rv_country.setVisibility(View.GONE);
        }


        callcountrylistapi().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody cartlist = response.body();
                Log.e("debug_res", "" + response.body());
                Log.e("debug_11954", "=" + response);
                if (response.code()==200 || response.isSuccessful()) {
                    if(main.equalsIgnoreCase("main"))
                    {
                        lv_progress_edittead.setVisibility(View.GONE);
                        scroll_edit.setVisibility(View.VISIBLE);

                    }else if(main.equalsIgnoreCase("button click")){
                        lv_progress_country.setVisibility(View.GONE);
                        rv_country.setVisibility(View.VISIBLE);
                    }

                    try {
                        JSONArray jsonArray=new JSONArray(response.body().string());

                        for (int i=0; i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            countryModelList.add(new Country(jsonObject.optString("full_name_english"),jsonObject.optString("id")));
                            countryidlist.add(jsonObject.optString("id"));
                            countryLablelist.add(jsonObject.optString("full_name_english"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                     if(main.equalsIgnoreCase("main"))
                    {
                        lv_progress_edittead.setVisibility(View.GONE);
                        scroll_edit.setVisibility(View.VISIBLE);
                    }else if(main.equalsIgnoreCase("button click")){
                         lv_progress_country.setVisibility(View.VISIBLE);
                         rv_country.setVisibility(View.GONE);
                     }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "" + getActivity().getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();
                Log.e("debug_175125", "pages: " + t);
                lv_progress_country.setVisibility(View.VISIBLE);
                rv_country.setVisibility(View.GONE);
                if(main.equalsIgnoreCase("main"))
                {
                    lv_progress_edittead.setVisibility(View.GONE);
                    scroll_edit.setVisibility(View.VISIBLE);
                }else if(main.equalsIgnoreCase("button click")){

                    lv_progress_country.setVisibility(View.VISIBLE);
                    rv_country.setVisibility(View.GONE);
                }
            }
        });

    }

    public Call<ResponseBody> callcountrylistapi() {
        return apiInterface.getCountryList("Bearer "+Login_preference.gettoken(getActivity()));
    }
}
