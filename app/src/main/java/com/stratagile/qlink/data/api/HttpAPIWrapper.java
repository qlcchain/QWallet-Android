package com.stratagile.qlink.data.api;

import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.constant.MainConstant;
import com.stratagile.qlink.entity.AssetsWarpper;
import com.stratagile.qlink.entity.Balance;
import com.stratagile.qlink.entity.BaseBack;
import com.stratagile.qlink.entity.BetResult;
import com.stratagile.qlink.entity.BuyQlc;
import com.stratagile.qlink.entity.ChainVpn;
import com.stratagile.qlink.entity.ConnectedWifiRecord;
import com.stratagile.qlink.entity.CreateWallet;
import com.stratagile.qlink.entity.EthWalletDetail;
import com.stratagile.qlink.entity.FreeNum;
import com.stratagile.qlink.entity.FreeRecord;
import com.stratagile.qlink.entity.GoogleResult;
import com.stratagile.qlink.entity.ImportWalletResult;
import com.stratagile.qlink.entity.MainAddress;
import com.stratagile.qlink.entity.RaceTimes;
import com.stratagile.qlink.entity.Raw;
import com.stratagile.qlink.entity.Record;
import com.stratagile.qlink.entity.RecordVpn;
import com.stratagile.qlink.entity.RegisterVpn;
import com.stratagile.qlink.entity.RegisterWiFi;
import com.stratagile.qlink.entity.Reward;
import com.stratagile.qlink.entity.ServerTime;
import com.stratagile.qlink.entity.TransactionResult;
import com.stratagile.qlink.entity.UpLoadAvatar;
import com.stratagile.qlink.entity.UpdateVpn;
import com.stratagile.qlink.entity.VertifyVpn;
import com.stratagile.qlink.entity.WifiRegisteResult;
import com.stratagile.qlink.utils.DigestUtils;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.ToastUtil;

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
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.HttpException;

/**
 * @author hu
 * @desc 对Request实体(不执行)在执行时所调度的线程，以及得到ResponseBody后根据retCode对Result进行进一步处理
 * @date 2017/5/31 16:56
 */
public class HttpAPIWrapper {

    private HttpApi mHttpAPI;
    private MainHttpApi mMainHttpAPI;

    @Inject
    public HttpAPIWrapper(HttpApi mHttpAPI, MainHttpApi mMainHttpAPI) {
        this.mHttpAPI = mHttpAPI;
        this.mMainHttpAPI = mMainHttpAPI;
    }

    public Observable<WifiRegisteResult> getRegistedSsid(Map map) {
        if (SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false)) {
            return wrapper(mMainHttpAPI.getRegistedSsid(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getRegistedSsid(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<RegisterWiFi> registeWWifi(Map map) {
        if (SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false)) {
            return wrapper(mMainHttpAPI.registeWWifi(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.registeWWifi(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<RegisterWiFi> registeWWifiV3(Map map) {
        if (SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false)) {
            return wrapper(mMainHttpAPI.registeWWifiV3(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.registeWWifiV3(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<BaseBack> createWallet(Map map) {
        if (SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false)) {
            return wrapper(mMainHttpAPI.createWallet(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.createWallet(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<CreateWallet> importWallet(Map map) {
        if (SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false)) {
            return wrapper(mMainHttpAPI.importWallet(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.importWallet(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<Balance> getBalance(Map map) {
        if (SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false)) {
            return wrapper(mMainHttpAPI.getBalance(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getBalance(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<Reward> reward(Map map) {
        if (SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false)) {
            return wrapper(mMainHttpAPI.reward(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.reward(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<Raw> getRaw(Map map) {
        if (SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false)) {
            return wrapper(mMainHttpAPI.getRaw(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getRaw(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<Record> recordQuerys(Map map) {
        if (SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false)) {
            return wrapper(mMainHttpAPI.recordQuerys(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.recordQuerys(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<ConnectedWifiRecord> recordSave(Map map) {
        if (SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false)) {
            return wrapper(mMainHttpAPI.recordSave(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.recordSave(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<BuyQlc> buyQlc(Map map) {
        if (SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false)) {
            return wrapper(mMainHttpAPI.buyQlc(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.buyQlc(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<BuyQlc> trasaction(Map map) {
        if (SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false)) {
            return wrapper(mMainHttpAPI.trasaction(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.trasaction(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<RegisterWiFi> vpnRegister(Map map) {
        if (SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false)) {
            return wrapper(mMainHttpAPI.vpnRegister(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.vpnRegister(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<RegisterVpn> vpnRegisterV2(Map map) {
        if (SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false)) {
            return wrapper(mMainHttpAPI.vpnRegisterV2(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.vpnRegisterV2(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<ChainVpn> vpnQuery(Map map) {
        if (SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false)) {
            return wrapper(mMainHttpAPI.vpnQuery(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.vpnQuery(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }
    public Observable<ChainVpn> vpnQueryV3(Map map) {
        if (SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false)) {
            return wrapper(mMainHttpAPI.vpnQueryV3(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.vpnQueryV3(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<VertifyVpn> vertifyVpnName(Map map) {
        if (SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false)) {
            return wrapper(mMainHttpAPI.vertifyVpnName(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.vertifyVpnName(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<RecordVpn> vpnRecordSave(Map map) {
        if (SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false)) {
            return wrapper(mMainHttpAPI.vpnRecordSave(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.vpnRecordSave(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<String> getNodes() {
        if (SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false)) {
            return wrapperGetToxJson(mMainHttpAPI.getNodes()).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapperGetToxJson(mHttpAPI.getNodes()).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<GoogleResult> latlngParseCountry(Map map) {
        if (SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false)) {
            return wrapperObject(mMainHttpAPI.latlngParseCountry(map)).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapperObject(mHttpAPI.latlngParseCountry(map)).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<ImportWalletResult> batchImportWallet(Map map) {
        if (SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false)) {
            return wrapper(mMainHttpAPI.batchImportWallet(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.batchImportWallet(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<UpLoadAvatar> updateMyAvatar(MultipartBody.Part photo, RequestBody p2pId) {     //String userId, String nickName   userId, nickName
        if (SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false)) {
            return wrapper(mMainHttpAPI.updateMyAvatar(p2pId, photo)).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.updateMyAvatar(p2pId, photo)).compose(SCHEDULERS_TRANSFORMER);
        }

    }
    public Observable<UpLoadAvatar> userHeadView(Map map) {
        if (SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false)) {
            return wrapper(mMainHttpAPI.userHeadView(map)).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.userHeadView(map)).compose(SCHEDULERS_TRANSFORMER);
        }

    }
    public Observable<BaseBack> heartBeat(Map map) {
        if (SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false)) {
            return wrapper(mMainHttpAPI.heartBeat(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.heartBeat(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<UpdateVpn> updateVpnInfo(Map map) {
        if (SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false)) {
            return wrapper(mMainHttpAPI.updateVpnInfo(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.updateVpnInfo(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<BaseBack> updateWiFiInfo(Map map) {
        if (SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false)) {
            return wrapper(mMainHttpAPI.updateWiFiInfo(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.updateWiFiInfo(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }


    public Observable<AssetsWarpper> getUnspentAsset(Map map) {
        if (SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false)) {
            return wrapper(mMainHttpAPI.getUnspentAsset(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getUnspentAsset(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<BaseBack> sendRawTransaction(Map map) {
        if (SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false)) {
            return wrapper(mMainHttpAPI.sendRawTransaction(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.sendRawTransaction(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<TransactionResult> v2Transaction(Map map) {
        if (SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false)) {
            return wrapper(mMainHttpAPI.v2Transaction(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.v2Transaction(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }
    public Observable<MainAddress> getMainAddress(Map map) {
        if (SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false)) {
            return wrapper(mMainHttpAPI.getMainAddress(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getMainAddress(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }
    public Observable<BuyQlc> bnb2qlc(Map map) {
        if (SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false)) {
            return wrapper(mMainHttpAPI.bnb2qlc(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.bnb2qlc(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<EthWalletDetail> getEthAddressDetail(String address, Map map) {
        if (SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false)) {
            return wrapperETH(mMainHttpAPI.getEthAddressDetail(address, map)).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapperETH(mHttpAPI.getEthAddressDetail(address, map)).compose(SCHEDULERS_TRANSFORMER);
        }

    }
//    public Observable<RaceTimes> getRaceTimes(Map map) {
//        if (SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false)) {
//            return wrapper(mMainHttpAPI.getRaceTimes(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
//        } else {
//            return wrapper(mHttpAPI.getRaceTimes(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
//        }
//
//    }
//
//    public Observable<ServerTime> getServerTime(Map map) {
//        if (SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false)) {
//            return wrapper(mMainHttpAPI.getServerTime(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
//        } else {
//            return wrapper(mHttpAPI.getServerTime(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
//        }
//
//    }
//    public Observable<BetResult> betRace(Map map) {
//        if (SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false)) {
//            return wrapper(mMainHttpAPI.betRace(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
//        } else {
//
//            return wrapper(mHttpAPI.betRace(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
//        }
//    }

    public Observable<FreeNum> zsFreeNum(Map map) {
        if (SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false)) {
            return wrapper(mMainHttpAPI.zsFreeNum(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.zsFreeNum(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<FreeNum> freeConnection(Map map) {
        if (SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false)) {
            return wrapper(mMainHttpAPI.freeConnection(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.freeConnection(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<FreeRecord> queryFreeRecords(Map map) {
        if (SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false)) {
            return wrapper(mMainHttpAPI.queryFreeRecords(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.queryFreeRecords(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }
    public Observable<BaseBack> reportVpnInfo(Map map) {
        if (SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false)) {
            return wrapper(mMainHttpAPI.reportVpnInfo(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.reportVpnInfo(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
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
                                            String errorTips = baseResponse.getMsg();
                                            if (errorTips.contains("|")) {
                                                errorTips = errorTips.substring(errorTips.indexOf("|") + 1, errorTips.length());
                                            }
                                            ToastUtil.displayShortToast(errorTips);
                                            e.onComplete();
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
                            KLog.i("请打开网络");
                            errorText = "Please open your network.";
                        } else if (e instanceof SocketTimeoutException) {
                            KLog.i("请求超时");
                            errorText = "The request has timed out. ";
                        } else if (e instanceof ConnectException) {
                            KLog.i("连接失败");
                            errorText = "Connection failed.";
                        } else {
                            KLog.i("请求失败");
                            errorText = "The request has failed.";
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
    private <T extends GoogleResult> Observable<T> wrapperObject(Observable<T> resourceObservable) {
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
    private <T extends EthWalletDetail> Observable<T> wrapperETH(Observable<T> resourceObservable) {
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
    private <T extends String> Observable<T> wrapperGetToxJson(Observable<T> resourceObservable) {
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
                    }
                });
    }
    //需要额外的添加其他的参数进去，所以把原有的参数和额外的参数通过这个方法一起添加进去.
    public static RequestBody addParams(Map<String, String> data) {
        Map<String, Object> map = new HashMap<>();
        if (SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false)) {
            map.put("appid", MainConstant.MainAppid);
            map.put("timestamp", Calendar.getInstance().getTimeInMillis() + "");
            map.put("params", JSONObject.toJSON(data));
            map.put("sign", DigestUtils.getSignature((JSONObject) JSONObject.toJSON(map), MainConstant.MainSign, "UTF-8"));
        } else {
            map.put("appid", "MIFI");
            map.put("timestamp", Calendar.getInstance().getTimeInMillis() + "");
            map.put("params", JSONObject.toJSON(data));
            map.put("sign", DigestUtils.getSignature((JSONObject) JSONObject.toJSON(map), MainConstant.unKownKeyButImportant, "UTF-8"));
        }
        KLog.i("传的参数为:" + map);
        MediaType textType = MediaType.parse("text/plain");
        String bodyStr = JSONObject.toJSON(map).toString();
        KLog.i("加密前的:" + bodyStr);
        try {
            bodyStr = URLEncoder.encode(bodyStr, "UTF-8");
            KLog.i("加密后的:" + bodyStr);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return RequestBody.create(textType, bodyStr);
    }

    public static RequestBody createBody(String jsonBody) {
        MediaType textType = MediaType.parse("text/plain");
        return RequestBody.create(textType, jsonBody);
    }
}
