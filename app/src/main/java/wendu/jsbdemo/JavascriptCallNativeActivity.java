package wendu.jsbdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.stratagile.qlink.R;

import wendu.dsbridge.DWebView;

public class JavascriptCallNativeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_js_call_native);
        final DWebView dwebView= (DWebView) findViewById(R.id.webview);
        // set debug mode
        DWebView.setWebContentsDebuggingEnabled(true);
        dwebView.addJavascriptObject(new JsApi(), null);
        dwebView.addJavascriptObject(new JsEchoApi(),"echo");
        dwebView.loadUrl("file:///android_asset/js-call-native.html");
    }
}
