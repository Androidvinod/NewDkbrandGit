package com.dolphin.zanders.Adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dolphin.zanders.Activity.NavigationActivity;

import com.dolphin.zanders.Fragment.LoginFragment;
import com.dolphin.zanders.Fragment.NewCartFragment;
import com.dolphin.zanders.Model.NewCartListModel;
import com.dolphin.zanders.Model.PaymentMethodModel.PaymentMethod;
import com.dolphin.zanders.Model.RemoveProductFomCart.RemoveCartProductModel;
import com.dolphin.zanders.Model.RemoveProductFomCart.RemoveProduct;
import com.dolphin.zanders.Model.UpdateCartQtyModel.UpdateCartQtyModel;
import com.dolphin.zanders.Preference.Login_preference;
import com.dolphin.zanders.R;
import com.dolphin.zanders.Retrofit.ApiClient;
import com.dolphin.zanders.Retrofit.ApiClientcusome;
import com.dolphin.zanders.Retrofit.ApiInterface;
import com.dolphin.zanders.Util.CheckNetwork;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import static com.dolphin.zanders.Activity.NavigationActivity.tv_bottomcount;

import static com.dolphin.zanders.Fragment.NewCartFragment.CallCartlistApi;
import static com.dolphin.zanders.Fragment.NewCartFragment.cartlistAdapter;
import static com.dolphin.zanders.Fragment.NewCartFragment.lv_cart_Main;
import static com.dolphin.zanders.Fragment.NewCartFragment.lv_cartlist_progress;
import static com.dolphin.zanders.Fragment.NewCartFragment.lv_nodata_cart;
import static com.dolphin.zanders.Fragment.NewCartFragment.lv_subtotal_layout;
import static com.dolphin.zanders.Fragment.NewCartFragment.recv_cart;
import static com.dolphin.zanders.Fragment.NewCartFragment.shimmer_cartlist;
import static com.dolphin.zanders.Fragment.NewCartFragment.tv_addlinal;
import static com.dolphin.zanders.Fragment.NewCartFragment.tv_cart_subtotal;
import static com.dolphin.zanders.Fragment.NewCartFragment.tv_messgenoti;
import static com.dolphin.zanders.Fragment.NewCartFragment.tv_noting;
import static com.dolphin.zanders.Fragment.ProductListFragment.subcat_id;
import static com.dolphin.zanders.Fragment.ProductListFragment.subcatename;


public class NewCartListAdapter extends RecyclerView.Adapter<com.dolphin.zanders.Adapter.NewCartListAdapter.MyViewHolder> {
        static ApiInterface api;
        private Context context;
        public static boolean flag = true;
        boolean isFlag=true;
        private List<NewCartListModel> cartlistdata;
        NavigationActivity parent;


        public NewCartListAdapter(Context context, List<NewCartListModel> cartlistdata) {
            this.context = context;
            this.cartlistdata = cartlistdata;
            parent = (NavigationActivity) context;
        }

        @Override
        public com.dolphin.zanders.Adapter.NewCartListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.newcart_row, parent, false);
            return new com.dolphin.zanders.Adapter.NewCartListAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final com.dolphin.zanders.Adapter.NewCartListAdapter.MyViewHolder holder, final int position) {
            api = ApiClient.getClient().create(ApiInterface.class);

            api = ApiClient.getClient().create(ApiInterface.class);
            final NewCartListModel cart_model = cartlistdata.get(position);
            Log.e("ada_cart_pdname",""+cart_model.getName());
            Log.e("ada_cart_pdprice",""+cart_model.getPrice());
            Log.e("ada_cart_pdquote",""+cart_model.getQuoteId());
            Log.e("ada_cart_pdimagee",""+cart_model.getImg());

            //Log.e("ada_cart_pdimage",""+cart_model.getExtensionAttributes());

         /*   holder.tv_cartlist_product_name.setTypeface(SplashActivity.montserrat_semibold);
            holder.tv_cart_sku.setTypeface(SplashActivity.montserrat_medium);
            holder.et_cart_qty.setTypeface(SplashActivity.montserrat_medium);
            holder.tv_cartlist_price.setTypeface(SplashActivity.montserrat_semibold);*/

            holder.tv_cartlist_product_name.setText(cart_model.getName());
            holder.tv_cartlist_msrp.setText(String.valueOf(cart_model.getPrice())+" "+Login_preference.getcurrencycode(context));
            holder.tv_cartlist_product_sku.setText(cart_model.getSku());
            holder.et_cart_qty.setText(String.valueOf(cart_model.getQty()));


            final RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.logo_red);
            requestOptions.error(R.drawable.logo_red);
            Glide.with(context)
                    .setDefaultRequestOptions(requestOptions)
                    .load(cart_model.getImg()).into(holder.iv_cartlist_product);


            holder.lv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String itemid= String.valueOf(cartlistdata.get(position).getItemId());
                    Log.e("debug_109","="+itemid);
                    callRemoveFromCartApi(itemid,position,v);
                }
            });








        /*holder.et_cart_qty.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String model_qty= cart_model.getQty();

                    if (String.valueOf( holder.et_cart_qty.getText()).equalsIgnoreCase("")) {
                        Toast.makeText(context, context.getString(R.string.quantity), Toast.LENGTH_SHORT).show();
                    } else if (String.valueOf( holder.et_cart_qty.getText()).equalsIgnoreCase("0")||String.valueOf( holder.et_cart_qty.getText()).equalsIgnoreCase("00")
                            ||String.valueOf( holder.et_cart_qty.getText()).equalsIgnoreCase("000")||String.valueOf( holder.et_cart_qty.getText()).equalsIgnoreCase("0000")
                            ||String.valueOf( holder.et_cart_qty.getText()).equalsIgnoreCase("00000")) {
                        flag = false;
                        Log.e("zero_click_254",""+flag);
                        CartListFragment.lv_cart_checkout.setAlpha(0.4f);
                        //  CartFragment.lv_cart_checkout.setEnabled(false);

                        Toast.makeText(context, context.getString(R.string.quantity_messge), Toast.LENGTH_SHORT).show();
                    }else if (String.valueOf(holder.et_cart_qty.getText()).equals(model_qty)) {
                        flag = true;
                        CartListFragment.lv_cart_checkout.setAlpha(1f);
                        Log.e("zero_click_261",""+flag);
                        // CartFragment.lv_cart_checkout.setEnabled(true);
                        //  Toast.makeText(context, "Same", Toast.LENGTH_SHORT).show();
                    } else {
                        flag = true;
                        final String itemid = cartList.get(position).getItemId();
                        Log.e("debugitem", "sss" + itemid);
                        Log.e("debugcustomertoken", "email=" + Login_preference.getCustomertoken(context));
                        Log.e("quote", "email=" + Login_preference.getquote_id(context));
                        if (CheckNetwork.isNetworkAvailable(context)) {
                            callUpdateCartApi(itemid, String.valueOf( holder.et_cart_qty.getText()), holder);
                        } else {
                            Toast.makeText(context, context.getString(R.string.internet), Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            }
        });*/


            holder.et_cart_qty.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    String model_qty= String.valueOf(cart_model.getQty());

                    if (actionId == EditorInfo.IME_ACTION_DONE) {

                        if (String.valueOf(v.getText()).equalsIgnoreCase("")) {
                            Toast.makeText(context, context.getString(R.string.quantity), Toast.LENGTH_SHORT).show();
                        } else if (String.valueOf(v.getText()).equalsIgnoreCase("0")||String.valueOf(v.getText()).equalsIgnoreCase("00")
                                ||String.valueOf(v.getText()).equalsIgnoreCase("000")||String.valueOf(v.getText()).equalsIgnoreCase("0000")
                                ||String.valueOf(v.getText()).equalsIgnoreCase("00000")) {
                            flag = false;
                            Log.e("zero_click_254",""+flag);
                            NewCartFragment.lv_cart_checkout.setAlpha(0.4f);
                            //  CartFragment.lv_cart_checkout.setEnabled(false);

                            Toast.makeText(context, context.getString(R.string.quantity_messge), Toast.LENGTH_SHORT).show();
                        }else if (String.valueOf(holder.et_cart_qty.getText()).equals(model_qty)) {
                            flag = true;
                            NewCartFragment.lv_cart_checkout.setAlpha(1f);
                            Log.e("zero_click_261",""+flag);
                            // CartFragment.lv_cart_checkout.setEnabled(true);
                            //  Toast.makeText(context, "Same", Toast.LENGTH_SHORT).show();
                        } else {
                            flag = true;
                            final String itemid = String.valueOf(cartlistdata.get(position).getItemId());
                            Log.e("debugitem", "sss" + itemid);
                            Log.e("debugcustomertoken", "email=" + Login_preference.getCustomertoken(context));
                            Log.e("quote", "email=" + Login_preference.getquote_id(context));
                            if (CheckNetwork.isNetworkAvailable(context)) {
                                AppCompatActivity activity = (AppCompatActivity) holder.et_cart_qty.getContext();
                                hideKeyboard(activity);
                                callUpdateCartApi(itemid, String.valueOf(v.getText()), holder);
                            } else {
                                Toast.makeText(context, context.getString(R.string.internet), Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                    return false;
                }
            });


/*
            final NewCartListModel NewCartListModel1 = cartlistdata.get(position);
            final RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.logo_red);
            requestOptions.error(R.drawable.logo_red);
            Glide.with(context)
                    .setDefaultRequestOptions(requestOptions)
                    .load(NewCartListModel1.getImg()).into(holder.iv_cartlist_product);

         //   NewCartFragment.cartid= "";
          //  NewCartFragment.cartid= String.valueOf(cartlistdata.get(position).getItemId());
            NavigationActivity.Check_String_NULL_Value(holder.tv_cartlist_product_name, NewCartListModel1.getName());
            NavigationActivity.Check_String_NULL_Value(holder.tv_cartlist_msrp, String.valueOf(NewCartListModel1.getPrice()) +"Kr");
            NavigationActivity.Check_String_NULL_Value(holder.et_cart_qty, String.valueOf(NewCartListModel1.getQty()));
            NavigationActivity.Check_String_NULL_Value(holder.tv_cartlist_product_sku, String.valueOf(NewCartListModel1.getSku()));

            
            holder.lv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String  itemid= String.valueOf(cartlistdata.get(position).getItemId());
                    Log.e("debg","="+itemid);
                    callRemoveFromCartApi(itemid,position,v);
                    
                }
            });*/
        }

    private void callUpdateCartApi(final String itemid, final String valueOf, final MyViewHolder holder) {
        Log.e("debug_232_update", "a" + itemid);
        Log.e("debug_233_update", "b" + valueOf);
        AppCompatActivity activity = (AppCompatActivity) holder.et_cart_qty.getContext();

        hideKeyboard(activity);

        NewCartFragment.cordinator_cart.setVisibility(View.GONE);
        lv_cartlist_progress.setVisibility(View.VISIBLE);

        callupdateCartQty(itemid, valueOf).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("debug_162update", "" + response);
                Log.e("debug_167", "" + response.body());
                AppCompatActivity activity = (AppCompatActivity) holder.et_cart_qty.getContext();

                hideKeyboard(activity);

                if (response.code()==200 || response.isSuccessful()) {
                    Log.e("status_update", "ok");
                    NewCartFragment.cordinator_cart.setVisibility(View.GONE);
                    lv_cartlist_progress.setVisibility(View.GONE);

                    try {
                        JSONObject jsonObject=new JSONObject(response.body().string());
                        Log.e("jsonObject=", "ok"+jsonObject);

                        // CartListFragment.CallCartlistApi();

                    /*    final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
*/
                        setupUI(holder.et_cart_qty);
                        Toast.makeText(context, "Quantity updated successfully.", Toast.LENGTH_SHORT).show();

                        AppCompatActivity activityy = (AppCompatActivity) holder.et_cart_qty.getContext();
                        hideKeyboard(activityy);

                        //CartListFragment.CallCartlistApi();
                        NewCartFragment myFragment = new NewCartFragment();
                        activityy.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                                0, 0, R.anim.fade_out).setCustomAnimations(R.anim.fade_in,
                                0, 0, R.anim.fade_out).replace(R.id.framlayout, myFragment).commit();
                           /* }
                        }, 100);*/

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    NewCartFragment.cordinator_cart.setVisibility(View.GONE);
                    lv_cartlist_progress.setVisibility(View.GONE);
                    AppCompatActivity activityy = (AppCompatActivity) holder.et_cart_qty.getContext();
                    hideKeyboard(activityy);
                    NavigationActivity.get_Customer_tokenapi();
                    Log.e("status_wish402", "not_ok");
                    callUpdateCartApi(itemid, valueOf, holder);
                    //Toast.makeText(context, updateCartQtyModel.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("error_wish", "" + t);
                Log.e("debug_remivr", "" + t);
                //CartFragment.cordinator_cart.setVisibility(View.VISIBLE);
                //CartFragment.lv_cartlist_progress.setVisibility(View.GONE);


                Toast.makeText(context, context.getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Call<ResponseBody> callupdateCartQty(String prod_idpass, String text) {
        Log.e("token", "=" + Login_preference.gettoken(context));
        Log.e("quoteid", "=" + Login_preference.getquote_id(context));
        Log.e("itemid", "=" + prod_idpass);
        Log.e("text_qty", "=" + text);
        ApiInterface  apiInterface = ApiClient.getClient().create(ApiInterface.class);

        String url="http://dkbraende.demoproject.info/rest/V1/carts/:cartId/items?cartItem[quoteId]="+
                Login_preference.getquote_id(context)+"&cartItem[qty]="+text+"&cartItem[item_id]="+prod_idpass;
        return apiInterface.udatecarttt("Bearer " + Login_preference.gettoken(context),url);
    }

        private void callRemoveFromCartApi(String itemid, int position, View v) {
            lv_cartlist_progress.setVisibility(View.VISIBLE);
            lv_cart_Main.setVisibility(View.GONE);

            callRemovecartapi(itemid).enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    Boolean paymentMehtodModel = response.body();

                    Log.e("resaaa", "=" + response.code());
                    Log.e("resaaacccc", "=" + response);

//                    Log.e("res_payment", "aaa" + response.body().toString());
                    //progressBar_review.setVisibility(View.GONE);

                  //  lv_cartlist_progress.setVisibility(View.GONE);
                   // lv_cart_Main.setVisibility(View.VISIBLE);


                    if(response.code()==200)
                    {
                    //    CallCartlistApi();
                       // cartlistdata.remove(position);
                       // cartlistAdapter.notifyDataSetChanged();
                        Toast.makeText(context, "Product Remove from cart successfully", Toast.LENGTH_SHORT).show();


                        AppCompatActivity activity = (AppCompatActivity) v.getContext();
                        NewCartFragment myFragment = new NewCartFragment();

                        activity.getSupportFragmentManager()
                                .beginTransaction().setCustomAnimations(R.anim.fade_in,
                                0, 0, R.anim.fade_out).addToBackStack(null).
                                replace(R.id.framlayout, myFragment).commit();
                    }else {

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
            Log.e("debug_11","=="+itemid);
            ///http://dkbraende.demoproject.info/rest//V1/carts/mine/items/162920


            String url=ApiClientcusome.MAIN_URLL+"carts/mine/items/"+itemid;
            Log.e("url11","=="+url);
            return api.removeitemfromcart("Bearer "+Login_preference.getCustomertoken(context),url);
        }
        @Override
        public int getItemCount() {
            return cartlistdata.size();
        }

        public void addAll(List<NewCartListModel> NewCartListModel) {
            cartlistdata.clear();
            for (NewCartListModel NewCartListModel1 : NewCartListModel) {
                add(NewCartListModel1);
            }
        }

        private void add(NewCartListModel f) {
            cartlistdata.add(f);
            notifyItemInserted(cartlistdata.size() - 1);
        }


        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView iv_cartlist_product;
            TextView tv_cartlist_product_name,tv_cartlist_product_sku,  tv_upc_title,   tv_cartlist_msrp;
            EditText et_cart_qty;
            LinearLayout lv_cart_click,lv_delete;

            public MyViewHolder(View view) {
                super(view);


                lv_delete = view.findViewById(R.id.lv_delete);
                lv_cart_click = view.findViewById(R.id.lv_cart_click);
                iv_cartlist_product = view.findViewById(R.id.iv_cartlist_product);
                tv_cartlist_product_name = view.findViewById(R.id.tv_cartlist_product_name);

                tv_cartlist_msrp = view.findViewById(R.id.tv_cartlist_msrp);
                et_cart_qty = view.findViewById(R.id.et_cart_qty);
                tv_upc_title = view.findViewById(R.id.tv_upc_title);

                tv_cartlist_product_sku = view.findViewById(R.id.tv_cartlist_product_sku);

            }
        }

        public void removeItem(int position) {
            String product_id = String.valueOf(cartlistdata.get(position).getItemId());
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


        //remove product from cart api calling
        private void CALL_REMOVE_FROM_CART_API(String product_id) {
            tv_messgenoti.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            lv_nodata_cart.setVisibility(View.GONE);
            recv_cart.setVisibility(View.VISIBLE);
            lv_cart_Main.setVisibility(View.VISIBLE);
            removefromcart(product_id).enqueue(new Callback<RemoveCartProductModel>() {
                @Override
                public void onResponse(Call<RemoveCartProductModel> call, Response<RemoveCartProductModel> response) {
                    RemoveCartProductModel removeproductmodel = response.body();
                    if (removeproductmodel.getStatus().equalsIgnoreCase("Success")) {
                        NavigationActivity.Check_String_NULL_Value(tv_cart_subtotal, removeproductmodel.getSubtotal());
                        NavigationActivity.Check_String_NULL_Value(tv_addlinal, removeproductmodel.getAdditionalAmountNeededForFreeShipping());
                        Toast.makeText(context, "" + removeproductmodel.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("removeitem_qty2466", "" + removeproductmodel.getItemsCount());
                        Log.e("removeitem_qty2466total", "" + removeproductmodel.getGrandTotal());
                        Login_preference.setquote_id(context, removeproductmodel.getQuoteId());
                        String item_count = null, count_wishlist;
                        if (removeproductmodel.getItemsCount() == 0 || removeproductmodel.getItemsCount().equals("0.00")) {
                            Login_preference.setCart_item_count(context, "");
                            //   item_count = String.valueOf(removeproductmodel.getItemsCount());
                        } else {
                            Login_preference.setCart_item_count(context, String.valueOf(removeproductmodel.getItemsCount()));
                            item_count = String.valueOf(removeproductmodel.getItemsCount());
                        }


                        if (item_count == null || item_count.equalsIgnoreCase("null") || item_count.equals("")) {
                            Log.e("count_40", "" + String.valueOf(removeproductmodel.getItemsCount()));
                            tv_bottomcount.setVisibility(View.GONE);
                        } else {
                            tv_bottomcount.setVisibility(View.VISIBLE);
                            Log.e("count_80", "" + String.valueOf(removeproductmodel.getItemsCount()));
                            NavigationActivity.Check_String_NULL_Value(tv_bottomcount, String.valueOf(removeproductmodel.getItemsCount()));
                        }
                        if (fetchResults(response) == null || fetchResults(response).size() == 0) {
                            Log.e("debug_175", "pages: " + fetchResults(response));
                            lv_nodata_cart.setVisibility(View.VISIBLE);
                            recv_cart.setVisibility(View.GONE);
                            lv_cart_Main.setVisibility(View.GONE);
                            tv_messgenoti.setVisibility(View.GONE);
                            tv_noting.setText(Html.fromHtml(context.getResources().getString(R.string.cartempty)));
                            shimmer_cartlist.stopShimmerAnimation();
                            shimmer_cartlist.setVisibility(View.GONE);
                            tv_bottomcount.setVisibility(View.GONE);
                        } else {
                            Log.e("debug_995", "pages: " + fetchResults(response));
                            recv_cart.setVisibility(View.VISIBLE);
                            lv_cart_Main.setVisibility(View.VISIBLE);
                            List<RemoveProduct> results = fetchResults(response);
                            List<RemoveProduct> product_arr = response.body().getProduct();
                            if (product_arr.isEmpty()) {
                                lv_cart_Main.setVisibility(View.GONE);
                                recv_cart.setVisibility(View.GONE);
                                lv_nodata_cart.setVisibility(View.VISIBLE);
                                tv_messgenoti.setVisibility(View.GONE);
                                tv_noting.setText(Html.fromHtml(context.getResources().getString(R.string.cartempty)));
                                shimmer_cartlist.stopShimmerAnimation();
                                shimmer_cartlist.setVisibility(View.GONE);
                                tv_bottomcount.setVisibility(View.GONE);
                            } else {
                                tv_bottomcount.setVisibility(View.VISIBLE);
                                lv_cart_Main.setVisibility(View.VISIBLE);
                                recv_cart.setVisibility(View.VISIBLE);
                                lv_nodata_cart.setVisibility(View.GONE);
                            }
                            shimmer_cartlist.stopShimmerAnimation();
                            shimmer_cartlist.setVisibility(View.GONE);
                        }
                    } else {
                        lv_cart_Main.setVisibility(View.VISIBLE);
                        recv_cart.setVisibility(View.VISIBLE);
                        lv_nodata_cart.setVisibility(View.GONE);
                        tv_messgenoti.setVisibility(View.GONE);
                        tv_noting.setText(Html.fromHtml(context.getResources().getString(R.string.cartempty)));
                        shimmer_cartlist.stopShimmerAnimation();
                        shimmer_cartlist.setVisibility(View.GONE);
                        //tv_bottomcount.setVisibility(View.GONE);
                        Log.e("debug_244else", "" + removeproductmodel.getMessage());
                        Toast.makeText(context, "" + removeproductmodel.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<RemoveCartProductModel> call, Throwable t) {
                    Log.e("debug_remivr", "" + t);
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
            api = ApiClientcusome.getClient().create(ApiInterface.class);
            return api.removeproductFromCart(
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

