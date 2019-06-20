package com.stratagile.qlink.ui.activity.otc.contract;

import com.stratagile.qlink.entity.LocalTokenBean;
import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;

import java.util.Map;

/**
 * @author hzp
 * @Package The contract for AddTokenActivity
 * @Description: $description
 * @date 2018/11/28 11:03:57
 */
public interface AddTokenContract {
    interface View extends BaseView<AddTokenContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();

        void getBinaTokensSuccess(LocalTokenBean localTokenBean);
    }

    interface AddTokenContractPresenter extends BasePresenter {
        /**
         *
         */
        void getBinaTokens(Map map);
    }
}