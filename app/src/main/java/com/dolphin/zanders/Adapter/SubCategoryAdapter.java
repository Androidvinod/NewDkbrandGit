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

import com.dolphin.zanders.Fragment.NewProductListFragment;
import com.dolphin.zanders.Model.ChildData_;
import com.dolphin.zanders.R;

import java.util.ArrayList;
import java.util.List;

import static com.dolphin.zanders.Activity.NavigationActivity.drawer;


public class SubCategoryAdapter extends RecyclerView.Adapter<com.dolphin.zanders.Adapter.SubCategoryAdapter.MyViewHolder> {
    Context context;
    private List<ChildData_> SubCategoryList;

    public SubCategoryAdapter(FragmentActivity context) {
        this.context = context;
        SubCategoryList = new ArrayList<>();
    }

    @NonNull
    @Override
    public com.dolphin.zanders.Adapter.SubCategoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.subcategory_row, viewGroup, false);
        return new com.dolphin.zanders.Adapter.SubCategoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull com.dolphin.zanders.Adapter.SubCategoryAdapter.MyViewHolder holder, int position) {
        final ChildData_ subcategory = SubCategoryList.get(position);
        final SubCategoryAdapter.MyViewHolder myViewHolder = (SubCategoryAdapter.MyViewHolder) holder;
        myViewHolder.lv_subcategory_click.setEnabled(true);
        myViewHolder.tvSubCategoryName.setText(Html.fromHtml(subcategory.getName()));
        holder.lv_subcategory_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                      //  myViewHolder.lv_subcategory_click.setEnabled(false);
                        Bundle b=new Bundle();
                        AppCompatActivity activity = (AppCompatActivity) v.getContext();
                        b.putString("subcat_id", String.valueOf(subcategory.getId()));
                        b.putString("subcatename",subcategory.getName());
                        b.putString("screen","subcategory");

                        NewProductListFragment myFragment = new NewProductListFragment();
                        myFragment.setArguments(b);
                        activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                                0, 0, R.anim.fade_out).setCustomAnimations(R.anim.fade_in,
                                0, 0, R.anim.fade_out).replace(R.id.framlayout, myFragment)
                                .addToBackStack(null).commit();

                    }
                }, 100);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });


        if(position==getItemCount()-1)
        {
            myViewHolder.viewsubcat.setVisibility(View.GONE);
        }else {
            myViewHolder.viewsubcat.setVisibility(View.VISIBLE);
        }

    }
  /*  public void add(Subcategory r) {
        subcategoryList.add(r);
        notifyItemInserted(subcategoryList.size() - 1);
    }

    public void addAll(List<Subcategory> moveResults) {

        for (Subcategory result : moveResults) {
            Log.e("debug_127adapter",""+result);
            add(result);
        }
    }
*/

    public void add(ChildData_ r) {
        SubCategoryList.add(r);
        notifyItemInserted(SubCategoryList.size() - 1);
    }

    public void addAll(List<ChildData_> moveResults) {

        for (ChildData_ result : moveResults) {
            Log.e("debug_127adapter",""+result);
            add(result);
        }
    }
    @Override
    public int getItemCount() {
        return SubCategoryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvSubCategoryName;
        LinearLayout lv_subcategory_click;
        View viewsubcat;

        public MyViewHolder(@NonNull View view) {
            super(view);

            //tv_features = view.findViewById(R.id.tv_features);
            lv_subcategory_click = view.findViewById(R.id.lv_subcategory_click);
            tvSubCategoryName = view.findViewById(R.id.tvSubCategoryName);
            viewsubcat = view.findViewById(R.id.viewsubcat);

        }
    }
}



