package com.dolphin.zanders.Fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.KeyEvent;
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
import com.dolphin.zanders.Activity.SplashActivity;
import com.dolphin.zanders.Adapter.Filterlist_Adapter;
import com.dolphin.zanders.Preference.Login_preference;
import com.dolphin.zanders.R;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;

import static com.dolphin.zanders.Activity.NavigationActivity.bottom_navigation;
import static com.dolphin.zanders.Activity.NavigationActivity.drawer;

public class AccountFragment extends Fragment implements View.OnClickListener {

    Toolbar toolbar_myaccpunt;
    View v;
    TextView tv_login_ititle,tv_account_title;
    LinearLayout lv_parent_account_freg, lv_account_dashbord, lv_account_information, lv_addressbook, lv_changepassword, lv_myorder, lv_myinvoice, lv_closeout, lv_aboutus,
            lv_termsandcondition, lv_freightpolicy, lv_logout, lv_itemonsell;
    NavigationActivity parent;

    public AccountFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_account_fregment, container, false);
        bottom_navigation.getMenu().getItem(4).setChecked(true);
        Filterlist_Adapter.filter_child_value_list.clear();
        FilterListFragment.selected_child="";
        FilterListFragment.filter_old_childlist.clear();
        Filterlist_Adapter.filter_grouppp_namelist.clear();

        parent = (NavigationActivity) getActivity();
        AllocateMemory();
        setHasOptionsMenu(true);
        hideKeyboard(parent);
        setupUI(lv_parent_account_freg);
        ((NavigationActivity) parent).setSupportActionBar(toolbar_myaccpunt);
        ((NavigationActivity) parent).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
        ((NavigationActivity) parent).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        toolbar_myaccpunt.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        lv_aboutus.setOnClickListener(this);
        lv_account_dashbord.setOnClickListener(this);
        lv_account_information.setOnClickListener(this);
        lv_addressbook.setOnClickListener(this);
        lv_changepassword.setOnClickListener(this);
        lv_myorder.setOnClickListener(this);
        lv_myinvoice.setOnClickListener(this);
        lv_closeout.setOnClickListener(this);
        lv_termsandcondition.setOnClickListener(this);
        lv_freightpolicy.setOnClickListener(this);
        lv_logout.setOnClickListener(this);
        lv_itemonsell.setOnClickListener(this);

        return v;
    }

    private void AllocateMemory() {

        tv_account_title = v.findViewById(R.id.tv_account_title);
        lv_parent_account_freg = v.findViewById(R.id.lv_parent_account_freg);
        toolbar_myaccpunt = v.findViewById(R.id.toolbar_myaccpunt);
        lv_aboutus = v.findViewById(R.id.lv_aboutus);
        lv_account_dashbord = v.findViewById(R.id.lv_account_dashbord);
        lv_account_information = v.findViewById(R.id.lv_account_information);
        lv_addressbook = v.findViewById(R.id.lv_addressbook);
        lv_changepassword = v.findViewById(R.id.lv_changepassword);
        lv_myorder = v.findViewById(R.id.lv_myorder);
        lv_myinvoice = v.findViewById(R.id.lv_myinvoice);
        lv_closeout = v.findViewById(R.id.lv_closeout);
        lv_termsandcondition = v.findViewById(R.id.lv_termsandcondition);
        lv_freightpolicy = v.findViewById(R.id.lv_freightpolicy);
        lv_logout = v.findViewById(R.id.lv_logout);
        tv_login_ititle = v.findViewById(R.id.tv_login_ititle);
        lv_itemonsell = v.findViewById(R.id.lv_itemonsell);
        if (Login_preference.getLogin_flag(parent).equalsIgnoreCase("1")) {
            tv_login_ititle.setText("Logout");
        } else {
            tv_login_ititle.setText("Login");
        }

        tv_account_title.setTypeface(NavigationActivity.montserrat_semibold);

    }

    @Override
    public void onClick(View view) {
        if (view == lv_aboutus) {
            loadFragment(new About_usFregment());
        } else if (view == lv_account_dashbord) {
            if (Login_preference.getLogin_flag(parent).equalsIgnoreCase("1")) {
                pushFragment(new AccountDashboard(), "my_account");
            } else {
                LoginFragment myFragment = new LoginFragment();
                String screen = "Account";
                Bundle b = new Bundle();
                b.putString("screen", "" + screen);
                myFragment.setArguments(b);
                parent.getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.fade_in,
                                0, 0, R.anim.fade_out)
                        .addToBackStack("Account")
                        .add(R.id.framlayout, myFragment)
                        .commit();

            }
        } else if (view == lv_account_information) {
            if (Login_preference.getLogin_flag(parent).equalsIgnoreCase("1")) {
                pushFragment(new Account_information(), "my_account");
            } else {
                LoginFragment myFragment = new LoginFragment();
                String screen = "Account";
                Bundle b = new Bundle();
                b.putString("screen", "" + screen);
                myFragment.setArguments(b);
                parent.getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.fade_in,
                                0, 0, R.anim.fade_out)
                        .addToBackStack("Account")
                        .add(R.id.framlayout, myFragment)
                        .commit();

            }
        } else if (view == lv_addressbook) {
            if (Login_preference.getLogin_flag(parent).equalsIgnoreCase("1")) {
                pushFragment(new Address_Book(), "my_account");
            } else {
                LoginFragment myFragment = new LoginFragment();
                String screen = "Account";
                Bundle b = new Bundle();
                b.putString("screen", "" + screen);
                myFragment.setArguments(b);
                parent.getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.fade_in,
                                0, 0, R.anim.fade_out)
                        .addToBackStack("Account")
                        .add(R.id.framlayout, myFragment)
                        .commit();

            }
        } else if (view == lv_changepassword) {
            if (Login_preference.getLogin_flag(parent).equalsIgnoreCase("1")) {
                pushFragment(new ChangePassword(), "my_account");
            } else {
                LoginFragment myFragment = new LoginFragment();
                String screen = "Account";
                Bundle b = new Bundle();
                b.putString("screen", "" + screen);
                myFragment.setArguments(b);
                parent.getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.fade_in,
                                0, 0, R.anim.fade_out)
                        .addToBackStack("Account")
                        .add(R.id.framlayout, myFragment)
                        .commit();

            }
        } else if (view == lv_myorder) {
            if (Login_preference.getLogin_flag(parent).equalsIgnoreCase("1")) {
                pushFragment(new MyOrderFragment(), "my_account");
            } else {
                LoginFragment myFragment = new LoginFragment();
                String screen = "Account";
                Bundle b = new Bundle();
                b.putString("screen", "" + screen);
                myFragment.setArguments(b);
                parent.getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.fade_in,
                                0, 0, R.anim.fade_out)
                        .addToBackStack("Account")
                        .add(R.id.framlayout, myFragment)
                        .commit();

            }
        } else if (view == lv_myinvoice) {
            loadFragment(new OfferFragment());
        } else if (view == lv_itemonsell) {
            loadFragment(new OfferFragment());
        } else if (view == lv_closeout) {
            loadFragment(new CloseoutProductFragment());
        } else if (view == lv_termsandcondition) {
            loadFragment(new Terms_conditions());
        } else if (view == lv_freightpolicy) {
            loadFragment(new Freight_Policy());
        } else if (view == lv_logout) {
            if (Login_preference.getLogin_flag(parent).equalsIgnoreCase("1")) {
                new iOSDialogBuilder(parent)
                        .setTitle(getString(R.string.app_name))
                        .setSubtitle(getString(R.string.logoutmessge))
                        .setBoldPositiveLabel(true)
                        .setCancelable(false)
                        .setPositiveListener(getString(R.string.yes), new iOSDialogClickListener() {
                            @Override
                            public void onClick(iOSDialog dialog) {
                                Intent intent = new Intent(getActivity(), NavigationActivity.class);
                                startActivity(intent);

                               /* if(getActivity()!=null)
                                {
                                    Login_preference.setLogin_flag(getActivity(), "0");
                                }*/

                                Login_preference.setLogin_flag(getActivity(), "0");
                                Login_preference.prefsEditor.remove("storeid").apply();
                                Login_preference.prefsEditor.remove("storeName").apply();
                                Login_preference.prefsEditor.remove("customeremail").apply();
                                Login_preference.prefsEditor.remove("customerpassword").apply();
                                Login_preference.prefsEditor.remove("quote_id").apply();
                                Login_preference.prefsEditor.remove("customertoken").apply();
                                Login_preference.prefsEditor.remove("quoteiddk").apply();
                                Login_preference.prefsEditor.remove("quote_id").apply();
                                Login_preference.prefsEditor.remove("item_count").apply();
                                Login_preference.prefsEditor.remove("customer_id").apply();
                                Login_preference.prefsEditor.remove("email").apply();
                                Login_preference.prefsEditor.remove("fullname").apply();
                                ///   Login_preference.prefsEditor.remove("items_qty").apply();
                                //Login_preference.prefsEditor.remove("login_flag").apply();
                                Login_preference.prefsEditor.remove("wishlist_count").apply();
                                NavigationActivity.tv_bottomcount.setVisibility(View.GONE);
                                dialog.dismiss();
                                parent.finish();
                                Toast.makeText(parent, "" + parent.getResources().getString(R.string.logout_message), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeListener(getString(R.string.no), new iOSDialogClickListener() {
                            @Override
                            public void onClick(iOSDialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .build().show();
            } else {
                LoginFragment myFragment = new LoginFragment();
                String screen = "Account";
                Bundle b = new Bundle();
                b.putString("screen", "" + screen);
                myFragment.setArguments(b);
                parent.getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.fade_in,
                                0, 0, R.anim.fade_out)
                        .addToBackStack("Account")
                        .add(R.id.framlayout, myFragment)
                        .commit();

            }


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
                ft.addToBackStack(add_to_backstack);
                ft.setCustomAnimations(R.anim.fade_in,
                        0, 0, R.anim.fade_out).setCustomAnimations(R.anim.fade_in,
                        0, 0, R.anim.fade_out);
                ft.commit();
            }
        }
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        // inflater.inflate(R.menu.menu_home, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void loadFragment(Fragment fragment) {
        parent.getSupportFragmentManager()
                .beginTransaction().setCustomAnimations(R.anim.fade_in,
                0, 0, R.anim.fade_out).replace(R.id.framlayout, fragment)
                .addToBackStack(null).commit();

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
