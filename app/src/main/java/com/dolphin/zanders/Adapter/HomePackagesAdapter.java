package com.dolphin.zanders.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dolphin.zanders.Model.HomePackagesModel;
import com.dolphin.zanders.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

public class HomePackagesAdapter extends RecyclerView.Adapter<HomePackagesAdapter.MyViewHolder> {
    Context context;
    private List<String> homePackagesModels;

    public HomePackagesAdapter(FragmentActivity context) {
        this.context = context;
        homePackagesModels = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.home_packages_row, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final String packages = homePackagesModels.get(position);

        Glide.with(context).load(packages).into(holder.iv_submanufacurers);

       /* if (position==0){
            holder.iv_submanufacurers.setBackgroundResource(R.drawable.manufacturer_one);
        }if (position%3==1){
            holder.iv_submanufacurers.setBackgroundResource(R.drawable.manufacturer_two);
        }*/

    }

    @Override
    public int getItemCount() {
        return homePackagesModels.size();
    }

    public void addAll(List<String> homePagesm) {
        for (String homePage: homePagesm) {
            add(homePage);
        }
    }
    public void add(String r) {
        homePackagesModels.add(r);
        notifyItemInserted(homePackagesModels.size() - 1);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_submanufacurers;

        public MyViewHolder(@NonNull View view) {
            super(view);

            iv_submanufacurers = (ImageView)view.findViewById(R.id.iv_submanufacurers);
        }
    }
}
