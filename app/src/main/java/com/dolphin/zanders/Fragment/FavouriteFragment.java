package com.dolphin.zanders.Fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dolphin.zanders.Activity.NavigationActivity;

import com.dolphin.zanders.Adapter.Favourites_Adapter;
import com.dolphin.zanders.Adapter.Filterlist_Adapter;
import com.dolphin.zanders.Model.FavouriteModel.GetFavouriteslist;
import com.dolphin.zanders.Model.FavouriteModel.Product;
import com.dolphin.zanders.Model.Manufacturerslist_model.GetManufacturelistModel;
import com.dolphin.zanders.Model.Manufacturerslist_model.Manufactur;
import com.dolphin.zanders.Model.ProductlistModel.GetCategoryProductlistInnerData;
import com.dolphin.zanders.Preference.Login_preference;
import com.dolphin.zanders.R;
import com.dolphin.zanders.Retrofit.ApiClient;
import com.dolphin.zanders.Retrofit.ApiInterface;
import com.dolphin.zanders.Util.CheckNetwork;
import com.facebook.shimmer.ShimmerFrameLayout;


import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.dolphin.zanders.Activity.NavigationActivity.bottom_navigation;
import static com.dolphin.zanders.Activity.NavigationActivity.drawer;
import static com.dolphin.zanders.Activity.NavigationActivity.tv_bottomcount;
import static com.dolphin.zanders.Activity.NavigationActivity.tv_wishlist_count;

public class FavouriteFragment extends Fragment {
    NavigationActivity parent;

    public static LinearLayout lv_favourite_main,lv_no_data_found_wishlist, lv_main_favourite,lv_progress_favoutite_bottom;
    public static RecyclerView recv_favourites;
    public static List<Product> favouriteproductlist = new ArrayList<Product>();

    private static Favourites_Adapter favourites_adapter;
    public static NestedScrollView nested_scroll_favourits;
    int islastcode;
    int visibleItemCount, totalItemCount, firstVisibleItemPosition;

    ApiInterface apiInterface;
    /*GridLayoutManager linearLayoutManager;*/
    Toolbar toolbar_favourites;

    public static ShimmerFrameLayout shimmer_view_favourites;
    private int edit_position;
    private View view;
    private boolean add = false;
    private Paint p = new Paint();
    TextView tv_noting;
    MenuItem login;
    View v;
    TextView tv_toolbar_fav;
    //pagination
    LinearLayoutManager linearLayoutManager;
    int pastvisibleitem, visibleitemcount, totalitemcount;
    int page_no = 1, page;
    int isLastPage;

    public FavouriteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_favourite_fregment, container, false);
        bottom_navigation.getMenu().getItem(2).setChecked(true);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        parent=(NavigationActivity) getActivity();
        setHasOptionsMenu(true);
        AllocateMemory();
        hideKeyboard(parent);
        setupUI(lv_favourite_main);
        Filterlist_Adapter.filter_child_value_list.clear();
        favouriteproductlist.clear();
        Filterlist_Adapter.filter_child_value_list.clear();
        FilterListFragment.selected_child="";
        FilterListFragment.filter_old_childlist.clear();
        Filterlist_Adapter.filter_grouppp_namelist.clear();

        ((NavigationActivity) parent).setSupportActionBar(toolbar_favourites);
        ((NavigationActivity) parent).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
        ((NavigationActivity) parent).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        toolbar_favourites.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        if (CheckNetwork.isNetworkAvailable(parent)) {
            CallGetWishlistApi(page_no);
        } else {
            Toast.makeText(parent, parent.getResources().getString(R.string.nointernet), Toast.LENGTH_SHORT).show();
        }
        AttachRecyclerview();
        tv_noting.setText("Wishlist is empty");

        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                getActivity().onBackPressed();

                return false;
            }
        });

        return v;
    }
    private void AttachRecyclerview() {
        //recv_favourites.getItemAnimator().setChangeDuration(700);
        favourites_adapter = new Favourites_Adapter(parent, favouriteproductlist);
        linearLayoutManager = new LinearLayoutManager(parent, LinearLayoutManager.VERTICAL, false);
        recv_favourites.setLayoutManager(linearLayoutManager);
        recv_favourites.setAdapter(favourites_adapter);

        initSwipe();
    }
    private void CallGetWishlistApi(int pageno) {
        //  lv_no_data_found_wishlist.setVisibility(View.GONE);
        if (pageno == 1) {
            Log.e("debug_page1", "" + pageno);
            lv_no_data_found_wishlist.setVisibility(View.GONE);
            favouriteproductlist.clear();
            lv_progress_favoutite_bottom.setVisibility(View.GONE);
            recv_favourites.setVisibility(View.VISIBLE);
            lv_main_favourite.setVisibility(View.VISIBLE);
        } else {
            lv_no_data_found_wishlist.setVisibility(View.GONE);
            lv_progress_favoutite_bottom.setVisibility(View.VISIBLE);
            recv_favourites.setVisibility(View.VISIBLE);
            lv_main_favourite.setVisibility(View.VISIBLE);
            lv_main_favourite.setVisibility(View.VISIBLE);
        }
        page = pageno;

        callgetwisglistapi(page).enqueue(new Callback<GetFavouriteslist>() {
            @Override
            public void onResponse(Call<GetFavouriteslist> call, Response<GetFavouriteslist> response) {
                Log.e("response_favourite", "" + response.body());
                GetFavouriteslist getFavouriteslist = response.body();

                shimmer_view_favourites.stopShimmerAnimation();
                shimmer_view_favourites.setVisibility(View.GONE);
                lv_no_data_found_wishlist.setVisibility(View.GONE);
                lv_main_favourite.setVisibility(View.VISIBLE);
                lv_progress_favoutite_bottom.setVisibility(View.GONE);
                if (getFavouriteslist.getStatus().equalsIgnoreCase("Success")) {
                    isLastPage = getFavouriteslist.getIsLast();
                    Log.e("isLastPage_168fav", "" + isLastPage);

                  //  Login_preference.set_wishlist_count(parent, String.valueOf(getFavouriteslist.getCount()));
                  //  String item_count = String.valueOf(getFavouriteslist.getCount());
                  //  Log.e("item_qtyfav", "" + item_count);
                    String item_count = null;
                    if(getFavouriteslist.getCount()==0 || getFavouriteslist.getCount().equals("0.00"))
                    {
                        Login_preference.set_wishlist_count(parent,"");
                        //   item_count = String.valueOf(removeproductmodel.getItemsQty());
                    }else {
                        Login_preference.set_wishlist_count(parent,String.valueOf(getFavouriteslist.getCount()));
                        item_count = String.valueOf(getFavouriteslist.getCount());
                    }
                    if (item_count == null || item_count.equalsIgnoreCase("null") || item_count.equals("")) {
                        Log.e("count_40fav", "" + String.valueOf(getFavouriteslist.getCount()));
                        tv_wishlist_count.setVisibility(View.GONE);
                    } else {
                        tv_wishlist_count.setVisibility(View.VISIBLE);
                        Log.e("count_80fav", "" + String.valueOf(getFavouriteslist.getCount()));
                        NavigationActivity.Check_String_NULL_Value(tv_wishlist_count, String.valueOf(getFavouriteslist.getCount()));
                    }
                    if (fetchResults(response) == null || fetchResults(response).size() == 0) {
                        Log.e("debug_166fav", "" + fetchResults(response));
                        shimmer_view_favourites.stopShimmerAnimation();
                        shimmer_view_favourites.setVisibility(View.GONE);
                        lv_no_data_found_wishlist.setVisibility(View.VISIBLE);
                        lv_main_favourite.setVisibility(View.GONE);
                        lv_progress_favoutite_bottom.setVisibility(View.GONE);
                    } else {
                        Log.e("debug_174fav", "" + fetchResults(response));
                        shimmer_view_favourites.stopShimmerAnimation();
                        shimmer_view_favourites.setVisibility(View.GONE);
                        lv_no_data_found_wishlist.setVisibility(View.GONE);
                        lv_main_favourite.setVisibility(View.VISIBLE);
                        lv_progress_favoutite_bottom.setVisibility(View.GONE);

                        List<Product> results = fetchResults(response);
                        if (results.isEmpty()) {
                            shimmer_view_favourites.stopShimmerAnimation();
                            shimmer_view_favourites.setVisibility(View.GONE);
                            lv_no_data_found_wishlist.setVisibility(View.VISIBLE);
                            lv_main_favourite.setVisibility(View.GONE);
                            lv_progress_favoutite_bottom.setVisibility(View.GONE);
                        } else {
                            shimmer_view_favourites.stopShimmerAnimation();
                            shimmer_view_favourites.setVisibility(View.GONE);
                            lv_no_data_found_wishlist.setVisibility(View.GONE);
                            lv_main_favourite.setVisibility(View.VISIBLE);
                            lv_progress_favoutite_bottom.setVisibility(View.GONE);

                            for (int i = 0; i < results.size(); i++) {
                                favouriteproductlist.add(new Product
                                                 (results.get(i).getType(),
                                                results.get(i).getProductId(),
                                                results.get(i).getProductName(),
                                                results.get(i).getProductSku(),
                                                results.get(i).getProductPrice(),
                                                results.get(i).getProductSpecialprice(),
                                                results.get(i).getProductImage(),
                                                results.get(i).getProductThumbnail()));
                            }

                          //  AttachRecyclerview();
                        }
                    }
                    favourites_adapter = new Favourites_Adapter(parent, favouriteproductlist);
                    linearLayoutManager = new LinearLayoutManager(parent, LinearLayoutManager.VERTICAL, false);
                    recv_favourites.setLayoutManager(linearLayoutManager);
                    recv_favourites.setAdapter(favourites_adapter);
                } else {
                    Log.e("debug_166", "" + fetchResults(response));
                    shimmer_view_favourites.stopShimmerAnimation();
                    shimmer_view_favourites.setVisibility(View.GONE);
                    lv_no_data_found_wishlist.setVisibility(View.GONE);
                    lv_main_favourite.setVisibility(View.GONE);
                    lv_progress_favoutite_bottom.setVisibility(View.GONE);
                    Toast.makeText(parent, ""+getFavouriteslist.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetFavouriteslist> call, Throwable t) {
                t.printStackTrace();

                shimmer_view_favourites.stopShimmerAnimation();
                shimmer_view_favourites.setVisibility(View.GONE);
                lv_no_data_found_wishlist.setVisibility(View.GONE);
                lv_main_favourite.setVisibility(View.GONE);
                lv_progress_favoutite_bottom.setVisibility(View.GONE);
                Toast.makeText(parent, "" + parent.getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();

            }
        });

        nested_scroll_favourits.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (v.getChildAt(v.getChildCount() - 1) != null) {
                    if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                            scrollY > oldScrollY) {
                        visibleitemcount = linearLayoutManager.getChildCount();
                        totalitemcount = linearLayoutManager.getItemCount();
                        pastvisibleitem = linearLayoutManager.findFirstVisibleItemPosition();

                        Log.e("debug_258", "current_page=" + page);
                        if ((visibleitemcount + pastvisibleitem) >= totalitemcount) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (isLastPage == 0) {

                                        page++;
                                        Log.e("debug_261page", "=" + page);
                                        Log.e("debug_270islast", "=" + isLastPage);
                                        lv_progress_favoutite_bottom.setVisibility(View.VISIBLE);
                                        if (CheckNetwork.isNetworkAvailable(parent)) {
                                            CallGetWishlistApi(page);
                                        } else {
                                            Toast.makeText(parent, parent.getResources().getString(R.string.nointernet), Toast.LENGTH_SHORT).show();
                                        }

                                    } else {
                                        Log.e("debug_2704", "" + isLastPage);
                                        lv_progress_favoutite_bottom.setVisibility(View.GONE);
                                    }
                                }
                            }, 1000);
                        }
                    }
                }
            }
        });
    }
    private Call<GetFavouriteslist> callgetwisglistapi(int page) {
        Log.e("customer_id", "" + Login_preference.getcustomer_id(getContext()));
        return apiInterface.getWishlist(Login_preference.getcustomer_id(parent), page);
    }

    private List<Product> fetchResults(Response<GetFavouriteslist> response) {
        GetFavouriteslist getFavouriteslist = response.body();
        return getFavouriteslist.getProduct();
    }
    private void AllocateMemory() {
        toolbar_favourites = v.findViewById(R.id.toolbar_favourites);
        lv_no_data_found_wishlist = v.findViewById(R.id.lv_no_data_found_wishlist);
        lv_main_favourite = v.findViewById(R.id.lv_main_favourite);
        lv_progress_favoutite_bottom = v.findViewById(R.id.lv_progress_favoutite_bottom);
        shimmer_view_favourites = v.findViewById(R.id.shimmer_view_favourites);
        nested_scroll_favourits = v.findViewById(R.id.nested_scroll_favourits);
        recv_favourites = v.findViewById(R.id.recv_favourites);
        tv_noting = v.findViewById(R.id.tv_noting);
        lv_favourite_main = v.findViewById(R.id.lv_favourite_main);
    }


    private void initSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Log.e("debug_255", "t" + direction);
                if (direction == ItemTouchHelper.RIGHT) {
                    Log.e("debug_261", "t" + direction);
                    favourites_adapter.addToCartItem(position);
                    favourites_adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                } else {
                    Log.e("debug_264", "t" + direction);
                    mdeletecartitem(viewHolder, direction);
                }
            }
            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView,
                                    RecyclerView.ViewHolder viewHolder,
                                    float dX, float dY, int actionState, boolean isCurrentlyActive) {
                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;
                    if (dX > 0) {
                        p.setColor(Color.parseColor("#06466c"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(parent.getResources(), R.drawable.cart_white);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width,
                                (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    } else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(parent.getResources(), R.drawable.ic_close_white_36dp);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width,
                                (float) itemView.getTop() + width,
                                (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recv_favourites);
    }

    private void mdeletecartitem(final RecyclerView.ViewHolder viewHolder, final int direction) {
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(parent);
        SpannableString spannableString = new SpannableString(parent.getResources().getString(R.string.removeitemm));
        builder.setTitle(spannableString);
        builder.setCancelable(false);

        builder.setPositiveButton(parent.getResources().getString(R.string.Remove), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int position = viewHolder.getAdapterPosition();
                if (direction == ItemTouchHelper.LEFT) {
                    Log.e("debug_322", "elee");
                    favourites_adapter.removeItem(position);
                } else {
                   // Log.e("debug_339", "else");
                   // removeView();
                    favourites_adapter.notifyDataSetChanged();
                }
            }
        });

        // Set the Negative button with No name
        builder.setNegativeButton(parent.getResources().getString(R.string.cancel), new DialogInterface
                .OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                favourites_adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                //CallGetWishlistApi();
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        textView.setTextColor(parent.getResources().getColor(R.color.black));
        Button nbutton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(parent.getResources().getColor(R.color.colorPrimary));
        Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(parent.getResources().getColor(R.color.colorPrimary));
    }

    private void removeView() {
        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
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

    @Override
    public void onResume() {
        super.onResume();
        shimmer_view_favourites.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        shimmer_view_favourites.stopShimmerAnimation();
        super.onPause();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.search:
                Filterlist_Adapter.filter_child_value_list.clear();
                FilterListFragment.selected_child="";
                FilterListFragment.filter_old_childlist.clear();
                Filterlist_Adapter.filter_grouppp_namelist.clear();

                pushFragment(new SearchFragment(), "search");
                return true;

            case android.R.id.home:
                parent.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

}
