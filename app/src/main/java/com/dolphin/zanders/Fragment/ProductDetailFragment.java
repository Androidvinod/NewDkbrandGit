package com.dolphin.zanders.Fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dolphin.zanders.Activity.NavigationActivity;
import com.dolphin.zanders.Adapter.AdditionalInfoAdapter;
import com.dolphin.zanders.Adapter.Filterlist_Adapter;
import com.dolphin.zanders.Adapter.PDetailImageSliderAdapter;

import com.dolphin.zanders.Adapter.TabPageAdapter;
import com.dolphin.zanders.Model.AddToCartModel.AddToCartModel;
import com.dolphin.zanders.Model.AddToWishlit.AddToWishlist;
import com.dolphin.zanders.Model.ProductDetailModel.Additional;
import com.dolphin.zanders.Model.ProductDetailModel.GetProductdetails;
import com.dolphin.zanders.Model.ProductDetailModel.Medium;
import com.dolphin.zanders.Model.RemoveWishlistModel.RemoveFromWishlistModel;
import com.dolphin.zanders.Model.Wishlistcheck_model.WishlistproductcheckModel;
import com.dolphin.zanders.Preference.Login_preference;
import com.dolphin.zanders.R;
import com.dolphin.zanders.Retrofit.ApiClient;
import com.dolphin.zanders.Retrofit.ApiInterface;
import com.dolphin.zanders.Util.CheckNetwork;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator;

import static com.dolphin.zanders.Activity.NavigationActivity.tv_bottomcount;
import static com.dolphin.zanders.Activity.NavigationActivity.tv_wishlist_count;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductDetailFragment extends Fragment implements View.OnClickListener {

    View v;
    AppBarLayout appbar_prodcut_detail;
    Toolbar toolbar_product_detail;
    public static List<Additional> additionals = new ArrayList<Additional>();
    PDetailImageSliderAdapter pDetailImageSliderAdapter;
    AdditionalInfoAdapter additionalInfoAdapter;
    RecyclerView recycler_product_image;
    TextView tv_daily_special_product_name,tv_pdetail_itemnumber,tv_pdetail_msrp,tv_pdetail_rs,
            tv_pdetail_warning,tv_pdetail_warningdetail,tv_addtocart,tv_pdetail_wishlist,tv_product_detail,tv_stock_status,tv_waringtext;
    LinearLayout lv_pdetail_addtocart,lv_pdetail_addtowishlist,lv_pdetail_instock,lv_productdetails_progress,lv_warring_popup;
    TabLayout tabLayout;
    ScrollingPagerIndicator recyclerIndicator;
    ViewPager viewPager;
    ApiInterface api;
    ImageView iv_defolticon;
    String Waring_sting,wishlist,stokecheck;
    public static String product_id ;
    MenuItem login;
    LinearLayout sliderDotspanel;
    TextView tv_rifles_lv_price_titlel,tv_rifles_lv_price,tv_rifles_Specialprice_titlel,tv_rifles_Specialprice,tv_rifles_map_titlel,tv_rifles_map,tv_availbleqty;
    LinearLayout lv_msrpl,lv_price,lv_Specialprice,lv_map,lv_pdetail_main,lv_ds_main_availbleqty;

    NavigationActivity parent;


    public static String quickOverView,features;
    public ProductDetailFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_product_detail, container, false);
        AllocateMemory(v);
        setupUI(lv_pdetail_main);
        api = ApiClient.getClient().create(ApiInterface.class);
        setHasOptionsMenu(true);
        Filterlist_Adapter.filter_child_value_list.clear();
        parent=(NavigationActivity) getActivity();
        ///  AttachRecyclerview();
        setTabLayout();
        hideKeyboard(parent);

        ((NavigationActivity) parent).setSupportActionBar(toolbar_product_detail);
       ((NavigationActivity) parent).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
        ((NavigationActivity) parent).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_36dp);
        toolbar_product_detail.setTitleTextColor(parent.getResources().getColor(R.color.black));
        tv_product_detail.setText("Product Detail");
        lv_pdetail_addtowishlist.setOnClickListener(this);
        lv_pdetail_addtocart.setOnClickListener(this);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            product_id = bundle.getString("product_id");
            wishlist = bundle.getString("wishlist");
            Log.e("cat_id", "" + product_id);
            if (CheckNetwork.isNetworkAvailable(parent)) {
                Productdetailsapi(product_id);
                Productcheckwishlist(product_id);
            } else {
                Toast.makeText(parent, parent.getResources().getString(R.string.intcon), Toast.LENGTH_SHORT).show();
            }
        }
        lv_warring_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("waring_121",""+Waring_sting);
                showCustomDialog(view,Waring_sting);
            }
        });
        return v;
    }
    private void Productcheckwishlist(String product_id) {
        callproductwishlistcheckapi(product_id).enqueue(new Callback<WishlistproductcheckModel>() {
            @Override
            public void onResponse(Call<WishlistproductcheckModel> call, Response<WishlistproductcheckModel> response) {
                Log.e("response",""+response.body());
                Log.e("status",""+response.body().getStatus());
                if (response.body().getStatus().equals("Success"))
                {
                    Log.e("show_data_wishlist",""+response.body().getIsWishlist());
                if (response.body().getIsWishlist().equals(true))
                {
                    tv_pdetail_wishlist.setText(parent.getResources().getString(R.string.removeformwishlist));
                }else {
                    tv_pdetail_wishlist.setText(parent.getResources().getString(R.string.addtowishlist));
                }
                }
                else{
                   // Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<WishlistproductcheckModel> call, Throwable t) {
                Toast.makeText(parent, ""+t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Call<WishlistproductcheckModel> callproductwishlistcheckapi(String product_id) {
        Log.e("product_id",""+product_id);
        Log.e("Customer_id",""+Login_preference.getcustomer_id(parent));
        return api.getwishlistcheck(product_id,Login_preference.getcustomer_id(parent));
    }

    private void Productdetailsapi(String product_id) {
        lv_productdetails_progress.setVisibility(View.VISIBLE);
        callproductdetailsgetapi(product_id).enqueue(new Callback<GetProductdetails>() {
            @Override
            public void onResponse(Call<GetProductdetails> call, Response<GetProductdetails> response) {
                Log.e("response",""+response.body());
                Log.e("status",""+response.body().getStatus());
                if (response.body().getStatus().equals("Success")){
                    lv_productdetails_progress.setVisibility(View.GONE);
                    Log.e("productname_121",""+response.body().getProduct().getProductName());
                    tv_daily_special_product_name.setText(response.body().getProduct().getProductName());
                    tv_pdetail_itemnumber.setText("ITEM NUMBER: "+response.body().getProduct().getItemNumber());
                  //  tv_pdetail_rs.setText(response.body().getProduct().getMsrp());
                    tv_stock_status.setText(response.body().getProduct().getAvailability());
                    if(parent!=null)
                    {
                        if(Login_preference.getLogin_flag(parent).equalsIgnoreCase("1"))
                        {

                            lv_ds_main_availbleqty.setVisibility(View.VISIBLE);
                            tv_availbleqty.setText(" "+response.body().getProduct().getAvailable_qty());
                            if(response.body().getProduct().getMsrp().equals("null")|| response.body().getProduct().getMsrp().equals("")|| response.body().getProduct().getMsrp().equals("$0.00")){
                                lv_msrpl.setVisibility(View.GONE);
                            }else {
                                lv_msrpl.setVisibility(View.VISIBLE);
                                tv_pdetail_rs.setText(response.body().getProduct().getMsrp());
                            }
                            if(response.body().getProduct().getProduct_price().equals("null")|| response.body().getProduct().getProduct_price().equals("")|| response.body().getProduct().getProduct_price().equals("$0.00")){
                                lv_price.setVisibility(View.GONE);
                            }else {
                                lv_price.setVisibility(View.VISIBLE);
                                tv_rifles_lv_price.setText(response.body().getProduct().getProduct_price());
                            }
                            if(response.body().getProduct().getProduct_specialprice().equals("null")|| response.body().getProduct().getProduct_specialprice().equals("")||response.body().getProduct().getProduct_specialprice().equals("$0.00")){
                                lv_Specialprice.setVisibility(View.GONE);
                            }else {
                                tv_rifles_lv_price.setPaintFlags(tv_rifles_lv_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                lv_Specialprice.setVisibility(View.VISIBLE);
                                tv_rifles_Specialprice.setText(response.body().getProduct().getProduct_specialprice());
                            }
                            if(response.body().getProduct().getMap().equals("null")|| response.body().getProduct().getMap().equals("")||response.body().getProduct().getMap().equals("$0.00")){
                                lv_map.setVisibility(View.GONE);
                            }else {
                                lv_map.setVisibility(View.VISIBLE);
                                tv_rifles_map.setText(response.body().getProduct().getMap());
                            }
                            // NavigationActivity.Check_String_NULL_Value( gridholder.tv_rifles_msrp,productlistInnerData.getProductPrice());
                        }else {
                            //    Log.e("debug_else","dd="+Login_preference.getLogin_flag(context));
                            lv_ds_main_availbleqty.setVisibility(View.GONE);
                            lv_price.setVisibility(View.GONE);
                            lv_Specialprice.setVisibility(View.GONE);
                            lv_map.setVisibility(View.GONE);
                            lv_msrpl.setVisibility(View.VISIBLE);
                            tv_pdetail_rs.setText(response.body().getProduct().getMsrp());

                        }

                    }


                    Waring_sting=response.body().getProduct().getWarning();
                    if (Waring_sting.equals("")|| Waring_sting.equals("null")||Waring_sting.equals(null)){
                        lv_warring_popup.setVisibility(View.GONE);
                    }else {
                        lv_warring_popup.setVisibility(View.VISIBLE);
                    }
                    stokecheck=response.body().getProduct().getAvailability();
                    if (response.body().getProduct().getAvailability().equals("Out of stock")){
                        lv_pdetail_instock.setBackgroundResource(R.drawable.rounded_layout_red);
                        lv_pdetail_addtocart.setAlpha((float) 0.3);
                        lv_pdetail_addtocart.setEnabled(false);
                    }else {
                        lv_pdetail_addtocart.setEnabled(true);
                        lv_pdetail_instock.setBackgroundResource(R.drawable.rounded_layout_blue);
                    }

                    Log.e("debug_272","="+response.body().getProduct().getQuickOverview());
                    Log.e("featuress_271","="+response.body().getProduct().getFeatures());
                    if (response.body().getProduct().getQuickOverview()!=null){
                        quickOverView= String.valueOf(Html.fromHtml(response.body().getProduct().getQuickOverview()));
                        QuickOverviewFragment.tv_quickoverview.setText(response.body().getProduct().getQuickOverview());
                    }else {
                        quickOverView="";
                    }
                    if (response.body().getProduct().getFeatures()!=null){

                        features= String.valueOf(Html.fromHtml(response.body().getProduct().getFeatures()));
                        FeaturesFragment.tv_features.setText( Html.fromHtml(response.body().getProduct().getFeatures()));
                    }else {
                        features="";
                    }
                    Log.e("quickOverView_272","="+quickOverView);
                    Log.e("features_272","="+features);



                    additionals = response.body().getProduct().getAdditional();
                    if (additionals.isEmpty()) {
                        //  tv_no_addressfafound.setVisibility(View.VISIBLE);
                    }else {
                        //  tv_no_addressfafound.setVisibility(View.GONE);
                        additionals = response.body().getProduct().getAdditional();
                        Log.e("array_size_146",""+additionals.size());

                    }
                    List<Medium> media = response.body().getProduct().getMedia();
                    Log.e("array_result",""+media.size());
                    if (media.size()==0) {
                        recycler_product_image.setVisibility(View.GONE);
                        iv_defolticon.setVisibility(View.VISIBLE);
                        Log.e("emptyyyy","eeeeeeeeeee");
                        Log.e("image_url",""+response.body().getProduct().getProductThumbnail());
                        final RequestOptions requestOptions1 = new RequestOptions();
                        requestOptions1.placeholder(R.drawable.logo_red);
                        requestOptions1.error(R.drawable.logo_red);

                        if(parent!=null)
                        {
                            Glide.with(parent)
                                    .setDefaultRequestOptions(requestOptions1)
                                    .load(response.body().getProduct().getProductThumbnail()).into(iv_defolticon);
                        }

                      //  tv_no_addressfafound.setVisibility(View.VISIBLE);
                    }else {
                        recycler_product_image.setVisibility(View.VISIBLE);
                        iv_defolticon.setVisibility(View.GONE);
                      //  tv_no_addressfafound.setVisibility(View.GONE);
                        List<Medium> media1 =fetchResults(response);
                        Log.e("array_size_146",""+media1.size());
//                        pDetailImageSliderAdapter.addAll(media1);

                    }
                }else {
                    lv_productdetails_progress.setVisibility(View.GONE);
                  //  tv_no_addressfafound.setVisibility(View.VISIBLE);
                }

            }
            @Override
            public void onFailure(Call<GetProductdetails> call, Throwable t) {
                Toast.makeText(parent, ""+t, Toast.LENGTH_SHORT).show();
                lv_productdetails_progress.setVisibility(View.GONE);
            }
        });
    }

    private List<Medium> fetchResults(Response<GetProductdetails> response) {
        GetProductdetails getProductdetails = response.body();
        return getProductdetails.getProduct().getMedia();
    }
    private Call<GetProductdetails> callproductdetailsgetapi(String product_id) {
        return api.getproductdetails(product_id);
    }
    private void showCustomDialog(View view,String Waring_sting) {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup =view.findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(parent).inflate(R.layout.waring_popup, viewGroup, false);
        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(parent);
        tv_waringtext=dialogView.findViewById(R.id.tv_waringtext);
        ImageView iv_close=dialogView.findViewById(R.id.iv_close);
        tv_waringtext.setText(Waring_sting);
        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);
        //finally creating the alert dialog and displaying it
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
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

    private void setTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText(parent.getResources().getString(R.string.overview)));
        tabLayout.addTab(tabLayout.newTab().setText(parent.getResources().getString(R.string.features)));
        tabLayout.addTab(tabLayout.newTab().setText(parent.getResources().getString(R.string.info)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final TabPageAdapter adapter = new TabPageAdapter(getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            //noinspection ConstantConditions
            TextView tv = (TextView)LayoutInflater.from(parent).inflate(R.layout.custom_tab,null);
            tv.setTypeface(NavigationActivity.montserratbold);
          //  tv.setTextAppearance((int) getResources().getDimension(R.dimen.text_14));
                tabLayout.getTabAt(i).setCustomView(tv);
        }
        tabLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        tabLayout.setTabTextColors(Color.parseColor("#000000"), Color.parseColor("#000000"));

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

  /*  private void AttachRecyclerview() {
        pDetailImageSliderAdapter = new PDetailImageSliderAdapter(getContext());
        recycler_product_image.setLayoutManager(new LinearLayoutManager(parent, LinearLayoutManager.HORIZONTAL, false));
        recycler_product_image.setItemAnimator(new DefaultItemAnimator());
        recycler_product_image.setAdapter(pDetailImageSliderAdapter);
        recyclerIndicator =v. findViewById(R.id.indicator_pdetail);

    }*/

    private void AllocateMemory(View v) {

      /*  appbar_prodcut_detail =v.findViewById(R.id.appbar_prodcut_detail);
        toolbar_product_detail =v.findViewById(R.id.toolbar_product_detail);
      */

        lv_pdetail_main =v.findViewById(R.id.lv_pdetail_main);
        tv_product_detail =v.findViewById(R.id.tv_product_detail);
      toolbar_product_detail =v.findViewById(R.id.toolbar_product_detail);
        recycler_product_image =v.findViewById(R.id.recycler_product_image);
        tv_daily_special_product_name =v.findViewById(R.id.tv_daily_special_product_name);
        tv_pdetail_itemnumber =v.findViewById(R.id.tv_pdetail_itemnumber);
        tv_pdetail_msrp =v.findViewById(R.id.tv_pdetail_msrp);
        tv_pdetail_rs =v.findViewById(R.id.tv_pdetail_rs);
        tv_pdetail_warning =v.findViewById(R.id.tv_pdetail_warning);
        tv_pdetail_warningdetail =v.findViewById(R.id.tv_pdetail_warningdetail);
        tv_addtocart =v.findViewById(R.id.tv_addtocart);
        tv_pdetail_wishlist =v.findViewById(R.id.tv_pdetail_wishlist);
        tv_stock_status =v.findViewById(R.id.tv_stock_status);
        lv_pdetail_addtocart =v.findViewById(R.id.lv_pdetail_addtocart);
        lv_pdetail_addtowishlist =v.findViewById(R.id.lv_pdetail_addtowishlist);
        lv_pdetail_instock =v.findViewById(R.id.lv_pdetail_instock);
        lv_productdetails_progress =v.findViewById(R.id.lv_productdetails_progress);
        lv_warring_popup =v.findViewById(R.id.lv_warring_popup);
        iv_defolticon =v.findViewById(R.id.iv_defolticon);
        tabLayout=(TabLayout) v.findViewById(R.id.tablayout);
        viewPager=(ViewPager) v.findViewById(R.id.viewpager);
     ///   recyclerIndicator =v. findViewById(R.id.indicator_pdetail);
        lv_ds_main_availbleqty =v. findViewById(R.id.lv_ds_main_availbleqty);
        tv_availbleqty =v. findViewById(R.id.tv_availbleqty);

        lv_msrpl = v.findViewById(R.id.lv_msrpl);
        lv_price = v.findViewById(R.id.lv_price);
        lv_Specialprice = v.findViewById(R.id.lv_Specialprice);
        lv_map = v.findViewById(R.id.lv_map);

        tv_rifles_lv_price_titlel = v.findViewById(R.id.tv_rifles_lv_price_titlel);
        tv_rifles_lv_price = v.findViewById(R.id.tv_rifles_lv_price);
        tv_rifles_Specialprice_titlel = v.findViewById(R.id.tv_rifles_Specialprice_titlel);
        tv_rifles_Specialprice = v.findViewById(R.id.tv_rifles_Specialprice);
        tv_rifles_map_titlel = v.findViewById(R.id.tv_rifles_map_titlel);
        tv_rifles_map = v.findViewById(R.id.tv_rifles_map);


       // pDetailImageSliderAdapter = new PDetailImageSliderAdapter(getContext());
        recycler_product_image.setLayoutManager(new LinearLayoutManager(parent, LinearLayoutManager.HORIZONTAL, false));
        recycler_product_image.setAdapter(pDetailImageSliderAdapter);
      //  recyclerIndicator.attachToRecyclerView(recycler_product_image);
        SnapHelper snapHelper = new LinearSnapHelper(); // Or PagerSnapHelper
        snapHelper.attachToRecyclerView(recycler_product_image);

    }

    @Override
    public void onClick(View view) {
        if (view == lv_pdetail_addtowishlist){

            Log.e("wishlist_data_474",""+tv_pdetail_wishlist.getText().toString());
            String wishlflag=tv_pdetail_wishlist.getText().toString();


            if (Login_preference.getLogin_flag(parent).matches("1")){
                if (wishlflag.equals("Remove from Wishlist")){
                    if (CheckNetwork.isNetworkAvailable(parent)) {
                        CallRemoveWishlistApi(product_id);
                    } else {
                        Toast.makeText(parent, parent.getString(R.string.nointernet), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    if (CheckNetwork.isNetworkAvailable(parent)) {
                        CallAddtoWishlistApi(product_id);
                    } else {
                        Toast.makeText(parent, parent.getString(R.string.internet), Toast.LENGTH_SHORT).show();
                    }
                }
                Log.e("wishlist_374",""+wishlist);

            }else {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        String screen = "productDetails";
                        Bundle b = new Bundle();
                        b.putString("screen", "" + screen);
                        b.putString("product_id", "" + product_id);
                        AppCompatActivity activity = (AppCompatActivity) parent;
                        Fragment myFragment = new LoginFragment();
                        myFragment.setArguments(b);
                        activity.getSupportFragmentManager()
                                .beginTransaction().setCustomAnimations
                                (R.anim.fade_in,
                                        0, 0, R.anim.fade_out).replace(R.id.framlayout, myFragment).addToBackStack(null).commit();
                    }
                }, 50);
            }
        }else if (view == lv_pdetail_addtocart){

            if (Login_preference.getLogin_flag(parent).matches("1")){
                    if (CheckNetwork.isNetworkAvailable(parent)) {
                        callAddtoCartApi(product_id);
                    } else {
                        Toast.makeText(parent, parent.getString(R.string.internet), Toast.LENGTH_SHORT).show();
                    }

            }else {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        String screen = "productDetails";
                        Bundle b = new Bundle();
                        b.putString("screen", "" + screen);
                        b.putString("product_id", "" + product_id);
                        AppCompatActivity activity = (AppCompatActivity) parent;
                        Fragment myFragment = new LoginFragment();
                        myFragment.setArguments(b);
                        activity.getSupportFragmentManager()
                                .beginTransaction().setCustomAnimations
                                (R.anim.fade_in,
                                        0, 0, R.anim.fade_out).replace(R.id.framlayout, myFragment).addToBackStack(null).commit();
                    }
                }, 50);
            }
        }
    }

    private void callAddtoCartApi(String product_id) {
        lv_productdetails_progress.setVisibility(View.VISIBLE);
        addtocart(product_id).enqueue(new Callback<AddToCartModel>() {
            @Override
            public void onResponse(Call<AddToCartModel> call, Response<AddToCartModel> response) {
                lv_productdetails_progress.setVisibility(View.GONE);
                AddToCartModel model=response.body();
                Log.e("response_2333", "" +response.body());
                Log.e("response33", "" + response);
                Log.e("debug_258messs", "" + model.getMessage());
                if(model.getStatus().equalsIgnoreCase("Success"))
                {
                    Toast.makeText(parent, "" + model.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("quote_iddddd_217", "" + model.getQuoteId());
                    Log.e("item_qty2466",""+model.getItemsCount());
                    Log.e("item_qty2466total",""+model.getGrandTotal());
                    Login_preference.setquote_id(parent,  model.getQuoteId());
                    Login_preference.setCart_item_count(parent, model.getItemsCount());
                    String item_count = String.valueOf(model.getItemsCount());
                    Log.e("item_qty",""+item_count);

                    if (item_count.equalsIgnoreCase("null") || item_count.equals("")) {
                        Log.e("count_40", "" + String.valueOf(model.getItemsCount()));
                        tv_bottomcount.setVisibility(View.GONE);
                    } else {
                        tv_bottomcount.setVisibility(View.VISIBLE);
                        Log.e("count_80", "" + String.valueOf(model.getItemsCount()));
                        NavigationActivity.Check_String_NULL_Value(tv_bottomcount, String.valueOf(model.getItemsCount()));
                    }
                }else {
                    Log.e("debug_244else",""+model.getMessage());
                    Toast.makeText(parent, "" + model.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<AddToCartModel> call, Throwable t) {
                lv_productdetails_progress.setVisibility(View.GONE);
                Toast.makeText(parent, parent.getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Call<AddToCartModel> addtocart(String product_id) {
        Log.e("emaikl_262", " " +  Login_preference.getemail(parent));
        Log.e("emaikl_262product_id", " " + product_id);
        return api.addToCart(Login_preference.getemail(parent),product_id);
    }

    private void CallRemoveWishlistApi(String product_id) {
        lv_productdetails_progress.setVisibility(View.VISIBLE);
            callremovewishlistApi(product_id).enqueue(new Callback<RemoveFromWishlistModel>() {
                @Override
                public void onResponse(Call<RemoveFromWishlistModel> call, Response<RemoveFromWishlistModel> response) {
                    RemoveFromWishlistModel addToWishlist = response.body();
                    lv_productdetails_progress.setVisibility(View.GONE);
                    if (addToWishlist.getStatus().equalsIgnoreCase("Success")) {
                     //   wishlist="0";
                        tv_pdetail_wishlist.setText(parent.getResources().getString(R.string.addtowishlist));
                        Toast.makeText(parent, addToWishlist.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("remove_wishlist", "" + addToWishlist.getCount());
                        String item_count = null;
                        if(addToWishlist.getCount()==0 || addToWishlist.getCount().equals("0.00"))
                        {
                            Login_preference.set_wishlist_count(parent,"");
                            //   item_count = String.valueOf(removeproductmodel.getItemsCount());
                        }else {
                            Login_preference.set_wishlist_count(parent,String.valueOf(addToWishlist.getCount()));
                            item_count = String.valueOf(addToWishlist.getCount());
                        }

                        if (item_count == null || item_count.equalsIgnoreCase("null") || item_count.equals("")) {
                            Log.e("count_40", "" + String.valueOf(addToWishlist.getCount()));
                            tv_wishlist_count.setVisibility(View.GONE);
                        } else {
                            tv_wishlist_count.setVisibility(View.VISIBLE);
                            Log.e("count_80", "" + String.valueOf(addToWishlist.getCount()));
                            NavigationActivity.Check_String_NULL_Value(tv_wishlist_count, String.valueOf(addToWishlist.getCount()));
                        }
                    } else {
                        Toast.makeText(parent, addToWishlist.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<RemoveFromWishlistModel> call, Throwable t) {
                    lv_productdetails_progress.setVisibility(View.GONE);
                    Toast.makeText(parent,""+parent.getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();
                }
            });
    }

    private Call<RemoveFromWishlistModel> callremovewishlistApi(String prod_idpass) {
        Log.e("inner_user_wish197",""+Login_preference.getcustomer_id(parent));
        Log.e("inner_wish_prod_idpass",""+ prod_idpass);
        return api.removewishlist(Login_preference.getcustomer_id(parent),prod_idpass);
    }

    private void CallAddtoWishlistApi(String product_id) {
        lv_productdetails_progress.setVisibility(View.VISIBLE);
        calladdtowishlistApi(product_id).enqueue(new Callback<AddToWishlist>() {
            @Override
            public void onResponse(Call<AddToWishlist> call, Response<AddToWishlist> response) {
                Log.e("debug_167",""+response.body());
                AddToWishlist addToWishlist = response.body();
                lv_productdetails_progress.setVisibility(View.GONE);
                Log.e("status_wish",""+addToWishlist.getStatus());
                if (addToWishlist.getStatus().equalsIgnoreCase("Success")) {
                    //wishlist="1";
                    tv_pdetail_wishlist.setText(parent.getResources().getString(R.string.removeformwishlist));
                    Toast.makeText(parent, addToWishlist.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("remove_wishlist", "" + addToWishlist.getCount());
                    Login_preference.set_wishlist_count(parent, String.valueOf(addToWishlist.getCount()));
                    String item_count = String.valueOf(addToWishlist.getCount());
                    Log.e("remove_222", "" + item_count);
                    if (item_count == null || item_count.equalsIgnoreCase("null") || item_count.equals("")) {
                        Log.e("count_40", "" + String.valueOf(addToWishlist.getCount()));
                        tv_wishlist_count.setVisibility(View.GONE);
                    } else {
                        tv_wishlist_count.setVisibility(View.VISIBLE);
                        Log.e("count_80", "" + String.valueOf(addToWishlist.getCount()));
                        NavigationActivity.Check_String_NULL_Value(tv_wishlist_count, String.valueOf(addToWishlist.getCount()));
                    }
                } else {
                    Toast.makeText(parent, addToWishlist.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<AddToWishlist> call, Throwable t) {
                Toast.makeText(parent,""+parent.getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();                lv_productdetails_progress.setVisibility(View.GONE);
            }
        });
    }

    private Call<AddToWishlist> calladdtowishlistApi(String prod_idpass) {
        Log.e("inner_user_wish197",""+Login_preference.getcustomer_id(parent));
        Log.e("inner_wish_prod_idpass",""+ prod_idpass);

        return api.addtowishlist(Login_preference.getcustomer_id(parent),prod_idpass);
    }
}
