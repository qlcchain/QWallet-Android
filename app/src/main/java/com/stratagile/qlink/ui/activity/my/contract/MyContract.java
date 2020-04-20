package com.stratagile.qlink.ui.activity.my.contract;

import com.stratagile.qlink.entity.RedPoint;
import com.stratagile.qlink.entity.UserInfo;
import com.stratagile.qlink.entity.VcodeLogin;
import com.stratagile.qlink.entity.reward.ClaimQgas;
import com.stratagile.qlink.entity.reward.InviteTotal;
import com.stratagile.qlink.entity.reward.RewardTotal;
import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author hzp
 * @Package The contract for MyFragment
 * @Description: $description
 * @date 2019/04/09 10:02:03
 */
public interface MyContract {
    interface View extends BaseView<MyContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();

        void setUsrInfo(UserInfo vcodeLogin);

        void setCanClaimTotal(RewardTotal rewardTotal);

        void setCanInviteClaimTotal(InviteTotal rewardTotal);

        void bindPushBack();

        void setMiningRewardCount(RewardTotal claimQgas);

        void setRedPoint(RedPoint redPoint);
    }

    interface MyContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);
    }
}