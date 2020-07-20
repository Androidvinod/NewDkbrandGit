package com.dolphin.zanders.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.dolphin.zanders.Fragment.AccountDashboard;
import com.dolphin.zanders.Fragment.Address_Book;
import com.dolphin.zanders.Fragment.EditAddressFragment;
import com.dolphin.zanders.Model.Addresss_Modell.Address;
import com.dolphin.zanders.Preference.Login_preference;
import com.dolphin.zanders.R;

import com.dolphin.zanders.Retrofit.ApiClientcusome;
import com.dolphin.zanders.Retrofit.ApiInterface;
import com.dolphin.zanders.Util.CheckNetwork;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class  Addressbook_Adapter  extends RecyclerView.Adapter<Addressbook_Adapter.MyViewHolder>{

    private Context context;
    private List<Address> models;
    String screenname;

    public Addressbook_Adapter(Context context, String screen) {
        this.context = context;
        this.screenname = screen;
        models = new ArrayList<Address>();
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_getaddressbooklist, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        final Address address = models.get(position);

      
      holder.lv_edit_addrss.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              String add_id= String.valueOf(models.get(position).getId());
              Bundle b=new Bundle();
              AppCompatActivity activity = (AppCompatActivity) v.getContext();
              b.putString("screen","edit address");
              b.putString("address","edit");
              b.putString("address_id",add_id);
              b.putString("screenname",screenname);

              EditAddressFragment myFragment = new EditAddressFragment();
              myFragment.setArguments(b);
              activity.getSupportFragmentManager().beginTransaction()
                      .addToBackStack(null).replace(R.id.framlayout, myFragment)
                      .commit();
          }
      });
      
      holder.lv_delete_add.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              String add_id= String.valueOf(models.get(position).getId());

              Log.e("debuaddid55","="+add_id);

                  Log.e("debuaddid554444444","="+add_id);

                  DelerteAddressApi(add_id,v,screenname,holder);

          }
      });

        List<String> street=new ArrayList<>();
        street=address.getStreet();
        Log.e("addres9999","43   "+address.getFirstname()+"\n"+street.get(0)+"\n"+address.getRegion()+","+address.getCity()+","+address.getPostcode()+"\n"+address.getLastname()+"\n"+"T :"+address.getTelephone());
        Log.e("address7777","43   "+address.getFirstname()+"\n"+address.getStreet()+"\n"+address.getRegion()+","+address.getCity()+","+address.getPostcode()+"\n"+address.getLastname()+"\n"+"T :"+address.getTelephone());
        holder.tv_adderss.setText(address.getFirstname()+" "+address.getLastname()+", \n"+street.get(0)+" "+","+address.getCity()+","+address.getPostcode()+"\n"+"T :"+address.getTelephone());

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public void addAll(List<Address> feesInnerData) {
        for (Address Address : feesInnerData) {
            add(Address);
        }
    }
    public void add(Address r) {
        models.add(r);
        notifyItemInserted(models.size() - 1);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_adderss;
         LinearLayout lv_edit_addrss,lv_delete_add,lv_add_progress,lv_add_mainnn;

        public MyViewHolder(@NonNull View view) {
            super(view);
            tv_adderss=view.findViewById(R.id.tv_adderss);
            lv_edit_addrss=view.findViewById(R.id.lv_edit_addrss);
            lv_delete_add=view.findViewById(R.id.lv_delete_add);
            lv_add_progress=view.findViewById(R.id.lv_add_progress);
            lv_add_mainnn=view.findViewById(R.id.lv_add_mainnn);

        }
    }

    private Call<Boolean> delerteAddressApi(String add_id) {
        ApiInterface customeapi = ApiClientcusome.getClient().create(ApiInterface.class);

        String url="http://dkbraende.demoproject.info/rest/V1/addresses/"+add_id;
        Log.e("debug_url","="+url);
        Log.e("token","="+ Login_preference.gettoken(context));

        return customeapi.deleteaddress("Bearer "+Login_preference.gettoken(context),url);
    }


    private void DelerteAddressApi(String add_id, View v, String screenname, MyViewHolder holder) {
        holder.lv_add_progress.setVisibility(View.VISIBLE);
        holder.lv_add_mainnn.setVisibility(View.GONE);
        delerteAddressApi(add_id).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                holder.lv_add_progress.setVisibility(View.GONE);
                holder.lv_add_mainnn.setVisibility(View.VISIBLE);

                Log.e("debug1111",""+ response.body());
                Log.e("debug1111dsgdf",""+ response);

                if(response.code()==200 || response.isSuccessful())
                {
                    if(response.body()==true)
                    {

                        Toast.makeText(context, "Address Deleted Successfully", Toast.LENGTH_SHORT).show();
                        if(screenname.equalsIgnoreCase("null") || screenname ==null)
                        {
                            AppCompatActivity activity = (AppCompatActivity) v.getContext();
                            AccountDashboard myFragment = new AccountDashboard();
                            activity.getSupportFragmentManager().beginTransaction()
                                    .addToBackStack("account").replace(R.id.framlayout, myFragment)
                                    .commit();
                        }
                        else if(screenname.equalsIgnoreCase("AddressBook"))
                        {
                            AppCompatActivity activity = (AppCompatActivity) v.getContext();
                            Address_Book myFragment = new Address_Book();
                            activity.getSupportFragmentManager().beginTransaction()
                                    .addToBackStack("Address_Book").replace(R.id.framlayout, myFragment)
                                    .commit();
                        }else if(screenname.equalsIgnoreCase("AccountDashboard")){
                            AppCompatActivity activity = (AppCompatActivity) v.getContext();
                            AccountDashboard myFragment = new AccountDashboard();
                            activity.getSupportFragmentManager().beginTransaction()
                                    .addToBackStack("account").replace(R.id.framlayout, myFragment)
                                    .commit();
                        }


                    }
                }else {
                    //   Toast.makeText(context(), "The password doesn't match this account. Verify the password and try again.", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                holder.lv_add_progress.setVisibility(View.GONE);
                holder.lv_add_mainnn.setVisibility(View.VISIBLE);
                Toast.makeText(context, ""+context.getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
