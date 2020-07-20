package com.dolphin.zanders.Adapter;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.dolphin.zanders.Activity.NavigationActivity;
import com.dolphin.zanders.Fragment.CartFragment;
import com.dolphin.zanders.Fragment.CheckoutFragment;
import com.dolphin.zanders.Model.Checkout_model.CheckoutTotalModel;
import com.dolphin.zanders.Model.Shipingmethod.ShippingMethod;
import com.dolphin.zanders.Preference.Login_preference;
import com.dolphin.zanders.R;
import com.dolphin.zanders.Retrofit.ApiClient;
import com.dolphin.zanders.Retrofit.ApiInterface;
import com.dolphin.zanders.Util.CheckNetwork;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dolphin.zanders.Fragment.CheckoutFragment.payment_method;
import static com.dolphin.zanders.Fragment.CheckoutFragment.shiping_method;
import static com.dolphin.zanders.Fragment.CheckoutFragment.shiping_method_order;
import static com.dolphin.zanders.Fragment.CheckoutFragment.tv_grand_total_value;
import static com.dolphin.zanders.Fragment.CheckoutFragment.tv_shippable_grand_total_main_value;
import static com.dolphin.zanders.Fragment.CheckoutFragment.tv_shippable_subtotal_value;
import static com.dolphin.zanders.Fragment.CheckoutFragment.tv_shipping_handling_bestway;
import static com.dolphin.zanders.Fragment.CheckoutFragment.tv_shipping_handling_bestway_value;
import static com.dolphin.zanders.Fragment.CheckoutFragment.tv_subtotal_value;


public class ShippingMethodAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;

    private List<ShippingMethod> datumList;
    private int lastSelectedPosition = -1;

    ApiInterface api;


    public ShippingMethodAdapter(FragmentActivity context) {
        this.context = context;
        datumList = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        viewHolder = getViewHolder(parent, inflater);
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.shipping_method_row, parent, false);
        viewHolder = new MyViewHolder(v1);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ShippingMethod datum = datumList.get(position);
        api = ApiClient.getClient().create(ApiInterface.class);

        final MyViewHolder myViewHolder = (MyViewHolder) holder;
        //myViewHolder.radio_btn_shipping.setText(Html.fromHtml(datum.getCarrierTitle()));
        String text=datum.getCarrierTitle()+" "+datum.getPrice();
        NavigationActivity.Check_String_NULL_Value(myViewHolder.radio_btn_shipping,text);
        myViewHolder.radio_btn_shipping.setChecked(lastSelectedPosition == position);
        myViewHolder.radio_btn_shipping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastSelectedPosition = position;
                shiping_method = datum.getCode();
                shiping_method_order=datum.getCode();
                Log.e("shipingmethod71", "" + shiping_method );
                Log.e("shipingmethod95", "" + shiping_method_order );
                if (shiping_method.equalsIgnoreCase("zandersship_PU") ){
                    Log.e("fist1111","");
                    new iOSDialogBuilder(context)
                            .setTitle(context.getString(R.string.app_name))
                            .setSubtitle("ALL PICKUP ORDERS MUST BE PLACED AT LEAST 4 BUSINESS HOURS PRIOR TO PICKUP, POWDER ORDERS MUST BE PLACED 24 HOURS PRIOR TO PICKUP.")
                            .setBoldPositiveLabel(false)
                            .setCancelable(false)
                            .setPositiveListener(context.getString(R.string.ok), new iOSDialogClickListener() {
                                @Override
                                public void onClick(iOSDialog dialog) {
                                    if (CheckNetwork.isNetworkAvailable(context)) {
                                        CallCheckoutTotalApi(CheckoutFragment.biiling_address_id,CheckoutFragment.shipping_address_id);
                                    } else {
                                        //    noInternetDialog(NavigationActivity.this);
                                        Toast.makeText(context,context.getString(R.string.internet), Toast.LENGTH_SHORT).show();
                                    }
                                      /*  CartFragment myFragment = new CartFragment();
                                        activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                                                0, 0, R.anim.fade_out).setCustomAnimations(R.anim.fade_in,
                                                0, 0, R.anim.fade_out).replace(R.id.framlayout, myFragment).addToBackStack(null).commit();
                                   */     dialog.dismiss();
                                }
                            })
                            .build().show();
                }else if (shiping_method.equalsIgnoreCase("zandersship_BW")){
                    Log.e("fist1111","");
                    if (CheckNetwork.isNetworkAvailable(context)) {
                        CallCheckoutTotalApi(CheckoutFragment.biiling_address_id,CheckoutFragment.shipping_address_id);
                    } else {
                        //    noInternetDialog(NavigationActivity.this);
                        Toast.makeText(context,context.getString(R.string.internet), Toast.LENGTH_SHORT).show();
                    }
                }else if (shiping_method.equalsIgnoreCase("zandersship_UM")){
                    if (CheckNetwork.isNetworkAvailable(context)) {
                        CallCheckoutTotalApi(CheckoutFragment.biiling_address_id,CheckoutFragment.shipping_address_id);
                    } else {
                        //    noInternetDialog(NavigationActivity.this);
                        Toast.makeText(context,context.getString(R.string.internet), Toast.LENGTH_SHORT).show();
                    }
                }else if (shiping_method.equalsIgnoreCase("zandersship_UG")){
                    new iOSDialogBuilder(context)
                            .setTitle(context.getString(R.string.app_name))
                            .setSubtitle("Actual Freight Will Be Charged")
                            .setBoldPositiveLabel(false)
                            .setCancelable(false)
                            .setPositiveListener(context.getString(R.string.ok), new iOSDialogClickListener() {
                                @Override
                                public void onClick(iOSDialog dialog) {
                                    if (CheckNetwork.isNetworkAvailable(context)) {
                                        CallCheckoutTotalApi(CheckoutFragment.biiling_address_id,CheckoutFragment.shipping_address_id);
                                    } else {
                                        //    noInternetDialog(NavigationActivity.this);
                                        Toast.makeText(context,context.getString(R.string.internet), Toast.LENGTH_SHORT).show();
                                    }
                                      /*  CartFragment myFragment = new CartFragment();
                                        activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                                                0, 0, R.anim.fade_out).setCustomAnimations(R.anim.fade_in,
                                                0, 0, R.anim.fade_out).replace(R.id.framlayout, myFragment).addToBackStack(null).commit();
                                   */     dialog.dismiss();
                                }
                            })
                            .build().show();
                }else if (shiping_method.equalsIgnoreCase("zandersship_U2")){
                    new iOSDialogBuilder(context)
                            .setTitle(context.getString(R.string.app_name))
                            .setSubtitle("Actual Freight Will Be Charged")
                            .setBoldPositiveLabel(true)
                            .setCancelable(false)
                            .setPositiveListener(context.getString(R.string.ok), new iOSDialogClickListener() {
                                @Override
                                public void onClick(iOSDialog dialog) {
                                    if (CheckNetwork.isNetworkAvailable(context)) {
                                        CallCheckoutTotalApi(CheckoutFragment.biiling_address_id,CheckoutFragment.shipping_address_id);
                                    } else {
                                        //    noInternetDialog(NavigationActivity.this);
                                        Toast.makeText(context,context.getString(R.string.internet), Toast.LENGTH_SHORT).show();
                                    }
                                      /*  CartFragment myFragment = new CartFragment();
                                        activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                                                0, 0, R.anim.fade_out).setCustomAnimations(R.anim.fade_in,
                                                0, 0, R.anim.fade_out).replace(R.id.framlayout, myFragment).addToBackStack(null).commit();
                                   */     dialog.dismiss();
                                }
                            })
                            .build().show();
                }else if (shiping_method.equalsIgnoreCase("zandersship_U1")){
                    new iOSDialogBuilder(context)
                            .setTitle(context.getString(R.string.app_name))
                            .setSubtitle("Actual Freight Will Be Charged")
                            .setBoldPositiveLabel(true)
                            .setCancelable(false)
                            .setPositiveListener(context.getString(R.string.ok), new iOSDialogClickListener() {
                                @Override
                                public void onClick(iOSDialog dialog) {
                                    if (CheckNetwork.isNetworkAvailable(context)) {
                                        CallCheckoutTotalApi(CheckoutFragment.biiling_address_id,CheckoutFragment.shipping_address_id);
                                    } else {
                                        //    noInternetDialog(NavigationActivity.this);
                                        Toast.makeText(context,context.getString(R.string.internet), Toast.LENGTH_SHORT).show();
                                    }
                                      /*  CartFragment myFragment = new CartFragment();
                                        activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                                                0, 0, R.anim.fade_out).setCustomAnimations(R.anim.fade_in,
                                                0, 0, R.anim.fade_out).replace(R.id.framlayout, myFragment).addToBackStack(null).commit();
                                   */     dialog.dismiss();
                                }
                            })
                            .build().show();
                }
                notifyDataSetChanged();
            }
        });
        if (lastSelectedPosition == position) {
            Log.e("selectedpo_76", "" + lastSelectedPosition);
            myViewHolder.radio_btn_shipping.setChecked(true);
        } else {
            myViewHolder.radio_btn_shipping.setChecked(false);
        }

        myViewHolder.lv_radio_shiping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                lastSelectedPosition = position;
                //   shippingmethod = cart_delivery_model.getCode();

             /*   if (CheckNetwork.isNetworkAvailable(context)) {
                    CALL_CART_CONFIRM_API(shippingmethod);
                } else {
                    Toast.makeText(context, "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
                }*/
            }
        });
        if (position == getItemCount() - 1) {
            myViewHolder.viewshipping.setVisibility(View.GONE);
        } else {
            myViewHolder.viewshipping.setVisibility(View.VISIBLE);
        }
    }



    private void CallCheckoutTotalApi(String biiling_address_id, String shipping_address_id) {
        CheckoutFragment.lv_checkout_main.setVisibility(View.GONE);
        CheckoutFragment.lv_checkout_progress.setVisibility(View.VISIBLE);
        callcheckoutapi(biiling_address_id,shipping_address_id).enqueue(new Callback<CheckoutTotalModel>() {
            @Override
            public void onResponse(Call<CheckoutTotalModel> call, Response<CheckoutTotalModel> response) {
                Log.e("response_check", "" + response.body());
                Log.e("response_77_checkout", "" + response);
                Log.e("statuscgh", "" + response.body().getStatus());
                if (response.body().getStatus().equals("Success")) {
                    Log.e("address_63shhip", "" + response.body().getTotals().getShippableSubtotal());
                    CheckoutFragment.lv_checkout_main.setVisibility(View.VISIBLE);
                    CheckoutFragment.lv_checkout_progress.setVisibility(View.GONE);

                    tv_shippable_subtotal_value.setText(response.body().getTotals().getShippableSubtotal());
                    tv_subtotal_value.setText(response.body().getTotals().getSubtotal());
                    tv_shipping_handling_bestway_value.setText(response.body().getTotals().getShippingAndHandling());
                    tv_shipping_handling_bestway.setText("Shipping & Handling("+response.body().getTotals().getShippingAndHandlingName()+")");
                    tv_grand_total_value.setText(response.body().getTotals().getGrandTotal());
                    tv_shippable_grand_total_main_value.setText(response.body().getTotals().getGrandTotal());

                } else {
                    CheckoutFragment.lv_checkout_main.setVisibility(View.VISIBLE);
                    CheckoutFragment.lv_checkout_progress.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<CheckoutTotalModel> call, Throwable t) {
                CheckoutFragment.lv_checkout_main.setVisibility(View.VISIBLE);
                CheckoutFragment.lv_checkout_progress.setVisibility(View.GONE);

                Toast.makeText(context, "" +context.getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Call<CheckoutTotalModel> callcheckoutapi(String biiling_address_id, String shipping_address_id) {
        api = ApiClient.getClient().create(ApiInterface.class);

        Log.e("debug_111_checkoui", "" + Login_preference.getemail(context));
        Log.e("biiling_address_id154", "" + CheckoutFragment.biiling_address_id);
        Log.e("shipping_address_id154", "" + CheckoutFragment.shipping_address_id);
        Log.e("shipping_address_id154", "" + shiping_method);
        Log.e("shipping_address_id154", "" + payment_method);

        return api.getCheckouttotal(Login_preference.getemail(context), biiling_address_id,
                shipping_address_id,shiping_method,Login_preference.getquote_id(context),payment_method);
    }

    @Override
    public int getItemCount() {
        return datumList == null ? 0 : datumList.size();
    }


    public void add(ShippingMethod r) {
        datumList.add(r);
        Log.e("debug_117adapter", "" + r);
        Log.e("debug_118adapter", "" + datumList.size());
        Log.e("debug_119adapter", "" + (datumList.size() - 1));
        notifyItemInserted(datumList.size() - 1);
    }

    public void addAll(List<ShippingMethod> moveResults) {
        Log.e("debug_124adapter", "" + moveResults.size());
        datumList.clear();
        for (ShippingMethod result : moveResults) {
            Log.e("debug_127adapter", "" + result);
            add(result);
        }
    }


    public ShippingMethod getItem(int position) {
        Log.e("pos_galadaadapter", "" + position);
        return datumList.get(position);
    }

    protected class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout lv_radio_shiping, lv_radio_text_click, lv_radio_click;
        RadioButton radio_btn_shipping;
        View viewshipping;


        public MyViewHolder(@NonNull View view) {
            super(view);
            radio_btn_shipping = itemView.findViewById(R.id.radio_btn_shipping);
            viewshipping = itemView.findViewById(R.id.viewshipping);
            lv_radio_shiping = itemView.findViewById(R.id.lv_radio_shiping);


        }
    }

}

