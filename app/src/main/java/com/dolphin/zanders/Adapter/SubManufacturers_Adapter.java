package com.dolphin.zanders.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dolphin.zanders.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

public class SubManufacturers_Adapter extends RecyclerView.Adapter<SubManufacturers_Adapter.MyViewHolder> {
    Context context;
    private List<String> subManufacturers_models;

    public SubManufacturers_Adapter(FragmentActivity context) {
        this.context = context;
        subManufacturers_models = new ArrayList<String>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.submanufacturers_row, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final String manufacturers_model = subManufacturers_models.get(position);
        Log.e("submanu_model",""+manufacturers_model);

        Glide.with(context).load(manufacturers_model).into(holder.iv_submanufacurers);
/*
        if (position==0){
            holder.iv_submanufacurers.setBackgroundResource(R.drawable.manufacturer_one);
        }if (position%3==1){
            holder.iv_submanufacurers.setBackgroundResource(R.drawable.manufacturer_two);
        }*/

    }

    @Override
    public int getItemCount() {
        return subManufacturers_models.size();
    }

    public void addAll(List<String> homePagesm) {
        for (String homePage: homePagesm) {
            add(homePage);
        }
    }
    public void add(String r) {
        subManufacturers_models.add(r);
        notifyItemInserted(subManufacturers_models.size() - 1);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_submanufacurers;

        public MyViewHolder(@NonNull View view) {
            super(view);

            iv_submanufacurers = (ImageView)view.findViewById(R.id.iv_submanufacurers);
        }
    }
}
