package com.stratagile.qlink.ui.activity.eos.contract;

import com.stratagile.qlink.entity.EosResource;
import com.stratagile.qlink.entity.eos.EosResourcePrice;
import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;

import java.util.Map;

/**
 * @author hzp
 * @Package The contract for EosBuyRamActivity
 * @Description: $description
 * @date 2018/12/06 14:39:06
 */
public interface EosBuyRamContract {
    interface View extends BaseView<EosBuyRamContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();

        void setEosResource(EosResource eosResource);

        void setEosResourcePrice(EosResourcePrice eosResourcePrice);
    }

    interface EosBuyRamContractPresenter extends BasePresenter {
        //        /**
//         *
//         */
//        void getBusinessInfo(Map map);
        void getEosResource(Map map);

        void getEosResourcePrice(Map map);
    }
}