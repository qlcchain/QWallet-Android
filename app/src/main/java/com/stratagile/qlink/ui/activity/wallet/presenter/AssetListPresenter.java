package com.stratagile.qlink.ui.activity.wallet.presenter;

import android.support.annotation.NonNull;

import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.db.VpnEntity;
import com.stratagile.qlink.db.VpnEntityDao;
import com.stratagile.qlink.db.WifiEntity;
import com.stratagile.qlink.db.WifiEntityDao;
import com.stratagile.qlink.entity.ChainVpn;
import com.stratagile.qlink.entity.WifiRegisteResult;
import com.stratagile.qlink.qlinkcom;
import com.stratagile.qlink.ui.activity.wallet.contract.AssetListContract;
import com.stratagile.qlink.ui.activity.wallet.AssetListFragment;
import com.stratagile.qlink.utils.LogUtil;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.ToastUtil;
import com.stratagile.qlink.utils.VpnUtil;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: presenter of AssetListFragment
 * @date 2018/01/18 20:42:28
 */
public class AssetListPresenter implements AssetListContract.AssetListContractPresenter {

    HttpAPIWrapper httpAPIWrapper;
    private final AssetListContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private AssetListFragment mFragment;

    @Inject
    public AssetListPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull AssetListContract.View view, AssetListFragment fragment) {
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
    public void getAssetListFromServer(String assetStatus, int requestPage, int onePageSize) {
        Map<String, String> map = new HashMap<>();
        map.put("pageSize", onePageSize + "");
        map.put("pageNum", requestPage + "");
    }

    @Override
    public void getAssetInfoFromServer(Map map) {
        Disposable disposable = httpAPIWrapper.getRegistedSsid(map)
                .subscribe(new Consumer<WifiRegisteResult>() {
                    @Override
                    public void accept(WifiRegisteResult result) throws Exception {
                        //isSuccesse
                        KLog.i("onSuccesse");
                        handleAssetFromServerBack(result);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //onError
                        KLog.i("onError");
                        throwable.printStackTrace();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //onComplete
                        KLog.i("onComplete");
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    /**
     * 处理我的资产信息，将数据库中保存的资产的ssid与服务端的比对，如果服务端的删除了
     * 就将数据库中的删除。
     */
    private void handleAssetFromServerBack(WifiRegisteResult wifiRegisteResult) {
        String delete = "";
        String update = "";
        String insert = "";
        LogUtil.addLog("server Assets count：" + wifiRegisteResult.getData().size(), getClass().getSimpleName());
        for (WifiRegisteResult.DataBean dataBean : wifiRegisteResult.getData()) {
            List<VpnEntity> vpnEntityList = AppConfig.getInstance().getDaoSession().getVpnEntityDao().loadAll();
            List<VpnEntity> vpnEntityListInSql = new ArrayList<>();
            if ("3".equals(dataBean.getType())) {
                vpnEntityListInSql = AppConfig.getInstance().getDaoSession().getVpnEntityDao().queryBuilder().where(VpnEntityDao.Properties.VpnName.eq(dataBean.getSsId())).list();
            }

            if (vpnEntityListInSql.size() > 0 || "".equals(dataBean.getType())) {
                for (VpnEntity vpnEntity : vpnEntityList) {
                    //3代表的是vpn资产
                    if (dataBean.getSsId().equals(vpnEntity.getVpnName()) && dataBean.getP2pId().equals("")) {
                        //ToastUtil.displayShortToast(AppConfig.getInstance().getResources().getString(R.string.delete_asset));
                        delete += vpnEntity.getVpnName()+",";
                        AppConfig.getInstance().getDaoSession().getVpnEntityDao().delete(vpnEntity);
                        break;
                    } else if (VpnUtil.isInSameNet(vpnEntity) && dataBean.getSsId().equals(vpnEntity.getVpnName()) && !dataBean.getP2pId().equals("")) {
                        vpnEntity.setP2pId(dataBean.getP2pId());
                        vpnEntity.setAddress(dataBean.getAddress());
                        vpnEntity.setRegisterQlc(dataBean.getRegisterQlc());
//                        vpnEntity.setQlc((float) dataBean.getQlc());
                        vpnEntity.setAssetTranfer(dataBean.getQlc());
                        update +=  vpnEntity.getVpnName()+",";
                        AppConfig.getInstance().getDaoSession().getVpnEntityDao().update(vpnEntity);
                    }
                }
            } else {
                if (dataBean.getP2pId() != null && !("".equals(dataBean.getP2pId())) && "3".equals(dataBean.getType())) {
                    VpnEntity vpnEntity = new VpnEntity();
                    vpnEntity.setP2pId(dataBean.getP2pId());
                    vpnEntity.setVpnName(dataBean.getSsId());
                    vpnEntity.setAddress(dataBean.getAddress());
                    vpnEntity.setRegisterQlc(dataBean.getRegisterQlc());
                    vpnEntity.setIsMainNet(SpUtil.getBoolean(AppConfig.getInstance(),ConstantValue.isMainNet,false));
//                    vpnEntity.setQlc((float) dataBean.getQlc());
                    vpnEntity.setAssetTranfer(dataBean.getQlc());
                    insert += vpnEntity.getVpnName()+",";
                    AppConfig.getInstance().getDaoSession().getVpnEntityDao().insert(vpnEntity);
                }

            }

        }
        LogUtil.addLog("delete Assets：" + delete, getClass().getSimpleName());
        LogUtil.addLog("update Assets：" + update, getClass().getSimpleName());
        LogUtil.addLog("insert Assets：" + insert, getClass().getSimpleName());
        for (WifiRegisteResult.DataBean dataBean : wifiRegisteResult.getData()) {
            List<WifiEntity> wifiEntityList = AppConfig.getInstance().getDaoSession().getWifiEntityDao().loadAll();
            List<WifiEntity> wifiEntityListInSql = new ArrayList<>();
            if ("0".equals(dataBean.getType())) {
                wifiEntityListInSql = AppConfig.getInstance().getDaoSession().getWifiEntityDao().queryBuilder().where(WifiEntityDao.Properties.Ssid.eq(dataBean.getSsId())).list();
            }
            if (wifiEntityListInSql.size() > 0 || "".equals(dataBean.getType())) {
                for (WifiEntity wifiEntity : wifiEntityList) {
                    //0代表的是wifi资产
                    if (dataBean.getSsId().equals(wifiEntity.getSsid()) && dataBean.getP2pId().equals("")) {
                        AppConfig.getInstance().getDaoSession().getWifiEntityDao().delete(wifiEntity);
                        break;
                    } else if (dataBean.getSsId().equals(wifiEntity.getSsid()) && !dataBean.getP2pId().equals("")) {
                        wifiEntity.setOwnerP2PId(dataBean.getP2pId());
                        wifiEntity.setWalletAddress(dataBean.getAddress());
                        wifiEntity.setPriceInQlc((float) dataBean.getQlc());
                        wifiEntity.setMacAdrees(dataBean.getMac());
                        AppConfig.getInstance().getDaoSession().getWifiEntityDao().update(wifiEntity);
                    }
                }
            } else {
                if (dataBean.getP2pId() != null && !("".equals(dataBean.getP2pId())) && "0".equals(dataBean.getType())) {
                    WifiEntity wifiEntity = new WifiEntity();
                    wifiEntity.setOwnerP2PId(dataBean.getP2pId());
                    wifiEntity.setSsid(dataBean.getSsId());
                    wifiEntity.setWalletAddress(dataBean.getAddress());
                    wifiEntity.setPriceInQlc((float) dataBean.getQlc());
                    wifiEntity.setMacAdrees(dataBean.getMac());
                    AppConfig.getInstance().getDaoSession().getWifiEntityDao().insert(wifiEntity);
                }

            }

        }
        mView.getAssetSuccess();
    }
}