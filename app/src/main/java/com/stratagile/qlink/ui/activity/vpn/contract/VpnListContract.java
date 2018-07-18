package com.stratagile.qlink.ui.activity.vpn.contract;

import com.stratagile.qlink.VpnProfile;
import com.stratagile.qlink.db.VpnEntity;
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

        void preConnect();

        void showNeedQlcDialog(VpnEntity vpnEntity);

        void refreshList();

        void onGetFreeNumBack(int num);
    }

    interface VpnListContractPresenter extends BasePresenter {
        void getVpn(Map map);

        void preConnectVpn(VpnEntity vpnEntity);

        void vpnProfileSendComplete();

        void getPasswordFromRemote(int type);

        void handleSendMessage(int message);

        void connectShareSuccess();

        void dialogConfirm();

        void zsFreeNum(Map map);

        void freeConnection(Map map);
    }
}