package com.dolphin.zanders.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.bumptech.glide.request.RequestOptions;

import com.dolphin.zanders.R;


import java.util.List;


public class PDetailImageSliderAdapter extends RecyclerView.Adapter<PDetailImageSliderAdapter.MyViewHolder> {
    private Context context;
    private List<com.dolphin.zanders.Model.ProductDetailModelNew.Medium> models;

    public PDetailImageSliderAdapter(Context context,List<com.dolphin.zanders.Model.ProductDetailModelNew.Medium> models) {
        this.context = context;
        this.models = models;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.productdetail_image_slider_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final com.dolphin.zanders.Model.ProductDetailModelNew.Medium medium = models.get(position);
        final RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.logo_app);
        requestOptions.error(R.drawable.logo_app);
        Log.e("imageurl_44",""+medium.getFile());
        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load("http://dkbraende.demoproject.info/pub/media/catalog/product"+medium.getFile()).into(holder.iv_productimage);

      //  Glide.with(context).load(medium.getUrl()).into(holder.iv_productimage);
}
    @Override
    public int getItemCount() {
        return models.size();
    }
    public void  addAll(List<com.dolphin.zanders.Model.ProductDetailModelNew.Medium> media1) {
        for (com.dolphin.zanders.Model.ProductDetailModelNew.Medium medium : media1) {
            add(medium);
        }
    }

    private  void add(com.dolphin.zanders.Model.ProductDetailModelNew.Medium r) {
        models.add(r);
        notifyItemInserted(models.size() - 1);

    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_productimage;
        View view;

        public MyViewHolder(View view) {
            super(view);
            iv_productimage=view.findViewById(R.id.iv_productimage);
        }
    }
}
    

