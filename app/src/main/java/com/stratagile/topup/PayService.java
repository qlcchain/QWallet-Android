package com.stratagile.topup;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface PayService {
    @GET
    Observable<Response<ResponseBody>> payBill(@Url String url);
}
