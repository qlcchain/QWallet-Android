package com.stratagile.qlink.ui.activity.vpn.contract;

import com.stratagile.qlink.VpnProfile;
import com.stratagile.qlink.db.VpnEntity;
import com.stratagile.qlink.entity.Balance;
import com.stratagile.qlink.entity.ChainVpn;
import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author hzp
 * @Package The contract for VpnListFragment
 * @Description: $description
 * @date 2018/02/06 15:16:44
 */
public interface VpnListContract {
    interface View extends BaseView<VpnListContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();

        void setVpnList(ArrayList<VpnEntity> vpnList);

        void startOrStopVPN(VpnProfile vpnProfile);

        void preConnect(int version);

        void showNeedQlcDialog(VpnEntity vpnEntity);

        void refreshList();

        void onGetFreeNumBack(int num);

        void onGetBalancelSuccess(Balance balance);
    }

    interface VpnListContractPresenter extends BasePresenter {
        void getVpn(Map map);

        void preConnectVpn(VpnEntity vpnEntity);

        void vpnProfileSendComplete(String content);

        void getPasswordFromRemote(int type);

        void handleSendMessage(int message);

        void connectShareSuccess(int version);

        void dialogConfirm();

        void zsFreeNum(Map map);

        void freeConnection(Map map);

        void getWalletBalance(Map map);

        void reportVpnInfo(Map map);
    }
}