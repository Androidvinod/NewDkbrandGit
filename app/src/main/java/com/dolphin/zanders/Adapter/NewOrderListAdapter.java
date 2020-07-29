package com.dolphin.zanders.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.dolphin.zanders.Activity.NavigationActivity;
import com.dolphin.zanders.Fragment.OrderView_Fragment;
import com.dolphin.zanders.Model.NewOrderModel.Item;
import com.dolphin.zanders.Preference.Login_preference;
import com.dolphin.zanders.R;

import java.util.ArrayList;
import java.util.List;


public class NewOrderListAdapter extends RecyclerView.Adapter<NewOrderListAdapter.MyViewHolder> {
        Context context;
        private List<Item> currentItemModels;

        public NewOrderListAdapter(Context context) {
            this.context = context;
            this.currentItemModels = new ArrayList<>();
        }

        @NonNull
        @Override
        public com.dolphin.zanders.Adapter.NewOrderListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.my_orders_row, viewGroup, false);
            return new com.dolphin.zanders.Adapter.NewOrderListAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final com.dolphin.zanders.Adapter.NewOrderListAdapter.MyViewHolder holder, int position) {
            final Item myItem_model = currentItemModels.get(position);
            NavigationActivity.Check_String_NULL_Value(holder.tv_order_date,myItem_model.getCreatedAt());
            NavigationActivity.Check_String_NULL_Value(holder.tv_order_total,String.valueOf(myItem_model.getBaseGrandTotal())+" "+ Login_preference.getcurrencycode(context));
            Log.e("debuf_11","=="+myItem_model.getIncrementId());

            if(myItem_model.getStatus()==null || myItem_model.getStatus().equalsIgnoreCase("null"))
            {
                holder.tv_status.setVisibility(View.GONE);
                holder.tv_order_status_value.setVisibility(View.GONE);
            }else {
                NavigationActivity.Check_String_NULL_Value(holder.tv_order_status_value,String.valueOf(myItem_model.getStatus()));
            }

            String Itemno="#"+myItem_model.getIncrementId();
            String total="$"+myItem_model.getGrandTotal();
            NavigationActivity.Check_String_NULL_Value(holder.tv_order_no,Itemno);


            holder.lv_view_order.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  AppCompatActivity activity = (AppCompatActivity) view.getContext();
                  Bundle b = new Bundle();
                  Log.e("order_id61","=="+currentItemModels.get(position).getItems().get(0).getOrderId());

                  b.putString("order_id", String.valueOf(currentItemModels.get(position).getItems().get(0).getOrderId()));
                  OrderView_Fragment myFragment = new OrderView_Fragment();
                  myFragment.setArguments(b);
                  activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                          0, 0, R.anim.fade_out).setCustomAnimations(R.anim.fade_in,
                          0, 0, R.anim.fade_out).addToBackStack("ordderlist").replace(R.id.framlayout, myFragment).commit();

              }
          });

        }


    public void addAll(List<Item> categories) {
        for (Item result : categories) {
            add(result);
        }
    }

    public void add(Item r) {
        currentItemModels.add(r);
        notifyItemInserted(currentItemModels.size() - 1);
    }

        @Override
        public int getItemCount() {
            return currentItemModels.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            LinearLayout lv_view_order,lv_order_click;
            TextView tv_order_total,tv_order_shipto_value,tv_order_date,tv_order_no,tv_order_status_value,tv_status;
            public MyViewHolder(@NonNull View view) {
                super(view);

                tv_status = view.findViewById(R.id.tv_status);
                lv_order_click = view.findViewById(R.id.lv_order_click);
                tv_order_total = view.findViewById(R.id.tv_order_total);
                tv_order_shipto_value = view.findViewById(R.id.tv_order_shipto_value);
                tv_order_date = view.findViewById(R.id.tv_order_date);
                tv_order_no = view.findViewById(R.id.tv_order_no);
                tv_order_status_value = view.findViewById(R.id.tv_order_status_value);
                lv_view_order = view.findViewById(R.id.lv_view_order);
            }
        }
    }

