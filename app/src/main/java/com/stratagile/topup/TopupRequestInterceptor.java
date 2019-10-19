package com.stratagile.topup;


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
public final class TopupRequestInterceptor implements Interceptor {

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
        Request.Builder orgRequestBuilder = orgRequest.newBuilder();
        Request newRequest = orgRequestBuilder.build();
        return chain.proceed(newRequest);

//        RequestBody orgRequestBody = orgRequest.body();
//        if (orgRequestBody instanceof FormBody) {
//            FormBody orgFormBody = (FormBody) orgRequestBody;
//            FormBody.Builder newFormBodyBuilder = new FormBody.Builder();
//            for (int i = 0; i < orgFormBody.size(); i++) {
//                map.put(orgFormBody.name(i), orgFormBody.value(i));
//                newFormBodyBuilder.addEncoded(orgFormBody.encodedName(i), orgFormBody.encodedValue(i));
//            }
//            //sort + md5
//            StringBuilder sb = new StringBuilder();
//            Iterator iter = map.keySet().iterator();
//            while (iter.hasNext()) {
//                String key = (String) iter.next();
//                sb.append(key == null ? "" : key);//key
//                sb.append(map.get(key) == null ? "" : map.get(key));//value
//            }
//            //append requestSign
//            orgRequestBuilder.delete(orgFormBody);
//            orgRequestBuilder.method(orgRequest.method(), newFormBodyBuilder.build());
//            Request newRequest = orgRequestBuilder.build();
//            return chain.proceed(newRequest);
//        }
//
//        return chain.proceed(orgRequest);
    }

}
