package com.stratagile.qlink.ui.activity.vpn.contract;

import com.stratagile.qlink.db.VpnEntity;
import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author zl
 * @Package The contract for RegisteWindowVpnActivityActivity
 * @Description: $description
 * @date 2018/08/03 11:56:07
 */
public interface RegisteWindowVpnActivityContract {
    interface View extends BaseView<RegisteWindowVpnActivityContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();

        void getScanPermissionSuccess();
    }

    interface RegisteWindowVpnActivityContractPresenter extends BasePresenter {
        //        /**
//         *
//         */
        void getScanPermission();

        void preAddVpn(VpnEntity vpnEntity);

        void upLoadImg(String p2pIdPc);

    }
}