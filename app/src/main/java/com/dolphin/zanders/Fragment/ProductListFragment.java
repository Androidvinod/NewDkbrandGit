package com.dolphin.zanders.Fragment;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.TextUtils;
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
 * A simple {@link Fragment} subclass.
 */
public class ProductListFragment extends Fragment implements View.OnClickListener {
    LinearLayout lvnodata_productlist, lv_maincategoty_products_main, lv_progress_product_bottom,lv_main_search_filter_sort,lv_produt_parent,lv_filter_main;
    RecyclerView recycler_productlist;
    FloatingActionButton btn_fab;
    NestedScrollView nested_scroll_productlist;
    TextView tv_product_toolbar,tv_totalfilter;
    public static CoordinatorLayout coordinator_product_main;
    public static LinearLayout lv_productlist_progress;
    Spinner spinner_sortby;
    String sort_selected = "";
    Integer srt_select;
    int pos = 0;
    ApiInterface apiInterface;
    Toolbar toolbar_productlist;
    public static String subcat_id, subcatename,selected_child_url;
    public static ArrayList<String> sort_name = new ArrayList<String>();
    public static ArrayList<String> sort_label = new ArrayList<String>();
    MenuItem login;
  //  public  ArrayList<String> filter_child_id = new ArrayList<>();
  //  public  ArrayList<String> filter_group_name = new ArrayList<>();
    public List<String> filtercount = new ArrayList<>();
    String default_selection;
    Bundle b;
    LinearLayout lv_filter,lv_asc_desc_arrow;
    ImageView iv_product_up_down;
    String asc_desc="asc";

    View v;
    private ShimmerFrameLayout shimmer_productlist;
    String sort_value;
    public static List<GetCategoryProductlistInnerData> productlist = new ArrayList<GetCategoryProductlistInnerData>();
    public static List<String> filter_group_namelist = new ArrayList<String>();
    ProductListAdapter productListAdapter;
    //pagination
    WrapContentGridLayoutManager layoutManager;
    int pastvisibleitem, visibleitemcount, totalitemcount;
    int page_no = 1, page;
    int isLastPage;
    private String screen="product_list",screen_type;
    NavigationActivity parent;

    boolean debug=true;
    String dir="asc";


    public ProductListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_product_list, container, false);
        productlist.clear();
        setHasOptionsMenu(true);
        AllocateMemory();
        parent=(NavigationActivity) getActivity();
        setupUI(lv_produt_parent);
        hideKeyboard(parent);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        //fill data in recyclerview and set adapter
        AttachRecyclerView();
        Log.e("debug_pro_filter", "" + Filterlist_Adapter.filter_child_value_list);
        ((NavigationActivity) parent).setSupportActionBar(toolbar_productlist);
        ((NavigationActivity) parent).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((NavigationActivity) parent).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_36dp);

        srt_select = 1000;
        b = this.getArguments();

        if (b != null) {
            subcat_id = b.getString("subcat_id");
            subcatename = b.getString("subcatename");
            screen_type = b.getString("screen");
            srt_select = b.containsKey("sort_select") ? b.getInt("sort_select") : 1000;

            filter_group_namelist=b.getStringArrayList("filter_group_namelist");
            selected_child_url = b.getString("selected_child");

//            srt_select = b.getInt("sort_select");

            Log.e("srt_slct",""+srt_select);
            Log.e("procudt_id_141",""+subcat_id);
            tv_product_toolbar.setText(subcatename);
            Log.e("screen_type", "" + screen_type);
        } else {
            Log.e(".bundle", "" + b);
            tv_product_toolbar.setText("Poduct List");
        }

        /*if (srt_select!=null){
            spinner_sortby.setSelection(Integer.parseInt(srt_select));
        }*/
        // pos = 2
        // srt_select = 0

        spinner_sortby.setSelection(srt_select); // position set

        if (CheckNetwork.isNetworkAvailable(parent)) {
            CALL_SORT_API();
        } else {
            Toast.makeText(parent, parent.getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
        }
        /*if (CheckNetwork.isNetworkAvailable(parent)) {
            CALL_SORT_API();
        } else {
            Toast.makeText(parent, parent.getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
        }*/

        Log.e("debug_166",""+srt_select);
        if (subcatename.equals("Daily specials products")) {
            lv_main_search_filter_sort.setVisibility(View.GONE);

        } else if (subcatename.equals("Popular products")) {
            lv_main_search_filter_sort.setVisibility(View.GONE);
        }
        else{
            lv_main_search_filter_sort.setVisibility(View.VISIBLE);
        }
        ///manufacturer
        if(screen_type==null)
        {

        }
        else if(screen_type.equals("Manufacturer")) {
            lv_filter_main.setVisibility(View.INVISIBLE);
        }


        if(filter_group_namelist ==null )
        {
            tv_totalfilter.setText("Total Filter [0]");
        }else {
            Log.e("filtercount", "" + filtercount);
            tv_totalfilter.setText("Total Filter ["+filter_group_namelist.size()+"]");
        }



        spinner_sortby.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                int selected_item_position = spinner_sortby.getSelectedItemPosition();
                sort_selected = sort_name.get(selected_item_position);
                Log.e("code_selected", "" + sort_selected);
                hideKeyboard(parent);
                if (CheckNetwork.isNetworkAvailable(parent)) {
                    CallGetCategoryProductlistApi(page_no, ApiClient.VIEWTYPE);
                } else {
                    Toast.makeText(parent, parent.getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        // clicklistner
        btn_fab.setOnClickListener(this);
        lv_filter.setOnClickListener(this);
        lv_asc_desc_arrow.setOnClickListener(this);

        if (CheckNetwork.isNetworkAvailable(parent)) {
            Log.e("debug_208apiclient",""+ApiClient.VIEWTYPE);

         //   CallGetCategoryProductlistApi(page_no, ApiClient.VIEWTYPE);
        } else {
            Toast.makeText(parent, parent.getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
        }
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
                    Log.e("selected_default_item", "" + default_selection);
                    /*sort_label.add(response.body().getDefaultSort().getValue());
                    sort_name.add(response.body().getDefaultSort().getName());*/

                    for (int i = 0; i < getSortModels.size(); i++) {
                        sort_name.add(getSortModels.get(i).getName());
                        sort_label.add(getSortModels.get(i).getLabel());
                        Log.e("sort_innerrr_label", "" + getSortModels.get(i).getLabel());
                    }

                    if (sort_label.isEmpty()){
                        Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_SHORT).show();
                    }else {
                        if (parent!=null){
                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(parent, R.layout.simple_spinner_item, sort_label);
                            spinnerArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_item); // The drop down view
                            spinner_sortby.setAdapter(spinnerArrayAdapter);
                            // pos = null
                            for (int i = 0; i < sort_name.size(); i++) {
                                pos = sort_label.indexOf(default_selection); // 2
                                Log.e("debug_186", "" + pos);
                            }
                            // pos = 2
                            // srt_select = null
                            Log.e("debug_301",""+srt_select);
//                            srt_select = sort_label.indexOf(default_selection);
                            if(srt_select == 1000){
                                Log.e("debug_307",""+pos);
                                spinner_sortby.setSelection(pos); // item number

                            }else{

                                Log.e("debug_310",""+srt_select);
                                spinner_sortby.setSelection(srt_select); // post = 2, post = 0,
                            }

                        }


                    }

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
        if(screen_type==null || screen_type.equals("null"))
        {
            return apiInterface.getProductlistsort(subcat_id);
        }
        else if(screen_type.equals("Manufacturer")) {
            return apiInterface.getsort();
        }else {
            return apiInterface.getProductlistsort(subcat_id);
        }
    }

    private void AttachRecyclerView() {
        Log.e("debug_3704",""+ApiClient.VIEWTYPE);
        if(ApiClient.VIEWTYPE==0)
        {
            btn_fab.setImageDrawable(parent.getResources().getDrawable(R.drawable.ic_view_list_white_36dp));
            Log.e("debug_3",""+ApiClient.VIEWTYPE);
            productListAdapter = new ProductListAdapter(parent, productlist, screen);
            layoutManager = new WrapContentGridLayoutManager(parent, 2);
            recycler_productlist.setLayoutManager(layoutManager);
            recycler_productlist.setAdapter(productListAdapter);
        }else if(ApiClient.VIEWTYPE==2) {
            btn_fab.setImageDrawable(parent.getResources().getDrawable(R.drawable.grid_ic));
            Log.e("debug_3dff",""+ApiClient.VIEWTYPE);
            productListAdapter = new ProductListAdapter(parent, productlist, screen);
            layoutManager = new WrapContentGridLayoutManager(parent, 1);
            recycler_productlist.setLayoutManager(layoutManager);
            recycler_productlist.setAdapter(productListAdapter);
        }else {
            btn_fab.setImageDrawable(parent.getResources().getDrawable(R.drawable.ic_view_list_white_36dp));

            Log.e("debug_3d",""+ApiClient.VIEWTYPE);
            productListAdapter = new ProductListAdapter(parent, productlist, screen);
            layoutManager = new WrapContentGridLayoutManager(parent, 2);
            recycler_productlist.setLayoutManager(layoutManager);
            recycler_productlist.setAdapter(productListAdapter);
        }
    }

    @SuppressLint("RestrictedApi")
    private void CallGetCategoryProductlistApi(int pageno, final int viewtype) {
        if (pageno == 1) {
            Log.e("debug_page1", "" + pageno);
            lv_progress_product_bottom.setVisibility(View.GONE);
            productlist.clear();
            btn_fab.setVisibility(View.GONE);
            lvnodata_productlist.setVisibility(View.GONE);
            shimmer_productlist.setVisibility(View.VISIBLE);
            coordinator_product_main.setVisibility(View.VISIBLE);

        } else {
            btn_fab.setVisibility(View.GONE);
           // Log.e("debug_pagecount", "" + pageno);
            lv_progress_product_bottom.setVisibility(View.VISIBLE);
            lvnodata_productlist.setVisibility(View.GONE);
            coordinator_product_main.setVisibility(View.VISIBLE);

        }
        page = pageno;

        callcategoryproductlistapi(page).enqueue(new Callback<GetCategoryProductlist>() {
            @Override
            public void onResponse(Call<GetCategoryProductlist> call, Response<GetCategoryProductlist> response) {
                GetCategoryProductlist model = response.body();
                btn_fab.setVisibility(View.GONE);
                shimmer_productlist.stopShimmerAnimation();
                shimmer_productlist.setVisibility(View.GONE);
                lvnodata_productlist.setVisibility(View.GONE);
                coordinator_product_main.setVisibility(View.VISIBLE);
                lv_progress_product_bottom.setVisibility(View.GONE);

                if (model.getStatus().equalsIgnoreCase("Success")) {
                    isLastPage=model.getIsLast();

                    Log.e("isLastPage_168", "" + isLastPage);
                    if (fetchResults(response) == null || fetchResults(response).size() == 0) {
                     //   Log.e("debug_166", "" + fetchResults(response));
                        shimmer_productlist.stopShimmerAnimation();
                        shimmer_productlist.setVisibility(View.GONE);
                        lvnodata_productlist.setVisibility(View.VISIBLE);
                        coordinator_product_main.setVisibility(View.GONE);
                        lv_progress_product_bottom.setVisibility(View.GONE);
                        btn_fab.setVisibility(View.GONE);
                    } else {
                       // Log.e("debug_174", "" + fetchResults(response));
                        shimmer_productlist.stopShimmerAnimation();
                        shimmer_productlist.setVisibility(View.GONE);
                        lvnodata_productlist.setVisibility(View.GONE);
                        coordinator_product_main.setVisibility(View.VISIBLE);
                        lv_progress_product_bottom.setVisibility(View.GONE);

                        List<GetCategoryProductlistInnerData> results = fetchResults(response);
                        Log.e("arraylist_results_437",""+results);
                        if (results.isEmpty()) {
                            shimmer_productlist.stopShimmerAnimation();
                            shimmer_productlist.setVisibility(View.GONE);
                            lvnodata_productlist.setVisibility(View.VISIBLE);
                            coordinator_product_main.setVisibility(View.GONE);
                            lv_progress_product_bottom.setVisibility(View.GONE);
                            btn_fab.setVisibility(View.GONE);
                        } else {
                            shimmer_productlist.stopShimmerAnimation();
                            shimmer_productlist.setVisibility(View.GONE);
                            lvnodata_productlist.setVisibility(View.GONE);
                            coordinator_product_main.setVisibility(View.VISIBLE);
                            lv_progress_product_bottom.setVisibility(View.GONE);

                            for (int i = 0; i < results.size(); i++) {
                                productlist.add(new GetCategoryProductlistInnerData
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
                            btn_fab.setVisibility(View.VISIBLE);
                        }
                    }
                    if (viewtype == 0) {
                        productListAdapter = new ProductListAdapter(parent, productlist,screen);
                        layoutManager = new WrapContentGridLayoutManager(parent, 2);
                        recycler_productlist.setLayoutManager(layoutManager);
                        recycler_productlist.setAdapter(productListAdapter);
                    } else if (viewtype == 2) {
                        productListAdapter = new ProductListAdapter(parent, productlist,screen);
                        layoutManager = new WrapContentGridLayoutManager(parent, 1);
                        recycler_productlist.setLayoutManager(layoutManager);
                        recycler_productlist.setAdapter(productListAdapter);
                    } else {
                        Log.e("debug_420",""+ApiClient.VIEWTYPE);
                        productListAdapter = new ProductListAdapter(parent, productlist,screen);
                        layoutManager = new WrapContentGridLayoutManager(parent, 2);
                        recycler_productlist.setLayoutManager(layoutManager);
                        recycler_productlist.setAdapter(productListAdapter);
                    }


                } else {
                    shimmer_productlist.stopShimmerAnimation();
                    shimmer_productlist.setVisibility(View.GONE);
                    lvnodata_productlist.setVisibility(View.GONE);
                    coordinator_product_main.setVisibility(View.VISIBLE);
                    lv_progress_product_bottom.setVisibility(View.GONE);
                    Toast.makeText(parent, "" + model.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetCategoryProductlist> call, Throwable t) {
                Toast.makeText(parent, "" + parent.getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();
                t.printStackTrace();

                shimmer_productlist.stopShimmerAnimation();
                shimmer_productlist.setVisibility(View.GONE);
                lvnodata_productlist.setVisibility(View.GONE);
                coordinator_product_main.setVisibility(View.VISIBLE);
                lv_progress_product_bottom.setVisibility(View.GONE);
                btn_fab.setVisibility(View.GONE);
            }
        });

        nested_scroll_productlist.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (v.getChildAt(v.getChildCount() - 1) != null) {
                    if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                            scrollY > oldScrollY) {
                        visibleitemcount = layoutManager.getChildCount();
                        totalitemcount = layoutManager.getItemCount();
                        pastvisibleitem = layoutManager.findFirstVisibleItemPosition();

                       // Log.e("debug_258", "current_page=" + page);
                        if ((visibleitemcount + pastvisibleitem) >= totalitemcount) {
                            Log.e("debug33islast", "=" + isLastPage);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if(isLastPage==0)
                                    {
                                        page++;
                                        Log.e("debug_261", "loadscroll" + page);
                                        //  Log.e("debug_270islast", "loadscroll" + isLastPage);
                                        lv_progress_product_bottom.setVisibility(View.VISIBLE);
                                        if (CheckNetwork.isNetworkAvailable(parent)) {
                                            CallGetCategoryProductlistApi(page, ApiClient.VIEWTYPE);
                                        } else {
                                            Toast.makeText(parent, getResources().getString(R.string.nointernet), Toast.LENGTH_SHORT).show();
                                        }

                                    }else {
                                        Log.e("debug_2704", "" + isLastPage);
                                        lv_progress_product_bottom.setVisibility(View.GONE);
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
        Log.e("debug44_4999", "==" + sort_selected);

        Log.e("filter_child_id_479", "==" + Filterlist_Adapter.filter_child_value_list);
        if (b==null || selected_child_url == null  ) {
            Log.e("filter_child_id_4444", "==" + Filterlist_Adapter.filter_child_value_list);
            Log.e("Daily44", "==" + subcatename);
            if (subcatename.equals("Daily specials products")) {
                Log.e("Daily", "==" + subcatename);
                String pro = "daily_special";
                if (Login_preference.getLogin_flag(parent).equalsIgnoreCase("1")) {
                    return apiInterface.getCustomProducts_withlogin(pro,
                            Login_preference.getcustomer_id(getContext()), page);
                } else {

                    return apiInterface.getCustomProducts_withoutlogin(pro, page);
                }
            } else if (subcatename.equals("Popular products")) {
                Log.e("Daily499", "==" + subcatename);
                String pro = "popular_products";
                if (Login_preference.getLogin_flag(parent).equalsIgnoreCase("1")) {
                    return apiInterface.getCustomProducts_withlogin(pro,
                            Login_preference.getcustomer_id(getContext()), page);
                } else {
                    return apiInterface.getCustomProducts_withoutlogin(pro, page);
                }
            }else if(screen_type==null){
                Log.e("debug_555", "==" + subcatename);
                Log.e("debug_555", "==" + subcat_id);

                if (Login_preference.getLogin_flag(parent).equalsIgnoreCase("1")) {
                    Log.e("peramiter_542",""+asc_desc);
                    Log.e("subcat_id542",""+subcat_id);
                    Log.e("sort_selected_542",""+sort_selected);
                    Log.e("customer_542",""+Login_preference.getcustomer_id(getContext()));
                    return apiInterface.categoryproducts_withlogin(subcat_id,
                            Login_preference.getcustomer_id(getContext()), page,asc_desc, sort_selected);
                } else {
                    Log.e("peramiter_546",""+asc_desc);
                    Log.e("debulogin541", "==" + sort_selected);
                    return apiInterface.categoryproducts_withoutlogin(subcat_id, page,asc_desc, sort_selected);
                }
            }

            else if(screen_type.equals("subcategory")){
                Log.e("debug_555", "==" + subcatename);
                Log.e("debug_555", "==" + subcat_id);
                if (Login_preference.getLogin_flag(parent).equalsIgnoreCase("1")) {
                    Log.e("peramiter_542",""+asc_desc);
                    Log.e("subcat_id542",""+subcat_id);
                    Log.e("sort_selected_542",""+sort_selected);
                    Log.e("customer_542",""+Login_preference.getcustomer_id(getContext()));
                    return apiInterface.categoryproducts_withlogin(subcat_id,
                            Login_preference.getcustomer_id(getContext()), page,asc_desc, sort_selected);
                } else {
                    Log.e("peramiter_546",""+asc_desc);
                    Log.e("debulogin541", "==" + sort_selected);
                    return apiInterface.categoryproducts_withoutlogin(subcat_id, page,asc_desc, sort_selected);
                }
            }
            else if (screen_type.equals("Manufacturer")) {
                if (Login_preference.getLogin_flag(parent).equalsIgnoreCase("1")) {
                    Log.e("peramiter_542",""+asc_desc);
                    Log.e("subcat_id542",""+subcat_id);
                    Log.e("sort_selected_542",""+sort_selected);
                    Log.e("customer_542",""+Login_preference.getcustomer_id(getContext()));
                    return apiInterface.manufacturerproducts_withlogin(subcat_id,
                            Login_preference.getcustomer_id(getContext()), page,asc_desc, sort_selected);
                } else {
                    Log.e("peramiter_546",""+asc_desc);
                    Log.e("debulogin541", "==" + sort_selected);
                    return apiInterface.manufaturerproducts_withoutlogin(subcat_id, page,asc_desc, sort_selected);
                }
            } else {
                Log.e("debug_555", "==" + subcatename);
                Log.e("debug_555", "==" + subcat_id);

                if (Login_preference.getLogin_flag(parent).equalsIgnoreCase("1")) {
                    Log.e("peramiter_542",""+asc_desc);
                    Log.e("subcat_id542",""+subcat_id);
                    Log.e("sort_selected_542",""+sort_selected);
                    Log.e("customer_542",""+Login_preference.getcustomer_id(getContext()));
                    return apiInterface.categoryproducts_withlogin(subcat_id,
                            Login_preference.getcustomer_id(getContext()), page,asc_desc, sort_selected);
                } else {
                    Log.e("peramiter_546",""+asc_desc);
                    Log.e("debulogin541", "==" + sort_selected);
                    return apiInterface.categoryproducts_withoutlogin(subcat_id, page,asc_desc, sort_selected);
                }
            }
        } else if (selected_child_url != null  ) {


            String MainPassURL;
            if (Login_preference.getLogin_flag(parent).equalsIgnoreCase("1")) {
                MainPassURL = ApiClient.MAIN_URLL + "getcategoryproductlist.php?" + "category_id=" + subcat_id + "&" + "customer_id=" +
                        Login_preference.getcustomer_id(getActivity()) + "&" + "page=" + page + "&" + "sort=" + sort_selected+ "&" + "dir=" + asc_desc+selected_child_url;
            } else {
                MainPassURL = ApiClient.MAIN_URLL + "getcategoryproductlist.php?" + "category_id=" + subcat_id + "&" + "page=" + page + "&" + "sort=" + sort_selected+ "&" + "dir=" + asc_desc+selected_child_url;
            }

            Log.e("debug_6660", "==" + selected_child_url);
            Log.e("debug_666022", "==" + MainPassURL);
            Log.e("debug_66602266", "==" + Filterlist_Adapter.filter_child_value_list);
            return apiInterface.call_filter_categoryproducts(MainPassURL);

        } else if (subcatename.equals("Daily specials products")) {
            Log.e("Daily", "==" + subcatename);
            String pro = "daily_special";
            if (Login_preference.getLogin_flag(parent).equalsIgnoreCase("1")) {
                return apiInterface.getCustomProducts_withlogin(pro,
                        Login_preference.getcustomer_id(getContext()), page);
            } else {
                return apiInterface.getCustomProducts_withoutlogin(pro, page);
            }
        } else if (subcatename.equals("Popular products")) {
            String pro = "popular_products";
            if (Login_preference.getLogin_flag(parent).equalsIgnoreCase("1")) {
                return apiInterface.getCustomProducts_withlogin(pro,
                        Login_preference.getcustomer_id(getContext()), page);
            } else {
                return apiInterface.getCustomProducts_withoutlogin(pro, page);
            }
        } else {

            if (Login_preference.getLogin_flag(parent).equalsIgnoreCase("1")) {
                return apiInterface.categoryproducts_withlogin(subcat_id,
                        Login_preference.getcustomer_id(getContext()), page,asc_desc, sort_selected);
            } else {
                Log.e("debulogin541", "==" + sort_selected);
                return apiInterface.categoryproducts_withoutlogin(subcat_id, page,asc_desc, sort_selected);
            }
        }
    }

    public  String Selected_Filter_Function()
   {
       filtercount.clear();
       Log.e("subcat_id366", "==" + subcat_id);
       Log.e("customerid366", "==" + Login_preference.getcustomer_id(getContext()));
       Log.e("sort_value", "==" + sort_value);
       String url = "",MainPassURL="",child_id_pass,group_name_pass;
       String customer_id = Login_preference.getcustomer_id(parent);
       Log.e("customer_id", "==" + customer_id);
       Log.e("peramiter_594", "==" + asc_desc);

       if (Login_preference.getLogin_flag(parent).equalsIgnoreCase("1")) {
           MainPassURL = ApiClient.MAIN_URLL + "getcategoryproductlist.php?" + "category_id=" + subcat_id + "&" + "customer_id=" + customer_id + "&" + "page=" + page + "&" + "sort=" + sort_selected+ "&" + "dir=" + asc_desc;
       } else {
           MainPassURL = ApiClient.MAIN_URLL + "getcategoryproductlist.php?" + "category_id=" + subcat_id + "&" + "page=" + page + "&" + "sort=" + sort_selected+ "&" + "dir=" + asc_desc;
       }

        //code for diiferrent filter id
       for(int i=0;i<Filterlist_Adapter.filter_child_value_list.size();i++)
       {
           for(int j=0;j<Filterlist_Adapter.filter_child_value_list.get(i).size();j++)
           {
               if(Filterlist_Adapter.filter_child_value_list.get(i).get(j).equals("0"))
               { }
               else {
                   child_id_pass=Filterlist_Adapter.filter_child_value_list.get(i).get(j);
                   group_name_pass=FilterListFragment.filter_group_namelist.get(i);
                   if(filtercount.contains(group_name_pass))
                   { }else {
                       filtercount.add(group_name_pass);
                   }
                   if(url.contains(group_name_pass))
                   {
                       url+="_"+child_id_pass;
                   }else {
                       url+="&"+group_name_pass+"="+child_id_pass;
                   }
               }
           }
       }
       Log.e("debug_main_url_5",""+url);
       Log.e("arrayList",""+filtercount);
       Log.e("arrayList_520",""+filtercount.size());

       if(filtercount.isEmpty() || filtercount ==null )
       {
           tv_totalfilter.setText("Total Filter [0]");
       }else {
           Log.e("filtercount", "" + filtercount);
           tv_totalfilter.setText("Total Filter ["+filtercount.size()+"]");
       }

       Log.e("debug_398", "==" + url);
       Log.e("mainurl", "==" + MainPassURL);
       MainPassURL = MainPassURL + url;
       Log.e("mainurl3900", "==" + MainPassURL);

       return MainPassURL;


       //code of selected item befor code

            /*String group_name, child_id;
            for (int i = 0; i < filter_group_name.size(); i++) {
                group_name = filter_group_name.get(i);
                child_id = filter_child_id.get(i);
                if (url.contains(group_name)) {
                    url += "_" + child_id;
                } else {
                    url += "&" + group_name + "=" + child_id;
                }
                //url+="&"+filter_group_name.get(i)+"="+filter_child_id.get(i);
            }*/

       //selected filter array

   }

    private void AllocateMemory() {
        lv_asc_desc_arrow = v.findViewById(R.id.lv_asc_desc_arrow);
        tv_totalfilter = v.findViewById(R.id.tv_totalfilter);
        tv_product_toolbar = v.findViewById(R.id.tv_product_toolbar);
        lv_filter = v.findViewById(R.id.lv_filter);
        coordinator_product_main = v.findViewById(R.id.coordinator_productlist_main);
        toolbar_productlist = v.findViewById(R.id.toolbar_productlist);
        lvnodata_productlist = v.findViewById(R.id.lvnodata_productlist);
        lv_maincategoty_products_main = v.findViewById(R.id.lv_maincategoty_products_main);
        shimmer_productlist = v.findViewById(R.id.shimmer_productlist);
        nested_scroll_productlist = v.findViewById(R.id.nested_scroll_productlist);
        recycler_productlist = v.findViewById(R.id.recycler_productlist);
        btn_fab = v.findViewById(R.id.btn_fab);
        lv_progress_product_bottom = v.findViewById(R.id.lv_progress_product_bottom);
        lv_productlist_progress = v.findViewById(R.id.lv_productlist_progress);
        spinner_sortby = v.findViewById(R.id.spinner_sortby);
        lv_main_search_filter_sort = v.findViewById(R.id.lv_main_search_filter_sort);
        lv_produt_parent = v.findViewById(R.id.lv_produt_parent);
        lv_filter_main = v.findViewById(R.id.lv_filter_main);
        iv_product_up_down = v.findViewById(R.id.iv_product_up_down);
    }

    @Override
    public void onResume() {

        super.onResume();

        shimmer_productlist.startShimmerAnimation();
    }

    @Override
    public void onPause() {

        shimmer_productlist.stopShimmerAnimation();
        super.onPause();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_home, menu);
        login = menu.findItem(R.id.login);
        if(Login_preference.getLogin_flag(parent).equalsIgnoreCase("1"))
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
                FilterListFragment.selected_child="";
                FilterListFragment.filter_old_childlist.clear();
                Filterlist_Adapter.filter_grouppp_namelist.clear();
                Log.e("serching_chid_855",""+FilterListFragment.selected_child);
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
    public void onClick(View v) {
        if (v == lv_filter) {
            int sp_pos = spinner_sortby.getSelectedItemPosition();
            Bundle b=new Bundle();
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            b.putString("subcat_id",subcat_id);
            b.putString("subcatename",subcatename);
            b.putInt("sort_select", sp_pos);
            String screen="productlist";
            b.putString("screen",screen);
            FilterListFragment myFragment = new FilterListFragment();
            myFragment.setArguments(b);
            activity.getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.fade_in,
                            0, 0, R.anim.fade_out)
                    .setCustomAnimations(R.anim.fade_in,
                            0, 0, R.anim.fade_out)
                    .addToBackStack(null).add(R.id.framlayout, myFragment)
                    .commit();

        } else if (v == btn_fab) {

            if (ApiClient.VIEWTYPE == 0) {
                btn_fab.setImageDrawable(parent.getResources().getDrawable(R.drawable.grid_ic));

              //  Log.e("data_show_verticle_btn", "grid_to_verticle");
                productListAdapter = new ProductListAdapter(parent, productlist,screen);
                layoutManager = new WrapContentGridLayoutManager(parent, 1);
                recycler_productlist.setLayoutManager(layoutManager);
                recycler_productlist.setAdapter(productListAdapter);
                ApiClient.VIEWTYPE = 2;
                // flag=true;
            } else if (ApiClient.VIEWTYPE == 2) {
                btn_fab.setImageDrawable(parent.getResources().getDrawable(R.drawable.ic_view_list_white_36dp));
                productListAdapter = new ProductListAdapter(parent, productlist,screen);
                layoutManager = new WrapContentGridLayoutManager(parent, 2);
                recycler_productlist.setLayoutManager(layoutManager);
                recycler_productlist.setAdapter(productListAdapter);
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

            /*if(lv_asc_desc_arrow.isClickable()==true && debug==true)
            {
                Log.e("debug_775","first");

                if (CheckNetwork.isNetworkAvailable(parent)) {
                    Log.e("debug_208apiclient",""+ApiClient.VIEWTYPE);
                    CallGetCategoryProductlistApi(page_no, ApiClient.VIEWTYPE);
                } else {
                    Toast.makeText(parent, getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
                }
                debug=false;

            }else if(lv_asc_desc_arrow.isClickable()==true && debug==false)
            {
                Log.e("debug_779","secong");
                debug=true;
            }*/

            /*if(dir.equals("asc"))
            {
                if (CheckNetwork.isNetworkAvailable(parent)) {
                    Log.e("debug_208apiclient",""+ApiClient.VIEWTYPE);
                    CallGetCategoryProductlistApi(page_no, ApiClient.VIEWTYPE);
                } else {
                    Toast.makeText(parent, getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
                }
            }else if(dir.equals("desc"))
            {

            }*/


        }
    }
}
