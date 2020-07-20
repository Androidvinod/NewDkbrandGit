package com.dolphin.zanders.Fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.dolphin.zanders.Activity.NavigationActivity;
import com.dolphin.zanders.Adapter.Filterlist_Adapter;
import com.dolphin.zanders.Model.CartWishlistCountModel.CountModel;
import com.dolphin.zanders.Model.CartlistModel.Cartlist;
import com.dolphin.zanders.Model.CartlistModel.CartlistInnerData;
import com.dolphin.zanders.Model.LoginModel.Login_Model;
import com.dolphin.zanders.Preference.Login_preference;
import com.dolphin.zanders.R;
import com.dolphin.zanders.Retrofit.ApiClient;
import com.dolphin.zanders.Retrofit.ApiClientcusome;
import com.dolphin.zanders.Retrofit.ApiInterface;
import com.dolphin.zanders.Util.CheckNetwork;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dolphin.zanders.Activity.NavigationActivity.bottom_navigation;
import static com.dolphin.zanders.Activity.NavigationActivity.tv_bottomcount;
import static com.dolphin.zanders.Activity.NavigationActivity.tv_wishlist_count;
import static com.dolphin.zanders.Fragment.HomeFragment_new.login_home;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {
    TextInputLayout text_inputlayout_login_email, text_inputlayout_password;
    EditText et_Email_login, et_Email_password;
    //CheckBox chk_rememberme;
    LinearLayout lv_login, lv_login_main;
    TextView tv_forgot_password,tv_createanewaccount;
    String email, password;
    Snackbar snackbar;
    ApiInterface apiInterface;
    Toolbar toolbar_login;
    LinearLayout lv_login_progress;
    String screen,subcat_id,subcatename;
    Bundle bundle;
    ScrollView scroll_login;
    NavigationActivity parent;
    MenuItem login;
    View v;
    ApiInterface api,customeapi;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_login, container, false);
        //parent.getSupportActionBar().hide();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        parent=(NavigationActivity) getActivity();
        customeapi = ApiClientcusome.getClient().create(ApiInterface.class);

        AllocateMemory();
        setHasOptionsMenu(true);
        setupUI(lv_login_main);
        Filterlist_Adapter.filter_child_value_list.clear();
        bundle = this.getArguments();
        ((NavigationActivity) parent).setSupportActionBar(toolbar_login);
        ((NavigationActivity) parent).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
        toolbar_login.setTitle(" ");
        toolbar_login.setNavigationIcon(R.drawable.ic_close_black_36dp);
        toolbar_login.setTitleTextColor(parent.getResources().getColor(R.color.black));
       // zanderstestacc@gmail.com
        //    123456
      // et_Email_login.setText("zanderstestacc@gmail.com");
      // et_Email_password.setText("IL8944");
        if (bundle != null) {
            screen = bundle.getString("screen");
            subcatename = bundle.getString("subcatename");
            subcat_id = bundle.getString("subcat_id");
        }else {
            Log.e("screen_95",""+screen);
            Log.e("screen_95",""+bundle);
        }
        Log.e("screen_97",""+screen);
        Log.e("bundle_97",""+bundle);
        lv_login.setOnClickListener(this);
        tv_forgot_password.setOnClickListener(this);
        tv_createanewaccount.setOnClickListener(this);
        toolbar_login.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parent.onBackPressed();
            }
        });
        return v;
    }

    private void AllocateMemory() {

        scroll_login = v.findViewById(R.id.scroll_login);
        text_inputlayout_login_email = v.findViewById(R.id.text_inputlayout_login_email);
        text_inputlayout_password = v.findViewById(R.id.text_inputlayout_password);
        et_Email_login = v.findViewById(R.id.et_Email_login);
        et_Email_password = v.findViewById(R.id.et_Email_password);
        //chk_rememberme = v.findViewById(R.id.chk_rememberme);
        lv_login = v.findViewById(R.id.lv_login);
        lv_login_main = v.findViewById(R.id.lv_login_main);
        tv_forgot_password = v.findViewById(R.id.tv_forgot_password);
        tv_createanewaccount = v.findViewById(R.id.tv_createanewaccount);
        toolbar_login = v.findViewById(R.id.toolbar_login);
        lv_login_progress = v.findViewById(R.id.lv_login_progress);
    }

    @Override
    public void onClick(View view) {
        if (view == lv_login) {
            email = et_Email_login.getText().toString();
            password = et_Email_password.getText().toString();

            if (et_Email_login.getText().length() == 0) {

                et_Email_login.setError(parent.getResources().getString(R.string.enteremail));
                et_Email_login.requestFocus();

            }else if(isValidEmailAddress(et_Email_login.getText().toString())==false)
            {
                et_Email_login.setError(parent.getResources().getString(R.string.entervalidemail));
                et_Email_login.requestFocus();
            } else if (et_Email_password.getText().length() == 0) {

                et_Email_password.setError(parent.getResources().getString(R.string.enterpw));
                et_Email_password.requestFocus();

            } else if (et_Email_password.getText().toString().length() <= 5) {
                et_Email_password.setError(parent.getResources().getString(R.string.pwsixval));
                et_Email_password.requestFocus();

            } else {
                if (CheckNetwork.isNetworkAvailable(parent)) {
                    CallLoginApi(email,password);
                    //Loginapi(email, password);
                } else {
                    Toast.makeText(parent, parent.getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
                }
            }
        }else if (view == tv_forgot_password){
            Forgotpassword myFragment = new Forgotpassword();
            parent.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                    0, 0, R.anim.fade_out).setCustomAnimations(R.anim.fade_in,
                    0, 0, R.anim.fade_out).replace(R.id.framlayout, myFragment).addToBackStack(null).commit();
        }else if (view== tv_createanewaccount){
            Registration_Fragment myFragment = new Registration_Fragment();
            parent.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                    0, 0, R.anim.fade_out).setCustomAnimations(R.anim.fade_in,
                    0, 0, R.anim.fade_out).replace(R.id.framlayout, myFragment).addToBackStack(null).commit();
        }
    }

    private void CallLoginApi(String email, String password) {
        get_Customer_tokenapi(email,password);
    }

    private void get_Customer_tokenapi(String email, String password) {
        scroll_login.setVisibility(View.GONE);
        lv_login_progress.setVisibility(View.VISIBLE);
        Log.e("response201tokenff",""+Login_preference.gettoken(getActivity()));
        Log.e("email",""+email);
        Log.e("password",""+password);
        String url="http://dkbraende.demoproject.info/rest/V1/integration/customer/token?username="+email+"&password="+password;
        Call<String> customertoken = customeapi.getcustomerToken(url);
        customertoken.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("response20066",""+response.toString());
                Log.e("response20166",""+response.body());

              if(response.code()==200)
              {
                  Login_preference.setCustomertoken(getActivity(),response.body());
                  CallCustomerData();

                  Login_preference.settokenemail(getActivity(),email);
                  Login_preference.settokenepassword(getActivity(),password);
              }else {
                  Toast.makeText(getActivity(), "The password doesn't match this account. Verify the password and try again.", Toast.LENGTH_SHORT).show();
                  scroll_login.setVisibility(View.VISIBLE);
                  lv_login_progress.setVisibility(View.GONE);
              }

            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private void CallCustomerData() {
        Call<ResponseBody> customertoken = customeapi.loginn("Bearer "+Login_preference.getCustomertoken(getActivity()));

        customertoken.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("res_login",""+response.toString());
                Log.e("res_logintoken",""+Login_preference.getCustomertoken(getActivity()));
                Log.e("res_login",""+response.body());
                if(response.code()==200)
                {
                    try {
                        JSONObject jsonObject=new JSONObject(response.body().string());
                        Log.e("jsonObject","fff"+jsonObject);

                        String email=jsonObject.getString("email");
                        String firstname=jsonObject.getString("firstname");
                        String lastname=jsonObject.getString("lastname");
                        String id=jsonObject.getString("id");
                        Log.e("email","fff"+email);
                        Log.e("firstname","fff"+firstname);
                        Log.e("iddd","fff"+id);
                        Login_preference.setLogin_flag(parent, "1");
                        Login_preference.setcustomer_id(parent, id);
                        Login_preference.setemail(parent,email);
                        Login_preference.setfirstname(parent, firstname);
                        Login_preference.setlastname(parent, lastname);
                        Login_preference.setstoreid(getActivity(),jsonObject.optString("store_id"));
                        Login_preference.setgrouppid(getActivity(),jsonObject.optString("group_id"));

                        getstoreName();

                        get_Customer_QuoteId();
                        callWishlistCountApi();
                        Intent intent=new Intent(getActivity(),NavigationActivity.class);
                        startActivity(intent);
                        getActivity().finish();

                    } catch (JSONException e) {
                        e.printStackTrace();

                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                    scroll_login.setVisibility(View.GONE);
                    lv_login_progress.setVisibility(View.GONE);
                }
                else {
                    scroll_login.setVisibility(View.VISIBLE);
                    lv_login_progress.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private void getstoreName() {
        Log.e("customertoken",""+Login_preference.getCustomertoken(getActivity()));
        Call<ResponseBody> getstorenamee = customeapi.getstorename("Bearer "+Login_preference.gettoken(getActivity()));
        getstorenamee.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("res_quoteid",""+response.toString());
                Log.e("resquoteiddd",""+response.body());
                if(response.isSuccessful() || response.code()==200)
                {

                    try {
                        JSONArray jsonArray=new JSONArray(response.body().string());

                        for (int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            Log.e("jsonObject",""+jsonObject);
                            Log.e("default_store_id",""+jsonObject.optString("default_store_id"));
                            Log.e("login",""+Login_preference.getstoreid(getActivity()));
                            if(Login_preference.getstoreid(getActivity()).equalsIgnoreCase(jsonObject.optString("default_store_id")))
                            {
                                if(getActivity()!=null) {
                                    Log.e("name","="+jsonObject.optString("name"));

                                    Login_preference.setstoreName(getActivity(), jsonObject.optString("name"));
                                }
                            }

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }else { }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private void get_Customer_QuoteId() {
        Log.e("customertoken",""+Login_preference.getCustomertoken(getActivity()));
        Call<Integer> customertoken = customeapi.getQuoteid("Bearer "+Login_preference.getCustomertoken(getActivity()),
                "http://dkbraende.demoproject.info/rest/V1/carts/mine/?customerId="+Login_preference.getcustomer_id(parent));
        customertoken.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                Log.e("res_quoteid",""+response.toString());
                Log.e("resquoteiddd",""+response.body());
                if(response.isSuccessful() || response.code()==200)
                {
                    if(getActivity()!=null) {
                        Login_preference.setquote_id(getActivity(), String.valueOf(response.body()));
                        Login_preference.setdkQuoteId(getActivity(), String.valueOf(response.body()));
                    }
                }else { }
            }
            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }


    private void callWishlistCountApi() {
        Log.e("response201tokenff",""+Login_preference.gettoken(getActivity()));
        Call<ResponseBody> customertoken = customeapi.defaultWishlistCount("Bearer "+Login_preference.getCustomertoken(getActivity()));
        customertoken.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response200gffgdf",""+response.toString());
                Log.e("response201fgd",""+response.body());
                if(response.code()==200 || response.isSuccessful())
                {
                    try {
                        JSONArray jsonObject = new JSONArray(response.body().string());
                        String count= jsonObject.getJSONObject(0).getString("total_items");
                        if(getActivity()!=null) {
                            BottomNavigationMenuView menuView1 = (BottomNavigationMenuView) bottom_navigation.getChildAt(0);

                            BottomNavigationItemView itemView_wishlist = (BottomNavigationItemView) menuView1.getChildAt(2);
                            View wishlist_badge = LayoutInflater.from(getActivity()).inflate(R.layout.wishlist_count, menuView1, false);
                            tv_wishlist_count = (TextView) wishlist_badge.findViewById(R.id.badge_wishlist);
                            Log.e("debug_309", "fg" + Login_preference.get_wishlist_count(getActivity()));
                            if (count.equalsIgnoreCase("null") || count.equals("") || count.equals("0")) {
                                tv_wishlist_count.setVisibility(View.GONE);
                            } else {
                                tv_wishlist_count.setVisibility(View.VISIBLE);
                                tv_wishlist_count.setText(count);

                                tv_wishlist_count.setText(count);
                                Login_preference.set_wishlist_count(getActivity(), count);
                            }
                            itemView_wishlist.addView(wishlist_badge);
                        }
                        Log.e("wishcount",""+count);
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
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    private void Loginapi(String email, String password) {


        Log.e("login_email", "" + email);
        Log.e("login_password", "" + password);

        callLoginApi().enqueue(new Callback<Login_Model>() {
            @Override
            public void onResponse(Call<Login_Model> call, Response<Login_Model> response) {
                Login_Model login_model = response.body();
                Log.e("debug1111",""+ response.body());
                Log.e("debug1111dsgdf",""+ response);
                Log.e("debug_mesahge_165",""+login_model.getMessage());
                if (login_model.getStatus().equalsIgnoreCase("Success")) {
                    scroll_login.setVisibility(View.GONE);
                    lv_login_progress.setVisibility(View.GONE);
                    login_home.setVisible(false);
                    Log.e("debug_mesahge",""+login_model.getMessage());
                    Toast.makeText(parent, "Login succesfully", Toast.LENGTH_SHORT).show();
                    if(getActivity()!=null)
                    {
                        Login_preference.setLogin_flag(parent, "1");
                        Login_preference.setcustomer_id(parent, login_model.getCustomerId());
                        Login_preference.setemail(parent, login_model.getEmail());
                        Login_preference.setfirstname(parent, login_model.getFirstname());
                        Login_preference.setlastname(parent, login_model.getLastname());
                    }
                    Log.e("bundle_197",""+bundle);
                    Log.e("screen",""+screen);
                    if (CheckNetwork.isNetworkAvailable(parent)) {
                        if(Login_preference.getLogin_flag(parent).equalsIgnoreCase("1"))
                        {
                            CallCartWishlistCount();
                        }
                    } else {
                        //    noInternetDialog(NavigationActivity.this);
                        Toast.makeText(parent, parent.getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
                    }
                    if(bundle==null)
                    {
                        Intent intent=new Intent(getActivity(),NavigationActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }else if(screen.equalsIgnoreCase("offer"))
                    {
                        OfferFragment myFragment = new OfferFragment();
                        parent.getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.fade_in, 0, 0, R.anim.fade_out)
                                .replace(R.id.framlayout, myFragment).commit();

                    }
                  /* else if (screen.equalsIgnoreCase("product_list")) {
                        ProductListFragment myFragment = new ProductListFragment();
                        Bundle b = new Bundle();
                        b.putString("subcat_id", "" + subcat_id);
                        b.putString("subcatename", "" + subcatename);
                        myFragment.setArguments(b);
                        parent.getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.fade_in, 0, 0, R.anim.fade_out)
                                .replace(R.id.framlayout, myFragment).commit();
                    }*/
                    else if (screen.equalsIgnoreCase("closeout_Product")) {
                        CloseoutProductFragment myFragment = new CloseoutProductFragment();
                        Bundle b = new Bundle();
                        myFragment.setArguments(b);
                        parent.getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.fade_in, 0, 0, R.anim.fade_out)
                                .replace(R.id.framlayout, myFragment).commit();
                    } //Search
                    else if(screen.equalsIgnoreCase("Search"))
                    {
                        SearchFragment myFragment = new SearchFragment();
                        parent.getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.fade_in, 0, 0, R.anim.fade_out)
                                .replace(R.id.framlayout, myFragment).commit();
                    }
                   else if(screen.equalsIgnoreCase("Favourites"))
                    {
                        FavouriteFragment myFragment = new FavouriteFragment();
                        parent.getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.fade_in, 0, 0, R.anim.fade_out)
                                .replace(R.id.framlayout, myFragment).commit();
                    }
                    else if(screen.equalsIgnoreCase("Account"))
                    {
                        AccountFragment myFragment = new AccountFragment();
                        parent.getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.fade_in, 0, 0, R.anim.fade_out)
                                .replace(R.id.framlayout, myFragment).commit();
                    }
                   else if(screen.equalsIgnoreCase("Cart"))
                    {
                        CartFragment myFragment = new CartFragment();
                        parent.getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.fade_in, 0, 0, R.anim.fade_out)
                                .replace(R.id.framlayout, myFragment).commit();
                    }  else{
                        Bundle b = new Bundle();
                        String title = "login";
                        b.putString("title", "" + title);
                        AppCompatActivity activity = (AppCompatActivity) v.getContext();
                        HomeFragment_new myFragment = new HomeFragment_new();
                        myFragment.setArguments(b);
                        activity.getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.fade_in,
                                0, 0, R.anim.fade_out)
                                .replace(R.id.framlayout, myFragment)
                                .commit();
                    }
                } else if(login_model.getStatus().equalsIgnoreCase("error")){
                    scroll_login.setVisibility(View.VISIBLE);
                    lv_login_progress.setVisibility(View.GONE);
                    Toast.makeText(getContext(), login_model.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Login_Model> call, Throwable t) {
                scroll_login.setVisibility(View.VISIBLE);
                lv_login_progress.setVisibility(View.GONE);
                Toast.makeText(parent, ""+parent.getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Call<Login_Model> callLoginApi() {
        Log.e("debug_email","df"+email);
        Log.e("debug_email","df"+password);
        return apiInterface.login(email, password);
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
        return apiInterface.getCount(Login_preference.getcustomer_id(parent));
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
       /* inflater.inflate(R.menu.menu_home, menu);
        login = menu.findItem(R.id.login);
        login.setVisible(false);
        if(Login_preference.getLogin_flag(parent).equalsIgnoreCase("1"))
        {
            login.setVisible(false);
        }else {
            login.setVisible(true);
        }*/
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
