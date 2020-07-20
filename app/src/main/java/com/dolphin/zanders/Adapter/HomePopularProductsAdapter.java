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

import com.bumptech.glide.Glide;
import com.dolphin.zanders.Activity.NavigationActivity;
import com.dolphin.zanders.Fragment.ProductDetailFragment;
import com.dolphin.zanders.Model.Home_model.ProductsTopseller;
import com.dolphin.zanders.Preference.Login_preference;
import com.dolphin.zanders.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import static com.dolphin.zanders.Activity.NavigationActivity.drawer;

public class HomePopularProductsAdapter extends RecyclerView.Adapter<HomePopularProductsAdapter.MyViewHolder> {
    Context context;
    private List<ProductsTopseller> homePopularProductsModels;

    public HomePopularProductsAdapter(FragmentActivity context) {
        this.context = context;
        this.homePopularProductsModels = new ArrayList<>();
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
        final ProductsTopseller homePopularProductsModel = homePopularProductsModels.get(position);

        holder.tv_stock_status.setText(homePopularProductsModel.getInStock());
        holder.tv_daily_special_product_name.setText(homePopularProductsModel.getName());
        holder.tv_daily_special_item_number.setText(homePopularProductsModel.getItemNumber());
        holder.tv_daily_special_upc.setText(homePopularProductsModel.getUpc());


        if (Login_preference.getLogin_flag(context).matches("1")) {
            if (homePopularProductsModel.getProduct_specialprice().equals("")||homePopularProductsModel.getProduct_specialprice().equals(null)||homePopularProductsModel.getProduct_specialprice().equals("$0.00")){
                holder.tv_price.setText(homePopularProductsModel.getProduct_price());
                holder.lv_specialprice.setVisibility(View.INVISIBLE);
                holder.lv_masrp.setVisibility(View.INVISIBLE);

            }else {
                holder.tv_price.setText(homePopularProductsModel.getProduct_price());
                holder.lv_masrp.setVisibility(View.INVISIBLE);
                holder.tv_price.setPaintFlags(holder.tv_daily_special_msrp.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                holder.lv_specialprice.setVisibility(View.VISIBLE);
                holder.tv_specialprice.setText(homePopularProductsModel.getProduct_specialprice());
            }
        }else {
            holder.lv_masrp.setVisibility(View.VISIBLE);
            holder.lv_price.setVisibility(View.INVISIBLE);
            holder.lv_specialprice.setVisibility(View.INVISIBLE);
            holder.tv_daily_special_msrp.setText(homePopularProductsModel.getMsrp());

        }
        Glide.with(context).load(homePopularProductsModel.getImage()).into(holder.iv_daily_specials);
        if (homePopularProductsModel.getInStock().equalsIgnoreCase("OUT OF STOCK")){
            holder.lv_daily_special_see_all.setBackgroundResource(R.drawable.rounded_layout_red);
        }else {
            holder.lv_daily_special_see_all.setBackgroundResource(R.drawable.rounded_layout_blue);
        }
        holder.tv_stock_status.setTypeface(NavigationActivity.montserrat_medium);
        holder.tv_daily_special_product_name.setTypeface(NavigationActivity.montserrat_medium);
        holder.tv_item_number_title.setTypeface(NavigationActivity.montserrat_regular);
        holder.tv_daily_special_item_number.setTypeface(NavigationActivity.montserrat_medium);
        holder.tv_upc_title.setTypeface(NavigationActivity.montserrat_regular);
        holder.tv_daily_special_upc.setTypeface(NavigationActivity.montserrat_light);
        holder.tv_msrp_title.setTypeface(NavigationActivity.montserratbold);
        holder.tv_daily_special_msrp.setTypeface(NavigationActivity.montserratbold);
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

    public void addAll(List<ProductsTopseller> popularprods) {
        for (ProductsTopseller pop: popularprods) {
            add(pop);
        }
    }
    public void add(ProductsTopseller r) {
        homePopularProductsModels.add(r);
        notifyItemInserted(homePopularProductsModels.size() - 1);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout lv_daily_special_see_all,lv_product_click;
        ImageView iv_daily_specials;
        TextView tv_daily_special_product_name,tv_daily_special_item_number,tv_daily_special_upc,tv_daily_special_msrp,tv_stock_status,tv_item_number_title,tv_upc_title,tv_msrp_title;

        LinearLayout lv_price,lv_masrp,lv_specialprice;
        TextView tv_price_title,tv_price,tv_specialprice;
        public MyViewHolder(@NonNull View view) {
            super(view);
            lv_product_click = view.findViewById(R.id.lv_product_click);

            lv_daily_special_see_all = view.findViewById(R.id.lv_daily_special_see_all);
            iv_daily_specials = view.findViewById(R.id.iv_daily_specials);
            tv_daily_special_product_name = view.findViewById(R.id.tv_daily_special_product_name);
            tv_daily_special_item_number = view.findViewById(R.id.tv_daily_special_item_number);
            tv_daily_special_upc = view.findViewById(R.id.tv_daily_special_upc);
            tv_daily_special_msrp = view.findViewById(R.id.tv_daily_special_msrp);
            tv_stock_status = view.findViewById(R.id.tv_stock_status);
            tv_item_number_title = view.findViewById(R.id.tv_item_number_title);
            tv_upc_title = view.findViewById(R.id.tv_upc_title);
            tv_msrp_title = view.findViewById(R.id.tv_msrp_title);

            lv_price = view.findViewById(R.id.lv_price);
            tv_price_title = view.findViewById(R.id.tv_price_title);
            tv_specialprice = view.findViewById(R.id.tv_specialprice);
            tv_price = view.findViewById(R.id.tv_price);
            lv_masrp = view.findViewById(R.id.lv_masrp);
            lv_specialprice = view.findViewById(R.id.lv_specialprice);
        }
    }
}

