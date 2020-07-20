package com.dolphin.zanders.Model;

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
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dolphin.zanders.Activity.NavigationActivity;

import com.dolphin.zanders.Fragment.LoginFragment;
import com.dolphin.zanders.Fragment.NewProductDetail_Fragment;


import com.dolphin.zanders.Model.NewProductListModel.Item;
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

import static com.dolphin.zanders.Activity.NavigationActivity.drawer;
import static com.dolphin.zanders.Activity.NavigationActivity.tv_wishlist_count;
import static com.dolphin.zanders.Fragment.ProductListFragment.subcat_id;
import static com.dolphin.zanders.Fragment.ProductListFragment.subcatename;


public class ProductListAdater extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    String screentype;
    Context context;
    private List<com.dolphin.zanders.Model.NewProductListModel.Item> ItemList;
    int choice = 0;
    LayoutInflater inflater;


    private List<Integer> wishlitProductidList = new ArrayList<>();
    private List<Integer> wishlitItemId = new ArrayList<>();

    public ProductListAdater(Context context,List<com.dolphin.zanders.Model.NewProductListModel.Item> ItemList) {
        this.context = context;
        //this.ItemList = new ArrayList<>();
        this.ItemList = ItemList;
    }
    public ProductListAdater(Context context,String scree) {
        this.context = context;
        this.ItemList = new ArrayList<>();
        //this.ItemList = ItemList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        choice = ApiClientcusome.VIEWTYPE;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        switch (choice) {
            case 0:
                View itemView = inflater.inflate(R.layout.product_row_cm, parent, false);
                return new MyViewHolder(itemView);
            case 2:
                View listitem = inflater.inflate(R.layout.new_product_row_verticle, parent, false);
                return new MyNewHolder(listitem);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final com.dolphin.zanders.Model.NewProductListModel.Item item = ItemList.get(position);

        switch (ApiClientcusome.VIEWTYPE) {
            case 2:
                //  Log.e("list", ApiClientcusome.VIEWTYPE + "");
                final MyNewHolder listHolder = (MyNewHolder) holder;
                listHolder.tv_product_name_verticle.setText(item.getName());
              //  listHolder.tv_rifles_lv_pricel.setText("" + item.getPrice());


                Log.e("debu_22tierpricelistt","="+item.getTierprice());
                if(item.getTierprice()==null || item.getTierprice().equalsIgnoreCase("null") || item.getTierprice().equalsIgnoreCase(null))
                {
                    listHolder.tv_verticle_tierprice.setVisibility(View.INVISIBLE);
                }else {
                    if(item.getTierprice().equalsIgnoreCase("0"))
                    {
                        listHolder.tv_verticle_tierprice.setVisibility(View.INVISIBLE);
                    }else {
                        listHolder.tv_verticle_tierprice.setVisibility(View.VISIBLE);
                        listHolder.tv_verticle_tierprice.setText(item.getTierprice()+" "+Login_preference.getcurrencycode(context));

                    }
                }
                Log.e("debu_22list","="+item.getSpecial_price());

                if(item.getSpecial_price().equalsIgnoreCase("0.0"))
                {
                    listHolder.lv_Specialprice_verticle.setVisibility(View.INVISIBLE);
                    listHolder.tv_rifles_lv_pricel.setText("" + item.getPrice()+" "+Login_preference.getcurrencycode(context));
                    listHolder.tv_rifles_lv_price_titlel.setTextColor(context.getResources().getColor(R.color.black));
                    listHolder.tv_rifles_lv_pricel.setTextColor(context.getResources().getColor(R.color.black));
                }else {
                    listHolder.tv_rifles_lv_pricel.setPaintFlags(listHolder.tv_rifles_lv_pricel.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    listHolder.lv_Specialprice_verticle.setVisibility(View.VISIBLE);

                    listHolder.tv_rifles_lv_pricel.setText("" + item.getPrice()+" "+Login_preference.getcurrencycode(context));
                    listHolder.tv_specialprice_list.setText("" + item.getSpecial_price()+" "+Login_preference.getcurrencycode(context));

                    listHolder.tv_rifles_lv_price_titlel.setTextColor(context.getResources().getColor(R.color.grey));
                    listHolder.tv_rifles_lv_pricel.setTextColor(context.getResources().getColor(R.color.grey));

                }


                final RequestOptions requestOptions = new RequestOptions();
                requestOptions.placeholder(R.drawable.logo_app);
                requestOptions.error(R.drawable.logo_app);
                Glide.with(context)
                        .setDefaultRequestOptions(requestOptions)
                        .load("http://dkbraende.demoproject.info/pub/media/catalog/product" + item.getImage()).into(listHolder.iv_product_verticle);


                //if item in wishlist

                Log.e("debug_48", "fg" + item.getIsWishlisted());
                if (ItemList.get(position).getIsWishlisted() == null || ItemList.get(position).getIsWishlisted().equalsIgnoreCase(null) || ItemList.get(position).getIsWishlisted().equalsIgnoreCase("null")) {
                    listHolder.iv_product_remove_wishlist.setVisibility(View.GONE);
                    listHolder.iv_product_add_wishlist.setVisibility(View.VISIBLE);
                } else {
                    if (ItemList.get(position).getIsWishlisted().equalsIgnoreCase("true") == true) {
                        listHolder.iv_product_add_wishlist.setVisibility(View.GONE);
                        listHolder.iv_product_remove_wishlist.setVisibility(View.VISIBLE);
                    } else if (ItemList.get(position).getIsWishlisted().equalsIgnoreCase("false")) {
                        listHolder.iv_product_remove_wishlist.setVisibility(View.GONE);
                        listHolder.iv_product_add_wishlist.setVisibility(View.VISIBLE);
                    }
                }


                listHolder.lv_main_product_row_verticle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {

                                Bundle b = new Bundle();
                                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                                b.putString("product_id", String.valueOf(ItemList.get(position).getId()));
                                b.putString("product_name", String.valueOf(ItemList.get(position).getName()));

                                //   b.putString("wishlist", item.getWishlist());
                                NewProductDetail_Fragment myFragment = new NewProductDetail_Fragment();
                                myFragment.setArguments(b);
                                activity.getSupportFragmentManager()
                                        .beginTransaction().setCustomAnimations(R.anim.fade_in,
                                        0, 0, R.anim.fade_out)
                                        .setCustomAnimations(R.anim.fade_in,
                                                0, 0, R.anim.fade_out)
                                        .addToBackStack("proud").replace(R.id.framlayout, myFragment)
                                        .commit();
                                if (drawer.isDrawerOpen(GravityCompat.START)) {
                                    drawer.closeDrawer(GravityCompat.START);
                                }
                            }
                        }, 100);

                    }
                });


                listHolder.lv_product_add_to_cart_verticle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (CheckNetwork.isNetworkAvailable(context)) {
                            if (Login_preference.getLogin_flag(context).equalsIgnoreCase("1")) {
                                String sku = ItemList.get(position).getSku();
                                CallAddtoCartApi(listHolder,sku);
                            } else {
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        // Log.e("debug_131", "ss" + screentype);
                                        // String screen = screentype;
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
                                }, 50);
                            }

                        } else {
                            Toast.makeText(context, context.getResources().getString(R.string.intcon), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                //add to wishlist

                listHolder.iv_product_add_wishlist.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        String prod_id = String.valueOf(ItemList.get(position).getId());
                        if (Login_preference.getLogin_flag(context).equalsIgnoreCase("1")) {
                            if (CheckNetwork.isNetworkAvailable(context)) {
                                CallAddtoWishlistApilistHolder_list(listHolder, prod_id, position, ItemList, view);
                            } else {
                                Toast.makeText(context, context.getString(R.string.internet), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            AppCompatActivity activity = (AppCompatActivity) view.getContext();
                            LoginFragment myFragment = new LoginFragment();
                            activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                                    0, 0, R.anim.fade_out).setCustomAnimations(R.anim.fade_in,
                                    0, 0, R.anim.fade_out).replace(R.id.framlayout, myFragment).addToBackStack(null).commit();
                        }
                    }
                });

                //remove from wishlist
                listHolder.iv_product_remove_wishlist.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        if (Login_preference.getLogin_flag(context).equalsIgnoreCase("1")) {
                            if (CheckNetwork.isNetworkAvailable(context)) {
                                Log.e("prod_id_476", "=" + ItemList.get(position).getId());
                                Log.e("prod_id_476size", "=" + wishlitProductidList.size());
                                Log.e("position_476", "=" + position);
                                Log.e("position_476", "=" + wishlitItemId.size());

                                if(wishlitProductidList.size()>0)
                                {
                                    if (wishlitProductidList.contains(ItemList.get(position).getId())) {
                                        int pos = wishlitProductidList.indexOf(ItemList.get(position).getId());
                                        Log.e("wishlist_it222", "=" + pos);
                                        if (wishlitItemId.size() > 0) {
                                            String wishlist_item_id = String.valueOf(wishlitItemId.get(pos));
                                            Log.e("wishlist_item_id333333", "=" + wishlist_item_id);
                                            ItemList.get(position).setWishlist_item_id(wishlist_item_id);
                                            callRemoveFromWishlistApi_listHolder(wishlist_item_id, position, v, listHolder, ItemList);
                                        }
                                    } else {
                                        ItemList.get(position).setWishlist_item_id("0");
                                    }
                                }else {
                                    final String itemid = String.valueOf(ItemList.get(position).getWishlist_item_id());
                                    Log.e("debg", "=" + itemid);

                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        public void run() {
                                            if (itemid.equalsIgnoreCase("0")) {
                                                Log.e("debg_rem0ve_if", "=" + itemid);

                                               /* if (wishlitProductidList.contains(ItemList.get(position).getId())) {
                                                    int pos = wishlitProductidList.indexOf(ItemList.get(position).getId());
                                                    Log.e("wishlist_it222", "=" + pos);
                                                    if (wishlitItemId.size() > 0) {
                                                        String wishlist_item_id = String.valueOf(wishlitItemId.get(pos));
                                                        Log.e("wishlist_item_id333333", "=" + wishlist_item_id);
                                                        ItemList.get(position).setWishlist_item_id(wishlist_item_id);
                                                        callRemoveFromWishlistApi_listHolder(wishlist_item_id, position, v, listHolder, ItemList);
                                                    }
                                                } else {
                                                    ItemList.get(position).setWishlist_item_id("0");
                                                }*/
                                            } else {
                                                Log.e("debg_remove_else", "=" + itemid);
                                                callRemoveFromWishlistApi_listHolder(itemid, position, v, listHolder, ItemList);
                                            }
                                        }
                                    }, 100);
                                }


                            } else {
                                Toast.makeText(context, context.getString(R.string.internet), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            AppCompatActivity activity = (AppCompatActivity) v.getContext();
                            LoginFragment myFragment = new LoginFragment();
                            activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                                    0, 0, R.anim.fade_out).setCustomAnimations(R.anim.fade_in,
                                    0, 0, R.anim.fade_out).replace(R.id.framlayout, myFragment).addToBackStack(null).commit();
                        }
                    }
                });

                break;
            case 0:
                ///grid holderr
                MyViewHolder gridholder = (MyViewHolder) holder;
                gridholder.tv_package_name.setText(item.getName());
                Log.e("debu_22tierprice","="+item.getTierprice());
                if(item.getTierprice()==null || item.getTierprice().equalsIgnoreCase("null") || item.getTierprice().equalsIgnoreCase(null))
                {
                    gridholder.tv_rifles_tierprice.setVisibility(View.INVISIBLE);
                }else {
                    if(item.getTierprice().equalsIgnoreCase("0"))
                    {
                        gridholder.tv_rifles_tierprice.setVisibility(View.INVISIBLE);
                    }else {
                        gridholder.tv_rifles_tierprice.setVisibility(View.VISIBLE);
                        gridholder.tv_rifles_tierprice.setText(item.getTierprice()+" "+Login_preference.getcurrencycode(context));

                    }
                }
                Log.e("debu_22","="+item.getSpecial_price());

                if(item.getSpecial_price().equalsIgnoreCase("0.0"))
                {
                    gridholder.lv_Specialprice.setVisibility(View.INVISIBLE);
                    gridholder.tv_package_price.setText("" + item.getPrice()+" "+Login_preference.getcurrencycode(context));
                    gridholder.tv_package_price.setTextColor(context.getResources().getColor(R.color.black));
                    gridholder.tv_rifles_lv_price_title.setTextColor(context.getResources().getColor(R.color.black));
                }else {
                    gridholder.tv_package_price.setPaintFlags(gridholder.tv_package_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    gridholder.lv_Specialprice.setVisibility(View.VISIBLE);
                    gridholder.tv_package_price.setText("" + item.getPrice()+" "+Login_preference.getcurrencycode(context));
                    gridholder.tv_rifles_Specialprice.setText("" + item.getSpecial_price()+" "+Login_preference.getcurrencycode(context));

                    gridholder.tv_package_price.setTextColor(context.getResources().getColor(R.color.grey));
                    gridholder.tv_rifles_lv_price_title.setTextColor(context.getResources().getColor(R.color.grey));

                }

//               Log.e("debug_48", "fg" + item.getMediaGalleryEntries().get(0).getFile());
                final RequestOptions requestOptions1 = new RequestOptions();
                requestOptions1.placeholder(R.drawable.logo_app);
                requestOptions1.error(R.drawable.logo_app);
                Glide.with(context)
                        .setDefaultRequestOptions(requestOptions1)
                        .load("http://dkbraende.demoproject.info/pub/media/catalog/product" + item.getImage()).into(gridholder.iv_package_img);
                /*Glide.with(context)
                        .setDefaultRequestOptions(requestOptions1)
                        .load("http://dkbraende.demoproject.info/pub/media/catalog/product" + item.getMediaGalleryEntries().get(0).getFile()).into(gridholder.iv_package_img);
           */     // ProductListActivity.shimmer_productlist.setVisibility(View.GONE);




                gridholder.lv_main_rifles_row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {

                                Bundle b = new Bundle();
                                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                                b.putString("product_id", String.valueOf(ItemList.get(position).getId()));
                                b.putString("product_name", String.valueOf(ItemList.get(position).getName()));

                                //   b.putString("wishlist", item.getWishlist());
                                NewProductDetail_Fragment myFragment = new NewProductDetail_Fragment();
                                myFragment.setArguments(b);
                                activity.getSupportFragmentManager()
                                        .beginTransaction().setCustomAnimations(R.anim.fade_in,
                                        0, 0, R.anim.fade_out)
                                        .setCustomAnimations(R.anim.fade_in,
                                                0, 0, R.anim.fade_out)
                                        .addToBackStack("product detail").replace(R.id.framlayout, myFragment)
                                        .commit();
                                if (drawer.isDrawerOpen(GravityCompat.START)) {
                                    drawer.closeDrawer(GravityCompat.START);
                                }
                            }
                        }, 100);

                    }
                });


                gridholder.lv_product_add_to_cart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (CheckNetwork.isNetworkAvailable(context)) {
                            if (Login_preference.getLogin_flag(context).equalsIgnoreCase("1")) {
                                String sku = ItemList.get(position).getSku();
                                CallAddtoCartApigrid(gridholder, sku);
                            } else {
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        // Log.e("debug_131", "ss" + screentype);
                                        // String screen = screentype;
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
                                }, 50);
                            }

                        } else {
                            Toast.makeText(context, context.getResources().getString(R.string.intcon), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                //if item in wishlist

                Log.e("debug_48", "fg" + item.getIsWishlisted());
                if (ItemList.get(position).getIsWishlisted() == null || ItemList.get(position).getIsWishlisted().equalsIgnoreCase(null) || ItemList.get(position).getIsWishlisted().equalsIgnoreCase("null")) {
                    gridholder.iv_rifles__remove_wishlist.setVisibility(View.GONE);
                    gridholder.iv_rifles_add_wishlist.setVisibility(View.VISIBLE);
                } else {
                    if (ItemList.get(position).getIsWishlisted().equalsIgnoreCase("true") == true) {
                        gridholder.iv_rifles_add_wishlist.setVisibility(View.GONE);
                        gridholder.iv_rifles__remove_wishlist.setVisibility(View.VISIBLE);
                    } else if (ItemList.get(position).getIsWishlisted().equalsIgnoreCase("false")) {
                        gridholder.iv_rifles__remove_wishlist.setVisibility(View.GONE);
                        gridholder.iv_rifles_add_wishlist.setVisibility(View.VISIBLE);
                    }
                }


                //add to wishlist

                gridholder.iv_rifles_add_wishlist.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        String prod_id = String.valueOf(ItemList.get(position).getId());
                        if (Login_preference.getLogin_flag(context).equalsIgnoreCase("1")) {
                            if (CheckNetwork.isNetworkAvailable(context)) {
                                CallAddtoWishlistApi_list(gridholder, prod_id, position, ItemList, view);
                            } else {
                                Toast.makeText(context, context.getString(R.string.internet), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            AppCompatActivity activity = (AppCompatActivity) view.getContext();
                            LoginFragment myFragment = new LoginFragment();
                            activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                                    0, 0, R.anim.fade_out).setCustomAnimations(R.anim.fade_in,
                                    0, 0, R.anim.fade_out).replace(R.id.framlayout, myFragment).addToBackStack(null).commit();
                        }
                    }
                });



                //remove from wishlist
                gridholder.iv_rifles__remove_wishlist.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        if (Login_preference.getLogin_flag(context).equalsIgnoreCase("1")) {
                            if (CheckNetwork.isNetworkAvailable(context)) {
                                Log.e("prod_id_476", "=" + ItemList.get(position).getId());
                                Log.e("prod_id_476size", "=" + wishlitProductidList.size());
                                Log.e("position_476", "=" + position);
                                Log.e("position_476", "=" + wishlitItemId.size());

                                if(wishlitProductidList.size()>0)
                                {
                                    if (wishlitProductidList.contains(ItemList.get(position).getId())) {
                                        int pos = wishlitProductidList.indexOf(ItemList.get(position).getId());
                                        Log.e("wishlist_it222", "=" + pos);
                                        if (wishlitItemId.size() > 0) {
                                            String wishlist_item_id = String.valueOf(wishlitItemId.get(pos));
                                            Log.e("wishlist_item_id333333", "=" + wishlist_item_id);
                                            ItemList.get(position).setWishlist_item_id(wishlist_item_id);
                                            callRemoveFromWishlistApi(wishlist_item_id, position, v, gridholder, ItemList);
                                        }
                                    } else {
                                        ItemList.get(position).setWishlist_item_id("0");
                                    }
                                }else {
                                    final String itemid = String.valueOf(ItemList.get(position).getWishlist_item_id());
                                    Log.e("debg", "=" + itemid);

                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        public void run() {
                                            if (itemid.equalsIgnoreCase("0")) {
                                                Log.e("debg_rem0ve_if", "=" + itemid);

                                           /* if (wishlitProductidList.contains(ItemList.get(position).getId())) {
                                                int pos = wishlitProductidList.indexOf(ItemList.get(position).getId());
                                                Log.e("wishlist_it222", "=" + pos);
                                                if (wishlitItemId.size() > 0) {
                                                    String wishlist_item_id = String.valueOf(wishlitItemId.get(pos));
                                                    Log.e("wishlist_item_id333333", "=" + wishlist_item_id);
                                                    ItemList.get(position).setWishlist_item_id(wishlist_item_id);
                                                    callRemoveFromWishlistApi(wishlist_item_id, position, v, gridholder, ItemList);
                                                }
                                            } else {
                                                ItemList.get(position).setWishlist_item_id("0");
                                            }*/
                                            } else {
                                                Log.e("debg_remove_else", "=" + itemid);
                                                callRemoveFromWishlistApi(itemid, position, v, gridholder, ItemList);
                                            }
                                        }
                                    }, 100);
                                }


                            } else {
                                Toast.makeText(context, context.getString(R.string.internet), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            AppCompatActivity activity = (AppCompatActivity) v.getContext();
                            LoginFragment myFragment = new LoginFragment();
                            activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                                    0, 0, R.anim.fade_out).setCustomAnimations(R.anim.fade_in,
                                    0, 0, R.anim.fade_out).replace(R.id.framlayout, myFragment).addToBackStack(null).commit();
                        }
                    }
                });
                break;
        }

    }


    public void addAll(List<Item> categories) {
        for (Item result : categories) {
            add(result);
        }
    }

    public void add(Item r) {
        ItemList.add(r);
        notifyItemInserted(ItemList.size() - 1);
    }

    @Override
    public int getItemCount() {
        return ItemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_package_price, tv_package_name,tv_rifles_tierprice,tv_rifles_Specialprice,tv_rifles_lv_price_title;
        ImageView iv_package_img, iv_rifles_add_wishlist, iv_rifles__remove_wishlist;
        LinearLayout lv_product_add_to_cart, lv_pb_prod, lv_product_main,lv_Specialprice;
        CardView lv_main_rifles_row;

        public MyViewHolder(@NonNull View view) {
            super(view);
            tv_rifles_lv_price_title = view.findViewById(R.id.tv_rifles_lv_price_title);
            lv_Specialprice = view.findViewById(R.id.lv_Specialprice);
            tv_rifles_Specialprice = view.findViewById(R.id.tv_rifles_Specialprice);
            tv_rifles_tierprice = view.findViewById(R.id.tv_rifles_tierprice);
            iv_rifles__remove_wishlist = view.findViewById(R.id.iv_rifles__remove_wishlist);
            iv_rifles_add_wishlist = view.findViewById(R.id.iv_rifles_add_wishlist);
            lv_product_add_to_cart = view.findViewById(R.id.lv_product_add_to_cart);
            lv_main_rifles_row = view.findViewById(R.id.lv_main_rifles_row);
            iv_package_img = view.findViewById(R.id.iv_rifles);
            tv_package_name = view.findViewById(R.id.tv_rifles_name);
            tv_package_price = view.findViewById(R.id.tv_rifles_lv_price);
            lv_pb_prod = view.findViewById(R.id.lv_pb_prod);
            lv_product_main = view.findViewById(R.id.lv_product_main);
        }
    }


    public class MyNewHolder extends RecyclerView.ViewHolder {
        LinearLayout lv_varticale, lv_product_add_to_cart_verticle, lv_pb_prod_verticle, lv_prod_verticle_main,lv_Specialprice_verticle;
        ImageView iv_product_add_wishlist, iv_product_remove_wishlist, iv_product_verticle;
        TextView tv_product_name_verticle,tv_verticle_tierprice,tv_specialprice_title_list,tv_specialprice_list,tv_rifles_lv_price_titlel;
        TextView tv_rifles_lv_pricel;
        CardView lv_main_product_row_verticle;


        public MyNewHolder(View itemView) {
            super(itemView);


            tv_rifles_lv_price_titlel = itemView.findViewById(R.id.tv_rifles_lv_price_titlel);
            tv_specialprice_list = itemView.findViewById(R.id.tv_specialprice_list);
            tv_specialprice_title_list = itemView.findViewById(R.id.tv_specialprice_title_list);
            lv_Specialprice_verticle = itemView.findViewById(R.id.lv_Specialprice_verticle);
            tv_verticle_tierprice = itemView.findViewById(R.id.tv_verticle_tierprice);
            lv_prod_verticle_main = itemView.findViewById(R.id.lv_prod_verticle_main);
            lv_pb_prod_verticle = itemView.findViewById(R.id.lv_pb_prod_verticle);
            lv_product_add_to_cart_verticle = itemView.findViewById(R.id.lv_product_add_to_cart_verticle);
            tv_rifles_lv_pricel = itemView.findViewById(R.id.tv_rifles_lv_pricel);
            iv_product_remove_wishlist = itemView.findViewById(R.id.iv_product_remove_wishlist);
            iv_product_add_wishlist = itemView.findViewById(R.id.iv_product_add_wishlist);
            tv_product_name_verticle = itemView.findViewById(R.id.tv_product_name_verticle);
            lv_main_product_row_verticle = itemView.findViewById(R.id.lv_main_product_row_verticle);
            lv_varticale = itemView.findViewById(R.id.lv_varticale);
            iv_product_verticle = itemView.findViewById(R.id.iv_product_verticle);
        }
    }



    //list holder add to wishlsit
    private void CallAddtoWishlistApilistHolder_list(final MyNewHolder holder, final String prod_id, final int position, final List<Item> itemList, final View v) {

        holder.lv_prod_verticle_main.setVisibility(View.GONE);
        holder.lv_pb_prod_verticle.setVisibility(View.VISIBLE);

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

                        itemList.get(position).setIsWishlisted("true");
                        callWishlistCountApi();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                callWishistApi(prod_id, itemList, position);
                            }
                        }, 0);

                        holder.iv_product_add_wishlist.setVisibility(View.GONE);
                        holder.iv_product_remove_wishlist.setVisibility(View.VISIBLE);
                        final Animation myAnim = AnimationUtils.loadAnimation(context, R.anim.bounce);
                        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                        myAnim.setInterpolator(interpolator);
                        holder.iv_product_remove_wishlist.startAnimation(myAnim);
                        holder.lv_prod_verticle_main.setVisibility(View.VISIBLE);
                        holder.lv_pb_prod_verticle.setVisibility(View.GONE);

                    } else {
                        holder.lv_prod_verticle_main.setVisibility(View.VISIBLE);
                        holder.lv_pb_prod_verticle.setVisibility(View.GONE);

                    }


                } else {
                    holder.lv_prod_verticle_main.setVisibility(View.VISIBLE);
                    holder.lv_pb_prod_verticle.setVisibility(View.GONE);

                    NavigationActivity.get_Customer_tokenapi();
                    CallAddtoWishlistApilistHolder_list(holder, prod_id, position, itemList, v);

                    Log.e("debug_error", "=" + response);
                    Log.e("error", "=" + response.body());
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                holder.lv_prod_verticle_main.setVisibility(View.VISIBLE);
                holder.lv_pb_prod_verticle.setVisibility(View.GONE);

                // Log.e("error_wish",""+t);
                Log.e("debug_remivr", "" + t.getMessage());
                Toast.makeText(context, context.getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();

            }
        });

    }


    // list holder remove wishlist
    private void callRemoveFromWishlistApi_listHolder(final String itemid, final int position, final View v, final MyNewHolder holder, final List<Item> itemList) {
        holder.lv_prod_verticle_main.setVisibility(View.GONE);
        holder.lv_pb_prod_verticle.setVisibility(View.VISIBLE);
        callRemoveWishlistapi(itemid).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                Boolean paymentMehtodModel = response.body();

                Log.e("resaaaremove wishlist", "=" + response.code());
                Log.e("resaaacccc", "=" + response);

                if (response.code() == 200) {

                    holder.iv_product_remove_wishlist.setVisibility(View.GONE);
                    holder.iv_product_add_wishlist.setVisibility(View.VISIBLE);
                    final Animation myAnim = AnimationUtils.loadAnimation(context, R.anim.bounce);
                    MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                    myAnim.setInterpolator(interpolator);
                    holder.iv_product_add_wishlist.startAnimation(myAnim);
                    itemList.get(position).setIsWishlisted("false");

                    //     Toast.makeText(context, "Product Remove from Wishlist successfully", Toast.LENGTH_SHORT).show();

                    callWishlistCountApi();
                    holder.lv_prod_verticle_main.setVisibility(View.VISIBLE);
                    holder.lv_pb_prod_verticle.setVisibility(View.GONE);

                } else {
                    holder.lv_prod_verticle_main.setVisibility(View.VISIBLE);
                    holder.lv_pb_prod_verticle.setVisibility(View.GONE);
                    NavigationActivity.get_Customer_tokenapi();

                   // callRemoveFromWishlistApi_listHolder(itemid,position,v,holder,itemList);
                }

            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                holder.lv_prod_verticle_main.setVisibility(View.VISIBLE);
                holder.lv_pb_prod_verticle.setVisibility(View.GONE);


                // String error=  t.printStackTrace();
                Toast.makeText(context, "" + context.getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();
                Log.e("debug_175125", "pages: " + t);
            }
        });
    }


    private Call<Boolean> calladdtowishnew(String itemid) {

        ApiInterface api = ApiClientcusome.getClient().create(ApiInterface.class);
        Log.e("debug_11111", "==" + itemid);
        Log.e("debug_11token", "==" + Login_preference.getCustomertoken(context));
        ///http://dkbraende.demoproject.info/rest//V1/carts/mine/items/162920

        String url = ApiClientcusome.MAIN_URLL + "wishlist/add/" + itemid;
        Log.e("url1111", "==" + url);
        return api.defaultaddtowishlist("Bearer " + Login_preference.getCustomertoken(context), url);
    }

    //grid holdr remove wishlist
    private void callRemoveFromWishlistApi(final String itemid, final int position, final View v, final MyViewHolder holder, final List<Item> itemList) {
        holder.lv_product_main.setVisibility(View.GONE);
        holder.lv_pb_prod.setVisibility(View.VISIBLE);
        callRemoveWishlistapi(itemid).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                Boolean paymentMehtodModel = response.body();

                Log.e("resaaaremove wishlist", "=" + response.code());
                Log.e("resaaacccc", "=" + response);

                if (response.code() == 200) {

                    holder.iv_rifles__remove_wishlist.setVisibility(View.GONE);
                    holder.iv_rifles_add_wishlist.setVisibility(View.VISIBLE);
                    final Animation myAnim = AnimationUtils.loadAnimation(context, R.anim.bounce);
                    MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                    myAnim.setInterpolator(interpolator);
                    holder.iv_rifles_add_wishlist.startAnimation(myAnim);
                    itemList.get(position).setIsWishlisted("false");

                    //     Toast.makeText(context, "Product Remove from Wishlist successfully", Toast.LENGTH_SHORT).show();

                    callWishlistCountApi();
                    holder.lv_product_main.setVisibility(View.VISIBLE);
                    holder.lv_pb_prod.setVisibility(View.GONE);

                } else {
                    holder.lv_product_main.setVisibility(View.VISIBLE);
                    holder.lv_pb_prod.setVisibility(View.GONE);
                    NavigationActivity.get_Customer_tokenapi();

                   // callRemoveFromWishlistApi(itemid,position,v,holder,itemList);
                }

            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                holder.lv_product_main.setVisibility(View.VISIBLE);
                holder.lv_pb_prod.setVisibility(View.GONE);


                // String error=  t.printStackTrace();
                Toast.makeText(context, "" + context.getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();
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
        return api.removeitemfromWishlistt("Bearer " + Login_preference.getCustomertoken(context), url);
    }

    //grid holder add to wishlsit

    private void CallAddtoWishlistApi_list(final MyViewHolder holder, final String prod_id, final int position, final List<Item> itemList, final View v) {

        holder.lv_product_main.setVisibility(View.GONE);
        holder.lv_pb_prod.setVisibility(View.VISIBLE);

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

                        itemList.get(position).setIsWishlisted("true");
                        callWishlistCountApi();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                callWishistApi(prod_id, itemList, position);
                            }
                        }, 0);

                        holder.iv_rifles_add_wishlist.setVisibility(View.GONE);
                        holder.iv_rifles__remove_wishlist.setVisibility(View.VISIBLE);
                        final Animation myAnim = AnimationUtils.loadAnimation(context, R.anim.bounce);
                        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                        myAnim.setInterpolator(interpolator);
                        holder.iv_rifles__remove_wishlist.startAnimation(myAnim);


                        holder.lv_product_main.setVisibility(View.VISIBLE);
                        holder.lv_pb_prod.setVisibility(View.GONE);

                    } else {
                        holder.lv_product_main.setVisibility(View.VISIBLE);
                        holder.lv_pb_prod.setVisibility(View.GONE);

                    }


                } else {
                    holder.lv_product_main.setVisibility(View.VISIBLE);
                    holder.lv_pb_prod.setVisibility(View.GONE);

                    NavigationActivity.get_Customer_tokenapi();
                    CallAddtoWishlistApi_list(holder, prod_id, position, itemList, v);

                    Log.e("debug_error", "=" + response);
                    Log.e("error", "=" + response.body());
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                holder.lv_product_main.setVisibility(View.VISIBLE);
                holder.lv_pb_prod.setVisibility(View.GONE);

                // Log.e("error_wish",""+t);
                Log.e("debug_remivr", "" + t.getMessage());
                Toast.makeText(context, context.getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();

            }
        });

    }



    //get wishlist api
    private void callWishistApi(final String prod_id, final List<Item> itemList, final int position) {
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
                        } else {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.e("product_id", "=" + jsonObject.getString("product_id"));
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
                            Log.e("prod_id_476", "=" + prod_id);
                            Log.e("prod_id_476size", "=" + wishlitProductidList.size());
                            Log.e("position_476", "=" + position);
                            Log.e("position_476", "=" + wishlitItemId.size());

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    if (wishlitProductidList.contains(prod_id)) {
                                        int pos = wishlitProductidList.indexOf(prod_id);
                                        Log.e("wishlist_it222", "=" + pos);
                                        if (wishlitItemId.size() > 0) {
                                            String wishlist_item_id = String.valueOf(wishlitItemId.get(pos));
                                            Log.e("wishlist_item_id333333", "=" + wishlist_item_id);
                                            itemList.get(position).setWishlist_item_id(wishlist_item_id);
                                        }
                                    } else {
                                        itemList.get(position).setWishlist_item_id("0");
                                    }
                                }
                            }, 1000);
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    // Toast.makeText(parent, ""+getFavouriteslist.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(context, "" + context.getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void CallAddtoCartApigrid(MyViewHolder listHolder, String sku) {
        ApiInterface api = ApiClientcusome.getClient().create(ApiInterface.class);
        listHolder.lv_product_main.setVisibility(View.GONE);
        listHolder.lv_pb_prod.setVisibility(View.VISIBLE);
        String url = "http://dkbraende.demoproject.info/rest/V1/carts/mine/items?cartItem[quoteId]=" + Login_preference.getdkQuoteId(context) + "&cartItem[qty]=1" + "&cartItem[sku]=" + sku;
        Log.e("skuu", "=" + sku);
        Log.e("customertokensss", "=" + Login_preference.getCustomertoken(context));
        Log.e("customertokensss", "=" + url);
        Call<ResponseBody> addtocart = api.getaddtocartapi("Bearer " + Login_preference.getCustomertoken(context), url);
        addtocart.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("res_addtocart", "" + response.toString());
                Log.e("resquotaddtocart", "" + response.body());


                if (response.isSuccessful() || response.code() == 200) {
                    JSONObject jsonObject = null;
                    try {
                        try {

                            listHolder.lv_product_main.setVisibility(View.VISIBLE);
                            listHolder.lv_pb_prod.setVisibility(View.GONE);
                            jsonObject = new JSONObject(response.body().string());
                            String name = jsonObject.getString("name");
                            String price = jsonObject.getString("price");
                            String product_type = jsonObject.getString("product_type");
                            String quote_id = jsonObject.getString("quote_id");
                            String sku = jsonObject.getString("sku");
                            String item_id = jsonObject.getString("item_id");
                            String qty = jsonObject.getString("qty");

                            Toast.makeText(context, "Add to cart SuccessFully", Toast.LENGTH_SHORT).show();
                            Log.e("jsonObjectss", "=" + jsonObject);
                            Log.e("names", "=" + name);
                            Log.e("prices", "=" + price);
                            Log.e("product_types", "=" + product_type);
                            Log.e("quote_ids", "=" + quote_id);
                            Log.e("skus", "=" + sku);
                            Log.e("item_ids", "=" + item_id);
                            Log.e("qtys", "=" + qty);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // "message": "The product that was requested doesn't exist. Verify the product and try again."
                    listHolder.lv_product_main.setVisibility(View.VISIBLE);
                    listHolder.lv_pb_prod.setVisibility(View.GONE);

                    NavigationActivity.get_Customer_tokenapi();
                   // CallAddtoCartApigrid(listHolder, sku);
                    Toast.makeText(context, "The product that was requested doesn't exist. Verify the product and try again.", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listHolder.lv_product_main.setVisibility(View.VISIBLE);
                listHolder.lv_pb_prod.setVisibility(View.GONE);
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    // add to cart
    private void CallAddtoCartApi(MyNewHolder listHolder, String sku) {
        ApiInterface api = ApiClientcusome.getClient().create(ApiInterface.class);
        listHolder.lv_prod_verticle_main.setVisibility(View.GONE);
        listHolder.lv_pb_prod_verticle.setVisibility(View.VISIBLE);
        String url = "http://dkbraende.demoproject.info/rest/V1/carts/mine/items?cartItem[quoteId]=" + Login_preference.getdkQuoteId(context) + "&cartItem[qty]=1" + "&cartItem[sku]=" + sku;
        Log.e("skuu", "=" + sku);
        Log.e("customertokensss", "=" + Login_preference.getCustomertoken(context));
        Log.e("customertokensss", "=" + url);
        Call<ResponseBody> addtocart = api.getaddtocartapi("Bearer " + Login_preference.getCustomertoken(context), url);
        addtocart.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("res_addtocart", "" + response.toString());
                Log.e("resqquotaddtocart", "" + response.body());


                if (response.isSuccessful() || response.code() == 200) {
                    JSONObject jsonObject = null;
                    try {
                        try {

                            listHolder.lv_prod_verticle_main.setVisibility(View.VISIBLE);
                            listHolder.lv_pb_prod_verticle.setVisibility(View.GONE);
                            jsonObject = new JSONObject(response.body().string());
                            String name = jsonObject.getString("name");
                            String price = jsonObject.getString("price");
                            String product_type = jsonObject.getString("product_type");
                            String quote_id = jsonObject.getString("quote_id");
                            String sku = jsonObject.getString("sku");
                            String item_id = jsonObject.getString("item_id");
                            String qty = jsonObject.getString("qty");

                            Toast.makeText(context, "Add to cart SuccessFully", Toast.LENGTH_SHORT).show();
                            Log.e("jsonObjectss", "=" + jsonObject);
                            Log.e("names", "=" + name);
                            Log.e("prices", "=" + price);
                            Log.e("product_types", "=" + product_type);
                            Log.e("quote_ids", "=" + quote_id);
                            Log.e("skus", "=" + sku);
                            Log.e("item_ids", "=" + item_id);
                            Log.e("qtys", "=" + qty);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // "message": "The product that was requested doesn't exist. Verify the product and try again."
                    listHolder.lv_prod_verticle_main.setVisibility(View.VISIBLE);
                    listHolder.lv_pb_prod_verticle.setVisibility(View.GONE);

                    NavigationActivity.get_Customer_tokenapi();
                    //CallAddtoCartApi(listHolder, sku);
                    Toast.makeText(context, "The product that was requested doesn't exist. Verify the product and try again.", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listHolder.lv_prod_verticle_main.setVisibility(View.VISIBLE);
                listHolder.lv_pb_prod_verticle.setVisibility(View.GONE);
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }


    //wishlist count
    private void callWishlistCountApi() {
        Log.e("response201tokenff", "" + Login_preference.getCustomertoken(context));
        ApiInterface api = ApiClientcusome.getClient().create(ApiInterface.class);
        Call<ResponseBody> customertoken = api.defaultWishlistCount("Bearer " + Login_preference.getCustomertoken(context));
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
                        Login_preference.set_wishlist_count(context, count);
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
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private Call<ResponseBody> getwishlistdata() {
        ApiInterface api = ApiClientcusome.getClient().create(ApiInterface.class);
        Log.e("debug_11token22", "==" + Login_preference.getCustomertoken(context));
        return api.defaultgetWishlistData("Bearer " + Login_preference.getCustomertoken(context));
    }



}





