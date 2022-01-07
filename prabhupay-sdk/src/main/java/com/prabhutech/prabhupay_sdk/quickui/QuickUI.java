package com.prabhutech.prabhupay_sdk.quickui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.prabhutech.prabhupay_sdk.R;

/**
 * Created by Niken on 7/31/2020.
 */
public class QuickUI {
    public static void showBasicAlertDialog(Activity activity, String title, String message, boolean isSkippable, SimpleCallback callback) {
        new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(isSkippable)
                .setPositiveButton(android.R.string.ok, ((dialog, which) -> {
                    dialog.dismiss();
                    callback.call();
                }))
                .show();
    }
}
