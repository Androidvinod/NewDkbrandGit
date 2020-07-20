package com.dolphin.zanders.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.SectionIndexer;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.dolphin.zanders.Fragment.ProductListFragment;
import com.dolphin.zanders.Model.Manufacturerslist_model.Manufactur;
import com.dolphin.zanders.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Manufacturerslist_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SectionIndexer {
    Context context;
    private List<Manufactur> model;
    private ArrayList<Integer> mSectionPositions;
    char singleChar;
    String lastChar = "";

    Manufactur datum;
    MyViewHolder myViewHolder;

    public Manufacturerslist_Adapter(Context context, List<Manufactur> model) {
        this.context = context;
        this.model = model;
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
        View v1 = inflater.inflate(R.layout.manufacturerslist_row, parent, false);
        viewHolder = new MyViewHolder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        datum = model.get(position);
        myViewHolder = (MyViewHolder) holder;
        if(datum.getId().equalsIgnoreCase("0"))
        {
            myViewHolder.lv_header_text.setVisibility(View.VISIBLE);
            myViewHolder.tvTitle.setText(datum.getName());
            myViewHolder.lv_subcategory_click.setVisibility(View.GONE);
        }else
        {
            myViewHolder.lv_subcategory_click.setVisibility(View.VISIBLE);
            myViewHolder.lv_header_text.setVisibility(View.GONE);
            Log.e("name_ada", "" + datum.getName());
            Log.e("name_id", "" + datum.getId());
            myViewHolder.tvmenufactureraweb.setText(datum.getWeb());
            myViewHolder.tvmenufacturerName.setText(datum.getName());
            myViewHolder.tvmenufactureraddress.setText(datum.getAddress());

        }
        if(datum.getId().equalsIgnoreCase("0"))
        {
            myViewHolder.lv_subcategory_click .setEnabled(false);
        }else {
            myViewHolder.lv_subcategory_click .setEnabled(true);

            myViewHolder.lv_subcategory_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("click", ""+model.get(position).getName());
                    Bundle b = new Bundle();
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    Log.e("deug_966",""+model.get(position).getId());
                    b.putString("subcat_id", model.get(position).getId());
                    b.putString("screen", "Manufacturer");
                    b.putString("subcatename", model.get(position).getName());
                    ProductListFragment myFragment = new ProductListFragment();
                    myFragment.setArguments(b);
                    activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                            0, 0, R.anim.fade_out).setCustomAnimations(R.anim.fade_in,
                            0, 0, R.anim.fade_out).addToBackStack(null).add(R.id.framlayout, myFragment).addToBackStack(null).commit();
                }
            });
        }
    }

    private void setdata_ToHeaderView(MyViewHolder myViewHolder, List<Manufactur> datum, int position) {
        singleChar = datum.get(position).getName().charAt(0);

        Log.e("singleChar", "" + singleChar);
        Log.e("lastChar", "=" + lastChar);
        //    firstStr= String.valueOf(first);
        if (lastChar == String.valueOf(singleChar) || lastChar.equalsIgnoreCase(String.valueOf(singleChar)))//acurate firs=a
        {

            Log.e("first_77", "" + singleChar);
            myViewHolder.lv_header_text.setVisibility(View.GONE);
            //myViewHolder.tvTitle.setText(String.valueOf(singleChar));

        } else {
            Log.e("singleChar_92", "" + singleChar);
            Log.e("singleChar_93", "" + lastChar);
            lastChar = String.valueOf(singleChar);
            Log.e("lasttChar_95", "" + lastChar);
            myViewHolder.lv_header_text.setVisibility(View.VISIBLE);
            myViewHolder.tvTitle.setText(String.valueOf(singleChar));

        }
    }

    @Override
    public int getItemCount() {
        return model == null ? 0 : model.size();
    }

    public void add(Manufactur r) {
        model.add(r);
        notifyItemInserted(model.size() - 1);
    }

    public void addAll(List<Manufactur> moveResults) {
        for (Manufactur result : moveResults) {
            add(result);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public Object[] getSections() {
        List<String> sections = new ArrayList<>();
        List<String> sections_atoz = new ArrayList<>();
        mSectionPositions = new ArrayList<>();

        //adding static data ato z
        for (char letter = 'A'; letter <= 'Z'; letter++) {
            sections_atoz.add(String.valueOf(letter));
        }

        Log.e("sections_atoz", "dfd"+sections_atoz);
        for (int i = 0, size = model.size(); i < size; i++) {
            Log.e("Debug_99", "dfd" + String.valueOf(model.get(i).getName().charAt(0)).toUpperCase());

            String section = String.valueOf(model.get(i).getName().charAt(0)).toUpperCase();
            Log.e("Debug_99section", "dfd" + section);
            Log.e("Debug_105section", "dfd" + sections);
            if (!sections.contains(section)) {
                Log.e("Debug_sec", "dfd" + sections);
                sections.add(section);
                mSectionPositions.add(i);
            }

            Log.e("Debug_selection_111", "selection=" + mSectionPositions);
        }
        Log.e("Debug_return", "=" + sections.toArray(new String[0]));

        return sections.toArray(new String[0]);
    }

    @Override
    public int getPositionForSection(int i) {
        Log.e("debug_119", "=" + mSectionPositions.get(i));
        return mSectionPositions.get(i);
    }

    @Override
    public int getSectionForPosition(int i) {
        return 0;
    }

    public Manufactur getItem(int position) {
        return model.get(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvmenufacturerName, tvmenufactureraddress, tvTitle, tvmenufactureraweb;
        LinearLayout lv_subcategory_click, lv_header_text;

        public MyViewHolder(@NonNull View view) {
            super(view);
            tvmenufacturerName = view.findViewById(R.id.tvmenufacturerName);
            tvmenufactureraddress = view.findViewById(R.id.tvmenufactureraddress);
            tvmenufactureraweb = view.findViewById(R.id.tvmenufactureraweb);
            lv_subcategory_click = view.findViewById(R.id.lv_subcategory_click);
            lv_header_text = view.findViewById(R.id.lv_header_text);
            tvTitle = view.findViewById(R.id.tvTitle);
        }
    }

}