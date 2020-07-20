package com.dolphin.zanders.Fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.util.Log;
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
import android.widget.Toast;

import com.dolphin.zanders.Activity.NavigationActivity;
import com.dolphin.zanders.Adapter.Filterlist_Adapter;
import com.dolphin.zanders.Adapter.Manufacturerslist_Adapter;
import com.dolphin.zanders.Model.Manufacturerslist_model.GetManufacturelistModel;
import com.dolphin.zanders.Model.Manufacturerslist_model.Manufactur;
import com.dolphin.zanders.R;
import com.dolphin.zanders.Retrofit.ApiClient;
import com.dolphin.zanders.Retrofit.ApiInterface;
import com.dolphin.zanders.Util.CheckNetwork;
import com.dolphin.zanders.Util.WrapContentGridLayoutManager;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.novoda.merlin.Connectable;
import com.novoda.merlin.Merlin;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import fastscroll.app.fastscrollalphabetindex.AlphabetIndexFastScrollRecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dolphin.zanders.Activity.NavigationActivity.bottom_navigation;
import static com.dolphin.zanders.Activity.NavigationActivity.drawer;

/**
 * A simple {@link Fragment} subclass.
 */
public class Manufacturerslist extends Fragment {
    NavigationActivity parent;

    Toolbar toolbar_manufacturer;
    RecyclerView rv_manufacturerlist;
    NestedScrollView nested_scroll_manufacturer;
    ApiInterface api;
    Manufacturerslist_Adapter manufacturerslist_adapter;
    public static List<Manufactur> manufacturList = new ArrayList<Manufactur>();
    ShimmerFrameLayout shimmer_manufacturerlist;
    AlphabetIndexFastScrollRecyclerView recyclerView;

    LinearLayout lv_nodata_manufacturer, lv_parent_manufacturer,lv_progress_manufacturer_bottom, lv_main_manufacturer;
    //pagination
    LinearLayoutManager linearLayoutManager;
    int pastvisibleitem, visibleitemcount, totalitemcount;
    int page_no = 1, page;
    int isLastPage;
    char singleChar;
    String lastChar = "";
    Merlin merlin;

    public Manufacturerslist() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_manufacturerslist, container, false);
        AllocateMemory(v);
        AttachRecyclerview();
        parent=(NavigationActivity) getActivity();
        bottom_navigation.getMenu().getItem(1).setChecked(true);
        setupUI(lv_parent_manufacturer);
        api = ApiClient.getClient().create(ApiInterface.class);
        setHasOptionsMenu(true);
        hideKeyboard(parent);
        Filterlist_Adapter.filter_child_value_list.clear();
        Filterlist_Adapter.filter_grouppp_namelist.clear();
        FilterListFragment.selected_child="";

        ((NavigationActivity) parent).setSupportActionBar(toolbar_manufacturer);
        ((NavigationActivity) parent).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
        ((NavigationActivity) parent).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        toolbar_manufacturer.setTitle(parent.getResources().getString(R.string.Manufacturers));
        toolbar_manufacturer.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        /*merlin = new Merlin.Builder().withConnectableCallbacks().build(getActivity());

        merlin.registerConnectable(new Connectable() {
            @Override
            public void onConnect() {
                // Do something you haz internet!
                callgetmenufaturerslistapi(page_no);
            }
        });*/

        if (CheckNetwork.isNetworkAvailable(parent)) {
            callgetmenufaturerslistapi(page_no);
        } else {
            Toast.makeText(parent, getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
        }
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                getActivity().onBackPressed();

                return false;
            }
        });
        return v;
    }

    private void AttachRecyclerview() {
        manufacturerslist_adapter = new Manufacturerslist_Adapter(parent, manufacturList);
        linearLayoutManager = new LinearLayoutManager(parent, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(manufacturerslist_adapter);

        //set scrollindexbar size
        recyclerView.setIndexTextSize(12);
        recyclerView.setIndexBarTextColor(R.color.black);
        recyclerView.setIndexBarCornerRadius(4);
        recyclerView.setIndexBarTransparentValue((float) 0.4);
        recyclerView.setIndexbarMargin(20);
        recyclerView.setIndexbarWidth(60);
        recyclerView.setIndexBarColor(R.color.grey);
        recyclerView.setIndexbarHighLateTextColor("#ff0000");
        recyclerView.setIndexBarHighLateTextVisibility(true);

    }


    private void callgetmenufaturerslistapi(int pageno) {
        page = pageno;
        lv_progress_manufacturer_bottom.setVisibility(View.GONE);
        manufacturList.clear();
        lv_nodata_manufacturer.setVisibility(View.GONE);
        lv_main_manufacturer.setVisibility(View.VISIBLE);

        callgetmenufatuApi(page).enqueue(new Callback<GetManufacturelistModel>() {
            @Override
            public void onResponse(Call<GetManufacturelistModel> call, Response<GetManufacturelistModel> response) {
                GetManufacturelistModel model = response.body();
                lv_nodata_manufacturer.setVisibility(View.GONE);
                lv_main_manufacturer.setVisibility(View.VISIBLE);
                lv_progress_manufacturer_bottom.setVisibility(View.GONE);

                if (model.getStatus().equalsIgnoreCase("Success")) {
                    isLastPage = model.getIsLast();

                    Log.e("isLastPage_168", "" + isLastPage);
                    if (fetchResults(response) == null || fetchResults(response).size() == 0) {
                        Log.e("debug_166", "" + fetchResults(response));
                        shimmer_manufacturerlist.stopShimmerAnimation();
                        shimmer_manufacturerlist.setVisibility(View.GONE);
                        lv_nodata_manufacturer.setVisibility(View.VISIBLE);
                        lv_main_manufacturer.setVisibility(View.GONE);
                        lv_progress_manufacturer_bottom.setVisibility(View.GONE);

                    } else {
                        Log.e("debug_174", "" + fetchResults(response));
                        shimmer_manufacturerlist.stopShimmerAnimation();
                        shimmer_manufacturerlist.setVisibility(View.GONE);
                        lv_nodata_manufacturer.setVisibility(View.GONE);
                        lv_main_manufacturer.setVisibility(View.VISIBLE);
                        lv_progress_manufacturer_bottom.setVisibility(View.GONE);

                        List<Manufactur> results = fetchResults(response);
                        if (results.isEmpty()) {
                            shimmer_manufacturerlist.stopShimmerAnimation();
                            shimmer_manufacturerlist.setVisibility(View.GONE);
                            lv_nodata_manufacturer.setVisibility(View.VISIBLE);
                            lv_main_manufacturer.setVisibility(View.GONE);
                            lv_progress_manufacturer_bottom.setVisibility(View.GONE);

                        } else {
                            shimmer_manufacturerlist.stopShimmerAnimation();
                            shimmer_manufacturerlist.setVisibility(View.GONE);
                            lv_nodata_manufacturer.setVisibility(View.GONE);
                            lv_main_manufacturer.setVisibility(View.VISIBLE);
                            lv_progress_manufacturer_bottom.setVisibility(View.GONE);


                            for (int j = 0; j < results.size(); j++) {
                                singleChar = results.get(j).getName().charAt(0); // 1,
                                Log.e("debug_183", "" + results.get(j).getId());
                                Log.e("debug_184", "" + results.get(j).getName());
                                if (lastChar == String.valueOf(singleChar) || lastChar.equalsIgnoreCase(String.valueOf(singleChar)))//acurate firs=a
                                {
                                    Log.e("debug_i19id2", "=" + results.get(j).getId());
                                    Log.e("debug_i19id2name", "=" + results.get(j).getName());
                                    manufacturList.add(new Manufactur
                                            (results.get(j).getId(),
                                                    results.get(j).getName(),
                                                    results.get(j).getAddress(),
                                                    results.get(j).getSerializedDisplayOn(),
                                                    results.get(j).getSerializedText(),
                                                    results.get(j).getPhone(),
                                                    results.get(j).getWeb(),
                                                    results.get(j).getImageType(),
                                                    results.get(j).getEnable(),
                                                    results.get(j).getImage()));
                                } else {
                                    Log.e("debug_elseid", "=" + results.get(j).getId());
                                    Log.e("debug_elsename", "=" + results.get(j).getName());


                                    lastChar = String.valueOf(singleChar); // 1,
                                    manufacturList.add(new Manufactur
                                            ("0", String.valueOf(singleChar),
                                                    "0", "0", "0",
                                                    "0", "0", "0", "0",
                                                    "0"));

                                    manufacturList.add(new Manufactur
                                            (results.get(j).getId(),
                                                    results.get(j).getName(),
                                                    results.get(j).getAddress(),
                                                    results.get(j).getSerializedDisplayOn(),
                                                    results.get(j).getSerializedText(),
                                                    results.get(j).getPhone(),
                                                    results.get(j).getWeb(),
                                                    results.get(j).getImageType(),
                                                    results.get(j).getEnable(),
                                                    results.get(j).getImage()));


                                }
                             //   manufacturerslist_adapter.notifyItemChanged(j);

                            }
                            AttachRecyclerview();
                            Log.e("debug_elsename2233", "=" + manufacturList.get(15).getId());
                            Log.e("debug_nameme2233", "=" + manufacturList.get(15).getName());
                            Log.e("debug_", "=" + manufacturList);
                        }
                    }
                } else {
                    shimmer_manufacturerlist.stopShimmerAnimation();
                    shimmer_manufacturerlist.setVisibility(View.GONE);
                    lv_nodata_manufacturer.setVisibility(View.GONE);
                    lv_main_manufacturer.setVisibility(View.VISIBLE);
                    lv_progress_manufacturer_bottom.setVisibility(View.GONE);
                    Toast.makeText(parent, "" + model.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetManufacturelistModel> call, Throwable t) {
                // progressBar_review.setVisibility(View.GONE);
                shimmer_manufacturerlist.stopShimmerAnimation();
                shimmer_manufacturerlist.setVisibility(View.GONE);
                lv_nodata_manufacturer.setVisibility(View.GONE);
                lv_main_manufacturer.setVisibility(View.VISIBLE);
                lv_progress_manufacturer_bottom.setVisibility(View.GONE);
                Toast.makeText(parent, "" + parent.getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private List<Manufactur> fetchResults(Response<GetManufacturelistModel> response) {
        GetManufacturelistModel getManufacturelistModel = response.body();
        return getManufacturelistModel.getManufacturs();
    }

    private Call<GetManufacturelistModel> callgetmenufatuApi(int page) {
        return api.getmanufacturerlistapi();
    }

    private void AllocateMemory(View v) {
        lv_parent_manufacturer = v.findViewById(R.id.lv_parent_manufacturer);
        lv_main_manufacturer = v.findViewById(R.id.lv_main_manufacturer);
        lv_nodata_manufacturer = v.findViewById(R.id.lv_nodata_manufacturer);
        toolbar_manufacturer = v.findViewById(R.id.toolbar_manufacturer);

        shimmer_manufacturerlist = v.findViewById(R.id.shimmer_manufacturerlist);
        lv_progress_manufacturer_bottom = v.findViewById(R.id.lv_progress_manufacturer_bottom);
        recyclerView = v.findViewById(R.id.recyclerView);
        //  rv_manufacturerlist=v.findViewById(R.id.rv_manufacturerlist);
        //  nested_scroll_manufacturer=v.findViewById(R.id.nested_scroll_manufacturer);


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

    @Override
    public void onResume() {
        super.onResume();
        shimmer_manufacturerlist.startShimmerAnimation();
       // merlin.bind();
    }

    @Override
    public void onPause() {
        shimmer_manufacturerlist.stopShimmerAnimation();
        //merlin.unbind();
        super.onPause();
    }
}
