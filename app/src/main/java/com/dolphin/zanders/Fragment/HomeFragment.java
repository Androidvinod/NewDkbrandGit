package com.dolphin.zanders.Fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dolphin.zanders.Activity.NavigationActivity;
import com.dolphin.zanders.Adapter.DailySpecialAdapter;
import com.dolphin.zanders.Adapter.Filterlist_Adapter;
import com.dolphin.zanders.Adapter.HomePackagesAdapter;
import com.dolphin.zanders.Adapter.HomePopularProductsAdapter;

import com.dolphin.zanders.Adapter.Manufacturers_Adapter;

import com.dolphin.zanders.Adapter.SubManufacturers_Adapter;
import com.dolphin.zanders.Model.Home_model.HomePage;
import com.dolphin.zanders.Model.Home_model.ProductsDaily;
import com.dolphin.zanders.Model.Home_model.ProductsManufacturer;
import com.dolphin.zanders.Model.Home_model.ProductsTopseller;
import com.dolphin.zanders.Preference.Login_preference;
import com.dolphin.zanders.R;
import com.dolphin.zanders.Retrofit.ApiClient;
import com.dolphin.zanders.Retrofit.ApiInterface;
import com.dolphin.zanders.Util.CheckNetwork;
import com.dolphin.zanders.Util.YouTubeConfig;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 */

public class HomeFragment extends Fragment implements View.OnClickListener {

    RecyclerView rv_manufacturers;
    private static Manufacturers_Adapter manufacturers_adapter;

    RecyclerView rv_sub_manufacturers;
    private static SubManufacturers_Adapter submanufacturers_adapter;

    private static List<HomePage> homePageList = new ArrayList<HomePage>();

    RecyclerView rv_daily_special;
    private static DailySpecialAdapter dailySpecialAdapter;

    RecyclerView rv_extra_package;
    private static HomePackagesAdapter homePackagesAdapter;

    RecyclerView rv_popular_products;
    private static HomePopularProductsAdapter homePopularProductsAdapter;

    TextView tv_manufacturers_title, tv_seeall_manufacturer, tv_daily_specials_title, tv_seeall_dailyspecial, tv_popularproducts_title, tv_seeall_popularproducts;

    LinearLayout lv_see_all_manufacturers, lv_popular_product_seeall, lv_zandersdaily_seeall;

    private ApiInterface home, homepage_ds, home_pp;

    public static String videourl, video_id, spl;

    LinearLayout lv_thumbnail,lv_main_home, lv_video_main,lv_main_manufacturers,lv_main_sub_manu,lv_main_daily_specials,lv_main_packages,lv_main_popular_products;
    ImageView iv_thumb_home;
    YouTubePlayer YPlay;

    private ShimmerFrameLayout shimmer_view_manufacturers, shimmer_view_daily_specials, shimmer_view_popular_products, shimmer_view_submanufacturers, shimmer_view_packages;

    String videoid;
    YouTubePlayerView youTubePlayerView;

    View v;
    NavigationActivity parent;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for parent fragment
        v = inflater.inflate(R.layout.fragment_home_fregment, container, false);
        AllocateMemory();
        parent=(NavigationActivity) getActivity();
        attachRecyclerView();
        setHasOptionsMenu(true);
        setTypeFace();
        setupUI(lv_main_home);
        Filterlist_Adapter.filter_child_value_list.clear();

        if (CheckNetwork.isNetworkAvailable(parent)) {

            CallHomeVideosApi();
            CallManufacturersApi();
            CallSubManufacturersApi();
            CallDailySpecialApi();
            CallPackagesApi();
            CallPopularProductsApi();
        } else {
            Toast.makeText(parent, parent.getResources().getString(R.string.intcon), Toast.LENGTH_SHORT).show();
        }

        lv_see_all_manufacturers.setOnClickListener(this);
        lv_popular_product_seeall.setOnClickListener(this);
        lv_zandersdaily_seeall.setOnClickListener(this);

        Log.e("login_flag_home", "" + Login_preference.getLogin_flag(parent));

        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer) {
                /*String videoId = "S0Q4gqBUs7c";*/
                if (videoid!=null){
                    youTubePlayer.loadVideo(videoid, 0);
                    youTubePlayer.pause();
                }
                super.onReady(youTubePlayer);
            }
        });

        return v;
    }

    private void attachRecyclerView() {

        rv_manufacturers = v.findViewById(R.id.rv_manufacturers);
        manufacturers_adapter = new Manufacturers_Adapter(parent);
        rv_manufacturers.setLayoutManager(new LinearLayoutManager(parent, LinearLayoutManager.HORIZONTAL, false));
        rv_manufacturers.setAdapter(manufacturers_adapter);

        rv_sub_manufacturers = v.findViewById(R.id.rv_sub_manufacturerss);
        submanufacturers_adapter = new SubManufacturers_Adapter(parent);
        rv_sub_manufacturers.setLayoutManager(new LinearLayoutManager(parent, LinearLayoutManager.HORIZONTAL, false));
        rv_sub_manufacturers.setAdapter(submanufacturers_adapter);

        rv_daily_special = v.findViewById(R.id.rv_daily_special);
        dailySpecialAdapter = new DailySpecialAdapter(parent);
        rv_daily_special.setLayoutManager(new LinearLayoutManager(parent, LinearLayoutManager.HORIZONTAL, false));
        rv_daily_special.setAdapter(dailySpecialAdapter);

        rv_extra_package = v.findViewById(R.id.rv_extra_package);
        homePackagesAdapter = new HomePackagesAdapter(parent);
        rv_extra_package.setLayoutManager(new LinearLayoutManager(parent, LinearLayoutManager.HORIZONTAL, false));
        rv_extra_package.setAdapter(homePackagesAdapter);

        rv_popular_products = v.findViewById(R.id.rv_popular_products);
        homePopularProductsAdapter = new HomePopularProductsAdapter(parent);
        rv_popular_products.setLayoutManager(new LinearLayoutManager(parent, LinearLayoutManager.HORIZONTAL, false));
        rv_popular_products.setAdapter(homePopularProductsAdapter);

    }

    private void setTypeFace() {
        tv_manufacturers_title.setTypeface(NavigationActivity.montserratbold);
        tv_seeall_manufacturer.setTypeface(NavigationActivity.montserratbold);
        tv_daily_specials_title.setTypeface(NavigationActivity.montserratbold);
        tv_seeall_dailyspecial.setTypeface(NavigationActivity.montserratbold);
        tv_popularproducts_title.setTypeface(NavigationActivity.montserratbold);
        tv_seeall_popularproducts.setTypeface(NavigationActivity.montserratbold);
    }

    private void AllocateMemory() {
        lv_main_home = v.findViewById(R.id.lv_main_home);
        youTubePlayerView = v.findViewById(R.id.youtube_player_view);
        home = ApiClient.getClient().create(ApiInterface.class);
        homepage_ds = ApiClient.getClient().create(ApiInterface.class);
        home_pp = ApiClient.getClient().create(ApiInterface.class);
        shimmer_view_manufacturers = v.findViewById(R.id.shimmer_view_manufacturers);
        shimmer_view_daily_specials = v.findViewById(R.id.shimmer_view_daily_specials);
        shimmer_view_popular_products = v.findViewById(R.id.shimmer_view_popular_products);
        shimmer_view_submanufacturers = v.findViewById(R.id.shimmer_view_submanufacturers);
        shimmer_view_packages = v.findViewById(R.id.shimmer_view_packages);
        lv_thumbnail = v.findViewById(R.id.lv_thumbnailll);
        lv_video_main = v.findViewById(R.id.lv_video_main);
        //iv_thumb_home = v.findViewById(R.id.iv_thumb_home);
        lv_main_manufacturers = v.findViewById(R.id.lv_main_manufacturers);
        lv_main_daily_specials = v.findViewById(R.id.lv_main_daily_specials);
        lv_main_popular_products = v.findViewById(R.id.lv_main_popular_products);
        lv_main_packages = v.findViewById(R.id.lv_main_packages);
        lv_main_sub_manu = v.findViewById(R.id.lv_main_sub_manu);
        lv_zandersdaily_seeall = v.findViewById(R.id.lv_zandersdaily_seeall);
        lv_popular_product_seeall = v.findViewById(R.id.lv_popular_product_seeall);
        lv_see_all_manufacturers = v.findViewById(R.id.lv_see_all_manufacturers);
        tv_manufacturers_title = v.findViewById(R.id.tv_manufacturers_title);
        tv_seeall_manufacturer = v.findViewById(R.id.tv_seeall_manufacturer);
        tv_daily_specials_title = v.findViewById(R.id.tv_daily_specials_title);
        tv_seeall_dailyspecial = v.findViewById(R.id.tv_seeall_dailyspecial);
        tv_popularproducts_title = v.findViewById(R.id.tv_popularproducts_title);
        tv_seeall_popularproducts = v.findViewById(R.id.tv_seeall_popularproducts);
    }

    private void CallHomeVideosApi() {

        homePageList.clear();
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);

        Call<ResponseBody> homevideos = api.getSubmanu();
        homevideos.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                JSONObject jsonObject = null;

                try {
                    jsonObject = new JSONObject(response.body().string());
                    videourl = jsonObject.getString("vedio");

                    if (jsonObject.getString("vedio") == null || videourl.equalsIgnoreCase(null) | jsonObject.getString("vedio").equalsIgnoreCase("") || videourl.equalsIgnoreCase("")) {
                        lv_video_main.setVisibility(View.GONE);
                    } else {
                        lv_video_main.setVisibility(View.VISIBLE);
                        videoid = getYoutubeID(videourl);
                        String vidpic = "http://img.youtube.com/vi/" + videoid + "/mqdefault.jpg";

                     /*   if (isAdded()){
                            final RequestOptions requestOptions = new RequestOptions();
                            requestOptions.placeholder(R.drawable.logo_red);
                            requestOptions.error(R.drawable.logo_red);
                            Glide.with(getContext())
                                    .setDefaultRequestOptions(requestOptions)
                                    .load(vidpic).into(iv_thumb_home);
                        }*/
                       // YoutubeVideoPlay(tst);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(parent, t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });

    }

/*    private void YoutubeVideoPlay(final String tst) {
        Log.e("vidsYT", "" + tst);
        YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        final AppCompatActivity activity = (AppCompatActivity) parent;
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.youtube_fragment, youTubePlayerFragment).commit();
        youTubePlayerFragment.initialize(YouTubeConfig.YT_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                if (!b) {

                    YPlay = youTubePlayer;
                    YPlay.loadVideo(tst);
                    YPlay.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);
                    YPlay.setShowFullscreenButton(true);
                    YPlay.play();
                    //YPlay.getCurrentTimeMillis();
                    YPlay.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                        @Override
                        public void onLoading() {

                        }

                        @Override
                        public void onLoaded(String s) {
                            YPlay.pause();
                        }

                        @Override
                        public void onAdStarted() {

                        }

                        @Override
                        public void onVideoStarted() {

                        }

                        @Override
                        public void onVideoEnded() {

                        }

                        @Override
                        public void onError(YouTubePlayer.ErrorReason errorReason) {

                        }
                    });
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText(getContext(), "Video Problem", Toast.LENGTH_SHORT).show();
            }
        });

    }*/

    private String getYoutubeID(String videourl) {
        if (TextUtils.isEmpty(videourl)) {
            return "";
        }
        video_id = "";

        String expression = "^.*((youtu.be" + "\\/)" + "|(v\\/)|(\\/u\\/w\\/)|(embed\\/)|(watch\\?))\\??v?=?([^#\\&\\?]*).*"; // var regExp = /^.*((youtu.be\/)|(v\/)|(\/u\/\w\/)|(embed\/)|(watch\?))\??v?=?([^#\&\?]*).*/;
        CharSequence input = videourl;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            String groupIndex1 = matcher.group(7);
            if (groupIndex1 != null && groupIndex1.length() == 11)
                video_id = groupIndex1;
        }
        if (TextUtils.isEmpty(video_id)) {
            if (videourl.contains("youtu.be/")) {
                spl = videourl.split("youtu.be/")[1];
                if (spl.contains("\\?")) {
                    video_id = spl.split("\\?")[0];
                } else {
                    video_id = spl;
                }
            }
        }
        Log.e("vidid", "" + video_id);
        return video_id;
    }

    private void CallManufacturersApi() {
        lv_main_manufacturers.setVisibility(View.VISIBLE);
        rv_manufacturers.setVisibility(View.VISIBLE);
        callManufacturers().enqueue(new Callback<HomePage>() {
            @Override
            public void onResponse(Call<HomePage> call, Response<HomePage> response) {
                shimmer_view_manufacturers.stopShimmerAnimation();
                shimmer_view_manufacturers.setVisibility(View.GONE);

                if (fetchResultsManufacturers(response)==null || fetchResultsManufacturers(response).size()==0)
                {
                    lv_main_manufacturers.setVisibility(View.GONE);
                    rv_manufacturers.setVisibility(View.GONE);
                    shimmer_view_manufacturers.stopShimmerAnimation();
                    shimmer_view_manufacturers.setVisibility(View.GONE);
                }else {
                    List<ProductsManufacturer> manufacturers = fetchResultsManufacturers(response);
                    if (manufacturers.isEmpty()){
                        lv_main_manufacturers.setVisibility(View.GONE);
                        rv_manufacturers.setVisibility(View.GONE);
                        shimmer_view_manufacturers.stopShimmerAnimation();
                        shimmer_view_manufacturers.setVisibility(View.GONE);
                    }else {
                        lv_main_manufacturers.setVisibility(View.VISIBLE);
                        rv_manufacturers.setVisibility(View.VISIBLE);
                    }
                    manufacturers_adapter.addAll(manufacturers);
                    shimmer_view_manufacturers.stopShimmerAnimation();
                    shimmer_view_manufacturers.setVisibility(View.GONE);
                }

                /*if (response.body().getStatus().equals("error")) {

                } else {
                    List<ProductsManufacturer> manufacturers = fetchResultsManufacturers(response);
                    manufacturers_adapter.addAll(manufacturers);
                }*/

            }

            @Override
            public void onFailure(Call<HomePage> call, Throwable t) {
                Toast.makeText(parent, t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });

    }

    private List<ProductsManufacturer> fetchResultsManufacturers(Response<HomePage> response) {
        HomePage homePage = response.body();
        return homePage.getManufacturers().getProductsManufacturers();
    }

    private Call<HomePage> callManufacturers() {
        return home.gethomepage();
    }

    private void CallSubManufacturersApi() {
        shimmer_view_submanufacturers.setVisibility(View.VISIBLE);
        lv_main_sub_manu.setVisibility(View.VISIBLE);
        rv_sub_manufacturers.setVisibility(View.VISIBLE);
        callSubManufacturers().enqueue(new Callback<HomePage>() {
            @Override
            public void onResponse(Call<HomePage> call, Response<HomePage> response) {
                shimmer_view_submanufacturers.stopShimmerAnimation();
                shimmer_view_submanufacturers.setVisibility(View.GONE);

                if (fetchResultssubManufacturers(response)==null|| fetchResultssubManufacturers(response).size()==0)
                {
                    lv_main_sub_manu.setVisibility(View.GONE);
                    rv_sub_manufacturers.setVisibility(View.GONE);
                    shimmer_view_submanufacturers.stopShimmerAnimation();
                    shimmer_view_submanufacturers.setVisibility(View.GONE);
                }else {
                    List<String> homePagesm = fetchResultssubManufacturers(response);
                    if (homePagesm.isEmpty()){
                        lv_main_sub_manu.setVisibility(View.GONE);
                        rv_sub_manufacturers.setVisibility(View.GONE);
                        shimmer_view_submanufacturers.stopShimmerAnimation();
                        shimmer_view_submanufacturers.setVisibility(View.GONE);
                    }else {
                        lv_main_sub_manu.setVisibility(View.VISIBLE);
                        rv_sub_manufacturers.setVisibility(View.VISIBLE);
                    }
                    submanufacturers_adapter.addAll(homePagesm);
                    shimmer_view_submanufacturers.stopShimmerAnimation();
                    shimmer_view_submanufacturers.setVisibility(View.GONE);
                }

                /*if (response.body().getStatus().equals("error")) {

                } else {
                    List<String> homePagesm = fetchResultssubManufacturers(response);
                    submanufacturers_adapter.addAll(homePagesm);
                }*/
            }

            @Override
            public void onFailure(Call<HomePage> call, Throwable t) {
                Toast.makeText(parent, t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });

    }

    private List<String> fetchResultssubManufacturers(Response<HomePage> response) {
        HomePage homePagesm = response.body();
        return homePagesm.getImageBlocks2();
    }

    private Call<HomePage> callSubManufacturers() {
        return home.gethomepage();
    }

    private void CallDailySpecialApi() {

        lv_main_daily_specials.setVisibility(View.VISIBLE);
        rv_daily_special.setVisibility(View.VISIBLE);
        callDailySpecials().enqueue(new Callback<HomePage>() {
            @Override
            public void onResponse(Call<HomePage> call, Response<HomePage> response) {
                shimmer_view_daily_specials.stopShimmerAnimation();
                shimmer_view_daily_specials.setVisibility(View.GONE);

                if (fetchResultsDailySpecials(response)==null || fetchResultsDailySpecials(response).size()==0)
                {
                    lv_main_daily_specials.setVisibility(View.GONE);
                    rv_daily_special.setVisibility(View.GONE);
                    shimmer_view_daily_specials.stopShimmerAnimation();
                    shimmer_view_daily_specials.setVisibility(View.GONE);
                }else {
                    List<ProductsDaily> dailySpecials = fetchResultsDailySpecials(response);
                    if (dailySpecials.isEmpty()){
                        lv_main_daily_specials.setVisibility(View.GONE);
                        rv_daily_special.setVisibility(View.GONE);
                        shimmer_view_daily_specials.stopShimmerAnimation();
                        shimmer_view_daily_specials.setVisibility(View.GONE);
                    }else {
                        lv_main_daily_specials.setVisibility(View.VISIBLE);
                        rv_daily_special.setVisibility(View.VISIBLE);
                    }
                    dailySpecialAdapter.addAll(dailySpecials);
                    shimmer_view_daily_specials.stopShimmerAnimation();
                    shimmer_view_daily_specials.setVisibility(View.GONE);
                }

                /*if (response.body().getStatus().equals("error")) {

                } else {
                    List<ProductsDaily> dailySpecials = fetchResultsDailySpecials(response);
                    dailySpecialAdapter.addAll(dailySpecials);
                }*/
            }

            @Override
            public void onFailure(Call<HomePage> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });

    }

    private List<ProductsDaily> fetchResultsDailySpecials(Response<HomePage> response) {
        HomePage homePageds = response.body();
        return homePageds.getDailySpecial().getProductsDaily();
    }

    private Call<HomePage> callDailySpecials() {
        return homepage_ds.gethomepage();
    }

    private void CallPackagesApi() {
        shimmer_view_packages.setVisibility(View.VISIBLE);
        lv_main_packages.setVisibility(View.VISIBLE);
        rv_extra_package.setVisibility(View.VISIBLE);
        callPackages().enqueue(new Callback<HomePage>() {
            @Override
            public void onResponse(Call<HomePage> call, Response<HomePage> response) {
                shimmer_view_packages.stopShimmerAnimation();
                shimmer_view_packages.setVisibility(View.GONE);

                if (fetchResultspackage(response)==null|| fetchResultspackage(response).size()==0)
                {
                    lv_main_packages.setVisibility(View.GONE);
                    rv_extra_package.setVisibility(View.GONE);
                    shimmer_view_packages.stopShimmerAnimation();
                    shimmer_view_packages.setVisibility(View.GONE);
                }else {
                    List<String> homePagesm = fetchResultspackage(response);
                    if (homePagesm.isEmpty()){
                        lv_main_packages.setVisibility(View.GONE);
                        rv_extra_package.setVisibility(View.GONE);
                        shimmer_view_packages.stopShimmerAnimation();
                        shimmer_view_packages.setVisibility(View.GONE);
                    }else {
                        lv_main_packages.setVisibility(View.VISIBLE);
                        rv_extra_package.setVisibility(View.VISIBLE);
                    }
                    homePackagesAdapter.addAll(homePagesm);
                    shimmer_view_packages.stopShimmerAnimation();
                    shimmer_view_packages.setVisibility(View.GONE);
                }

                /*if (response.body().getStatus().equals("error")) {

                } else {
                    List<String> homePagesm = fetchResultspackage(response);
                    homePackagesAdapter.addAll(homePagesm);
                }*/
            }

            @Override
            public void onFailure(Call<HomePage> call, Throwable t) {
                Toast.makeText(parent, t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });

    }

    private List<String> fetchResultspackage(Response<HomePage> response) {
        HomePage homePagesm = response.body();
        return homePagesm.getStaticImage();
    }

    private Call<HomePage> callPackages() {
        return home.gethomepage();
    }

    private void CallPopularProductsApi() {
        lv_main_popular_products.setVisibility(View.VISIBLE);
        rv_popular_products.setVisibility(View.VISIBLE);
        callopularProducts().enqueue(new Callback<HomePage>() {
            @Override
            public void onResponse(Call<HomePage> call, Response<HomePage> response) {
                shimmer_view_popular_products.stopShimmerAnimation();
                shimmer_view_popular_products.setVisibility(View.GONE);

                if (fetchResultsPopularProducts(response)==null || fetchResultsPopularProducts(response).size()==0)
                {
                    lv_main_popular_products.setVisibility(View.GONE);
                    rv_popular_products.setVisibility(View.GONE);
                    shimmer_view_popular_products.stopShimmerAnimation();
                    shimmer_view_popular_products.setVisibility(View.GONE);
                }else {
                    List<ProductsTopseller> popularprods = fetchResultsPopularProducts(response);
                    if (popularprods.isEmpty()){
                        lv_main_popular_products.setVisibility(View.GONE);
                        rv_popular_products.setVisibility(View.GONE);
                        shimmer_view_popular_products.stopShimmerAnimation();
                        shimmer_view_popular_products.setVisibility(View.GONE);
                    }else {
                        lv_main_popular_products.setVisibility(View.VISIBLE);
                        rv_popular_products.setVisibility(View.VISIBLE);
                    }
                    homePopularProductsAdapter.addAll(popularprods);
                    shimmer_view_popular_products.stopShimmerAnimation();
                    shimmer_view_popular_products.setVisibility(View.GONE);
                }

                /*if (response.body().getStatus().equals("error")) {

                } else {
                    List<ProductsTopseller> popularprods = fetchResultsPopularProducts(response);
                    homePopularProductsAdapter.addAll(popularprods);
                }*/
            }

            @Override
            public void onFailure(Call<HomePage> call, Throwable t) {
                Toast.makeText(parent, t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private List<ProductsTopseller> fetchResultsPopularProducts(Response<HomePage> response) {
        HomePage homePageds = response.body();
        return homePageds.getPopularProducts().getProductsTopsellers();
    }

    private Call<HomePage> callopularProducts() {
        return home_pp.gethomepage();
    }

    private void pushFragment(Fragment fragment, String add_to_backstack) {
        if (fragment == null)
            return;
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (ft != null) {
                ft.replace(R.id.framlayout, fragment);
                ft.addToBackStack(add_to_backstack);
                ft.setCustomAnimations(R.anim.fade_in,
                        0, 0, R.anim.fade_out).setCustomAnimations(R.anim.fade_in,
                        0, 0, R.anim.fade_out);
                ft.commit();
            }
        }
    }
    @Override
    public void onClick(View v) {
        if (v == lv_see_all_manufacturers) {
            Filterlist_Adapter.filter_child_value_list.clear();
            pushFragment(new Manufacturerslist(),"manufacturerslist");
            // pushFragment(new GetCategoriesProductlist_Fragment(),"product");
        } else if (v == lv_popular_product_seeall) {
            Filterlist_Adapter.filter_child_value_list.clear();
            String title="Popular products";
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            Bundle b = new Bundle();
            b.putString("subcatename",title);
            ProductListFragment myFragment = new ProductListFragment();
            myFragment.setArguments(b);
            activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                    0, 0, R.anim.fade_out).setCustomAnimations(R.anim.fade_in,
                    0, 0, R.anim.fade_out).replace(R.id.framlayout, myFragment).addToBackStack(null).commit();
            // pushFragment(new GetCategoriesProductlist_Fragment(),"product");
        } else if (v == lv_zandersdaily_seeall) {
            Filterlist_Adapter.filter_child_value_list.clear();
            String title="Daily specials products";
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            Bundle b = new Bundle();
            b.putString("subcatename",title);
            ProductListFragment myFragment = new ProductListFragment();
            myFragment.setArguments(b);
            activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                    0, 0, R.anim.fade_out).setCustomAnimations(R.anim.fade_in,
                    0, 0, R.anim.fade_out).replace(R.id.framlayout, myFragment).addToBackStack(null).commit();

            // pushFragment(new GetCategoriesProductlist_Fragment(),"product");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.search:
                Filterlist_Adapter.filter_child_value_list.clear();
                pushFragment(new SearchFragment(), "search");
                return true;

            case android.R.id.home:
                parent.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        shimmer_view_manufacturers.startShimmerAnimation();
        shimmer_view_daily_specials.startShimmerAnimation();
        shimmer_view_popular_products.startShimmerAnimation();
        shimmer_view_submanufacturers.startShimmerAnimation();
        shimmer_view_packages.startShimmerAnimation();
        if (YPlay != null) {
            YPlay.pause();
        }
    }
    @Override
    public void onPause() {
        shimmer_view_manufacturers.stopShimmerAnimation();
        shimmer_view_daily_specials.stopShimmerAnimation();
        shimmer_view_popular_products.stopShimmerAnimation();
        shimmer_view_submanufacturers.stopShimmerAnimation();
        shimmer_view_packages.stopShimmerAnimation();
        if (YPlay != null) {
            YPlay.pause();
        }
        super.onPause();
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideKeyboard(parent);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View currentFocusedView = activity.getCurrentFocus();
        if (currentFocusedView != null) {
            inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
