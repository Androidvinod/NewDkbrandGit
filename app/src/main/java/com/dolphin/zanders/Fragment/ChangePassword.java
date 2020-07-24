package com.dolphin.zanders.Fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import com.dolphin.zanders.Model.LoginModel.Login_Model;
import com.dolphin.zanders.Model.change_password.Changepassword;
import com.dolphin.zanders.Preference.Login_preference;
import com.dolphin.zanders.R;
import com.dolphin.zanders.Retrofit.ApiClient;
import com.dolphin.zanders.Retrofit.ApiInterface;
import com.dolphin.zanders.Util.CheckNetwork;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePassword extends Fragment implements View.OnClickListener {

    View v;
    Toolbar toolbar_change_password;
    TextView tv_change_pw;
    TextInputLayout text_inputlayout_current_password,text_inputlayout_new_password,text_inputlayout_confirm_new_password;
    EditText et_current_password,et_new_password,et_confirm_new_password;
    LinearLayout lv_change_pw_save,lv_changepw_progress,lv_change_main,lv_product_click;
    String current_pw,new_pw,confirm_new_pw;
    ApiInterface api;
    NavigationActivity parent;

    public ChangePassword() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_change_password, container, false);
        parent=(NavigationActivity) getActivity();
        AllocateMemory();
        setHasOptionsMenu(true);
        setupUI(lv_change_main);
        hideKeyboard(parent);
        api= ApiClient.getClient().create(ApiInterface.class);
        ((NavigationActivity) parent).setSupportActionBar(toolbar_change_password);
        ((NavigationActivity) parent).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
        ((NavigationActivity) parent).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_36dp);
        toolbar_change_password.setTitleTextColor(parent.getResources().getColor(R.color.black));
        toolbar_change_password.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parent.onBackPressed();
            }
        });
        tv_change_pw.setText("Skift kodeord");

        lv_change_pw_save.setOnClickListener(this);

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


    private void AllocateMemory() {

        lv_product_click = v.findViewById(R.id.lv_product_click);
        lv_change_pw_save = v.findViewById(R.id.lv_change_pw_save);
        lv_changepw_progress = v.findViewById(R.id.lv_changepw_progress);
        lv_change_main = v.findViewById(R.id.lv_change_main);
        toolbar_change_password = v.findViewById(R.id.toolbar_change_password);
        tv_change_pw = v.findViewById(R.id.tv_change_pw);

        text_inputlayout_current_password = v.findViewById(R.id.text_inputlayout_current_password);
        text_inputlayout_new_password = v.findViewById(R.id.text_inputlayout_new_password);
        text_inputlayout_confirm_new_password = v.findViewById(R.id.text_inputlayout_confirm_new_password);

        et_current_password = v.findViewById(R.id.et_current_password);
        et_new_password = v.findViewById(R.id.et_new_password);
        et_confirm_new_password = v.findViewById(R.id.et_confirm_new_password);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        //inflater.inflate(R.menu.menu_home, menu);

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
        if (view == lv_change_pw_save) {
            current_pw = et_current_password.getText().toString();
            new_pw = et_new_password.getText().toString();
            confirm_new_pw = et_confirm_new_password.getText().toString();

            if (et_current_password.getText().length() == 0) {

                Toast.makeText(parent, ""+parent.getResources().getString(R.string.entercurrentpw), Toast.LENGTH_SHORT).show();

               // et_current_password.setError(parent.getResources().getString(R.string.entercurrentpw));
                et_current_password.requestFocus();

            }else if (et_new_password.getText().length() == 0) {
                Toast.makeText(parent, ""+parent.getResources().getString(R.string.enternewpw), Toast.LENGTH_SHORT).show();
               // et_new_password.setError(parent.getResources().getString(R.string.enternewpw));
                et_new_password.requestFocus();

            } else if (et_new_password.getText().toString().length() <= 5) {
                Toast.makeText(parent, ""+parent.getResources().getString(R.string.pwsixval), Toast.LENGTH_SHORT).show();
              //  et_new_password.setError(parent.getResources().getString(R.string.pwsixval));
                et_new_password.requestFocus();

            }else if (et_confirm_new_password.getText().length() == 0) {
                Toast.makeText(parent, ""+parent.getResources().getString(R.string.enterconfirmpw), Toast.LENGTH_SHORT).show();
                //et_confirm_new_password.setError(parent.getResources().getString(R.string.enterconfirmpw));
                et_confirm_new_password.requestFocus();

            } else if (!new_pw.equals(confirm_new_pw)) {
                Toast.makeText(parent, ""+parent.getResources().getString(R.string.pwmatch), Toast.LENGTH_SHORT).show();
                Log.e("comper_data_145"," "+et_new_password.getText().toString()+" "+et_confirm_new_password.getText().toString());
              //  et_confirm_new_password.setError(parent.getResources().getString(R.string.pwmatch));
                et_confirm_new_password.requestFocus();

            } else {
                if (CheckNetwork.isNetworkAvailable(parent)) {
                    ChangePasswordApi(current_pw, new_pw);
                } else {
                    Toast.makeText(parent, parent.getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    private void ChangePasswordApi(String current_pw, String new_pw) {
        lv_changepw_progress.setVisibility(View.VISIBLE);
        lv_product_click.setVisibility(View.GONE);
        callchangepasswordApi(current_pw,new_pw).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                lv_changepw_progress.setVisibility(View.GONE);
                lv_product_click.setVisibility(View.VISIBLE);

                Log.e("debug1111",""+ response.body());
                Log.e("debug1111dsgdf",""+ response);

                if(response.code()==200 || response.isSuccessful())
                {
                    if(response.body()==true)
                    {
                        Toast.makeText(getActivity(), "Password Changed Successfully", Toast.LENGTH_SHORT).show();

                        pushFragment(new AccountFragment() ,"account");
                    }
                }else {
                    Toast.makeText(getActivity(), "The password doesn't match this account. Verify the password and try again.", Toast.LENGTH_SHORT).show();
                    lv_changepw_progress.setVisibility(View.GONE);
                    lv_product_click.setVisibility(View.VISIBLE);
                }
                /*if (changepassword.getStatus().equalsIgnoreCase("Success")) {
                    Log.e("debug_mesahge",""+changepassword.getMessage());
                    Toast.makeText(parent, "Login succesfully", Toast.LENGTH_SHORT).show();
                    loadFragment(new AccountFragment());
                } else if(changepassword.getStatus().equalsIgnoreCase("error")){
                    lv_changepw_progress.setVisibility(View.GONE);
                    Toast.makeText(getContext(), changepassword.getMessage(), Toast.LENGTH_SHORT).show();
                }*/
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                lv_changepw_progress.setVisibility(View.GONE);
                lv_product_click.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), ""+getActivity().getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Call<Boolean> callchangepasswordApi(String current_pw, String new_pw) {
        ApiInterface  customeapi = ApiClient.getClient().create(ApiInterface.class);

        String url="http://dkbraende.demoproject.info/rest/V1/customers/me/password?currentPassword="+current_pw+"&newPassword="+new_pw;
        Log.e("debug_url","="+url);
        Log.e("customertoken","="+Login_preference.getCustomertoken(getActivity()));

        return customeapi.changePassword("Bearer "+Login_preference.getCustomertoken(getActivity()),url);
    }




    private void loadFragment(Fragment fragment) {
        parent.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                0, 0, R.anim.fade_out).replace(R.id.framlayout, fragment).addToBackStack(null).commit();

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
                ft.setCustomAnimations(R.anim.fade_in,
                        0, 0, R.anim.fade_out).setCustomAnimations(R.anim.fade_in,
                        0, 0, R.anim.fade_out);
                ft.commit();
            }
        }

    }
}
