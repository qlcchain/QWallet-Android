package com.today.step.net;

import android.location.Location;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface CHttpApi {
    @POST("/api/gzbd/create.json")
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<CreateRecord> createRecord(@Body RequestBody map);

    @POST("/api/gzbd/focus.json")
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<CreateRecord> focusEpidemic(@Body RequestBody map);

    @POST("/api/gzbd/receive_v2.json")
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<ClaimQgas> epidemicReceive(@Body RequestBody map);

    @POST("/api/gzbd/claim_qlc_v2.json")
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<ClaimQgas> epidemicReceiveQlc(@Body RequestBody map);

    @POST("/api/gzbd/list.json")
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<EpidemicList> eepidemicList(@Body RequestBody map);
}
