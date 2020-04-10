package com.stratagile.qlink.ui.components;

import android.content.Context;
import android.support.v7.app.AlertDialog;

import com.stratagile.qlink.R;

public class AlertsCreator {
    public static AlertDialog.Builder createSimpleAlert(Context context, final String text) {
        return createSimpleAlert(context, null, text);
    }

    public static AlertDialog.Builder createSimpleAlert(Context context, final String title, final String text) {
        if (text == null) {
            return null;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title == null ? "test" : title);
        builder.setMessage(text);
        builder.setPositiveButton("test1", null);
        return builder;
    }
}
