package com.stratagile.qlink.ui.activity.vpn.presenter;

import android.support.annotation.NonNull;

import com.socks.library.KLog;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.db.TransactionRecord;
import com.stratagile.qlink.db.VpnEntity;
import com.stratagile.qlink.entity.Balance;
import com.stratagile.qlink.entity.RegisterVpn;
import com.stratagile.qlink.entity.UpdateVpn;
import com.stratagile.qlink.entity.VertifyVpn;
import com.stratagile.qlink.ui.activity.vpn.RegisteVpnActivity;
import com.stratagile.qlink.ui.activity.vpn.contract.RegisteVpnContract;
import com.stratagile.qlink.utils.SpUtil;

import java.util.Calendar;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.vpn
 * @Description: presenter of RegisteVpnActivity
 * @date 2018/02/06 15:41:02
 */
public class RegisteVpnPresenter implements RegisteVpnContract.RegisteVpnContractPresenter{

    HttpAPIWrapper httpAPIWrapper;
    private final RegisteVpnContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private RegisteVpnActivity mActivity;

    @Inject
    public RegisteVpnPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull RegisteVpnContract.View view, RegisteVpnActivity activity) {
        mView = view;
        this.httpAPIWrapper = httpAPIWrapper;
        mCompositeDisposable = new CompositeDisposable();
        this.mActivity = activity;
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
    public void registerVpn(Map map, String vpnName, VpnEntity vpnEntity) {

//        mView.showProgressDialog();
        Disposable disposable = httpAPIWrapper.vpnRegisterV2(map)
                .subscribe(new Consumer<RegisterVpn>() {
                    @Override
                    public void accept(RegisterVpn result) throws Exception {
                        //isSuccesse
                        KLog.i("onSuccesse");
                        mView.closeProgressDialog();
                        mView.registVpnSuccess();
                        TransactionRecord recordSave = new TransactionRecord();
                        recordSave.setTxid(result.getRecordId());
                        recordSave.setExChangeId(result.getRecordId());
                        recordSave.setAssetName(vpnName);
                        recordSave.setTransactiomType(5);
                        recordSave.setTimestamp(Calendar.getInstance().getTimeInMillis());
                        recordSave.setIsMainNet(SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false));
                        AppConfig.getInstance().getDaoSession().getTransactionRecordDao().insert(recordSave);
                        vpnEntity.setVpnName(result.getVpnName());
                        vpnEntity.setCountry(result.getCountry());
                        vpnEntity.setIsConnected(false);
                        vpnEntity.setP2pId(result.getP2pId());
                        vpnEntity.setProfileLocalPath(result.getProfileLocalPath());
                        if(ConstantValue.windowsVpnName.equals(vpnName))
                        {
                            vpnEntity.setAvatar(SpUtil.getString(AppConfig.getInstance(), ConstantValue.myAvatarPath, ""));
                            ConstantValue.windowsVpnName ="";
                        }else{
                            vpnEntity.setAvatar(result.getImgUrl());
                        }
                        vpnEntity.setAddress(result.getAddress());
                        vpnEntity.setIsInMainWallet(SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false));
                        vpnEntity.setOnline(true);
                        vpnEntity.setIsMainNet(SpUtil.getBoolean(AppConfig.getInstance(),ConstantValue.isMainNet,false));
                        AppConfig.getInstance().getDaoSession().getVpnEntityDao().insert(vpnEntity);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //onError
                        KLog.i("onError");
                        throwable.printStackTrace();
                        mView.closeProgressDialog();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //onComplete
                        KLog.i("onComplete");
                        mView.closeProgressDialog();
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void vertifyVpnName(Map map) {
        Disposable disposable = httpAPIWrapper.vertifyVpnName(map)
                .subscribe(new Consumer<VertifyVpn>() {
                    @Override
                    public void accept(VertifyVpn vertifyVpn) throws Exception {
                        //isSuccesse
                        KLog.i("onSuccesse");
                        mView.vertifyVpnBack(vertifyVpn);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //onError
                        KLog.i("onError");
                        throwable.printStackTrace();
                        mView.closeProgressDialog();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //onComplete
                        KLog.i("onComplete");
                        mView.closeProgressDialog();
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void getBalance(Map map) {
        Disposable disposable = httpAPIWrapper.getBalance(map)
                .subscribe(new Consumer<Balance>() {
                    @Override
                    public void accept(Balance balance) throws Exception {
                        //isSuccesse
                        KLog.i("onSuccesse");
                        mView.onGetBalancelSuccess(balance);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //onError
                        KLog.i("onError");
                        throwable.printStackTrace();
                        //mView.closeProgressDialog();
                        //ToastUtil.show(mFragment.getActivity(), mFragment.getString(R.string.loading_failed_1));
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

    @Override
    public void updateVpnInfo(Map map) {
        mView.showProgressDialog();
        Disposable disposable = httpAPIWrapper.updateVpnInfo(map)
                .subscribe(new Consumer<UpdateVpn>() {
                    @Override
                    public void accept(UpdateVpn updateVpn) throws Exception {
                        //isSuccesse
                        KLog.i("onSuccesse");
                        mView.updateVpnInfoSuccess(updateVpn.getData().getCountry());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //onError
                        KLog.i("onError");
                        throwable.printStackTrace();
                        mView.closeProgressDialog();
                        //ToastUtil.show(mFragment.getActivity(), mFragment.getString(R.string.loading_failed_1));
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //onComplete
                        mView.closeProgressDialog();
                        KLog.i("onComplete");
                    }
                });
        mCompositeDisposable.add(disposable);
    }
}