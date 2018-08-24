package com.stratagile.qlink.utils.eth;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AlertDialog;


import com.stratagile.qlink.R;
import com.stratagile.qlink.ui.activity.main.MainActivity;

import java.io.File;
import java.util.ArrayList;

public class Dialogs {
    public static void importWallets(final MainActivity c, final ArrayList<File> files) {
        String addresses = "";
        for (int i = 0; i < files.size() && i < 3; i++)
            addresses += WalletStorage.stripWalletName(files.get(i).getName()) + "\n";

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= 24) // Otherwise buttons on 7.0+ are nearly invisible
            builder = new AlertDialog.Builder(c);
        else
            builder = new AlertDialog.Builder(c);
        builder.setTitle("");
        builder.setMessage(String.format("", files.size(), files.size() > 1 ? "s" : "", addresses));
        builder.setPositiveButton("", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    WalletStorage.getInstance(c).importWallets(c, files);
//                    c.snackError("Wallet" + (files.size() > 1 ? "s" : "") + " successfully imported!");
//                    if (c.fragments != null && c.fragments[1] != null)
//                        ((FragmentWallets) c.fragments[1]).update();
                } catch (Exception e) {
//                    c.snackError("Error while importing wallets");
                    e.printStackTrace();
                }
                dialog.cancel();
            }
        });
        builder.setNegativeButton("", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public static void noImportWalletsFound(Context c) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= 24) // Otherwise buttons on 7.0+ are nearly invisible
            builder = new AlertDialog.Builder(c);
        else
            builder = new AlertDialog.Builder(c);
        builder.setTitle("");
        builder.setMessage("");
        builder.setNeutralButton("", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
