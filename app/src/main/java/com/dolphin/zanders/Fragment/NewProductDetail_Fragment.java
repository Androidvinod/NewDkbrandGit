package com.dolphin.zanders.Fragment;


import android.graphics.Paint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;


import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dolphin.zanders.Activity.NavigationActivity;

import com.dolphin.zanders.Activity.SplashActivity;
import com.dolphin.zanders.Adapter.Filterlist_Adapter;
import com.dolphin.zanders.Adapter.PDetailImageSliderAdapter;
import com.dolphin.zanders.Adapter.TierPriceAdapter;
import com.dolphin.zanders.Model.ProductDetailModelNew.Medium;
import com.dolphin.zanders.Model.ProductListAdater;
import com.dolphin.zanders.Model.TierPriceModel;
import com.dolphin.zanders.Model.porduct_showcustome.Item;
import com.dolphin.zanders.Preference.Login_preference;
import com.dolphin.zanders.R;
import com.dolphin.zanders.Retrofit.ApiClientcusome;
import com.dolphin.zanders.Retrofit.ApiInterface;
import com.dolphin.zanders.Util.CheckNetwork;
import com.dolphin.zanders.Util.MyBounceInterpolator;

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
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator;

import static com.dolphin.zanders.Activity.NavigationActivity.tv_wishlist_count;
import static com.dolphin.zanders.Fragment.ProductListFragment.subcat_id;
import static com.dolphin.zanders.Fragment.ProductListFragment.subcatename;

public class NewProductDetail_Fragment extends Fragment implements View.OnClickListener {
    Toolbar toolbar_product_detail;
    View v;
    ApiInterface api;
    LinearLayout lv_productdetails_progress,lv_pdetail_addtocart,lv_pdetail_addtowishlist,lv_tier_price;
    RecyclerView recycler_product_image;

    TextView tv_pdetail_wishlist,tv_daily_special_product_name,tv_description,tv_rifles_lv_price,tv_rifles_Specialprice,tv_shortdescription,tv_product_detail,tv_rifles_Specialprice_title,tv_rifles_lv_price_title;
    List<com.dolphin.zanders.Model.ProductDetailModelNew.Medium> medialist=new ArrayList<>();
    PDetailImageSliderAdapter pDetailImageSliderAdapter;
    ImageView iv_defolticon;

    NestedScrollView nested_scroll_detail;
    ScrollingPagerIndicator recyclerIndicator;
    String product_id,sku,wishlist_item_id="0",product_name;
    Boolean iswishlist=false;

    private List<Integer> wishlitProductidList=new ArrayList<>();
    private List<Integer> wishlitItemId=new ArrayList<>();
    RecyclerView recv_tier_price;
    TierPriceAdapter tierPriceAdapter;
    List<TierPriceModel> tierPriceModelList=new ArrayList<>();
    double special_price,price,tierprice;
    public NewProductDetail_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_product_detail__new, container, false);
        AllocateMemory(v);

        api = ApiClientcusome.getClient().create(ApiInterface.class);
        setHasOptionsMenu(true);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            product_id = bundle.getString("product_id");
            product_name = bundle.getString("product_name");

            Log.e("product_id", "" + product_id);
            if (CheckNetwork.isNetworkAvailable(getActivity())) {

                if(Login_preference.getLogin_flag(getActivity()).equalsIgnoreCase("1"))
                {
                    callWishistApi();
                }

                Productdetailsapiiiii(product_id);
               // Call_Product_detailsapi(product_id);

            } else {
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.intcon), Toast.LENGTH_SHORT).show();
            }
        }

        ((NavigationActivity) getActivity()).setSupportActionBar(toolbar_product_detail);
        ((NavigationActivity) getActivity()).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
        ((NavigationActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_36dp);
        toolbar_product_detail.setTitleTextColor(getActivity().getResources().getColor(R.color.black));
        tv_product_detail.setText(product_name);

        lv_pdetail_addtocart.setOnClickListener(this);
        lv_pdetail_addtowishlist.setOnClickListener(this);
        return v;
    }



    private void Productdetailsapiiiii(String product_id) {
        lv_productdetails_progress.setVisibility(View.VISIBLE);
        nested_scroll_detail.setVisibility(View.GONE);
        callproductdetailsgetapi(product_id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response",""+response.body());
                Log.e("response",""+response.body().toString());
                Log.e("response",""+response);

                if(response.isSuccessful() || response.code()==200)
                {
                    // Log.e("status",""+response.body().getItems());
                    lv_productdetails_progress.setVisibility(View.GONE);
                    nested_scroll_detail.setVisibility(View.VISIBLE);

                    for(int i=0;i<wishlitProductidList.size();i++)
                    {
                        Log.e("pos111111111", "=" + wishlitProductidList.get(i));
                        if(product_id.equalsIgnoreCase(String.valueOf(wishlitProductidList.get(i))))
                        {
                            iswishlist=true;
                            tv_pdetail_wishlist.setText(getActivity().getResources().getString(R.string.removeformwishlist));

                            Log.e("debug_152", "=" + wishlitItemId.size());
                            Log.e("product_id22", "=" + product_id);
                            Log.e("debug_1522222222222", "=" + wishlitItemId.get(i));
                            Log.e("debug_ii", "=" + i);
                            wishlist_item_id= String.valueOf( wishlitItemId.get(i));
                       /*     if(wishlitItemId.size()>0)
                            {
                                Log.e("wishlist_item_id_3317", "=" + wishlist_item_id);

                                wishlist_item_id= String.valueOf(wishlitItemId.get(pos));
                            }*/
                        }else {
                            Log.e("prod_163elase", "=" + product_id);

                          //  wishlist_item_id="0";
                          //  iswishlist=false;
                          //  tv_pdetail_wishlist.setText(getActivity().getResources().getString(R.string.addtowishlist));

                        }
                    }




                    if(iswishlist==false && wishlist_item_id.equalsIgnoreCase("0"))
                    {
                        tv_pdetail_wishlist.setText(getActivity().getResources().getString(R.string.addtowishlist));

                    }else {
                        tv_pdetail_wishlist.setText(getActivity().getResources().getString(R.string.removeformwishlist));
                    }



                    try {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        JSONArray itemarray=jsonObject.getJSONArray("items");

                        JSONObject itemarrayJSONObject = itemarray.getJSONObject(0);
                        //  tv_daily_special_product_name.setText(itemarray.get(0).getName());
                        tv_daily_special_product_name.setText(itemarrayJSONObject.getString("name"));
                        tv_rifles_lv_price.setText(itemarrayJSONObject.optString("price") + " "+Login_preference.getcurrencycode(getActivity()));
                        price= Double.parseDouble(itemarrayJSONObject.getString("price"));
                        tv_rifles_Specialprice.setText(" ");
                        sku=itemarrayJSONObject.getString("sku");
                        JSONArray attributearry = itemarrayJSONObject.getJSONArray("custom_attributes");
                        for(int i=0;i<attributearry.length();i++)
                        {
                            JSONObject attributeobject = attributearry.getJSONObject(i);
                            if(attributeobject.getString("attribute_code").equals("description"))
                            {
                                String value= String.valueOf(Html.fromHtml(String.valueOf(attributeobject.getString("value"))));
                                tv_description.setText(value);
                            }
                            if(attributeobject.getString("attribute_code").equals("short_description")){
                                String value= String.valueOf(Html.fromHtml(String.valueOf(attributeobject.getString("value"))));
                                tv_shortdescription.setText(value);
                            }
                            if(attributeobject.getString("attribute_code").equals("special_price")){
                                String value= String.valueOf(Html.fromHtml(String.valueOf(attributeobject.getString("value"))));
                                tv_rifles_Specialprice.setText(value +" "+Login_preference.getcurrencycode(getActivity()));
                                tv_rifles_Specialprice.setVisibility(View.VISIBLE);
                                tv_rifles_Specialprice_title.setVisibility(View.VISIBLE);
                                special_price= Double.parseDouble(attributeobject.getString("value"));
                            }else {
                                // tv_rifles_Specialprice.setVisibility(View.GONE);
                                // tv_rifles_Specialprice_title.setVisibility(View.GONE);
                            }
                        }

                        if(tv_rifles_Specialprice.getText().equals(" "))
                        {

                        }else {
                            tv_rifles_lv_price.setPaintFlags(tv_rifles_lv_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                           // tv_rifles_lv_price_title.setPaintFlags(tv_rifles_lv_price_title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        }
                        //gallary
                        JSONArray mediaarry=itemarrayJSONObject.getJSONArray("media_gallery_entries");
                        for (int i=0;i<mediaarry.length();i++)
                        {
                            try {
                                JSONObject object = mediaarry.getJSONObject(i);
                                medialist.add(new Medium(object.getString("file")));
                            } catch (Exception e) {
                                Log.e("Exception", "" + e);
                            } finally {
                                pDetailImageSliderAdapter.notifyItemChanged(i);
                            }
                        }



                        //tire array---------------------------------////////////////////

                        JSONArray tier_pricesarray=itemarrayJSONObject.getJSONArray("tier_prices");

                        if(tier_pricesarray.length()==0 || tier_pricesarray.isNull(0))
                        {
                            lv_tier_price.setVisibility(View.GONE);
                        }else {
                            lv_tier_price.setVisibility(View.VISIBLE);
                            for (int i=0;i<tier_pricesarray.length();i++)
                            {
                                try {
                                    JSONObject tierobject = tier_pricesarray.getJSONObject(i);
                                    Log.e("tierobject", "=" + tierobject);
                                    Log.e("tierobject", "=" + tierobject.optString("value"));
                                    Log.e("tierobject", "=" + tierobject.optString("qty"));
                                    Log.e("tierobject", "=" + tierobject);
                                    tierprice= Double.parseDouble(tierobject.optString("value"));
                                     getDiscount(special_price,tierprice,price);

                                    if(getDiscount(special_price,tierprice,price).equalsIgnoreCase("0"))
                                    {

                                        lv_tier_price.setVisibility(View.GONE);
                                    }else {
                                        lv_tier_price.setVisibility(View.VISIBLE);
                                        tierPriceModelList.add(new TierPriceModel(
                                                tierobject.optString("qty"),
                                                tierobject.optString("customer_group_id"),
                                                tierobject.optString("value"),
                                                getDiscount(special_price,tierprice,price)));

                                    }

                                } catch (Exception e) {
                                    Log.e("Exception", "" + e);
                                } finally {
                                    tierPriceAdapter.notifyItemChanged(i);
                                }
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {

                }


            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                lv_productdetails_progress.setVisibility(View.GONE);
                Log.e("emptyyyyfdfgfdg","eeeeeeeeeee"+t.getMessage());
            }
        });
    }

    private String getDiscount(double special_price, double tierprice, double price) {
        double total = 0,discount;
        Log.e("special_price","=="+special_price);
        Log.e("price","=="+price);

        if(special_price==0.0)
        {
            total=(tierprice * 100) /price;
            discount =100-total;
        }else {
            total=(tierprice * 100) /special_price;
            discount =100-total;
        }

        Log.e("total","=="+total);
        Log.e("discount","=="+discount);
        Log.e("discountroundd","=="+Math.round(discount));
        return String.valueOf(Math.round(discount));

    }


    private Call<ResponseBody> callproductdetailsgetapi(String product_id) {
        Log.e("debug_11","=="+product_id);
        return api.productsdetail("Bearer "+Login_preference.gettoken(getActivity()),"entity_id","eq",product_id);
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


                                } catch (Exception e) {
                                    Log.e("exception22", "=" + e.getLocalizedMessage());
                                }

                            }


                            Log.e("lengthpp", "=" + wishlitProductidList.get(0));
                            Log.e("length", "=" + wishlitItemId.get(0));
                            Log.e("size", "=" + wishlitItemId.size());

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
                Toast.makeText(getActivity(), "" + getActivity().getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private Call<ResponseBody> getwishlistdata() {
        ApiInterface api = ApiClientcusome.getClient().create(ApiInterface.class);
        Log.e("debug_11token22","=="+ Login_preference.getCustomertoken(getActivity()));
        return api.defaultgetWishlistData("Bearer "+Login_preference.getCustomertoken(getActivity()));
    }

    private void AllocateMemory(View v) {
        lv_tier_price =v.findViewById(R.id.lv_tier_price);
        recv_tier_price =v.findViewById(R.id.recv_tier_price);
        tv_pdetail_wishlist =v.findViewById(R.id.tv_pdetail_wishlist);
        lv_pdetail_addtowishlist =v.findViewById(R.id.lv_pdetail_addtowishlist);
        lv_pdetail_addtocart =v.findViewById(R.id.lv_pdetail_addtocart);
        tv_rifles_lv_price_title =v.findViewById(R.id.tv_rifles_lv_price_title);
        tv_rifles_Specialprice_title =v.findViewById(R.id.tv_rifles_Specialprice_title);
        tv_product_detail =v.findViewById(R.id.tv_product_detail);
        nested_scroll_detail =v.findViewById(R.id.nested_scroll_detail);
        iv_defolticon =v.findViewById(R.id.iv_defolticon);
        tv_description =v.findViewById(R.id.tv_description);
        tv_shortdescription =v.findViewById(R.id.tv_shortdescription);
        toolbar_product_detail =v.findViewById(R.id.toolbar_product_detail);
        recycler_product_image =v.findViewById(R.id.recycler_product_image);
        tv_daily_special_product_name =v.findViewById(R.id.tv_daily_special_product_name);
        lv_productdetails_progress =v.findViewById(R.id.lv_productdetails_progress);
        recyclerIndicator =v. findViewById(R.id.indicator_pdetail);
        tv_rifles_lv_price = v.findViewById(R.id.tv_rifles_lv_price);
        tv_rifles_Specialprice = v.findViewById(R.id.tv_rifles_Specialprice);

        pDetailImageSliderAdapter = new PDetailImageSliderAdapter(getContext(),medialist);
        recycler_product_image.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recycler_product_image.setAdapter(pDetailImageSliderAdapter);
        recyclerIndicator.attachToRecyclerView(recycler_product_image);
        SnapHelper snapHelper = new LinearSnapHelper(); // Or PagerSnapHelper
        snapHelper.attachToRecyclerView(recycler_product_image);


        //tierprice
        tierPriceAdapter = new TierPriceAdapter(getContext(),tierPriceModelList);
        recv_tier_price.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recv_tier_price.setAdapter(tierPriceAdapter);

    }

    @Override
    public void onClick(View v) {
        if(v==lv_pdetail_addtocart)
        {if (CheckNetwork.isNetworkAvailable(getActivity())) {

            if(Login_preference.getLogin_flag(getActivity()).equalsIgnoreCase("1"))
            {
                CallAddtoCartApi();
            }else {
                Bundle b = new Bundle();
                // b.putString("screen", "" + screen);
                b.putString("subcat_id", "" + subcat_id);
                b.putString("subcatename", "" + subcatename);
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                LoginFragment myFragment = new LoginFragment();
                myFragment.setArguments(b);
                activity.getSupportFragmentManager()
                        .beginTransaction().setCustomAnimations
                        (R.anim.fade_in,
                                0, 0, R.anim.fade_out).replace(R.id.framlayout, myFragment).addToBackStack(null).commit();

            }

        } else {
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.intcon), Toast.LENGTH_SHORT).show();
        }

        }else if(v==lv_pdetail_addtowishlist)
        {
            if(iswishlist==true)
            {
                //remove wishlist
                Log.e("wish_remove_id", "=" + wishlist_item_id);
                if (Login_preference.getLogin_flag(getActivity()).equalsIgnoreCase("1")) {
                    if (CheckNetwork.isNetworkAvailable(getActivity())) {
                        callRemoveFromWishlistApi(wishlist_item_id);
                    } else {
                        Toast.makeText(getActivity(), getActivity().getString(R.string.internet), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    LoginFragment myFragment = new LoginFragment();
                    getFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                            0, 0, R.anim.fade_out).setCustomAnimations(R.anim.fade_in,
                            0, 0, R.anim.fade_out).replace(R.id.framlayout, myFragment).addToBackStack(null).commit();
                }
            }else if(iswishlist==false)
            {
                //add wishlsit
                Log.e("wish_remove_add", "=" + wishlist_item_id);

                if (Login_preference.getLogin_flag(getActivity()).equalsIgnoreCase("1")) {
                    if (CheckNetwork.isNetworkAvailable(getActivity())) {
                        CallAddtoWishlistApi_list(product_id);
                    } else {
                        Toast.makeText(getActivity(), getActivity().getString(R.string.internet), Toast.LENGTH_SHORT).show();
                    }
                } else {

                    LoginFragment myFragment = new LoginFragment();
                    getFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                            0, 0, R.anim.fade_out).setCustomAnimations(R.anim.fade_in,
                            0, 0, R.anim.fade_out).replace(R.id.framlayout, myFragment).addToBackStack(null).commit();
                }

            }
        }

    }
    private void CallAddtoWishlistApi_list( final String prod_id) {

        lv_productdetails_progress.setVisibility(View.GONE);
        nested_scroll_detail.setVisibility(View.VISIBLE);

        calladdtowishnew(prod_id).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                Log.e("debug_166", "" + response);
                Log.e("debug_167", "" + response.body());
                Boolean addToWishlist = response.body();
                //  Log.e("response_168",""+addToWishlist);
                //   Log.e("status_wish",""+addToWishlist.getStatus());
                Log.e("status_wish", "ok");
                if (response.isSuccessful() || response.code() == 200) {

                    if (response.body() == true) {

                        //itemList.get(position).setIsWishlisted("true");
                        iswishlist=true;
                        tv_pdetail_wishlist.setText(getActivity().getResources().getString(R.string.removeformwishlist));

                        callWishlistCountApi();

                                Bundle b = new Bundle();
                                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                                b.putString("product_id",prod_id);
                                b.putString("product_name",product_name);
                                //   b.putString("wishlist", item.getWishlist());
                                NewProductDetail_Fragment myFragment = new NewProductDetail_Fragment();
                                myFragment.setArguments(b);
                                activity.getSupportFragmentManager()
                                        .beginTransaction().setCustomAnimations(R.anim.fade_in,
                                        0, 0, R.anim.fade_out)
                                        .setCustomAnimations(R.anim.fade_in,
                                                0, 0, R.anim.fade_out)
                                       .replace(R.id.framlayout, myFragment)
                                        .commit();

                                nested_scroll_detail.setVisibility(View.VISIBLE);
                        lv_productdetails_progress.setVisibility(View.GONE);

                    } else {
                        nested_scroll_detail.setVisibility(View.VISIBLE);
                        lv_productdetails_progress.setVisibility(View.GONE);

                    }


                } else {
                    nested_scroll_detail.setVisibility(View.VISIBLE);
                    lv_productdetails_progress.setVisibility(View.GONE);

                    NavigationActivity.get_Customer_tokenapi();
                    CallAddtoWishlistApi_list( prod_id);

                    Log.e("debug_error", "=" + response);
                    Log.e("error", "=" + response.body());
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                nested_scroll_detail.setVisibility(View.VISIBLE);
                lv_productdetails_progress.setVisibility(View.GONE);

                // Log.e("error_wish",""+t);
                Log.e("debug_remivr", "" + t.getMessage());
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();

            }
        });

    }



    private Call<Boolean> calladdtowishnew(String itemid) {

        ApiInterface api = ApiClientcusome.getClient().create(ApiInterface.class);
        Log.e("debug_11111", "==" + itemid);
        Log.e("debug_11token", "==" + Login_preference.getCustomertoken(getActivity()));
        ///http://dkbraende.demoproject.info/rest//V1/carts/mine/items/162920

        String url = ApiClientcusome.MAIN_URLL + "wishlist/add/" + itemid;
        Log.e("url1111", "==" + url);
        return api.defaultaddtowishlist("Bearer " + Login_preference.getCustomertoken(getActivity()), url);
    }


    private void callRemoveFromWishlistApi(final String itemid) {
        nested_scroll_detail.setVisibility(View.GONE);
        lv_productdetails_progress.setVisibility(View.VISIBLE);
        callRemoveWishlistapi(itemid).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                Boolean paymentMehtodModel = response.body();

                Log.e("resaaaremove wishlist", "=" + response.code());
                Log.e("resaaacccc", "=" + response);

                if (response.code() == 200) {



                    tv_pdetail_wishlist.setText(getActivity().getResources().getString(R.string.addtowishlist));
                    iswishlist=false;
                    wishlist_item_id="0";
                    callWishlistCountApi();

                    nested_scroll_detail.setVisibility(View.VISIBLE);
                    lv_productdetails_progress.setVisibility(View.GONE);

                } else {
                    nested_scroll_detail.setVisibility(View.VISIBLE);
                    lv_productdetails_progress.setVisibility(View.GONE);
                    NavigationActivity.get_Customer_tokenapi();

                    // callRemoveFromWishlistApi(itemid,position,v,holder,itemList);
                }

            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                nested_scroll_detail.setVisibility(View.VISIBLE);
                lv_productdetails_progress.setVisibility(View.GONE);


                // String error=  t.printStackTrace();
                Toast.makeText(getActivity(), "" + getActivity().getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();
                Log.e("debug_175125", "pages: " + t);
            }
        });
    }


    private Call<Boolean> callRemoveWishlistapi(String itemid) {

        ApiInterface api = ApiClientcusome.getClient().create(ApiInterface.class);
        Log.e("debug_11", "==" + itemid);
        ///http://dkbraende.demoproject.info/rest//V1/carts/mine/items/162920


        String url = ApiClientcusome.MAIN_URLL + "wishlist/delete/" + itemid;
        Log.e("url11", "==" + url);
        return api.removeitemfromWishlistt("Bearer " + Login_preference.getCustomertoken(getActivity()), url);
    }


    private void CallAddtoCartApi() {
        lv_productdetails_progress.setVisibility(View.VISIBLE);
        nested_scroll_detail.setVisibility(View.GONE);
        String url=ApiClientcusome.MAIN_URLL+"carts/mine/items?cartItem[quoteId]="+Login_preference.getdkQuoteId(getActivity())+"&cartItem[qty]=1"+"&cartItem[sku]="+sku;
        Log.e("skuu","="+sku);
        Log.e("customertoken","="+Login_preference.getCustomertoken(getActivity()));
        Log.e("customertoken","="+url);
        Call<ResponseBody> addtocart = api.getaddtocartapi("Bearer "+Login_preference.getCustomertoken(getActivity()), url);
        addtocart.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("res_addtocart",""+response.toString());
                Log.e("resquotaddtocart",""+response.body());

                if(response.code()==200 || response.isSuccessful())
                {
                    JSONObject jsonObject = null;
                    try {
                        try {

                            lv_productdetails_progress.setVisibility(View.GONE);
                            nested_scroll_detail.setVisibility(View.VISIBLE);
                            jsonObject = new JSONObject(response.body().string());
                            String name=jsonObject.getString("name");
                            String price=jsonObject.getString("price");
                            String product_type=jsonObject.getString("product_type");
                            String quote_id=jsonObject.getString("quote_id");
                            String sku=jsonObject.getString("sku");
                            String item_id=jsonObject.getString("item_id");
                            String qty=jsonObject.getString("qty");

                            Toast.makeText(getActivity(), "Add to cart SuccessFully", Toast.LENGTH_SHORT).show( );
                            Log.e("jsonObject","="+jsonObject);
                            Log.e("name","="+name);
                            Log.e("price","="+price);
                            Log.e("product_type","="+product_type);
                            Log.e("quote_id","="+quote_id);
                            Log.e("sku","="+sku);
                            Log.e("item_id","="+item_id);
                            Log.e("qty","="+qty);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }else {
                    NavigationActivity.get_Customer_tokenapi();
                    CallAddtoCartApi();
                }




            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }


    //wishlist count
    private void callWishlistCountApi() {
        Log.e("response201tokenff", "" + Login_preference.getCustomertoken(getActivity()));
        ApiInterface api = ApiClientcusome.getClient().create(ApiInterface.class);
        Call<ResponseBody> customertoken = api.defaultWishlistCount("Bearer " + Login_preference.getCustomertoken(getActivity()));
        customertoken.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response200gffgdf", "" + response.toString());
                Log.e("response201fgd", "" + response.body());
                if (response.code() == 200 || response.isSuccessful()) {
                    try {
                        JSONArray jsonObject = new JSONArray(response.body().string());

                        String count = jsonObject.getJSONObject(0).getString("total_items");
                        tv_wishlist_count.setText(count);
                        if(getActivity()!=null)
                        {

                            Login_preference.set_wishlist_count(getActivity(), count);
                        }
                        Log.e("wishcount", "" + count);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_home, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
