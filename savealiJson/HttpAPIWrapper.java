package com.stratagile.qlink.data.api;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.ninjia.ninjiabaselibrary.utils.VersionUtil;
import com.socks.library.KLog;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.constant.ContainValue;
import com.stratagile.qlink.entity.Balance;
import com.stratagile.qlink.entity.BaseBack;
import com.stratagile.qlink.entity.CreateWallet;
import com.stratagile.qlink.entity.WifiRegisteResult;
import com.stratagile.qlink.utils.DigestUtils;
import com.stratagile.qlink.utils.SignUtil;
import com.stratagile.qlink.utils.ToastUtil;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

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

/**
 * @author hu
 * @desc 对Request实体(不执行)在执行时所调度的线程，以及得到ResponseBody后根据retCode对Result进行进一步处理
 * @date 2017/5/31 16:56
 */
public class HttpAPIWrapper {

    private HttpApi mHttpAPI;

    @Inject
    public HttpAPIWrapper(HttpApi mHttpAPI) {
        this.mHttpAPI = mHttpAPI;
    }

    public Observable<WifiRegisteResult> getRegistedSsid(Map map) {
        return wrapper(mHttpAPI.getRegistedSsid(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
    }

    public Observable<BaseBack> registeWWifi(Map map) {
        return wrapper(mHttpAPI.registeWWifi(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
    }

    public Observable<CreateWallet> createWallet(Map map) {
        return wrapper(mHttpAPI.createWallet(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
    }

    public Observable<Balance> getBalance(Map map) {
        return wrapper(mHttpAPI.getBalance(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
    }
    public Observable<BaseBack> recordQuerys(Map map) {
        return wrapper(mHttpAPI.recordQuerys(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
    }

    public Observable<BaseBack> recordSave(Map map) {
        return wrapper(mHttpAPI.recordSave(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
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
                    public ObservableSource<? extends T> apply(@NonNull T baseResponse) throws Exception {
                        return Observable.create(
                                new ObservableOnSubscribe<T>() {
                                    @Override
                                    public void subscribe(@NonNull ObservableEmitter<T> e) throws Exception {
                                        if (!baseResponse.getCode().equals("0")) {
                                            ToastUtil.displayShortToast(baseResponse.getMsg());
                                            e.onComplete();
                                        } else {
                                            e.onNext(baseResponse);
                                        }
                                    }
                                });
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable e) throws Exception {
                        e.printStackTrace();
                        String errorText = "";
                        if (e instanceof HttpException) {
                            HttpException exception = (HttpException) e;
                            errorText = "网络错误";
                            return;
                        } else if (e instanceof UnknownHostException) {
                            KLog.i("请打开网络");
                            errorText = "请打开网络";
                        } else if (e instanceof SocketTimeoutException) {
                            KLog.i("请求超时");
                            errorText = "请求超时";
                        } else if (e instanceof ConnectException) {
                            KLog.i("连接失败");
                            errorText = "连接失败";
                        } else if (e instanceof HttpException) {
                            KLog.i("请求超时");
                            errorText = "请求超时";
                        } else {
                            KLog.i("请求失败");
                            errorText = "请求失败";
                        }
                        ToastUtil.displayShortToast(errorText);
                    }
                });
    }

    /**
     * 根据Http的response中关于状态码的描述，自定义生成本地的Exception
     *
     * @param resourceObservable
     * @param <T>
     * @return
     */
    private <T extends Object> Observable<T> wrapperObject(Observable<T> resourceObservable) {
        return resourceObservable
                .flatMap(new Function<T, ObservableSource<? extends T>>() {
                    @Override
                    public ObservableSource<? extends T> apply(@NonNull T baseResponse) throws Exception {
                        return Observable.create(
                                new ObservableOnSubscribe<T>() {
                                    @Override
                                    public void subscribe(@NonNull ObservableEmitter<T> e) throws Exception {
                                        if (baseResponse == null) {

                                        } else {
                                            //// TODO: 2017/6/8 没有做错误处理，因为现在后台返回的结果格式都不一样，等后台统一了返回再做处理
                                            e.onNext(baseResponse);
                                        }
                                    }
                                });
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable e) throws Exception {
                        e.printStackTrace();
                        String errorText = "";
                        if (e instanceof HttpException) {
                            HttpException exception = (HttpException) e;
                        } else if (e instanceof UnknownHostException) {
                            KLog.i("请打开网络");
                            errorText = "请打开网络";
                            return;
                        } else if (e instanceof SocketTimeoutException) {
                            KLog.i("请求超时");
                            errorText = "请求超时";
                        } else if (e instanceof ConnectException) {
                            KLog.i("连接失败");
                            errorText = "连接失败";
                        } else if (e instanceof HttpException) {
                            KLog.i("请求超时");
                            errorText = "请求超时";
                        } else {
                            KLog.i("请求失败");
                            errorText = "请求失败";
                        }
                        ToastUtil.displayShortToast(errorText);
                    }
                });
    }

    /**
     * 给任何Http的Observable加上在Service中运行的线程调度器
     */
    public static <T> ObservableTransformer<T, T> getSchedulerstransFormerToService() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    //需要额外的添加其他的参数进去，所以把原有的参数和额外的参数通过这个方法一起添加进去.
    public static RequestBody addParams(Map<String, String> data) {
        Map<String, Object> map = new HashMap<>();
        map.put("appid", "MIFI");
        map.put("timestamp", Calendar.getInstance().getTimeInMillis() + "");
        map.put("params", JSONObject.fromObject(data));
        map.put("sign", DigestUtils.getSignature(JSONObject.fromObject(map), ContainValue.unKownKeyButImportant, "UTF-8"));
        KLog.i("传的参数为:" + map);
        MediaType textType = MediaType.parse("text/plain");
        String bodyStr = JSONObject.fromObject(map).toString();
        KLog.i("加密前的:" + bodyStr);
        try {
            bodyStr = URLEncoder.encode(bodyStr, "UTF-8");
            KLog.i("加密后的:" + bodyStr);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return RequestBody.create(textType, bodyStr);
    }
}
