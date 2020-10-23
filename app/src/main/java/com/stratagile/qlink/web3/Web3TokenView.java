package com.stratagile.qlink.web3;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.stratagile.qlink.BuildConfig;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.entity.tokenscript.TokenScriptRenderCallback;
import com.stratagile.qlink.entity.tokenscript.WebCompletionCallback;
import com.stratagile.qlink.entity.walletconnect.EthereumMessage;
import com.stratagile.qlink.entity.walletconnect.Signable;
import com.stratagile.qlink.utils.Utils;
import com.stratagile.qlink.web3.entity.Address;
import com.stratagile.qlink.web3.entity.FunctionCallback;
import com.stratagile.qlink.web3.entity.PageReadyCallback;
import com.stratagile.qlink.web3.entity.Web3Transaction;

import org.jetbrains.annotations.NotNull;

import java.io.LineNumberReader;
import java.io.StringReader;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by James on 3/04/2019.
 * Stormbird in Singapore
 */
public class Web3TokenView extends WebView
{
    public static final String TOKENSCRIPT_ERROR = "<h2 style=\"color:rgba(207, 0, 15, 1);\">TokenScript Error</h2>";
    public static final String RENDERING_ERROR = "<html>" + TOKENSCRIPT_ERROR + "${ERR1}</html>";
    public static final String RENDERING_ERROR_SUPPLIMENTAL = "</br></br>Error in line $ERR1:</br>$ERR2";

    private static final String JS_PROTOCOL_CANCELLED = "cancelled";
    private static final String JS_PROTOCOL_ON_SUCCESSFUL = "executeCallback(%1$s, null, \"%2$s\")";
    private static final String JS_PROTOCOL_ON_FAILURE = "executeCallback(%1$s, \"%2$s\", null)";
    private static final String REFRESH_ERROR = "refresh is not defined";

    private JsInjectorClient jsInjectorClient;
    private TokenScriptClient tokenScriptClient;
    private PageReadyCallback assetHolder;
    private boolean showingError = false;
    private String unencodedPage;

    protected WebCompletionCallback keyPressCallback;

    @Nullable
    private OnSignPersonalMessageListener onSignPersonalMessageListener;
    @Nullable
    private OnSetValuesListener onSetValuesListener;

    public Web3TokenView(@NonNull Context context) {
        super(context);
        init();
    }

    public Web3TokenView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Web3TokenView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init() {
        tokenScriptClient = new TokenScriptClient(this);
        jsInjectorClient = new JsInjectorClient(getContext());
        WebSettings webSettings = super.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setUseWideViewPort(false);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUserAgentString(webSettings.getUserAgentString()
                                               + "AlphaWallet(Platform=Android&AppVersion=" + BuildConfig.VERSION_NAME + ")");
        WebView.setWebContentsDebuggingEnabled(true);

        setScrollBarSize(0);
        setVerticalScrollBarEnabled(false);
        setScrollContainer(false);
        setScrollbarFadingEnabled(true);

        setInitialScale(0);
        clearCache(true);
        showingError = false;

        addJavascriptInterface(new TokenScriptCallbackInterface(
                this,
                innerOnSignPersonalMessageListener,
                innerOnSetValuesListener), "alpha");

        super.setWebViewClient(tokenScriptClient);

        setWebChromeClient(new WebChromeClient()
        {
            @Override
            public boolean onConsoleMessage(ConsoleMessage msg)
            {
                if (!showingError && msg.messageLevel() == ConsoleMessage.MessageLevel.ERROR)
                {
                    if (msg.message().contains(REFRESH_ERROR)) return true; //don't stop for refresh error
                    String errorLine = "";
                    try
                    {
                        LineNumberReader lineNumberReader = new LineNumberReader(new StringReader(unencodedPage));
                        lineNumberReader.setLineNumber(0);

                        String lineStr;
                        while ((lineStr = lineNumberReader.readLine()) != null)
                        {
                            if (lineNumberReader.getLineNumber() == msg.lineNumber())
                            {
                                errorLine = Utils.escapeHTML(lineStr); //ensure string is displayed exactly how it is read
                                break;
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        errorLine = "";
                    }

                    String errorMessage = RENDERING_ERROR.replace("${ERR1}", msg.message());
                    if (!TextUtils.isEmpty(errorLine)) errorMessage += RENDERING_ERROR_SUPPLIMENTAL.replace("$ERR1", String.valueOf(msg.lineNumber())).replace("$ERR2", errorLine); //.replace("$ERR2", errorMessage)
                    showError(errorMessage);
                    unencodedPage = null;
                }
                return true;
            }
        });
    }

    public void showError(String error)
    {
        showingError = true;
        setVisibility(View.VISIBLE);
        loadData(error, "text/html", "utf-8");
    }

    @Override
    public void setWebChromeClient(WebChromeClient client)
    {
        super.setWebChromeClient(client);
    }

    @JavascriptInterface
    public void onValue(String data)
    {
        System.out.println(data);
    }

    public void setWalletAddress(@NonNull Address address)
    {
        jsInjectorClient.setWalletAddress(address);
    }

    public void setupWindowCallback(@NonNull FunctionCallback callback)
    {
        setWebChromeClient(
                new WebChromeClient()
                {
                    @Override
                    public void onCloseWindow(WebView window)
                    {
                        callback.functionSuccess();
                    }
                }
        );
    }

    public void setChainId(int chainId) {
        jsInjectorClient.setChainId(chainId);
    }

    public void setRpcUrl(@NonNull int chainId) {
        jsInjectorClient.setRpcUrl(ConstantValue.ethNodeUrl);
    }

    public void onSignPersonalMessageSuccessful(@NotNull Signable message, String signHex) {
        long callbackId = message.getCallbackId();
        callbackToJS(callbackId, JS_PROTOCOL_ON_SUCCESSFUL, signHex);
    }

    public void setKeyboardListenerCallback(WebCompletionCallback cpCallback)
    {
        keyPressCallback = cpCallback;
    }

    @Override
    public String getUrl()
    {
        return "TokenScript";
    }

    public void callToJS(String function) {
        post(() -> evaluateJavascript(function, value -> Log.d("WEB_VIEW", value)));
    }

    @JavascriptInterface
    public void TScallToJS(String fName, String script, TokenScriptRenderCallback cb)
    {
        post(() -> evaluateJavascript(script, value -> cb.callToJSComplete(fName, value)));
    }

    @JavascriptInterface
    public void callbackToJS(long callbackId, String function, String param) {
        String callback = String.format(function, callbackId, param);
        post(() -> evaluateJavascript(callback, value -> Log.d("WEB_VIEW", value)));
    }

    public void setOnSignPersonalMessageListener(@Nullable OnSignPersonalMessageListener onSignPersonalMessageListener) {
        this.onSignPersonalMessageListener = onSignPersonalMessageListener;
    }

    public void setOnSetValuesListener(@Nullable OnSetValuesListener onSetValuesListener) {
        this.onSetValuesListener = onSetValuesListener;
    }

    private final OnSignTransactionListener innerOnSignTransactionListener = new OnSignTransactionListener() {
        @Override
        public void onSignTransaction(Web3Transaction transaction, String url) {

        }
    };

    private final OnSignMessageListener innerOnSignMessageListener = new OnSignMessageListener() {
        @Override
        public void onSignMessage(EthereumMessage message) {

        }
    };

    private final OnSignPersonalMessageListener innerOnSignPersonalMessageListener = new OnSignPersonalMessageListener() {
        @Override
        public void onSignPersonalMessage(EthereumMessage message) {
            onSignPersonalMessageListener.onSignPersonalMessage(message);
        }
    };

    private final OnSetValuesListener innerOnSetValuesListener = new OnSetValuesListener() {
        @Override
        public void setValues(Map<String, String> updates)
        {
            if (onSetValuesListener != null) onSetValuesListener.setValues(updates);
        }
    };

    public void onSignCancel(@NotNull Signable message) {
        long callbackId = message.getCallbackId();
        callbackToJS(callbackId, JS_PROTOCOL_ON_FAILURE, JS_PROTOCOL_CANCELLED);
    }

    public void setOnReadyCallback(PageReadyCallback holder)
    {
        assetHolder = holder;
    }

    public String injectWeb3TokenInit(String view, String tokenContent, BigInteger tokenId)
    {
        return jsInjectorClient.injectWeb3TokenInit(getContext(), view, tokenContent, tokenId);
    }

    public String injectJS(String view, String buildToken)
    {
        return jsInjectorClient.injectJS(view, buildToken);
    }

    public String injectJSAtEnd(String view, String JSCode)
    {
        return jsInjectorClient.injectJSAtEnd(view, JSCode);
    }

    public String injectStyleAndWrapper(String viewData, String style)
    {
        return jsInjectorClient.injectStyleAndWrap(viewData, style);
    }

    private class TokenScriptClient extends WebViewClient
    {
        public TokenScriptClient(Web3TokenView web3)
        {
            super();
        }

        @Override
        public void onPageFinished(WebView view, String url)
        {
            super.onPageFinished(view, url);
            unencodedPage = null;
            if (assetHolder != null)
                assetHolder.onPageRendered(view);
        }

        @Override
        public void onPageCommitVisible(WebView view, String url)
        {
            super.onPageCommitVisible(view, url);
            if (assetHolder != null)
                assetHolder.onPageLoaded(view);
        }

        @Override
        public void onUnhandledKeyEvent(WebView view, KeyEvent event)
        {
            if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)
            {
                if (keyPressCallback != null)
                    keyPressCallback.enterKeyPressed();
            }
            super.onUnhandledKeyEvent(view, event);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            if (assetHolder != null)
            {
                return assetHolder.overridePageLoad(view, url);
            }
            else
            {
                return false;
            }
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error)
        {
            showError(RENDERING_ERROR.replace("${ERR1}", error.getDescription()));
        }
    }


    private void loadingError(Throwable e)
    {
        e.printStackTrace();
    }



    @Override
    public void destroy()
    {
        super.destroy();
    }

}
