package com.dolphin.zanders.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.dolphin.zanders.Activity.NavigationActivity;
import com.dolphin.zanders.Preference.Login_preference;
import com.dolphin.zanders.R;
import com.dolphin.zanders.Retrofit.ApiClientcusome;
import com.dolphin.zanders.Retrofit.ApiInterface;
import com.dolphin.zanders.Util.CheckNetwork;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Registration_Fragment extends Fragment implements View.OnClickListener {
    Toolbar toolbar_reg;
    NavigationActivity parent;
    TextInputEditText et_first_register,et_lastname_register,et_Email_register,et_password_register,et_veryfiy_password;
    LinearLayout  lv_register;
    ApiInterface apiInterface;
    ScrollView scroll_register;
    TextView tv_alredayaccount;
    LinearLayout lv_register_progress,lv_register_main;
    View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         v=inflater.inflate(R.layout.fragment_registration_, container, false);

        apiInterface = ApiClientcusome.getClient().create(ApiInterface.class);
        AllocateMemory(v);
        setupUI(lv_register_main);
        parent=(NavigationActivity) getActivity();
        ((NavigationActivity) parent).setSupportActionBar(toolbar_reg);
        ((NavigationActivity) parent).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
        toolbar_reg.setTitle(" ");
        toolbar_reg.setNavigationIcon(R.drawable.ic_close_black_36dp);
        toolbar_reg.setTitleTextColor(parent.getResources().getColor(R.color.black));

        toolbar_reg.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parent.onBackPressed();
            }
        });

        lv_register.setOnClickListener(this);
        tv_alredayaccount.setOnClickListener(this);
        return v;
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

    private void AllocateMemory(View v) {
        toolbar_reg= v.findViewById(R.id.toolbar_reg);
        lv_register= v.findViewById(R.id.lv_register);
        et_veryfiy_password= v.findViewById(R.id.et_veryfiy_password);
        et_password_register= v.findViewById(R.id.et_password_register);
        et_Email_register= v.findViewById(R.id.et_Email_register);
        et_first_register= v.findViewById(R.id.et_first_register);
        et_lastname_register=v.findViewById(R.id.et_lastname_register);
        scroll_register=v.findViewById(R.id.scroll_register);
        lv_register_progress=v.findViewById(R.id.lv_register_progress);
        lv_register_main=v.findViewById(R.id.lv_register_main);
        tv_alredayaccount=v.findViewById(R.id.tv_alredayaccount);


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

    @Override
    public void onClick(View v) {
       if(v==lv_register){
            if (et_first_register.getText().length() == 0) {

                Toast.makeText(getActivity(), getResources().getString(R.string.enterfirstname), Toast.LENGTH_SHORT).show();
                et_first_register.requestFocus();

            } else if (et_lastname_register.getText().length() == 0) {

                Toast.makeText(getActivity(), getResources().getString(R.string.enterlastname), Toast.LENGTH_SHORT).show();
                et_lastname_register.requestFocus();

            } else if (et_Email_register.getText().length()==0) {

                Toast.makeText(getActivity(), getResources().getString(R.string.enteremailaddress), Toast.LENGTH_SHORT).show();
                et_Email_register.requestFocus();
            }

            else if (isValidEmailAddress(et_Email_register.getText().toString())==false) {

                Toast.makeText(getActivity(), getResources().getString(R.string.validemail), Toast.LENGTH_SHORT).show();
                et_Email_register.requestFocus();


            }else if (et_password_register.getText().length()==0) {

                Toast.makeText(getActivity(), getResources().getString(R.string.enterpw), Toast.LENGTH_SHORT).show();
                et_password_register.requestFocus();
            }else if(et_veryfiy_password.getText().toString().equalsIgnoreCase(et_password_register.getText().toString())==false)
            {
                Toast.makeText(getActivity(),  getResources().getString(R.string.pwmatch), Toast.LENGTH_SHORT).show();
            }
            else if (et_veryfiy_password.getText().toString().length() <= 7) {
                Toast.makeText(getActivity(), ""+getResources().getString(R.string.validpassword), Toast.LENGTH_SHORT).show();
            }
           else {
                if (CheckNetwork.isNetworkAvailable(getActivity())) {
                    String firstname,lastname,email,passowrd,psw;
                    firstname=et_first_register.getText().toString();
                    lastname=et_lastname_register.getText().toString();
                    email=et_Email_register.getText().toString();
                    passowrd=et_password_register.getText().toString();
                    psw=et_veryfiy_password.getText().toString();

                    register(firstname, lastname,email,passowrd,psw);

                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.intcon), Toast.LENGTH_SHORT).show();
                }
            }
        }else if (v==tv_alredayaccount){
           LoginFragment myFragment = new LoginFragment();
           getFragmentManager().beginTransaction()
                   .setCustomAnimations(R.anim.fade_in,
                           0, 0, R.anim.fade_out)
                   .replace(R.id.framlayout, myFragment)
                   .commit();
       }
    }

    private void register(String firstname, String lastname, String email, String passowrd, String psw) {

        scroll_register.setVisibility(View.GONE);
        lv_register_progress.setVisibility(View.VISIBLE);
        Log.e("login_email", "" + email);
        Log.e("login_password", "" + firstname);

        callregisterApi(firstname,lastname,email,passowrd,psw).enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("code","="+ response.code());
                Log.e("success","="+ response.isSuccessful());
                if(response.code()==200 || response.isSuccessful())
                {
                    //ResponseBody login_model = response.body();
                    try {
                        JSONObject  jsonObject = new JSONObject(response.body().string());
                        Log.e("jsonObject","="+ jsonObject);
                        Log.e("jsonObject","="+ response);
                        String email=jsonObject.getString("email");
                        String firstname=jsonObject.getString("firstname");
                        String lastname=jsonObject.getString("lastname");
                        String group_id=jsonObject.getString("group_id");
                        Log.e("email111111111","="+email);
                        Log.e("firstname","="+firstname);
                        Log.e("lastname","="+lastname);
                        scroll_register.setVisibility(View.GONE);
                        lv_register_progress.setVisibility(View.GONE);
                        Toast.makeText(parent, "Sign Up succesfully", Toast.LENGTH_SHORT).show();

                        LoginFragment myFragment = new LoginFragment();
                        getFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.fade_in,
                                        0, 0, R.anim.fade_out)
                                .replace(R.id.framlayout, myFragment)
                                .commit();
                        Login_preference.setemail(parent, email);
                        Login_preference.setfirstname(parent, firstname);
                        Login_preference.setlastname(parent, lastname);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else if(response.code()==400)
                {
                    scroll_register.setVisibility(View.VISIBLE);
                    lv_register_progress.setVisibility(View.GONE);
                    Log.e("codedddddddddddddd","="+ response.code());
                    Log.e("codedres","="+ response.body());
                    Log.e("response","="+ response);
                    try {
                        Log.e("responseeeeee","="+ response.errorBody().string());
                        Toast.makeText(getActivity(), ""+getActivity().getResources().getString(R.string.registererror), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }else if(response.code()==401)
                {
                    NavigationActivity.get_Customer_tokenapi();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                scroll_register.setVisibility(View.VISIBLE);
                lv_register_progress.setVisibility(View.GONE);
                Log.e("error","="+t.getMessage());
                Toast.makeText(parent, ""+parent.getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private Call<ResponseBody> callregisterApi(String firstname, String lastname, String email, String passowrd, String psw) {
        Log.e("debug_email","df"+email);
        Log.e("debug_email","df"+firstname);
        String url="http://dkbraende.demoproject.info/rest/V1/customers/?customer[email]="+email+"&customer[firstname]="+firstname+"&customer[lastname]="+lastname+"&password="+passowrd;

        return apiInterface.register("Bearer "+Login_preference.gettoken(getActivity()), url);
    }
    private boolean isValidEmailAddress(String toString) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(toString);
        return m.matches();
    }
}
