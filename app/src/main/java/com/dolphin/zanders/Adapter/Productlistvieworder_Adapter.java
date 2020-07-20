package com.dolphin.zanders.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.dolphin.zanders.Model.NewOrderDetailModel.Item;
import com.dolphin.zanders.Model.OrderView_model.Product;
import com.dolphin.zanders.Model.Shipingmethod.ShippingMethod;
import com.dolphin.zanders.R;

import java.util.ArrayList;
import java.util.List;

public class Productlistvieworder_Adapter extends RecyclerView.Adapter<Productlistvieworder_Adapter.MyViewHolder> {

    Context context;

    private List<Item> products;


    public Productlistvieworder_Adapter(FragmentActivity context) {
        this.context = context;
        products = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_productlist_orderview, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Item product = products.get(position);

        holder.tv_productname.setText(product.getName());
        holder.tv_price.setText(""+product.getPrice());
        if(product.getSku()==null || product.getSku().equals("") || product.getSku()=="null")
        {
            holder.lv_sku.setVisibility(View.GONE);
        }else {
            holder.lv_sku.setVisibility(View.VISIBLE);
            holder.tv_sku.setText(""+product.getSku());
        }


        Log.e("qtyyyyyyy_50",""+product.getSku());
        holder.tv_qty.setText(""+Integer.toString(product.getQtyOrdered()));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
    public void addAll(List<Item> products) {
        for (Item product: products) {
            add(product);
        }
    }
    public void add(Item r) {
        products.add(r);
        notifyItemInserted(products.size() - 1);
    }

    protected class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_productname,tv_sku,tv_price,tv_qty;
        LinearLayout lv_sku;

        public MyViewHolder(@NonNull View view) {
            super(view);
            lv_sku = itemView.findViewById(R.id.lv_sku);
            tv_productname = itemView.findViewById(R.id.tv_productname);
            tv_sku = itemView.findViewById(R.id.tv_sku);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_qty = itemView.findViewById(R.id.tv_qty);


        }
    }
}
