package com.stratagile.qlink.ui.activity.vpn.contract;

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
    }

    interface VpnListContractPresenter extends BasePresenter {
        void getVpn(Map map);
    }
}