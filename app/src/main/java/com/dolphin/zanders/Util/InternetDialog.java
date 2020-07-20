package com.dolphin.zanders.Util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Spannable;
import android.text.SpannableString;
import android.widget.Button;
import android.widget.TextView;

import com.dolphin.zanders.Activity.NavigationActivity;
import com.dolphin.zanders.R;

public class InternetDialog {

    public  void noInternetDialog(final Context context) {
        // Create the object of
        // AlertDialog Builder class
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(context);
        // Set the message show for the Alert time
        builder.setMessage(context.getResources().getString(R.string.nointernet));
        //set title type face
        // Set Alert Title
        CustomTFSpan tfSpan ;

        tfSpan = new CustomTFSpan(NavigationActivity.montserrat_regular);

        SpannableString spannableString = new SpannableString(context.getResources().getString(R.string.nointernett));
        spannableString.setSpan(tfSpan, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setTitle(spannableString);
        // Set Cancelable false
        // for when the user clicks on the outside
        // the Dialog Box then it will remain show
        builder.setCancelable(false);
        // Set the positive button with yes name
        builder.setPositiveButton(context.getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if(CheckNetwork.isNetworkAvailable(context))
                {

                }else {
                    dialog.cancel();
                }
            }});

        // Set the Negative button with No name
        builder.setNegativeButton(context.getResources().getString(R.string.Cancel), new DialogInterface
                .OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });
        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        // Show the Alert Dialog box
        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        Button nbutton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(context.getResources().getColor(R.color.colorPrimary));


        textView.setTypeface(NavigationActivity.montserrat_semibold);
        pbutton.setTypeface(NavigationActivity.montserrat_semibold);
        nbutton.setTypeface(NavigationActivity.montserrat_semibold);

    }

}
