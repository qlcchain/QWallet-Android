package com.stratagile.topup;

/**
 * Created by hu on 2016/11/11.
 */

import com.socks.library.KLog;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * @Description 异常处理 拦截器
 * Created by EthanCo on 2016/7/14.
 */
public class TopupInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        KLog.i("请求的地址为：" + request.url());
        Response response = chain.proceed(request);
        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();
//        KLog.i("reqeust的body为:" + request.body());
//        KLog.i("reqeust的head为:" + request.headers());
        KLog.i(response.code());
        KLog.i(response.headers().toString());
        KLog.i(response.toString());
        KLog.i(request.url());
        return response;
    }
}