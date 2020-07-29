package com.dolphin.zanders.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dolphin.zanders.Activity.NavigationActivity;
import com.dolphin.zanders.Fragment.FavouriteFragment;
import com.dolphin.zanders.Fragment.NewCartFragment;
import com.dolphin.zanders.Fragment.NewProductDetail_Fragment;
import com.dolphin.zanders.Fragment.ProductDetailFragment;
import com.dolphin.zanders.Fragment.WishlistNewFragment;
import com.dolphin.zanders.Model.AddToCartModel.AddToCartModel;
import com.dolphin.zanders.Model.FavouriteModel.Product;
import com.dolphin.zanders.Model.RemoveWishlistModel.RemoveFromWishlistModel;
import com.dolphin.zanders.Model.RemoveWishlistModel.WishlistProduct;
import com.dolphin.zanders.Model.WishlistModel;
import com.dolphin.zanders.Preference.Login_preference;
import com.dolphin.zanders.R;
import com.dolphin.zanders.Retrofit.ApiClient;
import com.dolphin.zanders.Retrofit.ApiClientcusome;
import com.dolphin.zanders.Retrofit.ApiInterface;
import com.dolphin.zanders.Util.CheckNetwork;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dolphin.zanders.Activity.NavigationActivity.bottom_navigation;
import static com.dolphin.zanders.Activity.NavigationActivity.tv_bottomcount;
import static com.dolphin.zanders.Fragment.NewProductListFragment.lv_main_productlist;
import static com.dolphin.zanders.Fragment.NewProductListFragment.lv_productlist_progress;
import static com.dolphin.zanders.Fragment.WishlistNewFragment.lv_main_favourite;
import static com.dolphin.zanders.Fragment.WishlistNewFragment.lv_no_data_found_wishlist;
import static com.dolphin.zanders.Fragment.WishlistNewFragment.lv_progress_favoutite_bottom;
import static com.dolphin.zanders.Fragment.WishlistNewFragment.lv_wishlist_progress;
import static com.dolphin.zanders.Fragment.WishlistNewFragment.recv_favourites;
import static com.dolphin.zanders.Fragment.WishlistNewFragment.shimmer_view_favourites;
import static java.lang.Double.NaN;


public class NewWishListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    private List<WishlistModel> productList;
    ApiInterface getwish;
    static String prod_id;
    LayoutInflater inflater;
    ApiInterface apiInterface;

    public NewWishListAdapter(Context context, List<WishlistModel> model) {
        this.context = context;
        this.productList = model;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.wishlist_row, parent, false);
        return new com.dolphin.zanders.Adapter.NewWishListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        getwish = ApiClient.getClient().create(ApiInterface.class);
        final WishlistModel product = productList.get(position);

        Log.e("debug822", "=" + product.getSpecial_price());
        Log.e("debug822", "=" + product.getPrice());
        Log.e("debug822", "=" + product.getName());
        prod_id = product.getProduct_id();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        NewWishListAdapter.MyViewHolder myViewHolder = (NewWishListAdapter.MyViewHolder) holder;

        NavigationActivity.Check_String_NULL_Value(myViewHolder.tv_wishlist_product_name, product.getName());

        if (product.getSpecial_price().equals(NaN) || product.getSpecial_price() == NaN || product.getSpecial_price() == null || product.getSpecial_price() == 0) {
            myViewHolder.lv_special.setVisibility(View.INVISIBLE);
            NavigationActivity.Check_String_NULL_Value(myViewHolder.tv_wishlist_msrp, product.getPrice() + " " + Login_preference.getcurrencycode(context));

        } else {
            myViewHolder.tv_wishlist_msrp.setPaintFlags(myViewHolder.tv_wishlist_msrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            //  myViewHolder.tv_wishlist_msrp_title.setPaintFlags(myViewHolder.tv_wishlist_msrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            if (product.getSpecial_price() == null) {
                myViewHolder.lv_special.setVisibility(View.INVISIBLE);
            } else {
                myViewHolder.lv_special.setVisibility(View.VISIBLE);
                NavigationActivity.Check_String_NULL_Value(myViewHolder.tv_wishlist_msrp, product.getPrice() + " " + Login_preference.getcurrencycode(context));
                NavigationActivity.Check_String_NULL_Value(myViewHolder.tv_wishlist_special_price, product.getSpecial_price() + " " + Login_preference.getcurrencycode(context));
            }

        }


        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.logo_app);
        requestOptions.error(R.drawable.logo_app);
        Log.e("debug_fav_image", "" + product.getThumbnail());
        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load("http://dkbraende.demoproject.info/pub/media/catalog/product" + product.getThumbnail()).into(myViewHolder.iv_wishlist_product);

        myViewHolder.lv_productwish_click.setEnabled(true);

        myViewHolder.lv_productwish_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final Handler handler = new Handler();
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Bundle b = new Bundle();
                b.putString("product_id", String.valueOf(productList.get(position).getProduct_id()));
                b.putString("product_name", String.valueOf(productList.get(position).getName()));
                NewProductDetail_Fragment myFragment = new NewProductDetail_Fragment();
                myFragment.setArguments(b);
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.fade_in,
                                0, 0, R.anim.fade_out)
                        .setCustomAnimations(R.anim.fade_in,
                                0, 0, R.anim.fade_out)
                        .add(R.id.framlayout, myFragment)
                        .addToBackStack(null).commit();

            }
        });

        ((MyViewHolder) holder).lv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemid = String.valueOf(productList.get(position).getWishlist_item_id());
                Log.e("debg", "=" + itemid);
                callRemoveFromCartApi(itemid, position, v);
            }
        });

        myViewHolder.lv_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckNetwork.isNetworkAvailable(context)) {

                    if (Login_preference.getLogin_flag(context).equalsIgnoreCase("1")) {
                        String sku = productList.get(position).getSku();
                        CallAddtoCartApi(sku);
                    }

                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.intcon), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public Call<ResponseBody> callcartlistapi() {
        ApiInterface api = ApiClientcusome.getClient().create(ApiInterface.class);
        Log.e("email_237", "=" + Login_preference.getCustomertoken(context));
        return api.getcartlistapi("Bearer " + Login_preference.getCustomertoken(context));
    }

    private void callCartCcountApi() {
        callcartlistapi().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody cartlist = response.body();
                if (response.code() == 200 || response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().string());
                        Log.e("jsonArray111", "=" + jsonArray.length());
                        if (jsonArray.length() == 0) {
                            if (context != null) {
                                String cartitem_count = String.valueOf(jsonArray.length());
                                BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottom_navigation.getChildAt(0);
                                //cart item count
                                BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(3);
                                View notificationBadge = LayoutInflater.from(context).inflate(R.layout.badge_row, menuView, false);
                                tv_bottomcount = (TextView) notificationBadge.findViewById(R.id.badge);
                                if (cartitem_count.equalsIgnoreCase("null") || cartitem_count.equals("") || cartitem_count.equals("0")) {
                                    tv_bottomcount.setVisibility(View.GONE);
                                    Login_preference.setCart_item_count(context,"0");
                                } else {
                                    tv_bottomcount.setVisibility(View.VISIBLE);
                                    tv_bottomcount.setText(cartitem_count);
                                    Login_preference.setCart_item_count(context,cartitem_count);
                                }
                                itemView.addView(notificationBadge);
                            }
                        } else {
                            if (context != null) {
                                String cartitem_count = String.valueOf(jsonArray.length());
                                BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottom_navigation.getChildAt(0);
                                //cart item count
                                BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(3);
                                NavigationActivity.notificationBadge = LayoutInflater.from(context).inflate(R.layout.badge_row, menuView, false);
                                tv_bottomcount = (TextView) NavigationActivity.notificationBadge.findViewById(R.id.badge);
                                if (cartitem_count.equalsIgnoreCase("null") || cartitem_count.equals("") || cartitem_count.equals("0")) {
                                    tv_bottomcount.setVisibility(View.GONE);
                                    Login_preference.setCart_item_count(context,"0");
                                } else {
                                    tv_bottomcount.setVisibility(View.VISIBLE);
                                    tv_bottomcount.setText(cartitem_count);
                                    Login_preference.setCart_item_count(context,cartitem_count);
                                }
                                itemView.addView(NavigationActivity.notificationBadge);
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    NavigationActivity.get_Customer_tokenapi();
                    //  CallCartlistApi();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "" + context.getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();
                Log.e("debug_175125", "pages: " + t);
            }
        });
    }

    private void CallAddtoCartApi(String sku) {
        ApiInterface api = ApiClientcusome.getClient().create(ApiInterface.class);
        lv_main_favourite.setVisibility(View.GONE);
        lv_wishlist_progress.setVisibility(View.VISIBLE);


        String url = "http://dkbraende.demoproject.info/rest/V1/carts/mine/items?cartItem[quoteId]=" + Login_preference.getdkQuoteId(context) + "&cartItem[qty]=1" + "&cartItem[sku]=" + sku;
        Log.e("skuu", "=" + sku);
        Log.e("customertokensss", "=" + Login_preference.getCustomertoken(context));
        Log.e("getdkQuoteId", "=" + Login_preference.getdkQuoteId(context));
        Log.e("customertokensss", "=" + url);
        Call<ResponseBody> addtocart = api.getaddtocartapi("Bearer " + Login_preference.getCustomertoken(context), url);
        addtocart.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("res_addtocart", "" + response.toString());
                Log.e("resquotaddtocart", "" + response.body());


                if (response.isSuccessful() || response.code() == 200) {

                    shimmer_view_favourites.stopShimmerAnimation();
                    shimmer_view_favourites.setVisibility(View.GONE);
                    lv_no_data_found_wishlist.setVisibility(View.GONE);
                    lv_main_favourite.setVisibility(View.VISIBLE);

                    lv_wishlist_progress.setVisibility(View.GONE);


                    JSONObject jsonObject = null;
                    try {
                        try {
                            jsonObject = new JSONObject(response.body().string());
                            String qty = jsonObject.getString("qty");
                            Toast.makeText(context, "Add to cart SuccessFully", Toast.LENGTH_SHORT).show();
                            Log.e("qtys", "=" + qty);
                            callCartCcountApi();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == 400) {
                    shimmer_view_favourites.stopShimmerAnimation();
                    shimmer_view_favourites.setVisibility(View.GONE);
                    lv_no_data_found_wishlist.setVisibility(View.GONE);
                    lv_main_favourite.setVisibility(View.VISIBLE);
                    lv_wishlist_progress.setVisibility(View.GONE);
                    Toast.makeText(context, "" + context.getResources().getString(R.string.productoutofstock), Toast.LENGTH_SHORT).show();


                } else if (response.code() == 401) {
                    shimmer_view_favourites.stopShimmerAnimation();
                    shimmer_view_favourites.setVisibility(View.GONE);
                    lv_no_data_found_wishlist.setVisibility(View.GONE);
                    lv_main_favourite.setVisibility(View.VISIBLE);
                    lv_wishlist_progress.setVisibility(View.GONE);
                    NavigationActivity.get_Customer_tokenapi();
                    //CallAddtoCartApi();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }


    @Override
    public int getItemCount() {
        return productList == null ? 0 : productList.size();
    }


    private void callRemoveFromCartApi(String itemid, int position, View v) {

        lv_no_data_found_wishlist.setVisibility(View.GONE);
        lv_progress_favoutite_bottom.setVisibility(View.GONE);
        recv_favourites.setVisibility(View.VISIBLE);
        lv_main_favourite.setVisibility(View.VISIBLE);
        callRemovecartapi(itemid).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                Boolean paymentMehtodModel = response.body();

                Log.e("resaaaremove wishlist", "=" + response.code());
                Log.e("resaaacccc", "=" + response);

//                    Log.e("res_payment", "aaa" + response.body().toString());
                //progressBar_review.setVisibility(View.GONE);

                //  lv_cartlist_progress.setVisibility(View.GONE);
                // lv_cart_Main.setVisibility(View.VISIBLE);


                if (response.code() == 200) {
                    //    CallCartlistApi();
                    // cartlistdata.remove(position);
                    // cartlistAdapter.notifyDataSetChanged();
                    Toast.makeText(context, "Product Remove from Wishlist successfully", Toast.LENGTH_SHORT).show();


                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    WishlistNewFragment myFragment = new WishlistNewFragment();

                    activity.getSupportFragmentManager()
                            .beginTransaction().setCustomAnimations(R.anim.fade_in,
                            0, 0, R.anim.fade_out).addToBackStack(null).
                            replace(R.id.framlayout, myFragment).commit();
                } else {

                }


            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                // String error=  t.printStackTrace();
                Toast.makeText(context, "" + context.getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();
                Log.e("debug_175125", "pages: " + t);
            }
        });
    }


    private Call<Boolean> callRemovecartapi(String itemid) {

        ApiInterface api = ApiClientcusome.getClient().create(ApiInterface.class);
        Log.e("debug_11", "==" + itemid);
        ///http://dkbraende.demoproject.info/rest//V1/carts/mine/items/162920


        String url = ApiClientcusome.MAIN_URLL + "wishlist/delete/" + itemid;
        Log.e("url11", "==" + url);
        return api.removeitemfromWishlistt("Bearer " + Login_preference.getCustomertoken(context), url);
    }


    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public WishlistModel getItem(int position) {
        Log.e("pos_galada", "" + position);
        return productList.get(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_wishlist_product;
        TextView tv_wishlist_product_name, tv_wishlist_msrp_title, tv_wishlist_msrp, tv_wishlist_special_price;
        LinearLayout lv_special, lv_productwish_click, lv_delete, lv_cart;

        public MyViewHolder(@NonNull View view) {
            super(view);

            lv_cart = view.findViewById(R.id.lv_cart);
            lv_delete = view.findViewById(R.id.lv_delete);
            lv_productwish_click = view.findViewById(R.id.lv_productwish_click);
            lv_special = view.findViewById(R.id.lv_special);
            iv_wishlist_product = view.findViewById(R.id.iv_wishlist_product);
            tv_wishlist_product_name = view.findViewById(R.id.tv_wishlist_product_name);
            tv_wishlist_msrp_title = view.findViewById(R.id.tv_wishlist_msrp_title);
            tv_wishlist_msrp = view.findViewById(R.id.tv_wishlist_msrp);
            tv_wishlist_special_price = view.findViewById(R.id.tv_wishlist_special_price);

        }
    }


}


