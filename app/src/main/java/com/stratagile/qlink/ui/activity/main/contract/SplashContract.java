package com.stratagile.qlink.ui.activity.main.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author hzp
 * @Package The contract for SplashActivity
 * @Description: $description
 * @date 2018/01/09 11:24:32
 */
public interface SplashContract {
    interface View extends BaseView<SplashContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();
        /**
         * 登录成功，跳转到主页面
         */
        void loginSuccees();

        /**
         * 跳转到登录界面
         */
        void jumpToLogin();

        /**
         * 跳转到欢迎页面
         */
        void jumpToGuest();
    }

    interface SplashContractPresenter extends BasePresenter {
        /**
         * 判断接下来需要跳转到哪个界面去
         */
        void observeJump();

        /**
         * 申请需要的权限,如果权限不够,在某些手机上比如小米.会发生闪退现象
         */
        void getPermission();

        /**
         * 尝试自动登录
         */
        void doAutoLogin();

        /**
         * 获取最新的版本
         */
        void getLastVersion();

    }
}