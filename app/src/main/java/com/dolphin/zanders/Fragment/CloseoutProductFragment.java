package com.dolphin.zanders.Fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dolphin.zanders.Activity.NavigationActivity;
import com.dolphin.zanders.Adapter.CloseOutProductAdapter;
import com.dolphin.zanders.Adapter.Filterlist_Adapter;
import com.dolphin.zanders.Adapter.ProductListAdapter;
import com.dolphin.zanders.Model.ProductlistModel.GetCategoryProductlist;
import com.dolphin.zanders.Model.ProductlistModel.GetCategoryProductlistInnerData;
import com.dolphin.zanders.Model.Sortlist_model.GetSortModel;
import com.dolphin.zanders.Model.Sortlist_model.SortListSort;
import com.dolphin.zanders.Preference.Login_preference;
import com.dolphin.zanders.R;
import com.dolphin.zanders.Retrofit.ApiClient;
import com.dolphin.zanders.Retrofit.ApiInterface;
import com.dolphin.zanders.Util.CheckNetwork;
import com.dolphin.zanders.Util.WrapContentGridLayoutManager;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CloseoutProductFragment extends Fragment implements View.OnClickListener {

    TextView tv_sort_by_closout;
    public static LinearLayout lv_closeout_main, lv_progress_closeout_bottom, lv_closeout_progress, lvnodata_closeout, lv_maincategoty_products_main;
    Spinner spinner_closeout;
    NestedScrollView nested_scroll_closeout;
    public static ShimmerFrameLayout shimmer_closeout;
    RecyclerView recycler_closeout, recyclerView_sort;
    FloatingActionButton btn_fab_closeout;
    Toolbar toolbar_closeout;
    public static CoordinatorLayout coordinator_closeout_main;

    public static List<GetCategoryProductlistInnerData> closeouProductlist = new ArrayList<GetCategoryProductlistInnerData>();
    ApiInterface apiInterface;
    CloseOutProductAdapter productListAdapter;

    //pagination
    WrapContentGridLayoutManager layoutManager;
    int pastvisibleitem, visibleitemcount, totalitemcount;
    int page_no = 1, page;
    int isLastPage;
    private String screen = "closeout_Product";
    LinearLayout lv_asc_desc_arrow;
    ImageView iv_product_up_down;
    String asc_desc="asc";
    MenuItem login;
    View v;
    NavigationActivity parent;

    String sort_selected = "";
    String default_selection;

    public static ArrayList<String> sort_name = new ArrayList<String>();
    public static ArrayList<String> sort_label = new ArrayList<String>();

   /* String[] sort = new String[]{
            "Position",
            "Name",
            "Item Number",
            "Price",
            "Manufacturer"
    };*/

    public CloseoutProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_closeout_product, container, false);
        AllocateMemory();
        closeouProductlist.clear();
        parent=(NavigationActivity)getActivity();
        hideKeyboard(parent);
        setHasOptionsMenu(true);
        setupUI(lv_closeout_main);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        //fill data in recyclerview and set adapter
        AttachRecyclerView();

        ((NavigationActivity)parent).setSupportActionBar(toolbar_closeout);
        ((NavigationActivity)parent).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((NavigationActivity)parent).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_36dp);

        // clicklistner
        btn_fab_closeout.setOnClickListener(this);
        lv_asc_desc_arrow.setOnClickListener(this);
        // lv_spinner_closeout.setOnClickListener(this);
        if (CheckNetwork.isNetworkAvailable(getActivity())) {
            CALL_SORT_API();
            CallGetCategoryProductlistApi(page_no, ApiClient.VIEWTYPE);
        } else {
            Toast.makeText(getActivity(), parent.getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
        }

        spinner_closeout.setSelection(0);

        //spinner_closeout.setPrompt("Sort By");
        //spinner_closeout.setAdapter(new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, sort));

        spinner_closeout.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                /*int selected_item_position = spinner_closeout.getSelectedItemPosition();
                String selecteditem = String.valueOf(spinner_closeout.getSelectedItem());
                Toast.makeText(getContext(), selecteditem, Toast.LENGTH_SHORT).show();
                Log.e("selected_item",""+selected_item_position);*/
                int selected_item_position = spinner_closeout.getSelectedItemPosition();
                sort_selected = sort_name.get(selected_item_position);
                Log.e("code_selected", "" + sort_selected);

                if (CheckNetwork.isNetworkAvailable(getActivity())) {
                    CallGetCategoryProductlistApi(page_no, ApiClient.VIEWTYPE);
                } else {
                    Toast.makeText(getActivity(), parent.getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        return v;
    }

    private void CALL_SORT_API() {
        sort_label.clear();
        sort_name.clear();
        callsort().enqueue(new Callback<GetSortModel>() {
            @Override
            public void onResponse(Call<GetSortModel> call, Response<GetSortModel> response) {
                if (response.body().getStatus().equals("error")) {

                } else {
                    List<SortListSort> getSortModels = fetchResultsSort(response);
                    default_selection = response.body().getDefaultSort().getValue();
                    Log.e("selected_default_item",""+default_selection);
                    /*sort_label.add(response.body().getDefaultSort().getValue());
                    sort_name.add(response.body().getDefaultSort().getName());*/

                    for (int i = 0; i < getSortModels.size(); i++) {
                        sort_name.add(getSortModels.get(i).getName());
                        sort_label.add(getSortModels.get(i).getLabel());
                        Log.e("sort_innerrr_label", "" + getSortModels.get(i).getLabel());
                    }

                    if(getActivity()!=null)
                    {
                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_spinner_item, sort_label);
                        spinnerArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_item); // The drop down view
                        spinner_closeout.setAdapter(spinnerArrayAdapter);

                    }

                    int pos = 0;
                    for (int i=0;i<sort_name.size();i++)
                    {
                        pos=sort_label.indexOf(default_selection);
                        Log.e("debug_186",""+pos);
                    }
                    spinner_closeout.setSelection(pos);

                }
            }

            @Override
            public void onFailure(Call<GetSortModel> call, Throwable t) {

            }
        });
    }

    private List<SortListSort> fetchResultsSort(Response<GetSortModel> response) {
        GetSortModel getSortModel = response.body();
        return getSortModel.getSortList();
    }

    private Call<GetSortModel> callsort() {
        return apiInterface.getsort();
    }


    //calling api
    @SuppressLint("RestrictedApi")
    private void CallGetCategoryProductlistApi(int pageno, final int viewtype) {
        if (pageno == 1) {
            Log.e("closeout_debug_218", "" + pageno);
            lv_progress_closeout_bottom.setVisibility(View.GONE);
            closeouProductlist.clear();
            btn_fab_closeout.setVisibility(View.GONE);
            lvnodata_closeout.setVisibility(View.GONE);
            shimmer_closeout.setVisibility(View.VISIBLE);
            coordinator_closeout_main.setVisibility(View.VISIBLE);

        } else {
            btn_fab_closeout.setVisibility(View.GONE);
            Log.e("closeout_debug_227", "" + pageno);
            lv_progress_closeout_bottom.setVisibility(View.VISIBLE);
            lvnodata_closeout.setVisibility(View.GONE);
            coordinator_closeout_main.setVisibility(View.VISIBLE);

        }
        page = pageno;
        //lv_pb_categoryproductlist.setVisibility(View.VISIBLE);
        //    lv_no_data_found.setVisibility(View.GONE);
        callcategoryproductlistapi(page).enqueue(new Callback<GetCategoryProductlist>() {
            @Override
            public void onResponse(Call<GetCategoryProductlist> call, Response<GetCategoryProductlist> response) {
                Log.e("response_close_out",""+response);
                GetCategoryProductlist model = response.body();
                btn_fab_closeout.setVisibility(View.GONE);
                shimmer_closeout.stopShimmerAnimation();
                shimmer_closeout.setVisibility(View.GONE);
                lvnodata_closeout.setVisibility(View.GONE);
                coordinator_closeout_main.setVisibility(View.VISIBLE);
                lv_progress_closeout_bottom.setVisibility(View.GONE);

                if (model.getStatus().equalsIgnoreCase("Success")) {
                    isLastPage = model.getIsLast();

                    //  Log.e("isLastPage_168", "" + isLastPage);
                    if (fetchResults(response) == null || fetchResults(response).size() == 0) {
                        //   Log.e("debug_166", "" + fetchResults(response));
                        shimmer_closeout.stopShimmerAnimation();
                        shimmer_closeout.setVisibility(View.GONE);
                        lvnodata_closeout.setVisibility(View.VISIBLE);
                        coordinator_closeout_main.setVisibility(View.GONE);
                        lv_progress_closeout_bottom.setVisibility(View.GONE);
                        btn_fab_closeout.setVisibility(View.GONE);
                    } else {
                        //  Log.e("debug_174", "" + fetchResults(response));
                        shimmer_closeout.stopShimmerAnimation();
                        shimmer_closeout.setVisibility(View.GONE);
                        lvnodata_closeout.setVisibility(View.GONE);
                        coordinator_closeout_main.setVisibility(View.VISIBLE);
                        lv_progress_closeout_bottom.setVisibility(View.GONE);

                        List<GetCategoryProductlistInnerData> results = fetchResults(response);
                        if (results.isEmpty()) {
                            shimmer_closeout.stopShimmerAnimation();
                            shimmer_closeout.setVisibility(View.GONE);
                            lvnodata_closeout.setVisibility(View.VISIBLE);
                            coordinator_closeout_main.setVisibility(View.GONE);
                            lv_progress_closeout_bottom.setVisibility(View.GONE);
                            btn_fab_closeout.setVisibility(View.GONE);
                        } else {
                            shimmer_closeout.stopShimmerAnimation();
                            shimmer_closeout.setVisibility(View.GONE);
                            lvnodata_closeout.setVisibility(View.GONE);
                            coordinator_closeout_main.setVisibility(View.VISIBLE);
                            lv_progress_closeout_bottom.setVisibility(View.GONE);

                            for (int i = 0; i < results.size(); i++) {
                                closeouProductlist.add(new GetCategoryProductlistInnerData
                                        (results.get(i).getType(),
                                                results.get(i).getProductId(),
                                                results.get(i).getProductName(),
                                                results.get(i).getProductSku(),
                                                results.get(i).getProductPrice(),
                                                results.get(i).getMsrp(),
                                                results.get(i).getMap(),
                                                results.get(i).getProductSpecialprice(),
                                                results.get(i).getProductImage(),
                                                results.get(i).getProductThumbnail(),
                                                results.get(i).getWishlist(),
                                                results.get(i).getAvailability(),
                                                results.get(i).getAvailable_qty()));
                            }
                            btn_fab_closeout.setVisibility(View.VISIBLE);
                        }
                    }
                    if (viewtype == 0) {
                        Log.e("closeout_debug_197", "" + viewtype);
                        productListAdapter = new CloseOutProductAdapter(getActivity(), closeouProductlist, screen);
                        layoutManager = new WrapContentGridLayoutManager(getActivity(), 2);
                        recycler_closeout.setLayoutManager(layoutManager);
                        recycler_closeout.setAdapter(productListAdapter);
                    } else if (viewtype == 2) {
                        Log.e("closeout_debug_207", "" + viewtype);
                        productListAdapter = new CloseOutProductAdapter(getActivity(), closeouProductlist, screen);
                        layoutManager = new WrapContentGridLayoutManager(getActivity(), 1);
                        recycler_closeout.setLayoutManager(layoutManager);
                        recycler_closeout.setAdapter(productListAdapter);
                    } else {
                        Log.e("closeout_debug_209", "" + viewtype);
                        productListAdapter = new CloseOutProductAdapter(getActivity(), closeouProductlist, screen);
                        layoutManager = new WrapContentGridLayoutManager(getActivity(), 2);
                        recycler_closeout.setLayoutManager(layoutManager);
                        recycler_closeout.setAdapter(productListAdapter);
                    }
                    Log.e("closeout_debug_2222", "" + closeouProductlist);

                } else {
                    shimmer_closeout.stopShimmerAnimation();
                    shimmer_closeout.setVisibility(View.GONE);
                    lvnodata_closeout.setVisibility(View.GONE);
                    coordinator_closeout_main.setVisibility(View.VISIBLE);
                    lv_progress_closeout_bottom.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "" + model.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetCategoryProductlist> call, Throwable t) {
                Toast.makeText(getActivity(), "" +parent.getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
                // Log.e("debug_240", "mssage" + t);
                shimmer_closeout.stopShimmerAnimation();
                shimmer_closeout.setVisibility(View.GONE);
                lvnodata_closeout.setVisibility(View.GONE);
                coordinator_closeout_main.setVisibility(View.VISIBLE);
                lv_progress_closeout_bottom.setVisibility(View.GONE);
                btn_fab_closeout.setVisibility(View.GONE);
            }
        });

        nested_scroll_closeout.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (v.getChildAt(v.getChildCount() - 1) != null) {
                    if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                            scrollY > oldScrollY) {
                        visibleitemcount = layoutManager.getChildCount();
                        totalitemcount = layoutManager.getItemCount();
                        pastvisibleitem = layoutManager.findFirstVisibleItemPosition();

                        //  Log.e("debug_258", "current_page=" + page);
                        if ((visibleitemcount + pastvisibleitem) >= totalitemcount) {
                            //    Log.e("debug33islast", "=" + isLastPage);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (isLastPage == 0) {
                                        page++;
                                        //    Log.e("debug_261", "loadscroll" + page);
                                        //   Log.e("debug_270islast", "loadscroll" + isLastPage);
                                        lv_progress_closeout_bottom.setVisibility(View.VISIBLE);
                                        if (CheckNetwork.isNetworkAvailable(getActivity())) {
                                            CallGetCategoryProductlistApi(page, ApiClient.VIEWTYPE);
                                        } else {
                                            Toast.makeText(getActivity(), parent.getResources().getString(R.string.nointernet), Toast.LENGTH_SHORT).show();
                                        }

                                    } else {
                                        // Log.e("debug_2704", "" + isLastPage);
                                        lv_progress_closeout_bottom.setVisibility(View.GONE);
                                    }
                                }
                            }, 1000);
                        }

                    }
                }
            }
        });

    }

    private List<GetCategoryProductlistInnerData> fetchResults(Response<GetCategoryProductlist> response) {
        GetCategoryProductlist getCategoryProductlistInnerData = response.body();
        return getCategoryProductlistInnerData.getProduct();
    }

    private Call<GetCategoryProductlist> callcategoryproductlistapi(int page) {
        Log.e("sort_selection",""+sort_selected);
        return apiInterface.getcloseoutProduct(page, Login_preference.getcustomer_id(getActivity()),asc_desc, sort_selected);
    }

    private void AttachRecyclerView() {
        productListAdapter = new CloseOutProductAdapter(getActivity(), closeouProductlist, screen);
        layoutManager = new WrapContentGridLayoutManager(getActivity(), 2);
        recycler_closeout.setLayoutManager(layoutManager);
        recycler_closeout.setAdapter(productListAdapter);
        Log.e("debug_3704",""+ApiClient.VIEWTYPE);
        if(ApiClient.VIEWTYPE==0)
        {
            btn_fab_closeout.setImageDrawable(parent.getResources().getDrawable(R.drawable.ic_view_list_white_36dp));

            Log.e("debug_3",""+ApiClient.VIEWTYPE);
            productListAdapter = new CloseOutProductAdapter(getActivity(), closeouProductlist, screen);
            layoutManager = new WrapContentGridLayoutManager(getActivity(), 2);
            recycler_closeout.setLayoutManager(layoutManager);
            recycler_closeout.setAdapter(productListAdapter);
            Log.e("debug_3704",""+ApiClient.VIEWTYPE);
        }else if(ApiClient.VIEWTYPE==2) {
            btn_fab_closeout.setImageDrawable(parent.getResources().getDrawable(R.drawable.grid_ic));

            productListAdapter = new CloseOutProductAdapter(getActivity(), closeouProductlist, screen);
            layoutManager = new WrapContentGridLayoutManager(getActivity(), 1);
            recycler_closeout.setLayoutManager(layoutManager);
            recycler_closeout.setAdapter(productListAdapter);
            Log.e("debug_3704",""+ApiClient.VIEWTYPE);
        }else {
            btn_fab_closeout.setImageDrawable(parent.getResources().getDrawable(R.drawable.ic_view_list_white_36dp));

            productListAdapter = new CloseOutProductAdapter(getActivity(), closeouProductlist, screen);
            layoutManager = new WrapContentGridLayoutManager(getActivity(), 2);
            recycler_closeout.setLayoutManager(layoutManager);
            recycler_closeout.setAdapter(productListAdapter);
            Log.e("debug_3704",""+ApiClient.VIEWTYPE);
        }

    }

    private void AllocateMemory() {

        lv_closeout_main = v.findViewById(R.id.lv_closeout_main);
        lv_asc_desc_arrow = v.findViewById(R.id.lv_asc_desc_arrow);
        iv_product_up_down = v.findViewById(R.id.iv_product_up_down);
        coordinator_closeout_main = v.findViewById(R.id.coordinator_closeout_main);
        lvnodata_closeout = v.findViewById(R.id.lvnodata_closeout);
        lv_closeout_progress = v.findViewById(R.id.lv_closeout_progress);
        toolbar_closeout = v.findViewById(R.id.toolbar_closeout);
        tv_sort_by_closout = v.findViewById(R.id.tv_sort_by_closout);
        //lv_spinner_closeout = v.findViewById(R.id.lv_spinner_closeout);
        lv_progress_closeout_bottom = v.findViewById(R.id.lv_progress_closeout_bottom);
        spinner_closeout = v.findViewById(R.id.spinner_closeout);
        nested_scroll_closeout = v.findViewById(R.id.nested_scroll_closeout);
        shimmer_closeout = v.findViewById(R.id.shimmer_closeout);
        recycler_closeout = v.findViewById(R.id.recycler_closeout);
        btn_fab_closeout = v.findViewById(R.id.btn_fab_closeout);
    }

    @Override
    public void onResume() {
        super.onResume();
        shimmer_closeout.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        shimmer_closeout.stopShimmerAnimation();
        super.onPause();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_home, menu);
        login = menu.findItem(R.id.login);
        if(Login_preference.getLogin_flag(getActivity()).equalsIgnoreCase("1"))
        {
            login.setVisible(false);
        }else {
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
                pushFragment(new SearchFragment(), "search");
                return true;

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
    public void onClick(View v) {
        if (v == btn_fab_closeout) {

            if (ApiClient.VIEWTYPE == 0) {
                btn_fab_closeout.setImageDrawable(parent.getResources().getDrawable(R.drawable.grid_ic));

                //  Log.e("data_show_verticle_btn", "grid_to_verticle");
                productListAdapter = new CloseOutProductAdapter(getActivity(), closeouProductlist, screen);
                layoutManager = new WrapContentGridLayoutManager(getActivity(), 1);
                recycler_closeout.setLayoutManager(layoutManager);
                recycler_closeout.setAdapter(productListAdapter);
                ApiClient.VIEWTYPE = 2;
                // flag=true;
            } else if (ApiClient.VIEWTYPE == 2) {
                btn_fab_closeout.setImageDrawable(parent.getResources().getDrawable(R.drawable.ic_view_list_white_36dp));
                productListAdapter = new CloseOutProductAdapter(getActivity(), closeouProductlist, screen);
                layoutManager = new WrapContentGridLayoutManager(getActivity(), 2);
                recycler_closeout.setLayoutManager(layoutManager);
                recycler_closeout.setAdapter(productListAdapter);
                ApiClient.VIEWTYPE = 0;
                //flag=false;
            }

        }else if(v==lv_asc_desc_arrow)
        {
            if (asc_desc.equals("asc")){
                asc_desc="desc";
                CallGetCategoryProductlistApi(page_no, ApiClient.VIEWTYPE);
                iv_product_up_down.setImageResource(R.drawable.ic_red_down);


            }else if (asc_desc.equals("desc")){
                asc_desc="asc";
                CallGetCategoryProductlistApi(page_no, ApiClient.VIEWTYPE);
                iv_product_up_down.setImageResource(R.drawable.ic_red_up);

            }
        }
    }
}
