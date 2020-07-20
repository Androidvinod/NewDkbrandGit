package com.dolphin.zanders.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.dolphin.zanders.Model.FeaturesModel;
import com.dolphin.zanders.R;

import java.util.List;


public class FeaturesAdapter extends RecyclerView.Adapter<com.dolphin.zanders.Adapter.FeaturesAdapter.MyViewHolder> {
    Context context;
    private List<FeaturesModel> homeDailySpecials;

    public FeaturesAdapter(FragmentActivity context, List<FeaturesModel> homeDailySpecials) {
        this.context = context;
        this.homeDailySpecials = homeDailySpecials;
    }

    @NonNull
    @Override
    public com.dolphin.zanders.Adapter.FeaturesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.features_row, viewGroup, false);
        return new com.dolphin.zanders.Adapter.FeaturesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull com.dolphin.zanders.Adapter.FeaturesAdapter.MyViewHolder holder, int position) {
        final FeaturesModel featuresModel = homeDailySpecials.get(position);


    }

    @Override
    public int getItemCount() {
        return homeDailySpecials.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_features;

        public MyViewHolder(@NonNull View view) {
            super(view);

            tv_features = view.findViewById(R.id.tv_features);

        }
    }
}

