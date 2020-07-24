package com.dolphin.zanders.Fragment;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.core.text.HtmlCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dolphin.zanders.Activity.NavigationActivity;
import com.dolphin.zanders.Model.CategoryModel.Category;
import com.dolphin.zanders.Model.CategoryModel.CategoryModel;
import com.dolphin.zanders.Model.Homedk.Homedata;
import com.dolphin.zanders.Preference.Login_preference;
import com.dolphin.zanders.R;
import com.dolphin.zanders.Retrofit.ApiClient;
import com.dolphin.zanders.Retrofit.ApiClientcusome;
import com.dolphin.zanders.Retrofit.ApiInterface;
import com.dolphin.zanders.Util.CheckNetwork;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dolphin.zanders.Activity.NavigationActivity.bottom_navigation;
import static com.dolphin.zanders.Activity.NavigationActivity.drawer;


public class Home_dk extends Fragment {

    ImageView iv_banner;
    TextView tv_description;
    ApiInterface api;
    LinearLayout lv_login_progress;
    public static MenuItem login_home;
    String logingflag;
    public static Toolbar toolbar_homedk;

    public Home_dk() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_home_dk, container, false);
        api = ApiClientcusome.getClient().create(ApiInterface.class);
        bottom_navigation.getMenu().getItem(0).setChecked(true);
        AttachRecyclerview(v);

        logingflag = Login_preference.getLogin_flag(getActivity());
        ((NavigationActivity) getActivity()).setSupportActionBar(toolbar_homedk);
        ((NavigationActivity) getActivity()).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
        ((NavigationActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        toolbar_homedk.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        if (CheckNetwork.isNetworkAvailable(getActivity())) {
            gethomedata();
        } else {
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
        }
        return v;
    }

    private void AttachRecyclerview(View v) {
        iv_banner=v.findViewById(R.id.iv_banner);
        tv_description=v.findViewById(R.id.tv_description);
        lv_login_progress=v.findViewById(R.id.lv_login_progress);
        toolbar_homedk=v.findViewById(R.id.toolbar_homedk);
    }

    private void gethomedata() {
        lv_login_progress.setVisibility(View.VISIBLE);
        callCategoryApi().enqueue(new Callback<Homedata>() {
            @Override
            public void onResponse(Call<Homedata> call, Response<Homedata> response) {
              //  shimmer_view_catalog.stopShimmerAnimation();
             //   shimmer_view_catalog.setVisibility(View.GONE);
                lv_login_progress.setVisibility(View.GONE);
                Homedata categoryModel = response.body();
                Log.e("statussssss", "" + categoryModel.getStatus());
                if (categoryModel.getStatus().equalsIgnoreCase("Success")) {
                 //   shimmer_view_catalog.stopShimmerAnimation();
                 //   shimmer_view_catalog.setVisibility(View.GONE);
                    Glide.with(getActivity()).load(categoryModel.getMobileHomeBanner().getImage()).into(iv_banner);
                    tv_description.setText(HtmlCompat.fromHtml(categoryModel.getMainContent(), 0));


                } else {
              //      shimmer_view_catalog.stopShimmerAnimation();
                //    shimmer_view_catalog.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), categoryModel.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
            @Override
            public void onFailure(Call<Homedata> call, Throwable t) {
                //shimmer_view_catalog.stopShimmerAnimation();
               // shimmer_view_catalog.setVisibility(View.GONE);
                lv_login_progress.setVisibility(View.GONE);
                Toast.makeText(getActivity(), ""+t, Toast.LENGTH_SHORT).show();
                // String error=  t.printStackTrace();
              //  Toast.makeText(getActivity(), "" + getActivity().getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();
                Log.e("debug_175125", "pages: " + t);
            }
        });
    }
    private Call<Homedata> callCategoryApi() {
        return api.gethomedata("http://dkbraende.demoproject.info/customapi/AppHomePage.php?store=1");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_home, menu);
        login_home = menu.findItem(R.id.login);
        Log.e("menu_389--", "");
        if (logingflag.equals("1")) {
            Log.e("menu_392--", "false");
            login_home.setVisible(false);
        } else {
            Log.e("menu_395--", "true");
            login_home.setVisible(true);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

           /* case R.id.search:
                Filterlist_Adapter.filter_child_value_list.clear();
                Filterlist_Adapter.filter_grouppp_namelist.clear();
                FilterListFragment.selected_child="";

                pushFragment(new SearchFragment(), "search");
                return true;*/
            case R.id.login:
               // pushFragment(new LoginFragment(), "login");
                return true;

            default:
        }
        return super.onOptionsItemSelected(item);
    }



}