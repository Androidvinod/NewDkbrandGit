package com.dolphin.zanders.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.dolphin.zanders.Model.ProductDetailModel.Additional;
import com.dolphin.zanders.R;

import java.util.ArrayList;
import java.util.List;


public class AdditionalInfoAdapter extends RecyclerView.Adapter<AdditionalInfoAdapter.MyViewHolder> {
    Context context;
    private List<Additional> model;

    public AdditionalInfoAdapter(FragmentActivity context) {
        this.context = context;
        model = new ArrayList<Additional>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.additionalinfo_row, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Additional featuresModel = model.get(position);

        holder.tv_nametitle.setText(featuresModel.getLabel());
        holder.tv_value.setText(featuresModel.getValue());
    }
    @Override
    public int getItemCount() {
        return model.size();
    }

    public void  addAll(List<Additional> additionals) {
        model.clear();
        for (Additional additional : additionals) {
            add(additional);
        }
    }
    private  void add(Additional r) {
        model.add(r);
        notifyItemInserted(model.size() - 1);

    }
    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_nametitle,tv_value;

        public MyViewHolder(@NonNull View view) {
            super(view);
            tv_nametitle = view.findViewById(R.id.tv_nametitle);
            tv_value = view.findViewById(R.id.tv_value);

        }
    }
}


