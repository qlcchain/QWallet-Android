package com.stratagile.qlink.ui.activity.main.contract;

import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.entity.AppVersion;
import com.stratagile.qlink.entity.ImportWalletResult;
import com.stratagile.qlink.entity.UpLoadAvatar;
import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author hzp
 * @Package The contract for MainActivity
 * @Description: $description
 * @date 2018/01/09 09:57:09
 */
public interface MainContract {
    interface View extends BaseView<MainContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();

//        void getPermissionSuccess();

        void onCreatWalletSuccess(ArrayList<Wallet> importWalletResult, int flag);

        void getAvatarSuccess(UpLoadAvatar upLoadAvatar);

        void onGetFreeNumBack(int num);

        void onGetShowActBack(int isShow);

        void setLastVersion(AppVersion appVersion);
    }

    interface MainContractPresenter extends BasePresenter {
        void latlngParseCountry(Map map);

        void getTox();

//        void getLocation();

        void importWallet(Map map);

        void heartBeat(Map map);

        void userAvatar(Map map);
    }
}