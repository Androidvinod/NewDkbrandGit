package com.dolphin.zanders.Preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Login_preference {
    public static SharedPreferences mPrefs;
    public static SharedPreferences.Editor prefsEditor;

    public static void setgrouppid(Context context, String value)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("grouppid", value);
        prefsEditor.commit();
    }
    public static String getgroupid(Context context)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("grouppid", "");
    }

    public static void setstoreName(Context context, String value)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("storeName", value);
        prefsEditor.commit();
    }
    public static String getstoreName(Context context)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("storeName", "");
    }

    public static void setstoreid(Context context, String value)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("storeid", value);
        prefsEditor.commit();
    }
    public static String getstoreid(Context context)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("storeid", "");
    }

    public static void settokenemail(Context context, String value)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("customeremail", value);
        prefsEditor.commit();
    }
    public static String gettokenemail(Context context)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("customeremail", "");
    }
    public static void settokenepassword(Context context, String value)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("customerpassword", value);
        prefsEditor.commit();
    }
    public static String gettokenpassword(Context context)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("customerpassword", "");
    }

    public static void setshippingid(Context context, String value) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("shipping_id", value);
        prefsEditor.commit();
    }

    public static String getshippingid(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("shipping_id", "");
    }

    public static void setshippingadd(Context context, String value) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("shipping_add", value);
        prefsEditor.commit();
    }

    public static String getshippingadd(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("shipping_add", "");
    }
    public static void setbillingid(Context context, String value) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("billing_id", value);
        prefsEditor.commit();
    }

    public static String getbillingid(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("billing_id", "");
    }

    public static void setbillingadd(Context context, String value) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("billing_add", value);
        prefsEditor.commit();
    }

    public static String getbillingadd(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("billing_add", "");
    }

    public static void setLogin_flag(Context context, String value) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("login_flag", value);
        prefsEditor.commit();
    }

    public static String getLogin_flag(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("login_flag", "");
    }

    public static void setcustomer_id(Context context, String value) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("customer_id", value);
        prefsEditor.commit();
    }

    public static String getcustomer_id(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("customer_id", "");
    }

    public static void setemail(Context context, String value) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("email", value);
        prefsEditor.commit();
    }

    public static String getemail(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("email", "");
    }

    public static void setfirstname(Context context, String value) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("firstname", value);
        prefsEditor.commit();
    }

    public static String getfirstname(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("firstname", "");
    }

    public static void setlastname(Context context, String value) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("lastname", value);
        prefsEditor.commit();
    }

    public static String getlastname(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("lastname", "");
    }

    public static void setquote_id(Context context, String value)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("quote_id", value);
        prefsEditor.commit();
    }
    public static String getquote_id(Context context)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("quote_id", "");
    }
    public static void setCart_item_count(Context context, String value)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("item_count", value);
        prefsEditor.commit();
    }
    public static String getCart_item_count(Context context)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("item_count", "");
    }

    public static void set_wishlist_count(Context context, String value)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("wishlist_count", value);
        prefsEditor.commit();
    }
    public static String get_wishlist_count(Context context)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("wishlist_count", "");
    }
    public static void settoken(Context context, String value)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("token", value);
        prefsEditor.commit();
    }
    public static String gettoken(Context context)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("token", "");
    }

    public static void setcurrencycode(Context context, String value)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("currencycode", value);
        prefsEditor.commit();
    }
    public static String getcurrencycode(Context context)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("currencycode", "");
    }
    public static void setCustomertoken(Context context, String value)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("customertoken", value);
        prefsEditor.commit();
    }
    public static String getCustomertoken(Context context)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("customertoken", "");
    }

    public static void setdkQuoteId(Context context, String value)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("quoteiddk", value);
        prefsEditor.commit();
    }
    public static String getdkQuoteId(Context context)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("quoteiddk", "");
    }
}
