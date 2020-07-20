package com.dolphin.zanders.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dolphin.zanders.Activity.NavigationActivity;
import com.dolphin.zanders.Fragment.ProductDetailFragment;
import com.dolphin.zanders.Model.Home_model.ProductsDaily;
import com.dolphin.zanders.Preference.Login_preference;
import com.dolphin.zanders.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import static com.dolphin.zanders.Activity.NavigationActivity.drawer;

public class DailySpecialAdapter extends RecyclerView.Adapter<DailySpecialAdapter.MyViewHolder> {
    Context context;
    private List<ProductsDaily> homeDailySpecials;

    public DailySpecialAdapter(FragmentActivity context) {
        this.context = context;
        homeDailySpecials = new ArrayList<>();
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
        final ProductsDaily homeDailySpecial = homeDailySpecials.get(position);

        if (homeDailySpecial.getItemNumber().equalsIgnoreCase("") || homeDailySpecial.getItemNumber().equalsIgnoreCase(null)) {
            holder.lv_ds_main_item_number.setVisibility(View.INVISIBLE);
        } else {
            holder.lv_ds_main_item_number.setVisibility(View.VISIBLE);
        }
        if (homeDailySpecial.getUpc().equalsIgnoreCase("") || homeDailySpecial.getUpc().equalsIgnoreCase(null)) {
            holder.lv_ds_main_upc.setVisibility(View.INVISIBLE);
        } else {
            holder.lv_ds_main_upc.setVisibility(View.VISIBLE);
        }


        NavigationActivity.Check_String_NULL_Value(holder.tv_daily_special_product_name, homeDailySpecial.getName());
        NavigationActivity.Check_String_NULL_Value(holder.tv_daily_special_item_number, homeDailySpecial.getItemNumber());
        NavigationActivity.Check_String_NULL_Value(holder.tv_daily_special_upc, homeDailySpecial.getUpc());
        NavigationActivity.Check_String_NULL_Value(holder.tv_daily_special_msrp, homeDailySpecial.getMsrp());
        NavigationActivity.Check_String_NULL_Value(holder.tv_stock_status, homeDailySpecial.getInStock());
        Glide.with(context).load(homeDailySpecial.getImage()).into(holder.iv_daily_specials);

        if (homeDailySpecial.getInStock().equalsIgnoreCase("OUT OF STOCK")) {
            holder.lv_daily_special_see_all.setBackgroundResource(R.drawable.rounded_layout_red);
        } else {
            holder.lv_daily_special_see_all.setBackgroundResource(R.drawable.rounded_layout_blue);
        }
        if (Login_preference.getLogin_flag(context).matches("1")) {
            if (homeDailySpecial.getProduct_specialprice().equals("")||homeDailySpecial.getProduct_specialprice().equals(null)||homeDailySpecial.getProduct_specialprice().equals("$0.00")){
                holder.tv_price.setText(homeDailySpecial.getProduct_price());
                holder.lv_specialprice.setVisibility(View.INVISIBLE);
                holder.lv_masrp.setVisibility(View.INVISIBLE);

            }else {
                holder.tv_price.setText(homeDailySpecial.getProduct_price());
                holder.lv_masrp.setVisibility(View.INVISIBLE);
                holder.tv_price.setPaintFlags(holder.tv_daily_special_msrp.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                holder.lv_specialprice.setVisibility(View.VISIBLE);
                holder.tv_specialprice.setText(homeDailySpecial.getProduct_specialprice());
            }
        }else {
            holder.lv_masrp.setVisibility(View.VISIBLE);
            holder.lv_price.setVisibility(View.INVISIBLE);
            holder.lv_specialprice.setVisibility(View.INVISIBLE);
            holder.tv_daily_special_msrp.setText(homeDailySpecial.getMsrp());

        }
        holder.tv_stock_status.setTypeface(NavigationActivity.montserrat_medium);
        holder.tv_daily_special_product_name.setTypeface(NavigationActivity.montserrat_medium);
        holder.tv_item_number_title.setTypeface(NavigationActivity.montserrat_regular);
        holder.tv_daily_special_item_number.setTypeface(NavigationActivity.montserrat_medium);
        holder.tv_upc_title.setTypeface(NavigationActivity.montserrat_regular);
        holder.tv_daily_special_upc.setTypeface(NavigationActivity.montserrat_light);
        holder.tv_msrp_title.setTypeface(NavigationActivity.montserratbold);
        holder.tv_daily_special_msrp.setTypeface(NavigationActivity.montserratbold);
        holder.lv_product_click.setEnabled(true);
        holder.lv_product_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        //holder.lv_product_click.setEnabled(false);
                        AppCompatActivity activity = (AppCompatActivity) v.getContext();
                        Bundle b = new Bundle();
                        b.putString("product_id",homeDailySpecial.getProductId());
                        ProductDetailFragment myFragment = new ProductDetailFragment();
                        myFragment.setArguments(b);
                        activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                                0, 0, R.anim.fade_out).setCustomAnimations(R.anim.fade_in,
                                0, 0, R.anim.fade_out).replace(R.id.framlayout, myFragment).addToBackStack(null).commit();
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
        return homeDailySpecials.size();
    }

    public void addAll(List<ProductsDaily> dailySpecials) {
        for (ProductsDaily dailySpecial1 : dailySpecials) {
            add(dailySpecial1);
        }
    }

    public void add(ProductsDaily r) {
        homeDailySpecials.add(r);
        notifyItemInserted(homeDailySpecials.size() - 1);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout lv_daily_special_see_all, lv_product_click, lv_ds_main_item_number, lv_ds_main_upc,lv_specialprice;
        ImageView iv_daily_specials;
        TextView tv_daily_special_product_name, tv_daily_special_item_number, tv_daily_special_upc, tv_daily_special_msrp, tv_stock_status, tv_item_number_title, tv_upc_title, tv_msrp_title,tv_specialprice_title,tv_specialprice;

        LinearLayout lv_price,lv_masrp;
        TextView tv_price_title,tv_price;

        public MyViewHolder(@NonNull View view) {
            super(view);

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

        }
    }
}

