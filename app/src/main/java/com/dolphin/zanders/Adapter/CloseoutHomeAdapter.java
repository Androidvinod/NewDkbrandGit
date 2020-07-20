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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dolphin.zanders.Activity.NavigationActivity;
import com.dolphin.zanders.Fragment.ProductDetailFragment;
import com.dolphin.zanders.Model.ProductlistModel.GetCategoryProductlistInnerData;
import com.dolphin.zanders.Preference.Login_preference;
import com.dolphin.zanders.R;

import java.util.ArrayList;
import java.util.List;

import static com.dolphin.zanders.Activity.NavigationActivity.drawer;

public class CloseoutHomeAdapter extends RecyclerView.Adapter<CloseoutHomeAdapter.MyViewHolder> {
    Context context;
    private List<GetCategoryProductlistInnerData> modellist;

    public CloseoutHomeAdapter(FragmentActivity context) {
        this.context = context;
        modellist = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.dailyspecials_row, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    //if login show msrp otherwise show price
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final GetCategoryProductlistInnerData model = modellist.get(position);

        holder.lv_ds_main_item_number.setVisibility(View.GONE);
        holder.lv_ds_main_upc.setVisibility(View.GONE);

        Log.e("image_URL_60", "" + model.getProductThumbnail());
        Log.e("price_61", "" + model.getProductPrice());

        holder.lv_price.setVisibility(View.INVISIBLE);
        NavigationActivity.Check_String_NULL_Value(holder.tv_daily_special_product_name, model.getProductName());
        //NavigationActivity.Check_String_NULL_Value(holder.tv_daily_special_item_number, model.getItemNumber());
        //NavigationActivity.Check_String_NULL_Value(holder.tv_daily_special_upc, model.getUpc());
        NavigationActivity.Check_String_NULL_Value(holder.tv_daily_special_msrp, model.getMsrp());
        NavigationActivity.Check_String_NULL_Value(holder.tv_stock_status, model.getAvailability());
        Glide.with(context).load(model.getProductThumbnail()).into(holder.iv_daily_specials);

        //product is outof stock or not
        if (model.getAvailability().equalsIgnoreCase("OUT OF STOCK")) {
            holder.lv_daily_special_see_all.setBackgroundResource(R.drawable.rounded_layout_red);
            //  holder.lv_specialprice.setVisibility(View.GONE);
        } else {
            holder.lv_daily_special_see_all.setBackgroundResource(R.drawable.rounded_layout_blue);
            // holder.lv_specialprice.setVisibility(View.VISIBLE);
        }

        //showing different price
        if (Login_preference.getLogin_flag(context).matches("1")) {
            holder.lv_ds_main_availbleqty.setVisibility(View.VISIBLE);
            holder.tv_availbleqty.setText(" "+model.getAvailable_qty());
            if (model.getProductSpecialprice().equals("") || model.getProductSpecialprice().equals(null)
                    || model.getProductSpecialprice().equals("$0.00")||model.getProductSpecialprice().equals("$00.00")) {
                Log.e("price_75", "" + model.getProductPrice());
                if (model.getProductPrice().equals("") ||
                        model.getProductPrice().equals(null)
                        || model.getProductPrice().equals("$0.00")) {
                    holder.lv_price.setVisibility(View.INVISIBLE);
                } else {
                    holder.lv_price.setVisibility(View.VISIBLE);
                }
                holder.tv_price.setText(model.getProductPrice());
                holder.lv_specialprice.setVisibility(View.INVISIBLE);
                //    holder.lv_masrp.setVisibility(View.INVISIBLE);

            } else {
                holder.tv_price.setText(model.getProductPrice());
                //  holder.lv_masrp.setVisibility(View.INVISIBLE);
                holder.tv_price.setPaintFlags(holder.tv_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.lv_specialprice.setVisibility(View.VISIBLE);
                holder.lv_price.setVisibility(View.VISIBLE);
                holder.tv_specialprice.setText(model.getProductSpecialprice());
            }
            if(model.getMap().equals("null")|| model.getMap().equals("") || model.getMap().equals("$0.00")){
                holder.lv_map.setVisibility(View.INVISIBLE);
            }else {
                holder.lv_map.setVisibility(View.VISIBLE);
                holder.tv_map.setText(model.getMap());
            }

            if(model.getMsrp().equals("null")|| model.getMsrp().equals("") ||  model.getMap().equals("$0.00")){
                holder.lv_masrp.setVisibility(View.INVISIBLE);
            }else {
                holder.lv_masrp.setVisibility(View.VISIBLE);
                holder.tv_daily_special_msrp.setText(model.getMsrp());
            }
            Log.e("Availability_148",""+model.getAvailability());
            ///check out of stock in or instock
            if (model.getAvailability().equalsIgnoreCase("Out of stock")) {
                Log.e("debug_129", "adad" + model.getAvailability());
                holder.lv_specialprice.setVisibility(View.GONE);
            } else {
                if (model.getProductSpecialprice().equals("") || model.getProductSpecialprice().equals(null)
                        || model.getProductSpecialprice().equals("$0.00")||model.getProductSpecialprice().equals("$00.00")) {
                    Log.e("price_75", "" + model.getProductPrice());
                    if (model.getProductPrice().equals("") ||
                            model.getProductPrice().equals(null)
                            || model.getProductPrice().equals("$0.00")) {
                        holder.lv_price.setVisibility(View.INVISIBLE);
                    } else {
                        holder.lv_price.setVisibility(View.VISIBLE);
                    }
                    holder.tv_price.setText(model.getProductPrice());
                    holder.lv_specialprice.setVisibility(View.INVISIBLE);
                    //    holder.lv_masrp.setVisibility(View.INVISIBLE);

                }
              //  holder.lv_specialprice.setVisibility(View.VISIBLE);
                //  Log.e("debug_1222", "adad" + productlistInnerData.getAvailability());
            }
        } else {
            holder.lv_ds_main_availbleqty.setVisibility(View.GONE);
            holder.lv_masrp.setVisibility(View.VISIBLE);
            holder.lv_price.setVisibility(View.INVISIBLE);
            holder.lv_map.setVisibility(View.INVISIBLE);
            holder.lv_specialprice.setVisibility(View.INVISIBLE);
            holder.tv_daily_special_msrp.setText(model.getMsrp());

        }



        holder.lv_product_click.setEnabled(true);
        holder.lv_product_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        // holder.lv_product_click.setEnabled(false);
                        AppCompatActivity activity = (AppCompatActivity) v.getContext();
                        Bundle b = new Bundle();
                        b.putString("product_id", model.getProductId());
                        ProductDetailFragment myFragment = new ProductDetailFragment();
                        myFragment.setArguments(b);
                        activity.getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.fade_in,
                                        0, 0, R.anim.fade_out)
                                .setCustomAnimations(R.anim.fade_in,
                                        0, 0, R.anim.fade_out)
                                .add(R.id.framlayout, myFragment).addToBackStack(null).commit();
                        if (drawer.isDrawerOpen(GravityCompat.START)) {
                            drawer.closeDrawer(GravityCompat.START);
                        }
                    }
                }, 100);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });
        //Glide.with(context).load(manufacturers_model.getManufacturers_images()).into(holder.iv_manufacurers);

    }

    @Override
    public int getItemCount() {
        return modellist.size();
    }

    public void addAll(List<GetCategoryProductlistInnerData> dailySpecials) {
        for (GetCategoryProductlistInnerData dailySpecial1 : dailySpecials) {
            add(dailySpecial1);
        }
    }

    public void add(GetCategoryProductlistInnerData r) {
        modellist.add(r);
        notifyItemInserted(modellist.size() - 1);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout lv_daily_special_see_all,lv_map, lv_product_click, lv_ds_main_item_number, lv_ds_main_upc, lv_specialprice;
        ImageView iv_daily_specials;
        TextView tv_daily_special_product_name,tv_map_title,tv_map, tv_daily_special_item_number, tv_daily_special_upc, tv_daily_special_msrp, tv_stock_status, tv_item_number_title, tv_upc_title, tv_msrp_title, tv_specialprice_title, tv_specialprice;

        LinearLayout lv_price, lv_masrp,lv_ds_main_availbleqty;
        TextView tv_price_title, tv_price,tv_availbleqty;

        public MyViewHolder(@NonNull View view) {
            super(view);

            tv_map = view.findViewById(R.id.tv_map);
            lv_product_click = view.findViewById(R.id.lv_product_click);
            lv_daily_special_see_all = view.findViewById(R.id.lv_daily_special_see_all);
            lv_ds_main_upc = view.findViewById(R.id.lv_ds_main_upc);
            lv_ds_main_item_number = view.findViewById(R.id.lv_ds_main_item_number);
            iv_daily_specials = view.findViewById(R.id.iv_daily_specials);
            tv_daily_special_product_name = view.findViewById(R.id.tv_daily_special_product_name);
            tv_daily_special_item_number = view.findViewById(R.id.tv_daily_special_item_number);
            tv_daily_special_upc = view.findViewById(R.id.tv_daily_special_upc);
            tv_daily_special_msrp = view.findViewById(R.id.tv_daily_special_msrp);
            tv_stock_status = view.findViewById(R.id.tv_stock_status);
            tv_item_number_title = view.findViewById(R.id.tv_item_number_title);
            tv_upc_title = view.findViewById(R.id.tv_upc_title);
            tv_msrp_title = view.findViewById(R.id.tv_msrp_title);
            tv_specialprice_title = view.findViewById(R.id.tv_specialprice_title);
            tv_specialprice = view.findViewById(R.id.tv_specialprice);
            lv_price = view.findViewById(R.id.lv_price);
            tv_price_title = view.findViewById(R.id.tv_price_title);
            tv_price = view.findViewById(R.id.tv_price);
            lv_masrp = view.findViewById(R.id.lv_masrp);
            lv_specialprice = view.findViewById(R.id.lv_specialprice);
            lv_map = view.findViewById(R.id.lv_map);
            tv_map_title = view.findViewById(R.id.tv_map_title);
            tv_availbleqty = view.findViewById(R.id.tv_availbleqty);
            lv_ds_main_availbleqty = view.findViewById(R.id.lv_ds_main_availbleqty);

        }
    }
}

