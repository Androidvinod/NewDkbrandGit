package com.dolphin.zanders.Fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dolphin.zanders.Activity.NavigationActivity;
import com.dolphin.zanders.Adapter.Filterlist_Adapter;
import com.dolphin.zanders.Adapter.ProductListAdapter;
import com.dolphin.zanders.Model.NewProductListModel.NewProductListModel;
import com.dolphin.zanders.Model.ProductListAdater;
import com.dolphin.zanders.Model.porduct_showcustome.Item;
import com.dolphin.zanders.Model.porduct_showcustome.ProductModel;
import com.dolphin.zanders.Preference.Login_preference;
import com.dolphin.zanders.R;

import com.dolphin.zanders.Retrofit.ApiClientcusome;
import com.dolphin.zanders.Retrofit.ApiInterface;
import com.dolphin.zanders.Util.CheckNetwork;
import com.dolphin.zanders.Util.WrapContentGridLayoutManager;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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

public class NewProductListFragment extends Fragment {
    ProductListAdater productListAdater;
    public static ShimmerFrameLayout shimmer_productlist;
    RecyclerView recv_productlist;
    Toolbar toolbar_productlist;
    TextView tv_product_toolbar;
    ApiInterface apiInterface;
    String catid, subcatename, screen_type;
    WrapContentGridLayoutManager layoutManager;
    NavigationActivity parent;
    LinearLayout lvnodata_searchist;
    Bundle b;
    MenuItem login;
    View v;
    private String screen = "product_list";
    List<com.dolphin.zanders.Model.NewProductListModel.Item> results;
    FloatingActionButton btn_fab;
    public static LinearLayout lv_productlist_progress, lv_main_productlist;
    double special_price_value = 0;
    CoordinatorLayout coordinator_productlist;
    List<com.dolphin.zanders.Model.NewProductListModel.Item> productList = new ArrayList<>();
    private List<Integer> wishlitProductidList = new ArrayList<>();
    private List<Double> specialpricelist = new ArrayList<>();
    private List<Integer> wishlitItemId = new ArrayList<>();

    public NewProductListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_product_list2, container, false);
        //catid=getIntent().getStringExtra("id");
        parent = (NavigationActivity) getActivity();
        Log.e("debugeee", "e==" + catid);
        b = this.getArguments();
        allocateMemory(v);
        productList.clear();
        apiInterface = ApiClientcusome.getClient().create(ApiInterface.class);
        setHasOptionsMenu(true);

        if (b != null) {
            catid = b.getString("subcat_id");
            subcatename = b.getString("subcatename");
            screen_type = b.getString("screen");
//            srt_select = b.getInt("sort_select");
            Log.e("srt_slct", "" + subcatename);
            Log.e("procudt_id_141", "" + catid);

        } else {
            Log.e(".bundle", "" + b);
            // tv_product_toolbar.setText("Poduct List");
        }

        ((NavigationActivity) parent).setSupportActionBar(toolbar_productlist);
        ((NavigationActivity) parent).getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if (screen_type.equalsIgnoreCase("category")) {
            ((NavigationActivity) parent).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            toolbar_productlist.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawer.openDrawer(GravityCompat.START);
                }
            });
        } else if (screen_type.equalsIgnoreCase("subcategory")) {
            ((NavigationActivity) parent).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_36dp);
        }


        tv_product_toolbar.setText(subcatename);

        AttachRecyclerView();


        btn_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ApiClientcusome.VIEWTYPE == 0) {
                    btn_fab.setImageDrawable(parent.getResources().getDrawable(R.drawable.grid_ic));

                    //  Log.e("data_show_verticle_btn", "grid_to_verticle");
                    productListAdater = new ProductListAdater(getContext(), productList);
                    layoutManager = new WrapContentGridLayoutManager(getContext(), 1);
                    recv_productlist.setLayoutManager(layoutManager);
                    recv_productlist.setAdapter(productListAdater);
                    //   productListAdater.addAll(results);

                    ApiClientcusome.VIEWTYPE = 2;
                    // flag=true;
                } else if (ApiClientcusome.VIEWTYPE == 2) {
                    btn_fab.setImageDrawable(parent.getResources().getDrawable(R.drawable.ic_view_list_white_36dp));
                    productListAdater = new ProductListAdater(getContext(), productList);
                    layoutManager = new WrapContentGridLayoutManager(getContext(), 2);
                    recv_productlist.setLayoutManager(layoutManager);
                    recv_productlist.setAdapter(productListAdater);
                    // productListAdater.addAll(results);
                    ApiClientcusome.VIEWTYPE = 0;
                    //flag=false;
                }
            }
        });

        if (CheckNetwork.isNetworkAvailable(parent)) {

            if (Login_preference.getLogin_flag(parent).equalsIgnoreCase("1")) {
                callWishistApi();
            } else {
                CALL_productlist_Api();
            }


        } else {
            Toast.makeText(parent, parent.getResources().getString(R.string.intcon), Toast.LENGTH_SHORT).show();
        }


        return v;

    }

    private void AttachRecyclerView() {

        if (ApiClientcusome.VIEWTYPE == 0) {
            btn_fab.setImageDrawable(parent.getResources().getDrawable(R.drawable.ic_view_list_white_36dp));
            Log.e("debug_3", "" + ApiClientcusome.VIEWTYPE);
            productListAdater = new ProductListAdater(getContext(), productList);
            layoutManager = new WrapContentGridLayoutManager(getContext(), 2);
            recv_productlist.setLayoutManager(layoutManager);
            recv_productlist.setAdapter(productListAdater);
        } else if (ApiClientcusome.VIEWTYPE == 2) {
            btn_fab.setImageDrawable(parent.getResources().getDrawable(R.drawable.grid_ic));
            Log.e("debug_3dff", "" + ApiClientcusome.VIEWTYPE);
            productListAdater = new ProductListAdater(getContext(), productList);
            layoutManager = new WrapContentGridLayoutManager(getContext(), 1);
            recv_productlist.setLayoutManager(layoutManager);
            recv_productlist.setAdapter(productListAdater);

        } else {
            btn_fab.setImageDrawable(parent.getResources().getDrawable(R.drawable.ic_view_list_white_36dp));

            Log.e("debug_3d", "" + ApiClientcusome.VIEWTYPE);
            productListAdater = new ProductListAdater(getContext(), productList);
            layoutManager = new WrapContentGridLayoutManager(getContext(), 2);
            recv_productlist.setLayoutManager(layoutManager);
            recv_productlist.setAdapter(productListAdater);
        }


    }

    private void allocateMemory(View v) {
        lv_main_productlist = v.findViewById(R.id.lv_main_productlist);
        lv_productlist_progress = v.findViewById(R.id.lv_productlist_progress);
        recv_productlist = v.findViewById(R.id.recv_productlist);
        shimmer_productlist = v.findViewById(R.id.shimmer_productlist);
        toolbar_productlist = v.findViewById(R.id.toolbar_productlist);
        tv_product_toolbar = v.findViewById(R.id.tv_product_toolbar);
        btn_fab = v.findViewById(R.id.btn_fab);
        lvnodata_searchist = v.findViewById(R.id.lvnodata_searchist);
        coordinator_productlist = v.findViewById(R.id.coordinator_productlist);

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
                if (response.isSuccessful() || response.code() == 200) {
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(response.body().string());
                        Log.e("jsonarray", "=" + jsonArray);
                        Log.e("jsonarraylength", "=" + jsonArray.length());
                        //  Log.e("jsonarray66ss", "=" +jsonArray.getJSONObject(0).getJSONObject("product"));
                        if (jsonArray.length() == 0) {
                            CALL_productlist_Api();
                        } else {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.e("product_id", "=" + jsonObject.getString("product_id"));
                                    wishlitProductidList.add(jsonObject.getInt("product_id"));
                                    wishlitItemId.add(jsonObject.getInt("wishlist_item_id"));
                                    Log.e("productid2333", "=" + jsonObject.getInt("product_id"));
                                    Log.e("name", "=" + jsonObject.getJSONObject("product").optString("name"));
                                } catch (Exception e) {
                                    Log.e("exception22", "=" + e.getLocalizedMessage());
                                }
                            }
                            CALL_productlist_Api();
                            // wishListAdapter.notifyDataSetChanged();
                        }

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    NavigationActivity.get_Customer_tokenapi();
                    callWishistApi();
                    // Toast.makeText(parent, ""+getFavouriteslist.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(parent, "" + parent.getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private Call<ResponseBody> getwishlistdata() {
        ApiInterface api = ApiClientcusome.getClient().create(ApiInterface.class);
        Log.e("debug_11token22", "==" + Login_preference.getCustomertoken(parent));
        return api.defaultgetWishlistData("Bearer " + Login_preference.getCustomertoken(parent));
    }


    @SuppressLint("RestrictedApi")
    private void CALL_productlist_Api() {
        productList.clear();
        specialpricelist.clear();
        shimmer_productlist.setVisibility(View.VISIBLE);
        btn_fab.setVisibility(View.GONE);
        getproduct().enqueue(new Callback<ResponseBody>() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("responseeee_cate", "=" + response.body());
                Log.e("responseeee_45343cate", "=" + response);

                //  Log.e("Success_35", "=" + response.body().getSearchCriteria());
                if (response.code() == 200 || response.isSuccessful()) {
                    btn_fab.setVisibility(View.VISIBLE);
                    ResponseBody model = response.body();
                    // results = fetchResults(response);
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        Log.e("jsonObject33", "=" + jsonObject);

                        JSONArray itemarray = jsonObject.getJSONArray("items");
                        if (itemarray.length() == 0 || itemarray.isNull(0)) {
                            lvnodata_searchist.setVisibility(View.VISIBLE);
                            lv_productlist_progress.setVisibility(View.GONE);
                            shimmer_productlist.setVisibility(View.GONE);
                            coordinator_productlist.setVisibility(View.GONE);
                        } else {
                            lvnodata_searchist.setVisibility(View.GONE);
                            lv_productlist_progress.setVisibility(View.GONE);
                            shimmer_productlist.setVisibility(View.GONE);
                            coordinator_productlist.setVisibility(View.VISIBLE);


                            //special price
                            for (int i = 0; i < itemarray.length(); i++) {
                                JSONObject itemobject = itemarray.getJSONObject(i);
                                Log.e("debug_productid", "=" + itemobject.optString("id"));
                                Log.e("wiish_itemid", "=" + wishlitItemId.size());
                                //   JSONObject itemobject=itemarray.getJSONObject(k);

                                JSONArray attributearry = itemobject.getJSONArray("custom_attributes");
                                Log.e("attributearry", "=" + attributearry);
                                double special_price_value = 0;

                                for (int j = 0; j < attributearry.length(); j++) {
                                    JSONObject attributeobject = attributearry.getJSONObject(j);
                                    Log.e("attributeobject", "=" + attributeobject);
                                    Log.e("attributeobject666666", "=" + attributeobject.getString("attribute_code"));

                                    if (attributeobject.getString("attribute_code").equalsIgnoreCase("special_price") || attributeobject.getString("attribute_code").contains("special_price")) {
                                        String value = String.valueOf(Html.fromHtml(String.valueOf(attributeobject.getString("value") + "  Kr")));

                                        special_price_value = Double.parseDouble((attributeobject.getString("value")));
                                        Log.e("special_price33", "=" + special_price_value);
                                        specialpricelist.add(Double.valueOf(attributeobject.getString("value")));

                                    } else {
                                        Log.e("special_price44", "=" + i);

                                    }
                                }
                                //  System.exit(0);
                                //---------------------------------------------------------------------------------------

                             /*   Log.e("special_price_value33", "=" +special_price_value);
                                Log.e("debug_347", "=" +specialpricelist.size());
                                Log.e("debug_347itemarray", "=" +itemarray.length());
                                Log.e("special_p34443", "=" +special_price_value);*/

                                String isWishliste, imagge = null, wishlist_item_id = null, tier_prices;

                                JSONArray media_gallery_array = itemobject.optJSONArray("media_gallery_entries");
                                for (int j = 0; j < media_gallery_array.length(); j++) {
                                    JSONObject jsonObject1 = media_gallery_array.getJSONObject(j);
                                    JSONArray typesarray = jsonObject1.optJSONArray("types");
                                    Log.e("typesarraylength", "=" + typesarray.length());
                                    if (typesarray.length() == 0 || typesarray == null) {
                                        Log.e("if333", "=");
                                    } else {


                                        for (int k = 0; k < typesarray.length(); k++) {
                                            Log.e("small44", "=" + typesarray.get(k).equals("small_image"));
                                            if (typesarray.get(k).equals("thumbnail")) {
                                                imagge = jsonObject1.optString("file");
                                            } else {
                                                // imagge=jsonObject1.optString("file");
                                            }

                                        }
                                        //   imagge=jsonObject1.optString("file");
                                        Log.e("imagge444", "=" + imagge);
                                    }

                                }


                                JSONArray tierpricearray = itemobject.getJSONArray("tier_prices");

                                if (tierpricearray.length() == 0) {
                                    tier_prices = "0";
                                } else {
                                    tier_prices = tierpricearray.getJSONObject(tierpricearray.length() - 1).optString("value");
                                }

                                /*    Log.e("imagge222", "=" +tierpricearray.length());
                                    Log.e("imagge22277", "=" +imagge);
                                    Log.e("tier_prices33", "=" +tier_prices);*/
                                Log.e("wishlitsize", "=" + wishlitProductidList.size());
                                int id = Integer.parseInt(itemarray.getJSONObject(i).optString("id"));
                                Log.e("deebug_378", "=" + id);
                                if (wishlitProductidList.contains(id)) {
                                    Log.e("if", "=" + id);
                                    Log.e("if_item_id", "=" + wishlitProductidList.indexOf(id));
                                    int pos = wishlitProductidList.indexOf(id);
                                    if (wishlitItemId.size() > 0) {
                                        wishlist_item_id = String.valueOf(wishlitItemId.get(pos));
                                        Log.e("wishlist_item_id_3317", "=" + wishlist_item_id);
                                    }
                                    isWishliste = "true";
                                    Log.e("special_price366", "=" + special_price_value);
                                    productList.add(new com.dolphin.zanders.Model.NewProductListModel.Item(
                                            isWishliste, wishlist_item_id, imagge,
                                            itemobject.optInt("id"),
                                            itemobject.optString("sku"),
                                            itemobject.optString("name"),
                                            itemobject.optInt("price"),
                                            tier_prices, String.valueOf(special_price_value)
                                    ));
                                } else {
                                    wishlist_item_id = "0";
                                    isWishliste = "false";
                                    Log.e("else", "=" + itemarray.getJSONObject(i).optString("id"));
                                    Log.e("special_price886", "=" + special_price_value);

                                    productList.add(new com.dolphin.zanders.Model.NewProductListModel.Item(
                                            isWishliste, wishlist_item_id, imagge,
                                            itemobject.optInt("id"),
                                            itemobject.optString("sku"),
                                            itemobject.optString("name"),
                                            itemobject.optInt("price"),
                                            tier_prices, String.valueOf(special_price_value)
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
                } else {
                    btn_fab.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                shimmer_productlist.setVisibility(View.GONE);
                Log.e("debug_failure", "=" + t.getMessage());
            }
        });
    }


  /*  @SuppressLint("RestrictedApi")
    private void CALL_productlist_Api() {
        shimmer_productlist.setVisibility(View.VISIBLE);
        btn_fab.setVisibility(View.GONE);
        getcategory().enqueue(new Callback<ProductModel>() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                Log.e("responseeee_cate", "=" + response.body());
                Log.e("responseeee_45343cate", "=" + response);

                //  Log.e("Success_35", "=" + response.body().getSearchCriteria());


                if(response.code()==200 || response.isSuccessful())
                {
                    btn_fab.setVisibility(View.VISIBLE);


                    ProductModel model = response.body();
                     results = fetchResults(response);


                     if(results.size()==0 || results.isEmpty())
                     {
                         lvnodata_searchist.setVisibility(View.VISIBLE);
                         lv_productlist_progress.setVisibility(View.GONE);
                         shimmer_productlist.setVisibility(View.GONE);
                         coordinator_productlist.setVisibility(View.GONE);
                     }else {
                         lvnodata_searchist.setVisibility(View.GONE);
                         lv_productlist_progress.setVisibility(View.GONE);
                         shimmer_productlist.setVisibility(View.GONE);
                         coordinator_productlist.setVisibility(View.VISIBLE);
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

                }
            }
            @Override
            public void onFailure(Call<ProductModel> call, Throwable t) {
                t.printStackTrace();
                shimmer_productlist.setVisibility(View.GONE);
                Log.e("debug_failure", "=" + t.getMessage());
            }
        });
    }
*/

   /* private Call<ProductModel> getcategory() {
        Log.e("catid_113", "=" + catid);
        Log.e("catid_113", "=" + Login_preference.gettoken(getActivity()));
        return apiInterface.products("Bearer "+ Login_preference.gettoken(getActivity()),"category_id",catid);
    }*/


    private Call<ResponseBody> getproduct() {
        Log.e("catid_113", "=" + catid);
//        Log.e("catid_113", "=" + Login_preference.gettoken(getActivity()));
        return apiInterface.getproducts("Bearer " + Login_preference.gettoken(parent), "category_id", catid);
    }

    private List<com.dolphin.zanders.Model.NewProductListModel.Item> fetchResults(Response<NewProductListModel> response) {
        Log.e("newin_home_209", "" + response.body());
        NewProductListModel ProductModel = response.body();
        return ProductModel.getItems();
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
        if (Login_preference.getLogin_flag(parent).equalsIgnoreCase("1")) {
            login.setVisible(false);
        } else {
            login.setVisible(true);
        }
        super.onCreateOptionsMenu(menu, inflater);
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
