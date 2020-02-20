package com.stratagile.qlink.utils;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsMessage;

import com.socks.library.KLog;
import com.stratagile.qlink.entity.QLcSms;

import org.greenrobot.eventbus.EventBus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kotlin.collections.LongIterator;

/**
 * @Title:
 * @Description:
 * @Author:leo
 * @Since:
 * @Version:
 */

public class SmsObserver extends ContentObserver {

    private Context mContext;
    private Handler handler;
    public SmsObserver(Context context, Handler handler) {
        super(handler);
        mContext = context;
        this.handler = handler;
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        String code;
        if (uri.toString().equals("content://sms/raw"))  ////onChange会执行二次,第二次短信才会入库
        {
            return ;
        }

        Uri inboxUri = Uri.parse("content://sms/inbox");
        Cursor c = mContext.getContentResolver().query(inboxUri, null, null, null, "date desc");
        if (c != null) {
            if (c.moveToFirst()) {
                String body = c.getString(c.getColumnIndex("body"));//获取短信内容
                String address = c.getString(c.getColumnIndex("address"));//获取短信发送号码
                String _id = c.getString(c.getColumnIndex("_id"));//获取短信id
                String date = c.getString(c.getColumnIndex("date"));//获取短信date
                KLog.i("号码为：" + address);
                KLog.i("内容为：" + body);
                KLog.i("id为：" + _id);
                KLog.i("时间为：" + date);
                Message msg = new Message();
                msg.obj = new QLcSms(address, body, Integer.parseInt(_id), Long.parseLong(date));
                msg.what = 1;
                handler.sendMessage(msg);
//                EventBus.getDefault().post(new QLcSms(address, body, System.currentTimeMillis()));
            }
            c.close();
        }
    }
}
