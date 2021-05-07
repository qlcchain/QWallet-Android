package com.today.step.net;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.today.step.lib.TodayStep;

import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.HttpException;
import retrofit2.Retrofit;

public class CHttpApiWrapper {
    private static CHttpApiWrapper httpApiWrapper;
    private CHttpApi mHttpAPI;
    private CHttpApiWrapper(CHttpApi mHttpAPI) {
        this.mHttpAPI = mHttpAPI;
    }

    public static CHttpApiWrapper getInstance(Retrofit retrofit) {
        if (httpApiWrapper == null) {
            httpApiWrapper = new CHttpApiWrapper(retrofit.create(CHttpApi.class));
        }
        return httpApiWrapper;
    }

    public Observable<CreateRecord> createRecord(Map map) {
        return wrapper(mHttpAPI.createRecord(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
    }

    public Observable<CreateRecord> focusEpidemic(Map map) {
        return wrapper(mHttpAPI.focusEpidemic(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
    }

    public Observable<EpidemicList> eepidemicList(Map map) {
        return wrapper(mHttpAPI.eepidemicList(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
    }

    public Observable<ClaimQgas> epidemicReceive(Map map) {
        return wrapper(mHttpAPI.epidemicReceive(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
    }

    public Observable<ClaimQgas> epidemicReceiveQlc(Map map) {
        return wrapper(mHttpAPI.epidemicReceiveQlc(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
    }

    /**
     * 给任何Http的Observable加上通用的线程调度器
     */
    private static final ObservableTransformer SCHEDULERS_TRANSFORMER = new ObservableTransformer() {
        @Override
        public ObservableSource apply(@NonNull Observable upstream) {
            return upstream.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };

    private <T extends BaseBack> Observable<T> wrapper(Observable<T> resourceObservable) {
        return resourceObservable
                .flatMap(new Function<T, ObservableSource<? extends T>>() {
                    @Override
                    public ObservableSource<? extends T> apply(final @NonNull T baseResponse) throws Exception {
                        return Observable.create(
                                new ObservableOnSubscribe<T>() {
                                    @Override
                                    public void subscribe( @NonNull ObservableEmitter<T> e) throws Exception {
                                        if (!baseResponse.getCode().equals("0")) {
                                            String errorTips = baseResponse.getMsg();
                                            if (errorTips.contains("|")) {
                                                errorTips = errorTips.substring(errorTips.indexOf("|") + 1, errorTips.length());
                                            }
//                                            ToastUtil.displayShortToast(errorTips);
//                                            KLog.i("请求错误。。");
                                            QlcException qlcException = new QlcException(Integer.parseInt(baseResponse.getCode()), errorTips);
                                            e.onError(qlcException);
                                        } else {
                                            e.onNext(baseResponse);
                                        }
                                    }
                                });
                    }
                })
                /**
                 * 网络错误： You've encountered a network error!
                 请打开网络：Please open your network.
                 请求超时：The request has timed out.
                 连接失败: Connection failed.
                 请求失败： The request has failed.
                 */
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable e) throws Exception {
                        e.printStackTrace();
                        String errorText = "";
                        if (e instanceof HttpException) {
//                            errorText = "You've encountered a network error!";
                            return;
                        } else if (e instanceof UnknownHostException) {
//                            KLog.i("请打开网络");
                            errorText = "Please open your network.";
                        } else if (e instanceof SocketTimeoutException) {
//                            KLog.i("请求超时");
                            errorText = "The request has timed out. ";
                        } else if (e instanceof ConnectException) {
//                            KLog.i("连接失败");
                            errorText = "Connection failed.";
                        } else {
//                            KLog.i("请求失败");
                            errorText = "The request has failed.";
                        }
//                        ToastUtil.displayShortToast(errorText);
                    }
                });
    }

    //需要额外的添加其他的参数进去，所以把原有的参数和额外的参数通过这个方法一起添加进去.
    private static RequestBody addParams(Map<String, String> data) {
        Log.i("CHttpApiWrapper", "添加参数");
        Map<String, Object> map = new HashMap<>();
        map.put("appid", TodayStep.MainAppid);
        map.put("system", "Android" + SystemUtil.getSystemVersion() + " " + SystemUtil.getDeviceBrand() +SystemUtil.getSystemModel() + " version:20");
        map.put("timestamp", (Calendar.getInstance().getTimeInMillis() + new Random().nextInt(1000)) + "");
        map.put("params", JSONObject.toJSON(data));
        map.put("sign", DigestUtils.getSignature((JSONObject) JSONObject.toJSON(map), TodayStep.unKownKeyButImportant, "UTF-8"));
//        KLog.i("传的参数为:" + map);
        MediaType textType = MediaType.parse("text/plain");
        String bodyStr = JSONObject.toJSON(map).toString();
        try {
            bodyStr = URLEncoder.encode(bodyStr, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return RequestBody.create(textType, bodyStr);
    }

}
