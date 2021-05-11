package qlc.network;

import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class QlcHttpClient {

    private final String url;

    public QlcHttpClient(String url) throws MalformedURLException {
        this.url = url;
    }

    public JSONObject send(JSONObject params) throws IOException {

        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS).build();

        try {

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), params.toJSONString());

            Request request = new Request.Builder().url(url).post(body).build();
            Response response = client.newCall(request).execute();
            if (response == null)
                throw new qlc.network.QlcException(900, "Invalid response type");

            int statusCode = response.code();
            if (statusCode != 200) {
                throw new qlc.network.QlcException(statusCode, response.code() + "");
            }

            JSONObject result = JSONObject.parseObject(response.body().string());
            if (result.containsKey("result") || result.containsKey("error"))
                return result;
            else {
                throw new IOException();
            }
        } catch (Exception e) {
			KLog.i("error : " + e.getMessage());
            throw new IOException();
        }
    }

}
