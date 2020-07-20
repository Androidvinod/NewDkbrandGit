package com.dolphin.zanders.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dolphin.zanders.Activity.NavigationActivity;
import com.dolphin.zanders.Fragment.EditAddressFragment;
import com.dolphin.zanders.Model.Country;
import com.dolphin.zanders.R;

import java.util.List;

import static com.dolphin.zanders.Fragment.EditAddressFragment.tv_choose_country;

public class CountryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    private List<Country> countryModelList;


    public CountryAdapter(Context context, List<Country> countryModelList) {
        this.context = context;
        this.countryModelList = countryModelList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        viewHolder = getViewHolder(parent, inflater);
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.row_city, parent, false);
        viewHolder = new MyViewHolder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final Country country = countryModelList.get(position);
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.tv_country_name.setTypeface(NavigationActivity.montserrat_semibold);
        myViewHolder.tv_country_name.setText(country.getLabel());

        myViewHolder.lv_city_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_choose_country.setText(country.getLabel());
                tv_choose_country.setTextColor(context.getResources().getColor(R.color.black));
                EditAddressFragment.countryId=countryModelList.get(position).getValues();
                EditAddressFragment.dialog.dismiss();
            }
        });


    }


    @Override
    public int getItemCount() {
        return countryModelList == null ? 0 : countryModelList.size();
    }

    public void addAll(List<Country> datumListt) {
        for (Country result : datumListt) {
            Log.e("debug_127adapter", "" + result);
            add(result);
        }
    }
    public void add(Country r) {
        countryModelList.add(r);
        notifyItemInserted(countryModelList.size() - 1);
    }
    public Country getItem(int position) {
        Log.e("pos_galadaadapter", "" + position);
        return countryModelList.get(position);
    }

    protected class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_country_name;
        LinearLayout lv_city_click;

        public MyViewHolder(@NonNull View view) {
            super(view);
            tv_country_name = itemView.findViewById(R.id.tv_country_name);
            lv_city_click = itemView.findViewById(R.id.lv_city_click);

        }
    }

}




