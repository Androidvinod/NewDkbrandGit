package com.dolphin.zanders.Fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.dolphin.zanders.Activity.NavigationActivity;


import com.dolphin.zanders.Adapter.Filterlist_Adapter;
import com.dolphin.zanders.Adapter.ProductListAdapter;
import com.dolphin.zanders.Model.NewProductListModel.NewProductListModel;
import com.dolphin.zanders.Model.ProductListAdater;
import com.dolphin.zanders.Model.ProductlistModel.GetCategoryProductlist;
import com.dolphin.zanders.Model.ProductlistModel.GetCategoryProductlistInnerData;
import com.dolphin.zanders.Model.Sortlist_model.GetSortModel;
import com.dolphin.zanders.Model.Sortlist_model.SortListSort;
import com.dolphin.zanders.Model.porduct_showcustome.Item;
import com.dolphin.zanders.Model.porduct_showcustome.ProductModel;
import com.dolphin.zanders.Preference.Login_preference;
import com.dolphin.zanders.R;
import com.dolphin.zanders.Retrofit.ApiClient;
import com.dolphin.zanders.Retrofit.ApiClientcusome;
import com.dolphin.zanders.Retrofit.ApiInterface;
import com.dolphin.zanders.Util.CheckNetwork;
import com.dolphin.zanders.Util.WrapContentGridLayoutManager;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

import static com.dolphin.zanders.Activity.NavigationActivity.drawer;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements View.OnClickListener, SearchView.OnQueryTextListener {


    View v;

    ProductListAdater productListAdater;
    public static ShimmerFrameLayout shimmer_productlist;
    RecyclerView recv_productlist;
    Toolbar toolbar_productlist;
    TextView tv_product_toolbar;
    ApiInterface apiInterface;
    String catid,subcatename,screen_type;
    WrapContentGridLayoutManager layoutManager;
    NavigationActivity parent;

    private String screen="product_list";
    List<com.dolphin.zanders.Model.NewProductListModel.Item> results;
    FloatingActionButton btn_fab;
    public static LinearLayout lv_productlist_progress,lv_main_productlist;


    private List<Integer> wishlitProductidList=new ArrayList<>();
    private List<Integer> wishlitItemId=new ArrayList<>();

   EditText searchPlateEditText;
   SearchView searchView;
   LinearLayout lv_search,lv_search_mainn,lvnodata_searchist;
    String searchValue="";
    String searhkey;
    List<com.dolphin.zanders.Model.NewProductListModel.Item> productList=new ArrayList<>();

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_product_list2, container, false);
       // productlist.clear();
        parent=(NavigationActivity) getActivity();
        AllocateMemory(v);

        AttachRecyclerView();
        setupUI(lv_search_mainn);

        shimmer_productlist.setVisibility(View.GONE);
          //  setupUI(lv_produt_parent);
        setHasOptionsMenu(true);
        hideKeyboard(parent);

        //searchview
        SearchView_Focus();

        lv_search.setVisibility(View.VISIBLE);
        apiInterface = ApiClientcusome.getClient().create(ApiInterface.class);

        ((NavigationActivity) parent).setSupportActionBar(toolbar_productlist);
        tv_product_toolbar.setText(parent.getResources().getString(R.string.search));
        ((NavigationActivity) parent).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
        ((NavigationActivity) parent).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_36dp);

        //calling sort api

        Log.e("debug_filter_search", "" + Filterlist_Adapter.filter_child_value_list);

        if(getActivity()!=null)
        {
            if (CheckNetwork.isNetworkAvailable(getActivity())) {

            if(Login_preference.getLogin_flag(getActivity()).equalsIgnoreCase("1"))
            {
                callWishistApi();
            }

        } else {
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.intcon), Toast.LENGTH_SHORT).show();
        }


        }

        // clicklistner
        btn_fab.setOnClickListener(this);

        searchView.setOnQueryTextListener(this);

        //search view edittext clicklistner on keyboard button clicklistner
        searchPlateEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    if(getActivity()!=null)
                    {
                        if (CheckNetwork.isNetworkAvailable(getActivity())) {
                            Log.e("debug_66", "" + searhkey);
                            searhkey = textView.getText().toString();
                            int length = searhkey.length();
                            Log.e("text_length_157_", "" + length);
                            if (length >= 3) {
                                hideKeyboard(getActivity());
                                if (CheckNetwork.isNetworkAvailable(getActivity())) {
                                    CallSearchValueApi(searhkey);
                                } else {
                                    Toast.makeText(getContext(), getActivity().getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getContext(), getActivity().getResources().getString(R.string.vall), Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
                        }
                    }

                }
                return true;
            }
        });

        return v;
    }

    private void callWishistApi() {
        wishlitProductidList.clear();
        wishlitItemId.clear();
        getwishlistdata().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response_favourite", "" + response.body());
                Log.e("response_favourite", "" + response);
                ResponseBody getFavouriteslist = response.body();
                if(response.isSuccessful() || response.code()==200)
                {
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(response.body().string());
                        Log.e("jsonarray", "=" +jsonArray);
                        Log.e("jsonarraylength", "=" +jsonArray.length());
                        //  Log.e("jsonarray66ss", "=" +jsonArray.getJSONObject(0).getJSONObject("product"));

                        if(jsonArray.length()==0)
                        { }
                        else {

                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.e("product_id", "=" +jsonObject.getString("product_id"));
                                    wishlitProductidList.add(jsonObject.getInt("product_id"));
                                    wishlitItemId.add(jsonObject.getInt("wishlist_item_id"));
                                    Log.e("price", "=" + jsonObject.getJSONObject("product").optString("price"));
                                    Log.e("name", "=" + jsonObject.getJSONObject("product").optString("name"));
                                    Log.e("special_price", "=" + jsonObject.getJSONObject("product").optString("special_price"));
                                    Log.e("thumbnail", "=" + jsonObject.getJSONObject("product").optString("thumbnail"));
                                } catch (Exception e) {
                                    Log.e("exception22", "=" + e.getLocalizedMessage());
                                }

                            }
                            // wishListAdapter.notifyDataSetChanged();
                        }


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }else {

                    NavigationActivity.get_Customer_tokenapi();
                    callWishistApi();

                    // Toast.makeText(parent, ""+getFavouriteslist.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                if(getActivity()!=null)
                {
                    Toast.makeText(getActivity(), "" + getActivity().getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    private Call<ResponseBody> getwishlistdata() {
        ApiInterface api = ApiClientcusome.getClient().create(ApiInterface.class);

        if(getActivity()!=null)
        {
            Log.e("debug_11token22","=="+ Login_preference.getCustomertoken(getActivity()));

        }

        return api.defaultgetWishlistData("Bearer "+Login_preference.getCustomertoken(parent));
    }

    private void CallSearchValueApi(String searhkey) {
        searchValue="";
        lvnodata_searchist.setVisibility(View.GONE);
        lv_productlist_progress.setVisibility(View.VISIBLE);
        lv_main_productlist.setVisibility(View.GONE);
        getsearchValue(searhkey).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("res_searh", "" + response.body());
                Log.e("res_searh11", "" + response);

                if (response.isSuccessful() || response.code() == 200) {
                    JSONObject jsonObject1 = null;
                    try {
                        jsonObject1 = new JSONObject(response.body().string());
                        Log.e("jsonObject1", "=" + jsonObject1);

                        if(jsonObject1.optString("items")==null || jsonObject1.optString("items").equalsIgnoreCase("null"))
                        {
                            if(getActivity()!=null)
                            {
                                Toast.makeText(getActivity(), "No Search Result Found", Toast.LENGTH_SHORT).show();

                            }


                            lvnodata_searchist.setVisibility(View.VISIBLE);
                            lv_productlist_progress.setVisibility(View.GONE);
                            lv_main_productlist.setVisibility(View.GONE);

                        }
                        JSONArray jsonArray=jsonObject1.getJSONArray("items");
                        Log.e("jsonarray", "=" + jsonArray);

                        if (jsonArray.length() == 0 || jsonArray==null || jsonArray.equals("null")) {
                            Log.e("jso_else", "=" + jsonArray);

                            if(getActivity()!=null)
                            {
                                Toast.makeText(getActivity(), "No Search Result Found", Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.e("product_id", "=" + jsonObject.optString("id"));

                                    if(searchValue.equalsIgnoreCase(""))
                                    {
                                        searchValue= jsonObject.optString("id");
                                    }else {
                                        searchValue+= "," + jsonObject.optString("id");
                                    }

                                } catch (Exception e) {
                                    Log.e("exception22", "=" + e.getLocalizedMessage());
                                }
                            }

                            Log.e("finalsearch", "" + searchValue);

                            if(searchValue.equalsIgnoreCase(""))
                            {

                            }else {
                               // callSearchListApi(searchValue);
                                CALL_productlist_Api(searchValue);
                            }


                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    lvnodata_searchist.setVisibility(View.GONE);
                    lv_productlist_progress.setVisibility(View.VISIBLE);
                    lv_main_productlist.setVisibility(View.GONE);

                    // Toast.makeText(parent, ""+getFavouriteslist.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                if(getActivity()!=null)
                {
                    Toast.makeText(getActivity(), "" + getActivity().getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    private List<com.dolphin.zanders.Model.NewProductListModel.Item> fetchResults(Response<NewProductListModel> response) {
        Log.e("newin_home_209", "" + response.body());
        NewProductListModel ProductModel = response.body();
        return ProductModel.getItems();
    }
    @SuppressLint("RestrictedApi")
    private void CALL_productlist_Api(String searchValue) {
        productList.clear();
        shimmer_productlist.setVisibility(View.VISIBLE);
        btn_fab.setVisibility(View.GONE);
        lv_productlist_progress.setVisibility(View.VISIBLE);

        getsearchapi(searchValue).enqueue(new Callback<ResponseBody>() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("res_searchlist", "=" + response.body());
                Log.e("res_45343cate", "=" + response);

                if(response.code()==200 || response.isSuccessful())
                {
                    btn_fab.setVisibility(View.VISIBLE);
                    lv_productlist_progress.setVisibility(View.GONE);
                    shimmer_productlist.setVisibility(View.GONE);
                    lv_main_productlist.setVisibility(View.VISIBLE);
                    ResponseBody model = response.body();
                    // results = fetchResults(response);
                    try {
                        JSONObject jsonObject=new JSONObject(response.body().string());
                       // Log.e("jsonObject33", "=" + jsonObject);
                        JSONArray itemarray=jsonObject.getJSONArray("items");
                        if(itemarray.length()==0 || itemarray.isNull(0))
                        {
                            if(getActivity()!=null)
                            {
                                Toast.makeText(getActivity(), "No Search Result Found", Toast.LENGTH_SHORT).show();

                            }

                            lvnodata_searchist.setVisibility(View.VISIBLE);
                            lv_main_productlist.setVisibility(View.GONE);
                            lv_main_productlist.setVisibility(View.GONE);
                            btn_fab.setVisibility(View.GONE);
                        }else {
                            btn_fab.setVisibility(View.VISIBLE);
                            lv_productlist_progress.setVisibility(View.GONE);
                            shimmer_productlist.setVisibility(View.GONE);
                            lv_main_productlist.setVisibility(View.VISIBLE);


                            //special price
                            for(int i=0;i<itemarray.length();i++)
                            {
                                JSONObject itemobject=itemarray.getJSONObject(i);
                               // Log.e("debug_productid", "=" +itemobject.optString("id"));
                                //Log.e("wiish_itemid", "=" + wishlitItemId.size());
                                //   JSONObject itemobject=itemarray.getJSONObject(k);

                                JSONArray attributearry = itemobject.getJSONArray("custom_attributes");
                             //   Log.e("attributearry", "=" +attributearry);
                                double  special_price_value=0;

                                for(int j=0;j<attributearry.length();j++)
                                {
                                    JSONObject attributeobject = attributearry.getJSONObject(j);
                                    Log.e("attributeobject", "=" +attributeobject);
                                    Log.e("attributeobject666666", "=" +attributeobject.getString("attribute_code"));

                                    if(attributeobject.getString("attribute_code").equalsIgnoreCase("special_price") || attributeobject.getString("attribute_code").contains("special_price")){
                                        special_price_value= Double.parseDouble((attributeobject.getString("value")));
                                        Log.e("special_price33", "=" +special_price_value);

                                    }else {
                                        Log.e("special_price44", "=" +i);
                                    }
                                }
                                //---------------------------------------------------------------------------------------

                                String isWishliste,imagge = null,wishlist_item_id = null,tier_prices;


                                JSONArray media_gallery_array=itemobject.optJSONArray("media_gallery_entries");
                                for(int j=0;j<media_gallery_array.length();j++)
                                {
                                    JSONObject jsonObject1=media_gallery_array.getJSONObject(j);
                                    JSONArray typesarray=jsonObject1.optJSONArray("types");
                                    Log.e("typesarraylength", "=" +typesarray.length());
                                    if(typesarray.length()==0 || typesarray==null)
                                    {
                                        Log.e("if333", "=" );
                                    }else {

                                        for (int k=0;k<typesarray.length();k++)
                                        {
                                            Log.e("small44", "=" +typesarray.get(k).equals("small_image"));
                                            if( typesarray.get(k).equals("thumbnail"))
                                            {
                                                imagge=jsonObject1.optString("file");
                                            }else {
                                               // imagge=jsonObject1.optString("file");
                                            }

                                        }



                                        Log.e("imagge444", "=" +imagge);
                                    }

                                }
                                
                               // imagge=itemobject.optJSONArray("media_gallery_entries").optJSONObject(0).optString("file");
                                JSONArray tierpricearray=itemobject.getJSONArray("tier_prices");

                                if(tierpricearray.length()==0)
                                {
                                    tier_prices="0";
                                }else {
                                    tier_prices=tierpricearray.getJSONObject(tierpricearray.length()-1).optString("value");
                                }

                                Log.e("imagge222", "=" +tierpricearray.length());
                                Log.e("imagge22277", "=" +imagge);
                                Log.e("tier_prices33", "=" +tier_prices);

                                Log.e("wishlitsize", "=" +wishlitProductidList.size());
                                int id= Integer.parseInt(itemarray.getJSONObject(i).optString("id"));
                                Log.e("deebug_378", "=" +id);
                                if(wishlitProductidList.contains(id))
                                {
                                    Log.e("if", "=" +id);
                                    Log.e("if_item_id", "=" + wishlitProductidList.indexOf(id));
                                    int pos=wishlitProductidList.indexOf(id);

                                    if(wishlitItemId.size()>0)
                                    {
                                        wishlist_item_id= String.valueOf(wishlitItemId.get(pos));
                                        Log.e("wishlist_item_id_3317", "=" + wishlist_item_id);
                                    }
                                    isWishliste="true";
                                    Log.e("special_price366", "=" +special_price_value);
                                    productList.add(new com.dolphin.zanders.Model.NewProductListModel.Item(
                                            isWishliste,wishlist_item_id,imagge,
                                            itemobject.optInt("id"),
                                            itemobject.optString("sku"),
                                            itemobject.optString("name"),
                                            itemobject.optInt("price"),
                                            tier_prices,String.valueOf(special_price_value)
                                    ));

                                }else{
                                    wishlist_item_id= "0";
                                    isWishliste="false";

                                    Log.e("else", "=" +itemarray.getJSONObject(i).optString("id"));
                                    Log.e("special_price886", "=" +special_price_value);

                                    productList.add(new com.dolphin.zanders.Model.NewProductListModel.Item(
                                            isWishliste,wishlist_item_id,imagge,
                                            itemobject.optInt("id"),
                                            itemobject.optString("sku"),
                                            itemobject.optString("name"),
                                            itemobject.optInt("price"),
                                            tier_prices,String.valueOf(special_price_value)
                                    ));
                                }
                            }
                            AttachRecyclerView();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    shimmer_productlist.setVisibility(View.GONE);
                }else {
                    btn_fab.setVisibility(View.GONE);
                    shimmer_productlist.setVisibility(View.GONE);
                    btn_fab.setVisibility(View.GONE);
                    lv_productlist_progress.setVisibility(View.GONE);
                    shimmer_productlist.setVisibility(View.GONE);
                    lv_main_productlist.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                shimmer_productlist.setVisibility(View.GONE);
                shimmer_productlist.setVisibility(View.GONE);
                btn_fab.setVisibility(View.GONE);
                lv_productlist_progress.setVisibility(View.GONE);
                shimmer_productlist.setVisibility(View.GONE);
                lv_main_productlist.setVisibility(View.VISIBLE);
                Log.e("debug_failure", "=" + t.getMessage());
            }
        });
    }


   /* @SuppressLint("RestrictedApi")
    private void CALL_productlist_Api(String searchValue) {
        shimmer_productlist.setVisibility(View.VISIBLE);
        btn_fab.setVisibility(View.GONE);
        lv_productlist_progress.setVisibility(View.VISIBLE);
        getsearchapi(searchValue).enqueue(new Callback<ProductModel>() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                Log.e("responseeee_cate", "=" + response.body());
                Log.e("responseeee_45343cate", "=" + response);

                //  Log.e("Success_35", "=" + response.body().getSearchCriteria());


                if(response.code()==200 || response.isSuccessful())
                {
                    btn_fab.setVisibility(View.VISIBLE);

                    lv_productlist_progress.setVisibility(View.GONE);
                    shimmer_productlist.setVisibility(View.GONE);
                    lv_main_productlist.setVisibility(View.VISIBLE);
                    ProductModel model = response.body();
                    results = fetchResults(response);
                    if(results.isEmpty() || results== null || results.size()==0)
                    {
                        Toast.makeText(getActivity(), "No Search Result Found", Toast.LENGTH_SHORT).show();
                        lvnodata_searchist.setVisibility(View.VISIBLE);
                        lv_main_productlist.setVisibility(View.GONE);
                        lv_main_productlist.setVisibility(View.GONE);
                        btn_fab.setVisibility(View.GONE);
                    }else {
                        for(int i=0;i<results.size();i++)
                        {
                            Log.e("debug_productid", "=" + results.get(i).getId());
                            Log.e("wiish_itemid", "=" + wishlitItemId.size());
                            String isWishliste,imagge,wishlist_item_id = null;
                            if(wishlitProductidList.contains(results.get(i).getId()))
                            {
                                Log.e("if", "=" + results.get(i).getId());
                                Log.e("if_item_id", "=" + wishlitProductidList.indexOf(results.get(i).getId()));
                                int pos=wishlitProductidList.indexOf(results.get(i).getId());

                                if(wishlitItemId.size()>0)
                                {
                                    wishlist_item_id= String.valueOf(wishlitItemId.get(pos));
                                    Log.e("wishlist_item_id_3317", "=" + wishlist_item_id);
                                }

                                isWishliste="true";
                                results.get(i).setIsWishlisted(isWishliste);
                                results.get(i).setWishlist_item_id(wishlist_item_id);
                            }else{
                                wishlist_item_id= "0";
                                isWishliste="false";

                                results.get(i).setWishlist_item_id(wishlist_item_id);
                                results.get(i).setIsWishlisted(isWishliste);
                                Log.e("else", "=" + results.get(i).getId());

                            }
                        }
                        productListAdater.addAll(results);
                    }



                    shimmer_productlist.setVisibility(View.GONE);
                }else {
                    btn_fab.setVisibility(View.GONE);
                    lv_productlist_progress.setVisibility(View.GONE);
                    shimmer_productlist.setVisibility(View.GONE);
                    lv_main_productlist.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(Call<ProductModel> call, Throwable t) {
                t.printStackTrace();
                shimmer_productlist.setVisibility(View.GONE);
                btn_fab.setVisibility(View.GONE);
                lv_productlist_progress.setVisibility(View.GONE);
                shimmer_productlist.setVisibility(View.GONE);
                lv_main_productlist.setVisibility(View.VISIBLE);
                Log.e("debug_failure", "=" + t.getMessage());
            }
        });
    }
*/
    private Call<ProductModel> getcategory(String searchValue) {
        Log.e("catid_113", "=" + catid);
        return apiInterface.products("Bearer "+ Login_preference.gettoken(parent),"category_id",catid);
    }


    private Call<ResponseBody> getsearchapi(String searchValuee) {
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Log.e("debug_213n22", "==" + Login_preference.gettoken(parent
        ));
        Log.e("searchValuee22", "==" + searchValuee);
        //http://dkbraende.demoproject.info/rest/all/V1/products?searchCriteria[filterGroups][0][filters][0][field]=entity_id&searchCriteria[filterGroups][0][filters][0][value]=364,227&searchCriteria[filterGroups][0][filters][0][conditionType]=fineset
        String url=ApiClientcusome.MAIN_URLL+"products?searchCriteria[filterGroups][0][filters][0][field]=entity_id&searchCriteria[filterGroups][0][filters][0][value]="
                +searchValuee+"&searchCriteria[filterGroups][0][filters][0][conditionType]=fineset";

        Log.e("debug_searchvalue", "==" + url);
        return api.getSearchList("Bearer " + Login_preference.gettoken(parent),url);
    }


    private Call<ResponseBody> getsearchValue(String searhkey) {
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Log.e("debug_11token22", "==" + Login_preference.gettoken(parent));
        ////http://dkbraende.demoproject.info/rest/V1/search?searchCriteria[requestName]=quick_search_container&searchCriteria[filter_groups][0][filters][0][field]=search_term&searchCriteria[filter_groups][0][filters][0][value]=brændetårn&fields=items[id]
        String url=ApiClientcusome.MAIN_URLL+"search?searchCriteria[requestName]=quick_search_container&searchCriteria[filter_groups][0][filters][0][field]=search_term&searchCriteria[filter_groups][0][filters][0][value]="+searhkey+"&fields=items[id]";
        Log.e("debug_333", "==" + url);
        return api.getSearchValue("Bearer " + Login_preference.gettoken(parent),url);
    }


    private void SearchView_Focus() {
        searchView.setIconifiedByDefault(false);
        searchView.setFocusable(true);
        searchView.requestFocus();
        searchView.requestFocusFromTouch();
        searchView.setIconified(false);

    }
    @Override
    public void onClick(View v) {
        if (v == btn_fab) {

            if (ApiClientcusome.VIEWTYPE == 0) {
                btn_fab.setImageDrawable(parent.getResources().getDrawable(R.drawable.grid_ic));

                //  Log.e("data_show_verticle_btn", "grid_to_verticle");
                productListAdater = new ProductListAdater(getContext(),productList);
                layoutManager = new WrapContentGridLayoutManager(getContext(), 1);
                recv_productlist.setLayoutManager(layoutManager);
                recv_productlist.setAdapter(productListAdater);
                //   productListAdater.addAll(results);

                ApiClientcusome.VIEWTYPE = 2;
                // flag=true;
            } else if (ApiClientcusome.VIEWTYPE == 2) {
                btn_fab.setImageDrawable(parent.getResources().getDrawable(R.drawable.ic_view_list_white_36dp));
                productListAdater = new ProductListAdater(getContext(),productList);
                layoutManager = new WrapContentGridLayoutManager(getContext(), 2);
                recv_productlist.setLayoutManager(layoutManager);
                recv_productlist.setAdapter(productListAdater);
                // productListAdater.addAll(results);
                ApiClientcusome.VIEWTYPE = 0;
                //flag=false;
            }
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;

    }

    private void AttachRecyclerView() {
        Log.e("debug_3704",""+ApiClient.VIEWTYPE);
        if(ApiClientcusome.VIEWTYPE==0)
        {
            btn_fab.setImageDrawable(parent.getResources().getDrawable(R.drawable.ic_view_list_white_36dp));
            Log.e("debug_3",""+ApiClientcusome.VIEWTYPE);
            productListAdater = new ProductListAdater(getContext(),productList);
            layoutManager = new WrapContentGridLayoutManager(getContext(), 2);
            recv_productlist.setLayoutManager(layoutManager);
            recv_productlist.setAdapter(productListAdater);
        }else if(ApiClientcusome.VIEWTYPE==2) {
            btn_fab.setImageDrawable(parent.getResources().getDrawable(R.drawable.grid_ic));
            Log.e("debug_3dff",""+ApiClientcusome.VIEWTYPE);
            productListAdater = new ProductListAdater(getContext(),productList);
            layoutManager = new WrapContentGridLayoutManager(getContext(), 1);
            recv_productlist.setLayoutManager(layoutManager);
            recv_productlist.setAdapter(productListAdater);

        }else {
            btn_fab.setImageDrawable(parent.getResources().getDrawable(R.drawable.ic_view_list_white_36dp));

            Log.e("debug_3d",""+ApiClientcusome.VIEWTYPE);
            productListAdater = new ProductListAdater(getContext(),productList);
            layoutManager = new WrapContentGridLayoutManager(getContext(), 2);
            recv_productlist.setLayoutManager(layoutManager);
            recv_productlist.setAdapter(productListAdater);
        }



    }

    private void AllocateMemory(View v) {
        lvnodata_searchist = v.findViewById(R.id.lvnodata_searchist);
        lv_search_mainn = v.findViewById(R.id.lv_search_mainn);
        lv_search = v.findViewById(R.id.lv_search);
        searchView = v.findViewById(R.id.search);

        int search = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        searchPlateEditText = (EditText) searchView.findViewById(search);

        lv_main_productlist= v.findViewById(R.id.lv_main_productlist);
        lv_productlist_progress= v.findViewById(R.id.lv_productlist_progress);
        recv_productlist= v.findViewById(R.id.recv_productlist);
        shimmer_productlist = v.findViewById(R.id.shimmer_productlist);
        toolbar_productlist =v.findViewById(R.id.toolbar_productlist);
        tv_product_toolbar = v.findViewById(R.id.tv_product_toolbar);
        btn_fab = v.findViewById(R.id.btn_fab);

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
        super.onCreateOptionsMenu(menu, inflater);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                hideKeyboard(parent);
                parent.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
