package com.dolphin.zanders.Adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.dolphin.zanders.Fragment.NewCheckoutFragment;
import com.dolphin.zanders.Model.PaymentMethodModel.PaymentMethod;
import com.dolphin.zanders.R;
import java.util.List;

import static com.dolphin.zanders.Fragment.CheckoutFragment.payment_method_order;



    public class NewPaymentMethodAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        Context context;
        private List<PaymentMethod> datumList;
        int lastSelectedPosition =0;
        public NewPaymentMethodAdapter(FragmentActivity context,List<PaymentMethod> datumList) {
            this.context = context;
            this.datumList =datumList;
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
            View v1 = inflater.inflate(R.layout.payment_info_row, parent, false);
            viewHolder = new com.dolphin.zanders.Adapter.NewPaymentMethodAdapter.MyViewHolder(v1);
            return viewHolder;
        }

     
          @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            final PaymentMethod datum = datumList.get(position);

            final com.dolphin.zanders.Adapter.NewPaymentMethodAdapter.MyViewHolder myViewHolder = (com.dolphin.zanders.Adapter.NewPaymentMethodAdapter.MyViewHolder) holder;
            myViewHolder.rad_text.setText(Html.fromHtml(datum.getValue()));
              Log.e("debug_166", "k" + datum.getLabel());


            myViewHolder.rad_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    Log.e("debug_166", "" + datum.getLabel());
                    lastSelectedPosition = position;
                    notifyDataSetChanged();
                    NewCheckoutFragment.paymentMethod_pass=datumList.get(position).getLabel();
                    NewCheckoutFragment.payment_method_order=datumList.get(position).getLabel();

                }
            });

            if (lastSelectedPosition == position) {
                Log.e("selectedpo_76", "" + lastSelectedPosition);
                myViewHolder.rad_text.setChecked(true);
                NewCheckoutFragment.paymentMethod_pass=datumList.get(position).getLabel();
                NewCheckoutFragment.payment_method_order=datumList.get(position).getLabel();


            } else {
                myViewHolder.rad_text.setChecked(false);
            }
            if (position == getItemCount() - 1) {
                myViewHolder.viewpayment.setVisibility(View.GONE);
            } else {
                myViewHolder.viewpayment.setVisibility(View.VISIBLE);
            }
        }
    
  
        public void setupUI(View view) {

            // Set up touch listener for non-text box views to hide keyboard.
            if (!(view instanceof EditText)) {
                view.setOnTouchListener(new View.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        hideKeyboard((Activity) context);
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
        public int getItemCount() {
            return datumList == null ? 0 : datumList.size();
        }


        public void add(PaymentMethod r) {
            datumList.add(r);
            Log.e("debug_117adapter", "" + r);
            Log.e("debug_118adapter", "" + datumList.size());
            Log.e("debug_119adapter", "" + (datumList.size() - 1));
            notifyItemInserted(datumList.size() - 1);
        }

        public void addAll(List<PaymentMethod> moveResults) {
            Log.e("debug_124adapter", "" + moveResults.size());

            for (PaymentMethod result : moveResults) {
                Log.e("debug_127adapter", "" + result);
                add(result);
            }
        }


        public PaymentMethod getItem(int position) {
            Log.e("pos_galadaadapter", "" + position);
            return datumList.get(position);
        }

        protected class MyViewHolder extends RecyclerView.ViewHolder {

            LinearLayout lv_right_arrow, lv_radio_text_click, lv_radio_click;
            RadioButton rad_text;
            View viewpayment;


            public MyViewHolder(@NonNull View view) {
                super(view);
                rad_text = itemView.findViewById(R.id.rad_text);
                viewpayment = itemView.findViewById(R.id.viewpayment);
                lv_right_arrow = itemView.findViewById(R.id.lv_right_arrow);
                lv_radio_text_click = itemView.findViewById(R.id.lv_radio_text_click);
                lv_radio_click = itemView.findViewById(R.id.lv_radio_click);

            }
        }

    }

