package com.stratagile.qlink.ui.activity.wallet.contract;

import com.stratagile.qlink.entity.UpLoadAvatar;
import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author hzp
 * @Package The contract for ProfilePictureActivity
 * @Description: $description
 * @date 2018/01/30 15:17:54
 */
public interface ProfilePictureContract {
    interface View extends BaseView<ProfilePictureContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();

        void updateImgSuccess(UpLoadAvatar upLoadAvatar);
    }

    interface ProfilePictureContractPresenter extends BasePresenter {
        void upLoadImg();
        void upLoadImgPc(String p2pIdPc);
    }
}