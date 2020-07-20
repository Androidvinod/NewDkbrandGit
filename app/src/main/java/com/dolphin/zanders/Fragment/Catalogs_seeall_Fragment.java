package com.dolphin.zanders.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dolphin.zanders.Activity.NavigationActivity;
import com.dolphin.zanders.Adapter.CategoryAdapter;
import com.dolphin.zanders.Adapter.Category_seeall_Adapter;
import com.dolphin.zanders.Adapter.Filterlist_Adapter;
import com.dolphin.zanders.Adapter.SubCategoryAdapter;
import com.dolphin.zanders.Model.CategoriesModel;
import com.dolphin.zanders.Model.CategoryModel.Category;
import com.dolphin.zanders.Model.CategoryModel.CategoryModel;
import com.dolphin.zanders.Model.ChildData;
import com.dolphin.zanders.Model.SubCategoryModel.SubCategoryModel;
import com.dolphin.zanders.Model.SubCategoryModel.Subcategory;
import com.dolphin.zanders.Preference.Login_preference;
import com.dolphin.zanders.R;
import com.dolphin.zanders.Retrofit.ApiClient;
import com.dolphin.zanders.Retrofit.ApiClientcusome;
import com.dolphin.zanders.Retrofit.ApiInterface;
import com.dolphin.zanders.Util.CheckNetwork;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dolphin.zanders.Activity.NavigationActivity.drawer;

/**
 * A simple {@link Fragment} subclass.
 */
public class Catalogs_seeall_Fragment extends Fragment {
    Toolbar toolbar_subcategory;
    View v;
    RecyclerView recv_subcategory;

    LinearLayout lv_nodata_subcategory,lv_catalog_parent;

    TextView tv_messgenoti,tv_subcat_toolbar;
    Category_seeall_Adapter category_seeall_adapter;
    MenuItem login;
    NavigationActivity parent;

    private ShimmerFrameLayout mShimmerViewContainer;
    ApiInterface api;
    Bundle b;

    public Catalogs_seeall_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_sub_category, container, false);
        setHasOptionsMenu(true);
        AllocateMemory(v);
        setupUI(lv_catalog_parent);
        Filterlist_Adapter.filter_child_value_list.clear();
        Filterlist_Adapter.filter_grouppp_namelist.clear();
        FilterListFragment.selected_child="";

        AttachRecyclerView();
        Filterlist_Adapter.filter_child_value_list.clear();
        parent=(NavigationActivity) getActivity();
        ((NavigationActivity) parent).setSupportActionBar(toolbar_subcategory);
        ((NavigationActivity) parent).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
        ((NavigationActivity) parent).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_36dp);
        toolbar_subcategory.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        hideKeyboard(parent);
            tv_subcat_toolbar.setText(parent.getResources().getString(R.string.Catalogs));
         if(CheckNetwork.isNetworkAvailable(parent))
        {
            getCategoryList();
        }else {
           //noInternetDialog();
             Toast.makeText(parent, parent.getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();

         }
        return v;
    }
    private void AttachRecyclerView() {

        category_seeall_adapter = new Category_seeall_Adapter(parent);
        recv_subcategory.setLayoutManager(new LinearLayoutManager(parent));
        recv_subcategory.setAdapter(category_seeall_adapter);

    }

    private void getCategoryList() {
        lv_nodata_subcategory.setVisibility(View.GONE);
        recv_subcategory.setVisibility(View.VISIBLE);
        callCategoryApi().enqueue(new Callback<CategoriesModel>() {
            @Override
            public void onResponse(Call<CategoriesModel> call, Response<CategoriesModel> response) {
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                CategoriesModel categoryModel = response.body();
                Log.e("responseeee_cate", "=" + response.body());
//                Log.e("Success_35", "=" + response.body().getName());
                CategoriesModel model = response.body();

                if(response.code()==200)
                {
                    List<ChildData> results = fetchResults(response);
                    category_seeall_adapter.addAll(results);
                }else {

                }



            }

            @Override
            public void onFailure(Call<CategoriesModel> call, Throwable t) {
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                // String error=  t.printStackTrace();
                Toast.makeText(parent, "" + parent.getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();

                Log.e("debug_175125", "pages: " + t);
            }
        });
    }

    private Call<CategoriesModel> callCategoryApi() {
        api = ApiClientcusome.getClient().create(ApiInterface.class);
        return api.categories("Bearer "+Login_preference.gettoken(getActivity()));
    }

    private void AllocateMemory(View v) {
        recv_subcategory=v.findViewById(R.id.recv_subcategory);
        toolbar_subcategory=v.findViewById(R.id.toolbar_subcategory);
        lv_nodata_subcategory=v.findViewById(R.id.lv_nodata_subcategory);
        tv_messgenoti=v.findViewById(R.id.tv_messgenoti);
        tv_subcat_toolbar=v.findViewById(R.id.tv_subcat_toolbar);
        mShimmerViewContainer =v.findViewById(R.id.shimmer_subcategory);
        lv_catalog_parent =v.findViewById(R.id.lv_catalog_parent);




    }
    private List<ChildData> fetchResults(Response<CategoriesModel> response) {
        Log.e("newin_home_209", "" + response.body());
        CategoriesModel CategoriesModel = response.body();
        return CategoriesModel.getChildrenData();
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_home, menu);
        login = menu.findItem(R.id.login);
        if(Login_preference.getLogin_flag(parent).equalsIgnoreCase("1"))
        {
            login.setVisible(false);
        }else {
            login.setVisible(true);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        mShimmerViewContainer.stopShimmerAnimation();
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
