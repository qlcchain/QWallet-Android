package com.stratagile.qlink.ui.activity.vpn.presenter;

import android.support.annotation.NonNull;

import com.socks.library.KLog;
import com.stratagile.qlink.api.HttpObserver;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.db.VpnEntity;
import com.stratagile.qlink.entity.ChainVpn;
import com.stratagile.qlink.qlinkcom;
import com.stratagile.qlink.ui.activity.vpn.VpnListFragment;
import com.stratagile.qlink.ui.activity.vpn.contract.VpnListContract;
import com.stratagile.qlink.utils.LogUtil;
import com.stratagile.qlink.utils.QlinkUtil;
import com.stratagile.qlink.utils.SpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.vpn
 * @Description: presenter of VpnListFragment
 * @date 2018/02/06 15:16:44
 */
public class VpnListPresenter implements VpnListContract.VpnListContractPresenter{

    HttpAPIWrapper httpAPIWrapper;
    private final VpnListContract.View mView;
    //    private CompositeDisposable mCompositeDisposable;
    private VpnListFragment mFragment;

    @Inject
    public VpnListPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull VpnListContract.View view, VpnListFragment fragment) {
        mView = view;
        this.httpAPIWrapper = httpAPIWrapper;
//        mCompositeDisposable = new CompositeDisposable();
        this.mFragment = fragment;
    }
    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
//        if (!mCompositeDisposable.isDisposed()) {
//             mCompositeDisposable.dispose();
//        }
    }

    @Override
    public void getVpn(Map map) {
        httpAPIWrapper.vpnQuery(map).subscribe(new HttpObserver<ChainVpn>() {
            @Override
            public void onNext(ChainVpn chainVpn) {
                KLog.i("onSuccesse");
                handleVpnList(chainVpn);
                onComplete();
            }
        });
//        Disposable disposable = httpAPIWrapper.vpnQuery(map)
//                .subscribe(new Consumer<ChainVpn>() {
//                    @Override
//                    public void accept(ChainVpn chainVpn) throws Exception {
//                        //isSuccesse
//                        KLog.i("onSuccesse");
//                        handleVpnList(chainVpn);
//                        //mView.closeProgressDialog();
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        //onError
//                        KLog.i("onError");
//                        throwable.printStackTrace();
//                        //mView.closeProgressDialog();
//                        //ToastUtil.show(mFragment.getActivity(), mFragment.getString(R.string.loading_failed_1));
//                    }
//                }, new Action() {
//                    @Override
//                    public void run() throws Exception {
//                        //onComplete
//                        KLog.i("onComplete");
//                    }
//                });
//        mCompositeDisposable.add(disposable);
    }

    private void handleVpnList(ChainVpn chainVpn) {
        ArrayList<ChainVpn.DataBean.VpnListBean> vpnListBeans = new ArrayList<>();
        for (ChainVpn.DataBean dataBean : chainVpn.getData()) {
            vpnListBeans.addAll(dataBean.getVpnList());
        }
        List<VpnEntity> vpnEntityList = AppConfig.getInstance().getDaoSession().getVpnEntityDao().loadAll();
        Iterator itTemp = vpnEntityList.iterator();        //根据传入的集合(旧集合)获取迭代器
        while (itTemp.hasNext()) {          //遍历老集合
            VpnEntity obj = (VpnEntity) itTemp.next();       //记录每一个元素
            boolean isHad = false;
            int j = vpnListBeans.size();
            for (int i = 0; i < j; i++) {
                ChainVpn.DataBean.VpnListBean objData = vpnListBeans.get(i);
                if(objData.getVpnName().equals(obj.getVpnName()))
                {
                    isHad = true;
                    break;
                }
            }
            String P2PID = SpUtil.getString(AppConfig.getInstance(), ConstantValue.P2PID, "");
            if(!"".equals(P2PID) && !isHad && !P2PID.equals(obj.getP2pId())&& !obj.isConnected())
            {
                AppConfig.getInstance().getDaoSession().getVpnEntityDao().delete(obj);
            }
        }
        vpnEntityList = AppConfig.getInstance().getDaoSession().getVpnEntityDao().loadAll();
        ArrayList<String> newList = new ArrayList();     //创建新集合
        Iterator it = vpnEntityList.iterator();        //根据传入的集合(旧集合)获取迭代器
        while (it.hasNext()) {          //遍历老集合
            VpnEntity obj = (VpnEntity) it.next();       //记录每一个元素
            if (!newList.contains(obj.getVpnName())) {      //如果新集合中不包含旧集合中的元素
                newList.add(obj.getVpnName());       //将元素添加
            }else{
                AppConfig.getInstance().getDaoSession().getVpnEntityDao().delete(obj);
            }
        }
        vpnEntityList = AppConfig.getInstance().getDaoSession().getVpnEntityDao().loadAll();
        List<VpnEntity> toAddList = new ArrayList<>();
        for (ChainVpn.DataBean.VpnListBean vpnListBean : vpnListBeans) {
            boolean isAdded = false;
//            LogUtil.addLog("服务器给的资产名为：" + vpnListBean.getVpnName() + "  服务器给的资产p2pid为：" + vpnListBean.getP2pId(), getClass().getSimpleName());
            for (VpnEntity vpnEntity : vpnEntityList) {
                if (vpnListBean.getVpnName().equals(vpnEntity.getVpnName())) {
                    vpnEntity.setAssetTranfer(vpnListBean.getQlc());
                    vpnEntity.setAddress(vpnListBean.getAddress());
                    vpnEntity.setP2pId(vpnListBean.getP2pId());
                    vpnEntity.setCountry(vpnListBean.getCountry());
                    if (vpnListBean.getConnectNum() != 0) {
                        vpnEntity.setQlc((float) vpnListBean.getCost());
                        vpnEntity.setConnectMaxnumber(vpnListBean.getConnectNum());
                        vpnEntity.setAvatar(vpnListBean.getImgUrl());
                        vpnEntity.setP2pId(vpnListBean.getP2pId());
                        vpnEntity.setRegisterQlc(vpnListBean.getRegisterQlc());
                        vpnEntity.setProfileLocalPath(vpnListBean.getProfileLocalPath());
                    }
                    AppConfig.getInstance().getDaoSession().getVpnEntityDao().update(vpnEntity);
                    isAdded = true;
                    break;
                }
            }
            if (!isAdded) {
                VpnEntity tempVpnEntity = new VpnEntity();
                tempVpnEntity.setP2pId(vpnListBean.getP2pId());
                tempVpnEntity.setAddress(vpnListBean.getAddress());
                tempVpnEntity.setCountry(vpnListBean.getCountry());
                tempVpnEntity.setAssetTranfer(vpnListBean.getQlc());
                tempVpnEntity.setType(vpnListBean.getType());
                tempVpnEntity.setFriendNum("");
                tempVpnEntity.setGroupNum(-1);
                tempVpnEntity.setQlc(0f);
                tempVpnEntity.setVpnName(vpnListBean.getVpnName());
                tempVpnEntity.setQlc((float) vpnListBean.getCost());
                tempVpnEntity.setConnectMaxnumber(vpnListBean.getConnectNum());
                tempVpnEntity.setAvatar(vpnListBean.getImgUrl());
                tempVpnEntity.setRegisterQlc(vpnListBean.getRegisterQlc());
                tempVpnEntity.setProfileLocalPath(vpnListBean.getProfileLocalPath());
                toAddList.add(tempVpnEntity);
            }
        }
        AppConfig.getInstance().getDaoSession().getVpnEntityDao().insertInTx(toAddList);
        vpnEntityList = AppConfig.getInstance().getDaoSession().getVpnEntityDao().loadAll();
        for (ChainVpn.DataBean.VpnListBean vpnListBean : vpnListBeans) {
            for (VpnEntity vpnEntity : vpnEntityList) {
                if (vpnEntity.getVpnName().equals(vpnListBean.getVpnName()) && "".equals(vpnEntity.getFriendNum())) {
                    //判断是否是好友，是好友就把friendNum添加到WiFientity中，不是好友就要添加好友，再添加。
                    int friendNum = qlinkcom.GetFriendNumInFriendlist(vpnListBean.getP2pId());
                    byte[] p2pId = new byte[100];
                    String friendNumStr = "";
                    qlinkcom.GetFriendP2PPublicKey(vpnListBean.getP2pId(), p2pId);
                    friendNumStr = new String(p2pId).trim();
                    KLog.i(friendNumStr);
                    if (friendNumStr == null) {
                        friendNumStr = "";
                    }
                    if (friendNum < 0 && !vpnEntity.getP2pId().equals(SpUtil.getString(AppConfig.getInstance(), ConstantValue.P2PID, ""))) {
                        //不是好友，添加
                        KLog.i(friendNum + "需要添加好友" + vpnEntity.getVpnName());
                        friendNum = qlinkcom.AddFriend(vpnListBean.getP2pId());
                        if (friendNum >= 0) {
                            KLog.i(friendNum + "添加后好友" + vpnEntity.getVpnName());
                            vpnEntity.setFriendNum(new String(p2pId).trim());
                        }
                    } else {
                        KLog.i(friendNum + "已经是好友" + vpnEntity.getVpnName());
                        vpnEntity.setFriendNum(friendNumStr);
                        try {
                            if (qlinkcom.GetFriendConnectionStatus(vpnEntity.getFriendNum()) > 0 && vpnEntity.getConnectMaxnumber() == 0) {
                                vpnEntity.setOnline(true);
                                if (vpnEntity.getProfileLocalPath() == null || "".equals(vpnEntity.getProfileLocalPath())) {
                                    Map<String, String> infoMap = new HashMap<>();
                                    infoMap.put("vpnName", vpnEntity.getVpnName());
                                    infoMap.put("p2pId", vpnEntity.getP2pId());
                                    LogUtil.addLog("要获取信息的资产信息为：" + vpnEntity.toString(), getClass().getSimpleName());
                                    QlinkUtil.parseMap2StringAndSend(vpnEntity.getFriendNum(), ConstantValue.vpnBasicInfoReq, infoMap);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (!vpnEntity.getFriendNum().equals("")) {
                        if (qlinkcom.GetFriendConnectionStatus(friendNumStr) > 0) {
                            vpnEntity.setOnline(true);
                        } else {
                            vpnEntity.setOnline(false);
                        }
                    }
                    String myP2pId = SpUtil.getString(AppConfig.getInstance(), ConstantValue.P2PID, "");
                    if (vpnEntity.getVpnName().equals(vpnListBean.getVpnName()) && myP2pId.equals(vpnEntity.getP2pId())) {
                        if (ConstantValue.myStatus > 0) {
                            vpnEntity.setOnline(true);
                        }
                    }
                    AppConfig.getInstance().getDaoSession().getVpnEntityDao().update(vpnEntity);
                    break;
                }
            }
        }

        preShowVpn(vpnListBeans);
    }

    private void preShowVpn(ArrayList<ChainVpn.DataBean.VpnListBean> vpnListBeans) {
        ArrayList<VpnEntity> showList = new ArrayList<>();
        List<VpnEntity> vpnEntityList = AppConfig.getInstance().getDaoSession().getVpnEntityDao().loadAll();
        for (ChainVpn.DataBean.VpnListBean vpnListBean : vpnListBeans) {
            for (VpnEntity vpnEntity : vpnEntityList) {
                if (vpnEntity.getVpnName().equals(vpnListBean.getVpnName())) {
                    showList.add(vpnEntity);
                    break;
                }
            }
        }

        mView.setVpnList(showList);
    }

}