package com.stratagile.qlink.ui.activity.vpn.contract;

import com.stratagile.qlink.db.VpnEntity;
import com.stratagile.qlink.entity.Balance;
import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;

import java.util.Map;

/**
 * @author hzp
 * @Package The contract for ConnectVpnActivity
 * @Description: $description
 * @date 2018/02/08 16:38:02
 */
public interface ConnectVpnContract {
    interface View extends BaseView<ConnectVpnContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();

        void onRecordSuccess(boolean upUI);

        void onGetBalancelSuccess(Balance balance);
    }

    interface ConnectVpnContractPresenter extends BasePresenter {
        /**
         * 记录vpn的连接，
         */
        void connectToVpnRecord(VpnEntity vpnEntity,boolean upUI);

        void getBalance(Map map);
    }
}