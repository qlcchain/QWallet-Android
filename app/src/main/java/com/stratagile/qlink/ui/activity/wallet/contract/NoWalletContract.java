package com.stratagile.qlink.ui.activity.wallet.contract;

import com.stratagile.qlink.entity.CreateWallet;
import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;

import java.util.Map;

/**
 * @author hzp
 * @Package The contract for NoWalletActivity
 * @Description: $description
 * @date 2018/01/23 13:54:18
 */
public interface NoWalletContract {
    interface View extends BaseView<NoWalletContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();

        /**
         * 创建钱包成功，修改当前选中的钱包
         * @param createWallet
         * @param flag
         */
        void onCreatWalletSuccess(CreateWallet createWallet, int flag);

        void getScanPermissionSuccess();

        /**
         * 创建钱包失败，回滚钱包
         */
        void createWalletFaliure();
    }

    interface NoWalletContractPresenter extends BasePresenter {
       void createWallet(Map map, String fromType);
        void importWallet(Map map, String fromType);

        void getSacnPermission();
    }
}