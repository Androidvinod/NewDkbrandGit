package com.dolphin.zanders.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.dolphin.zanders.Model.TierPriceModel;
import com.dolphin.zanders.Preference.Login_preference;
import com.dolphin.zanders.R;
import java.util.ArrayList;
import java.util.List;




    public class  TierPriceAdapter  extends RecyclerView.Adapter<com.dolphin.zanders.Adapter.TierPriceAdapter.MyViewHolder>{

        private Context context;
        private List<TierPriceModel> models;
        String screenname;

        public TierPriceAdapter(Context context, List<TierPriceModel> models) {
            this.context = context;
            this.models =models;
        }
        @NonNull
        @Override
        public com.dolphin.zanders.Adapter.TierPriceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_tier_price, parent, false);
            return new com.dolphin.zanders.Adapter.TierPriceAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull com.dolphin.zanders.Adapter.TierPriceAdapter.MyViewHolder holder, int position)
        {
            final TierPriceModel tierPriceModel = models.get(position);

            //Køb 2 for 2.095,00 kr pr styk og spar 5%
            String price="Køb "+tierPriceModel.getQty()+" for "+tierPriceModel.getValue()+ " "+Login_preference.getcurrencycode(context)+" pr styk og spar "+tierPriceModel.getDiscount()+"%";

            holder.tv_price.setText(price);


         /*   List<String> street=new ArrayList<>();
            street=TierPriceModel.getStreet();
            Log.e("addres9999","43   "+TierPriceModel.getFirstname()+"\n"+street.get(0)+"\n"+TierPriceModel.getRegion()+","+TierPriceModel.getCity()+","+TierPriceModel.getPostcode()+"\n"+TierPriceModel.getLastname()+"\n"+"T :"+TierPriceModel.getTelephone());
            Log.e("TierPriceModel7777","43   "+TierPriceModel.getFirstname()+"\n"+TierPriceModel.getStreet()+"\n"+TierPriceModel.getRegion()+","+TierPriceModel.getCity()+","+TierPriceModel.getPostcode()+"\n"+TierPriceModel.getLastname()+"\n"+"T :"+TierPriceModel.getTelephone());
            holder.tv_adderss.setText(TierPriceModel.getFirstname()+" "+TierPriceModel.getLastname()+", \n"+street.get(0)+" "+","+TierPriceModel.getCity()+","+TierPriceModel.getPostcode()+"\n"+"T :"+TierPriceModel.getTelephone());
*/
        }

        @Override
        public int getItemCount() {
            return models.size();
        }

        public void addAll(List<TierPriceModel> feesInnerData) {
            for (TierPriceModel TierPriceModel : feesInnerData) {
                add(TierPriceModel);
            }
        }
        public void add(TierPriceModel r) {
            models.add(r);
            notifyItemInserted(models.size() - 1);
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv_price;
            LinearLayout lv_edit_addrss,lv_delete_add,lv_add_progress,lv_add_mainnn;

            public MyViewHolder(@NonNull View view) {
                super(view);
                tv_price=view.findViewById(R.id.tv_price);


            }
        }


    }


