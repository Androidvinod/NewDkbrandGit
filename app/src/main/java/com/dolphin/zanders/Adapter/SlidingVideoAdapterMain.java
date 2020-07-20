package com.dolphin.zanders.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dolphin.zanders.Model.slidervideo_model;
import com.dolphin.zanders.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class SlidingVideoAdapterMain extends RecyclerView.Adapter<SlidingVideoAdapterMain.MyViewHolder> {
    private Context context;
    private List<slidervideo_model> models;

    public SlidingVideoAdapterMain(Context context, List<slidervideo_model> models) {
        this.context = context;
        this.models = models;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sliding_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final slidervideo_model slidervideo_model = models.get(position);

        /*Glide.with(context)
                .load(vidpic)
                .into(holder.iv_thumb);*/



    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout lv_thumbnail;
        ImageView iv_thumb;
        View view;

        public MyViewHolder(View view) {
            super(view);
            lv_thumbnail = (LinearLayout) view.findViewById(R.id.lv_thumbnailll);
            iv_thumb = (ImageView) view.findViewById(R.id.iv_thumb);
        }
    }
}