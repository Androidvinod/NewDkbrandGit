package com.dolphin.zanders.Adapter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.dolphin.zanders.Model.PaymentMethodModel.PaymentMethod;
import com.dolphin.zanders.R;
import com.dolphin.zanders.Util.CardType;
import com.dolphin.zanders.Util.ValidationUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.kal.rackmonthpicker.RackMonthPicker;
import com.kal.rackmonthpicker.listener.DateMonthDialogListener;
import com.kal.rackmonthpicker.listener.OnCancelMonthDialogListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.dolphin.zanders.Fragment.CheckoutFragment.payment_method_order;



public class PaymentInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    static Dialog d ;
    Context context;

    public static String card_number,card_purchesorder_number,card_ship_date,card_exp_moth,card_exp_year,card_varification_no;

    public static EditText et_creditcard_shipping,et_card_month,et_creditcard_year;
    String dateshipping="",ordernumber="",comments="";
    LinearLayout lv_main_dilog;

    public static EditText et_shipping,et_ordernumber,et_comments;
    TextView tv_datepickeron;
    public static EditText et_creditcard_ordernumber,et_creditcard_comments,et_creditcard_number,et_creditcard_varification_no;
    private List<PaymentMethod> datumList;

    public static String paymentcode_ada;
    private int lastSelectedPosition = -1;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private DatePickerDialog mDatePickerDialog,creditcarddialog;
    RackMonthPicker rackMonthPicker;

    //card peramiter
    private static final int CARD_NUMBER_TOTAL_SYMBOLS = 19; // size of pattern 0000-0000-0000-0000
    private static final int CARD_NUMBER_TOTAL_DIGITS = 16; // max numbers of digits in pattern: 0000 x 4
    private static final int CARD_NUMBER_DIVIDER_MODULO = 5; // means divider position is every 5th symbol beginning with 1
    private static final int CARD_NUMBER_DIVIDER_POSITION = CARD_NUMBER_DIVIDER_MODULO - 1; // means divider position is every 4th symbol beginning with 0
    private static final char CARD_NUMBER_DIVIDER = '-';

    private static final int CARD_DATE_TOTAL_SYMBOLS = 5; // size of pattern MM/YY
    private static final int CARD_DATE_TOTAL_DIGITS = 4; // max numbers of digits in pattern: MM + YY
    private static final int CARD_DATE_DIVIDER_MODULO = 3; // means divider position is every 3rd symbol beginning with 1
    private static final int CARD_DATE_DIVIDER_POSITION = CARD_DATE_DIVIDER_MODULO - 1; // means divider position is every 2nd symbol beginning with 0
    private static final char CARD_DATE_DIVIDER = '/';

    private static final int CARD_CVC_TOTAL_SYMBOLS = 3;

    private static final int TOTAL_SYMBOLS = 19; // size of pattern 0000-0000-0000-0000
    private static final int TOTAL_DIGITS = 16; // max numbers of digits in pattern: 0000 x 4
    private static final int DIVIDER_MODULO = 5; // means divider position is every 5th symbol beginning with 1
    private static final int DIVIDER_POSITION = DIVIDER_MODULO - 1; // means divider position is every 4th symbol beginning with 0
    private static final char DIVIDER = '-';

    public PaymentInfoAdapter(FragmentActivity context) {
        this.context = context;
        datumList = new ArrayList<>();
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
        viewHolder = new MyViewHolder(v1);
        return viewHolder;
    }

    private void showCurrentTermsDialog(View view, String your_current_terms, String dateshippingg, String ordernumberr, String Waring_sting) {

        Log.e("ordernumber--103",""+ordernumber);
        final Dialog dialog = new Dialog(context, android.R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.current_terms_row);
        dialog.setCanceledOnTouchOutside(true);
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(dialog.getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
        lv_main_dilog=dialog.findViewById(R.id.lv_main_dilog);
        hideKeyboard((Activity) context);

        TextView tv_current_terms = dialog.findViewById(R.id.tv_current_terms);

        setupUI(lv_main_dilog);
        et_shipping = dialog.findViewById(R.id.et_shipping);
        tv_datepickeron = dialog.findViewById(R.id.tv_datepickeron);
        et_ordernumber = dialog.findViewById(R.id.et_ordernumber);
        et_comments = dialog.findViewById(R.id.et_comments);
        Log.e("date--93",""+dateshipping);

        Log.e("Waring_sting--104",""+comments);
        if (dateshipping.equals(null)|| dateshipping.equals("")|| dateshipping.equals("null")){
            et_shipping.setText("");
        }else{
            Log.e("set_date","");
            et_shipping.setText(dateshipping);
        }
        if (ordernumber.equals(null)|| ordernumber.equals("")|| ordernumber.equals("null")){
            et_ordernumber.setText("");
        }else {
            Log.e("set_order","");
            et_ordernumber.setText(ordernumber);
        }
        if (Waring_sting.equals(null)|| Waring_sting.equals("")|| Waring_sting.equals("null")){
            et_comments.setText("");
        }else {
            Log.e("set_comments","");
            et_comments.setText(comments);
        }

        this.ordernumber =et_ordernumber.getText().toString();
        comments=et_comments.getText().toString();

        LinearLayout lv_select_terms = dialog.findViewById(R.id.lv_select_terms);
        LinearLayout lv_main_terms = dialog.findViewById(R.id.lv_main_terms);
        setupUI(lv_main_terms);
        tv_datepickeron.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                mDatePickerDialog.getDatePicker().setMaxDate((long) (System.currentTimeMillis() + 1.21e+9));
                mDatePickerDialog.show();
                return false;
            }
        });
        et_shipping.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                mDatePickerDialog.getDatePicker().setMaxDate((long) (System.currentTimeMillis() + 1.21e+9));
                mDatePickerDialog.show();
                //setDateTimeField();
                return false;
            }
        });

        lv_select_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateshipping=et_shipping.getText().toString();
                ordernumber=et_ordernumber.getText().toString();
                comments=et_comments.getText().toString();
                if (et_shipping.getText().length() == 0) {
                    Toast.makeText(context, ""+context.getResources().getString(R.string.pickupdate_validation), Toast.LENGTH_SHORT).show();
                }else {

                    dialog.dismiss();
                }
            }
        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);
        // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void currentTermsDatepickerdialog() {
       // DatePickerDialog mDatePickerDialog;
        Calendar newCalendar = Calendar.getInstance();

        mDatePickerDialog = new DatePickerDialog(context,R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);//yyyy/MM/dd
                SimpleDateFormat sd = new SimpleDateFormat("yyyy/MM/dd");
                final Date startDate = newDate.getTime();
                String fdate = sd.format(startDate);
                Log.e("debugfate_145","xxc"+fdate);
                et_shipping.setText(fdate);
                dateshipping=fdate;

            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH));

        //mDatePickerDialog.show();
    }
    private void creditcardDatepickerDialog() {
        Calendar newCalendar = Calendar.getInstance();
        creditcarddialog = new DatePickerDialog(context,R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);//yyyy/MM/dd
                SimpleDateFormat sd = new SimpleDateFormat("yyyy/MM/dd");
                final Date startDate = newDate.getTime();
                String fdate = sd.format(startDate);
                Log.e("debugfate_145","xxc"+fdate);

                et_creditcard_shipping.setText(fdate);

            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH));
    }
    private void monthPickerDialog(final EditText editTextmonth, final EditText editTextYear)
    {
     new RackMonthPicker(context)
                .setLocale(Locale.ENGLISH)
                .setPositiveButton(new DateMonthDialogListener() {
                    @Override
                    public void onDateMonth(int month, int startDate, int endDate, int year, String monthLabel) {
                      Log.e("debug_mont178","fgd"+monthLabel);
                        editTextmonth.setText(String.valueOf(month));
                        editTextYear.setText(String.valueOf(year));
                    }
                })
                .setNegativeButton(new OnCancelMonthDialogListener() {
                    @Override
                    public void onCancel(AlertDialog dialog) {

                    }
                }).show();
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final PaymentMethod datum = datumList.get(position);
        currentTermsDatepickerdialog();
        creditcardDatepickerDialog();
        final MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.rad_text.setText(Html.fromHtml(datum.getLabel()));


        myViewHolder.rad_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.e("debug_166", "" + datum.getLabel());
                lastSelectedPosition = position;
                paymentcode_ada = datum.getValue();
                payment_method_order = datum.getValue();
                Log.e("paymentcode_111", "" + paymentcode_ada);
                Log.e("paymentcode_225", "" + payment_method_order);
                notifyDataSetChanged();
                if (datum.getLabel().equalsIgnoreCase("Your Current Terms")) {
                    showCurrentTermsDialog(v, "Your Current Terms",dateshipping,ordernumber,comments);
                } else if (datum.getLabel().equalsIgnoreCase("Credit Card")) {

                    Log.e("debug_card_281", "" + card_ship_date);
                    showCredit_Card_Dialog(v, "Credit Card");
                }
            }
        });

        if (lastSelectedPosition == position) {
            Log.e("selectedpo_76", "" + lastSelectedPosition);
            myViewHolder.rad_text.setChecked(true);
        } else {
            myViewHolder.rad_text.setChecked(false);
        }

        if (position == getItemCount() - 1) {
            myViewHolder.viewpayment.setVisibility(View.GONE);
        } else {
            myViewHolder.viewpayment.setVisibility(View.VISIBLE);
        }
    }
    private void showCredit_Card_Dialog(View v, String credit_card) {
        final Dialog dialog = new Dialog(context, android.R.style.Theme_Dialog);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.credit_card_row);
        ButterKnife.bind((Activity) context);

        LinearLayout lv_select_creditcard = null;
        TextView tv_credit_card_select,credit_card_type;
        et_creditcard_shipping = dialog.findViewById(R.id.et_creditcard_shipping);
        et_creditcard_ordernumber = dialog.findViewById(R.id.et_creditcard_ordernumber);
        et_creditcard_comments = dialog.findViewById(R.id.et_creditcard_comments);
        et_creditcard_number = dialog.findViewById(R.id.et_creditcard_number);
        credit_card_type = dialog.findViewById(R.id.credit_card_type);

        et_card_month = dialog.findViewById(R.id.et_card_month);
        et_creditcard_year = dialog.findViewById(R.id.et_creditcard_year);
        et_creditcard_varification_no = dialog.findViewById(R.id.et_creditcard_varification_no);
        lv_select_creditcard = dialog.findViewById(R.id.lv_select_creditcard);

        dialog.setCanceledOnTouchOutside(true);

        et_creditcard_ordernumber.setText(card_purchesorder_number);
        et_creditcard_varification_no.setText(card_varification_no);
        et_card_month.setText(card_exp_moth);
        et_creditcard_year.setText(card_exp_year);
        et_creditcard_number.setText(card_number);
        et_creditcard_shipping.setText(card_ship_date);

        final Observable<String> creditCardNumber =
                RxTextView.textChanges(et_creditcard_number)
                        .map(CharSequence::toString);

        // Create derived Observables
        final Observable<CardType> cardType =
                creditCardNumber
                        .map(CardType::fromNumber);

        final Observable<Boolean> isKnownCardType =
                cardType
                        .map(cardTypeValue -> cardTypeValue != CardType.CREDIT_CARD_TYPE);

        final Observable<Boolean> isValidCheckSum =
                creditCardNumber
                        .map(ValidationUtils::checkCardChecksum);

        final Observable<Boolean> isValidNumber =
                Observable.combineLatest(
                        isKnownCardType,
                        isValidCheckSum,
                        (isValidType, isChecksumCorrect) -> isValidType && isChecksumCorrect);

        cardType
                .map(Enum::toString)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(credit_card_type::setText);

        showErrorForEditText(et_creditcard_number, isValidNumber);

        et_creditcard_shipping.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                creditcarddialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                creditcarddialog.getDatePicker().setMaxDate((long) (System.currentTimeMillis() + 1.296e+9));
                creditcarddialog.show();
                //setDateTimeField();
                return false;
            }
        });

        et_card_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthPickerDialog(et_card_month,et_creditcard_year);
            }
        });

        et_creditcard_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("debug222","click");
                monthPickerDialog(et_card_month,et_creditcard_year);
            }
        });

        lv_select_creditcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_creditcard_shipping.getText().length()==0)
                {
                    Toast.makeText(context, ""+context.getResources().getString(R.string.shippingdate), Toast.LENGTH_SHORT).show();
                }else if(et_creditcard_number.getText().length()==0)
                {
                    Toast.makeText(context, ""+context.getResources().getString(R.string.creditcardnumer), Toast.LENGTH_SHORT).show();
                }else if(et_card_month.getText().length()==0)
                {
                    Toast.makeText(context, ""+context.getResources().getString(R.string.monthvaidate), Toast.LENGTH_SHORT).show();
                } else if(et_creditcard_year.getText().length()==0)
                {
                    Toast.makeText(context, ""+context.getResources().getString(R.string.yearvalidate), Toast.LENGTH_SHORT).show();
                } else if(et_creditcard_varification_no.getText().length()==0)
                {
                    Toast.makeText(context, ""+context.getResources().getString(R.string.varify_number), Toast.LENGTH_SHORT).show();
                }else {
                    card_purchesorder_number=et_creditcard_ordernumber.getText().toString();
                    card_number=et_creditcard_number.getText().toString();
                    card_exp_moth=et_card_month.getText().toString();
                    card_exp_year=et_creditcard_year.getText().toString();
                    card_ship_date=et_creditcard_shipping.getText().toString();
                    card_varification_no=et_creditcard_varification_no.getText().toString();

                   dialog.dismiss();
                }
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);
        // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private static void showErrorForEditText(EditText editText,
                                             Observable<Boolean> isValid) {
        getShowErrorForEditText(editText, isValid)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(value -> editText.setTextColor(
                        value ? Color.RED : Color.BLACK));
    }

    private static Observable<Boolean> getShowErrorForEditText(EditText editText,
                                                               Observable<Boolean> isValid) {
        // We need refCount because we have two subscribers: otherwise the first will be
        // unsubscribed automatically when second one arrives
        final Observable<Boolean> hasFocus = RxView.focusChanges(editText).publish().refCount();

        final Observable<Boolean> hasHadFocus =
                Observable.concat(
                        Observable.just(false),
                        hasFocus.filter(value -> value).firstElement().toObservable());

        return Observable.combineLatest(
                hasHadFocus, hasFocus, isValid,
                (hasHadFocusValue, hasFocusValue, isValidValue) ->
                        hasHadFocusValue && (!hasFocusValue && !isValidValue));
    }

    private String concatString(char[] digits, int dividerPosition, char divider) {
        final StringBuilder formatted = new StringBuilder();

        for (int i = 0; i < digits.length; i++) {
            if (digits[i] != 0) {
                formatted.append(digits[i]);
                if ((i > 0) && (i < (digits.length - 1)) && (((i + 1) % dividerPosition) == 0)) {
                    formatted.append(divider);
                }
            }
        }

        return formatted.toString();
    }

    private char[] getDigitArray(final Editable s, final int size) {
        char[] digits = new char[size];
        int index = 0;
        for (int i = 0; i < s.length() && index < size; i++) {
            char current = s.charAt(i);
            if (Character.isDigit(current)) {
                digits[index] = current;
                index++;
            }
        }
        return digits;
    }

    private boolean isInputCorrect(Editable s, int size, int dividerPosition, char divider) {
        boolean isCorrect = s.length() <= size;
        for (int i = 0; i < s.length(); i++) {
            if (i > 0 && (i + 1) % dividerPosition == 0) {
                isCorrect &= divider == s.charAt(i);
            } else {
                isCorrect &= Character.isDigit(s.charAt(i));
            }
        }
        return isCorrect;
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

