package com.stratagile.qlink.ui.activity.wordcup.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author hzp
 * @Package The contract for WordCupIntroduceActivity
 * @Description: $description
 * @date 2018/06/11 13:50:44
 */
public interface WordCupIntroduceContract {
    interface View extends BaseView<WordCupIntroduceContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();
    }

    interface WordCupIntroduceContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);
    }
}