package com.dolphin.zanders.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dolphin.zanders.Activity.NavigationActivity;
import com.dolphin.zanders.Fragment.FavouriteFragment;
import com.dolphin.zanders.Fragment.LoginFragment;
import com.dolphin.zanders.Fragment.ProductDetailFragment;
import com.dolphin.zanders.Model.AddToCartModel.AddToCartModel;
import com.dolphin.zanders.Model.AddToWishlit.AddToWishlist;
import com.dolphin.zanders.Model.FavouriteModel.Product;
import com.dolphin.zanders.Model.ProductlistModel.GetCategoryProductlistInnerData;
import com.dolphin.zanders.Model.RemoveProductFomCart.RemoveCartProductModel;
import com.dolphin.zanders.Model.RemoveProductFomCart.RemoveProduct;
import com.dolphin.zanders.Model.RemoveWishlistModel.RemoveFromWishlistModel;
import com.dolphin.zanders.Model.RemoveWishlistModel.WishlistProduct;
import com.dolphin.zanders.Preference.Login_preference;
import com.dolphin.zanders.R;
import com.dolphin.zanders.Retrofit.ApiClient;
import com.dolphin.zanders.Retrofit.ApiInterface;
import com.dolphin.zanders.Util.CheckNetwork;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dolphin.zanders.Activity.NavigationActivity.drawer;
import static com.dolphin.zanders.Activity.NavigationActivity.tv_bottomcount;

import static com.dolphin.zanders.Activity.NavigationActivity.tv_wishlist_count;
import static com.dolphin.zanders.Fragment.FavouriteFragment.lv_main_favourite;
import static com.dolphin.zanders.Fragment.FavouriteFragment.lv_no_data_found_wishlist;
import static com.dolphin.zanders.Fragment.FavouriteFragment.lv_progress_favoutite_bottom;
import static com.dolphin.zanders.Fragment.FavouriteFragment.nested_scroll_favourits;
import static com.dolphin.zanders.Fragment.FavouriteFragment.recv_favourites;
import static com.dolphin.zanders.Fragment.FavouriteFragment.shimmer_view_favourites;

public class Favourites_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    private List<Product> productList;
    ApiInterface getwish;
    static String prod_id;
    LayoutInflater inflater;
    ApiInterface apiInterface;

    public Favourites_Adapter(Context context, List<Product> model) {
        this.context = context;
        this.productList = model;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.wishlist_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        getwish = ApiClient.getClient().create(ApiInterface.class);
        final Product product = productList.get(position);
        prod_id = product.getProductId();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        final MyViewHolder myViewHolder = (MyViewHolder) holder;
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.logo_red);
        requestOptions.error(R.drawable.logo_red);
        Log.e("debug_fav_image",""+product.getProductThumbnail());
        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(product.getProductThumbnail()).into(myViewHolder.iv_wishlist_product);

        NavigationActivity.Check_String_NULL_Value(myViewHolder.tv_wishlist_product_name, product.getProductName());
        NavigationActivity.Check_String_NULL_Value(myViewHolder.tv_wishlist_product_name, product.getProductName());
        //  myViewHolder.tv_wishlist_product_name.setText(product.getProductName());
        //   myViewHolder.tv_wishlist_msrp.setText(product.getProductPrice());

        if (product.getProductSpecialprice().equalsIgnoreCase("") == true) {
            myViewHolder.lv_special.setVisibility(View.GONE);
            NavigationActivity.Check_String_NULL_Value(myViewHolder.tv_wishlist_msrp, product.getProductPrice());

        } else {
            myViewHolder.tv_wishlist_msrp.setPaintFlags(myViewHolder.tv_wishlist_msrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            myViewHolder.lv_special.setVisibility(View.VISIBLE);
            NavigationActivity.Check_String_NULL_Value(myViewHolder.tv_wishlist_msrp, product.getProductPrice());
            NavigationActivity.Check_String_NULL_Value(myViewHolder.tv_wishlist_special_price, product.getProductSpecialprice());
        }
        myViewHolder.lv_productwish_click.setEnabled(true);
        myViewHolder.lv_productwish_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                      //  myViewHolder.lv_productwish_click.setEnabled(false);
                        Bundle b = new Bundle();
                        b.putString("product_id", product.getProductId());
                        b.putString("wishlist", product.getProductId());

                        AppCompatActivity activity = (AppCompatActivity) v.getContext();
                        ProductDetailFragment myFragment = new ProductDetailFragment();
                        myFragment.setArguments(b);
                        activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                                0, 0, R.anim.fade_out).setCustomAnimations(R.anim.fade_in,
                                0, 0, R.anim.fade_out).addToBackStack(null).replace(R.id.framlayout, myFragment).addToBackStack(null).commit();
                        if (drawer.isDrawerOpen(GravityCompat.START)) {
                            drawer.closeDrawer(GravityCompat.START);
                        }
                    }
                }, 100);
            }
        });

    }

    @Override
    public int getItemCount() {
        return productList == null ? 0 : productList.size();
    }


    public void remove(Product r) {
        int position = productList.indexOf(r);
        if (position > -1) {
            productList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void removeItem(int position) {
        String product_id = productList.get(position).getProductId();
        Log.e("remove_product_id_113", "" + product_id);
        if (CheckNetwork.isNetworkAvailable(context)) {
            //remove product from wishlist api calling
            CALL_REMOVE_FROM_WISHLIST_API(product_id);
        } else {
            Toast.makeText(context, context.getString(R.string.internet), Toast.LENGTH_SHORT).show();
        }
        productList.remove(position);
        notifyItemRemoved(position);
    }

    public void addToCartItem(int position) {
        String product_id = productList.get(position).getProductId();
        if (CheckNetwork.isNetworkAvailable(context)) {
            Log.e("debug_174addtocart", "productid=" + product_id);
            callAddtoCartApi(product_id);
        } else {
            Toast.makeText(context, context.getString(R.string.internet), Toast.LENGTH_SHORT).show();
        }

    }

    private void callAddtoCartApi(String product_id) {

        addtocart(product_id).enqueue(new Callback<AddToCartModel>() {
            @Override
            public void onResponse(Call<AddToCartModel> call, Response<AddToCartModel> response) {

                AddToCartModel model = response.body();
                Log.e("response_2333", "" + response.body());
                Log.e("response33", "" + response);
                Log.e("debug_258messs", "" + model.getMessage());
                Log.e("quote_iddddd_217", "" + model.getQuoteId());

                Log.e("debug_243qty", "" + model.getItemsCount());
                if (model.getStatus().equalsIgnoreCase("Success")) {
                    Toast.makeText(context, "" + model.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("quote_iddddd_217", "" + model.getQuoteId());
                    Log.e("item_qty2466", "" + model.getItemsCount());
                    Log.e("item_qty2466total", "" + model.getGrandTotal());
                    Login_preference.setquote_id(context, model.getQuoteId());
                    Login_preference.setCart_item_count(context, model.getItemsCount());
                    String item_count = String.valueOf(model.getItemsCount());
                    Log.e("item_qty", "" + item_count);

                    if (item_count == null || item_count.equalsIgnoreCase("null") || item_count.equals("")) {
                        Log.e("count_40", "" + String.valueOf(model.getItemsCount()));
                        tv_bottomcount.setVisibility(View.GONE);
                    } else {
                        tv_bottomcount.setVisibility(View.VISIBLE);
                        Log.e("count_80", "" + String.valueOf(model.getItemsCount()));
                        NavigationActivity.Check_String_NULL_Value(tv_bottomcount, String.valueOf(model.getItemsCount()));
                    }
                } else {
                    Log.e("debug_244else", "" + model.getMessage());
                    Toast.makeText(context, "" + model.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddToCartModel> call, Throwable t) {
                // lv_product_progress.setVisibility(View.GONE);
                //  coordinator_product_main.setVisibility(View.VISIBLE);
                Toast.makeText(context, context.getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Call<AddToCartModel> addtocart(String product_id) {
        Log.e("emaikl_262", " " + Login_preference.getemail(context));
        Log.e("emaikl_262product_id", " " + product_id);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        return apiInterface.addToCart(
                Login_preference.getemail(context),
                product_id);
    }

    private List<WishlistProduct> fetchResults(Response<RemoveFromWishlistModel> response) {
        RemoveFromWishlistModel removeproductmodel = response.body();
        return removeproductmodel.getProduct();
    }
    private void CALL_REMOVE_FROM_WISHLIST_API(String product_id) {
        lv_no_data_found_wishlist.setVisibility(View.GONE);
        lv_progress_favoutite_bottom.setVisibility(View.GONE);
        recv_favourites.setVisibility(View.VISIBLE);
        lv_main_favourite.setVisibility(View.VISIBLE);
        callremovewishlistApi(product_id).enqueue(new Callback<RemoveFromWishlistModel>() {
            @Override
            public void onResponse(Call<RemoveFromWishlistModel> call, Response<RemoveFromWishlistModel> response) {

                RemoveFromWishlistModel removeFromWishlistModel = response.body();

                if (removeFromWishlistModel.getStatus().equalsIgnoreCase("Success")) {

                    Toast.makeText(context, removeFromWishlistModel.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("remove_wishlist", "" + removeFromWishlistModel.getCount());

                    String item_count = null;
                    if(removeFromWishlistModel.getCount()==0 || removeFromWishlistModel.getCount().equals("0.00"))
                    {
                        Login_preference.set_wishlist_count(context,"");
                        //   item_count = String.valueOf(removeproductmodel.getItemsCount());
                    }else {
                        Login_preference.set_wishlist_count(context,String.valueOf(removeFromWishlistModel.getCount()));
                        item_count = String.valueOf(removeFromWishlistModel.getCount());
                    }
                    if (item_count == null || item_count.equalsIgnoreCase("null") || item_count.equals("")) {
                        Log.e("count_40", "" + String.valueOf(removeFromWishlistModel.getCount()));
                       tv_wishlist_count.setVisibility(View.GONE);
                    } else {
                        tv_wishlist_count.setVisibility(View.VISIBLE);
                        Log.e("count_80", "" + String.valueOf(removeFromWishlistModel.getCount()));
                        NavigationActivity.Check_String_NULL_Value(tv_wishlist_count, String.valueOf(removeFromWishlistModel.getCount()));
                    }
                    if (fetchResults(response) == null || fetchResults(response).size() == 0) {
                        Log.e("debug_175", "pages: " + fetchResults(response));
                        lv_no_data_found_wishlist.setVisibility(View.VISIBLE);
                        nested_scroll_favourits.setVisibility(View.GONE);
                         shimmer_view_favourites.stopShimmerAnimation();
                        shimmer_view_favourites.setVisibility(View.GONE);
                        tv_wishlist_count.setVisibility(View.GONE);
                    } else {
                        Log.e("debug_995", "pages: " + fetchResults(response));
                        recv_favourites.setVisibility(View.VISIBLE);
                        nested_scroll_favourits.setVisibility(View.VISIBLE);
                        List<WishlistProduct> results = fetchResults(response);
                        List<WishlistProduct> product_arr = response.body().getProduct();
                        if (product_arr.isEmpty()) {
                            nested_scroll_favourits.setVisibility(View.GONE);
                            recv_favourites.setVisibility(View.GONE);
                            lv_no_data_found_wishlist.setVisibility(View.VISIBLE);
                            shimmer_view_favourites.stopShimmerAnimation();
                            shimmer_view_favourites.setVisibility(View.GONE);
                            tv_wishlist_count.setVisibility(View.GONE);
                        } else {
                            tv_wishlist_count.setVisibility(View.VISIBLE);
                            nested_scroll_favourits.setVisibility(View.VISIBLE);
                            recv_favourites.setVisibility(View.VISIBLE);
                            lv_no_data_found_wishlist.setVisibility(View.GONE);
                        }
                        shimmer_view_favourites.stopShimmerAnimation();
                        shimmer_view_favourites.setVisibility(View.GONE);
                    }
                } else {
                    nested_scroll_favourits.setVisibility(View.VISIBLE);
                    recv_favourites.setVisibility(View.VISIBLE);
                    lv_no_data_found_wishlist.setVisibility(View.GONE);
                    shimmer_view_favourites.stopShimmerAnimation();
                    shimmer_view_favourites.setVisibility(View.GONE);
                    Toast.makeText(context, removeFromWishlistModel.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<RemoveFromWishlistModel> call, Throwable t) {
                nested_scroll_favourits.setVisibility(View.VISIBLE);
                recv_favourites.setVisibility(View.VISIBLE);
                lv_no_data_found_wishlist.setVisibility(View.GONE);
                shimmer_view_favourites.stopShimmerAnimation();
                shimmer_view_favourites.setVisibility(View.GONE);
                Toast.makeText(context, context.getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Call<RemoveFromWishlistModel> callremovewishlistApi(String prod_idpass) {
        Log.e("customer_idd_fav", "" + Login_preference.getcustomer_id(context));
        Log.e("prod_idd_fav", "" + prod_idpass);
        return getwish.removewishlist(Login_preference.getcustomer_id(context), prod_idpass);
    }


    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public Product getItem(int position) {
        Log.e("pos_galada", "" + position);
        return productList.get(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_wishlist_product;
        TextView tv_wishlist_product_name, tv_wishlist_msrp_title, tv_wishlist_msrp, tv_wishlist_special_price;
        LinearLayout lv_special, lv_productwish_click;

        public MyViewHolder(@NonNull View view) {
            super(view);

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
