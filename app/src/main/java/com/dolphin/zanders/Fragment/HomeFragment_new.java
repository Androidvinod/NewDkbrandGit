package com.dolphin.zanders.Fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import android.widget.Toast;

import com.dolphin.zanders.Activity.NavigationActivity;
import com.dolphin.zanders.Adapter.CatalogHomeAdapter;
import com.dolphin.zanders.Adapter.CloseoutHomeAdapter;
import com.dolphin.zanders.Adapter.Filterlist_Adapter;
import com.dolphin.zanders.Adapter.ItemOnsaleHomeAdapter;
import com.dolphin.zanders.Model.CategoryModel.Category;
import com.dolphin.zanders.Model.CategoryModel.CategoryModel;
import com.dolphin.zanders.Model.ProductlistModel.GetCategoryProductlist;
import com.dolphin.zanders.Model.ProductlistModel.GetCategoryProductlistInnerData;
import com.dolphin.zanders.Preference.Login_preference;
import com.dolphin.zanders.R;
import com.dolphin.zanders.Retrofit.ApiClient;
import com.dolphin.zanders.Retrofit.ApiInterface;
import com.dolphin.zanders.Util.CheckNetwork;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dolphin.zanders.Activity.NavigationActivity.bottom_navigation;
import static com.dolphin.zanders.Activity.NavigationActivity.drawer;

/**
 * A simple {@link Fragment} subclass.
 */

public class HomeFragment_new extends Fragment implements View.OnClickListener {
    RecyclerView rv_closeoutlist, rv_itemsonsale, rv_catalogs;
    CloseoutHomeAdapter closeoutHomeAdapter;
    ItemOnsaleHomeAdapter itemOnsaleHomeAdapter;
    ShimmerFrameLayout shimmer_view_closeout, shimmer_view_items, shimmer_view_catalog;
    LinearLayout lv_see_all_closeout, lv_itemsonsale_seeall, lv_catalog_seeall, lv_main_new_home;
    private String screen = "closeout_Product";
    private String screenitem = "offer";
    ApiInterface apiInterface;
    public static MenuItem login_home;
    String logingflag;
    CatalogHomeAdapter catalogHomeAdapter;
    public static List<GetCategoryProductlistInnerData> productlist = new ArrayList<GetCategoryProductlistInnerData>();
    public static List<GetCategoryProductlistInnerData> closeouProductlist = new ArrayList<GetCategoryProductlistInnerData>();
    NavigationActivity parent;
    public static Toolbar toolbar_home;
    ApiInterface api;
    public HomeFragment_new() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home_fragment_new, container, false);
        bottom_navigation.getMenu().getItem(0).setChecked(true);
        AllocateMemory(v);
        Filterlist_Adapter.filter_child_value_list.clear();
        Filterlist_Adapter.filter_grouppp_namelist.clear();
        FilterListFragment.selected_child="";
        api = ApiClient.getClient().create(ApiInterface.class);
        AttachRecyclerview();
        setupUI(lv_main_new_home);
        setHasOptionsMenu(true);
        parent = (NavigationActivity) getActivity();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        logingflag = Login_preference.getLogin_flag(parent);
        Log.e("91_login", "" + logingflag);
        hideKeyboard(parent);

        ((NavigationActivity) parent).setSupportActionBar(toolbar_home);
        ((NavigationActivity) parent).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
        ((NavigationActivity) parent).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        toolbar_home.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        getCategoryList();
        if (CheckNetwork.isNetworkAvailable(getActivity())) {
            productlist.clear();
            closeouProductlist.clear();
            CallCoseoutProductlistApi(ApiClient.VIEWTYPE);
            CallGetitemonsaleApi(ApiClient.VIEWTYPE);
            getCategoryList();

        } else {
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
        }

        lv_see_all_closeout.setOnClickListener(this);
        lv_itemsonsale_seeall.setOnClickListener(this);
        lv_catalog_seeall.setOnClickListener(this);

        /*v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                getActivity().finish();
                return false;
            }
        });*/

        return v;

    }
    private void get_Customer_QuoteId() {
        Log.e("customertoken",""+Login_preference.getCustomertoken(getActivity()));
        Call<Integer> customertoken = api.getQuoteid("Bearer "+Login_preference.getCustomertoken(getActivity()),"http://dkbraende.demoproject.info/rest/V1/carts/mine/?customerId=12466");
        customertoken.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                Log.e("res_quoteid",""+response.toString());
                Log.e("resquoteiddd",""+response.body());
                Login_preference.setdkQuoteId(getActivity(), String.valueOf(response.body()));

            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }


    private void AttachRecyclerview() {
        //closeout adapter
        closeoutHomeAdapter = new CloseoutHomeAdapter(getActivity());
        rv_closeoutlist.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rv_closeoutlist.setAdapter(closeoutHomeAdapter);

        //itemon sale adapter
        itemOnsaleHomeAdapter = new ItemOnsaleHomeAdapter(getActivity());
        rv_itemsonsale.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rv_itemsonsale.setAdapter(itemOnsaleHomeAdapter);
        Log.e("closeout_debug_2222", "" + closeouProductlist);

        //catalog adapter
        catalogHomeAdapter = new CatalogHomeAdapter(getActivity());
        rv_catalogs.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rv_catalogs.setAdapter(catalogHomeAdapter);
    }

    private void getCategoryList() {
        // lv_nodata_category.setVisibility(View.GONE);
        //   recv_category.setVisibility(View.VISIBLE);
        callCategoryApi().enqueue(new Callback<CategoryModel>() {
            @Override
            public void onResponse(Call<CategoryModel> call, Response<CategoryModel> response) {
                shimmer_view_catalog.stopShimmerAnimation();
                shimmer_view_catalog.setVisibility(View.GONE);
                CategoryModel categoryModel = response.body();
                Log.e("statussssss", "" + categoryModel.getStatus());
                if (categoryModel.getStatus().equalsIgnoreCase("Success")) {
                    shimmer_view_catalog.stopShimmerAnimation();
                    shimmer_view_catalog.setVisibility(View.GONE);
                    Log.e("Totallllll", "" + categoryModel.getCategory());
                    if (fetchResults_catalogs(response) != null || fetchResults_catalogs(response).size() == 0) {
                        Log.e("debug_175", "pages: " + fetchResults_catalogs(response));
                        //   recv_category.setVisibility(View.VISIBLE);
                        List<Category> results = fetchResults_catalogs(response);
                        List<Category> category_arr = response.body().getCategory();
                        if (category_arr.isEmpty()) {
                            //   recv_category.setVisibility(View.GONE);
                            //  lv_nodata_category.setVisibility(View.VISIBLE);
                        } else {
                            // recv_category.setVisibility(View.VISIBLE);
                            //  lv_nodata_category.setVisibility(View.GONE);
                        }
                        catalogHomeAdapter.addAll(results);
                    } else {
                        // lv_nodata_category.setVisibility(View.VISIBLE);
                        // recv_category.setVisibility(View.GONE);
                    }
                } else {
                    shimmer_view_catalog.stopShimmerAnimation();
                    shimmer_view_catalog.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), categoryModel.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<CategoryModel> call, Throwable t) {
                shimmer_view_catalog.stopShimmerAnimation();
                shimmer_view_catalog.setVisibility(View.GONE);
                // String error=  t.printStackTrace();
                Toast.makeText(getActivity(), "" + getActivity().getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();

                Log.e("debug_175125", "pages: " + t);
            }
        });
    }

    private Call<CategoryModel> callCategoryApi() {
        return apiInterface.getCategoryData();
    }

    private void CallGetitemonsaleApi(int viewtype) {
        productlist.clear();
        int page = 1;
        callitemonsaleapi(page).enqueue(new Callback<GetCategoryProductlist>() {
            @Override
            public void onResponse(Call<GetCategoryProductlist> call, Response<GetCategoryProductlist> response) {
                Log.e("responseeee", "" + response.body());
                GetCategoryProductlist model = response.body();

                shimmer_view_items.stopShimmerAnimation();
                shimmer_view_items.setVisibility(View.GONE);

                if (model.getStatus().equalsIgnoreCase("Success")) {
                    if (fetchResults(response) == null || fetchResults(response).size() == 0) {
                        Log.e("oferdebug_166", "" + fetchResults(response));
                        shimmer_view_items.stopShimmerAnimation();
                        shimmer_view_items.setVisibility(View.GONE);

                    } else {
                        Log.e("offerdebug_174", "" + fetchResults(response));
                        shimmer_view_items.stopShimmerAnimation();
                        shimmer_view_items.setVisibility(View.GONE);

                        List<GetCategoryProductlistInnerData> results = fetchResults(response);
                        if (results.isEmpty()) {
                            shimmer_view_items.stopShimmerAnimation();
                            shimmer_view_items.setVisibility(View.GONE);
                        } else {
                            shimmer_view_items.stopShimmerAnimation();
                            shimmer_view_items.setVisibility(View.GONE);

                        }
                        itemOnsaleHomeAdapter.addAll(results);
                        shimmer_view_items.stopShimmerAnimation();
                        shimmer_view_items.setVisibility(View.GONE);
                    }
                } else {
                    shimmer_view_items.stopShimmerAnimation();
                    shimmer_view_items.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "" + model.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetCategoryProductlist> call, Throwable t) {
                Toast.makeText(getActivity(), "" + getActivity().getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
                shimmer_view_items.stopShimmerAnimation();
                shimmer_view_items.setVisibility(View.GONE);
                Log.e("offer_debug_240", "mssage" + t);
            }
        });
    }

    private Call<GetCategoryProductlist> callitemonsaleapi(int page) {
        Log.e("debug_55fff", "==" + Filterlist_Adapter.filter_child_value_list);

        if (Login_preference.getLogin_flag(getActivity()).equalsIgnoreCase("1")) {
            Log.e("debug_offepage", "" + page);

            return apiInterface.getOfferList_withlogin(Login_preference.getcustomer_id(getActivity()), "", "", page);
        } else {
            Log.e("debug_offepage", "" + page);
            return apiInterface.getOfferList_withoutlogin(page, "", "");
        }
    }

    private void AllocateMemory(View v) {
        toolbar_home = v.findViewById(R.id.toolbar_home);
        lv_main_new_home = v.findViewById(R.id.lv_main_new_home);
        rv_closeoutlist = v.findViewById(R.id.rv_closeoutlist);
        rv_itemsonsale = v.findViewById(R.id.rv_itemsonsale);
        rv_catalogs = v.findViewById(R.id.rv_catalogs);
        shimmer_view_closeout = v.findViewById(R.id.shimmer_view_closeout);
        shimmer_view_items = v.findViewById(R.id.shimmer_view_items);
        shimmer_view_catalog = v.findViewById(R.id.shimmer_view_catalog);
        lv_see_all_closeout = v.findViewById(R.id.lv_see_all_closeout);
        lv_itemsonsale_seeall = v.findViewById(R.id.lv_itemsonsale_seeall);
        lv_catalog_seeall = v.findViewById(R.id.lv_catalog_seeall);


    }
    //calling api

    @SuppressLint("RestrictedApi")
    private void CallCoseoutProductlistApi(final int viewtype) {
        closeouProductlist.clear();
        //lv_pb_categoryproductlist.setVisibility(View.VISIBLE);
        //    lv_no_data_found.setVisibility(View.GONE);
        int page = 1;
        callCloseoutproductlistapi(page).enqueue(new Callback<GetCategoryProductlist>() {
            @Override
            public void onResponse(Call<GetCategoryProductlist> call, Response<GetCategoryProductlist> response) {
                Log.e("response_close_out", "" + response);
                GetCategoryProductlist model = response.body();
                shimmer_view_closeout.stopShimmerAnimation();
                shimmer_view_closeout.setVisibility(View.GONE);

                if (model.getStatus().equalsIgnoreCase("Success")) {
                    //  Log.e("isLastPage_168", "" + isLastPage);
                    if (fetchResults(response) == null || fetchResults(response).size() == 0) {
                        //   Log.e("debug_166", "" + fetchResults(response));
                        shimmer_view_closeout.stopShimmerAnimation();
                        shimmer_view_closeout.setVisibility(View.GONE);
                    } else {
                        //  Log.e("debug_174", "" + fetchResults(response));
                        shimmer_view_closeout.stopShimmerAnimation();
                        shimmer_view_closeout.setVisibility(View.GONE);

                        List<GetCategoryProductlistInnerData> results = fetchResultsclose(response);
                        if (results.isEmpty()) {
                            shimmer_view_closeout.stopShimmerAnimation();
                            shimmer_view_closeout.setVisibility(View.GONE);
                        } else {
                            shimmer_view_closeout.stopShimmerAnimation();
                            shimmer_view_closeout.setVisibility(View.GONE);
                        }
                        closeoutHomeAdapter.addAll(results);
                        shimmer_view_closeout.stopShimmerAnimation();
                        shimmer_view_closeout.setVisibility(View.GONE);
                    }
                    Log.e("closeout_debug_2222", "" + closeouProductlist);

                } else {
                    shimmer_view_closeout.stopShimmerAnimation();
                    shimmer_view_closeout.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "" + model.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetCategoryProductlist> call, Throwable t) {
                Toast.makeText(getActivity(), "" + getActivity().getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
                // Log.e("debug_240", "mssage" + t);
                shimmer_view_closeout.stopShimmerAnimation();
                shimmer_view_closeout.setVisibility(View.GONE);
            }
        });


    }

    private List<Category> fetchResults_catalogs(Response<CategoryModel> response) {
        CategoryModel categoryModel = response.body();

        Log.e("statussssss", "" + categoryModel.getStatus());
        if (categoryModel.getStatus().equalsIgnoreCase("Success")) {
            Log.e("Totallllll", "" + categoryModel.getCategory());
        }
        return categoryModel.getCategory();
    }

    private List<GetCategoryProductlistInnerData> fetchResults(Response<GetCategoryProductlist> response) {
        GetCategoryProductlist getCategoryProductlistInnerData = response.body();
        return getCategoryProductlistInnerData.getProduct();
    }

    private List<GetCategoryProductlistInnerData> fetchResultsclose(Response<GetCategoryProductlist> response) {
        GetCategoryProductlist getCategoryProductlistInnerData = response.body();
        return getCategoryProductlistInnerData.getProduct();
    }

    private Call<GetCategoryProductlist> callCloseoutproductlistapi(int page) {
        return apiInterface.getcloseoutProduct(page, Login_preference.getcustomer_id(getActivity()), "", "");
    }

    @Override
    public void onClick(View view) {
        if (view == lv_see_all_closeout) {
            pushFragment(new CloseoutProductFragment(), "Closeout Product");
        } else if (view == lv_itemsonsale_seeall) {
            pushFragment(new OfferFragment(), "Offerlist");
        } else if (view == lv_catalog_seeall) {
            pushFragment(new Catalogs_seeall_Fragment(), "catalogs");
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
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_home, menu);
        login_home = menu.findItem(R.id.login);
        Log.e("menu_389--", "");
        if (logingflag.equals("1")) {
            Log.e("menu_392--", "false");
            login_home.setVisible(false);
        } else {
            Log.e("menu_395--", "true");
            login_home.setVisible(true);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

           /* case R.id.search:
                Filterlist_Adapter.filter_child_value_list.clear();
                Filterlist_Adapter.filter_grouppp_namelist.clear();
                FilterListFragment.selected_child="";

                pushFragment(new SearchFragment(), "search");
                return true;*/
            case R.id.login:
                pushFragment(new LoginFragment(), "login");
                return true;

            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        shimmer_view_catalog.startShimmerAnimation();
        shimmer_view_closeout.startShimmerAnimation();
        shimmer_view_items.startShimmerAnimation();

    }

    @Override
    public void onPause() {
        shimmer_view_catalog.stopShimmerAnimation();
        shimmer_view_closeout.stopShimmerAnimation();
        shimmer_view_items.stopShimmerAnimation();
        super.onPause();
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


}
