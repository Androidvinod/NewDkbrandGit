package com.dolphin.zanders.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
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

public class ItemOnsaleHomeAdapter extends RecyclerView.Adapter<ItemOnsaleHomeAdapter.MyViewHolder> {
    Context context;
    private List<GetCategoryProductlistInnerData> homePopularProductsModels;

    public ItemOnsaleHomeAdapter(FragmentActivity context) {
        this.context = context;
        this.homePopularProductsModels = new ArrayList<GetCategoryProductlistInnerData>();
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.popularproducts_row, viewGroup, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final GetCategoryProductlistInnerData homePopularProductsModel = homePopularProductsModels.get(position);
        holder.tv_stock_status.setText(homePopularProductsModel.getAvailability());
        holder.tv_daily_special_product_name.setText(homePopularProductsModel.getProductName());
        // holder.tv_daily_special_item_number.setText(homePopularProductsModel.get());
        //  holder.tv_daily_special_upc.setText(homePopularProductsModel.getUpc());
        if (Login_preference.getLogin_flag(context).matches("1")) {
            holder.lv_ds_main_availbleqty.setVisibility(View.VISIBLE);
            holder.tv_availbleqty.setText(" "+homePopularProductsModel.getAvailable_qty());
            if (homePopularProductsModel.getProductSpecialprice().equals("")||homePopularProductsModel.getProductSpecialprice().equals(null)||homePopularProductsModel.getProductSpecialprice().equals("$0.00")||homePopularProductsModel.getProductSpecialprice().equals("$00.00")){
                if (homePopularProductsModel.getProductPrice().equals("") ||
                        homePopularProductsModel.getProductPrice().equals(null)
                        || homePopularProductsModel.getProductPrice().equals("$0.00")) {
                    holder.lv_price.setVisibility(View.INVISIBLE);
                } else {
                    holder.lv_price.setVisibility(View.VISIBLE);
                }
                holder.tv_price.setText(homePopularProductsModel.getProductPrice());
                holder.lv_specialprice.setVisibility(View.INVISIBLE);

            }else {
                holder.tv_price.setText(homePopularProductsModel.getProductPrice());
                holder.tv_price.setPaintFlags(holder.tv_price.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                holder.lv_specialprice.setVisibility(View.VISIBLE);
                holder.lv_price.setVisibility(View.VISIBLE);
                holder.tv_specialprice.setText(homePopularProductsModel.getProductSpecialprice());
            }

            if(homePopularProductsModel.getMap().equals("null")|| homePopularProductsModel.getMap().equals("") || homePopularProductsModel.getMap().equals("$0.00")){
                holder.lv_map_price.setVisibility(View.INVISIBLE);
            }else {
                holder.lv_map_price.setVisibility(View.VISIBLE);
                holder.tv_special_map.setText(homePopularProductsModel.getMap());
            }

            if(homePopularProductsModel.getMsrp().equals("null")|| homePopularProductsModel.getMsrp().equals("") ||  homePopularProductsModel.getMap().equals("$0.00")){
                holder.lv_masrp.setVisibility(View.INVISIBLE);
            }else {
                holder.lv_masrp.setVisibility(View.VISIBLE);
                holder.tv_daily_special_msrp.setText(homePopularProductsModel.getMsrp());
            }
            if (homePopularProductsModel.getAvailability().equalsIgnoreCase("OUT OF STOCK")){
                holder.lv_daily_special_see_all.setBackgroundResource(R.drawable.rounded_layout_red);
                holder.lv_specialprice.setVisibility(View.GONE);
            }else {
                holder.lv_specialprice.setVisibility(View.VISIBLE);
                holder.lv_daily_special_see_all.setBackgroundResource(R.drawable.rounded_layout_blue);
            }
        }else {
            holder.lv_ds_main_availbleqty.setVisibility(View.GONE);
            holder.lv_masrp.setVisibility(View.VISIBLE);
            holder.lv_price.setVisibility(View.INVISIBLE);
            holder.lv_specialprice.setVisibility(View.INVISIBLE);
            holder.lv_map_price.setVisibility(View.INVISIBLE);
            holder.tv_daily_special_msrp.setText(homePopularProductsModel.getMsrp());

        }
        Glide.with(context).load(homePopularProductsModel.getProductImage()).into(holder.iv_daily_specials);


        holder.lv_product_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        // holder.lv_product_click.setEnabled(false);
                        Bundle b=new Bundle();
                        AppCompatActivity activity = (AppCompatActivity) v.getContext();

                        b.putString("product_id",homePopularProductsModel.getProductId());

                     /*   b.putString("cat_id",product_model.getValue());
                        b.putString("name",product_model.getCategory_name());
                        Log.e("categotyidd",""+product_model.getValue());
                        AppCompatActivity activity = (AppCompatActivity) view.getContext();*/
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
        return homePopularProductsModels.size();
    }

    public void addAll(List<GetCategoryProductlistInnerData> popularprods) {
        for (GetCategoryProductlistInnerData pop: popularprods) {
            add(pop);
        }
    }
    public void add(GetCategoryProductlistInnerData r) {
        homePopularProductsModels.add(r);
        notifyItemInserted(homePopularProductsModels.size() - 1);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout lv_daily_special_see_all,lv_product_click,lv_map_price;
        ImageView iv_daily_specials;
        TextView tv_daily_special_product_name,tv_special_map_title,tv_special_map,tv_daily_special_msrp,tv_stock_status,tv_msrp_title;

        LinearLayout lv_price,lv_masrp,lv_specialprice,lv_ds_main_availbleqty;
        TextView tv_price_title,tv_price,tv_specialprice,tv_availbleqty;
        public MyViewHolder(@NonNull View view) {
            super(view);
            lv_map_price = view.findViewById(R.id.lv_map_price);
            lv_product_click = view.findViewById(R.id.lv_product_click);

            tv_special_map = view.findViewById(R.id.tv_special_map);
            tv_special_map_title = view.findViewById(R.id.tv_special_map_title);
            lv_daily_special_see_all = view.findViewById(R.id.lv_daily_special_see_all);
            iv_daily_specials = view.findViewById(R.id.iv_daily_specials);
            tv_daily_special_product_name = view.findViewById(R.id.tv_daily_special_product_name);
            tv_daily_special_msrp = view.findViewById(R.id.tv_daily_special_msrp);
            tv_stock_status = view.findViewById(R.id.tv_stock_status);
            tv_msrp_title = view.findViewById(R.id.tv_msrp_title);

            lv_price = view.findViewById(R.id.lv_price);
            tv_price_title = view.findViewById(R.id.tv_price_title);
            tv_specialprice = view.findViewById(R.id.tv_specialprice);
            tv_price = view.findViewById(R.id.tv_price);
            lv_masrp = view.findViewById(R.id.lv_masrp);
            lv_specialprice = view.findViewById(R.id.lv_specialprice);
            tv_availbleqty = view.findViewById(R.id.tv_availbleqty);
            lv_ds_main_availbleqty = view.findViewById(R.id.lv_ds_main_availbleqty);
        }
    }
}

