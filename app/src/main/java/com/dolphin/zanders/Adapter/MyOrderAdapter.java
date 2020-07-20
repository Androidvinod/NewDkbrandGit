package com.dolphin.zanders.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dolphin.zanders.Activity.NavigationActivity;
import com.dolphin.zanders.Fragment.CheckoutFragment;
import com.dolphin.zanders.Fragment.OrderView_Fragment;
import com.dolphin.zanders.Model.Ordermodel.Order;
import com.dolphin.zanders.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.MyViewHolder> {
    Context context;
    private List<Order> currentOrderModels;

    public MyOrderAdapter(Context context, List<Order> currentOrderModels) {
        this.context = context;
        this.currentOrderModels = currentOrderModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.my_orders_row, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final Order myOrder_model = currentOrderModels.get(position);
        NavigationActivity.Check_String_NULL_Value(holder.tv_order_date,myOrder_model.getCreatedAt());
        String orderno="#"+myOrder_model.getIncrementId();
        String total="$"+myOrder_model.getGrandTotal();
        NavigationActivity.Check_String_NULL_Value(holder.tv_order_no,orderno);
        NavigationActivity.Check_String_NULL_Value(holder.tv_order_shipto_value,myOrder_model.getShipTo());
        NavigationActivity.Check_String_NULL_Value(holder.tv_order_total,total);

        Log.e("debug_51","fd"+myOrder_model.getStatus());
        if(myOrder_model.getStatus().equalsIgnoreCase("canceled"))
        {
            NavigationActivity.Check_String_NULL_Value(holder.tv_order_status_value,"Cancelled ");
        }else {
            String status=myOrder_model.getStatus();
            status=status.replace("_"," ");
            NavigationActivity.Check_String_NULL_Value(holder.tv_order_status_value,status);
        }

        holder.lv_view_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Bundle b = new Bundle();
                b.putString("incrementid",myOrder_model.getIncrementId());
                OrderView_Fragment myFragment = new OrderView_Fragment();
                myFragment.setArguments(b);
                activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                        0, 0, R.anim.fade_out).setCustomAnimations(R.anim.fade_in,
                        0, 0, R.anim.fade_out).replace(R.id.framlayout, myFragment).addToBackStack(null).commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return currentOrderModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout lv_view_order;
        TextView tv_order_total,tv_order_shipto_value,tv_order_date,tv_order_no,tv_order_status_value;
        public MyViewHolder(@NonNull View view) {
            super(view);

            tv_order_total = view.findViewById(R.id.tv_order_total);
            tv_order_shipto_value = view.findViewById(R.id.tv_order_shipto_value);
            tv_order_date = view.findViewById(R.id.tv_order_date);
            tv_order_no = view.findViewById(R.id.tv_order_no);
            tv_order_status_value = view.findViewById(R.id.tv_order_status_value);
            lv_view_order = view.findViewById(R.id.lv_view_order);
        }
    }
}