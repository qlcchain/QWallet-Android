package com.stratagile.qlink.ui.activity.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.URLUtil;
import android.webkit.WebBackForwardList;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.entity.eventbus.OnAppResume;
import com.stratagile.qlink.ui.activity.main.component.DaggerWebViewComponent;
import com.stratagile.qlink.ui.activity.main.contract.WebViewContract;
import com.stratagile.qlink.ui.activity.main.module.WebViewModule;
import com.stratagile.qlink.ui.activity.main.presenter.WebViewPresenter;
import com.stratagile.qlink.ui.activity.topup.TopupOrderListActivity;
import com.stratagile.qlink.utils.WXH5PayHandler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.main
 * @Description: $description
 * @date 2018/05/30 11:52:27
 */

public class WebViewActivity extends BaseActivity implements WebViewContract.View {

    @Inject
    WebViewPresenter mPresenter;
    @BindView(R.id.webView)
    WebView webView;

    private boolean isWxPay = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        mainColor = R.color.white;
        setContentView(R.layout.activity_web_view);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        if (getIntent().getStringExtra("url").contains("https://shop.huagaotx.cn/vendor")) {
            isWxPay = true;
        }
        setTitle(getIntent().getStringExtra("title"));
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDefaultFontSize(16);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setDefaultTextEncodingName("UTF-8");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setAppCacheEnabled(false);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.loadUrl(getIntent().getStringExtra("url"));
//        webView.loadUrl("https://www.baidu.com");
        KLog.i(getIntent().getStringExtra("url"));
        webView.setWebViewClient(new XWebViewClient());
    }



    @Override
    protected void setupActivityComponent() {
        DaggerWebViewComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .webViewModule(new WebViewModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(WebViewContract.WebViewContractPresenter presenter) {
        mPresenter = (WebViewPresenter) presenter;
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        progressDialog.hide();
    }

    public class XWebViewClient extends WebViewClient {

        private WXH5PayHandler mWXH5PayHandler;

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            KLog.i(url);
            title.setText(view.getTitle());
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
                    isWxPay = true;
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
                isWxPay = true;
                webView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        webView.setVisibility(View.INVISIBLE);
                        //setResult(RESULT_OK);
                        //finish();
                    }
                }, 2000);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAppResume(OnAppResume onAppResume) {
        if (isWxPay) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        if (isWxPay) {
            startActivity(new Intent(WebViewActivity.this, TopupOrderListActivity.class));
            setResult(RESULT_OK);
        }
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        // back history
        int index = -1; // -1表示回退history上一页
        String url;
        WebBackForwardList history = webView.copyBackForwardList();
        while (webView.canGoBackOrForward(index)) {
            url = history.getItemAtIndex(history.getCurrentIndex() + index).getUrl();
            if (URLUtil.isNetworkUrl(url) && !WXH5PayHandler.isWXH5Pay(url)) {
                webView.goBackOrForward(index);
                return;
            }
            index--;
        }
        super.onBackPressed();
        /*if (webView.getUrl().contains("partner_order_list")) {
            finish();
        }*/
    }



}