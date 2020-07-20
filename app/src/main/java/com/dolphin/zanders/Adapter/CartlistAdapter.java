package com.dolphin.zanders.Adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dolphin.zanders.Activity.NavigationActivity;
import com.dolphin.zanders.Fragment.CartFragment;
import com.dolphin.zanders.Fragment.ProductDetailFragment;
import com.dolphin.zanders.Model.AddToCartModel.AddToCartModel;
import com.dolphin.zanders.Model.AddToWishlit.AddToWishlist;
import com.dolphin.zanders.Model.CartlistModel.Cartlist;
import com.dolphin.zanders.Model.CartlistModel.CartlistInnerData;
import com.dolphin.zanders.Model.RemoveProductFomCart.RemoveCartProductModel;
import com.dolphin.zanders.Model.RemoveProductFomCart.RemoveProduct;
import com.dolphin.zanders.Model.UpdateCartQtyModel.UpdateCartProduct;
import com.dolphin.zanders.Model.UpdateCartQtyModel.UpdateCartQtyModel;
import com.dolphin.zanders.Preference.Login_preference;
import com.dolphin.zanders.R;
import com.dolphin.zanders.Retrofit.ApiClient;
import com.dolphin.zanders.Retrofit.ApiInterface;
import com.dolphin.zanders.Util.CheckNetwork;
import com.dolphin.zanders.Util.MyBounceInterpolator;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dolphin.zanders.Activity.NavigationActivity.drawer;
import static com.dolphin.zanders.Activity.NavigationActivity.tv_bottomcount;
import static com.dolphin.zanders.Activity.NavigationActivity.tv_wishlist_count;
import static com.dolphin.zanders.Fragment.CartFragment.CallCartlistApi;
import static com.dolphin.zanders.Fragment.CartFragment.cartlistAdapter;
import static com.dolphin.zanders.Fragment.CartFragment.lv_cart_Main;
import static com.dolphin.zanders.Fragment.CartFragment.lv_cart_parent;
import static com.dolphin.zanders.Fragment.CartFragment.lv_nodata_cart;
import static com.dolphin.zanders.Fragment.CartFragment.recv_cart;
import static com.dolphin.zanders.Fragment.CartFragment.shimmer_cartlist;
import static com.dolphin.zanders.Fragment.CartFragment.tv_addlinal;
import static com.dolphin.zanders.Fragment.CartFragment.tv_cart_subtotal;
import static com.dolphin.zanders.Fragment.CartFragment.tv_messgenoti;
import static com.dolphin.zanders.Fragment.CartFragment.tv_noting;

public class CartlistAdapter extends RecyclerView.Adapter<CartlistAdapter.MyViewHolder> {
    static ApiInterface apiInterface;
    private Context context;
    public static boolean flag=true;
    private List<CartlistInnerData> cartlistdata;

    public CartlistAdapter(Context context) {
        this.context = context;
        cartlistdata = new ArrayList<>();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cartlist_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        final CartlistInnerData cartlistInnerData1 = cartlistdata.get(position);
        final RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.logo_red);
        requestOptions.error(R.drawable.logo_red);
        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(cartlistInnerData1.getProductThumbnail()).into(holder.iv_cartlist_product);

        NavigationActivity.Check_String_NULL_Value(holder.tv_cartlist_product_name,cartlistInnerData1.getProductName());
        NavigationActivity.Check_String_NULL_Value(holder.tv_cartlist_msrp,cartlistInnerData1.getProductPrice());
        NavigationActivity.Check_String_NULL_Value(holder.et_cart_qty,String.valueOf(cartlistInnerData1.getProductQty()));
        NavigationActivity.Check_String_NULL_Value(holder.tv_cartlist_totle,String.valueOf(cartlistInnerData1.getRowTotal()));

        if(cartlistInnerData1.getUpc()==null)
        {
            holder.tv_upc_title.setVisibility(View.GONE);
            holder.tv_cart_upc.setVisibility(View.GONE);
        }else {
            holder.tv_upc_title.setVisibility(View.VISIBLE);
            holder.tv_cart_upc.setVisibility(View.VISIBLE);
            NavigationActivity.Check_String_NULL_Value(holder.tv_cart_upc,cartlistInnerData1.getUpc());
        }
        if(cartlistInnerData1.getItemNumber()==null)
        {
            holder.tv_cartitem_number_title.setVisibility(View.GONE);
            holder.tv_cart_item_number.setVisibility(View.GONE);
        }else {
            holder.tv_cartitem_number_title.setVisibility(View.VISIBLE);
            holder.tv_cart_item_number.setVisibility(View.VISIBLE);
            NavigationActivity.Check_String_NULL_Value(holder.tv_cart_item_number,cartlistInnerData1.getItemNumber());

        }
        holder.lv_cart_click.setEnabled(true);
        holder.lv_cart_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        holder.lv_cart_click.setEnabled(false);
                        Bundle b=new Bundle();
                        AppCompatActivity activity = (AppCompatActivity) v.getContext();
                        b.putString("product_id",cartlistInnerData1.getProductId());
                        b.putString("wishlist",cartlistInnerData1.getProductId());

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

        holder.et_cart_qty.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    final String productid=cartlistInnerData1.getProductId();
                    String email=Login_preference.getemail(context);
                    Log.e("debug110","sss"+productid);
                    Log.e("debug115","email="+email);
                    Log.e("debug115555","email="+v.getText());

                    if(String.valueOf(v.getText()).equalsIgnoreCase(""))
                    {
                        Toast.makeText(context, context.getString(R.string.quantity), Toast.LENGTH_SHORT).show();
                    }else if(String.valueOf(v.getText()).equalsIgnoreCase("0")){

                        CartFragment.lv_cart_checkout.setAlpha(0.4f);
                       // CartFragment.lv_cart_checkout.setEnabled(false);
                        flag=false;
                        Toast.makeText(context, context.getString(R.string.quantity_messge), Toast.LENGTH_SHORT).show();
                    } else {
                        flag=true;
                     //   CartFragment.lv_cart_checkout.setEnabled(true);
                        if (CheckNetwork.isNetworkAvailable(context)) {
                            callUpdateCartApi(productid, String.valueOf(v.getText()),holder);
                        } else {
                            Toast.makeText(context, context.getString(R.string.internet), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return cartlistdata.size();
    }

    public void addAll(List<CartlistInnerData> cartlistInnerData) {
        cartlistdata.clear();
        for (CartlistInnerData cartlistInnerData1 : cartlistInnerData) {
            add(cartlistInnerData1);
        }
    }

    private void add(CartlistInnerData f) {
        cartlistdata.add(f);
        notifyItemInserted(cartlistdata.size() - 1);
    }

    public void addToWishlistItem(int position) {
        String product_id = cartlistdata.get(position).getProductId();
        if (CheckNetwork.isNetworkAvailable(context)) {
            Log.e("debug114_addtowishlist","productid="+product_id);
            CallAddtoWishlistApi(product_id);
        } else {
            Toast.makeText(context, context.getString(R.string.internet), Toast.LENGTH_SHORT).show();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_cartlist_product;
        TextView tv_cartlist_product_name,tv_cartitem_number_title,tv_upc_title,tv_cart_item_number,tv_cart_upc,tv_cartlist_msrp,tv_cartlist_totle;
        EditText et_cart_qty;
        LinearLayout lv_cart_click;

        public MyViewHolder(View view) {
            super(view);

            tv_cartitem_number_title = view.findViewById(R.id.tv_cartitem_number_title);
            lv_cart_click = view.findViewById(R.id.lv_cart_click);
            iv_cartlist_product = view.findViewById(R.id.iv_cartlist_product);
            tv_cartlist_product_name = view.findViewById(R.id.tv_cartlist_product_name);
            tv_cart_item_number = view.findViewById(R.id.tv_cart_item_number);
            tv_cart_upc = view.findViewById(R.id.tv_cart_upc);
            tv_cartlist_msrp = view.findViewById(R.id.tv_cartlist_msrp);
            et_cart_qty = view.findViewById(R.id.et_cart_qty);
            tv_upc_title = view.findViewById(R.id.tv_upc_title);
            tv_cartlist_totle = view.findViewById(R.id.tv_cartlist_totle);

        }
    }

    public void removeItem(int position) {
        String product_id = cartlistdata.get(position).getProductId();
        Log.e("remove_product_id_113", "" + product_id);
        if (CheckNetwork.isNetworkAvailable(context)) {
            //remove product from cart api calling
            CALL_REMOVE_FROM_CART_API(product_id);
        } else {
            Toast.makeText(context, context.getString(R.string.internet), Toast.LENGTH_SHORT).show();
        }

        cartlistdata.remove(position);
        notifyItemRemoved(position);

    }

    //update cart quantity
    private void callUpdateCartApi(String prod_id, String text, final MyViewHolder holder)
    {
        Log.e("debug_232_update","a"+prod_id);
        Log.e("debug_233_update","b"+text);
        callupdateCartQty(prod_id,text).enqueue(new Callback<UpdateCartQtyModel>() {
            @Override
            public void onResponse(Call<UpdateCartQtyModel> call, Response<UpdateCartQtyModel> response) {
                Log.e("debug_162update",""+response);
                Log.e("debug_167",""+response.body());
                UpdateCartQtyModel updateCartQtyModel = response.body();
                Log.e("status_update",""+updateCartQtyModel.getStatus());
                if (updateCartQtyModel.getStatus().equalsIgnoreCase("Success")) {
                    Log.e("status_update","ok");
                   // Toast.makeText(context, updateCartQtyModel.getMessage()+" update quantity successfully", Toast.LENGTH_SHORT).show();
                    AppCompatActivity activity = (AppCompatActivity) holder.et_cart_qty.getContext();
                    hideKeyboard(activity);

                    CartFragment myFragment = new CartFragment();
                    activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                            0, 0, R.anim.fade_out).setCustomAnimations(R.anim.fade_in,
                            0, 0, R.anim.fade_out).replace(R.id.framlayout, myFragment).addToBackStack(null).commit();

                } else {
                    Toast.makeText(context, updateCartQtyModel.getMessage(), Toast.LENGTH_SHORT).show();
                    AppCompatActivity activity = (AppCompatActivity) holder.et_cart_qty.getContext();
                    hideKeyboard(activity);

                    CartFragment myFragment = new CartFragment();
                    activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                            0, 0, R.anim.fade_out).setCustomAnimations(R.anim.fade_in,
                            0, 0, R.anim.fade_out).replace(R.id.framlayout, myFragment).addToBackStack(null).commit();

                    Log.e("status_wish","not_ok");
                }
            }

            @Override
            public void onFailure(Call<UpdateCartQtyModel> call, Throwable t) {
                Log.e("error_wish",""+t);
                Log.e("debug_remivr", ""+t);
                Toast.makeText(context, context.getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Call<UpdateCartQtyModel> callupdateCartQty(String prod_idpass,String text) {
        Log.e("email",""+Login_preference.getcustomer_id(context));
        Log.e("ipid",""+ prod_idpass);
        Log.e("text",""+ text);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        return apiInterface.updatecart(Login_preference.getemail(context),prod_idpass,text);
    }

    //add to wishlist
    private void CallAddtoWishlistApi( String prod_id) {
        calladdtowishlistApi(prod_id).enqueue(new Callback<AddToWishlist>() {
            @Override
            public void onResponse(Call<AddToWishlist> call, Response<AddToWishlist> response) {
                Log.e("debug_162",""+response);
                Log.e("debug_167",""+response.body());
                AddToWishlist addToWishlist = response.body();
                Log.e("status_wishcart",""+addToWishlist.getStatus());
                if (addToWishlist.getStatus().equalsIgnoreCase("Success")) {
                    Log.e("status_wish","ok");
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

                    Log.e("status_wish","not_ok");
                }
            }

            @Override
            public void onFailure(Call<AddToWishlist> call, Throwable t) {
                Log.e("error_wish",""+t);
                Log.e("debug_remivr", ""+t);
                Toast.makeText(context, context.getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private Call<AddToWishlist> calladdtowishlistApi(String prod_idpass) {
        Log.e("inner_user_wish197",""+Login_preference.getcustomer_id(context));
        Log.e("inner_wish_prod_idpass",""+ prod_idpass);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        return apiInterface.addtowishlist(Login_preference.getcustomer_id(context),prod_idpass);
    }

    //remove product from cart api calling
    private void CALL_REMOVE_FROM_CART_API( String product_id) {
        tv_messgenoti.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        lv_nodata_cart.setVisibility(View.GONE);
        recv_cart.setVisibility(View.VISIBLE);
        lv_cart_Main.setVisibility(View.VISIBLE);
        removefromcart(product_id).enqueue(new Callback<RemoveCartProductModel>() {
            @Override
            public void onResponse(Call<RemoveCartProductModel> call, Response<RemoveCartProductModel> response) {
                RemoveCartProductModel removeproductmodel = response.body();
                if(removeproductmodel.getStatus().equalsIgnoreCase("Success"))
                {
                    NavigationActivity.Check_String_NULL_Value(tv_cart_subtotal,removeproductmodel.getSubtotal());
                    NavigationActivity.Check_String_NULL_Value(tv_addlinal,removeproductmodel.getAdditionalAmountNeededForFreeShipping());
                    Toast.makeText(context, "" + removeproductmodel.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("removeitem_qty2466",""+removeproductmodel.getItemsCount());
                    Log.e("removeitem_qty2466total",""+removeproductmodel.getGrandTotal());
                    Login_preference.setquote_id(context,  removeproductmodel.getQuoteId());
                    String item_count = null,count_wishlist;
                    if(removeproductmodel.getItemsCount()==0 || removeproductmodel.getItemsCount().equals("0.00"))
                    {
                        Login_preference.setCart_item_count(context,"");
                     //   item_count = String.valueOf(removeproductmodel.getItemsCount());
                    }else {
                        Login_preference.setCart_item_count(context,String.valueOf(removeproductmodel.getItemsCount()));
                        item_count = String.valueOf(removeproductmodel.getItemsCount());
                    }



                    if (item_count==null || item_count.equalsIgnoreCase("null") || item_count.equals("")) {
                        Log.e("count_40", "" + String.valueOf(removeproductmodel.getItemsCount()));
                        tv_bottomcount.setVisibility(View.GONE);
                    } else {
                        tv_bottomcount.setVisibility(View.VISIBLE);
                        Log.e("count_80", "" + String.valueOf(removeproductmodel.getItemsCount()));
                        NavigationActivity.Check_String_NULL_Value(tv_bottomcount, String.valueOf(removeproductmodel.getItemsCount()));
                    }
                    if (fetchResults(response) == null || fetchResults(response).size()==0) {
                        Log.e("debug_175", "pages: " + fetchResults(response));
                        lv_nodata_cart.setVisibility(View.VISIBLE);
                        recv_cart.setVisibility(View.GONE);
                        lv_cart_Main.setVisibility(View.GONE);
                        tv_messgenoti.setVisibility(View.GONE);
                        tv_noting.setText(Html.fromHtml(context.getResources().getString(R.string.cartempty)));
                        shimmer_cartlist.stopShimmerAnimation();
                        shimmer_cartlist.setVisibility(View.GONE);
                        tv_bottomcount.setVisibility(View.GONE);
                    }else {
                        Log.e("debug_995", "pages: " + fetchResults(response));
                        recv_cart.setVisibility(View.VISIBLE);
                        lv_cart_Main.setVisibility(View.VISIBLE);
                        List<RemoveProduct> results = fetchResults(response);
                        List<RemoveProduct> product_arr = response.body().getProduct();
                        if(product_arr.isEmpty())
                        {
                            lv_cart_Main.setVisibility(View.GONE);
                            recv_cart.setVisibility(View.GONE);
                            lv_nodata_cart.setVisibility(View.VISIBLE);
                            tv_messgenoti.setVisibility(View.GONE);
                            tv_noting.setText(Html.fromHtml(context.getResources().getString(R.string.cartempty)));
                            shimmer_cartlist.stopShimmerAnimation();
                            shimmer_cartlist.setVisibility(View.GONE);
                            tv_bottomcount.setVisibility(View.GONE);
                        }else {
                            tv_bottomcount.setVisibility(View.VISIBLE);
                            lv_cart_Main.setVisibility(View.VISIBLE);
                            recv_cart.setVisibility(View.VISIBLE);
                            lv_nodata_cart.setVisibility(View.GONE);
                        }
                        shimmer_cartlist.stopShimmerAnimation();
                        shimmer_cartlist.setVisibility(View.GONE);
                    }
                }else {
                    lv_cart_Main.setVisibility(View.VISIBLE);
                    recv_cart.setVisibility(View.VISIBLE);
                    lv_nodata_cart.setVisibility(View.GONE);
                    tv_messgenoti.setVisibility(View.GONE);
                    tv_noting.setText(Html.fromHtml(context.getResources().getString(R.string.cartempty)));
                    shimmer_cartlist.stopShimmerAnimation();
                    shimmer_cartlist.setVisibility(View.GONE);
                    //tv_bottomcount.setVisibility(View.GONE);
                    Log.e("debug_244else",""+removeproductmodel.getMessage());
                    Toast.makeText(context, "" + removeproductmodel.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<RemoveCartProductModel> call, Throwable t) {
                Log.e("debug_remivr", ""+t);
                lv_cart_Main.setVisibility(View.VISIBLE);
                recv_cart.setVisibility(View.VISIBLE);
                lv_nodata_cart.setVisibility(View.GONE);
                tv_messgenoti.setVisibility(View.GONE);
                tv_noting.setText(Html.fromHtml(context.getResources().getString(R.string.cartempty)));
                shimmer_cartlist.stopShimmerAnimation();
                shimmer_cartlist.setVisibility(View.GONE);
                tv_bottomcount.setVisibility(View.GONE);
                Toast.makeText(context, context.getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<RemoveProduct> fetchResults(Response<RemoveCartProductModel> response) {
        RemoveCartProductModel removeproductmodel = response.body();
        return removeproductmodel.getProduct();
    }

    public Call<RemoveCartProductModel> removefromcart(String product_id) {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        return apiInterface.removeproductFromCart(
                Login_preference.getemail(context),
                product_id);
    }
    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideKeyboard((Activity) context);
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
}