package com.dolphin.zanders.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dolphin.zanders.Fragment.ProductListFragment;
import com.dolphin.zanders.Model.Home_model.ProductsManufacturer;
import com.dolphin.zanders.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

public class Manufacturers_Adapter extends RecyclerView.Adapter<Manufacturers_Adapter.MyViewHolder> {
    Context context;
    private List<ProductsManufacturer> productsManufacturers;

    public Manufacturers_Adapter(FragmentActivity context) {
        this.context = context;
        productsManufacturers = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.manufacturers_row, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ProductsManufacturer productsManufacturer = productsManufacturers.get(position);

        /*if (position==0){
            holder.iv_manufacurers.setBackgroundResource(R.drawable.one);
        }if (position%3==1){
            holder.iv_manufacurers.setBackgroundResource(R.drawable.two);
        }if (position%3==2){
            holder.iv_manufacurers.setBackgroundResource(R.drawable.three);
        }*/

        Glide.with(context).load(productsManufacturer.getImage()).into(holder.iv_manufacurers);
        holder.iv_manufacurers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b=new Bundle();
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                b.putString("subcat_id",productsManufacturer.getId());
                b.putString("screen","Manufacturer");
                b.putString("subcatename",productsManufacturer.getName());
                ProductListFragment myFragment = new ProductListFragment();
                myFragment.setArguments(b);
                activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                        0, 0, R.anim.fade_out).setCustomAnimations(R.anim.fade_in,
                        0, 0, R.anim.fade_out).addToBackStack(null).replace(R.id.framlayout, myFragment).addToBackStack(null).commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return productsManufacturers.size();
    }

    public void addAll(List<ProductsManufacturer> productsManufacturers) {
        for (ProductsManufacturer productsManufacturer: productsManufacturers) {
            add(productsManufacturer);
        }
    }
    public void add(ProductsManufacturer r) {
        productsManufacturers.add(r);
        notifyItemInserted(productsManufacturers.size() - 1);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_manufacurers;

        public MyViewHolder(@NonNull View view) {
            super(view);

            iv_manufacurers = (ImageView)view.findViewById(R.id.iv_manufacurers);
        }
    }
}
