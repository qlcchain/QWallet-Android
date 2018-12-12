package com.stratagile.qlink.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.stratagile.qlink.R;
import com.stratagile.qlink.view.SweetAlertDialog;

public class DialogUtils {
    public static void showDialog(Context context, String content, OnClick click) {
        View view = ((Activity)context).getLayoutInflater().inflate(R.layout.alert_dialog_content, null, false);
        TextView tvContent = view.findViewById(R.id.tvContent);
        TextView tvOpreate = view.findViewById(R.id.tvOpreate);
        tvContent.setText(content);
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context);
        sweetAlertDialog.setView(view);
        sweetAlertDialog.show();
        tvOpreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sweetAlertDialog.cancel();
                if (click != null) {
                    click.onClick();
                }
            }
        });

    }

    public interface OnClick {
        void onClick();
    }
}
