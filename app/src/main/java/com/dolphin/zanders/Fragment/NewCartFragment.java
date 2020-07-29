package com.dolphin.zanders.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dolphin.zanders.Activity.NavigationActivity;
import com.dolphin.zanders.Adapter.CartlistAdapter_new;
import com.dolphin.zanders.Adapter.Filterlist_Adapter;
import com.dolphin.zanders.Adapter.NewCartListAdapter;
import com.dolphin.zanders.Model.CartlistModel.Cartlist;
import com.dolphin.zanders.Model.CartlistModel.CartlistInnerData;
import com.dolphin.zanders.Model.NewCartListModel;
import com.dolphin.zanders.Preference.Login_preference;
import com.dolphin.zanders.R;
import com.dolphin.zanders.Retrofit.ApiClient;
import com.dolphin.zanders.Retrofit.ApiClientcusome;
import com.dolphin.zanders.Retrofit.ApiInterface;
import com.dolphin.zanders.Util.CheckNetwork;
import com.dolphin.zanders.Util.WrapContentLinearLayoutManager;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dolphin.zanders.Activity.NavigationActivity.bottom_navigation;
import static com.dolphin.zanders.Activity.NavigationActivity.drawer;
import static com.dolphin.zanders.Activity.NavigationActivity.tv_bottomcount;
import static com.dolphin.zanders.Fragment.ProductListFragment.subcat_id;
import static com.dolphin.zanders.Fragment.ProductListFragment.subcatename;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewCartFragment extends Fragment implements View.OnClickListener {
    View v;
    Toolbar toolbar_cart;
    public static CoordinatorLayout cordinator_cart;
    public static RecyclerView recv_cart;
    public static LinearLayout lv_cart_checkout;
    public static Context context;
    public static LinearLayout lv_cartlist_progress, lv_nodata_cart, lv_cart_Main, lv_subtotal_layout, lv_cart_parent;

    public static TextView tv_messgenoti, tv_noting, tv_addlinal;
    public static TextView tv_cart_subtotal;
    //  public static Context context = null;
    public static ApiInterface cartlist;
    public static NewCartListAdapter cartlistAdapter;//CartlistAdapter
    private Paint p = new Paint();
    public static ShimmerFrameLayout shimmer_cartlist;
    MenuItem login;
    public static TextView tv_update_cart, tv_cart_tax, tv_taxt_value_cart, tv_cartt_subtotal, tv_subtotal_valuecart;
    public static List<NewCartListModel> cartlistdata = new ArrayList<>();
    NavigationActivity parent;

    public static String cartid, cart_sku = "", cart_product_type = "";


    public NewCartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_new_cart, container, false);
        bottom_navigation.getMenu().getItem(3).setChecked(true);
        cartlist = ApiClientcusome.getClient().create(ApiInterface.class);

        Filterlist_Adapter.filter_child_value_list.clear();
        FilterListFragment.selected_child = "";
        FilterListFragment.filter_old_childlist.clear();
        Filterlist_Adapter.filter_grouppp_namelist.clear();


        parent = (NavigationActivity) getActivity();
        context = getActivity();
        hideKeyboard(parent);
        AllocateMemory();
        AttachRecyclerView();
        //initSwipe();
        setupUI(lv_cart_parent);
        ((NavigationActivity) parent).setSupportActionBar(toolbar_cart);
        ((NavigationActivity) parent).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
        ((NavigationActivity) parent).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        toolbar_cart.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        if (CheckNetwork.isNetworkAvailable(parent)) {
            CallCartlistApi();
            getallpricedata();
        } else {
            Toast.makeText(parent, parent.getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
        }
        lv_cart_checkout.setOnClickListener(this);
        return v;
    }


    private void getallpricedata() {
        callgetpricedataapi().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("pricelistdata150", "" + response.body());
                if (response.isSuccessful() || response.code() == 200) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        JSONObject image = new JSONObject(jsonObject.getString("totals"));
                        Log.e("pricereponse_158", "" + image.getString("grand_total"));
                        Log.e("pricereponse_158", "" + image.getString("base_currency_code"));
                        tv_cart_subtotal.setText(image.getString("base_grand_total") + " " + Login_preference.getcurrencycode(context));
                        tv_subtotal_valuecart.setText(image.getString("base_subtotal") + " " + Login_preference.getcurrencycode(context));
                        tv_taxt_value_cart.setText(image.getString("base_tax_amount") + " " + Login_preference.getcurrencycode(context));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {

                    Log.e("error_293", "" + response.body());
                    NavigationActivity.get_Customer_tokenapi();
                    getallpricedata();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "" + context.getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();
                Log.e("debug_175125", "pages: " + t);
            }
        });
    }

    public static Call<ResponseBody> callgetpricedataapi() {
        Log.e("debugcustomertoen", "=" + Login_preference.getCustomertoken(context));

        return cartlist.getpricedata("Bearer " + Login_preference.getCustomertoken(context));
    }

    private void AttachRecyclerView() {
        cartlistAdapter = new NewCartListAdapter(parent, cartlistdata);//CartlistAdapter
        WrapContentLinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(parent, WrapContentLinearLayoutManager.VERTICAL, true);
        recv_cart.setLayoutManager(layoutManager);
        recv_cart.setAdapter(cartlistAdapter);
    }

    public static void CallCartlistApi() {
        cartlistdata.clear();
        tv_messgenoti.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        lv_nodata_cart.setVisibility(View.GONE);
        recv_cart.setVisibility(View.VISIBLE);
        lv_cart_Main.setVisibility(View.VISIBLE);

        callcartlistapi().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody cartlist = response.body();

                if (response.code() == 200 || response.isSuccessful()) {
                    shimmer_cartlist.stopShimmerAnimation();
                    shimmer_cartlist.setVisibility(View.GONE);
                    recv_cart.setVisibility(View.VISIBLE);
                    lv_cart_Main.setVisibility(View.VISIBLE);
                    lv_subtotal_layout.setVisibility(View.VISIBLE);
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().string());
                        Log.e("jsonArray", "" + jsonArray);
                        if (jsonArray.length() == 0) {
                            if (context != null) {
                                String cartitem_count = String.valueOf(jsonArray.length());
                                BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottom_navigation.getChildAt(0);
                                //cart item count
                                BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(3);
                                View notificationBadge = LayoutInflater.from(context).inflate(R.layout.badge_row, menuView, false);
                                tv_bottomcount = (TextView) notificationBadge.findViewById(R.id.badge);
                                if (cartitem_count.equalsIgnoreCase("null") || cartitem_count.equals("") || cartitem_count.equals("0")) {
                                    tv_bottomcount.setVisibility(View.GONE);
                                    Login_preference.setCart_item_count(context,"0");
                                } else {
                                    tv_bottomcount.setVisibility(View.VISIBLE);
                                    tv_bottomcount.setText(cartitem_count);
                                    Login_preference.setCart_item_count(context,cartitem_count);
                                }
                                itemView.addView(notificationBadge);
                            }
                        } else {
                            if (context != null) {
                                String cartitem_count = String.valueOf(jsonArray.length());
                                BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottom_navigation.getChildAt(0);
                                //cart item count
                                BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(3);
                                NavigationActivity.notificationBadge = LayoutInflater.from(context).inflate(R.layout.badge_row, menuView, false);
                                tv_bottomcount = (TextView) NavigationActivity.notificationBadge.findViewById(R.id.badge);
                                if (cartitem_count.equalsIgnoreCase("null") || cartitem_count.equals("") || cartitem_count.equals("0")) {
                                    tv_bottomcount.setVisibility(View.GONE);
                                    Login_preference.setCart_item_count(context,"0");
                                } else {
                                    tv_bottomcount.setVisibility(View.VISIBLE);
                                    tv_bottomcount.setText(cartitem_count);
                                    Login_preference.setCart_item_count(context,cartitem_count);
                                }
                                itemView.addView(NavigationActivity.notificationBadge);

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    // JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    //  cartid= String.valueOf(jsonArray.getJSONObject(0).getInt("item_id"));
                                }
                            }
                        }

                        if (jsonArray.length() == 0) {
                            lv_nodata_cart.setVisibility(View.VISIBLE);
                            cordinator_cart.setVisibility(View.GONE);
                            tv_noting.setText("Cart List is empty");
                        } else {

                            lv_nodata_cart.setVisibility(View.GONE);
                            cordinator_cart.setVisibility(View.VISIBLE);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                cartid = String.valueOf(jsonArray.getJSONObject(0).getInt("item_id"));

                                if (cart_sku.equalsIgnoreCase("")) {
                                    cart_sku = "entity[items]" + "[" + i + "]" + "[sku]=" + jsonObject.optString("sku");
                                } else {
                                    cart_sku += "&entity[items]" + "[" + i + "]" + "[sku]=" + jsonObject.optString("sku");
                                }

                                if (cart_product_type.equalsIgnoreCase("")) {
                                    cart_product_type = "entity[items]" + "[" + i + "]" + "[product_type]=" + jsonObject.optString("product_type");
                                } else {
                                    cart_product_type += "&entity[items]" + "[" + i + "]" + "[product_type]=" + jsonObject.optString("product_type");
                                }
                                Log.e("debug11", "=" + jsonObject.getJSONObject("extension_attributes").optString("image_url"));
                                cartlistdata.add(new NewCartListModel(jsonObject.getInt("item_id")
                                        , jsonObject.optString("sku")
                                        , jsonObject.optInt("qty")
                                        , jsonObject.optString("name")
                                        , jsonObject.optInt("price")
                                        , jsonObject.optString("product_type")
                                        , jsonObject.optString("quote_id"),
                                        jsonObject.getJSONObject("extension_attributes").optString("image_url")
                                ));
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    NavigationActivity.get_Customer_tokenapi();
                    CallCartlistApi();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "" + context.getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();
                Log.e("debug_175125", "pages: " + t);
            }
        });
    }

    public static Call<ResponseBody> callcartlistapi() {
        Log.e("email_237", "=" + Login_preference.getCustomertoken(context));
        return cartlist.getcartlistapi("Bearer " + Login_preference.getCustomertoken(context));
    }


    private void AllocateMemory() {
        tv_cartt_subtotal = v.findViewById(R.id.tv_cartt_subtotal);
        tv_taxt_value_cart = v.findViewById(R.id.tv_taxt_value_cart);
        tv_cart_tax = v.findViewById(R.id.tv_cart_tax);
        lv_cartlist_progress = v.findViewById(R.id.lv_cartlist_progress);
        cordinator_cart = v.findViewById(R.id.cordinator_cart);
        tv_update_cart = v.findViewById(R.id.tv_update_cart);
        tv_cart_subtotal = v.findViewById(R.id.tv_cart_subtotal);
        tv_noting = v.findViewById(R.id.tv_noting);
        toolbar_cart = v.findViewById(R.id.toolbar_cart);
        recv_cart = v.findViewById(R.id.recv_cart);
        lv_cart_checkout = v.findViewById(R.id.lv_cart_checkout);
        lv_cart_parent = v.findViewById(R.id.lv_cart_parent);
        tv_subtotal_valuecart = v.findViewById(R.id.tv_subtotal_valuecart);

        tv_messgenoti = v.findViewById(R.id.tv_messgenoti);
        lv_nodata_cart = v.findViewById(R.id.lv_nodata_cart);
        lv_cart_Main = v.findViewById(R.id.lv_cart_Main);
        lv_subtotal_layout = v.findViewById(R.id.lv_subtotal_layout);
        shimmer_cartlist = v.findViewById(R.id.shimmer_cartlist);
        tv_addlinal = v.findViewById(R.id.tv_addlinal);

        tv_cartt_subtotal.setTypeface(NavigationActivity.montserrat_semibold);
        tv_cart_tax.setTypeface(NavigationActivity.montserrat_semibold);
        tv_taxt_value_cart.setTypeface(NavigationActivity.montserrat_semibold);
        tv_subtotal_valuecart.setTypeface(NavigationActivity.montserrat_semibold);
        tv_cart_subtotal.setTypeface(NavigationActivity.montserrat_semibold);
    }


    @Override
    public void onClick(View v) {
        if (v == lv_cart_checkout) {
            if (NewCartListAdapter.flag == false) {
                Toast.makeText(context, context.getString(R.string.quantity_messge), Toast.LENGTH_SHORT).show();
            } else {

                Log.e("debug_cartod", "==" + cartid);
                Log.e("cart_sku", "==" + cart_sku);
                Bundle b = new Bundle();
                b.putString("cartid", "" + cartid);
                b.putString("cart_sku", "" + cart_sku);
                b.putString("cart_product_type", "" + cart_product_type);

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                NewCheckoutFragment myFragment = new NewCheckoutFragment();
                myFragment.setArguments(b);
                activity.getSupportFragmentManager()
                        .beginTransaction().setCustomAnimations(R.anim.fade_in,
                        0, 0, R.anim.fade_out).addToBackStack(null).
                        replace(R.id.framlayout, myFragment).commit();
            }

            //pushFragment(new NewCheckoutFragment(), "Checkout");


        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_home, menu);
        login = menu.findItem(R.id.login);
        if (Login_preference.getLogin_flag(parent).equalsIgnoreCase("1")) {
            login.setVisible(false);
        } else {
            login.setVisible(true);
        }
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
                ft.setCustomAnimations(R.anim.fade_in,
                        0, 0, R.anim.fade_out).setCustomAnimations(R.anim.fade_in,
                        0, 0, R.anim.fade_out);
                ft.commit();
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.search:
                Filterlist_Adapter.filter_child_value_list.clear();
                FilterListFragment.selected_child = "";
                FilterListFragment.filter_old_childlist.clear();
                Filterlist_Adapter.filter_grouppp_namelist.clear();

                pushFragment(new SearchFragment(), "search");
                return true;

            case android.R.id.home:
                parent.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        shimmer_cartlist.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        shimmer_cartlist.stopShimmerAnimation();
        super.onPause();
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
