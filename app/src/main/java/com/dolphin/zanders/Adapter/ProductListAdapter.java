package com.dolphin.zanders.Adapter;


import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dolphin.zanders.Activity.NavigationActivity;
import com.dolphin.zanders.Fragment.LoginFragment;
import com.dolphin.zanders.Fragment.OfferFragment;
import com.dolphin.zanders.Fragment.ProductDetailFragment;
import com.dolphin.zanders.Fragment.ProductListFragment;
import com.dolphin.zanders.Fragment.SearchFragment;
import com.dolphin.zanders.Model.AddToCartModel.AddToCartModel;
import com.dolphin.zanders.Model.AddToWishlit.AddToWishlist;
import com.dolphin.zanders.Model.ProductlistModel.GetCategoryProductlistInnerData;
import com.dolphin.zanders.Model.RemoveWishlistModel.RemoveFromWishlistModel;
import com.dolphin.zanders.Preference.Login_preference;
import com.dolphin.zanders.R;
import com.dolphin.zanders.Retrofit.ApiClient;
import com.dolphin.zanders.Retrofit.ApiInterface;
import com.dolphin.zanders.Util.CheckNetwork;
import com.dolphin.zanders.Util.MyBounceInterpolator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dolphin.zanders.Activity.NavigationActivity.drawer;
import static com.dolphin.zanders.Activity.NavigationActivity.tv_bottomcount;
import static com.dolphin.zanders.Activity.NavigationActivity.tv_wishlist_count;
import static com.dolphin.zanders.Fragment.ProductListFragment.subcat_id;
import static com.dolphin.zanders.Fragment.ProductListFragment.subcatename;

public class ProductListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<GetCategoryProductlistInnerData> productlistdata;
    int choice = 0;
    public Context context;
    LayoutInflater inflater;
    static ApiInterface apiInterface;
    String screentype;

    public ProductListAdapter(Context context, List<GetCategoryProductlistInnerData> model, String screen) {
        this.context = context;
        this.productlistdata = model;
        this.screentype = screen;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        choice = ApiClient.VIEWTYPE;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        switch (choice) {
            case 0:
                View itemView = inflater.inflate(R.layout.product_row, parent, false);
                return new MyViewHolder(itemView);
            case 2:
                View listitem = inflater.inflate(R.layout.product_row_vertical, parent, false);
                return new MyNewHolder(listitem);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        final GetCategoryProductlistInnerData productlistInnerData = productlistdata.get(position);
        switch (ApiClient.VIEWTYPE) {
            case 2:
                //  Log.e("list", ApiClient.VIEWTYPE + "");
                final MyNewHolder listHolder = (MyNewHolder) holder;
                Log.e("debug_129", "adad" + productlistInnerData.getWishlist());

                if (productlistInnerData.getWishlist().equals("1")) {
                    listHolder.iv_product_remove_wishlist.setVisibility(View.VISIBLE);
                    listHolder.iv_product_add_wishlist.setVisibility(View.GONE);
                } else {
                    listHolder.iv_product_remove_wishlist.setVisibility(View.GONE);
                    listHolder.iv_product_add_wishlist.setVisibility(View.VISIBLE);
                }
                final RequestOptions requestOptions = new RequestOptions();
                requestOptions.placeholder(R.drawable.app_icon_title);
                requestOptions.error(R.drawable.app_icon_title);
                Glide.with(context)
                        .setDefaultRequestOptions(requestOptions)
                        .load(productlistInnerData.getProductThumbnail()).into(listHolder.iv_rifles);
                NavigationActivity.Check_String_NULL_Value(listHolder.tv_rifles_name, productlistInnerData.getProductName());
                if (Login_preference.getLogin_flag(context).equalsIgnoreCase("1")) {
                    listHolder.lv_ds_main_availbleqty.setVisibility(View.VISIBLE);
                    listHolder.tv_availbleqty.setText(" " + productlistInnerData.getAvailable_qty());
                    //listHolder.tv_product_msrp_value.setText(productlistInnerData.getMsrp());
                    if (productlistInnerData.getMsrp().equals("null") || productlistInnerData.getMsrp().equals("")
                            || productlistInnerData.getMsrp().equals("$0.00")) {
                        listHolder.lv_msrpl.setVisibility(View.GONE);
                    } else {
                        listHolder.lv_msrpl.setVisibility(View.VISIBLE);
                        Log.e("msrp_valuesss_list",""+productlistInnerData.getMsrp());
                        listHolder.tv_product_msrp_value.setText(productlistInnerData.getMsrp());
                    }
                    if (productlistInnerData.getProductPrice().equals("null") ||
                            productlistInnerData.getProductPrice().equals("") || productlistInnerData.getProductPrice().equals("$0.00")) {
                        listHolder.lv_pricel.setVisibility(View.GONE);
                    } else {
                        listHolder.lv_pricel.setVisibility(View.VISIBLE);
                        listHolder.tv_rifles_lv_pricel.setText(productlistInnerData.getProductPrice());
                    }
                    if (productlistInnerData.getProductSpecialprice().equals("null") ||
                            productlistInnerData.getProductSpecialprice().equals("") || productlistInnerData.getProductSpecialprice().equals("$0.00")) {
                        listHolder.lv_Specialpricel.setVisibility(View.GONE);
                    } else {
                        listHolder.tv_rifles_lv_pricel.setPaintFlags(listHolder.tv_rifles_lv_pricel.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        listHolder.lv_Specialpricel.setVisibility(View.VISIBLE);
                        listHolder.tv_rifles_Specialpricel.setText(productlistInnerData.getProductSpecialprice());
                    }
                    if (productlistInnerData.getMap().equals("null") || productlistInnerData.getMap().equals("")
                            || productlistInnerData.getMap().equals("$0.00")) {
                        listHolder.lv_mapl.setVisibility(View.GONE);
                    } else {
                        listHolder.lv_mapl.setVisibility(View.VISIBLE);
                        listHolder.tv_rifles_mapl.setText(productlistInnerData.getMap());
                    }
                    ///check out of stock in or instock
                    if (productlistInnerData.getAvailability().equalsIgnoreCase("Out Of Stock")) {
                        Log.e("debug_129", "adad" + productlistInnerData.getAvailability());
                        listHolder.lv_product_add_to_cart_verticle.setAlpha(0.4f);
                        listHolder.lv_product_add_to_cart_verticle.setEnabled(false);
                        listHolder.lv_Specialpricel.setVisibility(View.GONE);

                    } else {
                        listHolder.lv_product_add_to_cart_verticle.setEnabled(true);
                        if (productlistInnerData.getProductSpecialprice().equals("null") ||
                                productlistInnerData.getProductSpecialprice().equals("") || productlistInnerData.getProductSpecialprice().equals("$0.00")) {
                            listHolder.lv_Specialpricel.setVisibility(View.GONE);
                        } else {
                            listHolder.tv_rifles_lv_pricel.setPaintFlags(listHolder.tv_rifles_lv_pricel.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            listHolder.lv_Specialpricel.setVisibility(View.VISIBLE);
                            listHolder.tv_rifles_Specialpricel.setText(productlistInnerData.getProductSpecialprice());
                        }
                        // Log.e("debug_1222","adad"+productlistInnerData.getAvailability());
                    }
                    //  listHolder.tv_rifles_lv_price.setText();
                } else {
                    listHolder.lv_ds_main_availbleqty.setVisibility(View.GONE);
                    listHolder.lv_pricel.setVisibility(View.GONE);
                    listHolder.lv_Specialpricel.setVisibility(View.GONE);
                    listHolder.lv_mapl.setVisibility(View.GONE);
                    //  listHolder.lv_price.setVisibility(View.INVISIBLE);
                    //  listHolder.lv_Specialprice.setVisibility(View.INVISIBLE);
                    //listHolder.lv_map.setVisibility(View.INVISIBLE);
                    NavigationActivity.Check_String_NULL_Value(listHolder.tv_product_msrp_value, productlistInnerData.getMsrp());
                }

                //call add to cart api
                listHolder.lv_product_add_to_cart_verticle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Login_preference.getLogin_flag(context).matches("1")) {
                            String product_id = productlistInnerData.getProductId();
                            if (CheckNetwork.isNetworkAvailable(context)) {
                                //   Log.e("debug_144","productid="+product_id);
                                if (screentype.equalsIgnoreCase("offer")) {
                                    callAddtoCartApi(product_id, OfferFragment.coordinator_product_main, OfferFragment.lv_productlist_progress);
                                } else if (screentype.equalsIgnoreCase("product_list")) {
                                    //Search
                                    callAddtoCartApi(product_id, ProductListFragment.coordinator_product_main, ProductListFragment.lv_productlist_progress);
                                } else if (screentype.equalsIgnoreCase("Search")) {
                                    //Search
                              //      callAddtoCartApi(product_id, SearchFragment.coordinator_product_main, SearchFragment.lv_productlist_progress);
                                }
                            } else {
                                Toast.makeText(context, context.getString(R.string.internet), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            //    Log.e("debug_131","ss"+screentype);
                            String screen = screentype;
                            Bundle b = new Bundle();
                            b.putString("screen", "" + screen);
                            b.putString("subcat_id", "" + subcat_id);
                            b.putString("subcatename", "" + subcatename);
                            AppCompatActivity activity = (AppCompatActivity) v.getContext();
                            LoginFragment myFragment = new LoginFragment();
                            myFragment.setArguments(b);
                            activity.getSupportFragmentManager()
                                    .beginTransaction().setCustomAnimations(R.anim.fade_in,
                                    0, 0, R.anim.fade_out).addToBackStack(null).
                                    replace(R.id.framlayout, myFragment).commit();
                        }
                    }
                });
                listHolder.lv_main_product_row_verticle.setEnabled(true);
                listHolder.lv_main_product_row_verticle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {

                                listHolder.lv_main_product_row_verticle.setEnabled(false);
                                Bundle b = new Bundle();
                                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                                b.putString("product_id", productlistInnerData.getProductId());
                                b.putString("wishlist", productlistInnerData.getWishlist());
                                ProductDetailFragment myFragment = new ProductDetailFragment();
                                myFragment.setArguments(b);
                                activity.getSupportFragmentManager()
                                        .beginTransaction()
                                        .setCustomAnimations(R.anim.fade_in,
                                                0, 0, R.anim.fade_out)
                                        .setCustomAnimations(R.anim.fade_in,
                                                0, 0, R.anim.fade_out)
                                        .add(R.id.framlayout, myFragment)
                                        .addToBackStack(null).commit();
                                if (drawer.isDrawerOpen(GravityCompat.START)) {
                                    drawer.closeDrawer(GravityCompat.START);
                                }
                            }
                        }, 100);
                    }
                });

                listHolder.iv_product_add_wishlist.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        String prod_id = productlistInnerData.getProductId();
                        ///  Log.e("prod_idddd_wish",""+prod_id);

                        if (Login_preference.getLogin_flag(context).matches("1")) {
                            if (CheckNetwork.isNetworkAvailable(context)) {
                                CallAddtoWishlistApi_list(listHolder, prod_id, position);
                            } else {
                                Toast.makeText(context, context.getString(R.string.internet), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    Log.e("debug_131", "ss" + screentype);
                                    String screen = screentype;
                                    Bundle b = new Bundle();
                                    b.putString("screen", "" + screen);
                                    b.putString("subcat_id", "" + subcat_id);
                                    b.putString("subcatename", "" + subcatename);
                                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
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
                });

                listHolder.iv_product_remove_wishlist.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String prod_id = productlistInnerData.getProductId();

                        if (CheckNetwork.isNetworkAvailable(context)) {
                            CallRemoveWishlistApi_list(listHolder, prod_id, position);
                        } else {
                            Toast.makeText(context, context.getString(R.string.nointernet), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case 0:
                Log.e("grid", ApiClient.VIEWTYPE + "");
                final MyViewHolder gridholder = (MyViewHolder) holder;
                final RequestOptions requestOptions1 = new RequestOptions();
                requestOptions1.placeholder(R.drawable.logo_red);
                requestOptions1.error(R.drawable.logo_red);
                Log.e("isWishlist_inner", "" + productlistInnerData.getWishlist());
                if (productlistInnerData.getWishlist().equals("1")) {
                    gridholder.iv_rifles__remove_wishlist.setVisibility(View.VISIBLE);
                    gridholder.iv_rifles_add_wishlist.setVisibility(View.GONE);
                } else {
                    gridholder.iv_rifles__remove_wishlist.setVisibility(View.GONE);
                    gridholder.iv_rifles_add_wishlist.setVisibility(View.VISIBLE);
                }

                Glide.with(context)
                        .setDefaultRequestOptions(requestOptions1)
                        .load(productlistInnerData.getProductThumbnail()).into(gridholder.iv_rifles);

                NavigationActivity.Check_String_NULL_Value(gridholder.tv_rifles_name, productlistInnerData.getProductName());
                Log.e("debug_253", "dd=" + Login_preference.getLogin_flag(context));
                Log.e("specialprice_308", "dd=" + productlistInnerData.getProductSpecialprice());

                if (Login_preference.getLogin_flag(context).equalsIgnoreCase("1")) {
                    gridholder.lv_ds_main_availbleqty.setVisibility(View.VISIBLE);
                    gridholder.tv_availbleqty.setText(" " + productlistInnerData.getAvailable_qty());

                    //gridholder.tv_rifles_msrp.setText(productlistInnerData.getMsrp());
                    if (productlistInnerData.getMsrp().equals("null") ||
                            productlistInnerData.getMsrp().equals("") ||
                            productlistInnerData.getMsrp().equals("$0.00")) {
                        gridholder.lv_msrp.setVisibility(View.GONE);
                    } else {
                        gridholder.lv_msrp.setVisibility(View.VISIBLE);
                        Log.e("grid_msrp",""+productlistInnerData.getMsrp());
                        gridholder.tv_rifles_msrp.setText(productlistInnerData.getMsrp());
                    }

                    if (productlistInnerData.getProductPrice().equals("null") || productlistInnerData.getProductPrice().equals("") ||
                            productlistInnerData.getProductPrice().equals("$0.00")) {
                        gridholder.lv_price.setVisibility(View.GONE);
                    } else {
                        gridholder.lv_price.setVisibility(View.VISIBLE);
                        gridholder.tv_rifles_lv_price.setText(productlistInnerData.getProductPrice());
                    }
                    if (productlistInnerData.getProductSpecialprice().equals("")||productlistInnerData.getProductSpecialprice().equals("null") ||
                            productlistInnerData.getProductSpecialprice().equals("$0.00")) {
                        gridholder.lv_Specialprice.setVisibility(View.GONE);
                    } else {
                        gridholder.tv_rifles_lv_price.setPaintFlags(gridholder.tv_rifles_lv_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        gridholder.lv_Specialprice.setVisibility(View.VISIBLE);
                        gridholder.tv_rifles_Specialprice.setText(productlistInnerData.getProductSpecialprice());
                    }
                    if (productlistInnerData.getMap().equals("null") || productlistInnerData.getMap().equals("")
                            || productlistInnerData.getMap().equals("$0.00")) {
                        gridholder.lv_map.setVisibility(View.GONE);
                    } else {
                        gridholder.lv_map.setVisibility(View.VISIBLE);
                        gridholder.tv_rifles_map.setText(productlistInnerData.getMap());
                    }
                    ///check out of stock in or instock
                    if (productlistInnerData.getAvailability().equalsIgnoreCase("Out Of Stock")) {
                        //  Log.e("debug_129","adad"+productlistInnerData.getAvailability());
                        gridholder.lv_product_add_to_cart.setAlpha(0.4f);
                        gridholder.lv_product_add_to_cart.setEnabled(false);
                        gridholder.lv_Specialprice.setVisibility(View.GONE);
                    } else {
                        gridholder.lv_product_add_to_cart.setEnabled(true);
                        if (productlistInnerData.getProductSpecialprice().equals("")||productlistInnerData.getProductSpecialprice().equals("null") ||
                                productlistInnerData.getProductSpecialprice().equals("$0.00")) {
                            gridholder.lv_Specialprice.setVisibility(View.GONE);
                        } else {
                            gridholder.tv_rifles_lv_price.setPaintFlags(gridholder.tv_rifles_lv_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            gridholder.lv_Specialprice.setVisibility(View.VISIBLE);
                            gridholder.tv_rifles_Specialprice.setText(productlistInnerData.getProductSpecialprice());
                        }
                        //Log.e("debug_1222","adad"+productlistInnerData.getAvailability());
                    }
                    // NavigationActivity.Check_String_NULL_Value( gridholder.tv_rifles_msrp,productlistInnerData.getProductPrice());
                } else {
                    //    Log.e("debug_else","dd="+Login_preference.getLogin_flag(context));
                    gridholder.lv_ds_main_availbleqty.setVisibility(View.GONE);
                    gridholder.lv_price.setVisibility(View.INVISIBLE);
                    gridholder.lv_Specialprice.setVisibility(View.INVISIBLE);
                    gridholder.lv_map.setVisibility(View.INVISIBLE);
                    gridholder.tv_rifles_msrp_title.setText(context.getResources().getText(R.string.msrp));
                    NavigationActivity.Check_String_NULL_Value(gridholder.tv_rifles_msrp, productlistInnerData.getMsrp());

                }

                gridholder.lv_product_add_to_cart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Login_preference.getLogin_flag(context).matches("1")) {
                            String product_id = productlistInnerData.getProductId();
                            if (CheckNetwork.isNetworkAvailable(context)) {
                                //  Log.e("debug_144","productid="+product_id);
                                if (screentype.equalsIgnoreCase("offer")) {
                                    callAddtoCartApi(product_id, OfferFragment.coordinator_product_main, OfferFragment.lv_productlist_progress);
                                } else if (screentype.equalsIgnoreCase("product_list")) {
                                    //Search
                                    callAddtoCartApi(product_id, ProductListFragment.coordinator_product_main, ProductListFragment.lv_productlist_progress);
                                } else if (screentype.equalsIgnoreCase("Search")) {
                                    //Search
                                  //  callAddtoCartApi(product_id, SearchFragment.coordinator_product_main, SearchFragment.lv_productlist_progress);
                                }

                            } else {
                                Toast.makeText(context, context.getString(R.string.internet), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.e("debug_131", "ss" + screentype);
                            String screen = screentype;
                            Bundle b = new Bundle();
                            b.putString("screen", "" + screen);
                            b.putString("subcat_id", "" + subcat_id);
                            b.putString("subcatename", "" + subcatename);
                            AppCompatActivity activity = (AppCompatActivity) v.getContext();
                            LoginFragment myFragment = new LoginFragment();
                            myFragment.setArguments(b);
                            activity.getSupportFragmentManager()
                                    .beginTransaction().setCustomAnimations(R.anim.fade_in,
                                    0, 0, R.anim.fade_out).addToBackStack(null).
                                    replace(R.id.framlayout, myFragment).commit();

                        }
                    }
                });
                gridholder.lv_main_rifles_row.setEnabled(true);
                gridholder.lv_main_rifles_row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {

                                //  gridholder.lv_main_rifles_row.setEnabled(false);
                                Bundle b = new Bundle();
                                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                                b.putString("product_id", productlistInnerData.getProductId());
                                b.putString("wishlist", productlistInnerData.getWishlist());
                                ProductDetailFragment myFragment = new ProductDetailFragment();
                                myFragment.setArguments(b);
                                activity.getSupportFragmentManager()
                                        .beginTransaction().setCustomAnimations(R.anim.fade_in,
                                        0, 0, R.anim.fade_out)
                                        .setCustomAnimations(R.anim.fade_in,
                                                0, 0, R.anim.fade_out)
                                        .addToBackStack(null).add(R.id.framlayout, myFragment)
                                        .commit();
                                if (drawer.isDrawerOpen(GravityCompat.START)) {
                                    drawer.closeDrawer(GravityCompat.START);
                                }
                            }
                        }, 100);
                    }
                });

                gridholder.iv_rifles_add_wishlist.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        String prod_id = productlistInnerData.getProductId();

                        if (Login_preference.getLogin_flag(context).matches("1")) {
                            if (CheckNetwork.isNetworkAvailable(context)) {
                                CallAddtoWishlistApi(gridholder, prod_id, position);
                            } else {
                                Toast.makeText(context, context.getString(R.string.internet), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    Log.e("debug_131", "ss" + screentype);
                                    String screen = screentype;
                                    Bundle b = new Bundle();
                                    b.putString("screen", "" + screen);
                                    b.putString("subcat_id", "" + subcat_id);
                                    b.putString("subcatename", "" + subcatename);
                                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
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
                });

                gridholder.iv_rifles__remove_wishlist.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String prod_id = productlistInnerData.getProductId();

                        if (CheckNetwork.isNetworkAvailable(context)) {
                            CallRemoveWishlistApi(gridholder, prod_id, position);
                        } else {
                            Toast.makeText(context, context.getString(R.string.nointernet), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
        }
    }


    @Override
    public int getItemCount() {
        return productlistdata.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout lv_main_rifles_row, lv_rifles_wishlist, lv_product_add_to_cart;
        ImageView iv_rifles_add_wishlist, iv_rifles__remove_wishlist, iv_rifles;
        TextView tv_rifles_name, tv_rifles_msrp_title, tv_rifles_msrp, tv_product_add_to_cart, tv_availbleqty;
        TextView tv_rifles_lv_price_title, tv_rifles_lv_price, tv_rifles_Specialprice_title, tv_rifles_Specialprice, tv_rifles_map_title, tv_rifles_map;
        LinearLayout lv_msrp, lv_price, lv_Specialprice, lv_map, lv_ds_main_availbleqty;

        public MyViewHolder(@NonNull View view) {
            super(view);
            lv_main_rifles_row = view.findViewById(R.id.lv_main_rifles_row);
            lv_rifles_wishlist = view.findViewById(R.id.lv_rifles_wishlist);
            lv_product_add_to_cart = view.findViewById(R.id.lv_product_add_to_cart);
            iv_rifles_add_wishlist = view.findViewById(R.id.iv_rifles_add_wishlist);
            iv_rifles__remove_wishlist = view.findViewById(R.id.iv_rifles__remove_wishlist);
            iv_rifles = view.findViewById(R.id.iv_rifles);
            tv_rifles_name = view.findViewById(R.id.tv_rifles_name);
            tv_rifles_msrp_title = view.findViewById(R.id.tv_rifles_msrp_title);
            tv_rifles_msrp = view.findViewById(R.id.tv_rifles_msrp);
            tv_product_add_to_cart = view.findViewById(R.id.tv_product_add_to_cart);
            tv_availbleqty = view.findViewById(R.id.tv_availbleqty);
            lv_ds_main_availbleqty = view.findViewById(R.id.lv_ds_main_availbleqty);

            lv_msrp = view.findViewById(R.id.lv_msrp);
            lv_price = view.findViewById(R.id.lv_price);
            lv_Specialprice = view.findViewById(R.id.lv_Specialprice);
            lv_map = view.findViewById(R.id.lv_map);

            tv_rifles_lv_price_title = view.findViewById(R.id.tv_rifles_lv_price_title);
            tv_rifles_lv_price = view.findViewById(R.id.tv_rifles_lv_price);
            tv_rifles_Specialprice_title = view.findViewById(R.id.tv_rifles_Specialprice_title);
            tv_rifles_Specialprice = view.findViewById(R.id.tv_rifles_Specialprice);
            tv_rifles_map_title = view.findViewById(R.id.tv_rifles_map_title);
            tv_rifles_map = view.findViewById(R.id.tv_rifles_map);

        }
    }

    public class MyNewHolder extends RecyclerView.ViewHolder {
        LinearLayout lv_main_product_row_verticle, lv_product_wishlist_verticle, lv_product_add_to_cart_verticle;
        ImageView iv_product_add_wishlist, iv_product_remove_wishlist, iv_rifles;
        TextView tv_rifles_name, tv_product_msrp_title, tv_product_msrp_value, tv_product_add_to_cart_verticle, tv_availbleqty;
        TextView tv_rifles_lv_price_titlel, tv_rifles_lv_pricel, tv_rifles_Specialprice_titlel, tv_rifles_Specialpricel, tv_rifles_map_titlel, tv_rifles_mapl;
        LinearLayout lv_msrpl, lv_pricel, lv_Specialpricel, lv_mapl, lv_ds_main_availbleqty;


        public MyNewHolder(View itemView) {
            super(itemView);
            lv_main_product_row_verticle = itemView.findViewById(R.id.lv_main_product_row_verticle);
            lv_product_wishlist_verticle = itemView.findViewById(R.id.lv_product_wishlist_verticle);
            lv_product_add_to_cart_verticle = itemView.findViewById(R.id.lv_product_add_to_cart_verticle);
            iv_product_add_wishlist = itemView.findViewById(R.id.iv_product_add_wishlist);
            iv_product_remove_wishlist = itemView.findViewById(R.id.iv_product_remove_wishlist);
            iv_rifles = itemView.findViewById(R.id.iv_product_verticle);
            tv_rifles_name = itemView.findViewById(R.id.tv_product_name_verticle);
            tv_product_msrp_title = itemView.findViewById(R.id.tv_product_msrp_title);
            tv_product_msrp_value = itemView.findViewById(R.id.tv_product_msrp_value);
            tv_product_add_to_cart_verticle = itemView.findViewById(R.id.tv_product_add_to_cart_verticle);
            tv_availbleqty = itemView.findViewById(R.id.tv_availbleqty);
            lv_ds_main_availbleqty = itemView.findViewById(R.id.lv_ds_main_availbleqty);


            lv_msrpl = itemView.findViewById(R.id.lv_msrpl);
            lv_pricel = itemView.findViewById(R.id.lv_pricel);
            lv_Specialpricel = itemView.findViewById(R.id.lv_Specialpricel);
            lv_mapl = itemView.findViewById(R.id.lv_mapl);

            tv_rifles_lv_price_titlel = itemView.findViewById(R.id.tv_rifles_lv_price_titlel);
            tv_rifles_lv_pricel = itemView.findViewById(R.id.tv_rifles_lv_pricel);
            tv_rifles_Specialprice_titlel = itemView.findViewById(R.id.tv_rifles_Specialprice_titlel);
            tv_rifles_Specialpricel = itemView.findViewById(R.id.tv_rifles_Specialpricel);
            tv_rifles_map_titlel = itemView.findViewById(R.id.tv_rifles_map_titlel);
            tv_rifles_mapl = itemView.findViewById(R.id.tv_rifles_mapl);
        }
    }


    private void callAddtoCartApi(String product_id, final CoordinatorLayout coordinatorLayout, final LinearLayout linearLayout) {
        linearLayout.setVisibility(View.VISIBLE);
        coordinatorLayout.setVisibility(View.GONE);
        addtocart(product_id).enqueue(new Callback<AddToCartModel>() {
            @Override
            public void onResponse(Call<AddToCartModel> call, Response<AddToCartModel> response) {
                linearLayout.setVisibility(View.GONE);
                coordinatorLayout.setVisibility(View.VISIBLE);

                AddToCartModel model = response.body();
                if (model.getStatus().equalsIgnoreCase("Success")) {
                    Toast.makeText(context, "" + model.getMessage(), Toast.LENGTH_SHORT).show();
                    Login_preference.setquote_id(context, model.getQuoteId());
                    Login_preference.setCart_item_count(context, model.getItemsCount());
                    String item_count = String.valueOf(model.getItemsCount());
                    //   Log.e("item_qty",""+item_count);

                    if (item_count == null || item_count.equalsIgnoreCase("null") || item_count.equals("")) {
                        // Log.e("count_40", "" + String.valueOf(model.getItemsCount()));
                        tv_bottomcount.setVisibility(View.GONE);
                    } else {
                        tv_bottomcount.setVisibility(View.VISIBLE);
                        //   Log.e("count_80", "" + String.valueOf(model.getItemsCount()));
                        NavigationActivity.Check_String_NULL_Value(tv_bottomcount, String.valueOf(model.getItemsCount()));
                    }
                } else {
                    //Log.e("debug_244else",""+model.getMessage());
                    Toast.makeText(context, "" + model.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddToCartModel> call, Throwable t) {
                linearLayout.setVisibility(View.GONE);
                coordinatorLayout.setVisibility(View.VISIBLE);
                Toast.makeText(context, context.getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Call<AddToCartModel> addtocart(String product_id) {
        //   Log.e("emaikl_262", " " +  Login_preference.getemail(context));
        //   Log.e("emaikl_262product_id", " " + product_id);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        return apiInterface.addToCart(
                Login_preference.getemail(context),
                product_id);
    }

    private Call<RemoveFromWishlistModel> callremovewishlistApi(String prod_idpass) {
        //  Log.e("inner_user_wish197",""+Login_preference.getcustomer_id(context));
        //  Log.e("inner_wish_prod_idpass",""+ prod_idpass);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        return apiInterface.removewishlist(Login_preference.getcustomer_id(context), prod_idpass);
    }

    private Call<AddToWishlist> calladdtowishlistApi(String prod_idpass) {
        //  Log.e("inner_user_wish197",""+Login_preference.getcustomer_id(context));
        // Log.e("inner_wish_prod_idpass",""+ prod_idpass);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        return apiInterface.addtowishlist(Login_preference.getcustomer_id(context), prod_idpass);
    }


    private void CallRemoveWishlistApi_list(final MyNewHolder listHolder, String prod_id, final int position) {
        callremovewishlistApi(prod_id).enqueue(new Callback<RemoveFromWishlistModel>() {
            @Override
            public void onResponse(Call<RemoveFromWishlistModel> call, Response<RemoveFromWishlistModel> response) {

                RemoveFromWishlistModel addToWishlist = response.body();

                if (addToWishlist.getStatus().equalsIgnoreCase("Success")) {

                    listHolder.iv_product_add_wishlist.setVisibility(View.VISIBLE);
                    listHolder.iv_product_remove_wishlist.setVisibility(View.GONE);
                    final Animation myAnim = AnimationUtils.loadAnimation(context, R.anim.bounce);
                    MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                    myAnim.setInterpolator(interpolator);
                    listHolder.iv_product_add_wishlist.startAnimation(myAnim);
                    productlistdata.get(position).setWishlist("0");
                    Toast.makeText(context, addToWishlist.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("remove_wishlist", "" + addToWishlist.getCount());
                    String item_count = null;
                    if (addToWishlist.getCount() == 0 || addToWishlist.getCount().equals("0.00")) {
                        Login_preference.set_wishlist_count(context, "");
                        //   item_count = String.valueOf(removeproductmodel.getItemsCount());
                    } else {
                        Login_preference.set_wishlist_count(context, String.valueOf(addToWishlist.getCount()));
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
                }
            }

            @Override
            public void onFailure(Call<RemoveFromWishlistModel> call, Throwable t) {
                Toast.makeText(context, context.getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void CallAddtoWishlistApi_list(final MyNewHolder listHolder, String prod_id, final int position) {

        calladdtowishlistApi(prod_id).enqueue(new Callback<AddToWishlist>() {
            @Override
            public void onResponse(Call<AddToWishlist> call, Response<AddToWishlist> response) {
                // Log.e("debug_166",""+response);
                //  Log.e("debug_167",""+response.body());
                AddToWishlist addToWishlist = response.body();
                //  Log.e("response_168",""+addToWishlist);
                //   Log.e("status_wish",""+addToWishlist.getStatus());

                if (addToWishlist.getStatus().equalsIgnoreCase("Success")) {

                    listHolder.iv_product_add_wishlist.setVisibility(View.GONE);
                    listHolder.iv_product_remove_wishlist.setVisibility(View.VISIBLE);
                    final Animation myAnim = AnimationUtils.loadAnimation(context, R.anim.bounce);
                    MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                    myAnim.setInterpolator(interpolator);
                    listHolder.iv_product_remove_wishlist.startAnimation(myAnim);
                    productlistdata.get(position).setWishlist("1");
                    //   Log.e("status_wish","ok");
                    Toast.makeText(context, addToWishlist.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("remove_wishlist", "" + addToWishlist.getCount());
                    Login_preference.set_wishlist_count(context, String.valueOf(addToWishlist.getCount()));
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
                    Toast.makeText(context, addToWishlist.getMessage(), Toast.LENGTH_SHORT).show();
                    // Log.e("status_wish","not_ok");
                }
            }

            @Override
            public void onFailure(Call<AddToWishlist> call, Throwable t) {
                // Log.e("error_wish",""+t);
                // Log.e("debug_remivr", ""+t);
                Toast.makeText(context, context.getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void CallAddtoWishlistApi(final MyViewHolder gridholder, String prod_id, final int position) {

        calladdtowishlistApi(prod_id).enqueue(new Callback<AddToWishlist>() {
            @Override
            public void onResponse(Call<AddToWishlist> call, Response<AddToWishlist> response) {
                //   Log.e("debug_166",""+response);
                //   Log.e("debug_167",""+response.body());
                AddToWishlist addToWishlist = response.body();
                //   Log.e("response_168",""+addToWishlist);
                //   Log.e("status_wish",""+addToWishlist.getStatus());

                if (addToWishlist.getStatus().equalsIgnoreCase("Success")) {

                    gridholder.iv_rifles_add_wishlist.setVisibility(View.GONE);
                    gridholder.iv_rifles__remove_wishlist.setVisibility(View.VISIBLE);
                    final Animation myAnim = AnimationUtils.loadAnimation(context, R.anim.bounce);
                    MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                    myAnim.setInterpolator(interpolator);
                    gridholder.iv_rifles__remove_wishlist.startAnimation(myAnim);

                    productlistdata.get(position).setWishlist("1");
                    // Log.e("status_wish","ok");
                    Toast.makeText(context, addToWishlist.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("remove_wishlist", "" + addToWishlist.getCount());
                    Login_preference.set_wishlist_count(context, String.valueOf(addToWishlist.getCount()));
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
                    Toast.makeText(context, addToWishlist.getMessage(), Toast.LENGTH_SHORT).show();
                    //  Log.e("status_wish","not_ok");
                }
            }

            @Override
            public void onFailure(Call<AddToWishlist> call, Throwable t) {
                // Log.e("error_wish",""+t);
                // Log.e("debug_remivr", ""+t);
                Toast.makeText(context, context.getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void CallRemoveWishlistApi(final MyViewHolder listHolder, String prod_id, final int position) {
        callremovewishlistApi(prod_id).enqueue(new Callback<RemoveFromWishlistModel>() {
            @Override
            public void onResponse(Call<RemoveFromWishlistModel> call, Response<RemoveFromWishlistModel> response) {

                RemoveFromWishlistModel addToWishlist = response.body();

                if (addToWishlist.getStatus().equalsIgnoreCase("Success")) {

                    listHolder.iv_rifles_add_wishlist.setVisibility(View.VISIBLE);
                    listHolder.iv_rifles__remove_wishlist.setVisibility(View.GONE);
                    final Animation myAnim = AnimationUtils.loadAnimation(context, R.anim.bounce);
                    MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                    myAnim.setInterpolator(interpolator);
                    listHolder.iv_rifles_add_wishlist.startAnimation(myAnim);
                    productlistdata.get(position).setWishlist("0");

                    Toast.makeText(context, addToWishlist.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("remove_wishlist", "" + addToWishlist.getCount());
                    String item_count = null;
                    if (addToWishlist.getCount() == 0 || addToWishlist.getCount().equals("0.00")) {
                        Login_preference.set_wishlist_count(context, "");
                        //   item_count = String.valueOf(removeproductmodel.getItemsCount());
                    } else {
                        Login_preference.set_wishlist_count(context, String.valueOf(addToWishlist.getCount()));
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
                    Toast.makeText(context, addToWishlist.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RemoveFromWishlistModel> call, Throwable t) {
                Toast.makeText(context, context.getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();
            }
        });
    }


}




