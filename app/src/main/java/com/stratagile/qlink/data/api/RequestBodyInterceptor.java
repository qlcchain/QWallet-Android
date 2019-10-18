package com.stratagile.qlink.data.api;


import java.io.IOException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeMap;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author hu
 * @desc 功能描述
 * 胡（请求的body的拦截器）
 * @date 2017/05/31
 */
public final class RequestBodyInterceptor implements Interceptor {

    private static final Comparator<String> SORT_COMPARATOR = new Comparator<String>() {

        @Override
        public int compare(String s, String t1) {
            return s.compareTo(t1);
        }
    };

    @Override
    public Response intercept(Chain chain) throws IOException {
        TreeMap<String, String> map = new TreeMap<String, String>(SORT_COMPARATOR);

        Request orgRequest = chain.request();

        Request.Builder orgRequestBuilder = orgRequest
                .newBuilder();
//                .header("User-Agent", "android")
//                .header("user-Agent", "android");
//                .header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjEwOTksImlzcyI6Imh0dHA6Ly9jaGluYS5pbndlY3J5cHRvLmNvbTo0NDMxL3YyL2xvZ2luIiwiaWF0IjoxNTI0NjQyOTAwLCJleHAiOjE1MjUyNDc3MDAsIm5iZiI6MTUyNDY0MjkwMCwianRpIjoiSTdsemdRcXBBVEdudmFIayJ9.GIQyWMZe5pQ5N7I2YGJecnNVrInYe73oqVQJlEyD4x4")
//                .header("neo-asset-id", "0xc56f33fc6ecfcd0c225c4ab356fee59390af8560be0e930faebe74a6daff7c9b")
//                .header("neo-gas-asset-id", "0x602c79718b16e442de58778e148d0b1084e3b2dffd5de6b7b16cee7969282de7");
        //请求定制：添加请求头
        if (orgRequest.body() == null) {
            return chain.proceed(orgRequest);
        }
        RequestBody orgRequestBody = orgRequest.body();
        if (orgRequestBody instanceof FormBody) {
            FormBody orgFormBody = (FormBody) orgRequestBody;
            FormBody.Builder newFormBodyBuilder = new FormBody.Builder();
            for (int i = 0; i < orgFormBody.size(); i++) {
                map.put(orgFormBody.name(i), orgFormBody.value(i));
                newFormBodyBuilder.addEncoded(orgFormBody.encodedName(i), orgFormBody.encodedValue(i));
            }
            //sort + md5
            StringBuilder sb = new StringBuilder();
            Iterator iter = map.keySet().iterator();
            while (iter.hasNext()) {
                String key = (String) iter.next();
                sb.append(key == null ? "" : key);//key
                sb.append(map.get(key) == null ? "" : map.get(key));//value
            }
            //append requestSign
            orgRequestBuilder.delete(orgFormBody);
            orgRequestBuilder.method(orgRequest.method(), newFormBodyBuilder.build());
            Request newRequest = orgRequestBuilder.build();
            return chain.proceed(newRequest);
        }

        return chain.proceed(orgRequest);
    }

}
