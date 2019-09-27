package com.stratagile.qlink.ui.activity.main;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.ui.activity.main.component.DaggerWebViewComponent;
import com.stratagile.qlink.ui.activity.main.contract.WebViewContract;
import com.stratagile.qlink.ui.activity.main.module.WebViewModule;
import com.stratagile.qlink.ui.activity.main.presenter.WebViewPresenter;

import java.net.URLDecoder;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        mainColor = R.color.white;
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        setTitle(getIntent().getStringExtra("title"));
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDefaultFontSize(16);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
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
//        webView.setWebViewClient(new WebViewClient(){
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                webView.loadUrl(url);
//                return true;
//            }
//
//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                super.onPageStarted(view, url, favicon);
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//                KLog.i(url);
//            }
//        });
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

}