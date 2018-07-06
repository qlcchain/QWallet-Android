package com.stratagile.qlink.utils;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.ui.activity.main.MainActivity;

import javax.inject.Singleton;

import static com.vondear.rxtools.RxTool.getContext;

/**
 * 提示工具类
 *
 * @author lijing
 */
@Singleton
public class ToastUtil {

    public static void init() {
        Message msg = handler.obtainMessage();
        Bundle b = new Bundle();
        b.putInt("showTime", 2);
        b.putString("tips", "");
        msg.setData(b);
        handler.sendMessage(msg);
    }

    private static Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            Bundle bundle = msg.getData();
            int showTime = bundle.getInt("showTime");
            String tips = bundle.getString("tips");
            switch (showTime) {
                case 0:
                    Toast.makeText(AppConfig.instance, tips, Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(AppConfig.instance, tips, Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }


        }

    };

    public static void displayShortToast(final String text) {
        if (text == null) {
            return;
        }
        try {
            new Thread() {
                @Override
                public void run() {
                    Message msg = handler.obtainMessage();
                    Bundle b = new Bundle();
                    b.putInt("showTime", 0);
                    b.putString("tips", text);
                    msg.setData(b);
                    handler.sendMessage(msg);
                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void show(Context context, String info) {
        displayShortToast(info);
    }

}
