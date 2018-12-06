package com.stratagile.qlink.ui.activity.eos.contract;

import com.stratagile.qlink.entity.EosResource;
import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;

import java.util.Map;

/**
 * @author hzp
 * @Package The contract for EosBuyCpuAndNetActivity
 * @Description: $description
 * @date 2018/12/05 18:03:46
 */
public interface EosBuyCpuAndNetContract {
    interface View extends BaseView<EosBuyCpuAndNetContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();

        void setEosResource(EosResource eosResource);
    }

    interface EosBuyCpuAndNetContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);

        void getEosResource(Map map);
    }
}