package com.stratagile.qlink.ui.activity.wifi.presenter;
import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.db.WifiEntity;
import com.stratagile.qlink.entity.qlink.QlinkEntity;
import com.stratagile.qlink.qlinkcom;
import com.stratagile.qlink.ui.activity.wifi.contract.WifiListContract;
import com.stratagile.qlink.ui.activity.wifi.WifiListFragment;
import com.stratagile.qlink.utils.QlinkUtil;
import com.stratagile.qlink.utils.ToastUtil;

import javax.inject.Inject;
import io.reactivex.disposables.CompositeDisposable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wifi
 * @Description: presenter of WifiListFragment
 * @date 2018/01/09 14:02:09
 */
public class WifiListPresenter implements WifiListContract.WifiListContractPresenter{

    HttpAPIWrapper httpAPIWrapper;
    private final WifiListContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private WifiListFragment mFragment;

    @Inject
    public WifiListPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull WifiListContract.View view, WifiListFragment fragment) {
        mView = view;
        this.httpAPIWrapper = httpAPIWrapper;
        mCompositeDisposable = new CompositeDisposable();
        this.mFragment = fragment;
    }
    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        if (!mCompositeDisposable.isDisposed()) {
             mCompositeDisposable.dispose();
        }
    }

    @Override
    public void getWifiListFromServer(String wifiStatus, int requestPage, int onePageSize) {
        Map<String, String> map = new HashMap<>();
        map.put("pageSize", onePageSize + "");
        map.put("pageNum", requestPage + "");
    }

    @Override
    public void createLinkToWifi(WifiEntity wifiEntity) {
        int friendNumber = qlinkcom.GetFriendNumInFriendlist(wifiEntity.getOwnerP2PId());
        if (friendNumber >= 0) {
            KLog.i("该好友在我的列表中的第:" + friendNumber);
        } else {
            int addFriendReslut = qlinkcom.AddFriend(wifiEntity.getOwnerP2PId());
            KLog.i("需要添加的好友的p2pId为:" + wifiEntity.getOwnerP2PId());
            KLog.i("添加好友的结果为:" + addFriendReslut);
            friendNumber = qlinkcom.GetFriendNumInFriendlist(wifiEntity.getOwnerP2PId());
        }
        if (qlinkcom.GetFriendConnectionStatus(wifiEntity.getFreindNum()) > 0) {
            KLog.i("好友编号为:" + friendNumber);
            Map<String, String> infoMap = new HashMap<>();
            infoMap.put("ssid", wifiEntity.getSsid());
            infoMap.put("mac", wifiEntity.getMacAdrees());
            QlinkUtil.parseMap2StringAndSend(wifiEntity.getFreindNum(), "wifibasicinfoReq", infoMap);
        } else {
            ToastUtil.displayShortToast(AppConfig.getInstance().getResources().getString(R.string.firend_is_not_online));
        }
    }

    //{"Type":"wifibasicinfoReq","Data": "{ "SSID": "YYM-5", "MAC":"00:0c:29:86:d9:94"}"}
    private String constractRequestWifiBaseInfoStr(WifiEntity wifiEntity) {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("type", "wifibasicinfoReq");
        Map<String, String> infoMap = new HashMap<>();
        infoMap.put("ssid", wifiEntity.getSsid());
        infoMap.put("mac", wifiEntity.getMacAdrees());
        requestMap.put("data", JSONObject.toJSON(infoMap).toString());
        return JSONObject.toJSON(requestMap).toString();
    }

    @Override
    public void handlerQlinkData(QlinkEntity qlinkEntity) {

    }

}