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

import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.text.Html;
import android.text.SpannableString;
import android.util.Log;
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
import com.dolphin.zanders.Adapter.CartlistAdapter;
import com.dolphin.zanders.Adapter.CartlistAdapter_new;
import com.dolphin.zanders.Adapter.Filterlist_Adapter;
import com.dolphin.zanders.Model.CartlistModel.Cartlist;
import com.dolphin.zanders.Model.CartlistModel.CartlistInnerData;
import com.dolphin.zanders.Preference.Login_preference;
import com.dolphin.zanders.R;
import com.dolphin.zanders.Retrofit.ApiClient;
import com.dolphin.zanders.Retrofit.ApiInterface;
import com.dolphin.zanders.Util.CheckNetwork;
import com.dolphin.zanders.Util.WrapContentLinearLayoutManager;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

import static com.dolphin.zanders.Activity.NavigationActivity.bottom_navigation;
import static com.dolphin.zanders.Activity.NavigationActivity.drawer;
import static com.dolphin.zanders.Activity.NavigationActivity.tv_bottomcount;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment implements View.OnClickListener {

    View v;
    Toolbar toolbar_cart;
    public static CoordinatorLayout cordinator_cart;
    public static RecyclerView recv_cart;
    public static LinearLayout lv_cart_checkout;
    public static Context context;
    public static LinearLayout lv_cartlist_progress,lv_nodata_cart, lv_cart_Main, lv_subtotal_layout, lv_cart_parent;

    public static TextView tv_messgenoti, tv_noting, tv_addlinal;
    public static TextView tv_cart_subtotal;
    //  public static Context context = null;
    public static ApiInterface cartlist;
    public static CartlistAdapter_new cartlistAdapter;//CartlistAdapter
    private Paint p = new Paint();
    public static ShimmerFrameLayout shimmer_cartlist;
    MenuItem login;
    public static TextView tv_update_cart;

    NavigationActivity parent;

    public CartFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_cart_fregment, container, false);
        bottom_navigation.getMenu().getItem(3).setChecked(true);
        cartlist = ApiClient.getClient().create(ApiInterface.class);

        Filterlist_Adapter.filter_child_value_list.clear();
        FilterListFragment.selected_child="";
        FilterListFragment.filter_old_childlist.clear();
        Filterlist_Adapter.filter_grouppp_namelist.clear();


        parent = (NavigationActivity) getActivity();
        context = parent;
        hideKeyboard(parent);
        AllocateMemory();
        AttachRecyclerView();
        initSwipe();
        setupUI(lv_cart_parent);
        ((NavigationActivity) parent).setSupportActionBar(toolbar_cart);
        ((NavigationActivity) parent).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
        ((NavigationActivity) parent).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        toolbar_cart.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        if (CheckNetwork.isNetworkAvailable(parent)) {
            CallCartlistApi();
        } else {
            Toast.makeText(parent, parent.getResources().getString(R.string.internet), Toast.LENGTH_SHORT).show();
        }
        lv_cart_checkout.setOnClickListener(this);


        return v;
    }

    private void AttachRecyclerView() {
        cartlistAdapter = new CartlistAdapter_new(parent);//CartlistAdapter
        WrapContentLinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(parent, WrapContentLinearLayoutManager.VERTICAL, false);
        recv_cart.setLayoutManager(layoutManager);
        recv_cart.setAdapter(cartlistAdapter);
    }

    public static void CallCartlistApi() {
        tv_messgenoti.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        lv_nodata_cart.setVisibility(View.GONE);
        recv_cart.setVisibility(View.VISIBLE);
        lv_cart_Main.setVisibility(View.VISIBLE);

        callcartlistapi().enqueue(new Callback<Cartlist>() {
            @Override
            public void onResponse(Call<Cartlist> call, Response<Cartlist> response) {
                Cartlist cartlist = response.body();
                Log.e("statuss", "" + cartlist.getStatus());
                if (cartlist.getStatus().equalsIgnoreCase("Success")) {

                    NavigationActivity.Check_String_NULL_Value(tv_cart_subtotal, cartlist.getSubtotal());

                    tv_addlinal.setText(cartlist.getAdditionalAmountNeededForFreeShipping());
                    Login_preference.setquote_id(context, cartlist.getQuoteId());

                    String item_count = null;
                    if (cartlist.getItemsCount().equalsIgnoreCase("0") || cartlist.getItemsCount().equals("0.00")) {
                        Login_preference.setCart_item_count(context, "");
                        //   item_count = String.valueOf(removeproductmodel.getItemsCount());
                    } else {
                        Login_preference.setCart_item_count(context, String.valueOf(cartlist.getItemsCount()));
                        item_count = String.valueOf(cartlist.getItemsCount());
                    }
                    if (item_count == null || item_count.equalsIgnoreCase("null") || item_count.equals("")) {
                        Log.e("count_40", "" + String.valueOf(cartlist.getItemsCount()));
                        tv_bottomcount.setVisibility(View.GONE);
                    } else {
                        tv_bottomcount.setVisibility(View.VISIBLE);
                        Log.e("count_80", "" + String.valueOf(cartlist.getItemsCount()));
                        NavigationActivity.Check_String_NULL_Value(tv_bottomcount, String.valueOf(cartlist.getItemsCount()));
                    }

                    if (fetchResults(response) == null || fetchResults(response).size() == 0) {
                        Log.e("debug_175", "pages: " + fetchResults(response));
                        lv_nodata_cart.setVisibility(View.VISIBLE);
                        recv_cart.setVisibility(View.GONE);
                        lv_cart_Main.setVisibility(View.GONE);
                        tv_messgenoti.setVisibility(View.GONE);
                        tv_noting.setText(Html.fromHtml(context.getResources().getString(R.string.cartempty)));
                        shimmer_cartlist.stopShimmerAnimation();
                        shimmer_cartlist.setVisibility(View.GONE);
                        lv_subtotal_layout.setVisibility(View.GONE);
                        tv_bottomcount.setVisibility(View.GONE);

                    } else {
                        Log.e("debug_995", "pages: " + fetchResults(response));
                        recv_cart.setVisibility(View.VISIBLE);
                        lv_cart_Main.setVisibility(View.VISIBLE);
                        lv_subtotal_layout.setVisibility(View.VISIBLE);
                        shimmer_cartlist.stopShimmerAnimation();
                        shimmer_cartlist.setVisibility(View.GONE);

                        List<CartlistInnerData> results = fetchResults(response);
                        List<CartlistInnerData> category_arr = response.body().getProduct();
                        Log.e("debug_9951533", "" + fetchResults(response));
                        Log.e("debug_995153377", "" + results);
                        Log.e("category_arr_191", "" + category_arr);
                        cartlistAdapter.addAll(results);

                        if (category_arr.isEmpty()) {
                            lv_cart_Main.setVisibility(View.GONE);
                            recv_cart.setVisibility(View.GONE);
                            lv_nodata_cart.setVisibility(View.VISIBLE);
                            tv_messgenoti.setVisibility(View.GONE);
                            tv_noting.setText(Html.fromHtml(context.getResources().getString(R.string.cartempty)));
                            shimmer_cartlist.stopShimmerAnimation();
                            shimmer_cartlist.setVisibility(View.GONE);
                            lv_subtotal_layout.setVisibility(View.GONE);
                            tv_bottomcount.setVisibility(View.GONE);
                        } else {
                            tv_bottomcount.setVisibility(View.VISIBLE);
                            lv_cart_Main.setVisibility(View.VISIBLE);
                            recv_cart.setVisibility(View.VISIBLE);
                            lv_nodata_cart.setVisibility(View.GONE);
                        }

                    }
                } else {
                    tv_bottomcount.setVisibility(View.GONE);
                    lv_nodata_cart.setVisibility(View.VISIBLE);
                    recv_cart.setVisibility(View.GONE);
                    lv_cart_Main.setVisibility(View.GONE);
                    tv_messgenoti.setVisibility(View.GONE);
                    tv_noting.setText(Html.fromHtml(context.getResources().getString(R.string.cartempty)));
                    shimmer_cartlist.stopShimmerAnimation();
                    shimmer_cartlist.setVisibility(View.GONE);
                    lv_subtotal_layout.setVisibility(View.GONE);
                    // Toast.makeText(context, ""+cartlist.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Cartlist> call, Throwable t) {
                Toast.makeText(context, "" + context.getResources().getString(R.string.wentwrong), Toast.LENGTH_SHORT).show();
                Log.e("debug_175125", "pages: " + t);
            }
        });
    }

    public static Call<Cartlist> callcartlistapi() {
        Log.e("email_237", "" + Login_preference.getemail(context));
        return cartlist.getcartlist(Login_preference.getemail(context));
    }

    public static List<CartlistInnerData> fetchResults(Response<Cartlist> response) {
        Cartlist cartlist = response.body();
        ///tv_cart_subtotal.setText(cartlist.getGrandTotal());
        return cartlist.getProduct();
    }

    private void AllocateMemory() {
        lv_cartlist_progress = v.findViewById(R.id.lv_cartlist_progress);
        cordinator_cart = v.findViewById(R.id.cordinator_cart);
        tv_update_cart = v.findViewById(R.id.tv_update_cart);
        tv_cart_subtotal = v.findViewById(R.id.tv_cart_subtotal);
        tv_noting = v.findViewById(R.id.tv_noting);
        toolbar_cart = v.findViewById(R.id.toolbar_cart);
        recv_cart = v.findViewById(R.id.recv_cart);
        lv_cart_checkout = v.findViewById(R.id.lv_cart_checkout);
        lv_cart_parent = v.findViewById(R.id.lv_cart_parent);

        tv_messgenoti = v.findViewById(R.id.tv_messgenoti);
        lv_nodata_cart = v.findViewById(R.id.lv_nodata_cart);
        lv_cart_Main = v.findViewById(R.id.lv_cart_Main);
        lv_subtotal_layout = v.findViewById(R.id.lv_subtotal_layout);
        shimmer_cartlist = v.findViewById(R.id.shimmer_cartlist);
        tv_addlinal = v.findViewById(R.id.tv_addlinal);
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
                if (direction == ItemTouchHelper.RIGHT) {
                    Log.e("debug_224", "wishlist");
                    cartlistAdapter.addToWishlistItem(position);
                    cartlistAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                } else {
                    Log.e("debug_227", "reove");
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
                        icon = BitmapFactory.decodeResource(parent.getResources(), R.drawable.ic_star_border_white_36dp);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    } else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(parent.getResources(), R.drawable.ic_close_white_36dp);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recv_cart);
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
                    cartlistAdapter.removeItem(position);
                } else {
                    //     removeView();
                    cartlistAdapter.notifyDataSetChanged();
                }
            }
        });

        // Set the Negative button with No name
        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface
                .OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cartlistAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
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


    @Override
    public void onClick(View v) {
        if (v == lv_cart_checkout) {
            Log.e("check_flag",""+CartlistAdapter_new.flag);
            if (CartlistAdapter_new.flag == false) {
                Toast.makeText(context, context.getString(R.string.quantity_messge), Toast.LENGTH_SHORT).show();
            } else {
                pushFragment(new CheckoutFragment(), "Checkout");
            }

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_home, menu);
        login = menu.findItem(R.id.login);
        if (Login_preference.getLogin_flag(parent).equalsIgnoreCase("1")) {
            login.setVisible(false);
        } else {
            login.setVisible(true);
        }
        super.onCreateOptionsMenu(menu, inflater);
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

    @Override
    public void onResume() {
        super.onResume();
        shimmer_cartlist.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        shimmer_cartlist.stopShimmerAnimation();
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
