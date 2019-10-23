package com.stratagile.qlink.ui.activity.finance.contract;

import com.stratagile.qlink.entity.InviteList;
import com.stratagile.qlink.entity.reward.Dict;
import com.stratagile.qlink.entity.reward.InviteTotal;
import com.stratagile.qlink.entity.reward.RewardTotal;
import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;

import java.util.Map;

/**
 * @author hzp
 * @Package The contract for InviteActivity
 * @Description: $description
 * @date 2019/04/23 15:34:34
 */
public interface InviteContract {
    interface View extends BaseView<InviteContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();

        void setData(InviteList inviteList);

        void setCanClaimTotal(InviteTotal rewardTotal);

        void setClaimedTotal(InviteTotal rewardTotal);

        void setAtlistInviteFriend(Dict dict);

        void setOneFriendReward(Dict dict);


    }

    interface InviteContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);

        void getInivteRank(Map map);
    }
}