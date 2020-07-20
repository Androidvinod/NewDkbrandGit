package com.dolphin.zanders.Fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.Toast;

import com.dolphin.zanders.Activity.NavigationActivity;
import com.dolphin.zanders.Model.Forgotpassword.Forgotpassword_model;
import com.dolphin.zanders.Model.LoginModel.Login_Model;
import com.dolphin.zanders.Preference.Login_preference;
import com.dolphin.zanders.R;
import com.dolphin.zanders.Retrofit.ApiClient;
import com.dolphin.zanders.Retrofit.ApiClientcusome;
import com.dolphin.zanders.Retrofit.ApiInterface;
import com.dolphin.zanders.Util.CheckNetwork;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class Forgotpassword extends Fragment {
    LinearLayout lv_submit, lv_forgot_progress, lv_forgot_main;
    TextInputLayout text_inputlayout_email;
    EditText et_Email;
    Toolbar toolbar_forgotpass;
    ApiInterface api;
    NavigationActivity parent;
    ScrollView scroll_forget;

    public Forgotpassword() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_forgotpassword, container, false);
        api = ApiClient.getClient().create(ApiInterface.class);
         AllocateMemory(v);
       setupUI(lv_forgot_main);
        setHasOptionsMenu(true);
        parent=(NavigationActivity) getActivity();
        hideKeyboard(parent);
        ((NavigationActivity) parent).setSupportActionBar(toolbar_forgotpass);
        ((NavigationActivity) parent).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
        ((NavigationActivity) parent).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_36dp);
        lv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = et_Email.getText().toString();
                if (et_Email.getText().length() == 0) {
                    et_Email.setError(parent.getResources().getString(R.string.enteremail));
                    et_Email.requestFocus();
                } else if (isValidEmailAddress(et_Email.getText().toString()) == false) {
                    et_Email.setError(parent.getResources().getString(R.string.entervalidemail));
                    et_Email.requestFocus();
                } else {

                    if (CheckNetwork.isNetworkAvailable(getActivity())) {
                        CallforgetApi(email);
                        //Loginapi(email, password);
                    } else {
                        Toast.makeText(getContext(), getActivity().getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return v;
    }

    private void CallforgetApi(String email) {
        ApiInterface apiInterface= ApiClientcusome.getClient().create(ApiInterface.class);
        scroll_forget.setVisibility(View.GONE);
        lv_forgot_progress.setVisibility(View.VISIBLE);
        Log.e("rdebug_104",""+ Login_preference.gettoken(getActivity()));
        Log.e("email",""+email);

        //http://dkbraende.demoproject.info/rest/V1/customers/password?email=info@gmail.com&template=email_reset
        String url="http://dkbraende.demoproject.info/rest/V1/customers/password?template=email_reset&email="+email;
        Log.e("debug_122","="+url);

        Call<Boolean> customertoken = apiInterface.forgetpassword("Bearer "+Login_preference.getCustomertoken(getActivity()),url);
        customertoken.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                Log.e("response20066","="+response.toString());
                Log.e("response20","="+response.body());
                Log.e("respo","="+response);

                if(response.isSuccessful() || response.code()==200)
                {
                    scroll_forget.setVisibility(View.VISIBLE);
                    lv_forgot_progress.setVisibility(View.GONE);
                    Log.e("response20166","=="+response.body());
                    Toast.makeText(getActivity(), "Mail sent successfully Please check your mail." , Toast.LENGTH_SHORT).show();
                    LoginFragment myFragment = new LoginFragment();
                    parent.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                            0, 0, R.anim.fade_out).setCustomAnimations(R.anim.fade_in,
                            0, 0, R.anim.fade_out).replace(R.id.framlayout, myFragment).addToBackStack(null).commit();

                }else {
                    Toast.makeText(getActivity(), "Email is not Exists", Toast.LENGTH_SHORT).show();

                    scroll_forget.setVisibility(View.VISIBLE);
                    lv_forgot_progress.setVisibility(View.GONE);

                }


            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                scroll_forget.setVisibility(View.VISIBLE);
                lv_forgot_progress.setVisibility(View.GONE);

                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }


    private void AllocateMemory(View v) {
        lv_submit = v.findViewById(R.id.lv_submit);
        toolbar_forgotpass = v.findViewById(R.id.toolbar_forgotpass);
        lv_forgot_progress = v.findViewById(R.id.lv_forgot_progress);
        text_inputlayout_email = v.findViewById(R.id.text_inputlayout_email);
        et_Email = v.findViewById(R.id.et_Email);
        lv_forgot_main = v.findViewById(R.id.lv_forgot_main);
        scroll_forget = v.findViewById(R.id.scroll_forget);
    }



    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
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

}
