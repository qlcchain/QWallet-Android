package com.stratagile.qlink.utils;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.socks.library.KLog;

public class XWebViewClient extends WebViewClient {

    private WXH5PayHandler mWXH5PayHandler;

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        KLog.i(url);
        if (TextUtils.isEmpty(url)) {
            return true;
        }

        Uri uri = null;
        try {
            uri = Uri.parse(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (uri == null) {
            return true;
        }

        if (!URLUtil.isNetworkUrl(url)) {
            //  处理微信h5支付2
            if (mWXH5PayHandler != null && mWXH5PayHandler.isWXLaunchUrl(url)) {
                mWXH5PayHandler.launchWX(view, url);
            } else {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    view.getContext().startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return true;
        }

        if (WXH5PayHandler.isWXH5Pay(url)) {
            // 处理微信h5支付1
            mWXH5PayHandler = new WXH5PayHandler();
            return mWXH5PayHandler.pay(url);
        } else if (mWXH5PayHandler != null) {
            // 处理微信h5支付3
            if (mWXH5PayHandler.isRedirectUrl(url)) {
                boolean result = mWXH5PayHandler.redirect();
                mWXH5PayHandler = null;
                return result;
            }
            mWXH5PayHandler = null;
        }

        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if (WXH5PayHandler.isWXH5Pay(url)) {

        }
    }
}
