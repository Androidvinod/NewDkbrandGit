package com.dolphin.zanders.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.dolphin.zanders.Fragment.SubCategoryFragment;
import com.dolphin.zanders.Model.CategoryModel.Category;
import com.dolphin.zanders.R;

import java.util.ArrayList;
import java.util.List;

import static com.dolphin.zanders.Activity.NavigationActivity.drawer;

public class CatalogHomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    private List<Category> datumList;
    public CatalogHomeAdapter(FragmentActivity context) {
        this.context = context;
        datumList = new ArrayList<>();
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
        View v1 = inflater.inflate(R.layout.cataloghome_row, parent, false);
        viewHolder = new MyViewHolder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Category datum = datumList.get(position);
        final MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.tv_catalog_name.setText(Html.fromHtml(datum.getName()));

        myViewHolder.lv_category_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {

                        Bundle b=new Bundle();
                        AppCompatActivity activity = (AppCompatActivity) v.getContext();
                        b.putString("categoryid",datum.getValue());
                        b.putString("categoryname",datum.getName());
                        SubCategoryFragment myFragment = new SubCategoryFragment();
                        myFragment.setArguments(b);
                        activity.getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.fade_in,
                                0, 0, R.anim.fade_out)
                                .setCustomAnimations(R.anim.fade_in,
                                0, 0, R.anim.fade_out)
                                .addToBackStack(null).replace(R.id.framlayout, myFragment)
                               .commit();

                    }
                }, 50);
            }
        });

    }

    @Override
    public int getItemCount() {
        return datumList == null ? 0 : datumList.size();
    }


    public void add(Category r) {
        datumList.add(r);
        Log.e("debug_117adapter",""+r);
        Log.e("debug_118adapter",""+datumList.size());
        Log.e("debug_119adapter",""+(datumList.size()-1));
        notifyItemInserted(datumList.size() - 1);
    }

    public void addAll(List<Category> moveResults) {
        Log.e("debug_124adapter",""+moveResults.size());

        for (Category result : moveResults) {
            Log.e("debug_127adapter",""+result);
            add(result);
        }
    }




    public Category getItem(int position) {
        Log.e("pos_galadaadapter", "" + position);
        return datumList.get(position);
    }
    protected class MyViewHolder extends RecyclerView.ViewHolder {
       TextView tv_catalog_name;
       LinearLayout lv_category_click;

        public MyViewHolder(@NonNull View view) {
            super(view);
            tv_catalog_name = itemView.findViewById(R.id.tv_catalog_name);
            lv_category_click = itemView.findViewById(R.id.lv_category_click);

        }
    }

}