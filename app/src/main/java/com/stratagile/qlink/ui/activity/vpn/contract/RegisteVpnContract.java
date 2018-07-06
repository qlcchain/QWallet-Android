package com.stratagile.qlink.ui.activity.vpn.contract;

import com.stratagile.qlink.entity.Balance;
import com.stratagile.qlink.entity.VertifyVpn;
import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;

import java.util.Map;

/**
 * @author hzp
 * @Package The contract for RegisteVpnActivity
 * @Description: $description
 * @date 2018/02/06 15:41:02
 */
public interface RegisteVpnContract {
    interface View extends BaseView<RegisteVpnContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();

        void vertifyVpnBack(VertifyVpn vertifyVpn);

        void registVpnSuccess();
        void onGetBalancelSuccess(Balance balance);

        void updateVpnInfoSuccess();
    }

    interface RegisteVpnContractPresenter extends BasePresenter {
        void registerVpn(Map map, String vpnName);

        void vertifyVpnName(Map map);

        void getBalance(Map map);

        void updateVpnInfo(Map map);
    }
}