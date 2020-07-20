package com.dolphin.zanders.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;


import com.dolphin.zanders.Fragment.NewProductListFragment;
import com.dolphin.zanders.Fragment.SubCategoryFragment;
import com.dolphin.zanders.Model.ChildData;
import com.dolphin.zanders.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.dolphin.zanders.Activity.NavigationActivity.drawer;

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    public static List<ChildData> datumList;
    public CategoryAdapter(FragmentActivity context) {
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
        View v1 = inflater.inflate(R.layout.category_row, parent, false);
        viewHolder = new MyViewHolder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ChildData datum = datumList.get(position);
        final MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.tv_category.setText(Html.fromHtml(datum.getName()));
        if(datumList.get(position).getChildrenData().size()!=0)
        {
            myViewHolder.iv_cat_side_arrow.setVisibility(View.VISIBLE);
        }else {
            myViewHolder.iv_cat_side_arrow.setVisibility(View.GONE);
        }
        myViewHolder.lv_category_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                if(datumList.get(position).getChildrenData().size()!=0)
                {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {

                            Bundle b=new Bundle();
                            AppCompatActivity activity = (AppCompatActivity) v.getContext();
                            b.putString("categoryid",String.valueOf(datumList.get(position).getId()));
                            b.putString("categoryname",datumList.get(position).getName());
                            b.putSerializable("subCatarraylist", (Serializable) datumList.get(position).getChildrenData());

                            SubCategoryFragment myFragment = new SubCategoryFragment();
                            myFragment.setArguments(b);
                            activity.getSupportFragmentManager().beginTransaction()
                                    .addToBackStack(null).replace(R.id.framlayout, myFragment)
                                    .commit();
                            if (drawer.isDrawerOpen(GravityCompat.START)) {
                                drawer.closeDrawer(GravityCompat.START);
                            }
                        }
                    }, 50);
                    if (drawer.isDrawerOpen(GravityCompat.START)) {
                        drawer.closeDrawer(GravityCompat.START);
                    }
                }else {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            Bundle b=new Bundle();
                            AppCompatActivity activity = (AppCompatActivity) v.getContext();
                            b.putString("subcat_id", String.valueOf(datum.getId()));
                            b.putString("subcatename",datum.getName());
                            b.putString("screen","subcategory");

                            NewProductListFragment myFragment = new NewProductListFragment();
                            myFragment.setArguments(b);
                            activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                                    0, 0, R.anim.fade_out).setCustomAnimations(R.anim.fade_in,
                                    0, 0, R.anim.fade_out).replace(R.id.framlayout, myFragment)
                                    .addToBackStack(null).commit();

                            if (drawer.isDrawerOpen(GravityCompat.START)) {
                                drawer.closeDrawer(GravityCompat.START);
                            }

                        }
                    }, 100);
                    if (drawer.isDrawerOpen(GravityCompat.START)) {
                        drawer.closeDrawer(GravityCompat.START);
                    }
                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return datumList == null ? 0 : datumList.size();
    }

    public void addAll(List<ChildData> categories) {
        for (ChildData result : categories) {
            add(result);
        }
    }

    public void add(ChildData r) {
        datumList.add(r);
        notifyItemInserted(datumList.size() - 1);
    }


  /*  public void add(Category r) {
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
    }*/




    public ChildData getItem(int position) {
        Log.e("pos_galadaadapter", "" + position);
        return datumList.get(position);
    }
    protected class MyViewHolder extends RecyclerView.ViewHolder {
       TextView tv_category;
       LinearLayout lv_category_click;
       View viewcat;
       ImageView iv_cat_side_arrow;

        public MyViewHolder(@NonNull View view) {
            super(view);
            viewcat = itemView.findViewById(R.id.viewcat);
            tv_category = itemView.findViewById(R.id.tv_category);
            lv_category_click = itemView.findViewById(R.id.lv_category_click);
            iv_cat_side_arrow = itemView.findViewById(R.id.iv_cat_side_arrow);

        }
    }

}