package com.dolphin.zanders.Activity;

import android.content.res.AssetManager;

import android.graphics.Typeface;
import android.os.Bundle;

import com.dolphin.zanders.Adapter.CategoryAdapter;
import com.dolphin.zanders.Adapter.Filterlist_Adapter;
import com.dolphin.zanders.Fragment.AccountFragment;
import com.dolphin.zanders.Fragment.FavouriteFragment;
import com.dolphin.zanders.Fragment.FilterListFragment;

import com.dolphin.zanders.Fragment.HomeFragment_new;
import com.dolphin.zanders.Fragment.Home_dk;
import com.dolphin.zanders.Fragment.LoginFragment;

import com.dolphin.zanders.Fragment.NewCartFragment;
import com.dolphin.zanders.Fragment.SearchFragment;

import com.dolphin.zanders.Fragment.WishlistNewFragment;
import com.dolphin.zanders.Model.CategoriesModel;
import com.dolphin.zanders.Model.ChildData;

import com.dolphin.zanders.Preference.Login_preference;
import com.dolphin.zanders.R;

import com.dolphin.zanders.Retrofit.ApiClientcusome;
import com.dolphin.zanders.Retrofit.ApiInterface;
import com.dolphin.zanders.Util.CheckNetwork;

import com.dolphin.zanders.Util.CustomTypefaceSpan;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.os.Handler;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.material.navigation.NavigationView;
import com.novoda.merlin.Connectable;
import com.novoda.merlin.Merlin;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.DefaultItemAnimator;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NavigationActivity extends AppCompatActivity {
    RecyclerView recv_category;

    //public static Toolbar toolbar;
    ImageView iv_navtoolbar_logo;
    public static DrawerLayout drawer;
    NavigationView navigationView;
    RelativeLayout relative_layout;
    public static BottomNavigationView bottom_navigation;
    public static AssetManager am;
    public static Typeface montserrat_medium, montserrat_regular, montserrat_semibold, montserratbold, montserrat_light;
    boolean doubleBackToExitPressedOnce = false;
   public static ApiInterface customeapi;

    ImageView nav_iv_close;
    CategoryAdapter categoryAdapter;
    public static TextView tv_bottomcount,tv_wishlist_count;
    LinearLayout lv_nodata_category;
    String cartitem_count,wishlist_count;
    private View notificationBadge,wishlist_badge;
    MenuItem login;
    public static NavigationActivity parent;

    ShimmerFrameLayout shimmer_nav_category;

    //hjkkjl






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_navigation);
        AllocateMemory();
        bottom_navigation.setItemIconTintList(null);


        //api = ApiClient.getClient().create(ApiInterface.class);
        customeapi = ApiClientcusome.getClient().create(ApiInterface.class);
        parent = (NavigationActivity) NavigationActivity.this;
        Log.e("debug_tokemn","="+Login_preference.gettoken(getApplicationContext()));
        //setSupportActionBar(toolbar);
        //toolbar.setTitle("");
        setNavigationIcon_headerview();
        setFontStyle();
        SetTypeface();
        bottom_navigation.setItemIconTintList(null);
        Bootom_Navigation_view();
        Filterlist_Adapter.filter_child_value_list.clear();
        //  navigationView.setNavigationItemSelectedListener(this);

        //bind data to recyclerview
        AttachRecyclerView();
        //calling api of categiry list for side menu

      //  gettokenapi();


        Filterlist_Adapter.filter_child_value_list.clear();
        Filterlist_Adapter.filter_grouppp_namelist.clear();
        FilterListFragment.selected_child="";
        CategoryAdapter.datumList.clear();


        if (CheckNetwork.isNetworkAvailable(NavigationActivity.this)) {


            Log.e("111",""+Login_preference.gettoken(NavigationActivity.this));
            if(Login_preference.gettoken(NavigationActivity.this).equalsIgnoreCase("") || Login_preference.gettoken(NavigationActivity.this) ==null)
            {
                Log.e("ddtooent",""+Login_preference.gettoken(NavigationActivity.this));
            }else {
                Log.e("c2oken",""+Login_preference.gettoken(NavigationActivity.this));
                if (Login_preference.getLogin_flag(this).equalsIgnoreCase("1")) {
                    //get_Customer_tokenapi();
                    get_Customer_QuoteId();
                    callWishlistCountApi();
                }
                getCategoryList();
            }

        } else {
            //    noInternetDialog(NavigationActivity.this);
            Toast.makeText(NavigationActivity.this, getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
        }
    }
    public static void get_Customer_QuoteId() {
        Log.e("customertoken",""+Login_preference.getCustomertoken(parent));
        Call<Integer> customertoken = customeapi.getQuoteid("Bearer "+Login_preference.getCustomertoken(parent)
                ,"http://dkbraende.demoproject.info/rest/V1/carts/mine/?customerId="+Login_preference.getcustomer_id(parent));
        customertoken.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                Log.e("res_quoteid",""+response.toString());
                Log.e("resquoteiddd",""+response.body());
                Login_preference.setdkQuoteId(parent, String.valueOf(response.body()));
                Login_preference.setquote_id(parent, String.valueOf(response.body()));

            }
            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(parent, t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    public static void get_Customer_tokenapi() {
        Log.e("response201tokenff",""+Login_preference.gettoken(parent));
        String email=Login_preference.gettokenemail(parent);
        String password=Login_preference.gettokenpassword(parent);
        String url=ApiClientcusome.MAIN_URLL+"integration/customer/token?username="+email+"&password="+password;
        Call<String> customertoken = customeapi.getcustomerToken(url);
        customertoken.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("response200",""+response.toString());
                Log.e("response201",""+response.body());
                Login_preference.setCustomertoken(parent,response.body());
                get_Customer_QuoteId();
                callWishlistCountApi();
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("failure_messge",""+t);
                Toast.makeText(parent, t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }


    public static void callWishlistCountApi() {
        Log.e("response201tokenff",""+Login_preference.gettoken(parent));
        Call<ResponseBody> customertoken = customeapi.defaultWishlistCount("Bearer "+Login_preference.getCustomertoken(parent));
        customertoken.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response200gffgdf",""+response.toString());
                Log.e("response201fgd",""+response.body());
                if(response.code()==200 || response.isSuccessful())
                {
                    try {
                        JSONArray jsonObject = new JSONArray(response.body().string());

                        String count= jsonObject.getJSONObject(0).getString("total_items");
                        tv_wishlist_count.setText(count);
                        Login_preference.set_wishlist_count(parent,count);
                        Log.e("wishcount",""+count);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {

                }

            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(parent, t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private void gettokenapi() {

        Call<String> homevideos = customeapi.gettoken("http://dkbraende.demoproject.info/rest/V1/integration/admin/token/?username=admin&password=9yWpe6v7(OZ7");
        homevideos.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("response200",""+response.toString());
                Log.e("response201",""+response.body());
                Login_preference.settoken(NavigationActivity.this,response.body());

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(NavigationActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

  /*  private void CallCartWishlistCount() {

        getcount().enqueue(new Callback<CountModel>() {
            @Override
            public void onResponse(Call<CountModel> call, Response<CountModel> response) {
                CountModel countModel = response.body();

                Log.e("statussssss", "" + countModel.getStatus());
                if (countModel.getStatus().equalsIgnoreCase("Success"))
                {

                    //show cart count
                    Log.e("debug_count", "" + countModel.getItemsCount());
                    String item_count = null,count_wishlist = null;
                    if(countModel.getItemsCount()==0)
                    {
                        Login_preference.setCart_item_count(NavigationActivity.this,"");
                        //  item_count = String.valueOf(countModel.getItemsCount());
                    }else {
                        Login_preference.setCart_item_count(NavigationActivity.this,String.valueOf( countModel.getItemsCount()));
                        item_count = String.valueOf(countModel.getItemsCount());
                    }
                    if(countModel.getWishlist()==0)
                    {
                        //wishlist count
                        Login_preference.set_wishlist_count(NavigationActivity.this, "");
                        //count_wishlist = String.valueOf(countModel.getWishlist());

                    }else {
                        //wishlist count
                        Login_preference.set_wishlist_count(NavigationActivity.this, String.valueOf(countModel.getWishlist()));
                        count_wishlist = String.valueOf(countModel.getWishlist());

                    }
                    Log.e("count_4011", "" + item_count);

                    if (item_count == null || item_count.equalsIgnoreCase("null") || item_count.equals("")) {
                        Log.e("count_40", "" + String.valueOf(countModel.getItemsCount()));
                        tv_bottomcount.setVisibility(View.GONE);
                    } else {
                        tv_bottomcount.setVisibility(View.VISIBLE);
                        Log.e("count_80", "" + String.valueOf(countModel.getItemsCount()));
                        NavigationActivity.Check_String_NULL_Value(tv_bottomcount, String.valueOf(countModel.getItemsCount()));
                    }
                    //show wishlist count
                    Log.e("item_qtyfav", "" + item_count);

                    if ( count_wishlist == null || count_wishlist.equalsIgnoreCase("null") || count_wishlist.equals("")) {
                        Log.e("count_40fav", "" + String.valueOf(countModel.getWishlist()));
                        tv_wishlist_count.setVisibility(View.GONE);
                    } else {
                        tv_wishlist_count.setVisibility(View.VISIBLE);
                        Log.e("count_80fav", "" + String.valueOf(countModel.getWishlist()));
                        NavigationActivity.Check_String_NULL_Value(tv_wishlist_count, String.valueOf(countModel.getWishlist()));
                    }
                }
                else {
                   // Toast.makeText(NavigationActivity.this, countModel.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<CountModel> call, Throwable t) {
                // String error=  t.printStackTrace();
                Toast.makeText(NavigationActivity.this, "" + getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();

                Log.e("debug_175125", "pages: " + t);
            }
        });
    }
*/

    private void AttachRecyclerView() {
        categoryAdapter = new CategoryAdapter(NavigationActivity.this);
        recv_category.setLayoutManager(new LinearLayoutManager(NavigationActivity.this));
        recv_category.setItemAnimator(new DefaultItemAnimator());
        recv_category.setAdapter(categoryAdapter);

    }

    private void getCategoryList() {
        lv_nodata_category.setVisibility(View.GONE);
        recv_category.setVisibility(View.VISIBLE);

        callCategoryApi().enqueue(new Callback<CategoriesModel>() {
            @Override
            public void onResponse(Call<CategoriesModel> call, Response<CategoriesModel> response) {

                Log.e("responseeee_cate", "=" + response.body());
                Log.e("responseeee_cate", "=" + response);
                if(response.code()==200 || response.isSuccessful())
                {

                    CategoriesModel categoryModel = response.body();
                    Log.e("responseeee_cate", "=" + response.body());
                    Log.e("responseeee_cate", "=" + response);
                    // Log.e("Success_35", "=" + response.body().getName());
                    CategoriesModel model = response.body();

                    List<ChildData> results = fetchResults(response);
                    categoryAdapter.addAll(results);
                    shimmer_nav_category.stopShimmerAnimation();
                    shimmer_nav_category.setVisibility(View.GONE);
                }else {

                }
            }

            @Override
            public void onFailure(Call<CategoriesModel> call, Throwable t) {
                // String error=  t.printStackTrace();
                Toast.makeText(NavigationActivity.this, "" + getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();
                shimmer_nav_category.stopShimmerAnimation();
                shimmer_nav_category.setVisibility(View.GONE);
                Log.e("debug_175125", "pages: " + t);
            }
        });
    }

    private Call<CategoriesModel> callCategoryApi() {
        customeapi = ApiClientcusome.getClient().create(ApiInterface.class);

        Log.e("debug_2211","="+Login_preference.gettoken(getApplicationContext()));
        return customeapi.categories("Bearer "+Login_preference.gettoken(NavigationActivity.this));
    }
/*
    private Call<CountModel> getcount() {
        return api.getCount(Login_preference.getcustomer_id(NavigationActivity.this));
    }*/
    private List<ChildData> fetchResults(Response<CategoriesModel> response) {
        Log.e("newin_home_209", "" + response.body());
        CategoriesModel CategoriesModel = response.body();
        return CategoriesModel.getChildrenData();
    }

   /* private List<Category> fetchResults(Response<CategoryModel> response) {
        CategoryModel categoryModel = response.body();

        Log.e("statussssss", "" + categoryModel.getStatus());
        if (categoryModel.getStatus().equalsIgnoreCase("Success")) {
            Log.e("Totallllll", "" + categoryModel.getCategory());
        }
        return categoryModel.getCategory();
    }*/

    private void SetTypeface() {
       Menu menu = navigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem mi = menu.getItem(i);

            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            applyFontToMenuItem(mi);
        }
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "montserratbold.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            selectFragment(item);
            return false;
        }
    };

    private void Bootom_Navigation_view() {

        bottom_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//        BottomNavigationViewHelper.disableShiftMode(bottom_navigation);

        Menu menu = bottom_navigation.getMenu();
        selectFragment(menu.getItem(0));

        cartitem_count = Login_preference.getCart_item_count(NavigationActivity.this);

        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottom_navigation.getChildAt(0);
        //cart item count
        BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(3);
        notificationBadge = LayoutInflater.from(this).inflate(R.layout.badge_row, menuView, false);
        tv_bottomcount = (TextView) notificationBadge.findViewById(R.id.badge);
        if (cartitem_count.equalsIgnoreCase("null") || cartitem_count.equals("") || cartitem_count.equals("0")) {
            tv_bottomcount.setVisibility(View.GONE);
        } else {
            tv_bottomcount.setVisibility(View.VISIBLE);
            tv_bottomcount.setText(cartitem_count);
        }
        itemView.addView(notificationBadge);

        //wishlist count show
        wishlist_count = Login_preference.get_wishlist_count(NavigationActivity.this);
        BottomNavigationItemView itemView_wishlist = (BottomNavigationItemView) menuView.getChildAt(2);
        wishlist_badge = LayoutInflater.from(this).inflate(R.layout.wishlist_count, menuView, false);
        tv_wishlist_count = (TextView) wishlist_badge.findViewById(R.id.badge_wishlist);
        Log.e("debug_309","fg"+Login_preference.get_wishlist_count(NavigationActivity.this));
        if (wishlist_count.equalsIgnoreCase("null") || wishlist_count.equals("") || wishlist_count.equals("0")) {
            tv_wishlist_count.setVisibility(View.GONE);
        } else {
            tv_wishlist_count.setVisibility(View.VISIBLE);
            tv_wishlist_count.setText(wishlist_count);
        }

        itemView_wishlist.addView(wishlist_badge);

    }

    private void pushFragment(Fragment fragment, String add_to_backstack) {
        if (fragment == null)
            return;
        FragmentManager fragmentManager = getSupportFragmentManager();
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
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    private void selectFragment(MenuItem item) {
        item.setChecked(true);
      /*  item.setChecked(true);
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();*/

        switch (item.getItemId()) {
            case R.id.bottom_homee:
               // pushFragment(new HomeFragment(), "Home");
                Home_dk fragment = new Home_dk();
                if (fragment == null)
                    return;
                FragmentManager fragmentManager = getSupportFragmentManager();
                if (fragmentManager != null) {
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    if (ft != null) {
                        bottom_navigation.setVisibility(View.VISIBLE);
                        ft.replace(R.id.framlayout, fragment);
                        ft.setCustomAnimations(R.anim.fade_in,
                                0, 0, R.anim.fade_out).setCustomAnimations(R.anim.fade_in,
                                0, 0, R.anim.fade_out);
                        ft.commit();
                    }
                }
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                break;
            case R.id.bottom_offers:
                Filterlist_Adapter.filter_child_value_list.clear();
                pushFragment(new SearchFragment(), "search");
                break;
            case R.id.bottom_favourite:
                if (Login_preference.getLogin_flag(this).equalsIgnoreCase("1")) {
                    pushFragment(new WishlistNewFragment(), "Favourites");
                } else {
                    LoginFragment myFragment = new LoginFragment();
                    String screen = "Favourites";
                    Bundle b = new Bundle();
                    b.putString("screen", "" + screen);
                    myFragment.setArguments(b);
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.fade_in,
                            0, 0, R.anim.fade_out)
                            .addToBackStack("Favourites")
                            .add(R.id.framlayout, myFragment)
                            .commit();

                }
                break;
            case R.id.bottom_cart:
               if (Login_preference.getLogin_flag(this).equalsIgnoreCase("1")) {
                    pushFragment(new NewCartFragment(), "cart");
                } else {
                    LoginFragment myFragment = new LoginFragment();
                    String screen = "Cart";
                    Bundle b = new Bundle();
                    b.putString("screen", "" + screen);
                    myFragment.setArguments(b);
                    getSupportFragmentManager().beginTransaction()
                            .addToBackStack(null)
                            .setCustomAnimations(R.anim.fade_in,
                            0, 0, R.anim.fade_out)
                            .addToBackStack("Cart")
                            .add(R.id.framlayout, myFragment).commit();

                }

                break;
            case R.id.bottom_account:

                pushFragment(new AccountFragment(), "my_account");

                break;
        }
    }

    public static void Check_String_NULL_Value(TextView textview, String text) {
        if (text == null|| text.equalsIgnoreCase("null") == true ) {
            //textview.setHint("");
            textview.setText("");
        } else {
            textview.setText(Html.fromHtml(NavigationActivity.Convert_String_First_Letter(text)));
        }
    }

    public static String Convert_String_First_Letter(String convert_string) {
        String upperString;

        if (convert_string.length() > 0) {
            upperString = convert_string.substring(0, 1).toUpperCase() + convert_string.substring(1);
        } else {
            upperString = " ";
        }
        return upperString;
    }

    private void AllocateMemory() {
        shimmer_nav_category = findViewById(R.id.shimmer_nav_category);
        bottom_navigation = findViewById(R.id.bottom_navigation);
       // toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        relative_layout = findViewById(R.id.relative_layout);
        iv_navtoolbar_logo = findViewById(R.id.iv_navtoolbar_logo);
        recv_category = findViewById(R.id.recv_category);
        nav_iv_close = findViewById(R.id.nav_iv_close);
        lv_nodata_category = findViewById(R.id.lv_nodata_category);
    }

    private void setFontStyle() {
        //holder.txt_pack_title.setTypeface(Home.typeface);
        //holder.txt_pack_title.setText(package_model.getPackage_title());
        am = NavigationActivity.this.getAssets();
        montserrat_medium = Typeface.createFromAsset(am,
                String.format(Locale.getDefault(), "montserrat_medium.ttf"));
        montserrat_regular = Typeface.createFromAsset(am,
                String.format(Locale.getDefault(), "montserrat_regular.ttf"));
        montserrat_semibold = Typeface.createFromAsset(am,
                String.format(Locale.getDefault(), "montserrat_semibold.ttf"));
        montserratbold = Typeface.createFromAsset(am,
                String.format(Locale.getDefault(), "montserratbold.ttf"));
        montserrat_light = Typeface.createFromAsset(am,
                String.format(Locale.getDefault(), "montserrat_light.ttf"));
    }

    private void setNavigationIcon_headerview() {

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, R.string.nav_app_bar_open_drawer_description, R.string.close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                float slideX = drawerView.getWidth() * slideOffset;
                relative_layout.setTranslationX(slideX);
            }
        };

        drawer.addDrawerListener(actionBarDrawerToggle);

        nav_iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_home, menu);
        login = menu.findItem(R.id.login);
        if(Login_preference.getLogin_flag(NavigationActivity.this).equalsIgnoreCase("1"))
        {
            login.setVisible(false);
        }else {
            login.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.search:
                Filterlist_Adapter.filter_child_value_list.clear();
                FilterListFragment.selected_child="";
                FilterListFragment.filter_old_childlist.clear();
                Filterlist_Adapter.filter_grouppp_namelist.clear();
                pushFragment(new SearchFragment(), "search");
                return true;
            case R.id.login:
                pushFragment(new LoginFragment(),"login");
                return true;

            default:
        }
        return super.onOptionsItemSelected(item);


    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        int count = fragmentManager.getBackStackEntryCount();
        Log.e("count", "" + count);
        if (count == 0) {
            if (doubleBackToExitPressedOnce) {
                Log.e("flage", "" + doubleBackToExitPressedOnce);
                super.onBackPressed();
                super.finish();
                return;
            }
            Log.e("flage1", "" + doubleBackToExitPressedOnce);
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "" + getResources().getString(R.string.back), Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            Log.e("count_494", "" + count);
//            String title = fragmentManager.getBackStackEntryAt(count-2 ).getName();
            //   Log.e("onBackPressetitle", "" + title);
            super.onBackPressed();

        }
    }

   /* @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            finish();
            Toast.makeText(this, "" + getResources().getString(R.string.back), Toast.LENGTH_SHORT).show();
        }
        else {
            getFragmentManager().popBackStack();
        }
        //super.onBackPressed();
    }*/
}
