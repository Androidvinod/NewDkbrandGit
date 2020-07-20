package com.dolphin.zanders.Fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dolphin.zanders.Activity.NavigationActivity;
import com.dolphin.zanders.Adapter.Filterlist_Adapter;
import com.dolphin.zanders.Model.Filter_model.FilterInnerModel;
import com.dolphin.zanders.Model.Filter_model.FilterModel;

import com.dolphin.zanders.Preference.Login_preference;
import com.dolphin.zanders.R;
import com.dolphin.zanders.Retrofit.ApiClient;
import com.dolphin.zanders.Util.CheckNetwork;
import com.dolphin.zanders.Util.TotalListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FilterListFragment extends Fragment implements TotalListener, View.OnClickListener {
    public static ArrayList<ArrayList<String>> new_filter_childlist = new ArrayList<>();
    public static ArrayList<ArrayList<String>> mGroupList = new ArrayList<>();
    Toolbar toolbar_filter;
    ExpandableListView expandableListView;
    Filterlist_Adapter expandableListAdapter;
    public static  ProgressBar pd;
    Bundle b;
   public static String sub_category_id,subcatename,searhkey;
    public static int sort = 0;
    public static LinearLayout lv_apply,lv_Reset,lvnodata_filter,lv_filter_main;
    NavigationActivity parent;

    String URL;
    public static String screen,appendUrl = "",selected_child="",selected_group="",selectedchild="";
    public static  CoordinatorLayout cordinator_filter;

    public static  TextView tv_noting,tv_messgenoti;
    public static ArrayList<String> filter_group_namelist = new ArrayList<>();


    public static ArrayList<String> filter_old_childlist = new ArrayList<>();

    public FilterListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_filter_list, container, false);
        AllocateMemory(v);
        setHasOptionsMenu(true);
        setupUI(lv_filter_main);
        parent=(NavigationActivity)getActivity();
        hideKeyboard(parent);
        ((NavigationActivity)parent).setSupportActionBar(toolbar_filter);
        ((NavigationActivity)parent).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
        ((NavigationActivity)parent).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_36dp);


        Filterlist_Adapter.mGroupList.clear();
        Filterlist_Adapter.pass_ArrayList_group.clear();
        Filterlist_Adapter.pass_ArrayList_child.clear();
        Filterlist_Adapter.selectedChildCheckBoxStates.clear();

        b = this.getArguments();
        if (b != null) {
            sub_category_id = b.getString("subcat_id");
            subcatename = b.getString("subcatename");
            screen = b.getString("screen");
            searhkey = b.getString("searhkey");
            sort = b.getInt("sort_select");

            Log.e("filter_sort", ""+sort);
            Log.e("sub_category_id85", ""+sub_category_id);
            Log.e("subcatename86", ""+subcatename);

            Log.e("debug_9999", ""+b.getStringArrayList("filter_child_id"));
            Log.e("debug_9999", ""+screen);

        }else {
            Log.e("debug_99997", ""+screen);
        }
        if (CheckNetwork.isNetworkAvailable(getActivity())) {
            CallFilterApi();
        } else {
            Toast.makeText(parent, parent.getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();
        }

        //apply button
        lv_apply.setOnClickListener(this);
        lv_Reset.setOnClickListener(this);

        return v;
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

    public  String Selected_Filter_Function()
    {

        Log.e("subcat_id366", "==" + sub_category_id);
        Log.e("customerid366", "==" + Login_preference.getcustomer_id(getContext()));

        String MainPassURL="",child_id_pass,group_name_pass;
        String customer_id = Login_preference.getcustomer_id(parent);
        Log.e("customer_id", "==" + customer_id);


        if (Login_preference.getLogin_flag(parent).equalsIgnoreCase("1")) {
         //   MainPassURL = ApiClient.MAIN_URLL + "getcategoryproductlist.php?" + "category_id=" + subcat_id + "&" + "customer_id=" + customer_id + "&" + "page=" + page + "&" + "sort=" + sort_selected+ "&" + "dir=" + asc_desc;

            MainPassURL=ApiClient.MAIN_URLL+"filterlist.php?category_id=" + sub_category_id+"&customer_id="+customer_id+"&";
        } else {
           // MainPassURL = ApiClient.MAIN_URLL + "getcategoryproductlist.php?" + "category_id=" + subcat_id + "&" + "page=" + page + "&" + "sort=" + sort_selected+ "&" + "dir=" + asc_desc;
            MainPassURL=ApiClient.MAIN_URLL+"filterlist.php?category_id=" + sub_category_id+"&";

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
                    Log.e("debug_193d", "==" + Filterlist_Adapter.filter_grouppp_namelist.get(i));
                    group_name_pass=Filterlist_Adapter.filter_grouppp_namelist.get(i);

                    if(appendUrl.contains(group_name_pass))
                    {
                        appendUrl +="_"+child_id_pass;
                    }else {
                        appendUrl +="&"+group_name_pass+"="+child_id_pass;
                    }
                }
            }
        }
        Log.e("debug_main_url_5",""+appendUrl);


        Log.e("debug_398", "==" + appendUrl);
        Log.e("mainurl", "==" + MainPassURL);
        MainPassURL = MainPassURL + appendUrl;
        Log.e("mainurl3900", "==" + MainPassURL);
        Log.e("appendUrl", "==" + appendUrl);

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


    public  void CallFilterApi() {
        filter_group_namelist.clear();
        lvnodata_filter.setVisibility(View.GONE);
        cordinator_filter.setVisibility(View.VISIBLE);
        tv_noting.setText("No Filter Found");
        tv_messgenoti.setVisibility(View.GONE);
        pd.setVisibility(View.VISIBLE);
        if(screen==null || screen.equalsIgnoreCase("null"))
        {
            URL= ApiClient.MAIN_URLL + "offerfilterlist.php";
        }

        if(screen.equalsIgnoreCase("productlist"))
        {
            if(FilterListFragment.selected_child.equalsIgnoreCase(""))
            {
                URL=ApiClient.MAIN_URLL + "filterlist.php?category_id=" + sub_category_id+"&customer_id="+Login_preference.getcustomer_id(getActivity());
                Log.e("deub179","gfh"+URL);
            }else {

                if (Login_preference.getLogin_flag(parent).equalsIgnoreCase("1")) {
                    //   MainPassURL = ApiClient.MAIN_URLL + "getcategoryproductlist.php?" + "category_id=" + subcat_id + "&" + "customer_id=" + customer_id + "&" + "page=" + page + "&" + "sort=" + sort_selected+ "&" + "dir=" + asc_desc;

                    URL=ApiClient.MAIN_URLL+"filterlist.php?category_id=" + sub_category_id+"&customer_id="+Login_preference.getcustomer_id(getActivity())+FilterListFragment.selected_child;
                } else {
                    // MainPassURL = ApiClient.MAIN_URLL + "getcategoryproductlist.php?" + "category_id=" + subcat_id + "&" + "page=" + page + "&" + "sort=" + sort_selected+ "&" + "dir=" + asc_desc;
                    URL=ApiClient.MAIN_URLL+"filterlist.php?category_id=" + sub_category_id+FilterListFragment.selected_child;

                }
            // URL= Selected_Filter_Function();
                Log.e("deub17888","h="+URL);
                Log.e("deub262","h="+  appendUrl);
               // Filterlist_Adapter.filter_child_value_list.clear();
            }

        }else if(screen.equalsIgnoreCase("offer")){

            if(FilterListFragment.selected_child.equalsIgnoreCase(""))
            {
                URL=ApiClient.MAIN_URLL+"offerfilterlist.php?"+"customer_id="+Login_preference.getcustomer_id(getActivity());;
            }else {
                if (Login_preference.getLogin_flag(parent).equalsIgnoreCase("1")) {
                    //   MainPassURL = ApiClient.MAIN_URLL + "getcategoryproductlist.php?" + "category_id=" + subcat_id + "&" + "customer_id=" + customer_id + "&" + "page=" + page + "&" + "sort=" + sort_selected+ "&" + "dir=" + asc_desc;

                    URL=ApiClient.MAIN_URLL+"offerfilterlist.php?customer_id=" +Login_preference.getcustomer_id(getActivity())+FilterListFragment.selected_child;
                } else {
                    // MainPassURL = ApiClient.MAIN_URLL + "getcategoryproductlist.php?" + "category_id=" + subcat_id + "&" + "page=" + page + "&" + "sort=" + sort_selected+ "&" + "dir=" + asc_desc;
                    URL=ApiClient.MAIN_URLL+"offerfilterlist.php?"+FilterListFragment.selected_child;

                }
                Log.e("debug_offer66","h="+URL);
            }
        }else if(screen.equalsIgnoreCase("Search"))
        {

            if(FilterListFragment.selected_child.equalsIgnoreCase(""))
            {
                Log.e("selected_child_301",""+FilterListFragment.selected_child);
                URL=ApiClient.MAIN_URLL+"filterlistSearch.php?q="+searhkey+"&customer_id="+Login_preference.getcustomer_id(getActivity());;
            }else {
                Log.e("selected_child_304",""+FilterListFragment.selected_child);
                if (Login_preference.getLogin_flag(parent).equalsIgnoreCase("1")) {

                    URL=ApiClient.MAIN_URLL+"filterlistSearch.php?q=" + searhkey+"&customer_id="+Login_preference.getcustomer_id(getActivity())+FilterListFragment.selected_child;
                } else {
                    URL=ApiClient.MAIN_URLL+"filterlistSearch.php?q=" + searhkey+FilterListFragment.selected_child;
                    Log.e("debug_searchr6667","h="+URL);
                }
            }
            Log.e("debug_searchr66677","h="+URL);

        }else {
            Log.e("deub_127",""+URL);
        }

        Log.e("deub_127",""+URL);
        pd.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response", "" + response);
                pd.setVisibility(View.GONE);
                lvnodata_filter.setVisibility(View.GONE);
                cordinator_filter.setVisibility(View.VISIBLE);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("status").equalsIgnoreCase("success"))
                    {
                        String filter = jsonObject.getString("filter");
                       Filterlist_Adapter.filter_child_value_list.clear();
                        JSONArray filter_array = new JSONArray(filter);
                        if (filter_array != null && filter_array.isNull(0) != true) {
                            final ArrayList<FilterModel> list = new ArrayList<FilterModel>();

                            for (int i = 0; i < filter_array.length(); i++)
                                try {
                                    JSONObject object = filter_array.getJSONObject(i);
                                    String name = object.getString("name");
                                    Log.e("main_name", "" + name);
                                    FilterModel gru = new FilterModel();

                                    gru.setName(object.getString("name"));
                                    gru.setLabel(object.getString("label"));
                                    filter_group_namelist.add(object.getString("name"));
                                    final ArrayList<FilterInnerModel> ch_list = new ArrayList<FilterInnerModel>();

                                    String options = object.getString("options");
                                    Log.e("options", "" + options);

                                    JSONArray arrsub = new JSONArray(options);
                                    for (int j = 0; j < arrsub.length(); j++) {
                                        try {
                                            JSONObject subobject = arrsub.getJSONObject(j);
                                            String subname = subobject.getString("attribute_name");
                                            Log.e("sub_name", "" + subname);
                                            FilterInnerModel ch = new FilterInnerModel();

                                            ch.setAttribute_name(subobject.getString("attribute_name"));
                                            ch.setAttribute_value(subobject.getString("attribute_value"));
                                            ch_list.add(ch);

                                        } catch (Exception e) {
                                            Log.e("Exception", "" + e);
                                        }
                                    }
                                    gru.setItems(ch_list);
                                    list.add(gru);
                                } catch (Exception e) {
                                    pd.setVisibility(View.GONE);
                                    Log.e("Exception", "" + e);
                                }
                            Attachrecyclreview(list);

                            //group clikc
                            expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                                @Override
                                public boolean onGroupClick(ExpandableListView listview, View view,
                                                            int group_pos, long id) {
                                    FilterModel group_model = list.get(group_pos);
                                    Log.e("iddddddddddddd", "" + group_model.getName());

                                    return false;
                                }
                            });
                            // This listener will show toast on child click
                            expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                                @Override
                                public boolean onChildClick(ExpandableListView listview, View view,
                                                            int groupPos, int childPos, long id) {
                                    FilterModel group_model = list.get(groupPos);
                                    FilterInnerModel child = group_model.getItems().get(childPos);

                                    return false;
                                }
                            });
                        } else {
                            //nodata found
                            pd.setVisibility(View.GONE);
                            lvnodata_filter.setVisibility(View.VISIBLE);
                            cordinator_filter.setVisibility(View.GONE);
                            tv_noting.setText("No Filter Found");
                            tv_messgenoti.setVisibility(View.GONE);
                            Filterlist_Adapter.filter_child_value_list.clear();
                            Filterlist_Adapter.filter_grouppp_namelist.clear();
                            FilterListFragment.selected_child="";


                        }

                    }else {
                        //nodata found
                        pd.setVisibility(View.GONE);
                        lvnodata_filter.setVisibility(View.VISIBLE);
                        cordinator_filter.setVisibility(View.GONE);
                        tv_noting.setText("No Filter Found");
                        tv_messgenoti.setVisibility(View.GONE);
                        Filterlist_Adapter.filter_child_value_list.clear();
                        Filterlist_Adapter.filter_grouppp_namelist.clear();
                        FilterListFragment.selected_child="";


                    }

                } catch (JSONException e) {
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.setVisibility(View.GONE);
                        lvnodata_filter.setVisibility(View.GONE);
                        cordinator_filter.setVisibility(View.VISIBLE);
                        pd.setVisibility(View.GONE);
                        Toast.makeText(getActivity(),parent.getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(),error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

    }

    private void Attachrecyclreview(ArrayList<FilterModel> list) {
        expandableListAdapter = new Filterlist_Adapter(getActivity(), list);
        expandableListAdapter.setmListener(this);
        expandableListView.setAdapter(expandableListAdapter);
    }

    private void AllocateMemory(View v) {
        lv_filter_main = v.findViewById(R.id.lv_filter_main);
        pd = v.findViewById(R.id.progressBar);
        toolbar_filter = v.findViewById(R.id.toolbar_filter);
        expandableListView = v.findViewById(R.id.expandableListView);
        cordinator_filter = v.findViewById(R.id.cordinator_filter);
        lvnodata_filter = v.findViewById(R.id.lvnodata_filter);
        tv_noting = v.findViewById(R.id.tv_noting);
        tv_messgenoti = v.findViewById(R.id.tv_messgenoti);
        lv_apply = v.findViewById(R.id.lv_apply);
        lv_Reset = v.findViewById(R.id.lv_Reset);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        //    inflater.inflate(R.menu.menu_home, menu);
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
    public void onTotalChanged(int sum) {
    }
    @Override
    public void expandGroupEvent(int groupPosition, boolean isExpanded) {
        if(isExpanded)
            expandableListView.collapseGroup(groupPosition);
        else
            expandableListView.expandGroup(groupPosition);
    }


    @Override
    public void onClick(View v) {
        if(v==lv_apply)
        {
            Log.e("debug_33", "==" + Filterlist_Adapter.filter_child_value_list);
            Log.e("debug_apply","461"+ FilterListFragment.selected_child);
            Log.e("debug_apply5","461"+ Filterlist_Adapter.filter_grouppp_namelist);

            if(screen.equalsIgnoreCase("productlist"))
            {
                // Log.e("debug_109child", ""+Filterlist_Adapter.pass_ArrayList_child);
                //  Log.e("debug_100grroup", ""+Filterlist_Adapter.pass_ArrayList_group);
                Bundle b=new Bundle();
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                b.putStringArrayList("filter_child_id", Filterlist_Adapter.pass_ArrayList_child);
                b.putStringArrayList("filter_group_name",Filterlist_Adapter.pass_ArrayList_group);
                b.putString("subcat_id",sub_category_id);
                b.putString("subcatename",subcatename);
                b.putInt("sort_select",sort);


                b.putString("selected_child",FilterListFragment.selected_child);
                b.putStringArrayList("filter_group_namelist",Filterlist_Adapter.filter_grouppp_namelist);


                Log.e("filter_debug_365",""+sort);

                ProductListFragment myFragment = new ProductListFragment();
                myFragment.setArguments(b);
                activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                        0, 0, R.anim.fade_out).setCustomAnimations(R.anim.fade_in,
                        0, 0, R.anim.fade_out).add(R.id.framlayout, myFragment)
                        .commit();
            }else if(screen.equalsIgnoreCase("offer")) {
                Log.e("debug_109child", ""+Filterlist_Adapter.pass_ArrayList_child);
                Log.e("debug_100grroup", ""+Filterlist_Adapter.pass_ArrayList_group);

                Bundle b=new Bundle();
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                b.putStringArrayList("filter_child_id", Filterlist_Adapter.pass_ArrayList_child);
                b.putStringArrayList("filter_group_name",Filterlist_Adapter.pass_ArrayList_group);
                // b.putStringArrayList("filter_child_value_list",Filterlist_Adapter.filter_child_value_list);
                b.putInt("sort_select",sort);
                b.putString("selected_child",FilterListFragment.selected_child);
                b.putStringArrayList("filter_group_namelist",Filterlist_Adapter.filter_grouppp_namelist);

                OfferFragment myFragment = new OfferFragment();
                myFragment.setArguments(b);
                activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                        0, 0, R.anim.fade_out).setCustomAnimations(R.anim.fade_in,
                        0, 0, R.anim.fade_out).add(R.id.framlayout, myFragment)
                        .commit();
            }else if(screen.equalsIgnoreCase("Search"))
            {
                Log.e("debug_109childsearch", ""+Filterlist_Adapter.pass_ArrayList_child);
                Log.e("debug_100grroup", ""+Filterlist_Adapter.pass_ArrayList_group);

                Bundle b=new Bundle();
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                b.putStringArrayList("filter_child_id", Filterlist_Adapter.pass_ArrayList_child);
                b.putStringArrayList("filter_group_name",Filterlist_Adapter.pass_ArrayList_group);
                b.putString("searhkey",searhkey);
                b.putInt("sort_select",sort);
                b.putString("selected_child",FilterListFragment.selected_child);
                b.putStringArrayList("filter_group_namelist",Filterlist_Adapter.filter_grouppp_namelist);

                SearchFragment myFragment = new SearchFragment();
                myFragment.setArguments(b);
                activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                        0, 0, R.anim.fade_out).setCustomAnimations(R.anim.fade_in,
                        0, 0, R.anim.fade_out).add(R.id.framlayout, myFragment)
                        .commit();
            }


        }else if(v==lv_Reset)
        {

            Filterlist_Adapter.filter_child_value_list.clear();
            FilterListFragment.selected_child="";
            FilterListFragment.filter_old_childlist.clear();
            Filterlist_Adapter.filter_grouppp_namelist.clear();

            Bundle b=new Bundle();
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            b.putString("screen",screen);
            b.putString("subcat_id",sub_category_id);
            b.putString("subcatename",subcatename);
            b.putString("searhkey",searhkey);
            b.putInt("sort_select",sort);

            FilterListFragment myFragment = new FilterListFragment();
            myFragment.setArguments(b);
            activity.getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.fade_in,
                            0, 0, R.anim.fade_out)
                    .setCustomAnimations(R.anim.fade_in,
                            0, 0, R.anim.fade_out)
                    .addToBackStack(null).replace(R.id.framlayout, myFragment)
                    .commit();
        }
    }
}
